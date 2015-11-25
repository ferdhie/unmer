package com.ferdianto.android.unmernews;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferdianto.android.unmernews.model.News;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ferdhie on 26-11-2015.
 */
public class NewsListViewAdapter extends ArrayAdapter<News> {
    private final List<News> values;
    private final Context context;
    private LayoutInflater inflater = null;
    private int resourceId;
    private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    public NewsListViewAdapter(Context context, int resource, List<News> values) {
        super(context, -1, values);
        this.resourceId=resource;
        this.values=values;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView==null) {
            view = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.newsContent = (TextView)view.findViewById(R.id.newsText);
            holder.newsTitle = (TextView)view.findViewById(R.id.newsTitle);
            holder.newsDate = (TextView)view.findViewById(R.id.newsDate);
            holder.newsImage = (ImageView)view.findViewById(R.id.news_image);
            view.setTag(holder);
        } else {
            view = (View)convertView;
            holder = (ViewHolder)view.getTag();
        }

        News news = getItem(position);
        Picasso.with(context).load(news.getNewsImage())
                .resize(300, 300)
                .centerCrop()
                .into(holder.newsImage);
        holder.newsTitle.setText(news.getTitle());
        holder.newsContent.setText(Html.fromHtml(news.getShortNews()));
        holder.newsDate.setText(fmt.format(news.getLastUpdate()));

        NewsClickListener listener = new NewsClickListener(context, news.getLink());
        holder.newsImage.setOnClickListener(listener);
        holder.newsTitle.setOnClickListener(listener);

        return view;
    }

    class ViewHolder {
        public TextView newsTitle;
        public TextView newsContent;
        public TextView newsDate;
        public ImageView newsImage;
    }
}
