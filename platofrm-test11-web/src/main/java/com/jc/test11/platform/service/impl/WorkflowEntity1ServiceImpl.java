package com.jc.test11.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jc.platform.common.constants.GlobalConstant;
import com.jc.platform.common.constants.StringPool;
import com.jc.platform.common.model.QueryParam;
import com.jc.platform.common.result.PageInfo;
import com.jc.platform.common.result.ResultModel;
import com.jc.platform.common.search.SqlUtils;
import com.jc.platform.common.utils.BeanUtil;
import com.jc.platform.common.utils.RequestUtil;
import com.jc.test11.platform.vo.WorkflowEntity1VO;
import com.jc.test11.platform.entity.WorkflowEntity1Entity;
import com.jc.test11.platform.mapper.IWorkflowEntity1Mapper;
import com.jc.test11.platform.service.IWorkflowEntity1Service;
import com.jc.platform.mybatis.utils.CollectCovertUtil;
import com.jc.platform.mybatis.utils.ConditionUtil;
import org.activiti.bpmn.model.FormProperty;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import com.jc.platform.common.result.ResultCodeEnum;
import com.jc.test11.platform.enums.WorkState;
import com.jc.test11.platform.annotation.IsWorkflowEditField;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import com.jc.platform.workflowengine.feign.IProcessServiceFeign;
import org.springframework.data.domain.PageRequest;
import com.jc.platform.workflow.common.model.ProcessVariable;
import com.jc.platform.workflow.common.model.impl.PlatformHisTaskImpl;
import com.jc.platform.workflow.common.model.impl.PlatformProcessInstanceHistoryImpl;
import com.jc.platform.workflow.common.model.impl.PlatformProcessInstanceImpl;
import com.jc.platform.workflow.common.model.impl.PlatformTaskImpl;
import com.jc.platform.workflow.common.enums.VariableGlobalEnum;
import com.jc.platform.workflow.common.enums.VariableLocalEnum;

import lombok.SneakyThrows;
import com.jc.platform.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ClassName WorkflowEntity1ServiceImpl.java
 * Description 普通工作流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class WorkflowEntity1ServiceImpl implements IWorkflowEntity1Service
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_workflowEntity1";

    @Resource
    private RedisUtils<String, ResultModel<WorkflowEntity1VO>> redisUtils;
    @Resource
    private RedisUtils<String, WorkflowEntity1VO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<WorkflowEntity1VO>> pageRedisUtils;

	@Resource(type = IWorkflowEntity1Mapper.class)
	private IWorkflowEntity1Mapper workflowEntity1Mapper;
    /**
     * 流程中心
     */
    @Autowired
    private IProcessServiceFeign processServiceFeign;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<WorkflowEntity1VO> insert(WorkflowEntity1VO workflowEntity1VO)
	{

	    //工作流默认状态为草稿
        workflowEntity1VO.setWorkflowState(WorkState.THE_DRAFT.VALUE);
		WorkflowEntity1Entity entity = new WorkflowEntity1Entity();
		entity.copyFormVO(workflowEntity1VO);

		workflowEntity1Mapper.insert(entity);


		workflowEntity1VO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(workflowEntity1VO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<WorkflowEntity1VO> update(WorkflowEntity1VO workflowEntity1)
	{
		WorkflowEntity1Entity entity = new WorkflowEntity1Entity();
		entity.copyFormVO(workflowEntity1);


		workflowEntity1Mapper.updateById(entity);


		workflowEntity1.setId(entity.getId());
		this.delColRedis();
		entity = workflowEntity1Mapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{

         int count = 0;
             WorkflowEntity1Entity entity = new WorkflowEntity1Entity();
             entity.setId(id);
             entity.setDeleted(true);
             count = workflowEntity1Mapper.updateById(entity);
		if (count > 0)
		{
			this.delColRedis();
			return ResultModel.success(true);
		}
		else
		{
			return ResultModel.success(false);
		}
	}

	@Override
	public ResultModel<Boolean> deleteBatch(List<Long> ids)
	{
        ids.forEach(id -> {
            WorkflowEntity1Entity entity = new WorkflowEntity1Entity();
            entity.setId(id);
            entity.setDeleted(true);
            workflowEntity1Mapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<WorkflowEntity1Entity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(WorkflowEntity1Entity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return workflowEntity1Mapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<WorkflowEntity1VO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<WorkflowEntity1VO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            WorkflowEntity1Entity entity = workflowEntity1Mapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        return entityModel;
	}

	@Override
	public ResultModel<List<WorkflowEntity1VO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<WorkflowEntity1VO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<WorkflowEntity1Entity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, WorkflowEntity1Entity.class);
                queryWrapper.eq("deleted",false);
                List<WorkflowEntity1Entity> entities = workflowEntity1Mapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<WorkflowEntity1VO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<WorkflowEntity1VO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<WorkflowEntity1Entity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, WorkflowEntity1Entity.class);
            queryWrapper.eq("deleted",false);
            IPage<WorkflowEntity1Entity> entityPage = workflowEntity1Mapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

            page = CollectCovertUtil.pageVO(entityPage);
            pageRedisUtils.set(redisKey, page, 300);
        }
        return ResultModel.success(page);

	}


	private void delFromRedisAsync(Object id)
	{
		CompletableFuture.runAsync(() -> {
			try
			{
				TimeUnit.SECONDS.sleep(1);
				redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
			}
			catch (InterruptedException e)
			{
				log.error("缓存删除线程错误", e);
				Thread.currentThread()
					  .interrupt();
			}
		});
	}

	private void delColRedis()
	{
		String keyPattern = RedisUtils.getRedisKey(KEY_PREFIX, "collection:*");
		Set<String> keys = redisUtils.keys(keyPattern);
		if (keys != null)
		{
			String[] keyArray = keys.toArray(new String[] {});
			redisUtils.del(keyArray);
		}
	}

    @SneakyThrows
	@Override
	public ResultModel<WorkflowEntity1VO> submit(WorkflowEntity1VO workflowEntity1VO) {
		List showFiledList = new ArrayList();
		Field[] fieldList = workflowEntity1VO.getClass().getDeclaredFields();
		for(int i = 0; i<fieldList.length; i++){
			String fieldName = fieldList[i].getName();
			IsWorkflowEditField annotation = fieldList[i].getAnnotation(IsWorkflowEditField.class);
			if(annotation == null){
				continue;
			}
			String enValue = annotation.value();
			showFiledList.add(new ProcessVariable(fieldName, enValue,
					null, false, null));
		}

		Map map = new HashMap();
		map.put(VariableLocalEnum.getVariableName(VariableLocalEnum.id), workflowEntity1VO.getId());
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.form), workflowEntity1VO);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.showForm),showFiledList);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.invoke),workflowEntity1VO.getRemoteCallPayload());

		// 是否工作流条件字段(需要在模板循环是否为条件字段，遍历写死)
        map.put("workCode",workflowEntity1VO.getWorkCode());



		ResultModel<ProcessInstanceImpl> resultModel = processServiceFeign.startProcessInstanceByBusinessId(
				WorkflowEntity1VO.IS_ENTITY_CODE,
				Long.toString(workflowEntity1VO.getId()),
				map);

		String code = resultModel.getCode();
		// 调起成功
		if (ResultCodeEnum.SUCCESS.code().equals(code)) {
			if (resultModel.getData() != null) {
				return ResultModel.success(workflowEntity1VO);
			}
		// 未找到流程实例
		} else if (ResultCodeEnum.NOT_FOUND_PROCESS.code().equals(code)) {
			return ResultModel.failed(ResultCodeEnum.NOT_FOUND_PROCESS);
		}
		return ResultModel.failed();
	}
	@Override
	public ResultModel<List<WorkflowEntity1VO>> importData(List<WorkflowEntity1VO> workflowEntity1List) {
		List<WorkflowEntity1Entity> saves = new ArrayList<>();
		List<WorkflowEntity1VO> returnList = new ArrayList<>();
		for (WorkflowEntity1VO workflowEntity1 : workflowEntity1List) {
			WorkflowEntity1Entity entity = new WorkflowEntity1Entity();
			entity.copyFormVO(workflowEntity1);
			saves.add(entity);
		}

		saves.forEach(entity -> {
			workflowEntity1Mapper.insert(entity);
			WorkflowEntity1VO workflowEntity1 = entity.copyToVO();
			returnList.add(workflowEntity1);
			redisUtils.set(RedisUtils.getRedisKey(KEY_PREFIX, workflowEntity1.getId()), ResultModel.success(workflowEntity1));
		});
		this.delColRedis();
		return ResultModel.success(workflowEntity1List);
	}
}
