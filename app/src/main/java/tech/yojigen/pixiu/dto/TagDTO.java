package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

public class TagDTO {
    @SerializedName("tag")
    private String tag;
    @SerializedName("name")
    private String name;
    @SerializedName("translated_name")
    private String translatedName;
    @SerializedName("illust")
    private IllustDTO illust;

    public boolean isNoTranslate() {
        return this.translatedName == null;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(String translatedName) {
        this.translatedName = translatedName;
    }

    public IllustDTO getIllust() {
        return illust;
    }

    public void setIllust(IllustDTO illust) {
        this.illust = illust;
    }
}
