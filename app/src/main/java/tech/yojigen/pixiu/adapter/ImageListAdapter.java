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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.dto.IllustDTO;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
    List<IllustDTO> illusts;
    int imageWidth;

    public ImageListAdapter(List<IllustDTO> illusts) {
        this.illusts = illusts;
    }

    @NonNull
    @Override
    public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagelist, parent, false);
        imageWidth = parent.getWidth() / 3;
        return new ImageListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
        int height = (int) (((float) illusts.get(position).getHeight() / (float) illusts.get(position).getWidth()) * imageWidth);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = imageWidth;
        holder.itemView.setLayoutParams(layoutParams);
        System.out.println(illusts.get(position).getTitle() + "| " + imageWidth + " " + "| " + height + " ");
        System.out.println(illusts.get(position).getImageUrls().getMedium().replace("pximg.net", "pixiv.cat"));
//        if (TextUtils.isEmpty(imageTag) || imageTag.endsWith(illusts.get(position).getImageUrls().getMedium())) {
        Glide.with(holder.itemView.getContext())
                .load(illusts.get(position).getImageUrls().getMedium())
//                .onlyRetrieveFromCache(true)
                .transition(withCrossFade(500))
                .into(holder.imageView);
//    }
//        holder.imageView.setTag(illusts.get(position).getImageUrls().getMedium());
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
        }
    }
}
