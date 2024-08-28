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
package com.ericsson.nms.dmt.rest.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.ericsson.nms.dmt.error.DmtBaseException;

/**
 * Custom serializer used by the Jackson serialization mechanism to output
 * {@link DmtBaseException} objects in JSON format.
 */
public class DefaultExceptionSerializer extends
		JsonSerializer<DmtBaseException> {

	@Override
	public void serialize(DmtBaseException exception, JsonGenerator gen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		gen.writeStartObject();
		gen.writeFieldName("messages");
		gen.writeStartArray();
		gen.writeStartObject();
		gen.writeStringField("type",
				ErrorTypeSelector.getProperErrorTypeFor(exception));
		gen.writeStringField("message", exception.getMessage());
		gen.writeEndObject();
		gen.writeEndArray();
		gen.writeEndObject();
	}
}
