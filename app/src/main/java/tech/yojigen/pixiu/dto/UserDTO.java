package tech.yojigen.pixiu.dto;

import android.text.TextUtils;

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
        private String px16;
        @SerializedName("px_50x50")
        private String px50;
        @SerializedName("px_170x170")
        private String px170;
        @SerializedName("medium")
        private String medium;

        public String getPx16() {
            return px16;
        }

        public void setPx16(String px16) {
            this.px16 = px16;
        }

        public String getPx50() {
            return px50;
        }

        public void setPx50(String px50) {
            this.px50 = px50;
        }

        public String getPx170() {
            return px170;
        }

        public void setPx170(String px170) {
            this.px170 = px170;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
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

    public String getHeadImage() {
        if (TextUtils.isEmpty(getImageUrls().getMedium())) {
            return getImageUrls().getPx170();
        }
        return getImageUrls().getMedium();
    }
}
