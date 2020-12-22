package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendDTO {
    @SerializedName("illusts")
    List<IllustDTO> illustList;
    @SerializedName("ranking_illusts")
    List<IllustDTO> rankingIllustList;
    @SerializedName("next_url")
    String nextUrl;

    public List<IllustDTO> getIllustList() {
        return illustList;
    }

    public void setIllustList(List<IllustDTO> illustList) {
        this.illustList = illustList;
    }

    public List<IllustDTO> getRankingIllustList() {
        return rankingIllustList;
    }

    public void setRankingIllustList(List<IllustDTO> rankingIllustList) {
        this.rankingIllustList = rankingIllustList;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
