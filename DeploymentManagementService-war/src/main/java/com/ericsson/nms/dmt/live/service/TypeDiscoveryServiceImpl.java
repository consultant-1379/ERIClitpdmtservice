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
package com.ericsson.nms.dmt.live.service;

import static com.ericsson.nms.dmt.Mode.LIVE;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;

import java.util.*;

import javax.inject.Inject;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.live.Node;
import com.ericsson.nms.dmt.live.litp.LitpClient;
import com.ericsson.nms.dmt.service.TypeDiscoveryService;
import com.ericsson.nms.dmt.service.qualifier.ServiceQualifier;

/**
 * Provides business methods used for type discovery. This component connects to
 * a LITP server instance to obtain item type details to provide to its
 * consumers.
 * 
 * @see TypeDiscoveryService
 */
@ServiceQualifier(mode = LIVE)
public class TypeDiscoveryServiceImpl implements TypeDiscoveryService {

	@Inject
	private LitpClient litpClient;

	@Inject
	private ItemTypeCachedRepository itemTypeRepository;

	@Override
	public ItemType getItemType(String itemTypeName) {
		return itemTypeRepository.get(itemTypeName);
	}

	private List<ItemType> getAllItemTypes() {
		return itemTypeRepository.getAll();
	}

	@Override
	public Set<ItemType> getAddableChildTypes(AbstractNode node) {
		if (node.isReference()) {
			return emptySet();
		}

		if (node.isRegular()) {
			return getAddableChildTypesToRegularNode(node);
		}

		return getAddableChildTypesToCollectionNode(node);
	}

	private Set<ItemType> getAddableChildTypesToCollectionNode(
			AbstractNode collectionNode) {
		if (isCollectionFull(collectionNode)) {
			return emptySet();
		}

		return getConcreteTypesFromHierarchy(collectionNode.getType());
	}

	private boolean isCollectionFull(AbstractNode collectionNode) {
		ItemTypeCollectionChild collectionNodeItemType = findCollectionNodeItemTypeDetails(collectionNode);
		return collectionNode.getNumberOfChildren() >= collectionNodeItemType
				.getMax();
	}

	private ItemTypeCollectionChild findCollectionNodeItemTypeDetails(
			AbstractNode collectionNode) {
		Node parentNode = litpClient
				.findNode(collectionNode.getPath().parent());
		ItemType parentNodeItemType = getItemType(parentNode.getType());
		return (ItemTypeCollectionChild) parentNodeItemType
				.getChild(collectionNode.getType());
	}

	private Set<ItemType> getAddableChildTypesToRegularNode(AbstractNode node) {
		Set<ItemType> result = new HashSet<>();
		Set<ItemType> existingChildTypes = getAlreadyExistingRegularChildTypes(node);
		ItemType nodeItemType = getItemType(node.getType());

		for (ItemTypeChild possibleChildType : nodeItemType.getChildren()) {
			if (possibleChildType.isRegular()) {
				Set<ItemType> possibleChildTypes = getConcreteTypesFromHierarchy(possibleChildType
						.getName());
				if (isMissingChildType(existingChildTypes, possibleChildTypes)) {
					result.addAll(possibleChildTypes);
				}
			}
		}
		return result;
	}

	private boolean isMissingChildType(Set<ItemType> existingChildTypes,
			Set<ItemType> possibleChildTypes) {
		return intersection(existingChildTypes, possibleChildTypes).isEmpty();
	}

	private Set<ItemType> getAlreadyExistingRegularChildTypes(AbstractNode node) {
		Set<ItemType> result = new HashSet<>();
		for (AbstractNode nodeChild : node.getChildren()) {
			if (nodeChild.isRegular()) {
				result.add(getItemType(nodeChild.getType()));
			}
		}
		return result;
	}

	private Set<ItemType> getConcreteTypesFromHierarchy(String itemTypeName) {
		Set<ItemType> extensions = getTypeExtensions(itemTypeName);
		if (extensions.isEmpty()) {
			return newHashSet(getItemType(itemTypeName));
		}

		Set<ItemType> result = new HashSet<>();
		for (ItemType extension : extensions) {
			result.addAll(getConcreteTypesFromHierarchy(extension.getName()));
		}

		return result;
	}

	private Set<ItemType> getTypeExtensions(String baseType) {
		Set<ItemType> result = new HashSet<>();
		for (ItemType itemType : getAllItemTypes()) {
			if (isBaseTypeOf(baseType, itemType)) {
				result.add(itemType);
			}
		}
		return result;
	}

	private boolean isBaseTypeOf(String baseType, ItemType itemType) {
		return itemType.hasBaseType()
				&& itemType.getBaseType().equals(baseType);
	}
}