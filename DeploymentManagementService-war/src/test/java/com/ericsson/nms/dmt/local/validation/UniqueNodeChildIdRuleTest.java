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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.ItemExistsException;

public class UniqueNodeChildIdRuleTest {

	private Node parentNode;

	@Before
	public void setup() {
		parentNode = mock(Node.class);
		when(parentNode.getChildById("blade1")).thenReturn(mock(Node.class));
	}

	@Test
	public void shouldNotThrowExceptionIfChildNodeDoesntExist() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.getId()).thenReturn("blade2");

		// when
		new UniqueNodeChildIdRule(parentNode, childNode).validate();

		// then
		// nothing is expected
	}

	@Test(expected = ItemExistsException.class)
	public void shouldThrowExceptionIfChildNodeAlreadyExist() {
		// given
		Node childNode = mock(Node.class);
		when(childNode.getId()).thenReturn("blade1");

		// when
		new UniqueNodeChildIdRule(parentNode, childNode).validate();
	}
}
