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

public class DMTDeleteNodeRestTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	private OperatorRegistry<DmtOperator> dmtRestProvider;

	@TestId(id = "Delete_Node", title = "Verify a HTTP status code of 200 is returned when node has been successfully deleted")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_delete_node_positive_flow")
	public void verifyWeCanDeleteANodePositiveFlow(
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

		// delete the node
		String deleteURL = getURL(baseURL, deleteNodeUrl, restool, mode,
				wcId);
		deleteNode(deleteURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
	}

	@TestId(id = "Delete_Node", title = "Verify a HTTP status code of 404 is returned when attempting to delete a node that has been already deleted")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_delete_node_negative_flow")
	public void verifyWeCanDeleteANodeNegativeFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("requestBody") final String jsonTocreateNode,
			@Input("deleteCorrectUrl") final String deleteNodeUrl,
			@Input("mode") final String mode,
			@Output("expectedErrorMessage") final String expectedErrorMessage,
			@Output("expectedErrorType") final String expectedErrorType,
			@Output("expectedResponseCode") final String expectedResponseCode,
			@Output("expected200ResponseCode") final String expected200ResponseCode,
			@Output("expectedCreateResponseCode") final String expectedCreateResponseCode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);

		// create a new node
		final Map<String, String> parameters = getParameters(jsonTocreateNode,
				restool);

		String createURL = getURL(baseURL, nodeURL, restool, mode, wcId);
		createItem(createURL, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedCreateResponseCode));

		// delete the node
		String nodeToBeDeleted = getURL(baseURL, deleteNodeUrl, restool,
				mode, wcId);
		deleteNode(nodeToBeDeleted, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expected200ResponseCode));

		// delete the same node again
		JsonNode json = deleteNode(nodeToBeDeleted, restool, parameters);
		assertThat(restool.getLastResponseCodes().get(0).toString(),
				is(expectedResponseCode));
		assertThat(json.path("messages").iterator().next().path("message")
				.asText(), is(expectedErrorMessage));
		assertThat(json.path("messages").iterator().next().path("type")
				.asText(), is(expectedErrorType));
	}

	@TestId(id = "Delete_Collection_Node", title = "Verify a HTTP status code of 405(MethodNotAllowedError) is returned when attempting to delete a collection node")
	@Context(context = { Context.REST })
	@Test(enabled = true)
	@DataDriven(name = "dmt_rest_delete_collection_negative_flow")
	public void verifyWeCantDeleteACollectionNodeNegativeFlow(
			@Input("baseURL") final String baseURL,
			@Input("nodeURL") final String nodeURL,
			@Input("mode") final String mode,
			@Output("expectedErrorMessage") final String expectedErrorMessage,
			@Output("expectedErrorType") final String expectedErrorType,
			@Output("expectedResponseCode") final String expectedResponseCode)
			throws JSONException, JsonProcessingException, IOException {

		final RestTool restool = getDmtRestTool();
		String wcId = getWorkingCopyId(baseURL, restool, mode);
		final Map<String, String> parameters = getParameters("", restool);

		// deployments collection url
		String nodeToBeDeleted = getURL(baseURL, nodeURL, restool, mode,
				wcId);

		// delete the collection node
		JsonNode json = deleteNode(nodeToBeDeleted, restool, parameters);

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
