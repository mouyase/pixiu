package tech.yojigen.pixiu.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.RecommendDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

public class MainViewModel extends ViewModel {
    private Gson gson = new Gson();
    private MutableLiveData<List<IllustDTO>> recommendList = new MutableLiveData();
    private Map recommendMap = new HashMap<>();
    private int recommendListPage = 0;
    private String recommendNextUrl;
    private MutableLiveData<List<IllustDTO>> followedList = new MutableLiveData();
    private Map followedMap = new HashMap<>();
    private int followedListPage = 0;
    private String followedNextUrl;

    private MutableLiveData<String> coverImageUrl = new MutableLiveData();

    public MainViewModel() {
        recommendList.setValue(new ArrayList());
        followedList.setValue(new ArrayList());
    }

    public void getRecommendData() {
        PixivData pixivData = new PixivData.Builder()
                .set("filter", "for_android")
                .set("include_ranking_illusts", true)
                .build();
        PixivClient.getInstance().get(Value.URL_API + "/v1/illust/recommended", pixivData, new PixivCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(String body) {
                RecommendDTO recommendDTO = gson.fromJson(body, RecommendDTO.class);
                if (recommendDTO.getRankingIllustList().size() > 0) {
                    coverImageUrl.postValue(recommendDTO.getRankingIllustList().get(0).getImageUrls().getMedium());
                }
                for (IllustDTO illustDTO : recommendDTO.getIllustList()) {
                    recommendMap.put(illustDTO.getId(), illustDTO);
                }
                recommendNextUrl = recommendDTO.getNextUrl();
                List<IllustDTO> illusts = new ArrayList<IllustDTO>(recommendMap.values());
                recommendList.postValue(illusts);
            }
        });
    }

    public MutableLiveData<String> getCoverImageUrl() {
        return coverImageUrl;
    }

    public MutableLiveData<List<IllustDTO>> getRecommendList() {
        return recommendList;
    }
}
