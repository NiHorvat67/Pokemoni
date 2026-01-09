package com.back.app.service;

public enum ImageFolder {
    ADVERTISEMENT("item_images"),
    ACCOUNT("profile_images");

    
    private final String folderName;
    
    ImageFolder(String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderName() {
        return folderName;
    }
}