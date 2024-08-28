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
package com.ericsson.nms.dmt.local.schema.parsing;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.net.URL;
import java.util.*;

import javax.inject.Inject;
import javax.xml.XMLConstants;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.local.schema.data.*;
import com.ericsson.nms.dmt.local.schema.data.SchemaElement.SchemaElementBuilder;
import com.ericsson.nms.dmt.local.schema.data.SimpleTypeRestriction.RestrictionType;
import com.sun.xml.xsom.*;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * This class is used to parse XML schema documents (XSDs files) and extract
 * only a limited set of data that these documents can provide. It uses the XSOM
 * library to provides its functionality.
 */
public class XsdParser {

	private final Logger logger;

	/**
	 * Constructor that takes a logger object
	 * 
	 * @param logger
	 *            - object used to register log entries
	 */
	@Inject
	public XsdParser(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Parses a single or a set of XML schema files (as long as they are
	 * included by the root document) and extract only a limited set of data
	 * which is stored manly in {@link Schema} and {@link SchemaElement}
	 * objects.
	 * 
	 * @param file
	 *            - path to the XML schema document
	 * @return a set of {@link Schema} objects containing the elements declared
	 *         in the XML schema document
	 */
	public Set<Schema> parse(URL file) {
		try {
			XSOMParser parser = new XSOMParser();
			parser.setAnnotationParser(new XsdAnnotationParserFactory());
			parser.parse(file);
			return parseSchemaSet(parser.getResult());
		} catch (Exception e) {
			throw new XsdParsingException(
					"Error when parsing the schema files.", e);
		}
	}

	private Set<Schema> parseSchemaSet(XSSchemaSet schemaSet) {
		Set<Schema> schemas = new HashSet<>();
		for (XSSchema schema : schemaSet.getSchemas()) {
			if (!schema.getTargetNamespace().equals(
					XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
				schemas.add(parseSchema(schema));
			}
		}
		return schemas;
	}

	private Schema parseSchema(XSSchema xsSchema) {
		Schema schema = new Schema(xsSchema.getTargetNamespace());
		for (XSElementDecl xsElementDecl : xsSchema.getElementDecls().values()) {
			logger.debug("Parsing declared element '{}' from namespace '{}'",
					xsElementDecl.getName(), xsElementDecl.getTargetNamespace());
			schema.addDeclaredElement(parseRootElement(xsElementDecl));
		}
		return schema;
	}

	private SchemaElement parseRootElement(XSElementDecl xsElement) {
		return parseElement(xsElement, null);
	}

	private SchemaElement parseElement(XSElementDecl xsElement,
			XSParticle particle) {
		SchemaElementBuilder builder = null;

		if (xsElement.getType().isSimpleType()) {
			builder = SchemaElement
					.simpleBuilder()
					.defaultValue(getDefaultValue(xsElement))
					.restrictions(
							getTypeRestrictions(xsElement.getType()
									.asSimpleType()));
		} else {
			builder = SchemaElement
					.complexBuilder()
					.attributes(
							getAttributes(xsElement.getType().asComplexType()))
					.children(getChildren(xsElement.getType().asComplexType()));
		}

		builder.name(xsElement.getName()).type(xsElement.getType().getName())
				.baseType(xsElement.getType().getBaseType().getName())
				.substitutables(getSubstitutables(xsElement))
				.description(getDescription(xsElement))
				.namespace(xsElement.getTargetNamespace())
				.global(xsElement.isGlobal());

		if (particle != null) {
			builder.minOccurs(particle.getMinOccurs().intValue()).maxOccurs(
					particle.getMaxOccurs().intValue());
		}

		return builder.build();
	}

	private String getDefaultValue(XSElementDecl xsElement) {
		if (xsElement.getDefaultValue() != null) {
			return xsElement.getDefaultValue().value;
		}
		return null;
	}

	private Set<String> getSubstitutables(XSElementDecl xsElement) {
		Set<String> result = new HashSet<>();
		for (XSElementDecl substitutable : xsElement.getSubstitutables()) {
			if (!substitutable.getName().equals(xsElement.getName())) {
				result.add(substitutable.getName());
			}
		}
		return result;
	}

	private List<SchemaElement> getChildren(XSComplexType complexType) {
		XSParticle particle = complexType.getContentType().asParticle();
		if (particle != null) {
			return parseParticle(particle);
		}
		return emptyList();
	}

	private List<SchemaElement> parseParticle(XSParticle particle) {
		XSTerm term = particle.getTerm();
		if (term.isModelGroup()) {
			return parseGroup(term.asModelGroup());
		} else if (term.isElementDecl()) {
			return asList(parseElement(term.asElementDecl(), particle));
		}
		return emptyList();
	}

	private List<SchemaElement> parseGroup(XSModelGroup modelGroup) {
		List<SchemaElement> result = new ArrayList<>();
		for (XSParticle particle : modelGroup.getChildren()) {
			result.addAll(parseParticle(particle));
		}
		return result;
	}

	private String getDescription(XSElementDecl xsElement) {
		XSAnnotation annotation = xsElement.getType().isSimpleType() ? xsElement
				.getAnnotation() : xsElement.getType().getAnnotation();

		return annotation != null ? annotation.getAnnotation().toString()
				: EMPTY;
	}

	private Collection<SimpleTypeRestriction> getTypeRestrictions(
			XSSimpleType xsSimpleType) {
		XSRestrictionSimpleType restriction = xsSimpleType.asRestriction();

		if (restriction == null) {
			return Collections.emptyList();
		}

		Map<RestrictionType, SimpleTypeRestriction> restrictions = new HashMap<>();
		for (XSFacet facet : restriction.getDeclaredFacets()) {
			RestrictionType restrictionType = RestrictionType.from(facet
					.getName());

			if (!restrictions.containsKey(restrictionType)) {
				restrictions.put(restrictionType, new SimpleTypeRestriction(
						restrictionType));
			}
			restrictions.get(restrictionType).addConstraint(
					restrictionType.normalizeValue(facet.getValue().value));
		}
		return restrictions.values();
	}

	private Collection<SchemaAttribute> getAttributes(
			XSComplexType xsComplexType) {
		Collection<SchemaAttribute> attributes = new ArrayList<>();
		for (XSAttributeUse attributeUse : xsComplexType.getAttributeUses()) {
			attributes.add(parseAttribute(attributeUse));
		}
		return attributes;
	}

	private SchemaAttribute parseAttribute(XSAttributeUse attrUse) {
		XSAttributeDecl attrDecl = attrUse.getDecl();

		SchemaAttribute.Builder builder = SchemaAttribute.builder()
				.name(attrDecl.getName()).type(attrDecl.getType().getName())
				.required(attrUse.isRequired());

		if (attrDecl.getDefaultValue() != null) {
			builder.defaultValue(attrDecl.getDefaultValue().value);
		}
		if (attrDecl.getFixedValue() != null) {
			builder.fixedValue(attrDecl.getFixedValue().value);
		}
		return builder.restrictions(getTypeRestrictions(attrDecl.getType()))
				.build();
	}
}
