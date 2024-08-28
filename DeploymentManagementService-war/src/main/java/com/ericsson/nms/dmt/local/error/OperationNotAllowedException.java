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

import static com.ericsson.nms.dmt.http.StatusCode.METHOD_NOT_ALLOWED;

import com.ericsson.nms.dmt.error.*;

/**
 * This exception is thrown when the requested operation cannot be performed on
 * the targeted node.
 */
@CausesStatusCode(METHOD_NOT_ALLOWED)
@CustomErrorType("MethodNotAllowedError")
public class OperationNotAllowedException extends BusinessException {

	private static final long serialVersionUID = -8837670835119731954L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public OperationNotAllowedException(String message) {
		super(message);
	}
}
