package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    private String id;
    private String name;
    private String account;
    private String mail_address;
    @SerializedName("profile_image_urls")
    private ImageUrls imageUrls;

    public class ImageUrls {
        @SerializedName("px_16x16")
        private String small;
        @SerializedName("px_50x50")
        private String middle;
        @SerializedName("px_170x170")
        private String large;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMiddle() {
            return middle;
        }

        public void setMiddle(String middle) {
            this.middle = middle;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMail_address() {
        return mail_address;
    }

    public void setMail_address(String mail_address) {
        this.mail_address = mail_address;
    }

    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }
}
