package com.infodms.dms.service.ontheway;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.sales.storage.ontheway.OnTheWayDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtSalesWayAddressLogPO;
import com.infodms.dms.po.TtSalesWayBillAddressPO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.service.DriverAuditService;
import com.infodms.dms.service.impl.DriverAuditServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OnTheWayServiceImpl implements  OnTheWayService {
	
	private static final OnTheWayDao dao = OnTheWayDao.getInstance();

	public PageResult<Map<String, Object>> getOnTheWayList(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) throws Exception {
		return dao.getOnTheWayList(request,loginUser,pageSize,currPage);
	}
    
	public PageResult<Map<String, Object>> getTtSalesWayBillDtlpo(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) throws Exception {
		return dao.getTtSalesWayBillDtlpo(request,loginUser,pageSize,currPage);
	}
   
	public PageResult<Map<String, Object>> getTtSalesWaybill(RequestWrapper request, Integer page_size,
			Integer curr_page) {
		return dao.getTtSalesWaybill( request,  page_size,curr_page);
	}

	public List<Map<String, Object>> getTtSalesWayBillDtl(RequestWrapper request) {
		return dao.getTtSalesWayBillDtl(request);
	}

	@SuppressWarnings("unchecked")
	public TtSalesWaybillPO getTtSalesWaybillPO(Long bill_id) {
		TtSalesWaybillPO salesWaybillPO  =  new  TtSalesWaybillPO();
		salesWaybillPO.setBillId(bill_id);
		List<TtSalesWaybillPO> list = dao.select(salesWaybillPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public TtSalesLogiPO getTtSalesLogiPOByLogiId(Long logiId) {
		TtSalesLogiPO logiPO =  new TtSalesLogiPO();
		logiPO.setLogiId(logiId);
		List<TtSalesLogiPO> list = dao.select(logiPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}

	public List<Map<String, Object>> getMateriaDetail(String billId) {
		return dao.getMateriaDetail(billId);
	}

	public Map<String, Object> getTtSalesWaybillByBillId(RequestWrapper request) {
		return dao.getTtSalesWaybillByBillId(request);
	}

	@SuppressWarnings("unchecked")
	public String saveOntheWayAddress(RequestWrapper request, AclUserBean loginUser) throws Exception {
		String dtl_ids = DaoFactory.getParam(request, "dtl_ids");
		if(StringUtils.isEmpty(dtl_ids)){
			throw  new Exception("没有获取到需要添加地址的车辆信息！");
		}
		String address = DaoFactory.getParam(request, "address");
		String[] ids = dtl_ids.split(",");
		TtSalesWayBillAddressPO addressPO ;
		String errinfo ="";
		List<TtSalesWayBillAddressPO> list = new ArrayList<TtSalesWayBillAddressPO>();
		List<TtSalesWayBillDtlPO> Waybilldtllist = new ArrayList<TtSalesWayBillDtlPO>();
		for (String dtl_id: ids) {
			TtSalesWayBillDtlPO billDtlPO = this.getTtSalesWayBillDtlPO(Long.valueOf(dtl_id));
//			if(null==billDtlPO.getStatus()||0==billDtlPO.getStatus()){
//				errinfo += "车架号："+billDtlPO.getVin()+",没有绑定司机，不允许上报;";
//				continue;
//			}
			addressPO = new TtSalesWayBillAddressPO();
			addressPO.setAddressId(DaoFactory.getPkId());
			addressPO.setAddress(address);
			addressPO.setBillId(billDtlPO.getBillId());
			addressPO.setDtlId(billDtlPO.getDtlId());
			addressPO.setVehicleId(billDtlPO.getVehicleId());
			addressPO.setCreateBy(loginUser.getUserId());
			Date date = new Date();
			addressPO.setCreateDate(date);
			addressPO.setVin(billDtlPO.getVin());
			addressPO.setAddressDate(date);
			addressPO.setIsSub(Constant.WAYBILL_DTL_STATUS_01);//在途
			list.add(addressPO);
			//更新明细表
			TtSalesWayBillDtlPO  temp = new TtSalesWayBillDtlPO();
			temp.setReportAddress(addressPO.getAddress());
			temp.setReportDate(date);
			temp.setDtlId(billDtlPO.getDtlId());
			temp.setUpdateBy(loginUser.getUserId());
			temp.setUpdateDate(new Date());
			temp.setStatus(Constant.WAYBILL_DTL_STATUS_01);//在途
			Waybilldtllist.add(temp);
		}
		
		if(StringUtils.isNotEmpty(errinfo)){
          throw new Exception(errinfo);			
		}else {
			dao.updateTtSalesWayBillDtlPOList(Waybilldtllist);//修改
			dao.insert(list);
		}
		
		
		return "SUCCESS";
	}

	@SuppressWarnings("unchecked")
	public TtSalesWayBillDtlPO getTtSalesWayBillDtlPO(Long dtl_id) {
		TtSalesWayBillDtlPO billDtlPO =  new TtSalesWayBillDtlPO();
		billDtlPO.setDtlId(dtl_id);
		List<TtSalesWayBillDtlPO> list = dao.select(billDtlPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
		     return	list.get(0);
		}else {
			return null;
		}
	}

	public List<Map<String, Object>> showOntheWayAddress(RequestWrapper request, AclUserBean loginUser)
			throws Exception {
		return dao.showOntheWayAddress(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public String updateTtSalesWayBillDtlStatusByDtlId(RequestWrapper request) throws Exception {
		TtSalesWayBillDtlPO billDtlPO = new TtSalesWayBillDtlPO();
		String dtl_id = DaoFactory.getParam(request, "dtl_id");
		String user_id = DaoFactory.getParam(request, "user_id");
		TcUserPO tcUserPO = new TcUserPO();
		tcUserPO.setUserId(Long.valueOf(user_id));
		List<TcUserPO> list2 = dao.select(tcUserPO);
		billDtlPO.setDtlId(Long.valueOf(dtl_id));
		List<TtSalesWayBillDtlPO> list = dao.select(billDtlPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			TtSalesWayBillDtlPO DtlPO = list.get(0);
			if(null!=DtlPO.getStatus()&&0!=DtlPO.getStatus()){
				return "ERROR";
			}else {
				//修改明细
				DtlPO.setStatus(Constant.WAYBILL_DTL_STATUS_01);
				DtlPO.setUpdateBy(Long.valueOf(user_id));
				DtlPO.setUpdateDate(new Date());
				DtlPO.setDriverUserId(Long.valueOf(user_id));
				DtlPO.setDriverBindDate(new Date());
				DtlPO.setDriverPhone(list2.get(0).getHandPhone());
				dao.updateTtSalesWayBillDtlPO(DtlPO);
			/*	//修改主表
				TtSalesWaybillPO ttSalesWaybillPO = new  TtSalesWaybillPO();
				ttSalesWaybillPO.setBillId(DtlPO.getBillId());
				ttSalesWaybillPO.se*/
				
				//增加绑定记录
				TtSalesWayAddressLogPO  logPO =  new TtSalesWayAddressLogPO();
				logPO.setLogId(DaoFactory.getPkId());
				logPO.setAfterStatus(Constant.WAYBILL_DTL_STATUS_01);
				logPO.setCreateBy(Long.valueOf(user_id));
				logPO.setCreateDate(new Date());
				logPO.setBusinessBy(Long.valueOf(user_id));
				logPO.setDtlId(Long.valueOf(dtl_id));
				logPO.setVehicleId(DtlPO.getVehicleId());
				dao.insert(logPO);
				//增加绑定记录
				
			}
			
		}
		return "SUCCESS";
	}

	@SuppressWarnings("unchecked")
	public String driverLocationReport(RequestWrapper request) throws Exception {
		String dtl_id =  DaoFactory.getParam(request, "dtl_id");
		String address = DaoFactory.getParam(request, "address");
		String user_id = DaoFactory.getParam(request, "user_id");
		TcUserPO tcUserPO = new TcUserPO();
		tcUserPO.setUserId(Long.valueOf(user_id));
		List<TcUserPO> list2 = dao.select(tcUserPO);
		String[] dtl_ids = dtl_id.split(",");
		List<TtSalesWayBillAddressPO> list = new ArrayList<TtSalesWayBillAddressPO>();
		TtSalesWayBillAddressPO addressPO;
		for (String dtl_id_e : dtl_ids) {
			TtSalesWayBillDtlPO billDtlPO =this.getTtSalesWayBillDtlPO(Long.valueOf(dtl_id_e));
			addressPO = new TtSalesWayBillAddressPO();
			addressPO.setAddress(address);
			addressPO.setAddressId(DaoFactory.getPkId());
			Date date = new Date();
			addressPO.setAddressDate(date);
			addressPO.setCreateBy(Long.valueOf(user_id));
			addressPO.setCreateDate(date);
			addressPO.setVehicleId(billDtlPO.getVehicleId());
			addressPO.setBillId(billDtlPO.getBillId());
			addressPO.setDtlId(billDtlPO.getDtlId());
			addressPO.setVin(billDtlPO.getVin());
			addressPO.setIsSub(Constant.WAYBILL_DTL_STATUS_01);
			TtSalesWaybillPO waybillPO = this.getTtSalesWaybillPO(billDtlPO.getBillId());
			addressPO.setBillNo(waybillPO.getBillNo());
			addressPO.setMatId(billDtlPO.getMatId());
			addressPO.setOrderNo(billDtlPO.getOrderNo());
			addressPO.setDriverPhone(list2.get(0).getHandPhone());
			list.add(addressPO);
			billDtlPO.setReportAddress(address);
			billDtlPO.setReportDate(date);
			billDtlPO.setUpdateBy(Long.parseLong(user_id));
			billDtlPO.setUpdateDate(new Date());
			billDtlPO.setStatus(Constant.WAYBILL_DTL_STATUS_01);//在途
			dao.updateTtSalesWayBillDtlPO(billDtlPO);
		}
		dao.insert(list);
		
		return "SUCCESS";
	}

	@SuppressWarnings("unchecked")
	public String leaveCarLocationReport(RequestWrapper request) throws Exception {
		
		String dtl_id =  DaoFactory.getParam(request, "dtl_id");
		String bill_id =  DaoFactory.getParam(request, "bill_id");
		String address = DaoFactory.getParam(request, "address");
		String user_id = DaoFactory.getParam(request, "user_id");
		
		TcUserPO tcUserPO = new TcUserPO();
		tcUserPO.setUserId(Long.valueOf(user_id));
		List<TcUserPO> list2 = dao.select(tcUserPO);
		String[] dtl_ids = dtl_id.split(",");
		List<TtSalesWayBillAddressPO> list = new ArrayList<TtSalesWayBillAddressPO>();
		List<TtSalesWayBillDtlPO> listTtSalesWayBillDtlPO = new ArrayList<TtSalesWayBillDtlPO>();
		TtSalesWayBillAddressPO addressPO;
		/**********************记录上报地址 start********************************/
		for (String dtl_id_e : dtl_ids) {
			TtSalesWayBillDtlPO billDtlPO =this.getTtSalesWayBillDtlPO(Long.valueOf(dtl_id_e));
			addressPO = new TtSalesWayBillAddressPO();
			addressPO.setAddress(address);
			addressPO.setAddressId(DaoFactory.getPkId());
			Date date = new Date();
			addressPO.setAddressDate(date);
			addressPO.setCreateBy(Long.valueOf(user_id));
			addressPO.setCreateDate(date);
			addressPO.setVehicleId(billDtlPO.getVehicleId());
			addressPO.setBillId(billDtlPO.getBillId());
			addressPO.setDtlId(billDtlPO.getDtlId());
			addressPO.setVin(billDtlPO.getVin());
			addressPO.setIsSub(Constant.WAYBILL_DTL_STATUS_02);
			addressPO.setDriverPhone(list2.get(0).getHandPhone());
			TtSalesWaybillPO waybillPO = this.getTtSalesWaybillPO(billDtlPO.getBillId());
			addressPO.setBillNo(waybillPO.getBillNo());
			addressPO.setMatId(billDtlPO.getMatId());
			addressPO.setOrderNo(billDtlPO.getOrderNo());
			list.add(addressPO);
			billDtlPO.setStatus(Constant.WAYBILL_DTL_STATUS_02);
			billDtlPO.setUpdateBy(Long.valueOf(user_id));
			billDtlPO.setUpdateDate(new Date());
			billDtlPO.setReportAddress(address);
			billDtlPO.setReportDate(date);
			listTtSalesWayBillDtlPO.add(billDtlPO);
		}
		dao.insert(list);
		/**********************记录上报地址 end********************************/
		//修改交接单明细状态
		dao.updateTtSalesWayBillDtlPOList(listTtSalesWayBillDtlPO);
		//检查交接单下的所有车辆是否已经都交车
		TtSalesWayBillDtlPO billDtlPO = new TtSalesWayBillDtlPO();
		billDtlPO.setBillId(Long.valueOf(bill_id));
		List<TtSalesWayBillDtlPO> billDtlList = dao.select(billDtlPO);
		
		List<TtSalesWayBillDtlPO> billDtlList2 = dao.getTtSalesWayBillDtlPObyStatusAndbillId(bill_id,Constant.WAYBILL_DTL_STATUS_02,0);
		if(billDtlList.size()==billDtlList2.size()){
			//修改交接单为已交车
			TtSalesWaybillPO salesWaybillPO = new TtSalesWaybillPO();
			salesWaybillPO.setBillId(Long.valueOf(bill_id));
			salesWaybillPO.setSendStatus(Long.valueOf(Constant.WAYBILL_STATUS_03));
			salesWaybillPO.setUpdateBy(Long.valueOf(user_id));
			salesWaybillPO.setUpdateDate(new Date());
			salesWaybillPO.setLastCarDate(new Date());
			dao.updateTtSalesWaybillPO(salesWaybillPO);
		}else{
			List<TtSalesWayBillDtlPO> billDtlList3 = dao.getTtSalesWayBillDtlPObyStatusAndbillId(bill_id,Constant.WAYBILL_DTL_STATUS_02,1);
			if(billDtlList.size()!=billDtlList3.size()){
				//修改交接单为已交车
				TtSalesWaybillPO salesWaybillPO = new TtSalesWaybillPO();
				salesWaybillPO.setBillId(Long.valueOf(bill_id));
				salesWaybillPO.setSendStatus(Long.valueOf(Constant.WAYBILL_STATUS_02));
				salesWaybillPO.setUpdateBy(Long.valueOf(user_id));
				salesWaybillPO.setUpdateDate(new Date());
				salesWaybillPO.setLastCarDate(new Date());
				dao.updateTtSalesWaybillPO(salesWaybillPO);
			}
		}
		return "SUCCESS";
	}

	@SuppressWarnings("unchecked")
	public String updateUnTtSalesWayBillDtlStatusByDtlId(RequestWrapper request) throws Exception {
		TtSalesWayBillDtlPO billDtlPO = new TtSalesWayBillDtlPO();
		String dtl_id = DaoFactory.getParam(request, "dtl_id");
		String user_id = DaoFactory.getParam(request, "user_id");
		billDtlPO.setDtlId(Long.valueOf(dtl_id));
		billDtlPO.setStatus(Constant.WAYBILL_DTL_STATUS_01);//在途
		TcUserPO po = new TcUserPO();
		po.setUserId(Long.valueOf(user_id));
		List<TcUserPO> list2 = dao.select(po);
		List<TtSalesWayBillDtlPO> list = dao.select(billDtlPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			TtSalesWayBillDtlPO DtlPO = list.get(0);
			/*if(null!=DtlPO.getStatus()&&0!=DtlPO.getStatus()){
				return "ERROR";
			}else {*/
				//修改明细
			    DtlPO.setDriverPhone(list2.get(0).getHandPhone());
				DtlPO.setStatus(null);
				DtlPO.setUpdateBy(Long.valueOf(user_id));
				DtlPO.setUpdateDate(new Date());
				DtlPO.setDriverBindDate(new Date());
				dao.updateTtSalesWayBillDtlPOSql(DtlPO);
				
				//增加绑定记录
				TtSalesWayAddressLogPO  logPO =  new TtSalesWayAddressLogPO();
				logPO.setLogId(DaoFactory.getPkId());
				logPO.setBeforeStatus(Constant.WAYBILL_DTL_STATUS_01);
				logPO.setCreateBy(Long.valueOf(user_id));
				logPO.setCreateDate(new Date());
				logPO.setBusinessBy(Long.valueOf(user_id));
				logPO.setDtlId(Long.valueOf(dtl_id));
				logPO.setVehicleId(DtlPO.getVehicleId());
				dao.insert(logPO);
				//增加绑定记录
				
//			}
			
			return "SUCCESS";
		}else{
			return "ERROR";
		}
		
	}

	@SuppressWarnings("unchecked")
	public String importExcelOnTheWay(List<TtSalesWayBillAddressPO> voList, RequestWrapper request,
			AclUserBean loginUser) throws Exception {
		
		String bill_id = DaoFactory.getParam(request, "billId");
		String errorInfo = "";
		Boolean  is_sub_flag = false;
		if (!CommonUtils.isNullList(voList)&&voList.size()>0) {
			List<TtSalesWayBillAddressPO> list = new ArrayList<TtSalesWayBillAddressPO>();
			List<TtSalesWayBillDtlPO> Waybilldtllist = new ArrayList<TtSalesWayBillDtlPO>();
			for (TtSalesWayBillAddressPO AddressPO : voList) {
				TtSalesWayBillDtlPO billDtlPO  =this.getTtSalesWayBillDtlPOByBillIdAndVin(Long.valueOf(bill_id),AddressPO.getVin());
				if(null==billDtlPO){
					errorInfo +="导入的VIN："+AddressPO.getVin()+"没有在交接单明细中;";	
				}else {
					//if(null==billDtlPO.getStatus()||0==billDtlPO.getStatus()){
					//	errorInfo+="导入的VIN："+AddressPO.getVin()+"没有绑定司机;";
					//	continue;
					//}
					//只修改没有交车的
					if(!Constant.WAYBILL_DTL_STATUS_02.equals(billDtlPO.getStatus())){
						TtSalesWaybillPO waybillPO = this.getTtSalesWaybillPO(billDtlPO.getBillId());
						AddressPO.setAddressId(DaoFactory.getPkId());
						AddressPO.setBillId(billDtlPO.getBillId());
						AddressPO.setBillNo(waybillPO.getBillNo());
						AddressPO.setMatId(billDtlPO.getMatId());//物料
						AddressPO.setOrderNo(billDtlPO.getOrderNo());
						AddressPO.setCreateBy(loginUser.getUserId());
						Date date = new Date();
						AddressPO.setCreateDate(date);
						AddressPO.setDtlId(billDtlPO.getDtlId());
						AddressPO.setVehicleId(billDtlPO.getVehicleId());
						AddressPO.setAddressDate(date);
						list.add(AddressPO);
						//更新明细表
						TtSalesWayBillDtlPO  temp = new TtSalesWayBillDtlPO();
						temp.setReportAddress(AddressPO.getAddress());
						temp.setReportDate(date);
						temp.setStatus(AddressPO.getIsSub());
						temp.setDtlId(billDtlPO.getDtlId());
//						billDtlPO.setReportAddress(AddressPO.getAddress());
//						billDtlPO.setReportDate(date);
						
						Waybilldtllist.add(temp);
						if(AddressPO.getIsSub()==Constant.WAYBILL_DTL_STATUS_02){
							is_sub_flag = true;
						}
					}
				}
			}
			if(StringUtils.isNotEmpty(errorInfo)){
				throw new Exception(errorInfo)  ;
			}else {
				dao.updateTtSalesWayBillDtlPOList(Waybilldtllist);//修改
				dao.insert(list);//地址
				if(is_sub_flag){//有交车的情况
					TtSalesWaybillPO po = new  TtSalesWaybillPO();
					po.setBillId(Long.valueOf(bill_id));
					po.setSendStatus(Long.valueOf(String.valueOf(Constant.WAYBILL_STATUS_02)));
				    dao.updateTtSalesWaybillPO(po);
				}
				//检查是否都已经交车了
				//检查交接单下的所有车辆是否已经都交车
				TtSalesWayBillDtlPO billDtlPO = new TtSalesWayBillDtlPO();
				billDtlPO.setBillId(Long.valueOf(bill_id));
				List<TtSalesWayBillDtlPO> billDtlList = dao.select(billDtlPO);
				
				List<TtSalesWayBillDtlPO> billDtlList2 = dao.getTtSalesWayBillDtlPObyStatusAndbillId(bill_id,Constant.WAYBILL_DTL_STATUS_02,0);
				if(billDtlList.size()==billDtlList2.size()){
					//修改交接单为已交车
					TtSalesWaybillPO salesWaybillPO = new TtSalesWaybillPO();
					salesWaybillPO.setBillId(Long.valueOf(bill_id));
					salesWaybillPO.setSendStatus(Long.valueOf(Constant.WAYBILL_STATUS_03));
					salesWaybillPO.setUpdateBy(loginUser.getUserId());
					salesWaybillPO.setUpdateDate(new Date());
					salesWaybillPO.setLastCarDate(new Date());
					dao.updateTtSalesWaybillPO(salesWaybillPO);
				}else{
					List<TtSalesWayBillDtlPO> billDtlList3 = dao.getTtSalesWayBillDtlPObyStatusAndbillId(bill_id,Constant.WAYBILL_DTL_STATUS_02,1);
					if(billDtlList.size()!=billDtlList3.size()){
						//修改交接单为已交车
						TtSalesWaybillPO salesWaybillPO = new TtSalesWaybillPO();
						salesWaybillPO.setBillId(Long.valueOf(bill_id));
						salesWaybillPO.setSendStatus(Long.valueOf(Constant.WAYBILL_STATUS_02));
						salesWaybillPO.setUpdateBy(loginUser.getUserId());
						salesWaybillPO.setUpdateDate(new Date());
						salesWaybillPO.setLastCarDate(new Date());
						dao.updateTtSalesWaybillPO(salesWaybillPO);
					}
				}
			}
			
			
			
		}
		
		return "SUCCESS";
	}

	@SuppressWarnings("unchecked")
	public TtSalesWayBillDtlPO getTtSalesWayBillDtlPOByBillIdAndVin(Long bill_id, String vin) {
		 
		 TtSalesWayBillDtlPO po = new TtSalesWayBillDtlPO();
		 po.setBillId(bill_id);
		 po.setVin(vin);
		 List<TtSalesWayBillDtlPO> list = dao.select(po);
		 if(!CommonUtils.isNullList(list)&&list.size()>0){
			 return list.get(0);
		 }else {
			 return null;
		}
		
	}

	public List<Map<String, Object>> getLogiName() {
		
		return dao.getLogiName();
	}

	public PageResult<Map<String, Object>> getCarFactoryOnTheWayList(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		
		return dao.getCarFactoryOnTheWayList(request,loginUser,pageSize,currPage);
	}
   
	@SuppressWarnings("unchecked")
	public TcPosePO getTcPostByPostId(AclUserBean loginUser) {
		
		TcPosePO po = new TcPosePO();
		po.setPoseId(loginUser.getPoseId());
		List<TcPosePO> list = dao.select(po);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
       		return	list.get(0);
		}else {
			return null;
		}
	}

	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListSGM(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		
		return dao.getCarFactoryOnTheWayListSGM( request,  loginUser,pageSize,  currPage);
	}

	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListDealer(RequestWrapper request,
			AclUserBean loginUser, Integer pageSize, Integer currPage) {
		
		return dao.getCarFactoryOnTheWayListDealer( request,  loginUser,pageSize,  currPage);
	}

	public List<Map<String, Object>> getbindCarlog(RequestWrapper request, AclUserBean loginUser) {
		
		return dao.getbindCarlog( request,  loginUser);
	}


}
