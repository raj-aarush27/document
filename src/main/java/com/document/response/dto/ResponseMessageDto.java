package com.document.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ResponseMessageDto {
	private int code;
	private String status;
	private String message;
	private Object data;

	public ResponseMessageDto() {
	}

	public ResponseMessageDto(String message) {
		this.message = message;
	}

	public ResponseMessageDto(int code, String status, String message, Object data) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ResponseMessageDto(int code) {
		this.code = code;
	}

	public ResponseMessageDto(int code, String status, Object data) {
		this.code = code;
		this.status = status;
		this.data = data;
	}

	public ResponseMessageDto(int code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
