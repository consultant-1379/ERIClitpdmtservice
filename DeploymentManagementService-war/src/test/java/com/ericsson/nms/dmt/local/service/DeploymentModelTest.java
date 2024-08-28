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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ericsson.nms.dmt.NodeData;
import com.ericsson.nms.dmt.NodePath;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.*;
import com.ericsson.nms.dmt.local.schema.SchemaRepository;
import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.validation.*;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeCreation;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeRemoval;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeUpdate;
import com.ericsson.nms.dmt.test.helper.LocalNodeFactory;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModelValidator.class)
public class DeploymentModelTest {

	private static Schema schema;
	@Mock
	private DeploymentModelBuilder modelBuilder;

	@Mock
	private SchemaRepository schemaRepository;

	@InjectMocks
	private DeploymentModel deploymentModel;

	@BeforeClass
	public static void initialSetup() {
		schema = SchemaFactory.newInstance("xsd/litp.xsd");
	}

	@Before
	public void setupBeforeEachTest() {
		when(schemaRepository.get("system")).thenReturn(
				schema.getDeclaredElement("system"));
		when(schemaRepository.get("blade")).thenReturn(
				schema.getDeclaredElement("blade"));
		when(schemaRepository.get("os-profile")).thenReturn(
				schema.getDeclaredElement("os-profile"));
		when(schemaRepository.get("bmc")).thenReturn(
				schema.getDeclaredElement("bmc"));

		Node root = LocalNodeFactory.newInstance("model1");
		when(modelBuilder.fromXml(notNull(String.class))).thenReturn(root);
		deploymentModel.loadFromXml("<root />");

		mockStatic(ModelValidator.class);
	}

	@Test
	public void shouldFindNode() {
		// given
		NodePath path = NodePath
				.fromString("/infrastructure/systems/bladex/disks/scsi1");

		// when
		Node node = deploymentModel.findNode(path);

		// then
		assertThat(node.getId(), is("scsi1"));
	}

	@Test(expected = InvalidLocationException.class)
	public void shouldThrowExceptionIfNodeCanNotBeFound() {
		// given
		NodePath path = NodePath.fromString("/ms/xx");

		// when
		deploymentModel.findNode(path);
	}

	@Test(expected = InvalidLocationException.class)
	public void shouldNotCreateNodeIfParentDoesNotExist() {
		// given
		NodePath path = NodePath.fromString("/ms/xx");
		NodeData data = mock(NodeData.class);

		// when
		deploymentModel.addNode(path, data);
	}

	@Test(expected = InvalidTypeException.class)
	public void shouldNotCreateNodeIfTypeDoesNotExist() {
		// given
		NodePath path = NodePath.fromString("/infrastructure/systems");
		NodeData data = new NodeData();
		data.setId("b1");
		data.setType("xyz-type");

		// when
		deploymentModel.addNode(path, data);
	}

	@Test(expected = InvalidChildTypeException.class)
	public void shouldNotCreateNodeIfTypeIsBaseType() {
		// given
		NodePath path = NodePath.fromString("/infrastructure/systems");
		NodeData data = new NodeData();
		data.setId("b1");
		data.setType("system");

		// when
		deploymentModel.addNode(path, data);
	}

	@Test(expected = InvalidChildTypeException.class)
	public void shouldNotCreateNodeIfParentDoesNotSupportChildWithTheGivenType() {
		// given
		NodePath path = NodePath.fromString("/infrastructure/systems");
		NodeData data = new NodeData();
		data.setId("b1");
		data.setType("os-profile");

		// when
		deploymentModel.addNode(path, data);
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldNotCreateNodeIfNodeValidationFails() {
		// given
		NodeCreation operation = mock(NodeCreation.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(operation.on(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.creation()).thenReturn(operation);
		doThrow(mock(AggregatePropertyException.class)).when(ModelValidator.class);
		ModelValidator.validate(operation);

		NodePath path = NodePath.fromString("/infrastructure/systems");
		NodeData data = new NodeData();
		data.setId("b1");
		data.setType("blade");

		// when
		try {
			deploymentModel.addNode(path, data);
		} catch (AggregatePropertyException ex) {
			verifyStatic();
			ModelValidator.validate(operation);
			throw ex;
		}
	}

	@Test
	public void shouldCreateNodeWithAnExtensionType() {
		// given
		NodeCreation operation = mock(NodeCreation.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(operation.on(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.creation()).thenReturn(operation);

		NodePath path = NodePath.fromString("/infrastructure/systems/bladex");
		NodeData data = new NodeData();
		data.setId("bmc");
		data.setType("bmc");
		data.addProperty("ipaddress", "192.168.160.3");
		data.addProperty("username", "usr123");
		data.addProperty("password_key", "123456");

		// when
		deploymentModel.addNode(path, data);

		// then
		Node node = deploymentModel.findNode(path.append("bmc"));
		assertThat(node.getProperties().size(), is(3));
		assertThat(node.hasProperty("ipaddress"), is(true));
		assertThat(node.hasProperty("username"), is(true));
		assertThat(node.hasProperty("password_key"), is(true));
		assertThat(node.getDeclaringSchema().getName(), is("bmc-base"));
		assertThat(node.getActualSchema().getName(), is("bmc"));
	}

	@Test
	public void shouldCreateNodeWithAllCollectionChildrenAppendedByDefault() {
		// given
		NodeCreation operation = mock(NodeCreation.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(operation.on(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.creation()).thenReturn(operation);

		NodePath path = NodePath.fromString("/infrastructure/systems");
		NodeData data = new NodeData();
		data.setId("b1");
		data.setType("blade");

		// when
		deploymentModel.addNode(path, data);

		// then
		Node node = deploymentModel.findNode(path.append("b1"));
		assertThat(node.getChildById("disks"), is(notNullValue()));
		assertThat(node.getChildById("network_interfaces"), is(notNullValue()));
	}

	@Ignore
	@Test
	public void shouldCreateNodeWithAllDefaultPropertiesAppendedIfNotProvided() {
		// TODO implement this test case when the default values are provided in
		// the xsds files
	}

	@Test(expected = InvalidLocationException.class)
	public void shouldNotUpdateNodeIfItDoesNotExist() {
		// given
		NodePath path = NodePath.fromString("/ms/xx");
		NodeData data = mock(NodeData.class);

		// when
		deploymentModel.updateNode(path, data);
	}

	@Test(expected = InvalidRequestException.class)
	public void shouldNotUpdateNodeIfNoPropertyIsProvided() {
		// given
		NodePath path = NodePath.fromString("/ms");
		NodeData data = new NodeData();

		// when
		deploymentModel.updateNode(path, data);
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldNotUpdateNodeIfNodeValidationFails() {
		// given
		NodeUpdate operation = mock(NodeUpdate.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.update()).thenReturn(operation);
		doThrow(mock(AggregatePropertyException.class)).when(ModelValidator.class);
		ModelValidator.validate(operation);

		NodePath path = NodePath.fromString("/infrastructure/systems/bladex");
		NodeData data = new NodeData();
		data.getProperties().put("system_name", "any_name");

		// when
		try {
			deploymentModel.updateNode(path, data);
		} catch (AggregatePropertyException ex) {
			verifyStatic();
			ModelValidator.validate(operation);
			throw ex;
		}
	}

	@Test
	public void shouldUpdateNodeWithNewPropertyValues() {
		// given
		NodeUpdate operation = mock(NodeUpdate.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.update()).thenReturn(operation);

		NodePath path = NodePath
				.fromString("/infrastructure/systems/bladex/disks/scsi1");
		NodeData data = new NodeData();
		data.getProperties().put("bootable", "false");
		data.getProperties().put("name", "xyz");
		data.getProperties().put("size", "30G");
		data.getProperties().put("uuid", "1234");

		// when
		deploymentModel.updateNode(path, data);

		// then
		Node node = deploymentModel.findNode(path);
		assertThat(node.getPropertyValue("bootable"), is("false"));
		assertThat(node.getPropertyValue("name"), is("xyz"));
		assertThat(node.getPropertyValue("size"), is("30G"));
		assertThat(node.getPropertyValue("uuid"), is("1234"));
	}

	@Test
	public void shouldUpdateNodeRemovingPropertiesProvidedWithNullValues() {
		// given
		NodeUpdate operation = mock(NodeUpdate.class);
		when(operation.of(notNull(Node.class))).thenReturn(operation);
		when(ModelValidator.update()).thenReturn(operation);

		NodePath path = NodePath
				.fromString("/infrastructure/systems/bladex/disks/scsi1");
		NodeData data = new NodeData();
		data.getProperties().put("bootable", null);

		// when
		deploymentModel.updateNode(path, data);

		// then
		Node node = deploymentModel.findNode(path);
		assertThat(node.hasProperty("bootable"), is(false));
	}

	@Test(expected = InvalidLocationException.class)
	public void shouldNotDeleteNodeIfItDoesNotExist() {
		// given
		NodePath path = NodePath.fromString("/ms/xx");

		// when
		deploymentModel.removeNode(path);
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldNotDeleteNodeIfNodeValidationFails() {
		// given
		NodeRemoval removal = mock(NodeRemoval.class);
		when(removal.of(notNull(Node.class))).thenReturn(removal);
		when(ModelValidator.removal()).thenReturn(removal);
		doThrow(mock(AggregatePropertyException.class)).when(ModelValidator.class);
		ModelValidator.validate(removal);
		NodePath path = NodePath.fromString("/infrastructure/systems/bladex");

		// when
		try {
			deploymentModel.removeNode(path);
		} catch (AggregatePropertyException ex) {
			verifyStatic();
			ModelValidator.validate(removal);
			throw ex;
		}
	}

	@Test
	public void shouldDeleteNode() {
		// given
		NodeRemoval removal = mock(NodeRemoval.class);
		when(removal.of(notNull(Node.class))).thenReturn(removal);
		when(ModelValidator.removal()).thenReturn(removal);
		NodePath path = NodePath.fromString("/infrastructure/systems/bladex");

		// when
		deploymentModel.removeNode(path);

		// then
		Node parent = deploymentModel.findNode(path.parent());
		assertThat(parent.getChildById("bladex"), is(nullValue()));
	}
}
