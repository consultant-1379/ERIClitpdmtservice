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
import com.ericsson.nms.dmt.local.error.PropertyNotAllowedException;

/**
 * Validation rule that verifies whether the node can be updated
 * 
 * @see ValidationRule
 */
class UpdatableNodeRule implements ValidationRule {

	private final Node node;

	/**
	 * Constructor that takes the node being updated
	 * 
	 * @param node
	 */
	public UpdatableNodeRule(Node node) {
		this.node = node;
	}

	@Override
	public void validate() {
		if (node.isCollection()) {
			throw new PropertyNotAllowedException(
					"Properties cannot be set on collections");
		}
	}
}
