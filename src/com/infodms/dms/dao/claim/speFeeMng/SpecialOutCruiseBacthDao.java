/**********************************************************************
* <pre>
* FILE : SpecialOutCruiseBacthDao.java
* CLASS : SpecialOutCruiseBacthDao
*
* AUTHOR : PGM
*
* FUNCTION : 巡航服务线路批复单.
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
 * $Id: SpecialOutCruiseBacthDao.java,v 1.1 2010/08/16 01:43:33 yuch Exp $
 */
package com.infodms.dms.dao.claim.speFeeMng;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsWrSpeoutfeeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrCruiseAuditingPO;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  巡航服务线路批复单
 * @author        :  PGM
 * CreateDate     :  2010-07-15
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class SpecialOutCruiseBacthDao extends BaseDao{
	public static Logger logger = Logger.getLogger(SpecialOutCruiseBacthDao.class);
	private static final SpecialOutCruiseBacthDao dao = new SpecialOutCruiseBacthDao ();
	public  static final SpecialOutCruiseBacthDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 巡航服务线路批复单
	 * @param           : 费用单据编码
	 * @param           : 巡航单据编码
	 * @param           : 经销商代码
	 * @param           : 经销商名称
	 * @param           : 费用单据上报开始时间
	 * @param           : 费用单据上报结束时间
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的巡航服务线路批复单信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 巡航服务线路批复单
	 */
	public  PageResult<Map<String, Object>>  getSpecialBacthQuery(TtAsWrSpeoutfeeBean feeBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select c.ID,c.CR_NO,d.DEALER_CODE,d.DEALER_SHORTNAME,  \n");
		sql.append("    to_char(c.MAKE_DATE,'yyyy-MM-dd') as  MAKE_DATE ,c.CR_WHITHER,c.CR_PRINCIPAL    \n");
		sql.append("     from  TT_AS_WR_CRUISE c , TM_DEALER d    where 1=1  and c.DEALER_ID =d.DEALER_ID and c.STATUS="+Constant.CURI_SERVICE_STATUS_04+" \n");//已上报
		if(!"".equals(feeBean.getCompanyId())&&!(null==feeBean.getCompanyId())){  //公司ID不为空
			sql.append("		AND c.COMPANY_ID = '"+feeBean.getCompanyId()+"' \n");
		}
		if(!"".equals(feeBean.getCrWhither())&&!(null==feeBean.getCrWhither())){  //巡航目的地不为空
			sql.append("		AND c.CR_WHITHER like '%"+feeBean.getCrWhither()+"%' \n");
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
		//SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if(!"".equals(feeBean.getStartDate())&&!(null==feeBean.getStartDate())){      //活动开始日期不为空
			sql.append("		AND c.MAKE_DATE >=to_date('"+feeBean.getStartDate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(feeBean.getEndDate())&&!(null==feeBean.getEndDate())){         //活动结束日期不为空
			sql.append("	    AND c.MAKE_DATE  <= to_date('"+feeBean.getEndDate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            ORDER BY  c.ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function       :  根据特殊外出费用ID条件,查询巡航服务线路批复单中符合条件的信息，其中包括：
	 * @param         :  request-ID
	 * @return        :  巡航服务线路批复单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public  TtAsWrSpeoutfeeBean  specialBacthQueryInit(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("      select c.ID,c.CR_NO,d.DEALER_CODE,d.DEALER_SHORTNAME,    \n");
		sql.append("      to_char(MAKE_DATE,'yyyy-MM-dd') as  MAKE_DATE ,c.CR_WHITHER,c.CR_MILEAGE,c.CR_DAY,c.CR_PRINCIPAL, \n");
		sql.append("      c.CR_PHONE,c.CR_CAUSE ,nvl(b.region_name,'')  PRIVINCE_NAME  \n");
		sql.append("      from  TT_AS_WR_CRUISE c , TM_DEALER d  ,tm_region b      \n");
		sql.append("      where 1=1  and c.DEALER_ID =d.DEALER_ID  and  d.province_id=b.region_code(+)  \n");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND c.ID = ? ");
		params.add(id);
		}
		sql.append("  ORDER BY  c.ID desc    \n ");
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
	 * Function       :  根据特殊外出费用ID条件,查询巡航服务线路批复单中符合条件的信息，其中包括：
	 * @param         :  request-ID
	 * @return        :  巡航服务线路批复单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public  List<TtAsWrSpeoutfeeBean>  specialBacthQueryList(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select a.ID,a.CR_ID,to_char(a.AUDITING_DATE,'yyyy-MM-dd')as AUDITING_DATE,a.AUDITING_PERSON,a.PRESON_DEPT,a.STATUS,a.AUDITING_OPINION , ORG.ORG_NAME,   T.NAME \n");
		sql.append("  from TT_AS_WR_CRUISE_AUDITING a  left join TM_ORG ORG  on a.PRESON_DEPT = ORG.ORG_ID  left join TC_USER T   on a.AUDITING_PERSON = T.USER_ID   \n");
		sql.append("  WHERE  1=1 \n");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND a.cr_id = ? \n ");
		params.add(id);
		}
		sql.append("  ORDER BY  a.ID DESC   \n ");
		List list = dao.select(TtAsWrSpeoutfeeBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,巡航服务线路批复单【审核】
	 * @param         :  request-ID
	 * @return        :  巡航服务线路批复单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public static void  specialBacth(TtAsWrCruiseAuditingPO AuditingPOContent){
		dao.insert(AuditingPOContent);
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,巡航服务线路批复单【批复】
	 * @param         :  request-ID
	 * @return        :  巡航服务线路批复单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public static void  specialBacthUpdate(TtAsWrCruisePO CruisePO, TtAsWrCruisePO CruisePOContent){
		dao.update(CruisePO,CruisePOContent);
	}
	
}