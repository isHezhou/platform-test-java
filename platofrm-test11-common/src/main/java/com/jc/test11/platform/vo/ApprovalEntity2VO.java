package com.jc.test11.platform.vo;

import com.jc.platform.common.supers.AbstractVO;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.excel.annotation.ExcelIgnore;
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
 * ClassName ApprovalEntity2VO.java
 * Description 树形审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 
 */
@ApiModel(value = "ApprovalEntity2VO", description = "树形审批菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class ApprovalEntity2VO extends AbstractVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体编码
     */
    public static final String IS_ENTITY_CODE = "47d77a8e6e39455da7e7de2a1fee2e19";

    /**
     * 主键
     */
    @NotNull(groups = Update.class, message = "主键不能为Null")
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    /**
     * 集团ID
     */
    @ApiModelProperty(value = "集团ID")
    private Long groupId;
    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long orgId;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Long deptId;
    /**
     * 流程状态
     */
    @Size(max=20,message = "流程状态字段长度不可以超过20个字符")
    @ApiModelProperty(value = "流程状态")
    private String workflowState;
    /**
     * 编码
     */
    @IsWorkflowEditField(value = "编码")
    @NotNull(message = "编码不可以为空")
    @ApiModelProperty(value = "编码")
    private Integer treeCode;
    /**
     * 名称
     */
    @IsWorkflowEditField(value = "名称")
    @Size(max=50,message = "名称字段长度不可以超过50个字符")
    @ApiModelProperty(value = "名称")
    private String treeName;
    /**
     * 内容1
     */
    @IsWorkflowEditField(value = "内容1")
    @NotNull(message = "内容1不可以为空")
    @ApiModelProperty(value = "内容1")
    private String content1;
    /**
     * 内容2
     */
    @IsWorkflowEditField(value = "内容2")
    @NotNull(message = "内容2不可以为空")
    @ApiModelProperty(value = "内容2")
    private String content2;
    /**
     * 内容3
     */
    @IsWorkflowEditField(value = "内容3")
    @NotNull(message = "内容3不可以为空")
    @ApiModelProperty(value = "内容3")
    private String content3;
    /**
     * 逻辑删除标识
     */
    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean deleted;
    /**
     * 工作流实例ID
     */
    @Size(max=100,message = "工作流实例ID字段长度不可以超过100个字符")
    @ApiModelProperty(value = "工作流实例ID")
    private String processInstanceId;
    /**
     * 父ID
     */
    @IsWorkflowEditField(value = "父ID")
    @NotNull(message = "父ID不可以为空")
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long createId;
    /**
     * 创建人
     */
    @Size(max=20,message = "创建人字段长度不可以超过20个字符")
    @ApiModelProperty(value = "创建人")
    private String createName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人ID
     */
    @ApiModelProperty(value = "修改人ID")
    private Long modifyId;
    /**
     * 修改人
     */
    @Size(max=20,message = "修改人字段长度不可以超过20个字符")
    @ApiModelProperty(value = "修改人")
    private String modifyName;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
    /**
     * 扩展字段
     */
    @Size(max=20,message = "扩展字段字段长度不可以超过20个字符")
    @ApiModelProperty(value = "扩展字段")
    private String extension;
    /**
     * 上级菜单名称
     */
    @ExcelIgnore
    @ApiModelProperty(value = "上级菜单名称")
    private String parentName;

    /**
     * 下级菜单
     */
    @ExcelIgnore
    @ApiModelProperty(value = "下级菜单")
    private List<ApprovalEntity2VO> children;

    public ApprovalEntity2VO(Long id)
    {
        this.id = id;
    }
    /**
     * 工作流提交时 使用该属性接收
     */
    @ExcelIgnore
    @ApiModelProperty(value = "remote")
    private RemoteCallPayload remoteCallPayload;

}



