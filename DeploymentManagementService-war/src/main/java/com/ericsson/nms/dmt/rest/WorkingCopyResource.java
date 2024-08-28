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

import static com.ericsson.nms.dmt.rest.DeploymentModelResource.GET_ROOT_CHILDREN_METHOD;
import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.CHILDREN;
import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.SELF;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.ok;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.ericsson.nms.dmt.WorkingCopy;
import com.ericsson.nms.dmt.rest.dto.JsonObjectWrapper;
import com.ericsson.nms.dmt.service.WorkingCopyService;

/**
 * REST resource on DMT service used to create and get details about stored
 * working copies.
 */
@Path("/wc")
public class WorkingCopyResource extends BaseResource {

	private static final String WORKING_COPIES_ROOT_FIELD = "working_copies";
	public static final String GET_WORKING_COPIES_METHOD = "listWorkingCopies";
	public static final String GET_WORKING_COPY_METHOD = "findWorkingCopy";

	@Inject
	private WorkingCopyService workingCopyService;

	/**
	 * Creates a new LOCAL working copy in DMT.
	 * 
	 * @param workingCopy
	 *            - JSON data containing the new working copy details
	 * @return - URI to the created resource
	 */
	@POST
	public Response createWorkingCopy(WorkingCopy workingCopy) {
		WorkingCopy createdWorkingCopy = workingCopyService
				.createWorkingCopy(workingCopy);
		return created(
				uriInfo.getAbsolutePathBuilder()
						.path(createdWorkingCopy.getId()).build()).build();
	}

	/**
	 * Returns a list of all persisted working copies
	 * 
	 * @return - JSON payload containing the list of working copies
	 */
	@GET
	@Path("")
	public Response listWorkingCopies() {
		List<WorkingCopy> workingCopies = workingCopyService
				.findWorkingCopies();

		for (WorkingCopy workingCopy : workingCopies) {
			injecUris(workingCopy);
		}
		return ok().entity(
				JsonObjectWrapper
						.wrap(WORKING_COPIES_ROOT_FIELD, workingCopies))
				.build();
	}

	/**
	 * Returns details about a specific working copy
	 * 
	 * @param id
	 *            - working copy id
	 * @return - JSON payload containing the working copy details
	 */
	@GET
	@Path("/{id}")
	public Response findWorkingCopy(@PathParam("id") String id) {
		WorkingCopy workingCopy = workingCopyService.findWorkingCopy(id);
		injecUris(workingCopy);
		return ok().entity(workingCopy).build();
	}

	private void injecUris(WorkingCopy workingCopy) {
		workingCopy.addLink(
				SELF,
				buildUriFromResourceMethod(WorkingCopyResource.class,
						GET_WORKING_COPY_METHOD, workingCopy.getId()));
		workingCopy.addLink(
				CHILDREN,
				buildUriFromResourceMethod(DeploymentModelResource.class,
						GET_ROOT_CHILDREN_METHOD, workingCopy.getId(),
						workingCopy.getMode()));
	}
}
