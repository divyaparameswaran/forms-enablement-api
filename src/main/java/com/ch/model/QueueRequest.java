package com.ch.model;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
public class QueueRequest {

    private String formStatus;
    private long count;


    /**
     * Constructs an empty queue request.
     */
    public QueueRequest() {
    //Empty constructor is needed for automatic deserialisation
    }

    /**
     * Constructs an empty queue request.
     */
    public QueueRequest(String formStatus, long count) {
        this.formStatus = formStatus;
        this.count = count;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public long getCount() {
        return count;
    }
}