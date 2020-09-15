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
import com.jc.test11.platform.vo.TestEntityVO;
import com.jc.test11.platform.entity.TestEntityEntity;
import com.jc.test11.platform.mapper.ITestEntityMapper;
import com.jc.test11.platform.service.ITestEntityService;
import com.jc.platform.mybatis.utils.CollectCovertUtil;
import com.jc.platform.mybatis.utils.ConditionUtil;
import com.jc.platform.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ClassName TestEntityServiceImpl.java
 * Description 普通菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class TestEntityServiceImpl implements ITestEntityService
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_testEntity";

    @Resource
    private RedisUtils<String, ResultModel<TestEntityVO>> redisUtils;
    @Resource
    private RedisUtils<String, TestEntityVO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<TestEntityVO>> pageRedisUtils;

	@Resource(type = ITestEntityMapper.class)
	private ITestEntityMapper testEntityMapper;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TestEntityVO> insert(TestEntityVO testEntityVO)
	{

		TestEntityEntity entity = new TestEntityEntity();
		entity.copyFormVO(testEntityVO);

		testEntityMapper.insert(entity);


		testEntityVO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(testEntityVO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TestEntityVO> update(TestEntityVO testEntity)
	{
		TestEntityEntity entity = new TestEntityEntity();
		entity.copyFormVO(testEntity);


		testEntityMapper.updateById(entity);


		testEntity.setId(entity.getId());
		this.delColRedis();
		entity = testEntityMapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{

         int count = 0;
             TestEntityEntity entity = new TestEntityEntity();
             entity.setId(id);
             entity.setDeleted(true);
             count = testEntityMapper.updateById(entity);
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
            TestEntityEntity entity = new TestEntityEntity();
            entity.setId(id);
            entity.setDeleted(true);
            testEntityMapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<TestEntityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(TestEntityEntity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return testEntityMapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<TestEntityVO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<TestEntityVO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            TestEntityEntity entity = testEntityMapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        return entityModel;
	}

	@Override
	public ResultModel<List<TestEntityVO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<TestEntityVO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<TestEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TestEntityEntity.class);
                queryWrapper.eq("deleted",false);
                List<TestEntityEntity> entities = testEntityMapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<TestEntityVO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<TestEntityVO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<TestEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TestEntityEntity.class);
            queryWrapper.eq("deleted",false);
            IPage<TestEntityEntity> entityPage = testEntityMapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

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
	public ResultModel<List<TestEntityVO>> importData(List<TestEntityVO> testEntityList) {
		List<TestEntityEntity> saves = new ArrayList<>();
		List<TestEntityVO> returnList = new ArrayList<>();
		for (TestEntityVO testEntity : testEntityList) {
			TestEntityEntity entity = new TestEntityEntity();
			entity.copyFormVO(testEntity);
			saves.add(entity);
		}

		saves.forEach(entity -> {
			testEntityMapper.insert(entity);
			TestEntityVO testEntity = entity.copyToVO();
			returnList.add(testEntity);
			redisUtils.set(RedisUtils.getRedisKey(KEY_PREFIX, testEntity.getId()), ResultModel.success(testEntity));
		});
		this.delColRedis();
		return ResultModel.success(testEntityList);
	}
}
