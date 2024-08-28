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
package com.ericsson.nms.dmt.local.schema.parsing;

import static com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType.PATTERN;
import static com.ericsson.nms.dmt.test.util.ResourceLoader.getFileUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.ericsson.nms.dmt.local.schema.data.*;

@RunWith(MockitoJUnitRunner.class)
public class XsdParserTest {

	@Mock
	private Logger logger;

	@InjectMocks
	private XsdParser xsdParser;

	@Test
	public void shouldParseXsdWithSimpleTypeElement() {
		// Given
		URL xsdUrl = getFileUrl("xsds/simple_type_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		assertThat(schemas.size(), is(1));
		Schema schema = schemas.iterator().next();
		assertThat(schema.getNamespace(),
				is("http://www.ericsson.com/dmt/simple_type_element"));
		assertThat(schema.getDeclaredElements().size(), is(1));
		SchemaElement element = schema.getDeclaredElements().iterator().next();
		assertThat(element.getName(), is("color"));
		assertThat(element.getType(), is("string"));
		assertThat(element.asSimpleElement().getDefaultValue(), is("blue"));
	}

	@Test
	public void shouldParseXsdWithComplexTypeElement() {
		// Given
		URL xsdUrl = getFileUrl("xsds/complex_type_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		ComplexSchemaElement element = schema.getDeclaredElements().iterator()
				.next().asComplexElement();

		assertThat(schema.getDeclaredElements().size(), is(1));
		assertThat(element.getName(), is("person"));
		assertThat(element.getType(), is("person_type"));
		assertThat(element.getChildren().size(), is(2));
		Iterator<SchemaElement> childrenIterator = element.getChildren()
				.iterator();
		SchemaElement firstChild = childrenIterator.next();
		assertThat(firstChild.getName(), is("firstName"));
		assertThat(firstChild.getType(), is("string"));
		SchemaElement secondChild = childrenIterator.next();
		assertThat(secondChild.getName(), is("age"));
		assertThat(secondChild.getType(), is("integer"));
	}

	@Test
	public void shouldParseXsdElementWithAnnotation() {
		// Given
		URL xsdUrl = getFileUrl("xsds/annotation_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		SchemaElement element = schema.getDeclaredElements().iterator().next();
		assertThat(element.getDescription(), is("Test annotation."));
	}

	@Test
	public void shouldParseXsdWithMultipleDeclaredElement() {
		// Given
		URL xsdUrl = getFileUrl("xsds/multiple_declared_elements.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		assertThat(schema.getDeclaredElements().size(), is(2));
	}

	@Test
	public void shouldParseXsdElementWithMinAnMaxOccurs() {
		// Given
		URL xsdUrl = getFileUrl("xsds/min_and_max_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		SchemaElement element = schema.getDeclaredElements().iterator().next();
		Iterator<SchemaElement> childrenIterator = element.asComplexElement()
				.getChildren().iterator();
		SchemaElement firstChild = childrenIterator.next();
		assertThat(firstChild.getMinOccurs(), is(0));
		assertThat(firstChild.getMaxOccurs(), is(-1));

	}

	@Test
	public void shouldParseXsdElementWithBaseType() {
		// Given
		URL xsdUrl = getFileUrl("xsds/base_type_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		SchemaElement element = schema.getDeclaredElements().iterator().next();

		assertThat(element.getBaseType(), is("animal_type"));

	}

	@Test
	public void shouldParseXsdElementWithAttributes() {
		// Given
		URL xsdUrl = getFileUrl("xsds/attribute_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		ComplexSchemaElement element = schema.getDeclaredElements().iterator()
				.next().asComplexElement();

		assertThat(element.getAttribute("id").getDefaultValue(),
				is(nullValue()));
		assertThat(element.getAttribute("id").isRequired(), is(true));
		assertThat(element.getAttribute("pps").getDefaultValue(), is("999"));
		assertThat(element.getAttribute("pps").isRequired(), is(false));
		assertThat(element.getAttribute("gender").getFixedValue(), is("male"));
		assertThat(element.getAttribute("gender").isRequired(), is(false));
	}

	@Test
	public void shouldParseXsdElementWithAttributeRestrictions() {
		// Given
		URL xsdUrl = getFileUrl("xsds/attribute_restrictions.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		ComplexSchemaElement element = schema.getDeclaredElements().iterator()
				.next().asComplexElement();
		SchemaAttribute attribute = element.getAttribute("gender");
		assertThat(attribute.getRestrictions().size(), is(1));
		SimpleTypeRestriction restriction = attribute.getRestrictions().get(0);
		assertThat(restriction.getType(), is(PATTERN));
		assertThat(restriction.getConstraints().size(), is(1));
		assertThat(restriction.getConstraints().iterator().next(),
				is("^(Male|Female)$"));

	}

	@Test
	public void shouldParseXsdWithElementRestrictions() {
		// Given
		URL xsdUrl = getFileUrl("xsds/restriction_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		SimpleSchemaElement element = schema.getDeclaredElements().iterator()
				.next().asSimpleElement();
		Set<SimpleTypeRestriction> restrictions = element.getRestrictions();
		assertThat(restrictions.size(), is(1));
		SimpleTypeRestriction restriction = restrictions.iterator().next();
		assertThat(restriction.getType(), is(PATTERN));
		assertThat(restriction.getConstraints().size(), is(1));
		assertThat(restriction.getConstraints().iterator().next(),
				is("^((red)|(green)|(blue))$"));
	}

	@Test
	public void shouldParseXsdElementWithSubstitutables() {
		// Given
		URL xsdUrl = getFileUrl("xsds/substitutable_element.xsd");

		// When
		Set<Schema> schemas = xsdParser.parse(xsdUrl);

		// Then
		Schema schema = schemas.iterator().next();
		SchemaElement element = schema.getDeclaredElements().iterator().next();
		assertThat(element.hasSubstitutables(), is(true));
		assertThat(element.getSubstitutables().iterator().next(), is("navn"));
		assertThat(element.canBeSubstitutedBy("navn"), is(true));
	}
}
