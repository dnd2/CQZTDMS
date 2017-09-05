package com.infodms.dms.dao.sales.storage.sendManage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsDlvryErpDtlPO;
import com.infodms.dms.po.TtVsDlvryErpPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;

public class TtVsDlvryErpDao extends BaseDao<PO>{
	private static final TtVsDlvryErpDao dao = new TtVsDlvryErpDao ();
	public static final TtVsDlvryErpDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//发运完成之后回写到tt_vs_dlvry_erp
	public void insertTtVsDlvryErp(Map<String,Object> map) throws ParseException{
		TtVsDlvryErpPO tvdep=new TtVsDlvryErpPO();
		tvdep.setSendcarHeaderId(BigDecimal.valueOf((Long) map.get("SENDCAR_HEADER_ID"))); //tt_vs_dlvry_erp 主键ID
		tvdep.setSendcarOrderNumber((String) map.get("BILL_NO"));    //交接单号
		tvdep.setOrderNumber((String) map.get("ORDER_NO"));         //订单号
		tvdep.setShipMethodCode((String) map.get("LOGI_NAME"));     //承运商
		tvdep.setMotorman((String) map.get("DRIVER_NAME"));        //司机
		tvdep.setMotormanPhone((String) map.get("DRIVER_TEL"));	     //司机电话
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date planLoadDate=sdf.parse(map.get("PLAN_LOAD_DATE").toString());
		tvdep.setFlatcarAssignDate(planLoadDate);  //装车时间
		tvdep.setFlatcarId(String.valueOf(map.get("BO_ID")));                //组板ID
		tvdep.setDeliveryId(String.valueOf(map.get("REQ_ID"))); 			//发运ID
		tvdep.setSendcarAmount(Integer.parseInt(map.get("VEH_NUM").toString()) );	    //装车数量
		Date dlvJjDate=sdf.parse(map.get("DLV_JJ_DATE").toString());
		tvdep.setArriveDate(dlvJjDate);         //到达时间
		tvdep.setCreateBy((Long) map.get("CREATE_BY"));            //创建人
		tvdep.setCreateDate(new Date());
		dao.insert(tvdep);
	}
	
	public void updateTtVsDlvryErp(Map<String,Object> map){
		TtVsDlvryErpPO oldTvdep=new TtVsDlvryErpPO();
		TtVsDlvryErpPO tvdep=new TtVsDlvryErpPO();
		oldTvdep.setSendcarHeaderId(( BigDecimal) (map.get("BILL_ID")));
		tvdep.setSendcarOrderNumber((String) map.get("BILL_NO"));
		tvdep.setOrderNumber((String) map.get("ORDER_NO"));
		tvdep.setShipMethodCode((String) map.get("LOGI_NAME"));
		tvdep.setMotorman((String) map.get("DRIVER_NAME"));
		tvdep.setMotormanPhone((String) map.get("DRIVER_TEL"));
		tvdep.setFlatcarAssignDate((Date) map.get("PLAN_LOAD_DATE"));
		tvdep.setFlatcarId((String) map.get("BO_ID"));
		tvdep.setDeliveryId((String)map.get("REQ_ID"));
		tvdep.setSendcarAmount((Integer) map.get("VEH_NUM"));
		tvdep.setArriveDate((Date) map.get("DLV_JJ_DATE"));
		tvdep.setUpdateBy((Long) map.get("CREATE_BY"));
		tvdep.setUpdateDate(new Date());
		dao.update(oldTvdep,tvdep);
	}
	
	//插入子表TT_VS_DLVRY_DTL
	public void insertTtVsDlvryErpDtl(Map<String,Object> map){
		TtVsDlvryErpDtlPO tvdedp=new TtVsDlvryErpDtlPO();
		tvdedp.setDetailId((Long) map.get("DETAIL_ID"));
		tvdedp.setSendcarHeaderId((Long) map.get("SENDCAR_HEADER_ID")); // TT_VS_DLVRY主表ID
		tvdedp.setVehicleId((Long) map.get("VEHICLE_ID"));        //车辆_id
		tvdedp.setReqId(Long.parseLong(map.get("REQ_ID").toString())); 				//发运ID
		tvdedp.setIsReceive((Long)map.get("IS_RECEIVE"));       //是否收车 默认为否
		tvdedp.setCreateBy((Long) map.get("CREATE_BY"));       //创建人
		tvdedp.setCreateDate(new Date());
		dao.insert(tvdedp);
	}
	
		
		public void updateTtVsDlvryErpDtl(Map<String,Object> map){
			TtVsDlvryErpDtlPO oldtvdedp=new TtVsDlvryErpDtlPO();
			TtVsDlvryErpDtlPO tvdedp=new TtVsDlvryErpDtlPO();
			oldtvdedp.setDetailId((Long)map.get("DETAIL_ID"));
			tvdedp.setSendcarHeaderId((Long) map.get("BILL_ID"));
			tvdedp.setVehicleId((Long) map.get("VEHICLE_ID"));
			tvdedp.setReqId((Long) map.get("REQ_ID")); 
			tvdedp.setIsReceive((Long)map.get("IS_RECEIVE"));
			tvdedp.setCreateBy((Long) map.get("CREATE_BY"));
			tvdedp.setCreateDate(new Date());
			dao.update(oldtvdedp,tvdedp);
		}
	
		//修改资源锁定表发运数量
		public void updateTtVsOrderResouce(Long req_detail_id,Integer deliveryAmount,Long updateBy){
			Map<String,Object> map=getDlvryAmount(String.valueOf(req_detail_id));
			//更新资源锁定表
			TtVsOrderResourceReservePO oldtorrp=new TtVsOrderResourceReservePO();
			TtVsOrderResourceReservePO torrp=new TtVsOrderResourceReservePO();
			String reserveId=queryTtVsOrderResouce(String.valueOf(req_detail_id));
			oldtorrp.setReserveId(Long.valueOf(reserveId));
			torrp.setDeliveryAmount(deliveryAmount+Integer.parseInt(map.get("DELIVERY_AMOUNT").toString()));
			if(deliveryAmount+Integer.parseInt(map.get("DELIVERY_AMOUNT").toString())==Integer.parseInt(map.get("AMOUNT").toString())){
				torrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_03);
			}
			torrp.setUpdateBy(updateBy);
			torrp.setUpdateDate(new Date());
			dao.update(oldtorrp, torrp);
			
			//更新订单资源子表
			TtVsDlvryReqDtlPO oldtvdrdp=new TtVsDlvryReqDtlPO();
			TtVsDlvryReqDtlPO tvdrdp=new TtVsDlvryReqDtlPO();
			oldtvdrdp.setDetailId(req_detail_id);
			tvdrdp.setDeliveryAmount(deliveryAmount+Integer.parseInt(map.get("DELIVERY_AMOUNT").toString()));
			tvdrdp.setUpdateBy(updateBy);
			tvdrdp.setUpdateDate(new Date());
			dao.update(oldtvdrdp, tvdrdp);
			
			//更新
			
		}
		
		//在途换车
		public void changeVehicle(Long reqId,Long oldVehicleId,Long newVehicleId,Long updateBy){
			TtVsDlvryErpDtlPO oldtvdedp=new TtVsDlvryErpDtlPO();
			TtVsDlvryErpDtlPO tvdedp=new TtVsDlvryErpDtlPO();
			//oldtvdedp.setDetailId(detail_id); //原单据detail_id
			oldtvdedp.setReqId(reqId);
			oldtvdedp.setVehicleId(oldVehicleId); //需要换车的原vehicleId
			tvdedp.setVehicleId(newVehicleId); // 需要换车的新的vehicleId
			tvdedp.setUpdateBy(updateBy);
			tvdedp.setUpdateDate(new Date());
			dao.update(oldtvdedp,tvdedp);
		}
		
		public String queryTtVsOrderResouce(String detail_id)
		{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select reserve_id from TT_VS_ORDER_RESOURCE_RESERVE \n");
			sbSql.append(" WHERE req_detail_id=").append(detail_id);
			
			return dao.pageQueryMap(sbSql.toString(), null, dao.getFunName()).get("RESERVE_ID").toString();
		}
		
		public Map<String,Object> getDlvryAmount(String detail_id){
			//String detail_id=(String) map.get("DETAIL_ID");
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select amount,nvl(delivery_amount,0) delivery_amount from TT_VS_ORDER_RESOURCE_RESERVE \n");
			sbSql.append(" WHERE req_detail_id=").append(detail_id);
			return dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		}

}
