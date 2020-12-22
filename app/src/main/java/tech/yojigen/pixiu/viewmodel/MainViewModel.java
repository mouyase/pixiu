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
    private MutableLiveData<List> recommendList = new MutableLiveData();
    private Map recommendMap = new HashMap<>();
    private int recommendListPage = 0;
    private MutableLiveData<List> followedList = new MutableLiveData();
    private Map followedMap = new HashMap<>();
    private int followedListPage = 0;

//    MainViewModel() {
//        recommendList.setValue(new ArrayList());
//        followedList.setValue(new ArrayList());
//    }

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
                for (IllustDTO illustDTO : recommendDTO.getIllustList()) {
                    System.out.println(illustDTO.getTitle());
                }
            }
        });
    }
}
