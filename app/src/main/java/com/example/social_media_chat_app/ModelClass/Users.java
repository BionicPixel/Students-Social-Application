package com.example.social_media_chat_app.ModelClass;

public class Users {

    public String uid, name, email, imageUri, status, branch, year;

    public Users() {
    }

    public String getUid() {
        return uid;
    }

    public Users(String uid, String name, String email, String imageUri, String status, String branch, String year) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status=status;
        this.branch = branch;
        this.year = year;
        System.out.println(toString());
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Users{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", status='" + status + '\'' +
                ", branch='" + branch + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
