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
import com.ericsson.nms.dmt.local.error.CardinalityException;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;

/**
 * Validation rule that verifies whether a collection node is already full based
 * on the maxOccurs value
 * 
 * @see ValidationRule
 */
class CollectionNodeCardinalityRule implements ValidationRule {

	private final Node parentNode;

	/**
	 * Constructor that takes the parent node that is being modified as the
	 * outcome of the operation performed on the deployment model
	 * 
	 * @param parentNode
	 *            - parent node
	 */
	public CollectionNodeCardinalityRule(Node parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public void validate() {
		if (parentNode.isCollection()) {
			SchemaElement childSchema = parentNode.getDeclaringSchema()
					.getFirstChild();
			if (isCollectionFull(childSchema)) {
				String message = String.format(
						"This collection requires a maximum "
								+ "of %s items not marked for removal",
						childSchema.getMaxOccurs());
				throw new CardinalityException(message);
			}
		}
	}

	private boolean isCollectionFull(SchemaElement childSchema) {
		return !childSchema.isMaxOccursUnbounded()
				&& parentNode.getNumberOfChildren() >= childSchema
						.getMaxOccurs();
	}
}
