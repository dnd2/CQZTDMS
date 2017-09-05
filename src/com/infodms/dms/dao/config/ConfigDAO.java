package com.infodms.dms.dao.config;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ConfigDAO extends BaseDao<PO>{
    public static Logger logger = Logger.getLogger(ConfigDAO.class);
    private static ConfigDAO dao = new ConfigDAO();
	public static final ConfigDAO getInstance() {
		return dao;
	}

	/**
	 * @FUNCTION : 页面元素配置获取关联列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-4
	 */
	public PageResult<Map<String, Object>> getRelatedList(JSONObject orginObject, int pageSize, int curPage){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select ELEMENT_ID as ID, F_GETVALUEBYSQL(REPLACE(CHANGE_SQL,'['||'"+orginObject.get("ID")+"'||']','"+orginObject.get("VALUE")+"')) as VALUE   \n");
		sql.append("from TC_FUNC_ELEMENT  \n");
		sql.append("where CHANGE_SQL like '%['||'"+orginObject.get("ID")+"'||']%'  \n");
		logger.info(sql.toString());
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}	
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
