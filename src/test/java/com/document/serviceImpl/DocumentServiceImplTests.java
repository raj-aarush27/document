package com.document.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.document.entity.DocumentManagementEO;
import com.document.repository.DocumentManagementRepository;
import com.document.repository.DocumentManagementRepositoryImpl;
import com.document.request.dto.DocumentListRequestDto;
import com.document.request.dto.DocumentUploadRequestDto;
import com.document.response.dto.DocumentListResponseDto;

@ExtendWith(SpringExtension.class)
class DocumentServiceImplTest {

	@InjectMocks
	private DocumentServiceImpl documentService;

	@Mock
	private DocumentManagementRepository documentManagementRepository;

	@Mock
	private DocumentManagementRepositoryImpl documentManagementRepositoryImpl;

	@BeforeEach
	void setUp() {
		documentService = new DocumentServiceImpl();
		documentService.docLocation = "src/test/resources/";
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUploadDocumentNewDocument() throws IOException {
		DocumentUploadRequestDto reqDto = new DocumentUploadRequestDto();
		reqDto.setUserId(1);
		reqDto.setAuthor("Test Author");
		MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain",
				"Sample content".getBytes());
		reqDto.setFile(mockFile);

		when(documentManagementRepository.save(any(DocumentManagementEO.class))).thenAnswer(i -> i.getArgument(0));

		String result = documentService.uploadDocument(reqDto);

		assertEquals("Success", result);
	}

	@Test
	void testDownloadDocumentValidId() throws MalformedURLException {
		DocumentManagementEO doc = new DocumentManagementEO();
		doc.setDocName("test.txt");

		when(documentManagementRepository.findById(1)).thenReturn(Optional.of(doc));

		Resource resource = documentService.downloadDocument(1);

		assertEquals(UrlResource.class, resource.getClass());
	}

	@Test
	void testGetDocumentList() {
		DocumentListRequestDto reqDto = new DocumentListRequestDto();
		Page<DocumentListResponseDto> mockPage = new PageImpl<>(List.of());

		when(documentManagementRepositoryImpl.getDocumentList(reqDto)).thenReturn(mockPage);

		Page<DocumentListResponseDto> result = documentService.getDocumentList(reqDto);

		assertEquals(mockPage, result);
	}
}
