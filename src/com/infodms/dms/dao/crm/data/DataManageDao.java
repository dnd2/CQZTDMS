package com.infodms.dms.dao.crm.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.eai.po.TcCodePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.exceptions.DAOException;


/**
 * 
 * <p>ComplaintAuditDao.java</p>
 *
 * <p>Description: 
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-2</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class DataManageDao extends BaseDao<PO>{
	
	private static final DataManageDao dao = new DataManageDao();
	private POFactory factory = POFactoryBuilder.getInstance();
	
	public static final DataManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDataQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String code=map.get("code");
		String name=map.get("name");
		String status=map.get("status");
		String parName=map.get("parName");
		StringBuilder sql= new StringBuilder();
		sql.append("select ts.code_id,\n" );
		sql.append("       ts.type,\n" );
		sql.append("       ts.type_name,\n" );
		sql.append("       ts.code_desc,\n" );
		sql.append("       ts.status,\n" );
		sql.append("       ts.if_dealer,\n" );
		sql.append("       ts.if_visible,\n" );
		sql.append("       null dealer_id,\n" );
		sql.append("        ts.code_level,");
		sql.append("       nvl(td.dealer_code, 'SUZUKI') dealer_code,\n" );
		sql.append("       nvl(td.dealer_shortname, '重庆长安铃木汽车有限公司') dealer_shortName\n" );
		sql.append("  from tc_code ts, tm_dealer td\n" );
		sql.append(" where ts.dealer_id = td.dealer_id(+)\n" );
		//sql.append("   and ts.if_visible = 10041001\n" );
		if(null!=name&&!"".equals(name)){
			sql.append(" and ts.code_desc  like'%"+name+"%'\n");
		}
		if(null!=parName&&!"".equals(parName)){
			sql.append(" and ts.type_name  like'%"+parName+"%'\n");
		}
		if(null!=code&&!"".equals(code)){
			sql.append(" and ts.code_id  ='"+code+"'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append(" and ts.status ="+status);
		}
		sql.append("   order by ts.type ,ts.code_id");
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDataDealerQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String typeName=map.get("typeName");
		String status=map.get("status");
		String dealerId=map.get("dealerId");
		String ifDealer=map.get("ifDealer");
		StringBuilder sql= new StringBuilder();
		sql.append("select ts.code_id,\n" );
		sql.append("       ts.type,\n" );
		sql.append("       ts.type_name,\n" );
		sql.append("       ts.code_desc,\n" );
		sql.append("       ts.status,\n" );
		sql.append("       ts.if_dealer,\n" );
		sql.append("       ts.dealer_id,\n" );
		sql.append("       ts.dealer_code,\n" );
		sql.append("       ts.code_LEVEL,\n" );
		sql.append("       ts.dealer_shortname\n" );
		sql.append("  from (select tc.code_id,\n" );
		sql.append("               tc.type,\n" );
		sql.append("               tc.type_name,\n" );
		sql.append("               tc.code_desc,\n" );
		sql.append("               tc.status,\n" );
		sql.append("               tc.if_dealer,\n" );
		sql.append("               null         dealer_id,\n" );
		sql.append("               'SUZUKI'         dealer_code,\n" );
		sql.append("               TC.CODE_LEVEL         CODE_LEVEL,\n" );
		sql.append("               '重庆长安铃木汽车有限公司' dealer_shortName\n" );
		sql.append("          from tc_code tc\n" );
		sql.append("         where  tc.dealer_id is null \n" );
		sql.append("        union\n" );
		sql.append("        select tcd.code_id,\n" );
		sql.append("               tcd.type,\n" );
		sql.append("               tcd.type_name,\n" );
		sql.append("               tcd.code_desc,\n" );
		sql.append("               tcd.status,\n" );
		sql.append("               10041001 if_dealer,\n" );
		sql.append("               tcd.dealer_id,\n" );
		sql.append("               td.dealer_code,\n" );
		sql.append("               TCD.CODE_LEVEL       CODE_LEVEL,\n" );
		sql.append("               td.dealer_shortname\n" );
		
		sql.append("          from tc_code tcd, tm_dealer td\n" );
		sql.append("         where td.dealer_id = tcd.dealer_id \n" );
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tcd.dealer_id=  "+dealerId+"\n");
		}
		sql.append("  )ts where 1 = 1 \n");
		if(null!=typeName&&!"".equals(typeName)){
			sql.append(" and ts.code_desc  like'%"+typeName+"%'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append(" and ts.status ="+status);
		}
//		if(null!=ifDealer&&!"".equals(ifDealer)){
			sql.append(" and ts.if_dealer =10041001\n");
//		}
		
		sql.append("  order by dealer_shortname,type,code_id");
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public List<Map<String, Object>> getDataNextList(
			Map<String, String> map) {
		String type=map.get("type");
		String dealerId=map.get("dealerId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tc.code_id,\n" );
		sql.append("       tc.type,\n" );
		sql.append("       tc.type_name,\n" );
		sql.append("       tc.code_desc,\n" );
		sql.append("       tc.status,\n" );
		sql.append("       tc.if_dealer,\n" );
		sql.append("       tc.dealer_id dealer_id,\n" );
		sql.append("       nvl(td.dealer_code, 'SUZUKI') dealer_code,\n" );
		sql.append("       nvl(td.dealer_shortname, '重庆长安铃木汽车有限公司') dealer_shortName\n" );
		sql.append("  from tc_code tc, tm_dealer td\n" );
		sql.append(" where tc.dealer_id = td.dealer_id(+)");


		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and td.dealer_id=  "+dealerId+"\n");
		}
		sql.append(" and tc.type="+type);

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}




	/**
	 * 功能树 createBy ChenLiang createDate 2009-10-28
	 * 
	 * @param iid
	 *            上级部门ID
	 */
	public  List<TcCodePO> getCodeList() {
		StringBuilder sql= new StringBuilder();
		sql.append("select ts.type,ts.code_id,ts.code_desc from (select t.type, t.code_id, t.code_desc,substr(t.code_id,0,4) sort1 ,substr(t.code_id,0,8) short2\n" );
		sql.append("  from tc_code t\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and t.code_id in\n" );
		sql.append("       (select tc.code_id\n" );
		sql.append("          from tc_code tc\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("         start with tc.type = 0\n" );
		sql.append("        connect by PRIOR Tc.code_id = Tc.type)) ts order by  ts.code_id,ts.sort1");


		
		List<TcCodePO> lst = factory.select(sql.toString(), null,
				new com.infoservice.po3.core.callback.DAOCallback<TcCodePO>() {
			public TcCodePO wrapper(ResultSet rs, int idx) {
				TcCodePO bean = new TcCodePO();
				try {
					bean.setType(rs.getString("type"));
					bean.setCodeId(rs.getString("code_Id"));
					bean.setCodeDesc(rs.getString("code_desc"));
					//bean.setChildCount(rs.getString("childCount"));
				} catch (SQLException e) {
					throw new DAOException(e);
				}
				return bean;
			}
		});
		
		return lst;
	}
	
	
	
	
}
