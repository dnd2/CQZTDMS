package com.infodms.dms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.dao.sales.storage.storagebase.DriverAuditDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.po.TtVsDriverPO;
import com.infodms.dms.service.DriverAuditService;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public class DriverAuditServiceImpl implements DriverAuditService {

	public PageResult<Map<String, Object>> getdriverAuditList(RequestWrapper request, AclUserBean loginUser,
			int pageSize, int currPage) {
		DriverAuditDao dao = new DriverAuditDao();
		PageResult<Map<String, Object>> list =	dao.getdriverAuditList(request,loginUser, pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String driverAudit(RequestWrapper request, AclUserBean loginUser) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		String status = DaoFactory.getParam(request, "status");
		String driverId = DaoFactory.getParam(request, "driver_id");
		String audit_remark = DaoFactory.getParam(request, "audit_remark");
		if(StringUtils.isEmpty(driverId)){
			throw new Exception("没有获取到该条信息！");
		}
		//审核
		TtVsDriverPO driverPO = new TtVsDriverPO();
		driverPO.setDriverId(Long.valueOf(driverId));
		driverPO.setAuditRemark(audit_remark);
		driverPO.setAuditBy(loginUser.getUserId());
		driverPO.setAuditDate(new Date());
		driverPO.setUpdateBy(loginUser.getUserId());
		driverPO.setUpdateDate(new Date());
		driverPO.setStatus(Integer.valueOf(status));
		dao.updateTtVsDriver(driverPO);
		//获取源信息
		TtVsDriverPO driver = new TtVsDriverPO();
		driver.setDriverId(Long.valueOf(driverId));
		driver = (TtVsDriverPO) dao.select(driver).get(0);
		
		if(String.valueOf(Constant.DRIVER_TYPE_03).equals(status)){//通过
			//根据司机id查询用户表中是否存在该司机的信息
			TcUserPO userPO  =  this.getTcUserByDriverId(Long.valueOf(driverId));
			//存在司机信息
			if(null!= userPO){
				//获取用户信息
				//根据用户id获取职位用户关系
				TrUserPosePO TrUserPose = this.getTrUserPosePoByUserId(userPO.getUserId());
				if (null!=TrUserPose) {
					//根据司机物流id获取职位
					TcPosePO po = this.getTcPosePOByLogiId(driver.getCarrierId());
					 //获取用户职位关系
					if(null!=po){
	                    //职位
						//如果两个职位不相同，修改
						if(po.getPoseId()!=TrUserPose.getPoseId()){
							TrUserPosePO posePO = new TrUserPosePO();
							posePO.setUpdateBy(loginUser.getUserId());
							posePO.setUpdateDate(new Date());
							posePO.setUserPoseId(TrUserPose.getUserPoseId());
							posePO.setPoseId(po.getPoseId());
							dao.upTrUserPosePO(posePO);
						}
						
					}
				}
			}
			//没有用户的情况
			else{
				TcUserPO tcUserPO =  new TcUserPO();
				Long user_id = DaoFactory.getPkId();
				tcUserPO.setUserId(user_id);
				tcUserPO.setUserType(Constant.SYS_USER_SGM);
				tcUserPO.setName(driver.getDriverName());
				tcUserPO.setHandPhone(driver.getDriverPhone());
				tcUserPO.setPhone(driver.getDriverPhone());
				tcUserPO.setCreateBy(loginUser.getUserId());
				tcUserPO.setCreateDate(new Date());
				tcUserPO.setAcnt(driver.getDriverPhone());
				tcUserPO.setPassword(driver.getPassword());
				tcUserPO.setUserStatus(Constant.STATUS_ENABLE);
				tcUserPO.setDriverFlag(1);
				dao.insert(tcUserPO);
				//查询职位
				TcPosePO posePoT = this.getTcPosePOByLogiId(driver.getCarrierId());
				if(null!=posePoT){
					TrUserPosePO userPosePO =  new TrUserPosePO();
					userPosePO.setPoseId(posePoT.getPoseId());
					userPosePO.setUserPoseId(DaoFactory.getPkId());
					userPosePO.setCreateBy(loginUser.getUserId());
					userPosePO.setUserId(user_id);
					userPosePO.setCreateDate(new Date());
					dao.insert(userPosePO);
				}else {
					 throw new Exception("没有找到对应的职位");
				}
			}
		}else if(String.valueOf(Constant.DRIVER_TYPE_04).equals(status)) {//拒绝
			
		}
		return "SUCCESS";
	}

	

	@SuppressWarnings("unchecked")
	public String driverAuditAll(RequestWrapper request, AclUserBean loginUser) throws Exception {
		  DriverAuditDao dao = new DriverAuditDao();  
		  List<TtVsDriverPO> list =  new ArrayList<TtVsDriverPO>();
		  String driver_idT = DaoFactory.getParam(request, "driver_id");
		  if(StringUtils.isEmpty(driver_idT)||driver_idT.length()<=0){
			  throw new Exception("没有获取到信息！");
		  }
		  String[] driver_ids = driver_idT.split(",");
		  if(null==driver_ids ||driver_ids.length<=0){
			  throw new Exception("没有获取到信息！");
		  }
		  String status = DaoFactory.getParam(request, "status");
		  String audit_remark = DaoFactory.getParam(request, "audit_remark");
		  TtVsDriverPO driverPO ;
		  for (int i = 0; i < driver_ids.length; i++) {
			  driverPO = new TtVsDriverPO();
			  String driver_id = driver_ids[i];
			  driverPO.setDriverId(Long.valueOf(driver_id));
			  driverPO.setStatus(Integer.valueOf(status));
			  driverPO.setAuditRemark(audit_remark);
			  driverPO.setAuditBy(loginUser.getUserId());
			  driverPO.setAuditDate(new Date());
			  driverPO.setUpdateBy(loginUser.getUserId());
			  driverPO.setUpdateDate(new Date());
			  dao.updateTtVsDriver(driverPO);
			  list.add(driverPO);
		}
//		  dao.insert(list);
		  
		 //批量更新或者新增
		  if(String.valueOf(Constant.DRIVER_TYPE_03).equals(status)){
			  for (String driver_id : driver_ids) {
				  Long driverId = Long.valueOf(driver_id);
				  TtVsDriverPO ttVsDriver = this.getTtVsDriver(driverId);
				  TcUserPO tcUser = this.getTcUserByDriverId(driverId);
				  if(null!=tcUser){//更新
					  TcPosePO tcPose = this.getTcPosePOByLogiId(ttVsDriver.getCarrierId());
					  TrUserPosePO trUserPose = this.getTrUserPosePoByUserId(tcUser.getUserId());
					  if(tcPose.getPoseId()!=trUserPose.getPoseId()){
                           TrUserPosePO trUserPosePO = new TrUserPosePO();
                           trUserPosePO.setUserPoseId(trUserPose.getUserPoseId());
                           trUserPosePO.setPoseId(tcPose.getPoseId());
                           dao.upTrUserPosePO(trUserPosePO);
					  }
				  }else{//新增
					    TtVsDriverPO driver = this.getTtVsDriver(driverId);
					    TcUserPO tcUserPO =  new TcUserPO();
						Long user_id = DaoFactory.getPkId();
						tcUserPO.setUserId(user_id);
						tcUserPO.setUserType(Constant.SYS_USER_SGM);
						tcUserPO.setName(driver.getDriverName());
						tcUserPO.setHandPhone(driver.getDriverPhone());
						tcUserPO.setPhone(driver.getDriverPhone());
						tcUserPO.setCreateBy(loginUser.getUserId());
						tcUserPO.setCreateDate(new Date());
						tcUserPO.setAcnt(driver.getDriverPhone());
						tcUserPO.setPassword(driver.getPassword());
						tcUserPO.setUserStatus(Constant.STATUS_ENABLE);
						tcUserPO.setDriverFlag(1);//是否司机
						dao.insert(tcUserPO);
						//查询职位
						TcPosePO posePoT = this.getTcPosePOByLogiId(driver.getCarrierId());
						if(null!=posePoT){
							TrUserPosePO userPosePO =  new TrUserPosePO();
							userPosePO.setPoseId(posePoT.getPoseId());
							userPosePO.setUserPoseId(DaoFactory.getPkId());
							userPosePO.setCreateBy(loginUser.getUserId());
							userPosePO.setUserId(user_id);
							userPosePO.setCreateDate(new Date());
							dao.insert(userPosePO);
						}else {
							 throw new Exception("没有找到对应的职位");
						}
				  }
				
			  }
		  }
		return "SUCCESS";
	}

	public Map<String, Object> getdriverAuditByDriverId(RequestWrapper request, AclUserBean loginUser)
			throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		String driver_id = DaoFactory.getParam(request, "driver_id");
		if(StringUtils.isEmpty(driver_id)){
			throw new Exception("没有查询到该条信息");
		}
		Map<String, Object> map = dao.getdriverAuditByDriverId(driver_id);
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	public String driverRegister(RequestWrapper request) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		String carrier_id = DaoFactory.getParam(request, "carrier_id");
		String driver_name = DaoFactory.getParam(request, "driver_name");
		String driver_phone = DaoFactory.getParam(request, "driver_phone");
		String password = DaoFactory.getParam(request, "password");
		String newPassword  = MD5Util.MD5Encryption(password);
		//之前的拒绝掉
		TtVsDriverPO driverPO2 =  new TtVsDriverPO();
		driverPO2.setDriverPhone(driver_phone);
		driverPO2.setStatus(Constant.DRIVER_TYPE_02);
		List<TtVsDriverPO> list = dao.select(driverPO2);
		
		if (!CommonUtils.isNullList(list)&&list.size()>0) {
			
			TtVsDriverPO driverPO = list.get(0);
			driverPO.setStatus(Constant.DRIVER_TYPE_04);
			driverPO.setUpdateBy(-1l);
			driverPO.setUpdateDate(new Date());
			driverPO.setAuditBy(-1l);
			driverPO.setAuditDate(new Date());
			dao.updateTtVsDriver(driverPO);
		}
		//==============================================end
		
		TtVsDriverPO driverPO =  new TtVsDriverPO();
		driverPO.setDriverId(DaoFactory.getPkId());
		driverPO.setCarrierId(Long.valueOf(carrier_id));
		driverPO.setApplyDate(new Date());
		driverPO.setCreateDate(new Date());
		driverPO.setCreateBy(-1l);
		driverPO.setApplyBy(-1l);
		driverPO.setDriverName(driver_name);
		driverPO.setDriverPhone(driver_phone);
		driverPO.setPassword(newPassword);
		driverPO.setStatus(Constant.DRIVER_TYPE_02);
		dao.insert(driverPO);
		
		
		return "SUCCESS";
	}

	public List<Map<String, Object>> getTtSalesLogi(RequestWrapper request) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		List<Map<String, Object> > list =  dao.getTtSalesLogi(request);
		return list;
	}

	@SuppressWarnings("unchecked")
	public TtVsDriverPO getTtVsDriver(Long driver_id) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		TtVsDriverPO driverPO =  new TtVsDriverPO();
		driverPO.setDriverId(driver_id);
		List<TtVsDriverPO> list = dao.select(driverPO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}

	public TcUserPO getTcUserByDriverId(Long driver_id) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		List<TcUserPO> list = dao.getUserByDriverId(driver_id);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public TrUserPosePO getTrUserPosePoByUserId(Long user_id) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		TrUserPosePO posePO =  new TrUserPosePO();
		posePO.setUserId(user_id);
		List<TrUserPosePO> list = dao.select(posePO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public TcPosePO getTcPosePOByLogiId(Long carrierId) throws Exception{
		DriverAuditDao dao = new DriverAuditDao();
		TcPosePO tcPosePO  =  new TcPosePO();
		tcPosePO.setLogiId(carrierId);
		List<TcPosePO> list = dao.select(tcPosePO);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}

	public String driverCalibration(RequestWrapper request) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		List<Map<String, Object>> list = dao.driverCalibration(request);
		if(!CommonUtils.isNullList(list)||list.size()<=0){
			return "SUCCESS";
		}else {
			return "ERROR";
		}
	}

	@SuppressWarnings("unchecked")
	public TcUserPO driverLogin(RequestWrapper request) throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		String driver_phone = DaoFactory.getParam(request, "driver_phone");
		String password = DaoFactory.getParam(request, "password");
		String newPassword  = MD5Util.MD5Encryption(password);
		TcUserPO po = new TcUserPO();
		po.setAcnt(driver_phone);
		po.setPassword(newPassword);
		List<TcUserPO> list = dao.select(po);
		if(!CommonUtils.isNullList(list)&&list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	/**
	 * 根据用户ID获取承运商ID
	 */
	public Map<String, Object> getLogiIdByUserId(String userId)
			throws Exception {
		DriverAuditDao dao = new DriverAuditDao();
		return dao.getLogiIdByUserId(userId);
	}

}
