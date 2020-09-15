package com.jc.test11.platform.controller;

import javax.servlet.http.HttpServletResponse;
import com.jc.test11.platform.vo.FileInfoVO;
import java.io.IOException;
import com.jc.platform.common.result.PageInfo;
import com.jc.test11.platform.service.ITreeEntityService;
import java.net.URLEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import com.jc.test11.platform.vo.TreeEntityVO;
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
import java.util.*;

/**
 * ClassName TreeEntityController.java
 * Description 树形菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@RestController
@Api(value = "/treeEntity", tags = "树形菜单")
@RequestMapping("treeEntity")
public class TreeEntityController
{

    private final ITreeEntityService treeEntityService;

    public TreeEntityController(ITreeEntityService treeEntityService)
    {
        this.treeEntityService = treeEntityService;
    }

	/**
	 * 保存
	 *
	 * @param treeEntityVO 实体
	 * @return 对象vo
	 */
	@ApiOperation(value = "树形菜单保存", notes = "树形菜单保存" )
	@ApiImplicitParam(name = "treeEntity", value = "树形菜单实体", required = true, dataType = "TreeEntityVO")
	@PostMapping(value = "/insert", produces = { "application/json;charset=UTF-8" })
	public ResultModel<TreeEntityVO> insert(@RequestBody TreeEntityVO treeEntityVO)
	{
		try
		{
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            treeEntityVO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            treeEntityVO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            treeEntityVO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            treeEntityVO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            treeEntityVO.setDeleted(false);
			treeEntityVO.setCreateId(userInfo.getUserId());
			treeEntityVO.setCreateName(userInfo.getUserName());
			treeEntityVO.setCreateTime(Calendar.getInstance().getTime());
            if (!treeEntityService.checkNotExists(treeEntityVO.getId(), "taCode", treeEntityVO.getTaCode()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "编码已存在");
            }
            String uploadPhotoCode = UUID.randomUUID().toString().replace("-","");
            treeEntityVO.setUploadPhoto(uploadPhotoCode);
            String uploadFileCode = UUID.randomUUID().toString().replace("-","");
            treeEntityVO.setUploadFile(uploadFileCode);
			return treeEntityService.insert(treeEntityVO);
		}
		catch (Exception exception)
		{
			log.error("树形菜单保存出错:", exception);
			throw exception;
		}
	}
	/**
     * 修改
     *
     * @param treeEntityVO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "树形菜单修改", notes = "树形菜单修改" )
    @ApiImplicitParam(name = "treeEntity", value = "树形菜单实体", required = true, dataType = "TreeEntityVO")
    @PostMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
    public ResultModel<TreeEntityVO> update(@RequestBody TreeEntityVO treeEntityVO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
			treeEntityVO.setModifyId(userInfo.getUserId());
			treeEntityVO.setModifyName(userInfo.getUserName());
			treeEntityVO.setModifyTime(Calendar.getInstance().getTime());
            if (!treeEntityService.checkNotExists(treeEntityVO.getId(), "taCode", treeEntityVO.getTaCode()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "编码已存在");
            }

            return treeEntityService.update(treeEntityVO);
        }
        catch (Exception exception)
        {
            log.error("树形菜单修改出错:", exception);
            throw exception;
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "树形菜单删除", notes = "树形菜单删除" )
    @ApiImplicitParam(name = "id", value = "树形菜单实体ID", required = true, dataType = "Long")
    @PostMapping("/delete")
    public ResultModel<Boolean> delete(@RequestParam Long id)
    {
        try
        {
            return treeEntityService.delete(id);
        }
        catch (Exception exception)
        {
            log.error("树形菜单删除出错:", exception);
            throw exception;
        }
    }
    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    @ApiOperation(value = "树形菜单批量删除", notes = "树形菜单批量删除" )
    @ApiImplicitParam(name = "ids", value = "树形菜单实体ID集合", required = true, dataType = "List<Long>")
    @PostMapping("/deleteBatch")
    public ResultModel<Boolean> deleteBatch(@RequestBody List<Long> ids)
    {
        try
        {
            return treeEntityService.deleteBatch(ids);
        }
        catch (Exception exception)
        {
            log.error("树形菜单批量删除出错:", exception);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "树形菜单实体ID", dataType = "Long"), @ApiImplicitParam(name = "property", value = "属性名称", required = true, dataType = "String"),
    @ApiImplicitParam(name = "value", value = "属性值", required = true, dataType = "String") })
    @GetMapping("/checkNotExists")
    public ResultModel<Boolean> checkNotExists(@RequestParam(required = false) Long id, @RequestParam String property, @RequestParam String value)
    {
        try
        {
            return ResultModel.success(treeEntityService.checkNotExists(id, property, value));
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
    public ResultModel<TreeEntityVO> get(@RequestParam Long id)
    {
        try
        {
            return treeEntityService.get(id);
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
    public ResultModel<List<TreeEntityVO>> find(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return treeEntityService.find(queryParam);
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
    public ResultModel<PageInfo<TreeEntityVO>> finds(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
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
            return treeEntityService.finds(queryParam);
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
    public ResultModel<List<TreeEntityVO>> treeList(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return treeEntityService.treeList(queryParam);
        }
        catch (Exception exception)
        {
            log.error("树形列表查询出错:", exception);
            throw exception;
        }
    }



    /**
     * 下载
     *
     * @param id 文件详情ID
     */
    @ApiOperation(value = "下载", notes = "下载")
    @ApiImplicitParam(name = "id", value = "文件id", required = true, dataType = "Long")
    @GetMapping("/download")
    public void download(@RequestParam Long id, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";

            FileInfoVO fileInfoVO = treeEntityService.getFileInfo(id);

            String encode = "file";
            encode = URLEncoder.encode(fileInfoVO.getName(), "utf-8");
            String headerValue = String.format("attachment; filename=\"%s\"", encode);
            response.setHeader(headerKey, headerValue);
            IOUtils.write(fileInfoVO.getFile(),response.getOutputStream());
        } catch (Exception exception) {
            log.error("文件下载:", exception);
        }
    }

    /**
     * 根据code获取文件集合
     */
    @ApiOperation(value = "获取文件集合", notes = "获取文件集合")
    @ApiImplicitParam(name = "businessCode", value = "获取文件集合", required = true, dataType = "String")
    @GetMapping("/findAllByBusinessCode")
    public ResultModel<List<FileInfoVO>> findAllByBusinessCode(@RequestParam(value = "businessCode") String businessCode)
    {
        try
        {
            return ResultModel.success(treeEntityService.findAllByBusinessCode(businessCode));
        }
        catch (Exception exception)
        {
            log.error("根据code获取文件集合:", exception);
            throw exception;
        }
    }
}
