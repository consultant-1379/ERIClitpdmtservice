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
package com.ericsson.nms.dmt.rest.hateoas;

/**
 * Provides a set of relation keys used by DMT to incorporate links into
 * resource representations
 */
public interface RelConstants {

	String SELF = "self";
	String HOME = "home";
	String CHILDREN = "children";
	String ITEM_TYPE = "item_type";
	String WORKING_COPY = "working_copy";
	String WORKING_COPIES = "working_copies";
	String ADDABLE_TYPES = "addable_types";
}
