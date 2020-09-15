package com.jc.test11.platform.vo;

import com.jc.platform.common.supers.AbstractVO;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
 * ClassName TheOrderVO.java
 * Description 订单菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 
 */
@ApiModel(value = "TheOrderVO", description = "订单菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class TheOrderVO extends AbstractVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体编码
     */
    public static final String IS_ENTITY_CODE = "cc0ffae606c5424ab08345d87a1f97e5";

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
     * 订单名称
     */
    @ExcelProperty(value = "订单名称")
    @NotNull(message = "订单名称不可以为空")
    @ApiModelProperty(value = "订单名称")
    private String theOrderName;
    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    @NotNull(message = "订单编号不可以为空")
    @ApiModelProperty(value = "订单编号")
    private String theOrderCode;
    /**
     * 起始日期
     */
    @ExcelProperty(value = "起始日期")
    @NotNull(message = "起始日期不可以为空")
    @ApiModelProperty(value = "起始日期")
    private Date startDate;
    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    @ApiModelProperty(value = "结束时间")
    private Date endDateTime;
    /**
     * 逻辑删除标识
     */
    @ExcelIgnore
    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean deleted;
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
    List<TheOrderVO> importExcelList;

}



