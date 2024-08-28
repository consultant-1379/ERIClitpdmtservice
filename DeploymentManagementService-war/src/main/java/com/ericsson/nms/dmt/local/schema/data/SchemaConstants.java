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

/**
 * Defines a set of constants used to deal with the LITP XML schema files
 */
public interface SchemaConstants {

	/**
	 * This is used to specify in the schema element 'maxOccurs' attribute that
	 * the element can have as many occurrences as the author wishes
	 */
	long MAX_OCCURS_UNBOUNDED = -1L;

	/**
	 * Attribute used in the LITP schema files to identify elements (nodes)
	 */
	String IDENTIFIER_ATTRIBUTE = "id";

	/**
	 * Base type defined by LITP for collection elements
	 */
	String COLLECTION_BASE_TYPE = "basecollection-type";

	/**
	 * Regular expression used to identify which elements in the LITP XML schema
	 * files are classified as references (or links)
	 */
	String LINK_ELEMENT_PATTERN = "^(\\S+)-link$";
}
