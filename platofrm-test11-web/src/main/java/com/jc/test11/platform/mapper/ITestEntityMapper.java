
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.TestEntityEntity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName TestEntityMapper.java
 * Description 普通菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface ITestEntityMapper extends BaseMapper<TestEntityEntity>
{
}
