package com.networkedassets.condoc.jsonDoclet.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="package" type="{}package" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="index" type="{}index"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "_package",
    "index"
})
@XmlRootElement(name = "root")
public class Root {

    @XmlElement(name = "package")
    protected List<Package> _package;
    @XmlElement(required = true)
    protected Index index;

    /**
     * Gets the value of the package property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the package property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackage().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Package }
     *
     *
     */
    public List<Package> getPackage() {
        if (_package == null) {
            _package = new ArrayList<Package>();
        }
        return this._package;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link Index }
     *     
     */
    public Index getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link Index }
     *     
     */
    public void setIndex(Index value) {
        this.index = value;
    }

}
