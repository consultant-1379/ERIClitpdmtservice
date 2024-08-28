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

package com.ericsson.nms.dmt.service.qualifier;

import javax.enterprise.util.AnnotationLiteral;

import com.ericsson.nms.dmt.Mode;

/**
 * Helper class that supports inline instantiation of {@link ServiceQualifier}
 * annotation type instances.
 */
public class ServiceQualifierLiteral extends
		AnnotationLiteral<ServiceQualifier> implements ServiceQualifier {

	private static final long serialVersionUID = 5363844607070134309L;

	private final Mode mode;

	private ServiceQualifierLiteral(Mode mode) {
		this.mode = mode;
	}

	/**
	 * Factory method that creates an instance of
	 * {@link ServiceQualifierLiteral}
	 * 
	 * @param mode
	 *            - refers to the current working copy mode
	 * @return
	 */
	public static ServiceQualifierLiteral from(Mode mode) {
		return new ServiceQualifierLiteral(mode);
	}

	@Override
	public Mode mode() {
		return mode;
	}
}
