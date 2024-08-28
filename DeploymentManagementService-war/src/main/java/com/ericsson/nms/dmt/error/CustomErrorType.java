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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ericsson.nms.dmt.rest.serializer.DefaultExceptionSerializer;

/**
 * Annotation used to override the error type field when serializing the
 * exception. By default, the error type is exception class simple name, but it
 * can be replaced by a custom value if the exception is annotated with
 * {@link CustomErrorType} and a non-empty value is provided.
 * 
 * @see DefaultExceptionSerializer
 */
@Retention(RUNTIME)
@Target(value = TYPE)
public @interface CustomErrorType {

	/**
	 * Specifies a custom error type name for the exception.
	 */
	String value() default EMPTY;
}
