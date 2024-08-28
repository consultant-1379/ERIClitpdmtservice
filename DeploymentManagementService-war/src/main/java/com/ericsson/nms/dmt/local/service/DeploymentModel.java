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

import static com.ericsson.nms.dmt.local.validation.ModelValidator.*;
import static java.lang.String.format;

import java.util.Iterator;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.ericsson.nms.dmt.NodeData;
import com.ericsson.nms.dmt.NodePath;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.*;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.*;

/**
 * Stateful object that represents a local deployment model during the lifespan
 * of a request to the application and allows internal components to perform
 * operations on it to fulfill the request. It uses the
 * {@link DeploymentModelBuilder} component to initialize itself from a XML
 * representation of the deployment model, which is persisted locally by the
 * application. After performing operations on it, the new state can be exported
 * back to XML. To accomplish that, it uses the {@link DeploymentModelExporter}
 * component.
 */
@RequestScoped
class DeploymentModel {

	private Node rootNode;

	@Inject
	private DeploymentModelBuilder modelBuilder;

	@Inject
	private DeploymentModelExporter modelExporter;

	@Inject
	private SchemaRepository schemaRepository;

	/**
	 * Initializes the object from a XML representation of the deployment model
	 * 
	 * @param modelDefinition
	 *            - XML serialization of the deployment model
	 */
	public void loadFromXml(String modelDefinition) {
		rootNode = modelBuilder.fromXml(modelDefinition);
	}

	/**
	 * Serializes the current state of the deployment model to XML format
	 * 
	 * @return
	 */
	public String exportToXml() {
		return modelExporter.exportToXml(rootNode);
	}

	/**
	 * Returns the node that can be found on the provide path
	 * 
	 * @param path
	 *            - path to the requested node
	 * @return
	 */
	public Node findNode(NodePath path) {
		return NodeFinder.find(path, rootNode);
	}

	/**
	 * Adds a new child node to the specified path.
	 * 
	 * @param path
	 *            - path to the parent node where the child should be added
	 * @param nodeData
	 *            - data to create the new node
	 */
	public void addNode(NodePath path, NodeData nodeData) {
		Node parent = findNode(path);

		ComplexSchemaElement actualSchema = getActualSchema(parent, nodeData);
		ComplexSchemaElement declaringSchema = getDeclaringSchema(parent,
				nodeData);
		Node child = createRegularNode(nodeData, declaringSchema, actualSchema);

		validate(creation().of(child).on(parent));

		parent.addChild(child);
	}

	private ComplexSchemaElement getActualSchema(Node parentNode,
			NodeData nodeData) {
		SchemaElement actualSchema = null;
		ComplexSchemaElement parentSchema = parentNode.getActualSchema();

		if (parentSchema.hasChild(nodeData.getType())) {
			actualSchema = parentSchema.getChild(nodeData.getType());
		} else {
			actualSchema = schemaRepository.get(nodeData.getType());
		}

		if (actualSchema == null || !actualSchema.isComplexElement()) {
			String message = format("Item type not registered: %s",
					nodeData.getType());
			throw new InvalidTypeException(message);
		}

		if (actualSchema.hasSubstitutables()) {
			String message = format(
					"'%s' is not an allowed type for child '%s'",
					nodeData.getType(), nodeData.getId());
			throw new InvalidChildTypeException(message);
		}

		return actualSchema.asComplexElement();
	}

	private ComplexSchemaElement getDeclaringSchema(Node parentNode,
			NodeData nodeData) {
		SchemaElement declaringSchema = null;
		ComplexSchemaElement parentSchema = parentNode.getActualSchema();

		if (parentSchema.hasChild(nodeData.getType())) {
			declaringSchema = parentSchema.getChild(nodeData.getType());
		} else {
			declaringSchema = parentSchema
					.getChildThatCanBeSubstitutedBy(nodeData.getType());

		}

		if (declaringSchema == null || !declaringSchema.isComplexElement()) {
			String message = format(
					"'%s' is not an allowed type for child '%s'",
					nodeData.getType(), nodeData.getId());
			throw new InvalidChildTypeException(message);
		}

		return declaringSchema.asComplexElement();
	}

	private Node createRegularNode(NodeData nodeData,
			ComplexSchemaElement declaringSchema,
			ComplexSchemaElement actualSchema) {
		Node node = Node.builder().id(nodeData.getId())
				.declaringSchema(declaringSchema).actualSchema(actualSchema)
				.build();
		appendPropertiesIncludingDefaultOnes(node, nodeData);
		appendCollectionSubNodes(node);
		return node;
	}

	private void appendPropertiesIncludingDefaultOnes(Node node,
			NodeData nodeData) {
		appendDefaultValuedProperties(node);
		appendProperties(node, nodeData);
	}

	private void appendProperties(Node node, NodeData nodeData) {
		Map<String, String> properties = nodeData.getProperties();
		for (String propertyName : properties.keySet()) {
			String propertyValue = properties.get(propertyName);
			if (propertyValue != null) {
				node.addProperty(propertyName, propertyValue);
			} else {
				node.removeProperty(propertyName);
			}
		}

	}

	private void appendDefaultValuedProperties(Node node) {
		ComplexSchemaElement nodeSchema = node.getActualSchema();
		for (SchemaElement childSchema : nodeSchema.getChildren()) {
			if (!childSchema.isSimpleElement()) {
				continue;
			}

			SimpleSchemaElement simpleChild = childSchema.asSimpleElement();
			if (simpleChild.hasDefaultValue()) {
				node.addProperty(simpleChild.getName(),
						simpleChild.getDefaultValue());
			}
		}
	}

	private void appendCollectionSubNodes(Node node) {
		for (SchemaElement childSchema : node.getActualSchema().getChildren()) {
			if (childSchema.isComplexElement()) {
				ComplexSchemaElement complexChild = childSchema
						.asComplexElement();
				if (complexChild.isCollectionContainerElement()) {
					Node child = Node.builder().id(childSchema.getName())
							.declaringSchema(complexChild)
							.actualSchema(complexChild).build();
					node.addChild(child);
				}
			}
		}
	}

	/**
	 * Updates the state of an existing node
	 * 
	 * @param path
	 *            - path to the node
	 * @param nodeData
	 *            - data to replace the current state
	 */
	public void updateNode(NodePath path, NodeData nodeData) {
		Node node = findNode(path);

		if (nodeData.getProperties().isEmpty()) {
			throw new InvalidRequestException(
					"Properties must be specified for update");
		}

		appendProperties(node, nodeData);
		validate(update().of(node));
	}

	/**
	 * Removes an existing node
	 * 
	 * @param path
	 *            - path to the node to be removed
	 */
	public void removeNode(NodePath path) {
		Node node = findNode(path);
		validate(removal().of(node));
		node.getParent().removeChild(node);
	}

	private static class NodeFinder {

		public static Node find(NodePath path, Node rootNode) {
			if (path.isRoot()) {
				return rootNode;
			}
			return findNode(path.elements().iterator(), rootNode);
		}

		private static Node findNode(Iterator<String> pathElements,
				Node currentNode) {
			String currentPathElement = pathElements.next();
			Node childNode = findNode(currentNode, currentPathElement);

			if (pathElements.hasNext()) {
				return findNode(pathElements, childNode);
			}

			return childNode;
		}

		private static Node findNode(Node parentNode, String id) {
			Node child = parentNode.getChildById(id);

			if (child == null) {
				throw new InvalidLocationException("Not found");
			}
			return child;
		}
	}
}