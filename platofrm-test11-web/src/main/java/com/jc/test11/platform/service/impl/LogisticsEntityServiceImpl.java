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
import com.jc.test11.platform.vo.LogisticsEntityVO;
import com.jc.test11.platform.entity.LogisticsEntityEntity;
import com.jc.test11.platform.mapper.ILogisticsEntityMapper;
import com.jc.test11.platform.service.ILogisticsEntityService;
import com.jc.platform.mybatis.utils.CollectCovertUtil;
import com.jc.platform.mybatis.utils.ConditionUtil;
import com.jc.test11.platform.vo.FileInfoVO;
import com.jc.test11.platform.entity.LogisticsEntityFileInfoEntity;
import com.jc.test11.platform.mapper.ILogisticsEntityFileInfoMapper;
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
 * ClassName LogisticsEntityServiceImpl.java
 * Description 物流菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class LogisticsEntityServiceImpl implements ILogisticsEntityService
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_logisticsEntity";

    @Resource
    private RedisUtils<String, ResultModel<LogisticsEntityVO>> redisUtils;
    @Resource
    private RedisUtils<String, LogisticsEntityVO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<LogisticsEntityVO>> pageRedisUtils;

	@Resource(type = ILogisticsEntityMapper.class)
	private ILogisticsEntityMapper logisticsEntityMapper;
	@Resource(type = ILogisticsEntityFileInfoMapper.class)
	private ILogisticsEntityFileInfoMapper logisticsEntityFileInfoMapper;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<LogisticsEntityVO> insert(LogisticsEntityVO logisticsEntityVO)
	{
		//添加业务下的文件ID
		List<FileInfoVO> uploadPhotoFileList = logisticsEntityVO.getUploadPhotoFileList();
		uploadPhotoFileList.forEach(item->{
			item.setBusinessCode(logisticsEntityVO.getUploadPhoto());
			this.insertFileInfo(item);
		});
		//添加业务下的文件ID
		List<FileInfoVO> uploadFileFileList = logisticsEntityVO.getUploadFileFileList();
		uploadFileFileList.forEach(item->{
			item.setBusinessCode(logisticsEntityVO.getUploadFile());
			this.insertFileInfo(item);
		});

		LogisticsEntityEntity entity = new LogisticsEntityEntity();
		entity.copyFormVO(logisticsEntityVO);

		logisticsEntityMapper.insert(entity);


		logisticsEntityVO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(logisticsEntityVO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<LogisticsEntityVO> update(LogisticsEntityVO logisticsEntity)
	{
		LogisticsEntityEntity entity = new LogisticsEntityEntity();
		entity.copyFormVO(logisticsEntity);

        //删除该业务ID下文件信息
		logisticsEntity.getDelFileList().forEach(item->{
			logisticsEntityFileInfoMapper.deleteById(item.getId());
		});
		//添加该业务下的文件信息
		List<FileInfoVO> uploadPhotoFileList = logisticsEntity.getUploadPhotoFileList();
		uploadPhotoFileList.forEach(item->{
			item.setBusinessCode(logisticsEntity.getUploadPhoto());
			//编辑时， 新上传的文件信息
			if(!item.getStatus().equals("success")){
			    this.insertFileInfo(item);
			}
		});
		//添加该业务下的文件信息
		List<FileInfoVO> uploadFileFileList = logisticsEntity.getUploadFileFileList();
		uploadFileFileList.forEach(item->{
			item.setBusinessCode(logisticsEntity.getUploadFile());
			//编辑时， 新上传的文件信息
			if(!item.getStatus().equals("success")){
			    this.insertFileInfo(item);
			}
		});

		logisticsEntityMapper.updateById(entity);


		logisticsEntity.setId(entity.getId());
		this.delColRedis();
		entity = logisticsEntityMapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{
        //根据id 获取上传字段的code 然后删除文件
        LogisticsEntityEntity logisticsEntityEntity = logisticsEntityMapper.selectById(id);
        this.deleteByBusinessCode(logisticsEntityEntity.getUploadPhoto());
        this.deleteByBusinessCode(logisticsEntityEntity.getUploadFile());

         int count = 0;
             LogisticsEntityEntity entity = new LogisticsEntityEntity();
             entity.setId(id);
             entity.setDeleted(true);
             count = logisticsEntityMapper.updateById(entity);
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
			//根据id 获取上传字段的code 然后删除文件
			LogisticsEntityEntity logisticsEntityEntity = logisticsEntityMapper.selectById(id);
			this.deleteByBusinessCode(logisticsEntityEntity.getUploadPhoto());
			this.deleteByBusinessCode(logisticsEntityEntity.getUploadFile());
            LogisticsEntityEntity entity = new LogisticsEntityEntity();
            entity.setId(id);
            entity.setDeleted(true);
            logisticsEntityMapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<LogisticsEntityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(LogisticsEntityEntity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return logisticsEntityMapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<LogisticsEntityVO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<LogisticsEntityVO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            LogisticsEntityEntity entity = logisticsEntityMapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        LogisticsEntityVO logisticsEntityVO = entityModel.getData();
        //根据业务ID获取文件列表，并赋值vo层文件集合
        List<FileInfoVO> uploadPhotoFileInfoList = this.findAllByBusinessCode(logisticsEntityVO.getUploadPhoto());
        logisticsEntityVO.setUploadPhotoFileList(uploadPhotoFileInfoList);
        //根据业务ID获取文件列表，并赋值vo层文件集合
        List<FileInfoVO> uploadFileFileInfoList = this.findAllByBusinessCode(logisticsEntityVO.getUploadFile());
        logisticsEntityVO.setUploadFileFileList(uploadFileFileInfoList);
        return entityModel;
	}

	@Override
	public ResultModel<List<LogisticsEntityVO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<LogisticsEntityVO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<LogisticsEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, LogisticsEntityEntity.class);
                queryWrapper.eq("deleted",false);
                List<LogisticsEntityEntity> entities = logisticsEntityMapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<LogisticsEntityVO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<LogisticsEntityVO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<LogisticsEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, LogisticsEntityEntity.class);
            queryWrapper.eq("deleted",false);
            IPage<LogisticsEntityEntity> entityPage = logisticsEntityMapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

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
	public FileInfoVO insertFileInfo(FileInfoVO fileInfoVO) {
		LogisticsEntityFileInfoEntity entity = new LogisticsEntityFileInfoEntity();
		entity.copyFormVO(fileInfoVO);

		logisticsEntityFileInfoMapper.insert(entity);

		fileInfoVO.setId(entity.getId());
		return fileInfoVO;
	}
	@Override
	public FileInfoVO getFileInfo(Long id) {
		LogisticsEntityFileInfoEntity entity = logisticsEntityFileInfoMapper.selectById(id);
		FileInfoVO fileInfoVO = entity.copyToVO();
		fileInfoVO.setFile(entity.getFile());
		return fileInfoVO;
	}

	@Override
	public void deleteByBusinessCode(String businessCode) {
		logisticsEntityFileInfoMapper.deleteByBusinessCode(businessCode);
	}

	@Override
	public List<FileInfoVO> findAllByBusinessCode(String businessCode) {
		List<LogisticsEntityFileInfoEntity> fileInfoEntities = logisticsEntityFileInfoMapper.findAllByBusinessCode(businessCode);
		List<FileInfoVO> restList = new ArrayList<>();
		fileInfoEntities.forEach(item->{
			restList.add(item.copyToVO());
		});
		return restList;
	}
	@Override
	public ResultModel<List<LogisticsEntityVO>> importData(List<LogisticsEntityVO> logisticsEntityList) {
		List<LogisticsEntityEntity> saves = new ArrayList<>();
		List<LogisticsEntityVO> returnList = new ArrayList<>();
		for (LogisticsEntityVO logisticsEntity : logisticsEntityList) {
			LogisticsEntityEntity entity = new LogisticsEntityEntity();
			entity.copyFormVO(logisticsEntity);
			saves.add(entity);
		}

		saves.forEach(entity -> {
			logisticsEntityMapper.insert(entity);
			LogisticsEntityVO logisticsEntity = entity.copyToVO();
			returnList.add(logisticsEntity);
			redisUtils.set(RedisUtils.getRedisKey(KEY_PREFIX, logisticsEntity.getId()), ResultModel.success(logisticsEntity));
		});
		this.delColRedis();
		return ResultModel.success(logisticsEntityList);
	}
}
