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

import static com.ericsson.nms.dmt.http.StatusCode.INTERNAL_SERVER_ERROR;

import com.ericsson.nms.dmt.error.CausesStatusCode;
import com.ericsson.nms.dmt.error.UnexpectedException;

/**
 * This exception is thrown when the deployment model serialization and
 * deserialization process fails for any unexpected reason.
 */
@CausesStatusCode(INTERNAL_SERVER_ERROR)
public class ModelParsingException extends UnexpectedException {

	private static final long serialVersionUID = 5270881352516942160L;

	/**
	 * Constructor that takes a custom error message and the cause exception
	 * 
	 * @param message
	 *            - error message
	 * @param t
	 *            - cause exception
	 */
	public ModelParsingException(String message, Throwable t) {
		super(message, t);
	}
}
