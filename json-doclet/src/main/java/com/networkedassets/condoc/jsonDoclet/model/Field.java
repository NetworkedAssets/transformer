package com.networkedassets.condoc.jsonDoclet.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for field complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="field"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{}typeInfo" minOccurs="0"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tag" type="{}tagInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="constant" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="annotation" type="{}annotationInstance" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="qualified" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="scope" type="{}scope" /&gt;
 *       &lt;attribute name="volatile" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="transient" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="static" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="final" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field", propOrder = {
    "type",
    "comment",
    "tag",
    "constant",
    "annotation",
    "modifier"
})
public class Field {

    protected TypeInfo type;
    protected String comment;
    protected List<TagInfo> tag;
    protected String constant;
    protected List<AnnotationInstance> annotation;
    protected List<String> modifier;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "qualified")
    protected String qualified;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "volatile")
    protected Boolean _volatile;
    @XmlAttribute(name = "transient")
    protected Boolean _transient;
    @XmlAttribute(name = "static")
    protected Boolean _static;
    @XmlAttribute(name = "final")
    protected Boolean _final;

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link TypeInfo }
     *
     */
    public TypeInfo getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link TypeInfo }
     *
     */
    public void setType(TypeInfo value) {
        this.type = value;
    }

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
     * Gets the value of the constant property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConstant() {
        return constant;
    }

    /**
     * Sets the value of the constant property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConstant(String value) {
        this.constant = value;
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
     * {@link AnnotationInstance }
     *
     *
     */
    public List<AnnotationInstance> getAnnotation() {
        if (annotation == null) {
            annotation = new ArrayList<AnnotationInstance>();
        }
        return this.annotation;
    }

    /**
     * Gets the value of the modifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getModifier() {
        if (modifier == null) {
            modifier = new ArrayList<String>();
        }
        return this.modifier;
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
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

    /**
     * Gets the value of the volatile property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isVolatile() {
        if (_volatile == null) {
            return false;
        } else {
            return _volatile;
        }
    }

    /**
     * Sets the value of the volatile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVolatile(Boolean value) {
        this._volatile = value;
    }

    /**
     * Gets the value of the transient property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTransient() {
        if (_transient == null) {
            return false;
        } else {
            return _transient;
        }
    }

    /**
     * Sets the value of the transient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransient(Boolean value) {
        this._transient = value;
    }

    /**
     * Gets the value of the static property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStatic() {
        if (_static == null) {
            return false;
        } else {
            return _static;
        }
    }

    /**
     * Sets the value of the static property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStatic(Boolean value) {
        this._static = value;
    }

    /**
     * Gets the value of the final property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFinal() {
        if (_final == null) {
            return false;
        } else {
            return _final;
        }
    }

    /**
     * Sets the value of the final property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFinal(Boolean value) {
        this._final = value;
    }

}
