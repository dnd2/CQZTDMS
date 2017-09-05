package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.infoservice.de.DEException;
import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeFaultPartsDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.FaultPartsVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
 * @ClassName     : OSC71 
 * @Description   : 故障法定名称配件信息下发      上端接口
 * @author        : luole
 * CreateDate     : 2013-5-24
 */
public class OSC71  extends AbstractSendTask{
	private final Logger LOG = Logger.getLogger(OSC71.class);
	private DeFaultPartsDao falutDao = DeFaultPartsDao.getInstance();
	private final Integer SEND_COUNT = 1000;
	public static void main(String[] args) throws Exception {
		/*ContextUtil.loadConf();
		DBService ds = DBService.getInstance();
		POContext.beginTxn(ds.getDefTxnManager(), -1);
		DEService.getInstance().init();
		OSC71 service = new OSC71();
		service.handleExecute();	
		POContext.endTxn(true);*/
	}

	@Override
	protected String handleExecute(){
		LOG.info("====故障法定名称配件信息下发开始====" );
		List<FaultPartsVO> vos = falutDao.queryPart();
		if (null == vos || vos.size() == 0) {
			LOG.info("====故障法定名称配件信息下发结束====" );
			return null;
		}
		DeUtility de = new DeUtility();
		try {
			while (vos.size() > 0) { //截取，如果超过1000条，则分多次下发
				List<FaultPartsVO> tmps = new ArrayList<FaultPartsVO>();
				int everySize = vos.size() >= SEND_COUNT ? SEND_COUNT : vos.size();//每次实际发送的条数
				for (int i = 0; i < everySize; i++) {
					FaultPartsVO tmp = vos.remove(0);
					tmps.add(tmp);
				}
				HashMap<String, Serializable> body = DEUtil.assembleBody(tmps);
				de.sendAllMsg("DRT33", body);
				LOG.info("====故障法定名称配件信息下发结束====,下发了(" + body.size() + ")条数据");
				for(FaultPartsVO pvo : tmps){
					falutDao.updateDePart(pvo.getFaultCode(),pvo.getPartCode());//更新
				}
			}	
		} catch (Exception e) {
			LOG.error("====故障法定名称配件信息下发失败====", e);
		}
		return null;
	
	}
	
}
