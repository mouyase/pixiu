package tech.yojigen.pixiu.dto;

import com.google.gson.Gson;

import java.util.List;

public class BundleIllustDTO {
    public static final int MODE_NORMAL = 0x1000;
    public static final int MODE_SINGLE = 0x2000;
    public static final int MODE_SEARCH = 0x3000;

    private int position;
    private String nextUrl;
    private int mode = MODE_NORMAL;
    private int nextTimes;
    private int searchIndex;
    private String searchKey;
    private List<IllustDTO> illustList;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getSearchIndex() {
        return searchIndex;
    }

    public void setSearchIndex(int searchIndex) {
        this.searchIndex = searchIndex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getNextTimes() {
        return nextTimes;
    }

    public void setNextTimes(int nextTimes) {
        this.nextTimes = nextTimes;
    }

    public List<IllustDTO> getIllustList() {
        return illustList;
    }

    public void setIllustList(List<IllustDTO> illustList) {
        this.illustList = illustList;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static BundleIllustDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, BundleIllustDTO.class);
    }
}
