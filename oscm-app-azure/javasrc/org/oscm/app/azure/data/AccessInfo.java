/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.oscm.app.azure.i18n.Messages;

public class AccessInfo {

    /**
     * ã‚¢ã‚¯ã‚»ã‚¹æƒ…å ±ã€
     */
    private List<AzureAccess> azureAccesses;

    /**
     * ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿ã€‚
     */
    public AccessInfo() {
        this.azureAccesses = new ArrayList<>();
    }

    /**
     * ã‚¢ã‚¯ã‚»ã‚¹æƒ…å ±ã‚’å�–å¾—ã€‚
     *
     * @return ã‚¢ã‚¯ã‚»ã‚¹æƒ…å ±
     */
    public List<AzureAccess> getAzureAccesses() {
        return azureAccesses;
    }

    /**
     * ã‚¢ã‚¯ã‚»ã‚¹æƒ…å ±ã‚’è¨­å®šã€‚
     *
     * @param ã‚¢ã‚¯ã‚»ã‚¹æƒ…å ±
     */
    public void setAzureAccesses(List<AzureAccess> azureAccesses) {
        this.azureAccesses = azureAccesses;
    }

    /**
     * å‡ºåŠ›æƒ…å ±ã‚’å�–å¾—ã€‚
     *
     * @param locale
     *            å‡ºåŠ›ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã�®è¨€èªž
     * @return å‡ºåŠ›æƒ…å ±
     */
    public String getOutput(String locale) {
        if (azureAccesses == null || azureAccesses.isEmpty()) {
            return Messages.get(locale, "accessInfo_NOT_AVAILABLE");
        }
        Iterator<AzureAccess> accessesItr = azureAccesses.iterator();
        List<String> messages = new ArrayList<>();
        while (accessesItr.hasNext()) {
            AzureAccess access = accessesItr.next();
            String message ;
            
        	if((access.getPublicIpAddress().isEmpty()&&access.getPrivateIpAddress().isEmpty()))
            {
            	message = Messages.get(locale, "accessInfo_RUNNING",access.getVmName(),access.getState());
            }
        	else if((access.getPublicIpAddress().isEmpty()))
            {
            	message = Messages.get(locale, "accessInfo_RUNNING_with_privateIP",access.getVmName(),access.getPrivateIpAddress(),access.getState());
            }
        	else if((access.getPrivateIpAddress().isEmpty()))
            {
            	message = Messages.get(locale, "accessInfo_RUNNING_with_publicIP",access.getVmName(),access.getPublicIpAddress(),access.getState());
            }
            else
            	message = Messages.get(locale, "accessInfo_RUNNING_with_all_IP",access.getVmName(),access.getPublicIpAddress(),access.getPrivateIpAddress(),access.getState());

            messages.add(message);
        }
        return StringUtils.join(messages, "; ");
    }
}
