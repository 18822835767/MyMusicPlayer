package view;

import androidx.appcompat.app.AppCompatActivity;
import entity.User;
import widget.BannerViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐首页所对应的view.
 * */
public class HomePageActivity extends AppCompatActivity {
    private User user;//记录登陆的用户
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        init();
    }
    
    private void init(){
        //为轮播图设置数据
        BannerViewPager banner= (BannerViewPager) findViewById(R.id.banner_view_pager);
        List<Integer> imageUrl=new ArrayList<>();
        imageUrl.add(R.drawable.one);
        imageUrl.add(R.drawable.two);
        imageUrl.add(R.drawable.thr);
        banner.setData(imageUrl);

        //得到登陆的用户
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
    }
}
