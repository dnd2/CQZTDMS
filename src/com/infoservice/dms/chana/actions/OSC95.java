package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.po.TtAsClientPO;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.ClientVO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSC95  extends AbstractReceiveAction {
	
	private static Logger logger = Logger.getLogger(OSC95.class);
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ContextUtil.loadConf();
		OSC95 o = new OSC95();
		try {
			 File file = new File("D:/20110930112825937000001601.dat");
			 InputStream is = new FileInputStream(file);
			 byte[] b = new byte[is.available()];
			 is.read(b, 0, b.length);
			 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
			 DEMessage msg = xml.convert(b);
			 is.close();
			 o.handleExecutor(msg);
		 	 }catch (Exception e) {
		        e.printStackTrace();
		     }
	}
	
	protected DEMessage handleExecutor(DEMessage msg) {
		logger.info("====店面系统加密信息上传开始====");
		Map<String, Serializable> bodys = msg.getBody();// 获得来至FRM2的消息数据
		for (Entry<String, Serializable> entry : bodys.entrySet()) {
			ClientVO vo = new ClientVO();
			vo = (ClientVO) entry.getValue();
			//vo.setSecretKey("93710B75169142C49426BF64DE06A861");
			try {
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(),
						-1); // 事务开启
				TtAsClientPO po = new TtAsClientPO();
				assemblePO(po,vo);
				Long id =Long.parseLong(SequenceManager.getSequence(""));
				po.setId(id);
				commonDao.insert(po);
				vo.setId(id);
				DEMessage rmsg = wrapperMsg(vo, null);
				POContext.endTxn(true);
				return rmsg;
			} catch (Exception e) {
				POContext.endTxn(false);
				logger.error("店面系统加密信息上传失败", e);
				throw new RpcException(e);
			} finally {
				POContext.cleanTxn();
			}
		}
		logger.info("====店面系统加密信息上传结束====");
		return null;
	}
	
	private DEMessage wrapperMsg(ClientVO vo, String msg) {
		if (null != msg) {
			//出错的时候
			vo = DeUtility.wrapperMsg(vo, msg);
		}
		HashMap<String, Serializable> body = new HashMap<String, Serializable>();
		body.put("body", vo);
		DeUtility de = new DeUtility();
		DEMessage rmsg = null;
		try {
			rmsg = de.assembleDEMessage("DRC95", body);
		} catch (DEException e) {
			logger.error(e, e);
		}
		return rmsg;
	}
	
	private <T extends PO> T assemblePO(T po, BaseVO vo) throws Exception {

		BeanUtils.copyProperties(po, vo);

		return po;
	}
}
