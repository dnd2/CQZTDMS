package com.infodms.dms.actions.parts.financeManager.dealerAccImpRecordManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.financeManager.dealerAccImpRecordManager.PartDaKuanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartDaKuanAudit {
	public Logger logger = Logger.getLogger(PartDaKuanAudit.class);
	
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	PartDaKuanDao dao = PartDaKuanDao.getInstance();
	private static final String XIANKUAN_DAKUAN_AUDIT = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDaKuanAudit.jsp";

	
	/**
	 * 整款打款审核初始化
	 */
	public void xiankuanDakuanAuditInit () {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String st = String.valueOf(logonUser.getOrgId());
			request.setAttribute("parentOrgId", st);
			act.setForword(XIANKUAN_DAKUAN_AUDIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "备件打款登记审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	
	/**
	 * 备件打款审核处理
	 */
	public void zhengcheDakuanAuditProcess() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String detail_id = CommonUtils.checkNull(request.getParamValue("detail_id"));
			String fin_type = CommonUtils.checkNull(request.getParamValue("fin_type"));
			String type = CommonUtils.checkNull(request.getParamValue("type"));
			String auditRemark = CommonUtils.checkNull(request.getParamValue("auditRemark"));
			
			StringBuffer sbSql = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			// 处理审核 type == 1 审核通过 type == 2 审核驳回
			if(type.equals("1")) {
				//审核通过
				/* -----------------------------------
				 * fin_type = 1 备件款
				 * fin_tpe  = 2 备件精品款
				 ------------------------------------*/
					// 备件款

					List<Map<String,Object>> pList = dao.pageQuery("select * from TT_PART_ACCOUNT_IMPORT_HISTORY where STATUS=1 and HISTRORY_ID="+detail_id, null, dao.getFunName());
                    if(pList == null || pList.size()==0){
                    	//throw new RuntimeException("该笔备件款已经审核或驳回,请刷新页面后重试!");
                    	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该笔备件款已经审核或驳回,请刷新页面后重试!");
                    }
					sbSql.append("UPDATE TT_PART_ACCOUNT_DEFINE T\n");
                    sbSql.append("   SET T.ACCOUNT_SUM = NVL(T.ACCOUNT_SUM, 0) +\n");
                    sbSql.append("                       (SELECT A.AMOUNT\n");
                    sbSql.append("                          FROM TT_PART_ACCOUNT_IMPORT_HISTORY A\n");
                    sbSql.append("                         WHERE A.HISTRORY_ID = ?)\n");
                    sbSql.append(" WHERE EXISTS (SELECT 1\n");
                    sbSql.append("          FROM TT_PART_ACCOUNT_IMPORT_HISTORY H\n");
                    sbSql.append("         WHERE H.CHILDORG_ID = T.CHILDORG_ID\n");
                    sbSql.append("           AND H.PARENTORG_ID = T.PARENTORG_ID\n");
                    sbSql.append("           AND H.ACCOUNT_PURPOSE = T.ACCOUNT_PURPOSE\n");
                    sbSql.append("           AND H.HISTRORY_ID = ?)");

                    params.add(detail_id);
				    params.add(detail_id);


				 // 入账成功就更新打款明细状态
					if(dao.update(sbSql.toString(), params) == 1) {
				    
						sbSql.delete(0, sbSql.length()); params.clear();
						
					    sbSql.append("UPDATE TT_PART_ACCOUNT_IMPORT_HISTORY SET STATUS = 2, AUDIT_REMARK = ? WHERE HISTRORY_ID = ?"); 
					    params.add(auditRemark);
					    params.add(detail_id);
					    
					    dao.update(sbSql.toString(), params);
					}
				
			} else {
				// 审核驳回
					List<Map<String,Object>> pList = dao.pageQuery("select * from TT_PART_ACCOUNT_IMPORT_HISTORY where STATUS=1 and HISTRORY_ID="+detail_id, null, dao.getFunName());
                    if(pList == null || pList.size()==0){
                    	//throw new RuntimeException("该笔备件款已经审核或驳回,请刷新页面后重试!");
                    	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该笔备件款已经审核或驳回,请刷新页面后重试!");
                    }
					sbSql.append("UPDATE TT_PART_ACCOUNT_IMPORT_HISTORY SET STATUS = 3, AUDIT_REMARK = ? WHERE HISTRORY_ID = ?"); 
					params.add(auditRemark);
					params.add(detail_id);
				
				dao.update(sbSql.toString(), params);
			}
			
			act.setOutData("errcode", 0);
		} catch (Exception e) {
			//BizException e1 = new BizException(act, e,
			//		ErrorCodeConstant.QUERY_FAILURE_CODE, "备件打款审核");
			//logger.error(logonUser, e1);
			//act.setException(e1);
			//act.setOutData("errcode", 1);
			//act.setOutData("msg", "系统错误，请联系管理员！");
			 
			String 	err = e.getMessage();
			//act.setOutData("errcode", 1);
			//act.setOutData("msg", "系统错误，请联系管理员！");
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, err);
			act.setException(e1);
		}
	}
	
	/**
	 * 备件打款审核查询
	 */
	public void xianKuanDakuanAuditQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("pz_no", CommonUtils.checkNull(request.getParamValue("pz_no")));
			condition.put("acount_kind", CommonUtils.checkNull(request.getParamValue("acount_kind")));
			condition.put("dealerIds", CommonUtils.checkNull(request.getParamValue("dealerIds")));
			condition.put("status", CommonUtils.checkNull(request.getParamValue("status")));
			if((Constant.OEM_ACTIVITIES).equals(String.valueOf((logonUser.getOrgId())))){
				condition.put("orgId", CommonUtils.checkNull(logonUser.getOrgId()));
			}else{
				condition.put("orgId", CommonUtils.checkNull(logonUser.getDealerId()));
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryMoneyAudit2(condition, curPage, 15);
			String st = String.valueOf(logonUser.getOrgId());
			act.setOutData("parentOrgId", st);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "备件打款审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

}
