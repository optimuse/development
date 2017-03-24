package org.oscm.app.azure;

import com.microsoft.azure.management.resources.ResourceManagementClient;
import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.controller.PropertyHandler;
import org.oscm.app.v1_0.exceptions.AbortException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by PLGrubskiM on 2017-03-22.
 */
public class AzureCommunicationTest {

    private static final String CLIENT_ID = "clientId";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

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

}
