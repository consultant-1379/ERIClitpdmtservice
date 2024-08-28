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

/**
 * Base class that represents an item type child and provides relation
 * information about itself and its item type parent
 */
public abstract class ItemTypeChild {

	private String name;
	private String description;
	private Category category;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Indicates whether the child is a collection of nodes
	 * 
	 * @see Category
	 * 
	 * @return
	 */
	public boolean isCollection() {
		return category.isCollection();
	}

	/**
	 * Indicates whether the child is a reference to another node
	 * 
	 * @see Category
	 * 
	 * @return
	 */
	public boolean isReference() {
		return category.isReference();
	}

	/**
	 * Indicates whether the child is regular, which means that it is not a
	 * collection nor a reference
	 * 
	 * @see Category
	 * 
	 * @return
	 */
	public boolean isRegular() {
		return !isCollection() && !isReference();
	}
}
