/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.data;

import com.microsoft.azure.management.network.models.ProvisioningState;

public class AzureState {

    /**
     * å‡¦ç�†çŠ¶æ…‹ã€
     */
    private String provisioningState;

    /**
     * çŠ¶æ…‹ã‚³ãƒ¼ãƒ‰ã€‚
     */
    private String statusCode = "";

    /**
     * ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿ã€‚
     */
    public AzureState(String provisioningState) {
        this.provisioningState = provisioningState;
    }

    /**
     * å‡¦ç�†çŠ¶æ…‹ã‚’å�–å¾—ã€‚
     *
     * @return å‡¦ç�†çŠ¶æ…‹
     */
    public String getProvisioningState() {
        return provisioningState;
    }

    /**
     * å‡¦ç�†çŠ¶æ…‹ã‚’è¨­å®šã€‚
     *
     * @param å‡¦ç�†çŠ¶æ…‹
     */
    public void setProvisioningState(String provisioningState) {
        this.provisioningState = provisioningState;
    }

    /**
     * çŠ¶æ…‹ã‚³ãƒ¼ãƒ‰ã‚’å�–å¾—ã€‚
     *
     * @return çŠ¶æ…‹ã‚³ãƒ¼ãƒ‰
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * çŠ¶æ…‹ã‚³ãƒ¼ãƒ‰ã‚’è¨­å®šã€‚
     *
     * @param çŠ¶æ…‹ã‚³ãƒ¼ãƒ‰
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * å‡¦ç�†æˆ�åŠŸåˆ¤å®šã€‚
     *
     * @return æˆ�åŠŸã�®å ´å�ˆ<code>true</code>ã€�ã��ã‚Œä»¥å¤–<code>false</code>
     */
    public boolean isSucceeded() {
        return ProvisioningState.SUCCEEDED.equals(provisioningState);
    }

    /**
     * å‰Šé™¤æˆ�åŠŸåˆ¤å®šã€‚
     *
     * @return æˆ�åŠŸã�®å ´å�ˆ<code>true</code>ã€�ã��ã‚Œä»¥å¤–<code>false</code>
     */
    public boolean isDeleted() {
        return ProvisioningState.DELETING.equals(provisioningState);
    }

    /**
     * å‡¦ç�†å¤±æ•—åˆ¤å®šã€‚
     *
     * @return å¤±æ•—ã�®å ´å�ˆ<code>true</code>ã€�ã��ã‚Œä»¥å¤–<code>false</code>
     */
    public boolean isFailed() {
        return ProvisioningState.FAILED.equals(provisioningState);
    }
}
