package com.jc.test11.platform.service;

import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.result.PageInfo;
import com.jc.platform.common.result.ResultModel;
import org.springframework.transaction.annotation.Transactional;
import com.jc.test11.platform.vo.TreeEntityVO;
import com.jc.test11.platform.group.Update;
import com.jc.test11.platform.vo.FileInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.*;


/**
 * ClassName ITreeEntityService.java
 * Description 树形菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Validated
public interface ITreeEntityService
{
	/**
	 * 保存
	 *
	 * @param treeEntity 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TreeEntityVO> insert(@Valid TreeEntityVO treeEntity);

	/**
	 * 修改
	 *
	 * @param treeEntity 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TreeEntityVO> update(TreeEntityVO treeEntity);


	/**
	 * 删除
	 *
	 * @param id 主键
	 * @return 是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<Boolean> delete(Long id);

	/**
	 * 批量删除
	 *
	 * @param ids 主键
	 * @return 是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<Boolean> deleteBatch(List<Long> ids);

	/**
	 * 校验属性是否存在，存在返回false，不存在返回true
	 *
	 * @param id       主键
	 * @param property 属性名称
	 * @param value    属性值
	 * @return 检查结果
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	Boolean checkNotExists(Long id, String property, String value);

	/**
	 * 查询对象
	 *
	 * @param id 主键
	 * @return 对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<TreeEntityVO> get(Long id);

	/**
	 * 查询对象集合
	 *
	 * @param queryParam 查询参数
	 * @return 对象集合
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<TreeEntityVO>> find(QueryParam queryParam);

	/**
	 * 分页查询
	 *
	 * @param queryParam 查询参数
	 * @return 分页对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<PageInfo<TreeEntityVO>> finds(QueryParam queryParam);

    /**
	 * 查询对象树
	 *
	 * @param queryParam 查询参数
	 * @return 对象树
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<TreeEntityVO>> treeList(QueryParam queryParam);


	/**
	 * 新增文件信息
	 *
	 * @param fileInfoVO
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	FileInfoVO insertFileInfo(FileInfoVO fileInfoVO);

	/**
	 * 查询文件对象
	 *
	 * @param id
	 * @return 对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	FileInfoVO getFileInfo(Long id);

	/**
	 * 根据业务字段code 删除文件信息
	 * @param businessCode
	 */
	@Transactional(rollbackFor = Exception.class)
	void deleteByBusinessCode(String businessCode);

	/**
	 * 根据业务code获取所有文件信息
	 *
	 * @param businessCode
	 * @return
	 */
	List<FileInfoVO> findAllByBusinessCode(String businessCode);

}
