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
package com.ericsson.nms.dmt.local.service;

import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContentAsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ModelParsingException;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

public class DeploymentModelBuilderTest {

	private static Schema schema;

	private static DeploymentModelBuilder builder;

	@BeforeClass
	public static void setup() {
		schema = SchemaFactory.newInstance("xsd/litp.xsd");

		SchemaRepository schemaRepository = mock(SchemaRepository.class);
		when(schemaRepository.get("root")).thenReturn(
				schema.getDeclaredElement("root"));
		when(schemaRepository.get("bmc")).thenReturn(
				schema.getDeclaredElement("bmc"));
		when(schemaRepository.get("blade")).thenReturn(
				schema.getDeclaredElement("blade"));
		when(schemaRepository.get("ms")).thenReturn(
				schema.getDeclaredElement("ms"));

		builder = new DeploymentModelBuilder(schemaRepository);
	}

	@Test
	public void shouldBuildFromDocumentWithOnlyRootElement() {
		// Given
		String xml = loadFileContentAsString("xmls/empty_root.xml");

		// When
		Node rootNode = builder.fromXml(xml);

		// Then
		assertThat(rootNode.getId(), is("root"));
		assertThat(rootNode.getDeclaringSchema().getName(), is("root"));
		assertThat(rootNode.getActualSchema().getName(), is("root"));
		assertThat(rootNode.getChildren().size(), is(0));
		assertThat(rootNode.getProperties().size(), is(0));
	}

	@Test
	public void shouldBuildFromDocumentWithSinglePropertyChild() {
		// Given
		String xml = loadFileContentAsString("xmls/single_property_child.xml");

		// When
		Node rootNode = builder.fromXml(xml);

		// Then
		assertThat(rootNode.getChildren().size(), is(0));
		assertThat(rootNode.getProperties().size(), is(1));
		assertThat(rootNode.getProperties().keySet().iterator().next(),
				is("hostname"));
		assertThat(rootNode.getProperties().values().iterator().next(),
				is("mshostname"));
	}

	@Test
	public void shouldBuildFromDocumentWithSingleRegularChild() {
		// Given
		String xml = loadFileContentAsString("xmls/single_regular_child.xml");

		// When
		Node rootNode = builder.fromXml(xml);

		// Then
		assertThat(rootNode.getChildren().size(), is(1));
		assertThat(rootNode.getChildren().get(0).getId(), is("infrastructure"));
		assertThat(
				rootNode.getChildren().get(0).getDeclaringSchema().getName(),
				is("infrastructure"));
		assertThat(rootNode.getChildren().get(0).getActualSchema().getName(),
				is("infrastructure"));
	}

	@Test
	public void shouldBuildFromDocumentWithSingleRegularChildThatHasExtensionType() {
		// Given
		String xml = loadFileContentAsString("xmls/single_regular_child_with_extension.xml");

		// When
		Node rootNode = builder.fromXml(xml);

		// Then
		assertThat(rootNode.getChildren().size(), is(1));
		assertThat(rootNode.getChildren().get(0).getId(), is("bmc"));
		assertThat(
				rootNode.getChildren().get(0).getDeclaringSchema().getName(),
				is("bmc-base"));
		assertThat(rootNode.getChildren().get(0).getActualSchema().getName(),
				is("bmc"));
	}

	@Test
	public void shouldBuildFromDocumentWithCollectionChild() {
		// Given
		String xml = loadFileContentAsString("xmls/collection_node_with_child.xml");

		// When
		Node rootNode = builder.fromXml(xml);

		// Then
		assertThat(rootNode.getChildren().size(), is(1));
		assertThat(rootNode.getChildren().get(0).getId(), is("deployments"));
		assertThat(
				rootNode.getChildren().get(0).getDeclaringSchema().getName(),
				is("deployments"));
		assertThat(rootNode.getChildren().get(0).getActualSchema().getName(),
				is("deployments"));
		assertThat(rootNode.getChildren().get(0).getChildren().size(), is(1));
	}

	@Test(expected = ModelParsingException.class)
	public void shouldThrowExceptionIfDocumentHasInvalidElement() {
		// Given
		String xml = loadFileContentAsString("xmls/invalid_node.xml");

		// When
		builder.fromXml(xml);
	}
}
