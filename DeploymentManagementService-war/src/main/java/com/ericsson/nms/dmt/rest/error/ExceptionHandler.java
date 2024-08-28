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

import javax.inject.Inject;
import javax.interceptor.*;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;

import com.ericsson.nms.dmt.error.DmtBaseException;
import com.ericsson.nms.dmt.error.UnexpectedException;

/**
 * CDI interceptor that is responsible for intercepting all calls to methods in
 * the REST resource classes and handle any exception thrown from them in a
 * gracefully and decoupled way. In order to work properly, the REST resource
 * classes must be annotated with {@link @ExceptionHandling}. The interceptor
 * logs any {@link UnexpectedException} to the server logging files and return
 * the proper HTTP status code for the captured exception.
 */
@Interceptor
@ExceptionHandling
public class ExceptionHandler {

	@Inject
	private ErrorCodeSelector statusCodeMatcher;

	/**
	 * Intercepts a method call and handle the exception
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object handleException(InvocationContext context) throws Exception {
		try {
			return context.proceed();
		} catch (DmtBaseException exception) {
			if (isLoggagle(exception)) {
				LoggerFactory.getLogger(
						context.getTarget().getClass().getSuperclass()).error(
						"An unexpected exception was thrown", exception);
			}
			return Response
					.status(statusCodeMatcher.getProperStatusCodeFor(exception)
							.getCode()).entity(exception).build();
		}
	}

	private boolean isLoggagle(DmtBaseException exception) {
		return exception instanceof UnexpectedException;
	}
}
