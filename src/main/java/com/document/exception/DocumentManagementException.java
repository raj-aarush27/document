package com.document.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.document.helper.MessageConstant;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DocumentManagementException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final MessageConstant messageConstant;

	public DocumentManagementException(MessageConstant messageConstant) {
		super(messageConstant.getMessage());
		this.messageConstant = messageConstant;
	}

	public MessageConstant getMessageConstant() {
		return messageConstant;
	}

}
