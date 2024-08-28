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
package com.ericsson.nms.dmt.local.validation;

/**
 * This interface defines the methods that a validation rule implementation must
 * provide. The implementation classes acts as strategies (see the GoF pattern)
 * as they contain the rules and logic to perform a specific business validation
 * onto the deployment model. Considering that there are multiple types of
 * business validation that can be applied to a deployment model depending on
 * the performed operation, these implementation classes organize them in a
 * small set of validation instructions and avoid code duplication as they
 * enable the rules to be reused in different operations.
 */
interface ValidationRule {

	/**
	 * Applies the business validation rule against the nodes being modified
	 */
	void validate();
}
