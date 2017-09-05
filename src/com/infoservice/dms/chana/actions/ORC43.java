package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import com.infodms.dms.po.TtPartPeriodReportPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DePartMonthReportDao;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.PartMonthReportVO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: ORC43 
* @Description: TODO(配件月报上报) 
* @author liuqiang 
* @date Sep 3, 2010 11:24:15 AM 
*
 */
public class ORC43 extends AbstractReceiveAction {
	private final Logger LOG = Logger.getLogger(ORC43.class);
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	private DePartMonthReportDao dao = DePartMonthReportDao.getInstance();
	
/*	public static void main(String[] args) {
		 ContextUtil.loadConf();
		 try {
			 File file = new File("D:/20111201084536640000029264.dat");
			 InputStream is = new FileInputStream(file);
			 byte[] b = new byte[is.available()];
			 is.read(b, 0, b.length);
			 ORC43 o = new ORC43();
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
		LOG.info("====月报上报开始====");
		Map<String, Serializable> bodys = msg.getBody();
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		/*	for (Entry<String, Serializable> entry : bodys.entrySet()) {
				PartMonthReportVO vo = new PartMonthReportVO();
				vo = (PartMonthReportVO) entry.getValue();
				untieVO(vo);
			}*/
			POContext.endTxn(true);
		} catch (Exception e) {
			POContext.endTxn(false);
			LOG.error("月报上报失败", e);
		}finally{
			POContext.cleanTxn();
		}
		LOG.info("====月报上报结束====");
		return null;
	
	}
	
	private void untieVO(PartMonthReportVO vo) throws Exception {
		/***********新增配件月报上报PO***********/
		TtPartPeriodReportPO partPO = new TtPartPeriodReportPO();
		Map<String, Object> map = commonDao.getDcsDealerCode(vo.getEntityCode());
		
		partPO.setEntityCode(map.get("DEALER_CODE").toString());
		partPO.setReportMonth(vo.getReportMonth());
		partPO.setReportYear(vo.getReportYear());
		partPO.setStorageCode(vo.getStorageCode());
		partPO.setPartBatchNo(vo.getPartBatchNo());
		partPO.setPartNo(vo.getPartNo());
		int i = dao.delete(partPO);
		if (i > 0) {
			LOG.warn("删除了" + i + "条记录, " + vo.getPartNo());
		}
		TtPartPeriodReportPO newPartPO = new TtPartPeriodReportPO();
		newPartPO = assemblePO(newPartPO, vo);
		newPartPO.setId(Long.parseLong(SequenceManager.getSequence("")));
		newPartPO.setCreateDate(new Date());
		dao.insert(newPartPO);
	}
	
	private <T extends PO> T assemblePO(T po, BaseVO vo) throws Exception {
		BeanUtils.copyProperties(po, vo);
		return po;
	}
}