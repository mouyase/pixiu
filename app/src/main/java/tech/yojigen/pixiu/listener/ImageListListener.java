package tech.yojigen.pixiu.listener;

import android.view.View;

import tech.yojigen.pixiu.dto.IllustDTO;

public interface ImageListListener {
    public void onItemClick(View view, IllustDTO illust, int position);

    public void onItemLongClick(View view, IllustDTO illust, int position);
}
