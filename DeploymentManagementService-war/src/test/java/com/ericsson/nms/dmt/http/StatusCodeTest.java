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
package com.ericsson.nms.dmt.http;

import static com.ericsson.nms.dmt.http.StatusCode.ACCEPTED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StatusCodeTest {

	@Test
	public void shouldReturnStatusCodeFromStringValue() {
		assertThat(StatusCode.from("202"), is(ACCEPTED));
	}

	@Test
	public void shouldReturnStatusCodeFromIntValue() {
		assertThat(StatusCode.from(202), is(ACCEPTED));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionForInvalidStringValue() {
		StatusCode.from("600");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionForInvalidIntValue() {
		StatusCode.from(600);
	}
}
