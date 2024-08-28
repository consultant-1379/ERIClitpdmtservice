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
package com.ericsson.nms.dmt;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CategoryTest {

	@Test
	public void shouldNotConsiderRegularItemAsCollection() {
		assertThat(Category.REGULAR_ITEM.isCollection(), is(false));
	}

	@Test
	public void shouldNotConsiderRegularItemAsReference() {
		assertThat(Category.REGULAR_ITEM.isReference(), is(false));
	}

	@Test
	public void shouldConsiderCollectionItemAsCollection() {
		assertThat(Category.COLLECTION_ITEM.isCollection(), is(true));
	}

	@Test
	public void shouldNotConsiderCollectionItemAsReference() {
		assertThat(Category.COLLECTION_ITEM.isReference(), is(false));
	}

	@Test
	public void shouldNotConsiderReferenceItemAsCollection() {
		assertThat(Category.REFERENCE_ITEM.isCollection(), is(false));
	}

	@Test
	public void shouldConsiderReferenceItemAsReference() {
		assertThat(Category.REFERENCE_ITEM.isReference(), is(true));
	}

	@Test
	public void shouldNotConsiderReferenceCollectionItemCollection() {
		assertThat(Category.REFERENCE_COLLECTION_ITEM.isCollection(), is(true));
	}

	@Test
	public void shouldConsiderReferenceCollectionItemAsReference() {
		assertThat(Category.REFERENCE_COLLECTION_ITEM.isReference(), is(true));
	}

}
