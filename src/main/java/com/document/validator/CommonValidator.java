package com.document.validator;

import org.springframework.stereotype.Component;

import com.document.exception.DocumentManagementException;
import com.document.helper.MessageConstant;
import com.document.request.dto.DocumentUploadRequestDto;

@Component
public class CommonValidator {

	public void validateDocument(DocumentUploadRequestDto reqDto) {
		if (reqDto.getFile().getSize() > 10 * 1024 * 1024)
			throw new DocumentManagementException(MessageConstant.DOC_SIZE_EXCEEDED);
		else if (reqDto.getFile() == null || reqDto.getFile().isEmpty())
			throw new DocumentManagementException(MessageConstant.DOC_MISSING);
		else if (!reqDto.getFile().getContentType().equalsIgnoreCase("application/pdf")
				&& !reqDto.getFile().getContentType().equalsIgnoreCase("image/png")
				&& !reqDto.getFile().getContentType().equalsIgnoreCase("image/jpeg")
				&& !reqDto.getFile().getContentType().equalsIgnoreCase("application/xlsx")
				&& !reqDto.getFile().getContentType().equalsIgnoreCase("application/docs"))
			throw new DocumentManagementException(MessageConstant.INVALID_DOC_TYPE);

	}

	public void validateDowloadDocument(Integer docId) {
		if (docId == null || docId == 0)
			throw new DocumentManagementException(MessageConstant.DOC_READ_ERROR);
	}
}
