package com.example.xapiservice.app.mapper;

import com.example.xapiservice.app.common.BaseMapper;
import com.example.xapiservice.app.entity.PaperBatch;
import com.example.xapiservice.app.vo.PaperBatchVo;

import java.util.List;

public interface PaperBatchMapper<T> extends BaseMapper<T> {

	void updateBySelective1(PaperBatchVo paperBatchVo);

	void deleteBybatchId(PaperBatchVo paperBatchVo);
	
	/**
	 * 
	 * @描述：根据组织机构编号及批次编号判断，该机构下是否存在该批次
	 * @修改日期：
	 * @param paperBatchVo
	 * @return
	 */
	int isExistsBatch(PaperBatchVo paperBatchVo);

    /**
     * 判断院校下批次编码或者批次名称是否已存在
     * @param paperBatch
     * @return
     */
	int isExistsBatchNumOrName(PaperBatch paperBatch);

	/**
	 * 
	 * @描述：判断该批次是否已经发布
	 * @修改日期：
	 * @param paperBatchVo
	 * @return
	 */
	int isAlreadyPublished(PaperBatchVo paperBatchVo);
	/**
	 * 
	 * @描述：根据组织机构编号及批次编号判断，该机构下批次名称是否存在
	 * @修改日期：
	 * @param paperBatchVo
	 * @return
	 */
	int isExistsBatchName(PaperBatchVo paperBatchVo);
	/**
	 * 
	 * @描述：根据组织机构编号及批次编号判断，该机构下批次是否引用
	 * @修改日期：
	 * @param paperBatchVo
	 * @return
	 */
	int isExistsBatchQuote(PaperBatchVo paperBatchVo);



	List<PaperBatch> findBySelectiveNopage(PaperBatch paperBatch);


}
