package tech.yojigen.pixiu.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.button.ButtonView;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityInfoBinding;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.TagDTO;
import tech.yojigen.util.YBundle;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class InfoActivity extends AppCompatActivity {
    ActivityInfoBinding viewBinding;
    IllustDTO illust;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        illust = YBundle.get(getIntent());
        viewBinding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
    }

    @SuppressLint("SimpleDateFormat")
//    2020年12月21日早上6点48分
    private SimpleDateFormat viewDateFormat = new SimpleDateFormat("yyyy年MM月dd日hh点mm分", Locale.CHINA);
    private SimpleDateFormat reFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.JAPAN);

    protected void initView() {
        viewBinding.titleBar.setLeftClickListener(v -> ActivityCompat.finishAfterTransition(this));
        viewBinding.title.setText(illust.getTitle());
        viewBinding.artist.setText(illust.getUser().getName());
        viewBinding.artistAccount.setText(illust.getUser().getAccount());
        viewBinding.countLike.setText(String.valueOf(illust.getTotalBookmarks()));
        viewBinding.countView.setText(String.valueOf(illust.getTotalView()));
        try {
            viewBinding.date.setText(viewDateFormat.format(reFormatter.parse(illust.getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewBinding.caption.setText(HtmlCompat.fromHtml(illust.getCaption(), FROM_HTML_MODE_LEGACY));
        Comparator<TagDTO> comparator = (o1, o2) -> {
            Collator collator = Collator.getInstance();
            int temp;
            String s1 = o1.getName();
            String s2 = o2.getName();
            if (!o1.isNoTranslate()) {
                if (o1.getName().length() < o1.getTranslatedName().length()) {
                    s1 = o1.getTranslatedName();
                }
            }
            if (!o2.isNoTranslate()) {
                if (o2.getName().length() < o2.getTranslatedName().length()) {
                    s2 = o2.getTranslatedName();
                }
            }
            temp = s1.length() - s2.length();
            return temp == 0 ? collator.getCollationKey(o1.getName()).compareTo(collator.getCollationKey(o2.getName())) : temp;
        };
        Collections.sort(illust.getTags(), comparator);
        for (TagDTO tagDTO : illust.getTags()) {
            String searchKey = tagDTO.getName();
            StringBuilder tagName = new StringBuilder();
            tagName.append(tagDTO.getName());
            if (!tagDTO.isNoTranslate()) {
                tagName.append("\n");
                tagName.append(tagDTO.getTranslatedName());
            }
            View view = LayoutInflater.from(this).inflate(R.layout.item_tags, viewBinding.tagsFlowLayout, false);
            ButtonView buttonView = view.findViewById(R.id.tag_button);
            buttonView.setText(tagName);
            buttonView.setOnClickListener(v -> {
                Intent intent = new Intent(this, SearchResultActivity.class);
                YBundle.set(intent,searchKey);
                startActivity(intent);
            });
            viewBinding.tagsFlowLayout.addView(view);
        }
        Glide.with(this)
                .load(illust.getUser().getHeadImage())
                .transition(withCrossFade(500))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        viewBinding.head.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (illust == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}