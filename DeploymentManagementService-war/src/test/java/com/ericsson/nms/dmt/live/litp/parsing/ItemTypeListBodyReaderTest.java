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
package com.ericsson.nms.dmt.live.litp.parsing;

import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.ItemType;

@RunWith(MockitoJUnitRunner.class)
public class ItemTypeListBodyReaderTest {

	@Mock
	private ParameterizedType typeMock;

	@InjectMocks
	private ItemTypeListBodyReader listBodyReader;

	@Test
	public void shouldConsiderListOfItemTypeAsReadable() {
		// Given
		when(typeMock.getActualTypeArguments()).thenReturn(
				new Type[] { ItemType.class });

		// When
		boolean readable = listBodyReader.isReadable(List.class, typeMock,
				null, null);

		// Then
		assertThat(readable, is(true));
	}

	@Test
	public void shouldNotConsiderListsOfClassesDifferentThanItemTypeAsReadable() {
		// Given
		when(typeMock.getActualTypeArguments()).thenReturn(
				new Type[] { Object.class });

		// When
		boolean readable = listBodyReader.isReadable(List.class, typeMock,
				null, null);

		// Then
		assertThat(readable, is(false));
	}

	@Test
	public void shouldNotConsiderClassesDifferentThanListOfItemTypeAsReadable() {
		// Given
		when(typeMock.getActualTypeArguments()).thenReturn(
				new Type[] { ItemType.class });

		// When
		boolean readable = listBodyReader.isReadable(Object.class, typeMock,
				null, null);

		// Then
		assertThat(readable, is(false));
	}

	@Test
	public void shouldReadFromJsonAndReturnAnItemType()
			throws WebApplicationException, IOException {
		// Given
		InputStream json = loadFileContent("litp/item_types/all.json");

		// When
		List<ItemType> itemType = listBodyReader.readFrom(null, null, null,
				null, null, json);

		// Then
		assertThat(itemType, is(notNullValue()));
	}
}
