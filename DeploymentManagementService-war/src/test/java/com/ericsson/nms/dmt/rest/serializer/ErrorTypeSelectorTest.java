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
package com.ericsson.nms.dmt.rest.serializer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ericsson.nms.dmt.error.*;

@SuppressWarnings("serial")
public class ErrorTypeSelectorTest {

	@Test
	public void shouldReturnClassSimpleNameIfExceptionDoesNotContainCustomAnnotation() {
		// Given
		DmtBaseException exception = new ExceptionWithoutAnnotation();

		// When
		String errorType = ErrorTypeSelector.getProperErrorTypeFor(exception);

		// Then
		assertThat(errorType, is("ExceptionWithoutAnnotation"));
	}

	@Test
	public void shouldReturnClassSimpleNameIfExceptionContainsCustomAnnotationWithEmptyValue() {
		// Given
		DmtBaseException exception = new ExceptionWithEmptyValueForAnnotation();

		// When
		String errorType = ErrorTypeSelector.getProperErrorTypeFor(exception);

		// Then
		assertThat(errorType, is("ExceptionWithEmptyValueForAnnotation"));
	}

	@Test
	public void shouldReturnCustomErrorTypeWhenExceptionContainsCustomAnnotationWithValidValue() {
		// Given
		DmtBaseException exception = new ExceptionWithValidValueForAnnotation();

		// When
		String errorType = ErrorTypeSelector.getProperErrorTypeFor(exception);

		// Then
		assertThat(errorType, is("CustomError"));
	}

	private static class ExceptionWithoutAnnotation extends BusinessException {
	}

	@CustomErrorType("")
	private static class ExceptionWithEmptyValueForAnnotation extends
			BusinessException {
	}

	@CustomErrorType("CustomError")
	private static class ExceptionWithValidValueForAnnotation extends
			BusinessException {
	}
}
