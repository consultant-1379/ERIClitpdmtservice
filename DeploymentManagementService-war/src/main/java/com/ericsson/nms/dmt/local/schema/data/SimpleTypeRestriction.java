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
package com.ericsson.nms.dmt.local.schema.data;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a XSD restriction (or facet). Restrictions are used to define
 * acceptable values for XML elements or attributes. This class can be used to
 * group multiple restrictions constraints of the same type in only one object
 * (i.e. a series of <i>enumeration</i> or <i>pattern</i> values).
 */
public class SimpleTypeRestriction {

	private final RestrictionType type;
	private final List<String> constraints;

	/**
	 * Constructor that takes the restriction type
	 * 
	 * @param type
	 *            - restriction type
	 */
	public SimpleTypeRestriction(RestrictionType type) {
		this.type = type;
		this.constraints = new ArrayList<>();
	}

	public RestrictionType getType() {
		return type;
	}

	/**
	 * Returns a list of existing constraints for the restriction type
	 * 
	 * @return
	 */
	public List<String> getConstraints() {
		return unmodifiableList(constraints);
	}

	/**
	 * Adds a new constraint to the current restriction
	 * 
	 * @param constraint
	 */
	public void addConstraint(String constraint) {
		constraints.add(constraint);
	}

	/**
	 * Informs whether the provided element value is an acceptable value for
	 * this restriction
	 * 
	 * @param value
	 *            - element value
	 * @return
	 */
	public boolean isAcceptableValue(String value) {
		for (String constraint : constraints) {
			if (type.valid(constraint, value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return type.tagname + ": " + constraints;
	}

	/**
	 * Enumeration that defines the possible restriction types for an XML schema
	 * element.
	 */
	public enum RestrictionType {

		/**
		 * Specifies the exact number of characters or list items allowed
		 */
		LENGTH("length"),

		/**
		 * Specifies the minimum number of characters or list items allowed
		 */
		MIN_LENGTH("minLength"),

		/**
		 * Specifies the maximum number of characters or list items allowed
		 */
		MAX_LENGTH("maxLength"),

		/**
		 * Defines the exact sequence of characters that are acceptable
		 */
		PATTERN("pattern") {
			@Override
			protected boolean valid(String constraint, String value) {
				return value.matches(constraint);
			}

			@Override
			public String normalizeValue(String value) {
				return "^" + value + "$";
			}
		},

		/**
		 * Defines a list of acceptable values
		 */
		ENUMERATION("enumeration"),

		/**
		 * Specifies the exact number of digits allowed
		 */
		TOTAL_DIGITS("totalDigits"),

		/**
		 * Specifies the maximum number of decimal places allowed
		 */
		FRACTION_DIGITS("fractionDigits"),

		/**
		 * Specifies the lower bounds for numeric values (the value must be
		 * greater than or equal to this value)
		 */
		MIN_INCLUSIVE("minInclusive"),

		/**
		 * Specifies the upper bounds for numeric values (the value must be less
		 * than or equal to this value)
		 */
		MAX_INCLUSIVE("maxInclusive"),

		/**
		 * Specifies the lower bounds for numeric values (the value must be
		 * greater than this value)
		 */
		MIN_EXCLUSIVE("minExclusive"),

		/**
		 * Specifies the upper bounds for numeric values (the value must be less
		 * than this value)
		 */
		MAX_EXCLUSIVE("maxExclusive"),

		/**
		 * Specifies how white space is handled
		 */
		WHITESPACE("whiteSpace");

		private String tagname;

		private RestrictionType(String tagname) {
			this.tagname = tagname;
		}

		/**
		 * Informs whether the value is an acceptable value for the specified
		 * restriction constraint
		 * 
		 * @param constraint
		 * @param value
		 * @return
		 */
		protected boolean valid(String constraint, String value) {
			return true; // TODO: only validate regex.
		}

		/**
		 * Transforms the restriction constraint value from XML schema to Java
		 * in order to produce the same effect
		 * 
		 * @param value
		 *            - constraint value
		 * @return
		 */
		public String normalizeValue(String value) {
			return value;
		}

		/**
		 * Factory method that creates a {@link RestrictionType} from the
		 * corresponding XML schema tag name, which is provided as string value
		 * 
		 * @param tagname
		 *            - the restriction tag name
		 * @return
		 */
		public static RestrictionType from(String tagname) {
			for (RestrictionType type : values()) {
				if (type.tagname.equals(tagname)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Invalid restriction type.");
		}
	}
}
