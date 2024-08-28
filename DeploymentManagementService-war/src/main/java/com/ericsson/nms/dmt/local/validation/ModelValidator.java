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
package com.ericsson.nms.dmt.local.validation;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.ericsson.nms.dmt.local.Node;

/**
 * This class is used to perform business validations on a specific node in the
 * deployment model, which is being subject to a change, and guarantee its
 * consistency. This class provides a set of static methods that, if chained,
 * can be read as a domain language.
 */
public class ModelValidator {

	private ModelValidator() {
	}

	/**
	 * Validates a specific operation being performed on a node in the
	 * deployment model. It executes all validation rules that apply to the
	 * operation being performed, unless any one of them fails during the
	 * validation process.
	 * 
	 * @param operation
	 *            - operation being performed
	 */
	public static void validate(NodeOperation operation) {
		for (ValidationRule rule : operation.getRules()) {
			rule.validate();
		}
	}

	/**
	 * Factory method to create a {@link NodeCreation} operation
	 * 
	 * @return
	 */
	public static NodeCreation creation() {
		return new NodeCreation();
	}

	/**
	 * Factory method to create a {@link NodeRemoval} operation
	 * 
	 * @return
	 */
	public static NodeRemoval removal() {
		return new NodeRemoval();
	}

	/**
	 * Factory method to create a {@link NodeUpdate} operation
	 * 
	 * @return
	 */
	public static NodeUpdate update() {
		return new NodeUpdate();
	}

	/**
	 * Defines the methods that the operations must provide
	 */
	public interface NodeOperation {

		/**
		 * Returns a list contained all the validation rules that must be
		 * applied to validate the operation being performed. The rules are
		 * executed in the same order as it is provided.
		 * 
		 * @return list of validation rules
		 */
		List<? extends ValidationRule> getRules();
	}

	/**
	 * Represents the operation of creating a new node in the deployment model.
	 */
	public static class NodeCreation implements NodeOperation {

		private Node parentNode;
		private Node childNode;

		private NodeCreation() {
		}

		/**
		 * Specifies the parent node where the node is being created
		 * 
		 * @param parentNode
		 *            - parent node
		 * @return
		 */
		public NodeCreation on(Node parentNode) {
			this.parentNode = parentNode;
			return this;
		}

		/**
		 * Specifies the node being created
		 * 
		 * @param childNode
		 *            - child node
		 * @return
		 */
		public NodeCreation of(Node childNode) {
			this.childNode = childNode;
			return this;
		}

		@Override
		public List<? extends ValidationRule> getRules() {
			return unmodifiableList(asList(new UniqueNodeChildIdRule(
					parentNode, childNode), new ExistingRegularNodeRule(
					parentNode, childNode), new CollectionNodeCardinalityRule(
					parentNode), new ValidNodeIdValueRule(childNode),
					new ValidNodePropertiesRule(childNode)));
		}
	}

	/**
	 * Represents the operation of removing a existing node from the deployment
	 * model.
	 */
	public static class NodeRemoval implements NodeOperation {

		private Node node;

		private NodeRemoval() {
		}

		/**
		 * Specifies the node being removed
		 * 
		 * @param node
		 * 
		 * @return
		 */
		public NodeRemoval of(Node node) {
			this.node = node;
			return this;
		}

		@Override
		public List<? extends ValidationRule> getRules() {
			return unmodifiableList(asList(new DeletableNodeRule(node)));
		}
	}

	/**
	 * Represents the operation of updating a existing node in the deployment
	 * model.
	 */
	public static class NodeUpdate implements NodeOperation {

		private Node node;

		private NodeUpdate() {
		}

		/**
		 * Specifies the node being updated
		 * 
		 * @param node
		 * 
		 * @return
		 */
		public NodeUpdate of(Node node) {
			this.node = node;
			return this;
		}

		@Override
		public List<? extends ValidationRule> getRules() {
			return unmodifiableList(asList(new UpdatableNodeRule(node),
					new ValidNodePropertiesRule(node)));
		}
	}
}
