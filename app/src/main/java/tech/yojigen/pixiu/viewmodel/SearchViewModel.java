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

    public SearchViewModel() {
        hotTagList.setValue(new ArrayList<>());
        getHotTags();
    }

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
}
