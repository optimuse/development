/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017
 *                                                                                                                                 
 *  Creation Date: 27 paź 2015                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.azure.controller;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import org.oscm.app.azure.data.FlowState;
import org.oscm.app.azure.exception.AzureClientException;
import org.oscm.app.v1_0.data.InstanceStatus;
import org.oscm.app.v1_0.data.ProvisioningSettings;
import org.oscm.app.v1_0.exceptions.APPlatformException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.oscm.app.azure.controller.PropertyHandler.API_USER_NAME;
import static org.oscm.app.azure.controller.PropertyHandler.CLIENT_ID;
import static org.oscm.app.azure.controller.PropertyHandler.CLIENT_SECRET;
import static org.oscm.app.azure.controller.PropertyHandler.FLOW_STATUS;
import static org.oscm.app.azure.controller.PropertyHandler.RESOURCE_GROUP_NAME;
import static org.oscm.app.azure.controller.PropertyHandler.TENANT_ID;

public class AzureControllerTest {

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
        String instanceId = "instanceId1";
        // when
        final InstanceStatus instanceStatus = ctrl.deleteInstance(instanceId, provSettingsMock);
        // then
        assertTrue(instanceStatus != null);
        assertTrue(instanceStatus.getChangedParameters().get(FLOW_STATUS)
                .equals(FlowState.DELETION_REQUESTED.toString()));
    }

    @Test
    public void modifyInstanceTest() throws APPlatformException {
        // given
        ProvisioningSettings provSettingsMock2 = mock(ProvisioningSettings.class);
        final HashMap<String, String> mock2parameters = fillParameters("2");
        mock2parameters.put(FLOW_STATUS, FlowState.FINISHED.toString());
        doReturn(mock2parameters).when(provSettingsMock2).getParameters();

        String instanceId = "instanceId1";

        fillParameters("2");
        // when
        final InstanceStatus instanceStatus = ctrl.modifyInstance(instanceId, provSettingsMock, provSettingsMock2);
        // then
        assertTrue(instanceStatus != null);
        assertTrue(instanceStatus.getChangedParameters().get(FLOW_STATUS)
                .equals(FlowState.MODIFICATION_REQUESTED.toString()));
    }

    @Test
    public void getInstanceStatusTest() throws APPlatformException {
        // given
        String instanceId = "instanceId1";
        // when
        final InstanceStatus instanceStatus = ctrl.getInstanceStatus(instanceId, provSettingsMock);
        // then
        assertTrue(instanceStatus != null);
    }

    @Test
    public void notifyInstanceTest() {
        //given
        //when
        //then
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
}
