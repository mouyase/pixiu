package tech.yojigen.pixiu.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.RecommendDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

public class MainViewModel extends ViewModel {
    private Gson gson = new Gson();
    private MutableLiveData<List<IllustDTO>> recommendList = new MutableLiveData();
    private Set recommendSet = new HashSet<>();
    private String recommendNextUrl;
    private MutableLiveData<List<IllustDTO>> followedList = new MutableLiveData();
    private Map followedMap = new HashMap<>();
    private String followedNextUrl;

    private MutableLiveData<String> coverImageUrl = new MutableLiveData();

    Boolean isRecommendLoading = false;

    public MainViewModel() {
        recommendList.setValue(new ArrayList());
        followedList.setValue(new ArrayList());
    }


    public void getRecommendData() {
        if (!isRecommendLoading) {
            isRecommendLoading = true;
            String url = TextUtils.isEmpty(recommendNextUrl) ? Value.URL_API + "/v1/illust/recommended" : recommendNextUrl;
            PixivData pixivData = TextUtils.isEmpty(recommendNextUrl) ? new PixivData.Builder()
                    .set("filter", "for_android")
                    .set("include_ranking_illusts", true)
                    .set("include_privacy_policy", true)
                    .build() : null;
            PixivClient.getInstance().get(url, pixivData, new PixivCallback() {
                @Override
                public void onFailure() {
                    isRecommendLoading = false;
                }

                @Override
                public void onResponse(String body) {
                    RecommendDTO recommendDTO = gson.fromJson(body, RecommendDTO.class);
                    if (recommendDTO.getRankingIllustList().size() > 0) {
                        coverImageUrl.postValue(recommendDTO.getRankingIllustList().get(0).getImageUrls().getMedium());
                    }
                    List<IllustDTO> newList = new ArrayList<>();
                    for (IllustDTO illustDTO : recommendDTO.getIllustList()) {
                        if (recommendSet.contains(illustDTO.getId())) {
                            continue;
                        } else {
                            recommendSet.add(illustDTO.getId());
                            newList.add(illustDTO);
                        }
                    }
                    recommendNextUrl = recommendDTO.getNextUrl();
                    recommendList.getValue().addAll(newList);
                    recommendList.postValue(recommendList.getValue());
                    isRecommendLoading = false;
                }
            });
        }
    }

    public void refreshRecommendData() {
        recommendNextUrl = null;
        recommendSet = new HashSet();
        getRecommendData();
    }

    public MutableLiveData<String> getCoverImageUrl() {
        return coverImageUrl;
    }

    public MutableLiveData<List<IllustDTO>> getRecommendList() {
        return recommendList;
    }
}
