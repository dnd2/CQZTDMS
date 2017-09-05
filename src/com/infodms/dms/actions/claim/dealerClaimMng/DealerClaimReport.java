/**********************************************************************
* <pre>
* FILE : DealerClaimReport.java
* CLASS : DealerClaimReport
* AUTHOR : XZM
* FUNCTION : 索赔申请上报包括一些功能：
*            [索赔申请单查询]
*            [索赔申请单明细查询]
*            [索赔申请单上报]
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| XZM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DealerClaimReport.java,v 1.16 2012/07/11 01:24:08 xiongc Exp $
 */
package com.infodms.dms.actions.claim.dealerClaimMng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimReportBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.dealerClaimMng.DealerClaimReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrAppAuditDetailPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrPartsitemBarcodePO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔申请上报功能维护
 * @author XZM
 * @version 1.0
 * @time 2010.06.03
 */
public class DealerClaimReport extends BaseAction{
	private Logger logger = Logger.getLogger(DealerClaimReport.class);
	private DealerClaimReportDao reportDao = new DealerClaimReportDao();;
	
	/** 索赔申请上报 首页面 */
	private final String CLAIM_REPORT_INIT = "/jsp/claim/dealerClaimMng/dealerClaimReportIndex.jsp";
	private final String CLAIM_REPORT_PRINT = "/jsp/claim/dealerClaimMng/dealerClaimReportPrint.jsp";
	
	/**
	 * 索赔申请上报页面初始化
	 */
	public void dealerClaimReportInit(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerClaimReportDao dao = new DealerClaimReportDao();
		try{
			String dealerId = logonUser.getDealerId();
			TmDealerPO p = new TmDealerPO();
			p.setDealerId(Long.valueOf(dealerId));
			p = (TmDealerPO) dao.select(p).get(0);
			act.setOutData("dealerLevel", p.getDealerLevel());
			act.setForword(this.CLAIM_REPORT_INIT);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔申请单查询
	 * @param 
	 *     索赔申请单号
	 *     工单号
	 *     行号
	 *     索赔类型
	 *     VIN
	 *     申请日期范围
	 *     申请单状态（只是"未上报"）
	 *     经销商代码（从SESSION中取得，限制用户查询数据）
	 */
	public void dealerClaimReportQuery(){
		DealerClaimReportDao reportDao = new DealerClaimReportDao();
		try{
			PageResult<Map<String,Object>> result = reportDao.queryClaim(request,loginUser,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * @author KFQ
	 * @serialData 2013-4-27 15:31
	 * 上报索赔申请单信息
	 * <pre>
	 * 过程：
	 *     1、上报对应索赔申请单
	 *        点击上报后，申请单状态由 "未上报" --> "结算审核"
	 *     2、为一般维修,售前维修,外出维修 使用的配件生成条码明细：
	 *     		条码编号生成规则：服务站代码(5位)+年月(4位)+seq_b 序列(7位)）
	 * </pre>
	 * @param 
	 *       索赔申请单ID
	 *       登陆用户ID
	 */
	@SuppressWarnings("unchecked")
	public void reportClaim(){
		AclUserBean logonUser = loginUser;
		try{
			String claimId = request.getParamValue("ID");//索赔申请单ID//批量审核的 
			Long userID = logonUser.getUserId();//用户信息
			String status ="";
			String auditOpinion = request.getParamValue("ADUIT_REMARK");//审核备注
			String claimType = request.getParamValue("claimType");//索赔类型
			String type = request.getParamValue("type");//审核备注
			String[] id = claimId.split(",");
			String pids = request.getParamValue("pids");//得到主因件的ID
			int auditStatus = Constant.PART_AUDIT_STATUS_01;
			double  BALANCE_NETITEM_AMOUNT  = 0.0;//外出总费用
			double  BALANCE_NETITEM_AMOUNT2  = 0.0;//补偿总费用
			StringBuffer sqll = new StringBuffer();
			sqll.append("SELECT * FROM Tt_As_Wr_Partsitem p WHERE p.audit_status=95681002 AND p.ID IN ("+claimId+")");
			List<TtAsWrPartsitemPO> pList = reportDao.select(TtAsWrPartsitemPO.class, sqll.toString(), null);
			StringBuffer sqlbug= new StringBuffer();
			sqlbug.append("SELECT *  from TT_AS_WR_APPLICATION t where t.status=10791001 and  t.ID in ("+claimId+")");
			List<TtAsWrApplicationPO> listpo= reportDao.select(TtAsWrApplicationPO.class , sqlbug.toString(), null);
			if(pList!=null && pList.size()>0 &&!"2".equalsIgnoreCase(type)){
				act.setOutData("ACTION_RESULT", "单据还有配件处于【挂起】状态,请先取消挂起再进行审核");
			}else if(listpo!=null && listpo.size()>0)
			{
				act.setOutData("ACTION_RESULT", "服务站已经撤销上报无需审核");
			}else{
				for(int temp=0;temp<id.length;temp++){
					//-------lj 3.23  更新审核人、时间
					StringBuffer sb = new StringBuffer();
					sb.append(" UPDATE  TT_AS_WR_APPLICATION  TT SET \n");
					sb.append("TT.AUDITING_MAN = "+logonUser.getUserId());
					sb.append(",\n" );
					sb.append("TT.AUDITING_DATE = sysdate " );
					sb.append("\n" );
					sb.append("where TT.ID ="+id[temp]);
					reportDao.update(sb.toString(), null);
				if("1".equals(type)){//如果是审核通过,还要进行旧件标签的生成
				auditStatus = Constant.PART_AUDIT_STATUS_03;
				status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();//结算室审核中
				if(pids!=null){//通过针对单个主因件进行数据更新
					String[] str = pids.split(",");
					for(int j=0;j<str.length;j++){
						String part_id = str[j];
						String[] PKID = request.getParamValues("PKID"+part_id);//补偿费ID
						String[] pass_price = request.getParamValues("pass_price"+part_id);//补偿费审核金额
						
						String[] netItem = request.getParamValues("netItem"+part_id);//其他项目ID
						String[] Amount = request.getParamValues("Amount"+part_id);//其他项目审核金额
						//更新该配件下面的补偿费
						if(PKID!=null){
							for(int i=0;i<PKID.length;i++){
								 BALANCE_NETITEM_AMOUNT2 = BALANCE_NETITEM_AMOUNT2 + Double.parseDouble(pass_price[i]);
								TtAsWrCompensationAppPO ap = new TtAsWrCompensationAppPO();
								TtAsWrCompensationAppPO ap2 = new TtAsWrCompensationAppPO();
								ap.setPkid(Long.valueOf(Long.valueOf(PKID[i])));
								ap2.setPassPrice(Double.valueOf(pass_price[i]));
								reportDao.update(ap, ap2);
							}
						}
						//更新该配件的其他项目金额
						if(netItem!=null){
							for(int i=0;i<netItem.length;i++){
								 BALANCE_NETITEM_AMOUNT = BALANCE_NETITEM_AMOUNT + Double.parseDouble(Amount[i]);
								TtAsWrNetitemPO np = new TtAsWrNetitemPO();
								TtAsWrNetitemPO np2 = new TtAsWrNetitemPO();
								np.setNetitemId(Long.valueOf(netItem[i]));
								np2.setBalanceAmount(Double.valueOf(Amount[i]));
								reportDao.update(np, np2);
							}
						}
						StringBuffer sql = new StringBuffer();
						sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");//整单通过的话，只根心配件状态为审核中的数据
						sql.append(" SET P.AUDIT_STATUS = "+auditStatus+",p.audit_by="+logonUser.getUserId()+",p.audit_date=sysdate WHERE\n");
						sql.append(" P.AUDIT_STATUS="+Constant.PART_AUDIT_STATUS_01+" and P.Part_Id IN\n");
						sql.append(" (SELECT P.Part_Id\n");
						sql.append(" FROM TT_AS_WR_PARTSITEM P where p.id="+claimId+")\n");
						/*sql.append(" START WITH P.PART_ID = "+part_id+"\n");
						sql.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)"); */
						reportDao.update(sql.toString(), null);
					}
				}
				
				
					// 艾春9.28添加 更新索赔单上回运标识
					String sql =    "UPDATE TT_AS_WR_APPLICATION T\n" +
									"   SET T.DATA_TYPE = NVL((SELECT MIN(P.IS_RETURN) FROM TT_AS_WR_PARTSITEM A, TM_PT_PART_BASE P\n" + 
									"                          WHERE A.PART_CODE = P.PART_CODE AND A.ID = T.ID), 95361002)\n" + 
									" WHERE T.ID="+id[temp]+"";
					// 更新索赔单上的标识
					reportDao.update(sql, null);
					
					
				
				
						Long  sNum=Long.valueOf(0);
						Long aNum=Long.valueOf(0);
					
					//查询明细并添加条码明细
						//新增了限制条件，只选择配件是审核通过了的
					List<Map<String,Object>> Parts = reportDao.Partsitem(id[temp]);
					if(Parts!=null&&Parts.size()>0){
					System.out.println(Parts.size()+"配件数量");
						for(int i=0;i<Parts.size();i++){
							Long quantity =Long.valueOf(Parts.get(i).get("QUANTITY").toString());
							aNum+=quantity;
						}
						System.out.println(aNum+"总数量");
					for(int i=0;i<Parts.size();i++){
						Long quantity =Long.valueOf(Parts.get(i).get("QUANTITY").toString());//配件数量
						String partId=Parts.get(i).get("PART_ID").toString();//配件明细ID
						String yieldly=Parts.get(i).get("YIELDLY").toString();//产地
						String dealer = Parts.get(i).get("DEALER_ID").toString();//索赔单中的经销商ID
						String dealerCode = Parts.get(i).get("DEALER_CODE").toString();//索赔单中的经销商ID
						for(int j=0;j<quantity;j++){
							if(quantity>0){
							sNum++;
							String xuHao=sNum.toString()+"/"+aNum.toString();
							TtAsWrPartsitemBarcodePO bp = new TtAsWrPartsitemBarcodePO();
							bp.setPartId(Long.valueOf(partId));
							bp.setSerialNumber(xuHao);
							List<TtAsWrPartsitemBarcodePO> list = reportDao.select(bp);
							if(list==null||list.size()==0){
								reportDao.insertBarcode(partId, yieldly, Long.valueOf(dealer),xuHao,dealerCode);
							}
						}
						}
						}
				}
			}else if("2".equalsIgnoreCase(type)){
				status = Constant.CLAIM_APPLY_ORD_TYPE_06.toString();//审核退回
			}else if("3".equalsIgnoreCase(type)){
				status = Constant.CLAIM_APPLY_ORD_TYPE_05.toString();//审核拒绝
				auditStatus = Constant.PART_AUDIT_STATUS_04;
				if(pids!=null){//通过针对单个主因件进行数据更新
					String[] str = pids.split(",");
					for(int j=0;j<str.length;j++){
						String part_id = str[j];
						String[] PKID = request.getParamValues("PKID"+part_id);//补偿费ID
						String[] netItem = request.getParamValues("netItem"+part_id);//其他项目ID
						//工时
						String[] labourId = request.getParamValues("labourId"+part_id);
						if(labourId!=null){
							for(int i=0;i<labourId.length;i++){
								String sql = "UPDATE Tt_As_Wr_Labouritem l SET l.balance_amount=0,l.balance_quantity=0 WHERE l.labour_id= "+labourId[i];
								reportDao.update(sql, null);
							}
						}
						//配件
						StringBuffer sql = new StringBuffer();
						sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");//整单拒绝则更新所有配件的状态为拒绝
						sql.append(" SET p.balance_amount=0,p.balance_quantity=0 ,P.AUDIT_STATUS = "+auditStatus+",p.audit_by="+logonUser.getUserId()+",p.audit_date=sysdate\n");
						sql.append(" WHERE P.Part_Id IN\n");
						sql.append(" (SELECT P.Part_Id\n");
						sql.append(" FROM TT_AS_WR_PARTSITEM P where p.id="+claimId+"\n");
						sql.append(" START WITH P.PART_ID = "+part_id+"\n");
						sql.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)"); 
						reportDao.update(sql.toString(), null);
						//补偿费
						if(PKID!=null){
							for(int i=0;i<PKID.length;i++){
								TtAsWrCompensationAppPO ap = new TtAsWrCompensationAppPO();
								TtAsWrCompensationAppPO ap2 = new TtAsWrCompensationAppPO();
								ap.setPkid(Long.valueOf(Long.valueOf(PKID[i])));
								ap2.setPassPrice(0.0);
								reportDao.update(ap, ap2);
							}
						}
						
						//辅料
						String[] fulId = request.getParamValues("FLID"+part_id);
						if(fulId!=null){
							for(int i=0;i<fulId.length;i++){
								String sql1 ="UPDATE tt_claim_accessory_dtl  d SET d.price=0 WHERE d.ID="+fulId[i];
								reportDao.update(sql1, null);
							}
						}
						//其他项目
						if(netItem!=null){
							for(int i=0;i<netItem.length;i++){
								TtAsWrNetitemPO np = new TtAsWrNetitemPO();
								TtAsWrNetitemPO np2 = new TtAsWrNetitemPO();
								np.setNetitemId(Long.valueOf(netItem[i]));
								np2.setBalanceAmount(0.0);
								reportDao.update(np, np2);
							}
						}
					}
				}
				
			}else if("4".equalsIgnoreCase(type)){
				status = Constant.CLAIM_APPLY_ORD_TYPE_02.toString();//审核撤销，回滚所有金额
				auditStatus = Constant.PART_AUDIT_STATUS_01;
				if(pids!=null){//通过针对单个主因件进行数据更新
					String[] str = pids.split(",");
					for(int j=0;j<str.length;j++){
						String part_id = str[j];
						String[] PKID = request.getParamValues("PKID"+part_id);//补偿费ID
						String[] netItem = request.getParamValues("netItem"+part_id);//其他项目ID
						//工时
						String[] labourId = request.getParamValues("labourId"+part_id);
						if(labourId!=null){
							for(int i=0;i<labourId.length;i++){
								String sql = "UPDATE Tt_As_Wr_Labouritem l SET l.balance_amount=l.labour_amount,l.balance_quantity=l.labour_quantity WHERE l.labour_id= "+labourId[i];
								reportDao.update(sql, null);
							}
						}
						//配件
						StringBuffer sql = new StringBuffer();
						sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");
						sql.append(" SET p.balance_amount=p.amount,p.balance_quantity=p.quantity,P.AUDIT_STATUS = "+auditStatus+",p.audit_by="+logonUser.getUserId()+",p.audit_date=sysdate   \n");
						sql.append(" WHERE P.Part_Id IN\n");
						sql.append(" (SELECT P.Part_Id\n");
						sql.append(" FROM TT_AS_WR_PARTSITEM P where p.id="+claimId+"\n");
						sql.append(" START WITH P.PART_ID = "+part_id+"\n");
						sql.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)"); 
						reportDao.update(sql.toString(), null);
						/*//补偿费
						if(PKID!=null){
							for(int i=0;i<PKID.length;i++){
								String sql1 = "UPDATE Tt_As_Wr_Compensation_App a SET a.pass_price = a.apply_price WHERE a.pkid="+PKID[i];
								reportDao.update(sql1.toString(), null);
							}
						}*/
						
						//辅料
						String[] fulId = request.getParamValues("FLID"+part_id);
						if(fulId!=null){
							for(int i=0;i<fulId.length;i++){
								String sql1 ="UPDATE tt_claim_accessory_dtl  d SET d.price=d.app_price WHERE d.ID="+fulId[i];
								reportDao.update(sql1, null);
							}
						}
						//其他项目
						if(netItem!=null){
							for(int i=0;i<netItem.length;i++){
								String sql2="UPDATE  Tt_As_Wr_Netitem n SET n.balance_amount = n.amount WHERE n.netitem_id="+netItem[i];
								reportDao.update(sql2, null);
							}
						}
					}
				}
			}
			
			/**
			 * 更新索赔单状态
			 */
			TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
			TtAsWrApplicationPO parameterPO2 = new TtAsWrApplicationPO();
			parameterPO.setStatus(Integer.parseInt(status));
			parameterPO.setReportDate(new Date());
			parameterPO.setUpdateBy(userID);
			parameterPO.setUpdateDate(new Date());
			//这个为单个审核的
			if(id!=null && id.length==1){
				TtAsWrApplicationPO tempPo1=new TtAsWrApplicationPO();
				tempPo1.setId(Long.parseLong(claimId));
				List<TtAsWrApplicationPO> select1 = reportDao.select(tempPo1);
				String RoNo = select1.get(0).getRoNo();
				TtAsRepairOrderPO tempPo=new TtAsRepairOrderPO();
				tempPo.setRoNo(RoNo);
				List<TtAsRepairOrderPO> select = reportDao.select(tempPo);
				String code = select.get(0).getCamCode();
				if(String.valueOf(Constant.CLA_TYPE_06).equals(claimType)&&jugeReplce(code)|| String.valueOf(Constant.CLA_TYPE_11).equals(claimType)){
				if("1".equals(type)){
					String[] amounts = DaoFactory.getParams(request, "Amount");
					for (String amount : amounts) {
						BALANCE_NETITEM_AMOUNT=BALANCE_NETITEM_AMOUNT+Double.parseDouble(amount);
					}
						parameterPO.setBalanceNetitemAmount(BALANCE_NETITEM_AMOUNT);
					}
				}
			}else{
				if(String.valueOf(Constant.CLA_TYPE_09).equals(claimType)){
					if("1".equalsIgnoreCase(type)){
					parameterPO.setBalanceNetitemAmount(BALANCE_NETITEM_AMOUNT);
					}
				}
			}
			parameterPO.setAuditOpinion(auditOpinion);
			parameterPO2.setId(Long.parseLong(id[temp]));
			reportDao.update(parameterPO2, parameterPO);
			
			/**
			 * 记录审核记录（专门针对外出维修的进行结算金额修改等操作）
			 */
			TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setAuditBy(logonUser.getUserId());
			dp.setAuditDate(new Date());
			dp.setAuditRemark(auditOpinion);
			dp.setAuditResult(Integer.parseInt(status));
			dp.setClaimId(Long.parseLong(id[temp]));
			dp.setCreateBy(logonUser.getUserId());
			dp.setCreateDate(new Date());
			dp.setAuditType(1);
			reportDao.insert(dp);
			
			//更新索赔单结算费用
				TtAsWrApplicationPO po=new TtAsWrApplicationPO();
				TtAsWrApplicationPO updatePo=new TtAsWrApplicationPO();
				po.setId(BaseUtils.ConvertLong(id[temp]));
				List<TtAsWrApplicationPO> list = reportDao.select(po);
				if(list!=null&& list.size()>0){
					updatePo=list.get(0);
						TtAsWrApplicationPO po1=new TtAsWrApplicationPO();
						//索赔单总费用 = 辅料费+工时费+材料费+补偿费+外出(其他)+活动+保养+材料打折+工时打折
						Double BalanceAmount=updatePo.getAccessoriesPrice()+updatePo.getLabourAmount()+updatePo.getPartAmount()+BALANCE_NETITEM_AMOUNT2+BALANCE_NETITEM_AMOUNT+updatePo.getCampaignFee()+updatePo.getFreeMPrice()+updatePo.getPartDown()+updatePo.getLabourDown();
						po1.setBalanceAmount(BalanceAmount);
						po1.setBalanceNetitemAmount(BALANCE_NETITEM_AMOUNT);
						po1.setCompensationMoney(BALANCE_NETITEM_AMOUNT2);
						reportDao.update(po, po1);
				}
		}
			act.setOutData("ACTION_RESULT", "1");
			act.setOutData("type",type );
			}	
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void reportClaimSubmmit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerClaimReportDao reportDao = new DealerClaimReportDao();
		try{
			RequestWrapper request = act.getRequest();
			String claimId = request.getParamValue("ID");//索赔申请单ID, 
			Long userID = logonUser.getUserId();//用户信息
			String userName = logonUser.getName();
			String status = Constant.CLAIM_APPLY_ORD_TYPE_03.toString();//审核中
			String[] id = claimId.split(",");
			/**
			 * @author KFQ
			 * 直接将索赔单状态改为审核中就可以了
			 */
			for(int temp=0;temp<id.length;temp++){
			TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
			TtAsWrApplicationPO parameterPO2 = new TtAsWrApplicationPO();
			parameterPO.setStatus(Integer.parseInt(status));
			parameterPO.setReporter(userName);
			parameterPO.setSubDate(new Date());
			parameterPO.setUpdateBy(userID);
			parameterPO.setUpdateDate(new Date());
			parameterPO2.setId(Long.parseLong(id[temp]));
			reportDao.update(parameterPO2, parameterPO);
			}
			act.setOutData("ACTION_RESULT", "1");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 检测选择的索赔单是否是常规保养。以及超期索赔单
	 */
	public void reportClaimCheck(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerClaimReportDao dao = new DealerClaimReportDao();
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM");
		try{
			//取得查询参数
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			System.out.println(id);
			String str="";
			String[] ids = id.split(",");
			Date date = new Date();
			TcCodePO c =new TcCodePO();
			c.setCodeId("94051002");
			c = (TcCodePO) dao.select(c).get(0);
			int day  = Integer.parseInt(c.getCodeDesc());
			String nowDate = sdf.format(new Date());//得到当前时间所在的月份
			for(int i=0;i<ids.length;i++){
				TtAsWrApplicationPO aa = new TtAsWrApplicationPO();
				aa.setId(Long.valueOf(ids[i]));
				aa = (TtAsWrApplicationPO) dao.select(aa).get(0);
				TtAsRepairOrderPO op = new TtAsRepairOrderPO();
				op.setRoNo(aa.getRoNo());
				op = (TtAsRepairOrderPO) dao.select(op).get(0);
				if(Constant.RO_PRO_STATUS_02.toString().equalsIgnoreCase(op.getOrderValuableType().toString())){
					str += "索赔单:【"+aa.getClaimNo()+"】 对应的工单已经作废,请重新生成！\n";
				}
				String creDate = sdf.format(aa.getCreateDate());//得到索赔单的创建时间所在月份
				if(!nowDate.equalsIgnoreCase(creDate)){
					str += "索赔单:【"+aa.getClaimNo()+"】 不能跨月上报,请废弃后重新生成！\n";
				}
				TtAsWrApplicationPO ap = new TtAsWrApplicationPO();
				ap.setId(Long.valueOf(ids[i]));
				ap = (TtAsWrApplicationPO) dao.select(ap).get(0);
				if(Constant.CLA_TYPE_02==ap.getClaimType()&&ap.getBalanceAmount()==0){
					str = "【"+ap.getClaimNo()+"】为常规保养不能上报,\n";
				}
				Long s = day*3600*24*1000L;
				if((date.getTime()-ap.getCreateDate().getTime())>s){
					str = str+"【"+ap.getClaimNo()+"】超过了系统规定的"+day+"天限制,\n";
				}
				TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
				p.setId(Long.valueOf(ids[i]));
				List<TtAsWrPartsitemPO> list  = dao.select(p);
				if(list!=null && list.size()>0){
					for(int k=0;k<list.size();k++){
						if(list.get(k).getDownProductCode()==null || list.get(k).getProducerCode()==null){
							str = str+"【"+ap.getClaimNo()+"】含有配件未选择相应的供应商,\n";
							break;
						}
					}
				}
			}
			act.setOutData("str", str);
			act.setOutData("ID", id);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @param claimId 索赔申请单号
	 * @return true : 该索赔单已经上报过 false :未上报过
	 */
	@SuppressWarnings("unused")
	private boolean isReport(String claimId){
		boolean result = false;
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(claimId);
		if(claimPO==null || !Constant.CLAIM_APPLY_ORD_TYPE_01.equals(claimPO.getStatus())){
			//索赔单状态不为"未上报，说明已经上报过"
			result = true;
		}
		return result;
	}
	/**
	 * 
	 * @param claimId 索赔单打印
	 *
	 */
	public void dealerClaimReportPrint(){
		ActionContext act = ActionContext.getContext();
		DealerClaimReportDao dao = new DealerClaimReportDao();
	
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			Double otherPrice = 0.0;
			TtAsWrApplicationExtPO bean = (TtAsWrApplicationExtPO) dao.getBaseBean(id);
			List<TtAsWrApplicationExtPO> list = dao.getDetail(id);
			TtAsWrNetitemPO p = new TtAsWrNetitemPO();
			p.setId(Long.valueOf(id));
			List<TtAsWrNetitemPO> oList = dao.select(p);
			if(oList!=null&&oList.size()>0){
				for(int i=0;i<oList.size();i++){
					otherPrice += oList.get(i).getAmount();
				}
			}
			String claimNo = request.getParamValue("claimNo");
			String length = "";
			ClaimBillMaintainDAO dao1 = ClaimBillMaintainDAO.getInstance();
			List<Map<String, Object>> claimNoAccessoryList = dao1.getclaimAccessoryDtl(claimNo);
			if(claimNoAccessoryList.size()==0){
				length = "0";
				act.setOutData("length", length);
			}
			act.setOutData("claimNoAccessoryList", claimNoAccessoryList);
			System.out.println("其他总费用："+otherPrice);
			act.setOutData("otherPrice", otherPrice);
			act.setOutData("bean", bean);
			act.setOutData("list", list);
			act.setForword(CLAIM_REPORT_PRINT);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商索赔管理--索赔单打印");
			act.setException(e1);
		}
	
   }
	/**
	 * 检测是否只是一个人在操作审核该单子
	 */
	public void checkReportClaimNotOne(){
		ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
		int res=dao.findStatusById(request);
		String id = DaoFactory.getParam(request, "ID");
		String type = DaoFactory.getParam(request, "type");
		act.setOutData("id", id);
		act.setOutData("type", type);
		super.setJsonSuccByres(res);
	}
	@SuppressWarnings("rawtypes")
	public boolean jugeReplce(String code){
		boolean flag=false;
		TtAsActivityPO po=new TtAsActivityPO();
		po.setActivityCode(code);
		po.setActivityType(Constant.SERVICEACTIVITY_TYPE_05);
		List select = reportDao.select(po);
		if(select!=null && select.size()==0){
			flag=true;
		}
		return flag;
	}
	/**
	 * 批量操作
	 */
	public void passOrRebutByIds(){
		int res=1;
		try {
			String idStrs = getParam("id");
			String type = getParam("type");
			String auditOpinion = getParam("ADUIT_REMARK");
			String[] ids = StringUtils.split(idStrs, ",");
			if("1".equals(type)){//批量审核通过
				for (String id : ids) {
					reportDao.doPassByClaimTypeAndId(id,auditOpinion,loginUser);
				}
			}
			if("2".equals(type)){//批量退回
				reportDao.doRebutByClaimId(ids,auditOpinion,loginUser);
			}
			for (String id : ids) {
				//-------lj 3.23  更新审核人、时间
				StringBuffer sb = new StringBuffer();
				AclUserBean logonUser = loginUser;
				sb.append(" UPDATE  TT_AS_WR_APPLICATION  TT SET \n");
				sb.append("TT.AUDITING_MAN = "+logonUser.getUserId());
				sb.append(",\n" );
				sb.append("TT.AUDITING_DATE = sysdate " );
				sb.append("\n" );
				sb.append("where TT.ID ="+id);
				reportDao.update(sb.toString(), null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			setJsonSuccByres(res);
		}
	}
}

