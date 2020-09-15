package com.jc.test11.platform.controller;

import com.jc.platform.common.result.PageInfo;
import com.jc.test11.platform.service.ITreeEntity1Service;
import com.jc.platform.workflow.common.model.impl.PlatformHisTaskImpl;
import com.jc.platform.workflow.common.model.ProcessVariable;
import com.jc.platform.workflow.common.model.PlatformHisTask;
import com.jc.test11.platform.dto.WorkflowDto;
import com.jc.test11.platform.enums.WorkState;
import com.jc.test11.platform.vo.TreeEntity1VO;
import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.model.UserInfo;
import com.jc.platform.common.search.QueryConstant;
import com.jc.platform.common.result.ResultCodeEnum;
import com.jc.platform.common.result.ResultModel;
import com.jc.platform.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import java.util.*;

/**
 * ClassName TreeEntity1Controller.java
 * Description 树形工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@RestController
@Api(value = "/treeEntity1", tags = "树形工作流菜单")
@RequestMapping("treeEntity1")
public class TreeEntity1Controller
{

    private final ITreeEntity1Service treeEntity1Service;

    public TreeEntity1Controller(ITreeEntity1Service treeEntity1Service)
    {
        this.treeEntity1Service = treeEntity1Service;
    }

	/**
	 * 保存
	 *
	 * @param treeEntity1VO 实体
	 * @return 对象vo
	 */
	@ApiOperation(value = "树形工作流菜单保存", notes = "树形工作流菜单保存" )
	@ApiImplicitParam(name = "treeEntity1", value = "树形工作流菜单实体", required = true, dataType = "TreeEntity1VO")
	@PostMapping(value = "/insert", produces = { "application/json;charset=UTF-8" })
	public ResultModel<TreeEntity1VO> insert(@RequestBody TreeEntity1VO treeEntity1VO)
	{
		try
		{
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            treeEntity1VO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            treeEntity1VO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            treeEntity1VO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            treeEntity1VO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            treeEntity1VO.setDeleted(false);
			treeEntity1VO.setCreateId(userInfo.getUserId());
			treeEntity1VO.setCreateName(userInfo.getUserName());
			treeEntity1VO.setCreateTime(Calendar.getInstance().getTime());
            if (!treeEntity1Service.checkNotExists(treeEntity1VO.getId(), "content2", treeEntity1VO.getContent2()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "内容2已存在");
            }
            if (!treeEntity1Service.checkNotExists(treeEntity1VO.getId(), "content3", treeEntity1VO.getContent3()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "内容3已存在");
            }
			return treeEntity1Service.insert(treeEntity1VO);
		}
		catch (Exception exception)
		{
			log.error("树形工作流菜单保存出错:", exception);
			throw exception;
		}
	}
	/**
     * 修改
     *
     * @param treeEntity1VO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "树形工作流菜单修改", notes = "树形工作流菜单修改" )
    @ApiImplicitParam(name = "treeEntity1", value = "树形工作流菜单实体", required = true, dataType = "TreeEntity1VO")
    @PostMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
    public ResultModel<TreeEntity1VO> update(@RequestBody TreeEntity1VO treeEntity1VO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
			treeEntity1VO.setModifyId(userInfo.getUserId());
			treeEntity1VO.setModifyName(userInfo.getUserName());
			treeEntity1VO.setModifyTime(Calendar.getInstance().getTime());
            if (!treeEntity1Service.checkNotExists(treeEntity1VO.getId(), "content2", treeEntity1VO.getContent2()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "内容2已存在");
            }
            if (!treeEntity1Service.checkNotExists(treeEntity1VO.getId(), "content3", treeEntity1VO.getContent3()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "内容3已存在");
            }

            return treeEntity1Service.update(treeEntity1VO);
        }
        catch (Exception exception)
        {
            log.error("树形工作流菜单修改出错:", exception);
            throw exception;
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "树形工作流菜单删除", notes = "树形工作流菜单删除" )
    @ApiImplicitParam(name = "id", value = "树形工作流菜单实体ID", required = true, dataType = "Long")
    @PostMapping("/delete")
    public ResultModel<Boolean> delete(@RequestParam Long id)
    {
        try
        {
            return treeEntity1Service.delete(id);
        }
        catch (Exception exception)
        {
            log.error("树形工作流菜单删除出错:", exception);
            throw exception;
        }
    }
    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    @ApiOperation(value = "树形工作流菜单批量删除", notes = "树形工作流菜单批量删除" )
    @ApiImplicitParam(name = "ids", value = "树形工作流菜单实体ID集合", required = true, dataType = "List<Long>")
    @PostMapping("/deleteBatch")
    public ResultModel<Boolean> deleteBatch(@RequestBody List<Long> ids)
    {
        try
        {
            return treeEntity1Service.deleteBatch(ids);
        }
        catch (Exception exception)
        {
            log.error("树形工作流菜单批量删除出错:", exception);
            throw exception;
        }
    }
    /**
     * 校验属性是否存在，存在返回false，不存在返回true
     *
     * @param id       主键
     * @param property 属性名称
     * @param value    属性值
     * @return 检查结果
     */
    @ApiOperation(value = "校验属性是否存在", notes = "校验属性是否存在，存在返回false，不存在返回true" )
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "树形工作流菜单实体ID", dataType = "Long"), @ApiImplicitParam(name = "property", value = "属性名称", required = true, dataType = "String"),
    @ApiImplicitParam(name = "value", value = "属性值", required = true, dataType = "String") })
    @GetMapping("/checkNotExists")
    public ResultModel<Boolean> checkNotExists(@RequestParam(required = false) Long id, @RequestParam String property, @RequestParam String value)
    {
        try
        {
            return ResultModel.success(treeEntity1Service.checkNotExists(id, property, value));
        }
        catch (Exception exception)
        {
            log.error("校验属性是否存在出错:", exception);
            throw exception;
        }
    }

    /**
     * 查询对象
     *
     * @param id 主键
     * @return 对象
     */
    @ApiOperation(value = "查询对象", notes = "查询对象" )
    @ApiImplicitParam(name = "id", value = "实体ID", required = true, dataType = "Long")
    @GetMapping("/get")
    public ResultModel<TreeEntity1VO> get(@RequestParam Long id)
    {
        try
        {
            return treeEntity1Service.get(id);
        }
        catch (Exception exception)
        {
            log.error("查询对象出错:", exception);
            throw exception;
        }
    }

	/**
	 * 查询对象集合
	 *
	 * @param sortBy      排序字段
	 * @param sortOrder   排序方式
	 * @param queryString 查询条件,查询条件按规则拼接后的字符串
	 * @return 对象集合
	 */
	@ApiOperation(value = "查询对象集合", notes = "查询对象集合")
	@ApiImplicitParams({ @ApiImplicitParam(name = "sortBy", value = "排序字段,和属性名称一致", dataType = "String"),
			@ApiImplicitParam(name = "sortOrder", value = "排序方式", allowableValues = "ASC,DESC", dataType = "String"),
			@ApiImplicitParam(name = "q", value = "查询条件,查询条件按规则拼接后的字符串", required = true, dataType = "String") })
	@GetMapping("/find")
    public ResultModel<List<TreeEntity1VO>> find(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return treeEntity1Service.find(queryParam);
        }
        catch (Exception exception)
        {
            log.error("查询对象集合出错:", exception);
            throw exception;
        }
    }

	/**
	 * 分页查询
	 *
	 * @param pageNum     查询页码
	 * @param pageSize    每页记录数
	 * @param sortBy      排序字段
	 * @param sortOrder   排序方式
	 * @param queryString 查询条件,查询条件按规则拼接后的字符串
	 * @return 分页对象
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "查询页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "sortBy", value = "排序字段,和属性名称一致", required = true, dataType = "String"),
			@ApiImplicitParam(name = "sortOrder", value = "排序方式", allowableValues = "ASC,DESC", required = true, dataType = "String"),
			@ApiImplicitParam(name = "q", value = "查询条件,查询条件按规则拼接后的字符串", required = true, dataType = "String") })
	@GetMapping("/finds")
    public ResultModel<PageInfo<TreeEntity1VO>> finds(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
           @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setPageNum(pageNum);
            queryParam.setPageSize(pageSize);
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return treeEntity1Service.finds(queryParam);
        }
        catch (Exception exception)
        {
            log.error("分页查询出错:", exception);
            throw exception;
        }
    }

    /**
     * 树形列表查询
     *
     * @param queryParam 查询参数
     * @return 树形列表
     */
	@ApiOperation(value = "树形列表查询", notes = "树形列表查询")
	@ApiImplicitParams({ @ApiImplicitParam(name = "sortBy", value = "排序字段,和属性名称一致", dataType = "String"),
			@ApiImplicitParam(name = "sortOrder", value = "排序方式", allowableValues = "ASC,DESC", dataType = "String"),
			@ApiImplicitParam(name = "q", value = "查询条件,查询条件按规则拼接后的字符串", required = true, dataType = "String") })
	@GetMapping("/treeList")
    public ResultModel<List<TreeEntity1VO>> treeList(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return treeEntity1Service.treeList(queryParam);
        }
        catch (Exception exception)
        {
            log.error("树形列表查询出错:", exception);
            throw exception;
        }
    }



    /**
     * 修改工作流form表单
     *
     * @param treeEntity1VO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "设备工作流修改", notes = "设备工作流修改" )
    @ApiImplicitParam(name = "treeEntity1", value = "设备工作流实体", required = false, dataType = "TreeEntity1VO")
    @PostMapping(value = "/updateByWorkflow", produces = { "application/json;charset=UTF-8" })
    public ResultModel<TreeEntity1VO> updateByWorkflow(@RequestBody(required = false) TreeEntity1VO treeEntity1VO,
                                                                  @RequestHeader("state") String state)
    {
        try
        {
            // 1 是结束  0 未结束
            if(!state.equals("0")){
                treeEntity1VO.setWorkflowState(state);
            }

            UserInfo userInfo = RequestUtil.getLoginUser();
			treeEntity1VO.setModifyId(userInfo.getUserId());
			treeEntity1VO.setModifyName(userInfo.getUserName());
			treeEntity1VO.setModifyTime(Calendar.getInstance().getTime());

            return treeEntity1Service.update(treeEntity1VO);
        }
        catch (Exception exception)
        {
            log.error("设备工作流修改:", exception);
            throw exception;
        }
    }

    /**
     * 提交工作流
     *
     * @param treeEntity1VO 数据对象
     * @return treeEntity1VO 对象
     */
    @ApiOperation(value = "提交工作流", notes = "提交工作流")
    @ApiImplicitParam(name = "treeEntity1VO", value = "数据对象", required = true, dataType = "TreeEntity1VO")
    @PostMapping("/submit")
    public ResultModel<TreeEntity1VO> submit(@RequestBody TreeEntity1VO treeEntity1VO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            treeEntity1VO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            treeEntity1VO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            treeEntity1VO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            treeEntity1VO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            treeEntity1VO.setDeleted(false);
			treeEntity1VO.setCreateId(userInfo.getUserId());
			treeEntity1VO.setCreateName(userInfo.getUserName());
			treeEntity1VO.setCreateTime(Calendar.getInstance().getTime());

            return treeEntity1Service.submit(treeEntity1VO);
        }
        catch (Exception exception)
        {
            log.error("提交工作流:", exception);
            throw exception;
        }
    }

}
