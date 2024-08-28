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

import static com.ericsson.nms.dmt.Category.REGULAR_ITEM;
import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContentAsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ericsson.nms.dmt.NodePath;
import com.ericsson.nms.dmt.live.Node;

public class NodeParserTest {

	private final NodeParser parser = new NodeParser(
			"https://ms1:9999/litp/rest/v1");

	@Test
	public void shouldParseAllFieldsOfSingleNodeJsonResponse() {
		// Given
		String jsonPayload = loadFileContentAsString("litp/nodes/ms.json");

		// When
		Node node = parser.parseSingleNode(jsonPayload);

		// Then
		assertThat(node.getId(), is("ms"));
		assertThat(node.getPath(), is(NodePath.fromString("/ms")));
		assertThat(node.getState(), is("Initial"));
		assertThat(node.getCategory(), is(REGULAR_ITEM));
		assertThat(node.getType(), is("ms"));
		assertThat(node.getChildren().size(), is(6));
		assertThat(node.getChildren().size(), is(notNullValue()));
		assertThat(node.getProperties().size(), is(1));
		assertThat(node.getProperties().containsKey("hostname"), is(true));
		assertThat(node.getProperties().containsValue("ms1"), is(true));

		Node nodeChild = node.getChildren().get(0);
		assertThat(nodeChild.getId(), is("items"));
		assertThat(nodeChild.getState(), is("Initial"));
		assertThat(nodeChild.getProperties().size(), is(0));
	}
}
