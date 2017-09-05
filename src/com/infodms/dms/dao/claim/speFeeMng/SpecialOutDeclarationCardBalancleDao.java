/**********************************************************************
* <pre>
* FILE : SpecialOutDeclarationCardBalancleDao.java
* CLASS : SpecialOutDeclarationCardBalancleDao
*
* AUTHOR : PGM
*
* FUNCTION : 特殊外出费用结算单.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-15| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: SpecialOutDeclarationCardBalancleDao.java,v 1.1 2010/08/16 01:43:33 yuch Exp $
 */
package com.infodms.dms.dao.claim.speFeeMng;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsWrSpeoutfeeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrSpeoutfeeAuditingPO;
import com.infodms.dms.po.TtAsWrSpeoutfeePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  特殊外出费用结算单
 * @author        :  PGM
 * CreateDate     :  2010-07-15
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class SpecialOutDeclarationCardBalancleDao extends BaseDao{
	public static Logger logger = Logger.getLogger(SpecialOutDeclarationCardBalancleDao.class);
	private static final SpecialOutDeclarationCardBalancleDao dao = new SpecialOutDeclarationCardBalancleDao ();
	public  static final SpecialOutDeclarationCardBalancleDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 特殊外出费用结算单
	 * @param           : 费用单据编码
	 * @param           : 巡航单据编码
	 * @param           : 经销商代码
	 * @param           : 经销商名称
	 * @param           : 费用单据上报开始时间
	 * @param           : 费用单据上报结束时间
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的特殊外出费用结算单信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 特殊外出费用结算单
	 */
	public  PageResult<Map<String, Object>>  getSpecialBalancleQuery(TtAsWrSpeoutfeeBean feeBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select s.ID,s.CR_ID,d.DEALER_CODE,d.DEALER_SHORTNAME,s.FEE_NO,c.CR_NO,to_char(s.MAKE_DATE,'yyyy-MM-dd')as MAKE_DATE,c.CR_WHITHER,  \n");
		sql.append("   sum(PASS_FEE)+sum(TRAFFIC_FEE)+sum(QUARTER_FEE)+sum(EAT_FEE)+sum(PERSON_SUBSIDE) as FEE   \n");
		sql.append("    from TM_DEALER d,TT_AS_WR_SPEOUTFEE s ,TT_AS_WR_CRUISE c    where 1=1  and c.id=s.cr_id and c.dealer_id=d.dealer_id  and s.STATUS="+Constant.SPE_OUTFEE_STATUS_04+" \n");//已审核
		if(!"".equals(feeBean.getCompanyId())&&!(null==feeBean.getCompanyId())){           //公司ID不为空
			sql.append("		AND s.COMPANY_ID ='"+feeBean.getCompanyId()+"' \n");
		}
		if(!"".equals(feeBean.getFeeNo())&&!(null==feeBean.getFeeNo())){           //费用单据编码不为空
			sql.append("		AND UPPER(s.FEE_NO) like UPPER('%"+feeBean.getFeeNo()+"%') \n");
		}
		if(!"".equals(feeBean.getCrNo())&&!(null==feeBean.getCrNo())){           //巡航单据编码不为空
			sql.append("		AND UPPER(c.CR_NO) like UPPER('%"+feeBean.getCrNo()+"%') \n");
		}
		if(!"".equals(feeBean.getDealerCode())&&!(null==feeBean.getDealerCode())){//经销商代码不为空
			sql.append("		AND UPPER(d.DEALER_CODE) like UPPER('%"+feeBean.getDealerCode()+"%') \n");
		}
		if(!"".equals(feeBean.getDealerName())&&!(null==feeBean.getDealerName())){//经销商名称不为空
			sql.append("		AND d.DEALER_NAME like '%"+feeBean.getDealerName()+"%' \n");
		}
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if(!"".equals(feeBean.getStartDate())&&!(null==feeBean.getStartDate())){      //活动开始日期不为空
			sql.append("		AND s.MAKE_DATE >=to_date('"+df.format(feeBean.getStartDate())+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(feeBean.getEndDate())&&!(null==feeBean.getEndDate())){         //活动结束日期不为空
			sql.append("	    AND s.MAKE_DATE  <= to_date('"+df.format(feeBean.getEndDate())+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            GROUP By s.ID,s.CR_ID,d.DEALER_CODE,d.DEALER_SHORTNAME,s.FEE_NO,c.CR_NO, s.MAKE_DATE,c.CR_WHITHER  \n");
		sql.append("            ORDER BY  s.ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function       :  根据特殊外出费用ID，查询特殊外出费用结算单具体信息
	 * @param         :  request-规则ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public  TtAsWrSpeoutfeeBean  SpecialBalancleUpdateQueryInit(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select s.ID, s.FEE_NO,c.CR_NO,c.CREATE_BY,to_char(s.MAKE_DATE,'yyyy-MM-dd')as MAKE_DATE,d.DEALER_SHORTNAME,   \n");
		sql.append("  to_char(s.START_DATE,'yyyy-MM-dd')as START_DATE,to_char(s.END_DATE,'yyyy-MM-dd')as END_DATE,abs(s.START_DATE-s.END_DATE)+1 CR_DAY,c.CR_WHITHER,s.PERSON_NUM,s.PERSON_NAME,s.SINGLE_MILEAGE,   \n");
		sql.append("  s.PASS_FEE,s.TRAFFIC_FEE,s.QUARTER_FEE,s.EAT_FEE,s.PERSON_SUBSIDE,  s.FEE_CHANNEL, \n");
		sql.append("  sum(PASS_FEE)+sum(TRAFFIC_FEE)+sum(QUARTER_FEE)+sum(EAT_FEE)+sum(PERSON_SUBSIDE) as  FEE, s.APPLY_CONTENT   \n");
		sql.append("  from TT_AS_WR_SPEOUTFEE s ,TT_AS_WR_CRUISE c,TM_DEALER d   \n");
		sql.append("  WHERE  1=1 and c.id=s.cr_id and c.dealer_id=d.dealer_id \n");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND s.ID = ? ");
		params.add(id);
		}
		sql.append("  GROUP BY  s.ID,s.FEE_NO,c.CR_NO,c.CREATE_BY,s.MAKE_DATE,d.DEALER_SHORTNAME, \n ");
		sql.append("  s.START_DATE,s.END_DATE,c.CR_DAY,c.CR_WHITHER,s.PERSON_NUM,s.PERSON_NAME,s.SINGLE_MILEAGE, \n ");
		sql.append("  s.PASS_FEE,s.TRAFFIC_FEE,s.QUARTER_FEE,s.EAT_FEE,s.PERSON_SUBSIDE,s.FEE_CHANNEL,s.APPLY_CONTENT  \n ");
		TtAsWrSpeoutfeeBean feeBean=new TtAsWrSpeoutfeeBean();
		List list = dao.select(TtAsWrSpeoutfeeBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				feeBean = (TtAsWrSpeoutfeeBean) list.get(0);
			}
		}
		return feeBean;
	}
	
	/**
	 * Function       :  根据特殊外出费用ID，查询车辆信息 
	 * @param         :  request-规则ID
	 * @return        :  特殊外出费用结算单---审核明细记录
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public  List<TtAsWrSpeoutfeeBean>  SpecialBalancleUpdateQueryList(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select v.ID,v.FEE_ID , v.VIN,v.ENGINE_NO,v.MODEL,to_char(v.PRODUCT_DATE,'yyyy-MM-dd') as PRODUCT_DATE ,v.MILEAGE,v.CUSTOMER_NAME,v.CUSTOMER_PHONE,to_char(v.SALE_DATE,'yyyy-MM-dd') as SALE_DATE,v.REMARK   \n");
		sql.append("  from TT_AS_WR_SPEOUTFEE_VEHICLE v   \n");
		sql.append("  WHERE  1=1 \n");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND v.FEE_ID = ? \n ");
		params.add(id);
		}
		sql.append("  ORDER BY  v.ID DESC   \n ");
		List list = dao.select(TtAsWrSpeoutfeeBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function       :  根据特殊外出费用ID，查询车辆信息 
	 * @param         :  request-规则ID
	 * @return        :  特殊外出费用结算单---车辆信息 
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public  List<TtAsWrSpeoutfeeBean>  SpecialBalancleQueryList(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("   select s.ID,s.FEE_ID,to_char(s.AUDITING_DATE,'yyyy-MM-dd')as AUDITING_DATE,s.AUDITING_PERSON,s.PRESON_DEPT,s.AUDITING_OPINION,s.STATUS, ORG.ORG_NAME, T.NAME   \n");
		sql.append("   from TT_AS_WR_SPEOUTFEE_AUDITING s   left join TM_ORG ORG  on s.PRESON_DEPT = ORG.ORG_ID  left join TC_USER T   on s.AUDITING_PERSON = T.USER_ID \n");
		sql.append("   WHERE  1=1 \n");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND s.FEE_ID = ? \n ");
		params.add(id);
		}
		sql.append("  ORDER BY  s.ID DESC   \n ");
		List list = dao.select(TtAsWrSpeoutfeeBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,特殊外出费用结算单【审核】
	 * @param         :  request-ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public static void  SpecialBalancleInsert(TtAsWrSpeoutfeeAuditingPO   AuditingPO ){
		dao.insert(AuditingPO);
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,特殊外出费用结算单【审核】
	 * @param         :  request-ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public static void  SpecialBalancle(TtAsWrSpeoutfeePO feePO,TtAsWrSpeoutfeePO feePOContent){
		dao.update(feePO, feePOContent);
	}
	/**
     * Function：获得附件信息列表
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-15
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ=(select id from Tt_As_Wr_Speoutfee where id='"+id+"')");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
}