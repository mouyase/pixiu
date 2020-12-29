package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.adapter.HotTagListAdapter;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivitySearchBinding;
import tech.yojigen.pixiu.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity {
    private SearchViewModel viewModel;
    private ActivitySearchBinding viewBinding;
    private HotTagListAdapter hotTagListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        initViewModel();
        initView();
        initViewEvent();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        hotTagListAdapter = new HotTagListAdapter(viewModel.getHotTagList().getValue());
        viewModel.getHotTagList().observe(this, tagDTOS -> hotTagListAdapter.notifyItemInserted(tagDTOS.size()));
        viewModel.getAutoCompliteArray().observe(this, strings -> viewBinding.searchView.setSuggestions(strings));
    }

    private void initViewEvent() {
    }

    private void initView() {
        // 去掉searchTextView默认的padding
        AppCompatEditText searchTextView = findViewById(R.id.searchTextView);
        searchTextView.setPadding(0, 0, 0, 0);

        viewBinding.searchView.setVoiceSearch(false);
        viewBinding.searchView.setEllipsize(true);
        viewBinding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(Value.BUNDLE_KEY_SEARCH, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        viewBinding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        viewBinding.searchView.setSubmitOnClick(true);

        viewBinding.titleBar.setOnTitleClickListener(v -> runOnUiThread(() -> viewBinding.searchView.showSearch()));
        viewBinding.titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_search) {
            @Override
            public void performAction(View view) {
                runOnUiThread(() -> viewBinding.searchView.showSearch());
            }
        });
        viewBinding.titleBar.setLeftClickListener(v -> finish());
        viewBinding.recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                return 1;
            }
        });
        viewBinding.recyclerView.setLayoutManager(gridLayoutManager);
        viewBinding.recyclerView.setAdapter(hotTagListAdapter);
    }

    @Override
    protected void onPause() {
        runOnUiThread(() -> viewBinding.searchView.closeSearch());
        super.onPause();
    }
}