package com.document.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import com.document.request.dto.DocumentListRequestDto;
import com.document.request.dto.DocumentUploadRequestDto;
import com.document.response.dto.DocumentListResponseDto;

public interface DocumentService {

	public String uploadDocument(DocumentUploadRequestDto reqDto);

	public Resource downloadDocument(Integer docId);

	public Page<DocumentListResponseDto> getDocumentList(DocumentListRequestDto reqDto);

}
