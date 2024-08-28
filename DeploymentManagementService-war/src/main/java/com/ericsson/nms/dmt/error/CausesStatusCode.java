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
package com.ericsson.nms.dmt.error;

import static com.ericsson.nms.dmt.http.StatusCode.INTERNAL_SERVER_ERROR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ericsson.nms.dmt.http.StatusCode;

/**
 * Annotation used to specify the HTTP {@link StatusCode} that should be
 * returned as an outcome for a given exception. The status code informed is
 * captured by the error handling mechanism an then returned to the user. This
 * annotation can be placed in the exception class declaration, in case the
 * status code is static and known at development time, or in an exception
 * attribute that contains the status code value, in case the status code value
 * is defined in runtime. For the later scenario it is only acceptable
 * attributes of the following types: {@link StatusCode}, {@link String} and
 * {@link Integer}.
 */
@Retention(RUNTIME)
@Target(value = { TYPE, FIELD })
public @interface CausesStatusCode {

	/**
	 * Specifies a static status code value for the annotated exception
	 */
	StatusCode value() default INTERNAL_SERVER_ERROR;

}
