package com.fjut.oj.controller;

import com.fjut.oj.interceptor.CheckUserIsLogin;
import com.fjut.oj.interceptor.CheckUserPrivate;
import com.fjut.oj.judge.util.Vjudge.Submitter;
import com.fjut.oj.judge.util.Vjudge.SubmitterImp;
import com.fjut.oj.localjudge.LocalJudgeHttpClient;
import com.fjut.oj.pojo.*;
import com.fjut.oj.pojo.enums.Result;
import com.fjut.oj.service.*;
import com.fjut.oj.util.ResultString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO: 把 JsonMsg 替换为 JsonInfoVO
 *
 * @author cjt
 */
@RestController
@RequestMapping("/submit")
@CrossOrigin
public class SubmitController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserSolveService userSolveService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserService userService;

    @Autowired
    private CeinfoService ceinfoService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private LocalJudgeHttpClient localJudgeHttp;

    /** 线程池 */
    @Autowired
    private ThreadPoolTaskExecutor executor;


    private Submitter sm = new SubmitterImp();

    @PostMapping("/submitProblem")
    @CheckUserIsLogin
    public JsonInfoVO submitProblem(HttpServletRequest req) {
        String strpid = req.getParameter("pid");
        if (strpid == null || strpid == "") {
            return new JsonInfoVO("FAIL", "pid未传入");
        }

        String user = req.getParameter("username");
        if (user == null || user == "") {
            return new JsonInfoVO("FAIL", "user未传入");
        }

        String code = req.getParameter("code");
        if (code == null || code == "") {
            return new JsonInfoVO("FAIL", "code未传入");
        }

        Integer pid = Integer.parseInt(strpid);
        Integer cid = Integer.parseInt(req.getParameter("cid") == null ? "-1" : req.getParameter("cid"));

        if (cid != -1) {
            ContestPO contestPO = contestService.queryContestByCid(cid);
            if (contestPO == null) {
                return new JsonInfoVO("FAIL", "没有查找到该比赛");
            }
            Date endTime = contestPO.getEndTime();

            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);

            if (endTime.compareTo(currentTime) < 0) {
                return new JsonInfoVO("FAIL", "比赛已经结束，提交失败");
            }

            Integer userNum = contestService.getContestUser(cid, user);
            if (userNum == 0) {
                // 用户之前没有提交过题目，添加该用户
                Contestuser contestuser = new Contestuser();
                contestuser.setTime(dateString);
                contestuser.setCid(cid);
                contestuser.setUsername(user);
                contestuser.setInfo("");
                contestuser.setStatu(1);
                contestService.insertContestuser(contestuser);
            }
        }

        String language = req.getParameter("language") == null ? "G++" : req.getParameter("language");
        Integer langid = ResultString.getSubmitLanguage(language);
        System.out.println(pid + " " + cid + " " + language + " " + langid + user + code);

        Timestamp submittime = new Timestamp(System.currentTimeMillis());
        Integer maxpid = statusService.queryMaxStatusId();
        Integer newpid = maxpid == null ? 1 : maxpid + 1;
        sm.doSubmit(user, pid, cid, langid, code, submittime);
        problemService.updateProblemtotalSubmit(pid);
        UserSolve userSolve = userSolveService.queryByUsernameAndPid(user, pid);
        if (userSolve == null) {
            // 该用户没有交过这道题目
            problemService.updateProblemtotalSubmitUser(pid);

        }
        return new JsonInfoVO("SUCCESS", "");
    }


    /**
     * FIXME: 没有设置事务，方法内代码过长，需要重构
     *
     * @author axiang [20190815] 提交到本地
     */
    @CheckUserPrivate
    @PostMapping("/submitProblemToLocal")
    public JsonInfoVO submitProblemToLocalJudge(@RequestParam("pid") String pidStr,
                                                @RequestParam("timeLimit") String timeLimitStr,
                                                @RequestParam("memoryLimit") String MemoryLimitStr,
                                                @RequestParam("code") String code,
                                                @RequestParam("language") String language,
                                                @RequestParam("username") String username,
                                                @RequestParam(value = "cid", required = false) String cidStr) throws InterruptedException {
        JsonInfoVO jsonInfoVO = new JsonInfoVO();
        Integer cid = -1;
        if (null != cidStr && !"".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        System.out.println("cidStr: "+cidStr);
        System.out.println("cid: "+cid);
        if (cid != -1) {
            ContestPO contestPO = contestService.queryContestByCid(cid);
            if (contestPO == null) {
                return new JsonInfoVO("FAIL", "没有查找到该比赛");
            }
            Date endTime = contestPO.getEndTime();
            Date currentTime = new Date();
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
            if (endTime.compareTo(currentTime) < 0) {
                return new JsonInfoVO("FAIL", "比赛已经结束，提交失败");
            }
            Integer userNum = contestService.getContestUser(cid, username);
            if (userNum == 0) {
                // 用户之前没有提交过题目，添加该用户
                Contestuser contestuser = new Contestuser();
                contestuser.setTime(dateString);
                contestuser.setCid(cid);
                contestuser.setUsername(username);
                contestuser.setInfo("");
                contestuser.setStatu(1);
                contestService.insertContestuser(contestuser);
            }
        }
        String type = "submit";
        Integer pid = Integer.parseInt(pidStr);
        Integer maxRid = statusService.queryMaxStatusId();
        Integer rid = maxRid == null ? 1 : maxRid + 1;
        Integer timeLimit = Integer.parseInt(timeLimitStr);
        Integer MemoryLimit = Integer.parseInt(MemoryLimitStr);

        LocalJudgeSubmitInfoPO localJudgeSubmitInfoBO = new LocalJudgeSubmitInfoPO();
        localJudgeSubmitInfoBO.setType(type);
        localJudgeSubmitInfoBO.setPid(pid);
        localJudgeSubmitInfoBO.setRid(rid);
        localJudgeSubmitInfoBO.setMemoryLimit(MemoryLimit);
        localJudgeSubmitInfoBO.setTimeLimit(timeLimit);
        localJudgeSubmitInfoBO.setCode(code);
        // 目前本地评测机只支持三种语言 JAVA Python2 C/C++
        localJudgeSubmitInfoBO.setLanguageId(("JAVA").equalsIgnoreCase(language) ? 2 : ("Python").equalsIgnoreCase(language) ? 3 : 1);
        String submitJsonStr = localJudgeHttp.submitToLocalJudge(localJudgeSubmitInfoBO);
        JSONObject jsonObject = JSONObject.fromObject(submitJsonStr);

        Status beforeStatus = new Status();
        // 如果提交到本地评测机成功，则插入数据库
        if ("success".equals(jsonObject.getString("ret"))) {
            Integer langId = ResultString.getSubmitLanguage(language);
            beforeStatus.setId(rid);
            beforeStatus.setRuser(username);
            beforeStatus.setPid(pid);
            beforeStatus.setCid(cid);
            beforeStatus.setLang(langId);
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            beforeStatus.setSubmitTime(formatter.format(currentTime));
            beforeStatus.setResult(0);
            beforeStatus.setScore(-1);
            beforeStatus.setTimeUsed("-");
            beforeStatus.setMemoryUsed("-");
            beforeStatus.setCode(code);
            beforeStatus.setCodelen(code.length());
            statusService.insertStatus(beforeStatus);
            //题目号为pid的题目解决总数+1
            problemService.updateProblemtotalSubmit(pid);
            // 查询用户是否提交过这道题
            UserSolve userSolve = userSolveService.queryByUsernameAndPid(username, pid);
            if (userSolve == null) {
                // 该用户没有交过这道题目
                problemService.updateProblemtotalSubmitUser(pid);
            }
            jsonInfoVO.setSuccess("代码提交成功！");
        } else {
            return new JsonInfoVO("FAIL", "评测机访问失败！");
        }

        final Integer finalRid = beforeStatus.getId();
        final Integer finalPid = beforeStatus.getPid();
        final String finalUsername = beforeStatus.getRuser();
        // 提交成功后，开启另外的线程获取结果
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                try {
                    getResultFromLocalJudgeSystem(finalRid, finalPid, finalUsername);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return jsonInfoVO;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void getResultFromLocalJudgeSystem(Integer rid, Integer pid, String username) throws InterruptedException {
        Status status = new Status();
        status.setId(rid);
        status.setPid(pid);
        status.setRuser(username);
        // 评测结果
        String judgingStatu = "judging";
        boolean quitLoop = false;
        int times = 100;
        // TODO: 可以根据活跃线程数executor.getActiveCount()来设置获取时间间隔times
        do {
            String getResultJsonStr = localJudgeHttp.getResultFromLocalJudge(rid);
            JSONObject jsonObject = JSONObject.fromObject(getResultJsonStr);
            if ("success".equals(jsonObject.getString("ret"))) {
                JSONObject resultJsonObj = JSONObject.fromObject(jsonObject.getString("result"));
                judgingStatu = resultJsonObj.getString("type");
                if ("padding".equals(judgingStatu)) {
                    status.setResult(Result.PENDING.getValue());
                    status.setTimeUsed("-");
                    status.setMemoryUsed("-");
                    statusService.updateStatusAfterJudge(status);
                } else if ("judging".equals(judgingStatu)) {
                    status.setResult(Result.JUDGING.getValue());
                    status.setTimeUsed("-");
                    status.setMemoryUsed("-");
                    statusService.updateStatusAfterJudge(status);
                } else if ("CE".equals(judgingStatu)) {
                    // 插入数据库内容，并设置 ceinfo 为 resultJsonObj.getString("info")
                    CeInfoPO ceinfo = new CeInfoPO();
                    ceinfo.setRid(status.getId());
                    ceinfo.setInfo(resultJsonObj.getString("info"));
                    ceinfoService.insertCeinfo(ceinfo);
                    status.setResult(Result.CE.getValue());
                    status.setTimeUsed("-");
                    status.setMemoryUsed("-");
                    statusService.updateStatusAfterJudge(status);
                    quitLoop = true;
                }
                //以下为编译正确返回的内容，即提交并且编译成功得到的结果，但不一定为AC
                else {
                    JSONArray retJsonArr = resultJsonObj.getJSONArray("ret");
                    // TODO: 测试中评测机返回多组不同IO的评测记录
                    judgingStatu = handleLocalJudgeReturns(retJsonArr, status);
                    quitLoop = true;
                }
            } else {
                quitLoop = true;
            }
            Thread.sleep(2000);
            times--;
        } while (times > 0 && !quitLoop);

        // 200s的获取结果执行完毕或者拿到AC/CE编译的结果后执行
        // 先拿到用户的所有AC题目记录
        UserSolve userSolve = userSolveService.queryACProblem(status.getRuser(), status.getPid());
        if ("AC".equalsIgnoreCase(judgingStatu)) {
            // 题目 AC 数量加一
            problemService.updateProblemTotalAc(status.getPid());
            if (userSolve == null) {
                // 用户写题数量 + 1
                userService.addAcnum(status.getRuser());
                // 用户之前未 AC 过,AC用户数目加一
                problemService.updateProblemtotalAcUser(status.getPid());
                // 用户之前未尝试过现在解决了
                userSolveService.replaceUserSolve(status.getRuser(), status.getPid(), 1);
            }
            // 对挑战模式的更新逻辑
            challengeService.updateOpenBlock(username,pid);
        } else {
            // 用户尝试过该题目，但没有解决
            if (userSolve == null) {
                userSolveService.replaceUserSolve(status.getRuser(), status.getPid(), 0);
            }
        }

    }

    @Transactional(rollbackFor = RuntimeException.class)
    public String handleLocalJudgeReturns(JSONArray retJsonArr, Status status) {
        CeInfoPO ceinfo = new CeInfoPO();
        String ans;
        String ceStr = "";
        String resStatu = "";
        int time = 0;
        int memory = 0;
        boolean isScore = false;
        boolean isAllScore = true;
        for (int i = 0; i < retJsonArr.size(); i++) {
            resStatu = retJsonArr.getJSONArray(i).getString(1);
            ceStr += ("测试结果：【" + resStatu + "】 ");
            ceStr += ("测试文件：【" + retJsonArr.getJSONArray(i).getString(0) + "】 ");
            if (resStatu.equals("SC")) {
                int score = retJsonArr.getJSONArray(i).getInt(5);
                ceStr += ("得分：【" + score + "】 ");
                isScore = true;
                if (100 != score) {
                    isAllScore = false;
                }
                time += retJsonArr.getJSONArray(i).getInt(2);
                ceStr += ("用时：【" + retJsonArr.getJSONArray(i).getInt(2) + "MS】 ");
            } else if (resStatu.equals("MLE") || resStatu.equals("OLE")) {
                time += retJsonArr.getJSONArray(i).getInt(4);
                ceStr += ("用时：【" + retJsonArr.getJSONArray(i).getInt(4) + "MS】 ");
            } else {
                time += retJsonArr.getJSONArray(i).getInt(2);
                ceStr += ("用时：【" + retJsonArr.getJSONArray(i).getInt(2) + "MS】 ");
            }
            memory = Math.max(memory, retJsonArr.getJSONArray(i).getInt(3));
            ceStr += ("内存：【" + retJsonArr.getJSONArray(i).getInt(3) + "KB】\n");
            if (!"AC".equals(resStatu) && !"SC".equals(resStatu)) {
                isAllScore = false;
                break;
            }
        }
        ceinfo.setInfo(ceStr);
        ceinfo.setRid(status.getId());
        ceinfoService.insertCeinfo(ceinfo);
        if (isScore && isAllScore) {
            status.setResult(Result.valueOf("AC").getValue());
            ans = "AC";
        } else {
            status.setResult(Result.valueOf(resStatu).getValue());
            ans = resStatu;
        }
        status.setTimeUsed(time + "MS");
        status.setMemoryUsed(memory + "KB");
        statusService.updateStatusAfterJudge(status);
        return ans;
    }

}
