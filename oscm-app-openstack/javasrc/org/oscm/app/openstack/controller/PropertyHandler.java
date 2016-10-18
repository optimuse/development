/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                        
 *       
 *  OpenStack controller implementation for the 
 *  Asynchronous Provisioning Platform (APP)
 *       
 *  Creation Date: 2013-11-29                                                      
 *                                                                              
 *******************************************************************************/
package org.oscm.app.openstack.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.oscm.app.openstack.data.FlowState;
import org.oscm.app.openstack.exceptions.HeatException;
import org.oscm.app.v1_0.BSSWebServiceFactory;
import org.oscm.app.v1_0.data.PasswordAuthentication;
import org.oscm.app.v1_0.data.ProvisioningSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle service parameters and controller configuration
 * settings.
 * <p>
 * The underlying <code>ProvisioningSettings</code> object of APP provides all
 * the specified service parameters and controller configuration settings
 * (key/value pairs). The settings are stored in the APP database and therefore
 * available even after restarting the application server.
 */
public class PropertyHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PropertyHandler.class);

    private final ProvisioningSettings settings;

    public static final String STACK_NAME = "STACK_NAME";
    public static final String STACK_ID = "STACK_ID";
    public static final String STACK_NAME_PATTERN = "STACK_NAME_PATTERN";

    // Name (not id) of the domain (if omitted, it is taken from
    // controller configuration)
    public static final String DOMAIN_NAME = "DOMAIN_NAME";

    // Default name of Domain
    private static final String DEFAULT_DOMAIN = "default";

    // URL of Heat template
    public static final String TEMPLATE_NAME = "TEMPLATE_NAME";

    // AccessInfo creation pattern
    public static final String ACCESS_INFO_PATTERN = "ACCESS_INFO_PATTERN";

    // Base URL of Heat templates
    public static final String TEMPLATE_BASE_URL = "TEMPLATE_BASE_URL";

    // Prefix for template parameters
    public static final String TEMPLATE_PARAMETER_PREFIX = "TP_";

    public static final String KEYSTONE_API_URL = "KEYSTONE_API_URL";
    public static final String API_USER_NAME = "API_USER_NAME";
    public static final String API_USER_PWD = "API_USER_PWD";

    /**
     * Defines whether manual steps are required before activation the
     * deployment for the customer.
     */
    public static final String MAIL_FOR_COMPLETION = "MAIL_FOR_COMPLETION";

    /**
     * The internal status of a provisioning operation as set by the controller
     * or the status dispatcher.
     */
    public static final String STATUS = "STATUS";

    // ID of the tenant/project
    public static final String TENANT_ID = "TENANT_ID";

    // Timeout for status check (msec)
    public static final String READY_TIMEOUT = "READY_TIMEOUT";

    // Start time of operation
    public static final String START_TIME = "START_TIME";

    // Attributes to overwrite credentials
    public static final String OPENSTACK_ATTRIBUTE_PREFIX = "OPENSTACK_";

    /**
     * Default constructor.
     *
     * @param settings
     *            a <code>ProvisioningSettings</code> object specifying the
     *            service parameters and configuration settings
     *
     */
    public PropertyHandler(ProvisioningSettings settings) {
        this.settings = settings;
    }

    /**
     * Returns the internal state of the current provisioning operation as set
     * by the controller or the dispatcher.
     *
     * @return the current status
     */
    public FlowState getState() {
        String status = settings.getParameters().get(STATUS);
        return (status != null) ? FlowState.valueOf(status) : FlowState.FAILED;
    }

    /**
     * Changes the internal state for the current provisioning operation.
     *
     * @param newState
     *            the new state to set
     */
    public void setState(FlowState newState) {
        settings.getParameters().put(STATUS, newState.toString());
    }

    /**
     * Returns the current service parameters and controller configuration
     * settings.
     *
     * @return a <code>ProvisioningSettings</code> object specifying the
     *         parameters and settings
     */
    public ProvisioningSettings getSettings() {
        return settings;
    }

    /**
     * Returns the name of the stack (=instance identifier).
     *
     * @return the name of the stack
     */
    public String getStackName() {
        return getOverwrittenProperty(settings.getParameters(), STACK_NAME,
                true);
    }

    public void setStackName(String stackName) {
        settings.getParameters().put(STACK_NAME, stackName);
    }

    /**
     * Returns the regex for the stack name
     * 
     * @return the regular expression
     */
    public String getStackNamePattern() {
        return getOverwrittenProperty(settings.getParameters(),
                STACK_NAME_PATTERN, false);
    }

    /**
     * Returns the heat specific id of the stack.
     *
     * @return the id of the stack
     */
    public String getStackId() {
        return getOverwrittenProperty(settings.getParameters(), STACK_ID,
                false);
    }

    public void setStackId(String stackId) {
        settings.getParameters().put(STACK_ID, stackId);
    }

    /**
     * Returns the access information pattern used to created the instance
     * access information using the output parameters of the created stack.
     *
     * @return the access information pattern
     */
    public String getAccessInfoPattern() {
        return getOverwrittenProperty(settings.getParameters(),
                ACCESS_INFO_PATTERN, true);
    }

    /**
     * Returns the URL of the template to be used for provisioning.
     *
     * @return the template URL
     */
    public String getTemplateUrl() throws HeatException {

        try {
<<<<<<< cf2913781d331aac796405fbf9d6203680dc32db
            String url = getValidatedProperty(settings.getParameters(),
                    TEMPLATE_NAME);
<<<<<<< 8085f023f5609892c35f54b1f182816d13a1e95e

            String baseUrl = settings.getParameters().get(TEMPLATE_BASE_URL);
            if (baseUrl == null || baseUrl.trim().length() == 0) {
                baseUrl = getValidatedProperty(settings.getConfigSettings(),
                        TEMPLATE_BASE_URL);
            }
=======
            String baseUrl = getValidatedProperty(settings.getConfigSettings(),
                    TEMPLATE_BASE_URL);
>>>>>>> adapt app and controller for user attributes via custom settings
=======
            String url = getOverwrittenProperty(settings.getParameters(),
                    TEMPLATE_NAME, true);
            String baseUrl = getOverwrittenProperty(
                    settings.getConfigSettings(), TEMPLATE_BASE_URL, true);
>>>>>>> replace all possible controller settings and add webservice to save
            return new URL(new URL(baseUrl), url).toExternalForm();
        } catch (MalformedURLException e) {
            throw new HeatException(
                    "Cannot generate template URL: " + e.getMessage());
        }
    }

    /**
     * Returns the domain name that defines the context for the provisioning. It
     * can either be defined within the controller settings of as instance
     * parameter. When present, the service parameter is preferred.
     *
     * @return the domain name
     */
    public String getDomainName() {
        String domain = getOverwrittenProperty(settings.getParameters(),
                DOMAIN_NAME, false);
        if (domain == null || domain.trim().length() == 0) {
            domain = settings.getConfigSettings().get(DOMAIN_NAME);
            if (domain == null || domain.trim().length() == 0) {
                domain = DEFAULT_DOMAIN;
            }
        }
        return domain;
    }

    public JSONObject getTemplateParameters() {
        JSONObject parameters = new JSONObject();
        Set<String> keySet = settings.getParameters().keySet();
        for (String key : keySet) {
            if (key.startsWith(TEMPLATE_PARAMETER_PREFIX)) {
                try {
                    parameters.put(
                            key.substring(TEMPLATE_PARAMETER_PREFIX.length()),
                            getOverwrittenProperty(settings.getParameters(),
                                    key, false));
                } catch (JSONException e) {
                    // should not happen with Strings
                    throw new RuntimeException(
                            "JSON error when collection template parameters",
                            e);
                }
            }
        }
        return parameters;
    }

    /**
     * Reads the requested property from the given source. If the key exists
     * with the corresponding prefix as attribute it will be read instead. If
     * validate is true and no value can be found, a RuntimeException will be
     * thrown.
     *
     * @param sourceProps
     *            The property object to take the settings from
     * @param key
     *            The key to retrieve the setting for
     * @param validate
     *            The flag for the validation of the value
     * @return the parameter value corresponding to the provided key
     */
    private String getOverwrittenProperty(Map<String, String> sourceProps,
            String key, boolean validate) {

        String value = null;

        if (settings.getAttributes()
                .containsKey(OPENSTACK_ATTRIBUTE_PREFIX + key)) {
            value = settings.getAttributes()
                    .get(OPENSTACK_ATTRIBUTE_PREFIX + key);

            if (value.trim().length() == 0) {
                value = sourceProps.get(key);
            }

        } else {
            value = sourceProps.get(key);
        }

        if (value == null && validate) {
            String message = String.format("No value set for property '%s'",
                    key);
            LOGGER.error(message);
            throw new RuntimeException(message);
        }

        return value;
    }

    /**
     * Return the URL of the Keystone API which acts as entry point to all other
     * API endpoints.
     *
     * @return the Keystone URL
     */
    public String getKeystoneUrl() {

        String keystoneURL = getOverwrittenProperty(settings.getParameters(),KEYSTONE_API_URL, false);
        if (keystoneURL == null || keystoneURL.trim().length() == 0) {
            keystoneURL = getOverwrittenProperty(settings.getConfigSettings(),
                    KEYSTONE_API_URL,true);
        }
        return keystoneURL;
    }

    /**
     * Returns the configured or overwritten password for API usage.
     *
     * @return the password
     */
    public String getPassword() {
        return getOverwrittenProperty(settings.getConfigSettings(),
                API_USER_PWD, true);
    }

    /**
     * Returns the configured user name for API usage.
     *
     * @return the user name
     */
    public String getUserName() {
        return getOverwrittenProperty(settings.getConfigSettings(),
                API_USER_NAME, true);
    }

    /**
     * Returns the mail address to be used for completion events (provisioned,
     * deleted). If not set, no events are required.
     *
     * @return the mail address or <code>null</code> if no events are required
     */
    public String getMailForCompletion() {
        String value = getOverwrittenProperty(settings.getParameters(),
                MAIL_FOR_COMPLETION, false);
        if (value == null || value.trim().length() == 0) {
            value = null;
        }
        return value;
    }

    public String getStackConfigurationAsString() throws HeatException {
        StringBuffer details = new StringBuffer();
        details.append("\t\r\nStackName: ");
        details.append(getStackName());
        details.append("\t\r\nStackId: ");
        details.append(getStackId());
        details.append("\t\r\nAPIUserName: ");
        details.append(getUserName());
        details.append("\t\r\nKeystoneAPIUrl: ");
        details.append(getKeystoneUrl());
        details.append("\t\r\nTenantID: ");
        details.append(getTenantId());
        details.append("\t\r\nDomainName: ");
        details.append(getDomainName());
        details.append("\t\r\nTemplateUrl: ");
        details.append(getTemplateUrl());
        details.append("\t\r\nAccessInfoPattern: ");
        details.append(getAccessInfoPattern());
        details.append("\t\r\n");
        return details.toString();
    }

    /**
     * Returns service interfaces for BSS web service calls.
     */
    public <T> T getWebService(Class<T> serviceClass) throws Exception {
        return BSSWebServiceFactory.getBSSWebService(serviceClass,
                settings.getAuthentication());
    }

    /**
     * Returns the instance or controller specific technology manager
     * authentication.
     */
    public PasswordAuthentication getTPAuthentication() {
        return settings.getAuthentication();
    }

    /**
     * Returns the locale set as default for the customer organization.
     *
     * @return the customer locale
     */
    public String getCustomerLocale() {
        String locale = settings.getLocale();
        if (locale == null || locale.trim().length() == 0) {
            locale = "en";
        }
        return locale;
    }

    /**
     * Returns the tenant id that defines the context for the provisioning.
     *
     * @return the tenant id
     */
    public String getTenantId() {
        String tenant = settings.getParameters().get(TENANT_ID);
        if (tenant == null || tenant.trim().length() == 0) {
            tenant = getOverwrittenProperty(settings.getConfigSettings(),
                    TENANT_ID, true);
        }
        return tenant;
    }

    /**
     * Set start time of operation
     * 
     * @param time
     */
    public void setStartTime(String time) {
        settings.getParameters().put(START_TIME, time);
    }

    /**
     * Return the start time of operation
     * 
     * @return the start time of string
     */
    public String getStartTime() {
        return settings.getParameters().get(START_TIME);
    }

    /**
     * Return the ready timeout which is waiting time of status changing If
     * number is not corrected, return 0
     * 
     * @return timeout value of long
     */
    public long getReadyTimeout() {
        String readyTimeout = settings.getConfigSettings().get(READY_TIMEOUT);
        if (readyTimeout == null || readyTimeout.trim().length() == 0) {
            LOGGER.warn("'READY_TIMEOUT' is not set and therefore ignored");
            return 0;
        }
        try {
            return Long
                    .parseLong(settings.getConfigSettings().get(READY_TIMEOUT));
        } catch (NumberFormatException ex) {
            LOGGER.warn(
                    "Wrong value set for property 'READY_TIMEOUT' and therefore ignored");
        }
        return 0;

    }

}
