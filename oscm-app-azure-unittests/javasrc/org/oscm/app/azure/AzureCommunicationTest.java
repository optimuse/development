package org.oscm.app.azure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.AbstractIterator;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.azure.management.compute.ComputeManagementClient;
import com.microsoft.azure.management.compute.VirtualMachineOperations;
import com.microsoft.azure.management.compute.models.NetworkProfile;
import com.microsoft.azure.management.compute.models.VirtualMachine;
import com.microsoft.azure.management.compute.models.VirtualMachineGetResponse;
import com.microsoft.azure.management.resources.DeploymentOperations;
import com.microsoft.azure.management.resources.ResourceManagementClient;
import com.microsoft.azure.management.resources.models.Deployment;
import com.microsoft.azure.management.resources.models.DeploymentExistsResult;
import com.microsoft.azure.management.resources.models.DeploymentOperationsCreateResult;
import com.microsoft.azure.management.resources.models.LongRunningOperationResponse;
import com.microsoft.azure.management.storage.StorageAccountOperations;
import com.microsoft.azure.management.storage.StorageManagementClient;
import com.microsoft.azure.management.storage.models.StorageAccount;
import com.microsoft.azure.management.storage.models.StorageAccountKeys;
import com.microsoft.azure.management.storage.models.StorageAccountListKeysResponse;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.windowsazure.core.OperationResponse;
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
    private static final String VMNAME = "vmname";
    private static final String RERSOURCE_GROUP_NAME = "resGroup";
    private static final String STORAGE_ACCOUNT_NAME = "storageAcc";
    private static final String TEMPLATE_URL = "http://templateUrl";
    private static final String DEPLOYMENT_NAME = "deploymentName";
    private static final String TEMPLATE_PARAMETER_URL = "http://templateParamUrl";
    private static final String IMAGE_ID_WINDOWS_SERVER_2012 = "WindowsServer 2012-R2-Datacenter";
    private static final String IMAGE_ID_WINDOWS_SERVER_2016 = "WindowsServer 2016-Datacenter";
    private static final String IMAGE_ID_REDHAT = "Linux RedHat";
    private static final String IMAGE_ID_INVALID = "Invalid";

    AzureCommunication azureComm;
    ResourceManagementClient resourceManagementClient;
    ComputeManagementClient computeManagementClient;
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

    @Test
    public void updateInstanceTest() throws AbortException {
        // given
        when(ph.getRegion()).thenReturn(REGION);
        when(ph.getResourceGroupName()).thenReturn(RERSOURCE_GROUP_NAME);
        when(ph.getStorageAccount()).thenReturn(STORAGE_ACCOUNT_NAME);
        when(ph.getTemplateUrl()).thenReturn(TEMPLATE_URL);
        when(ph.getDeploymentName()).thenReturn(DEPLOYMENT_NAME);
        when(ph.getTemplateParametersUrl()).thenReturn(TEMPLATE_PARAMETER_URL);
        when(ph.getInstanceCount()).thenReturn("1");
        when(ph.getVirtualMachineImageID()).thenReturn(IMAGE_ID_WINDOWS_SERVER_2012);
        azureComm = prepareAzureCommWithMocks();
        // when
        azureComm.updateInstance();
        // then
        verify(resourceManagementClient, times(1)).getDeploymentsOperations();
    }

    @Test
    public void deleteInstanceTest() throws AbortException {
        // given
        when(ph.getRegion()).thenReturn(REGION);
        when(ph.getResourceGroupName()).thenReturn(RERSOURCE_GROUP_NAME);
        when(ph.getStorageAccount()).thenReturn(STORAGE_ACCOUNT_NAME);
        when(ph.getTemplateUrl()).thenReturn(TEMPLATE_URL);
        when(ph.getDeploymentName()).thenReturn(DEPLOYMENT_NAME);
        when(ph.getTemplateParametersUrl()).thenReturn(TEMPLATE_PARAMETER_URL);
        when(ph.getInstanceCount()).thenReturn("1");
        when(ph.getVirtualMachineImageID()).thenReturn(IMAGE_ID_WINDOWS_SERVER_2012);
        when(ph.getVMName()).thenReturn(VMNAME);
        azureComm = prepareAzureCommWithMocks();
        // when
        azureComm.deleteInstance();
        // then
        verify(resourceManagementClient, times(2)).getDeploymentsOperations();
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
                    LongRunningOperationResponse result = mock(LongRunningOperationResponse.class);
                    when(operations.beginDeleting(any(String.class), any(String.class))).thenReturn(result);
                    DeploymentExistsResult check = mock(DeploymentExistsResult.class);
                    when(check.isExists()).thenReturn(false);
                    when(operations.checkExistence(any(String.class), any(String.class))).thenReturn(check);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                when(resourceManagementClient.getDeploymentsOperations()).thenReturn(operations);
                return resourceManagementClient;
            }

            @Override
            public ComputeManagementClient getComputeClient() {
                computeManagementClient = mock(ComputeManagementClient.class);
                VirtualMachineOperations vmOps = mock(VirtualMachineOperations.class);
                try {
                    VirtualMachineGetResponse responseValue = mock(VirtualMachineGetResponse.class);
                    VirtualMachine vmMock = mock(VirtualMachine.class);
                    when(vmMock.getName()).thenReturn(VMNAME);
                    when(vmMock.getProvisioningState()).thenReturn("Deleted");
                    NetworkProfile networkProfile = mock(NetworkProfile.class);
                    when(vmMock.getNetworkProfile()).thenReturn(networkProfile);
                    when(responseValue.getVirtualMachine()).thenReturn(vmMock);
                    when(vmOps.get(any(String.class), any(String.class))).thenReturn(responseValue);
                    when(vmOps.beginDeleting(any(String.class), any(String.class))).thenReturn(null);
                    when(computeManagementClient.getVirtualMachinesOperations()).thenReturn(vmOps);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return computeManagementClient;
            }

            @Override
            public StorageManagementClient getStorageClient() {
                StorageManagementClient mockStorageClient = mock(StorageManagementClient.class);
                StorageAccountOperations operations = mock(StorageAccountOperations.class);
                try {
                    StorageAccountListKeysResponse response = mock(StorageAccountListKeysResponse.class);
                    StorageAccountKeys keys = mock(StorageAccountKeys.class);
                    String key = "someKey";
                    when(keys.getKey1()).thenReturn(key);
                    when(response.getStorageAccountKeys()).thenReturn(keys);
                    when(operations.listKeys(any(String.class), any(String.class))).thenReturn(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                try {
                    OperationResponse result = mock(OperationResponse.class);
                    when(operations.delete(any(String.class), any(String.class))).thenReturn(result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                when(mockStorageClient.getStorageAccountsOperations()).thenReturn(operations);
                return mockStorageClient;
            }

            @Override
            protected Map<String, Object> getParametersMap(String parameters) {
                Map<String, Object> parametersMap = prepareParametersMap();
                return parametersMap;
            }

            @Override
            protected CloudStorageAccount parseConnectionString(String connectionString) throws URISyntaxException, InvalidKeyException {
                StorageCredentials storageCredentials = mock(StorageCredentials.class);
                when(storageCredentials.getAccountName()).thenReturn("accname");
                return new CloudStorageAccount(storageCredentials);
            }

            @Override
            protected Iterator<CloudBlobContainer> getCloudBlobContainerIterator(CloudBlobClient client){
                Iterator<CloudBlobContainer> mockIterator = mock(Iterator.class);
                return mockIterator;
            }
        };
    }

    private Map<String, Object> prepareParametersMap() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> parametersMap = new HashMap<>();
        Map<String, String> mockMap = new HashMap<>();
        parametersMap.put("network", mockMap);
        parametersMap.put("subnet", mockMap);
        parametersMap.put("imagePublisher", mockMap);
        parametersMap.put("imageOffer", mockMap);
        parametersMap.put("imageSku", mockMap);
        parametersMap.put("vmName", mockMap);
        parametersMap.put("networkInterface", mockMap);
        parametersMap.put("storageAccountName", mockMap);
        parametersMap.put("numberOfInstances", mockMap);
        map.put("parameters", parametersMap);
        return map;
    }
}
