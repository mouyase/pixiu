package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    int pageCount;
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
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

    public boolean isSingle() {
        if (this.pageCount > 1) {
            return false;
        }
        return true;
    }

//    public List<String> getLargeList() {
//        List<String> urlList = new ArrayList<>();
//        if (isSingle()) {
//
//        } else {
//
//        }
//        return urlList;
//    }
//
//    public List<String> getOriginalList() {
//        List<String> urlList = new ArrayList<>();
//        return urlList;
//    }
}
