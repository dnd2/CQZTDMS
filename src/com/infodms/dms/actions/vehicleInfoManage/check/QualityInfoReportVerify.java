package com.infodms.dms.actions.vehicleInfoManage.check;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.vehicleInfoManage.QualityInfoReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsQuelityFollowPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PageResult;

public class QualityInfoReportVerify extends BaseAction{
	
	//质改新增页面
	private QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
	
	/**
	 * 质改跟踪维护
	 */
	public void qualitativeChangeTracking(){		
		this.dateGroupList();
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTracking"), "质改跟踪维护");
	}
	/**
	 * 质改跟踪查看
	 */
	public void qualitativeChangeTemp(){		
		this.dateGroupList();
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTemp"), "质改跟踪查看");
			
	}
	
	/***
	 * 查询车型的数据
	 */
	@SuppressWarnings("unchecked")
	private void dateGroupList() {
		CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
		TmVhclMaterialGroupPO groupPO  = new TmVhclMaterialGroupPO();
		groupPO.setGroupLevel(2);
		List<TmVhclMaterialGroupPO> list= commonUtilDao.select(groupPO);
		act.setOutData("groupList",list);
	}
	
	/**
	 * 质改跟踪新增
	 */
	public void qualitativeChangeTrackingAdd(){		
		this.dateGroupList();
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTrackingAdd"), "质改跟踪新增");
	}
	/**
	 * 二级联接
	 */
	@SuppressWarnings("unchecked")
	public void groupchexitoxing(){		
		try{
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			String groupxi= request.getParamValue("groupxi");
			TmVhclMaterialGroupPO groupPO  = new TmVhclMaterialGroupPO();
			groupPO.setGroupLevel(3);
			groupPO.setParentGroupId(Long.parseLong(groupxi));
			List<TmVhclMaterialGroupPO> list= commonUtilDao.select(groupPO);
			act.setOutData("groupList",list);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"质改跟踪新增");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 底盘号的判断
	 */
	public void checkVin(){		
		try{
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			String vin= request.getParamValue("vin");
			TmVehiclePO tm = new TmVehiclePO();
			Boolean fag = commonUtilDao.queryVin(vin);
			if(fag)
			{
				tm = commonUtilDao.queryDate(vin);
				act.setOutData("tm", tm.getManufacturedate());
			}
			else
			{
				act.setOutData("tm", "该底盘号不存在!");
			}
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"质改跟踪新增");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 判断数据的准确性
	 */
	public void CheckOut(){		
		try{
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			String MAKER_CODE = request.getParamValue("part");
			String type = request.getParamValue("type");
			Boolean fag =  commonUtilDao.queryPart(MAKER_CODE,type);
			if(!fag){
				act.setOutData("part", "请输入正确的信息!");
			}
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"质改跟踪新增");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 查询质改跟踪信息详情
	 */
	public void queryDetail() {
		PageResult<Map<String, Object>> list = dao.queryById(request,getCurrPage(), Constant.PAGE_SIZE);
		act.setOutData("map", list.getRecords().get(0));
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeDetail"), "质改跟踪信息详情");
	}
	/**
	 * 查询质改跟踪信息修改
	 */
	public void queryDetailforUpdate(){
		this.dateGroupList();
		PageResult<Map<String, Object>> list = dao.queryById(request,getCurrPage(), Constant.PAGE_SIZE);
		act.setOutData("map", list.getRecords().get(0));
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTrackingUpdate"), "质改跟踪信息修改");
	}
	/**
	 * 删除质改跟踪
	 */
	@SuppressWarnings("unchecked")
	public void deleteThis() {
		try {
			String id = request.getParamValue("id");
			TtAsQuelityFollowPO q = new TtAsQuelityFollowPO();
			q.setId(Long.parseLong(id));
			dao.delete(q);
			act.setOutData("returnValue", 1);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"质改跟踪信息详情");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void selectmalfunction1() {
		try {
			PageResult<Map<String, Object>> ps = dao.selectmalfunction1(request,loginUser.getDealerId(),getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps",ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"质改跟踪信息详情");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void selectmalfunction2() {
		try {
			PageResult<Map<String, Object>> ps = dao.selectmalfunction2(request,loginUser.getDealerId(),getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps",ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"质改跟踪信息详情");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void selectmalfunction3() {
		try {
			PageResult<Map<String, Object>> ps = dao.selectmalfunction3(request,loginUser.getDealerId(),getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps",ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"质改跟踪信息详情");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 质量跟踪信息查询——zyw
	 */
	public void queFollowList(){
		PageResult<Map<String, Object>> ps =null;
		String param = getParam("page_amount");
		int parseInt = Integer.parseInt(param);
		ps=dao.queFollowFind(request,loginUser.getDealerId(),getCurrPage(), parseInt);
		act.setOutData("ps", ps);
		sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTracking"), "质改跟踪维护");
	}
	/**
	 * 质改跟踪导出
	 */
	public void exportToexcelTarking(){
		dao.exportToexcelTarking(request,loginUser,act,Constant.PAGE_SIZE_MAX, getCurrPage());
	}
	/**
	 * 添加质量跟踪信息——zyw
	 */
	@SuppressWarnings("unchecked")
	public void queFollowAdd(){
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//底盘号
		String carTieId = CommonUtils.checkNull(request.getParamValue("carTieId"));// 车系
		String carTypeId = request.getParamValue("carTypeId");// 车型
		String roCreateDate = request.getParamValue("roCreateDate");//车辆创建时间
		String roRepairDateOne = CommonUtils.checkNull(request.getParamValue("roRepairDateOne"));//维修日期起
		String roRepairDateTwo = CommonUtils.checkNull(request.getParamValue("roRepairDateTwo"));//维修日期止
		String malCode = CommonUtils.checkNull(request.getParamValue("malCode"));//故障类别代码
		String malName = CommonUtils.checkNull(request.getParamValue("malName"));//故障现象
		String partcode = request.getParamValue("partcode");// 零件号
		String partname = request.getParamValue("partname");//零件号名称
		String makerCode = request.getParamValue("makerCode");//部件厂代码
		String makerName = request.getParamValue("makerName");//部件厂名称
		String remark = request.getParamValue("remark");//备注
		String idupdate = request.getParamValue("id");//id
		/*String num="";
		int partNum =0;
		//算法维修日期都不为空的，通过零件号和维修日期算出零件个数
		if(!"".equals(roRepairDateOne) && !"".equals(roRepairDateTwo)){
			partNum=dao.queryQuantity(partcode, roRepairDateOne, roRepairDateTwo);
		}else{
			//如果vin为空？就以生产日期未开始，结束时间为当前时间来计算+零件号
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String todayTime = sdf.format(new Date());
			partNum=dao.queryQuantity(partcode, roCreateDate, todayTime);
		}
		num=String.valueOf(partNum);*/
		TtAsQuelityFollowPO q=null;
		TtAsQuelityFollowPO q1=null;
		try {
			if ("".equals(idupdate)|| null==idupdate) {//插入
			Long id = Utility.getLong(SequenceManager.getSequence(""));
			q = new TtAsQuelityFollowPO();
			q.setId(id);
			q.setPartNum(0L);
			q.setVin(vin);
			q.setCarTieId(BaseUtils.ConvertLong(carTieId));
			q.setCarTypeId(BaseUtils.ConvertLong(carTypeId));
			if (roCreateDate.length()<15) {
				roCreateDate=roCreateDate+" 00:00:00";
			}
			q.setRoCreateDate(DateTimeUtil.stringToDateByPattern(roCreateDate,"yyyy-MM-dd HH:mm:ss"));
			if(!"".equals(roRepairDateOne)){
				q.setRoRepairDateOne(DateTimeUtil.stringToDateByPattern(roRepairDateOne+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
			}
			if(!"".equals(roRepairDateTwo)){
				q.setRoRepairDateTwo(DateTimeUtil.stringToDateByPattern(roRepairDateTwo+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
			}
			q.setMalCode(malCode);
			q.setMalName(malName);
			q.setMakerCode(makerCode);
			q.setMakerName(makerName);
			q.setPartCode(partcode);
			q.setPartName(partname);
			q.setRemark(remark);
			q.setCreateUser(String.valueOf(loginUser.getUserId()));
			q.setCreateDate(new Date());
			dao.insert(q);
			}else {//修改
				q1 = new TtAsQuelityFollowPO();
				q = new TtAsQuelityFollowPO();
				q1.setId(Long.valueOf(idupdate));
				q.setPartNum(0L);
				q.setVin(vin);
				q.setCarTieId(BaseUtils.ConvertLong(carTieId));
				q.setCarTypeId(BaseUtils.ConvertLong(carTypeId));
				q.setRoCreateDate(DateTimeUtil.stringToDateByPattern(roCreateDate,"yyyy-MM-dd HH:mm:ss"));
				if(!"".equals(roRepairDateOne)){
					q.setRoRepairDateOne(DateTimeUtil.stringToDateByPattern(roRepairDateOne+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
				}
				if(!"".equals(roRepairDateTwo)){
					q.setRoRepairDateTwo(DateTimeUtil.stringToDateByPattern(roRepairDateTwo+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
				}
				q.setMalCode(malCode);
				q.setMalName(malName);
				q.setMakerCode(makerCode);
				q.setMakerName(makerName);
				q.setPartCode(partcode);
				q.setPartName(partname);
				q.setRemark(remark);
				q.setCreateUser(String.valueOf(loginUser.getUserId()));
				dao.update(q1, q);
			}
			sendMsgByUrl(sendUrl(QualityInfoReportVerify.class, "qualitativeChangeTracking"), "质改跟踪维护");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 查看主表
	 */
	public void partNumDetail(){
		PageResult<Map<String, Object>> list = dao.selectTtAsQuelityFollow(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("po", list.getRecords().get(0));
		super.sendMsgByUrl(sendUrl(QualityInfoReportVerify.class,"partNumDetail"), "质改跟踪");
	}
	/**
	 * 查看明细
	 */
	public void partNumDetai1Data(){
		PageResult<Map<String, Object>> list = dao.partNumDetail(act,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	public static Long CL(String str){
		return Long.parseLong(str);
	}
	public void showActive(){
		int res=dao.showActive(request);
		if(res!=-1){
			act.setOutData("succ", 1);
		}
	}
	/**
	 * 检测零件号
	 */
	public void checkpartcode(){
		int res=dao.checkpartcode(request,loginUser);
		setJsonSuccByres(res);
	}
}
