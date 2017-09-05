package com.infodms.dms.dao.parts.salesManager.carFactorySalesManager.boOrderMananger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 紧急订单授权dao
 * @author fanzhineng
 *
 */
@SuppressWarnings("rawtypes")
public class EmergentOrderDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(EmergentOrderDao.class);
	private static final EmergentOrderDao dao = new EmergentOrderDao();
	
	public EmergentOrderDao() {
		super();
	}

	public static final EmergentOrderDao getInstance() {
        return dao;
    }
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 得到经销商信息
	 * @param dealerType 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDealerInfos(Integer dealerType) throws Exception {
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct DEALER_NAME,DEALER_CODE\n");
		sql.append("  FROM TM_DEALER t, tt_part_sales_relation t2\n");
		sql.append(" WHERE t.dealer_id = t2.childorg_id\n");
		sql.append("   and t2.parentorg_id is not null\n");
		sql.append("   and t.PDEALER_TYPE = ?\n");
		sql.append("   and t.STATUS = 10011001"); 
		
		list.add(dealerType);
		List<Map<String, Object>>  ps = pageQuery01(sql.toString(), list, getFunName());
		return ps;
	}

	/**
	 * 根据备件编码获取备件信息有效的
	 * @param subCell
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validatePartInfo(String subCell) throws Exception {
		List<Object> paList = new ArrayList<Object>();
		String sql = "SELECT PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,IS_SALE,OUT_PLAN FROM TT_PART_DEFINE WHERE PART_OLDCODE = ? AND STATE = ?";
		paList.add(subCell);
		paList.add(Constant.STATUS_ENABLE);
		Map<String, Object> ps = pageQueryMap(sql, paList, getFunName());
		return ps;
	}

	/**
	 * 得到紧急订单授权数据
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getEmergentInfos(RequestWrapper request, Integer pageSize, Integer curPage) throws Exception{
		String isIssued = request.getParamValue("IS_ISSUED");//是否下发
		String uploadBatch = request.getParamValue("uploadBatch");//上传批次
		String orderType = request.getParamValue("orderType");
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT DISTINCT\n");
		sbSql.append("  TPEU.UPLOAD_BATCH,\n");
		sbSql.append("  TPEU.CREATE_DATE,\n");
		sbSql.append("  TPEU.ISSUED_DATE,\n");
		sbSql.append("  TPEU.STATE,\n");
		sbSql.append("  TPEU.IS_ISSUED,\n");
		sbSql.append("  (SELECT COUNT(DISTINCT TP.DEALER_ID) FROM TT_PART_EMERGENT_UPLOAD TP WHERE TP.UPLOAD_BATCH = TPEU.UPLOAD_BATCH) AS DEALER_COUNT,\n");
		sbSql.append("  (SELECT COUNT(DISTINCT TP.PART_ID) FROM TT_PART_EMERGENT_UPLOAD TP WHERE TP.UPLOAD_BATCH = TPEU.UPLOAD_BATCH) AS PART_COUNT\n"); 
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_EMERGENT_UPLOAD TPEU\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  1 = 1"); 
		
		if(StringUtil.notNull(isIssued)){
			sbSql.append("  AND TPEU.IS_ISSUED = '"+isIssued+"'\n");
		}
		if(StringUtil.notNull(uploadBatch)){
			sbSql.append("  AND TPEU.UPLOAD_BATCH LIKE ?\n");
			param.add("%"+uploadBatch.toUpperCase()+"%");
		}
		if(StringUtil.notNull(orderType)){
			sbSql.append("  AND TPEU.ORDER_TYPE = ?\n");
			param.add(orderType);
		}
		sbSql.append("GROUP BY\n");
		sbSql.append("  TPEU.UPLOAD_BATCH,\n");
		sbSql.append("  TPEU.DEALER_ID,\n");
		sbSql.append("  TPEU.DEALER_CODE,\n");
		sbSql.append("  TPEU.DEALER_NAME,\n");
		sbSql.append("  TPEU.CREATE_DATE,\n");
		sbSql.append("  TPEU.STATE,\n");
		sbSql.append("  TPEU.ISSUED_DATE,\n");
		sbSql.append("  TPEU.IS_ISSUED\n"); 

		sbSql.append("  ORDER BY TPEU.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 得到紧急订单授权数据(查询详情)
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getEmergentInfosDetail(RequestWrapper request, Integer pageSize, Integer curPage) throws Exception{
		String dealerCode = request.getParamValue("dealerCode");//经销商代码
		String dealerName = request.getParamValue("dealerName");//经销商名称
		String state = request.getParamValue("state");//是否有效
		String partOldcode = request.getParamValue("partOldcode");//备件编码
		String partCode = request.getParamValue("partCode");//件号
		String partCname = request.getParamValue("partCname");//备件名
		String isIssued = request.getParamValue("IS_ISSUED");//是否下发
		String uploadBatch = request.getParamValue("uploadBatch");//上传批次
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT DISTINCT\n");
		sbSql.append("  TPEU.UPLOAD_ID,\n");
		sbSql.append("  TPEU.DEALER_ID,\n");
		sbSql.append("  TPEU.DEALER_CODE,\n");
		sbSql.append("  TPEU.DEALER_NAME,\n");
		sbSql.append("  TPEU.PART_ID,\n");
		sbSql.append("  TPEU.PART_CODE,\n");
		sbSql.append("  TPEU.PART_OLDCODE,\n");
		sbSql.append("  TPEU.PART_CNAME,\n");
		sbSql.append("  TPEU.PART_QTY,\n");
		sbSql.append("  TPEU.STATE,\n");
		sbSql.append("  TPEU.CREATE_DATE,\n");
		sbSql.append("  TPEU.IS_ISSUED,\n");
		sbSql.append("  TPEU.ISSUED_DATE,\n");
		sbSql.append("  TPEU.REMARK,\n");
		sbSql.append("  TPEU.UPLOAD_BATCH\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_EMERGENT_UPLOAD TPEU\n");
		sbSql.append("WHERE\n");
		sbSql.append("  1 = 1\n");
		if(StringUtil.notNull(dealerCode)){
			sbSql.append("  AND TPEU.DEALER_CODE LIKE ?\n");
			param.add("%"+dealerCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("  AND TPEU.DEALER_NAME LIKE ?\n");
			param.add("%"+dealerName+"%");
		}
		if(StringUtil.notNull(state)){
			sbSql.append("  AND TPEU.STATE = ?\n");
			param.add(state);
		}
		
		if(StringUtil.notNull(partOldcode)){
			sbSql.append("  AND TPEU.PART_OLDCODE LIKE ?\n");
			param.add("%"+partOldcode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCode)){
			sbSql.append("  AND TPEU.PART_CODE LIKE ?\n");
			param.add("%"+partCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCname)){
			sbSql.append("  AND TPEU.PART_CNAME LIKE ?\n");
			param.add("%"+partCname+"%");
		}
		
		if(StringUtil.notNull(isIssued)){
			sbSql.append("  AND TPEU.IS_ISSUED = ?\n");
			param.add(isIssued);
		}
		if(StringUtil.notNull(uploadBatch)){
			sbSql.append("  AND TPEU.UPLOAD_BATCH = ?\n");
			param.add(uploadBatch);
		}
		sbSql.append("  ORDER BY TPEU.DEALER_CODE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 根据批次号获取所涉及到的经销商,和他的上级经销商
	 * @param batch
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDealerByUploadBatch(String batch) throws Exception{
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT DISTINCT TPEU.DEALER_ID,\n");
		sql.append("                        TPEU.DEALER_CODE,\n");
		sql.append("                        TPEU.DEALER_NAME,\n");
		sql.append("                        (SELECT TPSR.PARENTORG_ID\n");
		sql.append("                           FROM TT_PART_SALES_RELATION TPSR\n");
		sql.append("                          WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
		sql.append("                            AND ROWNUM = 1) AS PARENT_ID,\n");
		sql.append("                        (SELECT TPSR.PARENTORG_CODE\n");
		sql.append("                           FROM TT_PART_SALES_RELATION TPSR\n");
		sql.append("                          WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
		sql.append("                            AND ROWNUM = 1) AS PARENT_CODE,\n");
		sql.append("                        (SELECT TPSR.PARENTORG_NAME\n");
		sql.append("                           FROM TT_PART_SALES_RELATION TPSR\n");
		sql.append("                          WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
		sql.append("                            AND ROWNUM = 1) AS PARENT_NAME\n");
		sql.append("          FROM TT_PART_EMERGENT_UPLOAD TPEU\n");
		sql.append("         WHERE TPEU.UPLOAD_BATCH = ?) A\n");
		sql.append(" WHERE A.PARENT_ID IS NOT NULL");
		param.add(batch);
		List<Map<String, Object>> ps = pageQuery(sql.toString(), param, getFunName());
		return ps;
	}

	/**
	 * 根据批次号和经销商id获取所涉及到的备件信息
	 * @param batch
	 * @param dealerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPartInfoByBatchAndDealer(String batch,String dealerId) throws Exception{
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT DISTINCT\n");
		sbSql.append("  TPEU.PART_ID,\n");
		sbSql.append("  TPEU.PART_CODE,\n");
		sbSql.append("  TPEU.PART_OLDCODE,\n");
		sbSql.append("  TPEU.PART_CNAME,\n");
		sbSql.append("  (SELECT TPD.UNIT FROM TT_PART_DEFINE TPD WHERE TPD.PART_ID = TPEU.PART_ID) AS UNIT,\n"); 
		sbSql.append("  TPEU.PART_QTY\n");
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_EMERGENT_UPLOAD TPEU\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  TPEU.UPLOAD_BATCH = ?\n");
		sbSql.append("  AND TPEU.DEALER_ID = ?\n"); 
		param.add(batch);
		param.add(dealerId);
		List<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName());
		return ps;
	}

	/**
	 * 获取经销商是与经销商上级是否存在
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateChildOrgcodeAndParent(String dealerCode) throws Exception{
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TD.DEALER_ID,\n");
		sbSql.append("  TD.DEALER_CODE,\n");
		sbSql.append("  TD.DEALER_NAME,\n");
		sbSql.append("  TPSR.PARENTORG_ID AS PARENT_ID \n");
		sbSql.append(" FROM\n");
		sbSql.append("  TM_DEALER TD,TT_PART_SALES_RELATION TPSR \n");
		sbSql.append(" WHERE\n");
		sbSql.append(" TPSR.CHILDORG_ID = TD.DEALER_ID and TD.DEALER_CODE = ?"); 
		param.add(dealerCode);
		Map<String, Object> ps = pageQueryMap(sbSql.toString(), param, getFunName());
		return ps;
	}

	/**
	 * 根据批次号生成主订单信息（紧急订单）
	 * @param batch
	 * @param orderType 
	 * @param isEmpower 
	 * @param loginUser 
	 * @param ipaddr 
	 * @return
	 * @throws Exception
	 */
	public boolean makePartDlrOrderManin(String batch, Integer isEmpower, Integer orderType, AclUserBean loginUser, String ipaddr) throws Exception{
		boolean flag = true;
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String REMARK_INS = request.getParamValue("REMARK_INS");
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_DLR_ORDER_MAIN TPDOMT(\n");
			sbSql.append("  TPDOMT.ORDER_ID,\n");
			sbSql.append("  TPDOMT.ORDER_CODE,\n");
			sbSql.append("  TPDOMT.ORDER_TYPE,\n");
			sbSql.append("  TPDOMT.PAY_TYPE,\n");
			sbSql.append("  TPDOMT.DEALER_ID,\n");
			sbSql.append("  TPDOMT.DEALER_CODE,\n");
			sbSql.append("  TPDOMT.DEALER_NAME,\n");
			sbSql.append("  TPDOMT.SELLER_ID,\n");
			sbSql.append("  TPDOMT.SELLER_CODE,\n");
			sbSql.append("  TPDOMT.SELLER_NAME,\n");
			sbSql.append("  TPDOMT.BUYER_ID,\n");
			sbSql.append("  TPDOMT.BUYER_NAME,\n");
			sbSql.append("  TPDOMT.RCV_ORGID,\n");
			sbSql.append("  TPDOMT.RCV_ORG,\n");
			sbSql.append("  TPDOMT.ADDR_ID,\n");
			sbSql.append("  TPDOMT.ADDR,\n");
			sbSql.append("  TPDOMT.RECEIVER,\n");
			sbSql.append("  TPDOMT.TEL,\n");
			sbSql.append("  TPDOMT.POST_CODE,\n");
			sbSql.append("  TPDOMT.STATION,\n");
			sbSql.append("  TPDOMT.TRANS_TYPE,\n");
			sbSql.append("  TPDOMT.ACCOUNT_SUM,\n");
			sbSql.append("  TPDOMT.ACCOUNT_KY,\n");
			sbSql.append("  TPDOMT.ACCOUNT_DJ,\n");
			sbSql.append("  TPDOMT.ORDER_AMOUNT,\n");
			sbSql.append("  TPDOMT.DISCOUNT,\n");
			sbSql.append("  TPDOMT.REMARK,\n");
			sbSql.append("  TPDOMT.CREATE_DATE,\n");
			sbSql.append("  TPDOMT.CREATE_BY,\n");
			sbSql.append("  TPDOMT.IS_AUTCHK,\n");
			sbSql.append("  TPDOMT.STATE,\n");
			sbSql.append("  TPDOMT.FREIGHT,\n");
			sbSql.append("  TPDOMT.IS_TRANSFREE,\n");
			sbSql.append("  TPDOMT.OEM_FLAG,\n");
			sbSql.append("  TPDOMT.PRODUCE_FAC,\n");
			sbSql.append("  TPDOMT.IS_SUBORDER,\n");
			sbSql.append("  TPDOMT.ACCOUNT_ID,\n");
			sbSql.append("  TPDOMT.BALANCE_AMOUNT,\n");
			sbSql.append("  TPDOMT.SUM_VOLUME,\n");
			sbSql.append("  TPDOMT.IP_ADDRESS,\n");
			sbSql.append("  TPDOMT.IS_EMPOWER,\n");
			sbSql.append("  TPDOMT.UPLOAD_BATCH\n");
			sbSql.append(")\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID AS ORDER_ID,\n");
			sbSql.append("  F_GETID AS ORDER_CODE,\n");
			sbSql.append("  '"+orderType+"',\n");//订单类型紧急
			sbSql.append("  '"+Constant.CAR_FACTORY_SALES_PAY_TYPE_01+"',\n");//付款类型现金
			sbSql.append("  TEMP.DEALER_ID,\n");
			sbSql.append("  TEMP.DEALER_CODE,\n");
			sbSql.append("  TEMP.DEALER_NAME,\n");
			sbSql.append("  TEMP.PARENT_ID,\n");
			sbSql.append("  TEMP.PARENT_CODE,\n");
			sbSql.append("  TEMP.PARENT_NAME,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");//采购人id
			sbSql.append("  '"+loginUser.getName()+"',\n");//采购名称
			sbSql.append("  TEMP.DEALER_ID,\n");
			sbSql.append("  TEMP.DEALER_NAME,\n");
			sbSql.append("  TEMP.ADDR_ID,\n");
			
			sbSql.append(" (SELECT\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.PROVINCE_ID)||\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.CITY_ID)||\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.COUNTIES)||\n");
			sbSql.append("   NVL (TADDR_1.ADDR, '无')\n");
			sbSql.append(" FROM\n");
			sbSql.append("   TT_PART_ADDR_DEFINE TADDR_1\n");
			sbSql.append(" WHERE\n");
			sbSql.append("   TADDR_1.ADDR_ID = TEMP.ADDR_ID) AS ADDR,"); 
			
			//sbSql.append("  TEMP.ADDR,\n");
			sbSql.append("  TEMP.RECEIVER,\n");
			sbSql.append("  TEMP.TEL,\n");
			sbSql.append("  NVL(TEMP.POST_CODE,'无'),\n");
			sbSql.append("  NVL(TEMP.STATION,'无'),\n");
			sbSql.append("  9,\n");//快递
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_SUM\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("\n");
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_KY\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_DJ\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("  0,\n");
			sbSql.append("  TEMP.DISCOUNT_ZKL,\n");
			sbSql.append("  '已授权，总部下发！"+REMARK_INS+"',\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");
			sbSql.append("  10041002,\n");
			sbSql.append("  92161001,\n");
			sbSql.append("  0,\n");
			sbSql.append("  10041001,\n");
			sbSql.append("  10041002,\n");
			sbSql.append("  9703001,\n");
			sbSql.append("  10041002,\n");
			sbSql.append("  (SELECT TPAD1.ACCOUNT_ID FROM TT_PART_ACCOUNT_DEFINE TPAD1 WHERE TPAD1.CHILDORG_ID = TEMP.DEALER_ID AND TPAD1.PARENTORG_ID=TEMP.PARENT_ID AND TPAD1.ACCOUNT_PURPOSE='"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"' AND ROWNUM = 1) AS ACCOUNT_ID,\n");
			sbSql.append("  0,\n");
			sbSql.append("  0,\n");
			sbSql.append("  '"+ipaddr+"',\n");
			sbSql.append("  '"+isEmpower+"',\n");
			sbSql.append("  TEMP.UPLOAD_BATCH\n");
			sbSql.append("FROM (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      DISTINCT\n");
			sbSql.append("      TPEU.DEALER_ID,\n");
			sbSql.append("      TPEU.DEALER_CODE,\n");
			sbSql.append("      TPEU.DEALER_NAME,\n");
			sbSql.append("      TPEU.UPLOAD_BATCH,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_ID FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_ID,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_CODE FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_CODE,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_NAME FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_NAME,\n");
			sbSql.append("      (SELECT NVL(TPSR.DISCOUNT,1) FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS DISCOUNT_ZKL,\n");
			sbSql.append("      (SELECT TPAD.ADDR_ID FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS ADDR_ID,\n");
			sbSql.append("      (SELECT TPAD.ADDR FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS ADDR,\n");
			sbSql.append("      (SELECT TPAD.LINKMAN FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS RECEIVER,\n");
			sbSql.append("      (SELECT TPAD.TEL FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS TEL,\n");
			sbSql.append("      (SELECT TPAD.POST_CODE FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS POST_CODE,\n");
			sbSql.append("      (SELECT TPAD.STATION FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS STATION\n");
			sbSql.append("    FROM\n");
			sbSql.append("      TT_PART_EMERGENT_UPLOAD TPEU\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      TPEU.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append(") TEMP"); 
			dao.insert(sbSql.toString());
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 根据批次批量生成详细信息
	 * @param batch
	 * @param loginUser 
	 * @param orderType 
	 * @return
	 * @throws Exception
	 */
	public boolean makePartDlrOrderDtl(String batch, Integer orderType, AclUserBean loginUser) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_DLR_ORDER_DTL TEMP(\n");
			sbSql.append("  TEMP.LINE_ID,\n");
			sbSql.append("  TEMP.ORDER_ID,\n");
			sbSql.append("  TEMP.PART_ID,\n");
			sbSql.append("  TEMP.PART_CODE,\n");
			sbSql.append("  TEMP.PART_OLDCODE,\n");
			sbSql.append("  TEMP.PART_CNAME,\n");
			sbSql.append("  TEMP.UNIT,\n");
			sbSql.append("  TEMP.IS_DIRECT,\n");
			sbSql.append("  TEMP.IS_PLAN,\n");
			sbSql.append("  TEMP.IS_LACK,\n");
			sbSql.append("  TEMP.IS_REPLACED,\n");
			sbSql.append("  TEMP.STOCK_QTY,\n");
			sbSql.append("  TEMP.MIN_PACKAGE,\n");
			sbSql.append("  TEMP.BUY_QTY,\n");
			sbSql.append("  TEMP.BUY_PRICE,\n");
			sbSql.append("  TEMP.BUY_AMOUNT,\n");
			sbSql.append("  TEMP.IS_HAVA,\n");
			sbSql.append("  TEMP.CREATE_DATE,\n");
			sbSql.append("  TEMP.CREATE_BY,\n");
			sbSql.append("  TEMP.STATUS,\n");
			sbSql.append("  TEMP.EXPECT_ARRIVAL_DATE,\n");
			sbSql.append("  TEMP.VOLUME,\n");
			sbSql.append("  TEMP.BUY_VOLUME,\n");
			sbSql.append("  TEMP.BUY_PRICE1,\n");
			sbSql.append("  TEMP.PART_DISCOUNT,\n");
			sbSql.append("  TEMP.IS_DISCOUNT)\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID,\n");
			sbSql.append("  TEMP2.ORDER_ID,\n");
			sbSql.append("  TEMP2.PART_ID,\n");
			sbSql.append("  TEMP2.PART_CODE,\n");
			sbSql.append("  TEMP2.PART_OLDCODE,\n");
			sbSql.append("  TEMP2.PART_CNAME,\n");
			sbSql.append("  TEMP2.UNIT,\n");
			sbSql.append("  TEMP2.IS_DIRECT,\n");
			sbSql.append("  TEMP2.IS_PLAN,\n");
			sbSql.append("  TEMP2.IS_LACK,\n");
			sbSql.append("  TEMP2.IS_REPLACED,\n");
			sbSql.append("  TEMP2.ITEM_QTY,\n");
			sbSql.append("  TEMP2.MIN_PACKAGE,\n");
			sbSql.append("  TEMP2.PART_QTY,\n");
			sbSql.append("  TEMP2.SALE_PRICE1,\n");
			sbSql.append("  (TEMP2.PART_QTY * TEMP2.DISCOUNT),\n");
			sbSql.append("  TEMP2.UPORGSTOCk,\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");
			sbSql.append("  10011001,\n");
			sbSql.append("  TO_DATE('2016-12-01','YYYY-MM-DD'),\n");
			sbSql.append("  TEMP2.VOLUME,\n");
			sbSql.append("  (TEMP2.PART_QTY*TEMP2.VOLUME),\n");
			sbSql.append("  TEMP2.DISCOUNT,\n");
			sbSql.append("  TEMP2.PART_DISCOUNT,\n");
			sbSql.append("  TEMP2.IS_DISCOUNT\n");
			sbSql.append("FROM\n");
			sbSql.append("(\n");
			sbSql.append("        SELECT\n");
			sbSql.append("          TEM.DEALER_ID,\n");
			sbSql.append("          TEM.PARENT_ID,\n");
			sbSql.append("          TEM.PART_QTY,\n");
			sbSql.append("          TEM.DISCOUNT_ZKL,\n");
			sbSql.append("          TEM.ORDER_ID,\n");
			sbSql.append("          A .PART_ID,\n");
			sbSql.append("          NVL (A .IS_REPLACED, 10041002) AS IS_REPLACED,\n");
			sbSql.append("          NVL (A .IS_LACK, 10041002) AS IS_LACK,\n");
			sbSql.append("          A .PART_CODE,\n");
			sbSql.append("          NVL (A .IS_DIRECT, 10041002) AS IS_DIRECT,\n");
			sbSql.append("          NVL (A .IS_PLAN, 10041002) AS IS_PLAN,\n");
			sbSql.append("          A .PART_OLDCODE,\n");
			sbSql.append("          A .PART_CNAME,\n");
			sbSql.append("          A .STATE,\n");
			sbSql.append("          NVL(A.VOLUME,0) AS VOLUME,\n");
			sbSql.append("          A .MODEL_NAME,\n");
			sbSql.append("          A .PIC_URL,\n");
			sbSql.append("          A .REMARK,\n");
			sbSql.append("          NVL (A .UNIT, '件') UNIT,\n");
			sbSql.append("          NVL((SELECT SUM (S.normal_QTY) FROM VW_PART_STOCK S\n");
			sbSql.append("              WHERE\n");
			sbSql.append("              S.PART_ID = A .PART_ID\n");
			sbSql.append("              AND S.ORG_ID = TEM.DEALER_ID--当前用户dealer_id\n");
			sbSql.append("              AND S.STATE = 10011001\n");
			sbSql.append("              AND S.STATUS = 1\n");
			sbSql.append("            ),\n");
			sbSql.append("            0\n");
			sbSql.append("          ) AS ITEM_QTY,\n");
			sbSql.append("          NVL (A .PART_DISCOUNT, 1) PART_DISCOUNT,\n");
			sbSql.append("          A .IS_DISCOUNT,\n");
			sbSql.append("          F_get_min_package ("+orderType+", A .PART_ID, TEM.DEALER_ID) AS MIN_PACKAGE,--订单类型，备件id，当前用户dealer_id\n");
			sbSql.append("          TO_CHAR ( PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID), 'FM999999990.000000') AS SALE_PRICE1,--当前用户dealer_id，备件id\n");
			sbSql.append("          (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("              CASE\n");
			sbSql.append("            WHEN SUM (normal_QTY) > 0 THEN\n");
			sbSql.append("              '92181001'\n");
			sbSql.append("            WHEN SUM (normal_QTY) = 0 THEN\n");
			sbSql.append("              '92181002'\n");
			sbSql.append("            ELSE\n");
			sbSql.append("              '92181002'\n");
			sbSql.append("            END\n");
			sbSql.append("            FROM\n");
			sbSql.append("              VW_PART_STOCK E\n");
			sbSql.append("            WHERE\n");
			sbSql.append("              E .PART_ID = A .PART_ID\n");
			sbSql.append("            AND E .STATE = 10011001\n");
			sbSql.append("            AND E .STATUS = 1\n");
			sbSql.append("            AND E .ORG_ID = TEM.PARENT_ID--销售单位id\n");
			sbSql.append("          ) AS UPORGSTOCK,\n");
			sbSql.append("          NVL (\n");
			sbSql.append("            (\n");
			sbSql.append("              SELECT\n");
			sbSql.append("                QTY\n");
			sbSql.append("              FROM\n");
			sbSql.append("                VW_PART_DLR_THM_PER_SALE VPDTPS\n");
			sbSql.append("              WHERE\n");
			sbSql.append("                VPDTPS.PART_ID = A .PART_ID\n");
			sbSql.append("              AND ORG_ID = TEM.DEALER_ID\n");
			sbSql.append("            ),\n");
			sbSql.append("            '0'\n");
			sbSql.append("          ) AS qty,\n");
			sbSql.append("          NVL (A .is_special, '10041002') AS is_special,\n");
			sbSql.append("\n");
			sbSql.append("          TO_CHAR (\n");
			sbSql.append("            DECODE (\n");
			sbSql.append("              A .IS_DISCOUNT,\n");
			sbSql.append("              93111001,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * TEM.DISCOUNT_ZKL,\n");
			sbSql.append("             93111002,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * NVL (A .PART_DISCOUNT, 1),\n");
			sbSql.append("              93111003,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * TEM.DISCOUNT_ZKL * NVL (A .PART_DISCOUNT, 1)\n");
			sbSql.append("            ),\n");
			sbSql.append("            'FM999999990.000000'\n");
			sbSql.append("          ) AS DISCOUNT\n");
			sbSql.append("\n");
			sbSql.append("        FROM\n");
			sbSql.append("          TT_PART_DEFINE A,\n");
			sbSql.append("          (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("              DISTINCT\n");
			sbSql.append("              TPDOM1.ORDER_ID,\n");
			sbSql.append("              TPEU.DEALER_ID,\n");
			sbSql.append("              TPEU.DEALER_CODE,\n");
			sbSql.append("              TPEU.DEALER_NAME,\n");
			sbSql.append("              TPEU.UPLOAD_BATCH,\n");
			sbSql.append("              TPEU.PART_ID,\n");
			sbSql.append("              TPEU.PART_QTY,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_ID FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_ID,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_CODE FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_CODE,\n");
			sbSql.append("              (SELECT NVL(TPSR.DISCOUNT,1) FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS DISCOUNT_ZKL,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_NAME FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_NAME\n");
			sbSql.append("            FROM\n");
			sbSql.append("              TT_PART_EMERGENT_UPLOAD TPEU,\n");
			sbSql.append("              TT_PART_DLR_ORDER_MAIN TPDOM1\n");
			sbSql.append("            WHERE\n");
			sbSql.append("              TPEU.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("              AND TPDOM1.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("              AND TPDOM1.DEALER_ID = TPEU.DEALER_ID\n");
			sbSql.append("          )TEM\n");
			sbSql.append("        WHERE\n");
			sbSql.append("          A .STATUS = 1\n");
			sbSql.append("        AND TEM.PART_ID = A.PART_ID\n");
			if(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12.toString().equals(orderType) ){ //|| Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_13.toString().equals(orderType)
				//用品判断总部销售，分公司销售，备件不判断
				sbSql.append("        AND A .IS_SALE = '10041001'\n");
				sbSql.append("        AND A.OUT_PLAN = '10041001'\n");
			}
			
			sbSql.append("        AND NOT EXISTS (\n");
			sbSql.append("          SELECT\n");
			sbSql.append("            1\n");
			sbSql.append("          FROM\n");
			sbSql.append("            VW_PART_STOCK S\n");
			sbSql.append("          WHERE\n");
			sbSql.append("            S.PDSTATE = 10011002\n");
			sbSql.append("          AND S.ORG_ID = TEM.PARENT_ID\n");
			sbSql.append("          AND S.PART_ID = A .PART_ID\n");
			sbSql.append("          HAVING\n");
			sbSql.append("            SUM (S.NORMAL_QTY) = 0\n");
			sbSql.append("        )\n");
			sbSql.append("        AND A .IS_SPECIAL = 10041002\n");
			sbSql.append("        AND A .ARTICLES_TYPE = 92931005\n");
			sbSql.append("        AND NOT EXISTS (\n");
			sbSql.append("          SELECT\n");
			sbSql.append("            part_id\n");
			sbSql.append("          FROM\n");
			sbSql.append("            tt_part_STO_DEFINE tpsd\n");
			sbSql.append("          WHERE\n");
			sbSql.append("            A .part_id = tpsd.part_id\n");
			sbSql.append("          AND tpsd.state = 10011001\n");
			sbSql.append("          AND tpsd.status = 1\n");
			sbSql.append("        )\n");
			sbSql.append("        AND A .STATE = 10011001\n");
			sbSql.append(")TEMP2"); 
			dao.insert(sbSql.toString());
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 根据批次修改订单总金额，总体积
	 * @param batch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean updatePartDlrOrder(String batch) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT DISTINCT\n");
			sbSql.append("    TPDOD.ORDER_ID,\n");
			sbSql.append("    TPDOM.DEALER_ID,\n");
			sbSql.append("    SUM (TPDOD.BUY_VOLUME) SVO,\n");
			sbSql.append("    SUM (TPDOD.BUY_AMOUNT) SPR\n");
			sbSql.append("  FROM\n");
			sbSql.append("    TT_PART_DLR_ORDER_MAIN TPDOM,\n");
			sbSql.append("    TT_PART_DLR_ORDER_DTL TPDOD\n");
			sbSql.append("  WHERE\n");
			sbSql.append("    TPDOM.ORDER_ID = TPDOD.ORDER_ID\n");
			sbSql.append("  AND TPDOM.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("  GROUP BY\n");
			sbSql.append("    TPDOD.ORDER_ID,TPDOM.DEALER_ID"); 

			
			List<Map<String,Object>> list = pageQuery(sbSql.toString(), null, getFunName());
			for (Map<String,Object> map : list) {
				
				String orderId = map.get("ORDER_ID").toString();
				String dealerId = map.get("DEALER_ID").toString();
				String svo = map.get("SVO").toString();
				String spr = map.get("SPR").toString();
				String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_16,dealerId);
				sbSql = new StringBuffer();
				sbSql.append("UPDATE TT_PART_DLR_ORDER_MAIN TPDOM\n");
				sbSql.append(" SET TPDOM.SUM_VOLUME = '"+svo+"',\n");
				sbSql.append(" TPDOM.BALANCE_AMOUNT = '"+spr+"',\n");
				sbSql.append(" TPDOM.ORDER_AMOUNT = '"+spr+"',\n");
				sbSql.append(" TPDOM.ORDER_CODE = '"+orderCode+"'\n");
				sbSql.append("WHERE\n");
				sbSql.append(" TPDOM.ORDER_ID = '"+orderId+"'"); 
				dao.update(sbSql.toString(), null);
			}
			
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 根据批次批量生成历史记录
	 * @param batch
	 * @return
	 * @throws Exception
	 */
	public boolean makePartDlrOrderHistory(String batch)throws Exception {
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_OPERATION_HISTORY HIS(\n");
			sbSql.append("  HIS.OPT_ID,\n");
			sbSql.append("  HIS.BUSSINESS_ID,\n");
			sbSql.append("  HIS.OPT_BY,\n");
			sbSql.append("  HIS.OPT_NAME,\n");
			sbSql.append("  HIS.OPT_DATE,\n");
			sbSql.append("  HIS.WHAT,\n");
			sbSql.append("  HIS.STATUS,\n");
			sbSql.append("  HIS.OPT_TYPE,\n");
			sbSql.append("  HIS.ORDER_ID\n");
			sbSql.append(")\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID,\n");
			sbSql.append("  TEMP.ORDER_CODE,\n");
			sbSql.append("  TEMP.BUYER_ID,\n");
			sbSql.append("  TEMP.BUYER_NAME,\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '紧急订单下发！',\n");
			sbSql.append("  92161001,\n");
			sbSql.append("  93181001,\n");
			sbSql.append("  TEMP.ORDER_ID\n");
			sbSql.append("FROM(\n");
			sbSql.append("  SELECT\n");
			sbSql.append("    DISTINCT\n");
			sbSql.append("    TPDOM.ORDER_CODE,\n");
			sbSql.append("    TPDOM.DEALER_ID,\n");
			sbSql.append("    TPDOM.BUYER_ID,\n");
			sbSql.append("    TPDOM.BUYER_NAME,\n");
			sbSql.append("    TPDOM.ORDER_ID\n");
			sbSql.append("  FROM\n");
			sbSql.append("    TT_PART_DLR_ORDER_MAIN TPDOM\n");
			sbSql.append("  WHERE\n");
			sbSql.append("    TPDOM.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append(")TEMP"); 
			dao.insert(sbSql.toString());
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 根据批次号获取涉及到的服务商
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getDealerBybatch(RequestWrapper request, Integer pageSize, Integer curPage)throws Exception {
		String batch = request.getParamValue("uploadBatch");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName = request.getParamValue("dealerName");
		String IS_ISSUED = request.getParamValue("IS_ISSUED");
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  DISTINCT\n");
		sbSql.append("  TEMP1.DEALER_ID,\n");
		sbSql.append("  TEMP1.DEALER_CODE,\n");
		sbSql.append("  TEMP1.DEALER_NAME,\n");
		sbSql.append("  TEMP1.IS_ISSUED,\n");
		sbSql.append("  TO_CHAR(SUM(TEMP1.SUM_PART_PRICE),'FM999999999999990.00') AS SUM_MONEY,\n");
		sbSql.append("  COUNT(TEMP1.PART_ID) AS SUM_PART,\n");
		sbSql.append("  SUM(TEMP1.PART_QTY) AS SUM_QTY,\n");
		sbSql.append("  NVL(TEMP1.ACCOUNT_ID,0) AS ACCOUNT_ID,\n");
		sbSql.append("  NVL(TEMP1.ACCOUNT_KY,0) AS ACCOUNT_KY\n");
		sbSql.append("FROM(\n");
		sbSql.append("  SELECT\n");
		sbSql.append("    TEMP.DEALER_ID,\n");
		sbSql.append("    TEMP.DEALER_CODE,\n");
		sbSql.append("    TEMP.DEALER_NAME,\n");
		sbSql.append("    TEMP.IS_ISSUED,\n");
		sbSql.append("    TEMP.PART_ID,\n");
		sbSql.append("    TEMP.PART_QTY,\n");
		sbSql.append("    TO_CHAR(\n");
		sbSql.append("      DECODE(\n");
		sbSql.append("        TEMP.IS_DISCOUNT,\n");
		sbSql.append("        93111001,\n");
		sbSql.append("        TEMP.PART_PRICE * TEMP.DEALER_DISCOUNT * TEMP.PART_QTY,\n");
		sbSql.append("        93111002,\n");
		sbSql.append("        TEMP.PART_PRICE * TEMP.PART_DISCOUNT * TEMP.PART_QTY,\n");
		sbSql.append("        93111003,\n");
		sbSql.append("        TEMP.PART_PRICE * TEMP.DEALER_DISCOUNT * TEMP.PART_DISCOUNT * TEMP.PART_QTY\n");
		sbSql.append("      ) ,'FM999999999999990.00')AS SUM_PART_PRICE,\n");
		sbSql.append("    TEMP.ACCOUNT_ID,\n");
		sbSql.append("    TEMP.ACCOUNT_KY\n");
		sbSql.append("  FROM\n");
		sbSql.append("  (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("      TPEU.DEALER_ID,\n");
		sbSql.append("      TPEU.DEALER_CODE,\n");
		sbSql.append("      TPEU.DEALER_NAME,\n");
		sbSql.append("      TPSR.PARENTORG_ID,\n");
		sbSql.append("      TPEU.IS_ISSUED,\n");
		sbSql.append("      TPEU.PART_ID,\n");
		sbSql.append("      TPEU.PART_OLDCODE,\n");
		sbSql.append("      TPD.IS_DISCOUNT,--折扣类型\n");
		sbSql.append("      NVL(PKG_PART.F_GETPRICE (TPEU.DEALER_ID, TPEU.PART_ID),0) AS PART_PRICE,--单价\n");
		sbSql.append("      F_GET_DEALER_DISCOUNT(92151002,TPEU.DEALER_ID,TPSR.PARENTORG_ID) AS DEALER_DISCOUNT,--常规订单，经销商折扣率\n");
		sbSql.append("      F_GET_PART_DISCOUNT(TPEU.PART_ID, TPEU.DEALER_ID, TPSR.PARENTORG_ID) AS PART_DISCOUNT,--单品折扣率\n");
		sbSql.append("      TPEU.PART_QTY,\n");
		sbSql.append("      (SELECT VPDA.ACCOUNT_ID FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_ID,\n");
		sbSql.append("      (SELECT VPDA.ACCOUNT_KY FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_KY\n");
		sbSql.append("    FROM\n");
		sbSql.append("      TT_PART_EMERGENT_UPLOAD TPEU,\n");
		sbSql.append("      TT_PART_SALES_RELATION TPSR,\n");
		sbSql.append("      TT_PART_DEFINE TPD\n");
		sbSql.append("    WHERE\n");
		sbSql.append("      TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
		sbSql.append("      AND TPD.PART_ID = TPEU.PART_ID\n");
		
		sbSql.append("      AND TPEU.UPLOAD_BATCH = ?\n");
		if(StringUtil.notNull(batch)){
			params.add(batch);
		}else{
			return null;
		}
		if(StringUtil.notNull(dealerCode)){
			sbSql.append("      AND TPEU.DEALER_CODE = ?\n");
			params.add("%"+dealerCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("      AND TPEU.DEALER_NAME LIKE ?\n");
			params.add("%"+dealerName+"%");
		}
		if(StringUtil.notNull(IS_ISSUED)){
			sbSql.append("      AND TPEU.IS_ISSUED = ?\n");
			params.add(IS_ISSUED);
		}
		sbSql.append("  )TEMP\n");
		sbSql.append(")TEMP1\n");
		sbSql.append("GROUP BY\n");
		sbSql.append("  TEMP1.DEALER_ID,\n");
		sbSql.append("  TEMP1.DEALER_CODE,\n");
		sbSql.append("  TEMP1.DEALER_NAME,\n");
		sbSql.append("  TEMP1.IS_ISSUED,\n");
		sbSql.append("  TEMP1.ACCOUNT_ID,\n");
		sbSql.append("  TEMP1.ACCOUNT_KY\n"); 
		sbSql.append("  ORDER BY\n");
		sbSql.append("  TEMP1.DEALER_CODE ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 得到铺货订单数据
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getDistributionInfos(RequestWrapper request, Integer pageSize, Integer curPage) {
		String uploadBatch = request.getParamValue("uploadBatch");//上传批次
		String orderType = request.getParamValue("orderType");
		String IS_ISSUED = request.getParamValue("IS_ISSUED");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT DISTINCT\n");
		sbSql.append("  TPEU.UPLOAD_BATCH,\n");
		sbSql.append("  TPEU.CREATE_DATE,\n");
		sbSql.append("  TPEU.STATE,\n");
		sbSql.append("  TPEU.IS_ISSUED,\n");
		sbSql.append("  (SELECT (CASE WHEN (COUNT(DISTINCT TP.IS_ISSUED))>1 THEN '部分' ELSE '全部' END) FROM TT_PART_EMERGENT_UPLOAD TP WHERE TP.UPLOAD_BATCH = TPEU.UPLOAD_BATCH) AS ISSUED_COUNT,");
		sbSql.append("  (SELECT COUNT(DISTINCT TP.DEALER_ID) FROM TT_PART_EMERGENT_UPLOAD TP WHERE TP.UPLOAD_BATCH = TPEU.UPLOAD_BATCH) AS DEALER_COUNT,\n");
		sbSql.append("  (SELECT COUNT(DISTINCT TP.PART_ID) FROM TT_PART_EMERGENT_UPLOAD TP WHERE TP.UPLOAD_BATCH = TPEU.UPLOAD_BATCH) AS PART_COUNT\n"); 
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_EMERGENT_UPLOAD TPEU\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  1 = 1"); 
		
		if(StringUtil.notNull(uploadBatch)){
			sbSql.append("  AND TPEU.UPLOAD_BATCH LIKE ?\n");
			param.add("%"+uploadBatch.toUpperCase()+"%");
		}
		if(StringUtil.notNull(orderType)){
			sbSql.append("  AND TPEU.ORDER_TYPE = ?\n");
			param.add(orderType);
		}
		if(StringUtil.notNull(IS_ISSUED)){
			sbSql.append("  AND TPEU.IS_ISSUED = ?\n");
			param.add(IS_ISSUED);
		}
		sbSql.append("GROUP BY\n");
		sbSql.append("  TPEU.UPLOAD_BATCH,\n");
		sbSql.append("  TPEU.DEALER_ID,\n");
		sbSql.append("  TPEU.DEALER_CODE,\n");
		sbSql.append("  TPEU.DEALER_NAME,\n");
		sbSql.append("  TPEU.CREATE_DATE,\n");
		sbSql.append("  TPEU.IS_ISSUED,\n");
		sbSql.append("  TPEU.STATE\n");

		sbSql.append("  ORDER BY TPEU.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 批量生成铺货主数据
	 * @param batch
	 * @param dealerId
	 * @param orderType 
	 * @param loginUser 
	 * @param ipaddr 
	 * @param isEmpower 
	 * @return
	 */
	public boolean makeMainInfo(String batch, String dealerId, AclUserBean loginUser, Integer orderType, Integer isEmpower, String ipaddr) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_DLR_ORDER_MAIN TPDOMT(\n");
			sbSql.append("  TPDOMT.ORDER_ID,\n");
			sbSql.append("  TPDOMT.ORDER_CODE,\n");
			sbSql.append("  TPDOMT.ORDER_TYPE,\n");
			sbSql.append("  TPDOMT.PAY_TYPE,\n");
			sbSql.append("  TPDOMT.DEALER_ID,\n");
			sbSql.append("  TPDOMT.DEALER_CODE,\n");
			sbSql.append("  TPDOMT.DEALER_NAME,\n");
			sbSql.append("  TPDOMT.SELLER_ID,\n");
			sbSql.append("  TPDOMT.SELLER_CODE,\n");
			sbSql.append("  TPDOMT.SELLER_NAME,\n");
			sbSql.append("  TPDOMT.BUYER_ID,\n");
			sbSql.append("  TPDOMT.BUYER_NAME,\n");
			sbSql.append("  TPDOMT.RCV_ORGID,\n");
			sbSql.append("  TPDOMT.RCV_ORG,\n");
			sbSql.append("  TPDOMT.ADDR_ID,\n");
			sbSql.append("  TPDOMT.ADDR,\n");
			sbSql.append("  TPDOMT.RECEIVER,\n");
			sbSql.append("  TPDOMT.TEL,\n");
			sbSql.append("  TPDOMT.POST_CODE,\n");
			sbSql.append("  TPDOMT.STATION,\n");
			sbSql.append("  TPDOMT.TRANS_TYPE,\n");
			sbSql.append("  TPDOMT.ACCOUNT_SUM,\n");
			sbSql.append("  TPDOMT.ACCOUNT_KY,\n");
			sbSql.append("  TPDOMT.ACCOUNT_DJ,\n");
			sbSql.append("  TPDOMT.ORDER_AMOUNT,\n");
			sbSql.append("  TPDOMT.DISCOUNT,\n");
			sbSql.append("  TPDOMT.REMARK,\n");
			sbSql.append("  TPDOMT.CREATE_DATE,\n");
			sbSql.append("  TPDOMT.CREATE_BY,\n");
			sbSql.append("  TPDOMT.SUBMIT_DATE,\n");
			sbSql.append("  TPDOMT.SUBMIT_BY,\n");
			sbSql.append("  TPDOMT.IS_AUTCHK,\n");
			sbSql.append("  TPDOMT.STATE,\n");
			sbSql.append("  TPDOMT.FREIGHT,\n");
			sbSql.append("  TPDOMT.IS_TRANSFREE,\n");
			sbSql.append("  TPDOMT.OEM_FLAG,\n");
			sbSql.append("  TPDOMT.PRODUCE_FAC,\n");
			sbSql.append("  TPDOMT.IS_SUBORDER,\n");
			sbSql.append("  TPDOMT.ACCOUNT_ID,\n");
			sbSql.append("  TPDOMT.BALANCE_AMOUNT,\n");
			sbSql.append("  TPDOMT.SUM_VOLUME,\n");
			sbSql.append("  TPDOMT.IP_ADDRESS,\n");
			sbSql.append("  TPDOMT.IS_EMPOWER,\n");
			sbSql.append("  TPDOMT.UPLOAD_BATCH\n");
			sbSql.append(")\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID AS ORDER_ID,\n");
			sbSql.append("  F_GETID AS ORDER_CODE,\n");
			sbSql.append("  '"+orderType+"',\n");//订单类型紧急
			sbSql.append("  '"+Constant.CAR_FACTORY_SALES_PAY_TYPE_01+"',\n");//付款类型现金
			sbSql.append("  TEMP.DEALER_ID,\n");
			sbSql.append("  TEMP.DEALER_CODE,\n");
			sbSql.append("  TEMP.DEALER_NAME,\n");
			sbSql.append("  TEMP.PARENT_ID,\n");
			sbSql.append("  TEMP.PARENT_CODE,\n");
			sbSql.append("  TEMP.PARENT_NAME,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");//采购人id
			sbSql.append("  '"+loginUser.getName()+"',\n");//采购名称
			sbSql.append("  TEMP.DEALER_ID,\n");
			sbSql.append("  TEMP.DEALER_NAME,\n");
			sbSql.append("  TEMP.ADDR_ID,\n");
			
			sbSql.append(" (SELECT\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.PROVINCE_ID)||\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.CITY_ID)||\n");
			sbSql.append("   (SELECT DECODE(TMR_1.REGION_NAME,'','',TMR_1.REGION_NAME||'-') FROM TM_REGION TMR_1 WHERE TMR_1.REGION_CODE = TADDR_1.COUNTIES)||\n");
			sbSql.append("   NVL (TADDR_1.ADDR, '无')\n");
			sbSql.append(" FROM\n");
			sbSql.append("   TT_PART_ADDR_DEFINE TADDR_1\n");
			sbSql.append(" WHERE\n");
			sbSql.append("   TADDR_1.ADDR_ID = TEMP.ADDR_ID) AS ADDR,"); 
			
			//sbSql.append("  TEMP.ADDR,\n");
			sbSql.append("  TEMP.RECEIVER,\n");
			sbSql.append("  TEMP.TEL,\n");
			sbSql.append("  NVL(TEMP.POST_CODE,'无'),\n");
			sbSql.append("  NVL(TEMP.STATION,'无'),\n");
			sbSql.append("  4,\n");//汽运
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_SUM\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("\n");
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_KY\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      VPDA.ACCOUNT_DJ\n");
			sbSql.append("    FROM\n");
			sbSql.append("      VW_PART_DLR_ACCOUNT VPDA\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      VPDA.DEALER_ID = TEMP.DEALER_ID\n");
			sbSql.append("    AND VPDA.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("    AND VPDA.ACCOUNT_ID =(\n");
			sbSql.append("      SELECT\n");
			sbSql.append("        TPAD1.ACCOUNT_ID\n");
			sbSql.append("      FROM\n");
			sbSql.append("        TT_PART_ACCOUNT_DEFINE TPAD1\n");
			sbSql.append("      WHERE\n");
			sbSql.append("        TPAD1.CHILDORG_ID = TEMP.DEALER_ID\n");
			sbSql.append("      AND TPAD1.PARENTORG_ID = TEMP.PARENT_ID\n");
			sbSql.append("      AND TPAD1.ACCOUNT_PURPOSE = '"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"'\n");//普通备件款
			sbSql.append("      AND ROWNUM = 1\n");
			sbSql.append("    )\n");
			sbSql.append("  ),\n");
			sbSql.append("  0,\n");
			sbSql.append("  TEMP.DISCOUNT_ZKL,\n");
			sbSql.append("  '总部铺货！',\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");
			sbSql.append("  10041002,\n");
			sbSql.append("  '"+Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02+"',\n");//保存、提交
			sbSql.append("  0,\n");
			sbSql.append("  10041001,\n");
			sbSql.append("  '"+Constant.IF_TYPE_YES+"',\n");//总部铺货oem_a是
			sbSql.append("  9703001,\n");
			sbSql.append("  10041002,\n");
			sbSql.append("  (SELECT TPAD1.ACCOUNT_ID FROM TT_PART_ACCOUNT_DEFINE TPAD1 WHERE TPAD1.CHILDORG_ID = TEMP.DEALER_ID AND TPAD1.PARENTORG_ID=TEMP.PARENT_ID AND TPAD1.ACCOUNT_PURPOSE='"+Constant.PART_ACCOUNT_PURPOSE_TYPE_01+"' AND ROWNUM = 1) AS ACCOUNT_ID,\n");
			sbSql.append("  0,\n");
			sbSql.append("  0,\n");
			sbSql.append("  '"+ipaddr+"',\n");
			sbSql.append("  '"+isEmpower+"',\n");
			sbSql.append("  TEMP.UPLOAD_BATCH\n");
			sbSql.append("FROM (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      DISTINCT\n");
			sbSql.append("      TPEU.DEALER_ID,\n");
			sbSql.append("      TPEU.DEALER_CODE,\n");
			sbSql.append("      TPEU.DEALER_NAME,\n");
			sbSql.append("      TPEU.UPLOAD_BATCH,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_ID FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_ID,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_CODE FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_CODE,\n");
			sbSql.append("      (SELECT TPSR.PARENTORG_NAME FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_NAME,\n");
			sbSql.append("      (SELECT NVL(TPSR.DISCOUNT,1) FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS DISCOUNT_ZKL,\n");
			sbSql.append("      (SELECT TPAD.ADDR_ID FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS ADDR_ID,\n");
			sbSql.append("      (SELECT TPAD.ADDR FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS ADDR,\n");
			sbSql.append("      (SELECT TPAD.LINKMAN FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS RECEIVER,\n");
			sbSql.append("      (SELECT TPAD.TEL FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS TEL,\n");
			sbSql.append("      (SELECT TPAD.POST_CODE FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS POST_CODE,\n");
			sbSql.append("      (SELECT TPAD.STATION FROM TT_PART_ADDR_DEFINE TPAD WHERE TPAD.DEALER_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS STATION\n");
			sbSql.append("    FROM\n");
			sbSql.append("      TT_PART_EMERGENT_UPLOAD TPEU\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      TPEU.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("      AND TPEU.DEALER_ID IN ("+dealerId+")\n");
			sbSql.append(") TEMP"); 
			dao.insert(sbSql.toString());
		}catch(Exception e){
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 批量生成铺货详细数据
	 * @param batch
	 * @param orderType
	 * @param loginUser
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	public boolean makeDtlInfo(String batch, Integer orderType,AclUserBean loginUser, String dealerId) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_DLR_ORDER_DTL TEMP(\n");
			sbSql.append("  TEMP.LINE_ID,\n");
			sbSql.append("  TEMP.ORDER_ID,\n");
			sbSql.append("  TEMP.PART_ID,\n");
			sbSql.append("  TEMP.PART_CODE,\n");
			sbSql.append("  TEMP.PART_OLDCODE,\n");
			sbSql.append("  TEMP.PART_CNAME,\n");
			sbSql.append("  TEMP.UNIT,\n");
			sbSql.append("  TEMP.IS_DIRECT,\n");
			sbSql.append("  TEMP.IS_PLAN,\n");
			sbSql.append("  TEMP.IS_LACK,\n");
			sbSql.append("  TEMP.IS_REPLACED,\n");
			sbSql.append("  TEMP.STOCK_QTY,\n");
			sbSql.append("  TEMP.MIN_PACKAGE,\n");
			sbSql.append("  TEMP.BUY_QTY,\n");
			sbSql.append("  TEMP.BUY_PRICE,\n");
			sbSql.append("  TEMP.BUY_AMOUNT,\n");
			sbSql.append("  TEMP.IS_HAVA,\n");
			sbSql.append("  TEMP.CREATE_DATE,\n");
			sbSql.append("  TEMP.CREATE_BY,\n");
			sbSql.append("  TEMP.STATUS,\n");
			sbSql.append("  TEMP.EXPECT_ARRIVAL_DATE,\n");
			sbSql.append("  TEMP.VOLUME,\n");
			sbSql.append("  TEMP.BUY_VOLUME,\n");
			sbSql.append("  TEMP.BUY_PRICE1,\n");
			sbSql.append("  TEMP.PART_DISCOUNT,\n");
			sbSql.append("  TEMP.IS_DISCOUNT)\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID,\n");
			sbSql.append("  TEMP2.ORDER_ID,\n");
			sbSql.append("  TEMP2.PART_ID,\n");
			sbSql.append("  TEMP2.PART_CODE,\n");
			sbSql.append("  TEMP2.PART_OLDCODE,\n");
			sbSql.append("  TEMP2.PART_CNAME,\n");
			sbSql.append("  TEMP2.UNIT,\n");
			sbSql.append("  TEMP2.IS_DIRECT,\n");
			sbSql.append("  TEMP2.IS_PLAN,\n");
			sbSql.append("  TEMP2.IS_LACK,\n");
			sbSql.append("  TEMP2.IS_REPLACED,\n");
			sbSql.append("  TEMP2.ITEM_QTY,\n");
			sbSql.append("  TEMP2.MIN_PACKAGE,\n");
			sbSql.append("  TEMP2.PART_QTY,\n");
			sbSql.append("  TEMP2.SALE_PRICE1,\n");
			sbSql.append("  (TEMP2.PART_QTY * TEMP2.DISCOUNT),\n");
			sbSql.append("  TEMP2.UPORGSTOCk,\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"',\n");
			sbSql.append("  10011001,\n");
			sbSql.append("  TO_DATE('2016-12-01','YYYY-MM-DD'),\n");
			sbSql.append("  TEMP2.VOLUME,\n");
			sbSql.append("  (TEMP2.PART_QTY*TEMP2.VOLUME),\n");
			sbSql.append("  TEMP2.DISCOUNT,\n");
			sbSql.append("  TEMP2.PART_DISCOUNT,\n");
			sbSql.append("  TEMP2.IS_DISCOUNT\n");
			sbSql.append("FROM\n");
			sbSql.append("(\n");
			sbSql.append("        SELECT\n");
			sbSql.append("          TEM.DEALER_ID,\n");
			sbSql.append("          TEM.PARENT_ID,\n");
			sbSql.append("          TEM.PART_QTY,\n");
			sbSql.append("          TEM.DISCOUNT_ZKL,\n");
			sbSql.append("          TEM.ORDER_ID,\n");
			sbSql.append("          A .PART_ID,\n");
			sbSql.append("          NVL (A .IS_REPLACED, 10041002) AS IS_REPLACED,\n");
			sbSql.append("          NVL (A .IS_LACK, 10041002) AS IS_LACK,\n");
			sbSql.append("          A .PART_CODE,\n");
			sbSql.append("          NVL (A .IS_DIRECT, 10041002) AS IS_DIRECT,\n");
			sbSql.append("          NVL (A .IS_PLAN, 10041002) AS IS_PLAN,\n");
			sbSql.append("          A .PART_OLDCODE,\n");
			sbSql.append("          A .PART_CNAME,\n");
			sbSql.append("          A .STATE,\n");
			sbSql.append("          NVL(A.VOLUME,0) AS VOLUME,\n");
			sbSql.append("          A .MODEL_NAME,\n");
			sbSql.append("          A .PIC_URL,\n");
			sbSql.append("          A .REMARK,\n");
			sbSql.append("          NVL (A .UNIT, '件') UNIT,\n");
			sbSql.append("          NVL((SELECT SUM (S.normal_QTY) FROM VW_PART_STOCK S\n");
			sbSql.append("              WHERE\n");
			sbSql.append("              S.PART_ID = A .PART_ID\n");
			sbSql.append("              AND S.ORG_ID = TEM.DEALER_ID--当前用户dealer_id\n");
			sbSql.append("              AND S.STATE = 10011001\n");
			sbSql.append("              AND S.STATUS = 1\n");
			sbSql.append("            ),\n");
			sbSql.append("            0\n");
			sbSql.append("          ) AS ITEM_QTY,\n");
			sbSql.append("          NVL (A .PART_DISCOUNT, 1) PART_DISCOUNT,\n");
			sbSql.append("          A .IS_DISCOUNT,\n");
			sbSql.append("          F_get_min_package ("+orderType+", A .PART_ID, TEM.DEALER_ID) AS MIN_PACKAGE,--订单类型，备件id，当前用户dealer_id\n");
			sbSql.append("          TO_CHAR ( PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID), 'FM999999990.000000') AS SALE_PRICE1,--当前用户dealer_id，备件id\n");
			sbSql.append("          (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("              CASE\n");
			sbSql.append("            WHEN SUM (normal_QTY) > 0 THEN\n");
			sbSql.append("              '92181001'\n");
			sbSql.append("            WHEN SUM (normal_QTY) = 0 THEN\n");
			sbSql.append("              '92181002'\n");
			sbSql.append("            ELSE\n");
			sbSql.append("              '92181002'\n");
			sbSql.append("            END\n");
			sbSql.append("            FROM\n");
			sbSql.append("              VW_PART_STOCK E\n");
			sbSql.append("            WHERE\n");
			sbSql.append("              E .PART_ID = A .PART_ID\n");
			sbSql.append("            AND E .STATE = 10011001\n");
			sbSql.append("            AND E .STATUS = 1\n");
			sbSql.append("            AND E .ORG_ID = TEM.PARENT_ID--销售单位id\n");
			sbSql.append("          ) AS UPORGSTOCK,\n");
			sbSql.append("          NVL (\n");
			sbSql.append("            (\n");
			sbSql.append("              SELECT\n");
			sbSql.append("                QTY\n");
			sbSql.append("              FROM\n");
			sbSql.append("                VW_PART_DLR_THM_PER_SALE VPDTPS\n");
			sbSql.append("              WHERE\n");
			sbSql.append("                VPDTPS.PART_ID = A .PART_ID\n");
			sbSql.append("              AND ORG_ID = TEM.DEALER_ID\n");
			sbSql.append("            ),\n");
			sbSql.append("            '0'\n");
			sbSql.append("          ) AS qty,\n");
			sbSql.append("          NVL (A .is_special, '10041002') AS is_special,\n");
			sbSql.append("\n");
			sbSql.append("          TO_CHAR (\n");
			sbSql.append("            DECODE (\n");
			sbSql.append("              A .IS_DISCOUNT,\n");
			sbSql.append("              93111001,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * TEM.DISCOUNT_ZKL,\n");
			sbSql.append("             93111002,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * NVL (A .PART_DISCOUNT, 1),\n");
			sbSql.append("              93111003,\n");
			sbSql.append("              TO_CHAR (\n");
			sbSql.append("                PKG_PART.F_GETPRICE (TEM.DEALER_ID, A .PART_ID),\n");
			sbSql.append("                'FM999999990.000000'\n");
			sbSql.append("              ) * TEM.DISCOUNT_ZKL * NVL (A .PART_DISCOUNT, 1)\n");
			sbSql.append("            ),\n");
			sbSql.append("            'FM999999990.000000'\n");
			sbSql.append("          ) AS DISCOUNT\n");
			sbSql.append("\n");
			sbSql.append("        FROM\n");
			sbSql.append("          TT_PART_DEFINE A,\n");
			sbSql.append("          (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("              DISTINCT\n");
			sbSql.append("              TPDOM1.ORDER_ID,\n");
			sbSql.append("              TPEU.DEALER_ID,\n");
			sbSql.append("              TPEU.DEALER_CODE,\n");
			sbSql.append("              TPEU.DEALER_NAME,\n");
			sbSql.append("              TPEU.UPLOAD_BATCH,\n");
			sbSql.append("              TPEU.PART_ID,\n");
			sbSql.append("              TPEU.PART_QTY,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_ID FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_ID,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_CODE FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_CODE,\n");
			sbSql.append("              (SELECT NVL(TPSR.DISCOUNT,1) FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS DISCOUNT_ZKL,\n");
			sbSql.append("              (SELECT TPSR.PARENTORG_NAME FROM TT_PART_SALES_RELATION TPSR WHERE TPSR.CHILDORG_ID = TPEU.DEALER_ID AND ROWNUM = 1) AS PARENT_NAME\n");
			sbSql.append("            FROM\n");
			sbSql.append("              TT_PART_EMERGENT_UPLOAD TPEU,\n");
			sbSql.append("              TT_PART_DLR_ORDER_MAIN TPDOM1\n");
			sbSql.append("            WHERE\n");
			sbSql.append("              TPEU.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("              AND TPDOM1.UPLOAD_BATCH = TPEU.UPLOAD_BATCH\n");
			sbSql.append("              AND TPDOM1.DEALER_ID = TPEU.DEALER_ID\n");
			sbSql.append("              AND TPEU.DEALER_ID IN ("+dealerId+")\n");
			sbSql.append("          )TEM\n");
			sbSql.append("        WHERE\n");
			sbSql.append("          A .STATUS = 1\n");
			sbSql.append("        AND TEM.PART_ID = A.PART_ID\n");
//			sbSql.append("        AND A .IS_SALE = '10041001'\n");
//			sbSql.append("        AND A.OUT_PLAN = '10041001'\n");
			sbSql.append("        AND NOT EXISTS (\n");
			sbSql.append("          SELECT\n");
			sbSql.append("            1\n");
			sbSql.append("          FROM\n");
			sbSql.append("            VW_PART_STOCK S\n");
			sbSql.append("          WHERE\n");
			sbSql.append("            S.PDSTATE = 10011002\n");
			sbSql.append("          AND S.ORG_ID = TEM.PARENT_ID\n");
			sbSql.append("          AND S.PART_ID = A .PART_ID\n");
			sbSql.append("          HAVING\n");
			sbSql.append("            SUM (S.NORMAL_QTY) = 0\n");
			sbSql.append("        )\n");
			sbSql.append("        AND A .IS_SPECIAL = 10041002\n");
			sbSql.append("        AND A .ARTICLES_TYPE = 92931005\n");
			sbSql.append("        AND NOT EXISTS (\n");
			sbSql.append("          SELECT\n");
			sbSql.append("            part_id\n");
			sbSql.append("          FROM\n");
			sbSql.append("            tt_part_STO_DEFINE tpsd\n");
			sbSql.append("          WHERE\n");
			sbSql.append("            A .part_id = tpsd.part_id\n");
			sbSql.append("          AND tpsd.state = 10011001\n");
			sbSql.append("          AND tpsd.status = 1\n");
			sbSql.append("        )\n");
			sbSql.append("        AND A .STATE = 10011001\n");
			sbSql.append(")TEMP2"); 
			dao.insert(sbSql.toString());
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 铺货修改订单数据和金额订单号等
	 * @param batch
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean updateDlrOrderDate(String batch, String dealerIds) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT DISTINCT\n");
			sbSql.append("    TPDOD.ORDER_ID,\n");
			sbSql.append("    TPDOM.DEALER_ID,\n");
			sbSql.append("    SUM (TPDOD.BUY_VOLUME) SVO,\n");
			sbSql.append("    SUM (TPDOD.BUY_AMOUNT) SPR\n");
			sbSql.append("  FROM\n");
			sbSql.append("    TT_PART_DLR_ORDER_MAIN TPDOM,\n");
			sbSql.append("    TT_PART_DLR_ORDER_DTL TPDOD\n");
			sbSql.append("  WHERE\n");
			sbSql.append("    TPDOM.ORDER_ID = TPDOD.ORDER_ID\n");
			sbSql.append("  AND TPDOM.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("  AND TPDOM.DEALER_ID IN ("+dealerIds+")\n");
			sbSql.append("  GROUP BY\n");
			sbSql.append("    TPDOD.ORDER_ID,TPDOM.DEALER_ID"); 

			
			List<Map<String,Object>> list = pageQuery(sbSql.toString(), null, getFunName());
			for (Map<String,Object> map : list) {
				
				String orderId = map.get("ORDER_ID").toString();
				String dealerId = map.get("DEALER_ID").toString();
				String svo = map.get("SVO").toString();
				String spr = map.get("SPR").toString();
				String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_06,dealerId);
				sbSql = new StringBuffer();
				sbSql.append("UPDATE TT_PART_DLR_ORDER_MAIN TPDOM\n");
				sbSql.append(" SET TPDOM.SUM_VOLUME = '"+svo+"',\n");
				sbSql.append(" TPDOM.BALANCE_AMOUNT = '"+spr+"',\n");
				sbSql.append(" TPDOM.ORDER_AMOUNT = '"+spr+"',\n");
				sbSql.append(" TPDOM.ORDER_CODE = '"+orderCode+"'\n");
				sbSql.append("WHERE\n");
				sbSql.append(" TPDOM.ORDER_ID = '"+orderId+"'"); 
				dao.update(sbSql.toString(), null);
			}
			
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 铺货历史记录
	 * @param batch
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	public boolean makeDistributionOrderHistory(String batch, String dealerId) throws Exception {
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_OPERATION_HISTORY HIS(\n");
			sbSql.append("  HIS.OPT_ID,\n");
			sbSql.append("  HIS.BUSSINESS_ID,\n");
			sbSql.append("  HIS.OPT_BY,\n");
			sbSql.append("  HIS.OPT_NAME,\n");
			sbSql.append("  HIS.OPT_DATE,\n");
			sbSql.append("  HIS.WHAT,\n");
			sbSql.append("  HIS.STATUS,\n");
			sbSql.append("  HIS.OPT_TYPE,\n");
			sbSql.append("  HIS.ORDER_ID\n");
			sbSql.append(")\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID,\n");
			sbSql.append("  TEMP.ORDER_CODE,\n");
			sbSql.append("  TEMP.BUYER_ID,\n");
			sbSql.append("  TEMP.BUYER_NAME,\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '总部铺货！',\n");
			sbSql.append("  '"+Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02+"',\n");
			sbSql.append("  93181001,\n");
			sbSql.append("  TEMP.ORDER_ID\n");
			sbSql.append("FROM(\n");
			sbSql.append("  SELECT\n");
			sbSql.append("    DISTINCT\n");
			sbSql.append("    TPDOM.ORDER_CODE,\n");
			sbSql.append("    TPDOM.DEALER_ID,\n");
			sbSql.append("    TPDOM.BUYER_ID,\n");
			sbSql.append("    TPDOM.BUYER_NAME,\n");
			sbSql.append("    TPDOM.ORDER_ID\n");
			sbSql.append("  FROM\n");
			sbSql.append("    TT_PART_DLR_ORDER_MAIN TPDOM\n");
			sbSql.append("  WHERE\n");
			sbSql.append("    TPDOM.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("    AND TPDOM.DEALER_ID IN ("+dealerId+")\n");
			sbSql.append(")TEMP"); 
			dao.insert(sbSql.toString());
		}catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 资金效验
	 * @param batch
	 * @param dealerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean validationMoeny(String batch, String dealerId) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT\n");
			sbSql.append("  DISTINCT\n");
			sbSql.append("  TEMP1.DEALER_ID,\n");
			sbSql.append("  TEMP1.DEALER_CODE,\n");
			sbSql.append("  TEMP1.DEALER_NAME,\n");
			sbSql.append("  TEMP1.IS_ISSUED,\n");
			sbSql.append("  TO_CHAR(SUM(TEMP1.SUM_PART_PRICE),'FM999999999999990.00') AS SUM_MONEY,\n");
			sbSql.append("  COUNT(TEMP1.PART_ID) AS SUM_PART,\n");
			sbSql.append("  SUM(TEMP1.PART_QTY) AS SUM_QTY,\n");
			sbSql.append("  NVL(TEMP1.ACCOUNT_ID,0) AS ACCOUNT_ID,\n");
			sbSql.append("  NVL(TEMP1.ACCOUNT_KY,0) AS ACCOUNT_KY\n");
			sbSql.append("FROM(\n");
			sbSql.append("  SELECT\n");
			sbSql.append("    TEMP.DEALER_ID,\n");
			sbSql.append("    TEMP.DEALER_CODE,\n");
			sbSql.append("    TEMP.DEALER_NAME,\n");
			sbSql.append("    TEMP.IS_ISSUED,\n");
			sbSql.append("    TEMP.PART_ID,\n");
			sbSql.append("    TEMP.PART_QTY,\n");
			sbSql.append("    TO_CHAR(\n");
			sbSql.append("      DECODE(\n");
			sbSql.append("        TEMP.IS_DISCOUNT,\n");
			sbSql.append("        93111001,\n");
			sbSql.append("        TEMP.PART_PRICE * TEMP.DEALER_DISCOUNT * TEMP.PART_QTY,\n");
			sbSql.append("        93111002,\n");
			sbSql.append("        TEMP.PART_PRICE * TEMP.PART_DISCOUNT * TEMP.PART_QTY,\n");
			sbSql.append("        93111003,\n");
			sbSql.append("        TEMP.PART_PRICE * TEMP.DEALER_DISCOUNT * TEMP.PART_DISCOUNT * TEMP.PART_QTY\n");
			sbSql.append("      ) ,'FM999999999999990.00')AS SUM_PART_PRICE,\n");
			sbSql.append("    TEMP.ACCOUNT_ID,\n");
			sbSql.append("    TEMP.ACCOUNT_KY\n");
			sbSql.append("  FROM\n");
			sbSql.append("  (\n");
			sbSql.append("    SELECT\n");
			sbSql.append("      TPEU.DEALER_ID,\n");
			sbSql.append("      TPEU.DEALER_CODE,\n");
			sbSql.append("      TPEU.DEALER_NAME,\n");
			sbSql.append("      TPSR.PARENTORG_ID,\n");
			sbSql.append("      TPEU.IS_ISSUED,\n");
			sbSql.append("      TPEU.PART_ID,\n");
			sbSql.append("      TPEU.PART_OLDCODE,\n");
			sbSql.append("      TPD.IS_DISCOUNT,--折扣类型\n");
			sbSql.append("      PKG_PART.F_GETPRICE (TPEU.DEALER_ID, TPEU.PART_ID) AS PART_PRICE,--单价\n");
			sbSql.append("      F_GET_DEALER_DISCOUNT(92151002,TPEU.DEALER_ID,TPSR.PARENTORG_ID) AS DEALER_DISCOUNT,--常规订单，经销商折扣率\n");
			sbSql.append("      F_GET_PART_DISCOUNT(TPEU.PART_ID, TPEU.DEALER_ID, TPSR.PARENTORG_ID) AS PART_DISCOUNT,--单品折扣率\n");
			sbSql.append("      TPEU.PART_QTY,\n");
			sbSql.append("      (SELECT VPDA.ACCOUNT_ID FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_ID,\n");
			sbSql.append("      (SELECT VPDA.ACCOUNT_KY FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_KY\n");
			sbSql.append("    FROM\n");
			sbSql.append("      TT_PART_EMERGENT_UPLOAD TPEU,\n");
			sbSql.append("      TT_PART_SALES_RELATION TPSR,\n");
			sbSql.append("      TT_PART_DEFINE TPD\n");
			sbSql.append("    WHERE\n");
			sbSql.append("      TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
			sbSql.append("      AND TPD.PART_ID = TPEU.PART_ID\n");
			
			sbSql.append("      AND TPEU.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("      AND TPEU.DEALER_ID = '"+dealerId+"'\n");
			sbSql.append("      AND TPEU.IS_ISSUED = '"+Constant.IF_TYPE_NO+"'\n");
			
			sbSql.append("  )TEMP\n");
			sbSql.append(")TEMP1\n");
			sbSql.append("GROUP BY\n");
			sbSql.append("  TEMP1.DEALER_ID,\n");
			sbSql.append("  TEMP1.DEALER_CODE,\n");
			sbSql.append("  TEMP1.DEALER_NAME,\n");
			sbSql.append("  TEMP1.IS_ISSUED,\n");
			sbSql.append("  TEMP1.ACCOUNT_ID,\n");
			sbSql.append("  TEMP1.ACCOUNT_KY\n"); 
			sbSql.append("  ORDER BY\n");
			sbSql.append("  TEMP1.DEALER_CODE ASC\n");
			
			Map<String,Object> map = pageQueryMap(sbSql.toString(), null, getFunName());
			if(map.isEmpty()){
				flag = false;
			}else{
				Double SUM_MONEY = Double.valueOf(map.get("SUM_MONEY").toString());
				Double ky = Double.valueOf(map.get("ACCOUNT_KY").toString());
				if(ky < SUM_MONEY){
					flag = false;
				}
			}
			
		}catch(Exception e){
			flag = false;
			throw e;
		}
		return flag;
	}
	
	/**
	 * 批量占用资金
	 * @param batch
	 * @param dealerId
	 * @param loginUser 
	 * @return
	 */
	public boolean makeOccupyMoney(String batch, String dealerId, AclUserBean loginUser) throws Exception{
		boolean flag = true;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO TT_PART_ACCOUNT_RECORD TPAR(\n");
			sbSql.append("  TPAR.RECORD_ID,\n");
			sbSql.append("  TPAR.CHANGE_TYPE,\n");
			sbSql.append("  TPAR.DEALER_ID,\n");
			sbSql.append("  TPAR.ACCOUNT_ID,\n");
			sbSql.append("  TPAR.AMOUNT,\n");
			sbSql.append("  TPAR.FUNCTION_NAME,\n");
			sbSql.append("  TPAR.SOURCE_ID,\n");
			sbSql.append("  TPAR.SOURCE_CODE,\n");
			sbSql.append("  TPAR.CREATE_DATE,\n");
			sbSql.append("  TPAR.CREATE_BY\n");
			sbSql.append(")\n");
			sbSql.append("SELECT\n");
			sbSql.append("  F_GETID,--主键\n");
			sbSql.append("  '"+Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01+"',--占用\n");
			sbSql.append("  TPDOM.DEALER_ID,\n");
			sbSql.append("  TPDOM.ACCOUNT_ID,\n");
			sbSql.append("  TPDOM.BALANCE_AMOUNT,\n");
			sbSql.append("  '总部铺货预占',\n");
			sbSql.append("  TPDOM.ORDER_ID,\n");
			sbSql.append("  TPDOM.ORDER_CODE,\n");
			sbSql.append("  SYSDATE,\n");
			sbSql.append("  '"+loginUser.getUserId()+"'\n");
			sbSql.append("FROM\n");
			sbSql.append("  TT_PART_DLR_ORDER_MAIN TPDOM\n");
			sbSql.append("WHERE\n");
			sbSql.append("  TPDOM.UPLOAD_BATCH = '"+batch+"'\n");
			sbSql.append("  AND TPDOM.DEALER_ID IN ("+dealerId+")\n"); 
			dao.insert(sbSql.toString());
		}catch(Exception e){
			flag = false;
			throw e;
		}
		return flag;
	}

	/**
	 * 验证调拨价
	 * @param batch
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> validatePartBuyPrice(String batch, String dealerId) throws Exception{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("    SELECT\n");
		sbSql.append("      TPEU.DEALER_ID,\n");
		sbSql.append("      TPEU.DEALER_CODE,\n");
		sbSql.append("      TPEU.DEALER_NAME,\n");
		sbSql.append("      TPSR.PARENTORG_ID,\n");
		sbSql.append("      TPEU.IS_ISSUED,\n");
		sbSql.append("      TPEU.PART_ID,\n");
		sbSql.append("      TPEU.PART_OLDCODE,\n");
		sbSql.append("      TPD.IS_DISCOUNT,--折扣类型\n");
		sbSql.append("      NVL(PKG_PART.F_GETPRICE (TPEU.DEALER_ID, TPEU.PART_ID),0) AS PART_PRICE,--单价\n");
		sbSql.append("      F_GET_DEALER_DISCOUNT(92151002,TPEU.DEALER_ID,TPSR.PARENTORG_ID) AS DEALER_DISCOUNT,--常规订单，经销商折扣率\n");
		sbSql.append("      F_GET_PART_DISCOUNT(TPEU.PART_ID, TPEU.DEALER_ID, TPSR.PARENTORG_ID) AS PART_DISCOUNT,--单品折扣率\n");
		sbSql.append("      TPEU.PART_QTY,\n");
		sbSql.append("      (SELECT VPDA.ACCOUNT_ID FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_ID,\n");
		sbSql.append("      (SELECT VPDA.ACCOUNT_KY FROM VW_PART_DLR_ACCOUNT VPDA WHERE VPDA.DEALER_ID = TPEU.DEALER_ID AND VPDA.PARENTORG_ID = TPSR.PARENTORG_ID AND VPDA.ACCOUNT_PURPOSE = '95631001') AS ACCOUNT_KY\n");
		sbSql.append("    FROM\n");
		sbSql.append("      TT_PART_EMERGENT_UPLOAD TPEU,\n");
		sbSql.append("      TT_PART_SALES_RELATION TPSR,\n");
		sbSql.append("      TT_PART_DEFINE TPD\n");
		sbSql.append("    WHERE\n");
		sbSql.append("      TPSR.CHILDORG_ID = TPEU.DEALER_ID\n");
		sbSql.append("      AND TPD.PART_ID = TPEU.PART_ID\n");
		
		sbSql.append("      AND TPEU.UPLOAD_BATCH = '"+batch+"'\n");
		sbSql.append("      AND TPEU.DEALER_ID = '"+dealerId+"'\n");
		sbSql.append("      AND TPEU.IS_ISSUED = '"+Constant.IF_TYPE_NO+"'\n");
		
		List<Map<String, Object>> list = pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}

	

}
