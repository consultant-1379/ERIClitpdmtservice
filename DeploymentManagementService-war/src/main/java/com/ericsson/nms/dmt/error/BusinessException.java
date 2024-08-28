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

/**
 * Base class for business validation exceptions.
 */
public abstract class BusinessException extends DmtBaseException {

	private static final long serialVersionUID = -5802883538035068944L;

	/**
	 * Default constructor
	 */
	public BusinessException() {
	}

	/**
	 * Constructor that takes a error message
	 * 
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
	}

	/**
	 * Constructor that takes a cause exception
	 * 
	 * @param throwable
	 */
	public BusinessException(Throwable throwable) {
		super(throwable);
	}
}
