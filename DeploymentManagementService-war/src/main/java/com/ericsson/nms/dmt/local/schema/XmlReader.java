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
package com.ericsson.nms.dmt.local.schema;

import java.io.IOException;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Utility class that is used to read and validate the structure of XML files
 */
class XmlReader {

	public String load(URL xmlFile) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(xmlFile);
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());
		return output.outputString(document);
	}
}