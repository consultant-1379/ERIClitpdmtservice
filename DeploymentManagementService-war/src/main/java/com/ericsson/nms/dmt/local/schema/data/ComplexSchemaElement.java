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

import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.COLLECTION_BASE_TYPE;
import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.LINK_ELEMENT_PATTERN;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.google.common.base.Predicate;

/**
 * A complex schema element is an XML element that contains other elements
 * and/or attributes.
 * 
 * @see SchemaElement
 */
public class ComplexSchemaElement extends SchemaElement {

	private Map<String, SchemaAttribute> attributes;
	private Map<String, SchemaElement> children;

	private ComplexSchemaElement() {
	}

	/**
	 * Returns a set of the valid attributes that this element can contain in an
	 * XML document
	 * 
	 * @return
	 */
	public Set<SchemaAttribute> getAttributes() {
		return unmodifiableSet(newLinkedHashSet(attributes.values()));
	}

	/**
	 * Returns the attribute which matches the provided name
	 * 
	 * @param name
	 *            - attribute name
	 * @return
	 */
	public SchemaAttribute getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Informs whether the element has attributes
	 * 
	 * @return
	 */
	public boolean hasAttributes() {
		return !attributes.isEmpty();
	}

	/**
	 * Returns the children that this element has
	 * 
	 * @return
	 */
	public Set<SchemaElement> getChildren() {
		return unmodifiableSet(newLinkedHashSet(children.values()));
	}

	/**
	 * Utility method used to return the first child of the this element
	 * 
	 * @return
	 */
	public SchemaElement getFirstChild() {
		return hasChildren() ? getChildren().iterator().next() : null;
	}

	/**
	 * Returns the child element that can be substituted by the given element
	 * name
	 * 
	 * @param substitutable
	 *            - substitutable element name
	 * @return
	 * @throws UnexpectedException
	 *             if there are more than one child that can be substituted by
	 *             the given element name
	 */
	public SchemaElement getChildThatCanBeSubstitutedBy(String substitutable) {
		Set<SchemaElement> result = new HashSet<>();
		for (SchemaElement child : getChildren()) {
			if (child.canBeSubstitutedBy(substitutable)) {
				result.add(child);
			}
		}

		if (result.size() == 0) {
			return null;
		}

		if (result.size() > 1) {
			throw new UnexpectedException(
					String.format(
							"There are more than one schema element child that can be substituted by %s",
							substitutable));
		}

		return result.iterator().next();
	}

	/**
	 * Returns the child that matches the given name
	 * 
	 * @param name
	 *            - child name
	 * @return
	 */
	public SchemaElement getChild(String name) {
		return children.get(name);
	}

	/**
	 * Returns a set of children elements that are required to exist in the
	 * current element
	 * 
	 * @return
	 */
	public Set<SchemaElement> getRequiredChildren() {
		return filter(getChildren(), new Predicate<SchemaElement>() {
			@Override
			public boolean apply(SchemaElement child) {
				return child.isRequired();
			}
		});
	}

	/**
	 * Returns the child element order inside its parent element according to
	 * the order defined by the schema file
	 * 
	 * @param childName
	 *            - child name
	 * @return 0 or more means the proper order. When the return value is -1, it
	 *         means that the child doesn't exist on this element
	 */
	public int getChildOrder(String childName) {
		if (hasChild(childName)) {
			return children.get(childName).order;
		}
		return -1;
	}

	/**
	 * Informs whether this element has children
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * Informs whether this element has a child which matches the provided name
	 * 
	 * @param childName
	 *            - child name
	 * @return
	 */
	public boolean hasChild(String childName) {
		return children.containsKey(childName);
	}

	@Override
	public boolean isComplexElement() {
		return true;
	}

	/**
	 * Utility method that informs whether the corresponding XML element is a
	 * regular element, which means that it is not a collection nor a reference.
	 * This is not a concept that exists in the XML/XML Schema specifications,
	 * but is handy when it comes to the LITP item types world. Should be
	 * removed in the future to keep the class agnostic to LITP particularities.
	 * 
	 * @return
	 */
	public boolean isRegularElement() {
		return !isCollectionContainerElement() && !isReferenceElement();
	}

	/**
	 * Utility method that informs whether the corresponding XML element is a
	 * container (collection) of sub-elements from the same type or extensions
	 * (substitutables). In fact, a collection container schema element has only
	 * one possible child, which often has max occurs cardinality > 1 or
	 * unbounded. This is not a concept that exists in the XML/XML Schema
	 * specifications, but is handy when it comes to the LITP item types world.
	 * Should be removed in the future to keep the class agnostic to LITP
	 * particularities.
	 * 
	 * @return
	 */
	public boolean isCollectionContainerElement() {
		return isNotBlank(baseType) && baseType.equals(COLLECTION_BASE_TYPE);
	}

	/**
	 * Utility method that informs whether the corresponding XML element is used
	 * to reference another element in the XML document. This is not a concept
	 * that exists in the XML/XML Schema specifications, but is handy when it
	 * comes to the LITP item types world. Should be removed in the future to
	 * keep the class agnostic to LITP particularities.
	 * 
	 * @return
	 */
	public boolean isReferenceElement() {
		return name.matches(LINK_ELEMENT_PATTERN);
	}

	/**
	 * It is a reference element, this method returns the element name
	 * referenced by it. Otherwise, it returns null.
	 * 
	 * @return name of the element referenced by the current element
	 */
	public String getReferencedElementName() {
		Pattern pattern = Pattern.compile(LINK_ELEMENT_PATTERN);
		Matcher matcher = pattern.matcher(name);
		if (!matcher.matches()) {
			return null;
		}
		return matcher.group(1);
	}

	/**
	 * Builder class used to create instances of {@link ComplexSchemaElement}
	 * objects
	 */
	public static class ComplexSchemaElementBuilder extends
			SchemaElementBuilder {

		private final Map<String, SchemaAttribute> attributes = new LinkedHashMap<>();
		private final Map<String, SchemaElement> children = new LinkedHashMap<>();

		public ComplexSchemaElementBuilder child(SchemaElement child) {
			child.order = this.children.size();
			this.children.put(child.getName(), child);
			return this;
		}

		public ComplexSchemaElementBuilder children(
				Collection<SchemaElement> children) {
			for (SchemaElement child : children) {
				child(child);
			}
			return this;
		}

		public ComplexSchemaElementBuilder attribute(SchemaAttribute attribute) {
			this.attributes.put(attribute.getName(), attribute);
			return this;
		}

		public ComplexSchemaElementBuilder attributes(
				Collection<SchemaAttribute> attributes) {
			for (SchemaAttribute attribute : attributes) {
				attribute(attribute);
			}
			return this;
		}

		@Override
		public SchemaElement build() {
			ComplexSchemaElement element = new ComplexSchemaElement();
			element.name = this.name;
			element.type = this.type;
			element.baseType = this.baseType;
			element.description = this.description;
			element.namespace = this.namespace;
			element.global = this.global;
			element.minOccurs = this.minOccurs;
			element.maxOccurs = this.maxOccurs;
			element.substitutables = this.substitutables;
			element.attributes = this.attributes;
			element.children = this.children;
			return element;
		}
	}
}
