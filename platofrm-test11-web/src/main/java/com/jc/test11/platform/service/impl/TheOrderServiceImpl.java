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
import com.jc.test11.platform.vo.TheOrderVO;
import com.jc.test11.platform.entity.TheOrderEntity;
import com.jc.test11.platform.mapper.ITheOrderMapper;
import com.jc.test11.platform.service.ITheOrderService;
import com.jc.platform.mybatis.utils.CollectCovertUtil;
import com.jc.platform.mybatis.utils.ConditionUtil;
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
 * ClassName TheOrderServiceImpl.java
 * Description 订单菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class TheOrderServiceImpl implements ITheOrderService
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_theOrder";

    @Resource
    private RedisUtils<String, ResultModel<TheOrderVO>> redisUtils;
    @Resource
    private RedisUtils<String, TheOrderVO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<TheOrderVO>> pageRedisUtils;

	@Resource(type = ITheOrderMapper.class)
	private ITheOrderMapper theOrderMapper;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TheOrderVO> insert(TheOrderVO theOrderVO)
	{

		TheOrderEntity entity = new TheOrderEntity();
		entity.copyFormVO(theOrderVO);

		theOrderMapper.insert(entity);


		theOrderVO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(theOrderVO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TheOrderVO> update(TheOrderVO theOrder)
	{
		TheOrderEntity entity = new TheOrderEntity();
		entity.copyFormVO(theOrder);


		theOrderMapper.updateById(entity);


		theOrder.setId(entity.getId());
		this.delColRedis();
		entity = theOrderMapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{

         int count = 0;
             TheOrderEntity entity = new TheOrderEntity();
             entity.setId(id);
             entity.setDeleted(true);
             count = theOrderMapper.updateById(entity);
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
            TheOrderEntity entity = new TheOrderEntity();
            entity.setId(id);
            entity.setDeleted(true);
            theOrderMapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<TheOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(TheOrderEntity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return theOrderMapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<TheOrderVO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<TheOrderVO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            TheOrderEntity entity = theOrderMapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        return entityModel;
	}

	@Override
	public ResultModel<List<TheOrderVO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<TheOrderVO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<TheOrderEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TheOrderEntity.class);
                queryWrapper.eq("deleted",false);
                List<TheOrderEntity> entities = theOrderMapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<TheOrderVO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<TheOrderVO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<TheOrderEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TheOrderEntity.class);
            queryWrapper.eq("deleted",false);
            IPage<TheOrderEntity> entityPage = theOrderMapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

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

	@Override
	public ResultModel<List<TheOrderVO>> importData(List<TheOrderVO> theOrderList) {
		List<TheOrderEntity> saves = new ArrayList<>();
		List<TheOrderVO> returnList = new ArrayList<>();
		for (TheOrderVO theOrder : theOrderList) {
			TheOrderEntity entity = new TheOrderEntity();
			entity.copyFormVO(theOrder);
			saves.add(entity);
		}

		saves.forEach(entity -> {
			theOrderMapper.insert(entity);
			TheOrderVO theOrder = entity.copyToVO();
			returnList.add(theOrder);
			redisUtils.set(RedisUtils.getRedisKey(KEY_PREFIX, theOrder.getId()), ResultModel.success(theOrder));
		});
		this.delColRedis();
		return ResultModel.success(theOrderList);
	}
}
