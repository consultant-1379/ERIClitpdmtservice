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
package com.ericsson.nms.dmt.rest.error;

import static com.ericsson.nms.dmt.http.StatusCode.INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.nms.dmt.error.*;
import com.ericsson.nms.dmt.rest.error.ExceptionHandler;
import com.ericsson.nms.dmt.rest.error.ErrorCodeSelector;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class ExceptionHandlerTest {

	@Mock
	private ErrorCodeSelector mockedMatcher;

	@Mock
	private InvocationContext mockedContext;

	@InjectMocks
	private ExceptionHandler handler;

	@Before
	public void setup() {
		mockStatic(LoggerFactory.class);
		Object mockedTarget = mock(Object.class);
		Logger mockedLogger = mock(Logger.class);
		when(mockedContext.getTarget()).thenReturn(mockedTarget);
		when(LoggerFactory.getLogger(any(Class.class)))
				.thenReturn(mockedLogger);
		when(mockedMatcher.getProperStatusCodeFor(any(DmtBaseException.class)))
				.thenReturn(INTERNAL_SERVER_ERROR);
	}

	@Test
	public void shouldInterceptAndLogUnexpectedException() throws Exception {
		// Given
		when(mockedContext.proceed())
				.thenThrow(mock(UnexpectedException.class));

		// When
		Response response = (Response) handler.handleException(mockedContext);

		// Then
		verifyStatic();
		LoggerFactory.getLogger(any(Class.class));
		assertThat(response.getStatus(), is(INTERNAL_SERVER_ERROR.getCode()));
		assertThat(response.getEntity(),
				is(instanceOf(UnexpectedException.class)));
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotInterceptAnyExceptionThatDoesNotBelongToDmtBaseExceptionHierarchy()
			throws Exception {
		// Given
		when(mockedContext.proceed()).thenThrow(mock(RuntimeException.class));

		// When
		handler.handleException(mockedContext);
	}

	@Test
	public void shouldInterceptButNotLogBusinessException() throws Exception {
		// Given
		when(mockedContext.proceed()).thenThrow(mock(BusinessException.class));

		// When
		Response response = (Response) handler.handleException(mockedContext);

		// Then
		verifyStatic(times(0));
		LoggerFactory.getLogger(any(Class.class));
		assertThat(response.getEntity(),
				is(instanceOf(BusinessException.class)));
	}
}
