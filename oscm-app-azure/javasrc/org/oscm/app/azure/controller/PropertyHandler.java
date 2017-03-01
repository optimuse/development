/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.oscm.app.azure.data.FlowState;
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

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory
            .getLogger(PropertyHandler.class);

    /**
     * ProvisioningSettings.
     */
    private final ProvisioningSettings settings;

    /*(ConfigSettings) */

    /**
     * Azure
     */
    public static final String API_USER_NAME = "API_USER_NAME";

    /**
     * Azure‚
     */
    public static final String API_USER_PWD = "API_USER_PWD";

    /**
     * 
     */
    public static final String TEMPLATE_BASE_URL = "TEMPLATE_BASE_URL";

    /**
     * 
     */
    public static final String READY_TIMEOUT = "READY_TIMEOUT";

    /* (Parameters) */

    /**
     *
     */
    public static final String MAIL_FOR_COMPLETION = "MAIL_FOR_COMPLETION";

    /**
     * Azure‚
     */
    public static final String SUBSCRIPTION_ID = "SUBSCRIPTION_ID";

    /**
     * Azure‚
     */
    public static final String TENANT_ID = "TENANT_ID";

    /**
     * Azure‚
     */
    public static final String CLIENT_ID = "CLIENT_ID";

    /**
     * Azure
     */
    public static final String CLIENT_SECRET = "CLIENT_SECRET";

    /**
     * Azure
     */
    public static final String REGION = "REGION";

    /**
     * Azure
     */
    public static final String RESOURCE_GROUP_NAME = "RESOURCE_GROUP_NAME";
    
    /**
     * Azure
     */
    public static final String VIRTUAL_NETWORK = "VIRTUAL_NETWORK";
    
    /**
     * Azure
     */
    public static final String SUBNET = "SUBNET";
    
    /**
     * Azure
     */
    public static final String STORAGE_ACCOUNT = "STORAGE_ACCOUNT";
    
    /**
     * Azure
     */
    public static final String INSTANCE_COUNT = "INSTANCE_COUNT";
    
    /**
     * Azure
     */    
    public static final String VIRTUAL_MACHINE_IMAGE_ID = "VIRTUAL_MACHINE_IMAGE_ID";
    
    /**
     * Azure
     */    
    public static final String VM_NAME = "VmName";
    
    /**
     * Azure
     */
    public static final String INSTANCE_NAME = "INSTANCE_NAME";

    /**
     * Azure
     */
    public static final String TEMPLATE_NAME = "TEMPLATE_NAME";

    /**
     * Azure
     */
    public static final String TEMPLATE_PARAMETERS_NAME = "TEMPLATE_PARAMETERS_NAME";

    /**
     * Azure
     */
    public static final String DEPLOYMENT_NAME = "DEPLOYMENT_NAME";

    /**
     * 
     */
    public static final String FLOW_STATUS = "FLOW_STATUS";

    /**
     *
     */
    public static final String START_TIME = "START_TIME";

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
     * Reads the requested property from the available parameters. If no value
     * can be found, a RuntimeException will be thrown.
     *
     * @param sourceProps
     *            The property object to take the settings from
     * @param key
     *            The key to retrieve the setting for
     * @return the parameter value corresponding to the provided key
     */
    private static String getValidatedProperty(Map<String, String> sourceProps,
            String key) {
        String value = sourceProps.get(key);
        if (value == null) {
            String message = String.format("No value set for property '%s'",
                    key);
            logger.error(message);
            throw new RuntimeException(message);
        }
        return value;
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
     * Azure
     *
     * @return Azure
     */
    public String getUserName() {
        return getValidatedProperty(settings.getConfigSettings(), API_USER_NAME);
    	//return API_USER_NAMETest;
    }

    /**
     * Azure
     *
     * @return Azureã�®ãƒ¦ãƒ¼ã‚¶ãƒ¼ãƒ‘ã‚¹ãƒ¯ãƒ¼ãƒ‰
     */
    public String getPassword() {
       return getValidatedProperty(settings.getConfigSettings(), API_USER_PWD);
    //return API_USER_PWDTest;
    }

    /**
     * ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«ã�®é…�ç½®URLã‚’å�–å¾—ã€‚
     *
     * @return ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«ã�®é…�ç½®URL
     */
    public String getTemplateBaseUrl() {
       /*String baseUrl = getValidatedProperty(settings.getConfigSettings(),
                TEMPLATE_BASE_URL);
        return baseUrl.endsWith("/") ? baseUrl : (baseUrl + "/");*/
    	return getValidatedProperty(settings.getParameters(), TEMPLATE_BASE_URL);
    }

    /**
     * ã‚¿ã‚¤ãƒ ã‚¢ã‚¦ãƒˆæ™‚é–“(ãƒŸãƒªç§’)ã‚’å�–å¾—ã€‚
     *
     * @return ã‚¿ã‚¤ãƒ ã‚¢ã‚¦ãƒˆæ™‚é–“(ãƒŸãƒªç§’)
     */
    public long getReadyTimeout() {
        return Long.parseLong(getValidatedProperty(
                settings.getConfigSettings(), READY_TIMEOUT));
    }

    /* æŠ€è¡“ã‚µãƒ¼ãƒ“ã‚¹å®šç¾©ãƒ‘ãƒ©ãƒ¡ãƒ¼ã‚¿ãƒ¼ */

    /**
     * æ‰‹å‹•æ“�ä½œãƒ¡ãƒ¼ãƒ«ã‚¢ãƒ‰ãƒ¬ã‚¹ã‚’å�–å¾—ã€‚
     *
     * @return ãƒ¡ãƒ¼ãƒ«ã‚¢ãƒ‰ãƒ¬ã‚¹ã€�ã�¾ã�Ÿã�¯æ‰‹å‹•æ“�ä½œã�Œä¸�è¦�ã�ªå ´å�ˆ<code>null</code>
     */
    public String getMailForCompletion() {
        String value = settings.getParameters().get(MAIL_FOR_COMPLETION);
        return StringUtils.isBlank(value) ? null : value;
    }

    /**
     * Azureã�®ã‚µãƒ–ã‚¹ã‚¯ãƒªãƒ—ã‚·ãƒ§ãƒ³IDã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ã‚µãƒ–ã‚¹ã‚¯ãƒªãƒ—ã‚·ãƒ§ãƒ³ID
     */
    public String getSubscriptionId() {
        return getValidatedProperty(settings.getParameters(), SUBSCRIPTION_ID);
    	//return SUBSCRIPTION_IDTest;
    }

    /**
     * Azureã�®ãƒ†ãƒŠãƒ³ãƒˆIDã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ†ãƒŠãƒ³ãƒˆID
     */
    public String getTenantId() {
        return getValidatedProperty(settings.getParameters(), TENANT_ID);
    	//return TENANT_IDTest;
    }

    /**
     * Azureã�®ã‚¯ãƒ©ã‚¤ã‚¢ãƒ³ãƒˆIDã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ã‚¯ãƒ©ã‚¤ã‚¢ãƒ³ãƒˆID
     */
    public String getClientId() {
       return getValidatedProperty(settings.getParameters(), CLIENT_ID);
    	//return CLIENT_IDTest;
    }

    /**
     * Azureã�®ã‚¯ãƒ©ã‚¤ã‚¢ãƒ³ãƒˆã‚·ãƒ¼ã‚¯ãƒ¬ãƒƒãƒˆã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ã‚¯ãƒ©ã‚¤ã‚¢ãƒ³ãƒˆã‚·ãƒ¼ã‚¯ãƒ¬ãƒƒãƒˆ
     */
    public String getClientSecret() {
        return settings.getParameters().get(CLIENT_SECRET);
    	//return CLIENT_SECRETTest;
    }

    /**
     * Azureã�®ãƒªãƒ¼ã‚¸ãƒ§ãƒ³ã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒªãƒ¼ã‚¸ãƒ§ãƒ³
     */
    public String getRegion() {
        return getValidatedProperty(settings.getParameters(), REGION);
    }

    /**
     * Azureã�®ãƒªã‚½ãƒ¼ã‚¹ã‚°ãƒ«ãƒ¼ãƒ—å��ã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒªã‚½ãƒ¼ã‚¹ã‚°ãƒ«ãƒ¼ãƒ—å��
     */
    public String getResourceGroupName() {
    	
        return getValidatedProperty(settings.getParameters(),
                RESOURCE_GROUP_NAME);
    }
    

    public String getVirtualNetwork() {
    	return getValidatedProperty(settings.getParameters(),
                VIRTUAL_NETWORK);
	}

	public String getSubnet() {
		return getValidatedProperty(settings.getParameters(),
                SUBNET);
	}	
	
	public String getStorageAccount() {
		return getValidatedProperty(settings.getParameters(),
                STORAGE_ACCOUNT);
	}
	
	public String getInstanceCount() {
		return getValidatedProperty(settings.getParameters(),
				INSTANCE_COUNT);
	}

	public String getVirtualMachineImageID() {
		return getValidatedProperty(settings.getParameters(),
				VIRTUAL_MACHINE_IMAGE_ID);
	}
	public String getVMName() {
		return getValidatedProperty(settings.getParameters(),
				VM_NAME);
	}
	
	public String getInstanceName() {
		return getValidatedProperty(settings.getParameters(),
                INSTANCE_NAME);
	}

	/**
     * Azureã�®ãƒªã‚½ãƒ¼ã‚¹ã‚°ãƒ«ãƒ¼ãƒ—å��ã‚’è¨­å®šã�™ã‚‹ã€‚
     *
     * @param value
     *            Azureã�®ãƒªã‚½ãƒ¼ã‚¹ã‚°ãƒ«ãƒ¼ãƒ—å��
     */
    public void setResourceGroupName(String value) {
        settings.getParameters().put(RESOURCE_GROUP_NAME, value);
    }

    /**
     * Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«å��ã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«å��
     */
    private String getTemplateName() {
        return getValidatedProperty(settings.getParameters(), TEMPLATE_NAME);
    	
    }

    /**
     * Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«ã�®URLã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ•ã‚¡ã‚¤ãƒ«ã�®URL
     */
    public String getTemplateUrl() {
        try {
        	String url=new URL(new URL(getTemplateBaseUrl()), getTemplateName()).toExternalForm();
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot generate template URL: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ‘ãƒ©ãƒ¡ãƒ¼ã‚¿ãƒ¼ãƒ•ã‚¡ã‚¤ãƒ«å��ã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ‘ãƒ©ãƒ¡ãƒ¼ã‚¿ãƒ¼ãƒ•ã‚¡ã‚¤ãƒ«å��ã€�ã�¾ã�Ÿã�¯<code>null</code>
     */
    private String getTemplateParametersName() {
        String value = settings.getParameters().get(TEMPLATE_PARAMETERS_NAME);
        return StringUtils.isBlank(value) ? null : value;
    }

    /**
     * Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ‘ãƒ©ãƒ¡ãƒ¼ã‚¿ãƒ¼ãƒ•ã‚¡ã‚¤ãƒ«ã�®URLã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ†ãƒ³ãƒ—ãƒ¬ãƒ¼ãƒˆãƒ‘ãƒ©ãƒ¡ãƒ¼ã‚¿ãƒ¼ãƒ•ã‚¡ã‚¤ãƒ«ã�®URLã€�ã�¾ã�Ÿã�¯<code>null</code>
     */
    public String getTemplateParametersUrl() {
        String fileName = getTemplateParametersName();
        if (fileName == null) {
            return null;
        }
        try {
            return new URL(new URL(getTemplateBaseUrl()), fileName)
                    .toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Cannot generate template parameters URL: "
                            + e.getMessage(), e);
        }
    }

    /**
     * Azureã�®ãƒ‡ãƒ—ãƒ­ã‚¤ãƒ¡ãƒ³ãƒˆå��ã‚’å�–å¾—ã€‚
     *
     * @return Azureã�®ãƒ‡ãƒ—ãƒ­ã‚¤ãƒ¡ãƒ³ãƒˆå��
     */
    public String getDeploymentName() {
       return settings.getParameters().get(DEPLOYMENT_NAME);
    }

    /**
     * Azureã�®ãƒ‡ãƒ—ãƒ­ã‚¤ãƒ¡ãƒ³ãƒˆå��ã‚’è¨­å®šã€‚
     *
     * @param value
     *            Azureã�®ãƒ‡ãƒ—ãƒ­ã‚¤ãƒ¡ãƒ³ãƒˆå��
     */
    public void setDeploymentName(String value) {
        settings.getParameters().put(DEPLOYMENT_NAME, value);
        
    }

    /**
     * å‡¦ç�†é€²æ�—ã‚’å�–å¾—ã€‚
     *
     * @return å‡¦ç�†é€²æ�—
     */
    public FlowState getFlowState() {
        String status = settings.getParameters().get(FLOW_STATUS);
        return (status != null) ? FlowState.valueOf(status) : FlowState.FAILED;
    }

    /**
     * å‡¦ç�†é€²æ�—ã‚’è¨­å®šã€‚
     *
     * @param value
     *            å‡¦ç�†é€²æ�—
     */
    public void setFlowState(FlowState value) {
        settings.getParameters().put(FLOW_STATUS, value.toString());
    }

    /**
     * å‡¦ç�†é–‹å§‹æ™‚é–“(ãƒŸãƒªç§’)ã‚’å�–å¾—ã€‚
     *
     * @return å‡¦ç�†é–‹å§‹æ™‚é–“(ãƒŸãƒªç§’)
     */
    public String getStartTime() {
        return settings.getParameters().get(START_TIME);
    }

    /**
     * å‡¦ç�†é–‹å§‹æ™‚é–“(ãƒŸãƒªç§’)ã‚’è¨­å®šã€‚
     *
     * @param value
     *            å‡¦ç�†é–‹å§‹æ™‚é–“(ãƒŸãƒªç§’)
     */
    public void setStartTime(String value) {
        settings.getParameters().put(START_TIME, value);
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
        if (StringUtils.isBlank(locale)) {
            locale = "en";
        }
        return locale;
    }

    /**
     * ä½œæˆ�å‡¦ç�†å®Œäº†ãƒ¡ãƒ¼ãƒ«ã�«è¨˜è¼‰ã�™ã‚‹ã€�è¨­å®šæƒ…å ±ã‚’å�–å¾—ã€‚
     *
     * @return è¨­å®šæƒ…å ±
     */
    public String getConfigurationAsString() {
        StringBuffer details = new StringBuffer();
        details.append("\t\r\nAPIUserName: ");
        details.append(getUserName());
        details.append("\t\r\nTenantId: ");
        details.append(getTenantId());
        details.append("\t\r\nResourceGroupName: ");
        details.append(getResourceGroupName());
        details.append("\t\r\nTemplateUrl: ");
        details.append(getTemplateUrl());
        details.append("\t\r\nTemplateParametersUrl: ");
        details.append(StringUtils.defaultString(getTemplateParametersUrl()));
        details.append("\t\r\nRegion: ");
        details.append(getRegion());
        details.append("\t\r\n");
        return details.toString();
    }
}
