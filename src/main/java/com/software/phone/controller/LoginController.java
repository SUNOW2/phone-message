package com.software.phone.controller;

import com.software.phone.conf.CentreCutPageResponse;
import com.software.phone.conf.ResponseEntity;
import com.software.phone.dao.PhoneUser;
import com.software.phone.exception.MedicalException;
import com.software.phone.form.PhoneUserDeleteForm;
import com.software.phone.form.PhoneUserQueryForm;
import com.software.phone.form.PhoneUserUpdateForm;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author sunow
 * 注释：由于本项目使用了@RequestBody注解，所以使用postman测试的时候，要选择row模式，
 * Content-Type设置为"application/json"。
 * 前端界面上传数据时，务必设置Content-Type的值
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

    @Autowired
    private DiscoveryClient client;

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
    public ResponseEntity tokenLogin(@RequestBody LoginTokenPo loginTokenPo, HttpServletRequest request) {
        System.out.println("loginTokenPo = " + loginTokenPo);

        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setPhone(loginTokenPo.getPhone());
        Integer errNum = (Integer) redisComponent.get("medicalLoginError" + HttpRequestUtil.getIp(request));
        errNum = errNum == null ? 0 : errNum;

        // 根据联系方式获取用户信息，验证用户名、密码是否正确
        if(phoneUserService.getUserByPhone(phoneUser).getPassword().equals(loginTokenPo.getPassword())) {
            String token = jwtComponentUtil.createToken(loginTokenPo);
            redisComponent.set("medicalLoginError" + HttpRequestUtil.getIp(request), null);
            if (StringUtils.isNotEmpty(token)) {
                log.info("用户登录成功");
                phoneUser.setLoginDate(new Date());
                phoneUserService.updateRecord(phoneUser);
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
            if (StringUtils.isEmpty(chkCode)) {
                log.warn("用户未输入验证码");
                return this.getFailResult("请输入验证码");
            }
        }

        return this.getFailResult("登录失败");
    }

    /**
     * 描述：新增用户
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity insert(@RequestBody LoginPo loginPo) {
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

        /**
         * 将Date转换为LocalDateTime
         */
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("localDateTime = " + localDateTime);

        phoneUserService.saveRecord(phoneUser);
        return this.getSuccessResult();
    }

    /**
     * 描述：查询列表
     * @param phoneUser
     * @return
     */
    @RequestMapping(value = "/selectList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity selectList(PhoneUser phoneUser) {
        System.out.println("phoneUser = " + phoneUser);
        return this.getSuccessResult(phoneUserService.selectListRecord(phoneUser));
    }

    /**
     * 描述：分页查询
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

    /**
     * 描述：存储过程测试
     * @param user
     */
    @RequestMapping(value = "/procedure", method = {RequestMethod.POST, RequestMethod.GET})
    public void procedure(SysUser user) {
        System.out.println("user = " + user);
        phoneUserService.selectUserById(user);
        System.out.println("user = " + user);
    }

    /**
     * 描述：存储过程实现的分页
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

    /**
     * 批量删除
     * @param form
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Integer> deleteBatch(@RequestBody PhoneUserDeleteForm form) {
        /**
         * 注意：字符数组或者Integer数组没有区别，均可以使用
         */
        int delNum = phoneUserService.deleteByList(form.getList());

        return this.getSuccessResult("删除成功", delNum);
    }

    /**
     * 批量查询
     * @param form
     * @return
     */
    @RequestMapping(value = "/queryBatch", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity queryBatch(@RequestHeader(value = "Authorization")String authorization, PhoneUserQueryForm form) throws Exception {
        LoginTokenPo loginTokenPo = this.checkLogin(authorization);
        System.out.println("loginToken = " + loginTokenPo);
        List<PhoneUser> list = phoneUserService.queryBatch(form.getList());

        return this.getSuccessResult(list);
    }

    /**
     * 批量更新
     * @return
     */
    @RequestMapping(value = "/updateBatch", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity updateBatch(PhoneUserUpdateForm form) {
        List<PhoneUser> list = new ArrayList<>();

        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setId(4);
        phoneUser.setPhone("13160022076");
        phoneUser.setPassword("234");
        list.add(phoneUser);

        PhoneUser phoneUser1 = new PhoneUser();
        phoneUser1.setId(5);
        phoneUser1.setPhone("13160022077");
        phoneUser1.setPassword("234");
        list.add(phoneUser1);

        form.setList(list);

        phoneUserService.updateBatch(form.getList());
        return this.getSuccessResult();
    }

    /**
     * 测试用户是否携带了token,以及token是否已经过期
     * @param authorization
     * @return
     * @throws MedicalException
     */
    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity test(@RequestHeader(value = "Authorization") String authorization) throws MedicalException {
        System.out.println("authorization = " + authorization);
        if (jwtComponentUtil.getClaims(authorization) == null) {
            return this.getFailResult();
        }
        return this.getSuccessResult("测试成功");
    }

    /**
     * 用于测试Eureka
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity hello() {
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        log.info("/hello, host：" + serviceInstance.getHost() + ", service_id：" + serviceInstance.getServiceId());
        return this.getSuccessResult("Hello World");
    }

    /**
     * 用于测试feign
     * @param id
     * @return
     */
    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public ResponseEntity hello(@RequestParam Integer id) {
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setId(id);
        return this.getSuccessResult(phoneUserService.selectListRecord(phoneUser));
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public ResponseEntity hello(@RequestHeader Integer id, @RequestHeader String password) {
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setId(id);
        phoneUser.setPassword(password);
        return this.getSuccessResult(phoneUserService.selectListRecord(phoneUser));
    }
}
