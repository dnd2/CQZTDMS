package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.infodms.dms.bean.ComplaintDisposalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>Title:ComplaintDisposalDao.java</p>
 *
 * <p>Description: 客户投诉处理持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-1</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintDisposalDao extends BaseDao {
	
	private static final ComplaintDisposalDao dao = new ComplaintDisposalDao();
	
	public static final ComplaintDisposalDao getInstance() {
		return dao;
	}
	protected TtCrComplaintsPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据条件查询客户投诉表
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryComplaintDisposal(ComplaintDisposalBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCC.COMP_CODE,\n" );
		sql.append("       TCC.COMP_ID,\n" );
		sql.append("       TCC.LINK_MAN,\n" );
		sql.append("       TCC.TEL,\n" );
		sql.append("       ORG.ORG_NAME,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TCC.PROVINCE,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TCC.COMP_DEALER,\n" );
		sql.append("       TCC.VIN,\n");
		sql.append("       TCC.LICENSE_NO,\n" );
		sql.append("       TCC.COMP_SOURCE,\n" );
		sql.append("       TCC.COMP_LEVEL,\n" );
		sql.append("       TCC.COMP_TYPE,\n" );
		sql.append("       TO_CHAR(TCC.CREATE_DATE,'YYYY-MM-DD HH24:mi') AS CREATE_DATE,\n" );
		sql.append("       TCC.STATUS,\n" );
		sql.append("       B.AUDIT_RESULT\n" );
		sql.append("  FROM TT_CR_COMPLAINTS TCC, TT_CR_COMPLAINTS_AUDIT B,TM_DEALER TD,TM_ORG ORG,TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TCC.IS_DEL = 0\n" );
		sql.append("   AND TCC.COMP_ID = B.COMP_ID(+)\n" );
		sql.append("   AND NVL(TCC.UPDATE_DATE, TCC.CREATE_DATE) = B.CREATE_DATE(+)\n" );
		sql.append("   AND TCC.COMP_DEALER = TD.DEALER_ID(+)\n");
		sql.append("   AND TCC.OWN_ORG_ID = ORG.ORG_ID(+)\n");
		sql.append("   AND TCC.MODEL_CODE = TVMG.GROUP_CODE(+)\n");
		
		
		List<Object> params = new LinkedList<Object>();
		
		if(!"".equals(bean.getLinkman())){
			sql.append("   AND TCC.LINK_MAN LIKE'%" );
			sql.append(bean.getLinkman());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getTel())){
			sql.append("   AND TCC.TEL LIKE '%" );
			sql.append(bean.getTel());
			sql.append("%'\n");
		}
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND TCC.CREATE_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND TCC.CREATE_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(null!=bean.getCompDealerId()&&!"".equals(bean.getCompDealerId())){
			sql.append("   AND TCC.COMP_DEALER =" );
			sql.append(bean.getCompDealerId());
			sql.append("\n");
		}
		
		if(null!=bean.getCompDealerCode()&&!"".equals(bean.getCompDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getCompDealerCode(), params, "TD", "DEALER_CODE"));
			sql.append("\n");
		}
		
		if(!"".equals(bean.getCompLevel())){
			sql.append("   AND TCC.COMP_LEVEL =" );
			sql.append(bean.getCompLevel());
			sql.append("\n");
		}
		if(null!=bean.getOwnOrgId()&&!"".equals(bean.getOwnOrgId())){
			sql.append("   AND TCC.OWN_ORG_ID =" );
			sql.append(bean.getOwnOrgId());
			sql.append("\n");
		}
		
		if(null!=bean.getOwnOrgCode()&&!"".equals(bean.getOwnOrgCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getOwnOrgCode(), params, "ORG", "ORG_CODE"));
			sql.append("\n");
		}
		
		if(!"".equals(bean.getCompType2())){
			sql.append("   AND TCC.COMP_TYPE =" );
			sql.append(bean.getCompType2());
			sql.append("\n"); 
		}
		
		if("".equals(bean.getCompType2())){
			if(String.valueOf(Constant.COMP_TYPE_TYPE_01).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_QUALITY_01).append(",").append(Constant.SEDAN_QUALITY_02).append(",");
				sql.append(Constant.SEDAN_QUALITY_03).append(",").append(Constant.SEDAN_QUALITY_04).append(",");
				sql.append(Constant.SEDAN_QUALITY_05).append(",").append(Constant.SEDAN_QUALITY_06).append(",");
				sql.append(Constant.SEDAN_QUALITY_07).append(",").append(Constant.SEDAN_QUALITY_08).append(",");
				sql.append(Constant.SEDAN_QUALITY_09).append(",").append(Constant.SEDAN_QUALITY_10);
				sql.append(")\n");
			}
			
			if(String.valueOf(Constant.COMP_TYPE_TYPE_02).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_MEDIUM_COMPLAINS_01).append(",");
				sql.append(Constant.SEDAN_MEDIUM_COMPLAINS_02).append(",").append(Constant.SEDAN_MEDIUM_COMPLAINS_03);
				sql.append(")\n");
			}
			
			if(String.valueOf(Constant.COMP_TYPE_TYPE_03).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_SERVES_01).append(",").append(Constant.SEDAN_SERVES_02).append(",");
				sql.append(Constant.SEDAN_SERVES_03).append(",").append(Constant.SEDAN_SERVES_04).append(",");
				sql.append(Constant.SEDAN_SERVES_05).append(",").append(Constant.SEDAN_SERVES_06);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_04).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_ACCESSORIES_01).append(",").append(Constant.SEDAN_ACCESSORIES_02).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_03).append(",").append(Constant.SEDAN_ACCESSORIES_04).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_05).append(",").append(Constant.SEDAN_ACCESSORIES_06).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_07).append(",").append(Constant.SEDAN_ACCESSORIES_08);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_05).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SALE_CONSULTS_01).append(",").append(Constant.SALE_CONSULTS_02).append(",");
				sql.append(Constant.SALE_CONSULTS_03).append(",").append(Constant.SALE_CONSULTS_04).append(",");
				sql.append(Constant.SALE_CONSULTS_05).append(",").append(Constant.SALE_CONSULTS_06);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_06).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SALE_COMPLAINS_01).append(",").append(Constant.SALE_COMPLAINS_02).append(",");
				sql.append(Constant.SALE_COMPLAINS_03).append(",").append(Constant.SALE_COMPLAINS_04).append(",");
				sql.append(Constant.SALE_COMPLAINS_05).append(",").append(Constant.SALE_COMPLAINS_06).append(",");
				sql.append(Constant.SALE_COMPLAINS_07);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_07).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_HELPS_01).append(",").append(Constant.SEDAN_HELPS_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_08).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_SERVES_DEALER_01).append(",").append(Constant.SEDAN_SERVES_DEALER_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_09).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_ACTIVITY_01).append(",").append(Constant.SEDAN_ACTIVITY_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_10).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_01).append(",").append(Constant.SEDAN_OTHER_PROBLEMS_02).append(",");
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_03).append(",").append(Constant.SEDAN_OTHER_PROBLEMS_04).append(",");
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_05);
				sql.append(")\n");
			}			
		}
		
		if(!"".equals(bean.getStatus())){
			sql.append("   AND TCC.STATUS =" );
			sql.append(bean.getStatus());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getAuditResult())){
			sql.append(" AND B.AUDIT_RESULT = ");
			sql.append(bean.getAuditResult());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getCompSource())){
			sql.append("   AND TCC.COMP_SOURCE =" );
			sql.append(bean.getCompSource());
			sql.append("\n");
		}
		if(!"".equals(bean.getModelCode())){
			sql.append("   AND TCC.MODEL_CODE =" );
			sql.append(bean.getModelCode());
			sql.append("\n");
		}

		if(null!=bean.getOrgId()&&!"".equals(bean.getOrgId())){
			sql.append("   AND TCC.ORG_ID =" );
			sql.append(bean.getOrgId());
			sql.append("\n");
		}
	
		if(null!=bean.getDealerId()&&!"".equals(bean.getDealerId())){
			sql.append("   AND TCC.DEALER_ID IN(" );
			sql.append(bean.getDealerId());
			sql.append(")\n");
		}

		if(null!=bean.getCompCode()&&!"".equals(bean.getCompCode())){
			sql.append("   AND TCC.COMP_CODE LIKE'%" );
			sql.append(bean.getCompCode());
			sql.append("%'\n");
		}
		
		if(null!=bean.getLicenseNo()&&!"".equals(bean.getLicenseNo())){
			sql.append("   AND TCC.LICENSE_NO LIKE'%" );
			sql.append(bean.getLicenseNo());
			sql.append("%'\n");
		}
		
//		if(null!=bean.getIntStatus()&&!"".equals(bean.getIntStatus())){
//			sql.append("   AND TCC.INT_STATUS =" );
//			sql.append(bean.getIntStatus());
//			sql.append("\n");
//		}
		
		if("0".equals(bean.getIntStatus())){
			sql.append("   AND TCC.INT_STATUS =" );
			sql.append(bean.getIntStatus());
			sql.append("\n");
		}
		
		sql.append("   ORDER BY TCC.COMP_ID DESC\n");
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
		
		
	}
	
	/**
	 * 区域查询客户投诉处理
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryComplaintDisposalByOrg(ComplaintDisposalBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCC.COMP_CODE,\n" );
		sql.append("       TCC.COMP_ID,\n" );
		sql.append("       TCC.LINK_MAN,\n" );
		sql.append("       TCC.TEL,\n" );
		sql.append("       ORG.ORG_NAME,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TCC.PROVINCE,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TCC.MODEL_CODE,\n" );
		sql.append("       TCC.COMP_DEALER,\n" );
		sql.append("       TCC.LICENSE_NO,\n" );
		sql.append("       TCC.COMP_SOURCE,\n" );
		sql.append("       TCC.COMP_LEVEL,\n" );
		sql.append("       TCC.COMP_TYPE,\n" );
		sql.append("       TO_CHAR(TCC.CREATE_DATE,'YYYY-MM-DD hh24:mi') AS CREATE_DATE,\n" );
		sql.append("       TCC.STATUS,\n" );
		sql.append("       B.AUDIT_RESULT\n" );
		sql.append("  FROM TT_CR_COMPLAINTS TCC, TT_CR_COMPLAINTS_AUDIT B,TM_DEALER TD,TM_ORG ORG,TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TCC.IS_DEL = 0\n" );
		sql.append("   AND TCC.COMP_ID = B.COMP_ID(+)\n" );
		sql.append("   AND NVL(TCC.UPDATE_DATE, TCC.CREATE_DATE) = B.CREATE_DATE(+)\n" );
		sql.append("   AND TCC.COMP_DEALER = TD.DEALER_ID(+)\n");
		sql.append("   AND TCC.OWN_ORG_ID = ORG.ORG_ID(+)\n");
		sql.append("   AND TCC.MODEL_CODE = TVMG.GROUP_CODE(+)\n");
		//sql.append("   AND TCC.DEALER_ID IS NULL\n");
		sql.append("   AND NVL(TCC.DEALER_ID, 0) = 0\n");
		List<Object> params = new LinkedList<Object>();
		
		if(!"".equals(bean.getLinkman())){
			sql.append("   AND TCC.LINK_MAN LIKE'%" );
			sql.append(bean.getLinkman());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getTel())){
			sql.append("   AND TCC.TEL =" );
			sql.append(bean.getTel());
			sql.append("\n");
		}
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND TCC.CREATE_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND TCC.CREATE_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(bean.getCompDealerId())){
			sql.append("   AND TCC.COMP_DEALER =" );
			sql.append(bean.getCompDealerId());
			sql.append("\n");
		}
		
		if(null!=bean.getCompDealerCode()&&!"".equals(bean.getCompDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getCompDealerCode(), params, "TD", "DEALER_CODE"));
			sql.append("\n");
		}
		
		if(!"".equals(bean.getCompLevel())){
			sql.append("   AND TCC.COMP_LEVEL =" );
			sql.append(bean.getCompLevel());
			sql.append("\n");
		}
//		if(!"".equals(bean.getOwnOrgId())){
//			sql.append("   AND TCC.OWN_ORG_ID =" );
//			sql.append(bean.getOwnOrgId());
//			sql.append("\n");
//		}
		if(!"".equals(bean.getCompType2())){
			sql.append("   AND TCC.COMP_TYPE =" );
			sql.append(bean.getCompType2());
			sql.append("\n"); 
		}
		
		if("".equals(bean.getCompType2())){
			if(String.valueOf(Constant.COMP_TYPE_TYPE_01).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_QUALITY_01).append(",").append(Constant.SEDAN_QUALITY_02).append(",");
				sql.append(Constant.SEDAN_QUALITY_03).append(",").append(Constant.SEDAN_QUALITY_04).append(",");
				sql.append(Constant.SEDAN_QUALITY_05).append(",").append(Constant.SEDAN_QUALITY_06).append(",");
				sql.append(Constant.SEDAN_QUALITY_07).append(",").append(Constant.SEDAN_QUALITY_08).append(",");
				sql.append(Constant.SEDAN_QUALITY_09).append(",").append(Constant.SEDAN_QUALITY_10);
				sql.append(")\n");
			}
			
			if(String.valueOf(Constant.COMP_TYPE_TYPE_02).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_MEDIUM_COMPLAINS_01).append(",");
				sql.append(Constant.SEDAN_MEDIUM_COMPLAINS_02).append(",").append(Constant.SEDAN_MEDIUM_COMPLAINS_03);
				sql.append(")\n");
			}
			
			if(String.valueOf(Constant.COMP_TYPE_TYPE_03).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_SERVES_01).append(",").append(Constant.SEDAN_SERVES_02).append(",");
				sql.append(Constant.SEDAN_SERVES_03).append(",").append(Constant.SEDAN_SERVES_04).append(",");
				sql.append(Constant.SEDAN_SERVES_05).append(",").append(Constant.SEDAN_SERVES_06);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_04).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_ACCESSORIES_01).append(",").append(Constant.SEDAN_ACCESSORIES_02).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_03).append(",").append(Constant.SEDAN_ACCESSORIES_04).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_05).append(",").append(Constant.SEDAN_ACCESSORIES_06).append(",");
				sql.append(Constant.SEDAN_ACCESSORIES_07).append(",").append(Constant.SEDAN_ACCESSORIES_08);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_05).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SALE_CONSULTS_01).append(",").append(Constant.SALE_CONSULTS_02).append(",");
				sql.append(Constant.SALE_CONSULTS_03).append(",").append(Constant.SALE_CONSULTS_04).append(",");
				sql.append(Constant.SALE_CONSULTS_05).append(",").append(Constant.SALE_CONSULTS_06);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_06).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SALE_COMPLAINS_01).append(",").append(Constant.SALE_COMPLAINS_02).append(",");
				sql.append(Constant.SALE_COMPLAINS_03).append(",").append(Constant.SALE_COMPLAINS_04).append(",");
				sql.append(Constant.SALE_COMPLAINS_05).append(",").append(Constant.SALE_COMPLAINS_06).append(",");
				sql.append(Constant.SALE_COMPLAINS_07);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_07).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_HELPS_01).append(",").append(Constant.SEDAN_HELPS_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_08).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_SERVES_DEALER_01).append(",").append(Constant.SEDAN_SERVES_DEALER_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_09).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_ACTIVITY_01).append(",").append(Constant.SEDAN_ACTIVITY_02);
				sql.append(")\n");
			}
			if(String.valueOf(Constant.COMP_TYPE_TYPE_10).equals(bean.getCompType())){
				sql.append("   AND TCC.COMP_TYPE IN(" );
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_01).append(",").append(Constant.SEDAN_OTHER_PROBLEMS_02).append(",");
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_03).append(",").append(Constant.SEDAN_OTHER_PROBLEMS_04).append(",");
				sql.append(Constant.SEDAN_OTHER_PROBLEMS_05);
				sql.append(")\n");
			}			
		}
		if(!"".equals(bean.getStatus())){
			sql.append("   AND TCC.STATUS =" );
			sql.append(bean.getStatus());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getAuditResult())){
			sql.append(" AND B.AUDIT_RESULT = ");
			sql.append(bean.getAuditResult());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getCompSource())){
			sql.append("   AND TCC.COMP_SOURCE =" );
			sql.append(bean.getCompSource());
			sql.append("\n");
		}
		if(!"".equals(bean.getModelCode())){
			sql.append("   AND TCC.MODEL_CODE =" );
			sql.append(bean.getModelCode());
			sql.append("\n");
		}
		
		if(null!=bean.getOrgId()&&!"".equals(bean.getOrgId())){
			sql.append("   AND TCC.ORG_ID =" );
			sql.append(bean.getOrgId());
			sql.append("\n");
		}
		
		if(null!=bean.getDealerId()&&!"".equals(bean.getDealerId())){
			sql.append("   AND TCC.DEALER_ID IN(" );
			sql.append(bean.getDealerId());
			sql.append(")\n");
		}
		
		if(null!=bean.getCompCode()&&!"".equals(bean.getCompCode())){
			sql.append("   AND TCC.COMP_CODE LIKE'%" );
			sql.append(bean.getCompCode());
			sql.append("%'\n");
		}
		
		if(null!=bean.getLicenseNo()&&!"".equals(bean.getLicenseNo())){
			sql.append("   AND TCC.LICENSE_NO LIKE'%" );
			sql.append(bean.getLicenseNo());
			sql.append("%'\n");
		}
		
		if(null!=bean.getIntStatus()&&!"".equals(bean.getIntStatus())){
			sql.append("   AND TCC.INT_STATUS =" );
			sql.append(bean.getIntStatus());
			sql.append("\n");
		}
		
		sql.append("   ORDER BY TCC.COMP_ID DESC\n");
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
		
		
	}
	
	/**
	 * 新增时查询TC_CODE表格显示出多选框
	 * @return
	 */
	public List<Map<String, Object>> getCheckBox(){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT CODE_ID, CODE_DESC FROM TC_CODE WHERE TYPE = 1062");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		
		return list;
	}
	
	
	/**
	 * 根据投诉id查询TT_CR_COMPLAINTS表信息
	 * @param compId
	 * @return
	 */
	public Map<String, Object> getComplaintById(String compId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCC.LINK_MAN,\n" );
		sql.append("       TCC.COMP_ID,\n" );
		sql.append("       TCC.SEX,\n" );
		sql.append("       TO_CHAR(TCC.BIRTHDAY,'YYYY-MM-DD') AS BIRTHDAY,\n" );
		sql.append("       TCC.AGE,\n" );
		sql.append("       TCC.OWN_ORG_ID,\n" );
		sql.append("       ORG.ORG_CODE,\n" );
		sql.append("       ORG.ORG_NAME,\n" );
		sql.append("       TCC.TEL,\n" );
		sql.append("       TCC.PROVINCE,\n" );
		sql.append("       TCC.E_MAIL,\n" );
		sql.append("       TCC.CITY,\n" );
		sql.append("       TCC.ZIP_CODE,\n" );
		sql.append("       TCC.DISTRICT,\n" );
		sql.append("       TCC.ADDRESS,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_NAME,\n" );
		sql.append("       TCC.VIN,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TCC.MODEL_CODE,\n" );
		sql.append("	   TVMG.GROUP_NAME,\n");
		sql.append("       TCC.ENGINE_NO,\n" );
		sql.append("       TCC.LICENSE_NO,\n" );
		sql.append("       TO_CHAR(TCC.PURCHASED_DATE,'YYYY-MM-DD') AS PURCHASED_DATE,\n" );
		sql.append("       TCC.COMP_CODE,\n" );
		sql.append("       TCC.COMP_LEVEL,\n" );
		sql.append("       TCC.COM_TYPE,\n" );
		sql.append("       TCC.COMP_TYPE,\n" );
		sql.append("       TCC.COMP_SOURCE,\n" );
		sql.append("       TCC.COMP_CONTENT,\n" );
		sql.append("       TO_CHAR(TCC.CREATE_DATE,'YYYY-MM-DD hh24:mi') AS CREATE_DATE\n" );
		sql.append("  FROM TT_CR_COMPLAINTS TCC,TM_DEALER TD,TM_ORG ORG,TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TCC.COMP_ID =");
		sql.append(compId);
		sql.append("\n");
		sql.append("  AND TCC.COMP_DEALER = TD.DEALER_ID(+)");
		sql.append("  AND TCC.OWN_ORG_ID = ORG.ORG_ID(+)");
		sql.append("  AND TCC.MODEL_CODE = TVMG.GROUP_CODE(+)\n");

		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 根据dealerCode查询出dealerId
	 * @param dealerCode
	 * @return
	 */
	public String getDealerIdByCode(String dealerCode){
		String dealerId = "";
		String sql = "SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='"+dealerCode+"'";
		Map<String, Object> map = pageQueryMap(sql,null,getFunName());
		if(null!=map&&map.size()>0){
			dealerId = CommonUtils.checkNull(map.get("DEALER_ID"));
		}
		
		return dealerId;
	}
	
	
	/**
	 * 根据orgCode查询出orgId
	 * @param orgCode
	 * @return
	 */
	public String getOrgIdByCode(String orgCode){
		String orgId = "";
		String sql = "SELECT ORG_ID FROM TM_ORG WHERE ORG_CODE ='"+orgCode+"' AND ORG_TYPE="+Constant.ORG_TYPE_OEM;
		Map<String, Object> map = pageQueryMap(sql,null,getFunName());
		if(null!=map&&map.size()>0){
			orgId = CommonUtils.checkNull(map.get("ORG_ID"));
		}
		
		return orgId;
	}
	
	/**
	 * 取得车系列表
	 * @return
	 */
	public List<Map<String, Object>> getSeriesList(){
		
		String sql = "SELECT GROUP_ID,GROUP_CODE,GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE STATUS = 10011001 AND GROUP_LEVEL = 3";
		
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		
		return list;
	}
	
	/**
	 * 通过vin查询出车辆其他信息
	 * @param vin
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryVehicleInfo(String vin,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VIN,\n" );
		sql.append("       TV.VEHICLE_ID,\n" );
		sql.append("       TV.ENGINE_NO,\n" );
		sql.append("       TV.SERIES_ID,\n" );
		sql.append("       TO_CHAR(TV.PURCHASED_DATE,'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("       TAS.VEHICLE_NO,\n" );
		sql.append("       TMG.GROUP_NAME\n" );
		sql.append("  FROM TM_VEHICLE             TV,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES TAS,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TMG\n" );
		sql.append(" WHERE TV.VEHICLE_ID = TAS.VEHICLE_ID\n" );
		sql.append("   AND TV.SERIES_ID = TMG.GROUP_ID\n" );
		sql.append("   AND TMG.GROUP_LEVEL = 2");
		if(!"".equals(vin)){
			sql.append("   AND TV.VIN LIKE'%").append(vin).append("%'\n");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 
	* @Title: queryCityByName 
	* @Description: TODO(根据地级市名称模糊查询地区代码) 
	* @param @param name 地级市名称
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCityByName(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT REGION_CODE FROM TM_REGION \n");
		sql.append("WHERE REGION_NAME LIKE '").append(name).append("%'");
		sql.append("  AND REGION_TYPE = ").append(Constant.REGION_TYPE_03);
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * 
	* @Title: queryProvinceByName 
	* @Description: TODO(根据省市名查询省市代码) 
	* @param @param 省/市名称
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryProvinceByName(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT REGION_CODE FROM TM_REGION \n");
		sql.append("WHERE REGION_NAME = '").append(name).append("'");
		sql.append("  AND REGION_TYPE = ").append(Constant.REGION_TYPE_02);
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 
	* @Title: queryImpComplaintDisposal 
	* @Description: TODO(根据用户查询导入临时表,进行分页显示) 
	* @param @param userId
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryImpComplaintDisposal(Long userId, int pageSize, int curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.COMP_CODE, A.COMP_ID, A.LINK_MAN, A.TEL, A.COMP_SOURCE, A.COMP_LEVEL, A.COMP_TYPE, A.PROVINCE,\n" );
		sql.append("       B.DEALER_SHORTNAME, C.ORG_NAME, D.GROUP_NAME, TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE\n" );
		sql.append("  FROM TT_CR_COMPLAINTS_TMP A ,TM_DEALER B, TM_ORG C,TM_VHCL_MATERIAL_GROUP D\n" );
		sql.append(" WHERE A.IS_DEL = 0\n" );
		sql.append("   AND A.CREATE_BY = ").append(userId).append("\n");
		sql.append("   AND A.COMP_DEALER = B.DEALER_ID(+)\n");
		sql.append("   AND A.OWN_ORG_ID = C.ORG_ID(+)\n");
		sql.append("   AND A.MODEL_CODE = D.GROUP_CODE(+)\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), curPage, pageSize);
		return ps;
	}
	/**
	 * 
	* @Title: deleteTmp 
	* @Description: TODO(根据创建人删除临时表的数据) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void deleteTmp(Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM TT_CR_COMPLAINTS_TMP\n");
		sql.append("WHERE CREATE_BY = ").append(userId);
		delete(sql.toString(), null);
	}
	/**
	 * 
	* @Title: queryExportComplaint 
	* @Description: TODO(根据投诉信息id列表查询投诉信息主表子表和回访子表) 
	* @param @param ids
	* @param @return    设定文件 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryExportComplaint(String ids) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.COMP_ID, TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD')CREATE_DATE, A.COMP_CODE, A.LINK_MAN, A.TEL,\n" );
		sql.append("       A.COMP_CONTENT, A.CALL_CEN_CON, A.MODEL_CODE, B.ORG_NAME,\n" );
		sql.append("       TO_CHAR(A.SEND_DATE, 'YYYY-MM-DD')AS SEND_DATE,\n");
		sql.append("       C1.CODE_DESC AS COMP_SOURCE, C2.CODE_DESC AS COM_TYPE, C3.CODE_DESC AS COMP_TYPE,\n" );
		sql.append("       C4.CODE_DESC AS COMP_LEVEL, D1.REGION_NAME AS PROVINCE, D2.REGION_NAME AS CITY, E.DEALER_CODE, E.DEALER_NAME,\n" );
		sql.append("       F.COMP_ID, F.AUDIT_CONTENT, F.AUDIT_RESULT, F.NAME, F.PART_CODE, F.SUPPLIER,\n" );
		//sql.append("       /*投诉下发时间*/TO_CHAR(F.SEND_DATE, 'YYYY-MM-DD')SEND_DATE,\n" );
		sql.append("       /*首次回复时间*/TO_CHAR(F.CALL_DATE, 'YYYY-MM-DD')FIRST_CALL_DATE,\n" );
		sql.append("       /*处理完时间*/TO_CHAR(F.HANDLE_DATE, 'YYYY-MM-DD')HANDLE_DATE,\n" );
		sql.append("       /*处理周期*/F.DAYS_BET,\n" );
		sql.append("       G.SHUT, G.SATISFIED, G.CAUSE, G.CALL_PERSON, G.CALL_CYCLE, G.REMARK, G.CALL_DATE, G.CALL_FAIL,\n" );
		sql.append("       G.SUPPORT, G.SUPPORT_PERSON\n" );
		sql.append("FROM TT_CR_COMPLAINTS A, TM_ORG B, TC_CODE C1, TC_CODE C2, TC_CODE C3, TC_CODE C4,\n" );
		sql.append("     TM_REGION D1, TM_REGION D2, TM_DEALER E,\n" );
		sql.append("      /*根据COMP_ID查询投诉子表最新的记录*/\n" );
		sql.append("     (SELECT T1.COMP_ID, T1.AUDIT_CONTENT, T1.PART_CODE, T1.SUPPLIER,\n" );
		sql.append("       T2.CODE_DESC AS AUDIT_RESULT, T3.NAME,\n" );
		sql.append("       T6.MIN_DATE AS CALL_DATE, T7.CREATE_DATE AS HANDLE_DATE,\n" );
		sql.append("       /*处理周期=处理完时间-首次回复时间*/\n" );
		sql.append("       TRUNC(T7.CREATE_DATE,'DDD')-TRUNC(T6.MIN_DATE,'DDD') AS DAYS_BET\n" );
		sql.append("      FROM TT_CR_COMPLAINTS_AUDIT T1, TC_CODE T2, TC_USER T3,\n" );
		sql.append("      /*子表里最新的记录*/\n" );
		sql.append("      (SELECT TT1.COMP_ID, MAX(TT1.CREATE_DATE) AS MAX_DATE FROM TT_CR_COMPLAINTS_AUDIT TT1\n" );
		sql.append("        GROUP BY TT1.COMP_ID)T4,\n" );
		//sql.append("      /*总部首次下发到区域的时间(投诉下发时间)*/\n" );
		//sql.append("      (SELECT TT1.COMP_ID, TT1.SEND_DATE FROM TT_CR_COMPLAINTS TT1)T5,\n" );
		sql.append("      /*子表里最早的时间(首次回复时间 )*/\n" );
		sql.append("      (SELECT TT1.COMP_ID, MIN(TT1.CREATE_DATE) AS MIN_DATE FROM TT_CR_COMPLAINTS_AUDIT TT1\n" );
		sql.append("        GROUP BY TT1.COMP_ID)T6,\n" );
		sql.append("      /*(处理完时间)*/\n" );
		sql.append("      (SELECT TT1.COMP_ID, MAX(CREATE_DATE)AS CREATE_DATE FROM TT_CR_COMPLAINTS_AUDIT TT1\n" );
		sql.append("        WHERE TT1.AUDIT_RESULT IN (" ).
		    append(Constant.AUDIT_RESULT_TYPE_04).append(",").append(Constant.AUDIT_RESULT_TYPE_06).append(")\n");
		sql.append("        GROUP BY (TT1.COMP_ID))T7\n" );
		sql.append("      WHERE T1.AUDIT_RESULT = T2.CODE_ID(+)\n" );
		sql.append("        AND T1.CREATE_BY = T3.USER_ID(+)\n" );
		sql.append("        AND T1.COMP_ID = T4.COMP_ID\n" );
		//sql.append("        AND T1.COMP_ID = T5.COMP_ID(+)\n" );
		sql.append("        AND T1.COMP_ID = T6.COMP_ID\n" );
		sql.append("        AND T1.COMP_ID = T7.COMP_ID(+)\n" );
		sql.append("        AND T1.CREATE_DATE = MAX_DATE)F,\n" );
		sql.append("      /*查询子表结束*/\n" );
		sql.append("      /*根据COMP_ID查询投诉回访子表最新的记录*/\n" );
		sql.append("      (SELECT COMP_ID, SHUT, SATISFIED, CAUSE, CALL_PERSON, CALL_CYCLE, REMARK,\n" );
		sql.append("      CALL_DATE, CALL_FAIL, SUPPORT, SUPPORT_PERSON\n" );
		sql.append("       FROM TT_CR_COMPLAINTS_CALL) G\n" );
		sql.append("      /*查询子表结束*/\n" );
		sql.append("WHERE A.COMP_ID IN (" ).append(ids).append(")\n");
		sql.append("  AND A.COMP_ID = F.COMP_ID(+)\n" );
		sql.append("  AND A.COMP_ID = G.COMP_ID(+)\n" );
		sql.append("  AND A.OWN_ORG_ID = B.ORG_ID\n" );
		sql.append("  AND A.COMP_SOURCE = C1.CODE_ID\n" );
		sql.append("  AND A.COM_TYPE = C2.CODE_ID\n" );
		sql.append("  AND A.COMP_TYPE = C3.CODE_ID\n" );
		sql.append("  AND A.COMP_LEVEL = C4.CODE_ID\n" );
		sql.append("  AND A.PROVINCE = D1.REGION_CODE\n" );
		sql.append("  AND A.CITY = D2.REGION_CODE(+)\n" );
		sql.append("  AND A.COMP_DEALER = E.DEALER_ID(+)\n" );
		sql.append("  AND A.COMP_ID = G.COMP_ID(+)");

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
}
