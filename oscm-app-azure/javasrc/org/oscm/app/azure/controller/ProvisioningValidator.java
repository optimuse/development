/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.oscm.app.azure.AzureCommunication;
import org.oscm.app.azure.i18n.Messages;
import org.oscm.app.v1_0.exceptions.APPlatformException;
import org.oscm.app.v1_0.exceptions.AuthenticationException;
import org.oscm.app.v1_0.exceptions.ConfigurationException;
import org.oscm.app.v1_0.exceptions.SuspendException;

public class ProvisioningValidator {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory
            .getLogger(ProvisioningValidator.class);

    /**
     *
     *
     * @param ph
     *            a property handler for reading and writing service parameters
     *            and controller configuration settings
     * @throws APPlatformException
     */
    public static void validateParameters(PropertyHandler ph)
            throws APPlatformException {
        String resourceGroupName = ph.getResourceGroupName();
        if (StringUtils.isBlank(resourceGroupName)) {
            throw new APPlatformException(Messages.getAll("error_invalid_name",
                    new Object[] { resourceGroupName }));
        }
        String regex = "^[^_\\W][\\w-._]{1,29}";
        Matcher m = Pattern.compile(regex).matcher(resourceGroupName);
        if (!m.matches()) {
            logger.error("Validation error on resource group name: ["
                    + resourceGroupName + "/" + regex + "]");
            throw new APPlatformException(Messages.getAll("error_invalid_name",
                    new Object[] { resourceGroupName }));
        }

        AzureCommunication azureCom = new AzureCommunication(ph);
        String region = ph.getRegion();
        List<String> regions = azureCom.getAvailableRegions();
        if (regions.indexOf(region) < 0) {
            logger.error("Validation error on region: [" + region + "/"
                    + StringUtils.join(regions, ", ") + "]");
            throw new APPlatformException(Messages.getAll(
                    "error_invalid_region", new Object[] { region }));
        }
    }

    /**
     * 
     *
     * @param ph
     *            a property handler for reading and writing service parameters
     *            and controller configuration settings
     * @throws SuspendException
     *             if the request timeout
     * @throws AuthenticationException
     *             if the authentication of the user fails
     * @throws ConfigurationException
     *             if the configuration settings cannot be loaded
     * @throws APPlatformException
     *             if a general problem occurs in accessing APP
     */
    public static void validateTimeout(String instanceId, PropertyHandler ph)
            throws SuspendException, AuthenticationException,
            ConfigurationException, APPlatformException {
        long readyTimeout = ph.getReadyTimeout();
        if (ph.getStartTime() == null) {
            throw new RuntimeException(
                    "Controller must be set the start time at the beginning of the request");
        } else if ("suspended".equals(ph.getStartTime())) {
            logger.debug("Resume request, reset start time");
            ph.setStartTime(String.valueOf(System.currentTimeMillis()));
            return;
        }
        long startTime = Long.parseLong(ph.getStartTime());
        long currentTime = System.currentTimeMillis();
       /* logger.debug(
                "ExecutionTime: {}ms (StartTime: {}ms, CurrentTime: {}ms), ReadyTimeout: {}ms",
                currentTime - startTime, startTime, currentTime, readyTimeout);
        if (currentTime - startTime > readyTimeout) {
            logger.error("Request timeout, over {}ms", currentTime - startTime);
            ph.setStartTime("suspended");
            APPlatformServiceFactory.getInstance().storeServiceInstanceDetails(
                    AzureController.ID, instanceId, ph.getSettings(),
                    ph.getTPAuthentication());
            throw new SuspendException(Messages.getAll("error_ready_timeout",
                    readyTimeout));
        }*/
    }
}
