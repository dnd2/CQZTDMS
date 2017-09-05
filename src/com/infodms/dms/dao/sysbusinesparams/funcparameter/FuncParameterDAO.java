package com.infodms.dms.dao.sysbusinesparams.funcparameter;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FuncParameterDAO extends BaseDao{
	private static final FuncParameterDAO dao = new FuncParameterDAO ();
	public static final FuncParameterDAO getInstance() {
		return dao;
	}
	
	/**
	 * @FUNCTION : 获取参数列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-6-28
	 */
	public PageResult<Map<String, Object>> getParaList(JSONObject paraObject,int pageSize, int curPage)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t2.FUNC_ID,t2.FUNC_CODE,t2.FUNC_NAME,t1.PARA_NAME,t1.ELEMENT_ID,t1.PARA_TYPE from TC_FUNC_PARA t1,TC_FUNC t2 where t1.FUNC_ID=t2.FUNC_ID order by t2.FUNC_ID,t2.SORT_ORDER");		
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
