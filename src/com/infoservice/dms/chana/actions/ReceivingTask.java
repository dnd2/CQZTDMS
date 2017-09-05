package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagemanage.ReceivingStorageDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: 入库接口收 
* @Description: TODO(接收配件主信息) 
* @author ranj 
* @date 2014-2-8
*
 */
public class ReceivingTask extends AbstractSendTask {
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 发运申请相关
     * urgentDlvryReq 第三参数“new ReceivingStorageDao()” --> “ReceivingStorageDao.getInstance()”
     * Date:2017-06-29
     */
	private ReceivingStorageDao reDao = ReceivingStorageDao.getInstance();
	private final Logger LOG = Logger.getLogger(ReceivingTask.class);
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		ReceivingTask pt = new ReceivingTask();
		pt.handleExecute();
	}
		
	
	@Override
	protected String handleExecute() throws Exception {
		LOG.info("入库数据接收开始");
		insertVehicle();
		LOG.info("入库数据接收结束");
		return null;
	}
	@SuppressWarnings("unchecked")
	public void insertVehicle() {
			try {
				List<Map<String, Object>> erpList=reDao.getErpTmVehicle();
				if(erpList!=null && erpList.size()>0){
					for(int i=0;i<erpList.size();i++){
						Map<String, Object> mapErp=erpList.get(i);
						TmVehiclePO pp=new TmVehiclePO();
						pp.setVin(mapErp.get("VIN").toString());
						List<PO> r=reDao.select(pp);
						if(r!=null && r.size()>0){//有重复不能添加
							continue;
						}else{
							//根据物料编码获取物料组信息
							Map<String, Object> matMap=reDao.getMatGroup(mapErp.get("MATERIALCODE").toString());
							if(matMap==null){
								continue;
							}
							//分配区道位（分配规则按照同物料 入库时间最大后面入库）
							List<Map<String, Object>> sitList=reDao.getSitMsg(mapErp.get("MATERIALCODE").toString());
							if(sitList!=null && sitList.size()>0){
								Map<String, Object> sitMap=erpList.get(0);//获取第一个库位就行了
								//添加车辆表数据
								TmVehiclePO itv=new TmVehiclePO();
								String vehicleId = SequenceManager.getSequence(null);//车辆ID
								itv.setVehicleId(Long.parseLong(vehicleId));//车辆ID
								itv.setMaterialId(Long.parseLong(matMap.get("MATERIAL_ID").toString()));// 物料ID
								String vin=mapErp.get("VIN").toString();
								String vn = "";//vin号后8位
								if (vin != null && vin.length() > 8)
								{
									vn = vin.substring(vin.length() - 8, vin.length());
								}
								itv.setVn(vn);//VN号
								itv.setVin(vin);//vin号
								itv.setLifeCycle(Constant.VEHICLE_LIFE_02);//生命周期(选的车厂库存)
								itv.setSeriesId(Long.parseLong(matMap.get("SERIES_ID").toString()));//车系ID
								itv.setModelId(Long.parseLong(matMap.get("MODEL_ID").toString()));//车型ID
								itv.setPackageId(Long.parseLong(matMap.get("PACKAGE_ID").toString()));//配置ID
								//itv.setModelYear(modelYear);//年型
								itv.setColor(matMap.get("COLOR_NAME").toString());//颜色
								//itv.setProductDate(CommonUtils.parseDate(productDate));//生产日期
								//tvpo.setFactoryDate(CommonUtils.parseDate(factoryDate));//出厂时间
								//tvpo.setLicenseDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//扫码日期(有问题字段)
								itv.setYieldly(Long.parseLong(Constant.areaIdJZD));//产地
								itv.setSitId(Long.parseLong(sitMap.get("SIT_ID").toString()));//库位ID
								itv.setRemark(mapErp.get("REMARK").toString());//备注（中间表读取）
								itv.setPerId(-1L);//接车人【系统自动刷，无需接车员】
								List<Object> scode = new ArrayList<Object>();
								scode.add(sitMap.get("AREA_NAME"));
								scode.add(sitMap.get("ROAD_NAME"));
								scode.add(sitMap.get("SIT_NAME"));
								String sitCode = CommonUtils.getSitCode(scode);//获取库位码
								itv.setSitCode(sitCode);//库位码
								itv.setOfflineDate(CommonUtils.parseDate(mapErp.get("ACTIONDATE").toString()));//下线日期
								//itv.setCreateBy(logonUser.getUserId());//创建人
								itv.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
								itv.setOrgStorageDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//入库时间
								//itv.setPlanDetailId(Long.parseLong(planDetailId));//汇总订单号
								itv.setOutDetailId(Long.valueOf(Constant.DEFAULT_VALUE));//出库单明细ID（入库时候默认为-1）
								itv.setDealerId(Long.valueOf(Constant.DEFAULT_VALUE));//经销商ID（入库时候默认为-1）
								itv.setLockStatus(Constant.LOCK_STATUS_01);//锁定状态：正常状态
								itv.setSdNumber(-1L);//流水号
								itv.setPin(Constant.DEFAULT_VALUE.toString());//ping码 默认-1
								itv.setHegezhengCode(Constant.DEFAULT_VALUE.toString());//合格证号 默认-1
								itv.setStorageDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//跟入库时间一样
								itv.setProcessType("自动入库");
								itv.setHgzNo(Constant.DEFAULT_VALUE.toString());//合格证号(扫描 暂无 默认-1)
								itv.setCreateType(mapErp.get("TYPE").toString());//暂未定义-------------------------------------------
								reDao.insert(itv);
								//修改库位表对应数据
								TtSalesSitPO sp=new TtSalesSitPO();
								sp.setVehicleId(Long.parseLong(vehicleId));
								TtSalesSitPO sps=new TtSalesSitPO();
								sps.setSitId(Long.parseLong(sitMap.get("SIT_ID").toString()));
								reDao.update(sps, sp);
								//修改同步表状态（erp_tm_vehicle）状态
//								reDao.updateErpStatus(mapErp.get("VIN").toString());
							}
						}	
					}
				}
			} catch (Exception e) {
				LOG.error("入库接收失败  ", e);
				e.printStackTrace();
			}finally{
				try{
		            POContext.endTxn(true);		
				}catch(Exception e){
				}finally{
					POContext.cleanTxn();
				}
			}
	}
}
