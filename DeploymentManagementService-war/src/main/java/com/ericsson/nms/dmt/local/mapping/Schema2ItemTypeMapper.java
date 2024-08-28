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
package com.ericsson.nms.dmt.local.mapping;

import static com.ericsson.nms.dmt.Category.*;
import static com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType.PATTERN;

import java.util.Set;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.local.schema.data.*;
import com.ericsson.nms.dmt.mapping.Mapper;

/**
 * Mapping class used to transform {@link SchemaElement} objects into
 * {@link ItemType} objects
 * 
 * @see Mapper
 */
public class Schema2ItemTypeMapper implements Mapper<SchemaElement, ItemType> {

	@Override
	public ItemType map(SchemaElement schemaElement) {
		ItemType itemType = new ItemType();
		itemType.setName(schemaElement.getName());
		itemType.setDescription(schemaElement.getDescription());

		if (schemaElement.isComplexElement()) {
			Set<SchemaElement> children = schemaElement.asComplexElement()
					.getChildren();
			for (SchemaElement childSchema : children) {
				if (childSchema.isSimpleElement()) {
					itemType.addProperty(mapProperty(childSchema
							.asSimpleElement()));
					continue;
				}

				ComplexSchemaElement complexChild = childSchema
						.asComplexElement();
				ItemTypeChild child = complexChild.isCollectionContainerElement() ? mapCollectionChild(complexChild)
						: mapRegularChild(complexChild);
				child.setName(complexChild.isReferenceElement() ? complexChild
						.getReferencedElementName() : childSchema.getName());
				child.setDescription(childSchema.getDescription());

				itemType.addChild(child);
			}
		}
		return itemType;
	}

	private ItemTypeChild mapRegularChild(ComplexSchemaElement childSchema) {
		ItemTypeRegularChild child = new ItemTypeRegularChild();
		child.setCategory(getCategoryForRegularChild(childSchema));
		child.setRequired(childSchema.isRequired());
		return child;
	}

	private ItemTypeChild mapCollectionChild(ComplexSchemaElement childSchema) {
		ItemTypeCollectionChild child = new ItemTypeCollectionChild();
		child.setCategory(getCategoryForCollectionChild(childSchema));
		child.setMin(childSchema.getMinOccurs());
		child.setMax(childSchema.getMaxOccurs());
		return child;
	}

	private ItemTypeProperty mapProperty(SimpleSchemaElement childSchema) {
		ItemTypeProperty property = new ItemTypeProperty();
		property.setId(childSchema.getName());
		property.setDescription(childSchema.getDescription());
		property.setDefaultValue(childSchema.getDefaultValue());

		if (childSchema.hasRestriction(PATTERN)) {
			SimpleTypeRestriction restriction = childSchema
					.getRestriction(PATTERN);
			property.setRegex(restriction.getConstraints().iterator().next());
		}

		property.setRequired(childSchema.isRequired());
		return property;
	}

	private Category getCategoryForRegularChild(ComplexSchemaElement nodeSchema) {
		return nodeSchema.isReferenceElement() ? REFERENCE_ITEM : REGULAR_ITEM;
	}

	private Category getCategoryForCollectionChild(
			ComplexSchemaElement nodeSchema) {
		ComplexSchemaElement childSchema = nodeSchema.getFirstChild()
				.asComplexElement();
		return childSchema.isReferenceElement() ? REFERENCE_COLLECTION_ITEM
				: COLLECTION_ITEM;
	}
}
