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
import com.jc.test11.platform.vo.TreeEntityVO;
import com.jc.test11.platform.entity.TreeEntityEntity;
import com.jc.test11.platform.mapper.ITreeEntityMapper;
import com.jc.test11.platform.service.ITreeEntityService;
import com.jc.platform.mybatis.utils.CollectCovertUtil;
import com.jc.platform.mybatis.utils.ConditionUtil;
import com.jc.test11.platform.vo.FileInfoVO;
import com.jc.test11.platform.entity.TreeEntityFileInfoEntity;
import com.jc.test11.platform.mapper.ITreeEntityFileInfoMapper;
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
 * ClassName TreeEntityServiceImpl.java
 * Description 树形菜单
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@Service
public class TreeEntityServiceImpl implements ITreeEntityService
{
	private static final String KEY_PREFIX = "db_platofrmTest11:t_treeEntity";

    @Resource
    private RedisUtils<String, ResultModel<TreeEntityVO>> redisUtils;
    @Resource
    private RedisUtils<String, TreeEntityVO> listRedisUtils;
    @Resource
    private RedisUtils<String, PageInfo<TreeEntityVO>> pageRedisUtils;

	@Resource(type = ITreeEntityMapper.class)
	private ITreeEntityMapper treeEntityMapper;
	@Resource(type = ITreeEntityFileInfoMapper.class)
	private ITreeEntityFileInfoMapper treeEntityFileInfoMapper;

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TreeEntityVO> insert(TreeEntityVO treeEntityVO)
	{
		//添加业务下的文件ID
		List<FileInfoVO> uploadPhotoFileList = treeEntityVO.getUploadPhotoFileList();
		uploadPhotoFileList.forEach(item->{
			item.setBusinessCode(treeEntityVO.getUploadPhoto());
			this.insertFileInfo(item);
		});
		//添加业务下的文件ID
		List<FileInfoVO> uploadFileFileList = treeEntityVO.getUploadFileFileList();
		uploadFileFileList.forEach(item->{
			item.setBusinessCode(treeEntityVO.getUploadFile());
			this.insertFileInfo(item);
		});

		TreeEntityEntity entity = new TreeEntityEntity();
		entity.copyFormVO(treeEntityVO);

		treeEntityMapper.insert(entity);


		treeEntityVO.setId(entity.getId());
		this.delColRedis();
		return ResultModel.success(treeEntityVO);
	}

	@Override
	@CachePut(value = KEY_PREFIX, keyGenerator = "objectId")
	public ResultModel<TreeEntityVO> update(TreeEntityVO treeEntity)
	{
		TreeEntityEntity entity = new TreeEntityEntity();
		entity.copyFormVO(treeEntity);

        //删除该业务ID下文件信息
		treeEntity.getDelFileList().forEach(item->{
			treeEntityFileInfoMapper.deleteById(item.getId());
		});
		//添加该业务下的文件信息
		List<FileInfoVO> uploadPhotoFileList = treeEntity.getUploadPhotoFileList();
		uploadPhotoFileList.forEach(item->{
			item.setBusinessCode(treeEntity.getUploadPhoto());
			//编辑时， 新上传的文件信息
			if(!item.getStatus().equals("success")){
			    this.insertFileInfo(item);
			}
		});
		//添加该业务下的文件信息
		List<FileInfoVO> uploadFileFileList = treeEntity.getUploadFileFileList();
		uploadFileFileList.forEach(item->{
			item.setBusinessCode(treeEntity.getUploadFile());
			//编辑时， 新上传的文件信息
			if(!item.getStatus().equals("success")){
			    this.insertFileInfo(item);
			}
		});

		treeEntityMapper.updateById(entity);


		treeEntity.setId(entity.getId());
		this.delColRedis();
		entity = treeEntityMapper.selectById(entity.getId());
		return ResultModel.success(entity.copyToVO());
	}

	@Override
	@CacheEvict(value = KEY_PREFIX, keyGenerator = "simpleKey")
	public ResultModel<Boolean> delete(Long id)
	{
        //根据id 获取上传字段的code 然后删除文件
        TreeEntityEntity treeEntityEntity = treeEntityMapper.selectById(id);
        this.deleteByBusinessCode(treeEntityEntity.getUploadPhoto());
        this.deleteByBusinessCode(treeEntityEntity.getUploadFile());

         int count = 0;
             TreeEntityEntity entity = new TreeEntityEntity();
             entity.setId(id);
             entity.setDeleted(true);
             count = treeEntityMapper.updateById(entity);
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
			TreeEntityEntity treeEntityEntity = treeEntityMapper.selectById(id);
			this.deleteByBusinessCode(treeEntityEntity.getUploadPhoto());
			this.deleteByBusinessCode(treeEntityEntity.getUploadFile());
            TreeEntityEntity entity = new TreeEntityEntity();
            entity.setId(id);
            entity.setDeleted(true);
            treeEntityMapper.updateById(entity);
            redisUtils.del(RedisUtils.getRedisKey(KEY_PREFIX, id));
        });

        ids.forEach(this::delFromRedisAsync);
        this.delColRedis();

        return ResultModel.success(true);
	}
	@Override
	public Boolean checkNotExists(Long id, String property, String value)
	{
        QueryWrapper<TreeEntityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(TreeEntityEntity.class));
        queryWrapper.eq(SqlUtils.getColumnByProperty(property), value);

        if(id!=null && id>0){
            queryWrapper.ne(StringPool.ID, id);
        }

        queryWrapper.eq("deleted",false);

        return treeEntityMapper.selectCount(queryWrapper) <= 0;
	}

	@Override
	public ResultModel<TreeEntityVO> get(Long id)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, id);
        ResultModel<TreeEntityVO> entityModel = redisUtils.get(redisKey);
        if (entityModel == null)
        {
            TreeEntityEntity entity = treeEntityMapper.selectById(id);
            if (entity == null){
                return ResultModel.failed();
            }
            entityModel = ResultModel.success(entity.copyToVO());
            redisUtils.set(redisKey, entityModel);
        }

        TreeEntityVO treeEntityVO = entityModel.getData();
        //根据业务ID获取文件列表，并赋值vo层文件集合
        List<FileInfoVO> uploadPhotoFileInfoList = this.findAllByBusinessCode(treeEntityVO.getUploadPhoto());
        treeEntityVO.setUploadPhotoFileList(uploadPhotoFileInfoList);
        //根据业务ID获取文件列表，并赋值vo层文件集合
        List<FileInfoVO> uploadFileFileInfoList = this.findAllByBusinessCode(treeEntityVO.getUploadFile());
        treeEntityVO.setUploadFileFileList(uploadFileFileInfoList);
        return entityModel;
	}

	@Override
	public ResultModel<List<TreeEntityVO>> find(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:find." + queryParam.toString());
        List<TreeEntityVO> list = listRedisUtils.lGet(redisKey, 0, -1);
            if (list == null || list.isEmpty())
            {
                QueryWrapper<TreeEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TreeEntityEntity.class);
                queryWrapper.eq("deleted",false);
                List<TreeEntityEntity> entities = treeEntityMapper.selectList(queryWrapper);
                list = CollectCovertUtil.listVO(entities);
                if (list != null && !list.isEmpty())
                {
                    listRedisUtils.lSet(redisKey, list, 300);
                }
            }
        return ResultModel.success(list);
	}

	@Override
	public ResultModel<PageInfo<TreeEntityVO>> finds(QueryParam queryParam)
	{
        String redisKey = RedisUtils.getRedisKey(KEY_PREFIX, "collection:finds." + queryParam.toString());
        PageInfo<TreeEntityVO> page = pageRedisUtils.get(redisKey);
        if (page == null)
        {
            QueryWrapper<TreeEntityEntity> queryWrapper = ConditionUtil.getQueryWrapper(queryParam, TreeEntityEntity.class);
            queryWrapper.eq("deleted",false);
            IPage<TreeEntityEntity> entityPage = treeEntityMapper.selectPage(ConditionUtil.getPage(queryParam), queryWrapper);

            page = CollectCovertUtil.pageVO(entityPage);
            pageRedisUtils.set(redisKey, page, 300);
        }
        return ResultModel.success(page);

	}

    @Override
	public ResultModel<List<TreeEntityVO>> treeList(QueryParam queryParam)
	{
		List<TreeEntityVO> list = this.find(queryParam).getData();
		return ResultModel.success(findMenuChildren(list, null));
	}

	private List<TreeEntityVO> findMenuChildren(List<TreeEntityVO> list, TreeEntityVO parent)
	{
		List<TreeEntityVO> reslist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
		{
			TreeEntityVO menu = list.get(i);
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
					TreeEntityVO tmpMenu = new TreeEntityVO(menu.getParentId());
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
				menu.setParentName(parent.getTaName()+"");
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

	@Override
	public FileInfoVO insertFileInfo(FileInfoVO fileInfoVO) {
		TreeEntityFileInfoEntity entity = new TreeEntityFileInfoEntity();
		entity.copyFormVO(fileInfoVO);

		treeEntityFileInfoMapper.insert(entity);

		fileInfoVO.setId(entity.getId());
		return fileInfoVO;
	}
	@Override
	public FileInfoVO getFileInfo(Long id) {
		TreeEntityFileInfoEntity entity = treeEntityFileInfoMapper.selectById(id);
		FileInfoVO fileInfoVO = entity.copyToVO();
		fileInfoVO.setFile(entity.getFile());
		return fileInfoVO;
	}

	@Override
	public void deleteByBusinessCode(String businessCode) {
		treeEntityFileInfoMapper.deleteByBusinessCode(businessCode);
	}

	@Override
	public List<FileInfoVO> findAllByBusinessCode(String businessCode) {
		List<TreeEntityFileInfoEntity> fileInfoEntities = treeEntityFileInfoMapper.findAllByBusinessCode(businessCode);
		List<FileInfoVO> restList = new ArrayList<>();
		fileInfoEntities.forEach(item->{
			restList.add(item.copyToVO());
		});
		return restList;
	}
}
