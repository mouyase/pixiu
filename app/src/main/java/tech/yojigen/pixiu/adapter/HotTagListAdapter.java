package tech.yojigen.pixiu.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.TagDTO;
import tech.yojigen.pixiu.view.IllustActivity;
import tech.yojigen.pixiu.view.SearchResultActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HotTagListAdapter extends RecyclerView.Adapter<HotTagListAdapter.ViewHolder> {
    List<TagDTO> tagList;
    int itemWH;

    public HotTagListAdapter(List<TagDTO> tagList) {
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public HotTagListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_tag_list, parent, false);
        itemWH = parent.getWidth() / 3;
        return new HotTagListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotTagListAdapter.ViewHolder holder, int position) {
        TagDTO tagDTO = tagList.get(position);
        IllustDTO illust = tagDTO.getIllust();
        if (tagDTO.isNoTranslate()) {
            holder.translatedName.setVisibility(View.INVISIBLE);
        } else {
            holder.translatedName.setText(tagDTO.getTranslatedName());
        }
        holder.name.setText("#" + tagDTO.getTag());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = itemWH;
        layoutParams.height = itemWH;
        if (position == 0) {
            layoutParams.width = itemWH * 3;
            layoutParams.height = (int) (itemWH * 3 * 0.618);
            holder.name.setTextSize(24);
            holder.translatedName.setTextSize(16);
        } else {
            holder.name.setTextSize(14);
            holder.translatedName.setTextSize(10);
        }
        holder.itemView.setLayoutParams(layoutParams);
        Glide.with(holder.itemView.getContext())
                .load(illust.getImageUrls().getSquare())
                .transition(withCrossFade(500))
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SearchResultActivity.class);
            intent.putExtra(Value.BUNDLE_KEY_SEARCH, tagDTO.getTag());
            v.getContext().startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(v.getContext(), IllustActivity.class);
            List<IllustDTO> illustDTOList = new ArrayList<>();
            illustDTOList.add(illust);
            BundleIllustDTO bundleIllustDTO = new BundleIllustDTO();
            bundleIllustDTO.setPosition(0);
            bundleIllustDTO.setIllustList(illustDTOList);
            bundleIllustDTO.setMode(BundleIllustDTO.MODE_SINGLE);
            intent.putExtra(Value.BUNDLE_ILLUST_LIST, bundleIllustDTO.toJson());
            v.getContext().startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, translatedName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.username);
            translatedName = itemView.findViewById(R.id.translatedName);
        }
    }
}