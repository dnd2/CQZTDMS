package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.po3.bean.PO;

public class VerFlagDao extends BaseDao{
	public static Logger logger = Logger.getLogger(VerFlagDao.class);
	private static final VerFlagDao dao = new VerFlagDao ();
	public static final VerFlagDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	* @Title: 检验VER
	* @param: 表名
	* @param: 主键名 
	* @param: 主键值
	* @param: oldVer 
	* @return: flag
	* @throws
	*/
	public static boolean getVerFlag(String tableName,String fieldName,String fieldValue,String oldVer){
		boolean flag = true;
		String sql = "select NVL(VER,0) VER from " + tableName + " where 1=1 and " + fieldName + "=" + fieldValue;
		Map<String, Object> map = dao.pageQueryMap(sql, null, "com.infodms.dms.dao.common.VerFlagDao.getVerFlag");
		if(!"".equals(map)&&map!=null){
			String nowVer = map.get("VER").toString();
			if(oldVer.equals(nowVer)){
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
}
