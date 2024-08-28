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
package com.ericsson.nms.dmt.local.validation;

import static com.ericsson.nms.dmt.local.validation.ModelValidator.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeCreation;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeOperation;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeRemoval;
import com.ericsson.nms.dmt.local.validation.ModelValidator.NodeUpdate;

public class ModelValidatorTest {

	private Node parentNode;
	private Node node;

	@Before
	public void setup() {
		parentNode = mock(Node.class);
		node = mock(Node.class);
	}

	@Test
	public void shouldUseProperRulesToValidateNodeCreation() {
		// when
		NodeCreation operation = creation().of(node).on(parentNode);

		// then
		List<? extends ValidationRule> rules = operation.getRules();
		assertThat(rules.size(), is(5));
		assertThat(rules.get(0), is(instanceOf(UniqueNodeChildIdRule.class)));
		assertThat(rules.get(1), is(instanceOf(ExistingRegularNodeRule.class)));
		assertThat(rules.get(2),
				is(instanceOf(CollectionNodeCardinalityRule.class)));
		assertThat(rules.get(3), is(instanceOf(ValidNodeIdValueRule.class)));
		assertThat(rules.get(4), is(instanceOf(ValidNodePropertiesRule.class)));
	}

	@Test
	public void shouldUseProperRulesToValidateNodeUpdate() {
		// when
		NodeUpdate operation = update().of(node);

		// then
		List<? extends ValidationRule> rules = operation.getRules();
		assertThat(rules.size(), is(2));
		assertThat(rules.get(0), is(instanceOf(UpdatableNodeRule.class)));
		assertThat(rules.get(1), is(instanceOf(ValidNodePropertiesRule.class)));
	}

	@Test
	public void shouldUseProperRulesToValidateNodeDeletion() {
		// when
		NodeRemoval operation = removal().of(node);

		// then
		List<? extends ValidationRule> rules = operation.getRules();
		assertThat(rules.size(), is(1));
		assertThat(rules.get(0), is(instanceOf(DeletableNodeRule.class)));
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void shouldPerformAllRulesWhenValidating() {
		// given
		ValidationRule rule1 = mock(ValidationRule.class);
		ValidationRule rule2 = mock(ValidationRule.class);
		NodeOperation operation = mock(NodeOperation.class);
		when(operation.getRules()).thenReturn((List) asList(rule1, rule2));

		// when
		ModelValidator.validate(operation);

		// then
		verify(rule1).validate();
		verify(rule2).validate();
	}
}
