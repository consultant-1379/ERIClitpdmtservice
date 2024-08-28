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
import com.ericsson.nms.dmt.local.error.OperationNotAllowedException;

/**
 * Validation rule that verifies whether a node can be removed from the
 * deployment model
 * 
 * @see ValidationRule
 */
class DeletableNodeRule implements ValidationRule {

	private final Node node;

	/**
	 * Constructor that takes the node being removed from the deployment model
	 * 
	 * @param node
	 */
	public DeletableNodeRule(Node node) {
		this.node = node;
	}

	@Override
	public void validate() {
		if (node.isCollection()) {
			throw new OperationNotAllowedException(
					"Cannot directly delete Collection item");
		}
	}
}
