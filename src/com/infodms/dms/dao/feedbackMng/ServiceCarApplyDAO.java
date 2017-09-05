package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ServiceCarApplyDAO 
* @Description: TODO(服务车申请表) 
* @author wangchao 
* @date May 24, 2010 5:27:38 PM 
*
 */
public class ServiceCarApplyDAO extends BaseDao{
	private static final ServiceCarApplyDAO dao = new ServiceCarApplyDAO();
	public static final ServiceCarApplyDAO getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtIfServicecarExtPO>    返回类型 
	* @throws
	* modify by xiayanpeng 去除左联，此处无需
	 */
	public  PageResult<TtIfServicecarExtPO> applyQuery(String con, List<Object> param,int curpage,int pagesize) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.*,d.dealer_code as dealer_code, d.dealer_name as dealer_name,g.group_code as model_name ");
		sql.append(" from TT_IF_SERVICECAR t, tm_vhcl_material_group g,tm_dealer d  ");
		sql.append(" where  1=1 ");
		sql.append(" and g.group_id = (select group_id from tm_vhcl_material_group_r where material_id = t.group_id)");
		sql.append(" and t.dealer_id=d.dealer_id");
		sql.append(" and is_del=0 ");
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append(" order by t.create_date desc ");
		String s = sql.toString();
		PageResult<TtIfServicecarExtPO> rs = pageQuery(TtIfServicecarExtPO.class,sql.toString(), param,
				 pagesize, curpage);
		return rs;
	}
	/**
	 * 
	* @Title: queryByOrderId 
	* @Description: TODO(根据工单号查询服务车申请表) 
	* @param @param orderId
	* @param @return TtIfServicecarPO
	* @return TtIfServicecarPO    返回类型 
	* @throws
	 */
	public  TtIfServicecarPO queryByOrderId(String orderId) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		TtIfServicecarPO tisep = new TtIfServicecarPO();
		tisep.setOrderId(orderId);
		List<TtIfServicecarPO> ls = select(tisep);
		if (ls!=null) {
			if (ls.size()>0) {
				tisep = ls.get(0);
			}
		}
		
		return tisep;
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
		sql.append("select t.order_id as order_id,t.dealer_id as dealer_id," +
				"d.dealer_code as dealer_code,d.dealer_name as dealer_name,t.link_man as link_man," +
				"t.tel as tel,t.fax as fax,g.group_code as model_name," +
				"t.sale_amount as sale_amount,t.content as content, " +
				"a.audit_date as audit_date,u.name as audit_by_name," +
				"t.status as status,a.audit_content as audit_content");
		sql.append(" from TT_IF_SERVICECAR t ");
		sql.append(" left outer join TT_IF_SERVICECAR_AUDIT a on t.order_id=a.order_id ");
		sql.append(" left outer join TM_DEALER d on t.dealer_id=d.dealer_id ");
		sql.append(" left outer join TC_USER u on a.audit_by=u.user_id ");
		sql.append("  ,tm_vhcl_material_group g\n");
		sql.append(" where g.group_id = (select group_id from tm_vhcl_material_group_r where material_id=t.group_id)\n");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND t.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
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
	* @Title: submit 
	* @Description: TODO(申请上报) 
	* @param @param orderIds    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public  void submit(String[] orderIds,AclUserBean user) {
		Date date = new Date();
		for (int i = 0;i<orderIds.length;i++) {
			TtIfServicecarPO tisep = new TtIfServicecarPO();
			tisep.setOrderId(orderIds[i]);
			TtIfServicecarPO tisepU = new TtIfServicecarPO();
			tisepU.setOrderId(orderIds[i]);
			tisepU.setAppStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED); //已上报
			tisepU.setAppDate(date);
			tisepU.setUpdateDate(date);
			update(tisep, tisepU);
			
			TtIfServicecarAuditPO tisap = new TtIfServicecarAuditPO();
			//modify by xiayanpeng begin 未插入主键ID
			tisap.setId(Long.parseLong(SequenceManager.getSequence("")));
			//modify by xiayanpeng end 
			tisap.setOrderId(orderIds[i]);
			tisap.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED); //已上报
			tisap.setAuditDate(date);
			tisap.setAuditBy(user.getUserId());
			tisap.setOrgId(user.getOrgId());
			insert(tisap);
		}
	}
	/**
	 * 
	* @Title: deleteRecord 
	* @Description: TODO(删除记录) 
	* @param @param orderIds   
	* @return void    返回类型 
	* @throws
	 */
	public  void deleteRecord(String[] orderIds) {
		for (int i = 0;i<orderIds.length;i++) {
			TtIfServicecarPO tisep = new TtIfServicecarPO();
			tisep.setOrderId(orderIds[i]);
			TtIfServicecarPO tisepU = new TtIfServicecarPO();
			tisepU.setOrderId(orderIds[i]);
			tisepU.setIsDel(1); //将其设置为1，已删除
			update(tisep, tisepU);
		}
	}
	/**
	 * 
	* @Title: updateRecord 
	* @Description: TODO(修改记录) 
	* @param @param orderId
	* @param @param tisp   
	* @return void    返回类型 
	* @throws
	 */
	public  void updateRecord(String orderId,TtIfServicecarPO tisp) {
		
			TtIfServicecarPO tisep = new TtIfServicecarPO();
			tisep.setOrderId(orderId);
			update(tisep, tisp);
		
	}
	/**
	 * 
	* @Title: addRecord 
	* @Description: TODO(新增记录) 
	* @param @param tisp   
	* @return void    返回类型 
	* @throws
	 */
	public  void addRecord(TtIfServicecarPO tisp) {
		String activityId =SequenceManager.getSequence(""); 
		tisp.setId(Long.parseLong(activityId));
		insert(tisp);
	}
	/*
	 * 根据GROUP_ID查询物料信息
	 * @param po.id
	 * @return TmVhclMaterialGroupPO
	 */
	public TmVhclMaterialGroupPO getMaterialByGID(TmVhclMaterialGroupPO po){
		List<TmVhclMaterialGroupPO> lists = this.select(po);
		if(lists.size()>0)
			return lists.get(0);
		return null;
	}
	
	/*
	 * 服务车导出功能
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getServiceCarExl(String con){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select a.sale_amount,a.status,\n");
		sql.append("b.dealer_code,b.dealer_name,b.link_man,b.phone,b.fax_no,\n");
		sql.append("g.group_name,\n");
		sql.append("(select group_name from tm_vhcl_material_group where group_id=g.parent_group_id) as parent_group_name\n");
		sql.append("from tt_if_servicecar a,tm_dealer b,tm_vhcl_material_group g\n");
		sql.append("where a.dealer_id=b.dealer_id \n");
		sql.append("and a.group_id = g.group_id \n");
		sql.append(con);
		
		return pageQuery(sql.toString(), null,null);
	}
}
