package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class IncomingCallDetailReportDao extends BaseDao {

	private static final IncomingCallDetailReportDao dao = new IncomingCallDetailReportDao();

	public static final IncomingCallDetailReportDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageResult<Map<String, Object>> queryIncomingCallDetailReportforms(
			String dealName, String dateStart, String dateEnd, int pageSize,
			int curPage) {
		String sql = returnIncomingCallDetailReportformsSql(dealName,
				dateStart, dateEnd);

		return (PageResult<Map<String, Object>>) this.pageQuery(sql, null,
				this.getFunName(), pageSize, curPage);
	}

	private String returnIncomingCallDetailReportformsSql(String dealName,
			String dateStart, String dateEnd) {
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT TI.CREATE_DATE,CASE TI.CP_CHANEL_TYPE \n");
		sbSql.append(" WHEN 90001002 THEN '网络'\n");
		sbSql.append(" WHEN 90001003 THEN '数字营销转'\n");
		sbSql.append(" ELSE '来电' END INCOMING_TYPE,\n");
		sbSql.append(" TI.CP_NAME,TI.CP_PHONE,TI.CP_PROXY_AREA,TI.CP_UPDATE_CONTENT,\n ");
		sbSql.append(" TC.CODE_DESC as CP_CUSTOMER_KNOWN_CHANEL,TI.CP_VIN,TI.CP_MILEAGE,td.DEALER_NAME as CP_DEAL_DEALER,TI.CP_SEAT_COMMENT,TC2.CODE_DESC as CP_STATUS,\n ");// TI.CP_CONTACT,
		sbSql.append(" TI.CP_CONTENT,TC1.CODE_DESC as CP_CUSTOMER_TYPE,tu.NAME as CREATE_BY,nvl(tog.ORG_NAME,toc.ORG_NAME) as CP_DEAL_ORG,tu1.NAME as CP_RESPONSER,\n");
		sbSql.append(" CASE TI.CP_BIZ_TYPE \n");
		sbSql.append(" WHEN 9505 THEN '投诉'\n");
		sbSql.append(" ELSE '咨询' END CP_BIZ_TYPE,\n");
		sbSql.append(" tu2.NAME as UPDATE_BY,TI.UPDATE_DATE,TI.CP_SERIES_ID,th.GROUP_NAME as CP_MODEL_ID \n");
		sbSql.append(" FROM TT_CRM_INCOMING_CALL TI\n");
		// 了解渠道
		sbSql.append(" LEFT JOIN (SELECT CODE_ID,CODE_DESC FROM TC_CODE WHERE TYPE=7003) TC ON TI.CP_CUSTOMER_KNOWN_CHANEL=TC.CODE_ID\n");
		// 状态
		sbSql.append(" LEFT JOIN (SELECT CODE_ID,CODE_DESC FROM TC_CODE WHERE TYPE=9529) TC2 ON TI.CP_STATUS=TC2.CODE_ID\n");
		// 来电客户类型
		sbSql.append(" LEFT JOIN (SELECT CODE_ID,CODE_DESC FROM TC_CODE WHERE TYPE=7002) TC1 ON TI.CP_CUSTOMER_TYPE=TC1.CODE_ID\n");
		// 投诉咨询类型
		sbSql.append(" LEFT JOIN (SELECT CODE_ID,CODE_DESC FROM TC_CODE WHERE TYPE=9000) TC3 ON TI.CP_CHANEL_TYPE=TC3.CODE_ID\n");
		// 受理人
		sbSql.append(" LEFT JOIN (SELECT USER_ID,NAME FROM TC_USER )tu ON TI.CP_ACC_USER=tu.USER_ID\n");
		// 来电转大区或部门
		sbSql.append(" LEFT JOIN TM_ORG tog ON tog.ORG_ID=TI.CP_DEAL_ORG \n");
		sbSql.append(" LEFT JOIN TM_ORG_CUSTOM toc ON toc.ORG_ID=TI.CP_DEAL_ORG \n");
		// 当前处理的负责人
		sbSql.append(" LEFT JOIN (SELECT USER_ID,NAME FROM TC_USER )tu1 ON TI.CP_RESPONSER=tu1.USER_ID \n");
		// 最近跟进人
		sbSql.append(" LEFT JOIN (SELECT USER_ID,NAME FROM TC_USER )tu2 ON TI.UPDATE_BY=tu2.USER_ID \n");
		// 抱怨经销商ID
		sbSql.append(" LEFT JOIN TM_DEALER td ON td.DEALER_ID=TI.CP_DEAL_DEALER\n");
		// 车型
		sbSql.append(" LEFT JOIN (select distinct * from tm_vhcl_material_group t where t.group_level = 3 and t.status=10011001) th ON th.GROUP_ID=TI.CP_MODEL_ID\n");
		// 条件查询
		sbSql.append(" WHERE 1=1 \n");
		if (StringUtil.notNull(dealName)) {
			sbSql.append(" and tu.NAME like '%" + dealName + "%' \r\n");
		}

		if (StringUtil.notNull(dateStart)) {
			sbSql.append(" and TI.cp_acc_date >= to_date('" + dateStart
					+ "','yyyy-MM-dd')\r\n");
		}
		if (StringUtil.notNull(dateEnd)) {
			sbSql.append(" and TI.cp_acc_date <= to_date('" + dateEnd
					+ " 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		// sbSql.append("SELECT ROWNUM ID, TYPENAME, BIZCONTENT, nvl(COUNTDESC,0) COUNTDESC, nvl(COUNTRATE,0) COUNTRATE\n");
		// sbSql.append("  FROM (SELECT B.TYPE_NAME TYPENAME,\n");
		// sbSql.append("               B.CODE_DESC BIZCONTENT,\n");
		// sbSql.append("               COUNT(NVL(B.CODE_DESC, 0)) COUNTDESC,\n");
		// sbSql.append("               DECODE(INSTR(TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100,4)),'.'),\n");
		// sbSql.append("                      1,0 ||TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100, 4)),\n");
		// sbSql.append("                      TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100, 4))) COUNTRATE\n");
		// sbSql.append("          FROM TT_CRM_COMPLAINT A\n");
		// sbSql.append("          LEFT JOIN TC_CODE B ON A.CP_BIZ_CONTENT = B.CODE_ID\n");
		// sbSql.append("          LEFT JOIN TC_USER F ON A.CP_ACC_USER = F.USER_ID\n");
		// sbSql.append("         where a.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+"\r\n");
		/*
		 * if(StringUtil.notNull(dealName)){
		 * sbSql.append(" and f.name like '%"+dealName+"%' \r\n"); }
		 * 
		 * if(StringUtil.notNull(dateStart)){
		 * sbSql.append(" and a.cp_acc_date >= to_date('"
		 * +dateStart+"','yyyy-MM-dd')\r\n"); } if(StringUtil.notNull(dateEnd)){
		 * sbSql.append(" and a.cp_acc_date <= to_date('"+dateEnd+
		 * " 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n"); }
		 * sbSql.append("         group by b.type_name, b.code_desc\r\n");
		 * sbSql.append("         order by 1, 3 desc)\r\n");
		 * sbSql.append("        union all\r\n");
		 * sbSql.append("        select "+tatal+",'合计', '', "+tatal+",'' \r\n");
		 * sbSql.append("          from dual \r\n");
		 */

		return sbSql.toString();
	}

	/**
	 * 来电明细导出
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> incomingCallDetailQueryExport(
			String dealName, String dateStart, String dateEnd) throws Exception {
		Object[] obj = new Object[2];
		obj[0] = returnIncomingCallDetailReportformsSql(dealName, dateStart,
				dateEnd);
		obj[1] = null;
		List<Map<String, Object>> list = dao.pageQuery(String.valueOf(obj[0]),
				(List<Object>) obj[1], getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> queryIncomingCallChanelReportforms(
			String dealName, String dateStart, String dateEnd, int pageSize,
			int curPage) {
		String sql = returnIncomingCallChanelReportformsSql(dealName,
				dateStart, dateEnd);

		return (PageResult<Map<String, Object>>) this.pageQuery(sql, null,
				this.getFunName(), pageSize, curPage);
	}

	private String returnIncomingCallChanelReportformsSql(String dealName,
			String dateStart, String dateEnd) {
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		// SimpleDateFormat sdf = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(dt);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select CASE cl.CP_CHANEL_TYPE WHEN 90001002 THEN '网络' WHEN 90001003 THEN '数字营销转' ELSE '来电' END as incomingType,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031111, cl.num, '0')) as chanel0,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031001, cl.num, '0')) as chanel1,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031002, cl.num, '0')) as chanel2,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031003, cl.num, '0')) as chanel3,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031004, cl.num, '0')) as chanel4,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031005, cl.num, '0')) as chanel5,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031006, cl.num, '0')) as chanel6,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031007, cl.num, '0')) as chanel7,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031008, cl.num, '0')) as chanel8,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031009, cl.num, '0')) as chanel9,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031010, cl.num, '0')) as chanel10,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031011, cl.num, '0')) as chanel11,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031012, cl.num, '0')) as chanel12,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031013, cl.num, '0')) as chanel13,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031014, cl.num, '0')) as chanel14,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031015, cl.num, '0')) as chanel15,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031016, cl.num, '0')) as chanel16,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031017, cl.num, '0')) as chanel17,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031018, cl.num, '0')) as chanel18,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031019, cl.num, '0')) as chanel19,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031020, cl.num, '0')) as chanel20,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031021, cl.num, '0')) as chanel21,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031022, cl.num, '0')) as chanel22,");
		sbSql.append("  sum(decode(cl.CODE_ID, 70031023, cl.num, '0')) as chanel23");
		sbSql.append("  from (select rownum,t.*,nvl(c.code_desc, '未知渠道') as code_desc,c.code_id ");
		sbSql.append("  from (select nvl(tt.CP_CUSTOMER_KNOWN_CHANEL,'70031111') as CP_CUSTOMER_KNOWN_CHANEL, CP_CHANEL_TYPE,count(*) as num ");
		sbSql.append("  from tt_crm_incoming_call tt where 1=1 ");
		/*
		 * if(StringUtil.notNull(dealName)){
		 * sbSql.append(" and tu.NAME like '%"+dealName+"%' \r\n"); }
		 */
		if (StringUtil.notNull(dateStart)) {
			sbSql.append(" and tt.cp_acc_date >= to_date('" + dateStart
					+ "','yyyy-MM-dd')\r\n");
		} else
			sbSql.append(" and tt.cp_acc_date >= to_date('" + strDate
					+ "','yyyy-MM-dd')\r\n");
		if (StringUtil.notNull(dateEnd)) {
			sbSql.append(" and tt.cp_acc_date <= to_date('" + dateEnd
					+ " 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		} else
			sbSql.append(" and tt.cp_acc_date <= to_date('" + strDate
					+ " 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		sbSql.append("  group by tt.CP_CHANEL_TYPE,tt.CP_CUSTOMER_KNOWN_CHANEL) t ");
		sbSql.append("   right join tc_code c on t.CP_CUSTOMER_KNOWN_CHANEL = c.code_id ");
		sbSql.append("  where c.type = 7003) cl where cl.CP_CHANEL_TYPE is not null group by cl.CP_CHANEL_TYPE \n");
		/*
		 * if(StringUtil.notNull(dealName)){
		 * sbSql.append(" and tu.NAME like '%"+dealName+"%' \r\n"); }
		 * 
		 * if(StringUtil.notNull(dateStart)){
		 * sbSql.append(" and TI.cp_acc_date >= to_date('"
		 * +dateStart+"','yyyy-MM-dd')\r\n"); } if(StringUtil.notNull(dateEnd)){
		 * sbSql.append(" and TI.cp_acc_date <= to_date('"+dateEnd+
		 * " 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n"); }
		 */
		return sbSql.toString();
	}

	/**
	 * 了解渠道统计
	 * 
	 * @param dealName
	 * @param dateStart
	 * @param dateEnd
	 * @param tatal
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> incomingCallChanelQueryExport(
			String dealName, String dateStart, String dateEnd) throws Exception {
		Object[] obj = new Object[2];
		obj[0] = returnIncomingCallChanelReportformsSql(dealName, dateStart,
				dateEnd);
		obj[1] = null;
		List<Map<String, Object>> list = dao.pageQuery(String.valueOf(obj[0]),
				(List<Object>) obj[1], getFunName());
		return list;
	}

	public String returnIncomingCallDayReportSql(String dealName,
			String dateStart, String dateEnd) {
		StringBuffer sb = new StringBuffer();
		sb.append("select mm.*, nn.MODEL0, nn.MODEL1, nn.MODEL2, oo.num \n");
		sb.append("from (select a.CREATE_DATE, \n");
		sb.append(" CASE a.CP_CHANEL_TYPE \n");
		sb.append(" WHEN 90001002 THEN \n");
		sb.append(" '在线咨询' WHEN 90001003 THEN '数字营销转' ELSE '来电咨询' \n");
		sb.append(" END as CP_CHANEL_TYPE_CHN,\n");
		sb.append(" a.CP_CHANEL_TYPE, \n");
		sb.append(" sum(decode(a.CP_CUSTOMER_TYPE, 70021001, a.num, '0')) as chanel0, \n"); // --已经购车
		sb.append(" sum(decode(a.CP_CUSTOMER_TYPE, 70021002, a.num, '0')) as chanel1,\n"); // --潜在客户
		sb.append(" sum(decode(a.CP_CUSTOMER_TYPE, 70021003, a.num, '0')) as chanel2, \n"); // --潜在商家
		sb.append(" sum(decode(a.CP_CUSTOMER_TYPE, 70021004, a.num, '0')) as chanel3 \n"); // --其它咨询
		sb.append(" from (select to_char(CREATE_DATE, 'yyyy-MM-DD') as CREATE_DATE, \n");
		sb.append(" CP_CHANEL_TYPE, CP_CUSTOMER_TYPE,count(1) as num from tt_crm_incoming_call where CP_BIZ_TYPE = 9506 \n");
		sb.append(" group by to_char(CREATE_DATE, 'yyyy-MM-DD'),CP_CHANEL_TYPE,CP_CUSTOMER_TYPE) a \n");
		sb.append("  group by a.CREATE_DATE, a.CP_CHANEL_TYPE) mm \n");
		sb.append(" join ( \n");

		sb.append("select a.CREATE_DATE,\n");
		sb.append("        CASE a.CP_CHANEL_TYPE \n");
		sb.append("           WHEN 90001002 THEN\n");
		sb.append("             '在线咨询'\n");
		sb.append("          WHEN 90001003 THEN \n");
		sb.append("            '数字营销转'\n");
		sb.append("           ELSE '来电咨询' END as CP_CHANEL_TYPE_CHN,\n");
		sb.append("         a.CP_CHANEL_TYPE,\n");
		sb.append("        sum(decode(b.GROUP_NAME, 'S2', a.num, '0')) as model0, \n");// --S2
		sb.append("         sum(decode(b.GROUP_NAME, 'S3', a.num, '0')) as model1,\n");// --S3
		sb.append("        sum(decode(b.GROUP_NAME, NULL, a.num, '0')) as model2 \n"); // --S2+S3
		sb.append("   from (select to_char(CREATE_DATE, 'yyyy-MM-DD') as CREATE_DATE,\n");
		sb.append("                CP_CHANEL_TYPE,\n");
		sb.append("                CP_MODEL_ID,\n");
		sb.append("                count(1) as num\n");
		sb.append("           from tt_crm_incoming_call\n");
		sb.append("           where CP_BIZ_TYPE = 9506\n");
		sb.append("           group by to_char(CREATE_DATE, 'yyyy-MM-DD'),\n");
		sb.append("                    CP_CHANEL_TYPE,\n");
		sb.append("                    CP_MODEL_ID) a\n");
		sb.append("  join tm_vhcl_material_group b on a.CP_MODEL_ID = b.Group_Id\n");
		sb.append(" where b.Group_Level = 3\n");
		sb.append(" group by a.CREATE_DATE, a.CP_CHANEL_TYPE) nn on (mm.CP_CHANEL_TYPE =\n");
		sb.append(" nn.CP_CHANEL_TYPE AND\n");
		sb.append(" mm.CREATE_DATE =\n");
		sb.append(" nn.CREATE_DATE)\n");

		sb.append(" LEFT JOIN (select to_char(a.CREATE_DATE, 'yyyy-MM-DD') as CREATE_DATE,\n");
		sb.append(" CASE a.CP_CHANEL_TYPE\n");
		sb.append(" WHEN 90001002 THEN\n");
		sb.append(" '在线咨询'\n");
		sb.append(" WHEN 90001003 THEN\n");
		sb.append(" '数字营销转'\n");
		sb.append(" ELSE\n");
		sb.append("'来电咨询'\n");
		sb.append("END as CP_CHANEL_TYPE_CHN,\n");
		sb.append(" a.CP_CHANEL_TYPE,\n");
		sb.append(" count(1) as num\n");
		sb.append(" from tt_crm_incoming_call a\n");
		sb.append(" where a.CP_BIZ_TYPE = 9506\n");
		sb.append(" group by to_char(a.CREATE_DATE, 'yyyy-MM-DD'),\n");
		sb.append(" a.CP_CHANEL_TYPE) oo on (mm.CP_CHANEL_TYPE =\n");
		sb.append(" oo.CP_CHANEL_TYPE AND\n");
		sb.append(" mm.CREATE_DATE =\n");
		sb.append(" oo.CREATE_DATE)\n");
		return sb.toString();
	}

	public String returnIncomingDayRptSql(String dateStart, String dateEnd) {
		StringBuffer sb = new StringBuffer();
		sb.append("select vcg.*,");
		sb.append("(vcg.chanel0 + vcg.chanel1 + vcg.chanel2 + vcg.chanel3 + vcg.num1) as sum1,");
		sb.append("(vcg.chanela + vcg.chanelb + vcg.chanelc + vcg.chaneld + vcg.numa) as suma,");
		sb.append("(vcg.chanelw + vcg.chanelx + vcg.chanely + vcg.chanelz + vcg.numw) as sumw,");
		sb.append("(vcg.chanel0 + vcg.chanel1 + vcg.chanel2 + vcg.chanel3 + vcg.num1 +");
		sb.append("vcg.chanela + vcg.chanelb + vcg.chanelc + vcg.chaneld + vcg.numa +");
		sb.append("vcg.chanelw + vcg.chanelx + vcg.chanely + vcg.chanelz + vcg.numw) as sumDay,");
		sb.append("(vcg.chanel0 + vcg.chanela + vcg.chanelw) as sumQK,"); // --潜在客户
		sb.append("(vcg.chanel1 + vcg.chanelb + vcg.chanelx) as sumQS,"); // --潜在商家
		sb.append("(vcg.chanel2 + vcg.chanelc + vcg.chanely) as sumCUS,"); // --已购车咨询
		sb.append("(vcg.chanel3 + vcg.chaneld + vcg.chanelz) as sumINNER,"); // --内部购车咨询
		sb.append("(vcg.num1 + vcg.numa + vcg.numw) as sumCP,"); // --投诉
		sb.append("(vcg.model0 + vcg.modela + vcg.modelw) as sumS2,"); // --S2
		sb.append("(vcg.model1 + vcg.modelb + vcg.modelx) as sumS3,"); // --S3
		sb.append("(vcg.model2 + vcg.modelc + vcg.modely) as sumS23"); // --S2+S3
		sb.append(" from VW_CRM_DAYREPORT_GROUPBY vcg");
		sb.append(" where vcg.CREATE_DATE>='" + dateStart
				+ "' and vcg.CREATE_DATE<='" + dateEnd + "'");

		// sb.append(" where vcg.CREATE_DATE>=to_date('"+dateStart+"', 'yyyy-MM-dd HH24:mi:ss') and vcg.CREATE_DATE<=to_date('"+dateEnd+"', 'yyyy-MM-dd HH24:mi:ss')");
		return sb.toString();
	}

	public String returnVehicleInfoSql(String startDate, String endDate,
			String dealerCode, AclUserBean logonUser) {

		StringBuffer sb = new StringBuffer()
				.append("with xk_tab as "
						+ "(select t.dealer_id, "
						+ " t.yieldly, "
						+ "    nvl(t.amount, 0) amount,"
						+ "     nvl(t.freeze_amount, 0) freeze_amount "
						+ "    from TT_SALES_FIN_ACC t "
						+ "	   where t.fin_type = 10251001), "
						+ "cdhp_tab as "
						+ " (select t.dealer_id, "
						+ " t.yieldly, "
						+ " nvl(t.amount, 0) amount, "
						+ "  nvl(t.freeze_amount, 0) freeze_amount "
						+ "  from TT_SALES_FIN_ACC t "
						+ " where t.fin_type = 10251002), "
						+ "	ljfc_tab as "
						+ " (SELECT A.DEALER_ID, count(1) as sum_ "
						+ "  FROM VW_ORG_DEALER A, TM_VEHICLE B, VW_MATERIAL_GROUP_MAT C "
						+ "WHERE A.DEALER_ID = B.DEALER_ID "
						+ " AND B.MATERIAL_ID = C.MATERIAL_ID "
						+ "AND B.LOCK_STATUS = 10241001 "
						+ "AND B.Life_Cycle in (10321007, 10321005, 10321004, 10321003) "
						+ "GROUP BY A.ROOT_ORG_NAME,"
						+ "       A.DEALER_ID,"
						+ "     A.ORG_NAME,"
						+ "   A.DEALER_CODE,"
						+ " A.DEALER_NAME,"
						+ "A.REGION_NAME),"
						+ "ljzt_tab as "
						+ "(SELECT A.DEALER_ID, count(1) as sum_ "
						+ "  FROM VW_ORG_DEALER A, TM_VEHICLE B, VW_MATERIAL_GROUP_MAT C "
						+ "WHERE A.DEALER_ID = B.DEALER_ID"
						+ " AND B.MATERIAL_ID = C.MATERIAL_ID "
						+ "AND B.LOCK_STATUS = 10241001"
						+ "AND B.Life_Cycle = 10321005"
						+ "GROUP BY A.ROOT_ORG_NAME,"
						+ "       A.DEALER_ID,"
						+ "     A.ORG_NAME,"
						+ "   A.DEALER_CODE,"
						+ " A.DEALER_NAME,"
						+ "A.REGION_NAME),"
						+ "ljkc_tab as"
						+ "(SELECT A.DEALER_ID, count(1) as sum_"
						+ "  FROM VW_ORG_DEALER A, TM_VEHICLE B, VW_MATERIAL_GROUP_MAT C "
						+ "WHERE A.DEALER_ID = B.DEALER_ID"
						+ " AND B.MATERIAL_ID = C.MATERIAL_ID "
						+ "AND B.LOCK_STATUS = 10241001"
						+ "AND B.Life_Cycle = 10321003"
						+ " GROUP BY A.ROOT_ORG_NAME,"
						+ "        A.DEALER_ID,"
						+ "      A.ORG_NAME,"
						+ "    A.DEALER_CODE,"
						+ "   A.DEALER_NAME,"
						+ "  A.REGION_NAME),"
						+ "ljzd_tab as"
						+ " (SELECT A.DEALER_ID, count(1) as sum_"
						+ "    FROM VW_ORG_DEALER A, TM_VEHICLE B, VW_MATERIAL_GROUP_MAT C "
						+ "   WHERE A.DEALER_ID = B.DEALER_ID"
						+ "     AND B.MATERIAL_ID = C.MATERIAL_ID "
						+ "     AND B.LOCK_STATUS = 10241001"
						+ "    AND B.Life_Cycle = 10321004"
						+ "  GROUP BY A.ROOT_ORG_NAME,"
						+ "           A.DEALER_ID,"
						+ "          A.ORG_NAME,"
						+ "         A.DEALER_CODE,"
						+ "       A.DEALER_NAME,"
						+ "       A.REGION_NAME),"
						+ "zjze_tab as"
						+ " (SELECT b.DEALER_ID, SUM(a.AMOUNT) AS SUM_"
						+ "  FROM TT_SALES_OTHER_ACCEPT a, VW_ORG_DEALER b, TT_SALES_OTHER_CREDIT c "
						+ " where a.CRE_ID = c.CRE_ID"
						+ "    and b.DEALER_ID = c.DEAER_ID"
						+ "  GROUP BY b.DEALER_ID)," + "zjze2_tab as"
						+ "(SELECT b.DEALER_ID, SUM(a.AMOUNT) AS SUM_"
						+ "   from TT_SALES_FIN_IN_DET a, TM_DEALER b"
						+ "  where a.DEALER_ID = b.DEALER_ID"
						+ "  GROUP BY b.DEALER_ID),");
		if (CommonUtils.isEmpty(startDate) && CommonUtils.isEmpty(endDate)) {

			sb.append("ltyf_tab as"
					+ " (select t.dealer_id, SUM(t.send_num) AS SUM_ "
					+ "    from TT_SALES_ASSIGN t"
					+ "   where t.create_date > trunc(sysdate - 6, 'dd')"
					+ "     and t.create_date <= trunc(sysdate, 'dd')"
					+ "   GROUP BY t.DEALER_ID),");
		} else {
			sb.append("ltyf_tab as"
					+ " (select t.dealer_id, SUM(t.send_num) AS SUM_ "
					+ "    from TT_SALES_ASSIGN t" + "    where 1=1 ");
			if (startDate != null && !"".equals(startDate)) {
				sb.append(" AND t.create_date>=to_date(")
						.append("'" + startDate + "00:00:00'")
						.append(",'yyyy-mm-dd hh24:mi:ss')");
			}

			if (endDate != null && !"".equals(endDate)) {
				sb.append(" AND t.create_date<=to_date(")
						.append("'" + endDate + "00:00:00'")
						.append(",'yyyy-mm-dd hh24:mi:ss')");
			}
			sb.append(" GROUP BY t.DEALER_ID ),");
		}

		sb.append(
				""
						+ "dtzd_tab as"
						+ " (select T.DEALER_ID, count(1) AS SUM_"
						+ "    from TT_DEALER_ACTUAL_SALES t"
						+ " WHERE to_char(t.SALES_DATE, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') "
						+ " group by T.DEALER_ID),"
						+ "chcek_tab as(select T.DEALER_ID, SUM(chk_num) AS SUM_ from tt_vs_order t group by T.DEALER_ID) "
						+ "select t1.root_org_id,"
						+ " t1.root_org_name,"
						+ " (select t.DEALER_SHORTNAME"
						+ "   from tm_dealer t"
						+ "   where t.dealer_id = t1.dealer_id) DEALER_SHORTNAME, "
						+ "  t1.dealer_code, "
						+ "  t1.dealer_name, "
						+ "  t1.org_name, "
						+ " t1.region_name, "
						+ " nvl(t5.sum_, 0) leijifache, "
						+ " nvl(t6.sum_, 0) leijizaitu, "
						+ " nvl(t7.sum_, 0) leijikucun, "
						+ " nvl(t8.sum_, 0) leijizhongduan, "
						+ " nvl(t4.sum_, 0) chk_num, "
						+ " nvl(t11.sum_, 0) send_num, "
						+ " nvl(t12.SUM_, 0) dangtianshixiao, "
						+ " nvl(t9.sum_, 0) + nvl(t10.sum_, 0) amount, "
						+ " nvl(t2.amount, 0) + nvl(t2.freeze_amount, 0) + nvl(t3.amount, 0) +"
						+ " nvl(t3.freeze_amount, 0) yue_amount "

						+ " from VW_ORG_DEALER t1," + " xk_tab        t2,"
						+ "  cdhp_tab      t3," + "  chcek_tab   t4,"
						+ "  ljfc_tab      t5," + "ljzt_tab      t6,"
						+ "ljkc_tab      t7," + "ljzd_tab      t8,"
						+ "zjze_tab      t9," + " zjze2_tab     t10,"
						+ "ltyf_tab      t11," + "dtzd_tab      t12 "
						+ "where t1.dealer_id = t2.dealer_id(+)"
						+ " and t1.dealer_id = t3.dealer_id(+)"
						+ "and t1.dealer_id = t4.dealer_id(+)"
						+ "and t1.dealer_id = t5.dealer_id(+)"
						+ "and t1.dealer_id = t6.dealer_id(+)"
						+ "   and t1.dealer_id = t7.dealer_id(+)"
						+ "   and t1.dealer_id = t8.dealer_id(+)"
						+ "   and t1.dealer_id = t9.dealer_id(+)"
						+ "   and t1.dealer_id = t10.dealer_id(+)"
						+ "   and t1.dealer_id = t11.dealer_id(+)"
						+ "   and t1.dealer_id = t12.dealer_id(+)"
						+ "   and t1.dealer_type = 10771001"
						+ "   and t1.dealer_level = 10851001"
						+ "   and t2.yieldly(+) = ").append(Constant.areaIdJZD);
		sb.append("   and t3.yieldly(+) = ").append(Constant.areaIdJZD);
		if (!com.infoservice.infox.util.StringUtil.isEmpty(dealerCode)) {
			sb.append(" and t1.dealer_code like '%").append(dealerCode)
					.append("%'");
		}

		sb.append(
				"" + "   AND EXISTS (SELECT 1"
						+ "       FROM TR_POSE_REGION_DEALER TRD"
						+ "       WHERE TRD.DEALER_ID = t1.dealer_id"
						+ "       AND TRD.POSE_ID = ")
				.append(logonUser.getPoseId()).append(")");
		sb.append("   order by t1.root_org_id,root_org_name,org_name,region_name");
		return sb.toString();
	}

	/**
	 * 
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> incomingCallDayQueryExport(
			String dateStart, String dateEnd) throws Exception {
		Object[] obj = new Object[2];
		obj[0] = returnIncomingDayRptSql(dateStart, dateEnd);
		obj[1] = null;
		List<Map<String, Object>> list = dao.pageQuery(String.valueOf(obj[0]),
				(List<Object>) obj[1], getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> queryIncomingCallDayReportforms(
			String dealName, String dateStart, String dateEnd, int pageSize,
			int curPage) {
		String sql = returnIncomingDayRptSql(dateStart, dateEnd);
		return (PageResult<Map<String, Object>>) this.pageQuery(sql, null,
				this.getFunName(), pageSize, curPage);

	}

	public PageResult<Map<String, Object>> queryVehicleInfoReport(
			String dateStart, String dateEnd, String dealerCode, int pageSize,
			int curPage, AclUserBean logonUser) {
		String sql = returnVehicleInfoSql(dateStart, dateEnd, dealerCode,
				logonUser);
		return (PageResult<Map<String, Object>>) this.pageQuery(sql, null,
				this.getFunName(), pageSize, curPage);

	}

	public PageResult<Map<String, Object>> queryDitchReport(int pageSize,
			int curPage, AclUserBean logonUser, String PROVINCE_ID,
			String CITY_ID, String COUNTIES, String DLR_SERVICE_STATUS) {
		StringBuffer sql = returnDitchSql(PROVINCE_ID, CITY_ID, COUNTIES,
				DLR_SERVICE_STATUS);

		sql.append(" ORDER BY ROOT_ORG_ID," + "   ORG_NAME," + "    CITY_NAME,"
				+ "    region_name," + "    Service_Status");
		return this.pageQuery(sql.toString(), null, this.getFunName(),
				pageSize, curPage);
	}

	public List<Map<String, Object>> queryVehicleInfoReport(String dateStart,
			String dateEnd, String dealerCode, AclUserBean logonUser) {
		String sql = returnVehicleInfoSql(dateStart, dateEnd, dealerCode,
				logonUser);
		return this.pageQuery(sql, null, this.getFunName());
	}

	public List<Map<String, Object>> queryDitchReport(String PROVINCE_ID,
			String CITY_ID, String COUNTIES, String DLR_SERVICE_STATUS) {
		StringBuffer sql = returnDitchSql(PROVINCE_ID, CITY_ID, COUNTIES,
				DLR_SERVICE_STATUS);
		sql.append("ORDER BY T12.ROOT_ORG_ID," + "    T12.ORG_NAME,"
				+ "    T12.CITY_NAME," + "    t1.region_name,"
				+ "   t2.Service_Status");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}

	public List<Map<String, Object>> vehicleQueryReport(RequestWrapper request) {
		StringBuffer sql = returnvehicleQuerySql(request);
		String materialCode = CommonUtils.checkNull(request
				.getParamValue("materialCode"));// 物料
		String groupCode = CommonUtils.checkNull(request
				.getParamValue("groupCode"));// 物料组
		List<Object> params = new LinkedList<Object>();
		if (!"".equals(groupCode)) {
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar(
					"t.PACKAGE_ID", groupCode, params));
		}
		if (!"".equals(materialCode)) {
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,
					"tmvm", "MATERIAL_CODE"));
		}
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"t1", "DEALER_CODE"));
		}

		sql.append(" order by t2.root_org_id,t2.org_name,t.vin");
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}

	public PageResult<Map<String, Object>> vehicleQueryReport(int pageSize,
			int curPage, AclUserBean logonUser, RequestWrapper request) {
		StringBuffer sql = returnvehicleQuerySql(request);
		String materialCode = CommonUtils.checkNull(request
				.getParamValue("materialCode"));// 物料
		String groupCode = CommonUtils.checkNull(request
				.getParamValue("groupCode"));// 物料组
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		String state = CommonUtils.checkNull(request
				.getParamValue("state"));
		List<Object> params = new LinkedList<Object>();
		if (!"".equals(groupCode)) {
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar(
					"t.PACKAGE_ID", groupCode, params));
		}
		if (!"".equals(materialCode)) {
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,
					"TMVM", "MATERIAL_CODE"));
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"t1", "DEALER_CODE"));
		}
		if (!"".equals(state)) {
			switch (Integer.valueOf(state)) {
			case 1:
				//在库
				sql.append(" and t.life_cycle = 10321002 and t.lock_status != 10241008  ");
				break;
			case 2:
				//已配车
				sql.append(" and t.life_cycle = 10321002 and t.lock_status = 10241008");
				break;
				//已出库
			case 3:
				sql.append(" and t.life_cycle = 10321008");
				break;
			case 4:
				//经销商在途
				sql.append(" and t.life_cycle = 10321005");
				break;
			case 5:
				//经销商库存
				sql.append(" and t.life_cycle = 10321003");
				break;
			case 6:
				//已实销
				sql.append(" and t.life_cycle = 10321004");
				break;
			}
		}
		sql.append(" order by t2.root_org_id,t2.org_name,t.vin");
		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				params, this.getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String, Object>> outWarehouseReport(int pageSize,
			int curPage, AclUserBean logonUser, RequestWrapper request) {
		StringBuffer sql = outWarehouseReportSql(request);
		String materialCode = CommonUtils.checkNull(request
				.getParamValue("materialCode"));// 物料
		String groupCode = CommonUtils.checkNull(request
				.getParamValue("groupCode"));// 物料组
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		List<Object> params = new LinkedList<Object>();
		if (!"".equals(groupCode)) {
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar(
					"t4.PACKAGE_ID", groupCode, params));
		}
		if (!"".equals(materialCode)) {
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,
					"TMVM", "MATERIAL_CODE"));
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"t1", "DEALER_CODE"));
		}
		sql.append(" order by t1.root_org_id,t1.org_name,t4.vin");
		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				params, this.getFunName(), pageSize, curPage);
	}

	public List<Map<String, Object>> outWarehouseReport(RequestWrapper request) {
		StringBuffer sql = outWarehouseReportSql(request);
		String materialCode = CommonUtils.checkNull(request
				.getParamValue("materialCode"));// 物料
		String groupCode = CommonUtils.checkNull(request
				.getParamValue("groupCode"));// 物料组
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		List<Object> params = new LinkedList<Object>();
		if (!"".equals(groupCode)) {
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar(
					"t4.PACKAGE_ID", groupCode, params));
		}
		if (!"".equals(materialCode)) {
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,
					"TMVM", "MATERIAL_CODE"));
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"t1", "DEALER_CODE"));
		}
		sql.append(" order by t1.root_org_id,t1.org_name");
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}

	public StringBuffer returnDitchSql(String PROVINCE_ID, String CITY_ID,
			String COUNTIES, String DLR_SERVICE_STATUS) {

		StringBuffer sb = new StringBuffer();
		sb.append("with yjjxs_tab as"
				+ "(select t1.dealer_level, t1.counties, t1.Service_Status, count(1) as sum_ "
				+ "from tm_dealer t1"
				+ "  where t1.dealer_type = 10771001"
				+ "   and t1.dealer_level = 10851001 "
				+ " group by t1.dealer_level, t1.counties, t1.Service_Status),"
				+ "ejjxs_tab as"
				+ "(select t1.dealer_level, t1.counties, t1.Service_Status, count(1) as sum_ "
				+ "   from tm_dealer t1 "
				+ "where t1.dealer_type = 10771001"
				+ "and t1.dealer_level = 10851002"
				+ "group by t1.dealer_level, t1.counties, t1.Service_Status),"
				+ "A_tab as"
				+ "(select t1.image_comfirm_level,"
				+ "      t3.counties,"
				+ "    t3.Service_Status,"
				+ "  count(1) as sum_"
				+ "  from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ "  where t3.dealer_id = t1.fk_dealer_id"
				+ "   and t3.dealer_type = 10771001"
				+ "   and t1.image_comfirm_level = 80111001"
				+ " group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "B_tab as"
				+ " (select t1.image_comfirm_level,"
				+ "       t3.counties,"
				+ "     t3.Service_Status,"
				+ "     count(1) as sum_"
				+ "  from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ "   where t3.dealer_id = t1.fk_dealer_id"
				+ "  and t3.dealer_type = 10771001"
				+ "   and t1.image_comfirm_level = 80111002"
				+ "   and t3.Service_Status = 13691002"
				+ " group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "C_tab as"
				+ " (select t1.image_comfirm_level,"
				+ "     t3.counties,"
				+ "      t3.Service_Status,"
				+ "     count(1) as sum_"
				+ "  from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ "   where t3.dealer_id = t1.fk_dealer_id"
				+ "    and t3.dealer_type = 10771001"
				+ "  and t1.image_comfirm_level = 80111003"
				+ "  and t3.Service_Status = 13691002"
				+ "  group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "D_tab as"
				+ " (select t1.image_comfirm_level,"
				+ "     t3.counties,"
				+ "       t3.Service_Status,"
				+ "       count(1) as sum_"
				+ "  from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ " where t3.dealer_id = t1.fk_dealer_id"
				+ "   and t3.dealer_type = 10771001"
				+ "   and t1.image_comfirm_level = 80111004"
				+ " group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "E_tab as"
				+ " (select t1.image_comfirm_level,"
				+ "        t3.counties,"
				+ "        t3.Service_Status,"
				+ "       count(1) as sum_"
				+ " from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ " where t3.dealer_id = t1.fk_dealer_id"
				+ "  and t3.dealer_type = 10771001"
				+ "  and t1.image_comfirm_level = 80111005"
				+ " group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "F_tab as"
				+ "(select t1.image_comfirm_level,"
				+ "       t3.counties,"
				+ "       t3.Service_Status,"
				+ "        count(1) as sum_"
				+ "  from TM_DEALER_DETAIL t1, tm_dealer t3"
				+ " where t3.dealer_id = t1.fk_dealer_id"
				+ "   and t3.dealer_type = 10771001"
				+ "  and t1.image_comfirm_level = 80111006"
				+ " group by t1.image_comfirm_level, t3.counties, t3.Service_Status),"
				+ "KBDJS_tab as"
				+ "(select T3.City_Id, t3.counties, t3.Service_Status, count(1) as sum_"
				+ "  from tm_dealer t3" + " where t3.City_Id IS NULL"
				+ "   and t3.dealer_type = 10771001"
				+ " group by T3.City_Id, t3.counties, t3.Service_Status),"
				+ "KBQX_tab as"
				+ " (select t3.counties, t3.Service_Status, count(1) as sum_"
				+ "    from tm_dealer t3" + "   where t3.counties IS NULL"
				+ "     and t3.dealer_type = 10771001"
				+ "   group by t3.counties, t3.Service_Status),"
				+ "DAQU_tab as" + " (select T.ROOT_ORG_ID,"
				+ "       T.ROOT_ORG_NAME," + "      T.ORG_NAME,"
				+ "      T.CITY_NAME," + "      T2.REGION_NAME,"
				+ "      T2.REGION_CODE," + "      T1.DEALER_LEVEL, "
				+ "      T1.PROVINCE_ID, " + "      T1.CITY_ID, "
				+ "      T1.COUNTIES "
				+ " from VW_ORG_DEALER t, TM_DEALER T1, TM_REGION T2 "
				+ "WHERE T.DEALER_ID = T1.DEALER_ID"
				+ "  AND T1.COUNTIES = T2.REGION_CODE ");

		sb.append("GROUP BY T.ROOT_ORG_NAME," + "         T.ORG_NAME,"
				+ "         T.CITY_NAME," + "         T2.REGION_NAME,"
				+ "         T2.REGION_CODE," + "         T.ROOT_ORG_ID,"
				+ "         T1.DEALER_LEVEL," + "            T1.PROVINCE_ID, "
				+ " T1.CITY_ID, " + " T1.COUNTIES )");

		sb.append("select T12.ROOT_ORG_ID,T12.ROOT_ORG_NAME,"
				+ "     T12.ORG_NAME,"
				+ "     T12.CITY_NAME,"
				+ "     t1.region_name,"
				+ "     (select t.code_desc "
				+ "        FROM TC_CODE t"
				+ "       WHERE t.code_id = t2.Service_Status) as Service_Status,"
				+ "     nvl(t2.sum_, 0) yjjxs," + "     nvl(t3.sum_, 0) ejjxs,"
				+ "     nvl(t4.sum_, 0) A_tab," + "     nvl(t5.sum_, 0) B_tab,"
				+ "nvl(t6.sum_, 0) C_tab," + "    nvl(t7.sum_, 0) D_tab,"
				+ " nvl(t8.sum_, 0) E_tab," + " nvl(t9.sum_, 0) F_tab,"
				+ "nvl(t10.sum_, 0) KBDJS_tab," + "nvl(t11.sum_, 0) KBQX_tab "
				+ "from tm_region t1," + "   yjjxs_tab t2," + "  ejjxs_tab t3,"
				+ " A_tab     t4," + "  B_tab     t5," + "  C_tab     t6,"
				+ "  D_tab     t7," + "  E_tab     t8," + "  F_tab     t9,"
				+ "  KBDJS_tab t10," + " KBQX_tab  t11," + "  DAQU_tab  t12 "
				+ "where t1.region_code = t2.counties(+)"
				+ "and t1.region_code = t3.counties(+)"
				+ "and t1.region_code = t4.counties(+)"
				+ "and t1.region_code = t5.counties(+)"
				+ "and t1.region_code = t6.counties(+)"
				+ "and t1.region_code = t7.counties(+)"
				+ "and t1.region_code = t8.counties(+)"
				+ "and t1.region_code = t9.counties(+)"
				+ "and t1.region_code = t10.counties(+)"
				+ " and t1.region_code = t11.counties(+)"
				+ "and t1.region_code = t12.REGION_CODE(+)"
				+ " AND T12.ROOT_ORG_NAME IS NOT NULL ");
		if (!PROVINCE_ID.equals("")) {
			sb.append(" and t12.PROVINCE_ID = ").append(PROVINCE_ID);
		}
		if (!CITY_ID.equals("")) {
			sb.append(" and t12.CITY_ID = ").append(CITY_ID);
		}
		if (!COUNTIES.equals("")) {
			sb.append(" and t12.COUNTIES = ").append(COUNTIES);
		}
		if (!DLR_SERVICE_STATUS.equals("")) {
			sb.append(" and t2.SERVICE_STATUS = ").append(DLR_SERVICE_STATUS);
		}
		sb.append("union all " + " select 9999999999999999 ROOT_ORG_ID, "
				+ "    '合计' ROOT_ORG_NAME, " + "    '' ORG_NAME, "
				+ "    '' CITY_NAME, " + "     '' region_name, "
				+ "     '' Service_Status, "
				+ "     nvl(sum(t2.sum_), 0) yjjxs, "
				+ "     nvl(sum(t3.sum_), 0) ejjxs, "
				+ "     nvl(sum(t4.sum_), 0) A_tab, "
				+ "     nvl(sum(t5.sum_), 0) B_tab, "
				+ "    nvl(sum(t6.sum_), 0) C_tab, "
				+ "    nvl(sum(t7.sum_), 0) D_tab, "
				+ "    nvl(sum(t8.sum_), 0) E_tab, "
				+ "   nvl(sum(t9.sum_), 0) F_tab, "
				+ "   nvl(sum(t10.sum_), 0) KBDJS_tab, "
				+ "   nvl(sum(t11.sum_), 0) KBQX_tab "
				+ "  from tm_region t1, " + "   yjjxs_tab t2, "
				+ "   ejjxs_tab t3, " + "    A_tab     t4, "
				+ "    B_tab     t5, " + "    C_tab     t6, "
				+ "    D_tab     t7, " + "   E_tab     t8, "
				+ "   F_tab     t9, " + "   KBDJS_tab t10, "
				+ "   KBQX_tab  t11, " + "  DAQU_tab  t12 "
				+ " where t1.region_code = t2.counties(+) "
				+ "  and t1.region_code = t3.counties(+) "
				+ "   and t1.region_code = t4.counties(+) "
				+ "   and t1.region_code = t5.counties(+) "
				+ "  and t1.region_code = t6.counties(+) "
				+ " and t1.region_code = t7.counties(+) "
				+ "  and t1.region_code = t8.counties(+) "
				+ "  and t1.region_code = t9.counties(+) "
				+ " and t1.region_code = t10.counties(+) "
				+ "  and t1.region_code = t11.counties(+) "
				+ "  and t1.region_code = t12.REGION_CODE(+) "
				+ "  AND T12.ROOT_ORG_NAME IS NOT NULL");
		if (!PROVINCE_ID.equals("")) {
			sb.append(" and t12.PROVINCE_ID = ").append(PROVINCE_ID);
		}
		if (!CITY_ID.equals("")) {
			sb.append(" and t12.CITY_ID = ").append(CITY_ID);
		}
		if (!COUNTIES.equals("")) {
			sb.append(" and t12.COUNTIES = ").append(COUNTIES);
		}
		if (!DLR_SERVICE_STATUS.equals("")) {
			sb.append(" and t2.SERVICE_STATUS = ").append(DLR_SERVICE_STATUS);
		}

		return sb;
	}

	public StringBuffer returnvehicleQuerySql(RequestWrapper request) {
		String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));
		String regionId = CommonUtils.checkNull(request
				.getParamValue("__province_org"));
		String orgId = CommonUtils.checkNull(request
				.getParamValue("__large_org"));
		String SCS_DATE = CommonUtils.checkNull(request
				.getParamValue("SCS_DATE"));
		String SCE_DATE = CommonUtils.checkNull(request
				.getParamValue("SCE_DATE"));
		String RKS_DATE = CommonUtils.checkNull(request
				.getParamValue("RKS_DATE"));
		String RKE_DATE = CommonUtils.checkNull(request
				.getParamValue("RKE_DATE"));
		String SXS_DATE = CommonUtils.checkNull(request
				.getParamValue("SXS_DATE"));
		String SXE_DATE = CommonUtils.checkNull(request
				.getParamValue("SXE_DATE"));
		StringBuffer sb = new StringBuffer();
		sb.append("select t.vin, "
				+ " CASE "
				+ "   when t.life_cycle = 10321003 then "
				+ "    '经销商库存' "
				+ "   when t.life_cycle = 10321004 then "
				+ "    '已实销' "
				+ "   when t.life_cycle = 10321005 then "
				+ "    '经销商在途' "
				+ "   when t.life_cycle = 10321002 and t.lock_status != 10241008 then "
				+ "    '在库'  "
				+ "   when t.life_cycle = 10321002 and t.lock_status = 10241008 then "
				+ "    '已配车' "
				+ "    when t.life_cycle = 10321008 then "
				+ "    '已出库' "
				+ "  end life_cycle, "
				+ "  t.engine_no, "
				+ "  t2.root_org_name, "
				+ "   t2.org_name, "
				+ "   t1.dealer_code, "
				+ "   t1.dealer_shortname, "
				+ "   (select tt.GROUP_NAME "
				+ "      from TM_VHCL_MATERIAL_GROUP tt "
				+ "      where tt.group_level = 1) as pinpai, "
				+ "   G.GROUP_NAME AS chexi, "
				+ "   G1.GROUP_NAME AS chexing, "
				+ "   G2.GROUP_NAME AS peizhi, "
				+ "   t.color, "
				+ "   t.PRODUCT_DATE, "
				+ "   t.ORG_STORAGE_DATE, "
				+ "   t.hgz_no, "
				+ "   t.hgz_print_date, "
				+ "   t.SIT_CODE, "
				+ "   t.order_no, "
				+ "   b.send_date, "
				+ "   t.vhcl_price, "
				+ "   e.logi_name, "
				+ "   (select warehouse_name "
				+ "      from tm_warehouse tw "
				+ "     where tw.warehouse_id = t.warehouse_id) as warehouse_name, "
				+ "   t.STORAGE_DATE, "
				+ "   t.remark, "
				+ "   TMVM.Xp_Name, "
				+ "   sales_date "
				+ "  from TM_VEHICLE             t, "
				+ "    tm_dealer              t1, "
				+ "       vw_org_dealer          t2, "
				+ "    TM_VHCL_MATERIAL_GROUP G, "
				+ "    TM_VHCL_MATERIAL_GROUP G1, "
				+ "   TM_VHCL_MATERIAL_GROUP G2, "
				+ "    TM_VHCL_MATERIAL       TMVM, "
				+ "    tt_sales_alloca_de     b, "
				+ "    Tt_Sales_Ass_Detail    c, "
				+ "    tt_sales_assign        d, "
				+ "    tt_sales_logi          e, "
				+ "    tt_dealer_actual_sales f "
				+ " where t.dealer_id = t1.dealer_id(+)"
				+ " and t.dealer_id = t2.dealer_id(+) "
				+ " AND t.SERIES_ID = G.GROUP_ID(+) "
				+ " AND t.MODEL_ID = G1.GROUP_ID(+) "
				+ " AND t.PACKAGE_ID = G2.GROUP_ID(+) "
//				+ " AND t.MATERIAL_ID = TMVM.MATERIAL_ID "
				+ " and t.vehicle_id = b.vehicle_id(+) "
				+ " and b.ass_detail_id = c.ass_detail_id(+) "
				+ " and c.ass_id = d.ass_id(+) "
				+ " and c.logi_id = e.logi_id(+) "
				+ " and c.mat_id = TMVM.MATERIAL_ID(+) "
				+ " and t.vehicle_id = f.vehicle_id(+) ");
		if (!VIN.equals("")) {
			sb.append("and T.VIN LIKE'%").append(VIN).append("%'");
		}
		if (!"".equals(SCS_DATE)) {
			sb.append("AND T.PRODUCT_DATE>=TO_DATE('" + SCS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(SCE_DATE)) {
			sb.append("AND T.PRODUCT_DATE<=TO_DATE('" + SCE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(RKS_DATE)) {
			sb.append("AND T.STORAGE_DATE>=TO_DATE('" + RKS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(RKE_DATE)) {
			sb.append("AND T.STORAGE_DATE<=TO_DATE('" + RKE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(SXS_DATE)) {
			sb.append("AND F.sales_date>=TO_DATE('" + SXS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(SXE_DATE)) {
			sb.append("AND F.sales_date<=TO_DATE('" + SXE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!CommonUtils.isNullString(orgId)) {
			sb.append("   and t2.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionId)) {
			sb.append("   and t2.org_id in (").append(regionId).append(")\n");
		}
		return sb;
	}

	public StringBuffer outWarehouseReportSql(RequestWrapper request) {
		String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));
		String regionId = CommonUtils.checkNull(request
				.getParamValue("__province_org"));
		String orgId = CommonUtils.checkNull(request
				.getParamValue("__large_org"));

		StringBuffer sb = new StringBuffer();
		sb.append("select t1.root_org_id, "
				+ "      t1.root_org_name, "
				+ "     t1.org_name, "
				+ "     (select DEALER_SHORTNAME from tm_dealer where dealer_id = t1.dealer_id) as dealer_name, "
				+ "     t1.dealer_code, "
				+ "     t.bill_no,    "
				+ "     t2.ALLOCA_DATE, "
				+ "     t5.ASS_DATE, "
				+ "     t2.SEND_DATE, "
				+ "      t6.PLAN_CHK_DATE, "
				+ "      t6.order_no, "
				+ "      t6.RAISE_DATE, "
				+ "     (select tt.GROUP_NAME "
				+ "        from TM_VHCL_MATERIAL_GROUP tt "
				+ "       where tt.group_level = 1) as pinpai, "
				+ "     G.GROUP_NAME AS chexi, "
				+ "     G1.GROUP_NAME AS chexing, "
				+ "     G2.GROUP_NAME AS peizhi, "
				+ "     t4.color, "
				+ "     t4.vin, "
				+ "     t4.engine_no, t4.storage_date,"
				+ "     (select logi_name from tt_sales_logi where logi_id = t.logi_id) as logi_name "
				+ " from TT_SALES_WAYBILL       t, "
				+ "     vw_org_dealer          t1, "
				+ "     TT_SALES_ALLOCA_DE     t2, "
				+ "     TT_SALES_ASS_DETAIL    t3, "
				+ "    tm_vehicle             t4, "
				+ "     TT_SALES_ASSIGN        t5, "
				+ "     tt_vs_order            t6, "
				+ "     TM_VHCL_MATERIAL_GROUP G, "
				+ "     TM_VHCL_MATERIAL_GROUP G1, "
				+ "     TM_VHCL_MATERIAL_GROUP G2,TM_VHCL_MATERIAL tmvm"
				+ " where t.send_dealer_id = t1.dealer_id "
				+ "   and t.bill_id = t2.bill_id "
				+ "   and t2.ass_detail_id = t3.ass_detail_id "
				+ "  and t3.ass_id = t5.ass_id "
				+ " AND t3.STATUS = 10011001 "
				+ " AND t5.STATUS = 10011001 "
				+ " and t5.order_id = t6.order_id "
				+ " and t2.vehicle_id = t4.vehicle_id "
				+ " AND t4.SERIES_ID = G.GROUP_ID "
				+ " AND t4.MODEL_ID = G1.GROUP_ID "
				+ " AND t4.PACKAGE_ID = G2.GROUP_ID and t4.MATERIAL_ID = TMVM.MATERIAL_ID "
				+ "");

		if (!VIN.equals("")) {
			sb.append("and T4.VIN LIKE'%").append(VIN).append("%'");
		}

		String FPS_DATE = CommonUtils.checkNull(request
				.getParamValue("FPS_DATE"));
		String FPE_DATE = CommonUtils.checkNull(request
				.getParamValue("FPE_DATE"));

		String YSS_DATE = CommonUtils.checkNull(request
				.getParamValue("YSS_DATE"));
		String YSE_DATE = CommonUtils.checkNull(request
				.getParamValue("YSE_DATE"));

		String FYS_DATE = CommonUtils.checkNull(request
				.getParamValue("FYS_DATE"));
		String FYE_DATE = CommonUtils.checkNull(request
				.getParamValue("FYE_DATE"));
		String DCS_DATE = CommonUtils.checkNull(request
				.getParamValue("DCS_DATE"));
		String DCE_DATE = CommonUtils.checkNull(request
				.getParamValue("DCE_DATE"));
		String PCS_DATE = CommonUtils.checkNull(request
				.getParamValue("PCS_DATE"));
		String PCE_DATE = CommonUtils.checkNull(request
				.getParamValue("PCE_DATE"));

		String CKS_DATE = CommonUtils.checkNull(request
				.getParamValue("CKS_DATE"));
		String CKE_DATE = CommonUtils.checkNull(request
				.getParamValue("CKE_DATE"));
		if (!"".equals(FPS_DATE)) {
			sb.append("AND T5.ASS_DATE>=TO_DATE('" + FPS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(FPE_DATE)) {
			sb.append("AND T5.ASS_DATE<=TO_DATE('" + FPE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(YSS_DATE)) {
			sb.append("AND T4.STORAGE_DATE>=TO_DATE('" + YSS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(YSE_DATE)) {
			sb.append("AND T4.STORAGE_DATE<=TO_DATE('" + YSE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(FYS_DATE)) {
			sb.append("AND T2.SEND_DATE>=TO_DATE('" + FYS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(FYE_DATE)) {
			sb.append("AND T2.SEND_DATE<=TO_DATE('" + FYE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(DCS_DATE)) {
			sb.append("AND t6.RAISE_DATE>=TO_DATE('" + DCS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(DCE_DATE)) {
			sb.append("AND t6.RAISE_DATE<=TO_DATE('" + DCE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(PCS_DATE)) {
			sb.append("AND T2.ALLOCA_DATE>=TO_DATE('" + PCS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(PCE_DATE)) {
			sb.append("AND T2.ALLOCA_DATE<=TO_DATE('" + PCE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(CKS_DATE)) {
			sb.append("AND T2.OUT_DATE>=TO_DATE('" + CKS_DATE
					+ " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(CKE_DATE)) {
			sb.append("AND T2.OUT_DATE<=TO_DATE('" + CKE_DATE
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!CommonUtils.isNullString(orgId)) {
			sb.append("   and t1.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionId)) {
			sb.append("   and t1.org_id in (").append(regionId).append(")\n");
		}
		return sb;
	}

	public PageResult<Map<String, Object>> largeAreaReport(int pageSize,
			int curPage, AclUserBean logonUser, RequestWrapper request) {
		StringBuffer sql = largeAreaReportSql(request);

		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				null, this.getFunName(), pageSize, curPage);
	}

	public List<Map<String, Object>> largeAreaReport(RequestWrapper request) {
		StringBuffer sql = largeAreaReportSql(request);

		return (List<Map<String, Object>>) this.pageQuery(sql.toString(), null,
				this.getFunName());
	}

	public PageResult<Map<String, Object>> vehicleTypeReport(int pageSize,
			int curPage, AclUserBean logonUser, RequestWrapper request) {
		StringBuffer sql = vehicleTypeReportSql(request);

		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				null, this.getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String, Object>> dealerReport(int pageSize,
			int curPage, AclUserBean logonUser, RequestWrapper request) {
		StringBuffer sql = dealerReportSql(request);
		
		String regionId = CommonUtils.checkNull(request
				.getParamValue("__province_org"));
		String orgId = CommonUtils.checkNull(request
				.getParamValue("__large_org"));
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		
		if (!CommonUtils.isNullString(orgId)) {
			sql.append("   and a.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("   and a.org_id in (").append(regionId).append(")\n");
		}
		List<Object> params = new LinkedList<Object>();
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"a", "DEALER_CODE"));
		}
		sql.append( "order by a.root_org_id,a.root_org_name,a.dealer_id");

		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				params, this.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> dealerReport(RequestWrapper request) {
		StringBuffer sql = dealerReportSql(request);
		
		String regionId = CommonUtils.checkNull(request
				.getParamValue("__province_org"));
		String orgId = CommonUtils.checkNull(request
				.getParamValue("__large_org"));
		String dealerCode = CommonUtils.checkNull(request
				.getParamValue("dealerCode"));
		
		if (!CommonUtils.isNullString(orgId)) {
			sql.append("   and a.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("   and a.org_id in (").append(regionId).append(")\n");
		}
		List<Object> params = new LinkedList<Object>();
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"t1", "DEALER_CODE"));
		}
		sql.append( "order by a.root_org_id,a.root_org_name,a.dealer_id");

		return (List<Map<String, Object>>) this.pageQuery(sql.toString(),
				params, this.getFunName());
	}

	public List<Map<String, Object>> vehicleTypeReport(RequestWrapper request) {
		StringBuffer sql = vehicleTypeReportSql(request);

		return (List<Map<String, Object>>) this.pageQuery(sql.toString(), null,
				this.getFunName());
	}

	public StringBuffer vehicleTypeReportSql(RequestWrapper request) {
		String MODEL_ID = CommonUtils.checkNull(request
				.getParamValue("MODEL_ID"));
		String packageIdall = CommonUtils.checkNull(request
				.getParamValue("packageIdall"));
		String packageIds3 = CommonUtils.checkNull(request
				.getParamValue("packageIds3"));
		String packageIds2 = CommonUtils.checkNull(request
				.getParamValue("packageIds2"));
		String DATE = CommonUtils.checkNull(request.getParamValue("DATE"));

		StringBuffer sb = new StringBuffer();

		sb.append("with tab_a as " + " (select t.package_id, count(1) as drrk "
				+ " 		    from TM_VEHICLE t " + " 		   where 1=1 "
				+ "AND TRUNC(t.org_storage_date) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'))"
				+ " group by t.package_id), "
				+ " tab_b as "
				+ " 		 (select t.package_id, count(1) as dyrk "
				+ " 		    from TM_VEHICLE t "
				+ " 		   where t.org_storage_date >= trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'MM') "
				+ " 		     and t.org_storage_date <= last_day(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ " 		   group by t.package_id), "
				+ " 		tab_c as "
				+ " 		 (select t.package_id, count(1) as dnrk "
				+ " 		    from TM_VEHICLE t "
				+ " 		   where t.org_storage_date >= trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'YYYY') "
				+ " 		     and t.org_storage_date <= "
				+ " 		         add_months(trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'YYYY'), 12) - 1group by "
				+ " 		   t.package_id), "
				+ " 	tab_l as "
				+ " 		 (select t2.package_id, count(1) as drfp "
				+ " 		    from tt_sales_alloca_de t1, tm_vehicle t2 "
				+ " 		   where t1.vehicle_id = t2.vehicle_id "
				+ " 		     and TRUNC(t1.ALLOCA_DATE) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ " 		   group by t2.package_id), "
				+ " 		tab_m as "
				+ " 		 (select t2.package_id, count(1) as dyfp "
				+ " 		    from tt_sales_alloca_de t1, tm_vehicle t2 "
				+ " 		   where t1.vehicle_id = t2.vehicle_id "
				+ " 		     and t1.ALLOCA_DATE >= trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'MM') "
				+ " 		     and t1.ALLOCA_DATE <= last_day(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ " 		   group by t2.package_id), "
				+ " 		tab_n as "
				+ " 		 (select t2.package_id, count(1) as dnfp "
				+ " 		    from tt_sales_alloca_de t1, tm_vehicle t2 "
				+ " 		   where t1.vehicle_id = t2.vehicle_id "
				+ " 		     and t1.ALLOCA_DATE <= add_months(trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'YYYY'), 12) - 1 "
				+ " 	   group by t2.package_id), "
				+ " 		tab_d as "
				+ " 		 (select t.package_id, count(1) as drky "
				+ " 	    from TM_VEHICLE t "
				+ " 		   where TRUNC(t.org_storage_date) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ " 		     and t.life_cycle = 10321002 "
				+ " 		     and t.lock_status = 10241001group by t.package_id), "
				+ " 	tab_e as "
				+ " 	 (select t.package_id, count(1) as drdj "
				+ "   from TM_VEHICLE t "
				+ " 	   where TRUNC(t.org_storage_date) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "   and t.life_cycle = 10321002 "
				+ "    and t.lock_status != 10241001group by t.package_id), "
				+ " 	tab_f as "
				+ " 	 (select t.package_id, count(1) as drxs "
				+ "     from TM_VEHICLE t "
				+ "    where TRUNC(t.org_storage_date) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "     and t.life_cycle = 10321004group by t.package_id), "
				+ " tab_g as "
				+ "  (select t.package_id, count(1) as dyxs "
				+ "     from TM_VEHICLE t "
				+ "    where t.org_storage_date >= trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'MM') "
				+ "      and t.org_storage_date <= last_day(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "      and t.life_cycle = 10321004group by t.package_id), "
				+ " tab_h as "
				+ "  (select t.package_id, count(1) as dnxs "
				+ "     from TM_VEHICLE t "
				+ "    where t.org_storage_date >= trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'YYYY') "
				+ "      and t.org_storage_date <= add_months(trunc(to_date('"
				+ DATE
				+ "','yyyy-mm-dd'), 'YYYY'), 12) - 1 "
				+ "      and t.life_cycle = 10321004group by t.package_id), "
				+ " tab_i as "
				+ "  (select t.package_id, count(1) AS JXSKC "
				+ "     from TM_VEHICLE t "
				+ "    where t.life_cycle in (10321003, 10321005) "
				+ "    group by t.package_id), "
				+ " tab_k as "
				+ "  (select t.package_id, t.package_name,t.model_id, t.model_name "
				+ "     from VW_MATERIAL_GROUP_MAT t where 1=1");

		if (!MODEL_ID.equals("")) {
			sb.append(" AND MODEL_ID = ").append(MODEL_ID);
		}
		if (!packageIdall.equals("")) {
			sb.append(" AND package_Id = ").append(packageIdall);
		}
		if (!packageIds2.equals("")) {
			sb.append(" AND package_Id = ").append(packageIds2);
		}
		if (!packageIds3.equals("")) {
			sb.append(" AND package_Id = ").append(packageIds3);
		}
		sb.append("    group by t.package_id, t.package_name, t.model_name,t.model_id), "
				+ " tab_o as "
				+ "  (select t2.package_id, count(1) as FPWFY "
				+ "     from tt_sales_alloca_de t1, tm_vehicle t2 "
				+ "    where t1.vehicle_id = t2.vehicle_id "
				+ "      and t1.is_send = 10041002 "
				+ "    group by t2.package_id), "
				+ " tab_j as "
				+ "  (select k.package_id,k.model_id, "
				+ "          k.model_name, "
				+ "          k.package_name, "
				+ "         a.drrk, "
				+ "         b.dyrk, "
				+ "         c.dnrk, "
				+ "         d.drky, "
				+ "       e.drdj, "
				+ "        a.drrk as drkchj, "
				+ "       f.drxs, "
				+ "        g.dyxs, "
				+ "       h.dnxs, "
				+ "       i.JXSKC, "
				+ "      l.drfp, "
				+ "      m.dyfp, "
				+ "      n.dnfp, "
				+ "      o.FPWFY "
				+ "  from tab_k k, "
				+ "      tab_a a, "
				+ "      tab_b b, "
				+ "      tab_c c, "
				+ "      tab_d d, "
				+ "      tab_e e, "
				+ "     tab_f f, "
				+ "     tab_g g, "
				+ "     tab_h h, "
				+ "     tab_i i, "
				+ "     tab_l l, "
				+ "     tab_m m, "
				+ "     tab_n n, "
				+ "       tab_o o "
				+ "   where k.package_id = a.package_id(+) "
				+ "      and k.package_id = b.package_id(+) "
				+ "     and k.package_id = c.package_id(+) "
				+ "     and k.package_id = d.package_id(+) "
				+ "     and k.package_id = e.package_id(+) "
				+ "    and k.package_id = f.package_id(+) "
				+ "    and k.package_id = g.package_id(+) "
				+ "    and k.package_id = h.package_id(+) "
				+ "     and k.package_id = i.package_id(+) "
				+ "     and k.package_id = l.package_id(+) "
				+ "    and k.package_id = m.package_id(+) "
				+ "     and k.package_id = n.package_id(+) "
				+ "   and k.package_id = o.package_id(+) "
				+ "   UNION ALL "
				+ "   SELECT 9999999999999999 as package_id,9999999999999999 as model_id, "
				+ "        '合计' as model_name, "
				+ "        '' package_name, "
				+ "        sum(a.drrk) drrk, "
				+ "        sum(b.dyrk) dyrk, "
				+ "        sum(c.dnrk) dnrk, "
				+ "      sum(d.drky) drky, "
				+ "        sum(e.drdj) drdj, "
				+ "       sum(a.drrk) as drkchj, "
				+ "       sum(f.drxs) drxs, "
				+ "       sum(g.dyxs) dyxs, "
				+ "      sum(h.dnxs) dnxs, "
				+ "       sum(i.JXSKC) JXSKC, "
				+ "       sum(l.drfp) drfp, "
				+ "       sum(m.dyfp) dyfp, "
				+ "       sum(n.dnfp) dnfp, "
				+ "        sum(o.FPWFY) FPWFY "
				+ " from tab_k k, "
				+ "     tab_a a, "
				+ "      tab_b b, "
				+ "         tab_c c, "
				+ "          tab_d d, "
				+ "      tab_e e, "
				+ "         tab_f f, "
				+ "          tab_g g, "
				+ "          tab_h h, "
				+ "       tab_i i, "
				+ "          tab_l l, "
				+ "         tab_m m, "
				+ "      tab_n n, "
				+ "       tab_o o "
				+ "   where k.package_id = a.package_id(+) "
				+ "     and k.package_id = b.package_id(+) "
				+ "    and k.package_id = c.package_id(+) "
				+ "      and k.package_id = d.package_id(+) "
				+ "    and k.package_id = e.package_id(+) "
				+ "    and k.package_id = f.package_id(+) "
				+ "   and k.package_id = g.package_id(+) "
				+ "   and k.package_id = h.package_id(+) "
				+ "  and k.package_id = i.package_id(+) "
				+ "  and k.package_id = l.package_id(+) "
				+ "  and k.package_id = m.package_id(+) "
				+ "  and k.package_id = n.package_id(+) "
				+ "  and k.package_id = o.package_id(+)) "
				+ " select j.package_id, "
				+ " j.model_id,j.model_name, "
				+ "      j.package_name, "
				+ "       nvl(j.drrk, 0) drrk, "
				+ "       nvl(j.dyrk, 0) dyrk, "
				+ "      nvl(j.dnrk, 0) dnrk, "
				+ "      nvl(j.drky, 0) drky, "
				+ "      nvl(j.drdj, 0) drdj, "
				+ "       nvl(j.drkchj, 0) drkchj, "
				+ "       nvl(j.drxs, 0) drxs, "
				+ "       nvl(j.dyxs, 0) dyxs, "
				+ "      nvl(j.dnxs, 0) dnxs, "
				+ "       nvl(j.JXSKC, 0) JXSKC, "
				+ "       nvl(j.drfp, 0) drfp, "
				+ "       nvl(j.dyfp, 0) dyfp, "
				+ "       nvl(j.dnfp, 0) dnfp, "
				+ "        nvl(j.FPWFY, 0) FPWFY "
				+ "   from tab_j j "
				+ "  order by package_id ");

		return sb;
	}

	public StringBuffer largeAreaReportSql(RequestWrapper request) {
		String orgId = CommonUtils.checkNull(request
				.getParamValue("__large_org"));
		String DATE = CommonUtils.checkNull(request.getParamValue("DATE"));

		StringBuffer sb = new StringBuffer();

		sb.append("WITH ORG_DATA AS( "
				+ "  SELECT ORG_ID, ORG_NAME, NAME_SORT " + "   FROM TM_ORG "
				+ "  WHERE ORG_TYPE = 10191001 ");
		if (!orgId.equals("")) {
			sb.append(" and org_id=").append(orgId);
		}
		sb.append("   AND STATUS = 10011001 "
				+ "  AND ORG_LEVEL = 2 "
				+ " ), "
				+ " DK_COUNT AS ( "
				+ "  -- 经销商家数合计\n "
				+ "  SELECT O.ROOT_ORG_ID, COUNT(1) DKJS "
				+ "    FROM VW_ORG_DEALER O "
				+ "   WHERE EXISTS (SELECT 1 FROM TT_SALES_FIN_IN_DET I WHERE I.DEALER_ID = O.DEALER_ID AND I.STATUS = 18801003) "
				+ "   GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " YC_COUNT AS ( "
				+ "   -- 经销商到车家数合计\n "
				+ "  SELECT O.ROOT_ORG_ID, COUNT(1) DCJS "
				+ "   FROM VW_ORG_DEALER O "
				+ "  WHERE EXISTS (SELECT 1 FROM TT_VS_INSPECTION VI WHERE VI.DEALER_ID = O.DEALER_ID) "
				+ "  GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " CLYS_MODEL_COUNT AS ( "
				+ "   -- 车辆验收按车型统计\n "
				+ "  SELECT AA.ROOT_ORG_ID, "
				+ "        SUM(AA.M2014032694231207) M2014032694231207, "
				+ "        SUM(AA.M2014032694231208) M2014032694231208 "
				+ "   FROM (SELECT O.ROOT_ORG_ID, "
				+ "                DECODE(VMT.MODEL_ID, 2014032694231207, COUNT(1), 0) M2014032694231207, "
				+ "               DECODE(VMT.MODEL_ID, 2014032694231208, COUNT(1), 0) M2014032694231208 "
				+ "         FROM VW_MATERIAL_GROUP_MAT VMT, "
				+ "               TT_VS_INSPECTION      VI, "
				+ "              TM_VEHICLE            V, "
				+ "               VW_ORG_DEALER         O "
				+ "        WHERE VI.VEHICLE_ID = V.VEHICLE_ID "
				+ "          AND VI.DEALER_ID = O.DEALER_ID "
				+ "          AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "        GROUP BY O.ROOT_ORG_ID, VMT.MODEL_ID) AA "
				+ " GROUP BY AA.ROOT_ORG_ID " + " ), "
				+ " CLYS_ALL_COUNT AS ( " + "  -- 车辆验收全部统计\n "
				+ "  SELECT O.ROOT_ORG_ID, "
				+ "         COUNT(1) AS INSPECTION_ALL "
				+ "  FROM VW_MATERIAL_GROUP_MAT VMT, "
				+ "        TT_VS_INSPECTION      VI, "
				+ "      TM_VEHICLE            V, "
				+ "      VW_ORG_DEALER         O "
				+ "   WHERE VI.VEHICLE_ID = V.VEHICLE_ID "
				+ "     AND VI.DEALER_ID = O.DEALER_ID "
				+ "     AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "  GROUP BY O.ROOT_ORG_ID " + " ), "
				+ " CLYS_TODAY_COUNT AS ( " + "  -- 车辆验收当日统计\n "
				+ "  SELECT O.ROOT_ORG_ID, "
				+ "          COUNT(1) AS INSPECTION_TODAY "
				+ "   FROM VW_MATERIAL_GROUP_MAT VMT, "
				+ "        TT_VS_INSPECTION      VI, "
				+ "       TM_VEHICLE            V, "
				+ "       VW_ORG_DEALER         O "
				+ " WHERE VI.VEHICLE_ID = V.VEHICLE_ID "
				+ "    AND VI.DEALER_ID = O.DEALER_ID "
				+ "    AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "    AND TRUNC(VI.CREATE_DATE) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "  GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " CLZT_MODEL_COUNT AS ( "
				+ " -- 在途数\n "
				+ " SELECT AA.ROOT_ORG_ID, "
				+ "        SUM(AA.M2014032694231207) M2014032694231207, "
				+ "        SUM(AA.M2014032694231208) M2014032694231208 "
				+ "   FROM (SELECT O.ROOT_ORG_ID, "
				+ "               DECODE(VMT.MODEL_ID, 2014032694231207, COUNT(1), 0) M2014032694231207, "
				+ "              DECODE(VMT.MODEL_ID, 2014032694231208, COUNT(1), 0) M2014032694231208 "
				+ "         FROM TM_VEHICLE V, VW_ORG_DEALER O, VW_MATERIAL_GROUP_MAT VMT "
				+ "        WHERE V.DEALER_ID = O.DEALER_ID "
				+ "          AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "          AND V.LIFE_CYCLE = 10321005 "
				+ "          AND V.LOCK_STATUS = 10241001 "
				+ "        GROUP BY O.ROOT_ORG_ID, VMT.MODEL_ID) AA "
				+ "   GROUP BY AA.ROOT_ORG_ID "
				+ " ), "
				+ " CLZT_ALL_COUNT AS ( "
				+ "  SELECT O.ROOT_ORG_ID, "
				+ "         COUNT(1) AS ZTZS "
				+ "    FROM TM_VEHICLE V, VW_ORG_DEALER O "
				+ "   WHERE V.DEALER_ID = O.DEALER_ID "
				+ "      AND V.LIFE_CYCLE = 10321005 "
				+ "     AND V.LOCK_STATUS = 10241001 "
				+ "   GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " FYTJ_MODEL_COUNT AS ( "
				+ " SELECT AA.ROOT_ORG_ID, "
				+ "      SUM(AA.M2014032694231207) M2014032694231207, "
				+ "      SUM(AA.M2014032694231208) M2014032694231208 "
				+ "  FROM (SELECT O.ROOT_ORG_ID, "
				+ "         DECODE(VMT.MODEL_ID, 2014032694231207, COUNT(1), 0) M2014032694231207, "
				+ "         DECODE(VMT.MODEL_ID, 2014032694231208, COUNT(1), 0) M2014032694231208 "
				+ "     FROM TT_SALES_WAYBILL      A1, "
				+ "        TT_SALES_ALLOCA_DE    A2, "
				+ "        TT_SALES_ASS_DETAIL   A3, "
				+ "         TT_SALES_ASSIGN       A4, "
				+ "         VW_ORG_DEALER         O, "
				+ "         VW_MATERIAL_GROUP_MAT VMT "
				+ "     WHERE A1.BILL_ID = A2.BILL_ID "
				+ "      AND A3.MAT_ID = VMT.MATERIAL_ID "
				+ "       AND A3.ASS_DETAIL_ID = A2.ASS_DETAIL_ID "
				+ "      AND A3.ASS_ID = A4.ASS_ID "
				+ "      AND A4.DEALER_ID = O.DEALER_ID "
				+ "      AND A1.STATUS = 10011001 "
				+ "      AND A1.SEND_STATUS <> 97111004 "
				+ "      AND A1.IS_CONFIRM = 10041001 "
				+ "     GROUP BY O.ROOT_ORG_ID, VMT.MODEL_ID) AA "
				+ " GROUP BY AA.ROOT_ORG_ID "
				+ " ), "
				+ " FYTJ_ALL_COUNT AS ( "
				+ "   SELECT O.ROOT_ORG_ID, "
				+ "      COUNT(1) AS FYS_ALL "
				+ "    FROM TT_SALES_WAYBILL    A1, "
				+ "       TT_SALES_ALLOCA_DE  A2, "
				+ "       TT_SALES_ASS_DETAIL A3, "
				+ "       TT_SALES_ASSIGN     A4, "
				+ "        VW_ORG_DEALER       O "
				+ "  WHERE A1.BILL_ID = A2.BILL_ID "
				+ "    AND A3.ASS_DETAIL_ID = A2.ASS_DETAIL_ID "
				+ "    AND A3.ASS_ID = A4.ASS_ID "
				+ "    AND A4.DEALER_ID = O.DEALER_ID "
				+ "    AND A1.STATUS = 10011001 "
				+ "    AND A1.SEND_STATUS <> 97111004 "
				+ "    AND A1.IS_CONFIRM = 10041001 "
				+ "  GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " FYTJ_TODAY_COUNT AS ( "
				+ "  SELECT O.ROOT_ORG_ID, "
				+ "          COUNT(1) AS FYS_TODAY "
				+ "     FROM TT_SALES_WAYBILL    A1, "
				+ "         TT_SALES_ALLOCA_DE  A2, "
				+ "         TT_SALES_ASS_DETAIL A3, "
				+ "         TT_SALES_ASSIGN     A4, "
				+ "         VW_ORG_DEALER       O "
				+ "   WHERE A1.BILL_ID = A2.BILL_ID "
				+ "      AND A3.ASS_DETAIL_ID = A2.ASS_DETAIL_ID "
				+ "    AND A3.ASS_ID = A4.ASS_ID "
				+ "     AND A4.DEALER_ID = O.DEALER_ID "
				+ "    AND A1.STATUS = 10011001 "
				+ "    AND A1.SEND_STATUS <> 97111004 "
				+ "    AND A1.IS_CONFIRM = 10041001 "
				+ "    AND TRUNC(A1.BILL_CRT_DATE) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "   GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " KCTJ_MODEL_COUNT AS ( "
				+ "  SELECT AA.ROOT_ORG_ID, "
				+ "        SUM(AA.M2014032694231207) M2014032694231207, "
				+ "         SUM(AA.M2014032694231208) M2014032694231208 "
				+ "   FROM (SELECT O.ROOT_ORG_ID, "
				+ "               DECODE(VMT.MODEL_ID, 2014032694231207, COUNT(1), 0) M2014032694231207, "
				+ "                DECODE(VMT.MODEL_ID, 2014032694231208, COUNT(1), 0) M2014032694231208 "
				+ "           FROM TM_VEHICLE V, VW_ORG_DEALER O, VW_MATERIAL_GROUP_MAT VMT "
				+ "          WHERE V.DEALER_ID = O.DEALER_ID "
				+ "            AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "            AND V.LIFE_CYCLE = 10321003 "
				+ "            AND V.LOCK_STATUS = 10241001 "
				+ "           GROUP BY O.ROOT_ORG_ID, VMT.MODEL_ID) AA "
				+ "   GROUP BY AA.ROOT_ORG_ID "
				+ " ), "
				+ " KCTJ_ALL_COUNT AS ( "
				+ " SELECT O.ROOT_ORG_ID, "
				+ "         COUNT(1) AS KC_ALL "
				+ "    FROM TM_VEHICLE V, VW_ORG_DEALER O "
				+ "   WHERE V.DEALER_ID = O.DEALER_ID "
				+ "     AND V.LIFE_CYCLE = 10321003 "
				+ "     AND V.LOCK_STATUS = 10241001 "
				+ "    GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " SXTJ_MODEL_COUNT AS ( "
				+ " SELECT AA.ROOT_ORG_ID, "
				+ "        SUM(AA.M2014032694231207) M2014032694231207, "
				+ "        SUM(AA.M2014032694231208) M2014032694231208 "
				+ "   FROM (SELECT O.ROOT_ORG_ID, "
				+ "               DECODE(VMT.MODEL_ID, 2014032694231207, COUNT(1), 0) M2014032694231207, "
				+ "               DECODE(VMT.MODEL_ID, 2014032694231208, COUNT(1), 0) M2014032694231208 "
				+ "         FROM TT_DEALER_ACTUAL_SALES S, "
				+ "             TM_VEHICLE             V, "
				+ "              VW_MATERIAL_GROUP_MAT  VMT, "
				+ "             VW_ORG_DEALER          O "
				+ "       WHERE S.DEALER_ID = O.DEALER_ID "
				+ "         AND S.VEHICLE_ID = V.VEHICLE_ID "
				+ "         AND V.MATERIAL_ID = VMT.MATERIAL_ID "
				+ "         AND S.IS_RETURN = 10041002 "
				+ "       GROUP BY O.ROOT_ORG_ID, VMT.MODEL_ID) AA "
				+ "  GROUP BY AA.ROOT_ORG_ID "
				+ " ), "
				+ " SXTJ_ALL_COUNT AS ( "
				+ "  SELECT O.ROOT_ORG_ID, "
				+ "        COUNT(1) AS SXSJ_ALL "
				+ "  FROM TT_DEALER_ACTUAL_SALES S, "
				+ "       TM_VEHICLE             V, "
				+ "       VW_ORG_DEALER          O "
				+ "  WHERE S.DEALER_ID = O.DEALER_ID "
				+ "    AND S.VEHICLE_ID = V.VEHICLE_ID "
				+ "    AND S.IS_RETURN = 10041002 "
				+ "   GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " SXTJ_TODAY_COUNT AS ( "
				+ "   SELECT O.ROOT_ORG_ID, "
				+ "      COUNT(1) AS SXSJ_TODAY "
				+ "  FROM TT_DEALER_ACTUAL_SALES S, "
				+ "       TM_VEHICLE             V, "
				+ "       VW_ORG_DEALER          O "
				+ "  WHERE S.DEALER_ID = O.DEALER_ID "
				+ "    AND S.VEHICLE_ID = V.VEHICLE_ID "
				+ "    AND S.IS_RETURN = 10041002 "
				+ "    AND TRUNC(S.SALES_DATE) = TRUNC(to_date('"
				+ DATE
				+ "','yyyy-mm-dd')) "
				+ "  GROUP BY O.ROOT_ORG_ID "
				+ " ), "
				+ " QUERY_LIST AS ( "
				+ "   SELECT A.ORG_NAME, "
				+ "        a.NAME_SORT, "
				+ "        B.DKJS, "
				+ "       C.DCJS, "
				+ "       -- 车辆验收统计\n "
				+ "       D1.M2014032694231207 YS_S2, "
				+ "      D1.M2014032694231208 YS_S3, "
				+ "      D2.INSPECTION_ALL    INSPECTION_ALL, "
				+ "      D3.INSPECTION_TODAY  INSPECTION_TODAY, "
				+ "     -- 车辆在途统计\n "
				+ "     E1.M2014032694231207 ZT_S2, "
				+ "     E1.M2014032694231208 ZT_S3, "
				+ "    E2.ZTZS              ZTZS, "
				+ "    -- 发运统计\n "
				+ "     F1.M2014032694231207 FY_S2, "
				+ "     F1.M2014032694231208 FY_S3, "
				+ "    F2.FYS_ALL           FYS_ALL, "
				+ "     F3.FYS_TODAY         FYS_TODAY, "
				+ "    -- 库存统计\n "
				+ "     G1.M2014032694231207 KC_S2, "
				+ "     G1.M2014032694231208 KC_S3, "
				+ "    G2.KC_ALL            KC_ALL, "
				+ "     -- 实销统计\n "
				+ "    H1.M2014032694231207 SX_S2, "
				+ "    H1.M2014032694231208 SX_S3, "
				+ "    H2.SXSJ_ALL          SXSJ_ALL, "
				+ "    H3.SXSJ_TODAY        SXSJ_TODAY "
				+ "   FROM ORG_DATA         A, -- 大区主数据\n"
				+ "      DK_COUNT         B, -- 打款家数\n"
				+ "     YC_COUNT         C, -- 到车家数\n"
				+ "     CLYS_MODEL_COUNT D1, -- 车辆验收统计\n"
				+ "   CLYS_ALL_COUNT   D2, -- 车辆验收统计\n"
				+ "   CLYS_TODAY_COUNT D3, -- 车辆验收统计\n"
				+ "    CLZT_MODEL_COUNT E1, -- 车辆在途统计\n"
				+ "    CLZT_ALL_COUNT   E2, -- 车辆在途统计\n"
				+ "    FYTJ_MODEL_COUNT F1, -- 车辆发运统计\n"
				+ "    FYTJ_ALL_COUNT   F2, -- 车辆发运统计\n"
				+ "   FYTJ_TODAY_COUNT F3, -- 车辆发运统计\n"
				+ "    KCTJ_MODEL_COUNT G1, -- 库存统计\n"
				+ "    KCTJ_ALL_COUNT   G2, -- 库存统计\n"
				+ "   SXTJ_MODEL_COUNT H1, -- 实销统计 \n"
				+ "    SXTJ_ALL_COUNT   H2, -- 实销统计 \n"
				+ "    SXTJ_TODAY_COUNT H3 -- 实销统计  \n"
				+ "   WHERE A.ORG_ID = B.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = C.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = D1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = D2.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = D3.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = E1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = E2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F3.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = G1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = G2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = H1.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = H2.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = H3.ROOT_ORG_ID(+)\n"
				+ " UNION ALL\n"
				+ "  SELECT '合计' ORG_NAME,\n"
				+ "      999999 NAME_SORT,\n"
				+ "      SUM(B.DKJS) DKJS,\n"
				+ "    SUM(C.DCJS) DCJS,\n"
				+ "      -- 车辆验收统计\n"
				+ "     SUM(D1.M2014032694231207) Y2014032694231207,\n"
				+ "     SUM(D1.M2014032694231208) Y2014032694231208,\n"
				+ "     SUM(D2.INSPECTION_ALL) INSPECTION_ALL,\n"
				+ "     SUM(D3.INSPECTION_TODAY) INSPECTION_TODAY,\n"
				+ "      -- 车辆在途统计\n"
				+ "     SUM(E1.M2014032694231207) Z2014032694231207,\n"
				+ "    SUM(E1.M2014032694231208) Z2014032694231208,\n"
				+ "     SUM(E2.ZTZS) ZTZS,\n"
				+ "    -- 发运统计\n"
				+ "    SUM(F1.M2014032694231207) F2014032694231207,\n"
				+ "     SUM(F1.M2014032694231208) F2014032694231208,\n"
				+ "    SUM(F2.FYS_ALL) FYS_ALL,\n"
				+ "    SUM(F3.FYS_TODAY) FYS_TODAY,\n"
				+ "    -- 库存统计\n"
				+ "     SUM(G1.M2014032694231207) K2014032694231207,\n"
				+ "     SUM(G1.M2014032694231208) K2014032694231208,\n"
				+ "     SUM(G2.KC_ALL) KC_ALL,\n"
				+ "     -- 实销统计\n"
				+ "    SUM(H1.M2014032694231207) S2014032694231207,\n"
				+ "   SUM(H1.M2014032694231208) S2014032694231208,\n"
				+ "   SUM(H2.SXSJ_ALL) SXSJ_ALL,\n"
				+ "   SUM(H3.SXSJ_TODAY) SXSJ_TODAY\n"
				+ "  FROM ORG_DATA         A, -- 大区主数据\n"
				+ "     DK_COUNT         B, -- 打款家数\n"
				+ "     YC_COUNT         C, -- 到车家数\n"
				+ "    CLYS_MODEL_COUNT D1, -- 车辆验收统计\n"
				+ "     CLYS_ALL_COUNT   D2, -- 车辆验收统计\n"
				+ "     CLYS_TODAY_COUNT D3, -- 车辆验收统计\n"
				+ "    CLZT_MODEL_COUNT E1, -- 车辆在途统计\n"
				+ "    CLZT_ALL_COUNT   E2, -- 车辆在途统计\n"
				+ "    FYTJ_MODEL_COUNT F1, -- 车辆发运统计\n"
				+ "    FYTJ_ALL_COUNT   F2, -- 车辆发运统计\n"
				+ "    FYTJ_TODAY_COUNT F3, -- 车辆发运统计\n"
				+ "   KCTJ_MODEL_COUNT G1, -- 库存统计\n"
				+ "    KCTJ_ALL_COUNT   G2, -- 库存统计\n"
				+ "   SXTJ_MODEL_COUNT H1, -- 实销统计 \n"
				+ "   SXTJ_ALL_COUNT   H2, -- 实销统计 \n"
				+ "   SXTJ_TODAY_COUNT H3 -- 实销统计  \n"
				+ "  WHERE A.ORG_ID = B.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = C.ROOT_ORG_ID(+)\n"
				+ "    AND A.ORG_ID = D1.ROOT_ORG_ID(+)\n"
				+ "    AND A.ORG_ID = D2.ROOT_ORG_ID(+)\n"
				+ "    AND A.ORG_ID = D3.ROOT_ORG_ID(+)\n"
				+ "    AND A.ORG_ID = E1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = E2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = F3.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = G1.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = G2.ROOT_ORG_ID(+)\n"
				+ "   AND A.ORG_ID = H1.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = H2.ROOT_ORG_ID(+)\n"
				+ "  AND A.ORG_ID = H3.ROOT_ORG_ID(+)\n"
				+ " )\n"
				+ " SELECT nvl(ORG_NAME,0) ORG_NAME ,nvl(NAME_SORT,0) NAME_SORT,nvl(DKJS,0) DKJS,nvl(DCJS,0) DCJS, nvl(YS_S2,0) YS_S2,  nvl(YS_S3,0) YS_S3, "
				+ "nvl(INSPECTION_ALL,0) INSPECTION_ALL,nvl(INSPECTION_TODAY,0) INSPECTION_TODAY,nvl(ZT_S2,0) ZT_S2,"
				+ "nvl(ZT_S3,0) ZT_S3,nvl(ZTZS,0) ZTZS, nvl(FY_S2,0) FY_S2,"
				+ "nvl(FY_S3,0) FY_S3,nvl(FYS_ALL,0) FYS_ALL,nvl(FYS_TODAY,0) FYS_TODAY,"
				+ "nvl(KC_S2,0) KC_S2,nvl(KC_S3,0) KC_S3,nvl(KC_ALL,0) KC_ALL,nvl(SX_S2,0) SX_S2,"
				+ "nvl(SX_S3,0) SX_S3, nvl(SXSJ_ALL,0) SXSJ_ALL, nvl(SXSJ_TODAY,0) SXSJ_TODAY  FROM QUERY_LIST ORDER BY NAME_SORT");

		return sb;
	}

	public List<Map<String, Object>> getVehicleType() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct model_name,model_id from VW_MATERIAL_GROUP");
		List<Map<String, Object>> list = dao.pageQuery(sb.toString(), null,
				getFunName());
		return list;
	}

	public List<Map<String, Object>> getPackageIdS2() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct package_name,package_id from VW_MATERIAL_GROUP WHERE MODEL_ID = 2014032694231207 order by package_id");
		List<Map<String, Object>> list = dao.pageQuery(sb.toString(), null,
				getFunName());
		return list;
	}

	public List<Map<String, Object>> getPackageIdS3() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct package_name,package_id from VW_MATERIAL_GROUP WHERE MODEL_ID = 2014032694231208 order by package_id");
		List<Map<String, Object>> list = dao.pageQuery(sb.toString(), null,
				getFunName());
		return list;
	}

	public StringBuffer dealerReportSql(RequestWrapper request) {
		String DATE = CommonUtils.checkNull(request.getParamValue("DATE"));
		StringBuffer sb = new StringBuffer();
		sb.append("with dealer_info as "
+" (SELECT  t2.root_org_id, "
+"          t2.root_org_name, "
+"          t2.org_id, "
+"          t2.org_name, "
+"          t2.dealer_id, "
+"          t2.dealer_code, "
+"          t.dealer_shortname, "
+"          t1.image_comfirm_level, "
+"          t2.city_name, "
+"          sum(t3.amount) kyye "
+"     FROM tm_dealer             t, "
+"          vw_org_dealer_service t2, "
+"          tm_dealer_detail      t1, "
+"          TT_SALES_FIN_ACC      t3 "
+"    where t.dealer_id = t2.dealer_id(+) "
+"      and t.dealer_id = t1.FK_DEALER_ID(+) "
+"      and t.dealer_id = t3.dealer_id(+) "
+"      and t.dealer_type = 10771001 "
+"    group by t2.root_org_id, "
+"             t2.root_org_name, "
+"             t2.org_id, "
+"             t2.org_name, "
+"             t2.dealer_id, "
+"             t2.dealer_code, "
+"             t.dealer_shortname, "
+"             t1.image_comfirm_level, "
+"             t2.city_name), "

+" drtc as "
+"  (select aa.dealer_id, sum(aa.s2drtc) s2drtc, sum(aa.s3drtc) s3drtc "
+"     from (select t1.dealer_id, "
+"                  t2.MODEL_ID, "
+"                  DECODE(t2.MODEL_ID, 2014032694231207, sum(t.ORDER_AMOUNT), 0) s2drtc, "
+"                  DECODE(t2.MODEL_ID, 2014032694231208, sum(t.ORDER_AMOUNT), 0) s3drtc "
+"             from tt_vs_order_detail t, tt_vs_order t1, vw_material_group t2 "
+"            where t.order_id = t1.order_id "
+"              and t.package_id = t2.package_id "
+"              AND TRUNC(t1.RAISE_DATE) = TRUNC(to_date('"+ DATE + "','yyyy-mm-dd')) "
+"            group by t1.dealer_id, t2.MODEL_ID) aa "
+"    group by aa.dealer_id), "
+" dytc as "
+"  (select aa.dealer_id, sum(aa.s2dytc) s2dytc, sum(aa.s3dytc) s3dytc "
+"     from (select t1.dealer_id, "
+"                  t2.MODEL_ID, "
+"                  DECODE(t2.MODEL_ID, 2014032694231207, sum(t.ORDER_AMOUNT), 0) s2dytc, "
+"                  DECODE(t2.MODEL_ID, 2014032694231208, sum(t.ORDER_AMOUNT), 0) s3dytc "
+"             from tt_vs_order_detail t, tt_vs_order t1, vw_material_group t2 "
+"            where t.order_id = t1.order_id "
+"              and t.package_id = t2.package_id "
+"              and t1.RAISE_DATE >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'mm') "
+"              and t1.RAISE_DATE <= last_day(to_date('"+ DATE + "','yyyy-mm-dd')) "
+"            group by t1.dealer_id, t2.MODEL_ID) aa "
+"    group by aa.dealer_id), "

+" dntc as "
+"  (select aa.dealer_id, sum(aa.s2drtc) s2dntc, sum(aa.s3drtc) s3dntc "
+"     from (select t1.dealer_id, "
+"                  t2.MODEL_ID, "
+"                  DECODE(t2.MODEL_ID, 2014032694231207, sum(t.ORDER_AMOUNT), 0) s2drtc, "
+"                  DECODE(t2.MODEL_ID, 2014032694231208, sum(t.ORDER_AMOUNT), 0) s3drtc "
+"             from tt_vs_order_detail t, tt_vs_order t1, vw_material_group t2 "
+"            where t.order_id = t1.order_id "
+"              and t.package_id = t2.package_id "
+"              and t1.RAISE_DATE >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY') "
+"              and t1.RAISE_DATE <= add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY'), 12) - 1 "
+"            group by t1.dealer_id, t2.MODEL_ID) aa "
+"    group by aa.dealer_id), "

+" drsx as "
+"  (select aa.dealer_id, sum(aa.s2drsx) s2drsx, sum(aa.s3drsx) s3drsx "
+"     from (select t1.dealer_id, "
+"                  t1.model_id, "
+"                  DECODE(t1.MODEL_ID, 2014032694231207, count(1), 0) s2drsx, "
+"                  DECODE(t1.MODEL_ID, 2014032694231208, count(1), 0) s3drsx "
+"             from tt_dealer_actual_sales t, tm_vehicle t1 "
+"            where t.VEHICLE_id = t1.VEHICLE_id(+) "
+"              and t.IS_RETURN = 10041002 "
+"              AND TRUNC(t.SALES_DATE) = TRUNC(to_date('"+ DATE + "','yyyy-mm-dd')) "
+"            group by t1.dealer_id, t1.model_id) aa "
+"    group by aa.dealer_id), "

+" dysx as "
+"  (select aa.dealer_id, sum(aa.s2dysx) s2dysx, sum(aa.s3dysx) s3dysx "
+"     from (select t1.dealer_id, "
+"                  t1.model_id, "
+"                  DECODE(t1.MODEL_ID, 2014032694231207, count(1), 0) s2dysx, "
+"                  DECODE(t1.MODEL_ID, 2014032694231208, count(1), 0) s3dysx "
+"             from tt_dealer_actual_sales t, tm_vehicle t1 "
+"            where t.VEHICLE_id = t1.VEHICLE_id(+) "
+"              and t.IS_RETURN = 10041002 "
+"              and t.SALES_DATE >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'mm') "
+"              and t.SALES_DATE <= last_day(to_date('"+ DATE + "','yyyy-mm-dd')) "
+"            group by t1.dealer_id, t1.model_id) aa "
+"    group by aa.dealer_id), "
+" sysx as "
+"  (select aa.dealer_id, sum(aa.s2sysx) s2sysx, sum(aa.s3sysx) s3sysx "
+"     from (select t1.dealer_id, "
+"                  t1.model_id, "
+"                  DECODE(t1.MODEL_ID, 2014032694231207, count(1), 0) s2sysx, "
+"                  DECODE(t1.MODEL_ID, 2014032694231208, count(1), 0) s3sysx "
+"             from tt_dealer_actual_sales t, tm_vehicle t1 "
+"            where t.VEHICLE_id = t1.VEHICLE_id(+) "
+"              and t.IS_RETURN = 10041002 "
+"              and t.SALES_DATE >= add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd')), -1) "
+"              and t.SALES_DATE <= last_day(add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd')), -1)) "
+"            group by t1.dealer_id, t1.model_id) aa "
+"    group by aa.dealer_id), "

+" dnsx as "
+"  (select aa.dealer_id, sum(aa.s2dnsx) s2dnsx, sum(aa.s3dnsx) s3dnsx "
+"     from (select t1.dealer_id, "
+"                  t1.model_id, "
+"                  DECODE(t1.MODEL_ID, 2014032694231207, count(1), 0) s2dnsx, "
+"                  DECODE(t1.MODEL_ID, 2014032694231208, count(1), 0) s3dnsx "
+"             from tt_dealer_actual_sales t, tm_vehicle t1 "
+"            where t.VEHICLE_id = t1.VEHICLE_id(+) "
+"              and t.IS_RETURN = 10041002 "
+"              and t.SALES_DATE >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY') "
+"              and t.SALES_DATE <= add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY'), 12) - 1 "
+"            group by t1.dealer_id, t1.model_id) aa "
+"    group by aa.dealer_id), "
+" snsx as "
+"  (select t1.dealer_id, count(1) as snsx "
+"     from tt_dealer_actual_sales t, tm_vehicle t1 "
+"    where t.VEHICLE_id = t1.VEHICLE_id(+) "
+"      and t.IS_RETURN = 10041002 "
+"      and t.SALES_DATE >= trunc(trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'year') - 1, 'year') "
+"      and t.SALES_DATE <= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'year') - 1 "
+"    group by t1.dealer_id), "
+" ydrw as "
+"  (SELECT t1.dealer_id, sum(t2.sale_amount) ydrw "
+"     FROM TT_VS_MONTHLY_PLAN t1, TT_VS_MONTHLY_PLAN_DETAIL t2 "
+"    where t1.PLAN_ID = t2.PLAN_ID "
+"      and t1.status = 10271002 "
+"    group by t1.dealer_id), "
+" ndrw as "
+"  (SELECT t3.dealer_id, sum(t2.sale_amount) ndrw "
+"     FROM Tt_Vs_Yearly_Plan t1, Tt_Vs_Yearly_Plan_Detail t2, tm_dealer t3 "
+"    where t1.PLAN_ID = t2.PLAN_ID "
+"      and t1.company_id = t3.company_id "
+"      and t1.status = 10271002 "
+"    group by t3.dealer_id), "
+" dyhk as "
+" (select aa.dealer_id, sum(aa.amount) as dyhk "
+"   from (select t.dealer_id, t.amount "
+"            FROM TT_SALES_FIN_IN_DET t "
+"           where t.audit_date >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'mm') "
+"             and t.audit_date <= last_day(to_date('"+ DATE + "','yyyy-mm-dd')) "
+"             and t.status = 18801003 "
+"          union "
+"          select t2.dealer_id, t1.amount "
+"            FROM TT_SALES_OTHER_ACCEPT t1, Tt_Sales_Other_Credit t2 "
+"           where t1.cre_id = t2.cre_id "
+"             and t1.update_date >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'mm') "
+"             and t1.update_date <= last_day(to_date('"+ DATE + "','yyyy-mm-dd'))) aa "
+"   group by aa.dealer_id), "
+" dnhk as "
+" (select aa.dealer_id, sum(aa.amount) as dnhk "
+"    from (select t.dealer_id, t.amount "
+"            FROM TT_SALES_FIN_IN_DET t "
+"           where t.audit_date >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY') "
+"             and t.audit_date <= add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY'), 12) - 1 "
+"             and t.status = 18801003 "
+"          union "
+"          select t2.dealer_id, t1.amount "
+"            FROM TT_SALES_OTHER_ACCEPT t1, Tt_Sales_Other_Credit t2 "
+"           where t1.cre_id = t2.cre_id "
+"             and t1.update_date >= trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY') "
+"             and t1.update_date <= add_months(trunc(to_date('"+ DATE + "','yyyy-mm-dd'), 'YYYY'), 12) - 1) aa "
+"   group by aa.dealer_id) "
+" select a.root_org_id, "
+"        a.root_org_name, "
+"        a.org_name, "
+"        a.org_id, "
+"        nvl((l.dyhk/10000),0) as dyhk, "
+"        nvl((m.dnhk/10000),0) as dnhk, "
+"        a.dealer_id, "
+"        (select count(t.dealer_id) "
+"           from tm_vehicle t "
+"          where t.life_cycle in (10321003, 10321005) "
+"            and t.dealer_id = a.dealer_id) as jxskc, "
+"        a.dealer_code, "
+"        a.dealer_shortname, "
+"        (select code_desc from tc_code where code_id = a.image_comfirm_level) as image_comfirm_level, "
+"        a.city_name, "
+"        nvl(a.kyye, 0) kyye, "
+"        nvl(b.s2drtc, 0) s2drtc, "
+"        nvl(b.s3drtc, 0) s3drtc, "
+"        nvl(b.s2drtc + b.s3drtc, 0) as drtchj, "
+"        nvl(c.s2dytc, 0) s2dytc, "
+"        nvl(c.s3dytc, 0) s3dytc, "
+"        nvl((c.s2dytc + c.s3dytc), 0) as dytchj, "
+"        nvl(d.s2dntc, 0) s2dntc, "
+"        nvl(d.s3dntc, 0) s3dntc, "
+"        nvl(d.s2dntc + d.s3dntc, 0) as dntchj, "
+"        nvl(e.s2drsx, 0) s2drsx, "
+"        nvl(e.s3drsx, 0) s3drsx, "
+"        nvl(e.s2drsx + e.s3drsx, 0) as drsxhj, "
+"        nvl(f.s2dysx, 0) s2dysx, "
+"        nvl(f.s3dysx, 0) s3dysx, "
+"        nvl(f.s2dysx + f.s3dysx, 0) as dysxhj, "
+"        nvl(g.s2dnsx, 0) s2dnsx, "
+"        nvl(g.s3dnsx, 0) s3dnsx, "
+"        nvl(g.s2dnsx + g.s3dnsx, 0) as dnsxhj, "
+"        nvl(h.ydrw, 0) ydrw, "
+"        CONCAT(nvl(decode(nvl(h.ydrw,0),0,'100',(decode((c.s2dytc + c.s3dytc),0,'0',Cast((c.s2dytc + c.s3dytc) / h.ydrw as decimal(16, 2))) * 100)),0),'%') as ydwcl, "
+"        nvl(i.ndrw, 0) ndrw, "
+"        nvl(d.s2dntc + d.s3dntc, 0) as ntc, "
+"        CONCAT(nvl(decode(nvl(i.ndrw,0),0,'100',(decode((d.s2dntc + d.s3dntc),0,'0',Cast((d.s2dntc + d.s3dntc) / i.ndrw as decimal(16, 2)) * 100))),0),'%') as ndwcl, "
+"        nvl(j.s2sysx, 0) s2sysx,"
+"        case when nvl(j.s2sysx, 0) = 0 then '100%'when nvl(f.s2dysx, 0) = 0then '0%'when nvl(f.s2dysx, 0) = 0 and j.s2sysx = 0 then '100%'when j.s2sysx > 0 and f.s2dysx > 0 then CONCAT(cast((f.s2dysx-j.s2sysx) / j.s2sysx as decimal(16, 2))*100 ,'%')end as sys2sxhb,"
+"        nvl(j.s3sysx, 0) s3sysx,"
+"        case when nvl(j.s3sysx, 0) = 0 then '100%'when nvl(f.s3dysx, 0) = 0then '0%'when nvl(f.s3dysx, 0) = 0 and j.s3sysx = 0 then '100%'when j.s3sysx > 0 and f.s3dysx > 0 then CONCAT(cast((f.s3dysx-j.s3sysx) / j.s3sysx as decimal(16, 2))*100 ,'%')end as sys3sxhb, "
+"        nvl(g.s2dnsx + g.s3dnsx, 0) as dnsx, "
+"        nvl(k.snsx, 0) snsx, "
+"        case when nvl(k.snsx, 0) = 0 then '100%'when nvl((g.s2dnsx + g.s3dnsx), 0) = 0 then '0%' when k.snsx > 0 and (g.s2dnsx + g.s3dnsx) > 0 then CONCAT(cast(((g.s2dnsx + g.s3dnsx) - k.snsx) / k.snsx as decimal(16, 2))*100 ,'%')end as snsxhb "
+"   from dealer_info a, "
+"        drtc        b, "
+"        dytc        c, "
+"        dntc        d, "
+"        drsx        e, "
+"        dysx        f, "
+"        dnsx        g, "
+"        ydrw        h, "
+"        ndrw        i, "
+"        sysx        j, "
+"        snsx        k, "
+"        dyhk        l, "
+"        dnhk        m "
+"  where a.dealer_id = b.dealer_id(+) "
+"    and a.dealer_id = c.dealer_id(+) "
+"    and a.dealer_id = d.dealer_id(+) "
+"    and a.dealer_id = e.dealer_id(+) "
+"    and a.dealer_id = f.dealer_id(+) "
+"    and a.dealer_id = g.dealer_id(+) "
+"    and a.dealer_id = h.dealer_id(+) "
+"    and a.dealer_id = i.dealer_id(+) "
+"    and a.dealer_id = j.dealer_id(+) "
+"    and a.dealer_id = k.dealer_id(+) "
+"    and a.dealer_id = l.dealer_id(+) "
+"    and a.dealer_id = m.dealer_id(+) ");

		return sb;
	}
}
