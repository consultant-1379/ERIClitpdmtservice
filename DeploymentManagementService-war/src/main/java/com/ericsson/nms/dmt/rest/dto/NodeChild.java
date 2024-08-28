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

import org.codehaus.jackson.annotate.JsonProperty;

import com.ericsson.nms.dmt.rest.hateoas.LinkableResource;

/**
 * Data container to transfer information about a node and its children. It has
 * the proper structure used for data serialization.
 */
public class NodeChild extends LinkableResource {

	private String id;

	@JsonProperty("children")
	private boolean hasChildren;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}
