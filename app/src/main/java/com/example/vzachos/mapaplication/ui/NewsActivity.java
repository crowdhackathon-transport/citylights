package com.example.vzachos.mapaplication.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.example.vzachos.mapaplication.core.Constants;
import com.example.vzachos.mapaplication.core.News;
import com.example.vzachos.mapaplication.R;

import butterknife.InjectView;

public class NewsActivity extends BootstrapActivity {

    private News newsItem;

    @InjectView(R.id.tv_title) protected TextView title;
    @InjectView(R.id.tv_content) protected TextView content;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news);

        if (getIntent() != null && getIntent().getExtras() != null) {
            newsItem = (News) getIntent().getExtras().getSerializable(Constants.Extra.NEWS_ITEM);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(newsItem.getTitle());

        title.setText(newsItem.getTitle());
        content.setText(newsItem.getContent());

    }

}
