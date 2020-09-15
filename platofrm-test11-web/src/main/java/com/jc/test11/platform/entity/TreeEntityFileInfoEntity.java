package com.jc.test11.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jc.platform.common.supers.AbstractEntity;
import com.jc.test11.platform.vo.FileInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Base64;
import java.io.Serializable;

/**
 * ClassName TreeEntityFileInfoEntity
 * Description 树形菜单文件详情实体
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Data
@TableName("tree_entity_file_info")
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class TreeEntityFileInfoEntity extends AbstractEntity<FileInfoVO> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件详情 主键ID
     */
    @TableField(value = ("id"))
    private Long id;
    /**
     * businessId 与 上传字段 进行关联
     */
    @TableField(value = ("business_code"))
    private String businessCode;
    /**
     * 文件名称
     */
    @TableField(value = ("name"))
    private String name;
    /**
     * 文件二进制
     */
    @TableField(value = ("file"))
    private byte [] file;


    /**
     * 生成vo对象
     *
     * @return vo对象
     */
    public FileInfoVO copyToVO()
    {
        FileInfoVO vo = new FileInfoVO();
        vo.setId(this.getId());
        vo.setName(this.name);
        vo.setBusinessCode(this.businessCode);
        return vo;
    }

    /**
     * 从vo对象更新属性
     *
     * @param vo vo对象
     */
    public void copyFormVO(FileInfoVO vo) {
        this.setId(vo.getId());
        this.setBusinessCode(vo.getBusinessCode());
        this.setName(vo.getName());
        //判断base64Str中是否包含',', 去除逗号前的部分 例如: data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIA
        String base64Str = vo.getBase64Str();
        //截取逗号之后的
        base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
        //base64转二进制
        this.setFile(Base64.getDecoder().decode(base64Str));
    }
}
