package com.ferdianto.android.unmernews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * Created by ferdhie on 26-11-2015.
 */
public class NewsClickListener implements View.OnClickListener {
    private Context context;
    private String url;

    public NewsClickListener(Context context, String url) {
        this.context=context;
        this.url=url;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
