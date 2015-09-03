

package com.ssn.ui.custom.component;

import com.ssn.ui.form.SSNHomeForm;

/**
 *
 * @author pkumar2
 */public class SSNGalleryMetaData {
    
    
    private String mediaType="";
    private String mediaName="";
    private String mediaLocation="";
    private String editMediaLocation="";
    private String mediaFileLocation="";
    private String size="";
    private String sizeOnDisk="";
    private String created="";
    private String modiFied="";
    private String title="";
    private String userComments="";
    private String ssnRatings="";
    private Integer noOfFiles;
    private Integer noOfFolders;
    private String latitude="";
    private String longitude="";
    private String SsnKeywords="";
    private String faceTags="";
    private String voiceNotePath="";
    private String mediaFileName="";
    private String photoGrapher="";
    private String caption = "";
    private String address = "";
    
    private SSNHomeForm homeForm;
   
    

    public SSNGalleryMetaData() {
    }
    
    
    
    
   public String getMediaFileLocation() {
        return mediaFileLocation;
    }

    public void setMediaFileLocation(String mediaFileLocation) {
        this.mediaFileLocation = mediaFileLocation;
    }

   
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaLocation() {
        return mediaLocation;
    }

    public void setMediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeOnDisk() {
        return sizeOnDisk;
    }

    public void setSizeOnDisk(String sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the modiFied
     */
    public String getModiFied() {
        return modiFied;
    }

    /**
     * @param modiFied the modiFied to set
     */
    public void setModiFied(String modiFied) {
        this.modiFied = modiFied;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the userComments
     */
    public String getUserComments() {
        return userComments;
    }

    /**
     * @param userComments the userComments to set
     */
    public void setUserComments(String userComments) {
        if(userComments!=null)
            this.userComments = userComments;
        else{
            this.userComments = "";
        }
    }

    /**
     * @return the ssnRatings
     */
    public String getSsnRatings() {
        return ssnRatings;
    }

    /**
     * @param ssnRatings the ssnRatings to set
     */
    public void setSsnRatings(String ssnRatings) {
        this.ssnRatings = ssnRatings;
    }

    /**
     * @return the noOfFiles
     */
    public Integer getNoOfFiles() {
        return noOfFiles;
    }

    /**
     * @param noOfFiles the noOfFiles to set
     */
    public void setNoOfFiles(Integer noOfFiles) {
        this.noOfFiles = noOfFiles;
    }

    /**
     * @return the noOfFolders
     */
    public Integer getNoOfFolders() {
        return noOfFolders;
    }

    /**
     * @param noOfFolders the noOfFolders to set
     */
    public void setNoOfFolders(Integer noOfFolders) {
        this.noOfFolders = noOfFolders;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the SsnKeywords
     */
    public String getSsnKeywords() {
        return SsnKeywords;
    }

    /**
     * @param SsnKeywords the SsnKeywords to set
     */
    public void setSsnKeywords(String SsnKeywords) {
        this.SsnKeywords = SsnKeywords;
    }

    public String getVoiceNotePath() {
        return voiceNotePath;
    }

    public void setVoiceNotePath(String voiceNotePath) {
        this.voiceNotePath = voiceNotePath;
    }

    /**
     * @return the homeForm
     */
    public SSNHomeForm getHomeForm() {
        return homeForm;
    }

    /**
     * @param homeForm the homeForm to set
     */
    public void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    /**
     * @return the mediaName
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * @param mediaName the mediaName to set
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * @return the mediaFileName
     */
    public String getMediaFileName() {
        return mediaFileName;
    }

    /**
     * @param mediaFileName the mediaFileName to set
     */
    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    /**
     * @return the photoGrapher
     */
    public String getPhotoGrapher() {
        return photoGrapher;
    }

    /**
     * @param photoGrapher the photoGrapher to set
     */
    public void setPhotoGrapher(String photoGrapher) {
        this.photoGrapher = photoGrapher;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return the editMediaLocation
     */
    public String getEditMediaLocation() {
        return editMediaLocation;
    }

    /**
     * @param editMediaLocation the editMediaLocation to set
     */
    public void setEditMediaLocation(String editMediaLocation) {
        this.editMediaLocation = editMediaLocation;
    }

    /**
     * @return the faceTags
     */
    public String getFaceTags() {
        return faceTags;
    }

    /**
     * @param faceTags the faceTags to set
     */
    public void setFaceTags(String faceTags) {
        this.faceTags = faceTags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    
    
}
