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
package com.ericsson.nms.dmt.live.litp;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.*;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.live.Node;

/**
 * Provides the LITP CRUD operations to perform changes onto the managed
 * Deployment Model, as well as operations to discover the item types.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public interface LitpClient {

	/**
	 * Requests details of a specific node.
	 * 
	 * @param path
	 *            to the node
	 * @return
	 */
	@GET
	@Path("/{path}")
	Node findNode(@PathParam("path") NodePath path);

	/**
	 * Creates a new node instance in the deployment model.
	 * 
	 * @param path
	 *            to the parent node, where the new node should be created
	 * @param nodeData
	 */
	@POST
	@Path("/{path}")
	void createNode(@PathParam("path") NodePath path, NodeData nodeData);

	/**
	 * Updates an existing node with the given parameters.
	 * 
	 * @param path
	 *            to the node
	 * @param nodeData
	 */
	@PUT
	@Path("/{path}")
	void updateNode(@PathParam("path") NodePath path, NodeData nodeData);

	/**
	 * Remove an existing node and everything below it in the deployment model.
	 * 
	 * @param path
	 *            to the node
	 */
	@DELETE
	@Path("/{path}")
	void removeNode(@PathParam("path") NodePath path);

	/**
	 * Returns the {@link ItemType} instance for the given item type name.
	 * 
	 * @param typeName
	 *            - item type name
	 * @return
	 */
	@GET
	@Path("/item-types/{typeName}")
	ItemType getItemType(@PathParam("typeName") String typeName);

	/**
	 * Returns a list of {@link ItemType} registered with the LITP instance that
	 * can be used to build a model.
	 * 
	 * @return
	 */
	@GET
	@Path("/item-types")
	List<ItemType> getAllItemTypes();
}
