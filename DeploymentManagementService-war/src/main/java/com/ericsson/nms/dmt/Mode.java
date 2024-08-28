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

package com.ericsson.nms.dmt;

import static org.apache.commons.lang.StringUtils.capitalize;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * This enumeration is used to specify the executions modes of DMT. When the
 * operations being called on DMT refers to a LOCAL working copy, it means that
 * the changes are not submitted to LITP and are stored in a local working copy
 * that contains an ongoing draft of a deployment model. When the operations are
 * in LIVE mode, it means that any change performed is forwarded to a LITP
 * server and will cause a change in the running/managed deployment model.
 */
public enum Mode {

	/**
	 * Refers to deployment models persisted internally on DMT
	 */
	LOCAL,

	/**
	 * Refers to a single deployment model managed by a LITP server
	 */
	LIVE;

	/**
	 * Creates a {@link Mode} value from a string value
	 * 
	 * @param mode
	 *            - string value representing the mode
	 * @return
	 */
	public static Mode fromString(String mode) {
		return valueOf(mode.toUpperCase());
	}

	/**
	 * Returns the {@link Mode} name capitalized
	 * 
	 * @return
	 */
	@JsonValue
	public String getLabel() {
		return capitalize(name().toLowerCase());
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
