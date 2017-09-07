package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.po.TtVsDriverPO;
import com.infodms.yxdms.dao.IBaseDao;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class DriverAuditDao  extends IBaseDao {

	private static final DriverAuditDao dao = new DriverAuditDao();
	public static final DriverAuditDao getInstance(){
		if (dao == null) {
			return new DriverAuditDao();
		}
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 查询司机审核列表
	 * @param request
	 * @param loginUser 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getdriverAuditList(RequestWrapper request,AclUserBean loginUser, int pageSize,int currPage) {
		 TcPosePO tcPosePO = this.getTcPosePO(loginUser.getPoseId());
		 StringBuffer sql= new StringBuffer();
		 sql.append("select  d.*,dd.logi_name,f_get_tcuser_name(d.audit_by) AUDIT_NAME  from  tt_vs_driver d left join tt_sales_logi dd on dd.logi_id=d.carrier_id where 1=1");
		 if(null!=tcPosePO.getLogiId()&&0!=tcPosePO.getLogiId()){
			 sql.append(" and d.carrier_id ='"+tcPosePO.getLogiId()+"'");
		 }
		 DaoFactory.getsql(sql, " d.status", DaoFactory.getParam(request, "status"), 1);
         DaoFactory.getsql(sql, " d.carrier_id", DaoFactory.getParam(request, "carrier_id"), 1);
         DaoFactory.getsql(sql, " d.driver_name", DaoFactory.getParam(request, "driver_name"), 2);
         DaoFactory.getsql(sql, " d.driver_phone", DaoFactory.getParam(request, "driver_phone"), 2);
        
         sql.append(" order by apply_date desc");
         return this.pageQuery(sql.toString(), null, getFunName(),pageSize,currPage);
	}
	
	@SuppressWarnings("unchecked")
	public TcPosePO getTcPosePO(Long poseId) {
		 TcPosePO po = new TcPosePO();
		 po.setPoseId(poseId);
		 List<TcPosePO> list = dao.select(po);
		 if(!CommonUtils.isNullList(list)&&list.size()>0){
			 return  list.get(0);
		 }else {
			return null;
		}
	}
	/**
	 * 修改司机审核
	 * @param driverPO
	 */
	@SuppressWarnings("unchecked")
	public void updateTtVsDriver(TtVsDriverPO driverPO) {
		TtVsDriverPO ttVsDriverPO = new TtVsDriverPO();
		ttVsDriverPO.setDriverId(driverPO.getDriverId());
		dao.update(ttVsDriverPO, driverPO);
	}
	/**
	 * 根据id查询司机的信息
	 * @param driver_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getdriverAuditByDriverId(String driver_id) {
		StringBuffer sql= new StringBuffer();
		 sql.append("select  d.* from  tt_vs_driver  d where 1=1");
		 DaoFactory.getsql(sql, "d.driver_id", driver_id, 1);
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	
	public Map<String, Object> getLogiIdByUserId(String userId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TP.LOGI_ID\n" );
		sql.append("    FROM TC_USER TU, TR_USER_POSE TUP, TC_POSE TP\n" );
		sql.append("   WHERE TU.USER_ID = TUP.USER_ID\n" );
		sql.append("     AND TUP.POSE_ID = TP.POSE_ID\n" );
		sql.append("     AND TU.USER_STATUS = 10011001\n" );
		sql.append("     AND TU.USER_TYPE = 10021001\n" );
		sql.append("     AND TU.DRIVER_FLAG = '1'\n" );
		sql.append("     AND TP.POSE_TYPE = 10021001\n" );
		sql.append("     AND TP.POSE_STATUS = 10011001\n" );
		sql.append("     AND TP.POSE_BUS_TYPE = 10781007\n" );
		sql.append("     AND TU.USER_ID = '"+userId+"'");
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	/**
	 * 根据司机申请id查询是否存在用户
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUserByDriverId(String driverId) {
		StringBuffer sql= new StringBuffer();
		sql.append("select u.* from tc_user  u  where exists (select 1 from tt_vs_driver t where t.driver_id='"+driverId+"' and (t.driver_phone=u.hand_phone or t.driver_phone=u.phone ))");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<TcUserPO> getUserByDriverId(Long driverId) {
		StringBuffer sql= new StringBuffer();
		sql.append("select u.* from tc_user  u  where exists (select 1 from tt_vs_driver t where t.driver_id='"+driverId+"' and (t.driver_phone=u.hand_phone or t.driver_phone=u.phone ))");
		return dao.select(TcUserPO.class, sql.toString(), null);
	}
	/**
	 * 更新
	 * @param userPosePO
	 */
	@SuppressWarnings("unchecked")
	public void upTrUserPosePO(TrUserPosePO userPosePO) {
		TrUserPosePO temp = new TrUserPosePO();
		temp.setUserPoseId(userPosePO.getUserPoseId());
		dao.update(temp, userPosePO);
	}
	
	/**
	 * 获取承运商列表
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTtSalesLogi(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSL.*\n" );
		sql.append("  FROM TT_SALES_LOGI TSL, TC_POSE TP\n" );
		sql.append(" WHERE TSL.LOGI_ID = TP.LOGI_ID\n" );
		sql.append("   AND TSL.STATUS = 10011001");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据用户名查询司机
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> driverCalibration(RequestWrapper request) {
		String driver_phone = DaoFactory.getParam(request, "driver_phone");
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select *  from tc_user u where u.acnt='"+driver_phone+"'");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public void updateTtVsDriverByPhone(TtVsDriverPO driverPO2) {
		TtVsDriverPO po = new TtVsDriverPO();
		po.setDriverPhone(driverPO2.getDriverPhone());
		dao.update(po, driverPO2);
	}
	

}
