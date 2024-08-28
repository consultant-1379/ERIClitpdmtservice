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

import javax.ejb.ApplicationException;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ericsson.nms.dmt.rest.serializer.DefaultExceptionSerializer;

/**
 * Base class for all DMT exceptions. All exceptions defined in DMT must extend
 * this class, directly or indirectly.
 */
@ApplicationException(inherited = true, rollback = true)
@JsonSerialize(using = DefaultExceptionSerializer.class)
public abstract class DmtBaseException extends RuntimeException {

	private static final long serialVersionUID = -7862629115952545385L;

	/**
	 * Default constructor
	 */
	public DmtBaseException() {
	}

	/**
	 * Constructor that takes an wraps any exception that might be caught in
	 * runtime
	 * 
	 * @param t
	 *            - any exception
	 */
	public DmtBaseException(Throwable t) {
		super(t);
	}

	public DmtBaseException(String message, Throwable t) {
		super(message, t);
	}

	public DmtBaseException(String message) {
		super(message);
	}
}