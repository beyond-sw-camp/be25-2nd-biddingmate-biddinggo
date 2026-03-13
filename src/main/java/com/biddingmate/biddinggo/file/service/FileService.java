package com.biddingmate.biddinggo.file.service;

import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlRequest;
import com.biddingmate.biddinggo.file.dto.CreatePresignedUploadUrlResponse;

public interface FileService {
    CreatePresignedUploadUrlResponse createPresignedUploadUrl(CreatePresignedUploadUrlRequest request);
}
