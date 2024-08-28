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
package com.ericsson.nms.dmt.live.litp.error;

import javax.inject.Inject;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.ericsson.nms.dmt.http.StatusCode;
import com.ericsson.nms.dmt.live.litp.parsing.ErrorMessagesParser;

/**
 * Intercepts any error (or HTTP error status codes) caused when calling the
 * LITP REST service. The error messages sent back are then parsed and a
 * {@link LitpException} is raised as the outcome for the intercepted error.
 */
public class LitpErrorInterceptor implements ClientErrorInterceptor {

	@Inject
	private ErrorMessagesParser parser;

	@Override
	public void handle(ClientResponse<?> response) throws RuntimeException {
		StatusCode statusCode = StatusCode.from(response.getStatus());

		if (statusCode.isError()) {
			String payload = response.getEntity(String.class);
			LitpException exception = new LitpException(statusCode);
			exception.addMessages(parser.parse(payload));
			throw exception;
		}
		throw new UnexpectedException(
				"An error occurred when processing the LITP response data.");
	}
}
