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
package com.ericsson.nms.dmt.rest;

import static com.ericsson.nms.dmt.ModelInteraction.workingCopy;
import static com.ericsson.nms.dmt.NodePath.root;
import static com.ericsson.nms.dmt.rest.TypeDiscoveryResource.GET_ITEM_TYPE_METHOD;
import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.*;
import static com.ericsson.nms.dmt.service.qualifier.ServiceQualifierLiteral.from;
import static com.google.common.collect.Lists.transform;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.ok;

import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.mapping.Mapper;
import com.ericsson.nms.dmt.rest.dto.JsonObjectWrapper;
import com.ericsson.nms.dmt.rest.dto.NodeChild;
import com.ericsson.nms.dmt.service.ModelManagerService;
import com.google.common.base.Function;

/**
 * REST resource on DMT service to perform CRUD operations onto the deployment
 * model.
 */
@Path("/wc/{id}/{mode:(live|local)}/model")
public class DeploymentModelResource extends BaseResource {

	private static final String CHILDREN_ROOT_FIELD = "children";
	private static final String ADDABLE_TYPES_ROOT_FIELD = "item_types";
	public static final String GET_NODE_METHOD = "getNode";
	public static final String GET_CHILDREN_METHOD = "getNodeChildren";
	public static final String GET_ROOT_CHILDREN_METHOD = "getRootChildren";
	public static final String GET_ADDABLE_ITEM_TYPES_METHOD = "getAddableChildTypes";

	@Inject
	@Any
	private Instance<ModelManagerService> modelService;

	@PathParam("id")
	private String id;

	@PathParam("mode")
	private Mode mode;

	@Inject
	private Mapper<AbstractNode, NodeChild> nodeChildMapper;

	/**
	 * Requests details of the root node in regards to its children. The
	 * response informs whether the root node's children has also children.
	 * 
	 * @return - JSON payload informing which children the root node has, as
	 *         well as whether its children have also children.
	 */
	@GET
	@Path("/children")
	public Response getRootChildren() {
		return getNodeChildren(root());
	}

	/**
	 * Requests details of a specific node in regards to its children. The
	 * response informs whether the node's children has also children.
	 * 
	 * @param nodePath
	 *            - path to the node
	 * @return - JSON payload informing which children the node has, as well as
	 *         whether its children have also children.
	 */
	@GET
	@Path("/{path:.+}/children")
	public Response getNodeChildren(@PathParam("path") NodePath nodePath) {
		ModelInteraction modelInteraction = workingCopy(id).path(nodePath)
				.build();
		List<? extends AbstractNode> children = resolveServiceImpl()
				.getNodeChildren(modelInteraction);

		return ok().entity(buildNodeChildResponse(children)).build();
	}

	private Object buildNodeChildResponse(List<? extends AbstractNode> children) {
		List<NodeChild> nodeChildren = transform(children,
				new Function<AbstractNode, NodeChild>() {
					@Override
					public NodeChild apply(AbstractNode node) {
						NodeChild nodeChild = nodeChildMapper.map(node);
						nodeChild.addLink(
								SELF,
								buildUriFromResourceMethod(
										DeploymentModelResource.class,
										GET_NODE_METHOD, id, mode,
										node.getPath()));
						nodeChild.addLink(
								CHILDREN,
								buildUriFromResourceMethod(
										DeploymentModelResource.class,
										GET_CHILDREN_METHOD, id, mode,
										node.getPath()));
						return nodeChild;
					}
				});

		return JsonObjectWrapper.wrap(CHILDREN_ROOT_FIELD, nodeChildren);
	}

	/**
	 * Provides details about a specific node in the deployment model.
	 * 
	 * @param nodePath
	 *            - path to the node
	 * @return - JSON payload containing the node's details
	 */
	@GET
	@Path("/{path:.+}")
	public Response getNode(@PathParam("path") NodePath nodePath) {
		AbstractNode node = resolveServiceImpl().findNode(
				workingCopy(id).path(nodePath).build());
		injectNodeLinks(node);
		return ok().entity(node).build();
	}

	private void injectNodeLinks(AbstractNode node) {
		node.addLink(SELF, uriInfo.getAbsolutePath());
		node.addLink(
				CHILDREN,
				buildUriFromResourceMethod(DeploymentModelResource.class,
						GET_CHILDREN_METHOD, id, mode, node.getPath()));
		node.addLink(
				ITEM_TYPE,
				buildUriFromResourceMethod(TypeDiscoveryResource.class,
						GET_ITEM_TYPE_METHOD, mode, node.getType()));
		node.addLink(
				ADDABLE_TYPES,
				buildUriFromResourceMethod(DeploymentModelResource.class,
						GET_ADDABLE_ITEM_TYPES_METHOD, id, mode, node.getPath()));
	}

	/**
	 * Creates a new node in the deployment model.
	 * 
	 * @param nodePath
	 *            - path to the parent node (where the new node should be
	 *            created)
	 * @param nodeData
	 *            - data to be used to create the new node
	 * @return - empty payload
	 */
	@POST
	@Path("/{path:.+}")
	public Response createNode(@PathParam("path") NodePath nodePath,
			NodeData nodeData) {
		resolveServiceImpl().createNode(
				workingCopy(id).path(nodePath).data(nodeData).build());
		return created(
				uriInfo.getAbsolutePathBuilder().path(nodeData.getId()).build())
				.build();
	}

	/**
	 * Updates a specific node in the deployment model.
	 * 
	 * @param nodePath
	 *            - path to the node to be updated
	 * @param nodeData
	 *            - data used to update the node
	 * @return - empty payload
	 */
	@PUT
	@Path("/{path:.+}")
	public Response updateNode(@PathParam("path") NodePath nodePath,
			NodeData nodeData) {
		resolveServiceImpl().updateNode(
				workingCopy(id).path(nodePath).data(nodeData).build());

		return ok().build();
	}

	/**
	 * Removes a specific node from the deployment model.
	 * 
	 * @param nodePath
	 *            - path to the node to be removed
	 * @return - empty payload
	 */
	@DELETE
	@Path("/{path:.+}")
	public Response removeNode(@PathParam("path") NodePath nodePath) {
		resolveServiceImpl().removeNode(workingCopy(id).path(nodePath).build());
		return ok().build();
	}

	/**
	 * Returns a list of item types that can be added (as children) to a
	 * specific node in the deployment model. This method provides a way to find
	 * out beforehand which children can be created in a node. It hides the
	 * complexity from the REST API users as they don't need to know the
	 * particularities of each category of nodes.
	 * 
	 * @param nodePath
	 *            - path to the node
	 * @return - JSON payload containing a list of item type
	 */
	@GET
	@Path("/{path:.+}/addable_item_types")
	public Response getAddableChildTypes(@PathParam("path") NodePath nodePath) {
		Set<ItemType> addableTypes = resolveServiceImpl().getAddableChildTypes(
				workingCopy(id).path(nodePath).build());

		injectItemTypeLinks(addableTypes);
		return ok().entity(
				JsonObjectWrapper.wrap(ADDABLE_TYPES_ROOT_FIELD, addableTypes))
				.build();
	}

	private void injectItemTypeLinks(Set<ItemType> addableTypes) {
		for (ItemType itemType : addableTypes) {
			itemType.addLink(
					SELF,
					buildUriFromResourceMethod(TypeDiscoveryResource.class,
							GET_ITEM_TYPE_METHOD, mode, itemType.getName()));
		}
	}

	private ModelManagerService resolveServiceImpl() {
		return modelService.select(from(mode)).get();
	}
}
