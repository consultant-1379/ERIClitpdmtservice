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
package com.ericsson.nms.dmt.live.litp.error;

/**
 * Defines the data structure to store error messages that can be sent back as
 * result of validation issues in the deployment model.
 */
public class ErrorMessage {

	private String type;
	private String message;
	private String refersTo;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public String getRefersTo() {
		return refersTo;
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder class used to create instances of {@link ErrorMessage} objects
	 */
	public static class Builder {

		private final ErrorMessage instance = new ErrorMessage();

		public Builder type(String type) {
			this.instance.type = type;
			return this;
		}

		public Builder refersTo(String refersTo) {
			this.instance.refersTo = refersTo;
			return this;
		}

		public Builder message(String message) {
			this.instance.message = message;
			return this;
		}

		public ErrorMessage build() {
			return instance;
		}
	}
}