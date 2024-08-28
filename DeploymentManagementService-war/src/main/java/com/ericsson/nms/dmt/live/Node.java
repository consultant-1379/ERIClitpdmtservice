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
package com.ericsson.nms.dmt.live;

import java.util.List;

import com.ericsson.nms.dmt.*;

/**
 * Immutable class that represents a node in the LIVE deployment model (working
 * copy). It is basically a data container for storing temporarily node details
 * returned by the LITP service.
 */
public class Node extends AbstractNode {

	private NodePath path;
	private String type;
	private String state;
	private Category category;

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
		return path;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Node> getChildren() {
		return (List<Node>) super.getChildren();
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

		public Builder type(String type) {
			this.instance.type = type;
			return this;
		}

		public Builder path(NodePath path) {
			this.instance.path = path;
			return this;
		}

		public Builder state(String state) {
			this.instance.state = state;
			return this;
		}

		public Builder category(Category category) {
			this.instance.category = category;
			return this;
		}

		public Builder property(String name, String value) {
			this.instance.properties.put(name, value);
			return this;
		}

		public Builder child(Node node) {
			this.instance.children.put(node.getId(), node);
			return this;
		}

		public Node build() {
			return instance;
		}
	}
}
