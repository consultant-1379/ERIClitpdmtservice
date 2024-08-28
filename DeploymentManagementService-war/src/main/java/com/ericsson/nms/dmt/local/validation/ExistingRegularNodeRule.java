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

import java.util.ArrayList;
import java.util.List;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ItemExistsException;

/**
 * Validation rule that verifies whether a regular node from a specific type
 * hierarchy already exists on its parent
 * 
 * @see ValidationRule
 */
class ExistingRegularNodeRule implements ValidationRule {

	private final Node parentNode;
	private final Node childNode;

	/**
	 * Constructor that takes the parent node and the child node that is being
	 * created.
	 * 
	 * @param parentNode
	 *            - parent node - node where the child node is being created
	 * @param childNode
	 *            - node being created
	 */
	public ExistingRegularNodeRule(Node parentNode, Node childNode) {
		this.parentNode = parentNode;
		this.childNode = childNode;
	}

	@Override
	public void validate() {
		// TODO confirm if the error type/message is OK in this situation
		if (parentNode.isRegular() && childNode.isRegular()) {
			if (getExistingChildTypesAndExtensions().contains(
					childNode.getType())) {
				String message = String.format(
						"Item already exists in model: %s", childNode.getId());
				throw new ItemExistsException(message);
			}
		}
	}

	private List<String> getExistingChildTypesAndExtensions() {
		List<String> existing = new ArrayList<>();
		for (Node child : parentNode.getChildren()) {
			if (child.isRegular()) {
				existing.add(child.getDeclaringSchema().getName());
				existing.addAll(child.getDeclaringSchema().getSubstitutables());
			}
		}
		return existing;
	}
}
