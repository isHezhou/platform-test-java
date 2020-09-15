package com.jc.test11.platform.service;

import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.result.PageInfo;
import com.jc.platform.common.result.ResultModel;
import org.springframework.transaction.annotation.Transactional;
import com.jc.test11.platform.vo.ApprovalEntity2VO;
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
 * ClassName IApprovalEntity2Service.java
 * Description 树形审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Validated
public interface IApprovalEntity2Service
{
	/**
	 * 保存
	 *
	 * @param approvalEntity2 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<ApprovalEntity2VO> insert(@Valid ApprovalEntity2VO approvalEntity2);

	/**
	 * 修改
	 *
	 * @param approvalEntity2 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<ApprovalEntity2VO> update(ApprovalEntity2VO approvalEntity2);


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
	ResultModel<ApprovalEntity2VO> get(Long id);

	/**
	 * 查询对象集合
	 *
	 * @param queryParam 查询参数
	 * @return 对象集合
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<ApprovalEntity2VO>> find(QueryParam queryParam);

	/**
	 * 分页查询
	 *
	 * @param queryParam 查询参数
	 * @return 分页对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<PageInfo<ApprovalEntity2VO>> finds(QueryParam queryParam);

    /**
	 * 查询对象树
	 *
	 * @param queryParam 查询参数
	 * @return 对象树
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<ApprovalEntity2VO>> treeList(QueryParam queryParam);

	/**
	 * 提交
	 *
	 * @param approvalEntity2VO
	 * @return
	 */
    ResultModel<ApprovalEntity2VO> submit(ApprovalEntity2VO approvalEntity2VO);


}
