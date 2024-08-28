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
package com.ericsson.nms.dmt.test.helper;

import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContentAsString;

import com.ericsson.nms.dmt.live.Node;
import com.ericsson.nms.dmt.live.litp.parsing.NodeParser;

public class LiveNodeFactory {

	private static NodeParser parser = new NodeParser(
			"https://ms1:9999/litp/rest/v1");

	public static Node newInstance(String name) {
		String nodeJson = loadFileContentAsString("litp/nodes/" + name
				+ ".json");
		return parser.parseSingleNode(nodeJson);
	}
}
