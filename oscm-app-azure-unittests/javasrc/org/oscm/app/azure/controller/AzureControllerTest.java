/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017
 *                                                                                                                                 
 *  Creation Date: 27 paź 2015                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.azure.controller;

import java.util.HashMap;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.data.FlowState;
import org.oscm.app.azure.exception.AzureClientException;
import org.oscm.app.v1_0.data.InstanceStatus;
import org.oscm.app.v1_0.data.ProvisioningSettings;
import org.oscm.app.v1_0.exceptions.APPlatformException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.oscm.app.azure.controller.PropertyHandler.API_USER_NAME;
import static org.oscm.app.azure.controller.PropertyHandler.CLIENT_ID;
import static org.oscm.app.azure.controller.PropertyHandler.CLIENT_SECRET;
import static org.oscm.app.azure.controller.PropertyHandler.FLOW_STATUS;
import static org.oscm.app.azure.controller.PropertyHandler.RESOURCE_GROUP_NAME;
import static org.oscm.app.azure.controller.PropertyHandler.TENANT_ID;

public class AzureControllerTest {

    private static final String INSTANCE_ID_1 = "instanceId1";

    private AzureController ctrl;
    private ProvisioningSettings provSettingsMock;
    @Before
    public void setup() {
        ctrl = new AzureController();
        provSettingsMock = mock(ProvisioningSettings.class);
        final HashMap<String, String> parameters = fillParameters("1");
        doReturn(parameters).when(provSettingsMock).getParameters();
    }

    @Test(expected = AzureClientException.class)
    public void createInstanceTest() throws APPlatformException {
        // given
        // when
        ctrl.createInstance(provSettingsMock);
        // then
        // expect exception because of invalid authentication token
    }

    @Test
    public void deleteInstanceTest() throws APPlatformException {
        // given
        // when
        final InstanceStatus instanceStatus = ctrl.deleteInstance(INSTANCE_ID_1, provSettingsMock);
        // then
        assertTrue(instanceStatus != null);
        assertTrue(instanceStatus.getChangedParameters().get(FLOW_STATUS)
                .equals(FlowState.DELETION_REQUESTED.toString()));
    }

    @Test
    public void modifyInstanceTest() throws APPlatformException {
        // given
        final ProvisioningSettings parametersMockWithFlowState = getParametersMockWithFlowState("2", FlowState.FINISHED);
        // when
        final InstanceStatus instanceStatus = ctrl.modifyInstance(INSTANCE_ID_1, provSettingsMock, parametersMockWithFlowState);
        // then
        assertTrue(instanceStatus != null);
        assertTrue(instanceStatus.getChangedParameters().get(FLOW_STATUS)
                .equals(FlowState.MODIFICATION_REQUESTED.toString()));
    }

    @Test
    public void getInstanceStatusTest() throws APPlatformException {
        // given
        // when
        final InstanceStatus instanceStatus = ctrl.getInstanceStatus(INSTANCE_ID_1, provSettingsMock);
        // then
        assertTrue(instanceStatus != null);

    }

    @Test
    public void notifyInstanceTest() throws APPlatformException {
        //given
        Properties propertiesMock = mock(Properties.class);
        when(propertiesMock.get(any(String.class))).thenReturn("notfinish");
        //when
        final InstanceStatus instanceStatus = ctrl.notifyInstance(INSTANCE_ID_1, provSettingsMock, propertiesMock);
        //then
        assertTrue(instanceStatus == null);

    }

    @Test
    public void activateInstanceTest() {
        //given
        //when
        //then
    }

    @Test
    public void deactivateInstanceTest() {
        //given
        //when
        //then
    }

    @Test
    public void executeServiceOperation() {
        //given
        //when
        //then
    }

    @Test
    public void createUsers() {
        //given
        //when
        //then
    }

    @Test
    public void deleteUsers() {
        //given
        //when
        //then
    }

    @Test
    public void updateUsers() {
        //given
        //when
        //then
    }

    @Test
    public void getControllerStatus() {
        //given
        //when
        //then
    }

    @Test
    public void getOperationParameters() {
        //given
        //when
        //then
    }

    @Test
    public void setControllerSettings() {
        //given
        //when
        //then
    }

    private HashMap<String, String> fillParameters(String modifier) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put(RESOURCE_GROUP_NAME, "res" + modifier);
        parameters.put(TENANT_ID, "tenant1" + modifier);
        parameters.put(CLIENT_ID, "client1" + modifier);
        parameters.put(API_USER_NAME, "client1" + modifier);
        parameters.put(CLIENT_SECRET, "secret1" + modifier);

        return parameters;
    }

    private ProvisioningSettings getParametersMockWithFlowState(String modifier, FlowState flowState) {
        ProvisioningSettings provSettingsMock = mock(ProvisioningSettings.class);
        final HashMap<String, String> mockParameters = fillParameters(modifier);
        mockParameters.put(FLOW_STATUS, flowState.toString());
        doReturn(mockParameters).when(provSettingsMock).getParameters();
        return provSettingsMock;

    }
}
