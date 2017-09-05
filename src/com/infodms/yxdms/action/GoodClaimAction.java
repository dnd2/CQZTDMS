package com.infodms.yxdms.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.yxdms.dao.GoodClaimDAO;
import com.infodms.yxdms.entity.special.TtAsSpecialAmountRangePO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialRecordPO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.GoodClaimService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.GoodClaimServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;
/**
 * 特殊费用
 * @author yuewei
 *
 */
public class GoodClaimAction extends BaseAction{

	private GoodClaimService specialservice = new GoodClaimServiceImpl();
	private ClaimService claimservice=new ClaimServiceImpl();
	private GoodClaimDAO gdao=new GoodClaimDAO();
	private PageResult<Map<String, Object>> list = null;
	
	
	public void updateSpeInit(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(id);
		List<Map<String, Object>> psList=specialservice.findPartSupply(id);
		act.setOutData("t", data);
		act.setOutData("psList", psList);
		request.setAttribute("type", "update");
		sendMsgByUrl("goodClaim", "special_goodwill_claim", "服务站特殊费用修改");
		
	}
	public void printSpeInit(){
		String special_type = getParam("special_type");
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(id);
		act.setOutData("t", data);
		if("1".equals(special_type)){//善意索赔
			sendMsgByUrl("goodClaim", "special_goodwill_claim_print", "服务站特殊费用打印");
		}
		if("0".equals(special_type)){//退换车
			sendMsgByUrl("goodClaim", "special_change_car_print", "服务站特殊费用打印");
		}
	}
	
	public void viewSpe(){
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(id);
		List<Map<String, Object>> psList=specialservice.findPartSupply(id);
		List<Map<String, Object>> rList=specialservice.specialRecord(id);
		act.setOutData("t", data);
		act.setOutData("psList", psList);
		act.setOutData("rList", rList);
		act.setOutData("dealerId", loginUser.getDealerId());
		request.setAttribute("type", "view");
		sendMsgByUrl("goodClaim", "special_goodwill_claim_view", "服务站特殊费用明细");
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
	public void checkVin(){
		String vin=request.getParamValue("vin");
		TmVehiclePO v=new TmVehiclePO();
		v.setVin(vin);
		@SuppressWarnings("unchecked")
		List<TmVehiclePO> list=gdao.select(v);
		if(list.size()>0){
			act.setOutData("msg", "00");
		}else{
			act.setOutData("msg", "01");
		}
	}
	public void specialDealerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialDealerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("goodClaim", "special_dealer_list", "服务站特殊费用申请列表");
	}
	
	/**
	 * 服务站服务经理审核
	 *//*
	public void specialServiceManagerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialServiceManagerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("goodClaim", "special_dealer_manger_list", "服务站服务经理审核");
	}*/
	
	public void audit(){
		int res=specialservice.audit(request,loginUser);
		act.setOutData("identify", getParam("identify"));
		setJsonSuccByres(res);
	}
	
	public void serviceManagerSpeInit(){
		String id = getParam("id");
		getFile("id");
		Map<String, Object> data=specialservice.findSpeData(id);
		List<Map<String, Object>> psList=specialservice.findPartSupply(id);
		List<Map<String, Object>> rList=specialservice.specialRecord(id);
		act.setOutData("rList", rList);
		act.setOutData("t", data);
		act.setOutData("psList", psList);
		act.setOutData("psLength", psList.size());
		act.setOutData("type", DaoFactory.getParam(request, "type"));
		String url = getParam("url");
		request.setAttribute("url", url);
		sendMsgByUrl("goodClaim", "special_goodwill_claim_audit", "服务站服务经理审核页面跳转");
	}
	
	/**
	 * 区域经理审核
	 */
	public void specialRegionalManagerList(){
		List<TtAsSpecialAmountRangePO> lamount=querySpecialAmount(2+"");
		if(lamount.size()>0 && "区域经理审核".equals(lamount.get(0).getName())){
			String query = getParam("query");
			setCurrMonthTime("startTime", "endTime");
			if ("true".equals(query)) {
				list = specialservice.specialRegionalManagerList(lamount.get(0),request,loginUser, Constant.PAGE_SIZE, getCurrPage());
				act.setOutData("ps", list);
			}
			act.setOutData("lamount", lamount.get(0));
		}		
		act.setOutData("type", "2");//二级审核
		sendMsgByUrl("goodClaim", "special_regional_manger_list", "区域经理(分区)审核列表");
	}
	/**
	 * 区域总监(分区)审核
	 */
	public void specialRegionalDirectorList(){
		List<TtAsSpecialAmountRangePO> lamount=querySpecialAmount(3+"");
		if(lamount.size()>0 && "区域总监审核".equals(lamount.get(0).getName())){
			String query = getParam("query");
			setCurrMonthTime("startTime", "endTime");
			if ("true".equals(query)) {
				list = specialservice.specialRegionalDirectorList(lamount.get(0),request,loginUser, Constant.PAGE_SIZE, getCurrPage());
				act.setOutData("ps", list);
			}
			act.setOutData("lamount", lamount.get(0));
		}		
		act.setOutData("type", "3");//三级审核
		sendMsgByUrl("goodClaim", "special_regional_director_list", "区域总监审核列表");
	}
	/**
	 * 服务经理审核
	 */
	public void specialTecSupportList(){
		List<TtAsSpecialAmountRangePO> lamount=querySpecialAmount(1+"");
		if(lamount.size()>0 && "服务经理审核".equals(lamount.get(0).getName())){
			String query = getParam("query");
			setCurrMonthTime("startTime", "endTime");
			if ("true".equals(query)) {
				list = specialservice.specialTecSupportList(lamount.get(0),request,loginUser, Constant.PAGE_SIZE, getCurrPage());
				act.setOutData("ps", list);
			}
			act.setOutData("lamount", lamount.get(0));
		}		
		act.setOutData("type", "1");//一级审核
		sendMsgByUrl("goodClaim", "special_tec_manger_list", "服务站特殊费用申请列表");
	}
	/**
	 * 判断职位id
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private List<TtAsSpecialAmountRangePO> querySpecialAmount(String s) {
		TtAsSpecialAmountRangePO po=new TtAsSpecialAmountRangePO();
		po.setPositionId(Long.parseLong(s));
		List<TtAsSpecialAmountRangePO> list=gdao.select(po);
		return list;
	}
	/**
	 * 查看
	 */
	public void specialTecSeSupportList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = specialservice.specialTecSeSupportList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("goodClaim", "special_tec_manger_listSe", "服务站特殊费用申请列表");
	}
	/**
	 * 结算室(分区)审核
	 */
	public void specialClaimSettlementList(){
		List<TtAsSpecialAmountRangePO> lamount=querySpecialAmount(4+"");
		if(lamount.size()>0 && "结算审核".equals(lamount.get(0).getName())){
			String query = getParam("query");
			setCurrMonthTime("startTime", "endTime");
			if ("true".equals(query)) {
				list = specialservice.specialClaimSettlementList(lamount.get(0),request,loginUser, Constant.PAGE_SIZE, getCurrPage());
				act.setOutData("ps", list);
			}
			act.setOutData("lamount", lamount.get(0));
		}		
		act.setOutData("type", "4");//四级审核
		sendMsgByUrl("goodClaim", "special_claim_settlement_list", "结算室(分区)审核列表");
	}
	public void addspecialDealerInit(){
		String specialNo=specialservice.getSpecialNo();
		act.setOutData("dealerCode", loginUser.getDealerCode());
		act.setOutData("specialNo", specialNo);
		request.setAttribute("type", "add");
		sendMsgByUrl("goodClaim", "special_goodwill_claim", "服务站特殊费用申请");
		
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	//退换车跟善意索赔菜单
	public void	returnAndclaim(){
		sendMsgByUrl("goodClaim", "special_returncar_and_claim", "退换车及善意索赔申请");
		
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
		     sendMsgByUrl("goodClaim", "add_returncar_goodwill_claim", "退换车善意索赔新增");
		}
		if("0".equals(spe_type)){//退换车
			sendMsgByUrl("goodClaim", "add_special_change_car", "退换车新增");
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
		  sendMsgByUrl("goodClaim", "Audit_ReturnCar", "退换车及善意索赔审核页面");
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
			sendMsgByUrl("goodClaim", "viewAuditGoodwillclaim","退换车及善意索赔审核明细页面");
		}else if("update".equals(type) ){
			if ("0".equals(map.get("SPECIAL_TYPE").toString())) {
				sendMsgByUrl("goodClaim", "updateapplyReturnCar","退换车申请修改页面");
			}else {
				sendMsgByUrl("goodClaim", "updateapplyGoodwillclaim","善意索赔申请修改页面");
			}
		}else if ("updateCar".equals(type) ) {
			sendMsgByUrl("goodClaim", "updateapplyReturnCar","退换车申请修改页面");
		}else {
			sendMsgByUrl("goodClaim", "viewSpecialApplyDetailed","退换车及善意索赔申请明细页面");
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
		sendMsgByUrl("goodClaim", "viewsSpecialDetailed_Byapplyno","退换车及善意索赔申请单明细");
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
		sendMsgByUrl("goodClaim", "Special_cost_track_list","退换车及善意索赔申请状态跟踪");
		
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
		sendMsgByUrl("goodClaim", "Special_Technology_Tracking_list","特殊费用技术部跟踪");
	}
	/**
	 * 技术部撤销审核
	 */
	public void  CancelAudit(){
	int res = specialservice.CancelAudit(request,loginUser);
	setJsonSuccByres(res);
		
	}
}
