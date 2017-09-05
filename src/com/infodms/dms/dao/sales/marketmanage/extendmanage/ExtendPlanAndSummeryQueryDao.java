package com.infodms.dms.dao.sales.marketmanage.extendmanage;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExtendPlanAndSummeryQueryDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ExtendPlanAndSummeryQueryDao.class);
	private static final ExtendPlanAndSummeryQueryDao dao = new ExtendPlanAndSummeryQueryDao();
	public static final ExtendPlanAndSummeryQueryDao getInstance() {
		return dao;
	}

	public PageResult<Map<String, Object>> extendPlanQuery(Map<String,String> map,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
        String dealerCode=map.get("dealerCode");
        String planType=map.get("planType");
        String charge=map.get("charge");
        String place=map.get("place");
        String beginDate=map.get("beginDate");
        String endDate=map.get("endDate");
        String dutyType=map.get("dutyType");
        String orgId=map.get("orgId");

        StringBuilder sql= new StringBuilder();

        sql.append("SELECT TEP.PLAN_ID,\n" );
        sql.append("       TEP.PLAN_TYPE,\n" );
        sql.append("       TEP.CHARGE,\n" );
        sql.append("       TEP.TEL,\n" );
        sql.append("       TEP.PLACE,\n" );
        sql.append("       TO_CHAR(TEP.BEGIN_DATE, 'YYYY-MM-DD') BEGIN_DATE,\n" );
        sql.append("       TO_CHAR(TEP.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
        sql.append("       TEP.PRE_GUEST_NUM,\n" );
        sql.append("       TEP.ACT_GUEST_NUM,\n" );
        sql.append("       TEP.PRE_CARD_NUM,\n" );
        sql.append("       TEP.ACT_CARD_NUM,\n" );
        sql.append("       TEP.PRE_DEAL_NUM,\n" );
        sql.append("       TEP.ACT_DEAL_NUM,\n" );
        sql.append("       TEP.GUEST_RATE||'%' GUEST_RATE,\n" );
        sql.append("       TEP.CARD_RATE||'%' CARD_RATE,\n" );
        sql.append("       TEP.DEAL_RATE||'%' DEAL_RATE,\n" );
        sql.append("       TEP.STATUS,\n" );
        sql.append("       VOD.PQ_ORG_NAME,\n" );
        sql.append("       VOD.ROOT_ORG_NAME,\n" );
        sql.append("       TD.DEALER_SHORTNAME\n" );
        sql.append("  FROM TT_EXTEND_PLAN TEP,VW_ORG_DEALER VOD,TM_DEALER TD\n" );
        sql.append(" WHERE 1=1\n" );
        sql.append("   AND TEP.DEALER_ID = VOD.DEALER_ID\n" );
        sql.append("   AND TEP.DEALER_ID = TD.DEALER_ID\n" );
        sql.append("   AND TEP.STATUS IN(1,2)\n" );

        //如果是大区用户 只显示大区下边所有经销商
        if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
            sql.append("AND TEP.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(dutyType.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
            sql.append("AND TEP.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }

		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            String str="";

            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
            str=str.substring(0,str.lastIndexOf(","));

			sql.append("AND VOD.DEALER_CODE IN("+str+")\n");
		}
		if(!"".equals(planType)&&planType!=null){
			sql.append("AND TEP.PLAN_TYPE ="+planType+"\n");
		}
		if(!"".equals(charge)&&charge!=null){
			sql.append("AND TEP.CHARGE LIKE'%"+charge+"%'\n");
		}
		if(!"".equals(place)&&place!=null){
			sql.append("AND TEP.PLACE LIKE'%"+place+"%'\n");
		}
		if(!"".equals(beginDate)&&beginDate!=null){
			sql.append("AND TRUNC(TEP.BEGIN_DATE)>=TO_DATE('"+beginDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("AND TRUNC(TEP.END_DATE)<=TO_DATE('"+endDate+"','YYYY-MM-DD')\n");
		}
        sql.append("ORDER BY TEP.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * 通过业务主键取得附件信息
	 * @param
	 * @return
	 */
	public List<Map<String, Object>>  getAttachInfos(String ywzjs){

		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ ="+ywzjs+"ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
