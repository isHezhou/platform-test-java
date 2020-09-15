package com.jc.test11.platform.vo;

import com.jc.platform.common.supers.AbstractVO;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.excel.annotation.ExcelIgnore;
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
 * ClassName TreeEntityVO.java
 * Description 树形菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 
 */
@ApiModel(value = "TreeEntityVO", description = "树形菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class TreeEntityVO extends AbstractVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体编码
     */
    public static final String IS_ENTITY_CODE = "2970ffd903494ae794cc93e7846d5f07";

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
     * 编码
     */
    @NotNull(message = "编码不可以为空")
    @ApiModelProperty(value = "编码")
    private String taCode;
    /**
     * 菜单名称
     */
    @NotNull(message = "菜单名称不可以为空")
    @ApiModelProperty(value = "菜单名称")
    private String taName;
    /**
     * 上传图片
     */
    @Size(max=50,message = "上传图片字段长度不可以超过50个字符")
    @ApiModelProperty(value = "上传图片")
    private String uploadPhoto;
    private List<FileInfoVO> uploadPhotoFileList;
    /**
     * 上传文件
     */
    @Size(max=50,message = "上传文件字段长度不可以超过50个字符")
    @ApiModelProperty(value = "上传文件")
    private String uploadFile;
    private List<FileInfoVO> uploadFileFileList;
    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不可以为空")
    @ApiModelProperty(value = "开始时间")
    private Date stratDate;
    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不可以为空")
    @ApiModelProperty(value = "结束时间")
    private Date endDateTime;
    /**
     * 爱好
     */
    @NotNull(message = "爱好不可以为空")
    @ApiModelProperty(value = "爱好")
    private String hobby;
    /**
     * 逻辑删除标识
     */
    @ApiModelProperty(value = "逻辑删除标识")
    private Boolean deleted;
    /**
     * 父ID
     */
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
    private List<TreeEntityVO> children;

    public TreeEntityVO(Long id)
    {
        this.id = id;
    }
    /**
     * 删除文件集合
     */
    @ExcelIgnore
    private List<FileInfoVO> delFileList;

}



