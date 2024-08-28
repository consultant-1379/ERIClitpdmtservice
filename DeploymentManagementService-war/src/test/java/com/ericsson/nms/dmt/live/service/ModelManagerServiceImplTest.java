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
package com.ericsson.nms.dmt.live.service;

import static com.ericsson.nms.dmt.Mode.LIVE;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

import java.util.List;
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
import com.ericsson.nms.dmt.service.WorkingCopyService;
import com.ericsson.nms.dmt.test.helper.LiveNodeFactory;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ModelManagerServiceImplTest {

	@Mock
	private LitpClient mockedLitpService;

	@Mock
	private TypeDiscoveryServiceImpl mockedTypeService;

	@Mock
	private WorkingCopyService mockedWorkingCopyService;

	@Mock
	private WorkingCopy mockedWorkingCopy;

	@Mock
	private ModelInteraction mockedInteration;

	@InjectMocks
	private ModelManagerServiceImpl deploymentModelService;

	@Before
	public void setup() {
		when(mockedInteration.getWorkingCopyId()).thenReturn("123");
		when(mockedInteration.getNodePath()).thenReturn(
				NodePath.fromString("/ms"));
		when(mockedInteration.getNodeData()).thenReturn(mock(NodeData.class));

		when(mockedWorkingCopyService.findWorkingCopy(LIVE, "123")).thenReturn(
				mockedWorkingCopy);

		when(mockedLitpService.findNode(NodePath.fromString("/ms")))
				.thenReturn(LiveNodeFactory.newInstance("ms"));
		when(mockedLitpService.findNode(NodePath.fromString("/ms/ipaddresses")))
				.thenReturn(LiveNodeFactory.newInstance("ipaddresses"));
		when(mockedLitpService.findNode(NodePath.fromString("/ms/items")))
				.thenReturn(LiveNodeFactory.newInstance("items"));
		when(mockedLitpService.findNode(NodePath.fromString("/ms/libvirt")))
				.thenReturn(LiveNodeFactory.newInstance("libvirt"));
		when(
				mockedLitpService.findNode(NodePath
						.fromString("/ms/network_profile"))).thenReturn(
				LiveNodeFactory.newInstance("network_profile"));
		when(mockedLitpService.findNode(NodePath.fromString("/ms/services")))
				.thenReturn(LiveNodeFactory.newInstance("services"));
		when(mockedLitpService.findNode(NodePath.fromString("/ms/system")))
				.thenReturn(LiveNodeFactory.newInstance("system"));
	}

	@Test
	public void shouldCreateNode() {
		// When
		deploymentModelService.createNode(mockedInteration);
		// Then
		verify(mockedLitpService).createNode(notNull(NodePath.class),
				notNull(NodeData.class));
		verify(mockedWorkingCopy).refreshLastModified();
	}

	@Test
	public void shouldRemoveNode() {
		// When
		deploymentModelService.removeNode(mockedInteration);
		// Then
		verify(mockedLitpService).removeNode(notNull(NodePath.class));
		verify(mockedWorkingCopy).refreshLastModified();
	}

	@Test
	public void shouldFindNode() {
		// When
		Node node = deploymentModelService.findNode(mockedInteration);
		// Then
		assertThat(node, is(notNullValue()));
	}

	@Test
	public void shouldUpdateNode() {
		// When
		deploymentModelService.updateNode(mockedInteration);
		// Then
		verify(mockedLitpService).updateNode(notNull(NodePath.class),
				notNull(NodeData.class));
		verify(mockedWorkingCopy).refreshLastModified();
	}

	@Test
	public void shouldGetNodeChildren() {
		// When
		List<Node> children = deploymentModelService
				.getNodeChildren(mockedInteration);

		// Then
		assertThat(children.size(), is(6));
	}

	@Test
	public void shouldGetAddableChildTypesForNode() {
		// Given
		when(mockedTypeService.getAddableChildTypes(notNull(Node.class)))
				.thenReturn(
						Sets.newHashSet(mock(ItemType.class),
								mock(ItemType.class)));

		// When
		Set<ItemType> addableChildTypes = deploymentModelService
				.getAddableChildTypes(mockedInteration);

		// Then
		assertThat(addableChildTypes.size(), is(not(0)));
	}
}
