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
import com.jc.test11.platform.vo.ApprovalEntityVO;
import com.jc.test11.platform.entity.ApprovalEntityEntity;
import com.jc.test11.platform.mapper.IApprovalEntityMapper;
import com.jc.test11.platform.service.IApprovalEntityService;
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
 * ClassName ApprovalEntityServiceImpl.java
 * Description 普通审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class ApprovalEntityServiceImpl implements IApprovalEntityService
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_approvalEntity";

    @Resource
    private RedisUtils<String, ResultModel<ApprovalEntityVO>> redisUtils;
    @Resource
    private RedisUtils<String, ApprovalEntityVO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<ApprovalEntityVO>> pageRedisUtils;

	@Resource(type = IApprovalEntityMapper.class)
	private IApprovalEntityMapper approvalEntityMapper;
    /**
     * 流程中心
     */
    @Autowired
    private IProcessServiceFeign processServiceFeign;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<ApprovalEntityVO> insert(ApprovalEntityVO approvalEntityVO)
	{

	    //工作流默认状态为草稿
        approvalEntityVO.setWorkflowState(WorkState.THE_DRAFT.VALUE);
		ApprovalEntityEntity entity = new ApprovalEntityEntity();
		entity.copyFormVO(approvalEntityVO);

		approvalEntityMapper.insert(entity);


		approvalEntityVO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(approvalEntityVO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<ApprovalEntityVO> update(ApprovalEntityVO approvalEntity)
	{
		ApprovalEntityEntity entity = new ApprovalEntityEntity();
		entity.copyFormVO(approvalEntity);


		approvalEntityMapper.updateById(entity);


		approvalEntity.setId(entity.getId());
		this.delColRedis();
		entity = approvalEntityMapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{

         int count = 0;
             ApprovalEntityEntity entity = new ApprovalEntityEntity();
             entity.setId(id);
             entity.setDeleted(true);
             count = approvalEntityMapper.updateById(entity);
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
            ApprovalEntityEntity entity = new ApprovalEntityEntity();
            entity.setId(id);
            entity.setDeleted(true);
            approvalEntityMapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<ApprovalEntityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(ApprovalEntityEntity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return approvalEntityMapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<ApprovalEntityVO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<ApprovalEntityVO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            ApprovalEntityEntity entity = approvalEntityMapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        return entityModel;
	}

	@Override
	public ResultModel<List<ApprovalEntityVO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<ApprovalEntityVO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<ApprovalEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, ApprovalEntityEntity.class);
                queryWrapper.eq("deleted",false);
                List<ApprovalEntityEntity> entities = approvalEntityMapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<ApprovalEntityVO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<ApprovalEntityVO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<ApprovalEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, ApprovalEntityEntity.class);
            queryWrapper.eq("deleted",false);
            IPage<ApprovalEntityEntity> entityPage = approvalEntityMapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

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
	public ResultModel<ApprovalEntityVO> submit(ApprovalEntityVO approvalEntityVO) {
		List showFiledList = new ArrayList();
		Field[] fieldList = approvalEntityVO.getClass().getDeclaredFields();
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
		map.put(VariableLocalEnum.getVariableName(VariableLocalEnum.id), approvalEntityVO.getId());
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.form), approvalEntityVO);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.showForm),showFiledList);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.invoke),approvalEntityVO.getRemoteCallPayload());

		// 是否工作流条件字段(需要在模板循环是否为条件字段，遍历写死)
        map.put("csCode",approvalEntityVO.getCsCode());



		ResultModel<ProcessInstanceImpl> resultModel = processServiceFeign.startProcessInstanceByBusinessId(
				ApprovalEntityVO.IS_ENTITY_CODE,
				Long.toString(approvalEntityVO.getId()),
				map);

		String code = resultModel.getCode();
		// 调起成功
		if (ResultCodeEnum.SUCCESS.code().equals(code)) {
			if (resultModel.getData() != null) {
				return ResultModel.success(approvalEntityVO);
			}
		// 未找到流程实例
		} else if (ResultCodeEnum.NOT_FOUND_PROCESS.code().equals(code)) {
			return ResultModel.failed(ResultCodeEnum.NOT_FOUND_PROCESS);
		}
		return ResultModel.failed();
	}
	@Override
	public ResultModel<List<ApprovalEntityVO>> importData(List<ApprovalEntityVO> approvalEntityList) {
		List<ApprovalEntityEntity> saves = new ArrayList<>();
		List<ApprovalEntityVO> returnList = new ArrayList<>();
		for (ApprovalEntityVO approvalEntity : approvalEntityList) {
			ApprovalEntityEntity entity = new ApprovalEntityEntity();
			entity.copyFormVO(approvalEntity);
			saves.add(entity);
		}

		saves.forEach(entity -> {
			approvalEntityMapper.insert(entity);
			ApprovalEntityVO approvalEntity = entity.copyToVO();
			returnList.add(approvalEntity);
			redisUtils.set(RedisUtils.getRedisKey(KEY_PREFIX, approvalEntity.getId()), ResultModel.success(approvalEntity));
		});
		this.delColRedis();
		return ResultModel.success(approvalEntityList);
	}
}
