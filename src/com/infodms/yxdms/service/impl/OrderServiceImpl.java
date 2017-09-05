package com.infodms.yxdms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.yxdms.dao.OrderDAO;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OrderServiceImpl extends OrderDAO implements OrderService{

	/**
	 * 分情况处理
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> checkActivityByVin(RequestWrapper request) {
		Map<String, String> map=null;
		StringBuffer resAll=new StringBuffer();
		//String ro_type = DaoFactory.getParam(request, "ro_type");
		String vin = DaoFactory.getParam(request, "vin");
		String type = DaoFactory.getParam(request, "type");
		String[] partCodes = DaoFactory.getParams(request, "PART_CODE");
		String[] payTypeParts = DaoFactory.getParams(request, "PAY_TYPE_PART");
		map=new HashMap<String, String>();
		String res="";
		String checkParts="";
		//if(!"11441005".equals(ro_type) && !"11441008".equals(ro_type)&&!"11441004".equals(ro_type)){//不是做服务活动(除PDI和保养)
		if("update".equals(type)){
			checkParts = checkParts(resAll, vin, partCodes, payTypeParts);
			map.put("checkParts", checkParts);
			map.put("res", res);
		}else{
			res=super.checkActivityByVin(vin);//检查是否有没有做完的切换件服务活动
			map.put("res", res);
			if(!"".equals(res)){
				checkParts = checkParts(resAll, vin, partCodes, payTypeParts);
				map.put("checkParts", checkParts);
			}else{
				map.put("checkParts", checkParts);
			}
		}
		//}else{
			//map.put("checkParts", "");
			//map.put("res", "");
			
		//}
		//加是否提示的标示
		StringBuffer sb= new StringBuffer();
		sb.append("\n" );
		sb.append("select t.is_tips,t.is_return,v.vin\n" );
		sb.append("  from tt_as_wr_activity_templet t ,\n" );
		sb.append("       tt_as_activity            a,\n" );
		sb.append("       TT_AS_ACTIVITY_SUBJECT    s,\n" );
		sb.append("       TT_AS_ACTIVITY_VEHICLE v\n" );
		sb.append(" where a.subject_id = t.subject_id\n" );
		sb.append("   and t.subject_id = s.subject_id\n" );
		sb.append("   and v.activity_id=a.activity_id");
		DaoFactory.getsql(sb, "v.vin", vin, 1);
		List<Map<String,Object>> isTips = this.pageQuery(sb.toString(), null, getFunName());
		if(DaoFactory.checkListNull(isTips)){
			Map<String, Object> isTipsMap = isTips.get(0);
			String is_tips = BaseUtils.checkNull(isTipsMap.get("IS_TIPS"));
			map.put("is_tips",is_tips);
		}else{
			map.put("is_tips","");
		}
		return map;
	}

	private String checkParts(StringBuffer resAll, String vin,String[] partCodes, String[] payTypeParts) {
		int temp=0;
		String resTemp="";
		if(payTypeParts!=null && payTypeParts.length>0){
			for (String payTypePart : payTypeParts) {
				if("11801002".equals(payTypePart)){//不是自费
					 String restemp = super.checkActivity(vin,partCodes[temp]);//检查是否和切换件里面的件相同
					 if(!"".equals(restemp)){
						 resTemp+=restemp;
					 }
				}
				temp++;
			}
		}
		if(!"".equals(resTemp)){
			resAll.append(resTemp);
		}
		return resTemp;
	}

	public PageResult<Map<String, Object>> orderList(AclUserBean loginUser,RequestWrapper request,Integer pageSize, Integer currPage) {
		return super.orderList(loginUser,request,pageSize,currPage);
	}

	public List<Map<String, Object>> chooseRoType(RequestWrapper request) {
		return super.chooseRoType(request);
	}

	public PageResult<Map<String, Object>> addLabour(RequestWrapper request,
			Integer pageSize, Integer currPage) {
		return super.addLabour(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> addPart(RequestWrapper request,
			Integer pageSize, Integer currPage) {
		return super.addPart(request,pageSize,currPage);
	}

	public Map<String, Object> showInfoByVin(RequestWrapper request) {
		return super.showInfoByVin(request);
	}

	public Map<String, Object> findLoginUserInfo(Long userId) {
		return super.findLoginUserInfo(userId);
	}

	public int roInsert(RequestWrapper request, AclUserBean loginUser) {
		return roInsertSure(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findRepairById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select td.address,td.phone,td.dealer_shortname, t.*\n" );
		sb.append("  from Tt_As_Repair_Order t, tm_vehicle tm, tm_dealer td\n" );
		sb.append(" where t.dealer_id = td.dealer_id\n" );
		sb.append("   and tm.vin = t.vin");
		sb.append(" and  t.id="+DaoFactory.getParam(request, "id"));
		return super.pageQueryMap(sb.toString() , null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLaboursById(RequestWrapper request,String type) {
		StringBuffer sb= new StringBuffer();
		sb.append("select * from Tt_As_Ro_Labour l where 1=1 ");
		DaoFactory.getsql(sb, "l.Repair_Type_Code", type, 1);
		DaoFactory.getsql(sb, "l.ro_id", DaoFactory.getParam(request, "id"), 1);
		return super.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPartsById(RequestWrapper request,String type) {
		StringBuffer sb= new StringBuffer();
		sb.append("select * from tt_as_ro_repair_part  p where 1=1 ");
		DaoFactory.getsql(sb, "p.repairtypecode", type, 1);
		DaoFactory.getsql(sb, "p.ro_id", DaoFactory.getParam(request, "id"), 1);
		return super.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public String orderDelete(RequestWrapper request) {
		StringBuffer sb=new StringBuffer();
		String id = DaoFactory.getParam(request, "id");
		String vin = DaoFactory.getParam(request, "vin");
		TtAsRepairOrderPO temp = new TtAsRepairOrderPO();
		temp.setVin(vin);
		List<TtAsRepairOrderPO> listTemp = this.select(temp);
		List<TtAsRepairOrderPO> select =null;
		// 首先根据ID查询这条记录
		TtAsRepairOrderPO poById1 = new TtAsRepairOrderPO();
		poById1.setId(Long.parseLong(id));
		List<TtAsRepairOrderPO> list1 = this.select(poById1);
		Double inMileage = list1.get(0).getInMileage();
		String str = "";
		String sql = "select t.* from tt_as_repair_order t where t.create_date =(select max(r.create_date) from tt_as_repair_order r where  r.id not in ("
				+ id + ") and r.vin='" + vin + "')";
		 select = this.select(TtAsRepairOrderPO.class, sql, null);
		if(select!=null && select.size()==0){
			String sql1 = "select t.* from tt_as_repair_order t where t.create_date =(select max(r.create_date) from tt_as_repair_order r where  r.id in ("
					+ id + ") and r.vin='" + vin + "')";
			select = this.select(TtAsRepairOrderPO.class, sql1, null);
		}
		Double inMileage2 = select.get(0).getInMileage();
		for (TtAsRepairOrderPO ttAsRepairOrderPO : listTemp) {// 循环工单的
			if (ttAsRepairOrderPO.getInMileage() > inMileage) {
				str += ttAsRepairOrderPO.getRoNo() + ";";
			}
		}
		if (!str.equals("")) {
			sb.append("单号为：" + str
					+ "大于了另外工单的里程，请先废弃大里程的工单");
		} else {
			// 回滚最近的那个时间的里程
			TmVehiclePO pp = new TmVehiclePO();
			pp.setMileage(inMileage2);

			TmVehiclePO pp1 = new TmVehiclePO();
			pp1.setVin(vin);
			this.update(pp1, pp);
		}
		super.delete("delete from Tt_As_Repair_Order t where t.id="+DaoFactory.getParam(request, "id"), null);
		super.delete("delete from Tt_As_Ro_Labour l where l.ro_id="+DaoFactory.getParam(request, "id"), null);
		super.delete("delete from tt_as_ro_repair_part p where p.ro_id="+DaoFactory.getParam(request, "id"), null);
		super.delete("select t.* from tt_as_ro_out_data t where 1 = 1 and t.ro_no = (select o.ro_no from Tt_As_Repair_Order o where o.id ='"+DaoFactory.getParam(request, "id")+"')", null);
		return sb.toString();
	}

	/**
	 * 系统中同一个VIN号只能存在一张未结算状态的工单
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIsRoEndByVin(RequestWrapper request) {
		boolean flag=true;
		String vin = DaoFactory.getParam(request, "vin");
		TtAsRepairOrderPO t =new TtAsRepairOrderPO();
		t.setVin(vin);
		t.setRoStatus(11591001);
		List<TtAsRepairOrderPO> select = super.select(t);
		if(select!=null && select.size()>0){
			flag=false;
		}
		return flag;
	}

	public List<Map<String, Object>> findAccById(RequestWrapper request) {
		return super.findAccById(request);
	}

	public PageResult<Map<String, Object>> accList(RequestWrapper request,
			Integer pageSize, Integer currPage) {
		return super.accList(request,pageSize,currPage);
	}

	public String checkMileage(RequestWrapper request) {
		return super.checkMileage(request);
	}

	public List<Map<String, Object>> showInfoBylicenseNo(RequestWrapper request, AclUserBean loginUser) {
		return super.showInfoBylicenseNo(request,loginUser);
	}

	public List<Map<String, Object>> findVinListByVin(RequestWrapper request) {
	   return super.findVinListByVin(request);
	}

	public Map<String, Object> getGuaFlag(String partCode,String vin,String inMileage,String purchasedDate) {
		return super.getGuaFlag(partCode,vin,inMileage,purchasedDate);
	}

	public PageResult<Map<String, Object>> doActivity(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.doActivity(request,loginUser,pageSize,currPage);
	}

	public int deleteTrPart(RequestWrapper request) {
		return super.deleteTrPart(request);
	}

	public List<Map<String, Object>> findAccData(RequestWrapper request) {
		return super.findAccData(request);
	}

	public List<Map<String, Object>> findOutById(RequestWrapper request) {
		return super.findOutById(request);
	}
	//桥接模式 +适配器模式
	public String findFreeRoByPartId(RequestWrapper request) {
		String ro_no = DaoFactory.getParam(request, "ro_no");
		String partId = DaoFactory.getParam(request, "partId");
		return super.findFreeRoByPartId(ro_no,partId);
	}

	public Map<String, Object> showInfoByroNo(RequestWrapper request) {
		return super.showInfoByroNo(request);
	}

	public List<Map<String, Object>> checkaddPart(RequestWrapper request,AclUserBean loginUser) {
		return super.checkaddPart(request,loginUser);
	}

	public Map<String, Object> showInfoWinterByVin(RequestWrapper request,String str ,AclUserBean loginUser) {
		return super.showInfoWinterByVin( request,str, loginUser);
	}
	

}
