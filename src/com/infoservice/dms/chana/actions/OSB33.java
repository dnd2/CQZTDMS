package com.infoservice.dms.chana.actions;

import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import java.io.Serializable;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.po.TtVsVehicleTransferChkPO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.dao.DeVehicleAllocateResultDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.VehicleAllocateResultVO;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * @Title: DeVehicleAllocateResult.java
 *
 * @Description:经销商车辆调拨结果下发
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSB33 extends AbstractSendTask {
	private static Logger logger = Logger.getLogger(OSB33.class);
	private final DeVehicleAllocateResultDao dao = DeVehicleAllocateResultDao.getInstance();
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 发运申请相关
     * urgentDlvryReq 第三参数“new CheckVehicleDAO ()” --> “CheckVehicleDAO.getInstance()”
     * Date:2017-06-29
     */
	private final CheckVehicleDAO checkDao = CheckVehicleDAO.getInstance();
	
	private String allocateOutEntityCode; //调出经销商代码
	private String allocateOutEntityName; //调出经销商名称
	private String allocateInEntityCode; //调入经销商代码
	private String allocateInEntityName; //调入经销商名称
	
	@Override
	protected String handleExecute() throws Exception {
		logger.info("====经销商车辆调拨结果下发开始====");
		DeUtility de = new DeUtility();
		try{
			List<VehicleAllocateResultVO> list = dao.getDeVehicleAllocateResult();
			for (int i = 0; i < list.size(); i++) {
				try {
					HashMap<String, Serializable> body = new HashMap<String, Serializable>();
					VehicleAllocateResultVO vo = list.get(i);
					this.allocateInEntityCode = vo.getAllocateInEntityCode();
					this.allocateInEntityName = vo.getAllocateInEntityName();
					this.allocateOutEntityCode = vo.getAllocateOutEntityCode();
					this.allocateOutEntityName = vo.getAllocateOutEntityName();
					
					//发给调入经销商 调出经销商为空
					vo.setAllocateOutEntityCode("");
					vo.setAllocateOutEntityName("");
					vo.setAllocateInEntityCode(allocateInEntityCode);
					vo.setAllocateInEntityName(allocateInEntityName);
					body.put(String.valueOf(i), vo);
					de.sendAMsg("DRB33", allocateInEntityCode, body);
					
					//发给调出经销商 调入经销商为空
					vo.setAllocateInEntityCode("");
					vo.setAllocateInEntityName("");
					vo.setAllocateOutEntityCode(allocateOutEntityCode);
					vo.setAllocateOutEntityName(allocateOutEntityName);
					body.put(String.valueOf(i), vo);
					de.sendAMsg("DRB33", allocateOutEntityCode, body);
					
					TtVsVehicleTransferChkPO chkPO = new TtVsVehicleTransferChkPO();
					chkPO.setTransferId(Long.parseLong(list.get(i).getRemark()));
					TtVsVehicleTransferChkPO newPO = new TtVsVehicleTransferChkPO();
					//将发送成功的数据接口状态置成已完成
					newPO.setIfStatus(DEConstant.IF_STATUS_2);
					checkDao.update(chkPO, newPO);
				} catch (Exception e) {
					logger.error("经销商车辆调拨结果下发失败, id == " + list.get(i).getRemark(), e);
				}
			}
			logger.info("====经销商车辆调拨结果下发结束==== 下发了(" + list.size() + ")条数据");
		} catch(Exception e) {
			logger.error("经销商车辆调拨结果下发失败", e);
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		ContextUtil.loadConf();
		OSB33 o = new OSB33();
		o.execute();
	}
}
