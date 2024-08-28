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
package com.ericsson.nms.litp.dmt.test.cases;

import static com.ericsson.nms.litp.dmt.test.utility.DmtTafHelper.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONException;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.nms.litp.dmt.test.operators.DmtOperator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class DMTCreateNodeRestTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Create_Node", title = "Verify Create PayLoad is Correct")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_create_node_positive_flow")
	public void verifyThatTheCreatedNodeContainsLinksPositiveFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("deleteUrl") final String deleteNodeUrl,
			@Input("requestBody") final String jsonTocreateNode,
			@Input("mode") final String mode,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expectedCreateResponseCode") final String expectedCreateResponseCode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();

		String wcId = getWorkingCopyId(baseURL, restool, mode);

		// create a new node
		String createURL = getURL(baseURL, nodeURL, restool, mode, wcId);
		final Map<String, String> parameters = getParameters(jsonTocreateNode,
				restool);
		createItem(createURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedCreateResponseCode));

		// assert that the created node contains keys self, children, item_types
		// and addable_types within links
		JsonNode json = getJson(createURL, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// self
		assertThat(json.path("links").findValue("self"), is(notNullValue()));
		String selfUrl = getWcURL(json.path("links").findValue("self")
				.textValue());
		getJson(selfUrl, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// children
		assertThat(json.path("links").findValue("children"), is(notNullValue()));
		String childrenUrl = getWcURL(json.path("links").findValue("children")
				.textValue());
		getJson(childrenUrl, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// item_type
		assertThat(json.path("links").findValue("item_type"),
				is(notNullValue()));
		String itemTypeUrl = getWcURL(json.path("links").findValue("item_type")
				.textValue());
		getJson(itemTypeUrl, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// addable_types
		assertThat(json.path("links").findValue("addable_types"),
				is(notNullValue()));
		String addableTypesUrl = getWcURL(json.path("links")
				.findValue("addable_types").textValue());
		getJson(addableTypesUrl, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// delete the node
		String deleteURL = getURL(baseURL, deleteNodeUrl, restool, mode, wcId);
		deleteNode(deleteURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
	}

	@TestId(id = "Create_Node_Negative_Flow", title = "Verify a HTTP status code of 409 is returned when attempting to create an already existing node.")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_create_node_negative_flow")
	public void verifyWeCanCreateANodeNegativeFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("deleteUrl") final String deleteNodeUrl,
			@Input("requestBody") final String jsonTocreateNode,
			@Input("expectedCreateResponseCode") final String expectedCreateResponseCode,
			@Input("expectedDeleteResponseCode") final String expectedDeleteResponseCode,
			@Input("mode") final String mode,
			@Output("expectedErrorMessage") final String expectedErrorMessage,
			@Output("expectedErrorType") final String expectedErrorType,
			@Output("expectedResponseCode") final String expectedResponseCode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();

		String wcId = getWorkingCopyId(baseURL, restool, mode);
		String createURL = getURL(baseURL, nodeURL, restool, mode, wcId);

		final Map<String, String> parameters = getParameters(jsonTocreateNode,
				restool);

		// create a new node
		createItem(createURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedCreateResponseCode));

		// create the same node again
		JsonNode json = createItem(createURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
		assertThat(json.path("messages").iterator().next().path("message")
				.asText(), is(expectedErrorMessage));
		assertThat(json.path("messages").iterator().next().path("type")
				.asText(), is(expectedErrorType));

		// reset
		String deleteURL = getURL(baseURL, deleteNodeUrl, restool, mode, wcId);
		deleteNode(deleteURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedDeleteResponseCode));

	}

	private RestTool getDmtRestTool() {
		final DmtOperator dmtRest = getRestOperator();
		final RestTool restTool = dmtRest.retrieveRestData();

		return restTool;
	}

	private DmtOperator getRestOperator() {

		return dmtRestProvider.provide(DmtOperator.class);
	}

	private String getWcURL(String url) {
		String createdWcURL = url.split(":8080/")[1];

		return createdWcURL;
	}

}
