package com.networkedassets.condoc.jsonDoclet.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for wildcard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wildcard"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="extendsBound" type="{}typeInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="superBound" type="{}typeInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wildcard", propOrder = {
    "extendsBound",
    "superBound"
})
public class Wildcard {

    protected List<TypeInfo> extendsBound;
    protected List<TypeInfo> superBound;

    /**
     * Gets the value of the extendsBound property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extendsBound property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtendsBound().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInfo }
     * 
     * 
     */
    public List<TypeInfo> getExtendsBound() {
        if (extendsBound == null) {
            extendsBound = new ArrayList<TypeInfo>();
        }
        return this.extendsBound;
    }

    /**
     * Gets the value of the superBound property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the superBound property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuperBound().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInfo }
     * 
     * 
     */
    public List<TypeInfo> getSuperBound() {
        if (superBound == null) {
            superBound = new ArrayList<TypeInfo>();
        }
        return this.superBound;
    }

}
