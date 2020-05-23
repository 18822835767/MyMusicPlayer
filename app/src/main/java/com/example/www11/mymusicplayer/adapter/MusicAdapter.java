package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.BitmapWorkerTask;
import com.example.www11.mymusicplayer.util.ImageMemoryCache;
import com.example.www11.mymusicplayer.util.ViewHolderTool;

import java.lang.ref.WeakReference;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 采用imageView和task(BitmapWorkTask)双关联的方式来避免图片的乱序与闪图.
 * imageView和task关联的方式式采用弱引用进行的，防止回收不了.
 * */
public class MusicAdapter extends ArrayAdapter<Music> {

    /**
     * adapter所对应的listview
     * */
    private ListView mListView;
    
    /**
     * 空白图片
     * */
    private Bitmap mLoadingBitmap;

    public MusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
        }
        
        View view;
        Music music = getItem(position);
        String url = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.music_item, null);
        } else {
            view = convertView;
        }
        
        TextView musicName = ViewHolderTool.get(view,R.id.music_name);
        TextView singerName = ViewHolderTool.get(view,R.id.singer_name);
        ImageView image = ViewHolderTool.get(view,R.id.music_image);

        //设定歌曲名字和歌手名字
        if (music != null) {
            url = music.getPicUrl();
            musicName.setText(music.getName());
           singerName.setText(music.getSingerName());
        }

        /*
         * 先判断内存中是否有缓存好的图片，有直接拿，没有则去网络请求.
         * 关于cancelPotentialWork(url,image):
         * 若后台的任务是imageview在请求另外一张图片，则取消任务。
         * 若后台的任务请求的图片刚好和imageview需要的一致，则if下面的不执行.
         * 若该imageview后台无请求任务，cancelPo...返回true,则执行if里的语句.
         * */
        BitmapDrawable drawable = ImageMemoryCache.getBitmapFromMemoryCache(url);
        if(drawable != null){
            cancelPotentialWork(url,image);
            image.setImageDrawable(drawable);
        }else if(cancelPotentialWork(url, image)){
            //新建请求图片的task，该task含有imageview的弱引用
            BitmapWorkerTask task = new BitmapWorkerTask(image);
            //先给AsyncDrawable关联task的引用，imageview可以通过AsyncDrawable关联到task
            AsyncDrawable asyncDrawable = new AsyncDrawable(getContext().getResources(),
                    mLoadingBitmap, task);//先放入一张空白的图片
            //imageview设定该空白的图片
            image.setImageDrawable(asyncDrawable);
            //task根据url去请求图片
            task.execute(url);
        }
        return view;
    }

    /**
     * 取消其他图片的后台下载任务.
     * <p>
     * 若该imageView正在请求的下载任务和当前需要的图片不一致，则cancel掉该任务，并且返回true.
     * 若该imageView正在请求的下载任务和当前需要的图片一致，则继续该请求任务，返回false.
     * 若该imageView目前无下载任务，则返回true.
     * </p>
     */
    private boolean cancelPotentialWork(String url, ImageView imageView) {
        BitmapWorkerTask task = getBitmapWorkerTask(imageView);
        if (task != null) {
            String imageUrl = task.imageUrl;
            //将正在请求的url和需要使用的图片的url进行对比.
            if (imageUrl == null || !imageUrl.equals(url)) {
                //url不一致的情况下，将任务取消
                task.cancel(true);
            } else {
                //url一致的情况下，任务正常进行
                return false;
            }
        }
        return true;
    }

    /**
     * 通过imageView得到imageView关联的task.
     * <p>
     * 假如imageView正在请求图片，那么imageView的drawable将会是一个带有空白图片的AsyncDrawable，
     * 然后间接得到iamgeView关联的task,返回其关联的实时的task
     * 假如imageView没有在请求图片，即imageView内部的图片是BitmapDrawable，那么此时该imageView没有
     * 相关的task,返回null.
     * </p>
     */
    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();//得到imageView当前的drawable
            if (drawable instanceof AsyncDrawable) {//此时正在执行图片请求的任务，有相关联的任务并返回
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        //没有相关联的任务，imageView没有在执行请求任务，返回null.
        return null;
    }

    /**
     * imageView关联task的媒介.
     * <p>
     *     当imageView正在等待task请求图片的过程中，imageView设定的drawable是这一个，内部实际上是一个空白图片.
     *     当imageView请求图片结束后，内部设定的drawable将不是这个，而是另外的一个BitmapDrawable。
     *     请求图片的过程中,imageView可以通过该AsyncDrawable得到实时的关联的task.
     * </p>
     */
    static class AsyncDrawable extends BitmapDrawable {
        //task的弱引用
        private WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskWeakReference = new WeakReference<>(bitmapWorkerTask);
        }

        //得到task的引用
        BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskWeakReference.get();
        }
    }
}
