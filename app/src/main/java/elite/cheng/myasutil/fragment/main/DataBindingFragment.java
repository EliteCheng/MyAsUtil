package elite.cheng.myasutil.fragment.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elite.cheng.myasutil.R;
import elite.cheng.myasutil.fragment.BaseFragment;
import elite.cheng.myasutil.databinding.FragmentDataBindingBinding;
import elite.cheng.myasutil.model.UserInfo;

/**
 * Created by TC on 2018/8/8.
 */
public class DataBindingFragment extends BaseFragment {

    private FragmentDataBindingBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_data_binding, container, false);
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_data_binding,container,false);
        View view = mBinding.getRoot();
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {

    }

    private void initData() {
        UserInfo test = new UserInfo();
        test.setName("testDataBinding");
        mBinding.setUser(test);
    }
}
