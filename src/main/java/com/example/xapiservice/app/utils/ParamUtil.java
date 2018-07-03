package com.example.xapiservice.app.utils;

import com.example.xapiservice.app.common.BaseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ParamUtil {

    private ParamUtil(){}

    /**
     *
     * 检验参数是否为空
     * @param params 多个参数
     * @return isEmpty
     */
    public static boolean paramMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
    /**
     * 检验字符串是否为空
     * @param str str
     * @return isEmpty
     */
    private static boolean nullEmptyBlankJudge(String str){
        return null == str || str.isEmpty() || "".equals(str.trim());
    }
    
    public static BaseVo convert(HttpServletRequest request, BaseVo baseVo){
    	baseVo.setAppKey(request.getHeader("appKey"));
    	baseVo.setIp(request.getHeader("ip"));
    	baseVo.setUserName(request.getHeader("userName"));
    	baseVo.setPlatform(request.getHeader("platform"));
    	return baseVo;
    }

}
