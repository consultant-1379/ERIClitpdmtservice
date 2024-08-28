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

import static com.ericsson.nms.dmt.util.JsonPathSafeReader.read;
import static com.ericsson.nms.dmt.util.JsonPathSafeReader.readList;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import com.ericsson.nms.dmt.*;

/**
 * Helper class that provides logic to parse the item types payload returned by
 * LITP in JSON format to {@link ItemType} objects.
 */
public class ItemTypeParser {

	/**
	 * Takes a JSON payload that contains the response for the get all item
	 * types operation on LITP and parses it into a list of {@link ItemType}
	 * objects.
	 * 
	 * @param itemTypesJson
	 *            - JSON payload
	 * @return
	 */
	public List<ItemType> parseMultipleItemTypes(String itemTypesJson) {
		List<ItemType> result = new ArrayList<>();
		List<JSONObject> itemTypes = readList(itemTypesJson,
				"$._embedded.item-type[*]");
		for (JSONObject itemTypeJson : itemTypes) {
			result.add(parseSingleItemType(itemTypeJson.toJSONString()));
		}
		return result;
	}

	/**
	 * Takes a JSON payload that contains the response for the get single item
	 * type operation on LITP and parses it into an {@link ItemType} object.
	 * 
	 * @param itemTypeJson
	 *            - JSON payload
	 * @return
	 */
	public ItemType parseSingleItemType(String itemTypeJson) {
		ItemType item = new ItemType();
		item.setName((String) read(itemTypeJson, "$.id"));
		item.setDescription((String) read(itemTypeJson, "$.description"));
		String baseTypeUrl = read(itemTypeJson, "$._links.base-type.href");
		item.setBaseType(extractTypeFromUrl(baseTypeUrl));
		JSONObject propertiesNode = read(itemTypeJson, "$.properties");
		if (propertiesNode != null) {
			for (String propertyId : propertiesNode.keySet()) {
				item.addProperty(parseProperty(propertyId,
						(JSONObject) propertiesNode.get(propertyId)));
			}
		}

		List<JSONObject> itemNodes = readList(itemTypeJson,
				"$._embedded.item[*]");
		for (JSONObject itemElement : itemNodes) {
			item.addChild(parseChild(itemElement));
		}

		return item;
	}

	private String extractTypeFromUrl(String baseTypeUrl) {
		if (isBlank(baseTypeUrl)) {
			return null;
		}
		return baseTypeUrl.substring(baseTypeUrl.lastIndexOf("/") + 1);
	}

	private ItemTypeProperty parseProperty(String propertyId,
			JSONObject propertyNode) {
		ItemTypeProperty property = new ItemTypeProperty();
		property.setId(propertyId);
		property.setDescription((String) read(propertyNode, "$.description"));
		property.setRegex((String) read(propertyNode, "$.regex"));
		property.setRequired((Boolean) read(propertyNode, "$.required"));
		property.setDefaultValue(read(propertyNode, "$.default"));
		return property;
	}

	private ItemTypeChild parseChild(JSONObject childJson) {
		JSONObject linksNode = read(childJson, "$._links");
		String relationKey = linksNode.keySet().iterator().next();
		Category category = RelationParser
				.categoryFromItemTypeLinkRelationKey(relationKey);
		ItemTypeChild child = category.isCollection() ? parseCollectionChildSpecificFields(childJson)
				: parseRegularChildSpecificFields(childJson);
		child.setCategory(category);
		child.setDescription((String) read(childJson, "$.description"));
		String typeUrl = read(childJson, "$._links." + relationKey + ".href");
		child.setName(extractTypeFromUrl(typeUrl));
		return child;
	}

	private ItemTypeCollectionChild parseCollectionChildSpecificFields(
			JSONObject childJson) {
		ItemTypeCollectionChild child = new ItemTypeCollectionChild();
		child.setMin((int) read(childJson, "$.min"));
		child.setMax((int) read(childJson, "$.max"));
		return child;
	}

	private ItemTypeRegularChild parseRegularChildSpecificFields(
			JSONObject childJson) {
		ItemTypeRegularChild child = new ItemTypeRegularChild();
		child.setRequired((boolean) read(childJson, "$.required"));
		return child;
	}
}
