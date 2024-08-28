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

import com.ericsson.nms.dmt.error.BusinessException;

/**
 * Base class for exceptions related to property validation
 */
public abstract class AbstractPropertyException extends BusinessException {

	private static final long serialVersionUID = 3038681849375257017L;

	private String propertyName;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public AbstractPropertyException(String message) {
		super(message);
	}

	/**
	 * Constructor that takes a custom error message and the name of the
	 * property that the error refers to
	 * 
	 * @param message
	 * @param propertyName
	 */
	public AbstractPropertyException(String message, String propertyName) {
		super(message);
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}
