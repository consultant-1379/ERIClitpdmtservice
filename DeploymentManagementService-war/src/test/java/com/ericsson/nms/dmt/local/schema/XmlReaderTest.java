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

import static com.ericsson.nms.dmt.test.util.ResourceLoader.getFileUrl;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;

import org.jdom2.JDOMException;
import org.junit.Test;

public class XmlReaderTest {

	private final XmlReader xmlReader = new XmlReader();

	@Test
	public void shouldLoadXmlFileAndReturnItsContentAsAString()
			throws JDOMException, IOException {
		// given
		URL xmlUrl = getFileUrl("xmls/blade.xml");

		// when
		String xmlContent = xmlReader.load(xmlUrl);

		// then
		assertThat(xmlContent, is(notNullValue()));
		assertThat(xmlContent.contains("<macaddress>macaddress</macaddress>"),
				is(true));

	}

}
