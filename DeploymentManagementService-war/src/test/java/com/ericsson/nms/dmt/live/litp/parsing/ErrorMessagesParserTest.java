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
package com.ericsson.nms.dmt.live.litp.parsing;

import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContentAsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.ericsson.nms.dmt.live.litp.error.ErrorMessage;

public class ErrorMessagesParserTest {

	private final ErrorMessagesParser errorMessageParser = new ErrorMessagesParser();

	@Test
	public void shouldParseJsonErrorMessages() {
		// Given
		String json = loadFileContentAsString("litp/errors/properties_not_allowed.json");

		// When
		List<ErrorMessage> messages = errorMessageParser.parse(json);

		// Then
		assertThat(messages.size(), is(2));
		assertThat(messages.get(0).getType(), is("PropertyNotAllowedError"));
		assertThat(messages.get(0).getRefersTo(), is("a"));
		assertThat(messages.get(0).getMessage(),
				is("'a' is not an allowed property of deployment"));
	}
}
