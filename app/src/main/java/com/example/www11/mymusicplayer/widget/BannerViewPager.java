package com.example.www11.mymusicplayer.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 实现轮播图，自定义控件.
 * <p>
 * 通过无线循环实现。
 * 假如展示三张图片V1 V2 V3，那么程序中的图片实际上是V3 V1 V2 V3 V1。
 * 暂时轮播项目中的资源文件.
 * </p>
 */
public class BannerViewPager extends FrameLayout {
    private ViewPager mViewPager;
    private LinearLayout mIndicatorGroup;//展示 小圆点的线性布局
    private List<Integer> mImageUrls;//存放图片的Url，是“实际图片”的数量
    private List<View> mViews;//存放要展示的图片，"实际图片"的数量。
    private ImageView[] mTips;//存放 小圆点的数组
    private int mCount;//用户看到的图片数量
    private int mBannerTime = 1500;//轮播图的间隔时间，即1.5s.
    private int mCurrentItem = 0;//表示 轮播图的当前选中项(从0开始).
    private long mSlideTime = 0;//保存 手 滑动时的时间。下面进行判断，防止手滑动后又立即轮播
    private final int START = 10;
    private final int STOP = 20;
    private Context mContext;
    private Handler mHandler;

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mImageUrls = new ArrayList<>();
        mViews = new ArrayList<>();
        init(context, attrs);
    }

    /**
     * 加载“轮播图”布局、初始化控件、handler消息机制.
     */
    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_view_pager, this);
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicatorGroup = findViewById(R.id.indicator);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    //下一张图片
                    case START:
                        mViewPager.setCurrentItem(mCurrentItem + 1);
                        mHandler.removeCallbacks(runnable);
                        mHandler.postDelayed(runnable, mBannerTime);//在bannerTime后会调用runnable的run()
                        break;
                    //不切换下一张图片
                    case STOP:
                        mSlideTime = 0;
                        mHandler.removeCallbacks(runnable);
                        mHandler.postDelayed(runnable, mBannerTime);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 主要是向handler发送消息，控制轮播图是否立即放下一张图片.
     */
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();//记录现在的时间
            if (now - mSlideTime > mBannerTime - 500) {
                //手动滑动，且时间满足该条件
                mHandler.sendEmptyMessage(START);
            } else {
                mHandler.sendEmptyMessage(STOP);
            }
        }
    };

    /**
     * 初始化imageUrls的资源。
     */
    public void setData(List<Integer> imageUrls) {
        this.mImageUrls.clear();
        this.mCount = imageUrls.size();//存放用户“看到”的图片数量.
        //“轮播图”里存放的数量 = 用户看到的+2
        this.mImageUrls.add(imageUrls.get(mCount - 1));
        this.mImageUrls.addAll(imageUrls);
        this.mImageUrls.add(imageUrls.get(0));

        initIndicator();
        getShowImage();
        setUI();
    }

    /**
     * 设置“小圆点”指示器在线性布局中的参数
     */
    private void initIndicator() {
        mTips = new ImageView[mCount];
        //设置小圆点在
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutParams.height = 20;
        layoutParams.width = 20;
        layoutParams.leftMargin = 10;//点的左边距
        layoutParams.rightMargin = 10;//点的右边距
        for (int i = 0; i < mCount; i++) {
            ImageView imageView = new ImageView(mContext);
            //先设置第一个小圆点为红色，其他圈为黑色
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.red_circle);
            } else {
                imageView.setBackgroundResource(R.drawable.black_circle);
            }

            mTips[i] = imageView;
            mIndicatorGroup.addView(imageView, layoutParams);
        }
    }

    /**
     * 加载图片，存放到views容器里
     */
    private void getShowImage() {
        for (int i = 0; i < mImageUrls.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mImageUrls.get(i));

            mViews.add(imageView);
        }
    }

    /**
     * 设置UI.
     * <p>
     * 为ViewPager加适配器，监听器。设置初始的图片，handle消息机制.
     * </p>
     */
    private void setUI() {
        BannerAdapter adapter = new BannerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mViewPager.setCurrentItem(1);
        mHandler.postDelayed(runnable, mBannerTime);
    }

    /**
     * 监听ViewPager的pager的改变
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //主要是为了设置小圆点的下标
        @Override
        public void onPageSelected(int position) {
            int max = mViews.size();
            int temp = position;//当前pager的下标
            mCurrentItem = position;
            if (position == 0) {
                //选择到最左边的pager时
                mCurrentItem = max - 1;
            } else if (position == max) {
                //选择到最右边的pager时
                mCurrentItem = 1;
            }
            //小圆点的下标
            temp = mCurrentItem - 1;
            setIndicator(temp);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mCurrentItem = mViewPager.getCurrentItem();
            switch (state) {
                //自然滑动时
                case ViewPager.SCROLL_STATE_IDLE:
                    if (mCurrentItem == 0) {
                        //滑动到最左边的那张时,重新设置图片
                        mViewPager.setCurrentItem(mCount, false);
                    } else if (mCurrentItem == mCount + 1) {
                        //滑动到最右边的那张时,重新设置图片
                        mViewPager.setCurrentItem(1, false);
                    }
                    break;
                //用户用手去滑动时
                case ViewPager.SCROLL_STATE_DRAGGING:
                    //记录用户用手开始拖拽时的时间
                    mSlideTime = System.currentTimeMillis();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 负责小圆点的切换.
     * <p>
     * 把当前正显示的图片的小圆点变红色.
     * </p>
     */
    private void setIndicator(int position) {
        for (int i = 0; i < mTips.length; i++) {
            if (i == position) {
                mTips[i].setBackgroundResource(R.drawable.red_circle);
            } else {
                mTips[i].setBackgroundResource(R.drawable.black_circle);
            }
        }
    }

    /**
     * ViewPager适配器.
     * */
    class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
