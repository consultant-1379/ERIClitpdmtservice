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
package com.ericsson.nms.dmt.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class ResourceLoader {

	private ResourceLoader() {
	}

	public static InputStream loadFileContent(String filename) {
		return ResourceLoader.class.getClassLoader().getResourceAsStream(
				filename);
	}

	public static String loadFileContentAsString(String filename) {
		try {
			return IOUtils.toString(loadFileContent(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static URL getFileUrl(String filename) {
		return ResourceLoader.class.getClassLoader().getResource(filename);
	}
}
