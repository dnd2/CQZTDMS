package com.infodms.dms.actions.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TrPoseBinsPO;
import com.infodms.dms.po.TrPoseDealerPO;
import com.infodms.dms.po.TrPoseRegionPO;
import com.infodms.dms.po.TrRolePosePO;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiDealerRelationPO;
import com.infodms.dms.po.TtSalesLogiIntrevalPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class LogisticsManage  extends BaseDao<PO>{
	/**
	 * 
	 * @ClassName     : reservoirRegionManage 
	 * @Description   : 物流商管理控制类 
	 * @author        : ranjian
	 * CreateDate     : 2013-4-7
	 */
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final LogisticsDao reDao = LogisticsDao.getInstance();
	private final String logisticsInitUrl = "/jsp/sales/storage/storagebase/logistics/logisticsList.jsp";
	private final String addLogisticsUrl = "/jsp/sales/storage/storagebase/logistics/addLogistics.jsp";
	private final String editLogisticsUrl = "/jsp/sales/storage/storagebase/logistics/updateLogistics.jsp";
	private final String logiIntrevalEdit = "/jsp/sales/storage/storagebase/logistics/logiIntrevalEdit.jsp";
												

	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(logisticsInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"物流商管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsQuery() {
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String logiCode = request.getParamValue("LOGI_CODE"); // 物流商代码
			String logiFullName = request.getParamValue("LOGI_FULL_NAME"); //物流商名称
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String status = request.getParamValue("STATUS"); // 状态
			String conPer = request.getParamValue("CON_PER");// 联系人
			String conTel = request.getParamValue("CON_TEL");// 联系电话

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("LOGI_CODE", logiCode);
			map.put("LOGI_FULL_NAME", logiFullName);
			map.put("YIELDLY", yieldly);
			map.put("STATUS", status);
			map.put("CON_PER", conPer);
			map.put("CON_TEL", conTel);
			map.put("poseId", logonUser.getPoseId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getLogisticsQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商管理新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void addLogisticsInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setOutData("poseType", Constant.SYS_USER_SGM);//职位类别为车厂
			act.setOutData("poseBusType", Constant.POSE_BUS_TYPE_WL);//职位类型为储运物流
			act.setOutData("POSECODE", getPoseCode());
			List<Map<String, Object>> proviceList = reDao.getProvinceList("");
			act.setOutData("proviceList", proviceList);//省份列表
			act.setForword(addLogisticsUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"物流商管理新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	private String getPoseCode(){
		String roleCode ="";
		String sql="SELECT TO_CHAR(count(*)+1,'00009') as POSECODE FROM TC_POSE  T WHERE T.POSE_CODE LIKE 'P%' ";
		 Map<String, Object> result= super.pageQueryMap(sql, null, super.getFunName()) ;
			 roleCode = "P"+ result.get("POSECODE").toString().trim();
		return roleCode;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商管理修改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void editLogisticsInit(){ 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String id = request.getParamValue("Id");
			Map<String, Object> complaintMap = reDao.getSalesLogiMsg(id);
			TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
			tslap.setLogiId(Long.parseLong(id));
			List<TtSalesLogiAreaPO> listMata= reDao.getLogisticsMata(tslap);
			String disIds="";
			if(listMata.size()>0){
				for(int i=0;i<listMata.size();i++){
					TtSalesLogiAreaPO tsale=(TtSalesLogiAreaPO)listMata.get(i);
					disIds+=tsale.getDisId()+",";
				}
				disIds=disIds.substring(0,disIds.length()-1);//去掉最后一个逗号
			}
			
			act.setOutData("complaintMap", complaintMap);	
			act.setOutData("parmlog", disIds);	
			//获取职位信息
			String poseId = request.getParamValue("poseId"); // 查看的职位ID
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(new Long(poseId));
			List<TcPosePO> poseList = factory.select(posePO); // 通过ID得到职位对象
			posePO = poseList.get(0);

			String gjPose = "";
			Long companyId = logonUser.getCompanyId();
			TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
			List<TmBusinessAreaPO> areaList = SysPositionDAO
					.findPoseArea(poseId);
			act.setOutData("relList", areaList);
			// modify by xiayanpeng end
			if (posePO.getPoseType().equals(Constant.SYS_USER_DEALER)) {
				TmDealerPO dealerPo = new TmDealerPO();
				dealerPo.setDealerId(posePO.getCompanyId());//经销商端职位公司ID现在存为经销商ID
				TmDealerPO dealerPo_ = factory.select(dealerPo).get(0);
				act.setOutData("COMPANY_NAME", dealerPo_.getDealerName());
				act.setOutData("COMPANY_ID", dealerPo_.getDealerId());

			} else {
				Integer chooseDealer = posePO.getChooseDealer();
				if (Constant.IF_TYPE_YES.toString().equals(
						chooseDealer.toString())) {
					TrPoseDealerPO tpo = new TrPoseDealerPO();
					tpo.setPoseId(posePO.getPoseId());
					List tpo_list = factory.select(tpo);
					List<TmDealerPO> d_list = new ArrayList<TmDealerPO>();
					if (tpo_list != null && tpo_list.size() > 0) {
						for (int k = 0; k < tpo_list.size(); k++) {
							TrPoseDealerPO tpo_ = (TrPoseDealerPO) tpo_list
									.get(k);
							TmDealerPO dpo = new TmDealerPO();
							dpo.setDealerId(tpo_.getDealerId());
							List dpo_list = factory.select(dpo);
							for (int j = 0; j < dpo_list.size(); j++) {
								TmDealerPO dpo_ = (TmDealerPO) dpo_list.get(j);
								d_list.add(dpo_);
							}
						}
						act.setOutData("d_list", d_list);
					}
				}else{
					//根据经销商获取选中的省份
					List<TrPoseRegionPO> checkProviceList = CommonUtils
					.findCeckProvicePO(posePO.getPoseId());
					act.setOutData("checkProviceList", checkProviceList);//选中的省份列表
					List<TmRegionPO> checkCityList = CommonUtils.findCeckCityPO(posePO.getPoseId());
					act.setOutData("checkCityList", checkCityList);
					
				}
				List<Map<String, Object>> proviceList = reDao.getProvinceList(id);
				act.setOutData("proviceList", proviceList);//所有省份列表
				TmOrgPO orgPO = new TmOrgPO();
				orgPO.setOrgId(posePO.getOrgId());
				orgPO = factory.select(orgPO).get(0);
				act.setOutData("orgName", orgPO.getOrgName());
			}
			List<TmBusinessAreaPO> brandList = CommonUtils
					.findAllBusinessAreaPO(companyId);
			SysPositionDAO dao = new SysPositionDAO();
			List list = dao.getLogiList();
			act.setOutData("logiList", list);
			act.setOutData("brandList", brandList);
			act.setOutData("vpose", posePO);
			act.setOutData("gjzw", gjPose);
			act.setOutData("poseId", poseId);
			act.setOutData("poseBusType", Constant.POSE_BUS_TYPE_WL);//职位类型为储运物流
			//获取所有省份 待完善
			List<Map<String,Object>> rlist=reDao.getTmRegionLevel2();
			act.setOutData("rlist", rlist);
			act.setForword(editLogisticsUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理修改信息初始化");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	//根据省份ID获取市 待完善
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 里程信息列表显示
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void cityMileageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
			String disIds = CommonUtils.checkNull(request.getParamValue("disIds_")); // 
			String disIds_o = CommonUtils.checkNull(request.getParamValue("disIds_o")); // 显示页面传回来的里程IDS
			String isShow = CommonUtils.checkNull(request.getParamValue("isShow")); // 是否是显示页面返回
			
			String provinceSeach = CommonUtils.checkNull(request.getParamValue("PROVINCE_SEACH"));// 省
			String citySeach = CommonUtils.checkNull(request.getParamValue("CITY_SEACH")); // 市
			String countySeach = CommonUtils.checkNull(request.getParamValue("COUNTY_SEACH"));// 地区	
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("YIELDLY", yieldly);
			map.put("PROVINCE", provinceId);
			map.put("disIds", disIds);//里程IDS
			map.put("disIds_o", disIds_o);//显示页面传回来的里程IDS
			map.put("isShow", isShow);//是否是显示页面返回
			map.put("provinceSeach", provinceSeach);
			map.put("citySeach", citySeach);
			map.put("countySeach", countySeach);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getCityMileageQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "里程信息列表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加物流商
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void addLogistics() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String logiId=SequenceManager.getSequence("");
			String yieldaly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String logiCode = CommonUtils.checkNull(request.getParamValue("LOGI_CODE")); //物流商编码
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商简称
			String logiFullName = CommonUtils.checkNull(request.getParamValue("LOGI_FULL_NAME")); //物流商全称
			
			String corporation = request.getParamValue("CORPORATION"); //法人
			String conPer = request.getParamValue("CON_PER"); //联系人
			String conTel = request.getParamValue("CON_TEL"); //联系人电话
			String status = request.getParamValue("STATUS"); //状态
			String address = request.getParamValue("ADDRESS"); //地址
			String remark = request.getParamValue("REMARK"); //备注
			//String disIds = request.getParamValue("disIds_"); //运费里程IDS
			
			//String orgId = request.getParamValue("DEPT_ID"); // 部门ID
			//String dealerId = request.getParamValue("DEALER_ID"); // 经销商ID
			String poseType = request.getParamValue("POSE_TYPE"); // 职位类型
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = status; // 职位状态
			//String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型
			//String myfun = request.getParamValue("MYFUNS"); // 职位对应的功能
			String poseBusType = request.getParamValue("POSE_BUS_TYPE"); // 职位类型
//			String[] roleIds = request.getParamValues("ROLE_ID"); // 职位对应的功能
//			String logiCompany = CommonUtils.checkNull(request
//					.getParamValue("logiCompany")); // 物流公司
//			String gjyw = request.getParamValue("GG");
			String chooseDlr = request.getParamValue("chooseDlr"); // 取得页面中是否自选经销商
			String provice = CommonUtils.checkNull(request.getParamValue("hidProvice")); // 页面中为职位选择的小区
			String orgId="";
			//判断是否存在车厂组织，若不存在，不允许添加
			List<Map<String, Object>> elist=reDao.getOemOrg(logonUser.getCompanyId().toString());
			if(elist==null||elist.size()==0){
				act.setOutData("st", "oemOrg_error");
				return;
			}else{
				orgId=elist.get(0).get("ORG_ID").toString();
			}
			//获取里程
			TtSalesLogiPO tslp=new TtSalesLogiPO();//物流商管理表
			tslp.setLogiId(Long.parseLong(logiId));
			tslp.setYieldly(Long.parseLong(yieldaly));
			tslp.setLogiCode(logiCode);
			tslp.setLogiName(logiName);
			tslp.setLogiFullName(logiFullName);
			tslp.setCorporation(corporation);
			tslp.setConPer(conPer);
			tslp.setConTel(conTel);
			tslp.setStatus(Long.parseLong(status));
			tslp.setAddress(address);
			tslp.setRemark(remark);
			tslp.setCreateBy(logonUser.getUserId());//创建人
			tslp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
			reDao.logisticsAdd(tslp);//物流商管理表
//			if (null != disIds && !"".equals(disIds)) {
//				List<Map<String,Object>> disList=reDao.getDisByDisIDS(disIds,logiId);//根据里程IDS获取里程相关信息
//				String[] array = disIds.split(",");
//				 if(disList!=null && disList.size()>0){
//					 act.setOutData("st", "添加失败，该地市已有物流商管理");
//					 return;
////					 act.setOutData("returnValue", 2);//添加失败，该地市已有物流商管理
////					 act.setOutData("disList", disList);
//				  }	else{
//					  reDao.logisticsAdd(tslp);//物流商管理表
//					  for (int i = 0; i < array.length; i++) {
//							if(null != array[i] && !"".equals(array[i])){//过滤第一个
//							String logiAreaId=SequenceManager.getSequence("");
//							TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
//							tslap.setLogiAreaId(Long.parseLong(logiAreaId));//物流商管理与区域ID
//							tslap.setLogiId(Long.parseLong(logiId));//物流商ID
//							tslap.setDisId(Long.parseLong(array[i]));//里程ID
//							tslap.setCreateBy(logonUser.getUserId());//创建人
//							tslap.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
//							reDao.logisticsAreaAdd(tslap);
//							}
//						} 
//						
//				 }
//			}
			
			
			TcPosePO posePO3 = new TcPosePO();
			posePO3.setPoseCode(poseCode);
			posePO3.setCompanyId(logonUser.getCompanyId());
			List<TcPosePO> list = factory.select(posePO3);
			if (list != null && list.size() > 0) {
				act.setOutData("st", "poseCode_error");
				return;
			}
			TcPosePO posePO4 = new TcPosePO();
			posePO4.setPoseName(poseName);
			posePO4.setCompanyId(logonUser.getCompanyId());
			List<TcPosePO> list2 = factory.select(posePO4);
			if (list2 != null && list2.size() > 0) {
				act.setOutData("st", "poseName_error");
				return;
			}
//			String[] funshs = new String[0];
//			if (funsh != null && funsh.length() > 0) {
//				funsh = funsh.substring(0, funsh.length() - 1);
//				funshs = funsh.split("#");
//			}
//
//			String[] t1;
//			String[] t2;
//			HashMap<String, String[]> hashMap = new HashMap<String, String[]>(); // 将功能权限保存在map中
//			for (int i = 0; i < funshs.length; i++) { // 保存功能权限
//				t1 = funshs[i].split(":"); // 功能ID
//				if (t1.length > 1) {
//					t2 = funshs[i].split(":")[1].split(","); // 功能对应数据权限
//					hashMap.put(t1[0], t2);
//				}
//			}

			TcPosePO posePO = new TcPosePO(); // 待保存的职位对象
			Long poseId = factory.getLongPK(new TcPosePO());
			posePO.setPoseId(poseId);

			if (poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))) {
				posePO.setLogiId(Long.valueOf(logiId));
			}

			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			posePO.setPoseBusType(Integer.valueOf(poseBusType));
			// 新增创建人与创建时间
			posePO.setCreateBy(logonUser.getUserId());
			posePO.setCreateDate(new Date());
			posePO.setCompanyId(logonUser.getCompanyId());
			posePO.setOrgId(new Long(orgId));
			posePO.setChooseDealer(Integer.valueOf(chooseDlr));
			factory.insert(posePO);
			
			if(provice!=""){
				String[] proviceIds=provice.split(",");
				if(proviceIds.length>0){
					for(int i = 0; i < proviceIds.length; i++){
						String pro_id = proviceIds[i];
						TrPoseRegionPO tppo= new TrPoseRegionPO();
						tppo.setPoseRegionId(Long.parseLong(SequenceManager
								.getSequence("")));
						tppo.setPoseId(poseId);
						tppo.setRegionId(Long.parseLong(pro_id));
						tppo.setCreateBy(logonUser.getUserId());
						tppo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
						factory.insert(tppo);
						//更新管理区域对应的承运商ID
						TmOrgPO top=new TmOrgPO();
						top.setOrgId(Long.parseLong(pro_id));
						TmOrgPO top2=new TmOrgPO();
						top2.setOrgId(Long.parseLong(pro_id));
						top2.setLogiId(logiId);
						top2.setUpdateBy(logonUser.getUserId());
						top2.setUpdateDate(new Date());
						reDao.update(top, top2);
					}
				}
				
			}
			String brandId = ""; // 品牌ID
			String brandList[] = null;
			Enumeration enu = request.getParamNames();
			while (enu.hasMoreElements()) {
				String pot = (String) enu.nextElement();
				if (pot.startsWith("BRAND")) {
					brandList = request.getParamValues(pot);
				}
			}
			if (brandList != null && brandList.length > 0) {

				for (int i = 0; i < brandList.length; i++) {
					// modify by xiayanpeng BEGIN
					// 根据在职位维护中选择的业务范围，存入职位和业务范围关系表中tm_pose_business_area
					TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
					areaPO.setRelationId(factory
							.getLongPK(new TmPoseBusinessAreaPO()));
					areaPO.setPoseId(poseId);
					areaPO.setAreaId(new Long(brandList[i]));
					areaPO.setCreateDate(new Date());
					areaPO.setCreateBy(logonUser.getUserId());
					factory.insert(areaPO);
				}
			} else {
				brandId = "";
			}
//			String[] myfuns = new String[0];
//			if (myfun != null && !"".equals(myfuns)) {
//				myfuns = myfun.split(",");
//			}
//
//			for (int i = 0; i < roleIds.length; i++) { // 保存职位对应的功能
//				/**
//				 * added by andy.ten@tom.com 向 tr_role_pose表中保存数据
//				 */
				TrRolePosePO poseRolePO = new TrRolePosePO();
				poseRolePO.setRolePoseId(Long.valueOf(SequenceManager.getSequence("")));
				poseRolePO.setRoleId(Long.parseLong(Constant.logiRoleId));
				poseRolePO.setPoseId(new Long(poseId));
				poseRolePO.setCreateDate(new Date());
				poseRolePO.setCreateBy(logonUser.getUserId());
				factory.insert(poseRolePO);
//			}
				
				//通过小区ID查询对应的经销商, 将职位物流商与经销商的关系插入关系表
				String smallOrgIds = "";
				if(provice!=""){
					String[] sOrgIds=provice.split(",");
					for (int i = 0; i < sOrgIds.length; i++) {
						smallOrgIds += sOrgIds[i] + ",";
					}
					if("" != smallOrgIds) {
						smallOrgIds = smallOrgIds.substring(0,smallOrgIds.length()-1);
						List<Map<String, Object>> poseDealers = reDao.getPoseDealer(smallOrgIds);
						
						for (Map<String, Object> poseDealer : poseDealers) {
							TtSalesLogiDealerRelationPO sldr = new TtSalesLogiDealerRelationPO();
							sldr.setId(Long.parseLong(SequenceManager.getSequence("")));
							sldr.setDealerId(Long.parseLong(poseDealer.get("DEALER_ID").toString()));
							sldr.setLogiId(Long.parseLong(logiId));
							sldr.setLogiCode(logiCode);
							sldr.setLogiName(logiName);
							sldr.setYieldly(Long.parseLong(yieldaly));
							sldr.setStatus(new Long(Constant.STATUS_ENABLE));
							sldr.setPoseId(poseId);
							factory.insert(sldr);
						}
					}
				}
			
			
			act.setOutData("st", "succeed");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增物流商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改物流商
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void editLogistics() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String deptId = request.getParamValue("DEPT_ID"); // 部门ID
			String dealerId = request.getParamValue("DEALER_ID"); // 公司ID
			String poseType = request.getParamValue("POSE_TYPE"); // 职位类别
			String poseBusType = request.getParamValue("POSE_BUS_TYPE"); // 职位类别
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = request.getParamValue("STATUS"); // 职位状态
			String poseId = request.getParamValue("POSE_ID"); // 职位ID
			//String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型

			//String gjyw = request.getParamValue("GG");

			//String[] roleIds = request.getParamValues("ROLE_ID"); // 职位对应的功能
			
			//----------------修改承运商信息 start------
			String logiId=CommonUtils.checkNull(request.getParamValue("LOGI_ID"));//物流商ID
			String yieldaly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String logiCode = CommonUtils.checkNull(request.getParamValue("LOGI_CODE")); //物流商编码
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商简称
			String logiFullName = CommonUtils.checkNull(request.getParamValue("LOGI_FULL_NAME")); //物流商全称
			
			String corporation = request.getParamValue("CORPORATION"); //法人
			String conPer = request.getParamValue("CON_PER"); //联系人
			String conTel = request.getParamValue("CON_TEL"); //联系人电话
			String status = request.getParamValue("STATUS"); //状态
			String address = request.getParamValue("ADDRESS"); //地址
			String remark = request.getParamValue("REMARK"); //备注
			
			String provice = CommonUtils.checkNull(request.getParamValue("hidProvice")); // 页面中为职位选择的小区
			
			//String disIds = request.getParamValue("disIds_"); //运费里程IDS
			TtSalesLogiPO tslp=new TtSalesLogiPO();//物流商管理表
			tslp.setYieldly(Long.parseLong(yieldaly));
			tslp.setLogiCode(logiCode);
			tslp.setLogiName(logiName);
			tslp.setLogiFullName(logiFullName);
			tslp.setCorporation(corporation);
			tslp.setConPer(conPer);
			tslp.setConTel(conTel);
			tslp.setStatus(Long.parseLong(status));
			tslp.setAddress(address);
			tslp.setRemark(remark);
			tslp.setUpdateBy(logonUser.getUserId());//修改人
			tslp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			TtSalesLogiPO tslpseach=new TtSalesLogiPO();//物流商管理表查询条件
			tslpseach.setLogiId(Long.parseLong(logiId));
			reDao.logisticsUpdate(tslpseach, tslp);
			//删除对应的关联数据
//			TtSalesLogiAreaPO tslapseach= new TtSalesLogiAreaPO();
//			tslapseach.setLogiId(Long.parseLong(logiId));
//			
//			//添加新增数据道关系表
//			if (null != disIds && !"".equals(disIds)) {
//				List<Map<String,Object>> disList=reDao.getDisByDisIDS(disIds,logiId);//根据里程IDS获取里程相关信息
//				String[] array = disIds.split(",");
//				 if(disList!=null && disList.size()>0){
//					 act.setOutData("st", "修改失败，该地市已有物流商管理");
//					 return;
//					 //act.setOutData("returnValue", 2);//修改失败，该地市已有物流商管理
//					 //act.setOutData("disList", disList);
//				  }	else{
//					  reDao.logisticsUpdate(tslpseach, tslp);
//					  reDao.logisticsAreaDelete(tslapseach);
//					for (int i = 0; i < array.length; i++) {
//						if(null != array[i] && !"".equals(array[i])){//过滤第一个
//						String logiAreaId=SequenceManager.getSequence("");
//						TtSalesLogiAreaPO tslap=new TtSalesLogiAreaPO();//物流商管理与区域表
//						tslap.setLogiAreaId(Long.parseLong(logiAreaId));//物流商管理与区域ID
//						tslap.setLogiId(Long.parseLong(logiId));//物流商ID
//						tslap.setDisId(Long.parseLong(array[i]));//里程ID
//						tslap.setCreateBy(logonUser.getUserId());//创建人
//						tslap.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
//						reDao.logisticsAreaAdd(tslap);
//						}
//					}
//					//act.setOutData("returnValue", 1);
//			   }
//			}
			//---------------修改承运商信息 end------
			Long dealerOrgId = null;
			Long companyId = null;
			if (poseType.equals(String.valueOf(Constant.SYS_USER_DEALER))) {
				companyId = Long.parseLong(request.getParamValue("COMPANY_ID"));
			}
			TcPosePO posePO4 = new TcPosePO();
			posePO4.setPoseName(poseName);
			if (poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) {
				posePO4.setCompanyId(logonUser.getCompanyId());
			} else {
				posePO4.setCompanyId(companyId);
			}
			List<TcPosePO> list2 = factory.select(posePO4);
			posePO4.setPoseId(new Long(poseId));
			List<TcPosePO> list3 = factory.select(posePO4);
			if (list2 != null && list2.size() > 0
					&& (list3 == null || list3.size() < 1)) {
				act.setOutData("st", "该职位名称已存在,请更改职位名称!");
				return;
			}
//			String[] funshs = null;
//
//			if (funsh != null && funsh.length() > 0) {
//				funsh = funsh.substring(0, funsh.length() - 1);
//				funshs = funsh.split("#");
//			}
//
//			String[] t1;
//			String[] t2;
//			HashMap<String, String[]> hashMap = new HashMap<String, String[]>(); // 将功能权限保存在map中
//			if (funshs != null) {
//				for (int i = 0; i < funshs.length; i++) { // 保存功能权限
//					t1 = funshs[i].split(":"); // 功能ID
//					if (t1.length > 1) {
//						t2 = funshs[i].split(":")[1].split(","); // 功能对应数据权限
//						hashMap.put(t1[0], t2);
//					}
//				}
//			}

			TcPosePO posePO = new TcPosePO(); // 待保存的职位对象

			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setUpdateBy(logonUser.getUserId());
			posePO.setUpdateDate(new Date());
			posePO.setPoseBusType(Integer.valueOf(poseBusType));
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			posePO.setCompanyId(logonUser.getCompanyId());
			if (poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))) {
				posePO.setLogiId(Long.valueOf(logiId));
			}
			if (poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) { // 创建SGM职位
				posePO.setOrgId(new Long(deptId));
				posePO.setCompanyId(logonUser.getCompanyId());
				// modify by xiayanpeng BEGIN 删除原有逻辑
				// posePO.setOemPositionArea(brandId);
				// modify by xiayanpeng end

			} else { // 创建经销商职位

				posePO.setCompanyId(companyId);
			}
			String chooseDlr = request.getParamValue("chooseDlr");  //用户是否选择手选经销商
			if(chooseDlr != null && !"".equals(chooseDlr)){
				posePO.setChooseDealer(Integer.valueOf(chooseDlr));
			}else{
				posePO.setChooseDealer(Constant.IF_TYPE_NO);
			}
			TcPosePO posePO2 = new TcPosePO();
			posePO2.setPoseId(new Long(poseId));
			factory.update(posePO2, posePO);
			//取得该职位下有无手动选择经销商	
			if(chooseDlr != null && !"".equals(chooseDlr)){
				//如果该职位下经销商为手动选择
				if(Constant.IF_TYPE_YES.toString().equals(chooseDlr)){
					TrPoseRegionPO delpo= new TrPoseRegionPO();
					delpo.setPoseId(Long.valueOf(poseId));
					factory.delete(delpo);
					String[] dealerIds = request.getParamValues("dealerIds");
					if (dealerIds == null) {
						TrPoseDealerPO tpo = new TrPoseDealerPO();
						tpo.setPoseId(Long.valueOf(poseId));
						factory.delete(tpo);
					} else {
						TrPoseDealerPO tpo = new TrPoseDealerPO();
						tpo.setPoseId(Long.valueOf(poseId));
						factory.delete(tpo);
						for(int i = 0 ; i < dealerIds.length; i++){
							TrPoseDealerPO tpo_ = new TrPoseDealerPO();
							tpo_.setDealerId(Long.valueOf(dealerIds[i]));
							tpo_.setPoseId(Long.valueOf(poseId));
							tpo_.setCreateBy(logonUser.getUserId());
							tpo_.setCreateDate(new Date());
							tpo_.setPoseDealerId(Long.parseLong(SequenceManager
									.getSequence("")));
							factory.insert(tpo_);
						}
					}
				}else{
					TrPoseDealerPO tpo = new TrPoseDealerPO();
					tpo.setPoseId(Long.valueOf(poseId));
					factory.delete(tpo);
					TrPoseRegionPO delpo= new TrPoseRegionPO();
					delpo.setPoseId(Long.valueOf(poseId));
					factory.delete(delpo);
					//首先删除以前的职位与省份关系表数据
					//清除组织表中对应物流商ID
					TmOrgPO toop=new TmOrgPO();
					toop.setLogiId(logiId);
					TmOrgPO toop2=new TmOrgPO();
					toop2.setLogiId("");
					reDao.update(toop, toop2);
					
					//添加现在该职位下选中的省份
					
					if(provice!=""){
						String[] proviceIds=provice.split(",");
						if(proviceIds.length>0){
							for(int i = 0; i < proviceIds.length; i++){
								String pro_id = proviceIds[i];
								TrPoseRegionPO tppo= new TrPoseRegionPO();
								tppo.setPoseRegionId(Long.parseLong(SequenceManager
										.getSequence("")));
								tppo.setPoseId(Long.valueOf(poseId));
								tppo.setRegionId(Long.parseLong(pro_id));
								tppo.setCreateBy(logonUser.getUserId());
								tppo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
								factory.insert(tppo);
								//更新管理区域对应的承运商ID
								TmOrgPO top=new TmOrgPO();
								top.setOrgId(Long.parseLong(pro_id));
								TmOrgPO top2=new TmOrgPO();
								top2.setOrgId(Long.parseLong(pro_id));
								top2.setLogiId(logiId);
								top2.setUpdateBy(logonUser.getUserId());
								top2.setUpdateDate(new Date());
								reDao.update(top, top2);
							}
						}
						
					}
				}
			}
			// modify by xiayanpeng begin 经销商职位也维护业务范围
			String brandId = ""; // 品牌ID
			String brandList[] = null;
			Enumeration enu = request.getParamNames();
			while (enu.hasMoreElements()) {
				String pot = (String) enu.nextElement();
				if (pot.startsWith("BRAND")) {
					brandList = request.getParamValues(pot);
				}
			}
			if (brandList != null && brandList.length > 0) {
				// modify by xiayanpeng BEGIN 根据职位ID删除职位与业务范围关系表
				TmPoseBusinessAreaPO areaDelPO = new TmPoseBusinessAreaPO();
				areaDelPO.setPoseId(new Long(poseId));
				factory.delete(areaDelPO);
				// modify by xiayanpeng end
				for (int i = 0; i < brandList.length; i++) {
					// modify by xiayanpeng BEGIN
					// 根据在职位维护中选择的业务范围，存入职位和业务范围关系表中tm_pose_business_area
					TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
					areaPO.setRelationId(factory
							.getLongPK(new TmPoseBusinessAreaPO()));
					areaPO.setPoseId(new Long(poseId));
					areaPO.setAreaId(new Long(brandList[i]));
					areaPO.setCreateDate(new Date());
					areaPO.setCreateBy(logonUser.getUserId());
					factory.insert(areaPO);
				}
			} else {
				brandId = "";
			}

//			TrRolePosePO trPoseRoelPO = new TrRolePosePO();
//			trPoseRoelPO.setPoseId(new Long(poseId));
//			factory.delete(trPoseRoelPO);
//
//			for (int i = 0; i < roleIds.length; i++) { // 保存职位对应的功能
//				TrRolePosePO poseRolePO = new TrRolePosePO();
//				poseRolePO.setRolePoseId(factory.getLongPK(new TrRolePosePO()));
//				poseRolePO.setRoleId(new Long(roleIds[i]));
//				poseRolePO.setPoseId(new Long(poseId));
//				factory.insert(poseRolePO);
//			}
//
//			if (poseType.equals(Constant.SYS_USER_DEALER)) {
//				TrPoseBinsPO trPoseBinsPO2 = new TrPoseBinsPO();
//				trPoseBinsPO2.setPoseId(poseId);
//				factory.delete(trPoseBinsPO2);
//
//				if (gjyw != null && !"".equals(gjyw)) {
//					String[] temp = gjyw.split(",");
//					for (int i = 0; i < temp.length; i++) {
//						TrPoseBinsPO trPoseBinsPO = new TrPoseBinsPO();
//						trPoseBinsPO.setPoseBinsId(factory
//								.getStringPK(new TrPoseBinsPO()));
//						trPoseBinsPO.setPoseId(poseId);
//						trPoseBinsPO.setBinsCodeId(temp[i]);
//						factory.insert(trPoseBinsPO);
//					}
//				}
//			}
			
			//删除修改之前的物流商与经销商关系
			TtSalesLogiDealerRelationPO delsldr = new TtSalesLogiDealerRelationPO();
			delsldr.setLogiId(Long.parseLong(logiId));
			factory.delete(delsldr);
			
			//通过小区ID查询对应的经销商, 将职位物流商与经销商的关系插入关系表
			String smallOrgIds = "";
			if(provice!=""){
				String[] sOrgIds=provice.split(",");
				for (int i = 0; i < sOrgIds.length; i++) {
					smallOrgIds += sOrgIds[i] + ",";
				}
				if("" != smallOrgIds) {
					smallOrgIds = smallOrgIds.substring(0,smallOrgIds.length()-1);
					List<Map<String, Object>> poseDealers = reDao.getPoseDealer(smallOrgIds);
					
					for (Map<String, Object> poseDealer : poseDealers) {
						TtSalesLogiDealerRelationPO sldr = new TtSalesLogiDealerRelationPO();
						sldr.setId(Long.parseLong(SequenceManager.getSequence("")));
						sldr.setDealerId(Long.parseLong(poseDealer.get("DEALER_ID").toString()));
						sldr.setLogiId(Long.parseLong(logiId));
						sldr.setLogiCode(logiCode);
						sldr.setLogiName(logiName);
						sldr.setYieldly(Long.parseLong(yieldaly));
						sldr.setStatus(new Long(Constant.STATUS_ENABLE));
						sldr.setPoseId(new Long(poseId));
						factory.insert(sldr);
					}
				}
			}
			
			act.setOutData("st", "succeed");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 修改物流商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 打开装配区间设置页面
	 * @author liufazhong
	 */
	public void editLogiIntervalInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List<Map<String, Object>> rInterval = reDao.queryLogiInterval();
			act.setOutData("rInterval", rInterval);
			act.setForword(logiIntrevalEdit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "返利参数管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改返利区间规则
	 * @author liufazhong
	 */
	public void logiIntrevalUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String [] beginNums = request.getParamValues("beginNum");
			String [] endNums = request.getParamValues("endNum");
			String [] assDays = request.getParamValues("assDays");
			if (beginNums != null && beginNums.length > 0) {
				//删除原数据
				reDao.delete(new TtSalesLogiIntrevalPO());
				for(int i = 0;i < beginNums.length;i++){
					TtSalesLogiIntrevalPO tri = new TtSalesLogiIntrevalPO();
					tri.setLiId(Long.parseLong(SequenceManager.getSequence("")));
					tri.setBeginNum(Integer.parseInt(beginNums[i]));
					tri.setEndNum(Integer.parseInt(endNums[i]));
					tri.setAssDays(Integer.parseInt(assDays[i]));
					tri.setStatus(Constant.STATUS_ENABLE);
					tri.setCreateBy(logonUser.getUserId());
					tri.setCreateDate(new Date());
					tri.setUpdateBy(logonUser.getUserId());
					tri.setUpdateDate(new Date());
					reDao.insert(tri);
				}
				act.setOutData("message", "00");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "参数管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
