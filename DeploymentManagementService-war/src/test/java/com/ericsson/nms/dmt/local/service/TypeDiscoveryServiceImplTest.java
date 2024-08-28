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
package com.ericsson.nms.dmt.local.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.AbstractNode;
import com.ericsson.nms.dmt.ItemType;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.mapping.Schema2ItemTypeMapper;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.*;
import com.ericsson.nms.dmt.test.helper.LocalNodeFactory;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

@RunWith(MockitoJUnitRunner.class)
public class TypeDiscoveryServiceImplTest {

	@Spy
	private Schema2ItemTypeMapper mockedMapper;

	@Mock
	private SchemaRepository mockedSchemaRepository;

	@Mock
	private ComplexSchemaElement mockedComplexSchemaElement;

	@Mock
	private SchemaElement mockedSchemaElement;

	@Mock
	private Node mockedNode;

	@InjectMocks
	private TypeDiscoveryServiceImpl typeDiscoveryService;

	private static Schema litpSchema;

	@BeforeClass
	public static void loadLitpSchema() {
		litpSchema = SchemaFactory.newInstance("xsd/litp.xsd");
	}

	@Before
	public void setupMockObjects() {
		when(mockedSchemaRepository.get("ms")).thenReturn(
				litpSchema.getDeclaredElement("ms"));
		when(mockedSchemaRepository.get("bmc")).thenReturn(
				litpSchema.getDeclaredElement("bmc"));
		when(mockedSchemaRepository.get("bmc-base")).thenReturn(
				litpSchema.getDeclaredElement("bmc-base"));
		when(mockedSchemaRepository.get("blade")).thenReturn(
				litpSchema.getDeclaredElement("blade"));
		when(mockedSchemaRepository.get("cobbler-service")).thenReturn(
				litpSchema.getDeclaredElement("cobbler-service"));
	}

	@Test
	public void shouldGetItemType() {
		// When
		ItemType result = typeDiscoveryService.getItemType("ms");
		// Then
		assertThat(result, is(notNullValue()));
	}

	@Test(expected = InvalidLocationException.class)
	public void shouldThrowInvalidLocationExceptionWhenElementReturnedBySchemaRepositoryIsNull() {
		// Given
		when(mockedSchemaRepository.get(anyString())).thenReturn(null);
		// When
		typeDiscoveryService.getItemType("ms");
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
		Node blade = LocalNodeFactory.newInstance("blade");

		// When
		Set<ItemType> result = typeDiscoveryService.getAddableChildTypes(blade);

		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldReturnAddableChildTypesIfRegularNodeDoesntHaveAllItsChildren() {
		// Given
		Node nodeWithMissingChildren = LocalNodeFactory
				.newInstance("blade_without_all_children");

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
		Node msWithDeletedReferenceNode = LocalNodeFactory
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
		Node msNode = LocalNodeFactory.newInstance("ms_collection_not_full");
		List<Node> collectionChildren = msNode.getChildren();
		Node servicesNode = collectionChildren.get(0);
		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(servicesNode);

		// Then
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next().getName(), is("cobbler-service"));
	}

	@Test
	public void shouldNotReturnAnyAddableChildTypesIfCollectionIsFull() {
		// Given
		when(mockedNode.isReference()).thenReturn(false);
		when(mockedNode.isCollection()).thenReturn(true);
		when(mockedNode.getDeclaringSchema()).thenReturn(
				mockedComplexSchemaElement);
		when(mockedComplexSchemaElement.getFirstChild()).thenReturn(
				mockedSchemaElement);
		when(mockedNode.getNumberOfChildren()).thenReturn(1);
		when(mockedSchemaElement.getMaxOccurs()).thenReturn(1);
		when(mockedSchemaElement.isMaxOccursUnbounded()).thenReturn(false);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(mockedNode);

		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldNotReturnAnyAddableChildTypesIfCollectionIsReference() {
		// Given
		Node msNode = LocalNodeFactory
				.newInstance("ms_with_reference_collection");
		List<Node> collectionChildren = msNode.getChildren();
		Node itemsNode = collectionChildren.get(0);

		// When
		Set<ItemType> result = typeDiscoveryService
				.getAddableChildTypes(itemsNode);

		// Then
		assertThat(result.size(), is(0));
	}

}
