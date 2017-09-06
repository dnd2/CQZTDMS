package com.infodms.dms.dao.parts.storageManager.partReturns;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 备件退换货状态变更DAO
 * @author fanzhineng
 *
 */
@SuppressWarnings("rawtypes")
public class ReturnsPartStateChangeDao extends BaseDao{
	/*=================================================================================*/
	public static final Logger logger = Logger.getLogger(ReturnsPartStateChangeDao.class);
	public static final ReturnsPartStateChangeDao dao = new ReturnsPartStateChangeDao();
	private ReturnsPartStateChangeDao(){
	}
	public static final ReturnsPartStateChangeDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/*=================================================================================*/
	
	/**
	 * <p>
	 * Description: 生成TC_CODE的DECODE的sql
	 * </p>
	 * @param list 
	 * @param code 
	 * @return
	 */
    private String loadDecodeSql(List<TcCodePO> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (TcCodePO tc : list) {
            decodeSql += ",'" + tc.getCodeId() + "','" + tc.getCodeDesc() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }
	
	/**
	 * 获取备件退换货解封申请信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnPartAppl(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String UNLOC_CODE = CommonUtils.checkNull(request.getParamValue("UNLOC_CODE"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		String STATE = CommonUtils.checkNull(request.getParamValue("STATE"));
		sql.append("SELECT\n" );
		sql.append("  TPRUM.*,\n" );
		sql.append("  TU.NAME CREATE_BY_CN,\n" );
		sql.append("  TU1.NAME  CHECK_BY_CN\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_RETURN_UNLOCK_MAIN TPRUM,\n" );
		sql.append("  TC_USER TU,\n" );
		sql.append("  TC_USER TU1\n" );
		sql.append("WHERE TPRUM.CREATE_BY = TU.USER_ID(+)\n" );
		sql.append("  AND TPRUM.CHECK_BY = TU1.USER_ID(+)\n" );
		if(StringUtil.notNull(loginUser.getDealerId())){
			sql.append("  AND TPRUM.DEALER_ID = ?\n");
			params.add(loginUser.getDealerId());
		}else{
			sql.append("  AND TPRUM.DEALER_ID = ?\n");
			params.add(Constant.OEM_ACTIVITIES);
		}
		
		if(StringUtil.notNull(UNLOC_CODE)){
			sql.append("  AND TPRUM.UNLOC_CODE LIKE ?\n");
			params.add("%"+UNLOC_CODE+"%");
		}
		if(StringUtil.notNull(startDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(startDate);
		}
		if(StringUtil.notNull(endDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(endDate);
		}
		if(StringUtil.notNull(STATE)){
			sql.append("  AND TPRUM.STATE = ?\n");
			params.add(STATE);
		}
		sql.append(" ORDER BY TPRUM.CREATE_DATE DESC\n" );
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询备件退换货解封审核配置信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnPartConfig(RequestWrapper request, AclUserBean loginUser, Integer curPage,Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String dealerId = request.getParamValue("dealerId");
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
		String GET_ED_FLAG_BY_DEALER_ID = (String) request.getAttribute("GET_ED_FLAG_BY_DEALER_ID");
		
		sql.append("SELECT\n" );
		sql.append("  TMD.DEALER_ID,\n" );
		sql.append("  TMD.DEALER_CODE,\n" );
		sql.append("  TMD.DEALER_NAME,\n" );
		sql.append("  TPSR.PARENTORG_ID,\n" );
		sql.append("  TPSR.PARENTORG_CODE,\n" );
		sql.append("  TPSR.PARENTORG_NAME,\n" );
		sql.append("  TPRR.RE_ID,\n" );
		sql.append("  TPRR.PARENT_ID,\n" );
		sql.append("  NVL(TPRR.AMOUNT,0.00) AS AMOUNT,\n" );
		sql.append("  NVL(TPRR.STATE,'10011001') AS STATE,\n" );
		sql.append("  TPRR.REMARK,\n" );
		sql.append("  TPRR.UPDATE_DATE,\n" );
		sql.append("  TPRR.UPDATE_BY,\n" );
		sql.append("  (SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TPRR.UPDATE_BY) AS UPDATE_BY_CN\n" );
		sql.append("FROM\n" );
		sql.append("  TM_DEALER TMD,\n" );
		sql.append("  TT_PART_RETURN_RELATION TPRR,\n" );
		sql.append("  TT_PART_SALES_RELATION TPSR\n" );
		sql.append("WHERE\n" );
		sql.append("  TMD.DEALER_ID = TPRR.ORG_ID(+)\n" );
		sql.append("  AND TMD.DEALER_ID = TPSR.CHILDORG_ID(+)\n" );
		sql.append("  AND TPSR.state=10011001 \n" );
		sql.append("  AND TMD.PDEALER_TYPE = '"+Constant.PART_SALE_PRICE_DEALER_TYPE_01+"'\n");
		
		if("YES".equals(GET_ED_FLAG_BY_DEALER_ID)){
			sql.append("  AND TMD.DEALER_ID = ?\n");
			params.add(loginUser.getDealerId());
		}
		
		if(StringUtil.notNull(dealerId)){
			sql.append("  AND TMD.DEALER_ID = ?\n");
			params.add(dealerId);
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("  AND TMD.DEALER_CODE LIKE ?\n");
			params.add("%"+dealerCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(dealerName)){
			sql.append("  AND TMD.DEALER_NAME LIKE ?\n");
			params.add("%"+dealerName+"%");
		}
		sql.append("  ORDER BY TPRR.UPDATE_DATE DESC NULLS LAST\n" );
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 获取备件退换货已入库备件查询
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnParts(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
//		String unlocType = CommonUtils.checkNull(request.getParamValue("unlocType"));
		String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
		String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
		String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
		String whId = CommonUtils.checkNull(request.getParamValue("whId"));
		String returnType = CommonUtils.checkNull(request.getParamValue("returnType"));
        String unit = CommonUtils.checkNull(request.getParamValue("unit"));
        String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));
        String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));
        
//		String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));
		
//		if(Constant.RC_JF_TYPE_01.toString().equals(unlocType)){
			//退货解封
		sql.append("\nSELECT\n" );
		sql.append("  TEMP.*\n");
		sql.append("FROM(\n");
		sql.append(" SELECT\n" );
		sql.append("  TPDRM.RETURN_ID,\n" );
		sql.append("  TPDRM.RETURN_CODE,\n" );
//		sql.append("  TPDRM.RETURN_TYPE,\n" );
		sql.append("  TPDRM.DEALER_ID,\n" );
		sql.append("  TPDRM.DEALER_CODE,\n" );
		sql.append("  TPDRM.DEALER_NAME,\n" );
		sql.append("  TPDRM.SELLER_ID,\n" );
		sql.append("  TPDRM.SELLER_CODE,\n" );
		sql.append("  TPDRM.SELLER_NAME,\n" );
		sql.append("  TPDRM.STOCK_IN AS WH_ID,--直发和非直发都是存到这里\n" );
		sql.append("  TPDRM.STATE,\n" );
		sql.append("  TPDRD.DTL_ID,\n" );
		sql.append("  TPDRD.PART_ID,\n" );
		sql.append("  TPDRD.PART_OLDCODE,\n" );
		sql.append("  TPDRD.PART_CNAME,\n" );
		sql.append("  TPDRD.PART_CODE,\n" );
		sql.append("  TPDRD.UNIT,\n" );
		sql.append("  TPDRD.IN_QTY, \n" );
//		sql.append("  TPDRD.IN_QTY2 AS INQTY2,\n" );
		sql.append("  NVl(TPDRD.KY_QTY,TPDRD.IN_QTY) KY_QTY,\n" );
//		if(StringUtil.notNull(loginUser.getDealerId())){
//			sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+loginUser.getDealerId()+"', TPDRD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//		}else{
//			sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+Constant.OEM_ACTIVITIES+"', TPDRD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//		}
		sql.append("  TPDRD.INLOC_ID,\n" );
		sql.append("  TPDRM.IN_ID,\n" );
//		sql.append("  TPDRD.ORDER_ID,\n" );
		sql.append("  TPDRM.SO_CODE,\n" );
		sql.append("  TPDRM.IN_CODE,\n" );
		sql.append("  TPDRD.IN_BATCH_NO,\n" );
		sql.append("  TPDRD.CREATE_DATE,\n" );
		sql.append("  NVL(TPDRD.UNLOC_QTY,0) AS UNLOC_QTY\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_DLR_RETURN_MAIN TPDRM,\n" );
		sql.append("  TT_PART_DLR_RETURN_DTL TPDRD\n" );
		sql.append("WHERE TPDRM.RETURN_ID = TPDRD.RETURN_ID\n" );
//			if(Constant.PART_RETURN_TYPE2_01.toString().equals(returnType)){
//				//直发
//				sql.append("  AND TPDRM.STATE = '92361014'--单据状态已入库\n" );
//			}else{
//				sql.append("  AND (TPDRM.STATE = '92361006' or TPDRM.STATE = '92361099' ) --单据状态总部出库\n" );
//			}
//			
			
			//sql.append(" AND NVl(TPDRD.KY_QTY,TPDRD.IN_QTY) > 0\n" );

		if(StringUtil.notNull(partOldcode)){
			sql.append("  AND TPDRD.PART_OLDCODE LIKE ?\n");
			params.add("%"+partOldcode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCname)){
			sql.append("  AND TPDRD.PART_CNAME LIKE ?\n");
			params.add("%"+partCname+"%");
		}
		if(StringUtil.notNull(partCode)){
			sql.append("  AND TPDRD.PART_CODE LIKE ?\n");
			params.add("%"+partCode.toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(whId)){
			sql.append("  AND TPDRM.STOCK_IN = ?\n");
			params.add(whId);
		}
		if(StringUtil.notNull(returnType)){
			//非直发/直发
			sql.append("  AND TPDRM.RETURN_TYPE = ?\n");
			params.add(returnType);
		}
        if(StringUtil.notNull(unit)){
            sql.append("  AND TPDRD.UNIT = ?\n");
            params.add(unit);
        }
        if(StringUtil.notNull(returnCode)){
            sql.append("  AND TPDRM.RETURN_CODE = ?\n");
            params.add(returnCode);
        }
        if(StringUtil.notNull(inCode)){
            sql.append("  AND TPDRM.IN_CODE = ?\n");
            params.add(inCode);
        }
		sql.append(")TEMP WHERE TEMP.KY_QTY > 0\n");
//		}else if(Constant.RC_JF_TYPE_02.toString().equals(unlocType)){
//			//换货解封
//			sql.append("SELECT\n" );
//			sql.append("  CM.CHANGE_ID AS RETURN_ID,\n" );
//			sql.append("  CM.CHANGE_CODE AS RETURN_CODE,\n" );
//			sql.append("  CM.CHANGE_TYPE AS RETURN_TYPE,\n" );
//			sql.append("  CM.DEALER_ID,\n" );
//			sql.append("  CM.DEALER_CODE,\n" );
//			sql.append("  CM.DEALER_NAME,\n" );
//			sql.append("  CM.SELLER_ID,\n" );
//			sql.append("  CM.SELLER_CODE,\n" );
//			sql.append("  CM.SELLER_NAME,\n" );
//			sql.append("  CM.STOCK_IN AS WH_ID,\n" );
//			sql.append("  CM.STATE,\n" );
//			sql.append("  CD.DTL_ID,\n" );
//			sql.append("  CD.PART_ID,\n" );
//			sql.append("  CD.PART_OLDCODE,\n" );
//			sql.append("  CD.PART_CNAME,\n" );
//			sql.append("  CD.PART_CODE,\n" );
//			sql.append("  CD.UNIT,\n" );
//			if(StringUtil.notNull(loginUser.getDealerId())){
//				sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+loginUser.getDealerId()+"', CD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//			}else{
//				sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+Constant.OEM_ACTIVITIES+"', CD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//			}
//			sql.append("  CD.IN_QTY,\n" );
//			sql.append("  NVL(CD.KY_QTY,CD.IN_QTY) AS KY_QTY,\n" );
//			sql.append("  CD.INLOC_ID,\n" );
//			sql.append("  CD.SO_CODE,\n" );
//			sql.append("  NVL(CD.UNLOC_QTY,0) AS UNLOC_QTY\n" );
//			sql.append("FROM\n" );
//			sql.append("  TT_PART_DLR_CHANGE_MAIN CM,\n" );
//			sql.append("  TT_PART_DLR_CHANGE_DTL CD\n" );
//			sql.append("WHERE\n" );
//			sql.append("  CM.CHANGE_ID = CD.CHANGE_ID\n" );
//			sql.append("  AND CM.STATE = '93091006'\n" );
//			sql.append("  AND NVL(CD.KY_QTY,CD.IN_QTY)>0\n");
//			if(StringUtil.notNull(partOldcode)){
//				sql.append("  AND CD.PART_OLDCODE LIKE ?\n");
//				params.add("%"+partOldcode.toUpperCase()+"%");
//			}
//			if(StringUtil.notNull(partCname)){
//				sql.append("  AND CD.PART_CNAME LIKE ?\n");
//				params.add("%"+partCname+"%");
//			}
//			if(StringUtil.notNull(partCode)){
//				sql.append("  AND CD.PART_CODE LIKE ?\n");
//				params.add("%"+partCode.toUpperCase()+"%");
//			}
//			
//			if(StringUtil.notNull(whId)){
//				sql.append("  AND CM.STOCK_IN = ?\n");
//				params.add(whId);
//			}
//			if(StringUtil.notNull(changeType)){
//				//非直发/直发
//				sql.append("  AND CM.CHANGE_TYPE = ?\n");
//				params.add(changeType);
//			}
//		}else{
//			
//		}
		sql.append(" ORDER BY TEMP.CREATE_DATE DESC ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 根据退换单明细id组获取退货单信息
	 * @param request
	 * @param loginUser
	 * @param dtlId 
	 * @param unlocType 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReturnMainAndDtlByDtlIds(RequestWrapper request, AclUserBean loginUser, String dtlId) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		//退货解封
		sql.append("\nSELECT\n" );
		sql.append("  TEMP.*\n");
		sql.append("FROM(\n");
		sql.append("  SELECT\n" );
		sql.append("  TPDRM.RETURN_ID,\n" );
		sql.append("  TPDRM.RETURN_CODE,\n" );
//		sql.append("  TPDRM.RETURN_TYPE,\n" );
		sql.append("  TPDRM.DEALER_ID,\n" );
		sql.append("  TPDRM.DEALER_CODE,\n" );
		sql.append("  TPDRM.DEALER_NAME,\n" );
		sql.append("  TPDRM.SELLER_ID,\n" );
		sql.append("  TPDRM.SELLER_CODE,\n" );
		sql.append("  TPDRM.SELLER_NAME,\n" );
		sql.append("  TPDRM.STOCK_IN AS WH_ID,--直发和非直发都是存到这里\n" );
		sql.append("  TPDRM.STATE,\n" );
		sql.append("  TPDRD.DTL_ID,\n" );
		sql.append("  TPDRD.PART_ID,\n" );
		sql.append("  TPDRD.PART_OLDCODE,\n" );
		sql.append("  TPDRD.PART_CNAME,\n" );
		sql.append("  TPDRD.PART_CODE,\n" );
		sql.append("  TPDRD.UNIT,\n" );
		sql.append("  TPDRD.IN_QTY,\n" );
		sql.append("  NVl(TPDRD.KY_QTY,TPDRD.IN_QTY) KY_QTY,\n" );
//		if(StringUtil.notNull(loginUser.getDealerId())){
//			sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+loginUser.getDealerId()+"', TPDRD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//		}else{
//			sql.append("  TO_CHAR(PKG_PART.F_GETPRICE('"+Constant.OEM_ACTIVITIES+"', TPDRD.PART_ID),'FM999999990.000000') AS SALE_PRICE,\n" );
//		}
		sql.append("  TPDRD.INLOC_ID,\n" );
		sql.append("  TPDRM.IN_ID,\n" );
//		sql.append("  TPDRD.ORDER_ID,\n" );
		sql.append("  TPDRM.SO_CODE,\n" );
		sql.append("  TPDRM.IN_CODE,\n" );
		sql.append("  TPDRD.IN_BATCH_NO,\n" );
		sql.append("  NVL(TPDRD.UNLOC_QTY,0) AS UNLOC_QTY\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_DLR_RETURN_MAIN TPDRM,\n" );
		sql.append("  TT_PART_DLR_RETURN_DTL TPDRD\n" );
		sql.append("WHERE TPDRM.RETURN_ID = TPDRD.RETURN_ID\n" );
//		sql.append(" AND TPDRM.STATE IN ('92361014','92361006','92361099')--单据状态已入库\n" );
		sql.append(" AND TPDRM.STATE = '"+Constant.PART_DLR_RETURN_STATUS_06+"' --单据状态已入库\n" );
		sql.append(" AND TPDRD.DTL_ID = ? \n");
		params.add(dtlId);
		sql.append(")TEMP WHERE TEMP.KY_QTY>0\n");
		
		List<Map<String, Object>> rs = pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
	/**
	 * 获取退货备件解封明细
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnPartSelect(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String unlocId = request.getParamValue("unlocId");
//		String unlocType = request.getParamValue("unlocType");
//		if(Constant.RC_JF_TYPE_01.toString().equals(unlocType)){
			sql.append("SELECT\n" );
			sql.append("  UD.*,\n" );
			sql.append("  RM.RETURN_CODE,\n" );
//			sql.append("  RM.RETURN_TYPE,\n" );
			sql.append("  RD.IN_QTY,\n" );
			sql.append("  RD.UNIT,\n");
			sql.append("  RD.UNLOC_QTY,\n");
			sql.append("  RD.KY_QTY\n" );
			sql.append("FROM\n" );
			sql.append("  TT_PART_RETURN_UNLOCK_DTL UD,\n" );
			sql.append("  TT_PART_DLR_RETURN_MAIN RM,\n" );
			sql.append("  TT_PART_DLR_RETURN_DTL RD\n" );
			sql.append("WHERE\n" );
			sql.append("  UD.RDTL_ID = RD.DTL_ID\n" );
			sql.append("  AND RD.RETURN_ID = RM.RETURN_ID\n" );
			sql.append("  AND UD.UNLOC_ID = ?\n");
			params.add(unlocId);
//		}else if(Constant.RC_JF_TYPE_02.toString().equals(unlocType)){
//			sql.append("SELECT\n" );
//			sql.append("  UD.*,\n" );
//			sql.append("  CM.CHANGE_CODE AS RETURN_CODE,\n" );
//			sql.append("  CM.CHANGE_TYPE AS RETURN_TYPE,\n" );
//			sql.append("  CD.IN_QTY,\n" );
//			sql.append("  CD.UNIT,\n" );
//			sql.append("  CD.KY_QTY\n" );
//			sql.append("FROM\n" );
//			sql.append("  TT_PART_RETURN_UNLOCK_DTL UD,\n" );
//			sql.append("  TT_PART_DLR_CHANGE_MAIN CM,\n" );
//			sql.append("  TT_PART_DLR_CHANGE_DTL CD\n" );
//			sql.append("WHERE\n" );
//			sql.append("  UD.RDTL_ID = CD.DTL_ID\n" );
//			sql.append("  AND CD.CHANGE_ID = CM.CHANGE_ID\n" );
//			sql.append("  AND UD.UNLOC_ID = ?\n");
//			params.add(unlocId);
//		}
		
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 判断每个月的使用额度
	 * @param request
	 * @param loginUser
	 * @param firstDate
	 * @param lastDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getArradyAmountNowMonth(RequestWrapper request, AclUserBean loginUser, String firstDate, String lastDate) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String dealerId = loginUser.getDealerId();
		
		sql.append("SELECT\n" );
		sql.append("  NVL(SUM(TPRUM.AMOUNT),0) AS AMOUNT\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_RETURN_UNLOCK_MAIN TPRUM\n" );
		sql.append("WHERE\n" );
		sql.append("  TPRUM.DEALER_ID = ?\n" );
		params.add(dealerId);
		sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') >= '"+firstDate+"'\n" );
		sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') <= '"+lastDate+"'\n" );
		sql.append("  AND TPRUM.STATE IN (97231002,97231003,97231005)--提交，审核，解封\n" );
		sql.append("  AND TPRUM.UNLOC_TYPE = '"+Constant.RC_JF_TYPE_01+"'\n");
		//sql.append("  AND TPRUM.CHECK_BY = '-1'\n" );
		sql.append(" ORDER BY TPRUM.CREATE_DATE DESC\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * <p>
	 * Description: TODO 根据解封id获取解封主要信息
	 * </p>
	 * @param unlocId
	 * @param state
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object> getUnlocMainByUnlocId(String unlocId, int state) {

	    // 退货解封处理状态
        List<TcCodePO> jfStateList = CodeDict.dictMap.get(Constant.RC_JF_STATE.toString()); 
        String jfStateDC = this.loadDecodeSql(jfStateList, "T1.STATE"); 
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T1.UNLOC_ID, \n");
        sql.append("        T1.UNLOC_CODE, \n");
        sql.append("        T1.DEALER_ID, \n");
        sql.append("        T1.DEALER_CODE, \n");
        sql.append("        T1.DEALER_NAME, \n");
        sql.append("        T1.STATE, \n");
        sql.append("        "+jfStateDC+" STATE_DESC, \n");
        sql.append("        T1.CREATE_BY, \n");
        sql.append("        TU1.NAME        CREATER_NAME, \n");
        sql.append("        TO_CHAR(T1.CREATE_DATE, 'YYYY-MM-DD HH:MI:SS') CREATE_DATE, \n");
        
        // 解封申请已提交时
        if(state >= Constant.RC_JF_STATE_02){
            sql.append("        T1.SUBMIT_BY, \n");
            sql.append("        TU2.NAME        SUBMITER_NAME, \n");
            sql.append("        TO_CHAR(T1.SUBMIT_DATE, 'YYYY-MM-DD HH:MI:SS') SUBMIT_DATE, \n");
            
            // 解封申请已审核时
            if(state >= Constant.RC_JF_STATE_03){
                sql.append("        T1.CHECK_BY, \n");
                sql.append("        TU3.NAME        CHECKER_NAME, \n");
                sql.append("        T1.CHECK_REMARK, \n");
                sql.append("        TO_CHAR(T1.CHECK_DATE, 'YYYY-MM-DD HH:MI:SS') CHECK_DATE, \n");
                
                // 解封申请已解封时
                if(state == Constant.RC_JF_STATE_05){
                    sql.append("        T1.UNLOC_BY, \n");
                    sql.append("        TU4.NAME        UNLOCER_NAME, \n");
                    sql.append("        TO_CHAR(T1.UNLOC_DATE, 'YYYY-MM-DD HH:MI:SS') UNLOC_DATE, \n");
                }
            }
        }
        sql.append("        T1.REMARK \n");
        sql.append("   FROM TT_PART_RETURN_UNLOCK_MAIN T1 \n");
        sql.append("  INNER JOIN TC_USER TU1 \n");
        sql.append("     ON TU1.USER_ID = T1.CREATE_BY \n");
        // 解封申请已提交时
        if(state >= Constant.RC_JF_STATE_02){
            sql.append("   INNER JOIN TC_USER TU2 \n");
            sql.append("     ON TU2.USER_ID = T1.SUBMIT_BY \n");
            
            // 解封申请已审核时
            if(state >= Constant.RC_JF_STATE_03){
                sql.append("   INNER JOIN TC_USER TU3 \n");
                sql.append("     ON TU3.USER_ID = T1.CHECK_BY \n");
                
                // 解封申请已解封时
                if(state == Constant.RC_JF_STATE_05){
                    sql.append("   INNER JOIN TC_USER TU4 \n");
                    sql.append("     ON TU4.USER_ID = T1.UNLOC_BY \n");
                    
                }
            }
        }
        sql.append("   WHERE T1.UNLOC_ID = ? \n");
        
        List<Object> params = new ArrayList<Object>();
        params.add(unlocId);
        
	    Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
	    return map;
    }
	
	/**
	 * 获取解封单明细，根据解封单id
	 * @param unlocId
	 * @param unlocType 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUnlocDtlByUnlocId(String unlocId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
//		if(Constant.RC_JF_TYPE_01.toString().equals(unlocType)){
			sql.append("SELECT\n" );
			sql.append("  UD.*,\n" );
			sql.append("  RM.RETURN_CODE,\n" );
//			sql.append("  (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = RM.RETURN_TYPE) AS RETURN_CODE_CN,\n");
//			sql.append("  RM.RETURN_TYPE,\n" );
			sql.append("  RD.IN_QTY,\n" );
			sql.append("  RD.UNIT,\n");
			sql.append("  RD.KY_QTY\n" );
			sql.append("FROM\n" );
			sql.append("  TT_PART_RETURN_UNLOCK_DTL UD,\n" );
			sql.append("  TT_PART_DLR_RETURN_MAIN RM,\n" );
			sql.append("  TT_PART_DLR_RETURN_DTL RD\n" );
			sql.append("WHERE\n" );
			sql.append("  UD.RDTL_ID = RD.DTL_ID\n" );
			sql.append("  AND RD.RETURN_ID = RM.RETURN_ID\n" );
			sql.append("  AND UD.UNLOC_ID = ?\n");
			params.add(unlocId);
//		}else if(Constant.RC_JF_TYPE_02.toString().equals(unlocType)){
//			sql.append("SELECT\n" );
//			sql.append("  UD.*,\n" );
//			sql.append("  CM.CHANGE_CODE AS RETURN_CODE,\n" );
//			sql.append("  (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = CM.CHANGE_TYPE) AS RETURN_CODE_CN,\n");
//			sql.append("  CM.CHANGE_TYPE AS RETURN_TYPE,\n" );
//			sql.append("  CD.IN_QTY,\n" );
//			sql.append("  CD.UNIT,\n" );
//			sql.append("  CD.KY_QTY\n" );
//			sql.append("FROM\n" );
//			sql.append("  TT_PART_RETURN_UNLOCK_DTL UD,\n" );
//			sql.append("  TT_PART_DLR_CHANGE_MAIN CM,\n" );
//			sql.append("  TT_PART_DLR_CHANGE_DTL CD\n" );
//			sql.append("WHERE\n" );
//			sql.append("  UD.RDTL_ID = CD.DTL_ID\n" );
//			sql.append("  AND CD.CHANGE_ID = CM.CHANGE_ID\n" );
//			sql.append("  AND UD.UNLOC_ID = ?\n");
//			params.add(unlocId);
//		}
		
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 获取待审核的解封申请单
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnPartJFAppInfos(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String UNLOC_CODE = CommonUtils.checkNull(request.getParamValue("UNLOC_CODE"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		String STATE = CommonUtils.checkNull(request.getParamValue("STATE"));
		
		sql.append("SELECT\n" );
		sql.append("  TPRUM.*,\n" );
		sql.append("  TU.NAME CREATE_BY_CN,\n" );
		sql.append("  TU2.NAME SUBMIT_BY_CN,\n" );
		sql.append("  TU1.NAME  CHECK_BY_CN\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_RETURN_UNLOCK_MAIN TPRUM,\n" );
		sql.append("  TC_USER TU,\n" );
		sql.append("  TC_USER TU1,\n" );
		sql.append("  TC_USER TU2\n" );
		sql.append("WHERE TPRUM.CREATE_BY = TU.USER_ID(+)\n" );
		sql.append("  AND TPRUM.CHECK_BY = TU1.USER_ID(+)\n" );
		sql.append("  AND TPRUM.SUBMIT_BY = TU2.USER_ID(+)\n" );
		if(StringUtil.notNull(loginUser.getDealerId())){
			sql.append("  AND TPRUM.DEALER_ID = ?\n");
			params.add(loginUser.getDealerId());
		}
		
		if(StringUtil.notNull(UNLOC_CODE)){
			sql.append("  AND TPRUM.UNLOC_CODE LIKE ?\n");
			params.add("%"+UNLOC_CODE+"%");
		}
		if(StringUtil.notNull(startDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(startDate);
		}
		if(StringUtil.notNull(endDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(endDate);
		}
		if(StringUtil.notNull(STATE)){
			sql.append("  AND TPRUM.STATE = ?\n");
			params.add(STATE);
		}
		sql.append(" ORDER BY TPRUM.CREATE_DATE DESC\n" );
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 退换货解封单查询
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getReturnPartSelectByDealer(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String UNLOC_CODE = CommonUtils.checkNull(request.getParamValue("UNLOC_CODE"));
		String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
		String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		String STATE = CommonUtils.checkNull(request.getParamValue("STATE"));
		String unlocType = CommonUtils.checkNull(request.getParamValue("unlocType"));
		
//		String types = request.getParamValue("types");
		
		sql.append("SELECT\n" );
		sql.append("  TPRUM.*,\n" );
		sql.append("  TU.NAME CREATE_BY_CN,\n" );
		sql.append("  TU1.NAME  CHECK_BY_CN\n" );
		sql.append("FROM\n" );
		sql.append("  TT_PART_RETURN_UNLOCK_MAIN TPRUM,\n" );
		sql.append("  TC_USER TU,\n" );
		sql.append("  TC_USER TU1\n" );
		sql.append("WHERE TPRUM.CREATE_BY = TU.USER_ID(+)\n" );
		sql.append("  AND TPRUM.CHECK_BY = TU1.USER_ID(+)\n" );
		if(StringUtil.notNull(loginUser.getDealerId())){
			sql.append("  AND TPRUM.DEALER_ID = ?\n");
			params.add(loginUser.getDealerId());
		}
		//总部或配送中心
//		if("zb".equals(types)){
			sql.append("  AND TPRUM.DEALER_ID = ?\n");
			params.add(Constant.OEM_ACTIVITIES);
//		}else{
//			sql.append("  AND TPRUM.DEALER_ID <> ?\n");
//			params.add(Constant.OEM_ACTIVITIES);
//		}
		
		if(StringUtil.notNull(UNLOC_CODE)){
			sql.append("  AND TPRUM.UNLOC_CODE LIKE ?\n");
			params.add("%"+UNLOC_CODE+"%");
		}
		if(StringUtil.notNull(DEALER_CODE)){
			sql.append("  AND TPRUM.DEALER_CODE LIKE ?\n");
			params.add("%"+DEALER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(DEALER_NAME)){
			sql.append("  AND TPRUM.DEALER_NAME LIKE ?\n");
			params.add("%"+DEALER_NAME+"%");
		}
		if(StringUtil.notNull(startDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(startDate);
		}
		if(StringUtil.notNull(endDate)){
			sql.append("  AND TO_CHAR(TPRUM.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(endDate);
		}
		if(StringUtil.notNull(STATE)){
			sql.append("  AND TPRUM.STATE IN ("+STATE+")\n");
		}
		if(StringUtil.notNull(unlocType)){
			sql.append("  AND TPRUM.UNLOC_TYPE = ?\n");
			params.add(unlocType);
		}
		sql.append(" ORDER BY TPRUM.CREATE_DATE DESC\n" );
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 条件获取vwPartStock视图数据
	 * @param partId
	 * @param OrgId
	 * @param whId
	 * @param LocId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getVwPartStock(Map<String, String> paramMap){
	    String partId = paramMap.get("partId");
	    String orgId = paramMap.get("orgId");
	    String whId = paramMap.get("whId");
	    String locId = paramMap.get("locId");
	    String batchNo = paramMap.get("batchNo");
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT\n" );
		sql.append("  *\n" );
		sql.append("FROM\n" );
		sql.append("  VW_PART_STOCK\n" );
		sql.append("WHERE\n" );
		sql.append("  PART_ID = ?\n" );
		params.add(partId);
		sql.append("AND ORG_ID = ?\n" );
		params.add(orgId);
		sql.append("AND WH_ID = ?\n" );
		params.add(whId);
		sql.append("AND LOC_ID = ?\n");
		params.add(locId);
		sql.append("AND BATCH_NO = ?\n");
		params.add(batchNo);
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	

	/**
	 * <p>
	 * Description: 根据解封id获取解封配件的相关数量及入库数量和申请解封数量的差
	 * </p>
	 * @param unlocId 解封id
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReturnPartInQtyList(String unlocId){
	    StringBuffer sql = new StringBuffer();
	    sql.append(" SELECT T1.DTL_ID, \n");
	    sql.append("        T1.RDTL_ID, \n");
	    sql.append("        T1.PART_OLDCODE, \n");
	    sql.append("        T2.IN_QTY, \n");
	    sql.append("        T2.UNLOC_QTY, \n");
	    sql.append("        T1.APPLY_QTY, \n");
	    sql.append("        NVL(T2.IN_QTY, 0) - NVL(T2.UNLOC_QTY, 0) KY_QTY, \n");
	    sql.append("        NVL(NVL(T2.IN_QTY, 0) - NVL(T2.UNLOC_QTY, 0) - T1.APPLY_QTY, 0) DIFFERENCE \n");
	    sql.append("   FROM TT_PART_RETURN_UNLOCK_DTL T1 \n");
	    sql.append("  INNER JOIN TT_PART_DLR_RETURN_DTL T2 \n");
	    sql.append("     ON T2.DTL_ID = T1.RDTL_ID \n");
	    sql.append("  WHERE T1.UNLOC_ID = ? \n");
        List<Object> params = new ArrayList<Object>();
        params.add(unlocId);
	    List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
	    return list;
	}
	
	
}
