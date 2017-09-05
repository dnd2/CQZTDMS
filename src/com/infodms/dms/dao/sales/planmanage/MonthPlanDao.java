package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtVsMonthlyPlanPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthPlanDao extends BaseDao {
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final MonthPlanDao dao = new MonthPlanDao();

	public static final MonthPlanDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getGroupByOrg(String orgId, String companyId, String areaId) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT distinct B.GROUP_CODE, B.GROUP_NAME FROM  ");
		sql.append("       (SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("         WHERE TVMG.GROUP_LEVEL = 2\n");
		/*if (null != companyId && !"".equals(companyId)) {
			sql.append("           AND TVMG.COMPANY_ID = " + companyId + "\n");
		}*/
		sql.append("           AND TVMG.STATUS =  " + Constant.STATUS_ENABLE + "\n");
		/*sql.append("         START WITH TVMG.GROUP_ID IN\n");
		sql.append("                    (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM TM_AREA_GROUP TAG\n");
		if (null != areaId && !"".equals(areaId)) {
			sql.append("                      WHERE TAG.AREA_ID = " + areaId + ")\n");
		}
		sql.append("        CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---����ҵ��Χ�����ң��ҵ���ϵ����\n");*/
		sql.append("        ) B\n");
		sql.append("  ORDER BY B.GROUP_CODE, B.GROUP_NAME");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	/*
	 * �¶�����У����֯�Ƿ����
	 */
	public List<Map<String, Object>> oemMonthPlanCheckOrg(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from tm_org org\n");
		sql.append("         where org.ORG_CODE = p.ORG_CODE\n");
		sql.append("           and org.COMPANY_ID = " + companyId + "\n");// �๫˾����
		sql.append("           and org.STATUS = " + Constant.STATUS_ENABLE + "\n");// ��Ч
		sql.append("           and org.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");// ������֯
		sql.append("           and org.DUTY_TYPE = " + Constant.DUTY_TYPE_LARGEREGION + ")\n");// ����
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	
	public List<Map<String, Object>> oemMonthPlanCheckDlr(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is not null\n");
		sql.append("   and p.ORG_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from tm_dealer td\n");
		sql.append("         where td.DEALER_CODE = p.DEALER_CODE\n");
		sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");// �๫˾����
		sql.append("           and td.STATUS = " + Constant.STATUS_ENABLE + "\n");// ��Ч
		// sql.append(" and td.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");//������֯
		sql.append("          )\n");// ����
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * �¶�����У�鳵ϵ�Ƿ����
	 */
	public List<Map<String, Object>> oemMonthPlanCheckGroup(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
		sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
		sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");// ��Ч
		sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * �¶�����У����֯�Ƿ���ҵ��Χһ��
	 */
	public List<Map<String, Object>> oemMonthPlanCheckOrgArea(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("        from tm_org org, TM_ORG_BUSINESS_AREA area\n");
		sql.append("        where org.ORG_CODE = p.ORG_CODE\n");
		sql.append("           and org.ORG_ID = area.ORG_ID\n");
		sql.append("           and org.STATUS = " + Constant.STATUS_ENABLE + "\n");// ��Ч
		sql.append("           and area.AREA_ID = " + areaId + "\n");
		sql.append("           and org.COMPANY_ID = " + companyId + "\n");// �๫˾����
		sql.append("           and org.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");// ������֯
		sql.append("           and org.DUTY_TYPE = " + Constant.DUTY_TYPE_LARGEREGION + ")\n");// ����
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	public List<Map<String, Object>> sbuMonthPlanCheckDlrArea(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		// String dealerId = map.get("dealerId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is not null\n");
		sql.append("   and p.ORG_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("        from TM_DEALER td\n");
		sql.append("           where td.STATUS = " + Constant.STATUS_ENABLE + "\n");// ��Ч
		// sql.append("           and area.AREA_ID = " + areaId + "\n");
		sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");// �๫˾����
		sql.append("           )\n");// ������֯
		// sql.append(" and td.DUTY_TYPE =
		// "+Constant.DUTY_TYPE_LARGEREGION+")\n");//����
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * �¶�����У�鳵ϵ�Ƿ���ҵ��Χһ��
	 */
	public List<Map<String, Object>> oemMonthPlanCheckGroupArea(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String groupArea = map.get("groupArea").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.PLAN_YEAR = " + year + "\n");
		sql.append("   and p.PLAN_MONTH = " + month + "\n");
		sql.append("   and p.USER_ID = " + userId + "\n");
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and p.GROUP_CODE not in(" + PlanUtil.createSqlStr(groupArea) + ")\n");
		sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ������������
	 */
	public int insertPlan(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String dealersql = map.get("dealersql").toString();
		String companyId = map.get("companyId").toString();
		// String planType = map.get("planType").toString();
		String dutyType = map.get("dutyType").toString();
		/*
		 * String flag = null;//�ǳ������뻹��������
		 * if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){ flag =
		 * Constant.COMP_FLAG.toString();//���� }else{ flag =
		 * Constant.ARERA_FLAG.toString();//���� }
		 */
		int isFlag = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("merge into TT_VS_MONTHLY_PLAN tp\n");
		sql.append("using (select F_GETID() PLAN_ID,\n");
		sql.append("              " + companyId + " COMPANY_ID,\n");
		/// sql.append("              " + areaId + " AREA_ID,\n");
		sql.append("              aa.PLAN_YEAR,\n");
		sql.append("              aa.PLAN_MONTH,\n");
		sql.append("              " + Constant.ORG_TYPE_DEALER + " ORG_TYPE,\n");
		sql.append("              aa.DEALER_ID,\n");
		sql.append("             null PLAN_TYPE,\n");
		sql.append("              " + Constant.PLAN_MANAGE_UNCONFIRM + " STATUS,\n");
		sql.append("              0 VER,\n");
		sql.append("              " + userId + " CREATE_BY,\n");
		sql.append("              " + isFlag + " IS_FLAG,\n");
		sql.append("              sysdate CREATE_DATE\n");
		sql.append("         from (select distinct p.PLAN_YEAR, p.PLAN_MONTH,TD.DEALER_ID\n");
		sql.append("                 from TMP_VS_MONTHLY_PLAN p,TM_DEALER TD\n");
		sql.append("                where p.DEALER_CODE=TD.DEALER_CODE and p.PLAN_YEAR = " + year + "\n");
		 sql.append("          and exists(" + dealersql + " and v.DEALER_ID=td.dealer_id) \n");
		sql.append("                  and p.PLAN_MONTH = " + month + ") aa) a\n");
		sql.append("on (tp.COMPANY_ID = a.COMPANY_ID  and tp.DEALER_ID = a.DEALER_ID and tp.PLAN_YEAR = a.PLAN_YEAR and tp.PLAN_MONTH = a.PLAN_MONTH  and tp.STATUS = a.STATUS)\n");
		sql.append("WHEN MATCHED THEN UPDATE SET TP.CREATE_DATE=TP.CREATE_DATE \n");
		sql.append("when not matched then\n");
		sql.append("  insert\n");
		sql.append("    (PLAN_ID,\n");
		sql.append("     COMPANY_ID,\n");
		// sql.append("     AREA_ID,\n");
		sql.append("     PLAN_YEAR,\n");
		sql.append("     PLAN_MONTH,\n");
		sql.append("     ORG_TYPE,\n");
		sql.append("     DEALER_ID,\n");
		sql.append("     STATUS,\n");
		sql.append("     VER,\n");
		sql.append("     PLAN_TYPE,\n");
		sql.append("     CREATE_BY,\n");
		sql.append("     CREATE_DATE,\n");
		sql.append("     IS_FLAG)\n");
		sql.append("  values\n");
		sql.append("    (a.PLAN_ID,\n");
		sql.append("     a. COMPANY_ID,\n");
		// sql.append("     a.AREA_ID,\n");
		sql.append("     A.PLAN_YEAR,\n");
		sql.append("     A.PLAN_MONTH,\n");
		sql.append("     a.ORG_TYPE,\n");
		sql.append("     a.DEALER_ID,\n");
		sql.append("     a.STATUS,\n");
		sql.append("     a.VER,\n");
		sql.append("     a.PLAN_TYPE,\n");
		sql.append("     a.CREATE_BY,\n");
		sql.append("     a. CREATE_DATE,");
		sql.append("     a.IS_FLAG)\n");
		return update(sql.toString(), null);
	}

	/*
	 * ������ϸ������
	 */
	public int insertPlanDetail(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		String dealersql = map.get("dealersql").toString();
		String companyId = map.get("companyId").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("MERGE into TT_VS_MONTHLY_PLAN_DETAIL a\n");
		sql.append("using (select distinct tpp.PLAN_ID, tvmg.GROUP_ID, tmpp.SUM_AMT\n");
		sql.append("        from TT_VS_MONTHLY_PLAN  tpp,\n");
		sql.append("             TMP_VS_MONTHLY_PLAN tmpp,\n");
		sql.append("             TM_DEALER                 TD,\n");
		sql.append("             TM_VHCL_MATERIAL_GROUP tvmg\n");
		sql.append("       where tvmg.GROUP_CODE = tmpp.GROUP_CODE\n");
		sql.append("         and tmpp.GROUP_CODE = tvmg.GROUP_CODE\n");
		sql.append("         and tpp.DEALER_ID=TD.DEALER_ID\n");
		sql.append("         and tmpp.DEALER_CODE=TD.DEALER_CODE\n");
		sql.append("         and tpp.PLAN_YEAR = tmpp.PLAN_YEAR\n");
		sql.append("         and tpp.PLAN_MONTH = tmpp.PLAN_MONTH\n");
		sql.append("         and tpp.COMPANY_ID = tvmg.COMPANY_ID");
		sql.append("		 and tmpp.plan_year = " + year + "\n");
		sql.append("		 and tmpp.plan_month = " + month + "\n");
		sql.append("          and tpp.COMPANY_ID =" + companyId + "\n");
		 sql.append("          and exists(" + dealersql + " and v.DEALER_ID=tpp.dealer_id) \n");
		// sql.append("          and tpp.AREA_ID =" + areaId + "\n");
		sql.append("          and tpp.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
		sql.append("          and tmpp.USER_ID =" + userId + ") c\n");
		sql.append("on (a.PLAN_ID = c.PLAN_ID and a.MATERIAL_GROUPID = c.GROUP_ID)\n");
		sql.append("when MATCHED then\n");
		sql.append("  update\n");
		sql.append("     set a.SALE_AMOUNT = to_number(c.SUM_AMT),\n");
		sql.append("         a.UPDATE_BY   = " + userId + ",\n");
		sql.append("         a.UPDATE_DATE = sysdate\n");
		sql.append("when not matched then\n");
		sql.append("  INSERT\n");
		sql.append("    (a.DETAIL_ID,\n");
		sql.append("     a.PLAN_ID,\n");
		sql.append("     a.MATERIAL_GROUPID,\n");
		sql.append("     a.SALE_AMOUNT,\n");
		sql.append("     a.CREATE_BY,\n");
		sql.append("     a.CREATE_DATE)\n");
		sql.append("  values\n");
		sql.append("    (F_GETID(), c.PLAN_ID, c.GROUP_ID, c.SUM_AMT, " + userId + ", sysdate)");
		return update(sql.toString(), null);
	}

	/*
	 * ��ѯδȷ�ϵ��¶���������
	 */
	public List<Map<String, Object>> selectUnPublistDate(Map<String, Object> map) {
		String orgId = map.get("orgId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select distinct p.PLAN_YEAR, p.PLAN_MONTH, p.AREA_ID, ta.AREA_NAME,tc.CODE_ID,tc.CODE_DESC\n");
		sql.append("  from TT_VS_MONTHLY_PLAN p, TM_BUSINESS_AREA ta,TC_CODE TC\n");
		sql.append(" where p.AREA_ID = ta.AREA_ID\n");
		sql.append("  and p.PLAN_TYPE=tc.CODE_ID\n");
		sql.append("  and p.DEALER_ID is not null\n");
		sql.append("  and p.IS_FLAG = 0\n");
		sql.append("  and p.DEALER_ID in\n");
		sql.append("      (select td.dealer_id\n");
		sql.append("         from tm_dealer td\n");
		sql.append("        start with td.DEALER_ID in\n");
		sql.append("                   (select r.dealer_id\n");
		sql.append("                      from TM_DEALER_ORG_RELATION r\n");
		sql.append("                     where r.ORG_ID = " + orgId + ")\n");
		sql.append("       connect by prior td.DEALER_ID = td.PARENT_DEALER_D)");

		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("   and p.COMPANY_ID = " + companyId + "\n");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		/*
		 * sql.append(" and not exists (select 1\n"); sql.append(" from
		 * TT_VS_MONTHLY_PLAN tp\n"); sql.append(" where tp.ORG_ID =
		 * p.ORG_ID\n"); sql.append(" and tp.AREA_ID = p.AREA_ID\n");
		 * sql.append(" and tp.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");
		 * sql.append(" and tp.COMPANY_ID = "+companyId+"\n"); sql.append(" and
		 * tp.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n"); sql.append(" and
		 * tp.PLAN_YEAR = p.PLAN_YEAR\n"); sql.append(" and tp.PLAN_MONTH =
		 * p.PLAN_MONTH\n"); sql.append(" and tp.DEALER_ID is not null)\n");
		 */
		sql.append(" order by p.PLAN_YEAR, p.PLAN_MONTH");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ��ѯҵ��Χ�³�ϵ
	 */
	public List<Map<String, Object>> selectAreaGroup(String areaId, String companyId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("  WHERE TVMG.GROUP_LEVEL = 2\n");
		sql.append("    AND TVMG.COMPANY_ID = " + companyId + "\n");
		sql.append("    AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("                                FROM TM_AREA_GROUP TAG\n");
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");
		params.add(areaId);
		sql.append("   CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---����ҵ��Χ�����ң��ҵ���ϵ����\n");
		sql.append(" UNION\n");
		sql.append("   SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = 2\n");
		sql.append("    AND TVMG.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("                                FROM TM_AREA_GROUP TAG\n");
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");
		params.add(areaId);
		sql.append(" CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID ----����ҵ��Χ�����ң��ҵ���ϵ\n");
		sql.append("------union ԭ����Ϊ��֧������ҵ��Χ��ʱ��������õ�Ʒ�ƣ���ϵ�����ͣ�����");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * �����ѯ��֯����Ϣ ����true��ʾ���ڣ����򲻴���
	 */
	public boolean selectSingleOrg(TmOrgPO po) {
		List<TmOrgPO> list = dao.select(po);
		boolean isExists = true;
		if (null == list || list.size() == 0) {
			isExists = false;
		}
		return isExists;
	}

	/*
	 * �����ѯ��������Ϣ
	 */
	public List<TmVhclMaterialGroupPO> selectSingleMaterialGroup(TmVhclMaterialGroupPO po) {
		return dao.select(po);
	}

	/*
	 * ȫ��ʱ��У��ORG_CODE�Ƿ����
	 */
	public List<Map<String, Object>> checkNotExistsOrg(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where p.USER_ID = ?\n");
		params.add(userId);
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists\n");
		sql.append(" (select 1 from tm_org org where org.ORG_CODE = p.ORG_CODE)\n");
		sql.append(" order by p.ROW_NUMBER;");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ȫ��ʱ��У��ORG_CODE�Ƿ����
	 */
	public List<Map<String, Object>> checkNotExistsGroup(Map<String, Object> map) {
		String year = map.get("year").toString();
		String month = map.get("month").toString();
		String userId = map.get("userId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where p.USER_ID = ?\n");
		params.add(userId);
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists\n");
		sql.append(" (select 1 from TM_VHCL_MATERIAL_GROUP g where g.GROUP_CODE = p.GROUP_CODE)\n");
		sql.append(" order by p.ROW_NUMBER;");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * OEM����ʱ����ѯ��ʱ������
	 */
	public List<Map<String, Object>> oemSelectTmpMonthPlanOld(String year, String month, Long userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select org.ORG_CODE, org.ORG_NAME, g.GROUP_CODE, g.GROUP_NAME, plan.SUM_AMT\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN plan, TM_ORG org, TM_VHCL_MATERIAL_GROUP g\n");
		sql.append(" where plan.ORG_CODE = org.ORG_CODE\n");
		sql.append("   and plan.GROUP_CODE = g.GROUP_CODE\n");
		sql.append("   and plan.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and plan.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and plan.USER_ID = ?\n");
		params.add(userId);

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * OEM����ʱ����ѯ��ʱ�����ݣ����������� �����������֯�ĳɾ����� modify by wjb at 2010-08-18
	 */
	public List<Map<String, Object>> oemSelectTmpMonthPlan(String year, String month, Long userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select d.DEALER_CODE,d.DEALER_SHORTNAME, g.GROUP_CODE, g.GROUP_NAME, plan.SUM_AMT\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN plan, TM_DEALER d, TM_VHCL_MATERIAL_GROUP g\n");
		sql.append(" where plan.DEALER_CODE = d.DEALER_CODE\n");
		sql.append("   and plan.GROUP_CODE = g.GROUP_CODE\n");
		sql.append("   and plan.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and plan.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and plan.USER_ID = ?\n");
		sql.append(" order by d.DEALER_CODE,g.GROUP_CODE");
		params.add(userId);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ɾ��ҵ����У��û������δȷ�����Ŀ��
	 */
	public void clearUserMonthPlan(TtVsMonthlyPlanPO po) {
		TtVsMonthlyPlanPO cPo = new TtVsMonthlyPlanPO();
		cPo.setPlanId(po.getPlanId());
		dao.delete(cPo);
	}

	/*
	 * ��ѯδȷ���¶�����
	 */
	public PageResult<Map<String, Object>> selectUnconfirmMonthPlanOld(Map<String, Object> map, int curPage, int pageSize) {
		List<Object> params = new LinkedList<Object>();
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();
		StringBuffer sql = new StringBuffer("");
		sql.append("select plan.PLAN_YEAR,\n");
		sql.append("       plan.PLAN_MONTH,\n");
		sql.append("       plan.AREA_ID,\n");
		sql.append("       ta.AREA_NAME,\n");
		sql.append("       sum(detail.SALE_AMOUNT) amount,\n");
		sql.append("       tc.CODE_DESC\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        plan,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL detail,\n");
		sql.append("       TC_CODE                   tc,\n");
		sql.append("       TM_BUSINESS_AREA          ta\n");
		sql.append(" where plan.PLAN_Id = detail.PLAN_ID\n");
		sql.append("   and plan.STATUS = tc.CODE_ID\n");
		sql.append("   and plan.AREA_ID = ta.AREA_ID");
		sql.append("   and plan.COMPANY_ID = " + companyId + "\n");
		sql.append("   and plan.AREA_ID = " + areaId + "\n");
		sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
		sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
		sql.append("group by plan.PLAN_YEAR,\n");
		sql.append("         plan.PLAN_MONTH,\n");
		sql.append("         plan.AREA_ID,\n");
		sql.append("         ta.AREA_NAME,\n");
		sql.append("         tc.CODE_DESC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/*
	 * ��ѯδȷ���¶��������ڳ��������� modify by wjb at 2010-08-18 �б���ʾ���ӣ��������ͣ����ۼƻ������ۼƻ���
	 */
	public PageResult<Map<String, Object>> selectUnconfirmMonthPlan(Map<String, Object> map, int curPage, int pageSize) {
		List<Object> params = new LinkedList<Object>();
		String companyId = map.get("companyId").toString();
		// String areaId = map.get("areaId").toString();
		String dealerId = map.get("dealerId").toString();
		StringBuffer sql = new StringBuffer("");
		sql.append("select plan.PLAN_YEAR,\n");
		sql.append("       plan.PLAN_MONTH,\n");
		sql.append("       tcp.company_id AREA_ID,\n");
		// sql.append("       plan.PLAN_TYPE,\n");// ��������
		sql.append("       plan.PLAN_YEAR || '-'|| plan.PLAN_MONTH as PLAN_YM,\n");// ����
		sql.append("       tcp.company_shortname AREA_NAME,\n");
		sql.append("       sum(detail.SALE_AMOUNT) amount,\n");
		sql.append("       tc.CODE_DESC\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        plan,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL detail,\n");
		sql.append("       TC_CODE                   tc,\n");
		// sql.append("       TM_BUSINESS_AREA          ta,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       tm_company tcp \n");
		sql.append(" where plan.PLAN_Id = detail.PLAN_ID\n");
		sql.append("   and plan.STATUS = tc.CODE_ID\n");
		// sql.append("   and plan.AREA_ID = ta.AREA_ID");
		sql.append("   and PLAN.DEALER_ID=TMD.DEALER_ID");
		sql.append("   and plan.COMPANY_ID=tcp.COMPANY_ID");
		sql.append("   and plan.COMPANY_ID = " + companyId + "\n");
		sql.append("          and exists(" + dealerId + " and v.DEALER_ID=plan.dealer_id) \n");
		// sql.append("   and plan.AREA_ID = " + areaId + "\n");
		sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
		sql.append("   and tmd.status=" + Constant.STATUS_ENABLE + "\n");
		sql.append("   and plan.IS_FLAG=0\n");
		sql.append("group by plan.PLAN_YEAR,\n");
		sql.append("         plan.PLAN_MONTH,\n");
		// sql.append("         plan.PLAN_TYPE,\n");
		sql.append("         tcp.COMPANY_ID ,\n");
		sql.append("         tcp.COMPANY_SHORTNAME,\n");
		sql.append("         tc.CODE_DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/*
	 * OEM����ʱ����ѯ��ʱ������
	 */
	public List<Map<String, Object>> oemSelectUnconfirmMonthPlan(String year, String month, Long userId, String companyId, String dealerId, String planType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select td.DEALER_SHORTNAME,\n");
		sql.append("       td.DEALER_CODE,\n");
		sql.append("       material.GROUP_CODE,\n");
		sql.append("       material.GROUP_NAME,\n");
		sql.append("       detail.SALE_AMOUNT\n");
		sql.append("  from TM_DEALER                 td,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP material,\n");
		sql.append("       TT_VS_MONTHLY_PLAN        plan,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL detail\n");
		sql.append(" where TD.DEALER_ID = plan.DEALER_ID\n");
		sql.append("   and plan.PLAN_ID = detail.PLAN_ID\n");
		sql.append("   and material.GROUP_ID = detail.MATERIAL_GROUPID\n");
		// sql.append("   and plan.AREA_ID=" + areaId + "\n");
		  sql.append(" and exists(" + dealerId + " and v.DEALER_ID=td.dealer_id)\n");
		sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
		sql.append("   and plan.PLAN_YEAR = " + year + "\n");
		sql.append("   and plan.PLAN_MONTH = " + month + "\n");
		sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "");
		sql.append("   and plan.IS_FLAG=0\n");
		// sql.append("   and plan.PLAN_TYPE=" + planType + "\n");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ��ѯ���汾�ţ���ȷ��
	 */
	public Integer selectMaxPlanVer(String year, String month, Long userId, String companyId, String dealerId, String planType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
		sql.append("  from TT_VS_MONTHLY_PLAN plan\n");
		sql.append(" where 1=1\n");
		sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		// sql.append("   and plan.AREA_ID=" + areaId + "\n");
		sql.append(" and exists(" + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
		sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
		sql.append("   and plan.PLAN_YEAR = " + year + "\n");
		sql.append("   and plan.PLAN_MONTH = " + month + "\n");
		sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("   and plan.IS_FLAG = 0 \n");
		// sql.append("   and plan.PLAN_TYPE = " + planType + "\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		Integer planVer = new Integer(map.get("PLAN_VER").toString());
		return planVer;
	}

	/*
	 * ��ѯ��ʱ�����Ƿ�����ͬ��ORG������ͬ�ĳ�ϵ ����true��ʾ������ͬ����
	 */
	public boolean isDumpData(String orgCode, String groupCode, String year, String month, String userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		boolean bl = false;

		sql.append("select *\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p\n");
		sql.append(" where p.ORG_CODE = ?\n");
		params.add(orgCode);
		sql.append("   and p.GROUP_CODE = ?");
		params.add(groupCode);
		sql.append("   and p.PLAN_YEAR = ?");
		params.add(year);
		sql.append("   and p.PLAN_MONTH = ?");
		params.add(month);
		sql.append("   and p.USER_ID = ?");
		params.add(userId);

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());

		if (null != list && list.size() > 1) {
			bl = true;
		}
		return bl;
	}

	/*
	 * ��ѯ��ʱ�����Ƿ�����ͬ��ORG������ͬ�ĳ�ϵ ���������ظ����ݼ���
	 */
	public List<Map<String, Object>> talbeCheckDump(String year, String month, String userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
		sql.append("  from TMP_VS_MONTHLY_PLAN p1, TMP_VS_MONTHLY_PLAN p2\n");
		sql.append(" where p1.org_CODE = p2.org_CODE\n");
		sql.append("   and p1.GROUP_CODE = p2.GROUP_CODE\n");
		sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
		sql.append("   and p1.PLAN_YEAR = p2.PLAN_YEAR\n");
		sql.append("   and p1.PLAN_MONTH = p2.PLAN_MONTH\n");
		sql.append("   and p1.USER_ID = p2.USER_ID\n");
		sql.append("   and p1.ORG_CODE is not null\n");
		sql.append("   and p1.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and p1.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and p1.USER_ID=?");
		params.add(userId);
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}

	// �����¶Ȼ��ܣ�ҵ��Χû��
	public Map<String, Object> selectMonthPublistMap(Long orgId, String year, String month, List<Map<String, Object>> list, String areaId, String companyId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select a.ORG_CODE,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sql.append(" sum(nvl(b.a" + map.get("GROUP_ID") + ",0)) a" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("     a.ORG_NAME\n");
		sql.append("  from (select o.ORG_CODE, o.ORG_NAME, p.PLAN_ID\n");
		sql.append("          from TT_VS_MONTHLY_PLAN p, TM_ORG o\n");
		sql.append("         where p.ORG_ID = o.ORG_ID\n");
		sql.append("           and p.COMPANY_ID = " + companyId + "\n");
		sql.append("           and p.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
		sql.append("           and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("           and p.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("           and p.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("           and p.ORG_ID = ?\n");
		params.add(orgId);
		sql.append("           and p.AREA_ID = ?\n");
		params.add(areaId);
		sql.append("           and p.PLAN_VER = (SELECT nvl(MAX(PLAN_VER),0) plan_ver\n");
		sql.append("                              FROM TT_VS_MONTHLY_PLAN plan\n");
		sql.append("                             WHERE plan.PLAN_YEAR = p.PLAN_YEAR\n");
		sql.append("                               AND plan.PLAN_MONTH = p.PLAN_MONTH\n");
		sql.append("                               and plan.COMPANY_ID=p.COMPANY_ID\n");
		sql.append("                               and plan.AREA_ID=p.AREA_ID\n");
		sql.append("                               and plan.STATUS=p.STATUS");
		sql.append("                               and plan.ORG_TYPE = " + Constant.ORG_TYPE_OEM + ")) a,\n");
		sql.append("       (select \n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sql.append("   decode(detail.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",detail.SALE_AMOUNT,0) a" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("           detail.PLAN_ID\n");
		sql.append("          from TT_VS_MONTHLY_PLAN_DETAIL detail) b\n");
		sql.append(" where a.PLAN_ID = b.plan_id\n");
		sql.append(" group by a.ORG_CODE, a.ORG_NAME");

		return dao.pageQueryMap(sql.toString(), params, getFunName() + areaId);
	}

	/**
	 * �����¶Ȼ��ܣ�ҵ��Χû�� add by zhaolunda 2010-08-19 ������������󣺳������¶�����ֱ���·��������̣��������������<br>
	 * �ľ����������л���
	 */
	public Map<String, Object> selectMonthPublistMapVer1(Long orgId, String year, String month, List<Map<String, Object>> list, String areaId, String companyId, String planType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select a.ORG_CODE,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sql.append(" sum(nvl(b.a" + map.get("GROUP_ID") + ",0)) a" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("     a.ORG_NAME\n");

		sql.append("from (select p.PLAN_ID,\n");
		sql.append("               (select org_code from tm_org where org_id = " + orgId + ") ORG_CODE,\n");
		sql.append("               (select org_NAME from tm_org where org_id = " + orgId + ") org_NAME\n");
		sql.append("          from TT_VS_MONTHLY_PLAN p\n");
		sql.append("           where p.IS_FLAG=0\n");
		sql.append("           and p.COMPANY_ID = " + companyId + "\n");
		sql.append("           and p.PLAN_TYPE = " + planType + "\n");
		sql.append("           and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");// �޸�Ϊ��������֯����
		sql.append("           and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("           and p.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("           and p.PLAN_MONTH = ?\n");
		params.add(month);
		/** ********************************** */
		sql.append("          and p.dealer_id in\n");
		sql.append("       (select m.dealer_id\n");
		sql.append("          from tm_dealer m\n");
		sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
		sql.append("         START WITH m.dealer_id in\n");
		sql.append("                    (select rel.dealer_id\n");
		sql.append("                       from tm_dealer_org_relation rel\n");
		sql.append("                      where rel.org_id = ?))");
		/** ********************************** */
		params.add(orgId);
		sql.append("           and p.AREA_ID = ?\n");
		params.add(areaId);
		sql.append("           and p.PLAN_TYPE = " + planType + "\n");
		sql.append("           and p.PLAN_VER = (SELECT nvl(MAX(PLAN_VER),0) plan_ver\n");
		sql.append("                              FROM TT_VS_MONTHLY_PLAN plan\n");
		sql.append("                             WHERE plan.PLAN_YEAR = p.PLAN_YEAR\n");
		sql.append("                               AND plan.PLAN_MONTH = p.PLAN_MONTH\n");
		sql.append("                               and plan.COMPANY_ID=p.COMPANY_ID\n");
		sql.append("                               and plan.AREA_ID=p.AREA_ID\n");
		sql.append("                               and plan.PLAN_TYPE=p.PLAN_TYPE\n");
		sql.append("                               and plan.IS_FLAG=p.IS_FLAG\n");
		sql.append("                               and plan.STATUS=p.STATUS");
		sql.append("                               and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + ")) a,\n");// �޸�Ϊ��������֯����
		sql.append("       (select \n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sql.append("   decode(detail.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",detail.SALE_AMOUNT,0) a" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("           detail.PLAN_ID\n");
		sql.append("          from TT_VS_MONTHLY_PLAN_DETAIL detail) b\n");
		sql.append(" where a.PLAN_ID = b.plan_id\n");
		sql.append(" group by  a.ORG_NAME,a.ORG_CODE");

		return dao.pageQueryMap(sql.toString(), params, getFunName() + areaId);
	}

	/*
	 * 
	 */
	public List<Map<String, Object>> selectPublishPlan(List<Map<String, Object>> serList, Long orgId, String year, String month, String areaId, String companyId) {
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer("");

		sql.append("select a.DEALER_CODE,a.DEALER_ID,\n");
		for (int i = 0; i < serList.size(); i++) {
			Map<String, Object> map = serList.get(i);
			sql.append("sum(nvl(decode(d.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",d.bs,0),0)) y" + map.get("GROUP_ID") + ",\n");
			sql.append("sum(nvl(decode(d.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",d.cs,0),0)) m" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("a.DEALER_SHORTNAME from (\n");

		sql.append(" select td.dealer_id,\n");
		sql.append("               td.DEALER_CODE,\n");
		sql.append("               td.DEALER_SHORTNAME,\n");
		sql.append("               area.AREA_ID\n");
		sql.append("          from tm_dealer               td,\n");
		sql.append("               TM_DEALER_ORG_RELATION  r,\n");
		sql.append("               TM_DEALER_BUSINESS_AREA area\n");
		sql.append("         where td.DEALER_ID = r.DEALER_ID\n");
		sql.append("           and td.DEALER_ID = area.DEALER_ID\n");
		sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           and area.AREA_ID = " + areaId + "\n");
		sql.append("           and r.ORG_ID = " + orgId + ") a,\n");
		sql.append("       (select nvl(c.dealer_id, b.dealer_id) dealer_id,\n");
		sql.append("               b.SALE_AMOUNT bs,\n");
		sql.append("               c.SALE_AMOUNT cs,\n");
		sql.append("               nvl(c.MATERIAL_GROUPID, b.material_groupid) material_groupid\n");
		sql.append("          from (select p.DEALER_ID,\n");
		sql.append("                       detail.SALE_AMOUNT,\n");
		sql.append("                       detail.MATERIAL_GROUPID\n");
		sql.append("                  from TT_VS_YEARLY_PLAN p, TT_VS_YEARLY_PLAN_DETAIL detail\n");
		sql.append("                 where p.PLAN_ID = detail.PLAN_ID\n");
		sql.append("                   and p.PLAN_YEAR = " + year + "\n");
		sql.append("                   and detail.PLAN_MONTH = " + month + "\n");
		sql.append("                   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("                   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("                   and p.PLAN_VER = (select max(plan_ver) plan_ver\n");
		sql.append("                                       from TT_VS_YEARLY_PLAN\n");
		sql.append("                                      where PLAN_YEAR = " + year + "\n");
		sql.append("                                        and STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("                                        and ORG_TYPE = " + Constant.ORG_TYPE_DEALER + ")) b\n");
		sql.append("          full outer join (select mplan.DEALER_ID,\n");
		sql.append("                                 detail.SALE_AMOUNT,\n");
		sql.append("                                 detail.MATERIAL_GROUPID,\n");
		sql.append("                                 mplan.AREA_ID\n");
		sql.append("                            from TT_VS_MONTHLY_PLAN        mplan,\n");
		sql.append("                                 TT_VS_MONTHLY_PLAN_DETAIL detail\n");
		sql.append("                           where mplan.PLAN_ID = detail.PLAN_ID\n");
		sql.append("                   and mplan.PLAN_YEAR = " + year + "\n");
		sql.append("                   and mplan.PLAN_MONTH = " + month + "\n");
		sql.append("                   and mplan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
		sql.append("                   and mplan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("                             and mplan.ORG_ID = " + orgId + ") c on b.MATERIAL_GROUPID =\n");
		sql.append("                                                                       c.MATERIAL_GROUPID\n");
		sql.append("                                                                   and b.dealer_id =\n");
		sql.append("                                                                       c.DEALER_ID) d\n");
		sql.append(" where a.DEALER_ID = d.dealer_id(+)\n");
		sql.append(" group by a.dealer_id, a.dealer_code, a.DEALER_SHORTNAME\n");
		sql.append(" order by a.dealer_id");

		return dao.pageQuery(sql.toString(), params, getFunName() + areaId);
	}

	/**
	 * �����·������¶����� add by zhaolunda 2010-08-19 ������������󣺳������¶�����ֱ���·���������
	 */
	public List<Map<String, Object>> selectPublishPlanVer1(List<Map<String, Object>> serList, Long orgId, String year, String month, String areaId, String companyId, String planType) {
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer("");

		sql.append("select a.DEALER_CODE,a.DEALER_ID,\n");
		for (int i = 0; i < serList.size(); i++) {
			Map<String, Object> map = serList.get(i);
			sql.append("sum(nvl(decode(d.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",d.bs,0),0)) y" + map.get("GROUP_ID") + ",\n");
			sql.append("sum(nvl(decode(d.MATERIAL_GROUPID," + map.get("GROUP_ID") + ",d.cs,0),d.bs)) m" + map.get("GROUP_ID") + ",\n");
		}
		sql.append("a.DEALER_SHORTNAME from (\n");

		sql.append(" select td.dealer_id,\n");
		sql.append("      td.DEALER_CODE,\n");
		sql.append("      td.DEALER_SHORTNAME,\n");
		sql.append("      area.AREA_ID\n");
		sql.append(" from tm_dealer td,\n");
		sql.append("      TM_DEALER_BUSINESS_AREA area\n");
		sql.append("where td.DEALER_ID = area.DEALER_ID\n");
		sql.append("  and td.dealer_id in\n");
		sql.append("      (select m.dealer_id\n");
		sql.append("         from tm_dealer m\n");
		sql.append("       CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
		sql.append("        START WITH m.dealer_id in\n");
		sql.append("                   (select rel.dealer_id\n");
		sql.append("                      from tm_dealer_org_relation rel\n");
		sql.append("                     where rel.org_id = " + orgId + "))\n");
		sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           and area.AREA_ID = " + areaId + "\n");
		sql.append("           ) a,\n");
		sql.append("       (select nvl(c.dealer_id, b.dealer_id) dealer_id,\n");
		sql.append("               b.SALE_AMOUNT bs,\n");
		sql.append("               c.SALE_AMOUNT cs,\n");
		sql.append("               nvl(c.MATERIAL_GROUPID, b.material_groupid) material_groupid\n");
		sql.append("          from (select p.DEALER_ID,\n");
		sql.append("                       detail.SALE_AMOUNT,\n");
		sql.append("                       detail.MATERIAL_GROUPID\n");
		sql.append("                  from TT_VS_MONTHLY_PLAN p, TT_VS_MONTHLY_PLAN_DETAIL detail\n");
		sql.append("                 where p.PLAN_ID = detail.PLAN_ID\n");
		sql.append("                    and p.IS_FLAG=0\n");
		sql.append("                   and p.PLAN_YEAR = " + year + "\n");
		sql.append("                   and p.PLAN_TYPE = " + planType + "\n");
		sql.append("                   and p.PLAN_MONTH = " + month + "\n");
		sql.append("                   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("                   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("				   and p.PLAN_VER =\n");
		sql.append("    				   (SELECT nvl(MAX(PLAN_VER), 0) plan_ver\n");
		sql.append("                          FROM TT_VS_MONTHLY_PLAN plan\n");
		sql.append("                         WHERE plan.PLAN_YEAR = p.PLAN_YEAR\n");
		sql.append("                           AND plan.PLAN_MONTH = p.PLAN_MONTH\n");
		sql.append("                           and plan.COMPANY_ID = p.COMPANY_ID\n");
		sql.append("                           and plan.AREA_ID = p.AREA_ID\n");
		sql.append("                           and plan.PLAN_TYPE = p.PLAN_TYPE\n");
		sql.append("                           and plan.IS_FLAG = p.IS_FLAG\n");
		sql.append("                           and plan.STATUS = p.STATUS\n");
		sql.append("                           and plan.ORG_TYPE = p.ORG_TYPE)) b");
		sql.append("          full outer join (select mplan.DEALER_ID,\n");
		sql.append("                                 detail.SALE_AMOUNT,\n");
		sql.append("                                 detail.MATERIAL_GROUPID,\n");
		sql.append("                                 mplan.AREA_ID\n");
		sql.append("                            from TT_VS_MONTHLY_PLAN        mplan,\n");
		sql.append("                                 TT_VS_MONTHLY_PLAN_DETAIL detail\n");
		sql.append("                           where mplan.PLAN_ID = detail.PLAN_ID\n");
		sql.append("                   and mplan.IS_FLAG=1\n");
		sql.append("                   and mplan.PLAN_TYPE = " + planType + "\n");
		sql.append("                   and mplan.PLAN_YEAR = " + year + "\n");
		sql.append("                   and mplan.PLAN_MONTH = " + month + "\n");
		sql.append("                   and mplan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
		sql.append("                   and mplan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		/** ******************************************************************************** */
		/*
		 * sql.append(" and mplan.dealer_id in\n" ); sql.append(" (select
		 * m.dealer_id\n" ); sql.append(" from tm_dealer m\n" ); sql.append("
		 * CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" ); sql.append("
		 * START WITH m.dealer_id in\n" ); sql.append(" (select rel.dealer_id\n" );
		 * sql.append(" from tm_dealer_org_relation rel\n" ); sql.append(" where
		 * rel.org_id = "+orgId+"))");
		 */
		/** ******************************************************************************* */
		sql.append("                             ) c on b.MATERIAL_GROUPID =\n");
		sql.append("                                                                       c.MATERIAL_GROUPID\n");
		sql.append("                                                                   and b.dealer_id =\n");
		sql.append("                                                                       c.DEALER_ID) d\n");
		sql.append(" where a.DEALER_ID = d.dealer_id(+)\n");
		sql.append(" group by a.dealer_id, a.dealer_code, a.DEALER_SHORTNAME\n");
		sql.append(" order by a.dealer_id");

		return dao.pageQuery(sql.toString(), params, getFunName() + areaId);
	}

	/*
	 * ��ѯ�����·��¶���������汾��
	 */
	public String selectMaxSubPlanVer(String year, String month, String areaId, String userId, String planType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select nvl(max(p.plan_ver), 0) plan_ver\n");
		sql.append("  from TT_VS_MONTHLY_PLAN p\n");
		sql.append(" where p.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and p.PLAN_TYPE = ?\n");
		params.add(planType);
		sql.append("   and p.IS_FLAG=1");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		/*
		 * sql.append(" and p.CREATE_BY =?\n"); params.add(userId);
		 */
		sql.append("   and p.AREA_ID = ?");
		params.add(areaId);

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		return map.get("PLAN_VER").toString();
	}

	/*
	 * ��ѯ�Ѵ���������
	 */
	public List<TtVsMonthlyPlanPO> selectMonthClrList(TtVsMonthlyPlanPO po) {
		return dao.select(po);
	}

	/*
	 * ��ѯ�Ѵ��ڵ���ϸ����
	 */
	public List<Map<String, Object>> selectMonthClrDetailList(String companyId, String areaId, String planYear, String planMonth, String orgType, String dealerId, String planType, int isFlag, String groupId, String status) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TVMP.PLAN_ID, TVMPD.DETAIL_ID\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN TVMP, TT_VS_MONTHLY_PLAN_DETAIL TVMPD\n");
		sql.append(" WHERE TVMP.PLAN_ID = TVMPD.PLAN_ID\n");
		sql.append("   AND TVMP.COMPANY_ID = " + Long.parseLong(companyId) + "\n");
		sql.append("   AND TVMP.AREA_ID = " + Long.parseLong(areaId) + "\n");
		sql.append("   AND TVMP.PLAN_YEAR = " + planYear + "\n");
		sql.append("   AND TVMP.Plan_Month = " + planMonth + "\n");
		sql.append("   AND TVMP.ORG_TYPE = " + orgType + "\n");
		if (status != null && !status.equals("")) {
			sql.append("   AND TVMP.STATUS = " + status + "\n");
		}
		if (dealerId != null && !dealerId.equals("")) {
			sql.append("   AND exists( " + dealerId + " and v.DEALER_ID=TVMP.dealer_id)\n");
		}
		sql.append("   AND TVMP.PLAN_TYPE = " + planType + "\n");
		sql.append("   AND TVMP.IS_FLAG = " + isFlag + "\n");
		if (groupId != null && !groupId.equals(""))
			sql.append("   AND TVMPD.MATERIAL_GROUPID = " + groupId + "\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	/*
	 * ��ѯ�¶��������汾��
	 */
	public String selectMaxPlanVer(String year, String month, String companyId, String dealerId, String chngType, String planType, String logonOrgType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select nvl(max(plan_ver),0) plan_ver \n");
		sql.append("  from TT_VS_MONTHLY_PLAN t\n");
		sql.append(" where t.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and t.PLAN_MONTH = ?\n");
		params.add(month);
		/*sql.append("   and t.COMPANY_ID = ?\n");
		params.add(companyId);*/
		// sql.append("   and t.AREA_ID = " + areaId + "\n");
		sql.append("   and exists(" + dealerId + " and v.DEALER_ID=t.dealer_id)\n");
		sql.append("   and t.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		/*if (null != planType && !"".equals(planType)) {
			sql.append("   and t.PLAN_TYPE = " + planType + "\n");
		}*/
		// ԭ���߼���
		/*
		 * if("2".equals(chngType)){ sql.append("and t.ORG_TYPE =
		 * "+Constant.ORG_TYPE_OEM+"\n"); sql.append("and t.DEALER_ID is null");
		 * }else{ sql.append("and t.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");
		 * sql.append("and t.DEALER_ID is not null"); }
		 */
		// ���ڳ�������
		if ("2".equals(logonOrgType)) {
			sql.append("  and t.IS_FLAG=0 ");
		} else {
			sql.append("  and t.IS_FLAG=0 ");
		}
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, "");

		return map.get("PLAN_VER").toString();
	}

	/*
	 * ��ѯ�¶��������汾��
	 */
	public String dealerSelectMaxPlanVer(String year, String month, String companyId, String dealerId, String areaId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select nvl(max(plan_ver),0) plan_ver \n");
		sql.append("  from TT_VS_MONTHLY_PLAN t\n");
		sql.append(" where t.PLAN_YEAR = ?\n");
		params.add(year);
		sql.append("   and t.PLAN_MONTH = ?\n");
		params.add(month);
		sql.append("   and t.AREA_ID = " + areaId + "\n");
		sql.append("   and t.COMPANY_ID = ?\n");
		params.add(companyId);
		sql.append("   and t.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("and t.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("and t.DEALER_ID in (" + PlanUtil.createSqlStr(dealerId) + ")\n");

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, "");

		return map.get("PLAN_VER").toString();
	}

	/*
	 * ��ѯ��������ϸ
	 */
	public PageResult<Map<String, Object>> oemSelectDealerMonthPlan(String dutyType, String org_id, Map<String, Object> map, int pageSize, int curPage) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerCode = (String) map.get("dealerCode");
		// String logonOrgType = (String) map.get("logonOrgType");
		// String logonOrgId = (String) map.get("logonOrgId");
		// String companyId = map.get("companyId").toString();
		String dealerId = (String) map.get("dealerId");
		//String planType = map.get("planType").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ORG.PQ_ORG_CODE AS ORG_CODE,\n");
		sql.append("       ORG.PQ_ORG_NAME as ORG_NAME,\n");
		sql.append("       ORG.ROOT_DEALER_CODE AS DEALER_CODE,\n");
		sql.append("       ORG.ROOT_DEALER_SHORTNAME AS DEALER_SHORTNAME,\n");
		sql.append("       null PLAN_TYPE,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT), 0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g,\n");
		sql.append("       vw_org_dealer_all_new             ORG\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = ORG.DEALER_ID\n");
		sql.append("   and exists(" + dealerId + " and v.DEALER_ID=p.dealer_id)\n");
		// sql.append("   and p.AREA_ID = " + areaId + "\n");
		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
		// sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(dutyType))) {
			sql.append("   AND ORG.COMPANY_ID = " + org_id + "\n");
		}
		if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(dutyType))) {
			sql.append("   AND ORG.ROOT_ORG_ID = " + org_id + "\n");
		}
		if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(dutyType))) {
			sql.append("   AND ORG.PQ_ORG_ID = " + org_id + "\n");
		}
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("   and p.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		//sql.append("   and p.IS_FLAG = 0 \n");
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		/*if (null != planType && !"".equals(planType)) {
			sql.append("   and p.plan_Type = " + planType + "\n");
		}*/
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("           and ORG.ROOT_dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
			// params.add(dealerCode);
		}
		sql.append("group by ORG.PQ_ORG_CODE,\n");
		sql.append("         ORG.PQ_ORG_NAME,\n");
		sql.append("         ORG.ROOT_DEALER_CODE,\n");
		sql.append("         ORG.ROOT_DEALER_SHORTNAME,\n");
		// sql.append("         p.PLAN_TYPE,\n");
		sql.append("         d.MATERIAL_GROUPID,\n");
		sql.append("         g.GROUP_NAME,\n");
		sql.append("         g.GROUP_CODE\n");
		sql.append("order by ORG.ROOT_DEALER_SHORTNAME,ORG.ROOT_DEALER_CODE\n");
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}

	/*
	 * ��ѯ��������ϸ
	 */
	public PageResult<Map<String, Object>> oemSelectOrgMonthPlan(Map<String, Object> map, int pageSize, int curPage) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String orgCode = (String) map.get("orgCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();
		String planType = map.get("planType").toString();

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		// ԭ���߼�
		/*
		 * sql.append("select org.ORG_CODE,\n"); sql.append(" org.ORG_NAME,\n");
		 * sql.append(" d.MATERIAL_GROUPID,\n"); sql.append(" g.GROUP_NAME,\n");
		 * sql.append(" g.GROUP_CODE,\n"); sql.append("
		 * nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n"); sql.append(" from
		 * TT_VS_MONTHLY_PLAN p,\n"); sql.append(" TT_VS_MONTHLY_PLAN_DETAIL
		 * d,\n"); sql.append(" TM_ORG org,\n"); sql.append("
		 * TM_VHCL_MATERIAL_GROUP g\n"); sql.append(" where p.PLAN_ID =
		 * d.PLAN_ID\n"); sql.append(" and p.ORG_ID = org.ORG_ID\n");
		 * sql.append(" and p.COMPANY_ID="+companyId+"\n"); sql.append(" and
		 * p.AREA_ID = "+areaId+"\n"); sql.append(" and d.MATERIAL_GROUPID =
		 * g.GROUP_ID");
		 * 
		 * sql.append(" and p.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");
		 * sql.append(" and p.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");
		 * if(null!=plan_ver&&!"".equals(plan_ver)){ sql.append(" and p.PLAN_VER =
		 * ?\n"); params.add(plan_ver); } sql.append(" and p.PLAN_YEAR = ?\n");
		 * params.add(plan_year); sql.append(" and p.PLAN_MONTH = ?\n");
		 * params.add(plan_month);
		 * if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
		 * sql.append(" and p.ORG_ID=?\n"); params.add(logonOrgId); }
		 * if(null!=plan_desc&&!"".equals(plan_desc)){ sql.append(" and
		 * p.PLAN_DESC like ?\n"); params.add(plan_desc); }
		 * if(null!=orgCode&&!"".equals(orgCode)){ sql.append(" and org.org_code
		 * in ("+PlanUtil.createSqlStr(orgCode)+")\n");
		 * //params.add(dealerCode); } sql.append("group by org.ORG_CODE,\n");
		 * sql.append(" org.ORG_NAME,\n"); sql.append(" d.MATERIAL_GROUPID,\n");
		 * sql.append(" g.GROUP_NAME,\n"); sql.append(" g.GROUP_CODE\n");
		 * sql.append(" order by org.ORG_CODE");
		 */

		// ��������
		sql.append("select tmo.ORG_CODE,\n");
		sql.append("       tmo.ORG_NAME,\n");
		sql.append("       td.DEALER_CODE,\n");
		sql.append("       td.DEALER_SHORTNAME,\n");
		sql.append("       p.PLAN_TYPE,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT), 0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_DEALER                 td,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g,\n");
		sql.append("       TM_ORG                    tmo,\n");
		sql.append("       TM_DEALER_ORG_RELATION    tdor\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = td.DEALER_ID\n");
		sql.append("   and td.DEALER_ID = tdor.DEALER_ID\n");
		sql.append("   and tdor.ORG_ID = tmo.ORG_ID\n");
		sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		sql.append("   and p.AREA_ID = " + areaId + "\n");

		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID");
		// ������������������
		if (null != planType && !"".equals(planType)) {
			sql.append("   and p.plan_Type = " + planType + "\n");
		}
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("   and p.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("          and p.is_flag=1\n");
			sql.append("          and p.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from vw_org_dealer rel\n");
			sql.append("                      where rel.root_org_id = " + logonOrgId + "))");
		} else {
			sql.append("          and p.is_flag=0\n");
		}
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (null != orgCode && !"".equals(orgCode)) {
			// sql.append(" and org.org_code in
			// ("+PlanUtil.createSqlStr(orgCode)+")\n");

			sql.append("          and p.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel,tm_org org\n");
			sql.append("                      where rel.org_id=org.org_id and org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")))");

			/*
			 * sql.append(" AND p.DEALER_ID IN (SELECT D.DEALER_ID FROM
			 * TM_DEALER D,TM_ORG O,TM_DEALER_ORG_RELATION R\n"); sql.append("
			 * WHERE D.DEALER_ID = R.DEALER_ID\n"); sql.append(" AND O.ORG_ID =
			 * R.ORG_ID\n"); sql.append(" AND D.STATUS = 10011001\n");
			 * sql.append(" AND O.STATUS = 10011001\n"); sql.append(" AND
			 * O.ORG_CODE IN ("+PlanUtil.createSqlStr(orgCode)+"))\n");
			 */

			// params.add(dealerCode);
		}
		sql.append("group by tmo.ORG_CODE,\n");
		sql.append("         tmo.ORG_NAME,\n");
		sql.append("         td.DEALER_CODE,\n");
		sql.append("         td.DEALER_SHORTNAME,\n");
		sql.append("         p.PLAN_TYPE,\n");
		sql.append("         d.MATERIAL_GROUPID,\n");
		sql.append("         g.GROUP_NAME,\n");
		sql.append("         g.GROUP_CODE\n");
		sql.append("order by td.DEALER_SHORTNAME,td.DEALER_CODE\n");

		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}

	
	public PageResult<Map<String, Object>> oemSelectDealerMonthPlanTotal(String dutyType, String org_id, Map<String, Object> map, List<Map<String, Object>> serlist, int pageSize, int curPage) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerCode = (String) map.get("dealerCode");
		//String logonOrgType = (String) map.get("logonOrgType");
		// String logonOrgId = (String) map.get("logonOrgId");
		// String companyId = map.get("companyId").toString();
		String dealerId = map.get("dealerId").toString();
		// String planType = map.get("planType").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n");
		sql.append("SELECT YEARLY.DEALER_CODE,\n");
		sql.append("       YEARLY.DEALER_SHORTNAME,\n");
		sql.append("       null PLAN_TYPE,\n");
		for (int i = 0; i < serlist.size(); i++) {
			Map<String, Object> sermap = serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
			sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
			sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
		}
		sql.append("       nvl(sum(yearly.SALE_AMOUNT),0) SALE_AMOUNT,\n");
		sql.append("       YEARLY.DEALER_ID\n");
		sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
		sql.append("               D.ROOT_DEALER_CODE AS DEALER_CODE,\n");
		sql.append("               D.ROOT_DEALER_SHORTNAME AS DEALER_SHORTNAME,\n");
		// sql.append("               TVYP.PLAN_TYPE,\n");
		sql.append("               TVYPD.MATERIAL_GROUPID,\n");
		sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
		sql.append("          FROM TT_VS_MONTHLY_PLAN        TVYP,\n");
		sql.append("               TT_VS_MONTHLY_PLAN_DETAIL TVYPD,\n");
		sql.append("      		   vw_org_dealer_all_new             D\n");
		sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
		sql.append("           AND TVYP.DEALER_ID = D.DEALER_ID\n");
		sql.append("      and exists(" + dealerId + " and v.DEALER_ID=TVYP.dealer_id)\n");
		// sql.append("           AND TVYP.AREA_ID=" + areaId + "\n");
		// sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
		sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(dutyType))) {
			sql.append("   AND D.COMPANY_ID = " + org_id + "\n");
		}
		if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(dutyType))) {
			sql.append("   AND D.ROOT_ORG_ID = " + org_id + "\n");
		}
		if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(dutyType))) {
			sql.append("   AND D.PQ_ORG_ID = " + org_id + "\n");
		}
		//sql.append("           AND TVYP.IS_FLAG=0 \n");
		sql.append("           and TVYP.plan_year = " + plan_year + "\n");
		// params.add(plan_year);
		sql.append("           and TVYP.plan_month = " + plan_month + "\n");
		// params.add(plan_month);
		sql.append("           and TVYP.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		// params.add(Constant.PLAN_MANAGE_CONFIRM);
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("           and TVYP.PLAN_VER = " + plan_ver + "\n");
			params.add(plan_ver);
		}
		/*if (null != planType && !"".equals(planType)) {
			sql.append("           and TVYP.PLAN_TYPE = " + planType + "\n");
			// params.add(planType);
		}*/
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and TVYP.PLAN_DESC like " + plan_desc + "\n");
			// params.add(plan_desc);
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("           and D.ROOT_dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
			// params.add(dealerCode);
		}
		sql.append("         GROUP BY TVYP.DEALER_ID,\n");
		sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
		sql.append("                  D.ROOT_DEALER_CODE,\n");
		sql.append("                  D.ROOT_DEALER_SHORTNAME\n");
		sql.append("                  ) YEARLY\n");
		sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
		sql.append(" order by dealer_id");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}

	
	public PageResult<Map<String, Object>> oemSelectOrgMonthPlanTotal(String dutyType, String org_id, Map<String, Object> map, List<Map<String, Object>> serlist, int pageSize, int curPage) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String orgCode = (String) map.get("orgCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();

		String planType = map.get("planType").toString();

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		// ԭ���߼�
		/*
		 * sql.append("\n"); sql.append("SELECT YEARLY.ORG_CODE,\n");
		 * sql.append(" YEARLY.ORG_NAME,\n"); for(int i=0;i<serlist.size();i++){
		 * Map<String, Object> sermap=serlist.get(i); sql.append("
		 * nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n"); sql.append("
		 * "+sermap.get("GROUP_ID")+",\n"); sql.append(" YEARLY.SALE_AMOUNT)),0)
		 * S"+i+",\n"); } sql.append(" nvl(sum(yearly.SALE_AMOUNT),0)
		 * SALE_AMOUNT,\n"); sql.append(" YEARLY.ORG_ID\n"); sql.append(" FROM
		 * (SELECT TVYP.ORG_ID,\n"); sql.append(" ORG.ORG_CODE,\n");
		 * sql.append(" ORG.ORG_NAME,\n"); sql.append("
		 * TVYPD.MATERIAL_GROUPID,\n"); sql.append("
		 * nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n"); sql.append(" FROM
		 * TT_VS_MONTHLY_PLAN TVYP,\n"); sql.append(" TT_VS_MONTHLY_PLAN_DETAIL
		 * TVYPD,\n"); sql.append(" TM_ORG ORG\n"); sql.append(" WHERE
		 * TVYP.PLAN_ID = TVYPD.PLAN_ID\n"); sql.append(" AND TVYP.ORG_ID =
		 * ORG.ORG_ID\n"); sql.append(" AND TVYP.COMPANY_ID="+companyId+"\n");
		 * sql.append(" AND TVYP.AREA_ID="+areaId+"\n"); sql.append(" AND
		 * TVYP.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");
		 * if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
		 * sql.append(" and TVYP.ORG_ID=?\n"); params.add(logonOrgId); }
		 * sql.append(" and TVYP.plan_year = ?\n"); params.add(plan_year);
		 * sql.append(" and TVYP.plan_month = ?\n"); params.add(plan_month);
		 * sql.append(" and TVYP.STATUS = ?\n");
		 * params.add(Constant.PLAN_MANAGE_CONFIRM);
		 * if(null!=plan_ver&&!"".equals(plan_ver)){ sql.append(" and
		 * TVYP.PLAN_VER = ?\n"); params.add(plan_ver); }
		 * if(null!=plan_desc&&!"".equals(plan_desc)){ sql.append(" and
		 * TVYP.PLAN_DESC like ?\n"); params.add(plan_desc); }
		 * if(null!=orgCode&&!"".equals(orgCode)){ sql.append(" and ORG.ORG_CODE
		 * in ("+PlanUtil.createSqlStr(orgCode)+")\n");
		 * //params.add(dealerCode); }
		 * 
		 * sql.append(" GROUP BY TVYP.ORG_ID,\n"); sql.append("
		 * TVYPD.MATERIAL_GROUPID,\n"); sql.append(" ORG.ORG_CODE,\n");
		 * sql.append(" ORG.ORG_NAME) YEARLY\n"); sql.append(" GROUP BY ORG_ID,
		 * YEARLY.ORG_CODE, YEARLY.ORG_NAME\n"); sql.append(" order by ORG_id");
		 */

		sql.append("SELECT YEARLY.ORG_ID,\n");
		sql.append("       YEARLY.ORG_CODE,\n");
		sql.append("       YEARLY.ORG_NAME,\n");
		sql.append("       YEARLY.PLAN_TYPE,\n");
		for (int i = 0; i < serlist.size(); i++) {
			Map<String, Object> sermap = serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
			sql.append("                      " + sermap.get("GROUP_ID") + ",\n");
			sql.append("                      YEARLY.SALE_AMOUNT)),\n");
			sql.append("           0) S" + i + ",\n");
		}
		sql.append("       nvl(sum(yearly.SALE_AMOUNT), 0) SALE_AMOUNT\n");
		sql.append("  FROM (SELECT TOR.ORG_ID,\n");
		sql.append("               TOR.ORG_CODE,\n");
		sql.append("               TOR.ORG_NAME,\n");
		sql.append("               TVYP.PLAN_TYPE,\n");
		sql.append("               TVYPD.MATERIAL_GROUPID,\n");
		sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT), 0) AS SALE_AMOUNT\n");
		sql.append("          FROM TT_VS_MONTHLY_PLAN        TVYP,\n");
		sql.append("               TT_VS_MONTHLY_PLAN_DETAIL TVYPD,\n");
		sql.append("               TM_DEALER_ORG_RELATION    TDOR,\n");
		sql.append("               TM_ORG                    TOR,\n");
		sql.append("               TM_DEALER                 TD\n");
		sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
		sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND (TD.DEALER_ID = TDOR.DEALER_ID OR\n");
		sql.append("               TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
		sql.append("           AND TDOR.ORG_ID = TOR.ORG_ID\n");
		sql.append("           AND TVYP.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVYP.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("          and TVYP.IS_FLAG =0 \n");
			sql.append("          and TVYP.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from vw_org_dealer rel\n");
			sql.append("                      where rel.root_org_id = " + logonOrgId + "))");
		} else {
			sql.append("   and TVYP.IS_FLAG = 0 \n");
		}
		sql.append("           and TVYP.plan_year = ?\n");
		params.add(plan_year);
		sql.append("           and TVYP.plan_month = ?\n");
		params.add(plan_month);
		sql.append("           and TVYP.STATUS = ?\n");
		params.add(Constant.PLAN_MANAGE_CONFIRM);
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("           and TVYP.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		if (null != planType && !"".equals(planType)) {
			sql.append("           and TVYP.PLAN_TYPE = ?\n");
			params.add(planType);
		}
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and TVYP.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (null != orgCode && !"".equals(orgCode)) {
			sql.append("          and TVYP.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel,tm_org org\n");
			sql.append("                      where rel.org_id=org.org_id and  org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")))");
		}
		sql.append("         GROUP BY TVYPD.MATERIAL_GROUPID,\n");
		sql.append("                  TVYP.PLAN_TYPE,\n");
		sql.append("                  TOR.ORG_ID,\n");
		sql.append("                  TOR.ORG_CODE,\n");
		sql.append("                  TOR.ORG_NAME) YEARLY\n");
		sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME, YEARLY.PLAN_TYPE\n");
		sql.append(" order by YEARLY.ORG_ID");

		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}

	/*
	 * ------------------------------------------------����------------------------------------------------------
	 */
	// ��������ϸ
	public List<Map<String, Object>> oemSelectDealerDetailMonthPlanDown(Map<String, Object> map) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerCode = (String) map.get("dealerCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		// String companyId = map.get("companyId").toString();
		String dealerId = map.get("dealerId").toString();
		// String planType = map.get("planType").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select td.DEALER_CODE,\n");
		sql.append("       td.DEALER_SHORTNAME,\n");
		sql.append("       ORG.ORG_CODE,\n");
		sql.append("       ORG.ORG_NAME,\n");
		sql.append("       null PLAN_TYPE,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_DEALER                 td,\n");
		// sql.append("       TC_CODE                 TC,\n");
		sql.append("       TM_ORG ORG,\n");
		sql.append("	      vw_org_dealer_all_new          DORG,\n");
		sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = td.DEALER_ID\n");
		// sql.append("   and p.PLAN_TYPE = TC.CODE_ID\n");
		sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   and DORG.DEALER_ID = TD.DEALER_ID\n");
		if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND DORG.COMPANY_ID = " + map.get("org_id") + "\n");
		}
		if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND DORG.ROOT_ORG_ID = " + map.get("org_id") + "\n");
		}
		if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND DORG.PQ_ORG_ID = " + map.get("org_id") + "\n");
		}
		sql.append("   and ORG.ORG_ID = TDOR.ORG_ID\n");
		sql.append("   and exists(" + dealerId + " and v.DEALER_ID=p.dealer_id)\n");
		// sql.append("   and p.AREA_ID = " + areaId + "\n");
		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
		// sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("   and p.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("         and p.IS_FLAG = 0 \n");
			sql.append("          and p.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from vw_org_dealer rel\n");
			sql.append("                      where rel.root_org_id = " + logonOrgId + "))");
		} else {
			sql.append("   and p.IS_FLAG = 0 \n");
		}
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		/*if (null != planType && !"".equals(planType)) {
			sql.append("   and p.plan_Type = " + planType + "\n");
		}*/
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
			// params.add(dealerCode);
		}
		sql.append(" group by td.DEALER_CODE,\n");
		sql.append("          td.DEALER_SHORTNAME,\n");
		sql.append("          d.MATERIAL_GROUPID,\n");
		sql.append("          g.GROUP_NAME,ORG.ORG_CODE,ORG.ORG_NAME,\n");
		sql.append("          g.GROUP_CODE\n");
		sql.append("   order by td.DEALER_CODE");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	
	public List<Map<String, Object>> oemSelectOrgDetailMonthPlanDown(Map<String, Object> map) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String orgCode = (String) map.get("orgCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		String companyId = map.get("companyId").toString();
		String dealerId = map.get("dealerId").toString();
		// String planType = map.get("planType").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select TD.DEALER_CODE,\n");
		sql.append("       TD.DEALER_SHORTNAME,\n");
		sql.append("       ORG.ORG_CODE,\n");
		sql.append("       ORG.ORG_NAME,\n");
		sql.append("       TC.CODE_DESC PLAN_TYPE,\n");
		sql.append("       p.DEALER_ID,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_DEALER TD,\n");
		//sql.append("       TC_CODE TC,\n");
		sql.append("       TM_ORG ORG,\n");
		sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = TD.DEALER_ID\n");
	//	sql.append("   and p.PLAN_TYPE = TC.CODE_ID\n");
		sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   and ORG.ORG_ID = TDOR.ORG_ID\n");
		sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		//sql.append("   and p.AREA_ID = " + areaId + "\n");
		sql.append("          and exists(" + dealerId + " and v.DEALER_ID=p.dealer_id) \n");
		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID");
		/*if (null != planType && !"".equals(planType)) {
			sql.append("   and p.plan_Type = " + planType + "\n");
		}*/
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("   and p.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("          and p.is_flag=1\n");
			sql.append("          and p.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel\n");
			sql.append("                      where rel.org_id = " + logonOrgId + "))");
		} else {
			sql.append("          and p.is_flag=0\n");
		}
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (null != orgCode && !"".equals(orgCode)) {
			// sql.append(" and org.org_code in
			// ("+PlanUtil.createSqlStr(orgCode)+")\n");

			sql.append("          and p.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel,tm_org org\n");
			sql.append("                      where rel.org_id=org.org_id and org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")))");
		}
		sql.append("group by TD.DEALER_CODE,\n");
		sql.append("          TD.DEALER_SHORTNAME,\n");
		// sql.append("          TC.CODE_DESC,\n");
		sql.append("          p.DEALER_ID,\n");
		sql.append("          d.MATERIAL_GROUPID,\n");
		sql.append("          g.GROUP_NAME,ORG.ORG_CODE,ORG.ORG_NAME,\n");
		sql.append("          g.GROUP_CODE\n");
		sql.append("   order by p.DEALER_ID ");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	
	public List<Map<String, Object>> oemSelectDealerTotalMonthPlanTotalDown(Map<String, Object> map, List<Map<String, Object>> serlist) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerCode = (String) map.get("dealerCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		// String companyId = map.get("companyId").toString();
		String dealerId = map.get("dealerId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		// String planType = map.get("planType").toString();// ��������
		sql.append("\n");
		sql.append("SELECT YEARLY.DEALER_CODE,\n");
		sql.append("       YEARLY.DEALER_SHORTNAME,\n");
		sql.append("       null PLAN_TYPE,\n");
		for (int i = 0; i < serlist.size(); i++) {
			Map<String, Object> sermap = serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
			sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
			sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
		}
		sql.append("       nvl(sum(yearly.SALE_AMOUNT),0) SALE_AMOUNT,\n");
		sql.append("       YEARLY.DEALER_ID\n");
		sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.DEALER_SHORTNAME,\n");
		// sql.append("               TC.CODE_DESC PLAN_TYPE,\n");
		sql.append("               TVYPD.MATERIAL_GROUPID,\n");
		sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
		sql.append("          FROM TT_VS_MONTHLY_PLAN        TVYP,\n");
		sql.append("               TT_VS_MONTHLY_PLAN_DETAIL TVYPD,\n");
		sql.append("               TM_DEALER                TD,\n");
		//sql.append("               TC_CODE                TC,\n");
		sql.append("			   vw_org_dealer_all_new          D\n");
		sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
		sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
		sql.append("		   AND TD.DEALER_ID = D.DEALER_ID");
		if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND D.COMPANY_ID = " + map.get("org_id") + "\n");
		}
		if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND D.ROOT_ORG_ID = " + map.get("org_id") + "\n");
		}
		if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(map.get("dutyType").toString()))) {
			sql.append("   AND D.PQ_ORG_ID = " + map.get("org_id") + "\n");
		}
		// sql.append("           AND TVYP.PLAN_TYPE=TC.CODE_ID\n");
		sql.append("    and exists(" + dealerId + " and v.DEALER_ID=TVYP.dealer_id)\n");
		// sql.append("           AND TVYP.AREA_ID=" + areaId + "\n");
		// sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
		sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("           AND TVYP.IS_FLAG=0 \n");
			sql.append("          and TVYP.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from vw_org_dealer_all_new rel\n");
			sql.append("                      where rel.root_org_id =" + logonOrgId + "))");
		} else {
			sql.append("           AND TVYP.IS_FLAG=0 \n");
		}
		sql.append("           and TVYP.plan_year = ?\n");
		params.add(plan_year);
		sql.append("           and TVYP.plan_month = ?\n");
		params.add(plan_month);
		sql.append("           and TVYP.STATUS = ?\n");
		params.add(Constant.PLAN_MANAGE_CONFIRM);
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("           and TVYP.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		// ������������������
		/*if (null != planType && !"".equals(planType)) {
			sql.append("           and TVYP.PLAN_TYPE = ?\n");
			params.add(planType);
		}*/
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and TVYP.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
			// params.add(dealerCode);
		}
		sql.append("         GROUP BY TVYP.DEALER_ID,\n");
		sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  TD.DEALER_SHORTNAME\n");
		sql.append("                  ) YEARLY\n");
		sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
		sql.append(" order by dealer_id");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ��֯����
	 */
	public List<Map<String, Object>> oemSelectOrgTotalMonthPlanTotalDown(Map<String, Object> map, List<Map<String, Object>> serlist) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String orgCode = (String) map.get("orgCode");
		String logonOrgType = (String) map.get("logonOrgType");
		String logonOrgId = (String) map.get("logonOrgId");
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();

		String planType = map.get("planType").toString();// ��������
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		/*
		 * sql.append("\n"); sql.append("SELECT YEARLY.ORG_CODE,\n");
		 * sql.append(" YEARLY.ORG_NAME,\n"); for(int i=0;i<serlist.size();i++){
		 * Map<String, Object> sermap=serlist.get(i); sql.append("
		 * nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n"); sql.append("
		 * "+sermap.get("GROUP_ID")+",\n"); sql.append(" YEARLY.SALE_AMOUNT)),0)
		 * S"+i+",\n"); } sql.append(" sum(yearly.SALE_AMOUNT) SALE_AMOUNT,\n");
		 * sql.append(" YEARLY.ORG_ID\n"); sql.append(" FROM (SELECT
		 * TVYP.ORG_ID,\n"); sql.append(" ORG.ORG_CODE,\n"); sql.append("
		 * ORG.ORG_NAME,\n"); sql.append(" TVYPD.MATERIAL_GROUPID,\n");
		 * sql.append(" nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
		 * sql.append(" FROM TT_VS_MONTHLY_PLAN TVYP,\n"); sql.append("
		 * TT_VS_MONTHLY_PLAN_DETAIL TVYPD,\n"); sql.append(" TM_ORG ORG\n");
		 * sql.append(" WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n"); sql.append(" AND
		 * TVYP.ORG_ID = ORG.ORG_ID\n"); sql.append(" AND
		 * TVYP.COMPANY_ID="+companyId+"\n"); sql.append(" AND
		 * TVYP.AREA_ID="+areaId+"\n"); sql.append(" AND TVYP.ORG_TYPE =
		 * "+Constant.ORG_TYPE_OEM+"\n");
		 * if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
		 * sql.append(" and TVYP.ORG_ID=?\n"); params.add(logonOrgId); }
		 * sql.append(" and TVYP.plan_year = ?\n"); params.add(plan_year);
		 * sql.append(" and TVYP.plan_month = ?\n"); params.add(plan_month);
		 * sql.append(" and TVYP.STATUS = ?\n");
		 * params.add(Constant.PLAN_MANAGE_CONFIRM);
		 * if(null!=plan_ver&&!"".equals(plan_ver)){ sql.append(" and
		 * TVYP.PLAN_VER = ?\n"); params.add(plan_ver); }
		 * if(null!=plan_desc&&!"".equals(plan_desc)){ sql.append(" and
		 * TVYP.PLAN_DESC like ?\n"); params.add(plan_desc); }
		 * if(null!=orgCode&&!"".equals(orgCode)){ sql.append(" and ORG.ORG_CODE
		 * in ("+PlanUtil.createSqlStr(orgCode)+")\n");
		 * //params.add(dealerCode); }
		 * 
		 * sql.append(" GROUP BY TVYP.ORG_ID,\n"); sql.append("
		 * TVYPD.MATERIAL_GROUPID,\n"); sql.append(" ORG.ORG_CODE,\n");
		 * sql.append(" ORG.ORG_NAME) YEARLY\n"); sql.append(" GROUP BY ORG_ID,
		 * YEARLY.ORG_CODE, YEARLY.ORG_NAME\n"); sql.append(" order by ORG_id");
		 */

		sql.append("SELECT YEARLY.ORG_ID,\n");
		sql.append("       YEARLY.ORG_CODE,\n");
		sql.append("       YEARLY.ORG_NAME,\n");
		sql.append("       YEARLY.PLAN_TYPE,\n");
		for (int i = 0; i < serlist.size(); i++) {
			Map<String, Object> sermap = serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
			sql.append("                      " + sermap.get("GROUP_ID") + ",\n");
			sql.append("                      YEARLY.SALE_AMOUNT)),\n");
			sql.append("           0) S" + i + ",\n");
		}
		sql.append("       nvl(sum(yearly.SALE_AMOUNT), 0) SALE_AMOUNT\n");
		sql.append("  FROM (SELECT TOR.ORG_ID,\n");
		sql.append("               TOR.ORG_CODE,\n");
		sql.append("               TOR.ORG_NAME,\n");
		sql.append("               TVYP.PLAN_TYPE,\n");
		sql.append("               TVYPD.MATERIAL_GROUPID,\n");
		sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT), 0) AS SALE_AMOUNT\n");
		sql.append("          FROM TT_VS_MONTHLY_PLAN        TVYP,\n");
		sql.append("               TT_VS_MONTHLY_PLAN_DETAIL TVYPD,\n");
		sql.append("               TM_DEALER_ORG_RELATION    TDOR,\n");
		sql.append("               TM_ORG                    TOR,\n");
		sql.append("               TM_DEALER                 TD\n");
		sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
		sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND (TD.DEALER_ID = TDOR.DEALER_ID OR\n");
		sql.append("               TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
		sql.append("           AND TDOR.ORG_ID = TOR.ORG_ID\n");
		sql.append("           AND TVYP.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVYP.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		if (null != logonOrgId && null != logonOrgType && !"".equals(logonOrgId) && "LARGEREGION".equals(logonOrgType)) {
			sql.append("          and TVYP.IS_FLAG =1 \n");
			sql.append("          and TVYP.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel\n");
			sql.append("                      where rel.org_id = " + logonOrgId + "))");
		} else {
			sql.append("   and TVYP.IS_FLAG = 0 \n");
		}
		sql.append("           and TVYP.plan_year = ?\n");
		params.add(plan_year);
		sql.append("           and TVYP.plan_month = ?\n");
		params.add(plan_month);
		sql.append("           and TVYP.STATUS = ?\n");
		params.add(Constant.PLAN_MANAGE_CONFIRM);
		if (null != plan_ver && !"".equals(plan_ver)) {
			sql.append("           and TVYP.PLAN_VER = ?\n");
			params.add(plan_ver);
		}
		if (null != planType && !"".equals(planType)) {
			sql.append("           and TVYP.PLAN_TYPE = ?\n");
			params.add(planType);
		}
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and TVYP.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (null != orgCode && !"".equals(orgCode)) {
			sql.append("          and TVYP.dealer_id in\n");
			sql.append("       (select m.dealer_id\n");
			sql.append("          from tm_dealer m\n");
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
			sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
			sql.append("                    (select rel.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation rel,tm_org org\n");
			sql.append("                      where rel.org_id=org.org_id and  org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")))");
		}
		sql.append("         GROUP BY TVYPD.MATERIAL_GROUPID,\n");
		sql.append("                  TVYP.PLAN_TYPE,\n");
		sql.append("                  TOR.ORG_ID,\n");
		sql.append("                  TOR.ORG_CODE,\n");
		sql.append("                  TOR.ORG_NAME) YEARLY\n");
		sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME, YEARLY.PLAN_TYPE\n");
		sql.append(" order by YEARLY.ORG_ID");

		// ��������
		/*
		 * sql.append("\n"); sql.append("SELECT YEARLY.DEALER_CODE,\n");
		 * sql.append(" YEARLY.DEALER_SHORTNAME,\n"); sql.append("
		 * YEARLY.PLAN_TYPE,\n"); for(int i=0;i<serlist.size();i++){ Map<String,
		 * Object> sermap=serlist.get(i); sql.append("
		 * nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n"); sql.append("
		 * "+sermap.get("GROUP_ID")+",\n"); sql.append(" YEARLY.SALE_AMOUNT)),0)
		 * S"+i+",\n"); } sql.append(" nvl(sum(yearly.SALE_AMOUNT),0)
		 * SALE_AMOUNT \n"); sql.append(" FROM (SELECT TVYP.DEALER_ID,\n");
		 * sql.append(" TD.DEALER_CODE,\n"); sql.append("
		 * TD.DEALER_SHORTNAME,\n"); sql.append(" TC.CODE_DESC PLAN_TYPE,\n");
		 * sql.append(" TVYPD.MATERIAL_GROUPID,\n"); sql.append("
		 * nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n"); sql.append(" FROM
		 * TT_VS_MONTHLY_PLAN TVYP,\n"); sql.append(" TT_VS_MONTHLY_PLAN_DETAIL
		 * TVYPD,\n"); sql.append(" TM_DEALER TD,\n"); sql.append(" TC_CODE
		 * TC\n"); sql.append(" WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
		 * sql.append(" AND TVYP.DEALER_ID = TD.DEALER_ID\n"); sql.append(" AND
		 * TVYP.PLAN_TYPE = TC.CODE_ID\n"); sql.append(" AND
		 * TVYP.COMPANY_ID="+companyId+"\n"); sql.append(" AND
		 * TVYP.AREA_ID="+areaId+"\n"); sql.append(" AND TVYP.ORG_TYPE =
		 * "+Constant.ORG_TYPE_DEALER+"\n");
		 * if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
		 * sql.append(" and TVYP.IS_FLAG =1 \n"); sql.append(" and
		 * TVYP.dealer_id in\n" ); sql.append(" (select m.dealer_id\n" );
		 * sql.append(" from tm_dealer m\n" ); sql.append(" CONNECT BY PRIOR
		 * m.dealer_id = m.parent_dealer_d\n" ); sql.append(" START WITH
		 * m.dealer_id in\n" ); sql.append(" (select rel.dealer_id\n" );
		 * sql.append(" from tm_dealer_org_relation rel\n" ); sql.append(" where
		 * rel.org_id = "+logonOrgId+"))"); }else{ sql.append(" and TVYP.IS_FLAG =
		 * 0 \n"); } sql.append(" and TVYP.plan_year = ?\n");
		 * params.add(plan_year); sql.append(" and TVYP.plan_month = ?\n");
		 * params.add(plan_month); sql.append(" and TVYP.STATUS = ?\n");
		 * params.add(Constant.PLAN_MANAGE_CONFIRM);
		 * if(null!=plan_ver&&!"".equals(plan_ver)){ sql.append(" and
		 * TVYP.PLAN_VER = ?\n"); params.add(plan_ver); }
		 * if(null!=planType&&!"".equals(planType)){ sql.append(" and
		 * TVYP.PLAN_TYPE = ?\n"); params.add(planType); }
		 * if(null!=plan_desc&&!"".equals(plan_desc)){ sql.append(" and
		 * TVYP.PLAN_DESC like ?\n"); params.add(plan_desc); }
		 * if(null!=orgCode&&!"".equals(orgCode)){ //sql.append(" and
		 * ORG.ORG_CODE in ("+PlanUtil.createSqlStr(orgCode)+")\n");
		 * //params.add(dealerCode);
		 * 
		 * sql.append(" and tvyp.dealer_id in\n" ); sql.append(" (select
		 * m.dealer_id\n" ); sql.append(" from tm_dealer m\n" ); sql.append("
		 * CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" ); sql.append("
		 * START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
		 * sql.append(" (select rel.dealer_id\n" ); sql.append(" from
		 * tm_dealer_org_relation rel,tm_org org\n" ); sql.append(" where
		 * rel.org_id=org.org_id and org.org_code in
		 * ("+PlanUtil.createSqlStr(orgCode)+")))"); }
		 * 
		 * sql.append(" GROUP BY \n"); sql.append(" TVYPD.MATERIAL_GROUPID,\n");
		 * sql.append(" TVYP.DEALER_ID,\n"); sql.append(" TC.CODE_DESC,\n");
		 * sql.append(" TD.DEALER_CODE,\n"); sql.append(" TD.DEALER_SHORTNAME)
		 * YEARLY\n"); sql.append(" GROUP BY
		 * YEARLY.DEALER_ID,YEARLY.DEALER_CODE,
		 * YEARLY.DEALER_SHORTNAME,YEARLY.PLAN_TYPE\n"); sql.append(" order by
		 * DEALER_ID");
		 */

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * ��ѯ��������ϸ
	 */
	public PageResult<Map<String, Object>> dealerSelectMonthPlan(Map<String, Object> map, int pageSize, int curPage) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerId = map.get("dealerId").toString();
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();
		String task_type = (String) map.get("task_type");

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select td.DEALER_CODE,\n");
		sql.append("       td.DEALER_SHORTNAME,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       tc.CODE_DESC PLAN_TYPE_DESC,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_DEALER                 td,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g,\n");
		sql.append("       TC_CODE    tc\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = td.DEALER_ID\n");
		sql.append("   and p.PLAN_TYPE = tc.CODE_ID\n");
		sql.append("   and tc.CODE_ID = " + Constant.PLAN_TYPE_BUY + "\n");

		sql.append("   and p.IS_FLAG = 0\n");
		sql.append("   and p.AREA_ID=" + areaId + "\n");
		sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		sql.append("   and p.DEALER_ID in (" + PlanUtil.createSqlStr(dealerId) + ")\n");
		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		// if(null!=plan_ver&&!"".equals(plan_ver)){
		sql.append("   and p.PLAN_VER = ?\n");
		params.add(plan_ver);
		// }
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		if (task_type != null && !"".equals(task_type)) {
			sql.append("           and p.PLAN_TYPE = ?\n");
			params.add(task_type);
		}
		sql.append(" group by td.DEALER_CODE,\n");
		sql.append("          td.DEALER_SHORTNAME,\n");
		sql.append("          d.MATERIAL_GROUPID,\n");
		sql.append("          tc.CODE_DESC,\n");
		sql.append("          g.GROUP_NAME,\n");
		sql.append("          g.GROUP_CODE\n");
		sql.append("   order by td.DEALER_CODE");

		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}

	/*
	 * ��ѯ��������ϸ����
	 */
	public List<Map<String, Object>> dealerSelectMonthPlanDown(Map<String, Object> map) {
		String plan_year = (String) map.get("plan_year");
		String plan_month = (String) map.get("plan_month");
		String plan_ver = (String) map.get("plan_ver");
		String plan_desc = (String) map.get("plan_desc");
		String dealerId = (String) map.get("dealerId");
		String companyId = map.get("companyId").toString();
		String areaId = map.get("areaId").toString();

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select td.DEALER_CODE,\n");
		sql.append("       td.DEALER_SHORTNAME,\n");
		sql.append("       d.MATERIAL_GROUPID,\n");
		sql.append("       g.GROUP_NAME,\n");
		sql.append("       g.GROUP_CODE,\n");
		sql.append("       nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n");
		sql.append("  from TT_VS_MONTHLY_PLAN        p,\n");
		sql.append("       TT_VS_MONTHLY_PLAN_DETAIL d,\n");
		sql.append("       TM_DEALER                 td,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP    g\n");
		sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
		sql.append("   and p.DEALER_ID = td.DEALER_ID\n");
		sql.append("   and p.AREA_ID=" + areaId + "\n");
		sql.append("   and p.COMPANY_ID=" + companyId + "\n");
		sql.append("   and p.DEALER_ID in (" + PlanUtil.createSqlStr(dealerId) + ")\n");
		sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
		sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("   and p.PLAN_VER = ?\n");
		params.add(plan_ver);
		sql.append("   and p.PLAN_YEAR = ?\n");
		params.add(plan_year);
		sql.append("   and p.PLAN_MONTH = ?\n");
		params.add(plan_month);
		if (null != plan_desc && !"".equals(plan_desc)) {
			sql.append("           and p.PLAN_DESC like ?\n");
			params.add(plan_desc);
		}
		sql.append(" group by td.DEALER_CODE,\n");
		sql.append("          td.DEALER_SHORTNAME,\n");
		sql.append("          d.MATERIAL_GROUPID,\n");
		sql.append("          g.GROUP_NAME,\n");
		sql.append("          g.GROUP_CODE\n");
		sql.append("   order by td.DEALER_CODE");
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	public Integer setUnEnable(String year, String month, Long userId, String companyId, String dealerId, String planType) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("  update TT_VS_MONTHLY_PLAN plan set plan.plan_desc = " + Constant.STATUS_DISABLE + "\n");
		sql.append(" where 1=1\n");
		sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
		// sql.append("   and plan.AREA_ID=" + areaId + "\n");
		sql.append("   and exists(" + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
		sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
		sql.append("   and plan.PLAN_YEAR = " + year + "\n");
		sql.append("   and plan.PLAN_MONTH = " + month + "\n");
		sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
		sql.append("   and plan.IS_FLAG = 0 \n");
		// sql.append("   and plan.PLAN_TYPE = " + planType + "\n");
		return super.update(sql.toString(), null);
	}
}
