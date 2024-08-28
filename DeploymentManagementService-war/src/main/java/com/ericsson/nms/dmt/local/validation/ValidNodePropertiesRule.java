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

import static com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType.PATTERN;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import java.util.*;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.*;
import com.ericsson.nms.dmt.local.schema.data.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Validation rule that verifies whether the node is valid in regards to the
 * consistency of its properties. This validation rule basically uses the
 * corresponding {@link SchemaElement} for the specified node to check if it
 * contains all the required properties and if the existing properties names and
 * values are valid.
 * 
 * @see ValidationRule
 */
class ValidNodePropertiesRule implements ValidationRule {

	private final Node node;

	/**
	 * Constructor that takes the node subject to validation
	 * 
	 * @param node
	 */
	public ValidNodePropertiesRule(Node node) {
		this.node = node;
	}

	@Override
	public void validate() {
		AggregatePropertyException exception = new AggregatePropertyException();
		exception.addCauses(validateIfPropertyNamesAreValid());
		exception.addCauses(validateIfAllMandatoryPropertiesExist());
		exception.addCauses(validatePropertyValues());

		if (exception.hasCauses()) {
			throw exception;
		}
	}

	private List<AbstractPropertyException> validateIfPropertyNamesAreValid() {
		List<AbstractPropertyException> errors = new ArrayList<>();
		ComplexSchemaElement nodeSchema = node.getActualSchema();
		Collection<String> validPropertyNames = getValidPropertyNames(node);

		for (String providedPropertyName : node.getProperties().keySet()) {
			if (!validPropertyNames.contains(providedPropertyName)) {
				String message = String.format(
						"'%s' is not an allowed property of %s",
						providedPropertyName, nodeSchema.getName());
				errors.add(new PropertyNotAllowedException(message,
						providedPropertyName));
			}
		}
		return errors;
	}

	private List<AbstractPropertyException> validateIfAllMandatoryPropertiesExist() {
		List<AbstractPropertyException> errors = new ArrayList<>();
		Map<String, String> nodeProperties = node.getProperties();
		ComplexSchemaElement nodeSchema = node.getActualSchema();

		for (SchemaElement childSchema : nodeSchema.getRequiredChildren()) {
			if (!childSchema.isSimpleElement()
					|| childSchema.asSimpleElement().hasDefaultValue()) {
				continue;
			}

			if (!nodeProperties.containsKey(childSchema.getName())) {
				String message = String
						.format("ItemType '%s' is required to have a 'property' with id '%s' and type '%s'",
								nodeSchema.getName(), childSchema.getName(),
								childSchema.getType());
				errors.add(new MissingRequiredPropertyException(message,
						childSchema.getName()));
			}
		}
		return errors;
	}

	private List<AbstractPropertyException> validatePropertyValues() {
		List<AbstractPropertyException> errors = new ArrayList<>();
		ComplexSchemaElement nodeSchema = node.getActualSchema();
		Collection<String> validPropertyNames = getValidPropertyNames(node);

		for (String propertyName : node.getProperties().keySet()) {
			if (!validPropertyNames.contains(propertyName)) {
				continue;
			}

			SimpleSchemaElement propertySchema = nodeSchema.getChild(
					propertyName).asSimpleElement();

			if (propertySchema.hasRestriction(PATTERN)) {
				SimpleTypeRestriction restriction = propertySchema
						.getRestriction(PATTERN);
				String propertyValue = node.getPropertyValue(propertyName);

				if (!restriction.isAcceptableValue(propertyValue)) {
					String message = String.format(
							"Invalid value: '%s' for property type: %s",
							propertyValue, restriction);
					errors.add(new RegexException(message, propertyName));
				}
			}
		}
		return errors;
	}

	private Collection<String> getValidPropertyNames(Node node) {
		Set<SchemaElement> children = node.getActualSchema().getChildren();
		Collection<SchemaElement> properties = filter(children,
				new Predicate<SchemaElement>() {
					@Override
					public boolean apply(SchemaElement child) {
						return child.isSimpleElement();
					}
				});

		return transform(properties, new Function<SchemaElement, String>() {
			@Override
			public String apply(SchemaElement child) {
				return child.getName();
			}
		});
	}
}
