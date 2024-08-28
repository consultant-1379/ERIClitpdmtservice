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

import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.IDENTIFIER_ATTRIBUTE;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.AggregatePropertyException;
import com.ericsson.nms.dmt.local.error.InvalidPropertyException;
import com.ericsson.nms.dmt.local.schema.data.SchemaAttribute;
import com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction;

/**
 * Validation rule that verifies whether the ID of the given node is valid,
 * which means that it is an acceptable value for the constraints that might
 * exist on its corresponding {@link SchemaAttribute} object.
 * 
 * @see ValidationRule
 */
class ValidNodeIdValueRule implements ValidationRule {

	private final Node node;

	/**
	 * Constructor that takes the node subject to validation
	 * 
	 * @param node
	 */
	public ValidNodeIdValueRule(Node node) {
		this.node = node;
	}

	@Override
	public void validate() {
		AggregatePropertyException exception = new AggregatePropertyException();
		final String attrValue = node.getId();
		SchemaAttribute attribute = node.getActualSchema().getAttribute(
				IDENTIFIER_ATTRIBUTE);

		for (SimpleTypeRestriction restriction : attribute.getRestrictions()) {
			if (!restriction.isAcceptableValue(attrValue)) {
				String message = String.format(
						"Invalid value for item %s: '%s'",
						IDENTIFIER_ATTRIBUTE, attrValue);
				exception.addCause(new InvalidPropertyException(message,
						"item " + IDENTIFIER_ATTRIBUTE));
			}
		}
		if (exception.hasCauses()) {
			throw exception;
		}
	}
}
