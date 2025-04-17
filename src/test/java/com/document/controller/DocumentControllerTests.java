package com.document.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.document.request.dto.DocumentListRequestDto;
import com.document.request.dto.DocumentUploadRequestDto;
import com.document.response.dto.DocumentListResponseDto;
import com.document.response.dto.ResponseMessageDto;
import com.document.service.DocumentService;
import com.document.validator.CommonValidator;

class DocumentControllerTest {

	@Mock
	private DocumentService documentService;

	@Mock
	private CommonValidator commonValidator;

	@InjectMocks
	private DocumentController documentController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUploadDocumentSuccess() throws Exception {
		DocumentUploadRequestDto requestDto = new DocumentUploadRequestDto();
		doNothing().when(commonValidator).validateDocument(requestDto);
		doNothing().when(documentService).uploadDocument(requestDto);

		ResponseEntity<?> response = documentController.documentUpload(requestDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(((ResponseMessageDto) response.getBody()).getMessage().contains("success"));
	}

	@Test
	void testDownloadDocumentSuccess() throws Exception {
		Integer docId = 1;
		Resource mockResource = new ByteArrayResource("test data".getBytes()) {
			@Override
			public String getFilename() {
				return "test.txt";
			}
		};

		doNothing().when(commonValidator).validateDowloadDocument(docId);
		when(documentService.downloadDocument(docId)).thenReturn(mockResource);

		ResponseEntity<?> response = documentController.documentDownload(docId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("application/octet-stream", response.getHeaders().getContentType().toString());
	}

	@Test
	void testGetDocumentListSuccessList() throws Exception {
		DocumentListRequestDto requestDto = new DocumentListRequestDto();
		DocumentListResponseDto responseDto = new DocumentListResponseDto();
		Page<DocumentListResponseDto> page = new PageImpl<>(List.of(responseDto));

		when(documentService.getDocumentList(requestDto)).thenReturn(page);

		ResponseEntity<?> response = documentController.getDocumentList(requestDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(((ResponseMessageDto) response.getBody()).getData() instanceof Page);
	}

	@Test
	void testGetDocumentListNoRecords() throws Exception {
		DocumentListRequestDto requestDto = new DocumentListRequestDto();
		Page<DocumentListResponseDto> page = new PageImpl<>(List.of());

		when(documentService.getDocumentList(requestDto)).thenReturn(page);

		ResponseEntity<?> response = documentController.getDocumentList(requestDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(((ResponseMessageDto) response.getBody()).getMessage().contains("No record"));
	}
}
