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

import static com.ericsson.nms.dmt.Category.REFERENCE_ITEM;
import static com.ericsson.nms.dmt.test.util.ResourceLoader.loadFileContentAsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemTypeParserTest {

	private final ItemTypeParser parser = new ItemTypeParser();

	@Test
	public void shouldParseAllFieldsOfSingleItemTypeJsonResponse() {
		// Given
		String json = loadFileContentAsString("litp/item_types/ms.json");

		// When
		ItemType itemType = parser.parseSingleItemType(json);

		// Then
		assertThat(itemType.getName(), is("ms"));
		assertThat(itemType.getDescription(), is("Management Server."));
		assertThat(itemType.getBaseType(), is(nullValue()));
		assertThat(itemType.getProperties().size(), is(1));
		ItemTypeProperty property = itemType.getProperties().get(0);
		assertThat(property.getId(), is("hostname"));
		assertThat(property.getDescription(), is("hostname for this node."));
		assertThat(
				property.getRegex(),
				is("^(\\.[a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])$"));
		assertThat(property.isRequired(), is(true));
		assertThat(property.getDefaultValue().toString(), is("ms1"));
		assertThat(itemType.getChildren().size(), is(8));

		ItemTypeChild itemTypeChild = itemType.getChildren().get(0);
		assertThat(itemTypeChild.getName(), is("storage-profile-base"));
		assertThat(itemTypeChild.getDescription(),
				is("Reference to storage-profile-base"));
		assertThat(itemTypeChild.getCategory(), is(REFERENCE_ITEM));
		assertThat(((ItemTypeRegularChild) itemTypeChild).isRequired(),
				is(false));
		ItemTypeCollectionChild collectionChild = (ItemTypeCollectionChild) itemType
				.getChildren().get(1);
		assertThat(collectionChild.getMin(), is(0));
		assertThat(collectionChild.getMax(), is(9999));
	}

	@Test
	public void shouldParseItemsThatHaveBaseType() {
		// Given
		String json = loadFileContentAsString("litp/item_types/os-profile.json");

		// When
		ItemType itemType = parser.parseSingleItemType(json);

		// Then
		assertThat(itemType.getBaseType(), is("profile"));
	}

	@Test
	public void shouldParseAllItemTypesJsonResponse() {
		// Given
		String json = loadFileContentAsString("litp/item_types/all.json");

		// When
		List<ItemType> itemType = parser.parseMultipleItemTypes(json);

		// Then
		assertThat(itemType.size(), is(56));
	}
}
