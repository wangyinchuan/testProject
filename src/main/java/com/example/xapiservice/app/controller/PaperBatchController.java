package com.example.xapiservice.app.controller;

import com.example.xapiservice.app.common.ExceptionEnum;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.entity.PaperBatch;
import com.example.xapiservice.app.entity.Student;
import com.example.xapiservice.app.service.PaperBatchService;
import com.example.xapiservice.app.utils.JsonUtil;
import com.example.xapiservice.app.utils.KafkaUtils;
import com.example.xapiservice.app.vo.PaperBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 论文处理类 
 * @author lzz
 *
 */
@RestController
@RequestMapping("/paperBatch")
public class PaperBatchController extends BaseController {

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


	@Autowired
    private PaperBatchService  paperBatchService;

	/**
	 * 论文批次新增接口
	 * @param request
	 * @param response
	 * @param paperBatchVo
	 */
	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public void addBatch(HttpServletRequest request, HttpServletResponse response, PaperBatchVo paperBatchVo) {
		reflectParameter(paperBatchVo,request);
		String userName = request.getHeader("userName");
		if (!paraMandatoryCheck(Arrays.asList(paperBatchVo.getBatchNum(), 
				paperBatchVo.getBatchName(),paperBatchVo.getOrgId(),paperBatchVo.getOrgName()))) {
			responseErrorJason(response, ExceptionEnum.Y100101.getCode(),ExceptionEnum.Y100101.getMessage());
			return;
		}
		paperBatchVo.setCreator(userName);
		paperBatchVo.setModifier(userName);
		Result result=null;//paperBatchService.saveBatch(paperBatchVo);
		responseJason(response, result);

	}
	
	/**
	 * 论文批次修改接口
	 * @param request
	 * @param response
	 * @param paperBatchVo
	 */
	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public void editBatch(HttpServletRequest request, HttpServletResponse response, PaperBatchVo paperBatchVo) {
		reflectParameter(paperBatchVo,request);
		String userName = request.getHeader("userName");
		if (!paraMandatoryCheck(Arrays.asList(paperBatchVo.getBatchId(),paperBatchVo.getOrgId(),
				paperBatchVo.getOrgName()))) {
			responseErrorJason(response, ExceptionEnum.Y100101.getCode(),ExceptionEnum.Y100101.getMessage());
			return;
		}
		paperBatchVo.setModifier(userName);
		Result result = paperBatchService.updateBatch(paperBatchVo);
		responseJason(response,result);
	}
	/**
	 * 论文批次删除接口
	 * @param request
	 * @param response
	 * @param paperBatchVo
	 */
	@RequestMapping(value = "/optBatch", method = RequestMethod.POST)
	public void optBatch(HttpServletRequest request, HttpServletResponse response, PaperBatchVo paperBatchVo) {
		reflectParameter(paperBatchVo,request);
		if (!paraMandatoryCheck(Arrays.asList(paperBatchVo.getBatchId(),paperBatchVo.getOrgId()))) {
			responseErrorJason(response, ExceptionEnum.Y100101.getCode(),ExceptionEnum.Y100101.getMessage());
			return;
		}
		String userName = request.getHeader("userName");
		paperBatchVo.setModifier(userName);
		Result result = paperBatchService.optBatch(paperBatchVo);
		responseJason(response,result);
	}
	
	/**
	 * 论文批次列表接口
	 * @param request
	 * @param response
	 * @param paperBatchVo
	 */
	@RequestMapping(value = "/listBatch", method = RequestMethod.POST)
	public void listBatch(HttpServletRequest request, HttpServletResponse response, PaperBatchVo paperBatchVo) {
		reflectParameter(paperBatchVo,request);
		String page = paperBatchVo.getPage()!=null?paperBatchVo.getPage().toString():"";
		String rows = paperBatchVo.getRows()!=null?paperBatchVo.getRows().toString():"";
		if (!paraMandatoryCheck(Arrays.asList(page,rows,paperBatchVo.getOrgId(),
				paperBatchVo.getOrgName()))) {
			responseErrorJason(response, ExceptionEnum.Y100101.getCode(),ExceptionEnum.Y100101.getMessage());
			return;
		}
		Result result = paperBatchService.listBatch(paperBatchVo);
		responseJason(response, result);
	}

	/**
	 * json测试
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jsontest", method = RequestMethod.POST)
	public void JsonTest(HttpServletRequest request, HttpServletResponse response,String jsonstr) {
		if ("".equals(jsonstr) || jsonstr==null) {
			responseErrorJason(response, ExceptionEnum.Y100101.getCode(),ExceptionEnum.Y100101.getMessage());
			return;
		}

        System.out.println(jsonstr);
		List<Student> model2=new ArrayList<>();
		model2= JsonUtil.toList(jsonstr,Student.class);
		System.out.println(model2.size());

		//KafkaUtils kafkaData=new KafkaUtils(brokers,SSLKEYSTORE,SSLTRUSTSTORE,SSLPASSWORD);
		KafkaUtils kafkaData=new KafkaUtils("10.100.136.33:9092,10.100.136.34:9092,10.100.136.35:9092","","","");
		kafkaData.produce("pro-t0001-gczx-xapi",jsonstr);

		kafkaData.consume("pro-t0001-gczx-xapi");
		Result result =new Result(Result.SUCCESS,"存储成功!","ok",null);
		responseJason(response, result);
	}

}
