package com.example.career_map;

import java.io.Serializable;

public class EventModel implements Serializable {
    private String eventPoster, eventThumbnailPoster;
    private Long eventDateStamp;

    public EventModel() {
//        Firebase needs empty constructor
    }

    public EventModel(String eventPoster, String eventThumbnailPoster, Long eventDateStamp ) {
        this.eventPoster = eventPoster;
        this.eventThumbnailPoster = eventThumbnailPoster;
        this.eventDateStamp = eventDateStamp;

    }

    public String getEventPoster() {
        return eventPoster;
    }

    public String getEventThumbnailPoster() {
        return eventThumbnailPoster;
    }

    public Long getEventDateStamp() {
        return eventDateStamp;
    }

}