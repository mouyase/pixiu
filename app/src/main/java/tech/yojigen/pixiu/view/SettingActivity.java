package tech.yojigen.pixiu.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivitySettingBinding;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.util.YSetting;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding viewBinding;

    XUICommonListItemView modeSelect, pathSelect;

    private final int PATH_REQUEST_CODE = 0x1000;
    String pathString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
    }

    protected void initView() {
        viewBinding.titleBar.setLeftClickListener(v -> finish());

        modeSelect = viewBinding.groupListView.createItemView("模式选择");
        switch (PixiuApplication.getData().getNetworkMode()) {
            case PixivClient.MODE_NO_SNI:
                modeSelect.setDetailText("大陆直连模式");
                break;
            case PixivClient.MODE_NORMAL:
                modeSelect.setDetailText("普通模式");
                break;
            case PixivClient.MODE_PROXY:
                modeSelect.setDetailText("内置代理模式");
                break;
        }
        modeSelect.setOrientation(XUICommonListItemView.HORIZONTAL);
        modeSelect.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_NONE);
        XUIGroupListView.newSection(this)
                .setTitle("网络设置")
                .addItemView(modeSelect, v -> new MaterialDialog.Builder(this)
                        .items(new String[]{"大陆直连模式", "内置代理模式", "普通模式"})
                        .itemsCallback((dialog, itemView, p, text) -> {
                            if (text.equals("大陆直连模式")) {
                                PixivClient.getInstance().changeMode(PixivClient.MODE_NO_SNI);
                                YSetting.set(Value.SETTING_NETWORK_MODE, PixivClient.MODE_NO_SNI);
                                PixiuApplication.getData().setNetworkMode(PixivClient.MODE_NO_SNI);
                                modeSelect.setDetailText("大陆直连模式");
                            } else if (text.equals("内置代理模式")) {
                                PixivClient.getInstance().changeMode(PixivClient.MODE_PROXY);
                                YSetting.set(Value.SETTING_NETWORK_MODE, PixivClient.MODE_PROXY);
                                PixiuApplication.getData().setNetworkMode(PixivClient.MODE_PROXY);
                                modeSelect.setDetailText("内置代理模式");
                            } else if (text.equals("普通模式")) {
                                PixivClient.getInstance().changeMode(PixivClient.MODE_NORMAL);
                                YSetting.set(Value.SETTING_NETWORK_MODE, PixivClient.MODE_NORMAL);
                                PixiuApplication.getData().setNetworkMode(PixivClient.MODE_NORMAL);
                                modeSelect.setDetailText("普通模式");
                            }
                        })
                        .show())
                .addTo(viewBinding.groupListView);

        pathSelect = viewBinding.groupListView.createItemView("选择图片保存目录");
        try {
            pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
            pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        pathSelect.setDetailText(pathString);
        pathSelect.setOrientation(XUICommonListItemView.VERTICAL);
        pathSelect.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        XUIGroupListView.newSection(this)
                .setTitle("文件设置")
                .addItemView(pathSelect, v -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
                    startActivityForResult(intent, PATH_REQUEST_CODE);
                })
                .addTo(viewBinding.groupListView);

        XUICommonListItemView safeMode = viewBinding.groupListView.createItemView("青少年模式");
        safeMode.setOrientation(XUICommonListItemView.VERTICAL);
        safeMode.setDetailText("此功能仅为本地使用，不影响网页端设置");
        safeMode.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        safeMode.getSwitch().setChecked(PixiuApplication.getData().isSafeMode());
        safeMode.getSwitch().setOnCheckedChangeListener((v, isChecked) -> {
            YSetting.set(Value.SETTING_SAFE_MODE, isChecked);
            PixiuApplication.getData().setSafeMode(isChecked);
        });
        XUIGroupListView.newSection(this)
                .setTitle("浏览设置")
                .addItemView(safeMode, v -> {
                })
                .addTo(viewBinding.groupListView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        if (requestCode == PATH_REQUEST_CODE) {
            if (data.getData() != null) {
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                YSetting.set(Value.SETTING_PATH_URI, String.valueOf(data.getData()));
                PixiuApplication.getData().setPathUri(String.valueOf(data.getData()));
                try {
                    pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
                    pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pathSelect.setDetailText(pathString);
            }
        }
    }
}