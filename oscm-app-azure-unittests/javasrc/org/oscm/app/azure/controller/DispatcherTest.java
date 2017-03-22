/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2017
 *
 *******************************************************************************/
package org.oscm.app.azure.controller;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.AzureCommunication;
import org.oscm.app.azure.data.AccessInfo;
import org.oscm.app.azure.data.AzureState;
import org.oscm.app.azure.data.FlowState;
import org.oscm.app.v1_0.data.InstanceStatus;
import org.oscm.app.v1_0.data.ProvisioningSettings;
import org.oscm.app.v1_0.exceptions.APPlatformException;
import org.oscm.app.v1_0.intf.APPlatformService;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
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
    private PropertyHandler ph;
    private AzureCommunication mockAzureComm;
    private ProvisioningSettings settings;

    @Before
    public void setUp() {
        mockAzureComm = mock(AzureCommunication.class);
        mockPlatformService = mock(APPlatformService.class);
        HashMap<String, String> configSettings = null;
        settings = new ProvisioningSettings(createParameters(), configSettings, "en");
        ph = new PropertyHandler(settings);
        dispatcher = new Dispatcher(mockPlatformService, INSTANCE_ID, ph) {
            @Override
            public AzureCommunication getAzureCommunication() {
                return mockAzureComm;
            }
        };

    }

    @Test
    public void dispatchTest_provisioning() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.CREATION_REQUESTED.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo(FlowState.STARTING.toString())).thenReturn(output);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.CREATING.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.CREATING.toString()));
    }

    @Test
    public void dispatchTest_operation_startRequested() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.START_REQUESTED.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo(FlowState.STARTING.toString())).thenReturn(output);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.STARTING.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.STARTING.toString()));
    }

    @Test
    public void dispatchTest_operation_starting() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.STARTING.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo("RUNNING")).thenReturn(output);
        AzureState mockState = mock(AzureState.class);
        when(mockState.isSucceeded()).thenReturn(true);
        when(mockAzureComm.getStartingState()).thenReturn(mockState);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.FINISHED.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.FINISHED.toString()));
    }

    @Test
    public void dispatchTest_operation_stopRequested() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.STOP_REQUESTED.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo(FlowState.STOPPING.toString())).thenReturn(output);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.STOPPING.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.STOPPING.toString()));
    }

    @Test
    public void dispatchTest_operation_stopping() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.STOPPING.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo("STOPPED")).thenReturn(output);
        AzureState mockState = mock(AzureState.class);
        when(mockState.isSucceeded()).thenReturn(true);
        when(mockAzureComm.getStoppingState()).thenReturn(mockState);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.FINISHED.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.FINISHED.toString()));
    }

    @Test
    public void dispatchTest_activation() throws APPlatformException {
        // given
        settings.getParameters().put(PropertyHandler.FLOW_STATUS, FlowState.ACTIVATION_REQUESTED.toString());

        AccessInfo output = mock(AccessInfo.class);
        when(mockAzureComm.getAccessInfo(FlowState.STARTING.toString())).thenReturn(output);

        // when
        final InstanceStatus instanceStatus = dispatcher.dispatch();

        // then
        assertTrue(ph.getFlowState().toString().equals(FlowState.STARTING.toString()));
        assertTrue(instanceStatus.getChangedParameters().get(PropertyHandler.FLOW_STATUS)
                .equals(FlowState.STARTING.toString()));
    }

    private HashMap<String, String> createParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(PropertyHandler.CLIENT_ID, CLENT_ID);
        parameters.put(PropertyHandler.TENANT_ID, TENANT_ID);
        parameters.put(PropertyHandler.API_USER_NAME, USERNAME);
        parameters.put(PropertyHandler.API_USER_PWD, PASSWORD);
        return parameters;
    }
}
