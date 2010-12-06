//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.05 at 08:21:42 PM MEZ 
//


package org.simpleviewergallerydownloader.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}image" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="backgroundImagePath" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="enableRightClickOpen" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="frameColor" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="frameWidth" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="imagePath" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="maxImageHeight" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="maxImageWidth" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="navPosition" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="stagePadding" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="textColor" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="thumbPath" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="thumbnailColumns" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="thumbnailRows" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "image"
})
@XmlRootElement(name = "simpleviewerGallery")
public class SimpleviewerGallery {

    @XmlElement(required = true)
    protected List<Image> image;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String backgroundImagePath;
    @XmlAttribute(required = true)
    protected boolean enableRightClickOpen;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String frameColor;
    @XmlAttribute(required = true)
    protected BigInteger frameWidth;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String imagePath;
    @XmlAttribute(required = true)
    protected BigInteger maxImageHeight;
    @XmlAttribute(required = true)
    protected BigInteger maxImageWidth;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String navPosition;
    @XmlAttribute(required = true)
    protected BigInteger stagePadding;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String textColor;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String thumbPath;
    @XmlAttribute(required = true)
    protected BigInteger thumbnailColumns;
    @XmlAttribute(required = true)
    protected BigInteger thumbnailRows;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String title;

    /**
     * Gets the value of the image property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the image property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Image }
     * 
     * 
     */
    public List<Image> getImage() {
        if (image == null) {
            image = new ArrayList<Image>();
        }
        return this.image;
    }

    /**
     * Gets the value of the backgroundImagePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    /**
     * Sets the value of the backgroundImagePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackgroundImagePath(String value) {
        this.backgroundImagePath = value;
    }

    /**
     * Gets the value of the enableRightClickOpen property.
     * 
     */
    public boolean isEnableRightClickOpen() {
        return enableRightClickOpen;
    }

    /**
     * Sets the value of the enableRightClickOpen property.
     * 
     */
    public void setEnableRightClickOpen(boolean value) {
        this.enableRightClickOpen = value;
    }

    /**
     * Gets the value of the frameColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrameColor() {
        return frameColor;
    }

    /**
     * Sets the value of the frameColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrameColor(String value) {
        this.frameColor = value;
    }

    /**
     * Gets the value of the frameWidth property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFrameWidth() {
        return frameWidth;
    }

    /**
     * Sets the value of the frameWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFrameWidth(BigInteger value) {
        this.frameWidth = value;
    }

    /**
     * Gets the value of the imagePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the value of the imagePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagePath(String value) {
        this.imagePath = value;
    }

    /**
     * Gets the value of the maxImageHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxImageHeight() {
        return maxImageHeight;
    }

    /**
     * Sets the value of the maxImageHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxImageHeight(BigInteger value) {
        this.maxImageHeight = value;
    }

    /**
     * Gets the value of the maxImageWidth property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxImageWidth() {
        return maxImageWidth;
    }

    /**
     * Sets the value of the maxImageWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxImageWidth(BigInteger value) {
        this.maxImageWidth = value;
    }

    /**
     * Gets the value of the navPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavPosition() {
        return navPosition;
    }

    /**
     * Sets the value of the navPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavPosition(String value) {
        this.navPosition = value;
    }

    /**
     * Gets the value of the stagePadding property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStagePadding() {
        return stagePadding;
    }

    /**
     * Sets the value of the stagePadding property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStagePadding(BigInteger value) {
        this.stagePadding = value;
    }

    /**
     * Gets the value of the textColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Sets the value of the textColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextColor(String value) {
        this.textColor = value;
    }

    /**
     * Gets the value of the thumbPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThumbPath() {
        return thumbPath;
    }

    /**
     * Sets the value of the thumbPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThumbPath(String value) {
        this.thumbPath = value;
    }

    /**
     * Gets the value of the thumbnailColumns property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getThumbnailColumns() {
        return thumbnailColumns;
    }

    /**
     * Sets the value of the thumbnailColumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setThumbnailColumns(BigInteger value) {
        this.thumbnailColumns = value;
    }

    /**
     * Gets the value of the thumbnailRows property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getThumbnailRows() {
        return thumbnailRows;
    }

    /**
     * Sets the value of the thumbnailRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setThumbnailRows(BigInteger value) {
        this.thumbnailRows = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

}
