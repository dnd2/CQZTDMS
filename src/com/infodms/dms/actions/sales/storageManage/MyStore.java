package com.infodms.dms.actions.sales.storageManage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.storageManage.StoreQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerWarehousePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class MyStore {
	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	private final String MyStoreInit = "/jsp/sales/storageManage/storeQueryProduct.jsp";
	private final String MyStoreAddInit="/jsp/sales/storageManage/storeAddMyStore.jsp";
	private final String MyStoreModifyInit="/jsp/sales/storageManage/storeModifyMyStore.jsp";
	StoreQueryDAO dao = new StoreQueryDAO();//此DAO继承了 baseDAO
	/*
	 * 初始页面加载数据
	 */
	public void getMyStore() {
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		logonUser.getCompanyId();
		logonUser.getOemCompanyId();
		try {
			String warehouseName = request.getParamValue("warehouseName");
			String warehouseStatus = request.getParamValue("warehouseStatus");
			String warehouseType = request.getParamValue("warehouseType");
			String dealerId = request.getParamValue("dealer") ;
			String lowDealerId = request.getParamValue("lowID") ;
			if(dealerId == null) {
				dealerId = "" ;
			}
			if(lowDealerId == null) {
				lowDealerId = "" ;
			}
			int pageSize = Constant.PAGE_SIZE;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;// 页码
					
			PageResult<Map<String, Object>> ps = dao.getStoreList(dealerId,lowDealerId,logonUser.getCompanyId(),
					warehouseName, warehouseType, warehouseStatus, curPage,
					pageSize);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商仓库维护数据加载失败！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
/*
 * 经销商仓库维护数据跳转
 */
	public void detailMyStore() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds__) ;
			act.setOutData("dealerList", dealerList) ;
			List<Map<String, Object>> lowDelList = DealerInfoDao.getInstance().getLowDelaerInfo(dealerIds__) ;
			
			//Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId())) ;
			
			//act.setOutData("oemFlag", oemFlag) ;
			act.setOutData("lowDelList", lowDelList) ;
			act.setForword(MyStoreInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商仓库维护数据跳转失败！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 添加经销商页面跳转
	 */
	
	public void addInitMyStore(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds__) ;
			act.setOutData("dealerList", dealerList) ;
			List<Map<String, Object>> lowDelList = DealerInfoDao.getInstance().getLowDelaerInfo(dealerIds__) ;
			act.setOutData("lowDelList", lowDelList) ;
		act.setForword(MyStoreAddInit);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商仓库维护数据添加跳转失败！");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
	}
	/*
	 * 添加经销商仓库
	 */
	public void addMyStore(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request=act.getRequest();
		String warehouseName =CommonUtils.checkNull(request.getParamValue("warehouseName"));
		String warehouseStatus = CommonUtils.checkNull(request.getParamValue("warehouseStatus"));
		String warehouseType = CommonUtils.checkNull(request.getParamValue("warehouseType"));
		String remark=CommonUtils.checkNull(request.getParamValue("remark"));
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealer"));
		String lowDelId = CommonUtils.checkNull(request.getParamValue("lowDel"));
		TmDealerPO dealerPo=new TmDealerPO();
		dealerPo.setDealerCode(lowDelId);
		TmDealerPO dealerPo1=null;
		if(dao.select(dealerPo).size()>0){
		dealerPo1=(TmDealerPO)dao.select(dealerPo).get(0);
		}
		
		TmDealerWarehousePO po=new TmDealerWarehousePO();
		Date currTime=new Date(System.currentTimeMillis());
		po.setWarehouseName(warehouseName);
		if(warehouseType!=""){
		po.setWarehouseType(Integer.parseInt(warehouseType));
		}else{
			po.setWarehouseType(-1);
		}
		if(warehouseStatus!=""){
		po.setStatus(Integer.parseInt(warehouseStatus));
		}else{
			po.setStatus(-1);
		}
		if(!"".equals(dealerId)){
			po.setDealerId(Long.parseLong(dealerId)) ;
		}
		if(!Constant.DEALER_WAREHOUSE_TYPE_01.toString().equals(warehouseType) && !"".equals(lowDelId)) {
			po.setManageDealerId(Long.parseLong(dealerPo1.getDealerId().toString()));
		}
		po.setRemark(remark);
		po.setCreateDate(currTime);
		po.setCreateBy(logonUser.getUserId());
		po.setDealerComanyId(logonUser.getCompanyId());
		po.setCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
		String id=SequenceManager.getSequence("");
		po.setWarehouseId(Long.parseLong(id));
		dao.insert(po);
		Long poseId = logonUser.getPoseId();
		Long comId = logonUser.getCompanyId() ;
		List<Map<String, Object>> areaList = MaterialGroupManagerDao
				.getDealerId(comId.toString(),poseId.toString());
		String dealerIds__ = "";
		for(int i=0; i<areaList.size();i++) {
			dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
		}
		dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
		List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds__) ;
		act.setOutData("dealerList", dealerList) ;
		List<Map<String, Object>> lowDelList = DealerInfoDao.getInstance().getLowDelaerInfo(dealerIds__) ;
		act.setOutData("lowDelList", lowDelList) ;
		act.setForword(MyStoreInit);
	}
	/*
	 * 修改经销商仓库跳转
	 */
	public void modInitMyStore(){
		RequestWrapper request=act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
	try{
		String warehouseId =CommonUtils.checkNull(request.getParamValue("warehouseId"));
		TmDealerWarehousePO po=new TmDealerWarehousePO();
		po.setWarehouseId(Long.parseLong(warehouseId));
		Long poseId = logonUser.getPoseId();
		Long comId = logonUser.getCompanyId() ;
		List<Map<String, Object>> areaList = MaterialGroupManagerDao
				.getDealerId(comId.toString(),poseId.toString());
		String dealerIds__ = "";
		for(int i=0; i<areaList.size();i++) {
			dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
		}
		dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
		List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds__) ;
		act.setOutData("dealerList", dealerList) ;
		List<Map<String, Object>> lowDelList = DealerInfoDao.getInstance().getLowDelaerInfo(dealerIds__) ;
		act.setOutData("lowDelList", lowDelList) ;
		TmDealerWarehousePO tmDealerWarehousePO=(TmDealerWarehousePO) dao.select(po).get(0);
		TmDealerPO dealerPo=new TmDealerPO();
		dealerPo.setDealerId(tmDealerWarehousePO.getManageDealerId());
		TmDealerPO dealerPo1=null;
		if(dao.select(dealerPo).size()>0){
		dealerPo1=(TmDealerPO)dao.select(dealerPo).get(0);
		}
		act.setOutData("dealerPo1", dealerPo1);
		act.setOutData("tmDealerWarehousePO",tmDealerWarehousePO);
		act.setForword(MyStoreModifyInit);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商仓库维护数据修改跳转失败！");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
	}
	/*
	 * 修改经销商仓库
	 */
	public void modMyStore(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request=act.getRequest();
		String warehouseName = CommonUtils.checkNull(request.getParamValue("warehouseName"));
		String warehouseStatus =CommonUtils.checkNull(request.getParamValue("warehouseStatus"));
		String warehouseType = CommonUtils.checkNull(request.getParamValue("warehouseType"));
		String remark=CommonUtils.checkNull(request.getParamValue("remark"));
		String wareHouseId=CommonUtils.checkNull(request.getParamValue("warehouseId"));
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealer"));
		String lowDel = CommonUtils.checkNull(request.getParamValue("lowDel"));
		TmDealerPO dealerPo=new TmDealerPO();
		dealerPo.setDealerCode(lowDel);
		TmDealerPO dealerPo1=null;
		if(dao.select(dealerPo).size()>0){
		dealerPo1=(TmDealerPO)dao.select(dealerPo).get(0);
		}
		TmDealerWarehousePO po1=new TmDealerWarehousePO();
		po1.setWarehouseId(Long.parseLong(wareHouseId));
		TmDealerWarehousePO po=new TmDealerWarehousePO();
		po.setWarehouseName(warehouseName);
		if(warehouseType!=""){
		po.setWarehouseType(Integer.parseInt(warehouseType));
		}else{
			po.setWarehouseType(-1);
		}
		if(warehouseStatus!=""){
		po.setStatus(Integer.parseInt(warehouseStatus));
		}else{
			po.setStatus(-1);
		}
		if (!"".equals(dealerId)) {
			po.setDealerId(Long.parseLong(dealerId)) ;
		} 
		if (Constant.DEALER_WAREHOUSE_TYPE_01.toString().equals(warehouseType)) {
			po.setManageDealerId(new Long("-1")) ;
		} else {
			po.setManageDealerId(dealerPo1.getDealerId()) ;
		}
		
		po.setRemark(remark);
		dao.update(po1,po);
		Long poseId = logonUser.getPoseId();
		Long comId = logonUser.getCompanyId() ;
		List<Map<String, Object>> areaList = MaterialGroupManagerDao
				.getDealerId(comId.toString(),poseId.toString());
		String dealerIds__ = "";
		for(int i=0; i<areaList.size();i++) {
			dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
		}
		dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
		List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds__) ;
		act.setOutData("dealerList", dealerList) ;
		List<Map<String, Object>> lowDelList = DealerInfoDao.getInstance().getLowDelaerInfo(dealerIds__) ;
		act.setOutData("lowDelList", lowDelList) ;
		act.setForword(MyStoreInit);
	}
}
