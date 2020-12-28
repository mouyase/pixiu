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
import java.util.List;

import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.IllustListDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

public class IllustViewModel extends ViewModel {
    private Gson gson = new Gson();
    private String nextUrl;
    private String searchKey;
    private MutableLiveData<List<IllustDTO>> illustList = new MutableLiveData<>();
    private int nextTimes = 0;
    private int mode = 0;
    private int searchIndex = 0;

    public IllustViewModel() {
        illustList.setValue(new ArrayList<>());
    }

    private boolean isLoading = false;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void getMoreData() {
        switch (mode) {
            case BundleIllustDTO.MODE_SINGLE:
                return;
            case BundleIllustDTO.MODE_NORMAL:
                if (TextUtils.isEmpty(nextUrl)) {
                    return;
                }
                if (!isLoading) {
                    isLoading = true;
                    PixivClient.getInstance().get(nextUrl, new PixivCallback() {
                        @Override
                        public void onFailure() {
                            isLoading = false;
                        }

                        @Override
                        public void onResponse(String body) {
                            IllustListDTO illustListDTO = gson.fromJson(body, IllustListDTO.class);
                            nextUrl = illustListDTO.getNextUrl();
                            illustList.getValue().addAll(illustListDTO.getIllustList());
                            illustList.postValue(illustList.getValue());
                            isLoading = false;
                        }
                    });
                }
                break;
            case BundleIllustDTO.MODE_SEARCH:
                String startData, endData;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1 * nextTimes);
                startData = simpleDateFormat.format(calendar.getTime());
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1 * (nextTimes + 1));
                endData = simpleDateFormat.format(calendar.getTime());
                switch (this.searchIndex) {
                    case 0:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DATE, -1 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                    case 1:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.WEEK_OF_YEAR, -1 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        calendar.setTime(new Date());
                        calendar.add(Calendar.WEEK_OF_YEAR, -1 * (nextTimes + 1));
                        calendar.add(Calendar.DATE, +1);
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                    case 2:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -1 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -1 * (nextTimes + 1));
                        calendar.add(Calendar.DATE, +1);
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                    case 3:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -3 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -3 * (nextTimes + 1));
                        calendar.add(Calendar.DATE, +1);
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                    case 4:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -6 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MONTH, -6 * (nextTimes + 1));
                        calendar.add(Calendar.DATE, +1);
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                    case 5:
                        calendar.setTime(new Date());
                        calendar.add(Calendar.YEAR, -1 * nextTimes);
                        startData = simpleDateFormat.format(calendar.getTime());
                        calendar.setTime(new Date());
                        calendar.add(Calendar.YEAR, -1 * (nextTimes + 1));
                        calendar.add(Calendar.DATE, +1);
                        endData = simpleDateFormat.format(calendar.getTime());
                        break;
                }
                System.out.println(startData + "|" + endData);
                String url = Value.URL_API + "/v1/search/popular-preview/illust";
                if (!isLoading) {
                    isLoading = true;
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
                            isLoading = false;
                        }

                        @Override
                        public void onResponse(String body) {
                            IllustListDTO illustListDTO = gson.fromJson(body, IllustListDTO.class);
                            illustList.getValue().addAll(illustListDTO.getIllustList());
                            illustList.postValue(illustList.getValue());
                            isLoading = false;
                            nextTimes = nextTimes + 1;
                            if (illustList.getValue().size() < 30) {
                                getMoreData();
                            }
                        }
                    });
                }
                break;
        }
    }

    public MutableLiveData<List<IllustDTO>> getIllustList() {
        return illustList;
    }

    public void setBundle(BundleIllustDTO bundleIllustDTO) {
        this.illustList.getValue().addAll(bundleIllustDTO.getIllustList());
        this.illustList.postValue(this.illustList.getValue());
        this.mode = bundleIllustDTO.getMode();
        this.nextTimes = bundleIllustDTO.getNextTimes();
        this.nextUrl = bundleIllustDTO.getNextUrl();
        this.searchIndex = bundleIllustDTO.getSearchIndex();
        this.searchKey = bundleIllustDTO.getSearchKey();
    }
}