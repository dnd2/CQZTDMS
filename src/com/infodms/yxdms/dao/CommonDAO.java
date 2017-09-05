package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.po.TcCodePO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class CommonDAO extends IBaseDao{

	private static CommonDAO dao = new CommonDAO();
	public static final CommonDAO getInstance(){
		dao = (dao==null)?new CommonDAO():dao;
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public int del(String tableName, String idName,String id) {
		int res=1;
		try {
			StringBuffer sb= new StringBuffer();
			sb.append("delete from "+tableName+"  t \n");
			sb.append("where  t."+idName+"='"+id+"'");
			this.delete(sb.toString(), null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findBtnQury(String mainTainSql) {
		StringBuffer sb= new StringBuffer();
		sb.append("select b.*,n.btn_name from tt_as_zyw_common_btn b,tt_as_zyw_common_btn_name n \n");
		sb.append(" where b.btn_action=n.btn_action  and b.is_del is null and  n.is_del is null \n");
		DaoFactory.getsql(sb, "b.btn_identify", mainTainSql, 1);
		sb.append(" order by b.sort_order ");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<?> findSelectList(String type, String exist, String noExist,String sql) {
		List<?> list =null;
		if(sql!=null &&!"".equals(sql)){
			list= dao.pageQuery(sql, null, dao.getFunName());
		}else if(type!=null &&!"".equals(type)){
			StringBuffer sb=new StringBuffer();//拼sql查询语句
			sb.append("select tc.* from tc_code tc where 1=1 ");
			DaoFactory.getsql(sb, "tc.type", type, 1);
			if(exist!=null &&!"".equals(exist)){
				DaoFactory.getsql(sb, "tc.code_id", exist, 6);
			}
			if(noExist!=null &&!"".equals(noExist)){
				DaoFactory.getsql(sb, "tc.code_id", noExist, 8);
			}
			list = dao.pageQuery(sb.toString(), null, getFunName());
			
		}
		return list;
	}

}
