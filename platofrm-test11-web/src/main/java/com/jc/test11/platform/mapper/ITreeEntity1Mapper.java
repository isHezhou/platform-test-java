
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.TreeEntity1Entity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName TreeEntity1Mapper.java
 * Description 树形工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface ITreeEntity1Mapper extends BaseMapper<TreeEntity1Entity>
{
    /**
     * 根据流程实例ID获取对象
     * @param processInstanceId
     * @return
     */
    @Select("select * from tree_entity1 where process_instance_id = #{processInstanceId}")
    TreeEntity1Entity findTreeEntity1ByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
