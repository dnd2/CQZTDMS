/**********************************************************************
* <pre>
* FILE : ServiceActivityVinImportDao.java
* CLASS : ServiceActivityVinImportDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理.
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
 * $Id: ServiceActivityVinImportDao.java,v 1.1 2010/08/16 01:42:19 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtAsRecallVehiclePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---VIN清单导入
 * @author        :  PGM
 * CreateDate     :  2010-06-03
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityVinImportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityVinImportDao.class);
	private static final ServiceActivityVinImportDao dao = new ServiceActivityVinImportDao ();
	public  static final ServiceActivityVinImportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Function         : 服务活动管理---VIN清单导入删除(倒入VIN之前清空临时表)
	 * @param           : 活动编号
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public void serviceActivityVinImportDelete(TtAsRecallVehiclePO VehiclePO){
		dao.delete(VehiclePO);
	}
	/**
	 * Function         : 服务活动管理---VIN清单导入新增
	 * @param           : 活动编号
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public void serviceActivityVinImportAdd(TtAsRecallVehiclePO VehiclePO){
		dao.insert(VehiclePO);              
	}
	
	/**
	 * Function         : 服务活动管理---VIN导入清单查询
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  serviceActivityVinImportQuery(TtAsRecallVehiclePO VehiclePO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select ID,ACTIVITY_ID,VIN,DEALER_CODE,DEALER_NAME,SALE_STATUS,REPAIR_STATUS,CAR_STATUS,BUY_DATE,CUSTOMER_NAME,LINCENSE_TAG ,LINKMAN,LINKMAN_ZONE_NUM,LINKMAN_OFFICE_PHONE,LINKMAN_FAMILY_PHONE,LINKMAN_MOBILE,CUSTOMER_ADDRESS,EMAIL,POSTALCODE,PROVINCE,AREA,TOWN,ERROR_REMARK from TT_AS_RECALL_VEHICLE  \n");
		if(!"".equals(VehiclePO.getActivityId())&&!(null==VehiclePO.getActivityId())){//工单号不为空
			sql.append("	where  ACTIVITY_ID like '%"+VehiclePO.getActivityId()+"%' \n");
		}
		sql.append("order by ACTIVITY_ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function         : 服务活动管理---验证VIN是否真实
	 * @param           : VIN码
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public List VinVerificationTruth(String vin){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	select * from TM_VEHICLE \n");
		if(!"".equals(vin)&&null!=vin){//工单号不为空
			sql.append("	where  VIN = ? \n");
			params.add(vin);
		}
		sql.append("order by vehicle_id desc  \n");
        List list = dao.select(TtAsRecallVehiclePO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function         : 服务活动管理---验证VIN是否真实(VIN不存在，更新flag 为1；0 表示：真是数据；1 表示：不存在数据)
	 * @param           : VIN码
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  void  VinVerificationTruthOption(TtAsRecallVehiclePO RecallVehiclePO,TtAsRecallVehiclePO VehiclePOContent){
		dao.update(RecallVehiclePO, VehiclePOContent);
	}
	/** 
	 * Function       :  服务活动管理---根据dealercode查询dealerId
	 * @param         :  
	 * @return        :  serviceActivityVinDealerCode
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public  List serviceActivityVinDealerCode(TtAsRecallVehiclePO VehiclePO){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	select dealer_id from tm_dealer \n");
		if(!"".equals(VehiclePO.getDealerCode())&&!(null==VehiclePO.getDealerCode())){//工单号不为空
			sql.append("	where  dealer_code = ? \n");
			params.add(VehiclePO.getDealerCode());
		}
		sql.append("order by dealer_id desc  \n");
        List list = dao.select(TmDealerPO.class, sql.toString(), params);
        return list;
	}
	/** 
	 * Function       :  服务活动管理---VIN清单导入到业务表[TT_AS_ACTIVITY_VEHICLE]中（如果数据表中存在则更新，不存在则新增）
	 * @param         :  
	 * @return        :  serviceActivityVinImportVehicle
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public  void  serviceActivityVinImportVehicle(String activityId,Long createBy,Long updateBy){
		StringBuffer sql = new StringBuffer();
		sql.append(" merge into TT_AS_ACTIVITY_VEHICLE av"+
	              " using (select ACTIVITY_ID, VIN,DEALER_ID, DEALER_CODE, DEALER_NAME, CUSTOMER_NAME,  LINKMAN_MOBILE, CUSTOMER_ADDRESS, POSTALCODE,  LINCENSE_TAG, LINKMAN, LINKMAN_ZONE_NUM,  LINKMAN_OFFICE_PHONE, LINKMAN_FAMILY_PHONE,  EMAIL, PROVINCE, AREA,  TOWN from TT_AS_RECALL_VEHICLE  where  FLAG =0  and activity_id='"+activityId+"') rv"+
	              " on (av.vin=rv.vin and av.activity_id=rv.activity_id)"+
	              " when matched then"+
	              " update set"+
	             // " av.dealer_code=rv.dealer_code,"+
	             // " av.dealer_name=rv.dealer_name,"+
	              " av.customer_name=rv.customer_name,"+
	              " av.linkman_mobile=rv.linkman_mobile,"+
	              " av.customer_address=rv.customer_address,"+
	              " av.postal_code=rv.postalcode,"+
	              " av.UPDATE_BY='"+createBy+"',"+
	              " av.UPDATE_DATE=sysdate,"+
	              " av.LINCENSE_TAG=rv.LINCENSE_TAG,"+
	              " av.LINKMAN=rv.LINKMAN,"+
	              " av.LINKMAN_ZONE_NUM=rv.LINKMAN_ZONE_NUM,"+
	              " av.LINKMAN_OFFICE_PHONE=rv.LINKMAN_OFFICE_PHONE,"+
	              " av.LINKMAN_FAMILY_PHONE=rv.LINKMAN_FAMILY_PHONE,"+
	              " av.EMAIL=rv.EMAIL,"+
	              " av.PROVINCE=rv.PROVINCE,"+
	              " av.AREA=rv.AREA,"+
	              " av.TOWN=rv.TOWN"+
	              " when not matched then"+
	              " insert "+
	              " (id,activity_id,vin,dealer_id,customer_name,linkman_mobile,customer_address,postal_code,create_by,create_date,SALE_STATUS,REPAIR_STATUS,CAR_STATUS,LINCENSE_TAG,LINKMAN,LINKMAN_ZONE_NUM,LINKMAN_OFFICE_PHONE,LINKMAN_FAMILY_PHONE,EMAIL,PROVINCE,AREA,TOWN,IS_IMPORT,IS_SEND_RESULT,UPDATE_BY,UPDATE_DATE)"+
	              " values(f_getid,'"+activityId+"',rv.vin,rv.dealer_id,rv.customer_name,rv.linkman_mobile,rv.customer_address,rv.postalcode,'"+createBy+"',sysdate,"+Constant.SERVICEACTIVITY_SALE_STATUS_01+","+Constant.SERVICEACTIVITY_REPAIR_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_01+",rv.LINCENSE_TAG,rv.LINKMAN,rv.LINKMAN_ZONE_NUM,rv.LINKMAN_OFFICE_PHONE,rv.LINKMAN_FAMILY_PHONE,rv.EMAIL,rv.PROVINCE,rv.AREA,rv.TOWN,"+new Integer(1)+","+new Integer(0)+","+createBy+",sysdate)");
		  dao.update(sql.toString(), null);
	}
	/** 
	 * Function       :  服务活动管理---根据activity_id查询车辆数
	 * @param         :  活动ID
	 * @return        :  serviceActivityVinDealerCode
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public  List QueryCarNum(String activityId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT * from TT_AS_ACTIVITY_VEHICLE \n");
		if(!"".equals(activityId)&&null!=activityId){//活动ID不为空
			sql.append("	WHERE  ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("ORDER BY ACTIVITY_ID DESC   \n");
        List list = dao.select(TtAsActivityVehiclePO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function         : 服务活动管理---修改车辆数
	 * @param           : 
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  void  updateCarNumAndStatusOption(TtAsActivityPO activityPO,TtAsActivityPO activityPOContent){
		dao.update(activityPO, activityPOContent);
	}
	/*
	 * 查询所有模板文件的服务器配置
	 */
	public List<Map<String, Object>> selectTemplateParaConfig(String typeCode,Long companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.PARA_ID, p.TYPE_CODE, p.TYPE_NAME, p.PARA_NAME,p.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA p\n");  
		//sql.append(" where p.TYPE_CODE = ?");
		sql.append(" where p.para_id = ?");
		params.add(typeCode);

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * Function         : 服务活动管理---检验VIN是否重复
	 * @param           : VIN码
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public List checkVinRepeat(String activityId,String vin){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	select * from TT_AS_ACTIVITY_VEHICLE  where 1=1 and  CAR_STATUS ="+Constant.SERVICEACTIVITY_CAR_STATUS_01+"\n");
		if(!"".equals(activityId)&&null!=activityId){//服务活动ID不为空
			sql.append("	and   ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		if(!"".equals(vin)&&null!=vin){//工单号不为空
			sql.append("	and  VIN = ? \n");
			params.add(vin);
		}
		sql.append("order by ID desc  \n");
        List list = dao.select(TtAsActivityVehiclePO.class, sql.toString(), params);
        return list;
	}
}