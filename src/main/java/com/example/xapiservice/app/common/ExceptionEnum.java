package com.example.xapiservice.app.common;

/**
 * 异常类描述（E 系统  D 数据库 Y 业务系统错误 T第三方  S 成功）
 * author：ws
 * 日期2017/12/20.
 */
public enum ExceptionEnum {
    SysException("E1000000","系统异常"),
    CommonParmeterError("E1000001", "公共参数缺失错误"),
    E1000002("E1000002","App验证失败"),
    E1000003("E1000003","签名验证失败"),
    Y100101("Y100101","必传参数有空值"),
    Y100102("Y100102", "数据格式不正确"),
    Y100103("Y100103","参数数据格式错误"),
    D100201("D100201", "保存数据失败"),
    D100202("D100202", "修改数据失败"),
    D100203("D100203", "删除数据失败"),
    
    D100204("D100204", "数据已存在"),
    D100205("D100205", "该批次不存在"),
    D100206("D100206", "该学生论文选题不存在"),
    D100207("D100207", "该学生论文选题已存在有效数据"),
    D100208("D100208", "该论文进度不存在"),
    D100209("D100209", "该稿件不存在"),
    D100210("D100210", "该成绩不存在"),
    D100211("D100211", "该批次已经发布"),
    D100212("D100212", "该批次环节未设置交稿时间和评阅时间，不能发布"),
    D100213("D100213", "该论文环节不存在"),
    D100214("D100214", "该论文方向不存在"),
    D100215("D100215", "该学生选题存在多个有效数据"),
    D100216("D100216", "该学生选题已设置相同的层次专业信息"),
    D100217("D100217", "该批次编码或者批次名称已存在"),
    D100218("D100218", "该批次编码已存在"),

    Failue("S100199", "操作失败！"),
    Success("S100001","操作成功！"),
    SuccessSelect("S100002","查询成功！"),
    SuccessAddThesis("S100101","论题方向导入成功！"),
    SuccessEditThesis("S100102","论题方向修改成功！"),
    SuccessAddBatch("S100103","论文批次添加成功！"),
    SuccessEditBatch("S100104","论文批次修改成功！"),
    BatchNameExists("S100107","论文批次名称已存在！"),
    BatchQuote("S100108","该批次已使用不能删除！"),
    SuccessAddLink("S100105","环节添加成功！"),
    SuccessEditLink("S100106","环节修改成功！"),
    LinkNameExists("S100109","环节名称已存在！"),
    ThesisIdNoExists("S100110","该论题方向不存在！"),
    ThesisNameExists("S100112","该论题方向名称已存在！"),
    
    BatchThesisRelationExists("S100111","该批次和论题方向已关联！"),
    NoRelationExists("S100113","该批次和论题方向关系不存在！"),
    CanNotSamePerson("S100114","指导老师评阅老师不能是同一个！"),
    OnePersonOnly("S100115","数据有误，指导老师或评阅老师只能只能存在一人次！"),
	Unkow("999999","未知错误");
    String code;
    String message;

    ExceptionEnum() {
    }

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name();
    }

    public String getDisplay() {
        return message;
    }

    public String getMemo() {
        return message;
    }

    public Enum<?> getDefault() {
        return null;
    }
}
