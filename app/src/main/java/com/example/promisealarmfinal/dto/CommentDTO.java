package com.example.promisealarmfinal.dto;

public class CommentDTO {
    private String id;
    private String name;
    private String comment;
    private String uploadDate;

    public CommentDTO() {

    }

    public CommentDTO(String id, String name, String comment, String uploadDate) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.uploadDate = uploadDate;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
