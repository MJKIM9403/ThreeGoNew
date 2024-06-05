package com.io.threegonew.constant;

public enum FileType {
    USER("profile"),
    REVIEW_BOOK("bookcover"),
    REVIEW("images");

    private String path;

    FileType(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
