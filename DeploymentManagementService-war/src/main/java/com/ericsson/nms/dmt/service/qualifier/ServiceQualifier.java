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

import java.lang.annotation.*;

import javax.inject.Qualifier;

import com.ericsson.nms.dmt.Mode;

/**
 * CDI qualifier used in runtime to select the proper implementation for a
 * injection point.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface ServiceQualifier {

	Mode mode();
}
