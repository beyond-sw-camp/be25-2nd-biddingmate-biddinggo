package com.biddingmate.biddinggo.file.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlRequest;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlResponse;
import com.biddingmate.biddinggo.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 업로드 API")
public class FileController {
    private final FileService fileService;

    @PostMapping("/presigned-upload")
    @Operation(summary = "R2 presigned upload URL 발급", description = "이미지를 R2에 직접 업로드할 수 있는 presigned PUT URL을 발급합니다.")
    public ResponseEntity<ApiResponse<CreatePresignedUploadUrlResponse>> createPresignedUploadUrl(
            @RequestBody CreatePresignedUploadUrlRequest request) {

        CreatePresignedUploadUrlResponse result = fileService.createPresignedUploadUrl(request);

        return ApiResponse.of(HttpStatus.OK, null, "Presigned upload URL 발급 완료", result);
    }
}
