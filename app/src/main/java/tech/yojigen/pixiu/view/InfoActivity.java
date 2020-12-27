package tech.yojigen.pixiu.view;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;

import androidx.appcompat.app.AppCompatActivity;

import tech.yojigen.pixiu.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
//        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.slide);
//        //退出时使用
//        getWindow().setExitTransition(explode);
//        //第一次进入时使用
//        getWindow().setEnterTransition(explode);
//        //再次进入时使用
//        getWindow().setReenterTransition(explode);
        getWindow().setEnterTransition(new Explode().setDuration(2000));
        getWindow().setExitTransition(new Explode().setDuration(2000));
    }
}