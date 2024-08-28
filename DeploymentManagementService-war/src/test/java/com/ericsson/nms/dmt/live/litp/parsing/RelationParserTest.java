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

import static com.ericsson.nms.dmt.Category.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ericsson.nms.dmt.Category;

public class RelationParserTest {

	@Test
	public void shouldIdentifyRegularItemFromItemTypeLinkRelationKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeLinkRelationKey("self");

		// Then
		assertThat(category, is(REGULAR_ITEM));
	}

	@Test
	public void shouldIdentifyCollectionItemFromItemTypeLinkRelationKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeLinkRelationKey("collection-of");

		// Then
		assertThat(category, is(COLLECTION_ITEM));
	}

	@Test
	public void shouldIdentifyReferenceItemFromItemTypeLinkRelationKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeLinkRelationKey("reference-to");

		// Then
		assertThat(category, is(REFERENCE_ITEM));
	}

	@Test
	public void shouldIdentifyReferenceCollectionItemFromItemTypeLinkRelationKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeLinkRelationKey("ref-collection-of");

		// Then
		assertThat(category, is(REFERENCE_COLLECTION_ITEM));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrownErrorIfRelationKeyIsNotValid() {
		RelationParser.categoryFromItemTypeLinkRelationKey(" ");
	}

	@Test
	public void shouldIdentifyRegularItemFromItemTypeNameKey() {
		// When
		Category category = RelationParser.categoryFromItemTypeName("ms");

		// Then
		assertThat(category, is(REGULAR_ITEM));
	}

	@Test
	public void shouldIdentifyCollectionItemFromItemTypeNameKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeName("collection-of-ms-service");

		// Then
		assertThat(category, is(COLLECTION_ITEM));
	}

	@Test
	public void shouldIdentifyReferenceItemFromItemTypeNameKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeName("reference-to-system");

		// Then
		assertThat(category, is(REFERENCE_ITEM));
	}

	@Test
	public void shouldIdentifyReferenceCollectionItemFromItemTypeNameKey() {
		// When
		Category category = RelationParser
				.categoryFromItemTypeName("ref-collection-of-software-item");

		// Then
		assertThat(category, is(REFERENCE_COLLECTION_ITEM));
	}

	@Test
	public void shouldExtractActualItemTypeNameFromRegularItemKey() {
		// When
		String typeName = RelationParser.actualTypeFromItemTypeName("ms");
		// Then
		assertThat(typeName, is("ms"));
	}

	@Test
	public void shouldExtractActualItemTypeNameFromCollectionOfKey() {
		// When
		String typeName = RelationParser
				.actualTypeFromItemTypeName("collection-of-ms-service");

		// Then
		assertThat(typeName, is("ms-service"));
	}

	@Test
	public void shouldExtractActualItemTypeNameFromReferenceToKey() {
		// When
		String typeName = RelationParser
				.actualTypeFromItemTypeName("reference-to-system");

		// Then
		assertThat(typeName, is("system"));
	}

	@Test
	public void shouldExtractActualItemTypeNameFromReferenceCollectionOfKey() {
		// When
		String typeName = RelationParser
				.actualTypeFromItemTypeName("ref-collection-of-software-item");

		// Then
		assertThat(typeName, is("software-item"));
	}

	@Test
	public void shouldReturnNullIfRelationKeyIsNotValid() {
		// When
		String typeName = RelationParser.actualTypeFromItemTypeName(" ");
		// Then
		assertThat(typeName, is(nullValue()));
	}
}
