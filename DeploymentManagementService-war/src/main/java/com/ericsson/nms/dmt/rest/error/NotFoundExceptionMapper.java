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
package com.ericsson.nms.dmt.rest.error;

import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.HOME;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.NotFoundException;

import com.ericsson.nms.dmt.rest.HomeResource;
import com.ericsson.nms.dmt.rest.dto.MessageContainer;

/**
 * Resteasy exception mapping class used to capture 404 (not found) errors and
 * write a specific HTTP response.
 */
@Provider
public class NotFoundExceptionMapper implements
		ExceptionMapper<NotFoundException> {

	@Context
	private UriInfo uriInfo;

	@Override
	public Response toResponse(NotFoundException exception) {
		MessageContainer message = new MessageContainer(
				"Sorry. Resource not found!");
		message.addLink(HOME,
				uriInfo.getBaseUriBuilder().path(HomeResource.class).build());

		return Response.status(NOT_FOUND).entity(message)
				.type(APPLICATION_JSON).build();
	}
}
