package com.networkedassets.condoc.jsonDoclet.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for typeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="typeInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="wildcard" type="{}wildcard" minOccurs="0"/&gt;
 *         &lt;element name="generic" type="{}typeInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="qualified" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="dimension" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeInfo", propOrder = {
    "wildcard",
    "generic"
})
public class TypeInfo {

    protected Wildcard wildcard;
    protected List<TypeInfo> generic;
    @XmlAttribute(name = "qualified")
    protected String qualified;
    @XmlAttribute(name = "dimension")
    protected String dimension;

    /**
     * Gets the value of the wildcard property.
     *
     * @return
     *     possible object is
     *     {@link Wildcard }
     *
     */
    public Wildcard getWildcard() {
        return wildcard;
    }

    /**
     * Sets the value of the wildcard property.
     *
     * @param value
     *     allowed object is
     *     {@link Wildcard }
     *
     */
    public void setWildcard(Wildcard value) {
        this.wildcard = value;
    }

    /**
     * Gets the value of the generic property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the generic property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeneric().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInfo }
     * 
     * 
     */
    public List<TypeInfo> getGeneric() {
        if (generic == null) {
            generic = new ArrayList<TypeInfo>();
        }
        return this.generic;
    }

    /**
     * Gets the value of the qualified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualified() {
        return qualified;
    }

    /**
     * Sets the value of the qualified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualified(String value) {
        this.qualified = value;
    }

    /**
     * Gets the value of the dimension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * Sets the value of the dimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDimension(String value) {
        this.dimension = value;
    }

}
