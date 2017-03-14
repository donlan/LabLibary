package dong.lan.lablibrary.adapter;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-11 ： 下午11:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public abstract class AbstractBinder<T> implements AdapterBinder<T> {

    protected boolean isMainData = true;
    protected List<T> data;
    protected List<T> cache;
    protected BinderClickListener<T> clickListener;
    protected BaseAdapter<T> adapter;

    @Override
    public void init(List<T> data) {
        this.data = data;
    }

    @Override
    public T valueAt(int position) {
        if (isMainData)
            return data.get(position);
        else
            return cache.get(position);
    }

    @Override
    public int size() {
        if (isMainData)
            return data == null ? 0 : data.size();
        else
            return cache == null ? 0 : cache.size();
    }

    @Override
    public BaseAdapter<T> build() {
        adapter = null;
        adapter = new BaseAdapter<>(this);
        return adapter;
    }


}
