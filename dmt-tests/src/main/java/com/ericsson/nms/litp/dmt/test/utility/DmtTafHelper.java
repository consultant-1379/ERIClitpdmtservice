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
package com.ericsson.nms.litp.dmt.test.utility;

import java.io.IOException;
import java.util.*;

import com.ericsson.cifwk.taf.tal.rest.HttpHeaders;
import com.ericsson.cifwk.taf.tal.rest.RestExecutionParameters;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DmtTafHelper {

	private final static int ONLINE = 0;
	private final static int OFFLINE = 1;

	public static JsonNode getJson(final String url, final RestTool restool)
			throws IOException, JsonProcessingException {

		return processResponses(restool.get(url));
	}

	private static JsonNode processResponses(List<String> responses)
			throws IOException {
		String response = responses.iterator().next();
		if (isEmptyResponse(response)) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(responses.get(0));
	}

	private static boolean isEmptyResponse(String response) {
		return response.matches("^\\W*$");
	}

	public static Map<String, String> getParameters(
			final String jsonTocreateNode, final RestTool restool) {
		restool.setHttpHeaders(Collections
				.singletonList(HttpHeaders.APPLICATION_JSON.toString()));
		final Map<String, String> parameters = Collections.singletonMap(
				RestExecutionParameters.JSON_OBJECT, jsonTocreateNode);

		return parameters;
	}

	public static JsonNode createItem(final String url, final RestTool restool,
			final Map<String, String> parameters)
			throws JsonProcessingException, IOException {

		return processResponses(restool.post(url, parameters));
	}

	public static JsonNode deleteNode(final String url, final RestTool restool,
			final Map<String, String> parameters)
			throws JsonProcessingException, IOException {

		return processResponses(restool.delete(url, parameters));
	}

	public static String getWorkingCopyId(String baseUrl, RestTool restool,
			String modeType) throws JsonProcessingException, IOException {
		String modelId;
		JsonNode json = getJson(baseUrl, restool);
		JsonNode workingCopies = json.path("working_copies");
		if (modeType.equals("live")) {
			modelId = workingCopies.get(ONLINE).findValue("id").textValue();
		} else {
			modelId = workingCopies.get(OFFLINE).findValue("id").textValue();
		}
		return modelId;
	}

	public static String getURL(final String baseURL, final String nodeURL,
			RestTool restool, String mode, String wcId)
			throws JsonProcessingException, IOException {
		StringBuilder sb = new StringBuilder(baseURL);
		sb.append(wcId);
		sb.append("/");
		sb.append(mode);
		sb.append("/");
		sb.append(nodeURL);
		return sb.toString();
	}

	public static JsonNode modifyProperty(final String url,
			final String requestPropertyObject, final RestTool restool)
			throws JsonProcessingException, IOException {

		restool.setHttpHeaders(Collections
				.singletonList(HttpHeaders.APPLICATION_JSON.toString()));
		final Map<String, String> parameters = Collections.singletonMap(
				RestExecutionParameters.JSON_OBJECT, requestPropertyObject);
		return processResponses(restool.put(url, parameters));
	}

}
