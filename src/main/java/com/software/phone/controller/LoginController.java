package com.software.phone.controller;

import com.software.phone.conf.CentreCutPageResponse;
import com.software.phone.conf.ResponseEntity;
import com.software.phone.dao.PhoneUser;
import com.software.phone.exception.MedicalException;
import com.software.phone.po.LoginPo;
import com.software.phone.po.LoginTokenPo;
import com.software.phone.po.SysUser;
import com.software.phone.service.LoginService;
import com.software.phone.service.PhoneUserService;
import com.software.phone.utils.AppConstantsUtil;
import com.software.phone.utils.HttpRequestUtil;
import com.software.phone.utils.JwtComponentUtil;
import com.software.phone.utils.RedisComponentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunow
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtComponentUtil jwtComponentUtil;

    @Autowired
    private RedisComponentUtil redisComponent;

    @Autowired
    private PhoneUserService phoneUserService;

    /**
     * 描述：用户点击获取验证码
     *
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(LoginPo loginPo) {
        log.info("用户获取验证码");
        if (loginService.sendMessage(loginPo).split(":")[1].split(",")[0].equals("1")) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }

    /**
     * 描述：用户手机验证码登录
     *
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/oauth", method = RequestMethod.POST)
    public ResponseEntity oauth(LoginPo loginPo) {
        log.info("用户携带验证码进行登录验证");
        if (loginService.oauth(loginPo)) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }

    /**
     * 描述：用户用户名、密码登录
     *
     * @param loginTokenPo
     * @return
     */
    @RequestMapping(value = "/tokenLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity tokenLogin(LoginTokenPo loginTokenPo, HttpServletRequest request) {

        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setPhone(loginTokenPo.getPhone());
        Integer errNum = (Integer) redisComponent.get("medicalLoginError" + HttpRequestUtil.getIp(request));
        errNum = errNum == null ? 0 : errNum;

        // 根据联系方式获取用户信息，验证用户名、密码是否正确
        if(phoneUserService.getUserByPhone(phoneUser).getPassword().equals(loginTokenPo.getPassword())) {
            String token = jwtComponentUtil.createToken(loginTokenPo);
            redisComponent.set("medicalLoginError" + HttpRequestUtil.getIp(request), null);
            if (token != "") {
                log.info("用户登录成功");
                return this.getSuccessResult("登录成功", token);
            }
        } else {
            if(errNum == null) {
                redisComponent.set("medicalLoginError" + HttpRequestUtil.getIp(request), 1);
            } else {
                redisComponent.set("medicalLoginError" + HttpRequestUtil.getIp(request), errNum + 1);
            }
        }

        // 判断登录失败次数，失败两次及以上，需要输入验证码
        if(errNum > AppConstantsUtil.ERR_NUM) {
            String chkCode = loginTokenPo.getChkCode();
            if (chkCode == null) {
                log.warn("用户未输入验证码");
                return this.getFailResult("请输入验证码");
            }
        }

        return this.getFailResult("登录失败");
    }

    /**
     * 新增用户
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity insert(LoginPo loginPo) {
        // 验证是否重名
        System.out.println("loginPo = " + loginPo);
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setPhone(loginPo.getPhone());
        if(phoneUserService.countRecord(phoneUser) > 0) {
            return this.getFailResult("手机号已经注册");
        }

        phoneUser.setPassword(loginPo.getPassword());
        phoneUser.setLoginDate(new Date());
        phoneUser.setRegisterDate(new Date());

        phoneUserService.saveRecord(phoneUser);
        return this.getSuccessResult();
    }

    /**
     * 查询列表
     * @param phoneUser
     * @return
     */
    @RequestMapping(value = "/selectList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity selectList(PhoneUser phoneUser) {
        return this.getSuccessResult(phoneUserService.selectListRecord(phoneUser));
    }

    /**
     * 分页查询
     * @param phoneUser
     * @return
     */
    @RequestMapping(value = "/selectPageList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<CentreCutPageResponse<PhoneUser>> selectPageList(PhoneUser phoneUser) {
        int count = phoneUserService.countRecord(phoneUser);
        phoneUser.setPageStart((phoneUser.getPageNum()-1) * phoneUser.getPageSize());
        List<PhoneUser> list = phoneUserService.selectPageListRecord(phoneUser);
        return this.getSuccessResult(getCutPageResponse(phoneUser.getPageNum(), phoneUser.getPageSize(), count, list));
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity test(@RequestHeader(value = "Authorization") String authorization) throws MedicalException {
        if (jwtComponentUtil.getClaims(authorization) == null) {
            return this.getFailResult();
        }
        return this.getSuccessResult("测试成功");
    }

    /**
     * 存储过程测试
     * @param user
     */
    @RequestMapping(value = "/procedure", method = {RequestMethod.POST, RequestMethod.GET})
    public void procedure(SysUser user) {
        System.out.println("user = " + user);
        phoneUserService.selectUserById(user);
        System.out.println("user = " + user);
    }

    /**
     * 存储过程实现的分页
     * @return
     */
    @RequestMapping(value = "/procedurePage", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<CentreCutPageResponse<SysUser>> procedurePage() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("userName", "ad");
        params.put("offset", 0);
        params.put("limit", 10);

        List<SysUser> list = phoneUserService.selectUserPage(params);
        System.out.println("总量 = " + params.get("total").toString());
        return this.getSuccessResult(getCutPageResponse(0, 10, (Long)params.get("total"), list));
    }
}
