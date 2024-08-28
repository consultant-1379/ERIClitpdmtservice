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
package com.ericsson.nms.dmt.local.schema;

import javax.enterprise.context.ApplicationScoped;

/**
 * This component provides templates of deployment models for creating new
 * working copies.
 */
@ApplicationScoped
public class TemplateStore {

	private String defaultTemplate;

	void setDefaultTemplate(String template) {
		this.defaultTemplate = template;
	}

	/**
	 * Returns the default deployment model template
	 * 
	 * @return
	 */
	public String getDefaultTemplate() {
		return defaultTemplate;
	}
}
