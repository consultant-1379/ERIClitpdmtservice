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

import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.live.Node;

@RunWith(MockitoJUnitRunner.class)
public class NodeBodyReaderTest {

	private final NodeBodyReader nodeBodyReader = new NodeBodyReader();

	@Before
	public void setup() {
		nodeBodyReader.litpBaseUri = "https://ms1:9999/litp/rest/v1";
	}

	@Test
	public void shouldConsiderNodeClassAsReadable() {
		// When
		boolean readable = nodeBodyReader.isReadable(Node.class, null, null,
				null);

		// Then
		assertThat(readable, is(true));
	}

	@Test
	public void shouldNotConsiderClassesDifferentThanNodeAsReadable() {
		// When
		boolean readable = nodeBodyReader.isReadable(Object.class, null, null,
				null);

		// Then
		assertThat(readable, is(false));
	}

	@Test
	public void shouldReadFromJsonAndReturnANode()
			throws WebApplicationException, IOException {
		// Given
		InputStream json = loadFileContent("litp/nodes/ms.json");

		// When
		Node node = nodeBodyReader.readFrom(null, null, null, null, null, json);

		assertThat(node, is(notNullValue()));
	}
}
