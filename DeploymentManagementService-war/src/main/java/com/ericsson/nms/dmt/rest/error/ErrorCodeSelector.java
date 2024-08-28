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

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.error.CausesStatusCode;
import com.ericsson.nms.dmt.error.DmtBaseException;
import com.ericsson.nms.dmt.http.StatusCode;

/**
 * This class is responsible for finding out the proper HTTP {@link StatusCode}
 * for a given exception. It uses the metadata information provided through the
 * {@link CausesStatusCode} annotation in order to accomplish this task. If the
 * exception doesn't provide any {@link StatusCode}, it returns a default value
 * for it.
 */
class ErrorCodeSelector {

	@Inject
	private Logger logger;

	/**
	 * Returns the proper HTTP {@link StatusCode} for the given
	 * {@link DmtBaseException}
	 * 
	 * @param exception
	 *            exception which is to be matched to a {@link StatusCode}
	 * @return {@link StatusCode}
	 */
	public StatusCode getProperStatusCodeFor(DmtBaseException exception) {
		if (isExceptionClassAnnotated(exception)) {
			return getAnnotationFromClass(exception).value();
		}

		if (isAnyClassFieldAnnotated(exception)) {
			Field field = getAnnotatedClassField(exception);
			try {
				return getStatusCodeFromField(exception, field);
			} catch (Exception e) {
				logger.warn(
						String.format(
								"Unable to get the HTTP status code from the field '%s:%s'",
								e.getClass().getName(), field.getName()), e);
			}
		}

		return INTERNAL_SERVER_ERROR;
	}

	private boolean isExceptionClassAnnotated(DmtBaseException exception) {
		return getAnnotationFromClass(exception) != null;
	}

	private CausesStatusCode getAnnotationFromClass(DmtBaseException exception) {
		return exception.getClass().getAnnotation(CausesStatusCode.class);
	}

	private boolean isAnyClassFieldAnnotated(DmtBaseException exception) {
		return getAnnotatedClassField(exception) != null;
	}

	private Field getAnnotatedClassField(DmtBaseException exception) {
		for (Field field : exception.getClass().getDeclaredFields()) {
			if (field.getAnnotation(CausesStatusCode.class) != null) {
				return field;
			}
		}
		return null;
	}

	private StatusCode getStatusCodeFromField(DmtBaseException exception,
			Field field) throws Exception {
		field.setAccessible(true);
		Object fieldValue = field.get(exception);

		if (fieldValue instanceof StatusCode) {
			return (StatusCode) fieldValue;
		}
		if (fieldValue instanceof String) {
			return StatusCode.from((String) fieldValue);
		}
		if (fieldValue instanceof Integer) {
			return StatusCode.from((Integer) fieldValue);
		}

		throw new IllegalArgumentException("Field type is invalid");
	}
}
