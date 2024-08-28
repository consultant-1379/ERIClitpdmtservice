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
package com.ericsson.nms.dmt.live.service;

import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableList;
import static javax.ejb.ConcurrencyManagementType.BEAN;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.*;
import javax.ejb.*;
import javax.ejb.Timer;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.nms.dmt.ItemType;
import com.ericsson.nms.dmt.live.litp.LitpClient;
import com.google.common.base.Function;

/**
 * This component acts as a local repository (read cache) of item types details.
 * As item types are not volatile (which means that they don't change very
 * frequently), this component eagerly caches item type data in order to avoid
 * unnecessary round trips to the LITP server and improves the general
 * performance of the client applications. The cache is initialized
 * automatically a few moments after the application is deployed and refreshes
 * its cache after a specified period of time. The requests are only handled by
 * this component when the cache is fully initialized. While the cache is being
 * loaded the first time, all requests are forwarded to LITP if it cannot be
 * processed using the data that is already store in the local cache.
 */
@Singleton
@Startup
@ConcurrencyManagement(BEAN)
@TransactionAttribute(NOT_SUPPORTED)
public class ItemTypeCachedRepository {

	private static final long THREE_SECONDS = 3 * 1000;
	private static final long THREE_MINUTES = 3 * 60 * 1000;
	private static final long ONE_HOUR = 60 * 60 * 1000;

	private Map<String, ItemType> cache;

	private volatile boolean fullyLoaded;

	@Inject
	private LitpClient liptClient;

	@Resource
	private TimerService timerService;

	@Inject
	private Logger logger;

	/**
	 * Callback method that initialize the component
	 */
	@PostConstruct
	private void init() {
		logger.info("Starting up the LITP item types cache.");
		cache = new ConcurrentHashMap<>();
		scheduleCacheRefresh(THREE_SECONDS);
	}

	/**
	 * Returns a item type object from the cache
	 * 
	 * @param itemTypeName
	 *            - name of the requested item type
	 * @return
	 */
	public ItemType get(String itemTypeName) {
		ItemType cachedItemType = cache.get(itemTypeName);
		if (cachedItemType != null) {
			return cachedItemType;
		}

		ItemType freshItemType = liptClient.getItemType(itemTypeName);
		put(freshItemType);
		return freshItemType;
	}

	/**
	 * Returns all the existing item types
	 * 
	 * @return - list of all existing item types
	 */
	public List<ItemType> getAll() {
		if (fullyLoaded) {
			return unmodifiableList(new ArrayList<>(cache.values()));
		}
		return liptClient.getAllItemTypes();
	}

	private void put(ItemType itemType) {
		cache.put(itemType.getName(), itemType);
	}

	private void evict(String itemTypeName) {
		cache.remove(itemTypeName);
	}

	private void evictAll() {
		cache.clear();
		fullyLoaded = false;
	}

	/**
	 * Method used to schedule a new cache refresh.
	 * 
	 * @param milliseconds
	 *            - time in milliseconds for the next cache refresh
	 */
	private void scheduleCacheRefresh(long milliseconds) {
		logger.info("Setting a new cache refresh for {} seconds from now...",
				(milliseconds / 1000));

		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setInfo("ItemTypesCacheRefresh");
		timerConfig.setPersistent(false);
		timerService.createSingleActionTimer(milliseconds, timerConfig);
	}

	/**
	 * Callback method that is started by the container when it is time for a
	 * new cache refresh.
	 * 
	 * @param milliseconds
	 *            - time in milliseconds for the next cache refresh
	 */
	@Timeout
	private void executeCacheRefresh(Timer timer) {
		logger.info("Starting a new cache refresh...");

		try {
			List<ItemType> allItemTypes = liptClient.getAllItemTypes();
			Set<String> allItemTypeNames = toItemTypeNamesSet(allItemTypes);
			removeNonExistingValuesFromCache(allItemTypeNames);
			updateCachedValues(allItemTypeNames);
			fullyLoaded = true;
			logger.info("Cache is fully refreshed.");
			scheduleCacheRefresh(ONE_HOUR);
		} catch (Exception e) {
			logger.error(
					"An unexpected error has happend when refreshing the cache. "
							+ "DMT may be working with stale data.", e);
			scheduleCacheRefresh(THREE_MINUTES);
		}
	}

	private void updateCachedValues(Set<String> allItemTypeNames) {
		for (String itemTypeName : allItemTypeNames) {
			logger.debug("Caching/updating the item type '{}'", itemTypeName);
			put(liptClient.getItemType(itemTypeName));
		}
	}

	private void removeNonExistingValuesFromCache(Set<String> allItemTypeNames) {
		for (String cachedItemTypeName : cache.keySet()) {
			if (!allItemTypeNames.contains(cachedItemTypeName)) {
				logger.debug("Removing item type '{}' from cache...",
						cachedItemTypeName);
				evict(cachedItemTypeName);
			}
		}
	}

	private HashSet<String> toItemTypeNamesSet(List<ItemType> allItemTypes) {
		return newHashSet(transform(allItemTypes,
				new Function<ItemType, String>() {
					@Override
					public String apply(ItemType itemType) {
						return itemType.getName();
					}
				}));
	}

	/**
	 * Callback method that shut down the component.
	 */
	@PreDestroy
	private void shutdown() {
		logger.info("Shutting down the the LITP item types cache...");
		evictAll();
	}
}
