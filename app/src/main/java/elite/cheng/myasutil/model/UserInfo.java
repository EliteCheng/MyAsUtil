package elite.cheng.myasutil.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import elite.cheng.myasutil.BR;

/**
 * Created by TC on 2018/8/8.
 */
public class UserInfo extends BaseObservable {

    private String name;
    private int age;

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
