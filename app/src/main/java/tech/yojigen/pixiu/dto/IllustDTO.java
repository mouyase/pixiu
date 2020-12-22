package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

public class IllustDTO {
    @SerializedName("id")
    String id;
    @SerializedName("title")
    String title;
    @SerializedName("type")
    String type;
    @SerializedName("caption")
    String caption;
    @SerializedName("create_date")
    String createTime;
    @SerializedName("page_count")
    String pageCount;
    @SerializedName("width")
    int width;
    @SerializedName("height")
    int height;
    @SerializedName("user")
    UserDTO user;
    @SerializedName("image_urls")
    ImageUrls imageUrls;

    public class ImageUrls {
        @SerializedName("square_medium")
        String square;
        @SerializedName("medium")
        String medium;
        @SerializedName("large")
        String large;

        public String getSquare() {
            return square;
        }

        public void setSquare(String square) {
            this.square = square;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
