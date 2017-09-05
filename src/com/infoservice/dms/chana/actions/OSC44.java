package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DePartInfoDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.PartInfoVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * @Title: DePartInfo.java
 *
 * @Description:配件主数据下发
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-28
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSC44 extends AbstractSendTask {
	private static Logger logger = Logger.getLogger(OSC44.class);
	public DePartInfoDao dao = DePartInfoDao.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	//计划每次发送的配件数
	private final int SEND_COUNT = 500;
	@Override
	protected String handleExecute() throws Exception {
		logger.info("====配件主数据下发开始====");
		DeUtility de = new DeUtility();
		try{
			List<PartInfoVO> list = dao.getPartInfo();
			HashMap<String, Serializable> body = DEUtil.assembleBody(list);
			de.sendAllMsg("DRC44", body);//第一个参数是下端action名称
			//将下发的配件信息改成已下发
			for (PartInfoVO vo : list) {
				commonDao.updateComplete("TM_PT_PART_BASE", "PART_CODE", vo.getPartNo());
			}
			logger.info("====配件主数据下发结束====,下发了(" + body.size() + ")条数据");
		}catch(Exception e){
			logger.error("配件主数据下发失败", e);
			throw new RpcException(e);
		}
		return null;
	}
	/**
	 * 
	* @Title: sendData 
	* @Description: TODO(发送配件主数据到选定的经销商) 
	* @param @param dealerCodes dcsCode列表
	* @return void    返回类型 
	* @throws
	 */
	public List<String> sendData(List<String> dealerCodes,String partFlag) {
		logger.info("====配件主数据下发开始==== " + dealerCodes.size());
		DeUtility de = new DeUtility();
		try{
			List<PartInfoVO> list = new ArrayList();
			if("0".equals(partFlag)){
				 list = dao.getPartInfoAll();//查询出所有配件基础表的数据,数据量很大
			}else if("1".equals(partFlag)){
				 list = dao.getPartInfoAdd();//查询新增配件
			}
			List<String> dmsCodes = new ArrayList<String>();
			List<String> errCodes = new ArrayList<String>();
			for (String dealerCode : dealerCodes) {
					List<PartInfoVO> listTemp = list ;
					Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealerCode);
					Double dealerIncreaseRate = getDealerIncreaseRate(dealerCode);
					while (listTemp.size() > 0) {
						List<PartInfoVO> tmps = new ArrayList<PartInfoVO>();
						int everySize = listTemp.size() >= SEND_COUNT ? SEND_COUNT : listTemp.size();//每次实际发送的条数
						for (int i = 0; i < everySize; i++) {
							PartInfoVO tmp = listTemp.remove(0);
							tmp.setClaimPrice( new Double(new DecimalFormat("#.00").format(tmp.getClaimPrice()*(1+dealerIncreaseRate/100))));
							tmps.add(tmp);
						}
						HashMap<String, Serializable> body = DEUtil.assembleBody(tmps);
						de.sendAMsg("DRC44", dmsDealer.get("DMS_CODE").toString(), body);
					}
			}
			//将下发的配件信息改成已下发
			for (PartInfoVO vo : list) {
				commonDao.updateComplete("TM_PT_PART_BASE", "PART_CODE", vo.getPartNo());
			}
			logger.info("====配件主数据下发结束====");
			return errCodes;
			//可下发的经销商列表			
/*			if (dmsCodes.size() > 0) {
				while (list.size() > 0) {
					List<PartInfoVO> tmps = new ArrayList<PartInfoVO>();
					int everySize = list.size() >= SEND_COUNT ? SEND_COUNT : list.size();//每次实际发送的条数
					for (int i = 0; i < everySize; i++) {
						PartInfoVO tmp = list.remove(0);
						tmps.add(tmp);
					}
					HashMap<String, Serializable> body = DEUtil.assembleBody(tmps);
					de.sendMsg("DRC44", dmsCodes, body);
					//将下发的配件信息改成已下发
					for (PartInfoVO vo : tmps) {
						commonDao.updateComplete("TM_PT_PART_BASE", "PART_CODE", vo.getPartNo());
					}
					logger.info("====配件主数据下发结束====,下发了(" + body.size() + ")条数据");
				}	
			}*/

		} catch(Exception e){
			logger.error("配件主数据下发失败", e);
			throw new RpcException(e);
		}
	}
	
	private Double getDealerIncreaseRate(String dealerCode){
		StringBuffer sql= new StringBuffer();
		sql.append("select b.parameter_value\n");
		sql.append("  from tm_dealer a, tm_down_parameter b\n");  
		sql.append(" where a.dealer_id = b.dealer_id\n");  
		sql.append("   and a.dealer_code ='"+dealerCode+"'\n");
		Double dealerIncreaseRate = new Double (dao.pageQueryMap(sql.toString(),null, dao.getFunName()).get("PARAMETER_VALUE").toString());
		return dealerIncreaseRate;
	}
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		OSC44 o = new OSC44();
		List<String> dc = new ArrayList<String>();
		dc.add("S11194");
		o.sendData(dc, "0");
		//o.sendData(dc);
		POContext.endTxn(true);

	}
}
