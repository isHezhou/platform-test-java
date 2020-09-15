
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.WorkflowEntity1Entity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName WorkflowEntity1Mapper.java
 * Description 普通工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface IWorkflowEntity1Mapper extends BaseMapper<WorkflowEntity1Entity>
{
    /**
     * 根据流程实例ID获取对象
     * @param processInstanceId
     * @return
     */
    @Select("select * from workflow_entity1 where process_instance_id = #{processInstanceId}")
    WorkflowEntity1Entity findWorkflowEntity1ByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
