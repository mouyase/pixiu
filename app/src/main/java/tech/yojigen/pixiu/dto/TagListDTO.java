package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagListDTO {
    @SerializedName("tags")
    private List<TagDTO> tagList;

    public List<TagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDTO> tagList) {
        this.tagList = tagList;
    }
}
