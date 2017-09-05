package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.potentialCustomer.PotentialCustomerDao;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infodms.dms.po.TtPoCusLinkmanPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.vo.PoCusLinkmanInfoVO;
import com.infoservice.dms.chana.vo.PotentialCustomerVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: ORB41 
* @Description: TODO(接收潜在客户上报) 
* @author liuqiang 
* @date Sep 26, 2010 4:41:17 PM 
*
 */
public class ORB41 extends AbstractReceiveAction {
	private Logger LOG = Logger.getLogger(ORB41.class);
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private PotentialCustomerDao potentialCustomerDao = PotentialCustomerDao.getInstance();
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		LOG.info("====潜在客户上报开始====");
		Map<String, Serializable> bodys = msg.getBody();
		for (Entry<String, Serializable> entry : bodys.entrySet()) {
			PotentialCustomerVO vo = new PotentialCustomerVO();
			vo = (PotentialCustomerVO) entry.getValue();
			try {
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);	
				updatePotentialCustomer(vo);
				POContext.endTxn(true);
			} catch (Exception e) {
				POContext.endTxn(false);
				LOG.error("潜在客户上报失败 customerNo == " + vo.getCustomerNo() + " entityCode == " + vo.getEntityCode(), e);
				e.printStackTrace();
 			}finally{
				 POContext.cleanTxn();
			}	
		}
		LOG.info("====潜在客户上报结束====");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void updatePotentialCustomer(PotentialCustomerVO vo) throws Exception {
		LOG.info("潜在客户上报 customerNo == " + vo.getCustomerNo() + " entityCode == " + vo.getEntityCode());
		TmPotentialCustomerPO srcPo = new TmPotentialCustomerPO();
		Map<String, Object> map = deCommonDao.getDcsDealerCode2(vo.getEntityCode());
		srcPo.setEntityCode(map.get("DEALER_CODE").toString());
		srcPo.setCustomerNo(vo.getCustomerNo());
		List pos = potentialCustomerDao.select(srcPo);
		if (null != pos && pos.size() > 0) {
			LOG.info("存在此记录");
			//更新潜在客户主表
			TmPotentialCustomerPO destPo = assembleCustomer(srcPo, vo);
			potentialCustomerDao.update(srcPo, destPo);
			TtPoCusLinkmanPO linkPo = new TtPoCusLinkmanPO();
			linkPo.setCustomerNo(vo.getCustomerNo());
			LOG.info("删除潜在客户联系人子表.. " + vo.getCustomerNo());
			potentialCustomerDao.delete(linkPo);
		} else {
			//插入潜在客户主表
			LOG.info("不存在此记录");
			TmPotentialCustomerPO destPo = assembleCustomer(srcPo, vo);
			destPo.setId(Long.parseLong(SequenceManager.getSequence("")));
			destPo.setCreateDate(new Date());
			potentialCustomerDao.insert(destPo);
		}		
		//插入子表
		LOG.info("潜在客户主表插入或更新完成...");
		addLinkManInfo(vo);
		LOG.info("潜在客户子表插入完成...");
	}
	
	@SuppressWarnings("unchecked")
	private void addLinkManInfo(PotentialCustomerVO vo) {
		LinkedList linkMans = vo.getLinkManList();
		for (int i = 0; i < linkMans.size(); i++) {
			PoCusLinkmanInfoVO subVo = (PoCusLinkmanInfoVO) linkMans.get(i);
			TtPoCusLinkmanPO po = new TtPoCusLinkmanPO();
			po.setItemId(Long.parseLong(SequenceManager.getSequence("")));
			po.setCustomerNo(vo.getCustomerNo());
			po.setContactorDepartment(subVo.getContactorDepartment());
			po.setPositionName(subVo.getPositionName());
			po.setMobile(subVo.getMobile());
			po.setEMail(subVo.getEMail());
			po.setFax(subVo.getFax());
			po.setContactorName(subVo.getContactorName());
			po.setCompany(subVo.getCompany());
			po.setPhone(subVo.getPhone());
			po.setGender(subVo.getGender());
			po.setIsDefaultContactor(subVo.getIsDefaultContactor());
			po.setRemark(subVo.getRemark());
			po.setCreateDate(new Date());
			potentialCustomerDao.insert(po);
		}
	}
	
	private TmPotentialCustomerPO assembleCustomer(TmPotentialCustomerPO srcPo, PotentialCustomerVO vo) {
		TmPotentialCustomerPO po = new TmPotentialCustomerPO();
		po.setEntityCode(srcPo.getEntityCode());
		po.setCustomerNo(srcPo.getCustomerNo());
		po.setCustomerName(vo.getCustomerName());
		po.setLargeCustomerNo(vo.getLargeCustomerNo());
		po.setSodCustomerId(vo.getSodCustomerId());
		po.setCustomerType(vo.getCustomerType());
		po.setGender(vo.getGender());
		po.setBirthday(vo.getBirthday());
		po.setZipCode(vo.getZipCode());
		po.setCountryCode(vo.getCountryCode());
		po.setProvince(vo.getProvince());
		po.setCity(vo.getCity());
		po.setDistrict(vo.getDistrict());
		po.setAddress(vo.getAddress());
		po.setEMail(vo.getEMail());
		po.setCtCode(vo.getCtCode());
		po.setCertificateNo(vo.getCertificateNo());
		po.setHobby(vo.getHobby());
		po.setContactorMobile(vo.getContactorMobile());
		po.setContactorPhone(vo.getContactorPhone());
		po.setFax(vo.getFax());
		po.setEducationLevel(vo.getEducationLevel());
		po.setOwnerMarriage(vo.getOwnerMarriage());
		po.setBuyPurpose(vo.getBuyPurpose());
		po.setVocationType(vo.getVocationType());
		po.setPositionName(vo.getPositionName());
		po.setIsCrpvip(vo.getIsCrpvip());
		po.setIsFirstBuy(vo.getIsFirstBuy());
		po.setHasDriverLicense(vo.getHasDriverLicense());
		po.setRecommendEmpName(vo.getRecommendEmpName());
		po.setInitLevel(vo.getInitLevel());
		po.setIsWholesaler(vo.getIsWholesaler());
		po.setIntentLevel(vo.getIntentLevel());
		po.setFailConsultant(vo.getFailConsultant());
		po.setDelayConsultant(vo.getDelayConsultant());
		po.setIndustryFirst(vo.getIndustryFirst());
		po.setIndustrySecond(vo.getIndustrySecond());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“vo.getSoldBy()” --> “Long.parseLong(vo.getSoldBy())”
	     * Date:2017-06-29
	     */
		po.setSoldBy(Long.parseLong(vo.getSoldBy()));
		po.setCusSource(vo.getCusSource());
		po.setMediaType(vo.getMediaType());
		po.setIsReported(vo.getIsReported());
		po.setReportRemark(vo.getReportRemark());
		po.setReportDatetime(vo.getReportDatetime());
		po.setReportAuditingRemark(vo.getReportAuditingRemark());
		po.setReportAbortReason(vo.getReportAbortReason());
		po.setIsPersonDriveCar(vo.getIsPersonDriveCar());
		po.setAgeStage(vo.getAgeStage());
		po.setOrganTypeCode(vo.getOrganType());
		po.setIsDirect(vo.getIsDirect());
		po.setModifyReason(vo.getModifyReason());
		po.setFamilyIncome(vo.getFamilyIncome());
		po.setChoiceReason(vo.getChoiceReason());
		po.setBuyReason(vo.getBuyReason());
		po.setFoundDate(vo.getFoundDate());
		po.setUpdateDate(vo.getUpdateDate());
		po.setCampaignCode(vo.getCampaignCode());
		return po;
	}
	/*public static void main(String[] args) {
		 ContextUtil.loadConf();
		 POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		 ORB41 o = new ORB41();
		 try {
			 File file = new File("C:/20110729034249750000003605.dat");
			 InputStream is = new FileInputStream(file);
			 byte[] b = new byte[is.available()];
			 is.read(b, 0, b.length);
			 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
			 DEMessage msg = xml.convert(b);
			 System.out.println(msg.getAppName());
			 Map<String, Serializable> bodys = msg.getBody();
			 is.close();
			 o.handleExecutor(msg);
			 POContext.endTxn(true);
		 } catch (Exception e) {
			 	POContext.endTxn(false);
		        e.printStackTrace();
		     } finally{
		    	 POContext.cleanTxn();
		   }
	    }*/
}
