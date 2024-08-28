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
package com.ericsson.nms.dmt.mapping;

import com.ericsson.nms.dmt.AbstractNode;
import com.ericsson.nms.dmt.rest.dto.NodeChild;

/**
 * Mapping class used to transform {@link AbstractNode} objects into
 * {@link NodeChild} objects
 * 
 * @see Mapper
 */
public class AbstractNode2NodeChild implements Mapper<AbstractNode, NodeChild> {

	@Override
	public NodeChild map(AbstractNode node) {
		NodeChild nodeChild = new NodeChild();
		nodeChild.setId(node.getId());
		nodeChild.setHasChildren(node.hasChildren());
		return nodeChild;
	}
}
