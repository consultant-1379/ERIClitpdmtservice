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
import java.util.List;
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

public class WorkingCopiesRestTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Retrieve_All_Working_Copies_Rest_Test", title = "Should retrieve all working copies and response code 200")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "working_copies_find_all")
	public void shouldReturnAllWorkingCopiesPositiveFlow(
			@Input("workingCopyURL") final String workingCopyURL,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expectedDescription") final String expectedDescription,
			@Output("expectedMode") final String expectedMode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		JsonNode json = getJson(workingCopyURL, restool);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));

		JsonNode description = json.path("working_copies").findValue(
				"description");

		assertThat(description.textValue(), is(expectedDescription));

		JsonNode mode = json.path("working_copies").findValue("mode");

		assertThat(mode.textValue(), is(expectedMode));

	}

	@TestId(id = "Create_A_Working_Copy_Rest_Test", title = "Should create a working copy with response code 200")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "working_copies_create_a_wc")
	public void shouldCreateAWorkingCopyPositiveFlow(
			@Input("wcURL") final String workingCopyURL,
			@Output("requestBody") final String workingCopyToCreate,
			@Output("expectedCreateResponseCode") final String expectedRespondCode,
			@Output("createdWCDescription") final String createdWCDescription,
			@Output("createdWCMode") final String createdWCMode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();

		final Map<String, String> parameters = getParameters(
				workingCopyToCreate, restool);

		// create a working copy
		createItem(workingCopyURL, restool, parameters);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedRespondCode));

		// verify that the created Working Copy exist in database, with correct
		// description and mode
		String createdURL = getCreatedWcURLFromHeader(restool);
		JsonNode json = getJson(createdURL, restool);

		assertThat(json.findValue("description").textValue(),
				is(createdWCDescription));

		assertThat(json.findValue("mode").textValue(), is(createdWCMode));

	}

	@TestId(id = "Create_A_Working_Copy_Rest_Test", title = "Should find a working copy by id")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "working_copies_find_a_working_copy_by_id")
	public void shouldFindAWorkingCopyByIdPositiveFlow(
			@Input("wcURL") final String workingCopyURL,
			@Output("expectedResponseCode") final String expectedRespondCode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();

		// first get all working copies
		JsonNode json = getJson(workingCopyURL, restool);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedRespondCode));

		// then select the first working copy and get its id
		JsonNode workingCopies = json.path("working_copies");
		JsonNode id = workingCopies.get(0).findValue("id");

		// fetch the working copy from the database with that id.
		getJson(workingCopyURL.concat(id.textValue()), restool);

		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedRespondCode));
	}

	private RestTool getDmtRestTool() {
		final DmtOperator dmtRest = getRestOperator();
		final RestTool restTool = dmtRest.retrieveRestData();

		return restTool;
	}

	private DmtOperator getRestOperator() {

		return dmtRestProvider.provide(DmtOperator.class);
	}

	private String getCreatedWcURLFromHeader(final RestTool restool) {
		List<List<String>> list = restool.getLastResponseHeaders();
		String location = list.get(0).get(2);
		String createdWcURL = location.split(":8080/")[1];

		return createdWcURL;
	}

}
