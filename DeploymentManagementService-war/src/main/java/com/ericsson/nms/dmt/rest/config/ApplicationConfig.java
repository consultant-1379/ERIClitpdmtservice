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
package com.ericsson.nms.dmt.rest.config;

import static com.google.common.collect.Sets.union;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.ericsson.nms.dmt.rest.*;
import com.ericsson.nms.dmt.rest.error.NotFoundExceptionMapper;

/**
 * Defines a set of components (resources and providers) that compose the DMT
 * JAX-RS application and supplies additional metadata.
 */
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return union(getRestResourceClasses(), getProviderClasses());
	}

	private Set<Class<?>> getRestResourceClasses() {
		Set<Class<?>> resources = new HashSet<>();
		resources.add(HomeResource.class);
		resources.add(WorkingCopyResource.class);
		resources.add(DeploymentModelResource.class);
		resources.add(TypeDiscoveryResource.class);
		return resources;
	}

	private Set<Class<?>> getProviderClasses() {
		Set<Class<?>> providers = new HashSet<>();
		providers.add(JacksonConfig.class);
		providers.add(NotFoundExceptionMapper.class);
		return providers;
	}
}
