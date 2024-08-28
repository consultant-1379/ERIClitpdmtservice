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
import static org.hamcrest.CoreMatchers.is;
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

public class DMTNodePropertiesRestTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Retrive_Node_Properties_Rest_Test", title = "Should retrieve properties of the Node and response code 200")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_get_node_properties_positive_flow")
	public void shouldReturnNodePropertiesPositiveFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("propertiesSize") final int propertiesSize)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String rootURL = getURL(baseURL, nodeURL, restool, mode, wcId);

		JsonNode json = getJson(rootURL, restool);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		JsonNode properties = json.findValue("properties");

		assertThat(properties.size(), is(propertiesSize));
	}

	@TestId(id = "Incorrect_Url_Rest_Test", title = "Verify a HTTP status code of 404 is returned when url is incorrect.")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_get_node_properties_negative_flow")
	public void shouldReturnNodePropertiesNegativeFlow(
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

}
