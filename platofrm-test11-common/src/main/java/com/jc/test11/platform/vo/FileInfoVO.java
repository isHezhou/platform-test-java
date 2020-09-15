package com.jc.test11.platform.vo;

import com.jc.platform.common.supers.AbstractVO;
import lombok.*;

import java.io.Serializable;

/**
 * ClassName FileInfoVO
 * Description 文件详情
 *
 * @author 平台管理员
 * @version 7.0.0
 * @date 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class FileInfoVO extends AbstractVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    private Long id;
    /**
     * 业务code
     *
     * 针对与字段，不针对于业务ID
     */
    private String businessCode;
    /**
     * 文件状态 成功 只读
     */
    private String status;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件base64字符串
     *
     * 用于前台接收
     */
    private String base64Str;
    /**
     * 文件流
     */
    private byte[] file;
}

