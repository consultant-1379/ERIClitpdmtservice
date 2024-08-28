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

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.service.DeploymentModelBuilder;

public class LocalNodeFactory {

	private static SchemaRepository schemaRepository = new SchemaRepository();

	static {
		schemaRepository.put(SchemaFactory.newInstance("xsd/litp.xsd"));
	}

	public static Node newInstance(String name) {
		String nodeXml = loadFileContentAsString("xmls/" + name + ".xml");
		return new DeploymentModelBuilder(schemaRepository).fromXml(nodeXml);
	}
}
