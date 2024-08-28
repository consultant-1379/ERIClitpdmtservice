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

import java.net.URI;
import java.util.*;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class that helps the creation of REST representations that follow the
 * HATEOAS principle. It provides methods to its subclasses that enable them to
 * incorporate links into its representation.
 */
public abstract class LinkableResource {

	private final Map<String, URI> links = new LinkedHashMap<>();

	/**
	 * Returns all links (relation key/URI pair) contained in this resource.
	 * 
	 * @return
	 */
	@JsonProperty()
	public Map<String, URI> getLinks() {
		return Collections.unmodifiableMap(links);
	}

	/**
	 * Adds the given link to the resource.
	 * 
	 * @param rel
	 *            - relation key
	 * @param href
	 *            - URI value
	 */
	public void addLink(String rel, URI href) {
		links.put(rel, href);
	}
}
