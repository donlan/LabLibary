package dong.lan.lablibrary.model;

import android.util.Log;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午4:40.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 资产操作记录
 */

public class Record extends BmobObject {
    public static int in = 0;
    public static int out =1;

    private LUser user; //操作用户
    private Integer status; //状态 :入库0  出库1
    private Asset asset;  //对应资产
    private Integer count; //操作数量
    private Long createTime; //操作时间
    private String remark; //备注


    public Record(LUser user, Integer status, Asset asset, Integer count, Long createTime, String remark) {
        this.user = user;
        this.status = status;
        this.asset = asset;
        this.count = count;
        this.createTime = createTime;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public LUser getUser() {
        return user;
    }

    public void setUser(LUser user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    @Override
    public Subscription save() {
        save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.d("TAG", "done: "+s+","+e);
            }
        });
        return super.save();
    }
}
