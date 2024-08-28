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

import static com.ericsson.nms.dmt.rest.hateoas.RelConstants.SELF;
import static com.ericsson.nms.dmt.service.qualifier.ServiceQualifierLiteral.from;
import static javax.ws.rs.core.Response.ok;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.ericsson.nms.dmt.ItemType;
import com.ericsson.nms.dmt.Mode;
import com.ericsson.nms.dmt.service.TypeDiscoveryService;

/**
 * REST resource on DMT service to discovery and request details about item
 * types.
 */
@Path("/{mode:(live|local)}/item_types")
public class TypeDiscoveryResource extends BaseResource {

	public static final String GET_ITEM_TYPE_METHOD = "getItemType";

	@Inject
	@Any
	private Instance<TypeDiscoveryService> typesService;

	@PathParam("mode")
	private Mode mode;

	/**
	 * Provides details about a specific item type.
	 * 
	 * @param itemTypeName
	 *            - name of the item type
	 * @return - JSON payload containing the item type details
	 */
	@GET
	@Path("/{itemType}")
	public Response getItemType(@PathParam("itemType") String itemTypeName) {
		ItemType itemType = resolveServiceImpl().getItemType(itemTypeName);
		injectLinks(itemType);
		return ok().entity(itemType).build();
	}

	private void injectLinks(ItemType itemType) {
		itemType.addLink(SELF, uriInfo.getAbsolutePath());
	}

	private TypeDiscoveryService resolveServiceImpl() {
		return typesService.select(from(mode)).get();
	}
}