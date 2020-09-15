package com.jc.test11.platform.service;

import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.result.PageInfo;
import com.jc.platform.common.result.ResultModel;
import org.springframework.transaction.annotation.Transactional;
import com.jc.test11.platform.vo.TheOrderVO;
import com.jc.test11.platform.group.Update;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.*;


/**
 * ClassName ITheOrderService.java
 * Description 订单菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Validated
public interface ITheOrderService
{
	/**
	 * 保存
	 *
	 * @param theOrder 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TheOrderVO> insert(@Valid TheOrderVO theOrder);

	/**
	 * 修改
	 *
	 * @param theOrder 实体
	 * @return 对象vo
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<TheOrderVO> update(TheOrderVO theOrder);


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
	ResultModel<TheOrderVO> get(Long id);

	/**
	 * 查询对象集合
	 *
	 * @param queryParam 查询参数
	 * @return 对象集合
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<List<TheOrderVO>> find(QueryParam queryParam);

	/**
	 * 分页查询
	 *
	 * @param queryParam 查询参数
	 * @return 分页对象
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	ResultModel<PageInfo<TheOrderVO>> finds(QueryParam queryParam);




	/**
	 * 通过Excel 导入数据
	 *
	 * @param theOrderList
	 */
	@Transactional(rollbackFor = Exception.class)
	ResultModel<List<TheOrderVO>> importData(@Valid List<TheOrderVO> theOrderList);
}
