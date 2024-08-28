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

import static com.ericsson.nms.dmt.util.JsonPathSafeReader.read;
import static com.ericsson.nms.dmt.util.JsonPathSafeReader.readList;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import com.ericsson.nms.dmt.live.litp.error.ErrorMessage;

/**
 * Helper class that provides logic to parse the error payload returned by LITP
 * in JSON format to {@link ErrorMessage} objects.
 */
public class ErrorMessagesParser {

	/**
	 * Parses a JSON payload that contains error messages and returns them as a
	 * list of {@link ErrorMessage} objects.
	 * 
	 * @param responseBody
	 * @return
	 */
	public List<ErrorMessage> parse(String responseBody) {
		List<ErrorMessage> result = new ArrayList<>();
		List<JSONObject> messages = readList(responseBody, "$.messages[*]");
		for (JSONObject message : messages) {
			result.add(parseMessage(message));
		}
		return result;
	}

	private ErrorMessage parseMessage(JSONObject messageJson) {
		return ErrorMessage.builder()
				.type((String) read(messageJson, "$.type"))
				.message((String) read(messageJson, "$.message"))
				.refersTo((String) read(messageJson, "$.property_name"))
				.build();
	}
}