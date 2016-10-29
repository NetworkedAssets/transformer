package com.networkedassets.condoc.jsonDoclet.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for method complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="method"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tag" type="{}tagInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="parameter" type="{}methodParameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="return" type="{}typeInfo" minOccurs="0"/&gt;
 *         &lt;element name="exception" type="{}typeInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="annotation" type="{}annotationInstance" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="qualified" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="scope" type="{}scope" /&gt;
 *       &lt;attribute name="abstract" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="final" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="included" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="native" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="synchronized" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="static" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="varArgs" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "method", propOrder = {
    "comment",
    "tag",
    "parameter",
    "_return",
    "exception",
    "annotation",
    "modifier"
})
public class Method {

    protected String comment;
    protected List<TagInfo> tag;
    protected List<MethodParameter> parameter;
    @XmlElement(name = "return")
    protected TypeInfo _return;
    protected List<TypeInfo> exception;
    protected List<AnnotationInstance> annotation;
    protected List<String> modifier;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "signature")
    protected String signature;
    @XmlAttribute(name = "qualified")
    protected String qualified;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "abstract")
    protected Boolean _abstract;
    @XmlAttribute(name = "final")
    protected Boolean _final;
    @XmlAttribute(name = "included")
    protected Boolean included;
    @XmlAttribute(name = "native")
    protected Boolean _native;
    @XmlAttribute(name = "synchronized")
    protected Boolean _synchronized;
    @XmlAttribute(name = "static")
    protected Boolean _static;
    @XmlAttribute(name = "varArgs")
    protected Boolean varArgs;

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
     * Gets the value of the parameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MethodParameter }
     *
     *
     */
    public List<MethodParameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<MethodParameter>();
        }
        return this.parameter;
    }

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link TypeInfo }
     *
     */
    public TypeInfo getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link TypeInfo }
     *
     */
    public void setReturn(TypeInfo value) {
        this._return = value;
    }

    /**
     * Gets the value of the exception property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exception property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getException().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInfo }
     *
     *
     */
    public List<TypeInfo> getException() {
        if (exception == null) {
            exception = new ArrayList<TypeInfo>();
        }
        return this.exception;
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
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
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
     * Gets the value of the native property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isNative() {
        if (_native == null) {
            return false;
        } else {
            return _native;
        }
    }

    /**
     * Sets the value of the native property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNative(Boolean value) {
        this._native = value;
    }

    /**
     * Gets the value of the synchronized property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSynchronized() {
        if (_synchronized == null) {
            return false;
        } else {
            return _synchronized;
        }
    }

    /**
     * Sets the value of the synchronized property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSynchronized(Boolean value) {
        this._synchronized = value;
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
     * Gets the value of the varArgs property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isVarArgs() {
        if (varArgs == null) {
            return false;
        } else {
            return varArgs;
        }
    }

    /**
     * Sets the value of the varArgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVarArgs(Boolean value) {
        this.varArgs = value;
    }

}
