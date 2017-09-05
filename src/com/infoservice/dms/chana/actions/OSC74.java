package com.infoservice.dms.chana.actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeFaultModeDetailDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.FaultModeDetailVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
 * @ClassName     : OSC72 
 * @Description   : 故障法定名称失效模式信息下发      上端接口
 * @author        : luole
 * CreateDate     : 2013-5-24
 */
public class OSC74  extends AbstractSendTask{
	private final static Logger LOG = Logger.getLogger(OSC74.class);
	private DeFaultModeDetailDao falutModeDao = DeFaultModeDetailDao.getInstance();
	private final Integer SEND_COUNT = 1000;
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	public static void main(String[] args) {
		
		/*try {
			ContextUtil.loadConf();
			DEService.getInstance().init();
			DBService ds = DBService.getInstance();
			
			File file = new File("D:\\1.txt");
			InputStream is=null;
			BufferedReader br = null;
			String tmp = "";
			FileInputStream fis = new FileInputStream(file);
			is=new BufferedInputStream(new FileInputStream(file));
			br = new BufferedReader(new InputStreamReader(is,"GBK"));
			POContext.beginTxn(ds.getDefTxnManager(), -1);
			while((tmp=br.readLine())!=null){
				
				new OSC74().handleExecute(tmp);
				
			}
			POContext.endTxn(true);
			is.close();
			br.close();
			fis.close();
			
		} catch (Exception e) {
			POContext.endTxn(false);
			LOG.error(e, e);
			//throw new RpcException();
		} finally {
			//POContext.cleanTxn();
		}*/
	}

	public String handleExecute(String dealerCode){
		LOG.info("====故障法定名称失效模式信息下发开始====" );
		List<FaultModeDetailVO> vos = falutModeDao.queryModelDetail1();
		if (null == vos || vos.size() == 0) {
			LOG.info("====故障法定名称失效模式信息下发结束====" );
			return null;
		}
		DeUtility de = new DeUtility();
		try {
			while (vos.size() > 0) { //截取，如果超过1000条，则分多次下发
				List<FaultModeDetailVO> tmps = new ArrayList<FaultModeDetailVO>();
				int everySize = vos.size() >= SEND_COUNT ? SEND_COUNT : vos.size();//每次实际发送的条数
				for (int i = 0; i < everySize; i++) {
					FaultModeDetailVO tmp = vos.remove(0);
					tmps.add(tmp);
				}
				HashMap<String, Serializable> body = DEUtil.assembleBody(tmps);
				de.sendAMsg("DRT34",dealerCode, body);
				LOG.info("====故障法定名称失效模式信息下发结束====,下发了(" + body.size() + ")条数据");
			}	
		} catch (Exception e) {
			LOG.error("====故障法定名称失效模式信息下发失败====", e);
		}
		return null;
	
	}

	@Override
	protected String handleExecute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
