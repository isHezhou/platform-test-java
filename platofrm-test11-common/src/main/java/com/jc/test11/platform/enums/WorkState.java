package com.jc.test11.platform.enums;

/**
 * ClassName WorkState
 * Description {this.desc!''}
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
public enum WorkState {

    /**
     * 草稿
     */
    THE_DRAFT("草稿","1"),
    /**
     * 已驳回
     */
    HAS_BEEN_REJECTED("已驳回","2"),
    /***
     * 已提交
     */
    HAS_BEEN_SUBMITTED("已提交","3"),
    /**
     * 已通过
     */
    HAVE_BEEN_THROUGH("已通过","4"),
    /**
     * 未通过
     */
    NOT_THROUGH("未通过","5"),
    /**
     * 已完成
     */
    THE_END("已完成","6"),
    ;

    public final String VALUE;
    public final String DESC;

    WorkState(String desc, String value){
        this.DESC = desc;
        this.VALUE = value;
    }

}
