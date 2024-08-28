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

import com.ericsson.nms.dmt.error.CausesStatusCode;
import com.ericsson.nms.dmt.error.CustomErrorType;

/**
 * This exception is thrown when a node is created or updated but some required
 * properties are not provided
 */
@CausesStatusCode(UNPROCESSABLE)
@CustomErrorType("MissingRequiredPropertyError")
public class MissingRequiredPropertyException extends AbstractPropertyException {

	private static final long serialVersionUID = -2202849199812976528L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public MissingRequiredPropertyException(String message, String propertyName) {
		super(message, propertyName);
	}
}
