package com.txts.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.txts.util.CommonDao;

/**
 * @description 采集数据相关
 * 
 * @author lfy
 * @time 2018年10月10日-下午1:31:00
 */
@SuppressWarnings("all")
@Component
public class CollectUtil {
	@Autowired
	private CommonDao dao;
	/**
	 *  回写mes订单的状态和完成数量
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int updateMesMo(Map map){
		return dao.update("collectMapper.updateMesMo", map);
	}
	/**
	 * 新增订单详情
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int insertMoDetail(Map map){
		return dao.insert("collectMapper.insertMoDetail", map);
	}
	/**
	 * 修改订单详情
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int updateMoDetail(Map map){
		return dao.update("collectMapper.updateMoDetail", map);
	}
	/**
	 * 累加订单详情里的某个值
	 * 如需要累加订单的完成数量emd_endsl 
	 * 则参数map里放入
	 * 单号：key为emd_order和value为单号
	 * key为emd_endsl ,value为任意非空非0值
	 * 其它以次类推
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int updateMoDetailIncreace(Map map){
		// TODO 迟
		return dao.update("collectMapper.updateMoDetailIncreace", map);
	}
	public int updateMoDetailIncreaceState(Map map){
		// TODO 迟
		return dao.update("collectMapper.changeMoDetail", map);
	}
	public int changeMoDetail(Map map){
		// TODO 迟
		return dao.update("collectMapper.changeMoDetail", map);
	}
	/**
	 *  查询订单详情 
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public List<Map> selectMoDetail(Map map){
		return dao.findList("collectMapper.selectMoDetail", map);
	}
	/**
	 * 新增节拍表
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int insertBeat(Map map){
		return dao.insert("collectMapper.insertBeat", map);
	}
	/**
	 * 统计-查询节拍表
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public List<Map> selectBeat(Map map){
		return dao.findList("collectMapper.selectBeat", map);
	}
	/**
	 * 新增质量原因表
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int insertQuality(Map map){
		return dao.insert("collectMapper.insertQuality", map);
	}
	/**
	 * 查询-统计质量原因
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public List<Map> selectQuality(Map map){
		return dao.findList("collectMapper.selectQuality", map);
	}
	/**
	 * 新增一个稼动率表
	 * 	[eo_device_id],设备id
	 *	[eo_device_name],设备名字
	 *	[eo_start_time],启动时间
	 *	[eo_order],单号
	 *	[eo_end_time] ,结束时间
	 *	[eo_during_stamp],一共花费的时间 (小时，可以用小数点)
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public int insertOpera(Map map){ // TODO 迟
		return dao.insert("collectMapper.insertOpera", map);
	}
	/*/**
	 *  修改稼动率表的结束时间和持续时间
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	/*public int updateOpera(Map map){
		return dao.update("collectMapper.updateOpera", map);
	}*/
	/**
	 * 查询-统计稼动率表 -
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午3:18:27
	 * @param map
	 * @return
	 */
	public List<Map> selectOpera(Map map){
		return dao.findList("collectMapper.selectOpera", map);
	}
	/**
	 * 根据单号查询扫描条码
	 * 
	 * @author lfy
	 * @time 2018年10月12日-上午11:28:26
	 * @param moNo
	 * @return
	 */
	public String selectAllCodeByMoNo(String moNo){//迟 TODO
		 String result = (String) dao.findOne("collectMapper.selectAllCodeByMoNo", moNo);
		 String[]rs = result.split(" ");
		 result = rs[rs.length];//最后一个
		 return result;
	}
	/**
	 * 
	 * 查一个可出库的
	 * @author lfy
	 * @time 2018年10月20日-上午8:35:50
	 * @param map
	 * @return
	 */
	public List<Map> top1out(Map map){
		return dao.findList("collectMapper.top1out", map);
	}
	/**
	 * 
	 * 查一个可入库的
	 * @author lfy
	 * @time 2018年10月20日-上午8:35:50
	 * @param map
	 * @return
	 */
	public List<Map> top1in(Map map){
		return dao.findList("collectMapper.top1in", map);
	}
	/**
	 * 修改数据库
	 * 字段#{state},#{name}, #{rfid}
	 * 条件#{id}
	 * @author lfy
	 * @time 2018年10月20日-上午8:47:28
	 * @param map
	 * @return
	 */
	public int updateWarehouse(Map map){
		return dao.update("collectMapper.updateWarehouse", map);
	}
}
