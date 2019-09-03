package com.wlison.view.nice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wlison.view.nice.activity.NiceCalenderActivity;
import com.wlison.view.nice.activity.NiceCoordinatorActivity;
import com.wlison.view.nice.activity.NicePopupWindowActivity;
import com.wlison.view.nice.activity.NiceScrollViewActivity;
import com.wlison.view.nice.activity.NiceSpinnerActivity;
import com.wlison.view.nice.activity.NiceSwitchActivity;
import com.wlison.view.nice.adapter.MainRecyclerViewAdapter;
import com.wlison.view.nice.base.BaseActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);

        toolbar.setTitle("Nice");
    }

    private void initData() {
        String[] team = getResources().getStringArray(R.array.main_name_list);
        List<String> list = Arrays.asList(team);
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                initListener(position);
            }
        });
    }

    private void initListener(int position) {
        switch (position){
            case 0:
                openActivity(NiceSpinnerActivity.class);
                break;
            case 1:
                openActivity(NiceSwitchActivity.class);
                break;
            case 2:
                openActivity(NiceCalenderActivity.class);
                break;
            case 3:
                openActivity(NicePopupWindowActivity.class);
                break;
            case 4:
                openActivity(NiceCoordinatorActivity.class);
                break;
            case 5:
                openActivity(NiceScrollViewActivity.class);
                break;
            default:
        }
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);
    }

}
