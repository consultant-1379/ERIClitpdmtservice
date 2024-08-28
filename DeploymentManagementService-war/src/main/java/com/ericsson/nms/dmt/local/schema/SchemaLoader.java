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

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.local.schema.data.Schema;
import com.ericsson.nms.dmt.local.schema.parsing.XsdParser;

/**
 * This component is responsible for loading and processing the LITP schema
 * files (XSDs) at deployment time. The schema files are parsed only once and
 * then stored in memory during the entire application lifecycle. Besides the
 * schema files, this component also loads the deployment model templates used
 * to create new local working copies.
 */
@Startup
@Singleton
@TransactionAttribute(NOT_SUPPORTED)
public class SchemaLoader {

	private static final String PROPERTIES_FILE = "/litp_model.properties";
	private static final String PROP_SCHEMA_PATH = "schema.path";
	private static final String PROP_TEMPLATE_PATH = "template.path";

	@Inject
	private SchemaRepository schemaRepository;

	@Inject
	private TemplateStore templateStore;

	@Inject
	private XsdParser xsdParser;

	@Inject
	private XmlReader xmlReader;

	@Inject
	private Logger logger;

	/**
	 * Callback method that is invoked by the container when the application is
	 * deployed
	 */
	@PostConstruct
	private void startup() {
		logger.info("Loading the schema package...");
		try (InputStream propertiesFile = getClass().getResourceAsStream(
				PROPERTIES_FILE)) {
			if (propertiesFile != null) {
				loadSchemaPackage(propertiesFile);
				logger.info("Schema package was loaded successfully");
			} else {
				logger.warn("Schema package not found in the classpath");
			}
		} catch (Exception e) {
			logger.error("Failed to load the schema package", e);
		}
	}

	private void loadSchemaPackage(InputStream propertiesFile)
			throws IOException {
		Properties properties = new Properties();
		properties.load(propertiesFile);
		processSchemaFile(getClass().getResource(
				properties.getProperty(PROP_SCHEMA_PATH)));
		processTemplateFile(getClass().getResource(
				properties.getProperty(PROP_TEMPLATE_PATH)));
	}

	private void processSchemaFile(URL schemaFilePath) throws IOException {
		Set<Schema> schemas = xsdParser.parse(schemaFilePath);
		schemaRepository.putAll(schemas);
	}

	private void processTemplateFile(URL templateFilePath) {
		try {
			templateStore.setDefaultTemplate(xmlReader.load(templateFilePath));
		} catch (Exception e) {
			throw new SchemaLoadingException(
					"Failed to load the template file", e);
		}
	}
}
