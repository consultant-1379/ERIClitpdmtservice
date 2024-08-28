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

import static com.ericsson.nms.dmt.rest.WorkingCopyResource.GET_WORKING_COPIES_METHOD;
import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.SELF;
import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.WORKING_COPIES;
import static javax.ws.rs.core.Response.ok;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.ericsson.nms.dmt.rest.dto.MessageContainer;

/**
 * This class is the entry point to the DMT REST API. From this resource,
 * clients can obtain new URIs that guide to other resources and functionalities
 * provided by the REST API.
 */
@Path("")
public class HomeResource extends BaseResource {

	/**
	 * Returns the initial links that the clients can follow to discover other
	 * resources.
	 * 
	 * @return - initial links
	 */
	@GET
	public Response home() {
		MessageContainer message = new MessageContainer(
				"DMT Rest API entry point.");
		message.addLink(SELF, uriInfo.getAbsolutePath());
		message.addLink(
				WORKING_COPIES,
				buildUriFromResourceMethod(WorkingCopyResource.class,
						GET_WORKING_COPIES_METHOD));
		return ok().entity(message).build();
	}
}
