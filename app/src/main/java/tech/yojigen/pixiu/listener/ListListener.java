package tech.yojigen.pixiu.listener;

import android.view.View;

import tech.yojigen.pixiu.dto.IllustDTO;

public interface ListListener {
    public void onItemClick(View view, IllustDTO illust, int position);
}
