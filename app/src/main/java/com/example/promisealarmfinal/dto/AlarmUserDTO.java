package com.example.promisealarmfinal.dto;

public class AlarmUserDTO {
    private String id;
    private String name;
    private boolean off;
    private boolean online;

    public AlarmUserDTO() {

    }

    public AlarmUserDTO(String id, String name) {
        this.id = id;
        this.name = name;
        this.off = false;
        this.online = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
