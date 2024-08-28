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


/**
 * Immutable data container used to request interactions with a working copy. It
 * is used to obtain details about a specific node in the deployment model, as
 * well as to submit changes (create, update and delete) to it.
 */
public class ModelInteraction {

	private final String workingCopyId;
	private NodePath nodePath;
	private NodeData nodeData;

	private ModelInteraction(String workingCopyId) {
		this.workingCopyId = workingCopyId;
	}

	/**
	 * Builder method used to create an instance of this class.
	 * 
	 * @param workingCopyId
	 *            - id of the working copy which the interaction refers to
	 * @return
	 */
	public static Builder workingCopy(String workingCopyId) {
		return new Builder(workingCopyId);
	}

	/**
	 * Builder class used to create instances of {@link ModelInteraction}
	 * objects
	 */
	public static class Builder {

		private final ModelInteraction instance;

		private Builder(String workingCopyId) {
			this.instance = new ModelInteraction(workingCopyId);
		}

		public Builder path(NodePath path) {
			instance.nodePath = path;
			return this;
		}

		public Builder data(NodeData nodeData) {
			instance.nodeData = nodeData;
			return this;
		}

		public ModelInteraction build() {
			return instance;
		}
	}

	public String getWorkingCopyId() {
		return workingCopyId;
	}

	public NodePath getNodePath() {
		return nodePath;
	}

	public NodeData getNodeData() {
		return nodeData;
	}
}
