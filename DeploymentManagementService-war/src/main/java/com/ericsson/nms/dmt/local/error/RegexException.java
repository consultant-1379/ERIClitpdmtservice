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
 * This exception is thrown when a node property contains a value that is not
 * valid for a regular expression constraint associated to it
 */
@CausesStatusCode(UNPROCESSABLE)
@CustomErrorType("RegexError")
public class RegexException extends AbstractPropertyException {

	private static final long serialVersionUID = 5994970043483030615L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public RegexException(String message, String propertyName) {
		super(message, propertyName);
	}
}
