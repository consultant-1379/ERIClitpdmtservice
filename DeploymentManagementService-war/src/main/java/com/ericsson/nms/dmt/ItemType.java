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
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.codehaus.jackson.annotate.JsonMethod.NONE;

import java.util.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import com.ericsson.nms.dmt.rest.hateoas.LinkableResource;

/**
 * Class that models an item type. An item type describes all the properties and
 * children (sub-nodes) that a certain node in the deployment model can have.
 */
@JsonAutoDetect(NONE)
public class ItemType extends LinkableResource {

	private String name;
	private String description;
	private String baseType;
	private final Map<String, ItemTypeChild> children = new LinkedHashMap<>();
	private final List<ItemTypeProperty> properties = new ArrayList<>();

	@JsonProperty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public boolean hasBaseType() {
		return isNotBlank(baseType);
	}

	@JsonProperty
	public List<ItemTypeProperty> getProperties() {
		return unmodifiableList(properties);
	}

	public void addProperty(ItemTypeProperty property) {
		properties.add(property);
	}

	public List<ItemTypeChild> getChildren() {
		return unmodifiableList(new ArrayList<>(children.values()));
	}

	public ItemTypeChild getChild(String childType) {
		return children.get(childType);
	}

	public void addChild(ItemTypeChild child) {
		children.put(child.getName(), child);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemType other = (ItemType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}