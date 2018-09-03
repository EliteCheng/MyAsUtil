package elite.cheng.myasutil.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elite.cheng.myasutil.R;
import elite.cheng.myasutil.fragment.BaseFragment;
import elite.cheng.myasutil.view.MyButton;

/**
 * Created by TC on 2018/8/8.
 */
public class ScorollButtonFragment extends BaseFragment {
    private MyButton mBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoroll_button, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {
        mBtn = (MyButton) v.findViewById(R.id.ScorollToBtn);
    }

    private void initData() {

    }
}
