package tech.yojigen.pixiu.viewmodel;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private String searchKey;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void setKey(String searchKey) {
        this.searchKey = searchKey;
        for (int i = 0; i < 6; i++) {
            MutableLiveData mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(new ArrayList());
            illustListList.add(i, mutableLiveData);
            nextTimesList.add(i, 0);
            isLoadingList.add(i, false);
            refreshData(i);
        }
    }

    public void getData(int index) {
        String startData, endData;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1 * nextTimesList.get(index));
        startData = simpleDateFormat.format(calendar.getTime());
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1 * (nextTimesList.get(index) + 1));
        endData = simpleDateFormat.format(calendar.getTime());
        switch (index) {
            case 0:
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                endData = simpleDateFormat.format(calendar.getTime());
                break;
            case 1:
                calendar.setTime(new Date());
                calendar.add(Calendar.WEEK_OF_YEAR, -1 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.WEEK_OF_YEAR, -1 * (nextTimesList.get(index) + 1));
                calendar.add(Calendar.DATE, +1);
                endData = simpleDateFormat.format(calendar.getTime());
                break;
            case 2:
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -1 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -1 * (nextTimesList.get(index) + 1));
                calendar.add(Calendar.DATE, +1);
                endData = simpleDateFormat.format(calendar.getTime());
                break;
            case 3:
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -3 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -3 * (nextTimesList.get(index) + 1));
                calendar.add(Calendar.DATE, +1);
                endData = simpleDateFormat.format(calendar.getTime());
                break;
            case 4:
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -6 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -6 * (nextTimesList.get(index) + 1));
                calendar.add(Calendar.DATE, +1);
                endData = simpleDateFormat.format(calendar.getTime());
                break;
            case 5:
                calendar.setTime(new Date());
                calendar.add(Calendar.YEAR, -1 * nextTimesList.get(index));
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.YEAR, -1 * (nextTimesList.get(index) + 1));
                calendar.add(Calendar.DATE, +1);
                endData = simpleDateFormat.format(calendar.getTime());
                break;
        }
        System.out.println(startData + "|" + endData);
        String url = Value.URL_API + "/v1/search/popular-preview/illust";
        if (!isLoadingList.get(index)) {
            isLoadingList.set(index, true);
            PixivData pixivData = new PixivData.Builder()
                    .set("filter", "for_android")
                    .set("include_translated_tag_results", true)
                    .set("merge_plain_keyword_results", true)
                    .set("word", searchKey)
                    .set("search_target", "partial_match_for_tags")
                    .set("start_date", startData)
                    .set("end_date", endData)
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
                    nextTimesList.set(index, nextTimesList.get(index) + 1);
                    if (illustListList.get(index).getValue().size() < 30) {
                        getData(index);
                    }
                }
            });
        }
    }

    public void refreshData(int index) {
        illustListList.get(index).getValue().clear();
        nextTimesList.set(index, 0);
        isLoadingList.set(index, false);
        illustListList.get(index).getValue().clear();
        illustListList.get(index).postValue(illustListList.get(index).getValue());
        getData(index);
    }

    public List<MutableLiveData<List<IllustDTO>>> getIllustListList() {
        return illustListList;
    }
}