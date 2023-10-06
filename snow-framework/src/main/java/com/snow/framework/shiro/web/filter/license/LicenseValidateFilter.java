package com.snow.framework.shiro.web.filter.license;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.enums.OnlineStatus;
import com.snow.common.utils.ServletUtils;
import com.snow.framework.shiro.session.OnlineSession;
import com.snow.framework.shiro.session.OnlineSessionDAO;
import com.snow.system.domain.LicenseManage;
import com.snow.framework.shiro.service.SysLicenseManage;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;

import com.snow.system.domain.SysUserOnline;
import com.snow.system.service.ISysUserOnlineService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName LicenseValidateFilter
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/22 10:01
 * @Version 1.0
 */
public class LicenseValidateFilter extends AccessControlFilter {

    @Autowired
    SysLicenseManage sysLicenseManage;
    @Autowired
    private ISysUserOnlineService userOnlineService;
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;


    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 同一个用户最大会话数
     **/
    private int maxSession = -1;

    /**
     * 踢出之前登录的/之后登录的用户 默认false踢出之前登录的用户
     **/


    /**
     * 踢出后到的地址
     **/
    private String kickoutUrl;
    private String licenseOutUrl;


    private Map<String, String> filterChainDefinitionMap;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    private SessionManager sessionManager;


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    public static void main(String[] args) {
        String aaa = "/ruoyi/**";

        System.out.println(aaa.contains("*"));
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String requestURI = httpServletRequest.getRequestURI();

        boolean flag = false;


        for (String key : filterChainDefinitionMap.keySet()) {

            if (key.equals("/**")){
                continue;

            }
            if (key.contains("*")) {
                String s = key.replaceAll("\\*", "");
                if (requestURI.contains(s)) {
                    flag = true;
                    break;
                }
            }



            if (requestURI.equals(key)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            return true;
        }

        String license = null;
        String JSESSIONID = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return isAjaxResponse(request, response, "暂无license或license失效!配置申请license后登录", licenseOutUrl);
        }
        for (Cookie cookie : cookies) {
            if ("license".equals(cookie.getName())) {
                license = cookie.getValue();
            }
            if ("JSESSIONID".equals(cookie.getName())) {
                JSESSIONID = cookie.getValue();
            }
        }
        if (license == null) {
            return isAjaxResponse(request, response, "暂无license或license失效!配置申请license后登录", licenseOutUrl);
        }
        Subject subject = getSubject(request, response);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        LicenseManage licenseManage = sysLicenseManage.getLicenseManage(license, simpleDateFormat.format(new Date()));
        if (licenseManage == null) {

            return isAjaxResponse(request, response, "暂无license或license失效!配置license后登录", licenseOutUrl);
        }
        // 获取license 的最大在线用户数量

        //判断请求是否有 license 有的话,就执行下面的,

        //判断license 是否有效,

        //

        try {
            Session session = subject.getSession();
            // 当前登录用户
            SysUser user = ShiroUtils.getSysUser();
            String loginName = user.getLoginName();
            Serializable sessionId = session.getId();

            // 读取缓存用户 没有就存入
            Map<String, Map<String, String>> cache = SysLicenseManage.getCache();
            Map<String, String> s = cache.get(license);
            if (s == null || s.size() == 0) {
                Map<String, String> array = new LinkedHashMap<>();
                array.put(loginName, JSESSIONID);
                cache.put(license, array);
            } else {
                s.remove(loginName);
                s.put(loginName, JSESSIONID);
            }
            if (s.size() > licenseManage.getOnlineNumber()) {
                Map.Entry<String, String> next = s.entrySet().iterator().next();
                String key = next.getKey();

                SysUserOnline online = userOnlineService.selectOnlineById(next.getValue());
                if (online != null) {

                    OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
                    onlineSession.setStatus(OnlineStatus.off_line);
                    onlineSessionDAO.update(onlineSession);
                    online.setStatus(OnlineStatus.off_line);
                    userOnlineService.saveOnline(online);
                }

                s.remove(key);
            }
            return true;
        } catch (Exception e) {
            return isAjaxResponse(request, response, "您已在别处登录，请您修改密码或重新登录", kickoutUrl);
        }

    }


    private boolean isAjaxResponse(ServletRequest request, ServletResponse response, String msg, String redirectUrl) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (ServletUtils.isAjaxRequest(req)) {
            AjaxResult ajaxResult = AjaxResult.error(msg);
            ServletUtils.renderString(res, objectMapper.writeValueAsString(ajaxResult));
        } else {
            WebUtils.issueRedirect(request, response, redirectUrl);
        }
        return false;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }


    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    public void setLicenseOutUrl(String licenseOutUrl) {
        this.licenseOutUrl = licenseOutUrl;
    }

    public String getLicenseOutUrl() {
        return licenseOutUrl;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }
}
