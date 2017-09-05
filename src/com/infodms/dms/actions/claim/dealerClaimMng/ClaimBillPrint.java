package com.infodms.dms.actions.claim.dealerClaimMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrPartsitemExtPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class ClaimBillPrint {
	private Logger logger = Logger.getLogger(ClaimBillPrint.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	private final ClaimAuditingDao daom = ClaimAuditingDao.getInstance();
	private final String CLAIM_BILL_PRINT_URL = "/jsp/claim/dealerClaimMng/claimBillPrint.jsp";// 主页面（查询）
	private final String CLAIM_BILL_PRINT_URL1 = "/jsp/claim/dealerClaimMng/claimBillPrint1.jsp";// 主页面（查询）(轿车)
	/**
	 * 
	* @Title: printForward 
	* @Description: TODO(索赔单打印跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void printForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//zhumingwei 2011-03-11  此方法用于区分轿车和微车
		    TcCodePO codePo= new TcCodePO();
		    codePo.setType(Constant.chana+"");
		    TcCodePO poValue = (TcCodePO)dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			if("80081001".equals(codeId)){
				RequestWrapper request = act.getRequest();
				String id = request.getParamValue("id");
				List<TtAsWrPartsitemExtPO> ls = dao.queryPartLabour(id);
				TtAsWrApplicationExtPO tawep = dao.queryPrintById1(id);
				List<Map<String,Object>> seriesList = dao.queryOtherFee1(id);
				String amount1 = dao.queryOtherFee2(id);
//				for(int i=0;i<seriesList.size();i++){
//					Map map = seriesList.get(i);
//					String aaa = map.get("ITEM_DESC").toString();
//					String bbb = map.get("AMOUNT").toString();
//					System.out.println(aaa);
//					System.out.println(bbb);
//				}
				act.setOutData("seriesList", seriesList);
				act.setOutData("amount1", amount1);
				act.setOutData("tawep", tawep);
				act.setOutData("myList", ls);
				act.setOutData("codeId", Integer.parseInt(codeId));
				//zhumingwei 2011-03-11
				act.setForword(CLAIM_BILL_PRINT_URL1);
			}else{
				RequestWrapper request = act.getRequest();
				String id = request.getParamValue("id");
				List<TtAsWrPartsitemExtPO> ls = dao.queryPartLabour(id);
				TtAsWrApplicationExtPO tawep = dao.queryPrintById(id);
				Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
				List<TtAsWrOtherfeePO> seriesList = dao.queryOtherFee(companyId, "QT001");
				String QT001 = seriesList.get(0).getFeeName();
				List<TtAsWrOtherfeePO> seriesList1 = dao.queryOtherFee(companyId, "QT002");
				String QT002 = seriesList1.get(0).getFeeName();
				List<TtAsWrOtherfeePO> seriesList2 = dao.queryOtherFee(companyId, "QT003");
				String QT003 = seriesList2.get(0).getFeeName();
				List<TtAsWrOtherfeePO> seriesList3 = dao.queryOtherFee(companyId, "QT004");
				String QT004 = seriesList3.get(0).getFeeName();
				List<TtAsWrOtherfeePO> seriesList4 = dao.queryOtherFee(companyId, "QT005");
				String QT005 = seriesList4.get(0).getFeeName();
				act.setOutData("tawep", tawep);
				act.setOutData("myList", ls);
				act.setOutData("codeId", Integer.parseInt(codeId));
				//zhumingwei 2011-03-11
				act.setForword(CLAIM_BILL_PRINT_URL);
				act.setOutData("QT001", QT001);
				act.setOutData("QT002", QT002);
				act.setOutData("QT003", QT003);
				act.setOutData("QT004", QT004);
				act.setOutData("QT005", QT005);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
