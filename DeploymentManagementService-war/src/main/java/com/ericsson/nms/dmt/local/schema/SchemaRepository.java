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
package com.ericsson.nms.dmt.local.schema;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement;

/**
 * This component acts as a repository of {@link Schema} and
 * {@link SchemaElement} objects for other components in the application. The
 * application is supposed to have only one instance of this class.
 */
@ApplicationScoped
public class SchemaRepository {

	private final Map<String, Schema> cache = new ConcurrentHashMap<>();;

	@Inject
	private Logger logger;

	@PostConstruct
	private void init() {
		logger.info("Starting up the schema cache...");
	}

	/**
	 * Returns an instance of {@link SchemaElement} for the provided name and
	 * namespace
	 * 
	 * @param name
	 *            - schema element name
	 * @param namespace
	 *            - schema namespace
	 * @return
	 */
	public SchemaElement get(String name, String namespace) {
		Schema schema = cache.get(namespace);

		if (schema == null) {
			return null;
		}
		return schema.getDeclaredElement(name);
	}

	/**
	 * Returns an instance of {@link SchemaElement} for the provide name from
	 * any namespace
	 * 
	 * @param name
	 *            - schema element name
	 * @return
	 */
	public SchemaElement get(String name) {
		for (String namespace : cache.keySet()) {
			SchemaElement element = get(name, namespace);
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Stores an {@link Schema} object in memory
	 * 
	 * @param schema
	 *            - object to be cached
	 */
	public void put(Schema schema) {
		cache.put(schema.getNamespace(), schema);
	}

	/**
	 * Stores multiple {@link Schema} objects in memory
	 * 
	 * @param schemas
	 *            - list of objects to be cached
	 */
	public void putAll(Collection<Schema> schemas) {
		for (Schema schema : schemas) {
			put(schema);
		}
	}

	private void evictAll() {
		cache.clear();
	}

	@PreDestroy
	private void shutdown() {
		logger.info("Shutting down the schema cache...");
		evictAll();
	}
}
