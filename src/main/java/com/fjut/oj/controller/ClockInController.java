package com.fjut.oj.controller;

import com.fjut.oj.pojo.t_clock_in;
import com.fjut.oj.service.ClockInService;
import com.fjut.oj.util.IPTool;
import com.fjut.oj.util.JsonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author axiang [20190704]
 */
@Controller
@CrossOrigin
@RequestMapping("/clockin")
@ResponseBody
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    @GetMapping("/getUserClockIn")
    public JsonInfo queryAllClockInByUsername(@RequestParam(value = "username") String username, @RequestParam(value = "pagenum", required = false) String pageNumStr) {
        JsonInfo jsonInfo = new JsonInfo();
        Integer pageNum;
        if (null == pageNumStr) {
            pageNum = null;
        } else {
            pageNum = Integer.parseInt(pageNumStr);
        }
        List<t_clock_in> clockIns = clockInService.queryAllClockInByUsername(username, pageNum);
        if (null != clockIns) {
            jsonInfo.setSuccess();
            jsonInfo.addInfo(clockIns);
        } else {
            jsonInfo.setFail("未查询到签到记录！");
        }
        return jsonInfo;

    }

    @GetMapping("/getSomedayClockIn")
    public JsonInfo queryAllClockInByDate(@RequestParam("date") String dateStr) throws ParseException {
        JsonInfo jsonInfo = new JsonInfo();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        List<t_clock_in> clockIns = clockInService.queryAllClockInByDate(date);
        if (null != clockIns) {
            jsonInfo.setSuccess();
            jsonInfo.addInfo(clockIns);
        } else {
            jsonInfo.setFail("未查询到签到记录！");
        }
        return jsonInfo;
    }

    @GetMapping("/getUserTodayClockIn")
    public JsonInfo queryClockInByUserAndDate(@RequestParam("username") String username) {
        JsonInfo jsonInfo = new JsonInfo();
        Date date = new Date();
        List<t_clock_in> clockIns = clockInService.queryClockInByUsernameAndDate(username, date);
        if (0 != clockIns.size()) {
            jsonInfo.setSuccess();
            jsonInfo.addInfo(clockIns);
        } else {
            jsonInfo.setFail("未查询到签到记录！");
        }
        return jsonInfo;
    }

    @PostMapping("/setUserClockIn")
    public JsonInfo setClockInForNormalUser(HttpServletRequest req, @RequestParam("username") String username) {
        JsonInfo jsonInfo = new JsonInfo();
        IPTool ipTool = new IPTool();
        Date time = new Date();
        String sign = "日常";
        String ip = ipTool.getClientIpAddress(req);
        Integer todytimes = 1;
        t_clock_in clockIn = new t_clock_in();
        clockIn.setUsername(username);
        clockIn.setTime(time);
        clockIn.setSign(sign);
        clockIn.setIp(ip);
        clockIn.setTodytimes(todytimes);
        boolean isClockIn = clockInService.insertClockIn(clockIn);
        if (isClockIn) {
            jsonInfo.setSuccess("用户签到成功！");
        } else {
            jsonInfo.setFail("用户签到失败！");
        }
        return jsonInfo;
    }


}
