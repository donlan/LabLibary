package dong.lan.lablibrary.ui.base;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梁桂栋 on 17-1-18 ： 下午9:52.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class BaseFragment extends Fragment implements BaseView {

    protected final static String KEY_TITTLE = "tittle";

    protected View content;
    private Unbinder unbinder;


    @Override
    public void show(String text) {
        if (getView() != null)
            Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void bindView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        content = null;
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
    }
}
