package com.zhon.frame.mvp.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhon.frame.R;
import com.zhon.frame.mvp.test.bean.TestBean;

import java.util.List;

/*
 * 项目名:    BaseFrame
 * 包名       com.zhon.frame.mvp.login.adapter
 * 文件名:    TestAdapter
 * 创建者:    ZJB
 * 创建时间:  2017/9/7 on 13:43
 * 描述:     TODO
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.viewHolder> {

    private List<TestBean.StoriesBean> list;
    private Context context;

    public TestAdapter(List<TestBean.StoriesBean> list) {
        this.list = list;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImages().get(0)).crossFade().into(holder.imageView);
        holder.textView.setText(list.get(position).getTitle());
        holder.textView1.setText("2017-9-7 12:00");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView, textView1;

        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);
            textView1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}
