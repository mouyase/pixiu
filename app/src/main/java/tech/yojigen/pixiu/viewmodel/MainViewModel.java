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
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.IllustListDTO;
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
    private Set followedSet = new HashSet<>();
    private String followedNextUrl;

    private MutableLiveData<String> coverImageUrl = new MutableLiveData();

    private Boolean isRecommendLoading = false;
    private Boolean isFollowedLoading = false;

    public MainViewModel() {
        recommendList.setValue(new ArrayList());
        followedList.setValue(new ArrayList());
        refreshRecommendData();
        refreshFollowedData();
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

    public MutableLiveData<List<IllustDTO>> getRecommendList() {
        return recommendList;
    }

    public String getRecommendBundle(int position) {
        BundleIllustDTO bundleIllustDTO = new BundleIllustDTO();
        bundleIllustDTO.setPosition(position);
        bundleIllustDTO.setNextUrl(recommendNextUrl);
        bundleIllustDTO.setIllustList(getRecommendList().getValue());
        bundleIllustDTO.setMode(BundleIllustDTO.MODE_NORMAL);
        return bundleIllustDTO.toJson();
    }

    public String getFollowBundle(int position) {
        BundleIllustDTO bundleIllustDTO = new BundleIllustDTO();
        bundleIllustDTO.setPosition(position);
        bundleIllustDTO.setNextUrl(followedNextUrl);
        bundleIllustDTO.setIllustList(getFollowedList().getValue());
        bundleIllustDTO.setMode(BundleIllustDTO.MODE_NORMAL);
        return bundleIllustDTO.toJson();
    }

    public void getFollowedData() {
        if (!isFollowedLoading) {
            isFollowedLoading = true;
            String url = TextUtils.isEmpty(followedNextUrl) ? Value.URL_API + "/v2/illust/follow" : followedNextUrl;
            PixivData pixivData = TextUtils.isEmpty(followedNextUrl) ? new PixivData.Builder()
                    .set("restrict", "all")
                    .set("content_type", "illust")
                    .build() : null;
            PixivClient.getInstance().get(url, pixivData, new PixivCallback() {
                @Override
                public void onFailure() {
                    isFollowedLoading = false;
                }

                @Override
                public void onResponse(String body) {
                    IllustListDTO illustListDTO = gson.fromJson(body, IllustListDTO.class);
                    List<IllustDTO> newList = new ArrayList<>();
                    for (IllustDTO illustDTO : illustListDTO.getIllustList()) {
                        if (followedSet.contains(illustDTO.getId())) {
                            continue;
                        } else {
                            followedSet.add(illustDTO.getId());
                            newList.add(illustDTO);
                        }
                    }
                    followedNextUrl = illustListDTO.getNextUrl();
                    followedList.getValue().addAll(newList);
                    followedList.postValue(followedList.getValue());
                    isFollowedLoading = false;
                }
            });
        }
    }

    public void refreshFollowedData() {
        followedNextUrl = null;
        followedSet = new HashSet();
        getFollowedData();
    }

    public MutableLiveData<List<IllustDTO>> getFollowedList() {
        return followedList;
    }

    public MutableLiveData<String> getCoverImageUrl() {
        return coverImageUrl;
    }
}
