package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.mymusicplayer.R;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.util.BitmapWorkerTask;
import com.example.www11.mymusicplayer.util.ImageMemoryCache;
import com.example.www11.mymusicplayer.util.ViewHolderTool;


/**
 * 点击“我的歌单”时的ListView的适配器.
 */
public class SongListAdapter extends ArrayAdapter<SongList> {
    /**
     * 子项布局的id
     */
    private int mResourceId;

    /**
     * 歌单所在的listview
     */
    private ListView mListView;

    /**
     * 空白图片
     */
    private Bitmap mLoadingBitmap;

    public SongListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<SongList> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
        }

        SongList songList = getItem(position);
        View view;
        String url = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = ViewHolderTool.get(view, R.id.song_list_name);
        ImageView image = ViewHolderTool.get(view, R.id.song_list_image);

        if (songList != null) {
            url = songList.getCoverImgUrl();
            image.setImageResource(R.drawable.empty_photo);
            textView.setText(songList.getName());
        }

        /*
         * 先判断内存中是否有缓存好的图片，有直接拿，没有则去网络请求.
         * 关于cancelPotentialWork(url,image):
         * 若后台的任务是imageview在请求另外一张图片，则取消任务。
         * 若后台的任务请求的图片刚好和imageview需要的一致，则if下面的不执行.
         * 若该imageview后台无请求任务，cancelPo...返回true,则执行if里的语句.
         * */
        BitmapDrawable drawable = ImageMemoryCache.getBitmapFromMemoryCache(url);
        if (drawable != null) {
            image.setImageDrawable(drawable);
        } else if (BitmapWorkerTask.cancelPotentialWork(url, image)) {
            //新建请求图片的task，该task含有imageview的弱引用
            BitmapWorkerTask task = new BitmapWorkerTask(image);
            //先给AsyncDrawable关联task的引用，imageview可以通过AsyncDrawable关联到task
            BitmapWorkerTask.AsyncDrawable asyncDrawable = new BitmapWorkerTask.AsyncDrawable(
                    getContext().getResources(), mLoadingBitmap, task);//先放入一张空白的图片
            //imageview设定该空白的图片
            image.setImageDrawable(asyncDrawable);
            //task根据url去请求图片
            task.execute(url);
        }
        return view;
    }
}
