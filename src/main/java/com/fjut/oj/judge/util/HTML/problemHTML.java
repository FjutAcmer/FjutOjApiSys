package com.fjut.oj.judge.util.HTML;

import java.util.ArrayList;
import java.util.List;

public class problemHTML {
    private String Title;
    private String Dis;
    private String Input;
    private String Output;
    private List<String> SampleInput;
    private List<String> SampleOutput;
    private String TimeLimit;
    private String MenoryLimit;
    private String Int64;
    private boolean spj;
    private boolean admin=false;
    private int pid;
    private boolean isInContest = false;

    public problemHTML(){
        Dis="";
        Input="";
        Output="";
        TimeLimit="";
        MenoryLimit="";
        Int64="";
        spj=false;
        SampleInput=new ArrayList<String>();
        SampleOutput=new ArrayList<String>();
    }

    public problemHTML(int pid){
        Dis="";
        Input="";
        Output="";
        TimeLimit="";
        MenoryLimit="";
        Int64="";
        spj=false;
        SampleInput=new ArrayList<String>();
        SampleOutput=new ArrayList<String>();
        this.pid=pid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDis() {
        return Dis;
    }

    public void setDis(String dis) {
        Dis = dis;
    }

    public String getInput() {
        return Input;
    }

    public void setInput(String input) {
        Input = input;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public List<String> getSampleInput() {
        return SampleInput;
    }

    public void setSampleInput(List<String> sampleInput) {
        SampleInput = sampleInput;
    }

    public List<String> getSampleOutput() {
        return SampleOutput;
    }

    public void setSampleOutput(List<String> sampleOutput) {
        SampleOutput = sampleOutput;
    }

    public String getTimeLimit() {
        return TimeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        TimeLimit = timeLimit;
    }

    public String getMenoryLimit() {
        return MenoryLimit;
    }

    public void setMenoryLimit(String menoryLimit) {
        MenoryLimit = menoryLimit;
    }

    public String getInt64() {
        return Int64;
    }

    public void setInt64(String int64) {
        Int64 = int64;
    }

    public boolean isSpj() {
        return spj;
    }

    public void setSpj(boolean spj) {
        this.spj = spj;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isInContest() {
        return isInContest;
    }

    public void setInContest(boolean inContest) {
        isInContest = inContest;
    }

    public void addSample(String sampleinput,String sampleoutput){
        SampleInput.add(sampleinput);
        SampleOutput.add(sampleoutput);
    }

    private String getTitleHTML(){
        return "<h1 style='text-align:center'>"+Title+"</h1>";
    }

    private String getLimitHTML(){
        String s="";
        s+="<div class='row'>" +
                "<div style='text-align:center'>" +
                "TimeLimit:"+TimeLimit+"&nbsp;&nbsp;MemoryLimit:"+MenoryLimit+
                "</div>"+
                "</div>";
        return s;
    }

    private String getInt64HTML(){
        String s="";
        s+="<div class='row'>" +
                "<div style='text-align:center'>" +
                "64-bit integer IO format:<span class='badge'>" + Int64 +"</span>"+
                "</div>"+
                "</div>";
        return s;
    }

    private String getSpjHTML(){
        String s="";
        if(spj){
            s+="<div class='row'>" +
                    "<center>" +
                    //HTML.text("Special Judge","red") +
                    "</center>"+
                    "</div>";
        }
        return s;
    }

    private String edit(String href,String s){
        return "<a class=\"btn btn-default btn-sm\" style=\"\n" +
                "    margin-top: -16px;\n" +
                "    margin-right: -16px;\n" +
                "    float: right;\n" +
                "    border-radius: 0px 0px 0px 6px;\n" +
                "\" href='"+href+"'>"+s+"</a>";
    }

    public int getTime(){
        try {
            return Integer.parseInt(TimeLimit.substring(0, TimeLimit.length() - 2));
        }catch (Exception e){
            return 1000;
        }
    }

    public int getMemory(){
        try {
            return Integer.parseInt(MenoryLimit.substring(0,MenoryLimit.length()-2));
        }catch (Exception e){
            return 128;
        }
    }

}
