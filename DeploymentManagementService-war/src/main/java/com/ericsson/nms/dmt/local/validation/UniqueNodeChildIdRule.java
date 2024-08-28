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
package com.ericsson.nms.dmt.local.validation;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ItemExistsException;

/**
 * Validation rule that verifies whether a parent node already contains a child
 * with the same ID as the one being added
 * 
 * @see ValidationRule
 */
class UniqueNodeChildIdRule implements ValidationRule {

	private final Node parentNode;
	private final Node childNode;

	/**
	 * Constructor that takes the parent node and the node being added to it
	 * 
	 * @param parentNode
	 *            - parent node
	 * @param childNode
	 *            - node being added to the parent node
	 */
	public UniqueNodeChildIdRule(Node parentNode, Node childNode) {
		this.parentNode = parentNode;
		this.childNode = childNode;
	}

	@Override
	public void validate() {
		if (parentNode.getChildById(childNode.getId()) != null) {
			String message = String.format("Item already exists in model: %s",
					childNode.getId());
			throw new ItemExistsException(message);
		}
	}
}