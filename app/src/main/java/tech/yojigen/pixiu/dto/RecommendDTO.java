package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendDTO extends IllustListDTO {
    @SerializedName("ranking_illusts")
    List<IllustDTO> rankingIllustList;

    public List<IllustDTO> getRankingIllustList() {
        return rankingIllustList;
    }

    public void setRankingIllustList(List<IllustDTO> rankingIllustList) {
        this.rankingIllustList = rankingIllustList;
    }
}
