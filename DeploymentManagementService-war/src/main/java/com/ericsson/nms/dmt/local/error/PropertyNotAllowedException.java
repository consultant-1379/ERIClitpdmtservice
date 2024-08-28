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
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;

/**
 * This exception is thrown when a node is created or updated with properties
 * that are not defined in the corresponding node {@link ComplexSchemaElement}
 */
@CausesStatusCode(UNPROCESSABLE)
@CustomErrorType("PropertyNotAllowedError")
public class PropertyNotAllowedException extends AbstractPropertyException {

	private static final long serialVersionUID = 4833034431484243224L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public PropertyNotAllowedException(String message) {
		super(message);
	}

	/**
	 * Constructor that takes a custom error message and the name of the
	 * property the error refers to
	 * 
	 * @param message
	 * @param propertyName
	 */
	public PropertyNotAllowedException(String message, String propertyName) {
		super(message, propertyName);
	}
}
