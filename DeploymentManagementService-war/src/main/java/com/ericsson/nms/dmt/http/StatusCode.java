/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.dmt.http;

/**
 * Commonly used status codes defined by HTTP
 */
public enum StatusCode {

	ACCEPTED(202), BAD_REQUEST(400), CONFLICT(409), CREATED(201), FORBIDDEN(403), GONE(
			410), INTERNAL_SERVER_ERROR(500), MOVED_PERMANENTLY(301), NO_CONTENT(
			204), NOT_ACCEPTABLE(406), NOT_FOUND(404), METHOD_NOT_ALLOWED(405), NOT_MODIFIED(
			304), OK(200), PRECONDITION_FAILED(412), SEE_OTHER(303), SERVICE_UNAVAILABLE(
			503), TEMPORARY_REDIRECT(307), UNAUTHORIZED(401), UNPROCESSABLE(422), UNSUPPORTED_MEDIA_TYPE(
			415);

	private int code;

	private StatusCode(int code) {
		this.code = code;
	}

	/**
	 * Get the numeric code for the current {@link StatusCode}
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Indicates when a status code means error
	 * 
	 * @return
	 */
	public boolean isError() {
		return code >= 400 && code <= 599;
	}

	/**
	 * Factory method that creates a {@link StatusCode} from a {@link String}
	 * 
	 * @param code
	 *            as string
	 * @return
	 */
	public static StatusCode from(String code) {
		return from(Integer.valueOf(code));
	}

	/**
	 * Factory method that creates a {@link StatusCode} from a {@link Integer}
	 * 
	 * @param code
	 *            as integer
	 * @return
	 */
	public static StatusCode from(int code) {
		for (StatusCode sc : values()) {
			if (sc.getCode() == code) {
				return sc;
			}
		}
		throw new IllegalArgumentException("Invalid status code " + code);
	}
}
