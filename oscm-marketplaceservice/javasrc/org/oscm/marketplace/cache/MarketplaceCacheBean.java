/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 22, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.marketplace.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.oscm.internal.cache.MarketplaceConfiguration;
import org.oscm.internal.intf.MarketplaceService;
import org.oscm.internal.types.exception.ObjectNotFoundException;
import org.oscm.internal.vo.VOMarketplace;
import org.oscm.internal.vo.VOOrganization;
import org.oscm.logging.Log4jLogger;
import org.oscm.logging.LoggerFactory;
import org.oscm.types.enumtypes.LogMessageIdentifier;

/**
 * Singleton bean for caching marketplace configurations.
 * 
 * @author miethaner
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MarketplaceCacheBean {

    /** Caching marketplace configurations */
    private Map<String, MarketplaceConfiguration> configurationCache = new HashMap<String, MarketplaceConfiguration>();

    private static final Log4jLogger logger = LoggerFactory
            .getLogger(MarketplaceCacheBean.class);

    @EJB
    private MarketplaceService mp;

    protected Log4jLogger getLogger() {
        return logger;
    }

    protected MarketplaceService getMarketplaceService() {
        return mp;
    }

    /**
     * Gets the corresponding marketplace configuration for the given
     * marketplace id
     * 
     * @param marketplaceId
     *            the marketplace id
     * @return the configuration
     */
    @Lock(LockType.READ)
    public MarketplaceConfiguration getConfiguration(String marketplaceId) {
        MarketplaceConfiguration conf = configurationCache.get(marketplaceId);
        if (conf == null) {
            conf = loadConfiguration(marketplaceId);
        }
        return conf;
    }

    /**
     * Loads the marketplace configuration for the given marketplace id into the
     * cache
     * 
     * @param marketplaceId
     *            the marketplace id
     * @return the configuration for the loaded marketplace
     */
    @Lock(LockType.WRITE)
    public MarketplaceConfiguration loadConfiguration(String marketplaceId) {
        MarketplaceConfiguration conf = new MarketplaceConfiguration();
        VOMarketplace voMarketPlace = new VOMarketplace();
        List<VOOrganization> allowedOrgs = new ArrayList<VOOrganization>();
        try {
            voMarketPlace = getMarketplaceService().getMarketplaceById(
                    marketplaceId);
            allowedOrgs = getMarketplaceService()
                    .getAllOrganizationsWithAccessToMarketplace(marketplaceId);
        } catch (ObjectNotFoundException e) {
            getLogger().logError(Log4jLogger.SYSTEM_LOG, e,
                    LogMessageIdentifier.ERROR_MARKETPLACE_NOT_FOUND,
                    marketplaceId);
            return null;
        }

        Set<String> idSet = new TreeSet<String>();
        for (VOOrganization org : allowedOrgs) {
            idSet.add(org.getOrganizationId());
        }

        // Copy related attributes from VOMarketplace to
        // MarketplaceConfiguration
        copyAttribute(voMarketPlace, conf);
        conf.setAllowedOrganizations(idSet);
        configurationCache.put(marketplaceId, conf);

        return conf;
    }

    /**
     * Convenience method for converting from VOMarketplace to
     * MarketplaceConfiguration
     */
    private void copyAttribute(VOMarketplace voMarketPlace,
            MarketplaceConfiguration conf) {
        conf.setReviewEnabled(voMarketPlace.isReviewEnabled());
        conf.setSocialBookmarkEnabled(voMarketPlace.isSocialBookmarkEnabled());
        conf.setTaggingEnabled(voMarketPlace.isTaggingEnabled());
        conf.setCategoriesEnabled(voMarketPlace.isCategoriesEnabled());
        conf.setRestricted(voMarketPlace.isRestricted());
        conf.setLandingPage(voMarketPlace.isHasPublicLandingPage());
    }

    /**
     * Reset configuration with specified Marketplace ID.
     */
    @Lock(LockType.WRITE)
    public void resetConfiguration(String marketplaceId) {
        configurationCache.remove(marketplaceId);
    }

}
