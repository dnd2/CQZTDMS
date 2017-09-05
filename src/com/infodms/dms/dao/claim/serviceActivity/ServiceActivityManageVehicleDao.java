/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicleDao.java
* CLASS : ServiceActivityManageVehicleDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理---车辆信息确认.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-09| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageVehicleDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityVehicleBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---车辆信息确认
 * @author        :  PGM
 * CreateDate     :  2010-06-09
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageVehicleDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageVehicleDao.class);
	private static final ServiceActivityManageVehicleDao dao = new ServiceActivityManageVehicleDao ();
	public  static final ServiceActivityManageVehicleDao getInstance() {
		return dao;
	}
	/**
	 * Function         : 服务活动管理---查询活动编码
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-06-09
	 */
	public List serviceActivityCode(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_activity where STATUS in ("+Constant.SERVICEACTIVITY_STATUS_01+","+Constant.SERVICEACTIVITY_STATUS_02+") and IS_DEL="+Constant.IS_DEL_00+" \n");//尚未发布,已经发布,重新发布
		sql.append("order by activity_code desc  \n");
        List list = dao.select(TtAsActivityPO.class, sql.toString(), null);
        return list;
	}
	/**
	 * Function         : 服务活动管理---车辆信息确认
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 
	 */
	public  PageResult<Map<String, Object>>  getAllServiceActivityManageVehicleInfo(TtAsActivityVehicleBean VehicleBean,String checkedDealer, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select a.ID, b.ACTIVITY_ID,b.ACTIVITY_CODE,b.ACTIVITY_NAME,b.STATUS, a.VIN, d.DEALER_CODE,  d.DEALER_NAME, a.SALE_STATUS,  a.REPAIR_STATUS  \n");
		sql.append("    from TT_AS_ACTIVITY_VEHICLE a left join TT_AS_ACTIVITY b on a.ACTIVITY_ID = b.ACTIVITY_ID  left join TM_DEALER d on d.dealer_id=a.dealer_id  where 1=1 \n");
		if(!"".equals(VehicleBean.getCompanyId())&&!(null==VehicleBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  B.COMPANY_ID ='"+VehicleBean.getCompanyId()+"' \n");
		}
		if(!"".equals(VehicleBean.getActivityCodes())&&null!=VehicleBean.getActivityCodes()&&!"1".equals(VehicleBean.getActivityCodes())){//活动编号不为空
			sql.append("		AND b.ACTIVITY_CODE='"+VehicleBean.getActivityCodes()+"' \n");
		}
		if(!"".equals(VehicleBean.getStatus())&&!(null==VehicleBean.getStatus())){//活动状态不为空
			sql.append("		AND b.STATUS='"+VehicleBean.getStatus()+"' \n");
		}
		if(!"".equals(VehicleBean.getActivityCode())&&null!=VehicleBean.getActivityCode()&&!"1".equals(VehicleBean.getActivityCode())){//活动名称不为空
			sql.append("    and   b.ACTIVITY_CODE='"+VehicleBean.getActivityCode()+"' \n");
		}
		if(!"".equals(VehicleBean.getRepairStatus())&&!(null==VehicleBean.getRepairStatus())){      //维修状态不为空
			sql.append("		AND a.REPAIR_STATUS ="+VehicleBean.getRepairStatus()+" \n");
		}
		if(!"".equals(VehicleBean.getSaleStatus())&&!(null==VehicleBean.getSaleStatus())){         //销售状态不为空
			sql.append("	    AND a.SALE_STATUS  ="+VehicleBean.getSaleStatus()+"\n");
		}
		if ("1".equals(checkedDealer)) {//车辆责任经销商不在执行经销商列表中
			sql.append(" AND  not exists (select trim(c.dealer_code) from TT_AS_ACTIVITY_DEALER c where c.activity_id in (select activity_id  from TT_AS_ACTIVITY where 1=1 \n");
			if(!"".equals(VehicleBean.getActivityCode())&&null!=VehicleBean.getActivityCode()&&!"1".equals(VehicleBean.getActivityCode())){//活动编号不为空
				sql.append("    and   ACTIVITY_CODE='"+VehicleBean.getActivityCode()+"' )\n");
			}else{
				sql.append("  ) \n");
			}
			sql.append("    and  a.dealer_id =c.dealer_id) \n");
		} else  {//车辆责任经销商在执行经销商列表中
			sql.append(" AND  exists (select trim(c.dealer_code) from TT_AS_ACTIVITY_DEALER c where c.activity_id in (select activity_id  from TT_AS_ACTIVITY where 1=1 \n");
			if(!"".equals(VehicleBean.getActivityCode())&&null!=VehicleBean.getActivityCode()&&!"1".equals(VehicleBean.getActivityCode())){//活动编号不为空
				sql.append("    and   ACTIVITY_CODE='"+VehicleBean.getActivityCode()+"' )\n");
			}else{
				sql.append("  ) \n");
			}
			sql.append("    and  a.dealer_id =c.dealer_id) \n");
		}
		sql.append("            ORDER BY a.VIN desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  服务活动管理车辆信息确认修改
	 * @param         :  
	 * @return        :  
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageModify(TtAsActivityVehiclePO VehiclePO){
		TtAsActivityVehiclePO VehiclePOCon=new TtAsActivityVehiclePO();//条件
		//VehiclePOCon.setActivityId(VehiclePO.getActivityId());
		VehiclePOCon.setId(VehiclePO.getId());
		TtAsActivityVehiclePO VehiclePOC=new TtAsActivityVehiclePO();//内容
		VehiclePOC.setDealerId(VehiclePO.getDealerId());//经销商ID
		VehiclePOC.setSaleStatus(VehiclePO.getSaleStatus());//销售状态
		VehiclePOC.setRepairStatus(VehiclePO.getRepairStatus());//维修状态
		dao.update(VehiclePOCon,VehiclePOC);
		
	}
	/**
	 * Function         : 服务活动管理---车辆信息查询销售状态和维修状态
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public TtAsActivityVehiclePO serviceActivityManageVehicleSelectStatus(String activityId,String vin){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT ID,ACTIVITY_ID,SALE_STATUS,REPAIR_STATUS FROM TT_AS_ACTIVITY_VEHICLE \n");
		if(!"".equals(activityId)&&null!=activityId){//活动ID不为空
			sql.append("	WHERE  ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		if(!"".equals(vin)&&null!=vin){//VIN不为空
			sql.append("	and  VIN = ? \n");
			params.add(""+vin+"");
		}
		sql.append("ORDER BY ID DESC  \n");
        List<TtAsActivityVehiclePO> list = dao.select(TtAsActivityVehiclePO.class, sql.toString(), params);
        TtAsActivityVehiclePO vehiclePO=new TtAsActivityVehiclePO();
		if (list!=null){
			if (list.size()>0) {
				vehiclePO = (TtAsActivityVehiclePO) list.get(0);
			}
		}
        return vehiclePO;
	}
	/**
	 * Function         : 服务活动管理---根据经销商代码，查询经销商ID
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public TtAsActivityVehiclePO serviceActivityManageQueryDealerID(String dealerCode){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT DEALER_ID FROM TM_DEALER \n");
		if(!"".equals(dealerCode)&&null!=dealerCode){//活动ID不为空
			sql.append("	WHERE  DEALER_CODE = ? \n");
			params.add(dealerCode);
		}
		sql.append("ORDER BY DEALER_ID DESC  \n");
        List<TtAsActivityVehiclePO> list = dao.select(TtAsActivityVehiclePO.class, sql.toString(), params);
        TtAsActivityVehiclePO vehiclePO=new TtAsActivityVehiclePO();
		if (list!=null){
			if (list.size()>0) {
				vehiclePO = (TtAsActivityVehiclePO) list.get(0);
			}
		}
        return vehiclePO;
	}
	/**
	 * Function       :  删除车辆信息
	 * @param         :  request-工单号
	 * @return        :  服务活动管理
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-09
	 */
	public static void  serviceActivityManageDelete(String[] orderIds){
		for (int i = 0;i<orderIds.length;i++) {
			TtAsActivityVehiclePO  VehiclePO =new TtAsActivityVehiclePO();//orderId条件
			VehiclePO.setId(Long.parseLong(orderIds[i]));
			dao.delete(VehiclePO);
		}
	}
}