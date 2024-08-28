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
package com.ericsson.nms.dmt.live.litp;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.ClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.ericsson.nms.dmt.live.litp.error.*;
import com.ericsson.nms.dmt.live.litp.parsing.ErrorMessagesParser;

@RunWith(MockitoJUnitRunner.class)
public class LitpErrorInterceptorTest {

	@Mock
	private ErrorMessagesParser mockedParser;

	@Mock
	private ClientResponse<?> mockedResponse;

	@InjectMocks
	private LitpErrorInterceptor interceptor;

	@Test
	public void shouldInterceptHttpErrorAndThrowLitpException() {
		// Given
		List<ErrorMessage> messages = Arrays.asList(mock(ErrorMessage.class));
		when(mockedParser.parse(any(String.class))).thenReturn(messages);
		when(mockedResponse.getStatus()).thenReturn(404);
		when(mockedResponse.getEntity(String.class)).thenReturn(EMPTY);

		try {
			// When
			interceptor.handle(mockedResponse);
		} catch (LitpException e) {
			// Then
			assertThat(e.getStatusCode(), is(notNullValue()));
			assertThat(e.getMessages().size(), is(1));
		}
	}

	@Test(expected = UnexpectedException.class)
	public void shouldInterceptParsingErrorAndThrowUnexpectedException() {
		when(mockedResponse.getStatus()).thenReturn(200);

		interceptor.handle(mockedResponse);
	}
}
