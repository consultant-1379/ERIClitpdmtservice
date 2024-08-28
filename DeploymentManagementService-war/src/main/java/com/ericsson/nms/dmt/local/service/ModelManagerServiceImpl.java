/*------------------------------------------------------------------------------
 *******************************************************************************
k * COPYRIGHT Ericsson 2012
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
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.service.*;
import com.ericsson.nms.dmt.service.qualifier.ServiceQualifier;

/**
 * Provides business methods to perform operations on the deployment models
 * persisted in the local database.
 * 
 * @see ModelManagerService
 */
@Stateless(name = "ejb/localModelManagerService")
@TransactionAttribute(REQUIRED)
@ServiceQualifier(mode = LOCAL)
public class ModelManagerServiceImpl implements ModelManagerService {

	@Inject
	private WorkingCopyService workingCopyService;

	@Inject
	private DeploymentModel deploymentModel;

	@Inject
	@ServiceQualifier(mode = LOCAL)
	private TypeDiscoveryService typeService;

	@TransactionAttribute(SUPPORTS)
	@Override
	public Node findNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLocalWorkingCopy(interaction);
		deploymentModel.loadFromXml(workingCopy.getModelDefinition());
		return deploymentModel.findNode(interaction.getNodePath());
	}

	@TransactionAttribute(SUPPORTS)
	@Override
	public List<Node> getNodeChildren(ModelInteraction interaction) {
		Node node = findNode(interaction);
		return node.getChildren();
	}

	@Override
	public void createNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLocalWorkingCopy(interaction);
		deploymentModel.loadFromXml(workingCopy.getModelDefinition());
		deploymentModel.addNode(interaction.getNodePath(),
				interaction.getNodeData());
		workingCopy.setModelDefinition(deploymentModel.exportToXml());
	}

	@Override
	public void updateNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLocalWorkingCopy(interaction);
		deploymentModel.loadFromXml(workingCopy.getModelDefinition());
		deploymentModel.updateNode(interaction.getNodePath(),
				interaction.getNodeData());
		workingCopy.setModelDefinition(deploymentModel.exportToXml());
	}

	@Override
	public void removeNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLocalWorkingCopy(interaction);
		deploymentModel.loadFromXml(workingCopy.getModelDefinition());
		deploymentModel.removeNode(interaction.getNodePath());
		workingCopy.setModelDefinition(deploymentModel.exportToXml());
	}

	@Override
	@TransactionAttribute(SUPPORTS)
	public Set<ItemType> getAddableChildTypes(ModelInteraction interaction) {
		Node node = findNode(interaction);
		return typeService.getAddableChildTypes(node);
	}

	private WorkingCopy findLocalWorkingCopy(ModelInteraction interaction) {
		WorkingCopy workingCopy = workingCopyService.findWorkingCopy(LOCAL,
				interaction.getWorkingCopyId());
		return workingCopy;
	}
}