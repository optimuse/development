/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016
 *
 *  Creation Date: 2016-07-29
 *
 *******************************************************************************/
package org.oscm.app.azure.exception;

import com.microsoft.windowsazure.exception.ServiceException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class AzureServiceException extends AzureClientException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4021541374694794813L;

    /**
     * Azure
     */
    private String errorCode = "";

    /**
     * Azure
     */
    private String errorMessage = "";

    /**
     * @param message
     * @param cause
     */
    public AzureServiceException(String message, Throwable cause) {
        super(message, cause);
        init();
    }

    /**
     * @param message
     */
    public AzureServiceException(String message) {
        super(message);
        init();
    }

    /**
     * @param cause
     */
    public AzureServiceException(Throwable cause) {
        super(cause);
        init();
    }

    /**
     * Azure
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
     * Azure
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Azure
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Azure
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Azure
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
