package com.biddingmate.biddinggo.file.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.config.R2Properties;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlRequest;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

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
                "items/%d/%02d/%02d/%s.%s",
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

    private String buildPublicUrl(String fileKey) {
        String publicBaseUrl = r2Properties.getPublicBaseUrl();

        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            throw new CustomException(ErrorType.R2_PRESIGNED_URL_GENERATION_FAILED);
        }

        return publicBaseUrl.endsWith("/")
                ? publicBaseUrl + fileKey
                : publicBaseUrl + "/" + fileKey;
    }
}
