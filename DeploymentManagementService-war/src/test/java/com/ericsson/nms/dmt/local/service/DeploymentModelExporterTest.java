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

import static com.ericsson.nms.dmt.local.Node.builder;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

public class DeploymentModelExporterTest {

	private static Schema schema;
	private static DeploymentModelExporter exporter;

	@BeforeClass
	public static void setup() {
		schema = SchemaFactory.newInstance("xsd/litp.xsd");
		exporter = new DeploymentModelExporter();
	}

	@Test
	public void shouldExportNodeFromSingleNamespace() {
		// Given
		ComplexSchemaElement nodeSchema = schema.getDeclaredElement("ms")
				.asComplexElement();
		Node node = builder().id("ms1").actualSchema(nodeSchema)
				.declaringSchema(nodeSchema).build();

		// When
		String xml = exporter.exportToXml(node);

		// Then
		Element element = toElement(xml);
		assertThat(element.getName(), is("ms"));
		assertThat(element.getNamespacePrefix(), is("ns1"));
		assertThat(element.getNamespaceURI(), is(schema.getNamespace()));
		assertThat(element.getAttributeValue("id"), is("ms1"));
		assertThat(element.getChildren().size(), is(0));
	}

	@Test
	public void shouldExportNodeContainingSingleCollectionChild() {
		// Given
		ComplexSchemaElement nodeSchema = schema.getDeclaredElement("ms")
				.asComplexElement();
		ComplexSchemaElement childSchema = nodeSchema.getChild("ipaddresses")
				.asComplexElement();
		Node collectionChild = builder().id("ipaddresses")
				.actualSchema(childSchema).declaringSchema(childSchema).build();
		Node node = builder().id("ms1").actualSchema(nodeSchema)
				.declaringSchema(nodeSchema).child(collectionChild).build();

		// When
		String xml = exporter.exportToXml(node);

		// Then
		Element element = toElement(xml);
		assertThat(element.getName(), is("ms"));
		assertThat(element.getNamespacePrefix(), is("ns1"));
		assertThat(element.getNamespaceURI(), is(schema.getNamespace()));
		assertThat(element.getChildren().size(), is(1));

		Element childElement = element.getChildren().iterator().next();
		assertThat(childElement.getName(), is("ipaddresses"));
		assertThat(childElement.getNamespacePrefix(), is(EMPTY));
		assertThat(childElement.getAttributes().size(), is(0));
		assertThat(childElement.getChildren().size(), is(0));
	}

	@Test
	public void shouldExportNodeContainingSingleRegularChild() {
		// Given
		ComplexSchemaElement nodeSchema = schema.getDeclaredElement("blade")
				.asComplexElement();
		ComplexSchemaElement childSchema = schema.getDeclaredElement("bmc")
				.asComplexElement();
		Node regularChild = builder().id("bmc1").declaringSchema(childSchema)
				.actualSchema(childSchema).build();
		Node node = builder().id("blade1").declaringSchema(nodeSchema)
				.actualSchema(nodeSchema).child(regularChild).build();

		// When
		String xml = exporter.exportToXml(node);

		// Then
		Element element = toElement(xml);
		assertThat(element.getName(), is("blade"));
		assertThat(element.getNamespacePrefix(), is("ns1"));
		assertThat(element.getNamespaceURI(), is(schema.getNamespace()));
		assertThat(element.getChildren().size(), is(1));

		Element childElement = element.getChildren().iterator().next();
		assertThat(childElement.getName(), is("bmc"));
		assertThat(childElement.getNamespacePrefix(), is("ns1"));
		assertThat(childElement.getAttributeValue("id"), is("bmc1"));
		assertThat(childElement.getChildren().size(), is(0));
	}

	@Test
	public void shouldExportNodeContainingSingleProperties() {
		// Given
		ComplexSchemaElement nodeSchema = schema.getDeclaredElement("ms")
				.asComplexElement();
		Node node = builder().id("ms1").actualSchema(nodeSchema)
				.declaringSchema(nodeSchema).property("hostname", "ms1")
				.build();

		// When
		String xml = exporter.exportToXml(node);

		// Then
		Element element = toElement(xml);
		Element childElement = element.getChildren().iterator().next();
		assertThat(childElement.getName(), is("hostname"));
		assertThat(childElement.getNamespacePrefix(), is(EMPTY));
		assertThat(childElement.getText(), is("ms1"));
	}

	@Test
	public void shouldExportAndSortMultipleNodesAndPropertiesAccordingToSchemaOrder() {
		// Given
		ComplexSchemaElement nodeSchema = schema.getDeclaredElement("blade")
				.asComplexElement();
		ComplexSchemaElement bmcSchema = schema.getDeclaredElement("bmc")
				.asComplexElement();
		ComplexSchemaElement disksSchema = nodeSchema.getChild("disks")
				.asComplexElement();
		ComplexSchemaElement netInterfSchema = nodeSchema.getChild(
				"network_interfaces").asComplexElement();

		Node bmcNode = builder().id("bmc1").declaringSchema(bmcSchema)
				.actualSchema(bmcSchema).build();
		Node disksNode = builder().id("disks").declaringSchema(disksSchema)
				.actualSchema(disksSchema).build();
		Node netInterfNode = builder().id("network_interfaces")
				.declaringSchema(netInterfSchema).actualSchema(netInterfSchema)
				.build();

		Node node = builder().id("blade1").declaringSchema(nodeSchema)
				.actualSchema(nodeSchema).property("system_name", "abcdef")
				.property("serial", "123456").child(bmcNode)
				.child(netInterfNode).child(disksNode).build();

		// When
		String xml = exporter.exportToXml(node);

		// Then
		Element element = toElement(xml);

		assertThat(element.getChildren().get(0).getName(), is("serial"));
		assertThat(element.getChildren().get(1).getName(), is("system_name"));
		assertThat(element.getChildren().get(2).getName(), is("disks"));
		assertThat(element.getChildren().get(3).getName(),
				is("network_interfaces"));
		assertThat(element.getChildren().get(4).getName(), is("bmc"));
	}

	private Element toElement(String xml) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			return document.getRootElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
