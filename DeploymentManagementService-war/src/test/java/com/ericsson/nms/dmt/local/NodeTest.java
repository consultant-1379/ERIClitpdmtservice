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
package com.ericsson.nms.dmt.local;

import static com.ericsson.nms.dmt.Category.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.nms.dmt.NodePath;
import com.ericsson.nms.dmt.test.helper.LocalNodeFactory;

public class NodeTest {

	private Node nodeRoot;
	private Node nodeMs;
	private Node nodeSoftware;
	private Node nodeBlade;

	@Before
	public void setup() {
		nodeRoot = LocalNodeFactory.newInstance("model1");
		nodeMs = LocalNodeFactory.newInstance("ms");
		nodeSoftware = LocalNodeFactory.newInstance("software");
		nodeBlade = LocalNodeFactory.newInstance("blade");
	}

	@Test
	public void shouldGetRootNodePath() {
		// when
		NodePath nodePath = nodeRoot.getPath();
		// then
		assertThat(nodePath, is(NodePath.root()));

	}

	@Test
	public void shouldGetNonRootNodePath() {
		// when
		NodePath nodePath = nodeRoot.getChildById("ms").getPath();

		// then
		assertThat(nodePath, is(NodePath.fromString("/ms")));
	}

	@Test
	public void shouldRemoveChildNode() {
		// Given
		Node nodeProfiles = nodeSoftware.getChildById("profiles");

		// when
		nodeSoftware.removeChild(nodeProfiles);

		// then
		assertThat(nodeSoftware.getChildById("profiles"), is(nullValue()));
		assertThat(nodeProfiles.getParent(), is(nullValue()));
	}

	@Test
	public void shouldNotRemoveChildNodeIfItDoesNotContainTheChildId() {
		// assert it originally contained 4 items
		assertThat(nodeSoftware.getChildren().size(), is(4));

		// given
		Node invalidChild = nodeBlade.getChildren().iterator().next();

		// when
		nodeSoftware.removeChild(invalidChild);

		// then
		assertThat(nodeSoftware.getChildren().size(), is(4));
		assertThat(invalidChild.getParent(), is(not(nullValue())));
	}

	@Test
	public void shouldGetTypeForRegularNode() {
		assertThat(nodeMs.getType(), is("ms"));
	}

	@Test
	public void shouldGetTypeForCollectionNode() {
		// given
		Node node = nodeMs.getChildById("ipaddresses");

		// then
		assertThat(node.getType(), is("ip-range"));
	}

	@Test
	public void shouldGetTypeForReferenceNode() {
		// given
		Node node = nodeMs.getChildById("libvirtProviderLink");

		// then
		assertThat(node.getType(), is("libvirt-provider"));
	}

	@Test
	public void shouldGetTypeForReferenceCollectionNode() {
		// Given
		Node node = nodeMs.getChildById("services");

		// then
		assertThat(node.getType(), is("ms-service"));

	}

	@Test
	public void shouldGetCategoryForRegularNode() {
		assertThat(nodeMs.getCategory(), is(REGULAR_ITEM));
	}

	@Test
	public void shouldGetCategoryForReferenceCollectionNode() {
		// Given
		Node node = nodeMs.getChildById("ipaddresses");

		// then
		assertThat(node.getCategory(), is(REFERENCE_COLLECTION_ITEM));
	}

	@Test
	public void shouldGetCategoryForCollectionNode() {
		// Given
		Node node = nodeMs.getChildById("services");

		// then
		assertThat(node.getCategory(), is(COLLECTION_ITEM));
	}

	@Test
	public void shouldGetCategoryForReferenceNode() {
		// Given
		Node node = nodeMs.getChildById("libvirtProviderLink");

		// then
		assertThat(node.getCategory(), is(REFERENCE_ITEM));
	}
}
