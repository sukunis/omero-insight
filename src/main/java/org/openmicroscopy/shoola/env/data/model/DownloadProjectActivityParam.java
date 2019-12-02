package org.openmicroscopy.shoola.env.data.model;

import java.io.File;
import java.util.List;

import javax.swing.Icon;


import omero.gateway.model.ImageData;

public class DownloadProjectActivityParam {
	/** The icon associated to the parameters. */
    private Icon icon;
    
    /** The file to download the content into. */
    private File location;
    
    /** The collection of archived images to download. */
    private List<omero.gateway.model.DataObject> images;
    
    /** Flag indicating to override or not the files when saving.*/
    private boolean override;

    /** Flag for zipping the downloaded images */
    private boolean zip = false;
    
    /** Flag for preserving the original folder structure */
    private boolean keepOriginalPaths = true;
    

    private int numImages;
    
    
    /**
     * Creates a new instance.
     * 
     * @param location The file to download the content into.
     * @param images The archived images to download.
     * @param icon The icon associated to the parameters.
     */
    public DownloadProjectActivityParam(File location, List<omero.gateway.model.DataObject> images,int numImages,
    		Icon icon)
    {
    	this.location = location;
    	this.images = images;
    	this.icon = icon;
    	this.override = false;
    	this.numImages=numImages;

    }

    /**
     * Sets to <code>true</code> to override the files when saving,
     * <code>false</code> otherwise. Default is <code>false</code>.
     *
     * @param override The value to set.
     */
    public void setOverride(boolean override) { this.override = override; }

    /**
     * Returns <code>true</code> to override the files when saving,
     * <code>false</code> otherwise. Default is <code>false</code>.
     *
     * @return See above.
     */
    public boolean isOverride() { return override; }

    /**
     * Returns the icon.
     * 
     * @return See above.
     */
    public Icon getIcon() { return icon; }
    
    /**
     * Returns the path to the folder where to download the archived files.
     * 
     * @return See above.
     */
    public File getLocation() { return location; }
    
    /**
     * Returns the archived image to download.
     * 
     * @return See above.
     */
    public List<omero.gateway.model.DataObject> getImages() { return images; }

    /**
     * Returns if the downloaded images should be zipped
     * 
     * @return See above
     */
    public boolean isZip() {
        return zip;
    }

    /**
     * Sets the zip flag
     * 
     * @param zip
     *            Pass <code>true</code> if the downloaded images should be
     *            zipped
     */
    public void setZip(boolean zip) {
        this.zip = zip;
    }

    /**
     * Returns if the original folder structure should be preserved
     * 
     * @return See above
     */
    public boolean isKeepOriginalPaths() {
        return keepOriginalPaths;
    }

    /**
     * Sets the keepOriginalPaths flag
     * 
     * @param keepOriginalPaths
     *            Pass <code>true</code> to preserve the original folder
     *            structure
     */
    public void setKeepOriginalPaths(boolean keepOriginalPaths) {
        this.keepOriginalPaths = keepOriginalPaths;
    }
    
  
    public int getNumImages(){return numImages;}
    
    		
}