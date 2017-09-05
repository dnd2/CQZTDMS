package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.SpecialService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.SpecialServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;
/**
 * 特殊费用
 * @author yuewei
 *
 */
public class SpecialAction extends BaseAction{

	private SpecialService specialservice = new SpecialServiceImpl();
	private ClaimService claimservice=new ClaimServiceImpl();
	private PageResult<Map<String, Object>> list = null;
	
	
	public void updateSpeInit(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(special_type,id);
		act.setOutData("t", data);
		request.setAttribute("type", "update");
		if("1".equals(special_type)){//善意索赔
			sendMsgByUrl("special", "special_goodwill_claim", "服务站特殊费用修改");
		}
		if("0".equals(special_type)){//退换车
			sendMsgByUrl("special", "special_change_car", "服务站特殊费用修改");
		}
	}
	public void printSpeInit(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(special_type,id);
		act.setOutData("t", data);
		if("1".equals(special_type)){//善意索赔
			sendMsgByUrl("special", "special_goodwill_claim_print", "服务站特殊费用打印");
		}
		if("0".equals(special_type)){//退换车
			sendMsgByUrl("special", "special_change_car_print", "服务站特殊费用打印");
		}
	}
	
	public void viewSpe(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(special_type,id);
		act.setOutData("t", data);
		request.setAttribute("type", "view");
		if("1".equals(special_type)){//善意索赔
			sendMsgByUrl("special", "special_goodwill_claim_view", "服务站特殊费用明细");
		}
		if("0".equals(special_type)){//退换车
			sendMsgByUrl("special", "special_change_car_view", "服务站特殊费用明细");
		}
	}
	
	public void delSpe(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		int res=specialservice.delSpe(special_type,id);
		setJsonSuccByres(res);
	}
	
	public void queryPartCode(){
		list=specialservice.queryPartCode(request, Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps", list);
	}
	
	public void querySupplierCode(){
		list=specialservice.querySupplierCode(request, Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps", list);
	}
	
	public void findDataByVin(){
		Map<String,Object> map=specialservice.findDataByVin(getParam("vin"));
		act.setOutData("dataByVin", map);
	}
	
	public void qureyAllClaim(){
		list=specialservice.qureyAllClaim(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps", list);
	}
	
	public void saveOrUpdate(){
		int res=specialservice.saveOrUpdate(request,loginUser);
		act.setOutData("identify", getParam("identify"));
		setJsonSuccByres(res);
	}
	
	public void specialDealerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialDealerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_dealer_list", "服务站特殊费用申请列表");
	}
	/**
	 * 服务站服务经理审核
	 */
	public void specialServiceManagerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialServiceManagerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_dealer_manger_list", "服务站服务经理审核");
	}
	
	public void audit(){
		int res=specialservice.audit(request,loginUser);
		act.setOutData("identify", getParam("identify"));
		setJsonSuccByres(res);
	}
	
	public void serviceManagerSpeInit(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(special_type,id);
		act.setOutData("t", data);
		request.setAttribute("type", "audit");
		String url = getParam("url");
		request.setAttribute("url", url);
		if("1".equals(special_type)){//善意索赔
			sendMsgByUrl("special", "special_goodwill_claim_audit", "服务站服务经理审核页面跳转");
		}
		if("0".equals(special_type)){//退换车
			sendMsgByUrl("special", "special_change_car_audit", "服务站服务经理审核页面跳转");
		}
	}
	
	/**
	 * 区域经理审核
	 */
	public void specialRegionalManagerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialRegionalManagerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_regional_manger_list", "区域经理(分区)审核列表");
	}
	/**
	 * 区域总监(分区)审核
	 */
	public void specialRegionalDirectorList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialRegionalDirectorList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_regional_director_list", "区域总监审核列表");
	}
	/**
	 * 技术部(分区)审核
	 */
	public void specialTecSupportList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialTecSupportList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_tec_manger_list", "服务站特殊费用申请列表");
	}
	/**
	 * 结算室(分区)审核
	 */
	public void specialClaimSettlementList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialClaimSettlementList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "special_claim_settlement_list", "结算室(分区)审核列表");
	}
	public void addspecialDealerInit(){
		String spe_type = getParam("spe_type");
		String specialNo=specialservice.getSpecialNo();
		act.setOutData("dealerCode", loginUser.getDealerCode());
		act.setOutData("specialNo", specialNo);
		request.setAttribute("type", "add");
		if("1".equals(spe_type)){//善意索赔
			sendMsgByUrl("special", "special_goodwill_claim", "服务站特殊费用申请");
		}
		if("0".equals(spe_type)){//退换车
			sendMsgByUrl("special", "special_change_car", "服务站特殊费用申请");
		}
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	//退换车跟善意索赔菜单
	public void	returnAndclaim(){
		sendMsgByUrl("special", "special_returncar_and_claim", "退换车及善意索赔申请");
		
	}
	//退换车跟善意索赔申请查询
	public void	findSpecia(){
		 list = specialservice.findDatespecialapply(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
	    act.setOutData("ps",list);
	}
	//退换车善意索赔新增
	public void addspecialgoodwill(){
		String spe_type = getParam("spe_type");
		Map<String,Object> map = specialservice.findDateapplyByUserid(loginUser.getUserId());
		act.setOutData("mp", map);
		act.setOutData("type", spe_type);
		if("1".equals(spe_type)){//善意索赔
		     sendMsgByUrl("special", "add_returncar_goodwill_claim", "退换车善意索赔新增");
		}
		if("0".equals(spe_type)){//退换车
			sendMsgByUrl("special", "add_special_change_car", "退换车新增");
		}
	}
	//插入退换车及善意索赔数据
	public void  sureInsert(){
		String view = getParam("view");
		int	res=0;
		List<Map<String,Object>> list =  specialservice.checkapplyno(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
		if ("0".equals(view)) {  //0保存
			    view = "20501001";//未上报
				if (list.size()<1) {
					res= specialservice.sureInsert(request,view,loginUser);
					setJsonSuccByres(1);
				}else {
					setJsonSuccByres(-1);
				}
		}
		if ("1".equals(view)) {
			view = "20501002";//审核中
			if (list.size()<1) {
				res= specialservice.sureInsert(request,view,loginUser);
				this.setJsonSuccByres(1);
			}else {
				this.setJsonSuccByres(-1);
			}
		}
	}
	
	//退换车及善意索赔数据查询
	public void findSpeciaAudit(){
		list=specialservice.findreturncarAndclaim(request,Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps", list);
	}
	//查询总金额
	public void findtotleamount(){
		Map<String, Object> amount = specialservice.findtotleamount(request,loginUser);
		act.setOutData("amount", amount.get("TOTLEAMOUNT"));
		
	}
	//退换车及善意索赔审核页面
	public void  AuditReturnCar(){
	   getFile("id");
	   Map<String, Object>  map =	specialservice.findreturncarByid(request);
	   Map<String, Object>  mapcount =	specialservice.findreturncarByvin(request);
		  act.setOutData("map", map);
		  act.setOutData("mapcount", mapcount.get("VINCOUNT").toString());
		  sendMsgByUrl("special", "Audit_ReturnCar", "退换车及善意索赔审核页面");
	}
	//审核通过，拒绝，退回
	public void	AuditReturncarAndgoodwill(){
		String view = getParam("view");
		if ("0".equals(view)) {//通过
			view = "20501005";
			int res = specialservice.updateReturncar(request,loginUser, view);
			setJsonSuccByres(res);
		}
		if ("1".equals(view)) {
			view = "20501006";//退回
			int res = specialservice.updateReturncar(request,loginUser,view);
			setJsonSuccByres(res);
		}
		if ("2".equals(view)) {
			view = "20501004";//拒绝
			int res = specialservice.updateReturncar(request,loginUser,view);
			setJsonSuccByres(res);
		}
	}
	//查询申请单号
	public void findspecialappNo(){
	    list =	specialservice.findspecialappno(request, loginUser,Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps",list);
	}
	
	public void findDateByappno(){
		Map<String, Object> mp = specialservice.findDateByappno(request);
		act.setOutData("mp", mp);
	}
	//退换车及善意索赔申请的明细页
	public void viewSpecialApplyDetailed(){
		String  type =  getParam("type");
		Map<String, Object> map = specialservice.viewSpecialApplyDetailed(request);
		act.setOutData("map",map);
		getFile("id");
		if ("AudieDetail".equals(type)) {//善意索赔审核明细页
			sendMsgByUrl("special", "viewAuditGoodwillclaim","退换车及善意索赔审核明细页面");
		}else if("update".equals(type) ){
			if ("0".equals(map.get("SPECIAL_TYPE").toString())) {
				sendMsgByUrl("special", "updateapplyReturnCar","退换车申请修改页面");
			}else {
				sendMsgByUrl("special", "updateapplyGoodwillclaim","善意索赔申请修改页面");
			}
		}else if ("updateCar".equals(type) ) {
			sendMsgByUrl("special", "updateapplyReturnCar","退换车申请修改页面");
		}else {
			sendMsgByUrl("special", "viewSpecialApplyDetailed","退换车及善意索赔申请明细页面");
		}
	}
	//善意索赔导出
	public void Toexcelspecialapply(){
		list = specialservice.findreturncarAndclaim(request,Constant.PAGE_SIZE_MAX, getCurrPage());
		specialservice.Toexcelspecialapply(list);
	}
	//申请上报 审核操作
	public void	updatespecialapply(){
		String view = getParam("view");
		int res=0;
		if ("0".equals(view)) {  //0保存
			 view = "20501001";//未上报
		     res=specialservice.updatespecialapply(request,loginUser,view);
		}
		if ("1".equals(view)) {
			view = "20501002";//审核中
			res= specialservice.updatespecialapply(request,loginUser,view);
		}
		setJsonSuccByres(res);
	}
	//删除申请操作delSpecialapply
	public void delSpecialapply(){
		 String id = getParam("id");
		 int res = specialservice.delSpecialapply(id);
		 setJsonSuccByres(res);
	}
	//提报---撤销
	public void  SpecialApplyReport(){
	  String id =  getParam("id");
	  int res= specialservice.updatespecialapplyreport(request,loginUser,id);
	  setJsonSuccByres(res);
	}
	//申请单号查询
	public void  queryByapplyno(){
		Map<String, Object> map = specialservice.queryByapplyno(loginUser,request);
		act.setOutData("map", map);
		sendMsgByUrl("special", "viewsSpecialDetailed_Byapplyno","退换车及善意索赔申请单明细");
	}
	//索赔单号查询
	public void	queryByCLAIMNO(){
		Map<String, Object> map = specialservice.queryByCLAIMNO(loginUser,request);
		act.setOutData("ID", map.get("ID"));
		act.setOutData("RO_NO", map.get("RO_NO"));
		
	}
	//查询总额
	public void Querycountspecil(){
		Double count = specialservice.Querycountspecil(loginUser,request);
		act.setOutData("count", count);
	}
	
	public void specialstatusTracking(){
		sendMsgByUrl("special", "Special_cost_track_list","退换车及善意索赔申请状态跟踪");
		
	}
	public void specialstatusTrackQuery(){
		 list =	specialservice.specialstatusTrackQuery(request, loginUser,Constant.PAGE_SIZE, getCurrPage());
		 act.setOutData("ps", list);
	}
	/**
	 * 技术部状态跟踪
	 */
	public void TechnologyTracking(){
		String type = getParam("type");
		if ("query".equals(type)) {
			list =	specialservice.specialstatusTrackQuery(request, loginUser,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("special", "Special_Technology_Tracking_list","特殊费用技术部跟踪");
	}
	/**
	 * 技术部撤销审核
	 */
	public void  CancelAudit(){
	int res = specialservice.CancelAudit(request,loginUser);
	setJsonSuccByres(res);
		
	}
}
