package tech.yojigen.pixiu.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.HotTagListDTO;
import tech.yojigen.pixiu.dto.TagDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

public class SearchViewModel extends ViewModel {
    private Gson gson = new Gson();
    private MutableLiveData<List<TagDTO>> hotTagList = new MutableLiveData<>();
    private MutableLiveData<String[]> autoCompliteArray = new MutableLiveData<>();

    public SearchViewModel() {
        hotTagList.setValue(new ArrayList<>());
        getHotTags();
    }

//    public void getAutoComplete(String keyword) {
//        PixivData pixivData = new PixivData.Builder()
//                .set("merge_plain_keyword_results", true)
//                .set("word", keyword)
//                .build();
//        PixivClient.getInstance().get(Value.URL_API + "/v2/search/autocomplete", pixivData, new PixivCallback() {
//            @Override
//            public void onFailure() {
//
//            }
//
//            @Override
//            public void onResponse(String body) {
//                TagListDTO tagListDTO = gson.fromJson(body, TagListDTO.class);
//                List<String> tagList = new ArrayList<>();
//                for (TagDTO tagDTO : tagListDTO.getTagList()) {
//                    tagList.add(tagDTO.getName());
//                }
//                autoCompliteArray.postValue(tagList.toArray(new String[]{}));
//            }
//        });
//    }

    public void getHotTags() {
        PixivData pixivData = new PixivData.Builder().set("filter", "for_android").build();
        PixivClient.getInstance().get(Value.URL_API + "/v1/trending-tags/illust", pixivData, new PixivCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(String body) {
                HotTagListDTO hotTagListDTO = gson.fromJson(body, HotTagListDTO.class);
                hotTagList.getValue().addAll(hotTagListDTO.getTagList());
                hotTagList.postValue(hotTagList.getValue());
            }
        });
    }

    public MutableLiveData<List<TagDTO>> getHotTagList() {
        return hotTagList;
    }

    public MutableLiveData<String[]> getAutoCompliteArray() {
        return autoCompliteArray;
    }
}
