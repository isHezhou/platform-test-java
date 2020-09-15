package com.jc.test11.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jc.platform.common.supers.AbstractEntity;
import com.jc.test11.platform.vo.TreeEntityVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.math.*;

/**
 * ClassName TreeEntityEntity.java
 * Description 树形菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Data
@TableName("tree_entity")
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class TreeEntityEntity extends AbstractEntity<TreeEntityVO> implements Serializable
{
	private static final long serialVersionUID = 1L;

     @TableField(value = ("id"))
     private Long id;
     @TableField(value = ("tenant_id"))
     private Long tenantId;
     @TableField(value = ("group_id"))
     private Long groupId;
     @TableField(value = ("org_id"))
     private Long orgId;
     @TableField(value = ("dept_id"))
     private Long deptId;
     @TableField(value = ("ta_code"))
     private String taCode;
     @TableField(value = ("ta_name"))
     private String taName;
     @TableField(value = ("upload_photo"))
     private String uploadPhoto;
     @TableField(value = ("upload_file"))
     private String uploadFile;
     @TableField(value = ("strat_date"))
     private Date stratDate;
     @TableField(value = ("end_date_time"))
     private Date endDateTime;
     @TableField(value = ("hobby"))
     private String hobby;
     @TableField(value = ("deleted"))
     private Boolean deleted;
     @TableField(value = ("parent_id"))
     private Long parentId;
     @TableField(value = ("create_id"))
     private Long createId;
     @TableField(value = ("create_name"))
     private String createName;
     @TableField(value = ("create_time"))
     private Date createTime;
     @TableField(value = ("modify_id"))
     private Long modifyId;
     @TableField(value = ("modify_name"))
     private String modifyName;
     @TableField(value = ("modify_time"))
     private Date modifyTime;
     @TableField(value = ("extension"))
     private String extension;

	/**
	 * 生成vo对象
	 *
	 * @return vo对象
	 */
	public TreeEntityVO copyToVO()
	{
		TreeEntityVO vo = new TreeEntityVO();
        vo.setId(this.id);
        vo.setTenantId(this.tenantId);
        vo.setGroupId(this.groupId);
        vo.setOrgId(this.orgId);
        vo.setDeptId(this.deptId);
        vo.setTaCode(this.taCode);
        vo.setTaName(this.taName);
        vo.setUploadPhoto(this.uploadPhoto);
        vo.setUploadFile(this.uploadFile);
        vo.setStratDate(this.stratDate);
        vo.setEndDateTime(this.endDateTime);
        vo.setHobby(this.hobby);
        vo.setDeleted(this.deleted);
        vo.setParentId(this.parentId);
        vo.setCreateId(this.createId);
        vo.setCreateName(this.createName);
        vo.setCreateTime(this.createTime);
        vo.setModifyId(this.modifyId);
        vo.setModifyName(this.modifyName);
        vo.setModifyTime(this.modifyTime);
        vo.setExtension(this.extension);
		return vo;
	}

	/**
	 * 从vo对象更新属性
	 *
	 * @param vo vo对象
	 */
	public void copyFormVO(TreeEntityVO vo)
	{
        this.setId(vo.getId());
        this.setTenantId(vo.getTenantId());
        this.setGroupId(vo.getGroupId());
        this.setOrgId(vo.getOrgId());
        this.setDeptId(vo.getDeptId());
        this.setTaCode(vo.getTaCode());
        this.setTaName(vo.getTaName());
        this.setUploadPhoto(vo.getUploadPhoto());
        this.setUploadFile(vo.getUploadFile());
        this.setStratDate(vo.getStratDate());
        this.setEndDateTime(vo.getEndDateTime());
        this.setHobby(vo.getHobby());
        this.setDeleted(vo.getDeleted());
        this.setParentId(vo.getParentId());
        this.setCreateId(vo.getCreateId());
        this.setCreateName(vo.getCreateName());
        this.setCreateTime(vo.getCreateTime());
        this.setModifyId(vo.getModifyId());
        this.setModifyName(vo.getModifyName());
        this.setModifyTime(vo.getModifyTime());
        this.setExtension(vo.getExtension());
	}
}
