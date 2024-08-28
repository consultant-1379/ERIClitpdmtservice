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

import static com.ericsson.nms.dmt.local.schema.data.SchemaConstants.IDENTIFIER_ATTRIBUTE;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.AggregatePropertyException;
import com.ericsson.nms.dmt.local.error.InvalidPropertyException;
import com.ericsson.nms.dmt.local.schema.data.*;

public class ValidNodeIdValueRuleTest {

	private Node node;

	@Before
	public void setup() {
		SimpleTypeRestriction restriction = mock(SimpleTypeRestriction.class);
		when(restriction.isAcceptableValue("valid")).thenReturn(true);

		SchemaAttribute idAttribute = mock(SchemaAttribute.class);
		when(idAttribute.getRestrictions()).thenReturn(asList(restriction));

		ComplexSchemaElement nodeSchema = mock(ComplexSchemaElement.class);
		when(nodeSchema.getAttribute(IDENTIFIER_ATTRIBUTE)).thenReturn(
				idAttribute);

		node = mock(Node.class);
		when(node.getActualSchema()).thenReturn(nodeSchema);
	}

	@Test
	public void shouldNotThrowExceptionIfIdIsValid() {
		// given
		when(node.getId()).thenReturn("valid");

		// when
		new ValidNodeIdValueRule(node).validate();

		// then
		// nothing is expected
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldThrowExceptionIfIdIsInvalid() {
		// given
		when(node.getId()).thenReturn("£$%^&*");

		// when
		try {
			new ValidNodeIdValueRule(node).validate();
		} catch (AggregatePropertyException exception) {
			// then
			assertThat(exception.getCauses().get(0),
					is(instanceOf(InvalidPropertyException.class)));
			throw exception;
		}
	}
}
