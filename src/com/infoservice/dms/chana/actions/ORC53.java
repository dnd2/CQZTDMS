package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.infodms.dms.actions.repairOrder.WrRuleUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.repair.RepairOrderDao;
import com.infodms.dms.po.TmColorPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRepairOrderProblemPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoManagePO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.RepairOrderVO;
import com.infoservice.dms.chana.vo.RoAddItemVO;
import com.infoservice.dms.chana.vo.RoLabourVO;
import com.infoservice.dms.chana.vo.RoManageVO;
import com.infoservice.dms.chana.vo.RoRepairPartVO;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.filestore.FileStore;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class ORC53 extends AbstractReceiveAction {

	private static final Logger logger = Logger.getLogger(ORC53.class);

	private RepairOrderDao repairOrderDao = RepairOrderDao.getInstance();
	
    private ClaimBillMaintainDAO claimbillmaindao =  ClaimBillMaintainDAO.getInstance();

	private DeCommonDao commonDao = DeCommonDao.getInstance();

	private String itemActivityCode;//维修项目服务活动编号

	private String partActivityCode;//维修配件服务活动编号
	
	private  String purchasedDate; //购车日期
	
	private StringBuffer  troubleCause = new StringBuffer(); //故障原因与维修措施
	
	private StringBuffer  troubleDesc = new StringBuffer(); //故障描述
	
	private Long ro_id = 0L;
	
/*	 public static void main(String[] args) {
	 ContextUtil.loadConf();
	 try {
		 File file = new File("D:/TestFile/20130106105637739000429504.dat");
		 InputStream is = new FileInputStream(file);
		 byte[] b = new byte[is.available()];
		 is.read(b, 0, b.length);
		 ORC53 o = new ORC53();
		 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
		 DEMessage msg = xml.convert(b);
		 System.out.println(msg.getAppName());
		 is.close();
		 o.handleExecutor(msg);
	 	 }catch (Exception e) {
	        e.printStackTrace();
	     }
	}*/
	 
	
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {

		logger.info("====工单上报开始====");
		
		String msgId = msg.getMsgId();
		
		Map<String, Serializable> bodys = msg.getBody();

		for (Entry<String, Serializable> entry : bodys.entrySet()) {

			RepairOrderVO vo = new RepairOrderVO();

			vo = (RepairOrderVO) entry.getValue();

			try {
				
			   POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			   
			   this.setRo_id(Long.parseLong(SequenceManager.getSequence("")));
			   //检测车辆表中是否有该车，没有则跳过工单上报
			   TmVehiclePO tvp = new TmVehiclePO();
			   tvp.setVin(vo.getVin());
			   List list = repairOrderDao.select(tvp);
			   if(list==null||list.size()<1){
				   wrongVin(vo);
				   logger.info("没有在车辆表中找到该车,vin:"+vo.getVin());
				   POContext.endTxn(true);
				   logger.info("====工单上报结束====");
				   return null;
			   }
			   //检测车辆
			   boolean k = checkRepeat(vo); //检测是否重复工单
			   boolean srFlag = checkSpecial(vo);//检测客户接待过来的工单是否需要上传
			   if(k == false && srFlag == true){
				   boolean b = checkingRepairOrderVO(vo); 
				   untieRepairOrderVO(vo,b); 
			   } 
			   POContext.endTxn(true);
			   
			} catch (Exception e) {
				
				POContext.endTxn(false);
				
				logger.error("工单上报失败", e);								
				
			}finally{
				 POContext.cleanTxn();
			}			
		}
		logger.info("====工单上报结束====");

		return null;
	}

	/**
	 * 
	 * @Title: checkRepeat 
	 * @Description: TODO(检测重复工单) 
	 * @param @param vo
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")	
	private boolean checkRepeat(RepairOrderVO vo) throws Exception {
		boolean  b = false;
		TtAsRepairOrderPO ttAsRepairOrderPO = new TtAsRepairOrderPO();	 
		Map<String, Object> map = commonDao.getDcsDealerCode(vo.getEntityCode());
		ttAsRepairOrderPO.setDealerCode(map.get("DEALER_CODE").toString());
		
		ttAsRepairOrderPO.setRoNo(this.replaceRO(repairOrderDao,
		vo.getRepairTypeCode(), vo, ttAsRepairOrderPO.getDealerCode()));	   		
		
		List list = repairOrderDao.select(ttAsRepairOrderPO);
	    if(list.size()>0){
	    	//如果是问题工单则废弃上端的问题工单，覆盖
	    	ttAsRepairOrderPO = (TtAsRepairOrderPO) list.get(0);
	    	if(ttAsRepairOrderPO.getRemark1()!=null && (!"".equals(ttAsRepairOrderPO.getRemark1())) && "error".equals(ttAsRepairOrderPO.getRemark1())){
	    		//废弃
	    		for(int i=0;i<list.size();i++){
	    			ttAsRepairOrderPO = (TtAsRepairOrderPO) list.get(i);
	    			String delFlag = delForRecover(ttAsRepairOrderPO.getId(),vo.getEntityCode());
	    		}
	    	}else{
	    		b = true;
	    	}
	    }
 		return b;
	} 
	
	/**
	 * 
	 * @Title: checkingRepairOrderVO 
	 * @Description: TODO(将上报的VO进行相关验证，不合格的进入下端上报问题工单表) 
	 * @param @param vo
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")	
	private boolean checkingRepairOrderVO(RepairOrderVO vo) throws Exception {
		
		boolean  b = true;
		boolean is_pre_approve_order = checkPreApproveOrder(vo);
		TtAsRepairOrderProblemPO ttAsRepairOrderProblemPO = new TtAsRepairOrderProblemPO();
		
		Map<String, Object> map = commonDao.getDcsDealerCode(vo.getEntityCode());

		ttAsRepairOrderProblemPO.setDealerCode(map.get("DEALER_CODE").toString());
		//验证行驶里程 YH 2011.4.14
		TmVehiclePO vehicle1 = new TmVehiclePO();
		TmVehiclePO vehicleDms = new TmVehiclePO();
		vehicle1.setVin(vo.getVin());
		List list = repairOrderDao.select(vehicle1);
		//无零件的，正常工单，置InsurationNo为特殊标记，判断noPartsFlag是否为5
		int noPartsFlag = 100;
		if(list.size()>0){
			vehicleDms = (TmVehiclePO)list.get(0);
			if(vehicleDms.getPurchasedDate()!=null&&!"".equals(vehicleDms.getPurchasedDate())){
				this.purchasedDate = new SimpleDateFormat("yyyy-MM-dd").format( vehicleDms.getPurchasedDate());
			}
		}
		
		TmVehiclePO vehicle2 = new TmVehiclePO();

		vehicle2.setMileage(vo.getInMileage());
		
		//验证维修项目数 YH 2011.4.14
		LinkedList labourVoList = vo.getLabourVoList(); //维修项目数
		
		//验证维修配件数 YH 2011.4.14
		LinkedList repairPartVoList = vo.getRepairPartVoList();//维修配件数
		
		StringBuffer sb = new StringBuffer(); //记录错误信息
		
		sb.append("工单号:"+vo.getRoNo()+"\n");
		
		//YH 2010.11.30 如果下端的Mileage小于等于上端的就进入问题工单表
		if (false == compareInMileage(repairOrderDao, vehicle1, vehicle2, vo)) {
			b = false;
			sb.append("该上报工单行驶里程数小于等于系统正常值！\n");
			noPartsFlag++;
 		} 
		//如果是一般维修或售前维修必需要有维修项目和维修配件	YH 2010.04.19
		if(!is_pre_approve_order){
			if("11441001".equals(vo.getRepairTypeCode()) || "11441003".equals(vo.getRepairTypeCode())||"11441002".equals(vo.getRepairTypeCode())) {		
				if( null == labourVoList || labourVoList.size() < 1){
					b = false;	 
					sb.append("该上报工单没有维修项目！\n");
					noPartsFlag++;
				}
				
				if( null == repairPartVoList || repairPartVoList.size() < 1){
					b = false;
					sb.append("该上报工单没有维修配件！\n");
					if(noPartsFlag==100 || noPartsFlag==0){
						noPartsFlag=0;
					}
				}
				
				if( labourVoList.size() > repairPartVoList.size()){
					b = false;
					sb.append("该上报工单维修配件数不能小于维修项目数！\n");
					if(noPartsFlag==100 || noPartsFlag==0){
						noPartsFlag=0;
					}
				}
				//是否全自费标记
				boolean isSelfPayAll = true;
				//验证维修项目付费方式是否只有自费或索赔
				if(null != labourVoList || labourVoList.size()> 0){
					int temp_flag1=0;
					for (int i = 0; i < labourVoList.size(); i++) {
						RoLabourVO roLabourVO = (RoLabourVO) labourVoList.get(i);
						String pay_type = roLabourVO.getChargePartitionCode();
						if(null == roLabourVO.getLabourCode() || "".equals(roLabourVO.getLabourCode().trim())){
							b = false;
							noPartsFlag++;
							temp_flag1++;
						}
					}
					if(temp_flag1>0){
						sb.append(temp_flag1+"个维修项目代码:空！\n");
					}
					for (int i = 0; i < labourVoList.size(); i++) {  		   
						RoLabourVO roLabourVO = (RoLabourVO) labourVoList.get(i);
						String pay_type = roLabourVO.getChargePartitionCode();
						if(!("11801001".equals(pay_type)||"11801002".equals(pay_type))){
							b = false;
							sb.append("维修项目:"+roLabourVO.getLabourName()==null?"":roLabourVO.getLabourName()+"--"+pay_type+" 付费方式不对！\n");
							noPartsFlag++;
						}
						//如果有一项及以上的项目需要索赔则此维修工单不是全自费工单
						if("11801002".equals(pay_type)||"S".equals(pay_type)){
							isSelfPayAll = false;
						}
					}
				}
				//验证维修配件付费方式是否只有自费或索赔
				if( null != repairPartVoList || repairPartVoList.size() > 0){
					for (int i = 0; i < repairPartVoList.size(); i++) {
						RoRepairPartVO roRepairPartVO = (RoRepairPartVO) repairPartVoList.get(i);
						String pay_type = 	roRepairPartVO.getChargePartitionCode();
						if("11801001".equals(pay_type)||"11801002".equals(pay_type)){
						}else {
							b = false;
							sb.append("维修配件代码:"+roRepairPartVO.getPartNo()+"--"+pay_type+"付费方式不对！\n");
							noPartsFlag++;
						}
						//如果有一项及以上的配件需要索赔则此维修工单不是全自费工单
						if("11801002".equals(pay_type)||"S".equals(pay_type)){
							isSelfPayAll = false;
						}
						//判断三包
						if("11441003".equals(vo.getRepairTypeCode())){
							isSelfPayAll = false; //售前维修：全部默认为索赔	
						}else if("11441001".equals(vo.getRepairTypeCode())||"11441002".equals(vo.getRepairTypeCode())){
							/*判断三包期*/
							Integer  k	=  partIsGua(this.purchasedDate,vo.getInMileage().toString(),vo.getVin(),roRepairPartVO.getPartNo());
							if(1 == k){
								isSelfPayAll = false;//是三包，含有索赔项
							}
							//否三包忽略
						}
					}
				}
				//如果是11441001 一般维修，11441002 外出维修，11441003 售前维修，判断配件是否全部自费
				//如果全都是自费项目则置为问题工单，且标注到问题工单表里  modify by tanv 2012-10-30
				//如果维修项目和维修配件都为空的，也标记为全自费工单
				if(isSelfPayAll==true){
					b=false;
					sb.append("维修工单为全自费！\n");
					ttAsRepairOrderProblemPO.setIsSelfPayAll("11801001"); 
					noPartsFlag++;
				}
			}
			//如果是服务活动 工单验证
			if("11441005".equals(vo.getRepairTypeCode())){
				List params = new ArrayList();
				params.add(vo.getVin());
				params.add(map.get("DEALER_CODE").toString());
				params.add(vo.getInMileage());
				Object ACTIVITY_ID = commonDao.callFunction("F_GET_ACTIVITY_ID", java.sql.Types.VARCHAR,params );
				String ids = ACTIVITY_ID==null?"":ACTIVITY_ID.toString();
				//上端没有服务活动的工单
				if(null == ids || "".equals(ids)){
					b = false;
					sb.append("该上报车辆没有可对应的服务活动！\n");
					noPartsFlag++;
				}else{
					//验证服务活动维修项目是否是厂端下发的服务活动
					if( null != labourVoList || labourVoList.size() > 0){
						int lvFlag = 0;
						for (int i = 0; i < labourVoList.size(); i++) {  		   
							RoLabourVO roLabourVO = (RoLabourVO) labourVoList.get(i);
							String dmsCode = roLabourVO.getActivityCode();
							if(dmsCode!=null && (!"".equals(dmsCode))){
								String[] dcsIds = ids.split(",");
								boolean lvtempFlag = false;
								for(String dcsId:dcsIds){
									//取出activityCode
									TtAsActivityPO apo = new TtAsActivityPO();
									TtAsActivityPO apoTemp = new TtAsActivityPO();
									apo.setActivityId(Long.valueOf(dcsId));
									List<PO> aList=commonDao.select(apo);
									apoTemp=(TtAsActivityPO) aList.get(0);
									//比较下端的code,上端有这个活动，且未过期
									if(apoTemp.getActivityCode().equals(dmsCode) && apoTemp.getEnddate().getTime()>new Date().getTime()){
										lvtempFlag = true;
									}
								}
								if(lvtempFlag == false){
									b = false;
									sb.append("服务活动项目代码:("+dmsCode+")不是厂端对应的服务活动！\n");
									noPartsFlag++;
								}
							}else{
								lvFlag++;
							}
						}
						if(lvFlag==labourVoList.size() && labourVoList.size()>0){
							b = false;
							sb.append("服务活动项目数为0！\n");//1个服务活动都没有的置为问题工单
							noPartsFlag++;
						}
					}else{
						b = false;
						sb.append("维修项目数为空！\n");
						noPartsFlag++;
					}
				}
			}
		}
		//非售前维修，验证是否有购车时间,modify by tanv 2012.10.25
		if(!("11441003".equals(vo.getRepairTypeCode()))){	
			 TmVehiclePO vehicleTemp = new TmVehiclePO();
			 vehicleTemp.setVin(vo.getVin());
			 List listTemp = repairOrderDao.select(vehicleTemp);
			 if (listTemp.size() > 0) {
				 TmVehiclePO tp = (TmVehiclePO) list.get(0);
				 if(null == tp.getPurchasedDate() || "".equals(tp.getPurchasedDate())) {
					 b = false;
					 sb.append("该销售车辆购车时间为空！\n");
					 noPartsFlag++;
				 }
			 }
		}
		//上传上来的工单的发动机号不正确，则置为问题工单 add by tanv 2012-12-06
		if(!vehicleDms.getEngineNo().equals(vo.getEngineNo())){
			b = false;
			sb.append("车辆发动机号不符！\n");
			noPartsFlag++;
		}
		//车辆型号代码不正确 add by tanv 2013-01-17
		TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
		if(vo.getModel()==null || "".equals(vo.getModel())){
			sb.append("车辆型号为空：\n");
			b = false;
			noPartsFlag++;
		}else{
			tvmg.setGroupCode(vo.getModel());
			List groupList = (List)repairOrderDao.select(tvmg);
			if(groupList == null || groupList.size()==0){
				sb.append("车辆型号("+vo.getModel()+")代码不正确：\n");
				b = false;
				noPartsFlag++;	
			}
		}
		if(b == false) {
			ttAsRepairOrderProblemPO = assemblePO(ttAsRepairOrderProblemPO,vo);
			ttAsRepairOrderProblemPO.setId(this.getRo_id());
			ttAsRepairOrderProblemPO.setRemark1(sb.toString());
			ttAsRepairOrderProblemPO.setIsDe(1);
			ttAsRepairOrderProblemPO.setRoNo(this.replaceRO(repairOrderDao,
			vo.getRepairTypeCode(), vo, ttAsRepairOrderProblemPO.getDealerCode()));
			ttAsRepairOrderProblemPO.setLineNo(this.checkLineNo(vo));//添加唯一识别标识 modify by tanv 2012-10-30
			ttAsRepairOrderProblemPO.setInsurationCode(this.checkSRCode(vo));//添加是否特殊工单标识modify by tanv 2012-10-30
			//如果是11441001 一般维修，11441002 外出维修，11441003 售前维修，是否可以添加无零件
			if("11441001".equals(vo.getRepairTypeCode()) || "11441002".equals(vo.getRepairTypeCode())||"11441003".equals(vo.getRepairTypeCode())){
				if(noPartsFlag==0){
					ttAsRepairOrderProblemPO.setInsurationNo("50122501");//可添加无零件的特殊工单modify by tanv 2012-12-25
				}else{
					ttAsRepairOrderProblemPO.setInsurationNo(null);//用完将本字段置空
				}
			}else{
				ttAsRepairOrderProblemPO.setInsurationNo(null);//用完将本字段置空
			}
			ttAsRepairOrderProblemPO.setCreateDate(new Date());
			repairOrderDao.insert(ttAsRepairOrderProblemPO); 
		}
		return b;
	} 





	/**
	 * 
	 * @Title: untieRepairOrderVO 
	 * @Description: TODO(将上报的VO存到相应的PO里面) 
	 * @param @param vo
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private void untieRepairOrderVO(RepairOrderVO vo,boolean b) throws Exception {

		/***********维修工单主表PO 新增维修工单主表***********/
		TtAsRepairOrderPO ttAsRepairOrderPO = new TtAsRepairOrderPO();	 
		
		Map<String, Object> map = commonDao.getDcsDealerCode(vo.getEntityCode());

		ttAsRepairOrderPO.setDealerCode(map.get("DEALER_CODE").toString());
		
		Map companyCode = commonDao.getDcsCompanyCode(vo.getEntityCode());
		
		long oemCompanyId = this.getOemCompanyIdByCode(companyCode.get("DCS_CODE").toString());//YH 2010.12.9

		ttAsRepairOrderPO.setDealerId(Long.parseLong(map.get("DEALER_ID").toString())); //YH 2010.11.30

		ttAsRepairOrderPO = assemblePO(ttAsRepairOrderPO, vo);
		
		ttAsRepairOrderPO.setId(this.getRo_id());

		ttAsRepairOrderPO.setCreateDate(new Date());
		
		if("11441004".equals(ttAsRepairOrderPO.getRepairTypeCode())){ //如果维修类型是保养的 YH 2010.12.09 
			Integer freeTimes =  this.updateFreeTimes(ttAsRepairOrderPO.getVin());
			ttAsRepairOrderPO.setFreeTimes(freeTimes);
			if(1 == this.isFree(ttAsRepairOrderPO.getVin(), ttAsRepairOrderPO.getOutMileage(), oemCompanyId)){
				ttAsRepairOrderPO.setApprovalYn(1);
			}else if(0 == this.isFree(ttAsRepairOrderPO.getVin(), ttAsRepairOrderPO.getOutMileage(), oemCompanyId)){
			    ttAsRepairOrderPO.setApprovalYn(0);
			    if(true == b){
			    	this.updateFreeTimesForTmVehicle(ttAsRepairOrderPO.getVin(),freeTimes); //需要预授权的就 更新车辆表保养次数 YH 2011.8.11
			    }
			}
		}
		
		if("11441002".equals(ttAsRepairOrderPO.getRepairTypeCode())){ //如果维修类型是外出维修 YH 2010.12.09
			 ttAsRepairOrderPO.setApprovalYn(1); //需要预授权
		}
		
		ttAsRepairOrderPO.setRoStatus(Constant.RO_STATUS_01);//设为未结算 YH 2010.11.30
		
		ttAsRepairOrderPO.setIfStatus(DEConstant.IF_STATUS_0);//默认为未下发状态				
		
		ttAsRepairOrderPO.setOrderValuableType(Constant.RO_PRO_STATUS_01);
		
		 if( true == b){ //如果是通过验证工单 YH 2011.9.13
				//把行驶总里程更新到车辆表 如果下端的里程大于车信表的就更新 YH 2010.12.29
				TmVehiclePO vehicle1 = new TmVehiclePO();
				vehicle1.setVin(ttAsRepairOrderPO.getVin());
				TmVehiclePO vehicle2 = new TmVehiclePO();
				vehicle2.setMileage(ttAsRepairOrderPO.getInMileage());
				if (compareInMileage(repairOrderDao, vehicle1, vehicle2,vo)) {//YH 2010.11.30 如果下端的Mileage大于上端的就更新
					repairOrderDao.update(vehicle1, vehicle2);
				}
			  }
		//正常工单，车牌不为空，则更新到车辆信息表中 TANV 2012.10.25
		 if(b==true && ttAsRepairOrderPO.getVin()!=null && (!"".equals(ttAsRepairOrderPO.getVin()))){
				TmVehiclePO vehicle1 = new TmVehiclePO();
				vehicle1.setVin(ttAsRepairOrderPO.getVin());
				TmVehiclePO vehicle2 = new TmVehiclePO();
				vehicle2.setLicenseNo(vo.getLicense());
				repairOrderDao.update(vehicle1, vehicle2);
		 }
		//更新RoNo
		ttAsRepairOrderPO.setRoNo(this.replaceRO(repairOrderDao,
				ttAsRepairOrderPO.getRepairTypeCode(), vo, ttAsRepairOrderPO.getDealerCode()));
		     repairOrderDao.delCascade(ttAsRepairOrderPO); //先根据DEALER_CODE和RO_NO删除原来的记录
		 				
		//更新车辆表结束    
	  if(!"11441004".equals(ttAsRepairOrderPO.getRepairTypeCode())&&!"11441005".equals(ttAsRepairOrderPO.getRepairTypeCode())){ // YH 2011.7.29 保养类型,服务活不要其他项目		     
		/***********附加项目PO,新增记录到附加项目表*********/
		LinkedList addItemVoList = vo.getAddItemVoList();

		List<PO> ttAsRoAddItemPOs = new ArrayList<PO>();

		for (int i = 0; i < addItemVoList.size(); i++) {

			TtAsRoAddItemPO ttAsRoAddItemPO = new TtAsRoAddItemPO();

			RoAddItemVO roAddItemVO = (RoAddItemVO) addItemVoList.get(i);

			ttAsRoAddItemPO = assemblePO(ttAsRoAddItemPO, roAddItemVO);

			ttAsRoAddItemPO.setId(Long.parseLong(SequenceManager.getSequence("")));

			ttAsRoAddItemPO.setRoId(ttAsRepairOrderPO.getId());

			ttAsRoAddItemPO.setCreateDate(new Date());
			
			String ChargePartitionCode = ttAsRoAddItemPO.getChargePartitionCode();
			
			if("S".equals(ChargePartitionCode)||"11801002".equals(ChargePartitionCode)){ //下端逻辑混乱出现BUG YH 2011.8.29
				ttAsRoAddItemPO.setChargePartitionCode("11801002");		
			}else {
				ttAsRoAddItemPO.setChargePartitionCode("11801001");		
			}

			ttAsRoAddItemPOs.add(ttAsRoAddItemPO);
		}
		repairOrderDao.insert(ttAsRoAddItemPOs);
		logger.info("Insert TtAsRoAddItemPO success.");
 
		/******************维修项目PO,新增记录到维修项目表************/
		LinkedList labourVoList = vo.getLabourVoList();

		List<PO> TtAsRoLabourPOs = new ArrayList<PO>();

		for (int i = 0; i < labourVoList.size(); i++) {

			TtAsRoLabourPO ttAsRoLabourPO = new TtAsRoLabourPO();

			RoLabourVO roLabourVO = (RoLabourVO) labourVoList.get(i);

			ttAsRoLabourPO = assemblePO(ttAsRoLabourPO, roLabourVO);

			ttAsRoLabourPO.setId(Long.parseLong(SequenceManager.getSequence("")));

			ttAsRoLabourPO.setRoId(ttAsRepairOrderPO.getId());

			ttAsRoLabourPO.setCreateDate(new Date());
			
			ttAsRoLabourPO.setWrLabourcode(roLabourVO.getLabourCode());
			
			ttAsRoLabourPO.setWrLabourname(roLabourVO.getLabourName());
			
			if("S".equals(ttAsRoLabourPO.getChargePartitionCode())||"11801002".equals(ttAsRoLabourPO.getChargePartitionCode())){ //下端逻辑混乱出现BUG YH2011.8.29
				ttAsRoLabourPO.setChargePartitionCode("11801002");
			}else {
				ttAsRoLabourPO.setChargePartitionCode("11801001");
			}
			
			//付费方式
			if(null != ttAsRoLabourPO.getChargePartitionCode()){
				ttAsRoLabourPO.setPayType(Integer.parseInt(ttAsRoLabourPO.getChargePartitionCode()));
			}
			
			//判断付费方式
			if("11441003".equals(vo.getRepairTypeCode())){				
				ttAsRoLabourPO.setPayType(11801002); //售前维修：全部默认为索赔			
			}	
			troubleCause.append(roLabourVO.getTroubleCause()); //故障原因
			troubleDesc.append(roLabourVO.getTroubleDesc()); //故障描述
			
			if(null != ttAsRoLabourPO.getLabourName()){ //YH 2011.8.11 空白项目名称 不能进入
				if(!"".equals(ttAsRoLabourPO.getLabourName().trim())){
					TtAsRoLabourPOs.add(ttAsRoLabourPO); 	
				}		  
			}

			this.itemActivityCode = roLabourVO.getActivityCode();
		}
		repairOrderDao.insert(TtAsRoLabourPOs);
		logger.info("Insert ttAsRoLabourPO success.");
	  
		/******************维修配件PO,新增记录到维修配件表************/
		LinkedList repairPartVoList = vo.getRepairPartVoList();

		List<PO> ttAsRoRepairPartPOs = new ArrayList<PO>();

		for (int i = 0; i < repairPartVoList.size(); i++) {

			TtAsRoRepairPartPO ttAsRoRepairPartPO = new TtAsRoRepairPartPO();

			RoRepairPartVO roRepairPartVO = (RoRepairPartVO) repairPartVoList.get(i);

			ttAsRoRepairPartPO = assemblePO(ttAsRoRepairPartPO, roRepairPartVO);

			ttAsRoRepairPartPO.setId(Long.parseLong(SequenceManager.getSequence("")));

			ttAsRoRepairPartPO.setRoId(ttAsRepairOrderPO.getId());

			ttAsRoRepairPartPO.setCreateDate(new Date());
			
			if("S".equals(ttAsRoRepairPartPO.getChargePartitionCode())||"11801002".equals(ttAsRoRepairPartPO.getChargePartitionCode())){ //下端逻辑混乱出现BUG YH2011.8.29
				ttAsRoRepairPartPO.setChargePartitionCode("11801002");
			}else {
				ttAsRoRepairPartPO.setChargePartitionCode("11801001");			
			}
			
			//付费方式
			if(null != ttAsRoRepairPartPO.getChargePartitionCode()){
			    ttAsRoRepairPartPO.setPayType(Integer.parseInt(ttAsRoRepairPartPO.getChargePartitionCode()));
			}
			
			//判断付费方式
			if("11441003".equals(vo.getRepairTypeCode())){
				ttAsRoRepairPartPO.setPayType(11801002); //售前维修：全部默认为索赔	
			}else if("11441001".equals(vo.getRepairTypeCode())||"11441002".equals(vo.getRepairTypeCode())){
				/*判断三包期*/
				Integer  k	=  partIsGua(this.purchasedDate,vo.getInMileage().toString(),vo.getVin(),ttAsRoRepairPartPO.getPartNo());
				if(0 == k){
					ttAsRoRepairPartPO.setIsGua(0); //否三包
					ttAsRoRepairPartPO.setPayType(11801001);
				}
				if(1 == k){
					ttAsRoRepairPartPO.setIsGua(1);//是三包
					ttAsRoRepairPartPO.setPayType(11801002);
				}
			}
			ttAsRoRepairPartPOs.add(ttAsRoRepairPartPO);

			this.partActivityCode = roRepairPartVO.getActivityCode();
		}
		repairOrderDao.insert(ttAsRoRepairPartPOs);
		logger.info("Insert ttAsRoRepairPartPO success.");

		/******************维修工单辅料管理费PO,新增记录到工单辅料管理费表************/
		LinkedList manageVoList = vo.getManageVoList();

		List<PO> ttAsRoManagePOs = new ArrayList<PO>();

		for (int i = 0; i < manageVoList.size(); i++) {

			TtAsRoManagePO ttAsRoManagePO = new TtAsRoManagePO();

			RoManageVO roManageVO = (RoManageVO) manageVoList.get(i);

			ttAsRoManagePO = assemblePO(ttAsRoManagePO, roManageVO);

			ttAsRoManagePO.setId(Long
					.parseLong(SequenceManager.getSequence("")));

			ttAsRoManagePO.setRoId(ttAsRepairOrderPO.getId());

			ttAsRoManagePO.setCreateDate(new Date());
            		
			ttAsRoManagePOs.add(ttAsRoManagePO);
		}
		repairOrderDao.insert(ttAsRoManagePOs);
		logger.info("Insert ttAsRoManagePO success.");
	  }	
		  //最后插入工单表  工单表的服务活动编号赋值
		if (Utility.testString(itemActivityCode)) {
			ttAsRepairOrderPO.setCamCode(itemActivityCode);
		} else {
			ttAsRepairOrderPO.setCamCode(partActivityCode);
		}
		 ttAsRepairOrderPO.setIsDe(1); //表示是下端传上来的工单
		 if(false == b) {
		  ttAsRepairOrderPO.setRemark1("error");//表示是问题工单	 
		 }
		 ttAsRepairOrderPO.setGuaranteeDate(getPurchasedDate(ttAsRepairOrderPO.getVin()).getPurchasedDate());//生产日期
		 ttAsRepairOrderPO.setModel(getModelName(ttAsRepairOrderPO.getModel()));//车型名称
		 ttAsRepairOrderPO.setDelivererAdress(vo.getDelivererAddress()); //送修人地址
		 ttAsRepairOrderPO.setTroubleReason(troubleCause.toString());//故障原因
		 ttAsRepairOrderPO.setRepairMethod(troubleCause.toString());//维修措施
		 ttAsRepairOrderPO.setTroubleDescriptions(troubleDesc.toString()); //故障描述
		 ttAsRepairOrderPO.setRemark2("");//YH 2011.7.15
		 ttAsRepairOrderPO.setOwnerName(this.getCustomer(ttAsRepairOrderPO.getVin())); //带出车主姓名 YH 2011.8.17
		 ttAsRepairOrderPO.setRoNo(this.replaceRO(repairOrderDao, vo.getRepairTypeCode(), vo, ttAsRepairOrderPO.getDealerCode()));
		 ttAsRepairOrderPO.setLineNo(this.checkLineNo(vo));//添加特殊工单标识 修改LindeNo modify by tanv 2012-10-30
		 ttAsRepairOrderPO.setInsurationCode(this.checkSRCode(vo));//添加特殊工单修改 工单号modify by tanv 2012-11-01
		 ttAsRepairOrderPO.setInsurationNo(null);//用完将本字段置空modify by tanv 2012-10-30
		 repairOrderDao.insert(ttAsRepairOrderPO); //写入工单主表
		 logger.info("Insert ttAsRepairOrderPO success.");
	}

	private String getModelName(String model) throws Exception{ //YH 2011.7.19 修改ModelName
		TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
		tvmg.setGroupCode(model);
		List list = (List)repairOrderDao.select(tvmg);
		if(list != null && list.size()>0){
			TmVhclMaterialGroupPO mg = (TmVhclMaterialGroupPO)list.get(0);				
			return mg.getGroupName();
		}else{
			return "wrongCode";
		}
	}

	private TmVehiclePO getPurchasedDate(String vin) throws Exception{ //YH 2011.8.9 修改购车日期
		TmVehiclePO tv  = new TmVehiclePO();
 		tv.setVin(vin);
		List list = (List)repairOrderDao.select(tv);
		TmVehiclePO tvv = (TmVehiclePO)list.get(0);
 		return tvv;
	}

	private <T extends PO> T assemblePO(T po, BaseVO vo) throws Exception {
		BeanUtils.copyProperties(po, vo);
		return po;
	}

	@SuppressWarnings( { "unused", "unchecked" })
	//比较行政里程比车辆表大的就更新 YH 2010.11.30
	private boolean compareInMileage(RepairOrderDao dao, TmVehiclePO tp1,
			TmVehiclePO tp2, RepairOrderVO vo)throws Exception {
		List list = dao.select(tp1);
		boolean b = false;
		if (list.size() > 0) {
			TmVehiclePO tp = (TmVehiclePO) list.get(0);
			//添加外出维修判断+提交结算节点上来的，里程数相等也为正常工单 modify by tanv 2012-11-07
			if("11441002".equals(vo.getRepairTypeCode())){
				if(tp2.getMileage()!=null && (!"".equals(tp2.getMileage())) && tp2.getMileage()>=tp.getMileage()){
					b=true;
				}
			}else{
				if (tp.getMileage() < tp2.getMileage())
				b = true;
			}
		}
		return b;
	}

	//替换工单号RO为类型标识  YH.2010.12.9
	@SuppressWarnings( { "unused", "unchecked" })
	private String replaceRO(RepairOrderDao repairOrderDao,
			String repairTypeCode, RepairOrderVO vo, String dealerCode)throws Exception {
	//	String sql = " select BILL_TYPE from TM_BILL_TYPE where REPAIR_TYPE_CODE = '"+ repairTypeCode + "' ";
	//	Map<String, Object> map = repairOrderDao.pageQueryMap(sql, null,repairOrderDao.getFunName());
		String newRoNo = "";// map.get("BILL_TYPE").toString();
		String BILL_TYPE = "DR" ; //YH 2011.7.15 改过编码规则
		String BILL_TYPESR = "SR" ;//外出维修类工单11441002，modify by tanv 2012-10-30,special repair order,替换工单号SR为外出特殊维修工单类型标识
		if("11441002".equals(vo.getRepairTypeCode()) && true == checkPreApproveOrder(vo)){
			newRoNo = dealerCode + vo.getRoNo().replace("RO", BILL_TYPESR);
		}else{
			newRoNo = dealerCode + vo.getRoNo().replace("RO", BILL_TYPE); 
		}
		return newRoNo;
	}
	
	private Long checkLineNo(RepairOrderVO vo) {
		Long preApproveLineNo = 1l;
		//外出维修，在提交结算上来的工单，修改标识 modify by tanv 2012-11-21
		if("11441002".equals(vo.getRepairTypeCode()) && ("20102901".equals(vo.getInsurationCode()) || "20102902".equals(vo.getInsurationCode()))){
			preApproveLineNo = 2l;
		}
		return preApproveLineNo;
	}
	
	private String checkSRCode(RepairOrderVO vo) {
		String SRFalg = "0";//默认是正常工单modify by tanv 2012-11-21
		if("11441002".equals(vo.getRepairTypeCode()) && "10861011".equals(vo.getInsurationNo())
				&& ("".equals(vo.getInsurationCode())||null==vo.getInsurationCode())){
			SRFalg = "1"; //如果是从第一节点传上来的外出维修类工单置为特殊工单
		}
		return SRFalg;
	}
	@SuppressWarnings("unchecked")
	public Integer updateFreeTimes(String vin)throws Exception{
		
		TmVehiclePO tvp = new TmVehiclePO();		
		tvp.setVin(vin);
		List list =  commonDao.select(tvp);		
		if(list.size()>0){
			tvp = (TmVehiclePO)list.get(0);	
		}
		Integer freeTimes  = tvp.getFreeTimes();
		if("".equals(freeTimes)|| null == freeTimes || 0 == freeTimes ){ //首保为时候 把工单设为1 YH 2011.9.13
			freeTimes = 1;
		}else{
			freeTimes = freeTimes+1;
		}
		return freeTimes;
	}
	
	@SuppressWarnings("unchecked")
	public Integer updateFreeTimesForTmVehicle(String vin,Integer freeTimes)throws Exception{	// YH 2011.8.11	
		TmVehiclePO tvp = new TmVehiclePO();		
		tvp.setVin(vin);
		
		TmVehiclePO tvp2 = new TmVehiclePO();		
		tvp2.setVin(vin);
		tvp2.setFreeTimes(freeTimes);
		Integer i = commonDao.update(tvp, tvp2);
		return i;
	}
	
	
	@SuppressWarnings("unchecked") //通过companyCode得到oemcompanyId YH.2010.12.09
	public long getOemCompanyIdByCode(String companyCode)throws Exception{
		
	  TmCompanyPO tc = 	new TmCompanyPO();
	  
	  tc.setCompanyCode(companyCode);
	  
	  List list =  commonDao.select(tc);
	  
	  if(list.size()>0){	  
		  tc  = (TmCompanyPO)list.get(0);	  
	  }
	  
	  return tc.getOemCompanyId();
	}
	
	  //判断是否脱保 是否需要预授权
	@SuppressWarnings("unchecked")
	public Integer isFree(String vin,Double mileage,long companyId)throws Exception{
		int allTimes = 0;
		Date date = new Date();
		int day = 999999;
		int month = 999999; //当前时间和保修开始时间相差月份
		TmVehiclePO tvp = new TmVehiclePO();
		tvp.setVin(vin);
		
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		
		List<TmVehiclePO> lsv = claimbillmaindao.select(tvp);
		if (lsv!=null) {
			if (lsv.size()>0) {
				tvp = lsv.get(0);
				date = tvp.getPurchasedDate(); //保修改开始时间
			}
		}
		Date now = new Date(); //今天
		if (date!=null) {
			String formatStyle ="yyyy-MM-dd";  
			SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
			String d1 = df.format(date);
			String d2 = df.format(now);
			month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的差值
			day = Utility.compareDate(d1, d2, 0); //取得今日和保养开始时间的差值 天数
		}
		List<Map<String, Object>> freeTimes = claimbillmaindao.viewFreeTime1(vin);
		//判断保养次数是否为空
		if(freeTimes.get(0).get("FREE_TIMES")==null||freeTimes.get(0).get("FREE_TIMES").equals("")){
			allTimes=0;
		}
		else{allTimes = Integer.valueOf(freeTimes.get(0).get("FREE_TIMES").toString());}
		List<TtAsWrQamaintainPO> lsq=null;
		lsq =  claimbillmaindao.getFree(allTimes+1,companyId,month,day,Double.valueOf(mileage));
		if (lsq!=null && lsq.size()>0) { //存在记录，不需要授权
			return Constant.APPROVAL_YN_NO;
		}else {
			return Constant.APPROVAL_YN_YES;
		}
	}

	private void DelProblemOrder(RepairOrderVO vo) throws Exception{		
		TtAsRepairOrderProblemPO ttAsRepairOrderProblemPO = new TtAsRepairOrderProblemPO();
		ttAsRepairOrderProblemPO = assemblePO(ttAsRepairOrderProblemPO,vo);
		repairOrderDao.delete(ttAsRepairOrderProblemPO);	
	}


	//判断配件是否三包内的通用规则
	public Integer partIsGua(String purchasedDate,String inMileage,String vin,String partCode)throws Exception{
		int isGua=0;
			WrRuleUtil util = new WrRuleUtil();
			if(purchasedDate==null||purchasedDate.equals("")){
				isGua= 0;
			}
			else{
				try {
					WarrantyPartVO wp = util.wrRuleCompute(inMileage, purchasedDate, vin, partCode);					
					 if(wp.getIsInWarranty() == Constant.IF_TYPE_YES){				 
						 isGua= 1;
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return isGua;
	}
	
	//检测客户接待节点过来的工单，是否需要预授权的工单，modify by tanv 2012-10-29
	private boolean checkSpecial(RepairOrderVO vo) {
		boolean srFlag = true;
		//预授权节点过来的
		if(true == checkPreApproveOrder(vo) ){
			//默认这个节点过来的都不上传
			srFlag = false;
			//外出维修工单，保养类工单上传
			if(("11441002".equals(vo.getRepairTypeCode()) || "11441004".equals(vo.getRepairTypeCode()))){
				srFlag = true;
			}
		}
		return srFlag;
	}
	
	public String getCustomer(String vin)throws Exception{
	 String Customer_NAME = "";	
	 String sql = " SELECT tc.ctm_name\n" +
				 "  FROM tt_customer tc, tt_dealer_actual_sales a, tm_vehicle c\n" + 
				 "  where tc.ctm_id = a.ctm_id\n" + 
				 "   and a.vehicle_id = c.vehicle_id\n" + 
				 "   and c.vin = '"+vin+"'" + 
				 "   and A.Is_Return = 10041002";
	 Map<String, Object> map = repairOrderDao.pageQueryMap(sql.toString(), null, repairOrderDao.getFunName());
		if(map == null){
			return Customer_NAME;
 		}else{
			Customer_NAME = map.get("CTM_NAME").toString();
			return Customer_NAME;
		}
	}
 
	public Long getRo_id() {
		return ro_id;
	}
	public void setRo_id(Long ro_id) {
		this.ro_id = ro_id;
	}
	//检查是否预授权申请工单 add by tanv 2012-11-19
	private boolean checkPreApproveOrder(RepairOrderVO vo) {
		boolean srFlag = false;
		//预授权按钮
		if("10861011".equals(vo.getInsurationNo()) && ("".equals(vo.getInsurationCode()) || null==vo.getInsurationCode())){
			srFlag = true;
		}
		return srFlag;
	}
	//VIN 码错误工单的处理 add by tanv 2013-01-17 start
	private String wrongVin(RepairOrderVO vo) {
		try {
			//存入问题工单数据
			TtAsRepairOrderProblemPO ttAsRepairOrderProblemPO = new TtAsRepairOrderProblemPO();
			Map<String, Object> map = commonDao.getDcsDealerCode(vo.getEntityCode());
			ttAsRepairOrderProblemPO.setDealerCode(map.get("DEALER_CODE").toString());
			StringBuffer sb = new StringBuffer(); //记录错误信息
			sb.append("工单号:"+vo.getRoNo()+"\n");
			sb.append("车辆VIN码不存在！\n");
			ttAsRepairOrderProblemPO = assemblePO(ttAsRepairOrderProblemPO,vo);
			ttAsRepairOrderProblemPO.setId(this.getRo_id());
			ttAsRepairOrderProblemPO.setRemark1(sb.toString());
			ttAsRepairOrderProblemPO.setIsDe(1);
			ttAsRepairOrderProblemPO.setRoNo(this.replaceRO(repairOrderDao,vo.getRepairTypeCode(), vo, ttAsRepairOrderProblemPO.getDealerCode()));
			ttAsRepairOrderProblemPO.setLineNo(1L);//添加唯一识别标识 modify by tanv 2012-10-30
			repairOrderDao.insert(ttAsRepairOrderProblemPO); 
			//存入工单表数据
			TtAsRepairOrderPO ttAsRepairOrderPO = new TtAsRepairOrderPO();	 
			ttAsRepairOrderPO.setDealerCode(map.get("DEALER_CODE").toString());
			Map companyCode = commonDao.getDcsCompanyCode(vo.getEntityCode());
			long oemCompanyId = this.getOemCompanyIdByCode(companyCode.get("DCS_CODE").toString());//YH 2010.12.9
			ttAsRepairOrderPO.setDealerId(Long.parseLong(map.get("DEALER_ID").toString())); //YH 2010.11.30
			ttAsRepairOrderPO = assemblePO(ttAsRepairOrderPO, vo);
			ttAsRepairOrderPO.setId(this.getRo_id());
			ttAsRepairOrderPO.setCreateDate(new Date());
			ttAsRepairOrderPO.setApprovalYn(1);
			ttAsRepairOrderPO.setRoStatus(Constant.RO_STATUS_01);//设为未结算 YH 2010.11.30
			ttAsRepairOrderPO.setIfStatus(DEConstant.IF_STATUS_0);//默认为未下发状态		
			ttAsRepairOrderPO.setOrderValuableType(Constant.RO_PRO_STATUS_01);
			//更新RoNo
			ttAsRepairOrderPO.setRoNo(this.replaceRO(repairOrderDao,ttAsRepairOrderPO.getRepairTypeCode(), vo, ttAsRepairOrderPO.getDealerCode()));
			repairOrderDao.delCascade(ttAsRepairOrderPO); //先根据DEALER_CODE和RO_NO删除原来的记录
			ttAsRepairOrderPO.setRemark1("error");//表示是问题工单	 
			ttAsRepairOrderPO.setModel(getModelName(ttAsRepairOrderPO.getModel()));//车型名称
			ttAsRepairOrderPO.setDelivererAdress(vo.getDelivererAddress()); //送修人地址
			ttAsRepairOrderPO.setTroubleReason(troubleCause.toString());//故障原因
			ttAsRepairOrderPO.setRepairMethod(troubleCause.toString());//维修措施
			ttAsRepairOrderPO.setTroubleDescriptions(troubleDesc.toString()); //故障描述
			ttAsRepairOrderPO.setRemark2("");//YH 2011.7.15 
			ttAsRepairOrderPO.setOwnerName(vo.getOwnerName()); //带出车主姓名 YH 2011.8.17
			ttAsRepairOrderPO.setLineNo(1L);
			repairOrderDao.insert(ttAsRepairOrderPO); //写入工单主表
			logger.info("Insert ttAsRepairOrderPO success.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("====VIN码错误====");
			e.printStackTrace();
			return "failed";
		}
		return "success";
	}
	//VIN 码错误工单的处理 add by tanv 2013-01-17 end
	//废弃问题工单 add by tanv 2013-01-29 start
	private String delForRecover(Long roid, String entityCode) {
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		String rs = "";//执行结果
		if("".equals(entityCode)||null==entityCode){
			rs = "-1";
		}else{
			inParameter.add(roid);
			inParameter.add(entityCode);
			outParameter.add(Types.VARCHAR);
			outParameter = repairOrderDao.callProcedure("P_C_RO_DELFORRECOVER", inParameter, outParameter);
			rs = outParameter.get(0)==null?"-1":outParameter.get(0).toString();//返回值
		}
		return rs;
	}
	//废弃问题工单 add by tanv 2013-01-29 end
}

