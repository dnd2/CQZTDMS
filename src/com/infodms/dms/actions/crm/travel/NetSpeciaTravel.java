package com.infodms.dms.actions.crm.travel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.dao.sales.customerInfoManage.VehicleTestDriveDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.NetSpeciaStrokeOperatPO;
import com.infodms.dms.po.NetSpeciaStrokeOperatRelaPO;
import com.infodms.dms.po.NetSpeciaStrokePO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TtDealerActualSalesAuditPO;
import com.infodms.dms.po.TtVehicleTestDriveInfoPO;
import com.infodms.dms.po.TtVsSalesSituationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class NetSpeciaTravel extends BaseDao{
	public Logger logger = Logger.getLogger(NetSpeciaTravel.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static NetSpeciaTravelDao dao = new NetSpeciaTravelDao();

	public static final NetSpeciaTravelDao getInstance() {
		return dao;
	}
	
	private final String InitUrl = "/jsp/crm/taskmanage/NetSpeciaTravelInit.jsp";
	private final String AddInitUrl = "/jsp/crm/taskmanage/NetSpeciaTravelAdd.jsp";
	private final String InfoInitUrl = "/jsp/crm/taskmanage/NetSpeciaTravelInfo.jsp";
	private final String UpdateInitUrl = "/jsp/crm/taskmanage/NetSpeciaTravelUpdate.jsp";
	private final String queryVehicleTestDrive = "/jsp/sales/customerInfoManage/vehicleTestDriveQuery.jsp";
	
	
	//试乘试驾页面初始化 2013-2-4
	public void doInit(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        String today=sdf.format(new Date());
	        act.setOutData("today", today);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
			String dealerIds = logonUser.getDealerId();
			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
			act.setOutData("dealerList", dealerList);
			act.setForword(InitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾页面 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//网络日志初始化页面
	public void doAddInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(AddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾添加页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	//网络日志初始化页面
	public void doUpdateInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			netSpeciaQuery();
			netSpeciaQueryInfo();
			act.setForword(UpdateInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾添加页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}

	
	
	public void netSpeciaQuery(){
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);
		System.out.println("用户角色："+logonUser.getPoseId());
		String poseId=logonUser.getPoseId().toString();
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			PageResult<Map<String, Object>> ps =null;
			if(poseId=="4000010441" || "4000010441".equals(poseId)){
			ps = dao.getSubmitNetSpeciaList(logonUser.getUserId().toString(), startDate, endDate, curPage, Constant.PAGE_SIZE);	
			}else{
			 ps = dao.getNetSpeciaList(logonUser.getUserId().toString(), startDate, endDate, curPage, Constant.PAGE_SIZE);
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商销售当日情况");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void netSpeciaQueryInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String speciaId = CommonUtils.checkNull(request.getParamValue("speciaId"));

			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(speciaId));
			List<FsFileuploadPO> lists =dao.select(detail);
			act.setOutData("lists",lists);
			
			List<Map<String,Object>> list = dao.getNetSpeciaQueryInfoList(speciaId);
			act.setOutData("list", list);
			act.setOutData("speciaId", speciaId);
			
			List<Map<String,Object>> operatlist = dao.getNetOperatQueryInfoList(speciaId);
			act.setOutData("operatlist", operatlist);
			System.out.println("operat_Id+++++++++++++++++++++++++++++++++++:");
			//if(operatlist.size()>0) {
			//	DynaBean db = (DynaBean) operatlist.get(0);
			//	String operat_Id = db.get("OPERAT_ID").toString();
			//	System.out.println("operat_Id:"+operat_Id);
			
			//}
			List<Map<String,Object>> linklist = dao.getLinkQueryInfoList(speciaId);
			act.setOutData("linklist", linklist);
		
		
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(InfoInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void netSpeciaDel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String speciaId = CommonUtils.checkNull(request.getParamValue("speciaId"));
			NetSpeciaStrokePO po = new NetSpeciaStrokePO();
			po.setSpeciaId(speciaId);

			NetSpeciaStrokePO pov = new NetSpeciaStrokePO();
			pov.setStatus(Constant.STATUS_DISABLE.toString());
			dao.update(po, pov);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void netSpeciaSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String speciaId = CommonUtils.checkNull(request.getParamValue("speciaId"));
			NetSpeciaStrokePO po = new NetSpeciaStrokePO();
			po.setSpeciaId(speciaId);
			
			NetSpeciaStrokePO pov = new NetSpeciaStrokePO();
			pov.setSubmitStatus(Constant.FORECAST_STATUS_CONFIRM.toString());
			dao.update(po, pov);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//试乘试驾新增信息(经销商) 2013-2-5
	@SuppressWarnings("unchecked")
	public void doUpdateSave() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		String userId=logonUser.getUserId().toString();
		System.out.println("userId:"+userId);
		System.out.println("dealerCode:"+dealerCode);
		
		try{
			String speciaId = CommonUtils.checkNull(request.getParamValue("speciaId")); 
			System.out.println("+++++++++++++:"+speciaId);
			String province = CommonUtils.checkNull(request.getParamValue("province"));  //省份
			System.out.println("+++++++++++++:"+province);
		
			String mubiao = CommonUtils.checkNull(request.getParamValue("mubiao")); //目标市场
			String drivedate = CommonUtils.checkNull(request.getParamValue("drivedate")); //出差日期
			String drivename = CommonUtils.checkNull(request.getParamValue("drivename"));//出差人
			String drivemd = CommonUtils.checkNull(request.getParamValue("drivemd"));//出差目的
			String amwork = CommonUtils.checkNull(request.getParamValue("amwork"));//上午工作
			String pmwork = CommonUtils.checkNull(request.getParamValue("pmwork"));//下午工作
			String operatItem = CommonUtils.checkNull(request.getParamValue("operatItem"));//作业项目
			String operatWay = CommonUtils.checkNull(request.getParamValue("operatWay"));//作业方式
			System.out.println("operatItem:"+operatItem+"operatWay:"+operatWay);
			
			//int sCount= Integer.parseInt(snum);
			//System.out.println("sCount:"+sCount);
			//处理出差日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date drivedate1=df.parse(drivedate);
		
			
			NetSpeciaStrokePO oldDip = new NetSpeciaStrokePO();
			oldDip.setSpeciaId(speciaId);
			NetSpeciaStrokePO newDip = new NetSpeciaStrokePO();
			newDip.setSpeciaId(speciaId);
	
			newDip.setProvinceName(province);
			newDip.setCityName(mubiao);
			newDip.setBusinessDate(drivedate1);
			newDip.setBusinessTraveller(drivename);
			newDip.setBusinessGoal(drivemd);
			newDip.setBusinessJobam(amwork);
			newDip.setBusinessJobpm(pmwork);
			newDip.setOperatItemId(operatItem);
			newDip.setOperatWayId(operatWay);
			newDip.setCreateBy(userId);
			newDip.setStatus(Constant.STATUS_ENABLE.toString());//日志状态
			newDip.setSubmitStatus(Constant.FORECAST_STATUS_UNCONFIRM.toString());//未提报
			dao.update(oldDip, newDip);
			
	
			//建立日志信息和附件之间的关系
			String[] fids = request.getParamValues("fjid");//获取文件ID
			String tflag=Integer.toString(fids.length);
			
			FileUpLoadDAO fdaos = new FileUpLoadDAO();
			List<DynaBean> cList=fdaos.getNetCheckFile(speciaId);
			DynaBean cdb = cList.get(0);
			String cNum = cdb.get("CNUM").toString();
			//判断附件信息是否做修改,做修改
			if (tflag!=cNum && !tflag.equals(cNum)) {
				//更新之前所有的附件信息关联信息为空
			    fdaos.updateNetFile(speciaId.toString(),logonUser);
			    
				String[] fjids = request.getParamValues("fjid");//获取文件ID
				String fId=null;
				String fileYwzj=null;
				if(fjids!=null&&fjids.length>0){
					for(int i=0;i<fjids.length;i++){
						if(fjids[i]!=null||!"".equals(fjids[i])){
							fId=fjids[i];
							
						}
							//修改修改之后附件信息的关联关系！
							fdaos.updateRelationFile(speciaId.toString(),fId,logonUser);
					
						}
					}
			}
		
			
			for(int s=1;s<=5;s++){
				String operatId = CommonUtils.checkNull(request.getParamValue("operatId"+s));
				System.out.println("operatId:"+operatId);
				String scompanyname = CommonUtils.checkNull(request.getParamValue("companyname"+s));//拜访公司名称
				System.out.println("scompanyname:"+scompanyname);
				String sintent = CommonUtils.checkNull(request.getParamValue("intent"+s));//意向等级
				String scompanyxz = CommonUtils.checkNull(request.getParamValue("companyxz"+s));//公司性质
				String sshanquan = CommonUtils.checkNull(request.getParamValue("shanquan"+s));//所在商圈
				String scarbuesess = CommonUtils.checkNull(request.getParamValue("carbuesess"+s));//现有汽车品牌
			
				String scompanyaddress = CommonUtils.checkNull(request.getParamValue("companyaddress"+s));//公司地址
				String sdealertalk = CommonUtils.checkNull(request.getParamValue("dealertalk"+s));//经销商洽谈情况
				String snext_follow_date = CommonUtils.checkNull(request.getParamValue("next_follow_date"+s));//下次跟进时间
				
				String sfollow_type = CommonUtils.checkNull(request.getParamValue("follow_type"+s));//跟进方式
				String scontext = CommonUtils.checkNull(request.getParamValue("context"+s));//谈判内容
				Date snext_follow_date1=null;
				String vistId =null;
				String svistname =null;
				String svistzw = null;
				String svistphone =null;
				if(operatId!=null && !operatId.equals("")){
				//处理下次跟进日期
				if(snext_follow_date!=null && !"".equals(snext_follow_date)){
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			      sdf1.parse(snext_follow_date);
				}
				//String soperatId = SequenceManager.getSequence("");//本次任务ID
			    NetSpeciaStrokeOperatPO oldNsop = new NetSpeciaStrokeOperatPO();
				oldNsop.setOperatId(operatId);
				NetSpeciaStrokeOperatPO newNsop = new NetSpeciaStrokeOperatPO();
				newNsop.setOperatId(operatId);
				
				newNsop.setSpeciaId(speciaId);
				newNsop.setCompanyName(scompanyname);
				newNsop.setIntentLevel(sintent);
				newNsop.setCompanyNature(scompanyxz);
				newNsop.setLocationCircle(sshanquan);
				newNsop.setCarBrand(scarbuesess);
				newNsop.setCompanyAddress(scompanyaddress);
				newNsop.setDealerNegotiation(sdealertalk);
				newNsop.setFollowDate(snext_follow_date1);
				newNsop.setFollowWay(sfollow_type);
				newNsop.setNegotiatContent(scontext);
				newNsop.setCreateBy(userId);
				dao.update(oldNsop, newNsop);
		
			for(int i=1;i<=2;i++){
				
				if(s==1){	
					vistId = CommonUtils.checkNull(request.getParamValue("vistId"+i));//受访人id
					 svistname = CommonUtils.checkNull(request.getParamValue("vistname"+i));//受访人姓名
					 svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+i));//职务
					 svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+i));//联系电话
				}
				else if(s==2){
					 vistId = CommonUtils.checkNull(request.getParamValue("vistId"+(2+i)));//受访人id
					 svistname = CommonUtils.checkNull(request.getParamValue("vistname"+(2+i)));//受访人姓名
					 svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+(2+i)));//职务
					 svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+(2+i)));//联系电话				
				}
				else if(s==3){
					 vistId = CommonUtils.checkNull(request.getParamValue("vistId"+(4+i)));//受访人id
					 svistname = CommonUtils.checkNull(request.getParamValue("vistname"+(4+i)));//受访人姓名
					 svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+(4+i)));//职务
					 svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+(4+i)));//联系电话				
					
				}
				else if(s==4){
					 vistId = CommonUtils.checkNull(request.getParamValue("vistId"+(6+i)));//受访人id
					 svistname = CommonUtils.checkNull(request.getParamValue("vistname"+(6+i)));//受访人姓名
					 svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+(6+i)));//职务
					 svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+(6+i)));//联系电话				
					
				}
				else if(s==5){
					 vistId = CommonUtils.checkNull(request.getParamValue("vistId"+(8+i)));//受访人id
					 svistname = CommonUtils.checkNull(request.getParamValue("vistname"+(8+i)));//受访人姓名
					 svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+(8+i)));//职务
					 svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+(8+i)));//联系电话				
					
				}
			
				NetSpeciaStrokeOperatRelaPO oldNsrp = new NetSpeciaStrokeOperatRelaPO();
				oldNsrp.setLinkmanId(vistId);
				NetSpeciaStrokeOperatRelaPO newNsrp = new NetSpeciaStrokeOperatRelaPO();
				newNsrp.setLinkmanId(vistId);
		
				newNsrp.setOperatId(operatId);
				newNsrp.setCreateBy(userId);
				newNsrp.setLinkmanName(svistname);
				newNsrp.setLinkmanJob(svistzw);
				newNsrp.setLinkmanWay(svistphone);
			
				dao.update(oldNsrp, newNsrp);
		
				
			}
				}
			
			}
			
			//throw new Exception("该条数据已被处理过！");
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"试乘试驾客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	//试乘试驾新增信息(经销商) 2013-2-5
	@SuppressWarnings("unchecked")
	public void doSave() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		String userId=logonUser.getUserId().toString();
		System.out.println("userId:"+userId);
		System.out.println("dealerCode:"+dealerCode);
		
		try{
			String province = CommonUtils.checkNull(request.getParamValue("province"));  //省份
			System.out.println("+++++++++++++:"+province);
		
			String mubiao = CommonUtils.checkNull(request.getParamValue("mubiao")); //目标市场
			String drivedate = CommonUtils.checkNull(request.getParamValue("drivedate")); //出差日期
			String drivename = CommonUtils.checkNull(request.getParamValue("drivename"));//出差人
			String drivemd = CommonUtils.checkNull(request.getParamValue("drivemd"));//出差目的
			String amwork = CommonUtils.checkNull(request.getParamValue("amwork"));//上午工作
			String pmwork = CommonUtils.checkNull(request.getParamValue("pmwork"));//下午工作
			String operatItem = CommonUtils.checkNull(request.getParamValue("operatItem"));//作业项目
			String operatWay = CommonUtils.checkNull(request.getParamValue("operatWay"));//作业方式
			System.out.println("operatItem:"+operatItem+"operatWay:"+operatWay);
			
			String companyname = CommonUtils.checkNull(request.getParamValue("companyname"));//拜访公司名称
			String intent = CommonUtils.checkNull(request.getParamValue("intent"));//意向等级
			String companyxz = CommonUtils.checkNull(request.getParamValue("companyxz"));//公司性质
			String shanquan = CommonUtils.checkNull(request.getParamValue("shanquan"));//所在商圈
			String carbuesess = CommonUtils.checkNull(request.getParamValue("carbuesess"));//现有汽车品牌
			String vistname = CommonUtils.checkNull(request.getParamValue("vistname"));//受访人姓名
			String vistzw = CommonUtils.checkNull(request.getParamValue("vistzw"));//职务
			String vistphone = CommonUtils.checkNull(request.getParamValue("vistphone"));//联系电话
			
			String vistnameone = CommonUtils.checkNull(request.getParamValue("vistnameone"));//受访人姓名1
			String vistzwone = CommonUtils.checkNull(request.getParamValue("vistzwone"));//职务1
			String vistphoneone = CommonUtils.checkNull(request.getParamValue("vistphoneone"));//联系电话1
			
			String companyaddress = CommonUtils.checkNull(request.getParamValue("companyaddress"));//公司地址
			String dealertalk = CommonUtils.checkNull(request.getParamValue("dealertalk"));//经销商洽谈情况
			String next_follow_date = CommonUtils.checkNull(request.getParamValue("next_follow_date"));//下次跟进时间
			
			String follow_type = CommonUtils.checkNull(request.getParamValue("follow_type"));//跟进方式
			String context = CommonUtils.checkNull(request.getParamValue("context"));//谈判内容
			
			String snum = CommonUtils.checkNull(request.getParamValue("snum"));//作业实施情况第几次
			
			int sCount= Integer.parseInt(snum);
			System.out.println("sCount:"+sCount);
			System.out.println("next_follow_date:"+next_follow_date);
			System.out.println("vistname:"+vistname);
			//处理出差日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date drivedate1=df.parse(drivedate);
			//处理下次跟进日期
			Date next_follow_date1=null;
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			if(next_follow_date!=null && !"".equals(next_follow_date)){
			 next_follow_date1=df1.parse(next_follow_date);
			}
			NetSpeciaStrokePO dip = new NetSpeciaStrokePO();
			//driveInfoId = Utility.getLong(SequenceManager.getSequence(""));
			String speciaId = SequenceManager.getSequence("");//本次任务ID
			
			dip.setSpeciaId(speciaId);
			dip.setProvinceName(province);
			dip.setCityName(mubiao);
			dip.setBusinessDate(drivedate1);
			dip.setCreateDate(new Date());
			dip.setBusinessTraveller(drivename);
			dip.setBusinessGoal(drivemd);
			dip.setBusinessJobam(amwork);
			dip.setBusinessJobpm(pmwork);
			dip.setOperatItemId(operatItem);
			dip.setOperatWayId(operatWay);
			dip.setCreateBy(userId);
			dip.setStatus(Constant.STATUS_ENABLE.toString());//日志状态
			dip.setSubmitStatus(Constant.FORECAST_STATUS_UNCONFIRM.toString());//未提报
			dao.insert(dip);
			
			//建立日志信息和附件之间的关系
			String ywzj=speciaId.toString();
			String[] fjids = request.getParamValues("fjid");//获取文件ID
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
			
			
			NetSpeciaStrokeOperatPO nsop = new NetSpeciaStrokeOperatPO();
			String operatId = SequenceManager.getSequence("");//本次任务ID
			nsop.setOperatId(operatId);
			nsop.setSpeciaId(speciaId);
			nsop.setCompanyName(companyname);
			nsop.setIntentLevel(intent);
			nsop.setCompanyNature(companyxz);
			nsop.setLocationCircle(shanquan);
			nsop.setCarBrand(carbuesess);
			nsop.setCompanyAddress(companyaddress);
			nsop.setDealerNegotiation(dealertalk);
			nsop.setCreateDate(new Date());
			nsop.setFollowDate(next_follow_date1);
			nsop.setFollowWay(follow_type);
			nsop.setNegotiatContent(context);
			nsop.setCreateBy(userId);
			dao.insert(nsop);
			
			for(int i=0;i<2;i++){
				NetSpeciaStrokeOperatRelaPO nsrp = new NetSpeciaStrokeOperatRelaPO();
				String linkId = SequenceManager.getSequence("");//本次任务ID
				nsrp.setLinkmanId(linkId);
				nsrp.setOperatId(operatId);
				nsrp.setCreateDate(new Date());
				nsrp.setCreateBy(userId);
				nsrp.setLinkmanName(vistname);
				nsrp.setLinkmanJob(vistzw);
				nsrp.setLinkmanWay(vistphone);
				if(i==1){
					nsrp.setLinkmanName(vistnameone);
					nsrp.setLinkmanJob(vistzwone);
					nsrp.setLinkmanWay(vistphoneone);
				}
				dao.insert(nsrp);
			}
			
			for(int s=0;s<sCount;s++){
				String scompanyname = CommonUtils.checkNull(request.getParamValue("companyname"+s));//拜访公司名称
				System.out.println("scompanyname:"+scompanyname);
				String sintent = CommonUtils.checkNull(request.getParamValue("intent"+s));//意向等级
				String scompanyxz = CommonUtils.checkNull(request.getParamValue("companyxz"+s));//公司性质
				String sshanquan = CommonUtils.checkNull(request.getParamValue("shanquan"+s));//所在商圈
				String scarbuesess = CommonUtils.checkNull(request.getParamValue("carbuesess"+s));//现有汽车品牌
				String svistname = CommonUtils.checkNull(request.getParamValue("vistname"+s));//受访人姓名
				String svistzw = CommonUtils.checkNull(request.getParamValue("vistzw"+s));//职务
				String svistphone = CommonUtils.checkNull(request.getParamValue("vistphone"+s));//联系电话
				System.out.println("受访人姓名svistname:"+svistname);
				String svistnameone = CommonUtils.checkNull(request.getParamValue("vistnameone"+s));//受访人姓名1
				String svistzwone = CommonUtils.checkNull(request.getParamValue("vistzwone"+s));//职务1
				String svistphoneone = CommonUtils.checkNull(request.getParamValue("vistphoneone"+s));//联系电话1
				System.out.println("受访人姓名vistnameone:"+svistnameone);
				String scompanyaddress = CommonUtils.checkNull(request.getParamValue("companyaddress"+s));//公司地址
				String sdealertalk = CommonUtils.checkNull(request.getParamValue("dealertalk"+s));//经销商洽谈情况
				String snext_follow_date = CommonUtils.checkNull(request.getParamValue("next_follow_date"+s));//下次跟进时间
				
				String sfollow_type = CommonUtils.checkNull(request.getParamValue("follow_type"+s));//跟进方式
				String scontext = CommonUtils.checkNull(request.getParamValue("context"+s));//谈判内容
				Date snext_follow_date1=null;
				//处理下次跟进日期
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				if(snext_follow_date!=null && !"".equals(snext_follow_date)){
				snext_follow_date1=sdf1.parse(snext_follow_date);
				}
				
				String soperatId = SequenceManager.getSequence("");//本次任务ID
				nsop.setOperatId(soperatId);
				nsop.setSpeciaId(speciaId);
				nsop.setCompanyName(scompanyname);
				nsop.setIntentLevel(sintent);
				nsop.setCompanyNature(scompanyxz);
				nsop.setLocationCircle(sshanquan);
				nsop.setCarBrand(scarbuesess);
				nsop.setCompanyAddress(scompanyaddress);
				nsop.setDealerNegotiation(sdealertalk);
				nsop.setCreateDate(new Date());
				nsop.setFollowDate(snext_follow_date1);
				nsop.setFollowWay(sfollow_type);
				nsop.setNegotiatContent(scontext);
				nsop.setCreateBy(userId);
				dao.insert(nsop);
				
				for(int j=0;j<2;j++){
					NetSpeciaStrokeOperatRelaPO snsrp = new NetSpeciaStrokeOperatRelaPO();
					String slinkId = SequenceManager.getSequence("");//本次任务ID
					snsrp.setLinkmanId(slinkId);
					snsrp.setOperatId(soperatId);
					snsrp.setCreateDate(new Date());
					snsrp.setCreateBy(userId);
					snsrp.setLinkmanName(svistname);
					snsrp.setLinkmanJob(svistzw);
					snsrp.setLinkmanWay(svistphone);
					if(j==1){
						snsrp.setLinkmanName(svistnameone);
						snsrp.setLinkmanJob(svistzwone);
						snsrp.setLinkmanWay(svistphoneone);
					}
					dao.insert(snsrp);
					
				
				
			}
			
			
			
			}
			
			act.setOutData("flag", true);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"试乘试驾客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
 
	
}
