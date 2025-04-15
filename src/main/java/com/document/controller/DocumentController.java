package com.document.controller;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.document.helper.MessageConstant;
import com.document.request.dto.DocumentListRequestDto;
import com.document.request.dto.DocumentUploadRequestDto;
import com.document.response.dto.DocumentListResponseDto;
import com.document.response.dto.ResponseMessageDto;
import com.document.service.DocumentService;
import com.document.validator.CommonValidator;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * @author aarush.mishra
 *
 */
@RestController
@RequestMapping(value = "/api/v1/document")
public class DocumentController {

	@Autowired
	private CommonValidator commonValidator;

	@Autowired
	private DocumentService documentService;

	@Operation(description = "This api purpose is to upload the valid document")
	@PostMapping(value = "/upload-document")
	public ResponseEntity<?> documentUpload(@ModelAttribute DocumentUploadRequestDto reqDto) throws Exception {
		commonValidator.validateDocument(reqDto);
		documentService.uploadDocument(reqDto);
			return ok(new ResponseMessageDto(MessageConstant.SUCCESS.getCode(), MessageConstant.SUCCESS.getStatus(),
					MessageConstant.SUCCESS.getMessage()));
	}

	@Operation(description = "This api purpose is to download the document")
	@GetMapping(value = "/download-document", produces =  MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> documentDownload(@RequestParam ("docId") Integer docId) throws Exception {
		commonValidator.validateDowloadDocument(docId);
		Resource response = documentService.downloadDocument(docId);
		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + response.getFilename() + "\"";
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(response);
	}

	@Operation(description = "This api purpose is to provide the list of document")
	@PostMapping(value = "/document-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDocumentList(@RequestBody @Valid DocumentListRequestDto reqDto) throws Exception{
		Page<DocumentListResponseDto> list = documentService.getDocumentList(reqDto);
		if (list.getSize() > 0)
			return ok(new ResponseMessageDto(MessageConstant.SUCCESS_LIST.getCode(),
					MessageConstant.SUCCESS_LIST.getStatus(), list));
		else
			return ok(new ResponseMessageDto(MessageConstant.RECORD_NOT_FOUND.getCode(),
					MessageConstant.RECORD_NOT_FOUND.getStatus(), MessageConstant.RECORD_NOT_FOUND.getMessage()));
	}

}
