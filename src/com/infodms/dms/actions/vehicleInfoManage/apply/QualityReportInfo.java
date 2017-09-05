package com.infodms.dms.actions.vehicleInfoManage.apply;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.vehicleInfoManage.QualityReportInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtSalesQualityReportInfoPO;
import com.infoservice.po3.bean.PageResult;


public class QualityReportInfo extends BaseAction{

	private final String qualityReportInfoList = "/jsp/vehicleInfoManage/apply/qualityReportInfoList.jsp";
	private final String qualityReportInfoView = "/jsp/vehicleInfoManage/apply/qualityReportInfoView.jsp";
	private final String qualityReportInfoView1 = "/jsp/vehicleInfoManage/apply/qualityReportInfoView1.jsp";
	private final String qualityReportInfoView2 = "/jsp/vehicleInfoManage/apply/qualityReportInfoView2.jsp";
	private final String qualityReportInfoView4 = "/jsp/vehicleInfoManage/apply/qualityReportInfoView4.jsp";
	private final String printQualityInfoReport = "/jsp/vehicleInfoManage/apply/printQualityInfoReport.jsp";
	private final String printQualityInfoReport1 = "/jsp/vehicleInfoManage/apply/printQualityInfoReport1.jsp";
	private final String saveOrupdateInfoReport = "/jsp/vehicleInfoManage/apply/saveOrupdateInfoReport.jsp";
	private final String saveOrupdateInfoReport1 = "/jsp/vehicleInfoManage/apply/saveOrupdateTechnicalDept.jsp";
	private final String auditByTechnicalDept = "/jsp/vehicleInfoManage/apply/auditByTechnicalDept.jsp";
	private final String auditByTechnicalDept1 = "/jsp/vehicleInfoManage/apply/auditByTechnicalDept1.jsp";
	private final String auditByQualitydept = "/jsp/vehicleInfoManage/apply/auditByQualitydept.jsp";
	private final String techDeptInit = "/jsp/vehicleInfoManage/apply/techDeptInit.jsp";
	private final String qualiDeptInit = "/jsp/vehicleInfoManage/apply/qualiDeptInit.jsp";
	private QualityReportInfoDao dao =QualityReportInfoDao.getInstance();
	AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	/**
	 * 技术部门列表页面跳转
	 *//*
	public void technicalSupportDeptInit() {
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportDept"), "技术部门列表页面跳转");
	}*/
	
	/**
	 * 质量信息列表页面跳转（一级）
	 */
	public void technicalSupportDeptInit() {
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportDept"), "质量信息列表页面跳转（一级）");
	}
	
	
	/**
	 * 质量信息列表页面跳转（二级）
	 */
	public void technicalSupportDeptTwoInit() {
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportDeptTwo"), "质量信息列表页面跳转（二级）");
	}
	
	/**
	 * 质量信息查询页面跳转
	 */
	public void technicalSupportQueryInit() {
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportQuery"), "质量信息查询页面跳转");
	}
	/**
	 * 质量信息查询（一级）
	 */
	public void technicalSupportDeptInfo() {
		PageResult<Map<String,Object>> list=dao.technicalSupportDeptInfo(request,getCurrDealerId(),Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps",list);
	}
	
	/**
	 * 质量信息查询（二级）
	 */
	public void technicalSupportDeptTwoInfo() {
		PageResult<Map<String,Object>> list=dao.technicalSupportDeptInfo(request,getCurrDealerId(),Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps",list);
	}
	/**
	 * 技术部支持审批导出
	 */
	public void  exportToexceltechnical(){
		dao.exportToexceltechnical(request,loginUser,act,Constant.PAGE_SIZE_MAX,getCurrPage());
		
	}
	/**
	 * 质量部门列表页面跳转
	 */
	public void qualityDeptInit() {
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "qualityDept"), "质量部门列表页面跳转");
	}
	/**
	 * 质量部门列表数据
	 */
	public void qualityDeptInfo() {
		PageResult<Map<String,Object>> list=dao.qualityDeptInfo(request,getCurrDealerId(),Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps",list);
	}
	/**
	 * 技术部门审批通过
	 */
	public void auditPassByTechnicalDept() {
		int i=dao.auditPassByTechnicalDept(request,loginUser);
		if(i==0){
			super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportDept"), "技术部门列表页面跳转");
		}
	}
	/**
	 * 技术部门审批驳回
	 */
	public void auditRefuseByTechnicalDept() {
		int i = dao.auditRefuseByTechnicalDept(request,loginUser);
		if(i==0){
			super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "technicalSupportDept"), "技术部门列表页面跳转");
		}
	}
	/**
	 * 质量部门审批通过
	 */
	public void auditPassByQualityDept() {
		int i = dao.auditPassByQualityDept(request,loginUser);
		if(i==0){
			super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "qualityDept"), "质量部门列表页面跳转");
			
		}
	}
	/**
	 * 质量部门审批驳回（回复）
	 */
	public void auditRefuseByQualityDept() {
		int i = dao.auditRefuseByQualityDept(request,loginUser);
		if(i==0){
			super.sendMsgByUrl(sendUrl(QualityReportInfo.class, "qualityDept"), "质量部门列表页面跳转");
		}
	}
	/**
	 * 质量部门审批驳回
	 */
	@SuppressWarnings("unchecked")
	public void auditRefuseByQualityDept1() {
		try {
			String qualityid = DaoFactory.getParam(request,"qualityid");
			String verifyStatus = DaoFactory.getParam(request,"verifyStatus");
			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			
			dao.update(po, po2);
			dao.updateFlow(qualityid, loginUser, 1,verifyStatus);//审核记录
			this.sendMsgByUrl(null,qualiDeptInit, "质量部门列表页面跳转");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 质量部门审批通过
	 */
	@SuppressWarnings("unchecked")
	public void auditPassByQualityDept1() {
		try {
			String qualityid = DaoFactory.getParam(request,"qualityid");
			String verifyStatus = DaoFactory.getParam(request,"verifyStatus");
			String applyNews = DaoFactory.getParam(request,"applyNews");
			
			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			po2.setApplyNews(applyNews);
			
			dao.update(po, po2);
			dao.updateFlow(qualityid, loginUser, 1,verifyStatus);//审核记录
			this.sendMsgByUrl(null,qualiDeptInit, "质量部门列表页面跳转");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return
	 */
	public void techDeptInit() {
		this.sendMsgByUrl(null,techDeptInit, "技术部门列表页面跳转");
	}
	public void data1() {
		
		Integer currPage = getCurrPage();
		PageResult<Map<String,Object>> list=dao.data1(request,getCurrDealerId(),Constant.PAGE_SIZE,currPage);
		act.setOutData("ps",list);
	}
	public void data2() {
		
		Integer currPage = getCurrPage();
		PageResult<Map<String,Object>> list=dao.data2(request,getCurrDealerId(),Constant.PAGE_SIZE,currPage);
		act.setOutData("ps",list);
	}
	/**
	 * @return
	 */
	public void qualiDeptInit() {
		this.sendMsgByUrl(null,qualiDeptInit, "质量部门列表页面跳转");
	}
	

	/**
	 * 质量部门审批
	 */
	public void auditByQualitydept() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,auditByQualitydept, "质量部门审批");
	}
	/**
	 * 质量信息审核
	 */
	public void auditByTechnicalDept() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,auditByTechnicalDept, "技术部门审批");
	}
	/**
	 * 质量部门审批（车厂）
	 */
	public void audit1() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,auditByTechnicalDept1, "质量部门审批");
	}
	
	/**
	 * 删除
	 */
	@SuppressWarnings("unchecked")
	public void delQualityInfoReport() {
			String qualityid = DaoFactory.getParam(request,"qualityid");
			
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO(Long.parseLong(qualityid));
			dao.delete(po);
			String ywzj = String.valueOf(po.getQualityId());
			String[] fjids = request.getParamValues("fjid");
			try {
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			act.setOutData("success", "true");
			this.sendMsgByUrl(null,qualityReportInfoList, "跳转服务商列表页面");
	}
	/**
	 * 删除1
	 */
	@SuppressWarnings("unchecked")
	public void delQualityInfoReport1() {
		String qualityid = DaoFactory.getParam(request,"qualityid");
		
		TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO(Long.parseLong(qualityid));
		dao.delete(po);
		String ywzj = String.valueOf(po.getQualityId());
		String[] fjids = request.getParamValues("fjid");
		try {
			FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		act.setOutData("success", "true");
		this.sendMsgByUrl(null,techDeptInit, "跳转服务商列表页面");
	}
	/**
	 * view页面
	 */
	public void viewQualityInfoReport4() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,qualityReportInfoView4, "跳转view页面");
		
	}
	/**
	 * view页面
	 */
	public void viewQualityInfoReport() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,qualityReportInfoView, "跳转view页面");
		
	}
	/**
	 * view页面
	 */
	public void viewQualityInfoReport1() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,qualityReportInfoView1, "跳转view页面");
		
	}
	/**
	 * view页面
	 */
	public void viewQualityInfoReport2() {
		this.selectPoByIdOutData();
		String type= DaoFactory.getParam(request, "type");
		if ("hide".equals(type)) {
			act.setOutData("type", type);
		}
		this.sendMsgByUrl(null,qualityReportInfoView2, "跳转view页面");
		
	}
	/**
	 * 公共的查询和信息outData
	 * @param qualityid 
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void selectPoByIdOutData() {
		String qualityid = DaoFactory.getParam(request,"qualityid");
		String updateId = DaoFactory.getParam(request,"updateId");
		TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
		po.setQualityId(Long.parseLong(qualityid));
		po=(TtSalesQualityReportInfoPO) dao.select(po).get(0);
		Long currDealerId = getCurrDealerId();
		if(Utility.testString(currDealerId==null?"":currDealerId.toString())){
			TmDealerPO t=new TmDealerPO();
			t.setDealerId(currDealerId);
			List list = dao.select(t);
			if(list!=null&& list.size()>0){
				TmDealerPO temp = (TmDealerPO) list.get(0);
				po.setDealerCode(temp.getDealerCode());//经销商code
				po.setDealerName(temp.getDealerShortname());//经销商名称
			}
		}
		act.setOutData("updateId",updateId);
		act.setOutData("po", po);
		act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(qualityid)));
		List<FsFileuploadPO> fileList = dao.queryAttById(qualityid);// 取得附件
		act.setOutData("fileList", fileList);
	}
	/**
	 * 修改跳转
	 */
	public void updateQualityInfoReport() {
		this.selectPoByIdOutData();
		String type = request.getParamValue("type");
		act.setOutData("type", type);
		this.sendMsgByUrl(null,saveOrupdateInfoReport, "修改跳转");
	}
	/**
	 * 修改跳转
	 */
	public void updateQualityInfoReport1() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,saveOrupdateInfoReport1, "修改跳转");
	}
	/**
	 * 打印跳转
	 */
	public void printQualityInfoReport() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,printQualityInfoReport, "打印跳转");
	}
	/**
	 * 打印跳转
	 */
	public void printQualityInfoReport1() {
		this.selectPoByIdOutData();
		this.sendMsgByUrl(null,printQualityInfoReport1, "打印跳转");
	}
	
	/**
	 * 跳转服务商列表页面
	 */
	public void qualityReportInfoInit() {
		this.sendMsgByUrl(null,qualityReportInfoList, "跳转服务商列表页面");
	}
	
	 /**
     * 分页查询索赔的单号
     */
    public void queryVINInfoList() {
    		
    		PageResult<Map<String, Object>> ps = dao.queryVINInfoList(request,getCurrDealerId(),getCurrPage(), 15);
    		act.setOutData("ps",ps);
    		this.sendMsgByUrl(null,null, "分页查询索赔的单号");
    }
    /**
     *质量信息申报VIN带出查询
     */
    public void queryDataByVin() {
    	response.setContentType("application/json");
    	Map<String,Object> data  = dao.queryDataByVin(request);
    	act.setOutData("queryDataByVin", data);
    	this.sendMsgByUrl(null,null, "质量信息申报VIN带出查询");
    }
    /**
     * 查询主故障件代码
     */
    public void selectmalfunction() {
		PageResult<Map<String, Object>> ps = dao.selectmalfunction(request,getCurrDealerId(),getCurrPage(), 15);
		act.setOutData("ps",ps);
		this.sendMsgByUrl(null,null, "分页查询主故障件代码");
}
	/**
	 * 服务商上列表数据
	 */
	public void qualityReportInfo() {
		Integer currPage = getCurrPage();
		PageResult<Map<String,Object>> list=dao.qualityReportInfo(request,getCurrDealerId(),Constant.PAGE_SIZE,currPage);
		act.setOutData("ps",list);
	}
	/**
	 * 服务商上报新增或修改数据
	 */
	public void saveOrupdateInfo() {
		String qualityId = DaoFactory.getParam(request,"qualityid");
		
		String[] fjids=request.getParamValues("fjid");
		//新增or修改
		if(!"".equals(qualityId)){
			 this.selectPoByIdOutData();
		}
		qualityId = dao.saveOrupdate(request,getCurrDealerId(),qualityId,loginUser,fjids);
		this.sendMsgByUrl(null,qualityReportInfoList, "服务商上列表数据");
	}
	public void saveOrupdateInfo1() {
		String qualityId = DaoFactory.getParam(request,"qualityid");
		
		String[] fjids=request.getParamValues("fjid");
		//新增or修改
		if(!"".equals(qualityId)){
			this.selectPoByIdOutData();
		}
		qualityId = dao.saveOrupdate(request,getCurrDealerId(),qualityId,loginUser,fjids);
		this.sendMsgByUrl(null,techDeptInit, "服务商上列表数据");
	}
	
	public void saveOrupdateInfoReportInit(){
		act.setOutData("newdate", new Date());
		Map<String,Object> map=dao.getDealerInfo(loginUser.getDealerId());
		Map<String,Object> map1=new HashMap<String, Object>();
		map1.put("dealerCode", map.get("DEALERCODE").toString());
		map1.put("dealerName", map.get("DEALERNAME").toString());
		act.setOutData("po", map1);
		this.sendMsgByUrl(null,saveOrupdateInfoReport, "新增数据页面");
	}
	/**
	 * 数据上报
	 */
	public void saveOrupdateInfoReport() {
		String qualityId = DaoFactory.getParam(request,"qualityId");
		
		String[] fjids=request.getParamValues("fjid");
		//新增or修改
		if(!"".equals(qualityId)){
			this.selectPoByIdOutData();
		}
		qualityId = dao.saveOrupdate(request,getCurrDealerId(),qualityId,loginUser,fjids);
		this.sendMsgByUrl(null,qualityReportInfoList, "服务商上列表数据");
	}
	/**
	 * 审批历史查询
	 */
	public void auditRecord(){
		String qualityId = DaoFactory.getParam(request,"qualityid");
		List<Map<String, Object>> ps=dao.selectAuditRecord(qualityId);
		act.setOutData("list", ps);
		super.sendMsgByUrl(sendUrl(QualityReportInfo.class,"auditRecord"), "审批历史查询");
	}
	/**
	 * 公共的带数据信息的跳转
	 * @param data
	 * @param url
	 * @param msg
	 */
	public void sendMsgByUrl(PageResult<Map<String,Object>> data,String url, String msg) {
		try {
			act.setForword(url);
			act.setOutData("data",data);
		} catch (Exception e) {
			BizException bizexception = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, msg);
			logger.error(logger, bizexception);
			act.setException(bizexception);
		}
	}
	
	public void queryCarAlarm(){
		try {
			String vin =request.getParamValue("vin");
			HashMap params=new HashMap();
			params.put("vin", vin);
			PageResult<Map<String,Object>> list=dao.carAlarm(request,Constant.PAGE_SIZE,getCurrPage(),params);
			act.setOutData("ps", list);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"预警车辆查询");
			act.setException(e1);
		}
	}
	
	

}