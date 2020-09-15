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
import com.jc.test11.platform.service.IApprovalEntityService;
import com.jc.platform.workflow.common.model.impl.PlatformHisTaskImpl;
import com.jc.platform.workflow.common.model.ProcessVariable;
import com.jc.platform.workflow.common.model.PlatformHisTask;
import com.jc.test11.platform.dto.WorkflowDto;
import com.jc.test11.platform.enums.WorkState;
import com.jc.test11.platform.vo.ApprovalEntityVO;
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
 * ClassName ApprovalEntityController.java
 * Description 普通审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@RestController
@Api(value = "/approvalEntity", tags = "普通审批菜单")
@RequestMapping("approvalEntity")
public class ApprovalEntityController
{

    private final IApprovalEntityService approvalEntityService;

    public ApprovalEntityController(IApprovalEntityService approvalEntityService)
    {
        this.approvalEntityService = approvalEntityService;
    }

	/**
	 * 保存
	 *
	 * @param approvalEntityVO 实体
	 * @return 对象vo
	 */
	@ApiOperation(value = "普通审批菜单保存", notes = "普通审批菜单保存" )
	@ApiImplicitParam(name = "approvalEntity", value = "普通审批菜单实体", required = true, dataType = "ApprovalEntityVO")
	@PostMapping(value = "/insert", produces = { "application/json;charset=UTF-8" })
	public ResultModel<ApprovalEntityVO> insert(@RequestBody ApprovalEntityVO approvalEntityVO)
	{
		try
		{
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            approvalEntityVO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            approvalEntityVO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            approvalEntityVO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            approvalEntityVO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            approvalEntityVO.setDeleted(false);
			approvalEntityVO.setCreateId(userInfo.getUserId());
			approvalEntityVO.setCreateName(userInfo.getUserName());
			approvalEntityVO.setCreateTime(Calendar.getInstance().getTime());
			return approvalEntityService.insert(approvalEntityVO);
		}
		catch (Exception exception)
		{
			log.error("普通审批菜单保存出错:", exception);
			throw exception;
		}
	}
	/**
     * 修改
     *
     * @param approvalEntityVO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "普通审批菜单修改", notes = "普通审批菜单修改" )
    @ApiImplicitParam(name = "approvalEntity", value = "普通审批菜单实体", required = true, dataType = "ApprovalEntityVO")
    @PostMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
    public ResultModel<ApprovalEntityVO> update(@RequestBody ApprovalEntityVO approvalEntityVO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
			approvalEntityVO.setModifyId(userInfo.getUserId());
			approvalEntityVO.setModifyName(userInfo.getUserName());
			approvalEntityVO.setModifyTime(Calendar.getInstance().getTime());

            return approvalEntityService.update(approvalEntityVO);
        }
        catch (Exception exception)
        {
            log.error("普通审批菜单修改出错:", exception);
            throw exception;
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "普通审批菜单删除", notes = "普通审批菜单删除" )
    @ApiImplicitParam(name = "id", value = "普通审批菜单实体ID", required = true, dataType = "Long")
    @PostMapping("/delete")
    public ResultModel<Boolean> delete(@RequestParam Long id)
    {
        try
        {
            return approvalEntityService.delete(id);
        }
        catch (Exception exception)
        {
            log.error("普通审批菜单删除出错:", exception);
            throw exception;
        }
    }
    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    @ApiOperation(value = "普通审批菜单批量删除", notes = "普通审批菜单批量删除" )
    @ApiImplicitParam(name = "ids", value = "普通审批菜单实体ID集合", required = true, dataType = "List<Long>")
    @PostMapping("/deleteBatch")
    public ResultModel<Boolean> deleteBatch(@RequestBody List<Long> ids)
    {
        try
        {
            return approvalEntityService.deleteBatch(ids);
        }
        catch (Exception exception)
        {
            log.error("普通审批菜单批量删除出错:", exception);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "普通审批菜单实体ID", dataType = "Long"), @ApiImplicitParam(name = "property", value = "属性名称", required = true, dataType = "String"),
    @ApiImplicitParam(name = "value", value = "属性值", required = true, dataType = "String") })
    @GetMapping("/checkNotExists")
    public ResultModel<Boolean> checkNotExists(@RequestParam(required = false) Long id, @RequestParam String property, @RequestParam String value)
    {
        try
        {
            return ResultModel.success(approvalEntityService.checkNotExists(id, property, value));
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
    public ResultModel<ApprovalEntityVO> get(@RequestParam Long id)
    {
        try
        {
            return approvalEntityService.get(id);
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
    public ResultModel<List<ApprovalEntityVO>> find(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(QueryConstant.Q_PARAM) String queryString)
    {
        try
        {
            QueryParam queryParam = new QueryParam();
            queryParam.setSortOrder(sortOrder);
            queryParam.setSortBy(sortBy);
            queryParam.setQ(queryString);
            return approvalEntityService.find(queryParam);
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
    public ResultModel<PageInfo<ApprovalEntityVO>> finds(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder,
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
            return approvalEntityService.finds(queryParam);
        }
        catch (Exception exception)
        {
            log.error("分页查询出错:", exception);
            throw exception;
        }
    }




    /**
     * 修改工作流form表单
     *
     * @param approvalEntityVO 实体
     * @return 对象vo
     */
    @ApiOperation(value = "设备工作流修改", notes = "设备工作流修改" )
    @ApiImplicitParam(name = "approvalEntity", value = "设备工作流实体", required = false, dataType = "ApprovalEntityVO")
    @PostMapping(value = "/updateByWorkflow", produces = { "application/json;charset=UTF-8" })
    public ResultModel<ApprovalEntityVO> updateByWorkflow(@RequestBody(required = false) ApprovalEntityVO approvalEntityVO,
                                                                  @RequestHeader("state") String state)
    {
        try
        {
            // 1 是结束  0 未结束
            if(!state.equals("0")){
                approvalEntityVO.setWorkflowState(state);
            }

            UserInfo userInfo = RequestUtil.getLoginUser();
			approvalEntityVO.setModifyId(userInfo.getUserId());
			approvalEntityVO.setModifyName(userInfo.getUserName());
			approvalEntityVO.setModifyTime(Calendar.getInstance().getTime());

            return approvalEntityService.update(approvalEntityVO);
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
     * @param approvalEntityVO 数据对象
     * @return approvalEntityVO 对象
     */
    @ApiOperation(value = "提交工作流", notes = "提交工作流")
    @ApiImplicitParam(name = "approvalEntityVO", value = "数据对象", required = true, dataType = "ApprovalEntityVO")
    @PostMapping("/submit")
    public ResultModel<ApprovalEntityVO> submit(@RequestBody ApprovalEntityVO approvalEntityVO)
    {
        try
        {
            UserInfo userInfo = RequestUtil.getLoginUser();
            //赋值集团ID
            approvalEntityVO.setGroupId(userInfo.getGroupId());
            //赋值机构ID
            approvalEntityVO.setOrgId(userInfo.getOrgId());
            //赋值租户ID
            approvalEntityVO.setTenantId(userInfo.getTenantId());
            //赋值部门ID
            approvalEntityVO.setDeptId(userInfo.getDeptId());
            //逻辑删除
            approvalEntityVO.setDeleted(false);
			approvalEntityVO.setCreateId(userInfo.getUserId());
			approvalEntityVO.setCreateName(userInfo.getUserName());
			approvalEntityVO.setCreateTime(Calendar.getInstance().getTime());

            return approvalEntityService.submit(approvalEntityVO);
        }
        catch (Exception exception)
        {
            log.error("提交工作流:", exception);
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

            ResultModel<PageInfo<ApprovalEntityVO>> resultModel = approvalEntityService.finds(queryParam);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ApprovalEntityVO.class).autoCloseStream(Boolean.FALSE).sheet("普通审批菜单记录")
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
                ResultModel<PageInfo<ApprovalEntityVO>> resultModel = approvalEntityService.finds(queryParam);
                //先写入sheet0对象，依次递增
                WriteSheet mainSheet = EasyExcel.writerSheet(i-1, "普通审批菜单记录 第"+i+"页").head(ApprovalEntityVO.class).build();
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
    public ResultModel<List<ApprovalEntityVO>> importExcel(@RequestBody FileInfoVO fileInfoVO) {
       try{
            String base64Str = fileInfoVO.getBase64Str();
            //截取逗号之后的
            base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
            InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64Str));

            ExcelListener excelListener = new ExcelListener();
            ExcelReader excelReader = EasyExcel.read(in, ApprovalEntityVO.class, excelListener).build();

            //新增对象集合
            List<ApprovalEntityVO> importExcelList = new ArrayList<>();
            //获取excel的sheet数
            List<Sheet> sheets = excelReader.getSheets();
            for(int i=0;i<sheets.size();i++){
                //获取sheet对象
                ReadSheet readSheet = EasyExcel.readSheet(i).build();
                //读取数据
                excelReader.read(readSheet);
                List<Object> approvalEntityList = excelListener.getData();
                for (int j=0; j<approvalEntityList.size();j++){
                    ApprovalEntityVO approvalEntityVO = (ApprovalEntityVO) approvalEntityList.get(j);
                    UserInfo userInfo = RequestUtil.getLoginUser();
                    approvalEntityVO.setCreateId(userInfo.getUserId());
                    approvalEntityVO.setCreateName(userInfo.getUserName());
                    approvalEntityVO.setCreateTime(Calendar.getInstance().getTime());
                    //赋值集团ID
                    approvalEntityVO.setGroupId(userInfo.getGroupId());
                    //赋值机构ID
                    approvalEntityVO.setOrgId(userInfo.getOrgId());
                    //赋值租户ID
                    approvalEntityVO.setTenantId(userInfo.getTenantId());
                    //赋值部门ID
                    approvalEntityVO.setDeptId(userInfo.getDeptId());
                    approvalEntityVO.setDeleted(false);
                    importExcelList.add(approvalEntityVO);
                }
                //清空list数据
                excelListener.getData().clear();
            }
            ApprovalEntityVO approvalEntityVO = new ApprovalEntityVO();
            approvalEntityVO.setImportExcelList(importExcelList);
            return approvalEntityService.importData(approvalEntityVO.getImportExcelList());
        }catch (Exception exception){
            log.error("导入Excel数据:", exception);
            throw exception;
        }
    }
}
