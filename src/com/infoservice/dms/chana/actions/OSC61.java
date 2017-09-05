package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.po.TmCompanyPO;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.ExamineTargetDao;
import com.infoservice.dms.chana.po.TtAssessGuidelinePO;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.ExamineTargetVO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSC61  extends AbstractReceiveAction{
	private static final Logger logger = Logger.getLogger(OSC61.class);
	private ExamineTargetDao dao = ExamineTargetDao.getInstance();
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	
	public static void main(String[] args) {
	 ContextUtil.loadConf();
	 try {
		 File file = new File("D:/20120528155340024000024568.dat");
		 InputStream is = new FileInputStream(file);
		 byte[] b = new byte[is.available()];
		 is.read(b, 0, b.length);
		 OSC61 o = new OSC61();
		 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
		 DEMessage msg = xml.convert(b);
		 System.out.println(msg.getAppName());
		 is.close();
		 o.handleExecutor(msg);
	 	 }catch (Exception e) {
	        e.printStackTrace();
	     }
	}

	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		// TODO Auto-generated method stub
		logger.info("====考核指标上报开始====");
		Map<String, Serializable> bodys = msg.getBody();
			try {
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
				for (Entry<String, Serializable> entry : bodys.entrySet()) {
					ExamineTargetVO vo = new ExamineTargetVO();
					vo = (ExamineTargetVO)entry.getValue();
					untieExamineTargetVO(vo);
					}
				POContext.endTxn(true);
				} catch (Exception e) {
					POContext.endTxn(false);
					logger.error("考核指标上报失败", e);								
			}finally{
				 POContext.cleanTxn();
			}			
		logger.info("====考核指标上报结束====");
		return null;
	}
	
	private void untieExamineTargetVO(ExamineTargetVO vo) throws Exception{
		TtAssessGuidelinePO po = new TtAssessGuidelinePO();
		Map companyCode = commonDao.getDcsCompanyCode(vo.getEntityCode());
		long companyId = this.getCompanyIdByCode(companyCode.get("DCS_CODE").toString());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String uploadDate = sf.format(vo.getUploadDate());
		po = assemblePO(po,vo);
		po.setCompanyId(companyId);
		po.setSalesCar(dao.getSalesCar(uploadDate, companyId));
		po.setClaimSheetCount(dao.getClaimSheetCount(uploadDate, companyId));
		po.setCreateDate(new Date());
		Date time = dao.getTime(uploadDate,companyId);
		//检测是否当天重复上报
		if(time!=null){
			if(po.getDownTimestamp().after(time)){
				TtAssessGuidelinePO po1 = new TtAssessGuidelinePO();
				po1.setUploadDate(vo.getUploadDate());
				po1.setCompanyId(companyId);
				dao.update(po1, po);
				logger.info("Update TtAssessGuidelinePO success.");
			}
		}else{
			dao.insert(po);
			logger.info("Insert TtAssessGuidelinePO success.");
		}
	}
	
	private <T extends PO> T assemblePO(T po, BaseVO vo) throws Exception {

		BeanUtils.copyProperties(po, vo);

		return po;
	}
	
	@SuppressWarnings("unchecked") //通过companyCode得到companyId
	public long getCompanyIdByCode(String companyCode)throws Exception{
	  TmCompanyPO tc = 	new TmCompanyPO();
	  tc.setCompanyCode(companyCode);
	  List list =  commonDao.select(tc);
	  if(list.size()>0){	  
		  tc  = (TmCompanyPO)list.get(0);	  
	  }
	  return tc.getCompanyId();
	}
}
