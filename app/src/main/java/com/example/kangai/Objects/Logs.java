package com.example.kangai.Objects;

import androidx.annotation.NonNull;

public class Logs {
    Long timestamp;
    String log;

    public Logs(Long timestamp, String log) {
        this.timestamp = timestamp;
        this.log = log;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLog() {
        return this.log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @NonNull
    @Override
    public String toString() {
        return this.timestamp+" - "+this.log;
    }
}
