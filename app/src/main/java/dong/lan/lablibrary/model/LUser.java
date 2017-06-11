package dong.lan.lablibrary.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dong.lan.lablibrary.model.feature.IUserAction;
import dong.lan.lablibrary.utils.Secure;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午4:35.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class LUser extends BmobObject implements IUserAction{


    private String username; //用户名
    private String password; //密码(md5加密保存)

    public LUser() {
    }

    public LUser(String username, String password) {
        this.username = username;
        this.password = Secure.MD5(password);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void login(FindListener<LUser> findListener) {
        BmobQuery<LUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.addWhereEqualTo("password", password);
        query.findObjects(findListener);
    }

    @Override
    public void register(SaveListener<String> saveListener) {
        save(saveListener);
    }
}
