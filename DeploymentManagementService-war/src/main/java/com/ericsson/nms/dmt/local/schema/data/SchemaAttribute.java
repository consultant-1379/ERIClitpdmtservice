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
package com.ericsson.nms.dmt.local.schema.data;

import java.util.*;

import com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType;

/**
 * Represents an attribute of a {@link ComplexSchemaElement}. Attributes provide
 * additional information about an element. Simple elements cannot have
 * attributes.
 */
public class SchemaAttribute {

	private String name;
	private String type;
	private String defaultValue;
	private String fixedValue;
	private boolean required;
	private final List<SimpleTypeRestriction> restrictions = new ArrayList<>();

	private SchemaAttribute() {
	}

	/**
	 * Builder method to create instances of {@link SchemaAttribute} objects
	 * 
	 * @return
	 */
	public static Builder builder() {
		return new Builder();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getFixedValue() {
		return fixedValue;
	}

	public boolean isRequired() {
		return required;
	}

	/**
	 * Returns a list of restrictions (or facets) that match the given
	 * restriction type
	 * 
	 * @param type
	 *            - restriction type used to filter
	 * @return
	 */
	public List<SimpleTypeRestriction> getRestrictions(RestrictionType type) {
		List<SimpleTypeRestriction> result = new ArrayList<>();
		for (SimpleTypeRestriction restriction : restrictions) {
			if (restriction.getType() == type) {
				result.add(restriction);
			}
		}
		return result;
	}

	/**
	 * Returns a list of restrictions (or facets)
	 * 
	 * @return
	 */
	public List<SimpleTypeRestriction> getRestrictions() {
		return restrictions;
	}

	/**
	 * Informs whether the schema attribute has restrictions
	 * 
	 * @return
	 */
	public boolean hasRestrictions() {
		return !restrictions.isEmpty();
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
		SchemaAttribute other = (SchemaAttribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Builder class used to create instances of {@link SchemaAttribute} objects
	 */
	public static class Builder {

		private final SchemaAttribute instance;

		private Builder() {
			instance = new SchemaAttribute();
		}

		public Builder name(String name) {
			instance.name = name;
			return this;
		}

		public Builder type(String type) {
			instance.type = type;
			return this;
		}

		public Builder defaultValue(String defaultValue) {
			instance.defaultValue = defaultValue;
			return this;
		}

		public Builder fixedValue(String fixedValue) {
			instance.fixedValue = fixedValue;
			return this;
		}

		public Builder required(boolean required) {
			instance.required = required;
			return this;
		}

		public Builder restrictions(
				Collection<SimpleTypeRestriction> restrictions) {
			instance.restrictions.addAll(restrictions);
			return this;
		}

		public Builder restriction(SimpleTypeRestriction restriction) {
			instance.restrictions.add(restriction);
			return this;
		}

		public SchemaAttribute build() {
			return instance;
		}
	}
}
