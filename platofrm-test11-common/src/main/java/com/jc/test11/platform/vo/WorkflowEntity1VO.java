package com.jc.test11.platform.vo;

import com.jc.platform.common.supers.AbstractVO;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jc.platform.workflow.common.payload.RemoteCallPayload;
import com.jc.test11.platform.annotation.IsWorkflowEditField;
import org.springframework.format.annotation.DateTimeFormat;

import com.jc.test11.platform.group.Update;
import com.jc.test11.platform.group.Insert;
import javax.validation.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.math.*;
import javax.validation.Valid;


/**
 * ClassName WorkflowEntity1VO.java
 * Description 普通工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 
 */
@ApiModel(value = "WorkflowEntity1VO", description = "普通工作流菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class WorkflowEntity1VO extends AbstractVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体编码
     */
    public static final String IS_ENTITY_CODE = "84ce7caf49674c2fa4a8b836c8a340b5";

    /**
     * 主键
     */
    @ExcelIgnore
    @NotNull(groups = Update.class, message = "主键不能为Null")
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 租户ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    /**
     * 集团ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "集团ID")
    private Long groupId;
    /**
     * 机构ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "机构ID")
    private Long orgId;
    /**
     * 部门ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "部门ID")
    private Long deptId;
    /**
     * 流程状态
     */
    @ExcelProperty(value = "流程状态")
    @Size(max=20,message = "流程状态字段长度不可以超过20个字符")
    @ApiModelProperty(value = "流程状态")
    private String workflowState;
    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    @IsWorkflowEditField(value = "编号")
    @NotNull(message = "编号不可以为空")
    @ApiModelProperty(value = "编号")
    private Integer workCode;
    /**
     * 内容1
     */
    @ExcelProperty(value = "内容1")
    @IsWorkflowEditField(value = "内容1")
    @NotNull(message = "内容1不可以为空")
    @ApiModelProperty(value = "内容1")
    private String content1;
    /**
     * 内容2
     */
    @ExcelProperty(value = "内容2")
    @IsWorkflowEditField(value = "内容2")
    @NotNull(message = "内容2不可以为空")
    @ApiModelProperty(value = "内容2")
    private String content2;
    /**
     * 内容3
     */
    @ExcelProperty(value = "内容3")
    @IsWorkflowEditField(value = "内容3")
    @NotNull(message = "内容3不可以为空")
    @ApiModelProperty(value = "内容3")
    private String content3;
    /**
     * 逻辑删除标识
     */
    @ExcelIgnore
    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean deleted;
    /**
     * 工作流实例ID
     */
    @ExcelIgnore
    @Size(max=100,message = "工作流实例ID字段长度不可以超过100个字符")
    @ApiModelProperty(value = "工作流实例ID")
    private String processInstanceId;
    /**
     * 创建人ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "创建人ID")
    private Long createId;
    /**
     * 创建人
     */
    @ExcelIgnore
    @Size(max=20,message = "创建人字段长度不可以超过20个字符")
    @ApiModelProperty(value = "创建人")
    private String createName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "修改人ID")
    private Long modifyId;
    /**
     * 修改人
     */
    @ExcelIgnore
    @Size(max=20,message = "修改人字段长度不可以超过20个字符")
    @ApiModelProperty(value = "修改人")
    private String modifyName;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
    /**
     * 扩展字段
     */
    @ExcelIgnore
    @Size(max=20,message = "扩展字段字段长度不可以超过20个字符")
    @ApiModelProperty(value = "扩展字段")
    private String extension;
    /**
     * 导入数据时集合
     */
    @ExcelIgnore
    @Valid
    List<WorkflowEntity1VO> importExcelList;
    /**
     * 工作流提交时 使用该属性接收
     */
    @ExcelIgnore
    @ApiModelProperty(value = "remote")
    private RemoteCallPayload remoteCallPayload;

}



