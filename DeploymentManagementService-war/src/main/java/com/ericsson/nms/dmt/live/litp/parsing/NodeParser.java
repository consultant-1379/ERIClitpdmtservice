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
import static org.apache.commons.lang.StringUtils.difference;

import java.util.List;

import net.minidev.json.JSONObject;

import com.ericsson.nms.dmt.NodePath;
import com.ericsson.nms.dmt.live.Node;
import com.ericsson.nms.dmt.live.Node.Builder;

/**
 * Helper class that provides logic to parse the node payload returned by LITP
 * in JSON format to {@link Node} objects.
 */
public class NodeParser {

	private final String litpBaseUri;

	/**
	 * Constructor that takes the LITP service base URI as argument. The method
	 * uses it to extract only the part that refers to the node path from the
	 * URLs returned in the payload.
	 * 
	 * @param litpBaseUri
	 *            - LITP service base URI
	 */
	public NodeParser(String litpBaseUri) {
		this.litpBaseUri = litpBaseUri;
	}

	/**
	 * Takes a JSON payload that contains node details and parses it into a
	 * {@link Node} object.
	 * 
	 * @param nodeJson
	 *            - JSON payload
	 * @return
	 */
	public Node parseSingleNode(String nodeJson) {
		Builder nodeBuilder = parseNode((JSONObject) read(nodeJson, "$"));
		List<JSONObject> children = readList(nodeJson, "$._embedded.item[*]");
		for (JSONObject childJson : children) {
			nodeBuilder.child(parseNode(childJson).build());
		}
		return nodeBuilder.build();
	}

	private Builder parseNode(JSONObject nodeJson) {
		Builder builder = Node.builder();
		builder.id((String) read(nodeJson, "$.id"));
		String self = read(nodeJson, "$._links.self.href");
		builder.path(NodePath.fromString(difference(litpBaseUri, self)));
		builder.state((String) read(nodeJson, "$.state"));
		String itemTypeName = read(nodeJson, "$.item-type-name");
		builder.category(RelationParser
				.categoryFromItemTypeName(itemTypeName));
		builder.type(RelationParser.actualTypeFromItemTypeName(itemTypeName));
		JSONObject propertiesField = read(nodeJson, "$.properties");
		if (propertiesField != null) {
			for (String propertyName : propertiesField.keySet()) {
				builder.property(propertyName,
						(String) propertiesField.get(propertyName));
			}
		}
		return builder;
	}
}
