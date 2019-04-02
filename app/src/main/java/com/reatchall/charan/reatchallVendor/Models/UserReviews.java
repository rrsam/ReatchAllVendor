package com.reatchall.charan.reatchallVendor.Models;

public class UserReviews {
    private String userName;
    private String rating;
    private String review;
    private String profilePic;

    public UserReviews(String userName, String rating, String review, String profilePic) {
        this.userName = userName;
        this.rating = rating;
        this.review = review;
        this.profilePic = profilePic;
    }

    public UserReviews() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
