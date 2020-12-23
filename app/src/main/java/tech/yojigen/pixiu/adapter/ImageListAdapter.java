package tech.yojigen.pixiu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.listener.ImageListListener;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
    List<IllustDTO> illusts;
    int imageWidth;
    int column;
    ImageListListener imageListListener;

    public ImageListAdapter(List<IllustDTO> illusts, int column) {
        this.illusts = illusts;
        this.column = column >= 1 ? column : 0;
    }

    public void setListListener(ImageListListener listListener) {
        this.imageListListener = listListener;
    }

    @NonNull
    @Override
    public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagelist, parent, false);
        imageWidth = parent.getWidth() / column;
        return new ImageListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
        IllustDTO illust = illusts.get(position);
        int height = (int) (((float) illust.getHeight() / (float) illust.getWidth()) * imageWidth);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = imageWidth;
        holder.itemView.setLayoutParams(layoutParams);
        Glide.with(holder.itemView.getContext())
                .load(illust.getImageUrls().getMedium())
                .transition(withCrossFade(500))
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            this.imageListListener.onItemClick(v, illust, position);
        });
        holder.itemView.setOnLongClickListener(v -> {
            this.imageListListener.onItemLongClick(v, illust, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return illusts.size();
    }

    static class ImageListHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageListHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
