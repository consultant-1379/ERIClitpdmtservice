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
package com.ericsson.nms.dmt;

import static java.util.Arrays.asList;

/**
 * Defines the possible classifications for a node in the deployment model.
 */
public enum Category {

	REGULAR_ITEM, COLLECTION_ITEM, REFERENCE_ITEM, REFERENCE_COLLECTION_ITEM;

	/**
	 * Indicates when the node's category refers to a collection
	 * 
	 * @return true if it is a collection
	 */
	public boolean isCollection() {
		return asList(COLLECTION_ITEM, REFERENCE_COLLECTION_ITEM)
				.contains(this);
	}

	/**
	 * Indicates when the node's category is a reference (link to another node)
	 * 
	 * @return true if it is a reference
	 */
	public boolean isReference() {
		return asList(REFERENCE_ITEM, REFERENCE_COLLECTION_ITEM).contains(this);
	}
}