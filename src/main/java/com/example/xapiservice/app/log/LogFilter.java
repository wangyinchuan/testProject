package com.example.xapiservice.app.log;

import com.example.xapiservice.app.common.ExceptionEnum;
import com.example.xapiservice.app.common.LogTypeEnum;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.log.support.HttpServletResponseCopier;
import com.example.xapiservice.app.log.support.ParameterRequestWrapper;
import com.example.xapiservice.app.log.support.ServiceLog;
import com.example.xapiservice.app.service.AppService;
import com.example.xapiservice.app.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LogFilter implements Filter {

    @Autowired
    private ServiceLogSender serviceLogSender;
    @Autowired
    private AppService appService;
    @Value("${api.base.request.url}")
    private String apiBaseRequestUrl;
    @Value("${api.log.onOff}")
    private String apiLogOnOff;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if ("on".equals(apiLogOnOff)) {
            HttpServletRequest req = (HttpServletRequest) request;

            if (response.getCharacterEncoding() == null) {
                response.setCharacterEncoding("UTF-8");
            }
            
            String userName=URLDecoder.decode(req.getHeader("userName"), "UTF-8");
            long startTime = System.currentTimeMillis();
           // HttpServletRequest req1 = new ApiHttpServletRequest(req);
            Map<String , Object> extendParams=new HashMap<String, Object>();
            extendParams.put("appKey",req.getHeader("appKey"));
            extendParams.put("userName",userName);
            HttpServletRequest req1 = new ParameterRequestWrapper(req,extendParams); 
            HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

            chain.doFilter(req1, responseCopier);
            responseCopier.flushBuffer();

            byte[] copy = responseCopier.getCopy();
            long endTime = System.currentTimeMillis(); //请求结束时间
            ServiceLog serviceLog = new ServiceLog();
            serviceLog.setIp(getIpAddr(req));
            serviceLog.setBusinessIp(req.getHeader("ip"));
            serviceLog.setUserName(req.getHeader("userName"));
            serviceLog.setRequestURL(req.getRequestURL().toString());
            serviceLog.setAppKey(req.getHeader("appKey"));
            serviceLog.setCreateTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            serviceLog.setHttpMethod(req.getMethod());
            serviceLog.setRequestParam(requestParamsToJSON(req));
            serviceLog.setExecutionTime((double)(endTime - startTime));
            serviceLog.setRequestPath(req.getRequestURI().replaceFirst(apiBaseRequestUrl, ""));
            serviceLog.setHttpResponseStatus(String.valueOf(responseCopier.getStatus()));
            serviceLog.setLogType(LogTypeEnum.SERVICE.getCode());

            if (copy.length > 0) {
                String result = new String(copy, response.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(result);
                serviceLog.setInvokeStatus(jsonObject.getInteger("status"));
                serviceLog.setErrorCode(jsonObject.getString("errorCode"));
                serviceLog.setErrorMessage(jsonObject.getString("message"));
            } else { //处理出现异常
                serviceLog.setInvokeStatus(Result.ERROR);
                serviceLog.setErrorCode(ExceptionEnum.SysException.getCode());
                serviceLog.setErrorMessage(ExceptionEnum.SysException.getMessage());
            }
            serviceLogSender.sendServiceLog(serviceLog); //记录响应日志*/
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 请求参数转json
     * @param req req
     * @return string
     */
    private String requestParamsToJSON(ServletRequest req) {
        Map<String,String[]> params = req.getParameterMap();
        if (params == null || params.isEmpty()) {
            return null;
        }
        JSONObject jsonObj = new JSONObject();
        for (Map.Entry<String,String[]> entry : params.entrySet()) {
            String[] v = entry.getValue();
            Object o = (v.length == 1) ? v[0] : v;
            jsonObj.put(entry.getKey(), o);
        }
        return jsonObj.toJSONString();
    }

    /**
     * 获取IP地址
     *
     * @param request request
     * @return ip
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if(null != inet){
                    ip = inet.getHostAddress();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}