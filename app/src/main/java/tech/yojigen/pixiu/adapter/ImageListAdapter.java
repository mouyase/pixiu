package tech.yojigen.pixiu.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.dto.IllustDTO;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
    List<IllustDTO> illusts;
//    int imageWidth;

    public ImageListAdapter(List<IllustDTO> illusts) {
        this.illusts = illusts;
    }

    @NonNull
    @Override
    public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagelist, parent, false);
//        imageWidth = parent.getWidth() / 3;
        return new ImageListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
        String imageTag = (String) holder.imageView.getTag();
        if (TextUtils.isEmpty(imageTag) || imageTag.endsWith(illusts.get(position).getImageUrls().getMedium())) {
            Glide.with(holder.itemView)
                    .load(illusts.get(position).getImageUrls().getMedium())
                    .dontAnimate()
//                .onlyRetrieveFromCache(true)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.imageView.setImageDrawable(resource);
//                            ViewUtils.fadeIn(holder.imageView, 500, null);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
        holder.imageView.setTag(illusts.get(position).getImageUrls().getMedium());
    }

    @Override
    public int getItemCount() {
        return illusts.size();
    }

    class ImageListHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageListHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
//            ViewGroup.LayoutParams layoutParam = imageView.getLayoutParams();
//            layoutParam.width = imageWidth;
//            imageView.setLayoutParams(layoutParam);
        }
    }
}
