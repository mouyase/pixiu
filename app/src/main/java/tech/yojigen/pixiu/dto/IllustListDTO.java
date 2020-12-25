package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IllustListDTO {
    @SerializedName("illusts")
    private List<IllustDTO> illustList;
    @SerializedName("next_url")
    private String nextUrl;

    public List<IllustDTO> getIllustList() {
        return illustList;
    }

    public void setIllustList(List<IllustDTO> illustList) {
        this.illustList = illustList;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
