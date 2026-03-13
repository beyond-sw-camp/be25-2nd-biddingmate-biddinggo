package com.biddingmate.biddinggo.file.service;

import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlRequest;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlResponse;

import java.util.List;

public interface FileService {
    CreatePresignedUploadUrlResponse createPresignedUploadUrl(CreatePresignedUploadUrlRequest request);

    String buildPublicUrl(String fileKey);

    boolean isManagedFileKey(String fileKey);

    void deleteFiles(List<String> fileKeys);
}
