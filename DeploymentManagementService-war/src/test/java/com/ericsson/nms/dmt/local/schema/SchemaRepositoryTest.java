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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.local.schema.data.SchemaElement;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

@RunWith(MockitoJUnitRunner.class)
public class SchemaRepositoryTest {

	private static SchemaRepository schemaRepository = new SchemaRepository();

	@BeforeClass
	public static void setup() {
		schemaRepository.put(SchemaFactory.newInstance("xsd/litp.xsd"));
	}

	@Test
	public void shouldFindCachedObjectByNamespaceAndName() {
		// When
		SchemaElement schemaElement = schemaRepository.get("ms",
				"http://www.ericsson.com/litp");
		// Then
		assertThat(schemaElement.getName(), is("ms"));
	}

	@Test
	public void shouldNotFindCachedObjectWhenNamespaceIsWrong() {
		// When
		SchemaElement schemaElement = schemaRepository.get("ms",
				"http://www.abcd.com/test");
		// Then
		assertThat(schemaElement, is(nullValue()));
	}

	@Test
	public void shouldFindCachedObjectByName() {
		// When
		SchemaElement schemaElement = schemaRepository.get("cluster");
		// Then
		assertThat(schemaElement.getName(), is("cluster"));
	}
}