package com.example.xapiservice.app.service;

import com.example.xapiservice.app.common.BaseService;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.entity.PaperBatch;
import com.example.xapiservice.app.vo.PaperBatchVo;

public interface PaperBatchService extends BaseService<PaperBatch> {
	
	public Result updateBatch(PaperBatchVo paperBatchVo);
	
	public Result optBatch(PaperBatchVo paperBatchVo);
	
	public Result listBatch(PaperBatchVo paperBatchVo);
	
	/**
	 * 
	 * @描述：根据组织机构编号及批次编号判断，该机构下是否存在该批次
	 * @修改日期：
	 * @param paperBatchVo
	 * @return
	 */
	public Result isExistsBatch(PaperBatchVo paperBatchVo);

}
