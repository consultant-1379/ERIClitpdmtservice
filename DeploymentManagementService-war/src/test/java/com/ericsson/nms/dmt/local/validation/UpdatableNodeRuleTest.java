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

import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.PropertyNotAllowedException;

public class UpdatableNodeRuleTest {

	@Test(expected = PropertyNotAllowedException.class)
	public void shouldThrowExceptionWhenNodeIsNotUpdatable() {
		// Given
		Node mockedNode = mock(Node.class);
		when(mockedNode.isCollection()).thenReturn(true);

		// When
		new UpdatableNodeRule(mockedNode).validate();
	}

	@Test
	public void shouldNotThrowExceptionWhenNodeIsUpdatable() {
		// Given
		Node mockedNode = mock(Node.class);
		when(mockedNode.isCollection()).thenReturn(false);

		// When
		new UpdatableNodeRule(mockedNode).validate();
	}
}