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
package com.ericsson.nms.dmt;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class NodePathTest {

	@Test
	public void shouldReturnRootPath() {
		assertThat(NodePath.root().toString(), is("/"));
	}

	@Test
	public void shouldCreateFromString() {
		assertThat(NodePath.fromString("/ms").toString(), is("/ms"));
	}

	@Test
	public void shouldRemoveExtraSlashes() {
		assertThat(NodePath.fromString("/ms//deployments").toString(),
				is("/ms/deployments"));
	}

	@Test
	public void shouldRemoveUnecessaryLastSlash() {
		assertThat(NodePath.fromString("/ms/").toString(), is("/ms"));
	}

	@Test
	public void shouldPrependPathToTheOriginalPath() {
		assertThat(NodePath.fromString("/deployments").prepend("/ms")
				.toString(), is("/ms/deployments"));
	}

	@Test
	public void shouldAppendPathToTheOriginalPath() {
		assertThat(
				NodePath.fromString("/ms").append("/deployments").toString(),
				is("/ms/deployments"));
	}

	@Test
	public void shouldReturnParentPath() {
		assertThat(NodePath.fromString("/ms/deployments").parent().toString(),
				is("/ms"));
	}

	@Test
	public void shouldReturnRootAsParentPath() {
		assertThat(NodePath.fromString("/ms").parent().toString(), is("/"));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrownExceptionIfPathHasNoParent() {
		NodePath.fromString("/").parent();
	}

	@Test
	public void shouldReturnElementsOfPath() {
		List<String> elements = NodePath.fromString("/ms/plans/plan")
				.elements();

		assertThat(elements.size(), is(3));
		assertThat(elements.get(0), is("ms"));
		assertThat(elements.get(1), is("plans"));
		assertThat(elements.get(2), is("plan"));
	}

	@Test
	public void shouldReturnZeroElementsForRootPath() {
		List<String> elements = NodePath.fromString("/").elements();

		assertThat(elements.size(), is(0));
	}
}
