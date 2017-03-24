package org.oscm.app.azure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.AbstractIterator;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.azure.management.resources.DeploymentOperations;
import com.microsoft.azure.management.resources.ResourceManagementClient;
import com.microsoft.azure.management.resources.models.Deployment;
import com.microsoft.azure.management.resources.models.DeploymentOperationsCreateResult;
import com.microsoft.azure.management.storage.models.StorageAccount;
import com.microsoft.windowsazure.core.utils.BOMInputStream;
import com.microsoft.windowsazure.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.controller.PropertyHandler;
import org.oscm.app.v1_0.exceptions.AbortException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by PLGrubskiM on 2017-03-22.
 */
public class AzureCommunicationTest {

    private static final String CLIENT_ID = "clientId";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REGION = "region";
    private static final String RERSOURCE_GROUP_NAME = "resGroup";
    private static final String STORAGE_ACCOUNT_NAME = "storageAcc";
    private static final String TEMPLATE_URL = "http://templateUrl";
    private static final String DEPLOYMENT_NAME = "deploymentName";

    AzureCommunication azureComm;
    ResourceManagementClient resourceManagementClient;
    PropertyHandler ph;

    @Before
    public void setUp() {
        ph = mock(PropertyHandler.class);
        resourceManagementClient = mock(ResourceManagementClient.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_emptyClientId() throws AbortException {
        // given
        azureComm = new AzureCommunication(ph);
        // when
        // then
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_emptyUsername() throws AbortException {
        // given
        when(ph.getClientId()).thenReturn(CLIENT_ID);
        // when
        azureComm = new AzureCommunication(ph);
        // then
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_emptyPwd() throws AbortException {
        // given
        when(ph.getClientId()).thenReturn(CLIENT_ID);
        when(ph.getUserName()).thenReturn(USERNAME);
        // when
        azureComm = new AzureCommunication(ph);
        // then
    }

    @Test
    public void constructor_Ok() {
        // given
        // when
        azureComm = new AzureCommunication(ph){
            @Override
            protected AuthenticationResult acquireToken(AuthenticationContext context, String clientId,
                                                        String apiUserName, String apiPassword)
                    throws ExecutionException, InterruptedException {

                String accessTokenType = "accessTokenType";
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";
                long expiresIn = 1000000L;
                String idToken = "idToken";
                boolean isMultipleResourcesRefreshToken = false;
                AuthenticationResult mockAuthRes = new AuthenticationResult(accessTokenType, accessToken, refreshToken,
                        expiresIn, idToken, null, isMultipleResourcesRefreshToken);
                return mockAuthRes;
            }
        };
        // then
        // no exceptions
    }

    @Test
    public void createInstanceTest() throws Exception {
        // given
        when(ph.getRegion()).thenReturn(REGION);
        when(ph.getResourceGroupName()).thenReturn(RERSOURCE_GROUP_NAME);
        when(ph.getStorageAccount()).thenReturn(STORAGE_ACCOUNT_NAME);
        when(ph.getTemplateUrl()).thenReturn(TEMPLATE_URL);
        when(ph.getDeploymentName()).thenReturn(DEPLOYMENT_NAME);
        azureComm = prepareAzureCommWithMocks();
        // when
        azureComm.createInstance();
        // then
        verify(resourceManagementClient, times(1)).getDeploymentsOperations();
    }


    private AzureCommunication prepareAzureCommWithMocks() {
        return new AzureCommunication(ph){
            @Override
            protected AuthenticationResult acquireToken(AuthenticationContext context, String clientId,
                                                        String apiUserName, String apiPassword)
                    throws ExecutionException, InterruptedException {

                String accessTokenType = "accessTokenType";
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";
                long expiresIn = 1000000L;
                String idToken = "idToken";
                boolean isMultipleResourcesRefreshToken = false;
                AuthenticationResult mockAuthRes = new AuthenticationResult(accessTokenType, accessToken, refreshToken,
                        expiresIn, idToken, null, isMultipleResourcesRefreshToken);
                return mockAuthRes;
            }

            @Override
            protected void createOrUpdate() throws ServiceException, IOException, URISyntaxException {

            }

            @Override
            protected Iterator<StorageAccount> getStorageAccounts() throws ServiceException, IOException, URISyntaxException {
                Iterator<StorageAccount> storageAccs = new AbstractIterator<StorageAccount>() {

                    int howMany = 0;
                    @Override
                    protected StorageAccount computeNext() {
                        if (howMany >= 1) {
                            return null;
                        }
                        StorageAccount sa = mock(StorageAccount.class);
                        when(sa.getName()).thenReturn(STORAGE_ACCOUNT_NAME);
                        howMany++;
                        return sa;
                    }
                };
                return storageAccs;
            }

            @Override
            protected BOMInputStream getBOMInputStream(URL source, String url) throws IOException {
                BOMInputStream in = mock(BOMInputStream.class);
                return in;
            }

            @Override
            protected String getBOMtoString(BOMInputStream in) throws IOException {
                return "bomToString";
            }

            @Override
            public ResourceManagementClient getResourceClient() {
                DeploymentOperations operations = mock(DeploymentOperations.class);
                try {
                    DeploymentOperationsCreateResult value = mock(DeploymentOperationsCreateResult.class);
                    when(operations.createOrUpdate(any(String.class), any(String.class), any(Deployment.class)))
                            .thenReturn(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                when(resourceManagementClient.getDeploymentsOperations()).thenReturn(operations);
                return resourceManagementClient;
            }
        };
    }
}
