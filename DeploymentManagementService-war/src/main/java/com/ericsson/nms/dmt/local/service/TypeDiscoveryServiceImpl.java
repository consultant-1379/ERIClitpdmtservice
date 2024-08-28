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
package com.ericsson.nms.dmt.local.service;

import static com.ericsson.nms.dmt.Mode.LOCAL;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import com.ericsson.nms.dmt.AbstractNode;
import com.ericsson.nms.dmt.ItemType;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;
import com.ericsson.nms.dmt.mapping.Mapper;
import com.ericsson.nms.dmt.service.TypeDiscoveryService;
import com.ericsson.nms.dmt.service.qualifier.ServiceQualifier;

/**
 * Provides business methods used for type discovery. This component uses the
 * data extracted from the LITP schema document files to provide item type
 * details to its consumers.
 * 
 * @see TypeDiscoveryService
 */
@ServiceQualifier(mode = LOCAL)
public class TypeDiscoveryServiceImpl implements TypeDiscoveryService {

	@Inject
	private SchemaRepository schemaRepository;

	@Inject
	private Mapper<SchemaElement, ItemType> mapper;

	@Override
	public ItemType getItemType(String name) {
		SchemaElement element = findSchemaElement(name);
		return mapper.map(element);
	}

	private SchemaElement findSchemaElement(String name) {
		SchemaElement element = schemaRepository.get(name);

		if (element == null) {
			throw new InvalidLocationException("Not found");
		}
		return element;
	}

	@Override
	public Set<ItemType> getAddableChildTypes(AbstractNode node) {
		if (node.isReference()) {
			return emptySet();
		}

		if (node.isCollection()) {
			return getAddableChildTypesToCollectionNode((Node) node);
		}

		return getAddableChildTypesToRegularNode((Node) node);
	}

	private Set<ItemType> getAddableChildTypesToCollectionNode(
			Node collectionNode) {
		SchemaElement nodeSchema = collectionNode.getDeclaringSchema()
				.getFirstChild();
		if (!isCollectionNodeFull(collectionNode, nodeSchema)) {
			return getConcreteTypesFromHierarchy(nodeSchema);
		}
		return emptySet();
	}

	private boolean isCollectionNodeFull(Node collectionNode,
			SchemaElement nodeSchema) {
		return collectionNode.getNumberOfChildren() >= nodeSchema
				.getMaxOccurs() && !nodeSchema.isMaxOccursUnbounded();
	}

	private Set<ItemType> getConcreteTypesFromHierarchy(SchemaElement nodeSchema) {
		if (!nodeSchema.hasSubstitutables()) {
			return newHashSet(mapper.map(nodeSchema));
		}

		Set<ItemType> result = new HashSet<>();
		for (String substitutable : nodeSchema.getSubstitutables()) {
			result.addAll(getConcreteTypesFromHierarchy(findSchemaElement(substitutable)));
		}

		return result;
	}

	private Set<ItemType> getAddableChildTypesToRegularNode(Node node) {
		Set<ItemType> result = new HashSet<>();
		Set<ItemType> existingChildTypes = getAlreadyExistingRegularChildTypes(node);
		for (SchemaElement possibleChildSchema : node.getActualSchema()
				.getChildren()) {
			if (possibleChildSchema.isComplexElement()) {
				ComplexSchemaElement complexChild = possibleChildSchema
						.asComplexElement();
				if (complexChild.isRegularElement()) {
					Set<ItemType> possibleChildTypes = getConcreteTypesFromHierarchy(complexChild);
					if (isMissingChildType(existingChildTypes,
							possibleChildTypes)) {
						result.addAll(possibleChildTypes);
					}
				}

			}
		}
		return result;
	}

	private boolean isMissingChildType(Set<ItemType> existingChildTypes,
			Set<ItemType> possibleChildTypes) {
		return intersection(existingChildTypes, possibleChildTypes).isEmpty();
	}

	private Set<ItemType> getAlreadyExistingRegularChildTypes(Node node) {
		Set<ItemType> result = new HashSet<>();
		for (Node nodeChild : node.getChildren()) {
			if (nodeChild.isRegular()) {
				result.add(mapper.map(nodeChild.getActualSchema()));
			}
		}
		return result;
	}
}
