package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SysParameterManageDAO.java
 * @Package: com.infodms.dms.dao.sysbusinesparams.businesparamsmanage
 * @Description: 系统可变参数表维护
 * @date   : 2010-7-6 
 * @version: V1.0   
 */
public class SysParameterManageDAO extends BaseDao{
	public Logger logger = Logger.getLogger(SysParameterManageDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final SysParameterManageDAO dao = new SysParameterManageDAO ();
	public static final SysParameterManageDAO getInstance() {
		return dao;
	}
	
	
	/** 
	* @Title	  : getSysParameterList 
	* @Description: 参数信息列表展示
	* @return     : PageResult<Map<String,Object>>返回类型 
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public static PageResult <Map<String,Object>> getSysParameterList(String typeName,String paraName,int pageSize,int curPage,Long companyId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT BP.PARA_ID, BP.TYPE_NAME, BP.PARA_NAME, BP.PARA_VALUE, BP.REMARK\n");
		sql.append("  FROM TM_BUSINESS_PARA BP\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append(" AND BP.OEM_COMPANY_ID = " + companyId);
		if (null != typeName && !"".equals(typeName)) {
			sql.append("   AND BP.TYPE_NAME LIKE '%"+typeName.trim()+"%'\n");
		}
		if (null != paraName && !"".equals(paraName)) {
			sql.append("   AND BP.PARA_NAME LIKE '%"+paraName.trim()+"%'\n");
		}
		sql.append(" ORDER BY BP.PARA_ID\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	public static Map<String, Object> getSysParameterInfo(String para_id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT BP.PARA_ID, BP.TYPE_NAME, BP.PARA_NAME, BP.PARA_VALUE, BP.REMARK\n");
		sql.append("  FROM TM_BUSINESS_PARA BP\n");  
		sql.append(" WHERE BP.PARA_ID = "+para_id+"\n");  
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	/** 
	* @Title	  : getAllTypeList 
	* @Description: 得到系统参数名称列表
	* @throws 
	* @LastUpdate :2010-7-7
	*/
	public static List<Map<String,Object>> getAllTypeList(Long companyId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT DISTINCT BP.TYPE_NAME FROM TM_BUSINESS_PARA BP WHERE OEM_COMPANY_ID = "+companyId+" ORDER BY BP.TYPE_NAME\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
