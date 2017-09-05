package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVariableParaPO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:VariableParaManageDAO.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-7-9</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class VariableParaManageDAO extends BaseDao<TmVariableParaPO>{
	
	private static final VariableParaManageDAO dao = new VariableParaManageDAO();
	
	public static final VariableParaManageDAO getInstance() {
		return dao;
	}
	protected TmVariableParaPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 新增记录
	 * @param po
	 */
	public void addNewPara(TmVariableParaPO po){
		dao.insert(po);
	}
	
	
	/**
	 * 更新记录
	 * @param po1
	 * @param po2
	 */
	public void updatePara(TmVariableParaPO po1,TmVariableParaPO po2){
		dao.update(po1, po2);
	}
	
	
	
	/**
	 * 查询可变代码
	 * @param paraType
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryVariablePara(String paraType,String oemCompanyId,int pageSize,int curPage){
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVP.PARA_ID,\n" );
		sql.append("       TVP.PARA_TYPE,\n" );
		sql.append("       TVP.PARA_CODE,\n" );
		sql.append("       TVP.PARA_NAME,\n" );
		sql.append("       TVP.STATUS,\n" );
		sql.append("       TVP.ISSUE,\n" );
		sql.append("       TVP.REMARK\n" );
		sql.append("  FROM TM_VARIABLE_PARA TVP\n");
		sql.append("  WHERE TVP.OEM_COMPANY_ID =");
		sql.append(oemCompanyId);
		sql.append("\n");
		
		if(!"".equals(paraType)){
			sql.append("AND TVP.PARA_TYPE = ");
			sql.append(paraType);
			sql.append("\n");
		}
		sql.append("  ORDER BY TVP.PARA_ID DESC");
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	/**
	 * 根据参数类型和参数代码查询
	 * @param paraType 参数类型
	 * @param paraCode 参数代码
	 * @return
	 */
	public Map<String, Object> searchParaByCode(String oemCompanyId,String paraType,String paraCode){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVP.PARA_ID,\n" );
		sql.append("       TVP.PARA_TYPE,\n" );
		sql.append("       TVP.PARA_CODE,\n" );
		sql.append("       TVP.PARA_NAME,\n" );
		sql.append("       TVP.STATUS,\n" );
		sql.append("       TVP.ISSUE,\n" );
		sql.append("       TVP.REMARK\n" );
		sql.append("  FROM TM_VARIABLE_PARA TVP\n" );
		sql.append(" WHERE TVP.OEM_COMPANY_ID = " );
		sql.append(oemCompanyId);
		sql.append("\n");
		sql.append(" AND   TVP.PARA_TYPE = ");
		sql.append(paraType);
		sql.append("\n");
		sql.append(" AND   TVP.PARA_CODE = '");
		sql.append(paraCode);
		sql.append("'\n");

		Map<String, Object> map = (Map<String, Object>)pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 通过id查询记录
	 * @param paraId 可变参数id
	 * @return
	 */
	public Map<String, Object> searchParaById(String paraId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVP.PARA_ID,\n" );
		sql.append("       TVP.PARA_TYPE,\n" );
		sql.append("       TVP.PARA_CODE,\n" );
		sql.append("       TVP.PARA_NAME,\n" );
		sql.append("       TVP.STATUS,\n" );
		sql.append("       TVP.ISSUE,\n" );
		sql.append("       TVP.REMARK\n" );
		sql.append("  FROM TM_VARIABLE_PARA TVP\n" );
		sql.append(" WHERE TVP.PARA_ID = " );
		sql.append(paraId);
		sql.append("\n");
		
		
		Map<String, Object> map = (Map<String, Object>)pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
}

