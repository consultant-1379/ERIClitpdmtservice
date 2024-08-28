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
package com.ericsson.nms.dmt.local.schema.data;

import static com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SchemaAttributeTest {

	@Test
	public void shouldFilterRestrictions() {
		// Given
		SchemaAttribute attribute = SchemaAttribute.builder()
				.restriction(new SimpleTypeRestriction(ENUMERATION))
				.restriction(new SimpleTypeRestriction(PATTERN)).build();

		// Then
		assertThat(attribute.getRestrictions(PATTERN).size(), is(1));
		assertThat(attribute.getRestrictions(ENUMERATION).size(), is(1));
		assertThat(attribute.getRestrictions(LENGTH).size(), is(0));
	}

}
