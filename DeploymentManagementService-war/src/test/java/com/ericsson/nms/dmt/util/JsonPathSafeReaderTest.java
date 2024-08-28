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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;

public class JsonPathSafeReaderTest {

	private final String json = "{'id' : 'abc', 'colors' : ['blue', 'green'] }";

	@Test
	public void shouldReturnValueForJsonStringWhenPathIsValid() {
		String value = JsonPathSafeReader.read(json, "$.id");
		assertThat(value, is("abc"));
	}

	@Test
	public void shouldReturnNullForJsonStringWhenPathIsInvalid() {
		// When
		String value = JsonPathSafeReader.read(json, "$.name");

		assertThat(value, is(nullValue()));
	}

	@Test
	public void shouldReturnValueForJsonObjectWhenPathIsValid() {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
		String value = JsonPathSafeReader.read(jsonObject, "$.id");
		assertThat(value, is("abc"));
	}

	@Test
	public void shouldReturnNullForJsonObjectWhenPathIsInvalid() {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
		String value = JsonPathSafeReader.read(jsonObject, "$.name");
		assertThat(value, is(nullValue()));
	}

	@Test
	public void shouldReturnListOfValuesWhenPathIsValid() {
		List<String> values = JsonPathSafeReader.readList(json, "$.colors[*]");
		assertThat(values.size(), is(2));
	}

	@Test
	public void shouldReturnEmptyListWhenPathIsInvalid() {
		List<String> values = JsonPathSafeReader.readList(json, "$.fruits[*]");
		assertThat(values.size(), is(0));
	}
}
