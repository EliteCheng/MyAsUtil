package elite.cheng.myasutil.fragment.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elite.cheng.myasutil.R;
import elite.cheng.myasutil.fragment.BaseFragment;
import elite.cheng.myasutil.model.UserInfo;
import elite.cheng.myasutil.databinding.FragmentDataBindingBinding;

/**
 * Created by TC on 2018/8/8.
 */
public class DataBindingFragment extends BaseFragment {

    private FragmentDataBindingBinding mBinding;
    private Context mContext;

    private UserInfo test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_binding, container, false);
        View view = mBinding.getRoot();
        mContext = getContext();
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {

    }

    private void initData() {
        test = new UserInfo();
        test.setName("databindingTest");
        mBinding.setUser(test);
        mBinding.setIsVisable(true);
        mBinding.setUserNameUndefined("123456");
    }
}
