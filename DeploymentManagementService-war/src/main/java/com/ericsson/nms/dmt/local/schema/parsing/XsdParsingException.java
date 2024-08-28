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
package com.ericsson.nms.dmt.local.schema.parsing;

import com.ericsson.nms.dmt.error.DmtBaseException;

/**
 * This exception is thrown when the {@link XsdParser} can not parse
 * successfully and XML schema document. It can be caused by not-well structured
 * schema documents
 */
public class XsdParsingException extends DmtBaseException {

	private static final long serialVersionUID = 3003562028681178342L;

	/**
	 * Constructor that takes an custom error message and the root cause of the
	 * exception
	 * 
	 * @param message
	 *            - error message
	 * @param t
	 *            - root cause
	 */
	public XsdParsingException(String message, Throwable t) {
		super(message, t);
	}
}
