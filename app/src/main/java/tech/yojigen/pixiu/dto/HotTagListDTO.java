package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotTagListDTO {
    @SerializedName("trend_tags")
    List<TagDTO> tagList;

    public List<TagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDTO> tagList) {
        this.tagList = tagList;
    }
}
