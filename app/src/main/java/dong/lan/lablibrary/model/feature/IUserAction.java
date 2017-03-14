package dong.lan.lablibrary.model.feature;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dong.lan.lablibrary.model.LUser;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午4:43.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public interface IUserAction {

    void login(FindListener<LUser> findListener);

    void register(SaveListener<String> saveListener);
}
