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

import com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType;

public class SimpleTypeRestrictionTest {

	@Test
	public void shouldApplyPatternConstraintToValue() {
		// Given
		SimpleTypeRestriction restriction = new SimpleTypeRestriction(PATTERN);
		restriction.addConstraint("^[A-Za-z]+$");
		restriction.addConstraint("^(male|female)$");

		// Then
		assertThat(restriction.isAcceptableValue("Something"), is(true));
		assertThat(restriction.isAcceptableValue("male"), is(true));
		assertThat(restriction.isAcceptableValue("1234567890"), is(false));
	}

	@Test
	public void shouldConvertAllRestrictionTagNamesToRestrictionType() {
		assertThat(RestrictionType.from("length"), is(LENGTH));
		assertThat(RestrictionType.from("minLength"), is(MIN_LENGTH));
		assertThat(RestrictionType.from("maxLength"), is(MAX_LENGTH));
		assertThat(RestrictionType.from("pattern"), is(PATTERN));
		assertThat(RestrictionType.from("enumeration"), is(ENUMERATION));
		assertThat(RestrictionType.from("totalDigits"), is(TOTAL_DIGITS));
		assertThat(RestrictionType.from("fractionDigits"), is(FRACTION_DIGITS));
		assertThat(RestrictionType.from("minInclusive"), is(MIN_INCLUSIVE));
		assertThat(RestrictionType.from("maxInclusive"), is(MAX_INCLUSIVE));
		assertThat(RestrictionType.from("minExclusive"), is(MIN_EXCLUSIVE));
		assertThat(RestrictionType.from("maxExclusive"), is(MAX_EXCLUSIVE));
		assertThat(RestrictionType.from("whiteSpace"), is(WHITESPACE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenRestrictionTagNameIsInvalid() {
		RestrictionType.from("invalid-tag");
	}
}
