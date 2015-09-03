/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.ws.rest.response;

/**
 *
 * @author asingh8
 */
public class SSNAlbum {

    private long id;
    private String user_id;
    private String media_file_count;
    private String name;
    private String slug;
    private String description;
    private String is_active;
    private String created;
    private String modified;
    private String cover_photo;
    private String encrypted_name;
    private String visibility;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMedia_file_count() {
        return media_file_count;
    }

    public void setMedia_file_count(String media_file_count) {
        this.media_file_count = media_file_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getEncrypted_name() {
        return encrypted_name;
    }

    public void setEncrypted_name(String encrypted_name) {
        this.encrypted_name = encrypted_name;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    
}
