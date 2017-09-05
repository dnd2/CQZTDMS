/**********************************************************************
* <pre>
* FILE : ClaimRuleDao.java
* CLASS : ClaimRuleDao
*
* AUTHOR : PGM
*
* FUNCTION : 三包规则维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-14| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ClaimRuleDao.java,v 1.4 2011/01/12 10:15:01 yx Exp $
 */
package com.infodms.dms.dao.claim.basicData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrBackupListPO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  三包规则维护 
 * @author        :  PGM
 * CreateDate     :  2010-07-14
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ClaimRuleDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ClaimRuleDao.class);
	private static final ClaimRuleDao dao = new ClaimRuleDao ();
	public  static final ClaimRuleDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 三包规则维护 
	 * @param           : 三包规则代码
	 * @param           : 三包规则名称
	 * @param           : 规则类型
	 * @param           : 规则状态
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的三包规则维护信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 三包规则维护
	 */
	public  PageResult<Map<String, Object>>  getClaimRuleQuery(TtAsWrRulePO rulePO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select r.ID,r.RULE_CODE,r.RULE_NAME,r.RULE_TYPE,r.RULE_STATUS,r.COMPANY_ID,to_char(r.CREATE_DATE,'yyyy-MM-dd') as CREATE_DATE ,  \n");
		sql.append("    (select count (l.ID)  from TT_AS_WR_RULE_LIST l where l.RULE_ID =r.ID) as PARTSNUM    \n");
		sql.append("    from TT_AS_WR_RULE r  where 1=1 \n");
		if(!"".equals(rulePO.getCompanyId())&&!(null==rulePO.getCompanyId())){//公司ID不为空
			sql.append("		AND r.COMPANY_ID = '"+rulePO.getCompanyId()+"' \n");
		}
		if(!"".equals(rulePO.getRuleCode())&&!(null==rulePO.getRuleCode())){//三包规则代码不为空
			sql.append("		AND UPPER(r.RULE_CODE) like UPPER('%"+rulePO.getRuleCode()+"%')\n");
		}
		if(!"".equals(rulePO.getRuleName())&&!(null==rulePO.getRuleName())){//三包规则名称不为空
			sql.append("		AND r.RULE_NAME like '%"+rulePO.getRuleName()+"%' \n");
		}
		if(!"".equals(rulePO.getRuleType())&&!(null==rulePO.getRuleType())){//规则类型不为空
			sql.append("		AND r.RULE_TYPE like '%"+rulePO.getRuleType()+"%' \n");
		}
		if(!"".equals(rulePO.getRuleStatus())&&!(null==rulePO.getRuleStatus())){//规则状态不为空
			sql.append("		AND r.RULE_STATUS like '%"+rulePO.getRuleStatus()+"%' \n");
		}
		sql.append("            group by  r.ID,r.RULE_CODE,r.RULE_NAME,r.RULE_TYPE,r.RULE_STATUS,r.COMPANY_ID ,r.CREATE_DATE \n");
		sql.append("            ORDER BY  r.ID desc ,r.rule_status \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function       :  增加三包规则信息
	 * @param         :  request---三包规则代码、三包规则名称、三包规则类型、三包规则状态
	 * @return        :  三包规则维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public static void claimRuleAdd(TtAsWrRulePO RulePO) {
		dao.insert(RulePO);
    }
	
	/**
	 * Function       :  新增之后---查询三包规则信息
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public  TtAsWrRulePO  claimRuleAddAfter(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select r.ID,r.RULE_CODE,r.RULE_NAME,r.RULE_TYPE,r.RULE_STATUS,r.COMPANY_ID  \n");
		sql.append("  from TT_AS_WR_RULE r   \n");
		sql.append("  WHERE  1=1 ");
		if (id!=null&&!("").equals(id)){
		sql.append(" AND r.ID = ? ");
		params.add(id);
		}
		TtAsWrRulePO RulePO=new TtAsWrRulePO();
		List list = dao.select(TtAsWrRulePO.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				RulePO = (TtAsWrRulePO) list.get(0);
			}
		}
		return RulePO;
	}
	/**
	 * Function       :  修改三包规则信息
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public static void  claimRuleUpdate(TtAsWrRulePO RulePO,TtAsWrRulePO RulePOContent){
		dao.update(RulePO, RulePOContent);
	}
	/**
	 * Function       :  根据条件查询三包规则维护中符合条件的信息，其中包括：
	 * @param         :  request-配件代码、三包月份、三包里程
	 * @return        :  三包规则明细维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public  PageResult<Map<String, Object>>  claimRuleDetailQuery(TtAsWrRuleListPO RuleListPO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select rl.id ,rl.rule_id,rl.part_code,rl.part_name,rl.claim_month,rl.claim_melieage,decode(b.part_war_type,94031001,'常规件',94031002,'易损易耗件',94031003,'关注部位') part_war_type  \n");
		sql.append("     from TT_AS_WR_RULE_LIST rl,tm_pt_part_base b   where 1=1  and rl.part_code = b.part_code\n");
		if(!"".equals(RuleListPO.getRuleId())&&!(null==RuleListPO.getRuleId())){//ID不为空
			sql.append("		AND rl.rule_id = "+RuleListPO.getRuleId()+" \n");
		}
		if(!"".equals(RuleListPO.getPartCode())&&!(null==RuleListPO.getPartCode())){//配件代码不为空
			sql.append("		AND UPPER(rl.part_code) like UPPER('%"+RuleListPO.getPartCode()+"%')\n");
		}
		if(!"".equals(RuleListPO.getPartName())&&!(null==RuleListPO.getPartName())){//配件名称不为空
			sql.append("		AND UPPER(rl.part_name) like UPPER('%"+RuleListPO.getPartName()+"%')\n");
		}
		if(!"".equals(RuleListPO.getClaimMonth())&&!(null==RuleListPO.getClaimMonth())){//三包月份不为空
			sql.append("		AND rl.claim_month like '%"+RuleListPO.getClaimMonth()+"%' \n");
		}
		if(!"".equals(RuleListPO.getClaimMelieage())&&!(null==RuleListPO.getClaimMelieage())){//三包里程不为空
			sql.append("		AND rl.claim_melieage = "+RuleListPO.getClaimMelieage()+" \n");
		}
		if (Utility.testString(RuleListPO.getPartWarType())) {
			sql.append("		AND b.part_war_type = "+RuleListPO.getPartWarType()+" \n");
		}
		sql.append("            ORDER BY  rl.id  desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	public  PageResult<Map<String, Object>>  backUpClaimDetailQuery(TtAsWrBackupListPO backUpListPO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select rl.id ,rl.rule_id,rl.part_code,rl.part_name,rl.claim_month,rl.claim_melieage,decode(b.part_war_type,94031001,'常规件',94031002,'易损易耗件',94031003,'关注部位') part_war_type  \n");
		sql.append("     from TT_AS_WR_BACKUP_LIST rl,tm_pt_part_base b   where 1=1  and rl.part_code = b.part_code\n");
		if(!"".equals(backUpListPO.getRuleId())&&!(null==backUpListPO.getRuleId())){//ID不为空
			sql.append("		AND rl.rule_id = "+backUpListPO.getRuleId()+" \n");
		}
		if(!"".equals(backUpListPO.getPartCode())&&!(null==backUpListPO.getPartCode())){//配件代码不为空
			sql.append("		AND UPPER(rl.part_code) like UPPER('%"+backUpListPO.getPartCode()+"%')\n");
		}
		if(!"".equals(backUpListPO.getPartName())&&!(null==backUpListPO.getPartName())){//配件名称不为空
			sql.append("		AND UPPER(rl.part_name) like UPPER('%"+backUpListPO.getPartName()+"%')\n");
		}
		if(!"".equals(backUpListPO.getClaimMonth())&&!(null==backUpListPO.getClaimMonth())){//三包月份不为空
			sql.append("		AND rl.claim_month like '%"+backUpListPO.getClaimMonth()+"%' \n");
		}
		if(!"".equals(backUpListPO.getClaimMelieage())&&!(null==backUpListPO.getClaimMelieage())){//三包里程不为空
			sql.append("		AND rl.claim_melieage = "+backUpListPO.getClaimMelieage()+" \n");
		}
		if (Utility.testString(backUpListPO.getPartWarType())) {
			sql.append("		AND b.part_war_type = "+backUpListPO.getPartWarType()+" \n");
		}
		sql.append("            ORDER BY  rl.id  desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function         : 三包规则维护---配件清单导入删除(导入配件之前清空临时表)
	 * @param           : 规则ID
	 * @throws          : Exception
	 * LastUpdate       : 三包规则维护
	 */
	public void claimRulePartsImportDelete(TtAsWrRuleListTmpPO RuleListPO){
		dao.delete(RuleListPO);
	}
	/**
	 * Function         : 三包规则维护---配件清单导入新增
	 * @param           : 规则ID
	 * @throws          : Exception
	 * LastUpdate       : 三包规则维护
	 */
	public void claimRulePartsImportAdd(TtAsWrRuleListTmpPO RuleListPO){
		dao.insert(RuleListPO);              
	}
	public void claimRulePartsImportAddMerge(TtAsWrRuleListTmpPO RuleListPO){
		StringBuffer sql= new StringBuffer();
		sql.append("MERGE INTO tt_as_wr_rule_list r\n" );
		sql.append("USING\n" );
		sql.append("  (select rule_id,part_code, part_name, claim_month, claim_melieage\n" );
		sql.append("    from tt_as_wr_rule_list_tmp where rule_id="+RuleListPO.getRuleId()+") l ON ( r.rule_id=l.rule_id and r.part_code = l.part_code and\n" );
		sql.append("                                 r.part_name = l.part_name) WHEN\n" );
		sql.append("   MATCHED THEN\n" );
		sql.append("    UPDATE\n" );
		sql.append("       SET r.claim_month    = l.claim_month,\n" );
		sql.append("           r.claim_melieage = l.claim_melieage,\n" );
		sql.append("           r.update_by    = "+RuleListPO.getUpdateBy()+",\n" );
		sql.append("           r.update_date  = sysdate\n" );
		sql.append("  WHEN NOT MATCHED THEN\n" );
		sql.append("    INSERT\n" );
		sql.append("      (r.id,\n" );
		sql.append("       r.rule_id,\n" );
		sql.append("       r.part_code,\n" );
		sql.append("       r.part_name,\n" );
		sql.append("       r.claim_month,\n" );
		sql.append("       r.claim_melieage,\n" );
		sql.append("       r.create_by,\n" );
		sql.append("       r.create_date)\n" );
		sql.append("    VALUES\n" );
		sql.append("      (f_getid(),\n" );
		sql.append("       "+RuleListPO.getRuleId()+",\n" );
		sql.append("       '"+RuleListPO.getPartCode()+"',\n" );
		sql.append("       '"+RuleListPO.getPartName()+"',\n" );
		sql.append("       "+RuleListPO.getClaimMonth()+",\n" );
		sql.append("       "+RuleListPO.getClaimMelieage()+",\n" );
		sql.append("       "+RuleListPO.getCreateBy()+",\n" );
		sql.append("       sysdate)");

		dao.update(sql.toString(), null);
	}
	 /**
	 * Function       :  修改导入的配件信息中的三包月份、三包里程
	 * @param         :  request-规则ID
	 * @return        :  修改导入的配件信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */                                                    
	public static void claimRuleUpdateImportParts(String []idArray,String []claimMonthArray,String []claimMelieageArray,TtAsWrRuleListPO RuleListPOContent) {
		TtAsWrRuleListPO  RuleListPO  =new TtAsWrRuleListPO();
		for (int i = 0;i<idArray.length;i++) {
			RuleListPO.setId(Long.parseLong(idArray[i]));
			RuleListPOContent.setClaimMonth(Integer.parseInt(claimMonthArray[i]));
			RuleListPOContent.setClaimMelieage(Double.parseDouble(claimMelieageArray[i]));
			dao.update(RuleListPO,RuleListPOContent);
		}
    }
	
	public static void backUpClaimUpdateImportParts(String []idArray,String []claimMonthArray,String []claimMelieageArray,TtAsWrBackupListPO backUpListPOContent) {
		TtAsWrBackupListPO  backUpListPO  =new TtAsWrBackupListPO();
		for (int i = 0;i<idArray.length;i++) {
			backUpListPO.setId(Long.parseLong(idArray[i]));
			backUpListPOContent.setClaimMonth(Integer.parseInt(claimMonthArray[i]));
			backUpListPOContent.setClaimMelieage(Double.parseDouble(claimMelieageArray[i]));
			dao.update(backUpListPO,backUpListPOContent);
		}
    }
}