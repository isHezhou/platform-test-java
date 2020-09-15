package com.jc.test11.platform.controller;

import javax.servlet.http.HttpServletResponse;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.jc.test11.platform.utils.ExcelListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.jc.test11.platform.vo.FileInfoVO;
import java.io.IOException;
import com.alibaba.excel.EasyExcel;
import com.jc.platform.common.result.PageInfo;
import com.jc.test11.platform.service.ITheOrderService;
import com.jc.test11.platform.vo.TheOrderVO;
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
 * ClassName TheOrderController.java
 * Description 订单菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@RestController
@Api(value = "/theOrder", tags = "订单菜单")
@RequestMapping("theOrder")
public class TheOrderController
{

    private final ITheOrderService theOrderService;

    public TheOrderController(ITheOrderService theOrderService)
    {
        this.theOrderService = theOrderService;
    }

	/**
	 * 保存
	 *
	 * @param theOrderVO 实体
	 * @return 对象vo
	 */
	@ApiOperation(value = "订单菜单保存", notes = "订单菜单保存" )
	@ApiImplicitParam(name = "theOrder", value = "订单菜单实体", required = true, dataType = "TheOrderVO")
	@PostMapping(value = "/insert", produces = { "application/json;charset=UTF-8" })
	public ResultModel<TheOrderVO> insert(@RequestBody TheOrderVO theOrderVO)
	{
		try
		{
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            theOrderVO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            theOrderVO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            theOrderVO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            theOrderVO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            theOrderVO.setDeleted(false);
			theOrderVO.setCreateId(userInfo.getUserId());
			theOrderVO.setCreateName(userInfo.getUserName());
			theOrderVO.setCreateTime(Calendar.getInstance().getTime());
            if (!theOrderService.checkNotExists(theOrderVO.getId(), "theOrderName", theOrderVO.getTheOrderName()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "订单名称已存在");
            }
            if (!theOrderService.checkNotExists(theOrderVO.getId(), "theOrderCode", theOrderVO.getTheOrderCode()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "订单编号已存在");
            }
			return theOrderService.insert(theOrderVO);
		}
		catch (Exception exception)
		{
			log.error("订单菜单保存出错:", exception);
			throw exception;
		}
	}
	/**
     * 修改
     *
     * @param theOrderVO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "订单菜单修改", notes = "订单菜单修改" )
    @ApiImplicitParam(name = "theOrder", value = "订单菜单实体", required = true, dataType = "TheOrderVO")
    @PostMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
    public ResultModel<TheOrderVO> update(@RequestBody TheOrderVO theOrderVO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
			theOrderVO.setModifyId(userInfo.getUserId());
			theOrderVO.setModifyName(userInfo.getUserName());
			theOrderVO.setModifyTime(Calendar.getInstance().getTime());
            if (!theOrderService.checkNotExists(theOrderVO.getId(), "theOrderName", theOrderVO.getTheOrderName()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "订单名称已存在");
            }
            if (!theOrderService.checkNotExists(theOrderVO.getId(), "theOrderCode", theOrderVO.getTheOrderCode()+""))
            {
                return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "订单编号已存在");
            }

            return theOrderService.update(theOrderVO);
        }
        catch (Exception exception)
        {
            log.error("订单菜单修改出错:", exception);
            throw exception;
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "订单菜单删除", notes = "订单菜单删除" )
    @ApiImplicitParam(name = "id", value = "订单菜单实体ID", required = true, dataType = "Long")
    @PostMapping("/delete")
    public ResultModel<Boolean> delete(@RequestParam Long id)
    {
        try
        {
            return theOrderService.delete(id);
        }
        catch (Exception exception)
        {
            log.error("订单菜单删除出错:", exception);
            throw exception;
        }
    }
    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    @ApiOperation(value = "订单菜单批量删除", notes = "订单菜单批量删除" )
    @ApiImplicitParam(name = "ids", value = "订单菜单实体ID集合", required = true, dataType = "List<Long>")
    @PostMapping("/deleteBatch")
    public ResultModel<Boolean> deleteBatch(@RequestBody List<Long> ids)
    {
        try
        {
            return theOrderService.deleteBatch(ids);
        }
        catch (Exception exception)
        {
            log.error("订单菜单批量删除出错:", exception);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "订单菜单实体ID", dataType = "Long"), @ApiImplicitParam(name = "property", value = "属性名称", required = true, dataType = "String"),
    @ApiImplicitParam(name = "value", value = "属性值", required = true, dataType = "String") })
    @GetMapping("/checkNotExists")
    public ResultModel<Boolean> checkNotExists(@RequestParam(required = false) Long id, @RequestParam String property, @RequestParam String value)
    {
        try
        {
            return ResultModel.success(theOrderService.checkNotExists(id, property, value));
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
    public ResultModel<TheOrderVO> get(@RequestParam Long id)
    {
        try
        {
            return theOrderService.get(id);
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
    public ResultModel<List<TheOrderVO>> find(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return theOrderService.find(queryParam);
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
    public ResultModel<PageInfo<TheOrderVO>> finds(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
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
            return theOrderService.finds(queryParam);
        }
        catch (Exception exception)
        {
            log.error("分页查询出错:", exception);
            throw exception;
        }
    }




    /**
     * 导出Excel
     *
     * @param pageNum     查询页码
     * @param pageSize    每页记录数
     * @param sortBy      排序字段
     * @param sortOrder   排序方式
     * @param queryString 查询条件,查询条件按规则拼接后的字符串
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "查询页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段,和属性名称一致", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sortOrder", value = "排序方式", allowableValues = "ASC,DESC", required = true, dataType = "String"),
            @ApiImplicitParam(name = "q", value = "查询条件,查询条件按规则拼接后的字符串", required = true, dataType = "String") })
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
                         @RequestParam(QueryConstant.Q_PARAM) String queryString, HttpServletResponse response) throws IOException
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setPageNum(pageNum);
            queryParam.setPageSize(pageSize);
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);

            ResultModel<PageInfo<TheOrderVO>> resultModel = theOrderService.finds(queryParam);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), TheOrderVO.class).autoCloseStream(Boolean.FALSE).sheet("订单菜单记录")
                    .doWrite(resultModel.getData().getList());
        }
        catch (Exception exception)
        {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResultModel<Boolean> model = ResultModel.failed("下载文件失败");
            response.getWriter().println(JSON.toJSONString(model));
            log.error("dow file failed", exception);
        }
    }
    /**
     * 导出全部Excel
     *
     * @param pageNum     查询页码
     * @param pageSize    每页记录数
     * @param sortBy      排序字段
     * @param sortOrder   排序方式
     * @param queryString 查询条件,查询条件按规则拼接后的字符串
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "查询页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段,和属性名称一致", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sortOrder", value = "排序方式", allowableValues = "ASC,DESC", required = true, dataType = "String"),
            @ApiImplicitParam(name = "total", value = "总页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "q", value = "查询条件,查询条件按规则拼接后的字符串", required = true, dataType = "String") })
    @GetMapping("/exportExcelAll")
    public void exportExcelAll(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
                               @RequestParam Integer total, @RequestParam(QueryConstant.Q_PARAM) String queryString, HttpServletResponse response) throws IOException
    {
        try
        {

            QueryParam queryParam = new QueryParam();
            queryParam.setPageNum(pageNum);
            queryParam.setPageSize(pageSize);
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=测试实体2.xlsx");

            double ceil = Math.ceil(Double.valueOf(total) / Double.valueOf(pageSize));
            //新建ExcelWriter
            //写入多个sheet
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            for (int i = 1; i <= ceil; i++) {
                queryParam.setPageNum(i);
                ResultModel<PageInfo<TheOrderVO>> resultModel = theOrderService.finds(queryParam);
                //先写入sheet0对象，依次递增
                WriteSheet mainSheet = EasyExcel.writerSheet(i-1, "订单菜单记录 第"+i+"页").head(TheOrderVO.class).build();
                //向sheet0写入数据 传入空list这样只导出表头
                excelWriter.write(resultModel.getData().getList(),mainSheet);
            }
            //关闭流
            excelWriter.finish();
        }
        catch (Exception exception)
        {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResultModel<Boolean> model = ResultModel.failed("下载文件失败");
            response.getWriter().println(JSON.toJSONString(model));
            log.error("upload file failed", exception);
        }
    }
    /**
     * 导入Excel数据
     */
    @ApiOperation(value = "导入Excel数据", notes = "导入Excel数据")
    @ApiImplicitParam(name = "fileInfoVO", value = "excel文件信息", required = true, dataType = "FileInfoVO")
    @PostMapping(value = "/importExcel", produces = { "application/json;charset=UTF-8" })
    public ResultModel<List<TheOrderVO>> importExcel(@RequestBody FileInfoVO fileInfoVO) {
       try{
            String base64Str = fileInfoVO.getBase64Str();
            //截取逗号之后的
            base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
            InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64Str));

            ExcelListener excelListener = new ExcelListener();
            ExcelReader excelReader = EasyExcel.read(in, TheOrderVO.class, excelListener).build();

            //新增对象集合
            List<TheOrderVO> importExcelList = new ArrayList<>();
            //获取excel的sheet数
            List<Sheet> sheets = excelReader.getSheets();
            for(int i=0;i<sheets.size();i++){
                //获取sheet对象
                ReadSheet readSheet = EasyExcel.readSheet(i).build();
                //读取数据
                excelReader.read(readSheet);
                List<Object> theOrderList = excelListener.getData();
                for (int j=0; j<theOrderList.size();j++){
                    TheOrderVO theOrderVO = (TheOrderVO) theOrderList.get(j);
                    UserInfo userInfo = RequestUtil.getLoginUser();
                    theOrderVO.setCreateId(userInfo.getUserId());
                    theOrderVO.setCreateName(userInfo.getUserName());
                    theOrderVO.setCreateTime(Calendar.getInstance().getTime());
                    //赋值集团ID
                    theOrderVO.setGroupId(userInfo.getGroupId());
                    //赋值机构ID
                    theOrderVO.setOrgId(userInfo.getOrgId());
                    //赋值租户ID
                    theOrderVO.setTenantId(userInfo.getTenantId());
                    //赋值部门ID
                    theOrderVO.setDeptId(userInfo.getDeptId());
                    theOrderVO.setDeleted(false);
                    importExcelList.add(theOrderVO);
                }
                //清空list数据
                excelListener.getData().clear();
            }
            TheOrderVO theOrderVO = new TheOrderVO();
            theOrderVO.setImportExcelList(importExcelList);
            return theOrderService.importData(theOrderVO.getImportExcelList());
        }catch (Exception exception){
            log.error("导入Excel数据:", exception);
            throw exception;
        }
    }
}
