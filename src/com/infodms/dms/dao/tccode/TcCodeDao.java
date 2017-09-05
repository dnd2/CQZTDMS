package com.infodms.dms.dao.tccode;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class TcCodeDao extends BaseDao {
	private TcCodeDao(){}
	protected POFactory factory = POFactoryBuilder.getInstance();
	public static TcCodeDao getInstance(){
		return new TcCodeDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> genSelBoxExp(String type,int pageSize,int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select c.code_id,c.code_desc from tc_code   c where c.type="+type+" and status="+Constant.STATUS_ENABLE+" order by c.num asc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	
	}
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> genSelBoxExp(String type,String id,int pageSize,int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select c.code_id, c.code_desc," +
				"(case when c.code_id = (select k.kg_type from tt_crm_knowledge k where k.kg_id = "+id+") then 'true' else 'false' end)AS selected" +
				" from tc_code c where c.type = "+type+"  and c.status = "+Constant.STATUS_ENABLE+" order by c.num asc  ");		
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	public String getCodeDescByCodeId(String codeId){
		List<String> list = null;
		String codeDesc = "";
		try {
			if(null != codeId && !"".equals(codeId)){
				String sql = "select t.code_desc CODEDESC  from tc_code t where t.code_id='"+ codeId +"'";
				list = this.selectTmDataSet(new StringBuffer(sql),"CODEDESC");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null && list.size()>0) codeDesc = list.get(0);
		return codeDesc;
	}
	
	public List<Map<String, Object>> getTcCodesByType(String Type){
		List<Map<String, Object>> list = null;
		if(null != Type && !"".equals(Type)){
			String sql = "select t.code_id CODEID, t.code_desc CODEDESC  from tc_code t where t.type= "+ Type +"";
			try {
				list = this.pageQuery(sql, null, this.getFunName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public String getCodeDescByCodeId(String CodeId, List<Map<String, Object>> list){
		String CodeDesc = "";
		if(null != CodeId && !"".equals(CodeId)){
			for (Map<String, Object> map : list) {
				if(CodeId.equals(map.get("CODEID"))){
					CodeDesc = map.get("CODEDESC").toString();
					break;
				}
			}
		}
		return CodeDesc;
	}
	
}