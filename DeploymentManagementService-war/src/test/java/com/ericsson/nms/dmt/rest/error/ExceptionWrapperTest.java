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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.interceptor.InvocationContext;

import org.junit.Test;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.ericsson.nms.dmt.live.litp.error.LitpException;
import com.ericsson.nms.dmt.rest.error.ExceptionWrapper;

public class ExceptionWrapperTest {

	private final ExceptionWrapper exceptionWrapper = new ExceptionWrapper();

	@Test(expected = UnexpectedException.class)
	public void shouldWrapException() throws Exception {
		// Given
		InvocationContext mockedContext = mock(InvocationContext.class);
		when(mockedContext.proceed()).thenThrow(mock(RuntimeException.class));

		// When
		exceptionWrapper.wrapException(mockedContext);
	}

	@Test(expected = LitpException.class)
	public void shouldNotWrapException() throws Exception {
		// Given
		InvocationContext mockedContext = mock(InvocationContext.class);
		when(mockedContext.proceed()).thenThrow(mock(LitpException.class));

		// When
		exceptionWrapper.wrapException(mockedContext);
	}
}
