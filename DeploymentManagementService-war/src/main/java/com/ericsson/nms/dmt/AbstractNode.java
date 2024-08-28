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
package com.ericsson.nms.dmt;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.codehaus.jackson.annotate.JsonMethod.NONE;

import java.util.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import com.ericsson.nms.dmt.rest.hateoas.LinkableResource;

/**
 * Base class for a node (or item) in the deployment model. It provides all the
 * information about a node, including its properties (keys/values) as well as
 * its children, which are other nodes.
 */
@JsonAutoDetect(NONE)
public abstract class AbstractNode extends LinkableResource {

	protected String id;
	protected final Map<String, String> properties = new LinkedHashMap<>();
	protected final Map<String, AbstractNode> children = new LinkedHashMap<>();

	/**
	 * Identifies a node in the deployment model
	 * 
	 * @return
	 */
	@JsonProperty
	public String getId() {
		return id;
	}

	/**
	 * Returns the full path of a node in the deployment model
	 * 
	 * @return
	 */
	@JsonProperty
	public abstract NodePath getPath();

	/**
	 * Returns the item type name of the node
	 * 
	 * @return
	 */
	@JsonProperty
	public abstract String getType();

	/**
	 * Returns the current state of the node
	 * 
	 * @return
	 */
	@JsonProperty
	public abstract String getState();

	/**
	 * Returns the category of the node
	 * 
	 * @return
	 */
	public abstract Category getCategory();

	/**
	 * Returns the properties of the node
	 * 
	 * @return
	 */
	@JsonProperty
	public Map<String, String> getProperties() {
		return unmodifiableMap(properties);
	}

	/**
	 * Verifies whether the node contains the specified property
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	/**
	 * Returns the value associated with the specified property name
	 * 
	 * @param name
	 * @return
	 */
	public String getPropertyValue(String name) {
		return properties.get(name);
	}

	/**
	 * Returns the node's children
	 * 
	 * @return
	 */
	public List<? extends AbstractNode> getChildren() {
		return unmodifiableList(new ArrayList<>(children.values()));
	}

	/**
	 * Verifies whether a node contains children
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * Returns the number of children the node contains
	 * 
	 * @return
	 */
	public int getNumberOfChildren() {
		return children.size();
	}

	/**
	 * Indicates whether the node is a collection of nodes
	 * 
	 * @return
	 */
	public boolean isCollection() {
		return getCategory().isCollection();
	}

	/**
	 * Indicates whether the node is a reference to another node
	 * 
	 * @return
	 */
	public boolean isReference() {
		return getCategory().isReference();
	}

	/**
	 * Indicates whether the node is a regular node, which means that it is not
	 * a collection nor a reference
	 * 
	 * @return
	 */
	public boolean isRegular() {
		return !isReference() && !isCollection();
	}
}