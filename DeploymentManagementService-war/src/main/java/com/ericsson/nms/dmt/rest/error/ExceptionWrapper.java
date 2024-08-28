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

import javax.interceptor.*;

import com.ericsson.nms.dmt.error.DmtBaseException;
import com.ericsson.nms.dmt.error.UnexpectedException;

/**
 * CDI interceptor responsible for intercepting all calls to methods in the REST
 * resource classes and wrap any unexpected exception raised internally (by DMT
 * or any library that it depends on) in a {@link UnexpectedException}.
 */
@Interceptor
@ExceptionHandling
public class ExceptionWrapper {

	/**
	 * Intercepts a method call and, if an unknown exception is thrown, wraps it
	 * in a {@link UnexpectedException}
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object wrapException(InvocationContext context) throws Exception {
		try {
			return context.proceed();
		} catch (Exception e) {
			if (e instanceof DmtBaseException) {
				throw e;
			}
			throw UnexpectedException.wrap(e);
		}
	}
}
