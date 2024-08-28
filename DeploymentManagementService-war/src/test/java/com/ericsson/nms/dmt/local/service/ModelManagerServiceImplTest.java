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

import static com.ericsson.nms.dmt.Mode.LOCAL;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
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
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.service.TypeDiscoveryService;
import com.ericsson.nms.dmt.service.WorkingCopyService;
import com.ericsson.nms.dmt.test.helper.LocalNodeFactory;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ModelManagerServiceImplTest {

	@Mock
	private WorkingCopyService mockedWorkingCopyService;

	@Mock
	private DeploymentModel mockedDeploymentModel;

	@Mock
	private TypeDiscoveryService mockedTypeService;

	@Mock
	private WorkingCopy mockedWorkingCopy;

	private ModelInteraction modelInteration;

	@InjectMocks
	private ModelManagerServiceImpl modelManager;

	@Before
	public void setup() {
		NodePath path = NodePath.fromString("/test/blade");
		Node node = LocalNodeFactory.newInstance("blade");
		modelInteration = ModelInteraction.workingCopy("123").path(path)
				.data(mock(NodeData.class)).build();
		when(mockedWorkingCopy.getModelDefinition()).thenReturn("<before />");
		when(mockedWorkingCopyService.findWorkingCopy(LOCAL, "123"))
				.thenReturn(mockedWorkingCopy);
		when(mockedDeploymentModel.findNode(path)).thenReturn(node);
		when(mockedDeploymentModel.exportToXml()).thenReturn("<after />");
		when(mockedTypeService.getAddableChildTypes(node)).thenReturn(
				Sets.newHashSet(mock(ItemType.class), mock(ItemType.class)));
	}

	@Test
	public void shouldFindNode() {
		Node node = modelManager.findNode(modelInteration);

		assertThat(node, is(notNullValue()));
	}

	@Test
	public void shouldGetNodeChildren() {
		List<Node> children = modelManager.getNodeChildren(modelInteration);

		assertThat(children.size(), is(not(0)));
	}

	@Test
	public void shouldCreateNode() {
		modelManager.createNode(modelInteration);

		verify(mockedDeploymentModel).loadFromXml("<before />");
		verify(mockedWorkingCopy).setModelDefinition("<after />");
	}

	@Test
	public void shouldUpdateNode() {
		modelManager.updateNode(modelInteration);

		verify(mockedDeploymentModel).loadFromXml("<before />");
		verify(mockedWorkingCopy).setModelDefinition("<after />");
	}

	@Test
	public void shouldRemoveNode() {
		modelManager.removeNode(modelInteration);

		verify(mockedDeploymentModel).loadFromXml("<before />");
		verify(mockedWorkingCopy).setModelDefinition("<after />");
	}

	@Test
	public void shouldGetAddableItemTypes() {
		Set<ItemType> addableChildTypes = modelManager
				.getAddableChildTypes(modelInteration);

		assertThat(addableChildTypes.size(), is(not(0)));
	}
}
