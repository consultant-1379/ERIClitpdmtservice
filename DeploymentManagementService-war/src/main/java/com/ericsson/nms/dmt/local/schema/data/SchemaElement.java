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

import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.MAX_OCCURS_UNBOUNDED;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement.ComplexSchemaElementBuilder;
import com.ericsson.nms.dmt.local.schema.data.SimpleSchemaElement.SimpleSchemaElementBuilder;

/**
 * This abstract class is the base class for schema element objects. It provides
 * only a small set of the features defined by the XML Schema specifications.
 * More complex structures, for instance <i>choice</i>, <i>any</i> and so on,
 * are not supported. There are two extensions of this class:
 * {@link ComplexSchemaElement}, which represents complex typed elements, and
 * {@link SimpleSchemaElement} that represents simple typed elements.
 */
public abstract class SchemaElement {

	protected String name;
	protected String type;
	protected String baseType;
	protected String description;
	protected String namespace;
	protected boolean global;
	protected int minOccurs = 1;
	protected int maxOccurs = 1;
	protected int order = -1;
	protected Set<String> substitutables;

	/**
	 * Default constructor
	 */
	protected SchemaElement() {
	}

	/**
	 * Builder method used to create {@link SimpleSchemaElement} objects
	 * 
	 * @return
	 */
	public static SimpleSchemaElementBuilder simpleBuilder() {
		return new SimpleSchemaElementBuilder();
	}

	/**
	 * Builder method used to create {@link ComplexSchemaElement} objects
	 * 
	 * @return
	 */
	public static ComplexSchemaElementBuilder complexBuilder() {
		return new ComplexSchemaElementBuilder();
	}

	/**
	 * Utility method to cast an {@link SchemaElement} to a
	 * {@link SimpleSchemaElement}
	 * 
	 * @return
	 */
	public SimpleSchemaElement asSimpleElement() {
		return SimpleSchemaElement.class.cast(this);
	}

	/**
	 * Utility method to cast an {@link SchemaElement} to a
	 * {@link ComplexSchemaElement}
	 * 
	 * @return
	 */
	public ComplexSchemaElement asComplexElement() {
		return ComplexSchemaElement.class.cast(this);
	}

	/**
	 * Informs whether it is a simple element (has simple type)
	 * 
	 * @return
	 */
	public boolean isSimpleElement() {
		return false;
	}

	/**
	 * Informs whether it is a complex element (has complex type)
	 * 
	 * @return
	 */
	public boolean isComplexElement() {
		return false;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getBaseType() {
		return baseType;
	}

	/**
	 * Returns a list of element names that can substitute the current element
	 * in a XML document
	 * 
	 * @return
	 */
	public Set<String> getSubstitutables() {
		return unmodifiableSet(substitutables);
	}

	/**
	 * Informs whether it can be substituted by other elements in a XML document
	 * 
	 * @return
	 */
	public boolean hasSubstitutables() {
		return !substitutables.isEmpty();
	}

	/**
	 * Informs whether it can be substituted by a given element name
	 * 
	 * @param elementName
	 *            - name of the element that could substitute the current one
	 * @return
	 */
	public boolean canBeSubstitutedBy(String elementName) {
		return substitutables.contains(elementName);
	}

	public String getDescription() {
		return description;
	}

	public String getNamespace() {
		return namespace;
	}

	public boolean isGlobal() {
		return global;
	}

	public int getMinOccurs() {
		return minOccurs;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * Informs whether the element's max occurs cardinality is unbounded, which
	 * means that there is no limit for the times it can be repeated
	 * 
	 * @return
	 */
	public boolean isMaxOccursUnbounded() {
		return maxOccurs == MAX_OCCURS_UNBOUNDED;
	}

	/**
	 * Informs whether the element is required
	 * 
	 * @return
	 */
	public boolean isRequired() {
		return minOccurs > 0;
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
		SchemaElement other = (SchemaElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Base builder class used to create instances of extensions of
	 * {@link SchemaElement} objects
	 */
	public static abstract class SchemaElementBuilder {

		protected String name;
		protected String type;
		protected String baseType;
		protected final Set<String> substitutables = new HashSet<>();
		protected String description;
		protected String namespace;
		protected boolean global;
		protected int minOccurs = 1;
		protected int maxOccurs = 1;
		protected int order = -1;

		public SchemaElementBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SchemaElementBuilder type(String type) {
			this.type = type;
			return this;
		}

		public SchemaElementBuilder baseType(String baseType) {
			this.baseType = baseType;
			return this;
		}

		public SchemaElementBuilder substitutables(Set<String> substitutables) {
			this.substitutables.addAll(substitutables);
			return this;
		}

		public SchemaElementBuilder description(String description) {
			this.description = description;
			return this;
		}

		public SchemaElementBuilder namespace(String namespace) {
			this.namespace = namespace;
			return this;
		}

		public SchemaElementBuilder global(boolean global) {
			this.global = global;
			return this;
		}

		public SchemaElementBuilder minOccurs(int minOccurs) {
			this.minOccurs = minOccurs;
			return this;
		}

		public SchemaElementBuilder maxOccurs(int maxOccurs) {
			this.maxOccurs = maxOccurs;
			return this;
		}

		public abstract SchemaElement build();
	}
}