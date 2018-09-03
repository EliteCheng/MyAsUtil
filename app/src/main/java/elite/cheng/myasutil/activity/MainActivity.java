package elite.cheng.myasutil.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import elite.cheng.myasutil.R;
import elite.cheng.myasutil.fragment.main.DataBindingFragment;
import elite.cheng.myasutil.fragment.main.OverViewFragment;
import elite.cheng.myasutil.fragment.main.ScorollButtonFragment;
import elite.cheng.myasutil.util.ClickUtil;
import elite.cheng.myasutil.util.ToastUtil;
import elite.cheng.myasutil.view.TabItemView;

public class MainActivity extends BaseActivity {

    public TabLayout bottom_tablayout;
    List<TabItemView.ItemHolder> itemHolderList = new ArrayList<>();
    private FragmentTransaction transaction;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        initView();
        initData();
    }

    private void initView() {
        bottom_tablayout = (TabLayout) findViewById(R.id.bottom_tablayout);
    }

    private void initData() {
        setupFragment();
    }

    private void setupFragment() {
        /**
         * 初始化多组Item数据，供Tab使用：一个Tab Fragment和多个Blank Fragment
         */
        itemHolderList.add(new TabItemView.ItemHolder("Tag1", new OverViewFragment(),
                new TabItemView(context, "Home", 0, R.drawable.selector_wechat, 0)));
        itemHolderList.add(new TabItemView.ItemHolder("Tag2", new ScorollButtonFragment(),
                new TabItemView(context, "Btn", 0, R.drawable.selector_tab_overview, 0)));
        itemHolderList.add(new TabItemView.ItemHolder("Tag3", new DataBindingFragment(),
                new TabItemView(context, "MVVM", 0, R.drawable.selector_tab_remote_control, 0)));
        itemHolderList.add(new TabItemView.ItemHolder("Tag4", new DataBindingFragment(),
                new TabItemView(context, "Analysis", 0, R.drawable.selector_tab_stats, 0)));
        itemHolderList.add(new TabItemView.ItemHolder("Tag5", new DataBindingFragment(),
                new TabItemView(context, "Setting", 0, R.drawable.selector_tab_setting, 0)));
        /**
         * Tab 和 具体的数据ItemHolder 绑定
         */
        bottom_tablayout.addTab(bottom_tablayout.newTab().setCustomView(itemHolderList.get(0).tabItemView), 0, true);//默认第一个item被选中
        bottom_tablayout.addTab(bottom_tablayout.newTab().setCustomView(itemHolderList.get(1).tabItemView), 1, false);
        bottom_tablayout.addTab(bottom_tablayout.newTab().setCustomView(itemHolderList.get(2).tabItemView), 2, false);
        bottom_tablayout.addTab(bottom_tablayout.newTab().setCustomView(itemHolderList.get(3).tabItemView), 3, false);
        bottom_tablayout.addTab(bottom_tablayout.newTab().setCustomView(itemHolderList.get(4).tabItemView), 4, false);

        bottom_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private int needHidePosition;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabSelected:" + tab.getTag() + "\t" + tab.getPosition());
                changeFragment(tab.getPosition(), needHidePosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabUnselected:" + tab.getTag() + "\t" + tab.getPosition());
                needHidePosition = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabReselected:" + tab.getTag() + "\t" + tab.getPosition());
            }
        });

        //初始化第一个fragment
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.mainfrag, itemHolderList.get(0).fragment, itemHolderList.get(0).fragmentTag)
                .show(itemHolderList.get(0).fragment)
                .commit();
    }

    /**
     * fragment的切换
     *
     * @param showPosition 需要显示的Fragment
     * @param hidePosition 需要隐藏的Fragment
     */
    private void changeFragment(int showPosition, int hidePosition) {
        transaction = manager.beginTransaction();
        Fragment showFragment = manager.findFragmentByTag(itemHolderList.get(showPosition).fragmentTag);
        if (showFragment == null) {
            Log.e(TAG, "未找到fragment");
            transaction.add(R.id.mainfrag,
                    itemHolderList.get(showPosition).fragment,
                    itemHolderList.get(showPosition).fragmentTag);
        }
        transaction.hide((itemHolderList.get(hidePosition).fragment))
                .show((itemHolderList.get(showPosition).fragment))
                .commit();
    }


    /**
     * 退出Activity
     */
    @Override
    public void onBackPressed() {
        if (ClickUtil.isFastDoubleClick()) {
            moveTaskToBack(true);
        } else {
            ToastUtil.showShortToast(context, "再按一次程序返回后台运行");
        }
    }
}
