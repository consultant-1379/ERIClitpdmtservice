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
package com.ericsson.nms.dmt.live.litp.parsing;

import static com.ericsson.nms.dmt.Category.*;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.nms.dmt.Category;

/**
 * Helper class that parses the item types and nodes relation keys (provided as
 * part of the payload returned by LITP) in order to get valuable information
 * encoded on them.
 */
@SuppressWarnings("serial")
class RelationParser {

	private static final Map<Category, String> itemTypeRelationKeys;
	private static final Map<Category, String> itemTypeNamePatterns;

	static {
		itemTypeRelationKeys = new HashMap<Category, String>() {
			{
				put(REGULAR_ITEM, "self");
				put(COLLECTION_ITEM, "collection-of");
				put(REFERENCE_ITEM, "reference-to");
				put(REFERENCE_COLLECTION_ITEM, "ref-collection-of");
			}
		};

		itemTypeNamePatterns = new HashMap<Category, String>() {
			{
				put(REGULAR_ITEM, "(\\S+)");
				put(COLLECTION_ITEM, "^collection-of-(\\S+)$");
				put(REFERENCE_ITEM, "^reference-to-(\\S+)$");
				put(REFERENCE_COLLECTION_ITEM, "^ref-collection-of-(\\S+)$");
			}
		};
	}

	/**
	 * Parses and extracts the {@link Category} from an item type link relation
	 * key. For example:
	 * 
	 * Given an item type payload which contains : <i> "_links": {
	 * "reference-to": { ... </i> An <i>'REFERENCE_ITEM'</i> category is
	 * returned
	 * 
	 * @param relationKey
	 * @return
	 */
	public static Category categoryFromItemTypeLinkRelationKey(
			String relationKey) {
		for (Category category : Category.values()) {
			if (relationKey.equals(itemTypeRelationKeys.get(category))) {
				return category;
			}
		}
		throw new IllegalArgumentException(
				"Not a valid relation key for item types");
	}

	/**
	 * Parses and extracts the {@link Category} from an node type name key. For
	 * example:
	 * 
	 * Given a node payload which contains : <i>... "item-type-name":
	 * "collection-of-cluster", ...</i> An <i>'COLLECTION_ITEM'</i> category is
	 * returned
	 * 
	 * @param itemTypeName
	 * @return
	 */
	public static Category categoryFromItemTypeName(String itemTypeName) {
		for (Category category : asList(COLLECTION_ITEM, REFERENCE_ITEM,
				REFERENCE_COLLECTION_ITEM)) {
			if (itemTypeName.matches(itemTypeNamePatterns.get(category))) {
				return category;
			}
		}
		return REGULAR_ITEM;
	}

	/**
	 * Parses and extracts the actual node type from its type name key. For
	 * example:
	 * 
	 * Given a node payload which contains : <i>... "item-type-name":
	 * "collection-of-cluster", ...</i> The value <i>'cluster'</i> is returned
	 * 
	 * @param itemTypeName
	 * @return
	 */
	public static String actualTypeFromItemTypeName(String itemTypeName) {
		Category category = categoryFromItemTypeName(itemTypeName);
		Pattern pattern = Pattern
				.compile(itemTypeNamePatterns.get(category));
		Matcher matcher = pattern.matcher(itemTypeName);
		if (!matcher.matches()) {
			return null;
		}
		return matcher.group(1);
	}
}