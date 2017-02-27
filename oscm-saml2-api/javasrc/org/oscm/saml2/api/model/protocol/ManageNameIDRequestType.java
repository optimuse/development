/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2017
 *******************************************************************************/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.22 at 12:50:01 PM CEST 
//

package org.oscm.saml2.api.model.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.oscm.saml2.api.model.assertion.EncryptedElementType;
import org.oscm.saml2.api.model.assertion.NameIDType;

/**
 * <p>
 * Java class for ManageNameIDRequestType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ManageNameIDRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}RequestAbstractType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedID"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}NewID"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}NewEncryptedID"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}Terminate"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManageNameIDRequestType", propOrder = { "nameID",
        "encryptedID", "newID", "newEncryptedID", "terminate" })
public class ManageNameIDRequestType extends RequestAbstractType {

    @XmlElement(name = "NameID", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected NameIDType nameID;
    @XmlElement(name = "EncryptedID", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected EncryptedElementType encryptedID;
    @XmlElement(name = "NewID")
    protected String newID;
    @XmlElement(name = "NewEncryptedID")
    protected EncryptedElementType newEncryptedID;
    @XmlElement(name = "Terminate")
    protected TerminateType terminate;

    /**
     * Gets the value of the nameID property.
     * 
     * @return possible object is {@link NameIDType }
     * 
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     * 
     * @param value
     *            allowed object is {@link NameIDType }
     * 
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the encryptedID property.
     * 
     * @return possible object is {@link EncryptedElementType }
     * 
     */
    public EncryptedElementType getEncryptedID() {
        return encryptedID;
    }

    /**
     * Sets the value of the encryptedID property.
     * 
     * @param value
     *            allowed object is {@link EncryptedElementType }
     * 
     */
    public void setEncryptedID(EncryptedElementType value) {
        this.encryptedID = value;
    }

    /**
     * Gets the value of the newID property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getNewID() {
        return newID;
    }

    /**
     * Sets the value of the newID property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setNewID(String value) {
        this.newID = value;
    }

    /**
     * Gets the value of the newEncryptedID property.
     * 
     * @return possible object is {@link EncryptedElementType }
     * 
     */
    public EncryptedElementType getNewEncryptedID() {
        return newEncryptedID;
    }

    /**
     * Sets the value of the newEncryptedID property.
     * 
     * @param value
     *            allowed object is {@link EncryptedElementType }
     * 
     */
    public void setNewEncryptedID(EncryptedElementType value) {
        this.newEncryptedID = value;
    }

    /**
     * Gets the value of the terminate property.
     * 
     * @return possible object is {@link TerminateType }
     * 
     */
    public TerminateType getTerminate() {
        return terminate;
    }

    /**
     * Sets the value of the terminate property.
     * 
     * @param value
     *            allowed object is {@link TerminateType }
     * 
     */
    public void setTerminate(TerminateType value) {
        this.terminate = value;
    }

}
