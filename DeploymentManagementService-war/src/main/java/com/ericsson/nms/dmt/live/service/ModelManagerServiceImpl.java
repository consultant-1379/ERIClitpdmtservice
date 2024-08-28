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
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import com.ericsson.nms.dmt.*;
import com.ericsson.nms.dmt.live.Node;
import com.ericsson.nms.dmt.live.litp.LitpClient;
import com.ericsson.nms.dmt.service.*;
import com.ericsson.nms.dmt.service.qualifier.ServiceQualifier;

/**
 * Provides business methods to perform operations onto the deployment model
 * managed by a LITP server instance.
 * 
 * @see ModelManagerService
 */
@Stateless(name = "ejb/liveModelManager")
@TransactionAttribute(REQUIRED)
@ServiceQualifier(mode = LIVE)
public class ModelManagerServiceImpl implements ModelManagerService {

	@Inject
	private LitpClient litpClient;

	@Inject
	private WorkingCopyService workingCopyService;

	@Inject
	@ServiceQualifier(mode = LIVE)
	private TypeDiscoveryService typeService;

	@TransactionAttribute(SUPPORTS)
	@Override
	public Node findNode(ModelInteraction interaction) {
		findLiveWorkingCopy(interaction);
		return litpClient.findNode(interaction.getNodePath());
	}

	@TransactionAttribute(SUPPORTS)
	@Override
	public List<Node> getNodeChildren(ModelInteraction interaction) {
		findLiveWorkingCopy(interaction);
		NodePath nodePath = interaction.getNodePath();
		Node node = litpClient.findNode(nodePath);

		List<Node> result = new ArrayList<>();
		for (Node child : node.getChildren()) {
			NodePath childPath = nodePath.append(child.getId());
			result.add(litpClient.findNode(childPath));
		}

		return result;
	}

	@Override
	public void createNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLiveWorkingCopy(interaction);
		litpClient.createNode(interaction.getNodePath(),
				interaction.getNodeData());
		workingCopy.refreshLastModified();
	}

	@Override
	public void updateNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLiveWorkingCopy(interaction);
		litpClient.updateNode(interaction.getNodePath(),
				interaction.getNodeData());
		workingCopy.refreshLastModified();
	}

	@Override
	public void removeNode(ModelInteraction interaction) {
		WorkingCopy workingCopy = findLiveWorkingCopy(interaction);
		litpClient.removeNode(interaction.getNodePath());
		workingCopy.refreshLastModified();
	}

	@TransactionAttribute(SUPPORTS)
	@Override
	public Set<ItemType> getAddableChildTypes(ModelInteraction interaction) {
		findLiveWorkingCopy(interaction);
		Node node = litpClient.findNode(interaction.getNodePath());
		return typeService.getAddableChildTypes(node);
	}

	private WorkingCopy findLiveWorkingCopy(ModelInteraction interaction) {
		WorkingCopy workingCopy = workingCopyService.findWorkingCopy(LIVE,
				interaction.getWorkingCopyId());
		return workingCopy;
	}
}