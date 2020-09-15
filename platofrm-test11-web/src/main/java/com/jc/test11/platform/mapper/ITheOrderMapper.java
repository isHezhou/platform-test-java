
package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.TheOrderEntity;
import org.apache.ibatis.annotations.*;

/**
 * ClassName TheOrderMapper.java
 * Description 订单菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface ITheOrderMapper extends BaseMapper<TheOrderEntity>
{
}
