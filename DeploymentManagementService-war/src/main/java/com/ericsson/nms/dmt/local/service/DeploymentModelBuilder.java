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
import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.IDENTIFIER_ATTRIBUTE;

import java.io.StringReader;

import javax.inject.Inject;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.Node.Builder;
import com.ericsson.nms.dmt.local.error.ModelParsingException;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;

/**
 * Builds a tree of {@link Node} objects from a deployment model descriptor in
 * XML format.
 */
public class DeploymentModelBuilder {

	private final SchemaRepository schemaRepository;

	/**
	 * Constructor that takes a reference to the schema repository from where it
	 * obtains the schema elements used to deserialize the XML document.
	 * 
	 * @param schemaRepository
	 *            - reference to the schema repository
	 */
	@Inject
	public DeploymentModelBuilder(SchemaRepository schemaRepository) {
		this.schemaRepository = schemaRepository;
	}

	/**
	 * Deserializes a deployment model descriptor from XML format to a tree of
	 * {@link Node} objects
	 * 
	 * @param xml
	 *            - XML document content
	 * @return
	 */
	public Node fromXml(String xml) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element rootElement = document.getRootElement();
			ComplexSchemaElement rootSchema = schemaRepository.get(
					rootElement.getName()).asComplexElement();
			return buildNode(rootElement, rootSchema, rootSchema);
		} catch (Exception e) {
			throw new ModelParsingException(
					"Unexpected error was found when parsing the deployment model definition",
					e);
		}
	}

	private Node buildNode(Element element,
			ComplexSchemaElement referencedSchema,
			ComplexSchemaElement actualSchema) {
		Builder nodeBuilder = builder()
				.id(makeNodeId(element, referencedSchema))
				.declaringSchema(referencedSchema).actualSchema(actualSchema);

		for (Element childElement : element.getChildren()) {
			String childElementName = childElement.getName();
			ComplexSchemaElement childReferencedSchema = null;
			ComplexSchemaElement childActualSchema = null;

			if (actualSchema.hasChild(childElementName)) {
				SchemaElement childSchema = actualSchema
						.getChild(childElementName);

				if (childSchema.isSimpleElement()) {
					nodeBuilder.property(childElement.getName(),
							childElement.getText());
					continue;
				}

				childReferencedSchema = childSchema.asComplexElement();
				childActualSchema = childSchema.asComplexElement();
			} else {
				childReferencedSchema = actualSchema
						.getChildThatCanBeSubstitutedBy(childElementName)
						.asComplexElement();
				childActualSchema = schemaRepository.get(childElementName)
						.asComplexElement();
			}

			if (childReferencedSchema == null || childActualSchema == null) {
				throw new UnexpectedException(
						String.format(
								"Deployment model definition contains an invalid element: %s",
								childElement.getName()));
			}

			nodeBuilder.child(buildNode(childElement, childReferencedSchema,
					childActualSchema));
		}

		return nodeBuilder.build();
	}

	// FIXME remove this method when collections have ID
	private String makeNodeId(Element element, ComplexSchemaElement schema) {
		return schema.isCollectionContainerElement() ? element.getName()
				: element.getAttributeValue(IDENTIFIER_ATTRIBUTE);
	}
}
