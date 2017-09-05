/**********************************************************************
* <pre>
* FILE : ServiceActivityReclaimDao.java
* CLASS : ServiceActivityReclaimDao
*
* AUTHOR : PGM
*
* FUNCTION :  生产商再索赔
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-13| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ManufacturerReClaimDao.java,v 1.1 2010/08/16 01:44:22 yuch Exp $
 */
package com.infodms.dms.dao.claim.other;
import java.sql.ResultSet;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  生产商再索赔
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ManufacturerReClaimDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ManufacturerReClaimDao.class);
	private static final ManufacturerReClaimDao dao = new ManufacturerReClaimDao ();
	public  static final ManufacturerReClaimDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 生产商再索赔
	 * @param           : 申请单结算日
	 * @param           : 生产商代码
	 * @param           : 生产商名称
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的生产商再索赔结算费用清单信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 生产商再索赔
	 */
	public  PageResult<Map<String, Object>>  getManufacturerReClaimQuery(String producerCode,String producerName ,String startdate,String enddate,String oemCompanyId, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	   SELECT  DECODE(a.CLAIM_TYPE, '', '---', a.CLAIM_TYPE,a.CLAIM_TYPE) as CLAIM_TYPE,DECODE(a.GUARANTEE_DATE, '', '---', a.GUARANTEE_DATE,a.GUARANTEE_DATE) as GUARANTEE_DATE , \n");
		sql.append("       DECODE(a.IN_MILEAGE, '', '---', a.IN_MILEAGE,a.IN_MILEAGE) as IN_MILEAGE,    \n");
		sql.append("       DECODE(c.PRODUCER_CODE, '', '---', c.PRODUCER_CODE,c.PRODUCER_CODE) as PRODUCER_CODE,    \n");
		sql.append("       DECODE(c.PRODUCER_NAME, '', '---', c.PRODUCER_NAME,c.PRODUCER_NAME) as PRODUCER_NAME,   \n");
		sql.append("       DECODE(a.RO_NO, '', '---', a.RO_NO,a.RO_NO) as RO_NO,    \n");
		sql.append("       DECODE(a.LINE_NO, '', '---', a.LINE_NO,a.LINE_NO) as LINE_NO ,   \n");
		sql.append("       DECODE(a.VIN, '', '---', a.VIN,a.VIN) as VIN ,   \n");
		sql.append("       DECODE(c.PART_CODE, '', '---', c.PART_CODE,c.PART_CODE) as PART_CODE,    \n");
		sql.append("       DECODE(c.PART_NAME, '', '---', c.PART_NAME,c.PART_NAME) as PART_NAME ,   \n");
		sql.append("       DECODE(b.LABOUR_AMOUNT, '', '0.00', b.LABOUR_AMOUNT,b.LABOUR_AMOUNT) as LABOUR_AMOUNT ,   \n");
		sql.append("       DECODE(b.PARTS_AMOUNT, '', '0.00', b.PARTS_AMOUNT,b.PARTS_AMOUNT) as PARTS_AMOUNT,    \n");
		sql.append("       DECODE(b.OTHERITEM_AMOUNT, '', '0.00', b.OTHERITEM_AMOUNT,b.OTHERITEM_AMOUNT) as OTHERITEM_AMOUNT,    \n");
		sql.append("       DECODE(b.GROSS_CREDIT, '', '0.00', b.GROSS_CREDIT,b.GROSS_CREDIT) as GROSS_CREDIT,    \n");
		sql.append("       DECODE(e.GROUP_NAME, '', '---', e.GROUP_NAME,e.GROUP_NAME) as GROUP_NAME,    \n");
		sql.append("       DECODE(to_char(d.PURCHASED_DATE, 'yyyy-MM-dd'), '', '---', to_char(d.PURCHASED_DATE, 'yyyy-MM-dd'),to_char(d.PURCHASED_DATE, 'yyyy-MM-dd')) as PURCHASED_DATE   \n");
		sql.append("       FROM TT_AS_WR_APPLICATION a ,TT_AS_WR_APPPAYMENT  b, TT_AS_WR_PARTSITEM   c ,TM_VEHICLE  d,  TM_VHCL_MATERIAL_GROUP e  \n");
		sql.append("       WHERE   a.RO_NO = b.RO_NO AND a.LINE_NO = b.LINE_NO AND c.ID = a.ID AND  a.vin = d.vin  AND  d.MODEL_ID = e.GROUP_ID AND a.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_07+"  \n");//结算支付
		sql.append(" 	   AND  a.OEM_COMPANY_ID = ");
		sql.append(oemCompanyId);
		sql.append("\n");
		
		if(!"".equals(producerCode)&&null!=producerCode){//生产商代码不为空
			sql.append("		AND  c.PRODUCER_CODE like '%"+producerCode+"%'  \n");
		}
		if(!"".equals(producerName)&&null!=producerName){//生产商名称不为空
			sql.append("		AND  c.PRODUCER_NAME like '%"+producerName+"%'  \n");
		}
		
		if(!"".equals(startdate)&&!(null==startdate)){      //申请单结算日[开始]不为空
			sql.append("		AND a.CREATE_DATE >=to_date('"+startdate+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");

		}
		if(!"".equals(enddate)&&!(null==enddate)){         //申请单结算日[结束]不为空
			sql.append("	    AND a.CREATE_DATE  <= to_date('"+enddate+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            ORDER BY a.RO_NO desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}