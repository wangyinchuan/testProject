package com.example.xapiservice.app.log.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
* <pre>
* Api的 HttpServletRequest，主要是完成如下功能：
* 背景：现在我们所有的API都是要求传递cooperatorId，而且cooperatorId的定位是卖家QQ号。但是现在逐渐有针对买家的三方应用出现。
* 这些应用是以买家为纬度的，不需要传递cooperatorId。但是我们现在的鉴权体系包括后台的IDL都是要求传递cooperatorId，这里提出了这么一个逻辑：
* 如果没有传递cooperatorId，那么默认就是授权者，即uin。这样cooperatorId只有在和uin不同的情况下才需要传递。
* 通过这样，给买家用户造成一个不需要传递cooperatorId的假象。而且这个逻辑也是简化参数传递不错的逻辑。
*
* @author arganzheng
* @date 2012-9-10
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ApiHttpServletRequest extends HttpServletRequestWrapper {
    private static final String UIN = "appKey";
     private static final String COOPERATOR_ID = "appKey";
     
     private HttpServletRequest request;
     public ApiHttpServletRequest(HttpServletRequest request){
        super(request);
        this.request = request;
    }
    @Override
    public String getParameter(String name) {
        String value = request.getParameter(name);
        if(value == null && COOPERATOR_ID.equals(name)){
             value = request.getHeader(UIN);
        }
        return value;
    }
     @Override
    public Map getParameterMap() {
         Map map = request.getParameterMap();
         if(!map.containsKey(COOPERATOR_ID) && map.containsKey(UIN)){
              map = new HashMap(map);
              map.put(COOPERATOR_ID, map.get(UIN));
         }
         return map;
    }
     @Override
     public Enumeration getParameterNames() {
          if(request.getParameter(COOPERATOR_ID) == null && request.getHeader(UIN) != null){
               return Collections.enumeration(getParameterMap().keySet());
          }else{
               return request.getParameterNames();
          }
     }
     @Override
     public String[] getParameterValues(String name) {
          String[] values = request.getParameterValues(name);
          if(values == null && COOPERATOR_ID.equals(name)){
               values = request.getParameterValues(UIN);
          }
          return values;
     }
}