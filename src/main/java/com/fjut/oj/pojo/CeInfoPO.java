package com.fjut.oj.pojo;

/**
 * @author axiang
 */
public class CeInfoPO {
    Integer rid;
    String info;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CeInfoPO{" +
                "rid=" + rid +
                ", info='" + info + '\'' +
                '}';
    }
}
