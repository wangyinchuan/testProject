package com.example.xapiservice.app.service.impl;

import com.example.xapiservice.app.common.Constants;
import com.example.xapiservice.app.common.ExceptionEnum;
import com.example.xapiservice.app.common.Result;
import com.example.xapiservice.app.controller.BaseController;
import com.example.xapiservice.app.entity.PaperBatch;

import com.example.xapiservice.app.mapper.PaperBatchMapper;

import com.example.xapiservice.app.service.PaperBatchService;
import com.example.xapiservice.app.utils.DateUtil;
import com.example.xapiservice.app.utils.SysUtil;
import com.example.xapiservice.app.vo.PaperBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperBatchServiceImpl extends BaseController implements PaperBatchService {

    @Autowired
    private PaperBatchMapper<PaperBatch> paperBatchMapper;

    @Override
    public boolean insert(PaperBatch paperBatch) {
        try {
            paperBatchMapper.insert(paperBatch);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(PaperBatch paperBatch) {
        try {
            paperBatchMapper.update(paperBatch);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBySelective(PaperBatch paperBatch) {
        try {
            paperBatchMapper.updateBySelective(paperBatch);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(Object id) {
        try {
            paperBatchMapper.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PaperBatch getById(Object id) {
        return paperBatchMapper.getById(id);
    }

    @Override
    public List<PaperBatch> findBySelective(PaperBatch paperBatch) {
        return paperBatchMapper.findBySelective(paperBatch);
    }

    @Override
    public List<PaperBatch> findPageList(PaperBatch paperBatch) {
        return paperBatchMapper.findPageList(paperBatch);
    }

    @Override
    public int findPageCount(PaperBatch paperBatch) {
        return paperBatchMapper.findPageCount(paperBatch);
    }


    @Override
    public Result updateBatch(PaperBatchVo paperBatchVo) {
        Result result = new Result();
        try {
            Map<String, Object> payload = new HashMap<String, Object>();
            PaperBatchVo pbv = new PaperBatchVo();
            pbv.setBatchId(paperBatchVo.getBatchId());
            pbv.setIsDeleted(Constants.STRING_ZERO);
            pbv.setOrgId(paperBatchVo.getOrgId());
            int count = paperBatchMapper.isExistsBatchName(pbv);
            if (Constants.INT_ZERO == count) {
                payload.put("batchId", paperBatchVo.getBatchId());
                return new Result(Result.ERROR, ExceptionEnum.D100205.getMessage(), ExceptionEnum.D100205.getCode(), payload);
            }

            //判断批次编码或批次名称是否已存在（修改批次信息的时候，如果batchNum与batchName没有做修改，不会传进来）
            PaperBatch paperBatch = new PaperBatch();
            paperBatch.setIsDeleted(Constants.STRING_ZERO);
            paperBatch.setOrgId(paperBatchVo.getOrgId());
            paperBatch.setBatchNum(paperBatchVo.getBatchNum());
            paperBatch.setBatchName(paperBatchVo.getBatchName());
            boolean b = batchNumOrBatchNameExists(paperBatch);
            if(b){
                return new Result(Result.ERROR, ExceptionEnum.D100217.getMessage(), ExceptionEnum.D100217.getCode(), null);
            }

            paperBatchMapper.updateBySelective1(paperBatchVo);
            //返回数据
            payload.put("batchId", paperBatchVo.getBatchId());
            result = new Result(Result.SUCCESS, ExceptionEnum.SuccessEditBatch.getMessage(), "", payload);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(Result.ERROR, ExceptionEnum.D100201.getMessage(), ExceptionEnum.D100201.getCode(), null);
        }
        return result;
    }

    @Override
    public Result optBatch(PaperBatchVo paperBatchVo) {
        Result result = new Result();
        try {
            Map<String, Object> payload = new HashMap<String, Object>();

            int countName = paperBatchMapper.isExistsBatch(paperBatchVo);
            if (countName == Constants.INT_ZERO) {
                payload.put("batchId", paperBatchVo.getBatchId());
                return new Result(Result.ERROR, ExceptionEnum.D100205.getMessage(), ExceptionEnum.D100205.getCode(), payload);
            }

            int count = paperBatchMapper.isExistsBatchQuote(paperBatchVo);
            if (count > Constants.INT_ZERO) {
                payload.put("batchId", paperBatchVo.getBatchId());
                return new Result(Result.ERROR, ExceptionEnum.BatchQuote.getMessage(), ExceptionEnum.BatchQuote.getCode(), payload);
            }

            paperBatchMapper.deleteBybatchId(paperBatchVo);
            //返回数据
            payload.put("batchId", paperBatchVo.getBatchId());
            result = new Result(Result.SUCCESS, ExceptionEnum.Success.getMessage(), "", payload);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(Result.ERROR, ExceptionEnum.D100203.getMessage(), ExceptionEnum.D100203.getCode(), null);
        }
        return result;
    }

    @Override
    public Result listBatch(PaperBatchVo paperBatchVo) {
        Result result = new Result();
        /*try {
            List<PaperBatch> list = new ArrayList<PaperBatch>();
            PaperBatch pb = new PaperBatch();
            pb.setPage(paperBatchVo.getPage());
            pb.setRows(paperBatchVo.getRows());

            //创建查询选题结果发布、成绩发布、论文选题人数的对象
            ThesisStudent thesisStudent = new ThesisStudent();

            if (!nullEmptyBlankJudge(paperBatchVo.getSubjectEtime())) {
                pb.setSubjectEtime(DateUtil.stringtoDate(paperBatchVo.getSubjectEtime(), DateUtil.FORMAT_ONE));
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getSubjectStime())) {
                pb.setSubjectStime(DateUtil.stringtoDate(paperBatchVo.getSubjectStime(), DateUtil.FORMAT_ONE));
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getDefenceStime())) {
                pb.setDefenceStime(DateUtil.stringtoDate(paperBatchVo.getDefenceStime(), DateUtil.FORMAT_ONE));
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getDefenceEtime())) {
                pb.setDefenceEtime(DateUtil.stringtoDate(paperBatchVo.getDefenceEtime(), DateUtil.FORMAT_ONE));
            }
            if (paperBatchVo.getIsRepeat() != null) {
                pb.setIsRepeat(paperBatchVo.getIsRepeat());
            }
            if (paperBatchVo.getRepeatPercent() != null) {
                pb.setRepeatPercent(paperBatchVo.getRepeatPercent());
            }
            if (paperBatchVo.getBatchNum() != null) {
                pb.setBatchNum(paperBatchVo.getBatchNum());
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getAppKey())) {
                pb.setAppKey(paperBatchVo.getAppKey());
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getOrgId())) {
                pb.setOrgId(paperBatchVo.getOrgId());
                thesisStudent.setOrgId(paperBatchVo.getOrgId());
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getOrgName())) {
                pb.setOrgName(paperBatchVo.getOrgName());
                thesisStudent.setOrgName(paperBatchVo.getOrgName());
            }
            if (!nullEmptyBlankJudge(paperBatchVo.getBatchName())) {
                pb.setBatchName(paperBatchVo.getBatchName());
            }
            list = paperBatchMapper.findBySelective(pb);

            //返回的数据
            HashMap<String, Object> payload = new HashMap<String, Object>();

            List<HashMap<String, Object>> maps = new ArrayList<HashMap<String, Object>>();
            for (PaperBatch p : list) {

                thesisStudent.setBatchId(p.getBatchId());

                //选题结果发布人数
                int thesisPublishStuCount = thesisStudentMapper.getThesisPublishStuCount(thesisStudent);

                //成绩发布人数
                int achievementPublishStuCount = thesisStudentMapper.getAchievementPublishStuCount(thesisStudent);

                //参见论题方向选择的学生总数
                int thesisStudentCount = thesisStudentMapper.findPageCount(thesisStudent);


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("batchId", p.getBatchId() == null ? "" : p.getBatchId());
                map.put("batchNum", p.getBatchNum() == null ? "" : p.getBatchNum());
                map.put("batchName", p.getBatchName() == null ? "" : p.getBatchName());
                map.put("subjectStime", p.getSubjectStime() == null ? "" : DateUtil.dateToString(p.getSubjectStime(), DateUtil.FORMAT_ONE));
                map.put("subjectEtime", p.getSubjectEtime() == null ? "" : DateUtil.dateToString(p.getSubjectEtime(), DateUtil.FORMAT_ONE));
                map.put("defenceStime", p.getDefenceStime() == null ? "" : DateUtil.dateToString(p.getDefenceStime(), DateUtil.FORMAT_ONE));
                map.put("defenceEtime", p.getDefenceEtime() == null ? "" : DateUtil.dateToString(p.getDefenceEtime(), DateUtil.FORMAT_ONE));
                map.put("isRepeat", p.getIsRepeat() == null ? 0 : p.getIsRepeat());
                map.put("repeatPercent", p.getRepeatPercent() == null ? "" : p.getRepeatPercent());
                map.put("description", p.getDescription() == null ? "" : p.getDescription());
                map.put("isDeleted", p.getIsDeleted() == null ? "" : p.getIsDeleted());
                map.put("orgId", p.getOrgId() == null ? "" : p.getOrgId());
                map.put("orgName", p.getOrgName() == null ? "" : p.getOrgName());
                map.put("thesisStudentCount", thesisStudentCount);//选题学生总数
                map.put("achievementPublishStuCount", achievementPublishStuCount);//成绩发布人数
                map.put("thesisPublishStuCount", thesisPublishStuCount);//选题结果发布人数
                maps.add(map);
            }
            payload.put("batchList", maps);
            payload.put("count", paperBatchMapper.findPageCount(pb));
            result = new Result(Result.SUCCESS, ExceptionEnum.Success.getMessage(), "", payload);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(Result.ERROR, ExceptionEnum.D100201.getMessage(), ExceptionEnum.D100201.getCode(), null);
        }*/
        return result;
    }



    /**
     * 判断论文批次是否存在
     */
    @Override
    public Result isExistsBatch(PaperBatchVo paperBatchVo) {

        Map<String, Object> payload = new HashMap<String, Object>();

        int existsBatch = paperBatchMapper.isExistsBatch(paperBatchVo);
        if (Constants.INT_ZERO == existsBatch) {

            payload.put("batchId", paperBatchVo.getBatchId());

            return new Result(Result.ERROR, ExceptionEnum.D100205.getMessage(), ExceptionEnum.D100205.getCode(), payload);
        }
        return null;
    }



    protected static boolean nullAndEmpty(String str) {
        return null == str || str.isEmpty() || "".equals(str.trim());
    }

    /**
     * 判断院校下批次编码或者批次名称是否已存在
     * @param paperBatch
     * @return
     */
    private boolean batchNumOrBatchNameExists(PaperBatch paperBatch){

        int existsBatch = paperBatchMapper.isExistsBatchNumOrName(paperBatch);
        if(Constants.INT_ZERO < existsBatch)
            return true;
        return false;
    }

}
