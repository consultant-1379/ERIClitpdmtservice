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
package com.ericsson.nms.dmt.local;

import static com.ericsson.nms.dmt.Category.*;

import java.util.List;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.service.DeploymentModelBuilder;
import com.ericsson.nms.dmt.local.service.DeploymentModelExporter;

/**
 * This class represents a node in a LOCAL deployment model (working copy).
 * Unlike its counterpart in the LIVE package, this class provides methods that
 * allow changes to be made upon itself. A tree of nodes can be created from a
 * deployment model XML file using the class {@link DeploymentModelBuilder} and
 * serialized back to XML using the class {@link DeploymentModelExporter}. As
 * the node objects are built following the constraints provided by the LITP XSD
 * files, each node instance has a reference to the {@link ComplexSchemaElement}
 * that is used to declare itself in the XSD as well as a reference to the
 * concrete type ( {@link ComplexSchemaElement}) used to create it in the XML
 * file. The references to the {@link ComplexSchemaElement} objects it carries
 * are very important to get information to be able to validate the consistency
 * of the deployment model, as well as to serialize it to XML.
 */
public class Node extends AbstractNode {

	private static final String STATE = "Local";

	private Node parent;
	private ComplexSchemaElement declaringSchema;
	private ComplexSchemaElement actualSchema;

	private Node() {
	}

	/**
	 * Builder method used to create instances of {@link Node}
	 * 
	 * @return
	 */
	public static Builder builder() {
		return new Builder();
	}

	@Override
	public NodePath getPath() {
		if (isRootNode()) {
			return NodePath.root();
		}
		return parent.getPath().append(id);
	}

	/**
	 * Indicates whether it is the root node in the tree
	 * 
	 * @return
	 */
	public boolean isRootNode() {
		return parent == null;
	}

	/**
	 * Returns a reference to its parent node
	 * 
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Returns the {@link ComplexSchemaElement} instance that was used to
	 * declare the corresponding node element in the XSD file. When the XSD
	 * element used to declare it in the XSD file is the same as the one used to
	 * create the actual element in the XML file, this method returns the same
	 * object as the 'getActualSchema' method does. But when the actual element
	 * created in the XML file is a substitutable element, the returning values
	 * from both methods are different.
	 * 
	 * @return - the {@link ComplexSchemaElement} used to declare its
	 *         corresponding element in the XSD file
	 */
	public ComplexSchemaElement getDeclaringSchema() {
		return declaringSchema;
	}

	/**
	 * Returns the {@link ComplexSchemaElement} instance that was used to create
	 * the corresponding node element in the XML file. When the XSD element used
	 * to declare it in the XSD file is the same as the one used to create the
	 * actual element in the XML file, this method returns the same object as
	 * the 'getDeclaringSchema' method does. But when the actual element created
	 * in the XML file is a substitutable element (see substitutionGroup), the
	 * returning values from both methods are different.
	 * 
	 * @return - the {@link ComplexSchemaElement} used to create its
	 *         corresponding element in the XML file
	 */
	public ComplexSchemaElement getActualSchema() {
		return actualSchema;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Node> getChildren() {
		return (List<Node>) super.getChildren();
	}

	/**
	 * Returns the child for the specified ID or null if it does not exist.
	 * 
	 * @param id
	 *            - child ID
	 * @return
	 */
	public Node getChildById(String id) {
		return (Node) children.get(id);
	}

	/**
	 * Add a new child to itself
	 * 
	 * @param node
	 *            - child node
	 */
	public void addChild(Node node) {
		node.parent = this;
		children.put(node.getId(), node);
	}

	/**
	 * Removes a child from itself
	 * 
	 * @param child
	 *            - child to be removed
	 */
	public void removeChild(Node child) {
		if (children.remove(child.getId()) != null) {
			child.parent = null;
		}
	}

	/**
	 * Adds a new property to itself
	 * 
	 * @param name
	 *            - property name
	 * @param value
	 *            - property value
	 */
	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	/**
	 * Removes a property from itself
	 * 
	 * @param name
	 *            - property name
	 */
	public void removeProperty(String name) {
		properties.remove(name);
	}

	@Override
	public String getState() {
		return STATE;
	}

	@Override
	public String getType() {
		if (declaringSchema.isCollectionContainerElement()) {
			ComplexSchemaElement collectionChild = declaringSchema
					.getFirstChild().asComplexElement();
			return collectionChild.isReferenceElement() ? collectionChild
					.getReferencedElementName() : collectionChild.getName();
		}

		return actualSchema.isReferenceElement() ? actualSchema
				.getReferencedElementName() : actualSchema.getName();

	}

	@Override
	public Category getCategory() {
		if (declaringSchema.isCollectionContainerElement()) {
			return getCollectionNodeCategory();
		}
		return getRegularNodeCategory();
	}

	private Category getRegularNodeCategory() {
		if (declaringSchema.isReferenceElement()) {
			return REFERENCE_ITEM;
		}
		return REGULAR_ITEM;
	}

	private Category getCollectionNodeCategory() {
		ComplexSchemaElement childSchema = declaringSchema.getFirstChild()
				.asComplexElement();

		if (childSchema.isReferenceElement()) {
			return REFERENCE_COLLECTION_ITEM;
		}
		return COLLECTION_ITEM;
	}

	/**
	 * Builder class used to create instances of {@link Node} objects
	 */
	public static class Builder {

		private final Node instance = new Node();

		public Builder id(String id) {
			this.instance.id = id;
			return this;
		}

		public Builder declaringSchema(ComplexSchemaElement declaringSchema) {
			this.instance.declaringSchema = declaringSchema;
			return this;
		}

		public Builder actualSchema(ComplexSchemaElement actualSchema) {
			this.instance.actualSchema = actualSchema;
			return this;
		}

		public Builder child(Node child) {
			this.instance.addChild(child);
			return this;
		}

		public Builder property(String name, String value) {
			this.instance.addProperty(name, value);
			return this;
		}

		public Node build() {
			return instance;
		}
	}
}
