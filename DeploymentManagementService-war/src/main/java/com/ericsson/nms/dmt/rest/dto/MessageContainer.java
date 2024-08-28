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
package com.ericsson.nms.dmt.rest.dto;

import com.ericsson.nms.dmt.rest.hateoas.LinkableResource;

/**
 * Data container to transfer informational messages and links
 */
public class MessageContainer extends LinkableResource {

	private String message;

	/**
	 * Default constructor
	 */
	public MessageContainer() {
	}

	/**
	 * Constructor that takes a message
	 * 
	 * @param message
	 *            - informational message
	 */
	public MessageContainer(String message) {
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
