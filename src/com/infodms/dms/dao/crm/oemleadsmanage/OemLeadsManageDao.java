package com.infodms.dms.dao.crm.oemleadsmanage;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import org.apache.log4j.Logger;

import com.infodms.dms.actions.crmphone.push.SendPushService;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TPcLeadsTempPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OemLeadsManageDao extends BaseDao {
	public static Logger logger = Logger.getLogger(OemLeadsManageDao.class);
	private static final OemLeadsManageDao dao = new OemLeadsManageDao();

	public static final OemLeadsManageDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getGroupByOrg(String orgId, String companyId, String areaId) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT distinct B.GROUP_CODE, B.GROUP_NAME FROM  ");
		sql.append("       (SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("         WHERE TVMG.GROUP_LEVEL = 2\n");
		if (null != companyId && !"".equals(companyId)) {
			sql.append("           AND TVMG.COMPANY_ID = " + companyId + "\n");
		}
		sql.append("           AND TVMG.STATUS =  " + Constant.STATUS_ENABLE + "\n");
		sql.append("         START WITH TVMG.GROUP_ID IN\n");
		sql.append("                    (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM TM_AREA_GROUP TAG\n");
		if (null != areaId && !"".equals(areaId)) {
			sql.append("                      WHERE TAG.AREA_ID = " + areaId + ")\n");
		}
		sql.append("        CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---根据业范围往上找，找到车系级别\n");
		sql.append("        ) B\n");
		sql.append("  ORDER BY B.GROUP_CODE, B.GROUP_NAME");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}


	/*
	 * 客户所在省是否存在
	 */
	public List<Map<String, Object>> oemLeadsCheckProvince(Map<String, Object> map) {
		
		String province = map.get("province").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select *\r\n");
		sql.append("  from tm_region a\r\n");
		sql.append(" where a.region_code = '"+province+"'"); 


		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 客户所在市是否存在
	 */
	public List<Map<String, Object>> oemLeadsCheckCity(Map<String, Object> map) {
		
		String city = map.get("city").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select *\r\n");
		sql.append("  from tm_region a\r\n");
		sql.append(" where a.region_code = '"+city+"'"); 


		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 客户所在区是否存在
	 */
	public List<Map<String, Object>> oemLeadsCheckArea(Map<String, Object> map) {
		
		String area = map.get("area").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select *\r\n");
		sql.append("  from tm_region a\r\n");
		sql.append(" where a.region_code = '"+area+"'"); 


		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 经销商编码是否存在
	 */
	public List<Map<String, Object>> oemLeadsCheckDealer(Map<String, Object> map) {
		
		String dealerId = map.get("dealerId").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT * FROM TM_DEALER A WHERE A.DEALER_CODE='"+dealerId+"'"); 

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	
	/*
	 * 获取意向车系名称和代码
	 */
	public List<Map<String, Object>> getIntentCar() {
				
		StringBuffer sql = new StringBuffer("");

		sql.append("select tpiv.series_name,tpiv.series_id from t_pc_intent_vehicle tpiv where tpiv.up_series_id is null "); 

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/*
	 * 
	 */
	public List<Map<String, Object>> getDaoUser(String userId) {
		
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT *  FROM T_Pc_Daouser WHERE  User_Id= '"+userId+"' "); 

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/*
	 * 插入正在导入数据用户
	 */
	public void insertDaoUser(Long userId){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into  t_pc_daouser values('"+userId+"',sysdate) ");
		update(sql.toString(), null);
	}
	
	/*
	 * 插入主表数据
	 */
	public int insertLeads(Long userId) throws ParseException {
		
		
		 List<Map<String, Object>> usercount=dao.getDaoUser(userId.toString());
		
		if(usercount.size()>0 ) {
			return 0;
		}
		
		List<String> users = new LinkedList<String>();
		users.add(userId.toString());
		
		dao.callProcedure("insert_dao_user", users, null);
		TPcLeadsTempPO po2 = new TPcLeadsTempPO();
		po2.setCreateBy(userId.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//系统时间
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=dateformat.format(new Date());
		
		List<TPcLeadsTempPO> list = dao.select(po2);
		for(int i=0;i<list.size();i++) {
			po2 = list.get(i);
			TPcLeadsPO po = new TPcLeadsPO();
			//销售线索主键ID
			Long leadsCode = dao.getLongPK(po);
			
			//线索来源转换代码
			String origin = po2.getLeadsOrigin();
			if(origin=="客户中心"||"客户中心".equals(origin)) {
				origin = "60151003";
			} else if(origin=="官网"||"官网".equals(origin)) {
				origin = "60151004";
			} else if(origin=="网络媒体"||"网络媒体".equals(origin)) {
				origin = "60151006";
			} else if(origin=="车展"||"车展".equals(origin)) {
				origin = "60151007";
			} else if(origin=="巡展"||"巡展".equals(origin)) {
				origin = "60151007";
			} else if(origin=="试乘试驾"||"试乘试驾".equals(origin)) {
				origin = "60151008";
			} else if(origin=="上市活动"||"上市活动".equals(origin)) {
				origin = "60151009";
			} else if(origin=="媒体"||"媒体".equals(origin)) {
				origin = "60151010";
			} else if(origin=="汽车之家"||"汽车之家".equals(origin)) {
				origin = "60151017";
			} else if(origin=="易车网"||"易车网".equals(origin)) {
				origin = "60151018";
			} else if(origin=="社会化媒体"||"社会化媒体".equals(origin)) {
				origin = "60151021";
			}else if(origin=="天猫电商"||"天猫电商".equals(origin)) {
				origin = "60151022";
			}else {
				origin = "0";
			}
			String intent_car_name=po2.getIntentCar();
			String intent_car_code=null;
			String intentVehicle=null;
			
			TPcIntentVehiclePO tpivPo = new TPcIntentVehiclePO();
			TPcIntentVehiclePO tpivPoValue = new TPcIntentVehiclePO();
			tpivPo.setSeriesName(intent_car_name);
			tpivPoValue= (TPcIntentVehiclePO)dao.select(tpivPo).get(0);
			intent_car_code=tpivPoValue.getSeriesId().toString();
			
			if(intent_car_code=="1000" || "1000".equals(intent_car_code)){
				intentVehicle="10001003";
			}else if(intent_car_code=="1001" || "1001".equals(intent_car_code)){
				intentVehicle="10011003";
			}else if(intent_car_code=="1002" || "1002".equals(intent_car_code)){
				intentVehicle="10021002";
			}else if(intent_car_code=="1003" || "1003".equals(intent_car_code)){
				intentVehicle="10031003";
			}else if(intent_car_code=="1004" || "1004".equals(intent_car_code)){
				intentVehicle="10041002";
			}else if(intent_car_code=="1005" || "1005".equals(intent_car_code)){
				intentVehicle="10051001";
			}else if(intent_car_code=="1006" || "1006".equals(intent_car_code)){
				intentVehicle="10061000";
			}else if(intent_car_code=="1007" || "1007".equals(intent_car_code)){
				intentVehicle="10071000";
			}else if(intent_car_code=="1008" || "1008".equals(intent_car_code)){
				intentVehicle="10081002";
			}else if(intent_car_code=="1009" || "1009".equals(intent_car_code)){
				intentVehicle="10091001";
			}else if(intent_car_code=="1010" || "1010".equals(intent_car_code)){
				intentVehicle="10101001";
			}else if(intent_car_code=="1011" || "1011".equals(intent_car_code)){
				intentVehicle="10111002";
			}else{
				intentVehicle="0";
			}
	
			Long originL = Long.parseLong(origin);
			// 插入数据SQL（销售线索表）
			StringBuffer sql = new StringBuffer();
			sql.append("insert into t_pc_leads a\r\n");
			sql.append("(leads_code,leads_type,LEADS_ORIGIN,\r\n");
			sql.append("intent_vehicle,intent_car,customer_name,PROVINCE,city,area,\r\n");
			sql.append("collect_date,telephone,remark,data_source_id,create_date,create_by\r\n");
			sql.append(",leads_status)\r\n");
			
			sql.append("values \r\n");
			sql.append("  ("+leadsCode+",\r\n");//主键
			sql.append("   "+Constant.LEADS_TYPE_01+",\r\n");//线索类别
			sql.append("   "+originL+",\r\n");//线索来源
			sql.append("   "+intent_car_code+",\r\n");//意向车型//源代码插入为：intentVehicle 中华修改为插入：intent_car_code
			sql.append("   "+intent_car_code+",\r\n");//意向车系
			sql.append("   '"+po2.getCustomerName()+"',\r\n");
//			sql.append("   null,\r\n");//到店时间
//			sql.append("   null,\r\n");//离店时间
//			sql.append("   null,\r\n");//客户描述
			if(po2.getProvince()==null) {
				sql.append("   null,\r\n");
			} else {
				sql.append("   '"+po2.getProvince()+"',\r\n");
			}
			if(po2.getCity()==null) {
				sql.append("   '',\r\n");
			} else {
				sql.append("   '"+po2.getCity()+"',\r\n");
			}
			if(po2.getArea()==null) {
				sql.append("   null,\r\n");
			} else {
				sql.append("   '"+po2.getArea()+"',\r\n");
			}
			//收集时间
			if(po2.getCollectDate()==null) {
				sql.append("   null,\r\n");//收集时间
			} else {
				sql.append("   to_date('"+po2.getCollectDate()+"','yyyy-MM-dd'),\r\n");//收集时间
			}
			sql.append("   '"+po2.getTelephone()+"',\r\n");
			sql.append("   '"+date+"',\r\n");//备注
			//分派经销商代码
			if(po2.getDealerId()==null) {
				sql.append("   null,\r\n");//经销商代码
			} else {
				sql.append("   '"+po2.getDealerId()+"',\r\n");//经销商代码
			}
			sql.append("   sysdate,\r\n");
			sql.append("   '"+userId+"',\r\n");
			sql.append("   "+Constant.LEADS_STATUS_01+")\r\n");//线索状态
//			sql.append("   null,\r\n");//战败备注
//			sql.append("   null,\r\n"); //失效备注
//			sql.append("   null,\r\n"); //意向车型
//			sql.append("   null,\r\n"); //客户ID
//			sql.append("   null,\r\n"); //战败车型
//			sql.append("   null,\r\n"); //战败原因
//			sql.append("   null,\r\n"); //集客方式
//			sql.append("   null,\r\n"); //趋前迎接
//			sql.append("   null)\r\n"); //客户性别
			update(sql.toString(), null);
			
			// 自动分派开始
			//判断开始：是否自动分配到顾问（根据已建档客户信息进行判断*电话+姓名+经销商ID*）//现已改为电话+经销商验证
			//曾经建过档案
			//是否在建档客户信息中存在数据(必须在同一个经销商才能自动分派到顾问)
			if(po2.getDealerId() == null) { 
				//若经销为空,不做处理,不分派
			} else {
				List<Map<String, Object>> getHasList = null;
				getHasList=getHasCustomerImp(po2.getTelephone(),po2.getCustomerName(),po2.getDealerId());
				
				if(getHasList.size()>0) {
					//已存在，则自动分派顾问，如果客户类型不属于战败、失效、保有将线索设为无效，并增加一个接触点信息
					//获取已建档的分派顾问
					String adviser = getHasList.get(0).get("ADVISER").toString();
					String dealerId = getHasList.get(0).get("DEALER_ID").toString();
					String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
					String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
					//修改销售线索主表状态为重复
					TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
					oldLeadsPo.setLeadsCode(leadsCode);
					TPcLeadsPO newLeadsPo = new TPcLeadsPO();
					//newLeadsPo.setLeadsCode(leadsCode);
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						newLeadsPo.setIfHandle(10041001);
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_05.longValue());
					} else {
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
					}
					dao.update(oldLeadsPo, newLeadsPo);
					
					TPcLeadsAllotPO allotPo = new TPcLeadsAllotPO();
					//线索分派主键ID
					Long allotCode = dao.getLongPK(allotPo);
					allotPo.setLeadsAllotId(allotCode);
					allotPo.setLeadsCode(leadsCode);
					allotPo.setCustomerName(po2.getCustomerName());
					allotPo.setTelephone(po2.getTelephone());
					allotPo.setRemark(null);
					allotPo.setCreateDate(new Date());
					allotPo.setCreateBy(userId.toString());
					//当导入线索查询客户为已经建档，并且客户ctmType状态为有望和重购客户的时候设置线索分派表状态为无效
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						allotPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
						allotPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//设置线索为顾问已经确认
						allotPo.setConfirmDate(new Date());//设置确认线索时间
						allotPo.setCustomerId(Long.parseLong(customerId));
					} else {//客户类型属于战败、失效、保有
						allotPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
					}
					allotPo.setDealerId(dealerId);
					allotPo.setAllotDealerDate(new Date());
					allotPo.setAdviser(adviser);
					allotPo.setAllotAdviserDate(new Date());
					allotPo.setAllotAgain(Constant.IF_TYPE_NO);
					allotPo.setOldLeadsCode(null);
					insert(allotPo);
					
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_01, "公司类重复线索", customerId, adviser, dealerId);
						String repeatLeads = SequenceManager.getSequence("");
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, sdf.format(new Date()),"");
						//标记线索为重复线索
						CommonUtils.updateIfRepeat(allotCode.toString());
						CommonUtils.updateLeadStatus(leadsCode.toString());
					} else {
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, sdf.format(new Date()),"");
					}
				} else {//不存在建档，则自动分派到该经销商
					TPcLeadsAllotPO allotPo = new TPcLeadsAllotPO();
					//取经销商ID
					TmDealerPO dealerPo = new TmDealerPO();
					dealerPo.setDealerCode(po2.getDealerId());
					TmDealerPO dealerPo2 = (TmDealerPO)select(dealerPo).get(0);
					//线索分派主键ID
					Long allotCode = dao.getLongPK(allotPo);
					allotPo.setLeadsAllotId(allotCode);
					allotPo.setLeadsCode(leadsCode);
					allotPo.setCustomerName(po2.getCustomerName());
					allotPo.setTelephone(po2.getTelephone());
					allotPo.setRemark(null);
					allotPo.setCreateDate(new Date());
					allotPo.setCreateBy(userId.toString());
					allotPo.setStatus(Constant.STATUS_ENABLE);
					allotPo.setDealerId(dealerPo2.getDealerId().toString());
					allotPo.setAllotDealerDate(new Date());
					allotPo.setAdviser(null);
					allotPo.setAllotAdviserDate(null);
					allotPo.setAllotAgain(Constant.IF_TYPE_NO);
					allotPo.setOldLeadsCode(null);
					insert(allotPo);
					
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_01.toString(), allotCode.toString(), "", dealerPo2.getDealerId().toString(), "", sdf.format(new Date()),"");	
				}
			}
		}
		
		OemLeadsManageDao dao=OemLeadsManageDao.getInstance();
		//查询要发送经销商提醒的代码和条数
		List<Map<String, Object>> ps=dao.oemSelectLeads(date);
		if(ps.size()>0) 
		{
		for(int i=0;i<ps.size();i++) {
			 String dealId=ps.get(i).get("DEALID").toString();
			 String xcount=ps.get(i).get("XCOUNT").toString();
			 if(!"0".equals(dealId)){
			 List<Map<String, Object>> ps2=dao.oemSelectTcUser(dealId);
			 if(ps2.size()>0)
			 {
				 String user_id=ps2.get(0).get("USER_ID").toString();
				 if(user_id != null && user_id !=""){
					 HashMap hm = new HashMap();
					 hm.put("strpushtype", "100");
					 hm.put("strkey", "100");
					 SendPushService.sendNotification("您有待处理的导入线索"+xcount+"条",user_id,hm);
				 }
			 }
		  }
		}
	  }
		dao.callProcedure("delete_dao_user", users, null);
		return 1;
	}
	
	

	/*
	 * OEM导入时，查询线索表导入时经销商ID和分给经销商的条数
	 */
	public List<Map<String, Object>> oemSelectLeads(String date) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select case when data_source_id is not null then data_source_id when data_source_id is null then '0' end as dealId,count(1) as xcount from t_pc_leads tpl where tpl.leads_type='60141001' and tpl.remark='"+date+"' group by data_source_id ");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * OEM导入时，查询线索表导入时经销商ID和分给经销商的条数
	 */
	public List<Map<String, Object>> oemSelectTcUser(String dealId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select user_id from tc_user tu where tu.acnt like '"+dealId+"%' and pose_rank='60281002' ");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	
	

	/*
	 * OEM导入时，查询临时表数据（长安汽车） 结果集
	 */
	public List<Map<String, Object>> oemSelectTmpLeadsManage(Long userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select CUSTOMER_NAME,TELEPHONE,PROVINCE,CITY,AREA,LEADS_ORIGIN,COLLECT_DATE,DEALER_ID,INTENT_CAR from t_pc_leads_temp where leads_type = 60141001 and create_by="+userId+"");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 是否已存在客户建档数据(导入时)
	 */
	public List<Map<String, Object>> getHasCustomerImp(String telephone,String name,String dealerId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select A.DEALER_ID,A.ADVISER,A.customer_id,A.CTM_TYPE \r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
//		sql.append("   and a.CUSTOMER_NAME = '"+name+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_CODE = ''"); 
		} else {
			sql.append("   and B.DEALER_CODE = '"+dealerId+"'"); 
		}

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 是否已存在客户建档数据
	 */
	public List<Map<String, Object>> getHasCustomer(String telephone,String name,String dealerId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select A.DEALER_ID,A.ADVISER,A.CUSTOMER_ID,A.CTM_TYPE\r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
//		sql.append("   and a.CUSTOMER_NAME = '"+name+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_ID = ''"); 
		} else {
			sql.append("   and B.DEALER_ID = '"+dealerId+"'"); 
		}

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 是否已存在客户建档数据(本顾问的)
	 */
	public List<DynaBean> getHasCustomer2(String telephone,String name,String dealerId,String userId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select A.DEALER_ID,A.ADVISER,A.CUSTOMER_ID,A.CTM_TYPE \r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
//		sql.append("   and a.CUSTOMER_NAME = '"+name+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_ID = ''"); 
		} else {
			sql.append("   and B.DEALER_ID = '"+dealerId+"'"); 
		}
		if(userId == null) {
			sql.append("   and A.ADVISER = ''"); 
		} else {
			sql.append("   and A.ADVISER = '"+userId+"'"); 
		}

		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/**
	 * 销售线索分派查询
	 */
	
	public PageResult<Map<String, Object>> oemLeadsAllotQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String leadsOrigin,String dPro,String dCity,
			String dArea,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.leads_code,\r\n");
		sql.append("       c.code_desc as leads_origin,\r\n");
		sql.append("       to_char(a.collect_date, 'yyyy-MM-dd') collect_date,\r\n");
		sql.append("       b1.region_name as province,\r\n");
		sql.append("       b2.region_name as city,\r\n");
		sql.append("       b3.region_name as area,\r\n");
		sql.append("       a.customer_name,\r\n");
		sql.append("       a.telephone,\r\n");
		sql.append("       null AS DEALER_NAME,\r\n");
//		sql.append("       DECODE(D.DEALER_NAME,NULL,'',D.DEALER_NAME) AS DEALER_NAME,\r\n");
		sql.append("       a.remark,e.series_name\r\n");
		sql.append("  from t_pc_leads a\r\n");
		sql.append("  left join t_pc_leads_allot a2 on a.leads_code = a2.leads_code\r\n");
		sql.append("  left join tm_region b1 on a.province = b1.region_code\r\n");
		sql.append("  left join tm_region b2 on a.city = b2.region_code\r\n");
		sql.append("  left join tm_region b3 on a.area = b3.region_code");
		sql.append("  LEFT JOIN TC_CODE C ON A.LEADS_ORIGIN = C.CODE_ID"); 
		sql.append("  left join tm_dealer d on a2.dealer_id = to_char(d.dealer_id)");
		sql.append("  left join t_pc_intent_vehicle e on a.intent_vehicle = e.series_id");
		sql.append(" where 1 = 1 and a2.dealer_id is null and a.leads_status = "+Constant.LEADS_STATUS_01+" and a.leads_type = "+Constant.LEADS_TYPE_01+" "); 

		if (Utility.testString(customerName)) {//客户姓名
			sql.append(" and a.customer_name = '"+customerName+"' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sql.append(" and a.telephone = '"+telephone+"' ");
		}
		if (Utility.testString(startDate)) {//导入开始时间
			sql.append(" and a.create_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if (Utility.testString(endDate)) {//导入结束时间
			sql.append(" and a.create_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
		}
		if(Utility.testString(leadsOrigin)){//线索来源
			sql.append(" and a.leads_origin = '"+leadsOrigin+"' \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" and a.province = '"+dPro+"' \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" and a.city = '"+dCity+"' \n");
		}
		if(Utility.testString(dArea)){
			sql.append(" and a.area = '"+dArea+"' \n");
		}
//		if(Utility.testString(dealerCode)){
//			if (null != dealerCode && !"".equals(dealerCode)) {
//				String[] array = dealerCode.split(",");
//				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_CODE=A.DEALER_ID AND J.DEALER_CODE IN (\n");
//				for (int i = 0; i < array.length; i++) {
//					sql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sql.append(",");
//					}
//				}
//				sql.append("))\n");
//			}
//		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * ajax客户建档信息查询
	 */
	
	public List<Map<String, Object>> customerInfoQuery(
			String customerName,
			String telephone,String dealerId,
			String adviser,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.CUSTOMER_NAME,A.COME_REASON,A.IF_DRIVE,A.BUY_TYPE,A.CUSTOMER_ID,\r\n");
		sql.append("       A.TELEPHONE,\r\n");
		sql.append("       A.JC_WAY,\r\n");
		sql.append("       C1.CODE_DESC AS JC_WAY2,\r\n");
		sql.append("       A.BUY_BUDGET,\r\n");
		sql.append("       A.CTM_PROP,\r\n");
		sql.append("       C2.CODE_DESC AS CTM_PROP2,\r\n");
		sql.append("       C3.CODE_DESC AS IF_DRIVE2,\r\n");
		sql.append("       C4.CODE_DESC AS COME_REASON2,\r\n");
		sql.append("       C5.CODE_DESC AS BUY_TYPE2,\r\n");
		sql.append("       C6.CODE_DESC AS CTM_RANK2,\r\n");
		sql.append("       C7.CODE_DESC AS SALES_PROGRESS2,\r\n");
		sql.append("       C8.CODE_DESC AS BUY_BUDGET2,\r\n");
		sql.append("       A.INTENT_VEHICLE,\r\n");
		sql.append("       A.CTM_RANK,\r\n");
		sql.append("       A.PROVICE_ID,\r\n");
		sql.append("       A.CITY_ID,\r\n");
		sql.append("       A.TOWN_ID,B.UP_SERIES_ID,A.SALES_PROGRESS \r\n");
		sql.append("  FROM T_PC_INTENT_VEHICLE B,T_PC_CUSTOMER A LEFT JOIN TC_CODE C1 ON A.JC_WAY = C1.CODE_ID" +
				" LEFT JOIN TC_CODE C2 ON A.CTM_PROP = C2.CODE_ID " +
				" LEFT JOIN TC_CODE C3 ON A.IF_DRIVE = C3.CODE_ID " +
				" LEFT JOIN TC_CODE C4 ON A.COME_REASON = C4.CODE_ID " +
				" LEFT JOIN TC_CODE C5 ON A.BUY_TYPE = C5.CODE_ID " +
				" LEFT JOIN TC_CODE C6 ON A.CTM_RANK = C6.CODE_ID " +
				" LEFT JOIN TC_CODE C7 ON A.SALES_PROGRESS = C7.CODE_ID " +
				" LEFT JOIN TC_CODE C8 ON A.BUY_BUDGET = C8.CODE_ID " +
				"where 1=1 AND A.INTENT_VEHICLE = B.SERIES_ID AND A.CTM_TYPE IN(60341002,60341003) "); 

//			sql.append(" and a.customer_name = '"+customerName+"' ");
			sql.append(" and a.telephone = '"+telephone+"' ");
		if (Utility.testString(dealerId)) {//经销商ID
			sql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		if (Utility.testString(adviser)) {//顾问
			sql.append(" and a.adviser = '"+adviser+"' ");
		}
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * ajaxDCRC录入时验证是否建档并获取顾问,集客方式,购买类型,客户类型等客户信息.
	 */
	
	public List<Map<String, Object>> getAdviserIfHas(
			String telephone,String dealerId,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select tc.adviser,tu.name,tc.jc_way,\n" +
				"       C1.CODE_DESC as jc_way2,\n" + 
				"       tc.customer_name,\n" + 
				"       tc.IF_DRIVE as test_Driving,\n" + 
				"       C2.CODE_DESC as test_Driving2,\n" + 
				"       tc.buy_budget,\n" + 
				"       C3.Code_Desc as buy_budget2,\n" + 
				"       tc.buy_type,\n" + 
				"       C4.Code_Desc as buy_type2,\n" + 
				"       tc.intent_vehicle,\n" + 
				"       C5.Code_Desc as intent_vehicle2,\n" + 
				"       tc.ctm_prop as customer_Type,\n" + 
				"       C6.CODE_DESC as customer_Type2,\n" + 
				"		B.UP_SERIES_ID \n" +
				"  from t_pc_customer tc\n" + 
				"  LEFT JOIN TC_CODE C1 ON tc.jc_way = C1.CODE_ID\n" + 
				"  LEFT JOIN TC_CODE C2 ON tc.IF_DRIVE = C2.CODE_ID\n" + 
				"  LEFT JOIN TC_CODE C3 ON tc.buy_budget = C3.CODE_ID\n" + 
				"  LEFT JOIN TC_CODE C4 ON tc.buy_type = C4.CODE_ID\n" + 
				"  LEFT JOIN TC_CODE C5 ON tc.intent_vehicle = C5.CODE_ID\n" + 
				"  LEFT JOIN TC_CODE C6 ON tc.ctm_prop = C6.CODE_ID\n" 	+
				"  LEFT JOIN TC_USER tu ON tc.adviser = tu.user_id\n" 	+
				"  LEFT JOIN T_PC_INTENT_VEHICLE B ON tc.INTENT_VEHICLE = B.SERIES_ID \n"); 
				sql.append("  where tc.telephone = '"+telephone+"'");
		if (Utility.testString(dealerId)) {//经销商ID
			sql.append(" and tc.dealer_id = '"+dealerId+"' ");
		}
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * ajax客户建档信息查询
	 */
	
	public List<Map<String, Object>> customerInfoQuery3(
			String customerName,
			String telephone,String dealerId,
			String adviser,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.CUSTOMER_NAME,A.COME_REASON,A.IF_DRIVE,A.BUY_TYPE,A.CUSTOMER_ID,\r\n");
		sql.append("       A.TELEPHONE,\r\n");
		sql.append("       A.JC_WAY,\r\n");
		sql.append("       C1.CODE_DESC AS JC_WAY2,\r\n");
		sql.append("       A.BUY_BUDGET,\r\n");
		sql.append("       A.CTM_PROP,\r\n");
		sql.append("       C2.CODE_DESC AS CTM_PROP2,\r\n");
		sql.append("       C3.CODE_DESC AS IF_DRIVE2,\r\n");
		sql.append("       C4.CODE_DESC AS COME_REASON2,\r\n");
		sql.append("       C5.CODE_DESC AS BUY_TYPE2,\r\n");
		sql.append("       C6.CODE_DESC AS CTM_RANK2,\r\n");
		sql.append("       C7.CODE_DESC AS SALES_PROGRESS2,\r\n");
		sql.append("       C8.CODE_DESC AS BUY_BUDGET2,\r\n");
		sql.append("       C9.CODE_DESC AS CTM_TYPE2,\r\n");
		sql.append("       A.INTENT_VEHICLE,\r\n");
		sql.append("       A.CTM_RANK,\r\n");
		sql.append("       A.PROVICE_ID,\r\n");
		sql.append("       A.CITY_ID,A.CTM_TYPE,\r\n");
		sql.append("       A.TOWN_ID,B.UP_SERIES_ID,A.SALES_PROGRESS \r\n");
		sql.append("  FROM T_PC_INTENT_VEHICLE B,T_PC_CUSTOMER A LEFT JOIN TC_CODE C1 ON A.JC_WAY = C1.CODE_ID" +
				" LEFT JOIN TC_CODE C2 ON A.CTM_PROP = C2.CODE_ID " +
				" LEFT JOIN TC_CODE C3 ON A.IF_DRIVE = C3.CODE_ID " +
				" LEFT JOIN TC_CODE C4 ON A.COME_REASON = C4.CODE_ID " +
				" LEFT JOIN TC_CODE C5 ON A.BUY_TYPE = C5.CODE_ID " +
				" LEFT JOIN TC_CODE C6 ON A.CTM_RANK = C6.CODE_ID " +
				" LEFT JOIN TC_CODE C7 ON A.SALES_PROGRESS = C7.CODE_ID " +
				" LEFT JOIN TC_CODE C8 ON A.BUY_BUDGET = C8.CODE_ID " +
				" LEFT JOIN TC_CODE C9 ON A.CTM_TYPE = C9.CODE_ID " +
				"where 1=1 AND A.INTENT_VEHICLE = B.SERIES_ID AND A.CTM_TYPE IN(60341001,60341004,60341005) "); 

//			sql.append(" and a.customer_name = '"+customerName+"' ");
			sql.append(" and a.telephone = '"+telephone+"' ");
		if (Utility.testString(dealerId)) {//经销商ID
			sql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		if (Utility.testString(adviser)) {//顾问
			sql.append(" and a.adviser = '"+adviser+"' ");
		}
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * ajax客户建档信息查询(是否已有其他顾问的建档信息)
	 */
	
	public List<Map<String, Object>> customerInfoQuery2(
			String customerName,
			String telephone,String dealerId,
			String adviser,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.CUSTOMER_NAME,A.COME_REASON,A.IF_DRIVE,A.BUY_TYPE,A.CUSTOMER_ID,\r\n");
		sql.append("       A.TELEPHONE,\r\n");
		sql.append("       A.JC_WAY,\r\n");
		sql.append("       C1.CODE_DESC AS JC_WAY2,\r\n");
		sql.append("       A.BUY_BUDGET,\r\n");
		sql.append("       A.CTM_PROP,\r\n");
		sql.append("       C2.CODE_DESC AS CTM_PROP2,\r\n");
		sql.append("       C3.CODE_DESC AS IF_DRIVE2,\r\n");
		sql.append("       C4.CODE_DESC AS COME_REASON2,\r\n");
		sql.append("       C5.CODE_DESC AS BUY_TYPE2,\r\n");
		sql.append("       C6.CODE_DESC AS CTM_RANK2,\r\n");
		sql.append("       C7.CODE_DESC AS SALES_PROGRESS2,\r\n");
		sql.append("       C8.CODE_DESC AS BUY_BUDGET2,\r\n");
		sql.append("       A.INTENT_VEHICLE,\r\n");
		sql.append("       A.CTM_RANK,\r\n");
		sql.append("       A.PROVICE_ID,\r\n");
		sql.append("       A.CITY_ID,\r\n");
		sql.append("       A.TOWN_ID,B.UP_SERIES_ID,A.SALES_PROGRESS \r\n");
		sql.append("  FROM T_PC_INTENT_VEHICLE B,T_PC_CUSTOMER A LEFT JOIN TC_CODE C1 ON A.JC_WAY = C1.CODE_ID" +
				" LEFT JOIN TC_CODE C2 ON A.CTM_PROP = C2.CODE_ID " +
				" LEFT JOIN TC_CODE C3 ON A.IF_DRIVE = C3.CODE_ID " +
				" LEFT JOIN TC_CODE C4 ON A.COME_REASON = C4.CODE_ID " +
				" LEFT JOIN TC_CODE C5 ON A.BUY_TYPE = C5.CODE_ID " +
				" LEFT JOIN TC_CODE C6 ON A.CTM_RANK = C6.CODE_ID " +
				" LEFT JOIN TC_CODE C7 ON A.SALES_PROGRESS = C7.CODE_ID " +
				" LEFT JOIN TC_CODE C8 ON A.BUY_BUDGET = C8.CODE_ID " +
				"where 1=1 AND A.INTENT_VEHICLE = B.SERIES_ID "); 

//			sql.append(" and a.customer_name = '"+customerName+"' ");
			sql.append(" and a.telephone = '"+telephone+"' ");
		if (Utility.testString(dealerId)) {//经销商ID
			sql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		if (Utility.testString(adviser)) {//顾问
			sql.append(" and a.adviser <> '"+adviser+"' ");
		}
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * ajax根据车型获取颜色
	 */
	
	public List<DynaBean> colorInfoBySeries(
			String modelCode,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.COLOR_CODE,A.COLOR_NAME FROM TM_VHCL_MATERIAL A WHERE A.MODEL_CODE = '"+modelCode+"'"); 

		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/**
	 * ajax根据VIN获取车型颜色
	 */
	
	public List<DynaBean> seriesColorInfoByVIN(
			String vin,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT B.MATERIAL_NAME,A.COLOR,B.MATERIAL_ID,B.COLOR_CODE FROM TM_VEHICLE A, TM_VHCL_MATERIAL B WHERE A.MATERIAL_ID = B.MATERIAL_ID AND A.VIN ='"+vin+"'"); 

		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/**
	 * ajax未完成任务查询
	 */
	
	public List<DynaBean> unDoTaskQuery(
			String customerId,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT A1.CUSTOMER_ID, A1.TASK_TYPE, A2.CUSTOMER_NAME, A2.TELEPHONE, A1.TASK_ID\r\n");
		sbSql.append("  FROM (SELECT A.CUSTOMER_ID,\r\n");
		sbSql.append("               '跟进任务' AS TASK_TYPE,\r\n");
		sbSql.append("               A.FOLLOW_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_FOLLOW A WHERE A.TASK_STATUS = 60171001\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT B.CUSTOMER_ID,\r\n");
		sbSql.append("               '计划邀约任务' AS TASK_TYPE,\r\n");
		sbSql.append("               B.INVITE_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_INVITE B WHERE B.TASK_STATUS = 60171001\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT C.CUSTOMER_ID,\r\n");
		sbSql.append("               '邀约到店任务' AS TASK_TYPE,\r\n");
		sbSql.append("               C.INVITE_SHOP_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_INVITE_SHOP C WHERE C.TASK_STATUS = 60171001\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT D.CUSTOMER_ID,\r\n");
		sbSql.append("               '订单任务' AS TASK_TYPE,\r\n");
		sbSql.append("               D.ORDER_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_ORDER D WHERE D.TASK_STATUS = 60171001\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT D2.CUSTOMER_ID,\r\n");
		sbSql.append("               '退单任务' AS TASK_TYPE,\r\n");
		sbSql.append("               D2.ORDER_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_ORDER D2 WHERE D2.TASK_STATUS = 60171001 AND D2.ORDER_STATUS=60231007\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT E.CUSTOMER_ID,\r\n");
		sbSql.append("               '交车任务' AS TASK_TYPE,\r\n");
		sbSql.append("               E.ORDER_DETAIL_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_ORDER_DETAIL E WHERE E.TASK_STATUS = 60171001\r\n");
		sbSql.append("        UNION ALL\r\n");
		sbSql.append("        SELECT F.CUSTOMER_ID,\r\n");
		sbSql.append("               '回访任务' AS TASK_TYPE,\r\n");
		sbSql.append("               F.REVISIT_ID AS TASK_ID\r\n");
		sbSql.append("          FROM T_PC_REVISIT F WHERE F.TASK_STATUS = 60171001) A1,"); 
		sbSql.append("       T_PC_CUSTOMER A2"); 
		sbSql.append(" WHERE A1.CUSTOMER_ID = A2.CUSTOMER_ID\r\n");
		sbSql.append("   AND A1.CUSTOMER_ID = '"+customerId+"'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/**
	 * ajax获取提醒信息
	 */
	
	public List<Map<String, Object>> getRemindInfo(
			String dealerId,
			String adviser,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.CUSTOMER_ID,A.BEREMIND_ID,C.CUSTOMER_NAME,A.REMIND_NUM,C.TELEPHONE,\r\n");
		sql.append("       A.REMIND_TYPE,\r\n");
		sql.append("       B.CODE_DESC AS REMIND_TYPE_NAME,\r\n");
		sql.append("       TO_CHAR(A.REMIND_DATE, 'YYYY-MM-DD') AS REMIND_DATE\r\n");
		sql.append("  FROM T_PC_REMIND A\r\n");
		sql.append("   JOIN TC_CODE B\r\n");
		sql.append("    ON A.REMIND_TYPE = B.CODE_ID\r\n");
		sql.append("    left JOIN T_PC_CUSTOMER C ON A.CUSTOMER_ID = C.CUSTOMER_ID\r\n");
		sql.append(" WHERE A.REMIND_STATUS = "+Constant.TASK_STATUS_01+"");
		
		if (Utility.testString(dealerId)) {//经销商
			sql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		if (Utility.testString(adviser)) {//顾问
			sql.append(" and a.adviser = '"+adviser+"' ");
		} else {
			sql.append(" AND A.REMIND_TYPE IN("+Constant.REMIND_TYPE_01+","+Constant.REMIND_TYPE_10+","+Constant.REMIND_TYPE_11+","+Constant.REMIND_TYPE_13+","+Constant.REMIND_TYPE_14+","+Constant.REMIND_TYPE_15+","+Constant.REMIND_TYPE_21+")");
		}
		sql.append(" ORDER BY A.REMIND_DATE DESC ");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 销售线索查询
	 */
	
	public PageResult<Map<String, Object>> oemLeadsFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String leadsOrigin,String dPro,String dCity,
			String dArea,String dealerCode,String allotStatus,String seriesId,String timeOut,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from (\r\n");
		sbSql.append("select a.leads_code,a2.dealer_id,a2.LEADS_ALLOT_ID,a.leads_type,e.series_name,e.up_series_id,a.intent_vehicle,a.intent_car, \r\n");
		sbSql.append("       c.code_desc as leads_origin,a.leads_origin as leads_origin_code,\r\n");
		sbSql.append("       c2.code_desc as leads_status,a.leads_status as leads_status_code,\r\n");
		sbSql.append("       to_char(a.collect_date, 'yyyy-MM-dd') collect_date,\r\n");
		sbSql.append("       b1.region_name as province,a.province as province_code,\r\n");
		sbSql.append("       b2.region_name as city,a.city as city_code,\r\n");
		sbSql.append("       b3.region_name as area,a.area as area_code,\r\n");
		sbSql.append("       a.customer_name,decode(a2.dealer_id,null,60281001,60281002) as allot_status,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       d.dealer_shortname AS DEALER_NAME,\r\n");
		sbSql.append("       to_char(a2.allot_dealer_date, 'yyyy-MM-dd HH24:ss:mi') as allot_dealer_date,\r\n");
		sbSql.append("       a.remark,a.create_date,\r\n");
		//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理。
		//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
		sbSql.append("       decode(a2.allot_dealer_date,null,'10041002',case when to_char(a2.allot_dealer_date, 'HH24') >= 9 and to_char(a2.allot_dealer_date, 'HH24')<17\r\n");
		sbSql.append("         then (case when sysdate > to_date(to_char(a2.allot_dealer_date, 'yyyy-MM-dd') || ' 19:00:00','yyyy-MM-dd HH24;ss:mi') and a2.adviser is null then 10041001 else 10041002 end)\r\n");
		sbSql.append("          else (case when sysdate > to_date(to_char(a2.allot_dealer_date+1, 'yyyy-MM-dd') || ' 11:00:00','yyyy-MM-dd HH24:ss:mi') and a2.adviser is null then 10041001 else 10041002 end) end) as timeout\r\n");
		sbSql.append("  from t_pc_leads a\r\n");
		sbSql.append("  left join t_pc_leads_allot a2\r\n");
		sbSql.append("    on a.leads_code = a2.leads_code\r\n");
		sbSql.append("  left join tm_region b1\r\n");
		sbSql.append("    on a.province = b1.region_code\r\n");
		sbSql.append("  left join tm_region b2\r\n");
		sbSql.append("    on a.city = b2.region_code\r\n");
		sbSql.append("  left join tm_region b3\r\n");
		sbSql.append("    on a.area = b3.region_code\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.LEADS_ORIGIN = C.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c2\r\n");
		sbSql.append("    ON A.leads_status = C2.CODE_ID\r\n");
		sbSql.append("  left join tm_dealer d\r\n");
		sbSql.append("    on a2.dealer_id = d.dealer_id\r\n");
		sbSql.append("  left join t_pc_intent_vehicle e\r\n");
		sbSql.append("    on a.intent_vehicle = e.series_id\r\n");
		sbSql.append(") aa where 1=1 and aa.leads_type = "+Constant.LEADS_TYPE_01+""); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and aa.customer_name = '"+customerName+"' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and aa.telephone = '"+telephone+"' ");
		}
		if (Utility.testString(startDate)) {//导入开始时间
			sbSql.append(" and aa.create_date >= to_date('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
		}
		if (Utility.testString(endDate)) {//导入结束时间
			sbSql.append(" and aa.create_date <= to_date('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(Utility.testString(leadsOrigin)){//线索来源
			sbSql.append(" and aa.leads_origin_code = '"+leadsOrigin+"' \n");
		}
		if(Utility.testString(seriesId)){
			 sbSql.append("  AND (aa.up_series_id in ("+seriesId+")  or ( aa.intent_car in ("+seriesId+") and aa.intent_vehicle is null) )\n" );
	      }
		if(Utility.testString(dPro)){
			sbSql.append(" and aa.province_code = '"+dPro+"' \n");
		}
		if(Utility.testString(dCity)){
			sbSql.append(" and aa.city_code = '"+dCity+"' \n");
		}
		if(Utility.testString(dArea)){
			sbSql.append(" and aa.area_code = '"+dArea+"' \n");
		}
		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.dealer_id is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.dealer_id is not null \n");
		}
		if(Utility.testString(timeOut)){
			sbSql.append(" and aa.timeout = '"+timeOut+"' \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sbSql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=AA.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sbSql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sbSql.append(",");
					}
				}
				sbSql.append("))\n");
			}
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * ajax获取信息
	 */
	
	public PageResult<Map<String, Object>> getOverviewInfo(Map<String,String> map,int curPage, int pageSize) {
		String adviser=map.get("adviser");
		String flag=map.get("flag");
		StringBuffer sql = new StringBuffer();
		sql.append("  select  decode(d.customer_name,null,e.customer_name,d.customer_name) as  customer_name,");
		
		sql.append("  decode(d.telephone,null,e.telephone,d.telephone) as telephone,");
		
		sql.append("  b.name, c.code_desc, to_char(a.remind_date, 'yyyy-MM-dd') REMINDDATE ,  \r\n");
		
		sql.append("    decode(a.remind_num,null,1,0,1,remind_num) REMINDNUM  \r\n");
		
		sql.append("    from t_pc_remind a   \r\n");
		
		sql.append("   left join tc_user b on a.adviser = b.user_id   \r\n");
		
		sql.append("   left join tc_code c on a.remind_type = c.code_id   \r\n");
		
		sql.append("     left join t_pc_customer d on a.customer_id = d.customer_id  \r\n");
		
		sql.append("      left join t_pc_leads_allot e on a.beremind_id = e.leads_allot_id  \r\n");
		
		sql.append("   where A.REMIND_STATUS="+Constant.TASK_STATUS_01);
		
		if (Utility.testString(adviser)) { 
			sql.append(" and a.adviser = '"+adviser+"' ");
		}  
		if("1".equals(flag)){
			sql.append(" and a.remind_date<sysdate") ;
		}else{
			sql.append(" and a.remind_date>sysdate") ;
		}
		sql.append("  order by a.remind_date ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 顾问数量信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOverview(Map<String, String> map) {
		String dealerId=map.get("dealerId");
		String userId=map.get("userId");
	 
		StringBuilder sql= new StringBuilder();
		sql.append("select TU.USER_ID ADVISER,tu.name,ts.outs,ts.normals from (select t.user_id, sum(t.out_count) outs, sum(normal_count) normals\n" );
		sql.append("  from (select user_id, 1 out_count, 0 normal_count\n" );
		sql.append("          from t_pc_remind a, tc_user b\n" );
		sql.append("         where a.adviser = b.user_id\n" );
		sql.append(" and A.REMIND_STATUS="+Constant.TASK_STATUS_01 );
		sql.append("           and a.dealer_id = "+dealerId+"\n" );
		sql.append("           and a.remind_date < sysdate\n" );
		sql.append("        union all\n" );
		sql.append("        select user_id, 0 out_count, 1 normal_count\n" );
		sql.append("          from t_pc_remind a, tc_user b\n" );
		sql.append("         where a.adviser = b.user_id\n" );
		sql.append(" and A.REMIND_STATUS="+Constant.TASK_STATUS_01 );
		sql.append("           and a.dealer_id = "+dealerId+"\n" );
		sql.append("           and a.remind_date > sysdate) t\n" );
		sql.append(" where 1=1\n   " );
		if(null!=dealerId&&!"".equals(dealerId)){
//			sql.append(" and a.dealer_id="+dealerId);
			sql.append(" and   T.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+userId+")  \n");
			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		sql.append(" group by t.user_id) ts,tc_user tu\n" );
		sql.append(" where tu.user_id=ts.user_id");
		
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return ps;
	}
 
	
}