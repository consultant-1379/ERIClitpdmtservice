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

public class DMTItemTypesRestTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Retrieve_Item_Types_Rest_Test", title = "Verify that we can retrieve item type and response code 200")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_get_item_types_positive_flow")
	public void shouldReturnNodeItemTypesPositiveFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expectedItemName") final String expectedItemName)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String rootURL = getURL(baseURL, nodeURL, restool, mode, wcId);
		JsonNode json = getJson(rootURL, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// verify that you got the node item type
		JsonNode actualItemName = json.path("item_types").findValue("name");
		assertThat(actualItemName.textValue(), is(expectedItemName.toString()));

	}

	@TestId(id = "Retrieve_Item_Types_Rest_Test", title = "Verify that we can retrieve item type and response code 200")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_get_item_types_positive_flow")
	public void verifyThatTheItemTypesContainsLinksPositiveFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expectedItemName") final String expectedItemName)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String rootURL = getURL(baseURL, nodeURL, restool, mode, wcId);
		JsonNode json = getJson(rootURL, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
		// assert that it contains self
		assertThat(
				json.path("item_types").get(0).path("links").findValue("self"),
				is(notNullValue()));
		String selfUrl = getWcURL(json.path("item_types").get(0).path("links")
				.findValue("self").textValue());
		getJson(selfUrl, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		// verify that you got the node item type
		JsonNode actualItemName = json.path("item_types").findValue("name");
		assertThat(actualItemName.textValue(), is(expectedItemName.toString()));

	}

	@TestId(id = "Incorrect_Url_Rest_Test", title = "Verify a HTTP status code of 404 is returned when url is incorrect")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_get_item_types_negative_flow")
	public void shouldReturnNodeItemTypesNegativeFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedErrorMessage") final String expectedErrorMessage,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expectedErrorType") final String expectedErrorType)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String rootURL = getURL(baseURL, nodeURL, restool, mode, wcId);

		JsonNode json = getJson(rootURL, restool);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
		assertThat(json.path("messages").iterator().next().path("message")
				.asText(), is(expectedErrorMessage));
		assertThat(json.path("messages").iterator().next().path("type")
				.asText(), is(expectedErrorType));
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
