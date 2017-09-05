package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.bean.TtIfServiceCarAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * <p>Title:ServiceInfoDao.java</p>
 *
 * <p>Description: 服务车申请表资料上传持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-17</p>
 *
 * @author subo
 * @version 1.0
 * @remark
 */
public class ServiceCarApplyUplodaDAO extends BaseDao{
	private static final ServiceCarApplyUplodaDAO dao = new ServiceCarApplyUplodaDAO();
	public static final ServiceCarApplyUplodaDAO getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: applyUploadQuery 
	* @Description: TODO(查询轿车审批通过未上传的) 
	* @author add by subo
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtIfServicecarExtPO>    返回类型 
	* @throws
	 */
	public  PageResult<TtIfServicecarExtPO> applyUploadQuery(String con, List<Object> param,int curpage,int pagesize) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder("\n");
		sql.append("select t.*,d.dealer_code as dealer_code, d.dealer_name as dealer_name,g.group_code as model_name\n");
		sql.append(" from TT_IF_SERVICECAR t, tm_vhcl_material_group g,tm_dealer d,tm_vhcl_material_group_r r\n");
		sql.append(" where  1=1\n");
		sql.append(" and t.group_id=r.material_id\n");
		sql.append(" and r.group_id=g.group_id\n");
		sql.append(" and t.dealer_id=d.dealer_id\n");
		sql.append(" and t.app_status in ("+Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS);
		sql.append(" ,").append(Constant.SERVICE_APPLY_ACTIVE_CAR_AREA_REFUS);
		sql.append(" ,").append(Constant.SERVICE_APPLY_ACTIVE_CAR_SERVICE_REFUS);
		sql.append(" ,").append(Constant.SERVICE_APPLY_ACTIVE_CAR_AUDIT_REFUS).append(")\n");
		sql.append(" and is_del=0\n");
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append("\n order by t.create_date desc\n");
		String s = sql.toString();
		PageResult<TtIfServicecarExtPO> rs = pageQuery(TtIfServicecarExtPO.class,sql.toString(), param,
				 pagesize, curpage);
		return rs;
	}
	/**
	 * 
	* @Title: applyUploadQuery 
	* @Description: TODO(车厂端查询审批通过未签收的) 
	* @author add by subo
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtIfServicecarExtPO>    返回类型 
	* @throws
	 */
		public  PageResult<TtIfServicecarExtPO> applyUploadGetQuery(String con,
				List param,int curpage,int pagesize) {
			List<TmOrgPO> list = new ArrayList<TmOrgPO>();
			StringBuilder sql = new StringBuilder();
			sql.append("select t.* ,d.dealer_code as dealer_code,t.app_status as audit_status,d.dealer_name as dealer_name,g.group_code as model_name ");
			sql.append(" from TT_IF_SERVICECAR t, tm_vhcl_material_group g,tm_dealer d,tm_vhcl_material_group_r r ");
			//sql.append(" left outer join TT_IF_SERVICECAR_AUDIT a on t.order_id=a.order_id ");
			sql.append(" where  1=1 ");
			sql.append(" and is_del=0 ");
			sql.append(" and t.group_id=r.material_id");
			sql.append(" and r.group_id=g.group_id");
			sql.append(" and t.dealer_id=d.dealer_id");
			if (con!=null&&!("").equals(con)){
				sql.append(con);
			}
			sql.append(" order by t.app_date desc ");
			PageResult<TtIfServicecarExtPO> rs = pageQuery(TtIfServicecarExtPO.class,sql.toString(), param,
					 pagesize, curpage);
			return rs;
	}
	/**
	 * 
	* @Title: queryDetailByOrderId 
	* @Description: TODO(明细查询) 
	* @param @param orderId
	* @param @return TtIfServicecarExtPO 
	* @return TtIfServicecarExtPO    返回类型 
	* @throws
	 */
	public  TtIfServicecarExtPO queryDetailByOrderId(String orderId) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id,t.order_id as order_id,t.dealer_id as dealer_id,t.remark," +
				"d.dealer_code as dealer_code,d.dealer_name as dealer_name,t.link_man as link_man," +
				"t.tel as tel,t.fax as fax,g.group_name as model_name," +
				"t.sale_amount as sale_amount,t.content as content, " +
				"a.audit_date as audit_date,u.name as audit_by_name," +
				"t.status as status,a.audit_content as audit_content");
		sql.append(" from TT_IF_SERVICECAR t ");
		sql.append(" left outer join TT_IF_SERVICECAR_AUDIT a on t.order_id=a.order_id ");
		sql.append(" left outer join TM_DEALER d on t.dealer_id=d.dealer_id ");
		sql.append(" left outer join TC_USER u on a.audit_by=u.user_id ");
		sql.append(" left outer join tm_vhcl_material_group g  on t.group_id=g.group_id ");
		sql.append(" where  1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND t.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
//		sql.append(" order by a.audit_date desc");
		TtIfServicecarExtPO tisep = new TtIfServicecarExtPO();
		PageResult<TtIfServicecarExtPO> rs = pageQuery(TtIfServicecarExtPO.class,sql.toString(), null,
				 10, 1);
		List<TtIfServicecarExtPO> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				tisep = ls.get(0);
			}
		}
		return tisep;
	}
	/**
     * Function：获得附件信息列表
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='"+id+"'");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
    /*
     * 根据orderId查看资料签收明细
     */
    @SuppressWarnings("unchecked")
	public List<TtIfServiceCarAuditBean> queryAuditDetail(String orderId){
    	StringBuffer sql = new StringBuffer("\n");
    	sql.append("select a.*,u.name as user_name,o.org_name\n");
    	sql.append("from tt_if_servicecar_audit a,tc_user u,tm_org o\n");
    	sql.append("where a.audit_by =u.user_id\n");
    	sql.append("and u.company_id = o.org_id\n");
    	sql.append("and a.order_id = '").append(orderId).append("'\n");
    	sql.append("and a.audit_status>").append(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT).append("\n");
    	sql.append("order by a.audit_date desc\n");
    	return this.select(TtIfServiceCarAuditBean.class, sql.toString(), null);
    }
	/**
	 * 
	* @Title: queryAuditDetailByOrderId 
	* @Description: TODO(审批明细) 
	* @param @param orderId
	* @param @return List<TtIfServicecarExtPO>
	* @return List<TtIfServicecarExtPO>    返回类型 
	* @throws
	 */
	public  List<TtIfServicecarExtPO> queryAuditDetailByOrderId(String orderId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t1.*,U.name as audit_by_name,o.org_name as dept_name  from TT_IF_SERVICECAR_AUDIT t1  ");
		sql.append(" left outer join TC_USER U ON T1.audit_by=u.user_id ");
		sql.append(" left outer join tm_org o on t1.org_id = o.org_id ");
		sql.append(" where 1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND t1.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
		sql.append(" ORDER BY AUDIT_DATE DESC ");
		List<TtIfServicecarExtPO> rs = select(TtIfServicecarExtPO.class,sql.toString(), null);
		return rs;
	}
	/**
	 * 
	* @Title: updateRecord 
	* @Description: TODO(新增记录) 
	* @param @param tisp   
	* @return void    返回类型 
	* @throws
	 */
	public  void updateRecord(String id,Integer app_status,String remark) {
		if(null!=id&&!"".equals(id)){
		StringBuilder sql = new StringBuilder();
		sql.append("update TT_IF_SERVICECAR	t");
		if(null!=app_status)
		sql.append("	SET t.app_status="+app_status);
		if(null!=remark)
		sql.append("	,t.remark='"+remark+"'");
		
		sql.append("	where t.id="+Long.parseLong(id));
		update(sql.toString(),null);
		}
	}
}
