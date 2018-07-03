package com.example.xapiservice.app.controller;

import com.example.xapiservice.app.common.ExceptionEnum;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.xapiservice.app.utils.KafkaUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    @Value("${spring.cloud.stream.kafka.binder.brokers}")
    private String brokers;

    @Value("${spring.cloud.stream.kafka.binder.zk-nodes}")
    private String zknodes;

    @Value("${spring.cloud.stream.kafka.SSL_KEYSTORE}")
    private String SSLKEYSTORE;

    @Value("${spring.cloud.stream.kafka.SSL_TRUSTSTORE}")
    private String SSLTRUSTSTORE;

    @Value("${spring.cloud.stream.kafka.SSL_PASSWORD}")
    private String SSLPASSWORD;




    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected final Charset BASIC_REQUEST_ENCODING = Charset.forName("iso8859-1");

    protected static boolean nullAndEmpty(String str) {
        return null == str || str.isEmpty() || "".equals(str.trim());
    }

    protected boolean nullEmptyBlankJudge(String str) {
        return null == str || str.isEmpty() || "".equals(str.trim());
    }

    protected boolean paraMandatoryCheck(List<String> params) {
        for (String param : params) {
            if (nullEmptyBlankJudge(param)) {
                return false;
            }
        }
        return true;
    }

    protected boolean paramCheck(List<Integer> params) {
        for (Integer param : params) {
            if (param == -1) {
                return false;
            }
        }
        return true;
    }

    protected void responseErrorJason(HttpServletResponse response, String errorCode, String message) {
        responseErrorJason(response, errorCode, message, null);
    }

    protected void responseErrorJason(HttpServletResponse response, String errorCode, String message, Object payload) {
        Result result = new Result();
        result.setStatus(Result.ERROR);
        result.setErrorCode(errorCode);
        result.setMessage(message);
        if (payload != null) {
            result.setPayload(payload);
        }
        responseJason(response, result);
    }

    protected void responseErrorJason(HttpServletResponse response, String message) {
        responseErrorJason(response, "0", message);
    }

    protected void responseErrorJason(HttpServletResponse response, ExceptionEnum exceptionEnum) {
        responseErrorJason(response, exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    protected void responseErrorJason(HttpServletResponse response, ExceptionEnum exceptionEnum, Object payload) {
        responseErrorJason(response, exceptionEnum.getCode(), exceptionEnum.getMessage(), payload);
    }

    protected void responseErrorCodeJason(HttpServletResponse response, String errorCode) {
        ExceptionEnum apiErrorCode = ExceptionEnum.valueOf(errorCode);
        if (apiErrorCode == null) {
            return;
        }
        responseErrorJason(response, apiErrorCode);
    }

    protected void responseSuccessJason(HttpServletResponse response, Object payload) {
        responseSuccessJason(response, payload, "");
    }

    protected void responseSuccessJason(HttpServletResponse response, Object payload, String message) {
        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage(message);
        if (payload != null) {
            result.setPayload(payload);
        }
        responseJason(response, result);
    }

    protected void responseJason(HttpServletResponse response, Result result) {
        String resultJson = JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue);
        try {
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson);
            log.info(resultJson);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获取IP地址
     *
     * @param request request
     * @return ip
     */
    public String getIpAddr(HttpServletRequest request) {
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
                if (null != inet) {
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

    /**
     * 将HttpServletRequest中的参数反射至实体类
     *
     * @param <T>
     * @param clazz
     * @param request
     * @return
     */
    protected <T> void reflectParameter(T obj, HttpServletRequest request) {
        try {
            //获取对象的Class
            Class<?> clazz = obj.getClass();
            //获取Class中所有已声明的属性集合
            Field[] fileds = clazz.getDeclaredFields();
            //遍历属性结合
            for (Field f : fileds) {
                //过滤被final、static修饰的成员变量
                if ((f.getModifiers() & Modifier.FINAL) > 0
                        || (f.getModifiers() & Modifier.STATIC) > 0) {
                    continue;
                }
                //取消所有私有变量的限制
                f.setAccessible(true);//取消Field的访问检查
                //获取属性的类型,long/int/string....
                Class<?> fieldType = f.getType();
                //获取属性的名字
                String fieldName = f.getName();
                //根据属性的名字从request中获取value
                String paramVal = request.getParameter(fieldName);
                //判断类型,如果是String
                if (String.class == fieldType) {
                    f.set(obj, paramVal);
                    //判断类型,如果是long,则使用NumberUtils.toLong,即使为空,也赋默认值0L
                } else if (long.class == fieldType || Long.class == fieldType) {
                    f.set(obj, NumberUtils.toLong(paramVal));
                    //判断类型,如果是int,则使用NumberUtils.toInt,即使为空,也赋默认值0
                } else if (int.class == fieldType || Integer.class == fieldType) {
                    f.set(obj, NumberUtils.toInt(paramVal));
                    //判断类型,如果是date,则使用DateUtil.parseDateNewFormat格式化日期格式
                } else if (Date.class == fieldType) {
                    f.set(obj, DateUtil.stringtoDate(paramVal, DateUtil.FORMAT_ONE));
                }
            }
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    protected Map<String, Object> formatParam(HttpServletRequest request) throws UnsupportedEncodingException {
        String userName = request.getHeader("userName");
        String appKey = request.getHeader("appKey");
        Map<?, ?> parameters = request.getParameterMap();
        Charset charset = Charset.forName("UTF-8");
        Map<String, Object> formattedParameters = new HashMap<>();
        for (Map.Entry<?, ?> entry : parameters.entrySet()) {
            if (entry.getValue() == null || Array.getLength(entry.getValue()) == 0) {
                formattedParameters.put((String) entry.getKey(), null);
            } else {
                if (HttpMethod.GET.name().equals(request.getMethod())) {
                    formattedParameters.put((String) entry.getKey(), new String(((String) Array.get(entry.getValue(), 0)).getBytes(BASIC_REQUEST_ENCODING), charset));
                } else {
                    formattedParameters.put((String) entry.getKey(), URLDecoder.decode((String) Array.get(entry.getValue(), 0), charset.name()));
                }
            }
        }
        if (HttpMethod.GET.name().equals(request.getMethod())) {
            formattedParameters.put("appKey", new String(appKey.getBytes(BASIC_REQUEST_ENCODING), charset));
            formattedParameters.put("userName", new String(userName.getBytes(BASIC_REQUEST_ENCODING), charset));

        } else {
            formattedParameters.put("appKey", URLDecoder.decode(appKey, charset.name()));
            formattedParameters.put("userName", URLDecoder.decode(userName, charset.name()));
        }
        return formattedParameters;
    }
}
