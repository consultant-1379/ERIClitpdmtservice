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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import com.ericsson.nms.dmt.rest.error.ExceptionHandling;

/**
 * Base class for JAX-RS resources. It provides common functionalities to its
 * child classes in order to avoid repetitive code.
 */
@ExceptionHandling
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public abstract class BaseResource {

	@Context
	protected UriInfo uriInfo;

	/**
	 * Used to build full URIs to specific actions in other JAX-RS resource
	 * classes. It is used as a helper method to implement the HATEOAS feature
	 * in the DMT service.
	 * 
	 * @param clazz
	 *            - JAX-RS resource class
	 * @param methodName
	 *            - method that is annotated with @Path and contains the URI for
	 *            the resource/action
	 * @param args
	 *            - arguments to replace the path variables in the @Path
	 *            annotation
	 * @return - a full URL for the specified resource/action
	 */
	public URI buildUriFromResourceMethod(Class<? extends BaseResource> clazz,
			String methodName, Object... args) {

		String resourceUri = UriBuilder.fromResource(clazz)
				.path(clazz, methodName).build(args).toString();

		return uriInfo.getBaseUriBuilder()
				.path(removeDuplicatedSlashes(resourceUri)).build();

	}

	private String removeDuplicatedSlashes(String uri) {
		return uri.replaceAll("/+", "/");
	}
}
