package com.ferdianto.android.unmernews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.ferdianto.android.unmernews.model.News;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ferdhie on 26-11-2015.
 */
public class GetNewsTask extends AsyncTask<Object,Void,Integer> {
    private OkHttpClient httpClient;
    private ProgressDialog dialog = null;
    private Activity context;
    private final List<News> newsList;
    private NewsListViewAdapter adapter;

    public GetNewsTask(Activity context, OkHttpClient httpClient, List<News> newsList, NewsListViewAdapter adapter)   {
        super();
        this.newsList=newsList;
        this.httpClient=httpClient;
        this.context=context;
        this.adapter=adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        File cacheFile = new File(context.getFilesDir(), "unmer.html");
        if (!cacheFile.exists()) {
            dialog = ProgressDialog.show(context, "Unmer News", "Loading..", true);
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (dialog!=null) {
            dialog.dismiss();
        }

        if (result < 0) {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Loading news error, please check connection")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }

        dialog = null;

        if (result>0)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected Integer doInBackground(Object... params) {
        boolean refresh = (Boolean)params[0];
        try {
            File cacheFile = new File(context.getFilesDir(), "unmer.html");

            if (!cacheFile.exists() || refresh) {
                Response resp = httpClient.newCall(new Request.Builder().url("http://unmer.ac.id/news").build()).execute();
                if (!resp.isSuccessful())
                    throw new IOException("Invalid response " + resp.code());

                FileOutputStream fileOutputStream = new FileOutputStream(cacheFile, false);
                fileOutputStream.write(resp.body().bytes());
                fileOutputStream.close();
            }

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Document doc = Jsoup.parse(cacheFile, "UTF-8");
            int updated = 0;
            for(Element el: doc.select("#content2 .listing")) {
                String title = el.previousElementSibling().text();
                String imageLink = el.select(".thumb img").attr("src");
                String description = el.select(".description p").html();
                String date = el.select(".description .lastupdte i").text();
                String link = el.select(".description .moreinfo").attr("href");
                News news = new News();
                news.setTitle(title);
                news.setNewsImage(imageLink);
                news.setShortNews(description);
                news.setLink(link);
                try {
                    news.setLastUpdate(format.parse(date));
                    if (!newsList.contains(news)) {
                        newsList.add(news);
                        updated++;
                    }
                } catch (ParseException e) {
                    Log.e(GetNewsTask.class.getSimpleName(), "Error loading news: ", e);
                    continue;
                }
            }

            if (updated > 0) {
                Collections.sort(newsList, new Comparator<News>() {
                    @Override
                    public int compare(News lhs, News rhs) {
                        return (int) (rhs.getLastUpdate().getTime() - lhs.getLastUpdate().getTime());
                    }
                });
            }

            return updated;
        } catch (IOException e) {
            Log.e(GetNewsTask.class.getSimpleName(), "Error loading news: ", e);
            return -1;
        }
    }
}
