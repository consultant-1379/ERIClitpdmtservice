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
package com.ericsson.nms.dmt.rest.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * Helper class that provides a way to wrap an object as a nested value for a
 * key. This new key/value pair is taken into account by the Jackson engine
 * during the serialization/deserialization process.
 */
public class JsonObjectWrapper<T> {

	private final Map<String, T> objects = new LinkedHashMap<>();

	/**
	 * Default constructor
	 */
	public JsonObjectWrapper() {
	}

	/**
	 * Constructor that takes the first key/value pair to be wrapped.
	 * 
	 * @param name
	 *            - key
	 * @param object
	 *            - value
	 */
	public JsonObjectWrapper(String name, T object) {
		this.objects.put(name, object);
	}

	/**
	 * Helper factory method that takes a key/value pair to be wrapped.
	 * 
	 * @param name
	 *            - key
	 * @param object
	 *            - object
	 * @return
	 */
	public static <Z> JsonObjectWrapper<Z> wrap(String name, Z object) {
		return new JsonObjectWrapper<Z>(name, object);
	}

	/**
	 * Meant to be used during the serialization process in order to produce a
	 * JSON document with the keys and values wrapped by this object
	 * 
	 * @return
	 */
	@JsonAnyGetter
	public Map<String, T> any() {
		return objects;
	}

	/**
	 * Meant to be used during the deserialization process in order to produce a
	 * {@link JsonObjectWrapper} from a JSON document
	 * 
	 * @return
	 */
	@JsonAnySetter
	public void set(String name, T value) {
		objects.put(name, value);
	}
}
