package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    @SerializedName("meta_single_page")
    private MetaSinglePage metaSinglePage;
    @SerializedName("meta_pages")
    private List<MetaPage> metaPages;
    @SerializedName("tags")
    private List<TagDTO> tags;
    @SerializedName("sanity_level")
    private int level;
    @SerializedName("total_view")
    private int totalView;
    @SerializedName("total_bookmarks")
    private int totalBookmarks;
    @SerializedName("is_muted")
    private boolean isMuted;

    public class MetaSinglePage {
        @SerializedName("original_image_url")
        String original;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }

    public class MetaPage {
        @SerializedName("image_urls")
        private ImageUrls imageUrls;

        public ImageUrls getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(ImageUrls imageUrls) {
            this.imageUrls = imageUrls;
        }
    }

    public class ImageUrls {
        @SerializedName("square_medium")
        private String square;
        @SerializedName("medium")
        private String medium;
        @SerializedName("large")
        private String large;
        @SerializedName("original")
        private String original;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public int getTotalBookmarks() {
        return totalBookmarks;
    }

    public void setTotalBookmarks(int totalBookmarks) {
        this.totalBookmarks = totalBookmarks;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public String getCaption() {
        return caption;
    }

    public List<MetaPage> getMetaPages() {
        return metaPages;
    }

    public void setMetaPages(List<MetaPage> metaPages) {
        this.metaPages = metaPages;
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

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
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

    public MetaSinglePage getMetaSinglePage() {
        return metaSinglePage;
    }

    public void setMetaSinglePage(MetaSinglePage metaSinglePage) {
        this.metaSinglePage = metaSinglePage;
    }


    public boolean isBookmarked() {
        return isBookmarked;
    }


    public boolean isSingle() {
        return this.pageCount <= 1;
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
    public List<String> getOriginalList() {
        List<String> urlList = new ArrayList<>();
        if (isSingle()) {
            urlList.add(getMetaSinglePage().getOriginal());
        } else {
            for (MetaPage metaPage : getMetaPages()) {
                urlList.add(metaPage.getImageUrls().getOriginal());
            }
        }
        return urlList;
    }

    public boolean isUgoira() {
        return type.equals("ugoira");
    }
}
