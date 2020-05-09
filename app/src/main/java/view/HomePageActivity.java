package view;

import androidx.appcompat.app.AppCompatActivity;
import widget.BannerViewPager;

import android.os.Bundle;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐首页所对应的view.
 * */
public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        BannerViewPager banner= (BannerViewPager) findViewById(R.id.banner_view_pager);
        List<Integer> imageUrl=new ArrayList<>();
        imageUrl.add(R.drawable.one);
        imageUrl.add(R.drawable.two);
        imageUrl.add(R.drawable.thr);
        banner.setData(imageUrl);
    }
}
