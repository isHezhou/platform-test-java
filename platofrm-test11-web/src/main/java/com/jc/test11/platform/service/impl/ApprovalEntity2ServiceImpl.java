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
import com.jc.test11.platform.vo.ApprovalEntity2VO;
import com.jc.test11.platform.entity.ApprovalEntity2Entity;
import com.jc.test11.platform.mapper.IApprovalEntity2Mapper;
import com.jc.test11.platform.service.IApprovalEntity2Service;
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
 * ClassName ApprovalEntity2ServiceImpl.java
 * Description 树形审批菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class ApprovalEntity2ServiceImpl implements IApprovalEntity2Service
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_approvalEntity2";

    @Resource
    private RedisUtils<String, ResultModel<ApprovalEntity2VO>> redisUtils;
    @Resource
    private RedisUtils<String, ApprovalEntity2VO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<ApprovalEntity2VO>> pageRedisUtils;

	@Resource(type = IApprovalEntity2Mapper.class)
	private IApprovalEntity2Mapper approvalEntity2Mapper;
    /**
     * 流程中心
     */
    @Autowired
    private IProcessServiceFeign processServiceFeign;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<ApprovalEntity2VO> insert(ApprovalEntity2VO approvalEntity2VO)
	{

	    //工作流默认状态为草稿
        approvalEntity2VO.setWorkflowState(WorkState.THE_DRAFT.VALUE);
		ApprovalEntity2Entity entity = new ApprovalEntity2Entity();
		entity.copyFormVO(approvalEntity2VO);

		approvalEntity2Mapper.insert(entity);


		approvalEntity2VO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(approvalEntity2VO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<ApprovalEntity2VO> update(ApprovalEntity2VO approvalEntity2)
	{
		ApprovalEntity2Entity entity = new ApprovalEntity2Entity();
		entity.copyFormVO(approvalEntity2);


		approvalEntity2Mapper.updateById(entity);


		approvalEntity2.setId(entity.getId());
		this.delColRedis();
		entity = approvalEntity2Mapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{

         int count = 0;
             ApprovalEntity2Entity entity = new ApprovalEntity2Entity();
             entity.setId(id);
             entity.setDeleted(true);
             count = approvalEntity2Mapper.updateById(entity);
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
            ApprovalEntity2Entity entity = new ApprovalEntity2Entity();
            entity.setId(id);
            entity.setDeleted(true);
            approvalEntity2Mapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<ApprovalEntity2Entity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(ApprovalEntity2Entity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return approvalEntity2Mapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<ApprovalEntity2VO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<ApprovalEntity2VO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            ApprovalEntity2Entity entity = approvalEntity2Mapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        return entityModel;
	}

	@Override
	public ResultModel<List<ApprovalEntity2VO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<ApprovalEntity2VO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<ApprovalEntity2Entity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, ApprovalEntity2Entity.class);
                queryWrapper.eq("deleted",false);
                List<ApprovalEntity2Entity> entities = approvalEntity2Mapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<ApprovalEntity2VO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<ApprovalEntity2VO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<ApprovalEntity2Entity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, ApprovalEntity2Entity.class);
            queryWrapper.eq("deleted",false);
            IPage<ApprovalEntity2Entity> entityPage = approvalEntity2Mapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

            page = CollectCovertUtil.pageVO(entityPage);
            pageRedisUtils.set(redisKey, page, 300);
        }
        return ResultModel.success(page);

	}

    @Override
	public ResultModel<List<ApprovalEntity2VO>> treeList(QueryParam queryParam)
	{
		List<ApprovalEntity2VO> list = this.find(queryParam).getData();
		return ResultModel.success(findMenuChildren(list, null));
	}

	private List<ApprovalEntity2VO> findMenuChildren(List<ApprovalEntity2VO> list, ApprovalEntity2VO parent)
	{
		List<ApprovalEntity2VO> reslist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
		{
			ApprovalEntity2VO menu = list.get(i);
			if (parent == null)
			{
				if (Long.valueOf(0)
						.equals(menu.getParentId()))
				{
					menu.setChildren(findMenuChildren(list, menu));
					reslist.add(menu);
				}
				else
				{
					ApprovalEntity2VO tmpMenu = new ApprovalEntity2VO(menu.getParentId());
					if (!list.contains(tmpMenu))
					{
						menu.setChildren(findMenuChildren(list, menu));
						reslist.add(menu);
					}
				}
			}
			else if (parent.getId()
					.equals(menu.getParentId()))
			{
				menu.setParentName(parent.getTreeName()+"");
				menu.setChildren(findMenuChildren(list, menu));
				reslist.add(menu);
			}
		}
		return reslist.isEmpty() ? null : reslist;
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
	public ResultModel<ApprovalEntity2VO> submit(ApprovalEntity2VO approvalEntity2VO) {
		List showFiledList = new ArrayList();
		Field[] fieldList = approvalEntity2VO.getClass().getDeclaredFields();
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
		map.put(VariableLocalEnum.getVariableName(VariableLocalEnum.id), approvalEntity2VO.getId());
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.form), approvalEntity2VO);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.showForm),showFiledList);
		map.put(VariableGlobalEnum.getVariableName(VariableGlobalEnum.invoke),approvalEntity2VO.getRemoteCallPayload());

		// 是否工作流条件字段(需要在模板循环是否为条件字段，遍历写死)
        map.put("treeCode",approvalEntity2VO.getTreeCode());



		ResultModel<ProcessInstanceImpl> resultModel = processServiceFeign.startProcessInstanceByBusinessId(
				ApprovalEntity2VO.IS_ENTITY_CODE,
				Long.toString(approvalEntity2VO.getId()),
				map);

		String code = resultModel.getCode();
		// 调起成功
		if (ResultCodeEnum.SUCCESS.code().equals(code)) {
			if (resultModel.getData() != null) {
				return ResultModel.success(approvalEntity2VO);
			}
		// 未找到流程实例
		} else if (ResultCodeEnum.NOT_FOUND_PROCESS.code().equals(code)) {
			return ResultModel.failed(ResultCodeEnum.NOT_FOUND_PROCESS);
		}
		return ResultModel.failed();
	}
}
