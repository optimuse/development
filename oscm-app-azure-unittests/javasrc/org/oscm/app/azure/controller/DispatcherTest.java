/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2017
 *
 *******************************************************************************/
package org.oscm.app.azure.controller;

import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.AzureCommunication;
import org.oscm.app.azure.data.AccessInfo;
import org.oscm.app.azure.data.FlowState;
import org.oscm.app.v1_0.data.InstanceStatus;
import org.oscm.app.v1_0.data.ProvisioningSettings;
import org.oscm.app.v1_0.exceptions.APPlatformException;
import org.oscm.app.v1_0.exceptions.SuspendException;
import org.oscm.app.v1_0.intf.APPlatformService;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


/**
 * Created by PLGrubskiM on 2017-03-21.
 */
public class DispatcherTest {

    private static final String INSTANCE_ID = "instanceId";
    private static final String TENANT_ID = "tenantId";
    private static final String CLENT_ID = "clientId";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private Dispatcher dispatcher;
    private APPlatformService mockPlatformService;
    private PropertyHandler mockPropertyHandler;
    private AzureCommunication mockAzureComm;

    @Before
    public void setUp() {
        mockAzureComm = mock(AzureCommunication.class);
        mockPlatformService = mock(APPlatformService.class);
        mockPropertyHandler = mock(PropertyHandler.class);
        when(mockPropertyHandler.getClientId()).thenReturn(CLENT_ID);
        when(mockPropertyHandler.getTenantId()).thenReturn(TENANT_ID);
        when(mockPropertyHandler.getUserName()).thenReturn(USERNAME);
        when(mockPropertyHandler.getPassword()).thenReturn(PASSWORD);
        when(mockPropertyHandler.getCustomerLocale()).thenReturn("en");
        ProvisioningSettings mockSettings = mock(ProvisioningSettings.class);
        when(mockPropertyHandler.getSettings()).thenReturn(mockSettings);

        dispatcher = new Dispatcher(mockPlatformService, INSTANCE_ID, mockPropertyHandler) {
            @Override
            public AzureCommunication getAzureCommunication() {
                return mockAzureComm;
            }
        };

    }

    @Test
    public void dispatchTest_provisioning() {
        // given
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.CREATING);
        // when
        final InstanceStatus instanceStatus;
        try {
            instanceStatus = dispatcher.dispatch();
        } catch (APPlatformException e) {
            // then
            // assert the request is sent to Azure (fails because of invalid credentials)
            assertTrue(e instanceof SuspendException);
        }
    }

    @Test
    public void dispatchTest_operation_startRequested() throws APPlatformException {
        // given
        mockPropertyHandler = spy(mockPropertyHandler);
        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo("STARTING")).thenReturn(output);
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.START_REQUESTED);
        // when
        final InstanceStatus dispatch = dispatcher.dispatch();
        // then
        assertTrue(mockPropertyHandler.getFlowState().equals(FlowState.STARTING.toString()));
    }

    @Test
    public void dispatchTest_operation_starting() {
        // given
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.STARTING);
        // when
        final InstanceStatus instanceStatus;
        try {
            instanceStatus = dispatcher.dispatch();
        } catch (APPlatformException e) {
            // then
            // assert the request is sent to Azure (fails because of invalid credentials)
            assertTrue(e instanceof SuspendException);
        }
    }

    @Test
    public void dispatchTest_operation_stopRequested() {
        // given
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.STOP_REQUESTED);
        // when
        final InstanceStatus instanceStatus;
        try {
            instanceStatus = dispatcher.dispatch();
        } catch (APPlatformException e) {
            // then
            // assert the request is sent to Azure (fails because of invalid credentials)
            assertTrue(e instanceof SuspendException);
        }
    }

    @Test
    public void dispatchTest_operation_stopping() {
        // given
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.STOPPING);
        // when
        final InstanceStatus instanceStatus;
        try {
            instanceStatus = dispatcher.dispatch();
        } catch (APPlatformException e) {
            // then
            // assert the request is sent to Azure (fails because of invalid credentials)
            assertTrue(e instanceof SuspendException);
        }
    }

    @Test
    public void dispatchTest_activation() {
        // given
        when(mockPropertyHandler.getFlowState()).thenReturn(FlowState.CREATING);
        // when
        final InstanceStatus instanceStatus;
        try {
            instanceStatus = dispatcher.dispatch();
        } catch (APPlatformException e) {
            // then
            // assert the request is sent to Azure (fails because of invalid credentials)
            assertTrue(e instanceof SuspendException);
        }
    }
}
