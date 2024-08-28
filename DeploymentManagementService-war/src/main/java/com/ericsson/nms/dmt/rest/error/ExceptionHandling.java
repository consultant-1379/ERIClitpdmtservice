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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.interceptor.InterceptorBinding;

/**
 * Annotation that is meant to be used in the REST resource classes in order to
 * be provided with exception handling aspect.
 * 
 * @see ExceptionHandler
 */
@InterceptorBinding
@Inherited
@Retention(RUNTIME)
@Target(value = { TYPE, METHOD })
public @interface ExceptionHandling {
}
