package com.ajibadedah.backingapp.model;

import java.net.URL;

/**
 * Created by ajibade on 5/4/17
 */

public class Step {
    private String shortDescription;
    private String description;
    private URL videoURL;
    private URL thumbnailURL;

    public Step(String shortDescription, String description, URL videoURL, URL thumbnailURL) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public URL getVideoURL() {
        return videoURL;
    }

    public URL getThumbnailURL() {
        return thumbnailURL;
    }
}
