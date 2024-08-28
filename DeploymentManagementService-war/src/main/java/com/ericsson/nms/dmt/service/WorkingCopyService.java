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
package com.ericsson.nms.dmt.service;

import static com.ericsson.nms.dmt.Mode.LOCAL;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ericsson.nms.dmt.Mode;
import com.ericsson.nms.dmt.WorkingCopy;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.schema.TemplateStore;

/**
 * Business component that allows clients to perform operations in the local
 * data storage to create and get details about existing working copies.
 */
@Stateless
@TransactionAttribute(SUPPORTS)
public class WorkingCopyService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TemplateStore templateStore;

	/**
	 * Returns an existing working copy for the specified id.
	 * 
	 * @param id
	 *            - working copy id
	 * @return
	 * @throws InvalidLocationException
	 *             if the working copy does not exist
	 */
	public WorkingCopy findWorkingCopy(String id) {
		WorkingCopy workingCopy = em.find(WorkingCopy.class, id);

		if (workingCopy != null) {
			return workingCopy;
		}
		throw new InvalidLocationException(String.format(
				"Deployment model id = '%s' was not found.", id));
	}

	/**
	 * Returns an existing working copy for the specified mode and id.
	 * 
	 * @param mode
	 *            - working copy mode
	 * @param id
	 *            - working copy id
	 * @return
	 * @throws InvalidLocationException
	 *             if the working copy does not exist for the specified mode
	 */
	public WorkingCopy findWorkingCopy(Mode mode, String id) {
		WorkingCopy workingCopy = findWorkingCopy(id);

		if (workingCopy.getMode() == mode) {
			return workingCopy;
		}

		throw new InvalidLocationException(String.format(
				"Deployment model id = '%s' was not found in '%s' mode.", id,
				mode));
	}

	/**
	 * Returns a list of all existing working copies
	 * 
	 * @return
	 */
	public List<WorkingCopy> findWorkingCopies() {
		return em.createNamedQuery(WorkingCopy.FIND_ALL, WorkingCopy.class)
				.getResultList();
	}

	/**
	 * Creates a new working copy in the local data storage. By default, the
	 * {@link Mode} LOCAL is assigned to it as only one LIVE working copy can
	 * exist per DMT installation.
	 * 
	 * @param workingCopy
	 *            - working copy information to be persisted
	 * @return
	 */
	@TransactionAttribute(REQUIRED)
	public WorkingCopy createWorkingCopy(WorkingCopy workingCopy) {
		workingCopy.setModelDefinition(templateStore.getDefaultTemplate());
		workingCopy.setMode(LOCAL);
		em.persist(workingCopy);
		return workingCopy;
	}
}
