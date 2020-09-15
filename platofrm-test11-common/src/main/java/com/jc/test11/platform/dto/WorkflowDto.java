package com.jc.test11.platform.dto;

import lombok.Data;

/**
 * ClassName PlatofrmTest11Application
 * Description {this.desc!''}
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Data
public class WorkflowDto {
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    /**
     * 原因
     */
    private String reason;
    /**
     * 业务ID
     */
    private Long id;
}
