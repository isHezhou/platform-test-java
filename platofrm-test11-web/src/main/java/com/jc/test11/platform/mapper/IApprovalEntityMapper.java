
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.ApprovalEntityEntity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName ApprovalEntityMapper.java
 * Description 普通审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface IApprovalEntityMapper extends BaseMapper<ApprovalEntityEntity>
{
    /**
     * 根据流程实例ID获取对象
     * @param processInstanceId
     * @return
     */
    @Select("select * from approval_entity where process_instance_id = #{processInstanceId}")
    ApprovalEntityEntity findApprovalEntityByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
