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
package com.ericsson.nms.dmt.local.schema.parsing;

import com.sun.xml.xsom.parser.AnnotationParser;
import com.sun.xml.xsom.parser.AnnotationParserFactory;

/**
 * Factory class of {@link XsdAnnotationParser} objects. It takes advantage of
 * an existing hook in XSOM that allows client applications to parse annotations
 * in schema into an application specific data structure. The parsing
 * functionality is provided by the {@link XsdAnnotationParser} class
 * 
 * @see AnnotationParserFactory
 */
class XsdAnnotationParserFactory implements AnnotationParserFactory {

	@Override
	public AnnotationParser create() {
		return new XsdAnnotationParser();
	}
}
