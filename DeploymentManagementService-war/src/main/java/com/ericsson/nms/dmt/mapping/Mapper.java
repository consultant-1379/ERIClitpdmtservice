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
package com.ericsson.nms.dmt.mapping;

/**
 * Interface that defines the operations for mapping classes. Mapping classes
 * are used to transform instances of a certain object into another.
 * 
 * @param <T>
 *            - Input type
 * @param <S>
 *            - Output type
 */
public interface Mapper<T, S> {

	/**
	 * Transforms an instance of T type into a S type
	 * 
	 * @param object
	 *            - object to be transformed
	 * @return - instance of S containing the data extracted from T
	 */
	S map(T object);
}
