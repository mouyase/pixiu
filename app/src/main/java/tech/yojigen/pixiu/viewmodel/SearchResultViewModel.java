package tech.yojigen.pixiu.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.IllustListDTO;
import tech.yojigen.pixiu.dto.RecommendDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

public class SearchResultViewModel extends ViewModel {
    private Gson gson = new Gson();
    private List<MutableLiveData<List<IllustDTO>>> illustListList = new ArrayList<>();
    private List<Integer> nextTimesList = new ArrayList<>();

    private List<Boolean> isLoadingList = new ArrayList<>();

    public SearchResultViewModel() {
        for (int i = 0; i < 6; i++) {
            MutableLiveData mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(new ArrayList());
            illustListList.add(i, mutableLiveData);
            nextTimesList.add(i, 0);
            isLoadingList.add(i, false);
            getData(i);
        }
    }

    public void getData(int index) {
        String url = Value.URL_API + "/v1/search/popular-preview/illust";
        if (!isLoadingList.get(index)) {
            isLoadingList.set(index, true);
//            String url = TextUtils.isEmpty(nextUrl) ? Value.URL_API + "/v1/search/popular-preview/illust" : nextUrl;
            PixivData pixivData = new PixivData.Builder()
                    .set("filter", "for_android")
                    .set("include_translated_tag_results", true)
                    .set("merge_plain_keyword_results", true)
                    .set("word", "バーチャルYouTuber")
                    .set("search_target", "exact_match_for_tags")
                    .build();
            PixivClient.getInstance().get(url, pixivData, new PixivCallback() {
                @Override
                public void onFailure() {
                    isLoadingList.set(index, false);
                }

                @Override
                public void onResponse(String body) {
                    IllustListDTO illustListDTO = gson.fromJson(body, IllustListDTO.class);
                    illustListList.get(index).getValue().addAll(illustListDTO.getIllustList());
                    illustListList.get(index).postValue(illustListList.get(index).getValue());
                    isLoadingList.set(index, false);
                }
            });
        }
    }

    public List<MutableLiveData<List<IllustDTO>>> getIllustListList() {
        return illustListList;
    }
}