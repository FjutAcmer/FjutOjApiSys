package com.fjut.oj.pojo;

/**
 * @Author: wyx
 * @Despriction:
 * @Date:Created in 11:42 2019/6/28
 * @Modify By:
 */
public class ChallengeBlockDetail {
    private Integer id;
    private String name;
    private String type;
    private String des;
    private Integer totalScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
