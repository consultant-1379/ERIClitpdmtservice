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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.List;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Immutable class that represents the path to a specific node in the deployment
 * model
 */
public class NodePath {

	private static final String PATH_SEPARATOR = "/";

	private final String path;

	private NodePath(String path) {
		this.path = normalize(PATH_SEPARATOR + path);
	}

	/**
	 * Factory method that returns a {@link NodePath} instance for the root node
	 * 
	 * @return
	 */
	public static NodePath root() {
		return new NodePath(PATH_SEPARATOR);
	}

	/**
	 * Factory method that builds a {@link NodePath} instance from a
	 * {@link String}
	 * 
	 * @return
	 */
	public static NodePath fromString(String path) {
		return new NodePath(path);
	}

	private String normalize(String path) {
		String pathWithoutMultipleSeparators = path.replaceAll(PATH_SEPARATOR
				+ "+", PATH_SEPARATOR);
		if (pathWithoutMultipleSeparators.equals(PATH_SEPARATOR)) {
			return pathWithoutMultipleSeparators;
		}
		return pathWithoutMultipleSeparators.replaceAll(PATH_SEPARATOR + "$",
				EMPTY);
	}

	/**
	 * Prepend a path slice to an existing {@link NodePath} and returns a new
	 * instance of it
	 * 
	 * @param prependedPath
	 *            - path to be prepended
	 * @return new instance of {@link NodePath} containing the prepended value +
	 *         the existing path
	 * 
	 */
	public NodePath prepend(String prependedPath) {
		return fromString(prependedPath + PATH_SEPARATOR + path);
	}

	/**
	 * Append a path slice to an existing {@link NodePath} and returns a new
	 * instance of it
	 * 
	 * @param appendedPath
	 *            - path to be appended
	 * @return new instance of {@link NodePath} containing the existing path +
	 *         the appended value
	 */
	public NodePath append(String appendedPath) {
		return fromString(path + PATH_SEPARATOR + appendedPath);
	}

	/**
	 * Eliminates the very last element in the path and returns a new instance
	 * of {@link NodePath} for it
	 * 
	 * @return the parent path for the existing {@link NodePath}
	 * @throws RuntimeException
	 *             if the path doesn't have a parent (it is a root path)
	 */
	public NodePath parent() {
		if (isRoot()) {
			throw new RuntimeException("Path has no parent");
		}
		String parentPath = path.substring(0,
				path.lastIndexOf(PATH_SEPARATOR) + 1);
		return fromString(parentPath);
	}

	/**
	 * Returns a list of the elements that compose the node path.
	 * 
	 * @return - list of path elements
	 */
	public List<String> elements() {
		if (isRoot()) {
			return emptyList();
		}
		return unmodifiableList(asList(path.replaceAll("^" + PATH_SEPARATOR,
				EMPTY).split(PATH_SEPARATOR)));
	}

	public boolean isRoot() {
		return path.equals(PATH_SEPARATOR);
	}

	@JsonValue
	@Override
	public String toString() {
		return path;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodePath other = (NodePath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
