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
import com.ericsson.nms.dmt.local.error.CardinalityException;
import com.ericsson.nms.dmt.local.schema.data.ComplexSchemaElement;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;

public class CollectionNodeCardinalityRuleTest {

	private Node mockedNode;

	@Before
	public void setup() {
		SchemaElement collectionChildSchema = mock(SchemaElement.class);
		when(collectionChildSchema.getMaxOccurs()).thenReturn(10);
		ComplexSchemaElement declaringSchema = mock(ComplexSchemaElement.class);
		when(declaringSchema.getFirstChild()).thenReturn(collectionChildSchema);
		mockedNode = mock(Node.class);
		when(mockedNode.getDeclaringSchema()).thenReturn(declaringSchema);
	}

	@Test
	public void shouldNotThrowErrorIfParentNodeIsNotCollection() {
		// given
		when(mockedNode.isCollection()).thenReturn(false);

		// when
		new CollectionNodeCardinalityRule(mockedNode).validate();

		// then
		// nothing is expected
	}

	@Test
	public void shouldNotThrowErrorIfCollectionNodeMaxOccursWasNotReached() {
		// given
		when(mockedNode.isCollection()).thenReturn(true);
		when(mockedNode.getNumberOfChildren()).thenReturn(9);

		// when
		new CollectionNodeCardinalityRule(mockedNode).validate();

		// then
		// nothing is expected
	}

	@Test
	public void shouldNotThrowErrorIfCollectionNodeMaxOccursIsUnbounded() {
		// given
		when(mockedNode.isCollection()).thenReturn(true);
		when(
				mockedNode.getDeclaringSchema().getFirstChild()
						.isMaxOccursUnbounded()).thenReturn(true);
		when(mockedNode.getNumberOfChildren()).thenReturn(10);

		// when
		new CollectionNodeCardinalityRule(mockedNode).validate();

		// then
		// nothing is expected
	}

	@Test(expected = CardinalityException.class)
	public void shouldThrowErrorIfCollectionNodeIsFull() {
		// given
		when(mockedNode.isCollection()).thenReturn(true);
		when(
				mockedNode.getDeclaringSchema().getFirstChild()
						.isMaxOccursUnbounded()).thenReturn(false);
		when(mockedNode.getNumberOfChildren()).thenReturn(10);

		// when
		new CollectionNodeCardinalityRule(mockedNode).validate();
	}
}
