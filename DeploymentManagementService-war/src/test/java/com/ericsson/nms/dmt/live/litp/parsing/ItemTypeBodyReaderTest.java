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

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.ItemType;

@RunWith(MockitoJUnitRunner.class)
public class ItemTypeBodyReaderTest {

	@InjectMocks
	private ItemTypeBodyReader bodyReader;

	@Test
	public void shouldConsiderItemTypeClassAsReadable() {
		// When
		boolean readable = bodyReader.isReadable(ItemType.class, null, null,
				null);

		// Then
		assertThat(readable, is(true));
	}

	@Test
	public void shouldNotConsiderClassesDifferentThanItemTypeAsReadable() {
		// When
		boolean readable = bodyReader
				.isReadable(Object.class, null, null, null);

		// Then
		assertThat(readable, is(false));
	}

	@Test
	public void shouldReadFromJsonAndReturnAnItemType()
			throws WebApplicationException, IOException {
		// Given
		InputStream json = loadFileContent("litp/item_types/ms.json");

		// When
		ItemType itemType = bodyReader.readFrom(null, null, null, null, null,
				json);

		assertThat(itemType, is(notNullValue()));
	}
}