package dong.lan.lablibrary.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午4:37.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 资产
 */

public class Asset extends BmobObject implements Serializable {

    private LUser user; //所属用户
    private String no; //编号
    private String name; //资产名称
    private Integer count; //数量
    private String remark; //备注信息
    private Long createTime; //创建时间
    private Long updateTime; //更新时间

    public Asset() {
    }

    public Asset(LUser user, String no, String name, Integer count, String remark, long createTime, long updateTime) {
        this.user = user;
        this.no = no;
        this.name = name;
        this.count = count;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Asset(String no, String name, Integer count, String remark) {
        this.no = no;
        this.name = name;
        this.count = count;
        this.remark = remark;
    }


    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public LUser getUser() {
        return user;
    }

    public void setUser(LUser user) {
        this.user = user;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
