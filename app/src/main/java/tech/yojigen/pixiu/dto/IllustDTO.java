package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

public class IllustDTO {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("caption")
    private String caption;
    @SerializedName("create_date")
    private String createTime;
    @SerializedName("page_count")
    private int pageCount;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("user")
    private UserDTO user;
    @SerializedName("image_urls")
    private ImageUrls imageUrls;
    @SerializedName("is_bookmarked")
    private boolean isBookmarked;

    public class ImageUrls {
        @SerializedName("square_medium")
        private String square;
        @SerializedName("medium")
        private String medium;
        @SerializedName("large")
        private String large;

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

    public boolean isBookmarked() {
        return isBookmarked;
    }


    public boolean isSingle() {
        if (this.pageCount > 1) {
            return false;
        }
        return true;
    }


    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
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
