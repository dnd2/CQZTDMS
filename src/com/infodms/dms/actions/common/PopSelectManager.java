package com.infodms.dms.actions.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.PopSelect;
import com.infodms.dms.dao.common.PopSelectDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class PopSelectManager {	
	public Logger logger = Logger.getLogger(PopSelectManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	PopSelectDAO dao = PopSelectDAO.getInstance();
	PopSelect popSelect=new PopSelect();
	
	/**
	 * @FUNCTION : 弹出框标准查询
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-02
	 */
	@SuppressWarnings("unchecked")
	public void getResult(){
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
			String sqlName = request.getParamValue("sqlName");
			/*获取弹出框的参数条件*/
			String paraJson = new String(request.getParamValue("paraJson").getBytes("ISO8859-1"), "UTF-8");
			//logger.info("---------paraJson="+paraJson);
			JSONObject paraJsonObject = JSONObject.fromObject(paraJson);
			List<Object> params = new LinkedList<Object>();
			Iterator<?> it = paraJsonObject.entrySet().iterator();			
		    while (it.hasNext()) { 
		    	Entry<?, ?> entry = (Entry<?, ?>)it.next();
		        params.add(entry.getValue()); 
		    }
			String sql=popSelect.get(sqlName);
			/*获取弹出框的查询条件*/
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");			
			JSONObject paraObject = JSONObject.fromObject(json);
			//logger.info("---------paraObject="+paraObject);
			/*原SQL语句加上查询条件*/
			if (!paraObject.get("para").equals("")){
			    sql="select * from ("+sql+") where DESCR like '%"+paraObject.get("para")+"%' ";
			}
			//logger.info("---------sql="+sql);
			PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),params,dao.getFunName()+System.currentTimeMillis(),100,curPage);
			act.setOutData("ps", ps);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
	
}
