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

import static com.ericsson.nms.dmt.local.schema.data.SchemaElement.complexBuilder;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ComplexSchemaElementTest {

	private ComplexSchemaElement element;

	@Before
	public void setup() {
		SchemaElement child1 = complexBuilder().name("bmc-base")
				.substitutables(newHashSet("bmc")).minOccurs(1).build();
		SchemaElement child2 = complexBuilder().name("disk").minOccurs(0)
				.build();
		SchemaElement child3 = complexBuilder().name("cluster").minOccurs(1)
				.build();

		element = complexBuilder().child(child1).child(child2).child(child3)
				.name("server").build().asComplexElement();
	}

	@Test
	public void shouldReturnSimpleSchemaElement() {
		assertThat(element.isSimpleElement(), is(false));
	}

	@Test
	public void shouldReturnComplexSchemaElement() {
		assertThat(element.isComplexElement(), is(true));
	}

	@Test
	public void shouldFindChildSubstitutableByExtensionType() {
		SchemaElement result = element.getChildThatCanBeSubstitutedBy("bmc");

		assertThat(result.getName(), is("bmc-base"));
	}

	@Test
	public void shouldNotFindSubstitutableChild() {
		SchemaElement result = element
				.getChildThatCanBeSubstitutedBy("cluster");

		assertThat(result, is(nullValue()));
	}

	@Test
	public void shouldReturnRequiredChildren() {
		Set<SchemaElement> requiredChildren = element.getRequiredChildren();

		assertThat(requiredChildren.size(), is(2));
		assertThat(requiredChildren.contains(element.getChild("bmc-base")),
				is(true));
		assertThat(requiredChildren.contains(element.getChild("cluster")),
				is(true));
	}

	@Test
	public void shouldReturnChildOrder() {
		assertThat(element.getChildOrder("bmc-base"), is(0));
		assertThat(element.getChildOrder("disk"), is(1));
		assertThat(element.getChildOrder("cluster"), is(2));
		assertThat(element.getChildOrder("os-profile"), is(-1));
	}

	@Test
	public void shouldConsiderAsRegularElement() {
		assertThat(element.isRegularElement(), is(true));
		assertThat(element.isCollectionContainerElement(), is(false));
		assertThat(element.isReferenceElement(), is(false));
	}

	@Test
	public void shouldConsiderAsCollectionElement() {
		ComplexSchemaElement collectionElement = complexBuilder()
				.name("deployments").baseType("basecollection-type").build()
				.asComplexElement();

		assertThat(collectionElement.isCollectionContainerElement(), is(true));
		assertThat(collectionElement.isRegularElement(), is(false));
		assertThat(collectionElement.isReferenceElement(), is(false));
	}

	@Test
	public void shouldConsiderAsReferenceElement() {
		ComplexSchemaElement referenceElement = complexBuilder()
				.name("os-profile-link").build().asComplexElement();

		assertThat(referenceElement.isReferenceElement(), is(true));
		assertThat(referenceElement.isCollectionContainerElement(), is(false));
		assertThat(referenceElement.isRegularElement(), is(false));
	}

	@Test
	public void shouldReturnReferencedElementName() {
		ComplexSchemaElement referenceElement = complexBuilder()
				.name("os-profile-link").build().asComplexElement();

		assertThat(referenceElement.getReferencedElementName(),
				is("os-profile"));
	}
}
