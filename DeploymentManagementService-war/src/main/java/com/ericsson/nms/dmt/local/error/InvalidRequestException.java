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
package com.ericsson.nms.dmt.local.error;

import static com.ericsson.nms.dmt.http.StatusCode.UNPROCESSABLE;

import com.ericsson.nms.dmt.error.*;

/**
 * This exception is thrown when a request does not provide all the information
 * necessary to perform the requested operation
 */
@CausesStatusCode(UNPROCESSABLE)
@CustomErrorType("InvalidRequestError")
public class InvalidRequestException extends BusinessException {

	private static final long serialVersionUID = 3038681849375257017L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public InvalidRequestException(String message) {
		super(message);
	}
}
