/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicleStatusDao.java
* CLASS : ServiceActivityManageVehicleStatusDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---经销商管理
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageVehicleStatusDao.java,v 1.2 2010/09/06 13:05:46 liuq Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.TtAsActivityVehicleBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务车辆信息及状态查询
 * @author        :  PGM
 * CreateDate     :  2010-06-01
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageVehicleStatusDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageVehicleStatusDao.class);
	private static final ServiceActivityManageVehicleStatusDao dao = new ServiceActivityManageVehicleStatusDao ();
	public  static final ServiceActivityManageVehicleStatusDao getInstance() {
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
		sql.append("select activity_code,activity_name from tt_as_activity where STATUS in ("+Constant.SERVICEACTIVITY_STATUS_02+") and IS_DEL="+Constant.IS_DEL_00+" \n");//尚未发布
		sql.append("order by activity_code desc  \n");
        List list = dao.select(TtAsActivityPO.class, sql.toString(), null);
        return list;
	}
	/**
	 * Function         : 服务活动管理---服务车辆信息及状态查询
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageVehicleStatusQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select tav.ID, ta.ACTIVITY_ID ,ta.ACTIVITY_CODE,ta.ACTIVITY_NAME,  \n");
		sql.append("	 DECODE(tav.VIN, '', '---', tav.VIN, tav.VIN) as VIN,  \n");
		sql.append("	 DECODE(tav.LINCENSE_TAG, '', '---', tav.LINCENSE_TAG,tav.LINCENSE_TAG) as LINCENSE_TAG,  \n");
		sql.append("	 DECODE(tav.CUSTOMER_NAME, '', '---', tav.CUSTOMER_NAME,tav.CUSTOMER_NAME) as CUSTOMER_NAME, \n");
		sql.append("	 DECODE(tav.AREA, '', '---', tav.AREA,tav.AREA) as AREA,  \n");
		sql.append("	 DECODE(tav.LINKMAN, '', '---', tav.LINKMAN,tav.LINKMAN) as LINKMAN,  \n");
		sql.append("	 DECODE(d.DEALER_CODE, '', '---', d.DEALER_CODE,d.DEALER_CODE) as DEALER_CODE,  \n");
		sql.append("	 DECODE(tav.OPERATE_DEALER_CODE, '', '---', tav.OPERATE_DEALER_CODE,tav.OPERATE_DEALER_CODE) as OPERATE_DEALER_CODE,  \n");
		sql.append("	 DECODE(tav.REPAIR_DATE, '', '---',  tav.REPAIR_DATE, tav.REPAIR_DATE) as  REPAIR_DATE,  \n");
		sql.append("	 DECODE(tav.CAR_STATUS, '', '---',  tav.CAR_STATUS, tav.CAR_STATUS) as  CAR_STATUS  \n");
		sql.append("     from TT_AS_ACTIVITY ta  left join TT_AS_ACTIVITY_VEHICLE tav on ta.ACTIVITY_ID = tav.ACTIVITY_ID left join TM_DEALER d   on tav.DEALER_ID = d.DEALER_ID \n");
		sql.append("    WHERE ta.STATUS <> "+Constant.SERVICEACTIVITY_STATUS_01+" and  tav.ID is not null \n");//服务活动管理--服务活动活动状态:[10681001:尚未发布;]
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  ta.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(!"".equals(ActivityBean.getActivityCode())&&!(null==ActivityBean.getActivityCode())&&!"1".equals(ActivityBean.getActivityCode())){//活动编号不为空
			sql.append("		AND UPPER(ta.ACTIVITY_CODE) like UPPER('%"+ActivityBean.getActivityCode()+"%')\n");
		}
		if(!"".equals(ActivityBean.getDealerId())&&!(null==ActivityBean.getDealerId())){        //经销商ID
			sql.append("		AND tav.DEALER_ID ="+ActivityBean.getDealerId()+" \n");
		}
		if(!"".equals(ActivityBean.getCustomerName())&&!(null==ActivityBean.getCustomerName())){  //客户名称
			sql.append("	    AND tav.CUSTOMER_NAME  like '%"+ActivityBean.getCustomerName()+"%' \n");
		}
		if(!"".equals(ActivityBean.getDealerName())&&!(null==ActivityBean.getDealerName())){  //经销商名称
			sql.append("	    AND d.DEALER_NAME  like '%"+ActivityBean.getDealerName()+"%' \n");
		}
		sql.append("            ORDER BY tav.ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		//add by liuqiang. 2010-7-28
		TtAsActivityPO po = new TtAsActivityPO();
		try {
			po.setActivityId(rs.getLong("ACTIVITY_ID"));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
		return po;
	}
	/**
	 * Function         : VIN信息明细查询
	 * @param           : ID
	 * @return          : 根据ID查询出VIN信息明细
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-09
	 */
	public  TtAsActivityVehicleBean  serviceActivityManageVehicleStatusInfo(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT v.VIN ,d.DEALER_CODE,  v.LINCENSE_TAG, v.CUSTOMER_NAME,  v.PROVINCE, v.TOWN, v.AREA,  v.CUSTOMER_ADDRESS, v.POSTAL_CODE, v.EMAIL,  v.LINKMAN, v.LINKMAN_MOBILE,  v.LINKMAN_FAMILY_PHONE, v.LINKMAN_OFFICE_PHONE   \n");
		sql.append("  FROM  TT_AS_ACTIVITY_VEHICLE v  left join TM_DEALER d  on d.dealer_id=v.dealer_id ");
		sql.append("  where  1=1 ");
		if (null!=id&&!"".equals(id)&&!"null".equals(id)){
		sql.append(" AND v.ID = ? \n");
		params.add(id);
		}
		sql.append("order by v.VIN desc  \n");
		TtAsActivityVehicleBean VehicleBean =new TtAsActivityVehicleBean();
	    List list = dao.select(TtAsActivityVehicleBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				VehicleBean = (TtAsActivityVehicleBean) list.get(0);
			}
		}
		return VehicleBean;
	}
	/**
	 * Function         : 查询经销商ID
	 * @param           : ID
	 * @return          : 根据经销商代码dealer_Code
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-09
	 */
	public  TmDealerPO  QueryDealerID(String dealerCode){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT DEALER_ID FROM TM_DEALER   \n");
		sql.append("  where  1=1 ");
		if (null!=dealerCode&&!"".equals(dealerCode)){
		sql.append(" AND DEALER_CODE = ? \n");
		params.add(dealerCode);
		}
		sql.append("ORDER BY DEALER_ID DESC  \n");
		TmDealerPO dealerPO =new TmDealerPO();
	    List list = dao.select(TmDealerPO.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				dealerPO = (TmDealerPO) list.get(0);
			}
		}
		return dealerPO;
	}
	/**
	 * Function       :  修改车辆状态为：已经下发
	 * @param         :  request-活动ID
	 * @return        :  服务活动计划下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public static void  serviceActivityManageVehicleCarStatus(TtAsActivityVehiclePO VehiclePo,TtAsActivityVehiclePO VehiclePoContent){
		dao.update(VehiclePo, VehiclePoContent);
	}
	/**
	 * Function         : 服务活动管理---服务车辆信息及状态查询，根据VIN查询车辆维修历史明细信息
	 * @param           : vin
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageVehicleVin(String vin, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();//IS_RED :1 表示取消结算；IS_VALID ：0 表示有效数据
		sql.append("	  SELECT *  FROM (select a.ID, a.RO_NO,  a.RO_TYPE, a.START_MILEAGE, a.DEALER_ID,decode(a.IS_RED_CHANGE_MILEAGE, 0, '否', '是') AS IS_RED_CHANGE_MILEAGE,  \n");
		sql.append("	  a.VIN, a.LICENSE, a.OWNER_NAME,  a.OWNER_PROPERTY,  a.IS_SPECIALCASE,  \n");
		sql.append("	  a.START_TIME, a.END_TIME_SUPPOSED, '附加工时' ITEMTYPE, b.ADD_ITEM_CODE,  \n");
		sql.append("	  b.ADD_ITEM_NAME, '' ERRORDESC, '' NUMUNIT,  b.CHARGE_MODE \n");
		sql.append("	  from TT_AS_RO_ADDITEM b, TT_AS_REPAIR_ORDER a  \n");
		sql.append("	  where 1 = 1 and a.id = b.id and a.LAST_BALANCE_NO is null  and a.IS_RED <> 1 and a.IS_VALID = 0 \n");
		if(!"".equals(vin)&&null!=vin){
			sql.append("	 AND a.VIN = '"+vin+"' \n");
		}
		sql.append("	  union select a.ID,  a.RO_NO,  a.RO_TYPE, a.START_MILEAGE, a.DEALER_ID,  \n");
		sql.append("	  decode(a.IS_RED_CHANGE_MILEAGE, 0, '否', '是') AS IS_RED_CHANGE_MILEAGE,  \n");
		sql.append("	  a.VIN, a.LICENSE, a.OWNER_NAME,  a.OWNER_PROPERTY,  a.IS_SPECIALCASE,  \n");
		sql.append("	  a.START_TIME,  a.END_TIME_SUPPOSED,  '维修项目' ITEMTYPE,  b.SGM_LABOUR_CODE,  \n");
		sql.append("      b.LABOUR_NAME,  b.TROUBLE_DESC,   '',  b.CHARGE_MODE  \n");
		sql.append("      from TT_AS_RO_REPAIRITEM b, TT_AS_REPAIR_ORDER a  \n");//服务活动管理--服务活动活动状态:[10681001:尚未发布;]
		sql.append("       where 1 = 1 and a.id = b.id and a.LAST_BALANCE_NO is null and a.IS_RED <> 1 and a.IS_VALID = 0  \n");
		if(!"".equals(vin)&&null!=vin){
			sql.append("	 AND a.VIN = '"+vin+"' \n");
		}
		sql.append("	  union   select a.ID, a.RO_NO,  a.RO_TYPE,  a.START_MILEAGE, a.DEALER_ID, \n");
		sql.append("	  decode(a.IS_RED_CHANGE_MILEAGE, 0, '否', '是') AS IS_RED_CHANGE_MILEAGE, \n");
		sql.append("	  a.VIN, a.LICENSE, a.OWNER_NAME,  a.OWNER_PROPERTY, a.IS_SPECIALCASE, \n");
		sql.append("	  a.START_TIME,  a.END_TIME_SUPPOSED,  '销售零件' ITEMTYPE,  b.PART_NO,  b.PART_NAME,  '', b.PART_QUANTITY || '/' || b.UNIT, b.CHARGE_MODE \n");
		sql.append("	  from TT_AS_RO_SALEPARTS b, TT_AS_REPAIR_ORDER a \n");
		sql.append("	    where 1 = 1  and a.id = b.id  and a.LAST_BALANCE_NO is null and a.IS_RED <> 1  and a.IS_VALID = 0 \n");
		if(!"".equals(vin)&&null!=vin){
			sql.append("	 AND a.VIN = '"+vin+"' \n");
		}
		sql.append("	   union  SELECT ID,  ro_no,  ro_type,  start_mileage,  DEALER_ID, is_red_change_mileage,  \n");
		sql.append("	   vin,  license, owner_name,  owner_property,  is_specialcase,  start_time,  end_time_supposed,  \n");
		sql.append("	    itemtype, part_no,  part_name,  '',  xx, charge_mode  \n");
		sql.append("	     from (select distinct a.ID, a.RO_NO,  a.RO_TYPE,  a.START_MILEAGE, a.DEALER_ID,  \n");
		sql.append("	     decode(a.IS_RED_CHANGE_MILEAGE, 0, '否', '是') AS IS_RED_CHANGE_MILEAGE,  \n");
		sql.append("	      a.VIN,  a.LICENSE, a.OWNER_NAME,  a.OWNER_PROPERTY,  a.IS_SPECIALCASE,  \n");
		
		sql.append("	      a.START_TIME,  a.END_TIME_SUPPOSED,  '维修零件' ITEMTYPE, b.PART_NO, b.PART_NAME,  \n");
		sql.append("	        sum(b.PART_QUANTITY) over(partition by b.id, b.part_no) || '/' || b.UNIT as xx,  \n");
		sql.append("	      sum(b.part_quantity) over(partition by b.id, b.part_no) as sum,  b.CHARGE_MODE,  \n");
		sql.append("	        dense_rank() over(partition by b.id, b.part_no order by a.rowid) as ind  \n");
		sql.append("	       from TT_AS_RO_REPAIRPARTS b, TT_AS_REPAIR_ORDER a  \n");
		sql.append("	      where 1 = 1  and a.id = b.id and a.LAST_BALANCE_NO is null and a.IS_RED <> 1  and a.IS_VALID = 0 \n");
		if(!"".equals(vin)&&null!=vin){
			sql.append("	 AND a.VIN = '"+vin+"' \n");
		}
		sql.append("	       ) x  where x.sum > 0   and ind = 1) ORDER BY START_TIME DESC, RO_NO DESC\n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<TtAsActivityVehiclePO> queryVehicleByVin(TtAsActivityVehiclePO po) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT VIN FROM Tt_As_Activity_Vehicle \n");
		sql.append("WHERE ACTIVITY_ID = ").append(po.getActivityId());
		sql.append("AND VIN = '").append(po.getVin()).append("'");
		List<TtAsActivityVehiclePO> pos = select(TtAsActivityVehiclePO.class, sql.toString(), null);
		return pos;
	}

}