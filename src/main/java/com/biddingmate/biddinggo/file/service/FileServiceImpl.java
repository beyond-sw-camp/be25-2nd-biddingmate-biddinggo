package com.biddingmate.biddinggo.file.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.config.R2Properties;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlRequest;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlResponse;
import com.biddingmate.biddinggo.file.dto.DeleteFileRequest;
import com.biddingmate.biddinggo.file.dto.DeleteFileResponse;
import com.biddingmate.biddinggo.file.model.FileMetadata;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final String TEMP_FILE_KEY_PREFIX = "temp/items/";
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final R2Properties r2Properties;

    @Override
    public CreatePresignedUploadUrlResponse createPresignedUploadUrl(CreatePresignedUploadUrlRequest request) {
        validateRequest(request);

        String fileKey = generateFileKey(request.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(r2Properties.getBucket())
                    .key(fileKey)
                    .contentType(request.getContentType())
                    .build();

            Duration signatureDuration = Duration.ofMinutes(r2Properties.getPresignedUrlDurationMinutes());

            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(signatureDuration)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

            return CreatePresignedUploadUrlResponse.builder()
                    .uploadUrl(presignedRequest.url().toString())
                    .fileKey(fileKey)
                    .publicUrl(buildPublicUrl(fileKey))
                    .method("PUT")
                    .expiresInSeconds(signatureDuration.toSeconds())
                    .build();
        } catch (Exception exception) {
            throw new CustomException(ErrorType.R2_PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    @Override
    public DeleteFileResponse deleteFile(DeleteFileRequest request) {
        if (request == null || request.getFileKey() == null || request.getFileKey().isBlank()) {
            throw new CustomException(ErrorType.INVALID_FILE_UPLOAD_REQUEST);
        }

        deleteFile(request.getFileKey(), false);

        return DeleteFileResponse.builder()
                .fileKey(request.getFileKey())
                .build();
    }

    @Override
    public String buildPublicUrl(String fileKey) {
        validateFileKey(fileKey);

        String publicBaseUrl = r2Properties.getPublicBaseUrl();

        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            throw new CustomException(ErrorType.R2_PRESIGNED_URL_GENERATION_FAILED);
        }

        return publicBaseUrl.endsWith("/")
                ? publicBaseUrl + fileKey
                : publicBaseUrl + "/" + fileKey;
    }

    @Override
    public boolean isManagedFileKey(String fileKey) {
        return fileKey != null
                && !fileKey.isBlank()
                && fileKey.startsWith(TEMP_FILE_KEY_PREFIX)
                && fileKey.length() > TEMP_FILE_KEY_PREFIX.length();
    }

    @Override
    public FileMetadata getFileMetadata(String fileKey) {
        validateFileKey(fileKey);

        try {
            HeadObjectResponse response = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(r2Properties.getBucket())
                    .key(fileKey)
                    .build());

            return extractFileMetadata(response);
        } catch (S3Exception exception) {
            if (exception.statusCode() == 404) {
                throw new CustomException(ErrorType.UPLOADED_FILE_NOT_FOUND);
            }

            log.error("R2 파일 존재 여부 조회 실패 - fileKey: {}", fileKey, exception);
            throw new CustomException(ErrorType.FILE_LOOKUP_FAILED);
        } catch (Exception exception) {
            log.error("R2 파일 존재 여부 조회 실패 - fileKey: {}", fileKey, exception);
            throw new CustomException(ErrorType.FILE_LOOKUP_FAILED);
        }
    }

    @Override
    public void deleteFiles(List<String> fileKeys) {
        if (fileKeys == null || fileKeys.isEmpty()) {
            return;
        }

        for (String fileKey : fileKeys) {
            deleteFile(fileKey, true);
        }
    }

    private void validateRequest(CreatePresignedUploadUrlRequest request) {
        if (request == null
                || request.getOriginalFilename() == null || request.getOriginalFilename().isBlank()
                || request.getContentType() == null || request.getContentType().isBlank()
                || !ALLOWED_CONTENT_TYPES.contains(request.getContentType())) {
            throw new CustomException(ErrorType.INVALID_FILE_UPLOAD_REQUEST);
        }
    }

    private String generateFileKey(String originalFilename) {
        String extension = extractExtension(originalFilename);
        LocalDate today = LocalDate.now();

        return String.format(
                TEMP_FILE_KEY_PREFIX + "%d/%02d/%02d/%s.%s",
                today.getYear(),
                today.getMonthValue(),
                today.getDayOfMonth(),
                UUID.randomUUID(),
                extension
        );
    }

    private String extractExtension(String originalFilename) {
        int extensionIndex = originalFilename.lastIndexOf('.');

        if (extensionIndex < 0 || extensionIndex == originalFilename.length() - 1) {
            throw new CustomException(ErrorType.INVALID_FILE_UPLOAD_REQUEST);
        }

        return originalFilename.substring(extensionIndex + 1).toLowerCase();
    }

    private void validateFileKey(String fileKey) {
        if (!isManagedFileKey(fileKey)) {
            throw new CustomException(ErrorType.INVALID_FILE_UPLOAD_REQUEST);
        }
    }

    private FileMetadata extractFileMetadata(HeadObjectResponse response) {
        String contentType = response.contentType();
        Long contentLength = response.contentLength();

        if (contentType == null
                || contentType.isBlank()
                || !ALLOWED_CONTENT_TYPES.contains(contentType)
                || contentLength == null
                || contentLength <= 0
                || contentLength > Integer.MAX_VALUE) {
            throw new CustomException(ErrorType.INVALID_UPLOADED_FILE_METADATA);
        }

        return FileMetadata.builder()
                .contentType(contentType)
                .size(contentLength.intValue())
                .build();
    }

    private void deleteFile(String fileKey, boolean ignoreFailure) {
        if (!isManagedFileKey(fileKey)) {
            if (ignoreFailure) {
                return;
            }
            throw new CustomException(ErrorType.INVALID_FILE_UPLOAD_REQUEST);
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(r2Properties.getBucket())
                    .key(fileKey)
                    .build());
        } catch (Exception exception) {
            if (!ignoreFailure) {
                throw new CustomException(ErrorType.FILE_DELETE_FAILED);
            }

            log.warn("R2 파일 삭제 정리 실패 - fileKey: {}", fileKey, exception);
        }
    }
}
