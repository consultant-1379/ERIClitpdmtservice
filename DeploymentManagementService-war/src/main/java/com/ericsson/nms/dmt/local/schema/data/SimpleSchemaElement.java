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

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.*;

import com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType;

/**
 * A simple element is an XML element that contains only text. It cannot contain
 * any other elements or attributes.
 * 
 * @see SchemaElement
 */
public class SimpleSchemaElement extends SchemaElement {

	private String defaultValue;
	private Map<RestrictionType, SimpleTypeRestriction> restrictions;

	private SimpleSchemaElement() {
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean hasDefaultValue() {
		return isNotBlank(defaultValue);
	}

	/**
	 * Returns a list of restrictions (or facets) that match the given
	 * restriction type
	 * 
	 * @param type
	 *            - restriction type used to filter
	 * @return
	 */
	public SimpleTypeRestriction getRestriction(RestrictionType type) {
		return restrictions.get(type);
	}

	/**
	 * Returns a list of restrictions (or facets)
	 * 
	 * @return
	 */
	public Set<SimpleTypeRestriction> getRestrictions() {
		return unmodifiableSet(newHashSet(restrictions.values()));
	}

	/**
	 * Informs whether the schema attribute has restrictions
	 * 
	 * @return
	 */
	public boolean hasRestrictions() {
		return !restrictions.isEmpty();
	}

	/**
	 * Informs whether the schema attribute has restrictions of the given type
	 * 
	 * @param type
	 *            - restriction type used to filter
	 * @return
	 */
	public boolean hasRestriction(RestrictionType type) {
		return restrictions.containsKey(type);
	}

	@Override
	public boolean isSimpleElement() {
		return true;
	}

	/**
	 * Builder class used to create instances of {@link SimpleSchemaElement}
	 * objects
	 */
	public static class SimpleSchemaElementBuilder extends SchemaElementBuilder {

		private String defaultValue;
		private final Map<RestrictionType, SimpleTypeRestriction> restrictions = new HashMap<>();

		public SimpleSchemaElementBuilder restriction(
				SimpleTypeRestriction restriction) {
			this.restrictions.put(restriction.getType(), restriction);
			return this;
		}

		public SimpleSchemaElementBuilder restrictions(
				Collection<SimpleTypeRestriction> restrictions) {
			for (SimpleTypeRestriction restriction : restrictions) {
				restriction(restriction);
			}
			return this;
		}

		public SimpleSchemaElementBuilder defaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}

		@Override
		public SchemaElement build() {
			SimpleSchemaElement element = new SimpleSchemaElement();
			element.name = this.name;
			element.type = this.type;
			element.baseType = this.baseType;
			element.description = this.description;
			element.namespace = this.namespace;
			element.global = this.global;
			element.minOccurs = this.minOccurs;
			element.maxOccurs = this.maxOccurs;
			element.substitutables = this.substitutables;
			element.defaultValue = this.defaultValue;
			element.restrictions = this.restrictions;
			return element;
		}
	}
}
