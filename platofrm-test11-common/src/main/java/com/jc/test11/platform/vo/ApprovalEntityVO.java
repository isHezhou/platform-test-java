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
 * ClassName ApprovalEntityVO.java
 * Description 普通审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 
 */
@ApiModel(value = "ApprovalEntityVO", description = "普通审批菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class ApprovalEntityVO extends AbstractVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体编码
     */
    public static final String IS_ENTITY_CODE = "87ceea2b0c114fc89eebb12f88958122";

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
     * 内容1
     */
    @ExcelProperty(value = "内容1")
    @IsWorkflowEditField(value = "内容1")
    @Size(max=50,message = "内容1字段长度不可以超过50个字符")
    @ApiModelProperty(value = "内容1")
    private String content1;
    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    @IsWorkflowEditField(value = "编码")
    @NotNull(message = "编码不可以为空")
    @ApiModelProperty(value = "编码")
    private String csCode;
    /**
     * 内容2
     */
    @ExcelProperty(value = "内容2")
    @IsWorkflowEditField(value = "内容2")
    @Size(max=50,message = "内容2字段长度不可以超过50个字符")
    @ApiModelProperty(value = "内容2")
    private String content2;
    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    @IsWorkflowEditField(value = "名称")
    @Size(max=50,message = "名称字段长度不可以超过50个字符")
    @ApiModelProperty(value = "名称")
    private String csName;
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
    List<ApprovalEntityVO> importExcelList;
    /**
     * 工作流提交时 使用该属性接收
     */
    @ExcelIgnore
    @ApiModelProperty(value = "remote")
    private RemoteCallPayload remoteCallPayload;

}



