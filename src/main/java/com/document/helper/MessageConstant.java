package com.document.helper;

public enum MessageConstant {

	SUCCESS(1000, "SUCCESS!", "Document has been uploaded successfully."), SUCCESS_LIST(1000, "SUCCESS!"),
	FAILED(1001, "Failed", "Something went wrong. Please try after sometime"),
	BAD_REQUEST(1002, "BAD REQUEST", "The request is invalid or malformed"),
	NOT_FOUND(1003, "NOT FOUND", "The requested resource was not found"),
	DOC_MISSING(1006, "FILE MISSING", "Required document part is missing in the request"),
	INVALID_DOC_TYPE(1007, "INVALID DOCUMENT TYPE", "Uploaded document type is not supported"),
	INTERNAL_SERVER_ERROR(1008, "INTERNAL SERVER ERROR", "An unexpected error occurred on the server"),
	DOC_READ_ERROR(1009, "DOCUMENT DOWNLOAD ERROR", "Unable to download the document because id is null or 0"),
	FILTER_RESULT_NOT_FOUND(1010, "FILTER RESULT NOT FOUND", "No documents found matching the filter criteria"),
	DOC_SIZE_EXCEEDED(1011, "DOCUMENT SIZE EXCEEDED", "Maximum upload size exceeded"),
	RECORD_NOT_FOUND(1003, "RECORD NOT FOUND", "No Record Found."),;

	private final int code;
	private final String status;
	private  String message;

	MessageConstant(int code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	MessageConstant(int code, String status) {
		this.code = code;
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
