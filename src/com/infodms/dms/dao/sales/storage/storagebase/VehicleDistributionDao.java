package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class VehicleDistributionDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(VehicleDistributionDao.class);
	private static final VehicleDistributionDao dao = new VehicleDistributionDao ();
	public static final VehicleDistributionDao getInstance() {
		return dao;
	}

	/**
	 * 获取仓库
	 * add by andyzhou
	 * 2013-6-9
	 */
	public List<Map<String,Object>> getStorage(Long orgId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("select AREA_ID,AREA_CODE,AREA_NAME from TT_SALES_AREA where STATUS=10011001 order by AREA_CODE \n");
		logger.error("-----sbSql="+sbSql.toString());
		return pageQuery(sbSql.toString(), null,getFunName());
	}	
	
	
	/**
	 * 获取分布列表
	 * add by andyzhou
	 * 2013-6-8
	 */
	public List<Map<String,Object>> getDistribution(String areaId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append(" \n");
		logger.error("-----sbSql="+sbSql.toString());
		return pageQuery(sbSql.toString(), null,getFunName());
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
