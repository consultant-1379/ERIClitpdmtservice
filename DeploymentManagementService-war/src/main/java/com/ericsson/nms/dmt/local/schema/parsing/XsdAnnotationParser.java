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

import org.xml.sax.*;

import com.sun.xml.xsom.parser.AnnotationContext;
import com.sun.xml.xsom.parser.AnnotationParser;

/**
 * Used by the XSOM mechanism to parse <xs:annotation> elements.
 */
class XsdAnnotationParser extends AnnotationParser {

	private static final String DOCUMENTATION_TAG = "documentation";

	private final StringBuilder documentation = new StringBuilder();

	@Override
	public ContentHandler getContentHandler(AnnotationContext context,
			String parentElementName, ErrorHandler handler,
			EntityResolver resolver) {
		return new SaxContentHandler();
	}

	@Override
	public Object getResult(Object existing) {
		return documentation.toString().trim();
	}

	/**
	 * Looks for the <xs:documentation> element in a XSD document to extracts
	 * its value.
	 * 
	 * @see ContentHandler
	 */
	private class SaxContentHandler implements ContentHandler {

		private boolean parsingDocumentation = false;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (parsingDocumentation) {
				documentation.append(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (localName.equals(DOCUMENTATION_TAG)) {
				parsingDocumentation = false;
			}
		}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes atts) throws SAXException {
			if (localName.equals(DOCUMENTATION_TAG)) {
				parsingDocumentation = true;
			}
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
		}
	}
}