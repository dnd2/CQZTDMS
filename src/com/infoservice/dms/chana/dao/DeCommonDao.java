package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class DeCommonDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(DeCommonDao.class);
	private static final DeCommonDao dao = new DeCommonDao ();
	
	public static final DeCommonDao getInstance() {
		return dao;
	}
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据上端渠道code查询下端经销商公司code
	 * <p>
	 * @param dealerCode
	 * @return companyCode
	 * @throws Exception
	 */
	
	public Map<String, Object> getDmsDealerCode(String dealerCode) throws Exception {
		/*StringBuffer sql= new StringBuffer();
		sql.append("SELECT C.DMS_CODE,B.COMPANY_SHORTNAME\n" );
		sql.append("FROM TM_DEALER A, TM_COMPANY B, TI_DEALER_RELATION C\n" );
		sql.append("WHERE A.COMPANY_ID = B.COMPANY_ID\n" );
		sql.append("AND B.COMPANY_CODE = C.DCS_CODE\n" );
		sql.append("AND C.STATUS = 10011001\n" );
		sql.append("AND A.DEALER_CODE = '").append(dealerCode).append("'\n");*/
		
		Map<String, Object> map = this.getDmsDlrCode(dealerCode) ;
		if(map == null){
			throw new Exception("DCS端不存在编码为"+dealerCode+"的经销商记录");
		}else{
			return map;
		}
	}
	
	public Map<String, Object> getDmsDlrCode(String dealerCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT C.DMS_CODE,B.COMPANY_SHORTNAME\n" );
		sql.append("FROM TM_DEALER A, TM_COMPANY B, TI_DEALER_RELATION C\n" );
		sql.append("WHERE A.COMPANY_ID = B.COMPANY_ID\n" );
		sql.append("AND B.COMPANY_CODE = C.DCS_CODE\n" );
		sql.append("AND C.STATUS = 10011001\n" );
		sql.append("AND A.DEALER_CODE = '").append(dealerCode).append("'\n");
		
		return pageQueryMap(sql.toString(), null, getFunName());
	}
	
	public Map<String, Object> getDmsDlrCodeNew(Long dealerId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT C.DMS_CODE,B.COMPANY_SHORTNAME\n" );
		sql.append("FROM TM_DEALER A, TM_COMPANY B, vw_dealer_relation C\n" );
		sql.append("WHERE A.COMPANY_ID = B.COMPANY_ID\n" );
		sql.append("AND B.COMPANY_CODE = C.DCS_CODE\n" );
		sql.append("AND C.STATUS = 10011001\n" );
		sql.append("AND A.DEALER_ID = '").append(dealerId).append("'\n");
		
		return pageQueryMap(sql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getAllDmsCode(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DMS_CODE\n" );
		sql.append("FROM TI_DEALER_RELATION\n" );
		sql.append("WHERE STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		List<Map<String, Object>> codeList = pageQuery(sql.toString(), null, getFunName());
		return codeList;
	}
	
	/**
	 * 
	* @Title: getDcsDealerCode 
	* @Description: TODO(根据下端的dealerCode 查询上端的渠道dealerCode(售后专用)) 
	* @param @param dealerCode 下端dealerCode
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> getDcsDealerCode(String dealerCode) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID, DEALER_CODE, DEALER_NAME FROM TM_DEALER A \n");
		sql.append("\t WHERE A.COMPANY_ID = \n");
		sql.append("(SELECT B.COMPANY_ID FROM TM_COMPANY B \n");
		sql.append("\t WHERE B.COMPANY_CODE = \n");
		sql.append("(SELECT DCS_CODE from TI_DEALER_RELATION C \n");
		sql.append("\t WHERE C.DMS_CODE = '").append(dealerCode).append("')) \n");
		sql.append("AND A.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map == null || map.size() == 0) {
			throw new Exception("DCS端不存在编码为" + dealerCode + "的经销商记录");
		} else {
			return map;
		}
	}
	/**
	 * 
	* @Title: getDcsDealerCode2 
	* @Description: TODO(根据下端的dealerCode 查询上端的渠道dealerCode(销售专用)) 
	* @param @param dealerCode 下端dealerCode
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> getDcsDealerCode2(String dealerCode) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("select rownum,b.* from (\n" );
		sql.append("SELECT DEALER_ID, DEALER_CODE, DEALER_NAME FROM TM_DEALER A \n");
		sql.append("\t WHERE A.COMPANY_ID = \n");
		sql.append("(SELECT B.COMPANY_ID FROM TM_COMPANY B \n");
		sql.append("\t WHERE B.COMPANY_CODE = \n");
		sql.append("(SELECT DCS_CODE from TI_DEALER_RELATION C \n");
		sql.append("\t WHERE C.DMS_CODE = '").append(dealerCode).append("')) \n");
		//sql.append("AND A.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DVS);
		//sql.append("AND A.DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_01); //取消一级经销商限制 YH 2011.8.19
		sql.append(" ORDER BY A.DEALER_LEVEL ASC ) b where rownum = 1");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map == null || map.size() == 0) {
			throw new Exception("DCS端不存在编码为" + dealerCode + "的经销商记录");
		} else {
			return map;
		}
	}
	/**
	 * 
	* @Title: getDcsCompanyCode 
	* @Description: TODO(根据下端dealerCode取上端公司code) 
	* @param @param dealerCode 下端经销商公司code
	* @param @return    上端公司code
	* @return String    返回类型 
	 * @throws Exception 
	* @throws
	 */
	public Map<String, Object> getDcsCompanyCode(String dealerCode) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DCS_CODE, DMS_CODE FROM TI_DEALER_RELATION");
		sql.append(" WHERE DMS_CODE = '").append(dealerCode).append("'");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map == null || map.size() == 0) {
			throw new Exception("DCS端不存在编码为" + dealerCode + "的经销商记录");
		} else {
			return map;
		}
	}
	/**
	 * 
	* @Title: getOrgIdByDealerId 
	* @Description: TODO(根据经销商ID查询组织ID) 
	* @param @param dealerId
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	 */
	public Long getOrgIdByDealerId(Long dealerId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ORG_ID FROM TM_DEALER_ORG_RELATION");
		sql.append(" WHERE DEALER_ID = ").append(dealerId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return (Long.parseLong(String.valueOf(map.get("ORG_ID"))));
	}
	/**
	 * 
	* @Title: getDealerIdByComCode 
	* @Description: TODO(根据配件系统经销商code去DCS系统dealerId) 
	* @param @param comCode
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	 */
	public Map<String, Object> getDealerByComCode(String comCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.DEALER_ID, B.DEALER_CODE, DEALER_NAME \n");
		sql.append("FROM TM_COMPANY A, TM_DEALER B \n");
		sql.append("WHERE A.COMPANY_ID = B.COMPANY_ID");
		sql.append("  AND A.TOPUP_CODE = '").append(comCode).append("'");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map == null || map.size() == 0) {
			throw new IllegalArgumentException("DCS端不存在编码为" + comCode + "的经销商记录");
		} else {
			return map;
		}
	}
	
	public Map<String, Object> getPartByCode(String partCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT PART_ID \n");
		sql.append("FROM TM_PT_PART_BASE A \n");
		sql.append("WHERE PART_CODE = '").append(partCode).append("'");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map == null || map.size() == 0) {
			throw new IllegalArgumentException("DCS端不存在编码为" + partCode + "的配件");
		} else {
			return map;
		}
	}
	
	public static void main(String[] args) throws Exception {

	}
	
}
