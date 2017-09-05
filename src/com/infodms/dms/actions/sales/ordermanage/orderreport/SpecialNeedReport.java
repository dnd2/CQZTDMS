/**********************************************************************
* <pre>
* FILE : SpecialNeedReport.java
* CLASS : SpecialNeedReport
* AUTHOR : 
* FUNCTION : 订单提报
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-12|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderreport;


import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 订做车需求提报Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-12
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class SpecialNeedReport {
	
	public Logger logger = Logger.getLogger(SpecialNeedReport.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderreport/specialNeedReportQuery.jsp";
	private final String addUrl = "/jsp/sales/ordermanage/orderreport/specialNeedAdd.jsp";
	private final String modUrl = "/jsp/sales/ordermanage/orderreport/specialNeedModify.jsp";
	/**
	 * 订做车需求提报页面初始化
	 */
	public void specialNeedReportInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			String flag = request.getParamValue("flag");
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			
			act.setOutData("flag",flag);
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求提报页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求提报查询
	 */
	public void specialNeedReportQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			String dealerId = logonUser.getDealerId().toString();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			if("-1".equals(areaId)) {
				area = "";
				dealerId = "";
				for(int i=0; i<areaList.size(); i++) {
					if(i != areaList.size()-1) {
						area += (areaList.get(i).get("AREA_ID") + ",");
						dealerId += (areaList.get(i).get("DEALER_ID") + ",");
					} else {
						area += areaList.get(i).get("AREA_ID");
						dealerId += areaList.get(i).get("DEALER_ID");
					}
				}
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedList(areaId, area,dealerId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求提报页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求新增页面初始化
	 */
	public void specialNeedAddInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(addUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"订做车需求新增页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求新增页面物料组查询
	 */
	public void addMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String materialCode = request.getParamValue("materialCode");		//物料CODE
			String addedMaterialId = request.getParamValue("addedMaterialId"); //已添加配置ID
			List<Map<String, Object>> list = dao.getMaterialGroupInfo(materialCode, addedMaterialId);
			act.setOutData("info", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "订做车需求新增页面物料查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求新增保存
	 */
	public void specialNeedAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String materialIds = request.getParamValue("materialIds");						//物料ID
			String amounts = request.getParamValue("amounts");								//需求数量
			String fleetId = request.getParamValue("fleetId");								//集团客户id 
			String remark = request.getParamValue("remark");								//改装说明
			String area = request.getParamValue("area");									//业务范围
			String dealerId = request.getParamValue("dealerId");							//经销商ID
			String subFlag = request.getParamValue("subFlag");								//1 保存 2 提报 
			String[] fjid = request.getParamValues("fjid");									//新增的附件id
			String [] materialId = materialIds.split(",");
			String [] amount = amounts.split(",");
			Boolean isSecond = dao.viewDealerLever(dealerId) ;
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			
			TtVsSpecialReqPO tvsrp = new TtVsSpecialReqPO();
			Long reqId = Long.parseLong(SequenceManager.getSequence(""));
			if (null != fjid && fjid.length>0) {
				for (int i = 0; i < fjid.length; i++) {
					if (null != fjid[i] && !"".equals(fjid[i])) {
						FsFileuploadPO tempFileuploadPO = new FsFileuploadPO();
						tempFileuploadPO.setFjid(Long.parseLong(fjid[i]));
						FsFileuploadPO valueFileuploadPO = new FsFileuploadPO();
						valueFileuploadPO.setYwzj(reqId);
						dao.update(tempFileuploadPO, valueFileuploadPO);
					}
				}
			}
			tvsrp.setReqId(reqId);
			tvsrp.setAreaId(Long.parseLong(area));
			
			if(!CommonUtils.isNullString(productId)) {
				tvsrp.setProductComboId(Long.parseLong(productId)) ;
			}
			
			if(Utility.testString(fleetId)){
				tvsrp.setFleetId(new Long(fleetId));
			}
			tvsrp.setRefitDesc(remark);
			if(subFlag.equals("1")){
				tvsrp.setReqStatus(Constant.SPECIAL_NEED_STATUS_01);//保存
			}else{
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				// 获得是否需要大客户审核开关
				TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.SPECIAL_NEED_FLEET_CHECK_PARA, oemCompanyId);
				if(isSecond) {
					tvsrp.setReqStatus(Constant.SPECIAL_NEED_STATUS_10);//待上级经销商审核
				} else {
					if(para != null && para.getParaValue().equals("1")){
						if(!CommonUtils.isNullString(fleetId)) {
							tvsrp.setReqStatus(Constant.SPECIAL_NEED_STATUS_09);//待大客户部审核
						} else {
							tvsrp.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);//提报
						}
					}
					else{
						tvsrp.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);//提报
					}
				}
				
				tvsrp.setReqDate(new Date(System.currentTimeMillis()));
			}
			
			tvsrp.setDealerId(Long.parseLong(dealerId));
			tvsrp.setVer(0);
			tvsrp.setCreateBy(logonUser.getUserId());
			tvsrp.setCreateDate(new Date(System.currentTimeMillis()));
			dao.insert(tvsrp);						//插入订做车需求表
			for(int i=0; i<materialId.length; i++){
				TtVsSpecialReqDtlPO tvsrdp = new TtVsSpecialReqDtlPO();
				tvsrdp.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
				tvsrdp.setMaterialId(Long.parseLong(materialId[i]));
				tvsrdp.setReqId(tvsrp.getReqId());
				tvsrdp.setAmount(Integer.parseInt(amount[i]));
				tvsrdp.setCreateBy(logonUser.getUserId());
				tvsrdp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvsrdp);					//插入订做车需求明细表
			}
			if(subFlag.equals("1")){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "订做车需求新增保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求删除	
	 */
	public void specialNeedDel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");			//需求ID
			TtVsSpecialReqDtlPO tvsrdp = new TtVsSpecialReqDtlPO();
			TtVsSpecialReqPO tvsrp = new TtVsSpecialReqPO();
			tvsrdp.setReqId(Long.parseLong(reqId));
			dao.delete(tvsrdp);
			tvsrp.setReqId(Long.parseLong(reqId));
			dao.delete(tvsrp);
			FsFileuploadPO tempFileuploadPO = new FsFileuploadPO();
			tempFileuploadPO.setYwzj(Long.parseLong(reqId));
			dao.delete(tempFileuploadPO);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "订做车需求新增保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求提报	
	 */
	public void SpecialNeedReportSubmit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");			//需求ID
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			String fleetId = request.getParamValue("fleetId");								//集团客户id 
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 获得是否需要大客户审核开关
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.SPECIAL_NEED_FLEET_CHECK_PARA, oemCompanyId);
			//查询经销商等级
			
			Boolean boo = dao.viewDealerReq(reqId);
			if(boo){
				tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_10);//提报
			} else{
				if(para != null && para.getParaValue().equals("1")){
					if(!CommonUtils.isNullString(fleetId)) {
						tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_09);//待大客户部审核
					} else {
						tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);//提报
					}
				}
				else{
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);//提报
				}
			}
			tvsrpValue.setReqDate(new Date(System.currentTimeMillis()));
			tvsrpValue.setUpdateBy(logonUser.getUserId());
			tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
			dao.update(tvsrpContion, tvsrpValue);
			act.setOutData("id", reqId);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "订做车需求新增保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求修改页面初始化	
	 */
	public void SpecialNeedModifyInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			String ver = request.getParamValue("ver");
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			String areaId = tvsrpValue.getAreaId().toString();
			List<Map<String, Object>> list = dao.getSpecialNeedDetail(reqId);
			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			List<Map<String, Object>> attachList = dao.getAttachInfos(reqId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			String fleetId = "";
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetId = fleet.getFleetId().toString();
					fleetName = fleet.getFleetName();
				}
			}
			
			TtVsSpecialReqPO result = (TtVsSpecialReqPO) dao.select(tvsrpContion).get(0);
			Integer reqStatus = result.getReqStatus();
			act.setOutData("productId", tvsrpValue.getProductComboId() == null ? "" : tvsrpValue.getProductComboId().toString()) ;
			act.setOutData("list", list);
			act.setOutData("checkList", checkList);
			act.setOutData("areaList", areaList);
			act.setOutData("remark", remark);
			act.setOutData("areaId", areaId);
			act.setOutData("reqId", reqId);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("ver", ver);
			act.setOutData("fleetId", fleetId);
			act.setOutData("fleetName", fleetName);
			act.setForword(modUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.DETAIL_FAILURE_CODE, "订做车需求修改页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求修改保存	
	 */
	public void specialNeedModify(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");									//需求ID
			String ver = request.getParamValue("ver");
			String materialIds = request.getParamValue("materialIds");						//物料ID
			String amounts = request.getParamValue("amounts");								//需求数量
			String remark = request.getParamValue("remark");								//改装说明
			String fleetId = request.getParamValue("fleetId");								//集团客户id
			String area = request.getParamValue("area");									//业务范围
			String dealerId = request.getParamValue("dealerId");							//经销商ID
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			String delFileId = CommonUtils.checkNull(request.getParamValue("delFileIds"));	//要删除的附件id
			String[] fjid = request.getParamValues("fjid");									//新增的附件id
			String reqStatus = request.getParamValue("reqStatus");							//状态
			String[] delFileIds = delFileId.split(",");
			if (null != delFileIds && delFileIds.length>0) {
				for (int i = 0; i < delFileIds.length; i++) {
					if (null != delFileIds[i] && !"".equals(delFileIds[i])) {
						FsFileuploadPO fileuploadPO = new FsFileuploadPO();
						fileuploadPO.setFjid(Long.parseLong(delFileIds[i]));
						dao.delete(fileuploadPO);
					}
					
				}
			}
			if (null != fjid && fjid.length>0) {
				for (int i = 0; i < fjid.length; i++) {
					if (null != fjid[i] && !"".equals(fjid[i])) {
						FsFileuploadPO tempFileuploadPO = new FsFileuploadPO();
						tempFileuploadPO.setFjid(Long.parseLong(fjid[i]));
						FsFileuploadPO valueFileuploadPO = new FsFileuploadPO();
						valueFileuploadPO.setYwzj(Long.parseLong(reqId));
						dao.update(tempFileuploadPO, valueFileuploadPO);
					}
				}
			}
			
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_SPECIAL_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				String [] materialId = materialIds.split(",");
				String [] amount = amounts.split(",");
				TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
				TtVsSpecialReqDtlPO tvsrdpContion = new TtVsSpecialReqDtlPO();
				tvsrpContion.setReqId(Long.parseLong(reqId));
				if(Utility.testString(fleetId)){
					tvsrpValue.setFleetId(new Long(fleetId));
				}
				tvsrpValue.setRefitDesc(remark);
				tvsrpValue.setAreaId(Long.parseLong(area));
				tvsrpValue.setVer(Integer.parseInt(ver)+1);
				/*if(Constant.SPECIAL_NEED_STATUS_10.toString().equals(reqStatus)) {
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);
				}
				if(!Constant.SPECIAL_NEED_STATUS_10.toString().equals(reqStatus)) {
					tvsrpValue.setDealerId(Long.parseLong(dealerId));
				}*/
				tvsrpValue.setUpdateBy(logonUser.getUserId());
				tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				if(!CommonUtils.isNullString(productId)) {
					tvsrpValue.setProductComboId(Long.parseLong(productId)) ;
				}
				dao.update(tvsrpContion, tvsrpValue);	//更新订做车需求表
				
				tvsrdpContion.setReqId(Long.parseLong(reqId));
				dao.delete(tvsrdpContion);				//删除订做车需求明细表数据
				
				for(int i=0; i<materialId.length; i++){
					TtVsSpecialReqDtlPO tvsrdp = new TtVsSpecialReqDtlPO();
					tvsrdp.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
					tvsrdp.setMaterialId(Long.parseLong(materialId[i]));
					tvsrdp.setReqId(Long.parseLong(reqId));
					tvsrdp.setAmount(Integer.parseInt(amount[i]));
					tvsrdp.setCreateBy(logonUser.getUserId());
					tvsrdp.setCreateDate(new Date(System.currentTimeMillis()));
					dao.insert(tvsrdp);					//插入订做车需求明细表
				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "订做车需求修改保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void viewDelaerLever(){
		String delaerId = request.getParamValue("dealerId");			//需求ID
		Boolean boo = dao.viewDealerLever(delaerId);
		act.setOutData("boo", boo);
	}
}
