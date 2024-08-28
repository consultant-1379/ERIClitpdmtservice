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

import static java.util.Collections.unmodifiableList;

import java.util.*;

/**
 * An XML Schema describes the structure of an XML document. It defines the
 * elements that can appear in a document created from this schema.
 */
public class Schema {

	private final String namespace;
	private final Map<String, SchemaElement> declaredElements;

	public Schema(String namespace) {
		this.namespace = namespace;
		this.declaredElements = new HashMap<>();
	}

	/**
	 * Returns the schema target namespace
	 * 
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Returns the list of elements declared on this schema
	 * 
	 * @return
	 */
	public List<SchemaElement> getDeclaredElements() {
		return unmodifiableList(new ArrayList<SchemaElement>(
				declaredElements.values()));
	}

	/**
	 * Returns a specific {@link SchemaElement} for the given name
	 * 
	 * @param name
	 *            - element name
	 * @return
	 */
	public SchemaElement getDeclaredElement(String name) {
		return declaredElements.get(name);
	}

	/**
	 * Adds a new {@link SchemaElement} to the current schema
	 * 
	 * @param element
	 *            - element to be added
	 */
	public void addDeclaredElement(SchemaElement element) {
		this.declaredElements.put(element.getName(), element);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Schema other = (Schema) obj;
		if (namespace == null) {
			if (other.namespace != null) {
				return false;
			}
		} else if (!namespace.equals(other.namespace)) {
			return false;
		}
		return true;
	}
}
