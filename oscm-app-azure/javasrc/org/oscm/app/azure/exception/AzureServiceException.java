/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.exception;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.microsoft.windowsazure.exception.ServiceException;

public class AzureServiceException extends AzureClientException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4021541374694794813L;

    /**
     * Azure ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰ã€‚
     */
    private String errorCode = "";

    /**
     * Azure ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã€‚
     */
    private String errorMessage = "";

    /**
     * ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿ã€‚
     *
     * @param message
     * @param cause
     */
    public AzureServiceException(String message, Throwable cause) {
        super(message, cause);
        init();
    }

    /**
     * ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿ã€‚
     *
     * @param message
     */
    public AzureServiceException(String message) {
        super(message);
        init();
    }

    /**
     * ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿ã€‚
     *
     * @param cause
     */
    public AzureServiceException(Throwable cause) {
        super(cause);
        init();
    }

    /**
     * åˆ�æœŸå‡¦ç�†ã€‚
     * <p>
     * cause ã�« <code>ServiceException</code> ã�Œå�«ã�¾ã‚Œã‚‹å ´å�ˆã€�Azure
     * ã�®ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰ã�¨ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã‚’å�–å¾—ã�™ã‚‹ã€‚
     */
    private void init() {
        if (getCause() == null) {
            return;
        }
        if (getCause() instanceof ServiceException) {
            ServiceException ex = (ServiceException) getCause();
            if (ex.getError().getCode() != null) {
                this.errorCode = ex.getError().getCode();
                this.errorMessage = ex.getError().getMessage();
            } else if (ex.getMessage() != null) {
                // SDK ã�®ä»•æ§˜ã�«ã‚ˆã‚Šã€�ä¸Šè¨˜ã�§å�–å¾—ã�§ã��ã�ªã�„å ´å�ˆ message ã�‹ã‚‰æŠ½å‡º
                // azure-sdk-for-java/v0.9.3/ServiceException.java line: 261
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode responseDoc = objectMapper.readTree(ex
                            .getMessage());

                    JsonNode errorNode = responseDoc.get("Error");
                    if (errorNode == null) {
                        errorNode = responseDoc.get("error");
                    }
                    if (errorNode != null) {
                        responseDoc = errorNode;
                    }

                    if (responseDoc.get("Code") != null) {
                        this.errorCode = responseDoc.get("Code").getTextValue();
                    } else if (responseDoc.get("code") != null) {
                        this.errorCode = responseDoc.get("code").getTextValue();
                    }

                    if (responseDoc.get("Message") != null) {
                        this.errorMessage = responseDoc.get("Message")
                                .getTextValue();
                    } else if (responseDoc.get("message") != null) {
                        this.errorMessage = responseDoc.get("message")
                                .getTextValue();
                    }
                } catch (Exception e1) {
                    // å‡¦ç�†ã�ªã�—ã€‚
                }
            }
        }
    }

    /**
     * ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰ã‚’å�–å¾—ã€‚
     *
     * @return ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰ã‚’è¨­å®šã€‚
     *
     * @param ã‚¨ãƒ©ãƒ¼ã‚³ãƒ¼ãƒ‰
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã‚’å�–å¾—ã€‚
     *
     * @return ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã‚’è¨­å®šã€‚
     *
     * @param ã‚¨ãƒ©ãƒ¼ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
