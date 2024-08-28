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
package com.ericsson.nms.dmt.local.schema.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SimpleSchemaElementTest {

	private final SchemaElement schemaElement = SchemaElement.simpleBuilder()
			.build();

	@Test
	public void shouldReturnSimpleSchemaElement() {
		assertThat(schemaElement.isSimpleElement(), is(true));
	}

	@Test
	public void shouldReturnComplexSchemaElement() {
		assertThat(schemaElement.isComplexElement(), is(false));
	}

}
