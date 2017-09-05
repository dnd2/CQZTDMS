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
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.de.DEException;
import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeFaultPartsDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.FaultPartsVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
 * @ClassName     : OSC73 
 * @Description   : 故障法定名称配件信息下发      上端接口    下发给提定的经销商   初始化使用
 * @author        : luole
 * CreateDate     : 2013-7-5
 */
public class OSC73  extends AbstractSendTask{
	private final static Logger LOG = Logger.getLogger(OSC73.class);
	private DeFaultPartsDao falutDao = DeFaultPartsDao.getInstance();
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
				
				new OSC73().handleExecute(tmp);
				//System.out.println(tmp);
				
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

	public  String handleExecute(String dealerCode){
		LOG.info("====故障法定名称配件信息下发开始====" );
		List<FaultPartsVO> vos = falutDao.queryPart1();
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
				de.sendAMsg("DRT33", dealerCode, body);
				LOG.info("====故障法定名称配件信息下发结束====,下发了(" + body.size() + ")条数据");
			}	
		} catch (Exception e) {
			LOG.error("====故障法定名称配件信息下发失败====", e);
		}
		return null;
	
	}

	@Override
	protected String handleExecute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
