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

import static java.util.Collections.unmodifiableList;
import static org.codehaus.jackson.annotate.JsonMethod.NONE;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer.None;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ericsson.nms.dmt.error.*;
import com.ericsson.nms.dmt.http.StatusCode;

/**
 * A runtime exception thrown by {@link LitpErrorInterceptor} when an error is
 * identified.
 */
@JsonSerialize(using = None.class)
@JsonAutoDetect(NONE)
public class LitpException extends DmtBaseException {

	private static final long serialVersionUID = 3994960985961735130L;

	@CausesStatusCode
	private final StatusCode statusCode;
	private final List<ErrorMessage> messages = new ArrayList<>();

	/**
	 * Constructor that takes the HTTP status code returned by LITP
	 * 
	 * @param statusCode
	 *            - HTTP status code
	 */
	public LitpException(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void addMessage(ErrorMessage errorMessage) {
		this.messages.add(errorMessage);
	}

	public void addMessages(List<ErrorMessage> errorMessages) {
		this.messages.addAll(errorMessages);
	}

	@JsonProperty
	public List<ErrorMessage> getMessages() {
		return unmodifiableList(messages);
	}
}
