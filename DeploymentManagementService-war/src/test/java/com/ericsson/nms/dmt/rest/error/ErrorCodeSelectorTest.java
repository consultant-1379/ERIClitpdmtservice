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

import static com.ericsson.nms.dmt.http.StatusCode.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.ericsson.nms.dmt.error.CausesStatusCode;
import com.ericsson.nms.dmt.error.DmtBaseException;
import com.ericsson.nms.dmt.http.StatusCode;
import com.ericsson.nms.dmt.rest.error.ErrorCodeSelector;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({ "serial" })
public class ErrorCodeSelectorTest {

	@Mock
	private Logger mockedLogger;

	@InjectMocks
	private ErrorCodeSelector statusCodeMatcher;

	@Test
	public void shouldExtractErrorCodeFromCausesStatusCodeAnnotation() {
		assertThat(
				statusCodeMatcher
						.getProperStatusCodeFor(new AnnotatedExceptionClass()),
				is(BAD_REQUEST));
	}

	@Test
	public void shouldReturnInternalServerErrorIfExceptionIsNotAnnotatedWithCausesStatusCode() {
		assertThat(
				statusCodeMatcher
						.getProperStatusCodeFor(new NotAnnotatedException()),
				is(INTERNAL_SERVER_ERROR));
	}

	@Test
	public void shouldExtractErrorCodeFromStatusCodeAttribute() {
		assertThat(
				statusCodeMatcher.getProperStatusCodeFor(new ExceptionWithAnnotatedStatusCodeAttribute(
						NOT_FOUND)), is(NOT_FOUND));
	}

	@Test
	public void shouldExtractErrorCodeFromStringAttribute() {
		assertThat(
				statusCodeMatcher.getProperStatusCodeFor(new ExceptionWithAnnotatedStringStatusCodeAttribute(
						"422")), is(UNPROCESSABLE));
	}

	@Test
	public void shouldExtractErrorCodeFromIntegerAttribute() {
		assertThat(
				statusCodeMatcher.getProperStatusCodeFor(new ExceptionWithAnnotatedIntegerStatusCodeAttribute(
						403)), is(FORBIDDEN));
	}

	public void shouldRegisterLogAndReturnInternalServerErrorIfSpecifiedAttributeTypeIsNotSupported() {
		verify(mockedLogger).warn(anyString(), any(Exception.class));
		assertThat(
				statusCodeMatcher.getProperStatusCodeFor(new ExceptionWithNotSupportedAnnotatedStatusCodeAttribute(
						new BigInteger("422"))), is(INTERNAL_SERVER_ERROR));
	}

	// Exceptions used in the test cases

	@CausesStatusCode(BAD_REQUEST)
	private static class AnnotatedExceptionClass extends DmtBaseException {
	}

	private static class NotAnnotatedException extends DmtBaseException {
	}

	private static class ExceptionWithAnnotatedStatusCodeAttribute extends
			DmtBaseException {

		@CausesStatusCode
		private final StatusCode code;

		public ExceptionWithAnnotatedStatusCodeAttribute(StatusCode code) {
			this.code = code;
		}
	}

	private static class ExceptionWithAnnotatedStringStatusCodeAttribute extends
			DmtBaseException {

		@CausesStatusCode
		private final String code;

		public ExceptionWithAnnotatedStringStatusCodeAttribute(String code) {
			this.code = code;
		}
	}

	private static class ExceptionWithAnnotatedIntegerStatusCodeAttribute
			extends DmtBaseException {

		@CausesStatusCode
		private final int code;

		public ExceptionWithAnnotatedIntegerStatusCodeAttribute(int code) {
			this.code = code;
		}
	}

	private static class ExceptionWithNotSupportedAnnotatedStatusCodeAttribute
			extends DmtBaseException {

		@CausesStatusCode
		private final BigInteger code;

		public ExceptionWithNotSupportedAnnotatedStatusCodeAttribute(
				BigInteger code) {
			this.code = code;
		}
	}
}
