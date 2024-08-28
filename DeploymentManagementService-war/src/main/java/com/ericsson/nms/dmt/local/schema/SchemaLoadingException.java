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
package com.ericsson.nms.dmt.local.schema;

import com.ericsson.nms.dmt.error.DmtBaseException;

/**
 * This exception is thrown by the {@link SchemaLoader} component when it can
 * not load and process the LITP schema files successfully.
 */
public class SchemaLoadingException extends DmtBaseException {

	private static final long serialVersionUID = 3003562028681178342L;

	/**
	 * Constructor that takes a custom error message and the root cause of the
	 * exception
	 * 
	 * @param message
	 *            - custom error message
	 * @param t
	 *            - root cause of the exception
	 */
	public SchemaLoadingException(String message, Throwable t) {
		super(message, t);
	}
}
