/*

 * Powered By [JHOP3.0-AutoCode]
 * Web Site: http://www.techstar.com
 * Since  2014-07-30 
 */

package com.example.xapiservice.app.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.ResourceBundle;


/**
 * @author JHOP3.0-AutoCode
 * @since   2014-07-30 12:00 
 */

/***
 * 基于Spring MVC的控制层
 * 
 * @Date 2014-07-30 12:00
 * @author JHOP-AutoCode
 */
@Controller
public class BaseContorller {

	protected ResourceBundle getBundle(String properties) {
		ResourceBundle resource = ResourceBundle.getBundle(properties);
		return resource;
	}

	/**
	 * get current plan number
	 * 
	 * @return
	 */
	protected String getPlanNumber() {
		String planNumber = (String) getRequest().getSession().getAttribute(
				"KEY_PlanNumber");
		return planNumber;
	}


	/**
	 * get user name
	 * 
	 * @return
	 */
	protected String getCurrentProjectUsername() {
		String username = (String) getRequest().getSession().getAttribute(
				"KEY_USERNAME");
		return username;
	}

	/**
	 * get request
	 * 
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * get filter planName
	 * 
	 * @return
	 */
	protected String getPlanName() {
		String planName = (String) getRequest().getSession().getAttribute(
				"KEY_Name");
		return planName;
	}





	/**
	 * 移除最后一个数据
	 * 
	 * @param str
	 * @return
	 */
	protected String removeLastPoint(String str) {
		if (null != str && str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}


	/**
	 * 加载导出文件路径
	 * 
	 * @return
	 */
	protected String getFilePath() {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String exportFilePath = bundle.getString("exportFilePath");
		return exportFilePath;
	}

	/**
	 * 获得时间戳文件名
	 * 
	 * @param fileName
	 * @return
	 */
	protected String getFileName(String fileName) {
		// Calendar cal = Calendar.getInstance();
		// int year = cal.get(Calendar.YEAR);
		// int month = cal.get(Calendar.MONTH) + 1;
		// int day = cal.get(Calendar.DAY_OF_MONTH);
		// int hour = cal.get(Calendar.HOUR_OF_DAY);
		// int minute = cal.get(Calendar.MINUTE);
		// int second = cal.get(Calendar.SECOND);
		// int millisecond = cal.get(Calendar.MILLISECOND);
		fileName = getTimestamp();
		return fileName;
	}

	/**
	 * 获得时间戳
	 * 
	 * @return
	 */
	protected String getTimestamp() {
		Calendar cal = Calendar.getInstance();
		String time = "";
		time += cal.get(Calendar.YEAR);
		time += cal.get(Calendar.MONTH) + 1;
		time += cal.get(Calendar.DAY_OF_MONTH);
		time += cal.get(Calendar.HOUR_OF_DAY);
		time += cal.get(Calendar.MINUTE);
		time += cal.get(Calendar.SECOND);
		time += cal.get(Calendar.MILLISECOND);
		return time;
	}

	
}
