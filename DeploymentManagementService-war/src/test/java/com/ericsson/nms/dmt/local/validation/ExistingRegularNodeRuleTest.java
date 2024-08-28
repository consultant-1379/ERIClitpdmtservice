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
package com.ericsson.nms.dmt.local.validation;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ItemExistsException;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;

public class ExistingRegularNodeRuleTest {

	private Node regularParentNode;
	private Node collectionParentNode;

	@Before
	public void setup() {
		ComplexSchemaElement regularChildSchema = mock(ComplexSchemaElement.class);
		when(regularChildSchema.getName()).thenReturn("disk");
		when(regularChildSchema.getSubstitutables()).thenReturn(
				newHashSet("scsi-block-device", "sata-block-device"));
		Node regularChild = mock(Node.class);
		when(regularChild.isRegular()).thenReturn(true);
		when(regularChild.getDeclaringSchema()).thenReturn(regularChildSchema);

		ComplexSchemaElement collectionChildSchema = mock(ComplexSchemaElement.class);
		when(collectionChildSchema.getName()).thenReturn("bmc-base");
		when(collectionChildSchema.getSubstitutables()).thenReturn(
				newHashSet("bmc"));
		Node collectionChild = mock(Node.class);
		when(collectionChild.isCollection()).thenReturn(true);
		when(collectionChild.getDeclaringSchema()).thenReturn(
				collectionChildSchema);

		// setting up the regular parent node
		regularParentNode = mock(Node.class);
		when(regularParentNode.isRegular()).thenReturn(true);
		when(regularParentNode.getChildren()).thenReturn(
				asList(regularChild, collectionChild));

		// setting up the collection parent node
		collectionParentNode = mock(Node.class);
		when(collectionParentNode.getChildren()).thenReturn(
				asList(regularChild));
	}

	@Test
	public void shouldNotThrowExceptionIfChildDoesNotExistInRegularParentNode() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.isRegular()).thenReturn(true);
		when(childNode.getType()).thenReturn("os-profile");

		// when
		new ExistingRegularNodeRule(regularParentNode, childNode).validate();

		// then
		// nothing is expected
	}

	@Test
	public void shouldNotThrowExceptionIfParentHasOtherChildWithTheSameTypeButDifferentCategory() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.isRegular()).thenReturn(true);
		when(childNode.getType()).thenReturn("bmc");

		// when
		new ExistingRegularNodeRule(regularParentNode, childNode).validate();

		// then
		// nothing is expected
	}

	@Test(expected = ItemExistsException.class)
	public void shouldThrowExceptionIfParentNodeAlreadyHasRegularChildWithTheSameTimeAndCategory() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.isRegular()).thenReturn(true);
		when(childNode.getType()).thenReturn("scsi-block-device");

		// when
		new ExistingRegularNodeRule(regularParentNode, childNode).validate();
	}

	@Test
	public void shouldNotThrowExceptionIfChildAlreadyExistInCollectionParentNode() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.isRegular()).thenReturn(true);
		when(childNode.getType()).thenReturn("scsi-block-device");

		// when
		new ExistingRegularNodeRule(collectionParentNode, childNode).validate();

		// then
		// nothing is expected
	}
}
