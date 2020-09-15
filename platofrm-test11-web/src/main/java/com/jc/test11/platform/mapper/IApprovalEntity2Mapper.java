
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.ApprovalEntity2Entity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName ApprovalEntity2Mapper.java
 * Description 树形审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface IApprovalEntity2Mapper extends BaseMapper<ApprovalEntity2Entity>
{
    /**
     * 根据流程实例ID获取对象
     * @param processInstanceId
     * @return
     */
    @Select("select * from approval_entity2 where process_instance_id = #{processInstanceId}")
    ApprovalEntity2Entity findApprovalEntity2ByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
