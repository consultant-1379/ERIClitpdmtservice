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
package com.ericsson.nms.dmt.local.error;

import static com.ericsson.nms.dmt.http.StatusCode.UNPROCESSABLE;

import java.util.*;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ericsson.nms.dmt.error.BusinessException;
import com.ericsson.nms.dmt.error.CausesStatusCode;
import com.ericsson.nms.dmt.rest.serializer.AggregatePropertyExceptionSerializer;

/**
 * This exception aggregates several property validation exceptions
 */
@CausesStatusCode(UNPROCESSABLE)
@JsonSerialize(using = AggregatePropertyExceptionSerializer.class)
public class AggregatePropertyException extends BusinessException {

	private static final long serialVersionUID = 6535773726628310507L;

	private final List<AbstractPropertyException> causes = new ArrayList<>();

	/**
	 * Adds a new cause exception
	 * 
	 * @param cause
	 *            - exception to be aggregated
	 */
	public void addCause(AbstractPropertyException cause) {
		this.causes.add(cause);
	}

	/**
	 * Adds multiple cause exceptions
	 * 
	 * @param cause
	 *            - exception to be aggregated
	 */
	public void addCauses(List<AbstractPropertyException> causes) {
		this.causes.addAll(causes);
	}

	/**
	 * Informs whether the exception contains causes
	 * 
	 * @return
	 */
	public boolean hasCauses() {
		return !causes.isEmpty();
	}

	public List<AbstractPropertyException> getCauses() {
		return Collections.unmodifiableList(causes);
	}
}
