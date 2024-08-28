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

import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.IDENTIFIER_ATTRIBUTE;

import java.util.*;

import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ModelParsingException;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;

/**
 * Serializes a tree of {@link Node} objects into a XML representation of the
 * deployment model. The resulting XML deployment model descriptor can be
 * imported into a LITP CLI tool.
 */
public class DeploymentModelExporter {

	/**
	 * Builds a XML representation of a tree of {@link Node} objects which
	 * compose the deployment model
	 * 
	 * @param rootNode
	 *            - the root node in the deployment model
	 * @return
	 */
	public String exportToXml(Node rootNode) {
		return exportToXml(rootNode, new NamespacePrefixGenerator());
	}

	private String exportToXml(Node rootNode, NamespacePrefixGenerator prefixGen) {
		try {
			XMLOutputter output = new XMLOutputter();
			Document document = new Document();
			Element rootElement = buildElement(rootNode, prefixGen);
			document.setRootElement(configureNamespaces(rootElement, prefixGen));
			output.setFormat(Format.getPrettyFormat());
			return output.outputString(document);
		} catch (Exception e) {
			throw new ModelParsingException(
					"Failed to serialize the deployment model definition", e);
		}
	}

	private Element buildElement(Node node, NamespacePrefixGenerator prefixGen) {
		ComplexSchemaElement actualNodeSchema = node.getActualSchema();
		Element element = new Element(actualNodeSchema.getName());

		if (actualNodeSchema.isGlobal()) {
			String uri = actualNodeSchema.getNamespace();
			element.setNamespace(prefixGen.getNamespaceFor(uri));
		}

		// FIXME remove if clause when collections have ID as well
		if (!node.isCollection()) {
			element.setAttribute(new Attribute(IDENTIFIER_ATTRIBUTE, node
					.getId()));
		}

		for (String propertyName : node.getProperties().keySet()) {
			Element propertyElement = new Element(propertyName);
			propertyElement.setText(node.getPropertyValue(propertyName));
			element.addContent(propertyElement);
		}

		for (Node child : node.getChildren()) {
			element.addContent(buildElement(child, prefixGen));
		}

		return sortChildrenAccordingToSchemaOrder(element, actualNodeSchema);
	}

	private Element sortChildrenAccordingToSchemaOrder(Element element,
			final ComplexSchemaElement nodeSchema) {
		element.sortChildren(new Comparator<Element>() {
			@Override
			public int compare(Element element1, Element element2) {
				int element1Order = getChildElementOrder(element1.getName(),
						nodeSchema);
				int element2Order = getChildElementOrder(element2.getName(),
						nodeSchema);

				if (element1Order != element2Order) {
					return element1Order - element2Order;
				}

				return 0;
			}
		});

		return element;
	}

	private int getChildElementOrder(String childElementName,
			ComplexSchemaElement nodeSchema) {
		if (nodeSchema.hasChild(childElementName)) {
			return nodeSchema.getChildOrder(childElementName);
		}

		SchemaElement childThatAcceptsSubstitution = nodeSchema
				.getChildThatCanBeSubstitutedBy(childElementName);
		if (childThatAcceptsSubstitution != null) {
			return nodeSchema.getChildOrder(childThatAcceptsSubstitution
					.getName());
		}

		return -1;
	}

	private Element configureNamespaces(Element rootElement,
			NamespacePrefixGenerator prefixGen) {
		for (Namespace ns : prefixGen.getNamespaces()) {
			rootElement.addNamespaceDeclaration(ns);
		}
		return rootElement;
	}

	private static class NamespacePrefixGenerator {

		private static final String NAMESPACE_PREFIX = "ns";
		private final Map<String, Namespace> namespaces = new HashMap<>();

		public Namespace getNamespaceFor(String uri) {
			Namespace ns = namespaces.get(uri);

			if (ns == null) {
				String prefix = NAMESPACE_PREFIX + (namespaces.size() + 1);
				ns = Namespace.getNamespace(prefix, uri);
				namespaces.put(uri, ns);
			}
			return ns;
		}

		public Collection<Namespace> getNamespaces() {
			return namespaces.values();
		}
	}
}
