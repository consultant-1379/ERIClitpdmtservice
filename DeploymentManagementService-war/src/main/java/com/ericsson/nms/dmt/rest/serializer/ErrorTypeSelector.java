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

import static org.apache.commons.lang.StringUtils.isNotBlank;

import com.ericsson.nms.dmt.error.CustomErrorType;
import com.ericsson.nms.dmt.error.DmtBaseException;

/**
 * Utility class used to select the proper error type for an exception thrown by
 * the application. It is used by the serialization mechanism to produce proper
 * error messages.
 */
class ErrorTypeSelector {

	/**
	 * Returns the proper error type for the provided exception. If the
	 * exception is annotated with {@link CustomErrorType} and contains a
	 * non-empty value, it gets the error type from there. Otherwise, it returns
	 * the exception class simple name.
	 * 
	 * @param exception
	 *            - exception thrown by the application
	 * @return proper error type
	 */
	public static String getProperErrorTypeFor(DmtBaseException exception) {
		if (hasCustomErrorType(exception)) {
			return getAnnotationFromClass(exception).value();
		}

		return exception.getClass().getSimpleName();
	}

	private static boolean hasCustomErrorType(DmtBaseException exception) {
		CustomErrorType annotation = getAnnotationFromClass(exception);

		return annotation != null && isNotBlank(annotation.value());
	}

	private static CustomErrorType getAnnotationFromClass(
			DmtBaseException exception) {
		return exception.getClass().getAnnotation(CustomErrorType.class);
	}
}
