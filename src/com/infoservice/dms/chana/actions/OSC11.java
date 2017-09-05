package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.dao.DeClaimLabourDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.ClaimLabourVO;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: OSC11 
* @Description: TODO(下发索赔工时) 
* @author liuqiang 
* @date Aug 3, 2010 2:37:28 PM 
*
 */
public class OSC11 {
	private static final Logger LOG = Logger.getLogger(OSC11.class);
	private DeClaimLabourDao dao = DeClaimLabourDao.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC11 service = new OSC11();
		//service.execute("", 1L);
	}

	public String execute(String ids, Long companyId, List<String> dealerCodes) {
		LOG.info("====索赔工时下发开始====");
		List<ClaimLabourVO> vos = dao.queryClaimLabour(ids, companyId);
		if (null == vos || vos.size() == 0) {
			return null;
		}
		List<String> dmsCodes = new ArrayList<String>();
		for (String dealerCode : dealerCodes) {
			try {
				Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealerCode);
				//可下发的经销商列表
				dmsCodes.add(dmsDealer.get("DMS_CODE").toString());
			} catch (Exception e) {
				LOG.error("Cann't send to " + dealerCode, e);
			}
		}
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		DeUtility de = new DeUtility();
		if (dmsCodes.size() > 0) {
			try {
				de.sendMsg("DRC11", dmsCodes, body);
			} catch (Exception e) {
				LOG.error("索赔工时失败", e);
				throw new RpcException(e);
			}
			LOG.info("====索赔工时下发结束====,下发了(" + body.size() + ")条数据");
		}
//		try {
//			de.sendAllMsg("DRC11", body);
//		} catch (Exception e) {
//			LOG.error("索赔工时失败", e);
//			//throw new RpcException(e);
//		}
		//更新TT_AS_WR_WRLABINFO 接口状态为已下发
//		for (ClaimLabourVO vo : vos) {
//			commonDao.updateComplete("TT_AS_WR_WRLABINFO", "LABOUR_CODE", vo.getLabourCode());
//		}
		return null;
	}
}
