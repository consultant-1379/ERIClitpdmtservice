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

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.ericsson.nms.dmt.*;

/**
 * This interface specifies a set of business methods that allow clients to
 * perform changes in a deployment model as well as to get details from it. It
 * has two implementations which are selected in runtime depending on the
 * current working copy mode. The first one ({@link Mode} = LIVE) connects to a
 * LITP server instance to perform the requested operations. The second one (
 * {@link Mode} = LOCAL) performs all operations onto the locally persisted
 * working copies.
 */
@Local
public interface ModelManagerService {

	/**
	 * Requests details of a node.
	 * 
	 * @param interaction
	 *            - request information to find a node
	 * @return - node details
	 */
	AbstractNode findNode(ModelInteraction interaction);

	/**
	 * Requests the list of children that a specific node has
	 * 
	 * @param interaction
	 *            - request information to get node children
	 * @return - node's children
	 */
	List<? extends AbstractNode> getNodeChildren(ModelInteraction interaction);

	/**
	 * Creates a new node instance in the deployment model.
	 * 
	 * @param interaction
	 *            - request information to create a node
	 */
	void createNode(ModelInteraction interaction);

	/**
	 * Remove an existing node and everything below it in the deployment model.
	 * 
	 * @param interaction
	 *            - request information to remove a node
	 */
	void removeNode(ModelInteraction interaction);

	/**
	 * Updates an existing node with the given parameters.
	 * 
	 * @param interaction
	 *            - request information to update a node
	 */
	void updateNode(ModelInteraction interaction);

	/**
	 * Returns the list of possible item types that can be added to a node in a
	 * specific working copy
	 * 
	 * @param interaction
	 *            - request information to retrieve the addable types
	 * @return - item types that can be used to create new children on a node
	 */
	Set<ItemType> getAddableChildTypes(ModelInteraction interaction);
}