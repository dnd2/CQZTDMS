package com.infodms.dms.actions.crm.customer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.follow.FollowManage;
import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.customer.CustomerManageDao;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCompetVechilePO;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDecorationPO;
import com.infodms.dms.po.TPcDefeatVehiclePO;
import com.infodms.dms.po.TPcDrivingPO;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TPcInsurencePO;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcLinkManPO;
import com.infodms.dms.po.TPcLookPO;
import com.infodms.dms.po.TPcRemindPO;
import com.infodms.dms.po.TPcVechilePO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CustomerManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final CustomerManageDao dao = CustomerManageDao.getInstance();

	private final String CUSTOMER_QUERY_URL = "/jsp/crm/customer/customerInit.jsp";// 客户查询页面
	private final String CUSTOMER_ADD_QUERY_URL = "/jsp/crm/customer/customerAddInit.jsp";// 客户查询页面
	private final String CUSTOMER_ADD_INIT = "/jsp/crm/customer/customerAdd.jsp";// 客户新增页面
	private final String CTM_UPDATE_INIT = "/jsp/crm/customer/customerUpdate.jsp";// 客户修改
	private final String TO_INSURE_URL = "/jsp/crm/customer/insureAdd.jsp";//保险公司的添加
	private final String TO_INSURE_UPDATE_URL = "/jsp/crm/customer/insureUpdate.jsp";//保险公司的添加
	private final String TO_DOC_URL = "/jsp/crm/customer/docAdd.jsp";//装饰新增
	private final String TO_DOC_UPDATE_URL = "/jsp/crm/customer/docUpdate.jsp";//装饰修改
	private final String TO_LINK_URL = "/jsp/crm/customer/linkAdd.jsp";//联系人新增
	private final String TO_LINK_UPDATE_URL = "/jsp/crm/customer/linkUpdate.jsp";//联系人修改
	private final String TO_VEHICLE_URL = "/jsp/crm/customer/vehicleAdd.jsp";//车辆信息添加
	private final String TO_VEHICLE_UPDATE_URL = "/jsp/crm/customer/vehicleUpdate.jsp";//车辆信息修改
	private final String TO_DRIVE_URL = "/jsp/crm/customer/driveAdd.jsp";//试乘试驾新增
	private final String TO_DRIVE_UPDATE_URL = "/jsp/crm/customer/driveUpdate.jsp";//试乘试驾修改
	private final String toIntentListURL = "/jsp/crm/customer/intentVechileList.jsp";
	private final String toCompetListURL = "/jsp/crm/customer/competVechileList.jsp";
	private final String TO_ADVISER_LIST = "/jsp/crm/customer/adviserList.jsp";
	
	
	/**
	 * 进入客户查询主界面
	 */
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			boolean flag=CommonUtils.judgeMgrLogin(logonUser.getUserId().toString());
			if(flag){
				act.setOutData("isMgr", 1);
			}else{
				act.setOutData("isMgr", 0);
			}
			FollowManage.getManager(logonUser, act);
			act.setOutData("funcStr", funcStr);
			act.setForword(CUSTOMER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 进入顾问选择界面
	 */
	public void changeAdviserInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String customerIds=act.getRequest().getParamValue("ctmIds");
			Map<String,String> map=new HashMap<String,String>();
			map.put("dealerId", logonUser.getDealerId().toString());
			map.put("userId", Long.toString(logonUser.getUserId()));
			List<Map<String,Object>> list=CommonUtils.queryUser(map);
			act.setOutData("userList", list);
			act.setOutData("customerIds", customerIds);
			act.setForword(TO_ADVISER_LIST);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 确认分配顾问
	 */
	public void sureDipatch() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String customerIds=act.getRequest().getParamValue("ctmIds");
			String adviser=act.getRequest().getParamValue("adviser");
			String []ctmIds=customerIds.split(",");
			for(int i=0;i<ctmIds.length;i++){
				if(ctmIds[i]!=null&&!"".equals(ctmIds[i])){
					//修改客户所属
					//原来信息
					TPcCustomerPO tpc0=new TPcCustomerPO();
					tpc0.setCustomerId(new Long(ctmIds[i]));
					tpc0=(TPcCustomerPO) dao.select(tpc0).get(0);
					
					TPcCustomerPO tpc=new TPcCustomerPO();
					tpc.setCustomerId(new Long(ctmIds[i]));
					TPcCustomerPO tpc1=new TPcCustomerPO();
					tpc1.setAdviser(new Long(adviser));
					dao.update(tpc, tpc1);
					//修改提醒信息
					TPcRemindPO tprp=new TPcRemindPO();
					tprp.setAdviser(tpc0.getAdviser().toString());
					tprp.setCustomerId(new Long(ctmIds[i]));
					tprp.setRemindStatus(Constant.TASK_STATUS_01);
					TPcRemindPO tprp1=new TPcRemindPO();
					tprp1.setAdviser(adviser);
					dao.update(tprp, tprp1);
					
				}
			}
			act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 进入客户查询主界面
	 */
	public void doAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			act.setForword(CUSTOMER_ADD_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 客户新增初始化
	 */
	public void addPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CUSTOMER_ADD_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 客户修改初始化
	 */
	public void ctmUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			String ctmId=request.getParamValue("ctmId");

			String isClose=request.getParamValue("isClose");
			String beRemindId = act.getRequest().getParamValue("beRemindId");
			if(beRemindId!=null&&!"".equals(beRemindId)){
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(beRemindId,Constant.REMIND_TYPE_20.toString());
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(beRemindId,Constant.REMIND_TYPE_20.toString());
			}
			
			//获取客户表的数据
			TPcCustomerPO tpc=new TPcCustomerPO();
			tpc.setCustomerId(new Long(ctmId));
			tpc=(TPcCustomerPO) dao.select(tpc).get(0);
			//获取下次回访时间
			Date nextContactTime=tpc.getNextContactTime();
			//获取预计订车时间
			Date preOrderTime=tpc.getPreOrderTime();
			//订车时间
			Date orderTime=tpc.getOrderTime();
			//生日
			Date birthDay=tpc.getBirthday();
			//交车日期
			Date delvTime=tpc.getDelvTime();
			//提醒日期
			Date specialTime=tpc.getSpecialTime();
			//建档日期
			Date createDate=tpc.getCreateDate();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Map<String,String> datemap=new HashMap<String, String>();
			datemap.put("nextContactTime", nextContactTime==null?"":sdf.format(nextContactTime));
			datemap.put("preOrderTime", preOrderTime==null?"":sdf.format(preOrderTime));
			datemap.put("orderTime", orderTime==null?"":sdf.format(orderTime));
			datemap.put("birthDay", birthDay==null?"":sdf.format(birthDay));
			datemap.put("delvTime", delvTime==null?"":sdf.format(delvTime));
			datemap.put("specialTime", specialTime==null?"":sdf.format(specialTime));
			datemap.put("createDate", createDate==null?"":sdf.format(createDate));
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			//加载试乘试驾信息
			List<Map<String,Object>> driveList=dao.driveList(map);
			//加载其他联系人信息
			List<Map<String,Object>> linkList=dao.linkList(map);
			String relationship=null;
			Iterator it1 = (Iterator) linkList.iterator();
			while(it1.hasNext()) {
				DynaBean db1 = (DynaBean)it1.next();
				if(db1.get("RELATIONSHIP")!=null&&!"".equals(db1.get("RELATIONSHIP"))) {
					relationship = db1.get("RELATIONSHIP").toString();
				} else {
					relationship = "";
				}
			
			}
			
			//加载装饰装潢信息
			List<Map<String,Object>> docList=dao.docList(map);
			
			//加载保险信息
			List<Map<String,Object>> insureList=dao.insureList(map);
			//加载接触点信息
			List<Map<String,Object>> contactList=dao.contactList(map);
			//车辆信息
			List<Map<String,Object>> vehicleList=dao.vehicleList(map);
			//获取销售店名称
			TmDealerPO td=new TmDealerPO();
			td.setDealerId(tpc.getDealerId());
			td=(TmDealerPO) dao.select(td).get(0);
			//获取意向车型名称和id
			TPcIntentVehiclePO tpv=new TPcIntentVehiclePO();
			if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
				tpv.setSeriesId(new Long(tpc.getIntentVehicle()));
				tpv=(TPcIntentVehiclePO) dao.select(tpv).get(0);
			}
			//每次进入修改页面都加入一条浏览记录
			addLook(ctmId,logonUser);
			map.put("poseRank", Constant.DEALER_USER_LEVEL_04.toString());
			int adviserCount=dao.lookList(map);//获取销售顾问浏览次数数据
			map.put("poseRank", Constant.DEALER_USER_LEVEL_02.toString());
			int saleMgrCount=dao.lookList(map);//获取销售经理浏览次数数据
			map.put("poseRank", Constant.DEALER_USER_LEVEL_01.toString());
			int mgrCount=dao.lookList(map);//获取总经理浏览次数数据
			map.put("poseRank", Constant.DEALER_USER_LEVEL_05.toString());
			int dcrcCount=dao.lookList(map);//获取DCRC浏览次数数据
			//获取竞品车型
			
			TPcDefeatVehiclePO tpcv=new TPcDefeatVehiclePO();
			if(tpc.getCompetVechile()!=null){
				tpcv.setSeriesId(new Long(tpc.getCompetVechile()));
				if(dao.select(tpcv).size()>0){
					tpcv=(TPcDefeatVehiclePO) dao.select(tpcv).get(0);
				}
			}
			
			//获取其他品牌
			TPcDefeatVehiclePO tpcv1=new TPcDefeatVehiclePO();
			if(tpc.getOtherProduct()!=null){
				tpcv1.setSeriesId(new Long(tpc.getOtherProduct()));
				if(dao.select(tpcv1).size()>0){
					tpcv1=(TPcDefeatVehiclePO) dao.select(tpcv1).get(0);
				}
			}
			
			//获取客户动态
			List<Map<String,Object>> varList=dao.getVarList(map);
			int varCount=varList.size();
			if(varCount<12){
				varCount=12-varCount;
			}
			//获取集客方式和日期和线索来源
			List<Map<String,Object>> leadsList=dao.getLeadsList(map);
			String JC_WAY=null;
			Iterator it = (Iterator) leadsList.iterator();
			while(it.hasNext()) {
				DynaBean db = (DynaBean)it.next();
				if(db.get("JC_WAY")!=null&&!"".equals(db.get("JC_WAY"))) {
					JC_WAY = db.get("JC_WAY").toString();
				} else {
					JC_WAY = "";
				}
			
			}
			//获取接下来的操作任务
			List<Map<String,Object>> nextList=dao.getNextTask(map);
			//获取客户等级变化
			map.put("ctmRank", "60101005");
			List<Map<String,Object>> rankOVarList=dao.getRankVarList(map);//查询o的等级的数据(顶部)
			map.put("ctmRank", "60101001");
			List<Map<String,Object>> rankHVarList=dao.getRankVarList(map);//查询H的等级的数据(顶部)
			map.put("ctmRank", "60101002");
			List<Map<String,Object>> rankAVarList=dao.getRankVarList(map);//查询A的等级的数据(顶部)
			map.put("ctmRank", "60101003");
			List<Map<String,Object>> rankBVarList=dao.getRankVarList(map);//查询B的等级的数据(顶部)
			map.put("ctmRank", "60101004");
			List<Map<String,Object>> rankCVarList=dao.getRankVarList(map);//查询C的等级的数据(顶部)
			map.put("ctmRank", "60101006");
			List<Map<String,Object>> rankEVarList=dao.getRankVarList(map);//查询E的等级的数据(顶部)
			map.put("ctmRank", "60101007");
			List<Map<String,Object>> rankLVarList=dao.getRankVarList(map);//查询L的等级的数据(顶部)
			
			List<Map<String,Object>> dataList=null;
			dataList=dao.loadDataList(map);
			
			TcUserPO tu=new TcUserPO();
			tu.setUserId(tpc.getAdviser());
			tu=(TcUserPO) dao.select(tu).get(0);
			String leadsOrigin=CommonUtils.getCodeDesc(tpc.getLeadsOrigin());
			act.setOutData("leadsOrigin", leadsOrigin);
			act.setOutData("dataList", dataList);
			act.setOutData("rankOVarList", rankOVarList);
			act.setOutData("rankHVarList", rankHVarList);
			act.setOutData("rankAVarList", rankAVarList);
			act.setOutData("rankBVarList", rankBVarList);
			act.setOutData("rankCVarList", rankCVarList);
			act.setOutData("rankEVarList", rankEVarList);
			act.setOutData("rankLVarList", rankLVarList);
			act.setOutData("varCount", varCount);
			act.setOutData("varList", varList);
			act.setOutData("leadsList", leadsList);
			act.setOutData("JC_WAY",JC_WAY);
			act.setOutData("relationship",relationship);
			act.setOutData("tpcv", tpcv);
			act.setOutData("tpcv1", tpcv1);
			act.setOutData("adviserCount", adviserCount);
			act.setOutData("saleMgrCount", saleMgrCount);
			act.setOutData("mgrCount", mgrCount);
			act.setOutData("dcrcCount", dcrcCount);
			act.setOutData("tpv", tpv);
			act.setOutData("td", td);
			act.setOutData("datemap", datemap);
			act.setOutData("tpc", tpc);
			act.setOutData("funcStr", funcStr);
			act.setOutData("driveList", driveList);
			act.setOutData("linkList", linkList);
			act.setOutData("docList", docList);
			act.setOutData("insureList", insureList);
			act.setOutData("contactList", contactList);
			act.setOutData("vehicleList", vehicleList);
			act.setOutData("nextList", nextList);
			act.setOutData("isClose", isClose);
			act.setOutData("tu", tu);
			act.setForword(CTM_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 添加浏览
	 * @param ctmId
	 * @param logonUser
	 */
	public void addLook(String ctmId,AclUserBean logonUser){
		TPcLookPO tplp=new TPcLookPO();
		String lookId=SequenceManager.getSequence("");
		tplp.setLookId(new Long(lookId));
		tplp.setCtmId(new Long(ctmId));
		tplp.setUserId(logonUser.getUserId());
		tplp.setLookDate(new Date());
		tplp.setCreateDate(new Date());
		tplp.setCreateBy(logonUser.getUserId());
		dao.insert(tplp);
		
	}
	
	
	/**
	 * FUNCTION		:	查询活跃客户
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void customerQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String customerName = CommonUtils.checkNull(request.getParamValue("customerName"));	//客户名称	
			String phone =  CommonUtils.checkNull(request.getParamValue("phone"));	//客户电话
			String vechile =  CommonUtils.checkNull(request.getParamValue("vechile"));	//意向车型
			String ctmNo=CommonUtils.checkNull(request.getParamValue("ctmNo"));	//客户编码
			String ctmType=CommonUtils.checkNull(request.getParamValue("ctmType"));
			String adviserId=CommonUtils.checkNull(request.getParamValue("adviserId"));//顾问 
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));//客户等级
			String jcway=CommonUtils.checkNull(request.getParamValue("collect_fashion"));//集客方式
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		
			Map<String ,String > map=new HashMap<String,String>();
			map.put("customerName", customerName);
			map.put("phone", phone);
			map.put("vechile", vechile);
			map.put("ctmNo", ctmNo);
			map.put("ctmType", ctmType);
			map.put("dealerId", logonUser.getDealerId());
			String poseRank=CommonUtils.getPoseRank(logonUser);
			if(!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				map.put("logonId", logonUser.getUserId().toString());
			}
				
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("adviserId", adviserId);
			map.put("groupId", groupId);
			map.put("ctmRank", ctmRank);
			map.put("jcway", jcway);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCustomerQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "活跃客户查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	查询所有客户
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void customerAllList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String customerName = CommonUtils.checkNull(request.getParamValue("customerName"));		
			String phone =  CommonUtils.checkNull(request.getParamValue("phone"));	
			String vechile =  CommonUtils.checkNull(request.getParamValue("vechile"));	
			String ctmNo=CommonUtils.checkNull(request.getParamValue("ctmNo"));	//客户编码
			String adviserId=CommonUtils.checkNull(request.getParamValue("adviserId"));//顾问 
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String jcway=CommonUtils.checkNull(request.getParamValue("collect_fashion1"));//集客方式
			System.out.println("执行customerAllList："+jcway);
			Map<String ,String > map=new HashMap<String,String>();
			map.put("customerName", customerName);
			map.put("phone", phone);
			map.put("vechile", vechile);
			map.put("ctmNo", ctmNo);
			map.put("dealerId", logonUser.getDealerId());
			map.put("logonId", logonUser.getUserId().toString());
			map.put("adviserId", adviserId);
			map.put("groupId", groupId);
			map.put("jcway", jcway);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize!=null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCustomerAllList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有客户查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 客户信息的添加
	 */
	public void customerAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String customerName=CommonUtils.checkNull(request.getParamValue("customerName"));
				String telephone=CommonUtils.checkNull(request.getParamValue("telephone"));
				String ifDriving=CommonUtils.checkNull(request.getParamValue("ifDriving"));
				String jcway=CommonUtils.checkNull(request.getParamValue("jcway"));
				String comeReason=CommonUtils.checkNull(request.getParamValue("comeReason"));
				String intentVechile=CommonUtils.checkNull(request.getParamValue("intentVechile"));
				String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));
				String buyVechile=CommonUtils.checkNull(request.getParamValue("buyVechile"));
				String buyBudget=CommonUtils.checkNull(request.getParamValue("buyBudget"));
				String buyWay=CommonUtils.checkNull(request.getParamValue("buyWay"));
				String buyType=CommonUtils.checkNull(request.getParamValue("buyType"));
				String provinceId=CommonUtils.checkNull(request.getParamValue("dPro"));
				String cityId=CommonUtils.checkNull(request.getParamValue("dCity"));
				String townId=CommonUtils.checkNull(request.getParamValue("dArea"));
				String address=CommonUtils.checkNull(request.getParamValue("address"));
				String birthDay=CommonUtils.checkNull(request.getParamValue("birthDay"));
				String degree=CommonUtils.checkNull(request.getParamValue("degree"));
				String interestOne=CommonUtils.checkNull(request.getParamValue("interestOne"));
				String interestTwo=CommonUtils.checkNull(request.getParamValue("interestTwo"));
				String interestThree=CommonUtils.checkNull(request.getParamValue("interestThree"));
				String company=CommonUtils.checkNull(request.getParamValue("company"));
				String job=CommonUtils.checkNull(request.getParamValue("job"));
				String income=CommonUtils.checkNull(request.getParamValue("income"));
				String home=CommonUtils.checkNull(request.getParamValue("home"));
				String gender=CommonUtils.checkNull(request.getParamValue("gender"));
				String political=CommonUtils.checkNull(request.getParamValue("political"));
				String ifMarry=CommonUtils.checkNull(request.getParamValue("ifMarry"));
				String phoneNumber=CommonUtils.checkNull(request.getParamValue("phoneNumber"));
				String indroduceMan=CommonUtils.checkNull(request.getParamValue("indroduceMan"));
				String concern=CommonUtils.checkNull(request.getParamValue("concern"));
				String colorLike=CommonUtils.checkNull(request.getParamValue("colorLike"));
				String userQQ=CommonUtils.checkNull(request.getParamValue("userQQ"));
				String buyUse=CommonUtils.checkNull(request.getParamValue("buyUse"));
				String officeNumber=CommonUtils.checkNull(request.getParamValue("officeNumber"));
				String industry=CommonUtils.checkNull(request.getParamValue("industry"));
				String companyAddress=CommonUtils.checkNull(request.getParamValue("companyAddress"));
				String ctmProp=CommonUtils.checkNull(request.getParamValue("ctmProp"));//客户性质
				String ctmType=CommonUtils.checkNull(request.getParamValue("ctmType"));//客户性质
				String nextTime= CommonUtils.checkNull(request.getParamValue("nextTime"));//下次回访时间
				String intentColor=CommonUtils.checkNull(request.getParamValue("intentColor"));//意向颜色
				String fitTime= CommonUtils.checkNull(request.getParamValue("fitTime"));//适合时间
				String fitArea=CommonUtils.checkNull(request.getParamValue("fitArea"));//适合地点
				String carFrum=CommonUtils.checkNull(request.getParamValue("carFrum"));//爱车讲堂
				String defeatType= CommonUtils.checkNull(request.getParamValue("defeatType"));//战败类型
				String defeatReason=CommonUtils.checkNull(request.getParamValue("defeatReason"));//战败原因
				String defeatRemark=CommonUtils.checkNull(request.getParamValue("defeatRemark"));//战败备注
				String specialTime=CommonUtils.checkNull(request.getParamValue("specialTime"));//战败原因
				String specialType=CommonUtils.checkNull(request.getParamValue("specialType"));//战败备注
				String orderTime=CommonUtils.checkNull(request.getParamValue("orderTime"));//订车日期
				String preOrderTime=CommonUtils.checkNull(request.getParamValue("preOrderTime"));//预计订车日期
				String delvTime=CommonUtils.checkNull(request.getParamValue("delvTime"));//预计订车日期
				String ctmRemark=CommonUtils.checkNull(request.getParamValue("ctmRemark"));//客户描述
				String imgUrl=CommonUtils.checkNull(request.getParamValue("img_url"));//图片路径
				String salesProgress=CommonUtils.checkNull(request.getParamValue("salesProgress"));//销售流程进度
				String paperNo=CommonUtils.checkNull(request.getParamValue("paperNo"));//证件号码
				String paperType=CommonUtils.checkNull(request.getParamValue("paperType"));//证件类型
				TPcCustomerPO tcp=new TPcCustomerPO();
				String seq=SequenceManager.getSequence("");
				tcp.setCustomerId(new Long(seq));
				tcp.setCustomerName(customerName);
				tcp.setCustomerCode(SequenceManager.getCtmNo());
				Map<String,String> map=new HashMap<String,String>();
				map.put("paperType", paperType);
				map.put("paperNo", paperNo);
				map.put("ctmRemark", ctmRemark);
				map.put("imgUrl", imgUrl);
				map.put("intentColor", intentColor);
				map.put("jcway",jcway);
				map.put("intentVechile",intentVechile);
				map.put("ctmRank",ctmRank);
				map.put("buyVechile",buyVechile);
				map.put("buyBudget",buyBudget);
				map.put("buyWay",buyWay);
				map.put("provinceId",provinceId);
				map.put("cityId",cityId);
				map.put("townId",townId);
				map.put("address",address);
				map.put("birthDay",birthDay);
				map.put("degree",degree);
				map.put("interestOne",interestOne);
				map.put("interestTwo",interestTwo);
				map.put("interestThree",interestThree);
				map.put("comeReason",comeReason);
				map.put("job",job);
				map.put("income",income);
				map.put("home",home);
				map.put("gender",gender);
				map.put("political",political);
				map.put("ifMarry",ifMarry);
				map.put("phoneNumber",phoneNumber);
				map.put("indroduceMan",indroduceMan);
				map.put("concern",concern);
				map.put("colorLike",colorLike);
				map.put("officeNumber",officeNumber);
				map.put("buyUse",buyUse);
				map.put("userQQ",userQQ);
				map.put("industry",industry);
				map.put("telephone",telephone);
				map.put("ifDriving",ifDriving);
				map.put("company", company);
				map.put("companyAddress",companyAddress);
				map.put("ctmType", ctmType);
				map.put("ctmProp", ctmProp);
				map.put("nextTime", nextTime);
				map.put("fitTime", fitTime);
				map.put("fitArea", fitArea);
				map.put("carFrum", carFrum);
				map.put("defeatType", defeatType);
				map.put("defeatReason", defeatReason);
				map.put("defeatRemark", defeatRemark);
				map.put("specialTime", specialTime);
				map.put("specialType", specialType);
				map.put("orderTime", orderTime);
				map.put("preOrderTime", preOrderTime);
				map.put("delvTime",delvTime);
				map.put("buyType", buyType);
				map.put("salesProgress", salesProgress);
				tcp=setData(map,tcp);
				tcp.setDealerId(new Long(logonUser.getDealerId()));
				tcp.setCreateDate(new Date());
				tcp.setCreateBy(logonUser.getUserId());
				tcp.setStatus(new Long(Constant.STATUS_ENABLE));
				String flag;
				try {
					dao.insert(tcp);
					flag=seq;
				} catch (Exception e) {
					flag="0";
				}
				act.setOutData("flag", flag);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 客户信息的x修改
	 */
	public void customerUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String customerName=CommonUtils.checkNull(request.getParamValue("customerName"));
				String telephone=CommonUtils.checkNull(request.getParamValue("telephone"));
				String ifDriving=CommonUtils.checkNull(request.getParamValue("ifDriving"));
				String jcway=CommonUtils.checkNull(request.getParamValue("jcway"));
				String comeReason=CommonUtils.checkNull(request.getParamValue("comeReason"));
				String intentVechile=CommonUtils.checkNull(request.getParamValue("intentVechile"));
				String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));
				String buyVechile=CommonUtils.checkNull(request.getParamValue("buyVechile"));
				String buyBudget=CommonUtils.checkNull(request.getParamValue("buyBudget"));
				String buyWay=CommonUtils.checkNull(request.getParamValue("buyWay"));
				String provinceId=CommonUtils.checkNull(request.getParamValue("dPro"));
				String cityId=CommonUtils.checkNull(request.getParamValue("dCity"));
				String townId=CommonUtils.checkNull(request.getParamValue("dArea"));
				String address=CommonUtils.checkNull(request.getParamValue("address"));
				String birthDay=CommonUtils.checkNull(request.getParamValue("birthDay"));
				String degree=CommonUtils.checkNull(request.getParamValue("degree"));
				String interestOne=CommonUtils.checkNull(request.getParamValue("interestOne"));
				String interestTwo=CommonUtils.checkNull(request.getParamValue("interestTwo"));
				String interestThree=CommonUtils.checkNull(request.getParamValue("interestThree"));
				String company=CommonUtils.checkNull(request.getParamValue("company"));
				String job=CommonUtils.checkNull(request.getParamValue("job"));
				String income=CommonUtils.checkNull(request.getParamValue("income"));
				String home=CommonUtils.checkNull(request.getParamValue("home"));
				String gender=CommonUtils.checkNull(request.getParamValue("gender"));
				String political=CommonUtils.checkNull(request.getParamValue("political"));
				String ifMarry=CommonUtils.checkNull(request.getParamValue("ifMarry"));
				String phoneNumber=CommonUtils.checkNull(request.getParamValue("phoneNumber"));
				String indroduceMan=CommonUtils.checkNull(request.getParamValue("indroduceMan"));
				String concern=CommonUtils.checkNull(request.getParamValue("concern"));
				String colorLike=CommonUtils.checkNull(request.getParamValue("colorLike"));
				String userQQ=CommonUtils.checkNull(request.getParamValue("userQQ"));
				String buyUse=CommonUtils.checkNull(request.getParamValue("buyUse"));
				String officeNumber=CommonUtils.checkNull(request.getParamValue("officeNumber"));
				String industry=CommonUtils.checkNull(request.getParamValue("industry"));
				String companyAddress=CommonUtils.checkNull(request.getParamValue("companyAddress"));
				String ctmProp=CommonUtils.checkNull(request.getParamValue("ctmProp"));//客户性质
				String ctmType=CommonUtils.checkNull(request.getParamValue("ctmType"));//客户性质
				String nextTime= CommonUtils.checkNull(request.getParamValue("nextTime"));//下次回访时间
				String intentColor=CommonUtils.checkNull(request.getParamValue("intentcolor"));//意向颜色
				String fitTime= CommonUtils.checkNull(request.getParamValue("fitTime"));//适合时间
				String fitArea=CommonUtils.checkNull(request.getParamValue("fitArea"));//适合地点
				String carFrum=CommonUtils.checkNull(request.getParamValue("carFrum"));//爱车讲堂
				String defeatType= CommonUtils.checkNull(request.getParamValue("defeatType"));//战败类型
				String defeatReason=CommonUtils.checkNull(request.getParamValue("defeatReason"));//战败原因
				String defeatRemark=CommonUtils.checkNull(request.getParamValue("defeatRemark"));//战败备注
				String specialTime=CommonUtils.checkNull(request.getParamValue("specialTime"));//战败原因
				String specialType=CommonUtils.checkNull(request.getParamValue("specialType"));//战败备注
				String orderTime=CommonUtils.checkNull(request.getParamValue("orderTime"));//订车日期
				String preOrderTime=CommonUtils.checkNull(request.getParamValue("preOrderTime"));//预计订车日期
				String delvTime=CommonUtils.checkNull(request.getParamValue("delvTime"));//预计订车日期
				String ctmRemark=CommonUtils.checkNull(request.getParamValue("ctmRemark"));//客户描述
				String imgUrl=CommonUtils.checkNull(request.getParamValue("img_url"));//图片路径
				String salesProgress=CommonUtils.checkNull(request.getParamValue("salesProgress"));//销售流程进度
				String buyType=CommonUtils.checkNull(request.getParamValue("buyType"));//购买类型
				String paperNo=CommonUtils.checkNull(request.getParamValue("paperNo"));//证件号码
				String paperType=CommonUtils.checkNull(request.getParamValue("paperType"));//证件类型
				String ctmId=request.getParamValue("ctmId");
				TPcCustomerPO tcp=new TPcCustomerPO();
				tcp.setCustomerName(customerName);
				tcp.setCustomerCode(SequenceManager.getCtmNo());
				Map<String,String> map=new HashMap<String,String>();
				map.put("paperType", paperType);
				map.put("paperNo", paperNo);
				map.put("ctmRemark", ctmRemark);
				map.put("imgUrl", imgUrl);
				map.put("intentColor", intentColor);
				map.put("jcway",jcway);
				map.put("intentVechile",intentVechile);
				map.put("ctmRank",ctmRank);
				map.put("buyVechile",buyVechile);
				map.put("buyBudget",buyBudget);
				map.put("buyWay",buyWay);
				map.put("provinceId",provinceId);
				map.put("cityId",cityId);
				map.put("townId",townId);
				map.put("address",address);
				map.put("birthDay",birthDay);
				map.put("degree",degree);
				map.put("interestOne",interestOne);
				map.put("interestTwo",interestTwo);
				map.put("interestThree",interestThree);
				map.put("comeReason",comeReason);
				map.put("job",job);
				map.put("income",income);
				map.put("home",home);
				map.put("gender",gender);
				map.put("political",political);
				map.put("ifMarry",ifMarry);
				map.put("phoneNumber",phoneNumber);
				map.put("indroduceMan",indroduceMan);
				map.put("concern",concern);
				map.put("colorLike",colorLike);
				map.put("officeNumber",officeNumber);
				map.put("buyUse",buyUse);
				map.put("userQQ",userQQ);
				map.put("industry",industry);
				map.put("telephone",telephone);
				map.put("ifDriving",ifDriving);
				map.put("company", company);
				map.put("companyAddress",companyAddress);
				map.put("ctmType", ctmType);
				map.put("ctmProp", ctmProp);
				map.put("nextTime", nextTime);
				map.put("fitTime", fitTime);
				map.put("fitArea", fitArea);
				map.put("carFrum", carFrum);
				map.put("defeatType", defeatType);
				map.put("defeatReason", defeatReason);
				map.put("defeatRemark", defeatRemark);
				map.put("specialTime", specialTime);
				map.put("specialType", specialType);
				map.put("orderTime", orderTime);
				map.put("preOrderTime", preOrderTime);
				map.put("delvTime",delvTime);
				map.put("buyType", buyType);
				map.put("salesProgress", salesProgress);
				tcp=setData(map,tcp);
				//tcp.setCreateDate(new Date());
				//tcp.setCreateBy(logonUser.getUserId());
				tcp.setUpdateDate(new Date());
				tcp.setUpdateBy(logonUser.getUserId());
				tcp.setStatus(new Long(Constant.STATUS_ENABLE));
				TPcCustomerPO tcp0=new TPcCustomerPO();
				tcp0.setCustomerId(new Long(ctmId));
				int flag;
				try {
					flag=dao.update(tcp0,tcp);
				} catch (Exception e) {
					flag=0;
				}
				act.setOutData("flag", flag);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 设置页面传入的数据
	 * @param map
	 * @param tcp
	 * @return
	 * @throws Exception
	 */
	public TPcCustomerPO setData(Map<String,String> map,TPcCustomerPO tcp) throws Exception{
		String comeReason=map.get("comeReason");
		String jcway=map.get("jcway");
		String intentVechile=map.get("intentVechile");
		String ctmRank=map.get("ctmRank");
		String buyVechile=map.get("buyVechile");
		String buyBudget=map.get("buyBudget");
		String buyWay=map.get("buyWay");
		String provinceId=map.get("provinceId");
		String cityId=map.get("cityId");
		String townId=map.get("townId");
		String address=map.get("address");
		String birthDay=map.get("birthDay");
		String degree=map.get("degree");
		String interestOne=map.get("interestOne");
		String interestTwo=map.get("interestTwo");
		String interestThree=map.get("interestThree");
		String company=map.get("company");
		String job=map.get("job");
		String income=map.get("income");
		String home=map.get("home");
		String gender=map.get("gender");
		String political=map.get("political");
		String ifMarry=map.get("ifMarry");
		String phoneNumber=map.get("phoneNumber");
		String indroduceMan=map.get("indroduceMan");
		String concern=map.get("concern");
		String colorLike=map.get("colorLike");
		String officeNumber=map.get("officeNumber");
		String buyUse=map.get("buyUse");
		String userQQ=map.get("userQQ");
		String industry=map.get("industry");
		String telephone=map.get("telephone");
		String ifDriving=map.get("ifDriving");
		String companyAddress=map.get("companyAddress");
		String ctmType=map.get("ctmType");
		String ctmProp=map.get("ctmProp");
		String nextTime=map.get("nextTime");
		String intentColor=map.get("intentColor");
		String fitTime=map.get("fitTime");
		String fitArea=map.get("fitArea");
		String carFrum=map.get("carFrum");
		String defeatType=map.get("defeatType");
		String defeatReason=map.get("defeatReason");
		String defeatRemark=map.get("defeatRemark");
		String specialTime=map.get("specialTime");
		String specialType=map.get("specialType");
		String orderTime=map.get("orderTime");
		String preOrderTime=map.get("preOrderTime");
		String delvTime=map.get("delvTime");
		String imgUrl=map.get("imgUrl");
		String ctmRemark=map.get("ctmRemark");
		String buyType=map.get("buyType");
		String salesProgress=map.get("salesProgress");
		String paperType=map.get("paperType");
		String paperNo=map.get("paperNo");
		if(paperNo!=null&&!"".endsWith(paperNo)){
			tcp.setPaperNo(paperNo);
		}
		if(paperType!=null&&!"".endsWith(paperType)){
			tcp.setPaperType(paperType);
		}
		if(salesProgress!=null&&!"".endsWith(salesProgress)){
			tcp.setSalesProgress(salesProgress);
		}
		if(buyType!=null&&!"".endsWith(buyType)){
			tcp.setBuyType(buyType);
		}
		if(imgUrl!=null&&!"".endsWith(imgUrl)){
			tcp.setImgUrl(imgUrl);
		}
		if(ctmRemark!=null&&!"".equals(ctmRemark)){
			tcp.setCtmRemark(ctmRemark);
		}
		if(delvTime!=null&&!"".equals(delvTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setDelvTime(sdf.parse(delvTime));;
		}
		if(orderTime!=null&&!"".equals(orderTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setOrderTime(sdf.parse(orderTime));;
		}
		if(preOrderTime!=null&&!"".equals(preOrderTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setPreOrderTime(sdf.parse(preOrderTime));;
		}
		
		if(specialTime!=null&&!"".equals(specialTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setSpecialTime(sdf.parse(specialTime));;
		}
		if(specialType!=null&&!"".equals(specialType)){
			tcp.setSpecialType(specialType);
		}
		
		if(defeatRemark!=null&&!"".equals(defeatRemark)){
			tcp.setDefeatRemark(defeatRemark);
		}
		if(defeatReason!=null&&!"".equals(defeatReason)){
			tcp.setDefeatReason(defeatReason);
		}
		
		if(defeatType!=null&&!"".equals(defeatType)){
			tcp.setDefeatType(defeatType);
		}
		if(carFrum!=null&&!"".equals(carFrum)){
			tcp.setCarFrum(new Long(carFrum));
		}
		if(fitTime!=null&&!"".equals(fitTime)){
			tcp.setFitTime(fitTime);
		}
		if(fitArea!=null&&!"".equals(fitArea)){
			tcp.setFitArea(fitArea);
		}
		if(nextTime!=null&&!"".equals(nextTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setNextContactTime(sdf.parse(nextTime));
		}
		if(intentColor!=null&&!"".equals(intentColor)){
			tcp.setIntentcolor(intentColor);
		}
		if(ctmProp!=null&&!"".equals(ctmProp)){
			tcp.setCtmProp(ctmProp);
		}
		if(ctmType!=null&&!"".equals(ctmType)){
			tcp.setCtmType(ctmType);
		}
		if(comeReason!=null&&!"".equals(comeReason)){
			tcp.setComeReason(comeReason);
		}
		if(jcway!=null&&!"".equals(jcway)){
			tcp.setJcWay(jcway);
		}
		if(intentVechile!=null&&!"".equals(intentVechile)){
			tcp.setIntentVehicle(intentVechile);
		}
		if(ctmRank!=null&&!"".equals(ctmRank)){
			tcp.setCtmRank(ctmRank);
		}
		if(buyVechile!=null&&!"".equals(buyVechile)){
			tcp.setBuyVehicle(buyVechile);
		}
		if(buyBudget!=null&&!"".equals(buyBudget)){
			tcp.setBuyBudget(buyBudget);
		}
		
		if(buyWay!=null&&!"".equals(buyWay)){
			tcp.setBuyWay(buyWay);
		}
	
		if(provinceId!=null&&!"".equals(provinceId)){
			tcp.setProviceId(provinceId);
		}
		
		if(cityId!=null&&!"".equals(cityId)){
			tcp.setCityId(cityId);
		}
		if(townId!=null&&!"".equals(townId)){
			tcp.setTownId(townId);
		}
		if(address!=null&&!"".equals(address)){
			tcp.setAddress(address);
		}
		if(birthDay!=null&&!"".equals(birthDay)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tcp.setBirthday(sdf.parse(birthDay));
		}
		if(degree!=null&&!"".equals(degree)){
			tcp.setDegree(degree);
		}
		if(interestOne!=null&&!"".equals(interestOne)){
			tcp.setInterestOne(interestOne);
		}
		if(interestTwo!=null&&!"".equals(interestTwo)){
			tcp.setInterestTwo(interestTwo);
		}
		if(interestThree!=null&&!"".equals(interestThree)){
			tcp.setInterestThree(interestThree);
		}
		if(company!=null&&!"".equals(company)){
			tcp.setCompany(company);
		}
		if(job!=null&&!"".equals(job)){
			tcp.setJob(job);
		}
		if(income!=null&&!"".equals(income)){
			tcp.setIncome(income);
		}
		if(home!=null&&!"".equals(home)){
			tcp.setHome(home);
		}
		if(gender!=null&&!"".equals(gender)){
			tcp.setGender(gender);
		}
		if(political!=null&&!"".equals(political)){
			tcp.setPolitical(political);
		}
		if(ifMarry!=null&&!"".equals(ifMarry)){
			tcp.setIfMarry(ifMarry);
		}
		if(phoneNumber!=null&&!"".equals(phoneNumber)){
			tcp.setPhonenumber(phoneNumber);
		}
		if(indroduceMan!=null&&!"".equals(indroduceMan)){
			tcp.setIntroduceMan(indroduceMan);
		}
		if(concern!=null&&!"".equals(concern)){
			tcp.setConcern(concern);
		}
		if(colorLike!=null&&!"".equals(colorLike)){
			tcp.setColorLike(colorLike);
			
		}
		if(userQQ!=null&&!"".equals(userQQ)){
			tcp.setUserQq(userQQ);
		}
		if(buyUse!=null&&!"".equals(buyUse)){
			tcp.setBuyUse(buyUse);
		}
		if(officeNumber!=null&&!"".equals(officeNumber)){
			tcp.setOfficeNumber(officeNumber);
		}
		if(industry!=null&&!"".equals(industry)){
			tcp.setIndustry(industry);
		}
		if(telephone!=null&&!"".equals(telephone)){
			tcp.setTelephone(telephone);
			
		}
		if(ifDriving!=null&&!"".equals(ifDriving)){
			tcp.setIfDrive(new Long(ifDriving));
		}
		if(companyAddress!=null&&!"".equals(companyAddress)){
			tcp.setCompanyAddress(companyAddress);
		}
		
		return tcp;
	}
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toInsureAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			act.setOutData("ctmId", ctmId);
			act.setForword(TO_INSURE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getUserList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long companyId = new Long(logonUser.getCompanyId());
			String userCode = CommonUtils.checkNull(request.getParamValue("userCode"));
			String userName = CommonUtils.checkNull(request.getParamValue("userName"));
			String userId=request.getParamValue("userId");
			Map<String,String> map=new HashMap<String,String>();
			map.put("companyId", companyId.toString());
			map.put("userId", userId);
			map.put("userCode", userCode);
			map.put("userName", userName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCustomerQueryList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void insureAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String enCompany=request.getParamValue("enCompany");
			String enTime=request.getParamValue("enTime");
			String enVar=request.getParamValue("enVar");
			String enMoney=request.getParamValue("enMoney");
			String enRemark=request.getParamValue("enRemark");
			//插入保险数据
			TPcInsurencePO tip=new TPcInsurencePO();
			tip.setInsurenceId(new Long(SequenceManager.getSequence("")));
			if(enCompany!=null&&!"".equals(enCompany)){
				enCompany=enCompany.trim();
			}
			tip.setInsurenceCompany(enCompany);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(enTime!=null&&!"".equals(enTime)){
				tip.setInsurenceDate(sdf.parse(enTime));
			}
			if(enMoney!=null&&!"".equals(enMoney)){
				tip.setInsurenceMoney(new Long(enMoney));
			}
			tip.setInsurenceVar(enVar);
			tip.setRemark(enRemark);
			tip.setCtmId(new Long(ctmId));
			tip.setCreateDate(new Date());
			tip.setCreateBy(logonUser.getUserId());
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tip);
			//查询所有该用户的保险数据
			//加载保险信息
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			List<Map<String,Object>> insureList=dao.insureList(map);
			act.setOutData("insureList", insureList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 保险信息修改初始化
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toInsureUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String insureId=request.getParamValue("insureId");
			act.setOutData("ctmId", ctmId);
			TPcInsurencePO tp=new TPcInsurencePO();
			tp.setInsurenceId(new Long(insureId));
			tp=(TPcInsurencePO) dao.select(tp).get(0);
			SimpleDateFormat ds=new SimpleDateFormat("yyyy-MM-dd");
			String insureDate=ds.format(tp.getInsurenceDate());
			act.setOutData("tp", tp);
			if(tp.getInsurenceCompany()!=null&&!"".equals(tp.getInsurenceCompany())){
				TcCodePO tc=new TcCodePO();
				tc.setCodeId(tp.getInsurenceCompany());
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("company", tc.getCodeDesc());
			}
			act.setOutData("insureDate",insureDate );
			act.setForword(TO_INSURE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION 保险公司信息修该
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void updateInsure() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String enCompany=request.getParamValue("enCompany");
			String enTime=request.getParamValue("enTime");
			String enVar=request.getParamValue("enVar");
			String enMoney=request.getParamValue("enMoney");
			String enRemark=request.getParamValue("enRemark");
			String insureId=request.getParamValue("insureId");
			//插入保险数据
			TPcInsurencePO tip=new TPcInsurencePO();
			tip.setInsurenceCompany(enCompany);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			tip.setInsurenceDate(sdf.parse(enTime));
			tip.setInsurenceMoney(new Long(enMoney));
			tip.setInsurenceVar(enVar);
			tip.setRemark(enRemark);
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			TPcInsurencePO tip0=new TPcInsurencePO();
			tip0.setInsurenceId(new Long(insureId));
			dao.update(tip0,tip);
			//加载保险信息
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			List<Map<String,Object>> insureList=dao.insureList(map);
			act.setOutData("insureList", insureList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION 删除保险公司
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void deleteInsure() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String insureId=request.getParamValue("insureId");
			String rowIndex=request.getParamValue("rowIndex");
			//删除数据
			try {
				TPcInsurencePO tip=new TPcInsurencePO();
				tip.setStatus(new Long(Constant.STATUS_DISABLE));
				TPcInsurencePO tip0=new TPcInsurencePO();
				tip0.setInsurenceId(new Long(insureId));
				dao.update(tip0,tip);
			} catch (Exception e) {
				rowIndex="0";
			}
			act.setOutData("rowIndex", rowIndex);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 装饰装潢初始化start
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toDocAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			act.setOutData("ctmId", ctmId);
			act.setForword(TO_DOC_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toDocUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String docId=request.getParamValue("docId");
			act.setOutData("ctmId", ctmId);
			TPcDecorationPO tp=new TPcDecorationPO();
			tp.setDecorationId(new Long(docId));
			tp=(TPcDecorationPO) dao.select(tp).get(0);
			act.setOutData("tp", tp);
			act.setForword(TO_DOC_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void docAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String exProject=request.getParamValue("exProject");
			String exName=request.getParamValue("exName");
			String exPrice=request.getParamValue("exPrice");
			String exMoney=request.getParamValue("exMoney");
			String amount=request.getParamValue("amount");
			String giveOrBuy=request.getParamValue("giveOrBuy");
			String docId=SequenceManager.getSequence("");
			//插入保险数据
			TPcDecorationPO tip=new TPcDecorationPO();
			tip.setDecorationId(new Long(docId));
			tip.setExproject(exProject);
			tip.setExname(exName);
			tip.setPrice(new Double(exPrice));
			tip.setMoney(new Double(exMoney));
			tip.setGiveorbuy(giveOrBuy);
			tip.setAmount(new Long(amount));
			tip.setCtmId(new Long(ctmId));
			tip.setCreateDate(new Date());
			tip.setCreateBy(logonUser.getUserId());
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tip);
			//加载装饰装潢信息
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			List<Map<String,Object>> docList=dao.docList(map);
			act.setOutData("docList", docList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION 保险公司信息修该
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void deleteDoc() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String docId=request.getParamValue("docId");
			String rowIndex=request.getParamValue("rowIndex");
			//删除数据
			try {
				TPcDecorationPO tip=new TPcDecorationPO();
				tip.setStatus(new Long(Constant.STATUS_DISABLE));
				TPcDecorationPO tip0=new TPcDecorationPO();
				tip0.setDecorationId(new Long(docId));
				dao.update(tip0,tip);
			} catch (Exception e) {
				rowIndex="0";
			}
			act.setOutData("rowIndex", rowIndex);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void docUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String exProject=request.getParamValue("exProject");
			String exName=request.getParamValue("exName");
			String exPrice=request.getParamValue("exPrice");
			String exMoney=request.getParamValue("exMoney");
			String amount=request.getParamValue("amount");
			String giveOrBuy=request.getParamValue("giveOrBuy");
			String docId=request.getParamValue("docId");
			//修改数据
			TPcDecorationPO tip=new TPcDecorationPO();
			tip.setExproject(exProject);
			tip.setExname(exName);
			tip.setPrice(new Double(exPrice));
			tip.setMoney(new Double(exMoney));
			tip.setGiveorbuy(giveOrBuy);
			tip.setAmount(new Long(amount));
			tip.setCtmId(new Long(ctmId));
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			TPcDecorationPO tip0=new TPcDecorationPO();
			tip0.setDecorationId(new Long(docId));
			dao.update(tip0,tip);
			//加载装饰装潢信息
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			List<Map<String,Object>> docList=dao.docList(map);
			act.setOutData("docList", docList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//end
	
	/**
	 * FUNCTION : 联系人start  
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toLinkAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			act.setOutData("ctmId", ctmId);
			act.setForword(TO_LINK_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :转到其他联系人的修改
	 * @throws :
	 *             
	 */
	public void toLinkUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String linkId=request.getParamValue("linkId");
			act.setOutData("ctmId", ctmId);
			TPcLinkManPO tp=new TPcLinkManPO();
			tp.setLinkId(new Long(linkId));
			tp=(TPcLinkManPO) dao.select(tp).get(0);
			if(tp.getCardType()!=null&&!"".equals(tp.getCardType().toString())){
				TcCodePO tc =new TcCodePO();
				tc.setCodeId(tp.getCardType().toString());
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("cardType", tc.getCodeDesc());
			}
			
			act.setOutData("tp", tp);
			act.setForword(TO_LINK_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION :其他联系人的添加
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void linkAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String linkman=request.getParamValue("linkman");
			String linktel=request.getParamValue("linktel");
			String cardType=request.getParamValue("cardType");
			String cardno=request.getParamValue("cardno");
			String relationship=request.getParamValue("relationship");
			String linkId=SequenceManager.getSequence("");
			//插入联系人数据
			TPcLinkManPO tip=new TPcLinkManPO();
			tip.setLinkId(new Long(linkId));
			tip.setLinkMan(linkman);
			tip.setLinkPhone(linktel);
			tip.setCtmId(new Long(ctmId));
			tip.setCardType(new Long(cardType));
			tip.setCardCode(cardno);
			tip.setRelationship(relationship);
			tip.setCreateDate(new Date());
			tip.setCreateBy(logonUser.getUserId());
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tip);
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			//加载其他联系人信息
			List<Map<String,Object>> linkList=dao.linkList(map);
			act.setOutData("linkList", linkList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * FUNCTION 联系人信息修改
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void linkDelete() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String linkId=request.getParamValue("linkId");
			String rowIndex=request.getParamValue("rowIndex");
			//删除数据
			try {
				TPcLinkManPO tip=new TPcLinkManPO();
				tip.setStatus(new Long(Constant.STATUS_DISABLE));
				TPcLinkManPO tip0=new TPcLinkManPO();
				tip0.setLinkId(new Long(linkId));
				dao.update(tip0,tip);
			} catch (Exception e) {
				rowIndex="0";
			}
			act.setOutData("rowIndex", rowIndex);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void linkUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String linkId=request.getParamValue("linkId");
			String linkman=request.getParamValue("linkman");
			String linktel=request.getParamValue("linktel");
			String cardType=request.getParamValue("cardType");
			String cardno=request.getParamValue("cardno");
			String relationship=request.getParamValue("relationship");
			//修改数据
			TPcLinkManPO tip=new TPcLinkManPO();
			tip.setLinkMan(linkman);
			tip.setLinkPhone(linktel);
			tip.setCtmId(new Long(ctmId));
			tip.setCardType(new Long(cardType));
			tip.setCardCode(cardno);
			tip.setRelationship(relationship);
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			tip.setCtmId(new Long(ctmId));
			TPcLinkManPO tip0=new TPcLinkManPO();
			tip0.setLinkId(new Long(linkId));
			dao.update(tip0,tip);
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			//加载其他联系人信息
			List<Map<String,Object>> linkList=dao.linkList(map);
			act.setOutData("linkList", linkList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//end
	/**
	 * FUNCTION : 车辆信息start  
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toVehicleAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			act.setOutData("ctmId", ctmId);
			act.setForword(TO_VEHICLE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * FUNCTION :其他联系人的添加
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void vehicleAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String vin=request.getParamValue("vin");
			String modelCode=request.getParamValue("modelCode");
			String modelName=request.getParamValue("modelName");
			String purDate=request.getParamValue("purDate");
			String color=request.getParamValue("color");
			String enVin=request.getParamValue("enVin");
			String purPrice=request.getParamValue("purPrice");
			String vehicleNo=request.getParamValue("vehicleNo");
			String boardDate=request.getParamValue("boardDate");
			String pin=request.getParamValue("pin");
			String productDate=request.getParamValue("productDate");
			String vehicleId=SequenceManager.getSequence("");
			//插入联系人数据
			TPcVechilePO tip=new TPcVechilePO();
			tip.setCtmId(new Long(ctmId));
			tip.setVechileId(new Long(vehicleId));
			if(vin!=null&&!"".equals(vin)){
				tip.setVin(vin);
			}
			if(modelCode!=null&&!"".equals(modelCode)){
				tip.setModelCode(modelCode);
			}
			if(modelName!=null&&!"".equals(modelName)){
				tip.setModelName(modelName);
			}
			if(purDate!=null&&!"".equals(purDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBuyDate(sdf.parse(purDate));
			}
			if(enVin!=null&&!"".equals(enVin)){
				tip.setLowVin(enVin);
			}
			if(color!=null&&!"".equals(color)){
				tip.setVechileColor(new Long(color));
			}
			
			if(purPrice!=null&&!"".equals(purPrice)){
				tip.setPrice(new Double(purPrice));
			}
			if(vehicleNo!=null&&!"".equals(vehicleNo)){
				tip.setCarNumber(vehicleNo);
			}
			if(boardDate!=null&&!"".equals(boardDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBoardDate(sdf.parse(boardDate));
			}
			if(pin!=null&&!"".equals(pin)){
				tip.setPin(pin);
			}
			if(productDate!=null&&!"".equals(productDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setProductDate(sdf.parse(productDate));
			}
			tip.setCreateDate(new Date());
			tip.setCreateBy(logonUser.getUserId());
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tip);
			//查询所有该用户的联系人
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			//车辆信息
			List<Map<String,Object>> vehicleList=dao.vehicleList(map);
			act.setOutData("vehicleList", vehicleList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * FUNCTION 联系人信息删除
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void vehicleDelete() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicleId=request.getParamValue("vehicleId");
			String rowIndex=request.getParamValue("rowIndex");
			//删除数据
			try {
				TPcVechilePO tip=new TPcVechilePO();
				tip.setStatus(new Long(Constant.STATUS_DISABLE));
				TPcVechilePO tip0=new TPcVechilePO();
				tip0.setVechileId(new Long(vehicleId));
				dao.update(tip0,tip);
			} catch (Exception e) {
				rowIndex="0";
			}
			act.setOutData("rowIndex", rowIndex);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :转到其他联系人的修改
	 * @throws :
	 *             
	 */
	public void toVehicleUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String vehicleId=request.getParamValue("vehicleId");
			act.setOutData("ctmId", ctmId);
			TPcVechilePO tp=new TPcVechilePO();
			tp.setVechileId(new Long(vehicleId));
			tp=(TPcVechilePO) dao.select(tp).get(0);
			String boardDate=null;
			String buyDate =null;
			String productDate=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(tp.getBoardDate()!=null){
				 boardDate=sdf.format(tp.getBoardDate());
			}
			if(tp.getBuyDate()!=null){
				 buyDate =sdf.format(tp.getBuyDate());
			}
			if(tp.getBuyDate()!=null){
				 productDate=sdf.format(tp.getProductDate());
			}
			if(tp.getVechileColor()!=null&&!"".equals(tp.getVechileColor().toString())){
				TcCodePO tc =new TcCodePO();
				tc.setCodeId(tp.getVechileColor().toString());
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("color", tc.getCodeDesc());
			}
			
			act.setOutData("boardDate", boardDate);
			act.setOutData("buyDate", buyDate);
			act.setOutData("productDate", productDate);
			act.setOutData("tp", tp);
			act.setForword(TO_VEHICLE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void vehicleUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String vin=request.getParamValue("vin");
			String modelCode=request.getParamValue("modelCode");
			String modelName=request.getParamValue("modelName");
			String purDate=request.getParamValue("purDate");
			String color=request.getParamValue("color");
			String enVin=request.getParamValue("enVin");
			String purPrice=request.getParamValue("purPrice");
			String vehicleNo=request.getParamValue("vehicleNo");
			String boardDate=request.getParamValue("boardDate");
			String pin=request.getParamValue("pin");
			String productDate=request.getParamValue("productDate");
			String vehicleId=request.getParamValue("vehicleId");
			//插入联系人数据
			TPcVechilePO tip=new TPcVechilePO();
			tip.setCtmId(new Long(ctmId));
			tip.setVechileId(new Long(vehicleId));
			if(vin!=null&&!"".equals(vin)){
				tip.setVin(vin);
			}
			if(modelCode!=null&&!"".equals(modelCode)){
				tip.setModelCode(modelCode);
			}
			if(modelName!=null&&!"".equals(modelName)){
				tip.setModelName(modelName);
			}
			if(purDate!=null&&!"".equals(purDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBuyDate(sdf.parse(purDate));
			}
			if(enVin!=null&&!"".equals(enVin)){
				tip.setLowVin(enVin);
			}
			if(color!=null&&!"".equals(color)){
				tip.setVechileColor(new Long(color));
			}
			
			if(purPrice!=null&&!"".equals(purPrice)){
				tip.setPrice(new Double(purPrice));
			}
			if(vehicleNo!=null&&!"".equals(vehicleNo)){
				tip.setCarNumber(vehicleNo);
			}
			if(boardDate!=null&&!"".equals(boardDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBoardDate(sdf.parse(boardDate));
			}
			if(pin!=null&&!"".equals(pin)){
				tip.setPin(pin);
			}
			if(productDate!=null&&!"".equals(productDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setProductDate(sdf.parse(productDate));
			}
			TPcVechilePO tip0=new TPcVechilePO();
			tip0.setVechileId(new Long(vehicleId));
			dao.update(tip0,tip);
			Map<String,String> map=new HashMap<String,String>();
			map.put("ctmId", ctmId);
			//车辆信息
			List<Map<String,Object>> vehicleList=dao.vehicleList(map);
			act.setOutData("vehicleList", vehicleList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//end
	//end
	/**
	 * FUNCTION :  试乘试驾start  
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toDriveAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			TaskManageDao taskDao = new TaskManageDao();
			//获取意向车型一级列表
			List<DynaBean> menusAList = taskDao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = taskDao.getIntentVehicleB();
			act.setOutData("ctmId", ctmId);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setForword(TO_DRIVE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * FUNCTION :试乘试驾添加
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void driveAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String idNo=request.getParamValue("idNo");
			String driveModel=request.getParamValue("driveModel");
			String driveMan=request.getParamValue("driveMan");
			String driveRoad=request.getParamValue("driveRoad");
			String firstRoad=request.getParamValue("firstRoad");
			String endRoad=request.getParamValue("endRoad");
			String driveDate=request.getParamValue("driveDate");
			String driveId=SequenceManager.getSequence("");
			//插入联系人数据
			TPcDrivingPO tip=new TPcDrivingPO();
			tip.setCtmId(new Long(ctmId));
			tip.setDrivingId(new Long(driveId));
			if(idNo!=null&&!"".equals(idNo)){
				tip.setCardNo(idNo);
			}
			if(driveModel!=null&&!"".equals(driveModel)){
				tip.setDrivingVechile(new Long(driveModel));
			}
			if(driveMan!=null&&!"".equals(driveMan)){
				tip.setDrivingMan(new Long(driveMan));
			}
			if(driveDate!=null&&!"".equals(driveDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setDrivingDate(sdf.parse(driveDate));
			}
			if(driveRoad!=null&&!"".equals(driveRoad)){
				tip.setDrivingRoad(driveRoad);
			}
			if(firstRoad!=null&&!"".equals(firstRoad)){
				tip.setFirstMile(new Long(firstRoad));
			}
			
			if(endRoad!=null&&!"".equals(endRoad)){
				tip.setEndMile(new Long(endRoad));
			}
			tip.setCreateDate(new Date());
			tip.setCreateBy(logonUser.getUserId());
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tip);
			//查询所有时辰试驾信息
			 Map<String, String> map=new HashMap<String, String>();
			 map.put("ctmId", ctmId);
			List<Map<String,Object>> driveList=dao.driveList(map);
			act.setOutData("driveList", driveList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * FUNCTION 联系人信息删除
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void driveDelete() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String driveId=request.getParamValue("driveId");
			String rowIndex=request.getParamValue("rowIndex");
			//删除数据
			try {
				TPcDrivingPO tip=new TPcDrivingPO();
				tip.setStatus(new Long(Constant.STATUS_DISABLE));
				TPcDrivingPO tip0=new TPcDrivingPO();
				tip0.setDrivingId(new Long(driveId));
				dao.update(tip0,tip);
			} catch (Exception e) {
				rowIndex="0";
			}
			act.setOutData("rowIndex", rowIndex);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :转到其他联系人的修改
	 * @throws :
	 *             
	 */
	public void toDriveUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String driveId=request.getParamValue("driveId");
			act.setOutData("ctmId", ctmId);
			TPcDrivingPO tp=new TPcDrivingPO();
			tp.setDrivingId(new Long(driveId));
			tp=(TPcDrivingPO) dao.select(tp).get(0);
			String driveDate ="";
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(tp.getDrivingDate()!=null){
				driveDate=sdf.format(tp.getDrivingDate());
			}
			TaskManageDao taskDao=new TaskManageDao();
			List<DynaBean> menusAList = taskDao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = taskDao.getIntentVehicleB();
			//获取一级意向车型对应二级列表
			List<DynaBean> menusABList = taskDao.getIntentVehicleAB(tp.getDrivingVechile().toString());
			DynaBean db2 = menusABList.get(0);
			String upSeriesCode = db2.get("PARENTID").toString();
			List<DynaBean> menusABList2 = taskDao.getIntentVehicleAB2(upSeriesCode);
			act.setOutData("menusABList2", menusABList2);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("driveDate", driveDate);
			act.setOutData("upSeriesCode", upSeriesCode);
			act.setOutData("tp", tp);
			act.setForword(TO_DRIVE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 试乘试驾修改
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void driveUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmId=request.getParamValue("ctmId");
			String idNo=request.getParamValue("idNo");
			String driveModel=request.getParamValue("driveModel");
			String driveMan=request.getParamValue("driveMan");
			String driveRoad=request.getParamValue("driveRoad");
			String firstRoad=request.getParamValue("firstRoad");
			String endRoad=request.getParamValue("endRoad");
			String driveDate=request.getParamValue("driveDate");
			String driveId=request.getParamValue("driveId");
			//插入联系人数据
			TPcDrivingPO tip=new TPcDrivingPO();
			tip.setCtmId(new Long(ctmId));
			tip.setDrivingId(new Long(driveId));
			if(idNo!=null&&!"".equals(idNo)){
				tip.setCardNo(idNo);
			}
			if(driveModel!=null&&!"".equals(driveModel)){
				tip.setDrivingVechile(new Long(driveModel));
			}
			if(driveMan!=null&&!"".equals(driveMan)){
				tip.setDrivingMan(new Long(driveMan));
			}
			if(driveDate!=null&&!"".equals(driveDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setDrivingDate(sdf.parse(driveDate));
			}
			if(driveRoad!=null&&!"".equals(driveRoad)){
				tip.setDrivingRoad(driveRoad);
			}
			if(firstRoad!=null&&!"".equals(firstRoad)){
				tip.setFirstMile(new Long(firstRoad));
			}
			
			if(endRoad!=null&&!"".equals(endRoad)){
				tip.setEndMile(new Long(endRoad));
			}
			tip.setStatus(new Long(Constant.STATUS_ENABLE));
			TPcDrivingPO tip0=new TPcDrivingPO();
			tip0.setDrivingId(new Long(driveId));
			dao.update(tip0,tip);
			//加载试乘试驾信息
			 Map<String, String> map=new HashMap<String, String>();
			 map.put("ctmId", ctmId);
			List<Map<String,Object>> driveList=dao.driveList(map);
			act.setOutData("driveList", driveList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//end
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toIntentVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String textId=request.getParamValue("textId");
			String textName=request.getParamValue("textName");
			act.getSession().get(Constant.LOGON_USER);
			act.setOutData("textId", textId);
			act.setOutData("textName", textName);
			act.setForword(toIntentListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getIntentVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String seriesCode = CommonUtils.checkNull(request.getParamValue("seriesCode"));
			String seriesName = CommonUtils.checkNull(request.getParamValue("seriesName"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("seriesCode", seriesCode);
			map.put("seriesName", seriesName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getIntentVechileList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 保存竞品
	 */
	public void saveCompet() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String ctmId=CommonUtils.checkNull(request.getParamValue("ctmId"));
			String competVechile=CommonUtils.checkNull(request.getParamValue("competVechile"));
			String competReason=CommonUtils.checkNull(request.getParamValue("competReason"));
			String otherProduct=CommonUtils.checkNull(request.getParamValue("otherProduct"));
			TPcCustomerPO tcp=new TPcCustomerPO();
			tcp.setCustomerId(new Long(ctmId));
			TPcCustomerPO tcp1=new TPcCustomerPO();
			if(competVechile!=null&&!"".equals(competVechile)){
				tcp1.setCompetVechile(competVechile.trim());
			}
			if(competReason!=null&&!"".equals(competReason)){
				tcp1.setCompetReason(competReason.trim());			
			}
			if(otherProduct!=null&&!"".equals(otherProduct)){
				tcp1.setOtherProduct(otherProduct.trim());
			}
			int i=dao.update(tcp, tcp1);
			act.setOutData("flag", i);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toCompetVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String level=request.getParamValue("level");
			act.setOutData("competLevel", level);
			act.setForword(toCompetListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:获取竞品车型
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getCompetVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String competCode = CommonUtils.checkNull(request.getParamValue("competCode"));
			String competName = CommonUtils.checkNull(request.getParamValue("competName"));
			String competLevel=CommonUtils.checkNull(request.getParamValue("competLevel"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("competCode", competCode);
			map.put("competName", competName);
			map.put("competLevel", competLevel);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCompetVechileList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
