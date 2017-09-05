package com.infodms.dms.actions.report.dmsReport;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.tag.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;

public class Accord extends BaseAction{
	
	private final AccordDAO dao = AccordDAO.getInstance();
	
	public void accord(){
		int res=1;
		try {
			String type = super.getParam("type");
			String claimid = super.getParam("claimid");
			dao.insert("insert into tt_AS_Accord  values("+getSeq()+",'"+loginUser.getUserId()+"',sysdate,'"+type+"','"+claimid+"') ");
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	public void memory_page_on(){
		int res=1;
		try {
			String page = super.getParam("page");
			String query_id = super.getParam("query_id");
			String sql="update query_title t set t.memory_page='"+page+"' where t.query_id="+query_id;
			dao.update(sql, null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	public void memory_page_off(){
		int res=1;
		try {
			String query_id = super.getParam("query_id");
			String sql="update query_title t set t.memory_page=null where t.query_id="+query_id;
			dao.update(sql, null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	@SuppressWarnings("unchecked")
	public void memory_filed_on(){
		int res=1;
		try {
			String query_id = super.getParam("query_id");
			String sql="update query_title t set t.memory_filed='1' where t.query_id="+query_id;
			dao.update(sql, null);
			StringBuffer sb= new StringBuffer();
			sb.append("select t.* from \n" );
			sb.append(" query_Condition t,query_title tt \n" );
			sb.append("where t.query_id=tt.query_id \n");
			DaoFactory.getsql(sb, "tt.query_id", query_id, 1);
			sb.append(" order by t.Condition_id \n");
			List<Map<String, String>> conditionLsit = dao.pageQuery(sb.toString(), null, dao.getFunName());
			for (Map<String, String> map : conditionLsit) {
				String param = map.get("CONDITION_FEILD");
				String feild="";
				String[] condition_feild = DaoFactory.getParams(request, param);
				if(condition_feild.length>1){
					for (int i = 0; i < condition_feild.length; i++) {
						feild+=condition_feild[i]+",";
					}
				}else{
					feild=condition_feild[0];
				}
				String condition_feild_sql="update query_Condition t set t.condition_value='"+feild+"' where t.query_id="+query_id+" and t.condition_feild='"+param+"'";
				dao.update(condition_feild_sql, null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	@SuppressWarnings("unchecked")
	public void memory_filed_off(){
		int res=1;
		try {
			String query_id = super.getParam("query_id");
			String sql="update query_title t set t.memory_filed=null where t.query_id="+query_id;
			dao.update(sql, null);
			String condition_feild_sql="update query_Condition t set t.condition_value=null where t.query_id="+query_id;
			dao.update(condition_feild_sql, null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
}
