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
package com.ericsson.nms.dmt.service;

import java.util.Set;

import com.ericsson.nms.dmt.*;

/**
 * This interface specifies a set of business methods that allow clients to
 * perform type discovery operations. It has two implementations which are
 * selected in runtime depending on the current working copy mode. The first one
 * ({@link Mode} = LIVE) connects to a LITP server instance get the item type
 * data. The second one ( {@link Mode} = LOCAL) extracts locally the item type
 * data from the LITP XSDs files.
 */
public interface TypeDiscoveryService {

	/**
	 * Returns the {@link ItemType} instance for the given item type name.
	 * 
	 * @param itemTypeName
	 *            - item type name
	 * @return
	 */
	ItemType getItemType(String itemTypeName);

	/**
	 * Returns the list of possible item types that can be added to a specific
	 * node
	 * 
	 * @param node
	 * @return - item types that can be used to create new children on the
	 *         provided node
	 */
	Set<ItemType> getAddableChildTypes(AbstractNode node);
}