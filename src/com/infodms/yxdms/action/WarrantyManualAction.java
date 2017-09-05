package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.yxdms.dao.ClaimBalanceDAO;
import com.infodms.yxdms.dao.ClaimDAO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.WarrantyManualService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.service.impl.WarrantyManualServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;

/**
 * 质保手册
 * @author yuewei
 *
 */
public class WarrantyManualAction extends BaseAction{
	
	private WarrantyManualService warrantymanualservice = new WarrantyManualServiceImpl();
	private OrderService orderservice=new OrderServiceImpl();
	private ClaimService claimservice=new ClaimServiceImpl();
	PageResult<Map<String, Object>> list=null;
	
	public void expotWarrantyData(){
		list=warrantymanualservice.findWarrantyData(request,loginUser, Constant.PAGE_SIZE_MAX, getCurrPage());
		warrantymanualservice.expotWarrantyData(act, list);
	}
	public void addWarrantyManualInit(){
		Map<String,Object> data=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",data);
		request.setAttribute("type", "add");
		sendMsgByUrl("warranty", "warranty_manual_add", "质保手册服务站新增");
	}
	
	public void auditWarrantyManual(){
		try {
		    int res=warrantymanualservice.auditWarrantyManual(loginUser,request);
			act.setOutData("succ", res);
       } catch (Exception e) {
    	    BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, e.getMessage());
			act.setException(e1);
		}
	}
	
	public void updateWarrantyManual(){
		Map<String,Object> userInfo=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",userInfo);
		Map<String,Object> data=warrantymanualservice.findWarrantyManual(loginUser,request);
		List<Map<String,Object>> list=warrantymanualservice.findWarrantyManualSub(loginUser,request);
		act.setOutData("t", data);
		act.setOutData("list", list);
		this.getFile("id");
		request.setAttribute("type", "update");
		sendMsgByUrl("warranty", "warranty_manual_add", "质保手册服务站修改");
	}
	
	public void auditWarrantyManualInit(){
		Map<String,Object> data=warrantymanualservice.findWarrantyManual(loginUser,request);
		List<Map<String,Object>> list=warrantymanualservice.findWarrantyManualSub(loginUser,request);
		act.setOutData("t", data);
		act.setOutData("list", list);
		this.getFile("id");
		request.setAttribute("type", "audit");
		sendMsgByUrl("warranty", "warranty_manual_audit", "质保手册车厂审核");
	}
	
	
	public void auditViewWarrantyManual(){
		Map<String,Object> data=warrantymanualservice.findWarrantyManual(loginUser,request);
		List<Map<String,Object>> list=warrantymanualservice.findWarrantyManualSub(loginUser,request);
		act.setOutData("t", data);
		act.setOutData("list", list);
		this.getFile("id");
		request.setAttribute("type", "view");
		sendMsgByUrl("warranty", "warranty_manual_view", "质保手册车厂明细");
	}
	public void viewWarrantyManual(){
		Map<String,Object> userInfo=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",userInfo);
		Map<String,Object> data=warrantymanualservice.findWarrantyManual(loginUser,request);
		List<Map<String,Object>> list=warrantymanualservice.findWarrantyManualSub(loginUser,request);
		act.setOutData("t", data);
		act.setOutData("list", list);
		this.getFile("id");
		request.setAttribute("type", "view");
		sendMsgByUrl("warranty", "warranty_manual_view", "质保手册服务站明细");
	}
	public void findInfoByVin(){
		Map<String,Object> data=warrantymanualservice.findInfoByVin(request);
		act.setOutData("vinInfo",data);
	}
	public void addWarrantyManual(){
		int res=warrantymanualservice.addWarrantyManual(request,loginUser);
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		setJsonSuccByres(res);
	}
	
	public void warrantyManualList(){
		String query = getParam("query");
		AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
		List<Map<String, Object>> orgList;
		try {
			orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ("true".equals(query)) {
			list = warrantymanualservice.warrantyManualList(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("warranty", "warranty_manual_list_dealer", "质保手册服务站查询");
	}
	
	public void warrantyManualListTemp(){
		ClaimDAO dao = ClaimDAO.getInstance();
		String query = getParam("query");
		String sql = "SELECT * FROM tm_org WHERE org_level = 2";
		List<Map<String, Object>> org = dao.pageQuery01(sql, null, dao.getFunName());
		act.setOutData("org", org);
		if ("true".equals(query)) {
			list = warrantymanualservice.warrantyManualList(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("warranty", "warranty_manual_list_temp", "质保手册车厂查询");
	}
	
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
}
