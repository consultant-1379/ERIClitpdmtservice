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
package com.ericsson.nms.dmt.util;

import java.util.Collections;
import java.util.List;

import net.minidev.json.JSONObject;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

/**
 * Helper class that wraps the JsonPath API and provides a small set of the
 * methods available in that API. Opposed to the JsonPath API, the methods
 * provided by this class are safe, which means that they don't throw exceptions
 * if the path provided cannot be resolved in the current JSON document.
 */
public class JsonPathSafeReader {

	/**
	 * Applies the path to the provided JSON string
	 * 
	 * @param json
	 *            - JSON document
	 * @param jsonPath
	 *            - path to the wished field value
	 * @return object matched by the specified path or null in case it doesn't
	 *         exist
	 */
	public static <T> T read(String json, String jsonPath) {
		try {
			return JsonPath.read(json, jsonPath);
		} catch (InvalidPathException e) {
			return null;
		}
	}

	/**
	 * Applies the path to the provided {@link JSONObject}
	 * 
	 * @param json
	 *            - JSON document
	 * @param jsonPath
	 *            - path to the wished field value
	 * @return object matched by the specified path or null in case it doesn't
	 *         exist
	 */
	public static <T> T read(JSONObject json, String jsonPath) {
		try {
			return JsonPath.read(json, jsonPath);
		} catch (InvalidPathException e) {
			return null;
		}
	}

	/**
	 * Applies the path to the provided JSON string
	 * 
	 * @param json
	 *            - JSON document
	 * @param jsonPath
	 *            - path to the wished field value
	 * @return list of objects matched by the specified path or an empty list in
	 *         case it doesn't exist
	 */
	public static <T> List<T> readList(String json, String jsonPath) {
		try {
			return JsonPath.read(json, jsonPath);
		} catch (InvalidPathException e) {
			return Collections.emptyList();
		}
	}
}
