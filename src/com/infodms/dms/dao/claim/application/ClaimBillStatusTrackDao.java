package com.infodms.dms.dao.claim.application;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class ClaimBillStatusTrackDao extends BaseDao{

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> pdiView(AclUserBean loginUser,RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.claim_no,\n" );
		sb.append("       a.app_color,\n" );
		sb.append("       a.app_package_name,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.model_name,a.engine_no,\n" );
		sb.append("       n.*\n" );
		sb.append("  from Tt_As_Wr_Application a, tt_as_wr_netitem n\n" );
		sb.append(" where a.id=n.id  and  a.claim_type = 10661011");
		DaoFactory.getsql(sb, "a.dealer_id", loginUser.getDealerId(), 1);
		DaoFactory.getsql(sb, "a.id", DaoFactory.getParam(request, "ID"), 1);
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLaboursById(RequestWrapper request) {
		return pageQuery("select l.*  from Tt_As_Wr_Labouritem l where l.id="+DaoFactory.getParam(request, "ID"), null,getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPartsById(RequestWrapper request) {
		return pageQuery("select p.* from Tt_As_Wr_Partsitem p where p.id="+DaoFactory.getParam(request, "ID"), null,getFunName());
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> findClaimPoById(RequestWrapper request) {
		return pageQueryMap("select a.*,(select max(d.audit_remark) from Tt_As_Wr_App_Audit_Detail d where d.audit_date = (select max(d.audit_date) from Tt_As_Wr_App_Audit_Detail d where d.claim_id = a.id)) as audit_remark from Tt_As_Wr_Application a where a.id="+DaoFactory.getParam(request, "ID"), null,getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String ywzj) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A WHERE 1=1");
		DaoFactory.getsql(sql, "A.YWZJ", ywzj, 1);
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> findoutrepair(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_Outrepair t where 1=1 and t.id='"+DaoFactory.getParam(request, "ID")+"'");
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> findoutrepairmoney(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Wr_Outrepair_money t where 1=1 and t.out_id='"+DaoFactory.getParam(request, "ID")+"'");
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> findComPoById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from tt_as_wr_compensation_app c where c.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "ID")+")");
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAccPoById(RequestWrapper request) {
		return pageQuery("select d.* from tt_claim_accessory_dtl d where d.claim_no=(select a.claim_no from tt_as_wr_application a where a.id="+DaoFactory.getParam(request, "ID")+")", null,getFunName());
	}
	

}
