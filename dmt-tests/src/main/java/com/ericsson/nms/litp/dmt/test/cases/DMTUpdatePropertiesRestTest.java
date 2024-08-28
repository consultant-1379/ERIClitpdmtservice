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

public class DMTUpdatePropertiesRestTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Modify_Property_Positive_Flow_Rest_Test", title = "Verify a HTTP status code of 200 is returned when node property successfully modified")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_update_property_positive_flow")
	public void verifyWeCanUpdatePropertiesPositiveFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("createdProfileURL") final String createdProfileURL,
			@Input("requestPropertyObject") final String requestPropertyObject,
			@Input("requestModifyPropertyObject") final String requestModifyPropertyObject,
			@Input("mode") final String mode,
			@Output("expectedCreateResponseCode") final String expectedCreateResponseCode,
			@Output("expected200ResponseCode") final String expected200ResponseCode)
			throws JSONException, JsonProcessingException, IOException {

		JsonNode jsonBefore;
		JsonNode jsonAfter;
		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String profileURL = getURL(baseURL, nodeURL, restool, mode, wcId);

		final Map<String, String> parameters = getParameters(
				requestPropertyObject, restool);

		// create a new node
		createItem(profileURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedCreateResponseCode));

		// created node url
		String createdURL = getURL(baseURL, createdProfileURL, restool,
				mode, wcId);
		jsonBefore = getJson(createdURL, restool);

		// modify the property
		modifyProperty(createdURL, requestModifyPropertyObject, restool);

		// verify the updated property
		jsonAfter = getJson(createdURL, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expected200ResponseCode));
		assertThat(jsonBefore.toString(), not(jsonAfter.toString()));

		// reset the model
		deleteNode(createdURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expected200ResponseCode));
	}

	@TestId(id = "Modify_Property_Negative_Flow_Rest_Test", title = "Verify a HTTP status code of 422 is returned when updating propery not successful.")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_update_property_negative_flow")
	public void verifyWeCanUpdatePropertiesNegativeFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Input("requestPropertyObject") final String requestPropertyObject,
			@Output("expectedErrorType") final String expectedErrorType)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		String deploymentURL = getURL(baseURL, nodeURL, restool, mode, wcId);
		JsonNode json = modifyProperty(deploymentURL, requestPropertyObject,
				restool);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
		assertThat(json.path("messages").size(), is(not(0)));
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
