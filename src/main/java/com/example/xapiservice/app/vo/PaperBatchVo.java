package com.example.xapiservice.app.vo;


import com.example.xapiservice.app.common.BasePage;

import java.util.Date;

/**
 * 论文批次
 * @author admin
 *
 */
public class PaperBatchVo extends BasePage {
	
	private static final long serialVersionUID = -1405444979545251012L;
	private Integer sid; //编号
	private Date sysCreate;//创建时间
	private String creator; //创建人
	private Date sysModified; //更新时间
	private String modifier; //更新人
	private String isDeleted; //删除状态  (0:存在 1：删除)
	private String batchId; //论文批次编号
	private String appKey; //应用key
	private String batchNum; //批次编号
	private String batchName; //批次名称
	private String subjectStime; //选题开始时间
	private String subjectEtime; //选题截止时间
	private String defenceStime; //答辩开始时间
	private String defenceEtime; //答辩截止时间
	private Integer isRepeat; //是否重复
	private Double repeatPercent; //重复比例
	private Integer publishStatus; //发布状态
	private Date publishTime; //发布时间
	private String publishName; //发布人
	private String description; //描述
	
	private String orgId;//组织机构代码编号
	private String orgName;//组织机构代码名称
	private Integer isActive;

	private String defenceApplyStime;
	private String defenceApplyEtime;

	public String getDefenceApplyStime() {
		return defenceApplyStime;
	}

	public void setDefenceApplyStime(String defenceApplyStime) {
		this.defenceApplyStime = defenceApplyStime;
	}

	public String getDefenceApplyEtime() {
		return defenceApplyEtime;
	}

	public void setDefenceApplyEtime(String defenceApplyEtime) {
		this.defenceApplyEtime = defenceApplyEtime;
	}
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Date getSysCreate() {
		return sysCreate;
	}
	public void setSysCreate(Date sysCreate) {
		this.sysCreate = sysCreate;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getSysModified() {
		return sysModified;
	}
	public void setSysModified(Date sysModified) {
		this.sysModified = sysModified;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getSubjectStime() {
		return subjectStime;
	}
	public void setSubjectStime(String subjectStime) {
		this.subjectStime = subjectStime;
	}
	public String getSubjectEtime() {
		return subjectEtime;
	}
	public void setSubjectEtime(String subjectEtime) {
		this.subjectEtime = subjectEtime;
	}
	public String getDefenceStime() {
		return defenceStime;
	}
	public void setDefenceStime(String defenceStime) {
		this.defenceStime = defenceStime;
	}
	public String getDefenceEtime() {
		return defenceEtime;
	}
	public void setDefenceEtime(String defenceEtime) {
		this.defenceEtime = defenceEtime;
	}
	public Integer getIsRepeat() {
		return isRepeat;
	}
	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}
	public Double getRepeatPercent() {
		return repeatPercent;
	}
	public void setRepeatPercent(Double repeatPercent) {
		this.repeatPercent = repeatPercent;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public String getPublishName() {
		return publishName;
	}
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
}

