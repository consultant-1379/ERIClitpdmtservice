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

import static com.ericsson.nms.dmt.http.StatusCode.INTERNAL_SERVER_ERROR;

/**
 * Exception used to wrap unknowns (3PP libraries) and unexpected exceptions.
 */
@CausesStatusCode(INTERNAL_SERVER_ERROR)
public class UnexpectedException extends DmtBaseException {

	private static final long serialVersionUID = 5719219883893636257L;

	/**
	 * Default constructor
	 */
	public UnexpectedException() {
	}

	/**
	 * Constructor that takes an error message
	 * 
	 * @param message
	 */
	public UnexpectedException(String message) {
		super(message);
	}

	/**
	 * Constructor that takes any exception
	 * 
	 * @param throwable
	 */
	public UnexpectedException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor that takes an error message and any exception
	 * 
	 * @param message
	 * @param throwable
	 */
	public UnexpectedException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Utility method that wraps any type of exception in a
	 * {@link UnexpectedException}
	 * 
	 * @param t
	 *            - any exception
	 * @return
	 */
	public static UnexpectedException wrap(Throwable t) {
		return new UnexpectedException(t);
	}
}
