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

import com.ericsson.nms.dmt.ItemType;
import com.ericsson.nms.dmt.live.litp.parsing.ItemTypeParser;

public class ItemTypeFactory {

	private static ItemTypeParser parser = new ItemTypeParser();

	public static ItemType newInstance(String name) {
		String itemTypeJson = loadFileContentAsString("litp/item_types/" + name
				+ ".json");
		return parser.parseSingleItemType(itemTypeJson);
	}
}
