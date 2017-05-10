package com.ajibadedah.backingapp.model;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ajibade on 5/4/17
 */

public class Step {
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step() {
        //empty constructor
    }

    public Step(String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public static class StepTypeAdapter implements JsonDeserializer<Step> {

        @Override
        public Step deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Step step = new Step();
            JsonObject stepJson = json.getAsJsonObject();

            step.setDescription(stepJson.get("description").getAsString());
            step.setShortDescription(stepJson.get("shortDescription").getAsString());
            step.setVideoURL(stepJson.get("videoURL").getAsString());
            step.setThumbnailURL(stepJson.get("thumbnailURL").getAsString());
            return step;
        }
    }
}
