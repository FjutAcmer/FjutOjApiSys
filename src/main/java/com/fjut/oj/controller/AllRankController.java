package com.fjut.oj.controller;

import com.fjut.oj.pojo.User;
import com.fjut.oj.service.AllTopTenService;
import com.fjut.oj.util.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AllRankController {

    @Autowired
    private AllTopTenService allTopTenService;

    @PostMapping(value = "/Galltop",produces="application/json")
    @ResponseBody
    public JsonMsg getAllTop(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin","*");
        List<User> list = allTopTenService.getAcbTOP();
        List<User> list_1 = allTopTenService.getRatingTOP();
        List<User> list_2 = allTopTenService.getAcTOP();
//        List<User> list_3 = allTopTenService.getActiveTop();
//        if(list_3.size()>=10){
//            list_3 = list_3.subList(0,9);
//        }
        return JsonMsg.success().addInfo(list).addInfo(list_1).addInfo(list_2);
    }
}