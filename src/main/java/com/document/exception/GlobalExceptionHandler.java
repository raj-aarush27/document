package com.document.exception;

import static org.springframework.http.ResponseEntity.ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.document.helper.MessageConstant;
import com.document.response.dto.ResponseMessageDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex,
			HttpServletRequest request) {
		logger.warn("Missing parameter '{}' in request: {}", ex.getParameterName(), request.getRequestURI());
		return buildResponse(MessageConstant.BAD_REQUEST);
	}

	@ExceptionHandler(DocumentManagementException.class)
	public ResponseEntity<?> handleDocumentException(Exception ex, HttpServletRequest request) {
		logger.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
		return buildResponseForDocument(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
		logger.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
		return buildResponseForDocument(ex.getMessage());
	}

	private ResponseEntity<?> buildResponse(MessageConstant msgCons) {
		ResponseMessageDto response = new ResponseMessageDto(msgCons.getCode(), msgCons.getStatus(),
				msgCons.getMessage());
		return ok(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
	}

	private ResponseEntity<?> buildResponseForDocument(String msg) {
		MessageConstant msgCons = getMsgObjectFromMsg(msg);
		ResponseMessageDto response = new ResponseMessageDto(msgCons.getCode(), msgCons.getStatus(),
				msgCons.getMessage());
		return ok(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
	}

	public static MessageConstant getMsgObjectFromMsg(String message) {
		for (MessageConstant msgConst : MessageConstant.values()) {
			if (msgConst.getMessage() != null && msgConst.getMessage().equalsIgnoreCase(message)) {
				return msgConst;
			}
		}
		return null;
	}
}
