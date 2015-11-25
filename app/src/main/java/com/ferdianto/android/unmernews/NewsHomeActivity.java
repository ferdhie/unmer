package com.ferdianto.android.unmernews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ferdianto.android.unmernews.model.News;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NewsHomeActivity extends ActionBarActivity {

    private OkHttpClient httpClient = new OkHttpClient();

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsListViewAdapter listViewAdapter;
    private List<News> newsList;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home);

        listView = (ListView)findViewById(R.id.news_listview);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        TextView emptyText = (TextView)findViewById(R.id.empty_textview);
        listView.setEmptyView(emptyText);
        emptyText.setText("Loading..");

        newsList = new ArrayList<>();
        listViewAdapter = new NewsListViewAdapter(this, R.layout.news_item, newsList);
        listView.setAdapter(listViewAdapter);

        new GetNewsTask(this, httpClient, newsList, listViewAdapter).execute(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetNewsTask(NewsHomeActivity.this, httpClient, newsList, listViewAdapter) {
                    @Override
                    protected void onPostExecute(Integer result) {
                        super.onPostExecute(result);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }.execute(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
