/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.custom.component;

import org.springframework.social.facebook.api.Album;

/**
 *
 * @author asingh8
 */
public class SSNAlbumNode {
    private Album album;
    
    public SSNAlbumNode(Album album) {
        this.album = album;
    }
    
    @Override
    public String toString() {
        String albumName = null;
        
        if(this.album != null) {
            albumName = this.album.getName();
        }
        
        return albumName;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    
    
}
