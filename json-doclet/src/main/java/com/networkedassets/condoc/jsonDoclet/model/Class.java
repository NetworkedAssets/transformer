package com.networkedassets.condoc.jsonDoclet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for class complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="class"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tag" type="{}tagInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="generic" type="{}typeParameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="class" type="{}typeInfo" minOccurs="0"/&gt;
 *         &lt;element name="interface" type="{}typeInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="constructor" type="{}constructor" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="method" type="{}method" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="annotation" type="{}annotationInstance" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="field" type="{}field" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="qualified" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="scope" type="{}scope" /&gt;
 *       &lt;attribute name="abstract" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="error" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="exception" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="externalizable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="included" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="serializable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="class" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "class", propOrder = {
    "comment",
    "tag",
    "generic",
    "clazz",
    "_interface",
    "constructor",
    "method",
    "annotation",
    "field",
    "modifier"
})
public class Class {

    protected String comment;
    protected List<TagInfo> tag;
    protected List<TypeParameter> generic;
    @XmlElement(name = "class")
    protected TypeInfo clazz;
    @XmlElement(name = "interface")
    protected List<TypeInfo> _interface;
    protected List<Constructor> constructor;
    protected List<Method> method;
    protected List<AnnotationInstance> annotation;
    protected List<Field> field;
    protected List<String> modifier;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "qualified")
    protected String qualified;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "abstract")
    protected Boolean _abstract;
    @XmlAttribute(name = "error")
    protected Boolean error;
    @XmlAttribute(name = "exception")
    protected Boolean exception;
    @XmlAttribute(name = "externalizable")
    protected Boolean externalizable;
    @XmlAttribute(name = "included")
    protected Boolean included;
    @XmlAttribute(name = "serializable")
    protected Boolean serializable;
    @XmlAttribute(name = "type")
    protected String type;
    private List<String> innerClasses;

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
     * {@link TypeParameter }
     * 
     * 
     */
    public List<TypeParameter> getGeneric() {
        if (generic == null) {
            generic = new ArrayList<TypeParameter>();
        }
        return this.generic;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link TypeInfo }
     *     
     */
    @JsonProperty("class")
    public TypeInfo getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeInfo }
     *     
     */
    @JsonProperty("class")
    public void setClazz(TypeInfo value) {
        this.clazz = value;
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
     * {@link TypeInfo }
     * 
     * 
     */
    public List<TypeInfo> getInterface() {
        if (_interface == null) {
            _interface = new ArrayList<TypeInfo>();
        }
        return this._interface;
    }

    /**
     * Gets the value of the constructor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the constructor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConstructor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Constructor }
     * 
     * 
     */
    public List<Constructor> getConstructor() {
        if (constructor == null) {
            constructor = new ArrayList<Constructor>();
        }
        return this.constructor;
    }

    /**
     * Gets the value of the method property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the method property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Method }
     * 
     * 
     */
    public List<Method> getMethod() {
        if (method == null) {
            method = new ArrayList<Method>();
        }
        return this.method;
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
     * Gets the value of the field property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Field }
     * 
     * 
     */
    public List<Field> getField() {
        if (field == null) {
            field = new ArrayList<Field>();
        }
        return this.field;
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
     * Gets the value of the abstract property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAbstract() {
        if (_abstract == null) {
            return false;
        } else {
            return _abstract;
        }
    }

    /**
     * Sets the value of the abstract property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAbstract(Boolean value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isError() {
        if (error == null) {
            return false;
        } else {
            return error;
        }
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setError(Boolean value) {
        this.error = value;
    }

    /**
     * Gets the value of the exception property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isException() {
        if (exception == null) {
            return false;
        } else {
            return exception;
        }
    }

    /**
     * Sets the value of the exception property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setException(Boolean value) {
        this.exception = value;
    }

    /**
     * Gets the value of the externalizable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isExternalizable() {
        if (externalizable == null) {
            return false;
        } else {
            return externalizable;
        }
    }

    /**
     * Sets the value of the externalizable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExternalizable(Boolean value) {
        this.externalizable = value;
    }

    /**
     * Gets the value of the included property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIncluded() {
        if (included == null) {
            return true;
        } else {
            return included;
        }
    }

    /**
     * Sets the value of the included property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncluded(Boolean value) {
        this.included = value;
    }

    /**
     * Gets the value of the serializable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSerializable() {
        if (serializable == null) {
            return false;
        } else {
            return serializable;
        }
    }

    /**
     * Sets the value of the serializable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSerializable(Boolean value) {
        this.serializable = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "class";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    public List<String> getInnerClasses() {
        if (innerClasses == null) {
            innerClasses = new ArrayList<>();
        }
        return innerClasses;
    }
}
