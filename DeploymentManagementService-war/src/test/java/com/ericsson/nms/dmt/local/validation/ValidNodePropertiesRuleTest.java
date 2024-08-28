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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.*;

import com.ericsson.nms.dmt.local.Node;
import com.ericsson.nms.dmt.local.error.*;
import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;
import com.ericsson.nms.dmt.test.helper.SchemaFactory;

public class ValidNodePropertiesRuleTest {

	private static Schema schema;
	private final Node.Builder nodeBuilder = Node.builder();

	@BeforeClass
	public static void initialSetup() {
		schema = SchemaFactory.newInstance("xsd/litp.xsd");
	}

	@Before
	public void setupBeforeEachTest() {
		SchemaElement nodeSchema = schema
				.getDeclaredElement("scsi-block-device");

		nodeBuilder.actualSchema(nodeSchema.asComplexElement())
				.property("name", "disk1").property("uuid", "abcedefg")
				.property("size", "10G").property("bootable", "true");
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldThrowExceptionIfNodeContainsInvalidProperties() {
		// given
		Node node = nodeBuilder.property("xxxxx", "123")
				.property("yyyyy", "9999").build();

		// when
		try {
			new ValidNodePropertiesRule(node).validate();
		} catch (AggregatePropertyException exception) {
			// then
			assertThat(exception.getCauses().size(), is(2));
			assertThat(exception.getCauses().get(0),
					is(instanceOf(PropertyNotAllowedException.class)));
			throw exception;
		}
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldThrowExceptionIfNodeDoesntContainAllMandatoryProperties() {
		// given
		Node node = nodeBuilder.build();
		node.removeProperty("name");
		node.removeProperty("uuid");
		node.removeProperty("size");
		node.removeProperty("bootable");

		// when
		try {
			new ValidNodePropertiesRule(node).validate();
		} catch (AggregatePropertyException exception) {
			// then
			assertThat(exception.getCauses().size(), is(3));
			assertThat(exception.getCauses().get(0),
					is(instanceOf(MissingRequiredPropertyException.class)));
			throw exception;
		}
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldThrowExceptionIfNodeContainsPropertiesWithInvalidValues() {
		// given
		Node node = nodeBuilder.property("bootable", "invalid-value").build();

		// when
		try {
			new ValidNodePropertiesRule(node).validate();
		} catch (AggregatePropertyException exception) {
			// then
			assertThat(exception.getCauses().size(), is(1));
			assertThat(exception.getCauses().get(0),
					is(instanceOf(RegexException.class)));
			throw exception;
		}
	}

	@Test(expected = AggregatePropertyException.class)
	public void shouldThrowExceptionWithDifferentErrorMessagesForEachTypeOfInconsistency() {
		// given
		Node node = nodeBuilder.property("invalid-key", "123")
				.property("bootable", "invalid-value").build();
		node.removeProperty("name");

		// when
		try {
			new ValidNodePropertiesRule(node).validate();
		} catch (AggregatePropertyException exception) {
			// then
			assertThat(exception.getCauses().size(), is(3));
			throw exception;
		}
	}

	@Test
	public void shouldNotThrowAnyExceptionIfNodePropertiesAreValid() {
		// given
		Node node = nodeBuilder.build();

		// when
		new ValidNodePropertiesRule(node).validate();

		// then
		// nothing is expected
	}
}
