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

import static org.mockito.Mockito.mock;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.schema.parsing.XsdParser;
import com.ericsson.nms.dmt.test.util.ResourceLoader;

public class SchemaFactory {

	private static XsdParser xsdParser = new XsdParser(mock(Logger.class));

	public static Schema newInstance(String xsdPath) {
		return xsdParser.parse(ResourceLoader.getFileUrl(xsdPath)).iterator()
				.next();
	}
}
