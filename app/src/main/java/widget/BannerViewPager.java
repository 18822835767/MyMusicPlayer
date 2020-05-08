package widget;

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
import java.util.jar.Attributes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 实现轮播图，自定义控件.
 * <p>
 * 通过无线循环实现。
 * 假如展示三张图片V1 V2 V3，那么程序中的图片实际上是V3 V1 V2 V3 V1。
 * </p>
 */
public class BannerViewPager extends FrameLayout {
    private ViewPager viewPager;
    private LinearLayout indicatorGroup;//展示 小圆点的线性布局
    private List<Integer> imageUrls;//存放图片的Url，是“实际图片”的数量
    private List<View> views;//存放要展示的图片，"实际图片"的数量。
    private ImageView[] tips;//存放 小圆点的数组
    private int count;//用户看到的图片数量
    private int bannerTime = 1500;//轮播图的间隔时间，即1.5s.
    private int currentItem = 0;//表示 轮播图的当前选中项(从0开始).
    private long slideTime = 0;//保存 手 滑动时的时间。下面进行判断，防止手滑动后又立即轮播
    private final int START = 10;
    private final int STOP = 20;
    private Context context;
    private Handler handler;

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        imageUrls = new ArrayList<>();
        views = new ArrayList<>();
        init(context, attrs);
    }

    /**
     * 加载“轮播图”布局、初始化控件、handler消息机制.
     */
    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_view_pager, this);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        indicatorGroup = (LinearLayout) findViewById(R.id.indicator);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case START:
                        viewPager.setCurrentItem(currentItem + 1);
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, bannerTime);//在bannerTime后会调用runnable的run()
                        break;
                    case STOP:
                        slideTime = 0;
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, bannerTime);
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
            if (now - slideTime > bannerTime - 500) {
                //手动滑动，且时间满足该条件
                handler.sendEmptyMessage(START);
            } else {
                handler.sendEmptyMessage(STOP);
            }
        }
    };

    /**
     * 初始化imageUrls的资源。
     */
    public void setData(List<Integer> imageUrls) {
        this.imageUrls.clear();
        this.count = imageUrls.size();//存放用户“看到”的图片数量.
        //“轮播图”里存放的数量 = 用户看到的+2
        this.imageUrls.add(imageUrls.get(count - 1));
        this.imageUrls.addAll(imageUrls);
        this.imageUrls.add(imageUrls.get(0));

        initIndicator();
        getShowImage();
        setUI();
    }

    /**
     * 设置“小圆点”指示器在线性布局中的参数
     */
    private void initIndicator() {
        tips = new ImageView[count];
        //设置小圆点在
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutParams.height = 10;
        layoutParams.width = 10;
        layoutParams.leftMargin = 5;//点的左边距
        layoutParams.rightMargin = 5;//点的右边距
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            //先设置第一个小圆点为红色，其他圈为黑色
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.red_circle);
            } else {
                imageView.setBackgroundResource(R.drawable.black_circle);
            }

            tips[i] = imageView;
            indicatorGroup.addView(imageView, layoutParams);
        }
    }

    /**
     * 加载图片，存放到views容器里
     */
    private void getShowImage() {
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageUrls.get(i));

            views.add(imageView);
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
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(1);
        handler.postDelayed(runnable, bannerTime);
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
            int max = views.size();
            int temp = position;//当前pager的下标
            currentItem = position;
            if (position == 0) {
                //选择到最左边的pager时
                currentItem = max - 1;
            } else if (position == max) {
                //选择到最右边的pager时
                currentItem = 1;
            }
            //小圆点的下标
            temp = currentItem - 1;
            setIndicator(temp);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            currentItem = viewPager.getCurrentItem();
            switch (state) {
                //自然滑动时
                case ViewPager.SCROLL_STATE_IDLE:
                    if(currentItem == 0){
                        //滑动到最左边的那张时,重新设置图片
                        viewPager.setCurrentItem(count,false);
                    }else if(currentItem == count + 1){
                        //滑动到最右边的那张时,重新设置图片
                        viewPager.setCurrentItem(1,false);
                    }
                    break;
                //用户用手去滑动时
                case ViewPager.SCROLL_STATE_DRAGGING:
                    //记录用户用手开始拖拽时的时间
                    slideTime = System.currentTimeMillis();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 负责小圆点的切换.
     * <p>
     *     把当前正显示的图片的小圆点变红色.
     * </p>
     * */
    private void setIndicator(int position) {
        for(int i=0;i< tips.length ;i++){
            if(i == position){
                tips[i].setBackgroundResource(R.drawable.red_circle);
            }else{
                tips[i].setBackgroundResource(R.drawable.black_circle);
            }
        }
    }

    class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }
}
