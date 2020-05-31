package com.example.www11.mymusicplayer.util;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder工具类.
 */
public class ViewHolderTool {
    /**
     * 通过view和id得到控件.
     *
     * @param view 对应的是item的view
     * @param id   view中的控件的id
     * @return 所需要的控件
     */
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
