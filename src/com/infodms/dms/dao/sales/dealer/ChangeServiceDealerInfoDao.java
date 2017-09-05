package com.infodms.dms.dao.sales.dealer;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: CHANADMS
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-5
 * 
 * @author zjy
 * @mail zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark
 */
public class ChangeServiceDealerInfoDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ChangeServiceDealerInfoDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static ChangeServiceDealerInfoDao dao = new ChangeServiceDealerInfoDao();

	public static final ChangeServiceDealerInfoDao getInstance() {
		if (dao == null) {
			dao = new ChangeServiceDealerInfoDao();
		}
		return dao;
	}
	private ChangeServiceDealerInfoDao() {}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	public PageResult<Map<String, Object>> queryServiceDealerInfo(Map<String, Object> map, int curPage, int pageSize,AclUserBean logonUser) throws Exception {

		String itemName = CommonUtils.checkNull(map.get("itemName"));
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		String STATUS = CommonUtils.checkNull(map.get("STATUS"));
		
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));

		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select  (select name from tc_user where user_id = a.audit_by_DEALER) as audit_by_DEALER_name,(select name from tc_user where user_id = a.audit_by) as audit_by_name,"
				+ "a.*,t2.root_org_name,t2.org_name,(select code_desc from tc_code where code_id = a.audit_status) as audit_status_name "
				+ "from tmp_tm_dealer_new a,vw_org_dealer_service t2 where a.dealer_id=t2.dealer_id\n");
		DaoFactory.getsql(sbSql, "T2.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "T2.ORG_ID", __province_org, 1);
		if (!itemName.equals("")) {
			sbSql.append("   and a.item_Name = ?\n");
			params.add(itemName);
		}
		if (!STATUS.equals("")) {
			sbSql.append("   and a.audit_STATUS = ?\n");
			params.add(STATUS);
		}
		if (!dealerCode.equals("")) {
			sbSql.append("   and a.dealer_Code  like '%"+dealerCode+"%'\n");
		}
		if (!dealerName.equals("")) {
			sbSql.append("   and a.dealer_Name  like '%"+dealerName+"%'\n");
		}
		if (!dealEnd.equals("")) {
			sbSql.append("   and trunc(a.create_date) <= trunc(to_date('"+dealEnd+"','yyyy-mm-dd'))\n");
		}
		
		if (!dealStart.equals("")) {
			sbSql.append("   and trunc(a.create_date) >= trunc(to_date('"+dealStart+"','yyyy-mm-dd'))\n");
		}
		if (!CommonUtils.checkNull(logonUser.getDealerId()).equals("")) {
			sbSql.append("   and a.dealer_id = "+logonUser.getDealerId()+"\n");
		}

//		if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_VS){
//			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("VOD.DEALER_ID", logonUser));
//		}else if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WR){
//			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByService("VOD", logonUser));
//			Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
//			if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue()){
//				sbSql.append(" )\n ");
//			}
//		}
		
		sbSql.append("  order by a.dealer_id,a.VAR desc");
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String, Object>> queryAuditServiceDealerInfo1(Map<String, Object> map, int curPage, int pageSize) throws Exception {


		String itemName = CommonUtils.checkNull(map.get("itemName"));
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		
		String STATUS = CommonUtils.checkNull(map.get("STATUS"));
		
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));

		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select (select name from tc_user where user_id = a.audit_by) as audit_by_name,a.* from tmp_tm_dealer_new a ,vw_org_dealer_service t2 where a.dealer_id=t2.dealer_id\n");
		DaoFactory.getsql(sbSql, "T2.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "T2.ORG_ID", __province_org, 1);
		if (!itemName.equals("")) {
			sbSql.append("   and a.item_Name = ?\n");
			params.add(itemName);
		}
		if (!dealerCode.equals("")) {
			sbSql.append("   and a.dealer_Code like '%"+dealerCode+"%'\n");
//			params.add(dealerCode);
		}
		if (!dealerName.equals("")) {
			sbSql.append("   and a.dealer_Name like '%"+dealerName+"%'\n");
//			params.add(dealerName);
		}
		if (!dealEnd.equals("")) {
			sbSql.append("   and trunc(a.create_date) <= trunc(to_date('"+dealEnd+"','yyyy-mm-dd'))\n");
		}
		
		if (!dealStart.equals("")) {
			sbSql.append("   and trunc(a.create_date) >= trunc(to_date('"+dealStart+"','yyyy-mm-dd'))\n");
		}
		if (!STATUS.equals("")) {
			sbSql.append("   and a.audit_STATUS = ?\n");
			params.add(STATUS);
		}
		sbSql.append("   and a.audit_Status = 20051001\n");
		sbSql.append("  order by a.dealer_id,a.var desc");
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String, Object>> queryAuditServiceDealerInfo2(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		String itemName = CommonUtils.checkNull(map.get("itemName"));
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		
		String STATUS = CommonUtils.checkNull(map.get("STATUS"));
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select (select name from tc_user where user_id = a.audit_by_dealer) as audit_by_dealer_name,(select name from tc_user where user_id = a.audit_by) as audit_by_name,a.* from tmp_tm_dealer_new a ,vw_org_dealer_service t2 where a.dealer_id=t2.dealer_id\n");
		DaoFactory.getsql(sbSql, "T2.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "T2.ORG_ID", __province_org, 1);
		if (!itemName.equals("")) {
			sbSql.append("   and a.item_Name = ?\n");
			params.add(itemName);
		}
		if (!dealerCode.equals("")) {
			sbSql.append("   and a.dealer_Code  like '%"+dealerCode+"%'\n");
//			params.add(dealerCode);
		}
		if (!dealerName.equals("")) {
			sbSql.append("   and a.dealer_Name  like '%"+dealerName+"%'\n");
//			params.add(dealerName);
		}
		if (!dealEnd.equals("")) {
			sbSql.append("   and trunc(a.create_date) <= trunc(to_date('"+dealEnd+"','yyyy-mm-dd'))\n");
		}
		
		if (!dealStart.equals("")) {
			sbSql.append("   and trunc(a.create_date) >= trunc(to_date('"+dealStart+"','yyyy-mm-dd'))\n");
		}
		if (!STATUS.equals("")) {
			sbSql.append("   and a.audit_STATUS = ?\n");
			params.add(STATUS);
		}
		sbSql.append("   and a.audit_Status = 20051002\n");
		sbSql.append("  order by a.dealer_id,a.var desc");
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	public String getMaxVar(String dealerId) throws Exception {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String var = "";
		sbSql.append("SELECT MAX(VAR) AS VAR FROM tmp_tm_dealer_new WHERE DEALER_ID = ?\n");
		params.add(dealerId);
		Map v = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if(v.get("VAR")!=null){
			var = String.valueOf((new Integer(v.get("VAR").toString())+1));
		}else{
			var = "1";
		}
		return var;
	}
	
	public boolean checkAuditStatus(String dealerId) throws Exception {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String var = getMaxVar(dealerId);
		boolean exists = false;
		sbSql.append("select distinct t.audit_status from tmp_tm_dealer_new t WHERE DEALER_ID = ? and var = ? and audit_status not in(20051003,20051004)\n");
		params.add(dealerId);
		params.add(new Integer(var)-1);
		Map v = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if(v!=null&&v.size()>0){
			exists = true;
		}else{
			exists = false;
		}
		return exists;
	}
}
