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

import static com.ericsson.nms.dmt.Mode.LIVE;
import static com.ericsson.nms.dmt.Mode.LOCAL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.nms.dmt.WorkingCopy;
import com.ericsson.nms.dmt.error.InvalidLocationException;
import com.ericsson.nms.dmt.local.schema.TemplateStore;

@RunWith(MockitoJUnitRunner.class)
public class WorkingCopyServiceTest {

	@InjectMocks
	private WorkingCopyService workingCopyService;

	@Mock
	private EntityManager entityManagerMock;

	@Mock
	private WorkingCopy workingCopyMock;

	@Mock
	private TemplateStore templateStoreMock;

	@Before
	public void setupMockObjects() {
		when(entityManagerMock.find(WorkingCopy.class, "id")).thenReturn(
				workingCopyMock);

	}

	@Test
	public void shouldFindWorkingCopyById() {
		// when
		WorkingCopy workingCopy = workingCopyService.findWorkingCopy("id");

		// then
		assertThat(workingCopy, is(notNullValue()));

	}

	@Test(expected = InvalidLocationException.class)
	public void shouldThrowExceptionWhenWorkingCopyIdIsInvalid() {
		// given
		when(entityManagerMock.find(WorkingCopy.class, "inValidId"))
				.thenReturn(null);

		// when
		workingCopyService.findWorkingCopy("inValidId");
	}

	@Test
	public void shouldFindWorkingCopyByIdAndMode() {
		// given
		when(workingCopyMock.getMode()).thenReturn(LIVE);

		// when
		WorkingCopy workingCopy = workingCopyService
				.findWorkingCopy(LIVE, "id");
		// then
		assertThat(workingCopy, is(notNullValue()));

	}

	@Test(expected = InvalidLocationException.class)
	public void shouldThrowExceptionWhenWorkingCopyModeIsInvalid() {
		// given
		when(workingCopyMock.getMode()).thenReturn(LOCAL);

		// when
		workingCopyService.findWorkingCopy(LIVE, "id");

	}

	@Test
	public void shouldCreateWorkingCopy() {
		// given
		when(templateStoreMock.getDefaultTemplate()).thenReturn(
				"defaultTemplate");

		WorkingCopy wCopy = new WorkingCopy();

		// when
		WorkingCopy workingCopy = workingCopyService.createWorkingCopy(wCopy);

		// then
		assertThat(workingCopy, is(notNullValue()));
		assertThat(workingCopy.getMode(), is(LOCAL));
		assertThat(workingCopy.getModelDefinition(), is("defaultTemplate"));
	}
}
