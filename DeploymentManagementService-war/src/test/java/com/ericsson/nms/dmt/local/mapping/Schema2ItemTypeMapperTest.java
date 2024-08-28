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
package com.ericsson.nms.dmt.local.mapping;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

public class Schema2ItemTypeMapperTest {

	private static Schema2ItemTypeMapper mapper;
	private static Schema schema;

	@BeforeClass
	public static void setup() {
		mapper = new Schema2ItemTypeMapper();
		schema = SchemaFactory.newInstance("xsd/litp.xsd");
	}

	@Test
	public void shouldMapSchemaToItemTypeForMs() {
		// Given
		SchemaElement msSchemaElement = schema.getDeclaredElement("ms");

		// When
		ItemType itemType = mapper.map(msSchemaElement);

		// Then
		assertThat(itemType, is(notNullValue()));
		assertThat(itemType.getName(), is("ms"));
		assertThat(itemType.getDescription(), is("Management Server."));
		assertThat(itemType.getChildren().size(), is(10));
	}

	@Test
	public void shouldMapSchemaToItemTypeForSimpleElement() {
		// Given
		SchemaElement msSchemaElement = schema.getDeclaredElement("ms");

		// When
		ItemType itemType = mapper.map(msSchemaElement);

		// Then
		List<ItemTypeProperty> msProperties = itemType.getProperties();
		ItemTypeProperty propertyItem = msProperties.get(0); // hostname
		assertThat(propertyItem.getId(), is("hostname"));
	}

	@Test
	public void shouldMapSchemaToItemTypeForCollectionNonReferenceElement() {
		// Given
		SchemaElement msSchemaElement = schema.getDeclaredElement("ms");

		// When
		ItemType itemType = mapper.map(msSchemaElement);

		// Then
		List<ItemTypeChild> msChildren = itemType.getChildren();
		ItemTypeChild ipaddressesItem = msChildren.get(2);
		assertThat(ipaddressesItem.getName(), is("ipaddresses"));
		assertThat(ipaddressesItem.isCollection(), is(true));
		assertThat(ipaddressesItem.isReference(), is(true));
		assertThat(ipaddressesItem.isRegular(), is(false));
	}

	@Test
	public void shouldMapSchemaToItemTypeForRegularReferenceElement() {
		// Given
		SchemaElement msSchemaElement = schema.getDeclaredElement("ms");

		// When
		ItemType itemType = mapper.map(msSchemaElement);

		// Then
		List<ItemTypeChild> msChildren = itemType.getChildren();
		ItemTypeChild libvirtProviderLinkItem = msChildren.get(4);
		assertThat(libvirtProviderLinkItem.getName(), is("libvirt-provider"));
		assertThat(libvirtProviderLinkItem.isCollection(), is(false));
		assertThat(libvirtProviderLinkItem.isReference(), is(true));
	}

	@Test
	public void shouldMapSchemaToItemTypeForBlade() {
		// Given
		SchemaElement msSchemaElement = schema.getDeclaredElement("blade");

		// When
		ItemType itemType = mapper.map(msSchemaElement);

		// Then
		assertThat(itemType, is(notNullValue()));
		assertThat(itemType.getName(), is("blade"));
		assertThat(itemType.getDescription(), is("A blade system."));
		assertThat(itemType.getChildren().size(), is(3));
	}

	@Test
	public void shouldMapSchemaToItemTypeForRegularNonReferenceElement() {
		// Given
		SchemaElement bladeSchemaElement = schema.getDeclaredElement("blade");

		// When
		ItemType itemType = mapper.map(bladeSchemaElement);

		// Then
		List<ItemTypeChild> bladeChildren = itemType.getChildren();
		ItemTypeChild bmcBaseItem = bladeChildren.get(2);
		assertThat(bmcBaseItem.getName(), is("bmc-base"));
		assertThat(bmcBaseItem.isCollection(), is(false));
		assertThat(bmcBaseItem.isReference(), is(false));
		assertThat(bmcBaseItem.isRegular(), is(true));
	}
}
