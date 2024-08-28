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
package com.ericsson.nms.dmt.error;

import static com.ericsson.nms.dmt.http.StatusCode.NOT_FOUND;

/**
 * Business exception that can be raised by DMT when it receives a request to be
 * performed on an existing object/resource.
 */
@CausesStatusCode(NOT_FOUND)
@CustomErrorType("InvalidLocationError")
public class InvalidLocationException extends BusinessException {

	private static final long serialVersionUID = -8837670835119731954L;

	/**
	 * Constructor that takes an error message
	 * 
	 * @param message
	 */
	public InvalidLocationException(String message) {
		super(message);
	}
}
