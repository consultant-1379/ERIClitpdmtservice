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

import com.ericsson.nms.dmt.error.*;

/**
 * This exception is thrown when a change in a node (create or delete) causes a
 * cardinality mismatch (the number of existing child nodes is not valid
 * compared to the min and max number of allowed children)
 */
@CausesStatusCode(UNPROCESSABLE)
@CustomErrorType("CardinalityError")
public class CardinalityException extends BusinessException {

	private static final long serialVersionUID = 3038681849375257017L;

	/**
	 * Constructor that takes a custom error message
	 * 
	 * @param message
	 */
	public CardinalityException(String message) {
		super(message);
	}
}
