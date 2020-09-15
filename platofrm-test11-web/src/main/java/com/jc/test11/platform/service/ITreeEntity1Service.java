package com.jc.test11.platform.service;

import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.result.PageInfo;
import com.jc.platform.common.result.ResultModel;
import org.springframework.transaction.annotation.Transactional;
import com.jc.test11.platform.vo.TreeEntity1VO;
import com.jc.test11.platform.group.Update;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import com.jc.platform.workflow.common.model.PlatformHisTask;
import com.jc.platform.workflow.common.model.ProcessVariable;
import com.jc.platform.workflow.common.model.impl.PlatformHisTaskImpl;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.*;


/**
 * ClassName ITreeEntity1Service.java
 * Description 树形工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Validated
public interface ITreeEntity1Service
{
	/**
	 * 保存
	 *
	 * @param treeEntity1 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TreeEntity1VO> insert(@Valid TreeEntity1VO treeEntity1);

	/**
	 * 修改
	 *
	 * @param treeEntity1 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TreeEntity1VO> update(TreeEntity1VO treeEntity1);


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
	ResultModel<TreeEntity1VO> get(Long id);

	/**
	 * 查询对象集合
	 *
	 * @param queryParam 查询参数
	 * @return 对象集合
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<TreeEntity1VO>> find(QueryParam queryParam);

	/**
	 * 分页查询
	 *
	 * @param queryParam 查询参数
	 * @return 分页对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<PageInfo<TreeEntity1VO>> finds(QueryParam queryParam);

    /**
	 * 查询对象树
	 *
	 * @param queryParam 查询参数
	 * @return 对象树
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<TreeEntity1VO>> treeList(QueryParam queryParam);

	/**
	 * 提交
	 *
	 * @param treeEntity1VO
	 * @return
	 */
    ResultModel<TreeEntity1VO> submit(TreeEntity1VO treeEntity1VO);


}
