package com.infodms.dms.dao.afterSales;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PinApplyDAO extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(PinApplyDAO.class);
	private PinApplyDAO() {
		
	}
	
	private static class PinApplyDAOSingleton {
		private static PinApplyDAO dao = new PinApplyDAO() ;
	}
	
	public static PinApplyDAO getInstance() {
		return PinApplyDAOSingleton.dao ;
	}
	
	/*public int orderModify(String orderId, Map<String, String> map) throws ParseException {
		String orderStatus = map.get("orderStatus") ;
		String updateBy = map.get("updateBy") ;
		String updateDate = map.get("updateDate") ;
		

		TtVsOrderPO oldOrder = new TtVsOrderPO() ;
		oldOrder.setOrderId(Long.parseLong(orderId)) ;
		TtVsOrderPO newOrder = new TtVsOrderPO() ;

		if(!CommonUtils.isNullString(orderStatus)) {
			newOrder.setOrderStatus(Integer.parseInt(orderStatus)) ;
		}

		if(!CommonUtils.isNullString(updateBy)) {
			newOrder.setUpdateBy(Long.parseLong(updateBy)) ;
		}

		if(!CommonUtils.isNullString(updateDate)) {
			newOrder.setUpdateDate(new Date((Long.parseLong(updateDate)))) ;
		}

		return super.update(oldOrder, newOrder) ;
	}*/
	public List<Map<String, Object>> GenerateNumber(String dealerId,String date) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select t.pin_no from TT_AS_PIN_APPLY t where t.dealer_id="+dealerId+" and TO_CHAR(t.create_date,'YYYYMMDD')=TO_CHAR(sysdate,'YYYYMMDD') order by t.create_date desc " );

		return super.pageQuery(sql.toString(),null, getFunName());
	}
	/**
	 * pin码查看申请查询
	 * @param paraMap
	 * @return
	 */
	public PageResult<Map<String,Object>> pinList(Map<String,Object> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.pin_no,\n" );
		sql.append("       t.vin,\n" );
		sql.append("       t.dealer_id,\n" );
		sql.append("       t.remark,\n" );
		sql.append("       t.auditor,\n" );
		sql.append("       TO_CHAR(t.auditor_time, 'YYYY-MM-DD') as auditor_time,\n" );
		sql.append("       t.pin_code,\n" );
		sql.append("       t.reply,\n" );
		sql.append("       t.status,\n" );
		sql.append("       t.create_by,\n" );
		sql.append("       TO_CHAR(t.create_date, 'YYYY-MM-DD') as create_date,\n" );
		sql.append("       d.dealer_code,\n" );
		sql.append("       d.dealer_name,\n" );
		sql.append("       u.name,\n" );
		sql.append("       u1.name AS ANAME\n" );
		sql.append("  from TT_AS_PIN_APPLY t\n" );
		sql.append("  left join tm_dealer d\n" );
		sql.append("    on t.dealer_id = d.dealer_id\n" );
		sql.append("  left join tc_user u on t.create_by=u.user_id left join tc_user u1 on t.auditor=u1.user_id where 1=1 \n" );

		if(paraMap.get("ftype")!=null){
			sql.append(" and t.STATUS !="+Constant.PIN_APPLY_STATUS_01+" ");
		}
		if(paraMap.get("dealerdId")!=null){
			sql.append(" and t.dealer_id in ("+paraMap.get("dealerdId")+") ");
		}
		if(paraMap.get("pinNo")!=null){
			sql.append(" and t.pin_No like ? ");
			listPar.add("%"+paraMap.get("pinNo")+"%");
		}
		if(paraMap.get("creatDate")!=null){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("creatDate"));
		}
		if(paraMap.get("outPlantDate")!=null){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("outPlantDate"));
		}
		if(paraMap.get("vin")!=null){
			sql.append(" and t.VIN like ? ");
			listPar.add("%"+paraMap.get("vin")+"%");
		}
		if(paraMap.get("dealer_name")!=null){
			sql.append(" and d.dealer_name like ? ");
			listPar.add("%"+paraMap.get("dealer_name")+"%");
		}
		if(paraMap.get("status")!=null){
			sql.append(" and t.STATUS = ? ");
			listPar.add(paraMap.get("status"));
		}
		if(paraMap.get("id")!=null){
			sql.append(" and t.id = ? ");
			listPar.add(paraMap.get("id"));
		}
		sql.append("  ORDER BY t.STATUS ASC,t.CREATE_DATE DESC");
					
		return super.pageQuery(sql.toString(), listPar,getFunName(), pageSize, curPage);
		
		}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
