package com.networkedassets.condoc.jsonDoclet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for package complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="package"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tag" type="{}tagInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="annotation" type="{}annotation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="enum" type="{}enum" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="interface" type="{}interface" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="class" type="{}class" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "package", propOrder = {
    "comment",
    "tag",
    "annotation",
    "_enum",
    "_interface",
    "clazz"
})
public class Package {

    protected String comment;
    protected List<TagInfo> tag;
    protected List<Annotation> annotation;
    @XmlElement(name = "enum")
    protected List<Enum> _enum;
    @XmlElement(name = "interface")
    protected List<Interface> _interface;
    @XmlElement(name = "class")
    protected List<Class> clazz;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the comment property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the tag property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tag property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTag().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TagInfo }
     *
     *
     */
    public List<TagInfo> getTag() {
        if (tag == null) {
            tag = new ArrayList<TagInfo>();
        }
        return this.tag;
    }

    /**
     * Gets the value of the annotation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Annotation }
     *
     *
     */
    public List<Annotation> getAnnotation() {
        if (annotation == null) {
            annotation = new ArrayList<Annotation>();
        }
        return this.annotation;
    }

    /**
     * Gets the value of the enum property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enum property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnum().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Enum }
     *
     *
     */
    public List<Enum> getEnum() {
        if (_enum == null) {
            _enum = new ArrayList<Enum>();
        }
        return this._enum;
    }

    /**
     * Gets the value of the interface property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interface property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterface().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Interface }
     *
     *
     */
    public List<Interface> getInterface() {
        if (_interface == null) {
            _interface = new ArrayList<Interface>();
        }
        return this._interface;
    }

    /**
     * Gets the value of the clazz property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Class }
     *
     *
     */
    @JsonProperty("class")
    public List<Class> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<>();
        }
        return this.clazz;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
