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
package com.ericsson.nms.dmt.live.litp.parsing;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.io.IOUtils;

import com.ericsson.nms.dmt.live.Node;

/**
 * Message body reader that parses complex JSON payload into {@link Node}
 * objects.
 */
@Dependent
public class NodeBodyReader implements MessageBodyReader<Node> {

	@Resource(name = "litpRestBaseUri")
	String litpBaseUri;

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type.equals(Node.class);
	}

	@Override
	public Node readFrom(Class<Node> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> valuedMap, InputStream inputStream)
			throws IOException, WebApplicationException {
		String json = IOUtils.toString(inputStream);
		return new NodeParser(litpBaseUri).parseSingleNode(json);
	}
}