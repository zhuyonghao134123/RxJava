package cn.zyh.rxjavademot;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zyh.rxjavademot.base.BaseFragment;
import cn.zyh.rxjavademot.module.cache_6.CacheFragment;
import cn.zyh.rxjavademot.module.elementary_1.ElementaryFragment;
import cn.zyh.rxjavademot.module.map_2.MapFragment;
import cn.zyh.rxjavademot.module.token_4.TokenFragment;
import cn.zyh.rxjavademot.module.token_advanced_5.TokenAdvancedFragment;
import cn.zyh.rxjavademot.module.zip_3.ZipFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tooBar)
    Toolbar tooBar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private List<BaseFragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //支持tooBar
        setSupportActionBar(tooBar);
        //viewPager设置数据适配器
        fragmentList.add(new ElementaryFragment());
        fragmentList.add(new MapFragment());
        fragmentList.add(new ZipFragment());
        fragmentList.add(new TokenFragment());
        fragmentList.add(new TokenAdvancedFragment());
        fragmentList.add(new CacheFragment());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                BaseFragment currentFragment = fragmentList.get(position);
                return currentFragment;
            }

            //页面数量
            public int getCount() {
                return fragmentList.size();
            }

            //页面标题
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "基本";
                    case 1:
                        return "转换Map";
                    case 2:
                        return "压合ZIP";
                    case 3:
                        return "token-flatMap";
                    case 4:
                        return "token-retryWhen-";
                    case 5:
                        return "cache";
                    default:
                        return "基本";
                }
            }
        });
        //为tabLayout设置viewpager
        tabs.setupWithViewPager(viewPager);
    }
}
