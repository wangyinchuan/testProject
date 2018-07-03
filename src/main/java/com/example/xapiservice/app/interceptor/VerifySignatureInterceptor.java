package com.example.xapiservice.app.interceptor;

import com.example.xapiservice.app.common.ExceptionEnum;
import com.example.xapiservice.app.common.PlatformEnum;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.service.AppService;
import com.example.xapiservice.app.utils.DateUtil;
import com.example.xapiservice.app.utils.HtmlUtil;
import com.example.xapiservice.app.utils.MD5;
import com.example.xapiservice.app.utils.ParamUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Arrays;


/**
 * 验证拦截器
 *
 * Created by guxuyang on 05/07/2017.
 */
public class VerifySignatureInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(VerifySignatureInterceptor.class);
    @Autowired
    private AppService appService;
    @Value("${dissertation.verifySignature.onOff}")
    private String onOff;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器开始时间"+DateUtil.getNow());
        if ("on".equals(onOff)) {
            String appKey = request.getHeader("appKey");
            String timestamp = request.getHeader("timestamp");
            String signatureNonce = request.getHeader("signatureNonce");
            String platform = request.getHeader("platform");
            String signature = request.getHeader("signature");
            String userName=URLDecoder.decode(request.getHeader("userName"), "UTF-8");
            String ip = request.getHeader("ip");

            if (!ParamUtil.paramMandatoryCheck(Arrays.asList(appKey, timestamp, signature, signatureNonce, platform,userName,ip))) {
                Result result = new Result(Result.ERROR, ExceptionEnum.CommonParmeterError.getMessage(), ExceptionEnum.CommonParmeterError.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }
            if (PlatformEnum.getByCode(platform) == null) {
                Result result = new Result(Result.ERROR, "平台参数错误", ExceptionEnum.CommonParmeterError.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }

            String appSecret = appService.findAppSecretByAppkey(appKey);
            if (appSecret == null) {
                Result result = new Result(Result.ERROR, ExceptionEnum.E1000002.getMessage(), ExceptionEnum.E1000002.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }

           /* String str = String.format("timestamp=%s&signatureNonce=%s&platform=%s&appSecret=%s&userName=%s&ip=%s", timestamp, signatureNonce, platform, appSecret,userName,ip);
            String sig = MD5.md5(str);
            boolean flag = signature.equals(sig);
            log.info("signature: {}, str: {}, flag: {}, sig: {}", signature, str, flag, sig);*/

            if (verifySignature(timestamp,signatureNonce,platform,appSecret,signature,userName,ip)) {
                return true;
            } else {
                Result result = new Result(Result.ERROR, ExceptionEnum.E1000003.getMessage(), ExceptionEnum.E1000003.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }
        }
        System.out.println("拦截器结束时间"+DateUtil.getNow());
        return true;
    }

    /**
     * 校验签名
     */
    private static boolean verifySignature(String timestamp, String signatureNonce, String platform, String appSecret, String signature,String userName,String ip) {
        String str = String.format("timestamp=%s&signatureNonce=%s&platform=%s&appSecret=%s&userName=%s&ip=%s", timestamp, signatureNonce, platform, appSecret,userName,ip);
        return signature.equals(MD5.md5(str));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}

