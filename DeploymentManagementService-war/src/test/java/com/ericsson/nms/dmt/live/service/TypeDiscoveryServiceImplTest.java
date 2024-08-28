/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.dmt.live.service;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.live.Node;
import com.ericsson.nms.dmt.live.litp.LitpClient;
import com.ericsson.nms.dmt.test.helper.ItemTypeFactory;
import com.ericsson.nms.dmt.test.helper.LiveNodeFactory;

@RunWith(MockitoJUnitRunner.class)
public class TypeDiscoveryServiceImplTest {

	@Mock
	private LitpClient mockedLitpClient;

	@Mock
	private ItemTypeCachedRepository mockedItemTypeRepo;

	@Mock
	private NodePath mockedNodePath;

	@Mock
	private AbstractNode mockedAbstractNode;

	@InjectMocks
	private TypeDiscoveryServiceImpl typeDiscoveryService;

	@Before
	public void setup() {
		ItemType msItemType = ItemTypeFactory.newInstance("ms");
		ItemType bmcItemType = ItemTypeFactory.newInstance("bmc");
		ItemType bmcBaseItemType = ItemTypeFactory.newInstance("bmc-base");
		ItemType bladeItemType = ItemTypeFactory.newInstance("blade");
		ItemType cobblerServiceItemType = ItemTypeFactory
				.newInstance("cobbler-service");

		when(mockedItemTypeRepo.get("ms")).thenReturn(msItemType);
		when(mockedItemTypeRepo.get("bmc")).thenReturn(bmcItemType);
		when(mockedItemTypeRepo.get("bmc-base")).thenReturn(bmcBaseItemType);
		when(mockedItemTypeRepo.get("blade")).thenReturn(bladeItemType);
		when(mockedItemTypeRepo.get("cobbler-service")).thenReturn(
				cobblerServiceItemType);
		when(mockedItemTypeRepo.getAll()).thenReturn(
				asList(msItemType, bladeItemType, bmcItemType, bmcBaseItemType,
						cobblerServiceItemType));

		Node msNode = LiveNodeFactory.newInstance("ms");

		when(mockedLitpClient.findNode(NodePath.fromString("/ms"))).thenReturn(
				msNode);
	}

	@Test
	public void shouldGetItemType() {
		// When
		ItemType item = typeDiscoveryService.getItemType("ms");

		// Then
		assertThat(item, is(notNullValue()));
	}

	@Test
	public void shouldReturnEmptySetWhenNodeIsReference() {
		// Given
		AbstractNode mockedNode = mock(AbstractNode.class);
		when(mockedNode.isReference()).thenReturn(true);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(mockedNode);

		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldNotReturnAnyAddableChildTypesIfRegularNodeHasAllItsChildren() {
		// Given
		Node nodeWithAllChildren = LiveNodeFactory.newInstance("blade2");

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(nodeWithAllChildren);

		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldReturnAddableChildTypesIfRegularNodeDoesntHaveAllItsChildren() {
		// Given
		Node nodeWithMissingChildren = LiveNodeFactory.newInstance("blade1");

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(nodeWithMissingChildren);

		// Then
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next().getName(), is("bmc"));
	}

	@Test
	public void shouldNotReturnReferenceChildrenOfRegularNode() {
		// Given
		Node msWithDeletedReferenceNode = LiveNodeFactory
				.newInstance("ms_with_deleted_reference");

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(msWithDeletedReferenceNode);

		// Then
		assertThat(result.size(), is(0));

	}

	@Test
	public void shouldReturnAddableChildTypesIfCollectionIsNotFull() {
		// Given
		Node collectionNode = mock(Node.class);
		when(collectionNode.getPath()).thenReturn(
				NodePath.fromString("/ms/services"));
		when(collectionNode.getType()).thenReturn("ms-service");
		when(collectionNode.getNumberOfChildren()).thenReturn(9998);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(collectionNode);

		// Then
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next().getName(), is("cobbler-service"));
	}

	@Test
	public void shouldNotReturnAnyAddableChildTypesIfCollectionIsFull() {
		// Given
		Node collectionNode = mock(Node.class);
		when(collectionNode.getPath()).thenReturn(
				NodePath.fromString("/ms/services"));
		when(collectionNode.getType()).thenReturn("ms-service");
		when(collectionNode.getNumberOfChildren()).thenReturn(9999);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(collectionNode);

		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldNotReturnAnyAddableChildTypesIfCollectionIsReference() {
		// Given
		Node collectionNode = mock(Node.class);
		when(collectionNode.isCollection()).thenReturn(true);
		when(collectionNode.isReference()).thenReturn(true);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(collectionNode);

		// Then
		assertThat(result.size(), is(0));
	}
}