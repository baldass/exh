package com.txts.util;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * 简易的通用方法 需要先配置spring和mybaits整合的SqlSessionTemplate
 * 
 * @time 2018年8月1日-下午2:45:08
 * @author lfy
 */
@Repository("commonDao")
public class CommonDao {

	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	/**
	 * 新增
	 * 
	 * @time 2018年8月1日-下午2:46:14
	 * @author lfy
	 */
	public int insert(String str, Object obj) {
		return sqlSessionTemplate.insert(str, obj);
	}

	/**
	 * 更新
	 * 
	 * @time 2018年8月1日-下午2:46:22
	 * @author lfy
	 */
	public int update(String str, Object obj) {
		return sqlSessionTemplate.update(str, obj);
	}

	/**
	 * 批量增删改 不要放太多数据 集合数据太大可能会导致内存溢出（看内存能否容纳）
	 * 
	 * @time 2018年8月1日-下午2:46:27
	 * @author lfy
	 */
	@SuppressWarnings("rawtypes")
	public void batch(String str, List objs) {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		// 批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			if (objs != null) {
				for (int i = 0, size = objs.size(); i < size; i++) {
					sqlSession.update(str, objs.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * 删除
	 * 
	 * @time 2018年8月1日-下午2:46:51
	 * @author lfy
	 */
	public int delete(String str, Object obj) {
		return sqlSessionTemplate.delete(str, obj);
	}

	/**
	 * 查一个
	 * 
	 * @time 2018年8月1日-下午2:46:55
	 * @author lfy
	 */
	public Object findOne(String str, Object obj) {
		return sqlSessionTemplate.selectOne(str, obj);
	}

	/**
	 * 查集合
	 * 
	 * @time 2018年8月1日-下午2:47:00
	 * @author lfy
	 */
	@SuppressWarnings("rawtypes")
	public List findList(String str, Object obj) {
		return sqlSessionTemplate.selectList(str, obj);
	}

}