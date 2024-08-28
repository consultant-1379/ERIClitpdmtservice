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

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.ericsson.nms.dmt.error.DmtBaseException;
import com.ericsson.nms.dmt.local.error.AbstractPropertyException;
import com.ericsson.nms.dmt.local.error.AggregatePropertyException;

/**
 * Custom serializer used by the Jackson serialization mechanism to output
 * {@link DmtBaseException} objects in JSON format.
 */
public class AggregatePropertyExceptionSerializer extends
		JsonSerializer<AggregatePropertyException> {

	@Override
	public void serialize(AggregatePropertyException exception,
			JsonGenerator gen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		gen.writeStartObject();
		gen.writeFieldName("messages");
		gen.writeStartArray();
		for (AbstractPropertyException cause : exception.getCauses()) {
			writeCause(gen, cause);
		}
		gen.writeEndArray();
		gen.writeEndObject();
	}

	private void writeCause(JsonGenerator gen, AbstractPropertyException cause)
			throws JsonGenerationException, IOException {
		gen.writeStartObject();
		gen.writeStringField("type",
				ErrorTypeSelector.getProperErrorTypeFor(cause));
		gen.writeStringField("message", cause.getMessage());
		gen.writeStringField("refersTo", cause.getPropertyName());
		gen.writeEndObject();
	}
}
