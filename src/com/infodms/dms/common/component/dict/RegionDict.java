package com.infodms.dms.common.component.dict;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.RegionBean;
import com.infodms.dms.common.FileConstant;
import com.infoservice.filestore.FileStore;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * @author ZhaoLi 
 * 生成身份城市js的任务
 */
public class RegionDict{
	private static RegionDict instance = null;
	public static Date dt = null;
	
	private static Logger logger = Logger.getLogger(RegionDict.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static final String tm_region_sql = "  select a.region_code,a.region_name,b.region_code parent_id,TRIM(a.region_type) region_type from tm_region a, tm_region b where a.STATUS = 10011001 and a.parent_id = b.region_id order by a.region_name asc";
	private RegionDict() {
	}

	public static RegionDict getInstance() {
		if (instance == null) {
			synchronized (RegionDict.class) {
				if (instance == null) {
					instance = new RegionDict();
				}
			}
		}
		return instance;
	}

	public void init() {
		try{
			List<RegionBean> codeList = null;
			if(dt==null || hasCodeUpdate()){
				codeList = selRegionList();
			}
			byte[] codeJson = null;
			if(codeList != null){
				codeJson = CodeDict.toJsonFormat("var regionData = ", codeList);
			}
			String fid = null;
			if(codeJson != null){
				fid = FileStore.getInstance().write("regionData.js",
						codeJson);
			}
			String codeJsUrl = null;
			if(fid != null){
				codeJsUrl = FileStore.getInstance().getDomainURL(fid);
				FileConstant.regionJsUrl = codeJsUrl.toUpperCase();
				dt = new Date();
				logger.error(codeJsUrl);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public static boolean hasCodeUpdate(){
		String sql = "select count(region_id) result from tm_region where update_date > ?";
		List<Object> params = Arrays.asList(new Object[]{dt});
		List<Integer> rsList = null;
		boolean rs = false;
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			rsList = factory.select(sql, params, new DAOCallback<Integer>(){
				public Integer wrapper(ResultSet rs, int idx) {
					Integer result;
					try {
						result = rs.getInt("result");
					} catch (SQLException e) {
						logger.error("检查数据字典表是否更新,发生错误");
						throw new DAOException(e);
					}
					return result;
				}});
			if(rsList.get(0) > 0){
				logger.info("地区表有更新!");
			}else{
				logger.info("地区表没更新!");
			}
			POContext.endTxn(true);
			rs = rsList.get(0) > 0;
		} catch (Exception e) {
			POContext.endTxn(false);
		}finally{
			POContext.cleanTxn();
		}
		return rs;
	}
	
	public static List<RegionBean> selRegionList(){
		List<RegionBean> codeList = null;
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			codeList = factory.select(tm_region_sql, null, new DAOCallback<RegionBean>(){
				public RegionBean wrapper(ResultSet rs, int idx) {
					RegionBean bean = new RegionBean();
					try {
						bean.setRegionCode(rs.getString("region_code"));
						bean.setRegionName(rs.getString("region_name"));
						bean.setParId(rs.getString("parent_id"));
						bean.setRegionType(rs.getString("region_type"));
					} catch (SQLException e) {
						throw new DAOException(e);
					}
					return bean;
				}});
			POContext.endTxn(true);
		} catch (Exception e) {
			POContext.endTxn(false);
		} finally{
			logger.info("执行了查询地区表的SQL: "+tm_region_sql);
			POContext.cleanTxn();
		}
		return codeList;
	}
	
}
