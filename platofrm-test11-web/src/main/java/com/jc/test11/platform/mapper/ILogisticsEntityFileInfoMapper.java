package com.jc.test11.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.test11.platform.entity.LogisticsEntityFileInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName IFileInfoMapper.java
 * Description 物流菜单 文件信息Mapper接口
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Mapper
public interface ILogisticsEntityFileInfoMapper extends BaseMapper<LogisticsEntityFileInfoEntity> {

    /**
     * 根据业务ID 查询该业务下所有的文件信息
     *
     * @param businessCode
     * @return
     */
    @Select("select id,business_code,name from logistics_entity_file_info where business_code = '${businessCode}'")
    List<LogisticsEntityFileInfoEntity> findAllByBusinessCode(@Param("businessCode") String businessCode);

    /**
     * 根据业务ID 删除该业务ID下所有文件信息
     *
     * @param businessCode
     */
    @Select("delete from logistics_entity_file_info where business_code = '${businessCode}'")
    void deleteByBusinessCode(@Param("businessCode") String businessCode);

}
