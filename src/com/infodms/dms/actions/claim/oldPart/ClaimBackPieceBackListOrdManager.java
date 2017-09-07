package com.infodms.dms.actions.claim.oldPart;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOldPartApproveStoreListBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.bean.TtAsWrPartsitemApplyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.oldPart.ClaimApproveOldPartStoredDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TrReturnLogisticsPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrReturnedOrderDetailPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.jatools.c.b;
/**
 * 类说明：索赔旧件管理--回运清单维护
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimBackPieceBackListOrdManager extends BaseAction{
	public Logger logger = Logger.getLogger(ClaimBackPieceBackListOrdManager.class);
	private ClaimBackListDao dao=ClaimBackListDao.getInstance();
	private final String queryBackListOrdUrl = "/jsp/claim/oldPart/queryBackListMaintain.jsp";
	private final String addClaimPageUrl = "/jsp/claim/oldPart/addClaimBackOrd.jsp";
	private final String queryClaimBackDetailUrl = "/jsp/claim/oldPart/queryClaimBackDetail.jsp";
	private final String modClaimBackDetailUrl = "/jsp/claim/oldPart/modifyClaimBackDetail.jsp";
	private final String exportClaimBackDetailUrl = "/jsp/claim/oldPart/exportClaimBackDetail.jsp";
	/** 旧件回运清单管理 */
	private final String OLDPART_RETURN_MANAGE_INDEX = "/jsp/claim/oldPart/oldPartReturnManage.jsp";
	/** 旧件回运清单新增页面 */
	private final String OLDPART_RETURN_ADD = "/jsp/claim/oldPart/addReturnOrderPage.jsp";
	/** 旧件回运清单明细页面 */
	private final String OLDPART_RETURN_DETAIL = "/jsp/claim/oldPart/queryReturnOrderDetail.jsp";
	/** 旧件回运清单明细页面 */
	private final String LOGISTICS_ORDER_ADD = "/jsp/claim/oldPart/addLogisticsOrderPage.jsp";
	
	//zhumingwei 2011-04-12
	private final String RETURN_MANAGE_INDEX = "/jsp/claim/oldPart/returnManage.jsp";
	private final String ADD_OLD_RETURN = "/jsp/claim/oldPart/addOldReturn.jsp";
	private final String OLDPART_RETURN_ADD_ZW = "/jsp/claim/oldPart/addReturn.jsp";
	private final String OLDPART_RETURN_DETAIL11 = "/jsp/claim/oldPart/queryReturnOrderDetail11.jsp";
	private final String modClaimBackDetailUrl11 = "/jsp/claim/oldPart/modifyClaimBackDetail11.jsp";
	private final String modClaimBackDetailUrl11lj = "/jsp/claim/oldPart/modifyClaimBackDetail11lj.jsp";
	private final String modClaimBackDetailUrl22 = "/jsp/claim/oldPart/modifyClaimBackDetail22.jsp";
	private final String queryClaimBackDetailUrl33 = "/jsp/claim/oldPart/queryClaimBackDetail33.jsp";
	private final String queryClaimBackDetailUrl33Dealer = "/jsp/claim/oldPart/queryClaimBackDetail33Dealer.jsp";
	//旧件抵扣通知单 服务站
	private final String oldPartDeductionQuery_Url= "/jsp/claim/oldPart/oldPartDeductionQuery.jsp";
	//旧件抵扣通知单明细页面
	private final String oldPartDeductionDetail_Url= "/jsp/claim/oldPart/oldPartDeductionDetail.jsp";
	
	private final String CLAIM_DEDUCTION_SECOND_URL= "/jsp/claim/oldPart/claimDeductionSecondQuery.jsp";//二次抵扣索赔单
	private final String CLAIM_DEDUCTION_SECOND_SHOW_URL= "/jsp/claim/oldPart/claimDeductionSecondShow.jsp";//二次抵扣索赔单展示
	
	public void queryListPage(){
		super.sendMsgByUrl(queryBackListOrdUrl, "索赔件回运清单维护--初始化");
	}
	public void queryBackListByCondition(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			//查询条件
			String back_order_no = request.getParamValue("back_order_no");//回运清单号
			String freight_type = request.getParamValue("freight_type");//货运方式
			String create_start_date = request.getParamValue("create_start_date");//建单开始时间
			String create_end_date = request.getParamValue("create_end_date");//建单结束时间
			String report_start_date = request.getParamValue("report_start_date");// 提报起始日期
			String report_end_date = request.getParamValue("report_end_date");// 提报结束日期
			String ord_status=request.getParamValue("ord_status");//处理状态
			String yieldly = request.getParamValue("yieldly");//产地
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer whereStr = new StringBuffer();
			StringBuffer orderByStr = new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,tawor.create_date,tawor.return_date,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.arrive_date,tawor.part_amount,tawor.parkage_amount,\n");
			sqlStr.append("tc.code_desc status_desc,tawor.status,tce.code_desc freight_type\n");
			sqlStr.append("from TT_AS_WR_OLD_RETURNED tawor,tc_code tc,tc_code tce\n");
			sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tce.code_id(+)\n");
			if(back_order_no!=null&&!"".equals(back_order_no))
				whereStr.append(" and tawor.return_no like '%"+back_order_no.toUpperCase()+"%'\n");
			if(freight_type!=null&&!"".equals(freight_type))
				whereStr.append(" and tawor.transport_type="+freight_type+"\n");
			if (create_start_date != null && !"".equals(create_start_date))
				whereStr.append(" and tawor.create_date>=to_date('" + create_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (create_end_date != null && !"".equals(create_end_date))
				whereStr.append(" and tawor.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_start_date != null && !"".equals(report_start_date))
				whereStr.append(" and tawor.return_date>=to_date('" + report_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_end_date != null && !"".equals(report_end_date))
				whereStr.append(" and tawor.return_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if(ord_status!=null&&!"".equals(ord_status))
				whereStr.append(" and tawor.status=" + ord_status+"\n");
			if(yieldly!=null&&!"".equals(yieldly))
				whereStr.append(" and tawor.yieldly=" + yieldly+"\n");
			whereStr.append(" and tawor.dealer_id="+loginUser.getDealerId()+"\n");
			orderByStr.append(" order by tawor.id desc\n");
			
			PageResult<TtAsWrBackListQryBean> ps = dao.queryClaimBackList(sqlStr.toString()+ whereStr.toString() + orderByStr.toString(), curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryBackListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function：索赔件回运清单维护--新建清单页面
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-2 赵伦达
	 */
	public void addClaimBackOrdPage(){
		super.sendMsgByUrl(addClaimPageUrl, "索赔件回运清单维护--新建清单页面");
	}
	/**
	 * Function：索赔件回运清单维护--生成索赔回运清单预操作
	 *           生成索赔回运单预操作
	 *           系统生成一个单号并将时间段回置给前台页面
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-3 赵伦达
	 */
	public void createNewBackOrdPre(){
		try {
			String start_date=request.getParamValue("submit_start_date");
			String end_date=request.getParamValue("submit_end_date");
			String orderId=SequenceManager.getSequence("GO");//生成单号
			act.setOutData("claimBackOrdId", orderId);
			act.setOutData("userSubmitDate", start_date+"至"+end_date);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--生成索赔回运清单预操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单维护--生成回运清单中条件查询
	 *           清单中未回运的清单列表
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-3 赵伦达
	 */
	@SuppressWarnings("unused")
	public void queryClaimUnBackList(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			String command=request.getParamValue("command");
			String claimOrdId=request.getParamValue("claimOrdId");
			String claimQryDate=request.getParamValue("claimQryDate");
			String freight_type=request.getParamValue("freight_type");
			String back_type=request.getParamValue("back_type");
			String boxTotalNum=request.getParamValue("boxTotalNum");
			String q_claim_no=request.getParamValue("q_claim_no");
			String q_vin=request.getParamValue("q_vin");
			String q_part_code=request.getParamValue("q_part_code");
			String q_part_name=request.getParamValue("q_part_name");
			String yieldly = request.getParamValue("yieldly");

			StringBuffer sqlStr=new StringBuffer();
			//dao=ClaimBackListDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			String[] dateArray=claimQryDate.split("#");
			if(command!=null&&"2".equals(command)){//查询索赔申请单表和索赔申请单之索赔配件表
				if(back_type!=null&&Integer.parseInt(back_type)==Constant.BACK_TRANSPORT_TYPE_01){//紧急回运
					sqlStr.append("select t1.id apply_id,t2.part_id,t1.id claim_id,t1.claim_no,t1.vin,t2.part_code,t2.part_name,\n");
					sqlStr.append("t2.BALANCE_QUANTITY-nvl(t2.return_num,0) quantity,nvl(t2.return_num,0) return_num \n");
					sqlStr.append("from TT_AS_WR_APPLICATION t1,TT_AS_WR_PARTSITEM t2,tm_pt_part_base t3 ,tm_vehicle tv\n");
					sqlStr.append("where 1=1 and t1.id=t2.id \n");
					sqlStr.append("and t2.part_code=t3.part_code\n");
					sqlStr.append(" and t1.dealer_id='"+loginUser.getDealerId()+"' \n");
					sqlStr.append(" and (t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_02+"' \n");
					sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_03+"' \n");
					sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_04+"' \n");
					sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"') \n");
					sqlStr.append("and t3.is_return= "+Constant.IS_NEED_RETURN+" \n");
					if(!dateArray[0].equals(dateArray[1])){
						sqlStr.append(" and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
						sqlStr.append(" and t1.report_date<add_months(to_date('"+dateArray[1]+"','YYYY-MM'),1) \n");
					}else{
						sqlStr.append("and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
						sqlStr.append("and t1.report_date<add_months(to_date('"+dateArray[0]+"','YYYY-MM'),1) \n");
					}
					if(q_claim_no!=null&&!"".equals(q_claim_no)){
						sqlStr.append(" and t1.claim_no like '%"+q_claim_no.toUpperCase()+"%'\n");
					}
					if(q_vin!=null&&!"".equals(q_vin)){
						sqlStr.append(" and t1.vin like '%"+q_vin+"%'\n");
					}
					if(q_part_code!=null&&!"".equals(q_part_code)){
						sqlStr.append(" and t2.part_code like '%"+q_part_code+"%'\n");
					}
					if(q_part_name!=null&&!"".equals(q_part_name)){
						sqlStr.append(" and t2.part_name like '%"+q_part_name+"%'\n");
					}
					sqlStr.append(" and tv.vin = t1.vin  and tv.yieldly="+yieldly+" ");
					sqlStr.append(" and t2.BALANCE_QUANTITY-nvl(t2.return_num,0)>0 \n");
					sqlStr.append(" order by t1.id desc ");
				}else if(back_type!=null&&Integer.parseInt(back_type)==Constant.BACK_TRANSPORT_TYPE_02){//常规回运
					sqlStr.append("select t1.id apply_id,t1.id part_id,t1.id claim_id,t1.claim_no,t1.vin,'' part_code,'' part_name,\n" );
					sqlStr.append("sum(nvl(t2.BALANCE_QUANTITY,0)-nvl(t2.return_num,0)) quantity,sum(nvl(t2.return_num,0)) return_num\n" );
					sqlStr.append("from TT_AS_WR_APPLICATION t1,TT_AS_WR_PARTSITEM t2,tm_pt_part_base t3,tm_vehicle tv\n" );
					sqlStr.append("where 1=1 and t1.id=t2.id  and t1.dealer_id='"+loginUser.getDealerId()+"'\n");
					sqlStr.append("and t2.part_code=t3.part_code\n");
					//sqlStr.append("and t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_04+"'\n" );//
					//业务要求,技术室审核通过后并且未回运的所有单子
					sqlStr.append("and t1.status in (").append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
					sqlStr.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
					sqlStr.append("and t3.is_return= "+Constant.IS_NEED_RETURN+" \n");
					if(!dateArray[0].equals(dateArray[1])){
						sqlStr.append(" and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
						sqlStr.append(" and t1.report_date<add_months(to_date('"+dateArray[1]+"','YYYY-MM'),1) \n");
					}else{
						sqlStr.append("and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
						sqlStr.append("and t1.report_date<add_months(to_date('"+dateArray[0]+"','YYYY-MM'),1) \n");
					}
					if(q_claim_no!=null&&!"".equals(q_claim_no)){
						sqlStr.append(" and t1.claim_no like '%"+q_claim_no.toUpperCase()+"%'\n");
					}
					if(q_vin!=null&&!"".equals(q_vin)){
						sqlStr.append(" and t1.vin like '%"+q_vin+"%'\n");
					}
					if(q_part_code!=null&&!"".equals(q_part_code)){
						sqlStr.append(" and t2.part_code like '%"+q_part_code+"%' \n");
					}
					if(q_part_name!=null&&!"".equals(q_part_name)){
						sqlStr.append(" and t2.part_name like '%"+q_part_name+"%' \n");
					}
					sqlStr.append(" and t2.BALANCE_QUANTITY-nvl(t2.return_num,0)>0 \n");
					sqlStr.append("    and tv.vin = t1.vin  and tv.yieldly="+yieldly+" ");
					sqlStr.append("group by t1.id,t1.id,t1.claim_no,t1.vin\n" );
					sqlStr.append("order by t1.id desc");
				}
				System.out.println("---------------"+sqlStr.toString());
				
				Integer pageSize = Integer.MAX_VALUE;
				PageResult<TtAsWrPartsitemApplyBean> ps=dao.queryUnBackClaimList(sqlStr.toString(), curPage, pageSize);
				act.setOutData("ps", ps);
				act.setOutData("joinFlag", "0");
				act.setForword(addClaimPageUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--生成回运清单中条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单维护--加入索赔单信息
	 *          说明：
	 *          常规回运：将一张或多张索赔单下的配件统统回运
	 *          紧急回运：将一张或多张索赔单下的其中某个配件进行紧急回运
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-5 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void joinClaimOldPart(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			//dao=ClaimBackListDao.getInstance();
			//request中取出的参数
			String claimOrdId=trimStr(request.getParamValue("claimOrdId"));//回运清单号
			String claimQryDate=trimStr(request.getParamValue("claimQryDate"));//提报日期
			String freight_type=trimStr(request.getParamValue("freight_type"));//货运方式
			String back_type=trimStr(request.getParamValue("back_type"));//回运类型
			String boxTotalNum=trimStr(request.getParamValue("boxTotalNum"));//装箱总数
			String q_claim_no=request.getParamValue("q_claim_no");//索赔单查询条件
			String q_vin=request.getParamValue("q_vin");//VIN查询条件
			String q_part_code=request.getParamValue("q_part_code");//配件代码查询条件
			String q_part_name=request.getParamValue("q_part_name");//配件名称查询条件
			String remark = request.getParamValue("remark");//备注
			String idStr=trimStr(request.getParamValue("idStr"));//选中的id串
			int backNum=getTotalBackPartNum(request,idStr);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			int pageAddPartNum=0;//记录页面添加配件数
			int pageAddPartItemNum=0;//记录页面添加配件项数
			int pageAddClaimum=0;//记录页面新添加索赔单数
			String[] array=idStr.split(",");//将前台选中记录存成数组，常规回运存放的是索赔表中的id，紧急回运存放的是索赔配件表中的配件id
			//以下是常规回运流程
			if(Constant.BACK_TRANSPORT_TYPE_02.equals(Integer.parseInt(back_type))){
				Map params=new HashMap();
				params.put("company_id", companyId);
				params.put("idStr", idStr);
				params.put("claimOrdId", claimOrdId);
				params.put("claimQryDate", claimQryDate);
				params.put("freight_type", freight_type);
				params.put("back_type", back_type);
				params.put("boxTotalNum", boxTotalNum);
				params.put("remark", remark);
				@SuppressWarnings("unused")
				Map retMap=dao.addNormalBackProcess(request, params,loginUser.getUserId(), Long.parseLong(loginUser.getDealerId()));
			}else if(Constant.BACK_TRANSPORT_TYPE_01.equals(Integer.parseInt(back_type))){//以下是紧急回运流程
				//根据索赔申请id查询在索赔回运表中是否存在
				if(claimOrdId!=null&&!"".equals(claimOrdId)){
					long mainTableId=Long.parseLong(SequenceManager.getSequence(""));
					if(dao.isExistClaimBackId(claimOrdId)){//在回运单中已存在，只需要存回运明细
						int existClaimNoNum=dao.getClaimOrdToTalNum(claimOrdId);//取出已存在索赔单数
						int existPartItemNum=dao.getClaimPartItemNumByClaimId(claimOrdId);//取出已存在配件项数
						int existPartNum=dao.getClaimPartNum(claimOrdId);//取出已存在配件数
						int newAddPartNum=0;
						//保存回运明细实体
						for(int i=0;i<array.length;i++){
							TtAsWrOldReturnedDetailPO voTAWORDP=new TtAsWrOldReturnedDetailPO();
							TtAsWrApplicationPO applyObj=new TtAsWrApplicationPO();
							TtAsWrPartsitemPO partItem=new TtAsWrPartsitemPO();
							applyObj=dao.getClaimApplyOrdPo(array[i]);
							partItem=dao.getClaimPartItemPo(array[i]);
							TmVehiclePO tmVinInfo=dao.getVehicleInfo(applyObj.getVin());
							//判断索赔回运清单明细表中是否存在
							if(dao.isExistPartitemAtBackDetail(array[i])){//存在
								int samePartExistedNum=0;
								samePartExistedNum=dao.getExistReturnedDetailPartNum(array[i]);//获取该配件在索赔回运明细中的回运数量
								if(samePartExistedNum>0){//存在将索赔配件总数减去已存在的回运明细中的回运数量得到需回运数量
									voTAWORDP.setNReturnAmount(Float.valueOf(partItem.getQuantity()-samePartExistedNum).intValue());//需回运数量
								}else{//不存在将总数直接存放到明细表中的需回运数量
									voTAWORDP.setNReturnAmount(Float.valueOf(partItem.getQuantity()).intValue());//需回运数量
								}
								newAddPartNum+=Integer.parseInt(request.getParamValue("urgeBackNum"+array[i]));
								pageAddPartNum+=(dao.getBackedPartNumByReturnPartId(claimOrdId,array[i])
								                +Integer.parseInt(request.getParamValue("urgeBackNum"+array[i])));
								voTAWORDP.setReturnAmount(dao.getBackedPartNumByReturnPartId(claimOrdId,array[i])
										+Integer.parseInt(request.getParamValue("urgeBackNum"+array[i])));//回运数量
								voTAWORDP.setUpdateBy(loginUser.getUserId());
								voTAWORDP.setUpdateDate(new Date());
								dao.updateClaimBackOrdDetailInfo(String.valueOf(dao.getDetailMainId(applyObj.getClaimNo(),array[i])),
										voTAWORDP);
								//修改索赔申请单之索赔配件中的配件已回运数量信息
								TtAsWrPartsitemPO tawpp=new TtAsWrPartsitemPO();
								tawpp.setReturnNum(Integer.valueOf(samePartExistedNum+Integer.parseInt(request.getParamValue("urgeBackNum"+array[i]))).floatValue());
								tawpp.setUpdateBy(loginUser.getUserId());
								tawpp.setUpdateDate(new Date());
								dao.updateClaimApplyPartInfo(array[i], tawpp);
							}else{//不存在,向索赔回运清单明细表插入新数据
								voTAWORDP.setId(Long.parseLong(SequenceManager.getSequence("")));
								pageAddPartItemNum+=1;//有新的配件加入
								newAddPartNum+=Integer.parseInt(request.getParamValue("urgeBackNum"+array[i]));
								voTAWORDP.setReturnId(Long.parseLong(dao.getReturnIdByReturnNo(claimOrdId)));
								voTAWORDP.setClaimNo(applyObj.getClaimNo());
								//判断是否有新的索赔单插入
								if(!dao.isExistSameClaimNo(String.valueOf(mainTableId).toString(),applyObj.getClaimNo())){
									pageAddClaimum+=1;
								}
								voTAWORDP.setVin(applyObj.getVin());
								voTAWORDP.setPartId(Long.parseLong(array[i]));
								voTAWORDP.setNReturnAmount(Float.valueOf(partItem.getQuantity()).intValue());
								pageAddPartNum+=Integer.parseInt(request.getParamValue("urgeBackNum"+array[i]));
								voTAWORDP.setReturnAmount(Integer.parseInt(request.getParamValue("urgeBackNum"+array[i])));
								voTAWORDP.setBoxNo(request.getParamValue("boxOrd"+array[i]));
								voTAWORDP.setSignAmount(0);
								voTAWORDP.setProducerCode(partItem.getProducerCode());//配件生产商code
								voTAWORDP.setProducerName(partItem.getProducerName());//配件生产商name
								voTAWORDP.setCreateDate(new Date());
								voTAWORDP.setCreateBy(loginUser.getUserId());
								voTAWORDP.setProcFactory(tmVinInfo==null?"":String.valueOf(tmVinInfo.getYieldly()));//产地
								dao.insert(voTAWORDP);
								//修改索赔申请单之索赔配件中的配件已回运数量信息
								TtAsWrPartsitemPO tawpp=new TtAsWrPartsitemPO();
								tawpp.setReturnNum(Integer.valueOf(request.getParamValue("urgeBackNum"+array[i])).floatValue());
								tawpp.setUpdateBy(loginUser.getUserId());
								tawpp.setUpdateDate(new Date());
								dao.updateClaimApplyPartInfo(array[i], tawpp);
							}
							//修改索赔回运清单表
							TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
							mainPo.setTransportType(Integer.parseInt(freight_type));//货运方式
							if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
								mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
							}
							mainPo.setWrStartDate(claimQryDate.replace("#", "至"));
							mainPo.setReturnType(Integer.parseInt(back_type));
							mainPo.setWrAmount(pageAddClaimum+existClaimNoNum);//索赔单数
							mainPo.setPartItemAmount(pageAddPartItemNum+existPartItemNum);//回运配件项数
							mainPo.setPartAmount(newAddPartNum+existPartNum);//回运配件数
							mainPo.setStatus(Constant.BACK_LIST_STATUS_01);//将回运清单置为未上报状态
							mainPo.setUpdateBy(loginUser.getUserId());
							mainPo.setUpdateDate(new Date());
							mainPo.setOemCompanyId(companyId);
							dao.updateClaimBackOrdMainInfo(claimOrdId, mainPo);
						}
					}else{
						//保存索赔回运清单表
						TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
						mainPo.setId(mainTableId);
						mainPo.setDealerId(Long.parseLong(loginUser.getDealerId()));
						mainPo.setReturnNo(claimOrdId);
						mainPo.setWrAmount(dao.getClaimTotalNum("part_id",idStr.replace(",","','")));
						mainPo.setPartItemAmount(dao.getClaimItemTotalNum("part_id",idStr.replace(",","','")));
						mainPo.setPartAmount(backNum);
						if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
							mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
						}
						mainPo.setTransportType(Integer.parseInt(freight_type));
						mainPo.setStatus(Constant.BACK_LIST_STATUS_01);
						mainPo.setReturnType(Integer.parseInt(back_type));
						mainPo.setWrStartDate(claimQryDate);
						mainPo.setCreateDate(new Date());
						mainPo.setCreateBy(loginUser.getUserId());
						mainPo.setOemCompanyId(companyId);
						if(remark!=null&&!remark.equals("")){
						mainPo.setRemark(remark);
						}
						dao.insert(mainPo);
						
						if(dao.isExistClaimBackId(claimOrdId)){
							//保存索赔清单回运明细表
							for(int i=0;i<array.length;i++){
								TtAsWrOldReturnedDetailPO insertTAWORDP=new TtAsWrOldReturnedDetailPO();
								TtAsWrApplicationPO applyObj=new TtAsWrApplicationPO();
								TtAsWrPartsitemPO partItem=new TtAsWrPartsitemPO();
								applyObj=dao.getClaimApplyOrdPo(array[i]);
								partItem=dao.getClaimPartItemPo(array[i]);
								TmVehiclePO tmVinInfo=dao.getVehicleInfo(applyObj.getVin());
								
								insertTAWORDP.setId(Long.parseLong(SequenceManager.getSequence("")));
								insertTAWORDP.setReturnId(mainTableId);
								insertTAWORDP.setClaimNo(trimStr(applyObj.getClaimNo()));
								insertTAWORDP.setVin(applyObj.getVin());
								insertTAWORDP.setPartId(Long.parseLong(array[i]));
								insertTAWORDP.setNReturnAmount(Float.valueOf(partItem.getQuantity()).intValue());
								insertTAWORDP.setReturnAmount(Integer.parseInt(request.getParamValue("urgeBackNum"+array[i])));
								insertTAWORDP.setBoxNo(request.getParamValue("boxOrd"+array[i]));
								insertTAWORDP.setSignAmount(0);
								insertTAWORDP.setProducerCode(partItem.getProducerCode());//配件生产商code
								insertTAWORDP.setProducerName(partItem.getProducerName());//配件生产商name
								insertTAWORDP.setCreateDate(new Date());
								insertTAWORDP.setCreateBy(loginUser.getUserId());
								insertTAWORDP.setProcFactory(tmVinInfo==null?"":String.valueOf(tmVinInfo.getYieldly()));//产地
								dao.insert(insertTAWORDP);
								//修改索赔申请单之索赔配件中的配件已回运数量信息
								TtAsWrPartsitemPO tawpp=new TtAsWrPartsitemPO();
								tawpp.setReturnNum(partItem.getReturnNum()+
										Integer.valueOf(request.getParamValue("urgeBackNum"+array[i])).floatValue());
								tawpp.setUpdateBy(loginUser.getUserId());
								tawpp.setUpdateDate(new Date());
								dao.updateClaimApplyPartInfo(array[i], tawpp);
							}
						}
					}
			    }
			}
			//返回查询结果集
			StringBuffer sqlStr=new StringBuffer();
			String[] dateArray=claimQryDate.split("至");
			if(back_type!=null&&Integer.parseInt(back_type)==Constant.BACK_TRANSPORT_TYPE_01){//紧急回运
				sqlStr.append("select t1.id apply_id,t2.part_id,t1.id claim_id,t1.claim_no,t1.vin,t2.part_code,t2.part_name,\n");
				sqlStr.append("t2.quantity-nvl(t2.return_num,0) quantity,nvl(t2.return_num,0) return_num \n");
				sqlStr.append("from TT_AS_WR_APPLICATION t1,TT_AS_WR_PARTSITEM t2,tm_pt_part_base t3 \n");
				sqlStr.append("where 1=1 and t1.id=t2.id \n");
				sqlStr.append("and t2.part_code=t3.part_code\n");
				sqlStr.append(" and t1.dealer_id='"+loginUser.getDealerId()+"' \n");
				sqlStr.append(" and (t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_02+"' \n");
				sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_03+"' \n");
				sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_04+"' \n");
				sqlStr.append(" or t1.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"') \n");
				sqlStr.append("and t3.is_return= "+Constant.IS_NEED_RETURN+" \n");
				if(!dateArray[0].equals(dateArray[1])){
					sqlStr.append(" and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
					sqlStr.append(" and t1.report_date<=to_date('"+dateArray[1]+"','YYYY-MM') \n");
				}else{
					sqlStr.append("and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
					sqlStr.append("and t1.report_date<=last_day(to_date('"+dateArray[0]+"','YYYY-MM')) \n");
				}
				if(q_claim_no!=null&&!"".equals(q_claim_no)){
					sqlStr.append(" and t1.claim_no like '%"+q_claim_no.toUpperCase()+"%'\n");
				}
				if(q_vin!=null&&!"".equals(q_vin)){
					sqlStr.append(" and t1.vin like '%"+q_vin+"%'\n");
				}
				if(q_part_code!=null&&!"".equals(q_part_code)){
					sqlStr.append(" and t2.part_code like '%"+q_part_code+"%'\n");
				}
				if(q_part_name!=null&&!"".equals(q_part_name)){
					sqlStr.append(" and t2.part_name like '%"+q_part_name+"%'\n");
				}
				sqlStr.append(" and t2.quantity-nvl(t2.return_num,0)>0 \n");
				sqlStr.append(" order by t1.id desc ");
			}else if(back_type!=null&&Integer.parseInt(back_type)==Constant.BACK_TRANSPORT_TYPE_02){//常规回运
				sqlStr.append("select t1.id apply_id,t1.id part_id,t1.id claim_id,t1.claim_no,t1.vin,'' part_code,'' part_name,\n" );
				sqlStr.append("sum(t2.BALANCE_QUANTITY-nvl(t2.return_num,0)) quantity,sum(nvl(t2.return_num,0)) return_num\n" );
				sqlStr.append("from TT_AS_WR_APPLICATION t1,TT_AS_WR_PARTSITEM t2,tm_pt_part_base t3 \n" );
				sqlStr.append("where 1=1 and t1.id=t2.id  and t1.dealer_id='"+loginUser.getDealerId()+"'\n");
				sqlStr.append("and t2.part_code=t3.part_code\n");
				sqlStr.append("and t1.status in (").append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
				sqlStr.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");//技术室审核通过后旧件回运
				sqlStr.append("and t3.is_return= "+Constant.IS_NEED_RETURN+" \n");
				if(!dateArray[0].equals(dateArray[1])){
					sqlStr.append(" and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
					sqlStr.append(" and t1.report_date<ADD_MONTHS(to_date('"+dateArray[1]+"','YYYY-MM'),1) \n");
				}else{
					sqlStr.append("and t1.report_date>=to_date('"+dateArray[0]+"','YYYY-MM') \n");
					sqlStr.append("and t1.report_date<ADD_MONTHS(to_date('"+dateArray[0]+"','YYYY-MM'),1) \n");
				}
				if(q_claim_no!=null&&!"".equals(q_claim_no)){
					sqlStr.append(" and t1.claim_no like '%"+q_claim_no.toUpperCase()+"%'\n");
				}
				if(q_vin!=null&&!"".equals(q_vin)){
					sqlStr.append(" and t1.vin like '%"+q_vin+"%'\n");
				}
				if(q_part_code!=null&&!"".equals(q_part_code)){
					sqlStr.append(" and t2.part_code like '%"+q_part_code+"%' \n");
				}
				if(q_part_name!=null&&!"".equals(q_part_name)){
					sqlStr.append(" and t2.part_name like '%"+q_part_name+"%' \n");
				}
				sqlStr.append(" and t2.BALANCE_QUANTITY-nvl(t2.return_num,0)>0 \n");
				sqlStr.append("group by t1.id,t1.id,t1.claim_no,t1.vin\n" );
				sqlStr.append("order by t1.id desc");
			}
			Integer pageSize = Integer.MAX_VALUE;
			PageResult<TtAsWrPartsitemApplyBean> ps=dao.queryUnBackClaimList(sqlStr.toString(), curPage, pageSize);
			act.setOutData("joinFlag", "1");//加入成功标志
			act.setOutData("ps", ps);
			act.setForword(addClaimPageUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔件回运清单维护--加入索赔单信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单维护--获取选中记录的配件数量
	 * @return:		@param req
	 * @return:		@param idStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-5
	 */
	public int getTotalBackPartNum(RequestWrapper req,String idStr){
		int retNum=0;
		try {
			if(idStr==null||"".equals(idStr)) return 0;
			String[] array=idStr.split(",");
			
			for(int count=0;count<array.length;count++){
				String backNum=req.getParamValue("urgeBackNum"+array[count]);
				if(backNum!=null&&!"".equals(backNum)){
					retNum+=Integer.parseInt(backNum);
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--获取选中记录的配件数量");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		return retNum;
	}
	/**
	 * Function：索赔件回运清单维护--查询明细
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	public void queryBackClaimDetailInfo(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String oper=request.getParamValue("oper");//获取操作动作
			// 处理当前页
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_type,tcc.code_desc transport_desc,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc,tc_user tu\n" );
			sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append("and tawor.create_by=tu.user_id and tawor.return_type=tccc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			sqlStr.delete(0,sqlStr.length());
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.part_id,tawp.part_code,\n");
			if ("query".equals(oper) || "export".equals(oper)) {
				sqlStr.append("tawp.part_name,taword.n_return_amount,taword.return_amount,\n");
			}else{
				if(detailInfoBean.getReturn_type().equals(Constant.BACK_TRANSPORT_TYPE_01)){//紧急回运
					sqlStr.append("tawp.part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
				}else{//常规回运
					sqlStr.append("tawp.part_name,taword.n_return_amount,taword.return_amount,\n");
				}
			}
			sqlStr.append("taword.box_no,taword.warehouse_region,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,tcc.code_desc proc_factory\n");
			sqlStr.append("from tt_as_wr_old_returned_detail taword,\n");
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc,tc_code tcc\n");
			sqlStr.append("where taword.part_id=tawp.part_id\n");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+)\n");
			sqlStr.append("and taword.proc_factory=tcc.code_id(+)\n");
			sqlStr.append("and taword.return_id="+claimId+"\n");
			sqlStr.append(" order by taword.box_no, taword.claim_no, taword.part_name,taword.barcode_no ");
			//sqlStr.append(" order by tcc.code_desc,taword.create_date, nlssort(tawp.part_CODE,'NLS_SORT=SCHINESE_RADICAL_M')\n");
			System.out.println("sql=="+sqlStr);
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			
			if("query".equals(oper)){//转到查询明细页面
				act.setForword(queryClaimBackDetailUrl);
			}else if("mod".equals(oper)){//转到修改页面
				act.setForword(modClaimBackDetailUrl);
			}else if("export".equals(oper)) { //转到导出数据详细页面
				act.setForword(exportClaimBackDetailUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	public void queryBackClaimDetailInfo33(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String oper=request.getParamValue("oper");//获取操作动作
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,to_char(tawor.price,'FM999999990.00') price,to_char(tawor.auth_price,'FM999999990.00') auth_price,tawor.price_remark,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tawor.sign_remark,tawor.transport_remark,tawor.tel,tccc.code_desc return_desc,tawor.status,tawor.remark,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_no,tawor.transport_type,tcc.code_desc transport_desc,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,(select c.name from tc_user c where c.user_id=tawor.create_by) as creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc\n" );
			sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append("and tawor.return_type=tccc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.barcode_no,taword.id,to_char(a.report_date,'YYYY-MM-DD') as report_date,taword.claim_no,taword.vin,taword.part_id,tawp.old_part_code as part_code,\n");
			if ("query".equals(oper) || "export".equals(oper)) {
				sqlStr.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
			}else{
				if(detailInfoBean.getReturn_type().equals(Constant.BACK_TRANSPORT_TYPE_01)){//紧急回运
					sqlStr.append("tawp.old_part_cname as part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
				}else{//常规回运
					sqlStr.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
				}
			}
			sqlStr.append("taword.box_no,taword.warehouse_region,to_char(taword.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,taword.other_remark,ba.area_name  proc_factory,taword.sign_amount\n");
			sqlStr.append("from tt_as_wr_returned_order_detail taword, tm_business_area ba,tt_as_wr_application_claim a,\n");
			sqlStr.append("tt_as_wr_app_part tawp,tc_code tc \n");
			sqlStr.append("where taword.claim_part_id=tawp.claim_part_id  \n");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+)\n");
			sqlStr.append("and taword.proc_factory=ba.area_id(+) and taword.claim_id=a.id \n");
			sqlStr.append("and taword.return_id="+claimId+"\n");
			//sqlStr.append(" ORDER BY TAWORD.BOX_NO, TAWORD.CLAIM_NO, TAWORD.PART_NAME, TAWORD.BARCODE_NO\n");
			sqlStr.append(" ORDER BY TAWORD.Claim_No,a.report_date\n");
			System.out.println("sql=="+sqlStr);
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> flist= smDao.queryAttachFileInfo(claimId);
			act.setOutData("flist", flist);
			
			if("query".equals(oper)){//转到查询明细页面
				act.setForword(queryClaimBackDetailUrl33);
			}else if("mod".equals(oper)){//转到修改页面
				act.setForword(modClaimBackDetailUrl);
			}else if("export".equals(oper)) { //转到导出数据详细页面
				act.setForword(exportClaimBackDetailUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	@SuppressWarnings("unused")
	public void queryBackClaimDetailInfo33Dealer(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String oper=request.getParamValue("oper");//获取操作动作
			// 处理当前页
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sb=new StringBuffer();
			sb.append("select \n" );
//			sb.append("         (select d.transport_id\n" );
//			sb.append("          from tm_oldpart_transport_detail d\n" );
//			sb.append("         where d.detail_id = tawor.tran_person) as transport_id,\n" );
//			sb.append("          (select d.transport_name\n" );
//			sb.append("          from tm_oldpart_transport_detail d\n" );
//			sb.append("         where d.detail_id = tawor.tran_person) as transport_name,");
			sb.append("  tawor.id,tawor.sign_remark,tawor.price,tawor.auth_price,tawor.price_remark,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sb.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sb.append("tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,tawor.remark,\n" );
			sb.append("tc.code_desc status_desc,tawor.transport_company,tawor.transport_remark,tawor.transport_type,tcc.code_desc transport_desc,\n" );
			sb.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.transport_no tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sb.append("from tt_as_wr_returned_order tawor,tc_code tc,tc_code tcc,\n" );
			sb.append("tc_code tccc,tc_user tu\n" );
			sb.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sb.append(" and tawor.return_type=tccc.code_id(+) and tawor.create_by=tu.user_id(+)\n" );
			sb.append("and tawor.id="+claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sb.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sb.delete(0,sb.length());
			//根据回运主键查询索赔配件明细表信息
			sb.append("select taword.barcode_no,taword.id,to_char((select report_date from tt_as_wr_application_claim where id = taword.claim_id),'YYYY-MM-DD') as report_date,taword.claim_no,taword.vin,taword.part_id,tawp.old_part_code as part_code,\n");
			if ("query".equals(oper) || "export".equals(oper)) {
				sb.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
			}else{
				if(detailInfoBean.getReturn_type().equals(Constant.BACK_TRANSPORT_TYPE_01)){//紧急回运
					sb.append("tawp.old_part_cname as part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
				}else{//常规回运
					sb.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
				}
			}
			sb.append("taword.box_no,taword.warehouse_region,");
			sb.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,taword.other_remark,ba.area_name  proc_factory,taword.sign_amount\n");
			sb.append("from tt_as_wr_returned_order_detail taword, tm_business_area ba,\n");
			sb.append("tt_as_wr_app_part tawp,tc_code tc \n");
			sb.append("where taword.claim_part_id=tawp.claim_part_id  \n");
			sb.append("and taword.deduct_remark=tc.code_id(+)\n");
			sb.append("and taword.proc_factory=ba.area_id(+)\n");
			sb.append("and taword.return_id="+claimId+"\n");
			sb.append(" ORDER BY TAWORD.BOX_NO, TAWORD.CLAIM_NO, TAWORD.PART_NAME, TAWORD.BARCODE_NO\n");
			System.out.println("sql=="+sb);
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sb.toString());
			act.setOutData("detailList", detailList);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claimId);
			act.setOutData("fileList", fileList);
			
				act.setForword(queryClaimBackDetailUrl33Dealer);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//zyw 2015-1-28重构优化 
	public void queryBackClaimDetailInfo11(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=getParam("ORDER_ID");//获取物流单ID
			String borrow_no=getParam("borrow_no");//borrow_no
			String partCode = getParam("part_code");
			String partName = getParam("part_name");
			if(Utility.testString(partName)){
				partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
			}
			String oper=getParam("oper");//获取操作动作
			StringBuffer sql = getReturnSqlByClaimId(claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sql.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			StringBuffer sb = getListRerurnSql(claimId, partCode, partName,oper, detailInfoBean);
			
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sb.toString());
			if(checkListNull(detailList)){
				if(BaseUtils.testString(borrow_no)){
					TtAsPartBorrowPO borrow=new TtAsPartBorrowPO();
					borrow.setId(Long.parseLong(borrow_no) );
					List<TtAsPartBorrowPO> borrows= dao.select(borrow);
					if(checkListNull(borrows)){
						borrow=borrows.get(0);
						act.setOutData("borrowPerson", borrow.getBorrowPerson());
						act.setOutData("borrowPhone", borrow.getBorrowPhone());
						act.setOutData("requireDate", borrow.getRequireDate());
					}
				}
		   }
			act.setOutData("detailList", detailList);
			act.setOutData("returnId", claimId);
			act.setOutData("part_code", partCode);
			act.setOutData("part_name", partName);
			if("query".equals(oper)){//转到查询明细页面
				act.setForword(queryClaimBackDetailUrl);
			}else if("mod".equals(oper)){//转到修改页面
				act.setForword(modClaimBackDetailUrl11);
			}else if("export".equals(oper)) { //转到导出数据详细页面
				act.setForword(exportClaimBackDetailUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryBackClaimDetailInfo11lj(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String return_no=getParam("return_no");//回运单ID
			Map<String,Object> map=dao.finddatabyreturnno(return_no);
			AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("map", map);
			act.setOutData("loginUser", loginUser.getName());
			act.setForword(modClaimBackDetailUrl11lj);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	private StringBuffer getListRerurnSql(String claimId, String partCode,
			String partName, String oper,
			TtAsWrOldPartBackListDetailBean detailInfoBean) {
		StringBuffer sb =new StringBuffer();
		sb.append(" select * from  (");
		//根据回运主键查询索赔配件明细表信息
		sb.append("select taword.barcode_no,(select a.repair_type from tt_as_wr_application_claim a where a.id=taword.claim_id) as claim_type,taword.id,taword.claim_no,taword.vin,taword.part_id,tawp.old_part_code as part_code,\n");
		if ("query".equals(oper) || "export".equals(oper)) {
			sb.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
		}else{
			if(Constant.BACK_TRANSPORT_TYPE_01.equals(detailInfoBean.getReturn_type())){//紧急回运
				sb.append("tawp.old_part_cname as part_name,nvl(tawp.quantity,0)-nvl(tawp.return_num,0)+nvl(taword.return_amount,0) n_return_amount,taword.return_amount,\n");
			}else{//常规回运
				sb.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
			}
		}
		sb.append("taword.box_no,taword.warehouse_region,");
		sb.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,ba.area_name proc_factory\n");
		sb.append("from tt_as_wr_returned_order_detail taword,\n");
		sb.append("tt_as_wr_app_part tawp,tc_code tc,tm_business_area ba\n");
		sb.append("where taword.claim_part_id=tawp.claim_part_id\n");
		sb.append("and taword.deduct_remark=tc.code_id(+)\n");
		sb.append("and taword.proc_factory=ba.area_id(+)\n");
		sb.append("and taword.return_id="+claimId+"\n");
		DaoFactory.getsql(sb, "tawp.old_part_code", partCode, 2);
		DaoFactory.getsql(sb, "tawp.old_part_cname", partName, 2);
		sb.append(" ORDER BY taword.box_no,taword.claim_no,taword.part_name,taword.barcode_no ");
		//根据排序让服务活动的排在最前面
		sb.append("    )  t where 1 = 1 order by case when t.claim_type = "+Constant.REPAIR_TYPE_02+" then 1 else 2 end asc ");
		return sb;
	}
	private StringBuffer getReturnSqlByClaimId(String claimId) {
		//根据回运主键查询索赔配件清单表信息
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append(" select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
		sqlStr.append(" tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
		sqlStr.append(" tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,decode(tawor.remark,null,'',tawor.remark) remark,\n" );
		sqlStr.append(" tc.code_desc status_desc,tawor.transport_type,tcc.code_desc transport_desc,decode(tawor.price,null,0,tawor.price) price,\n" );
		sqlStr.append(" to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
		sqlStr.append(" from tt_as_wr_returned_order tawor,tc_code tc,tc_code tcc,\n" );
		sqlStr.append(" tc_code tccc,tc_user tu\n" );
		sqlStr.append(" where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
		sqlStr.append(" and tawor.create_by=tu.user_id(+) and tawor.return_type=tccc.code_id(+)\n" );
		sqlStr.append(" and tawor.id="+claimId+"\n" );
		return sqlStr;
	}
	
	//zhumingwei 2011-04-13
	public void queryBackClaimDetailInfo22(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取物流单ID
			String return_type = DaoFactory.getParam(request, "return_type");
			act.setOutData("claimId", claimId);
			act.setOutData("return_type", return_type);
			TtAsWrReturnedOrderPO po = new TtAsWrReturnedOrderPO();
			po.setId(Long.parseLong(claimId));
			TtAsWrReturnedOrderPO poValue = (TtAsWrReturnedOrderPO)dao.select(po).get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//货运单号
			if(poValue.getReturnNo()!=null){
				String returnNo  = poValue.getReturnNo();
				act.setOutData("returnNo", returnNo);
			}
			//发运日期
			if(poValue.getSendTime()!=null){
				String sendTime = sdf.format(poValue.getSendTime());
				act.setOutData("sendTime", sendTime);
			}
			//预计到货时间
			if (poValue.getArriveDate() != null) {
				String arriveDate = sdf.format(poValue.getArriveDate());
				act.setOutData("arriveDate", arriveDate);
			}
//			TtAsWrReturnedOrderPO op = new TtAsWrReturnedOrderPO();
//			TrReturnLogisticsPO sp = new TrReturnLogisticsPO();
//			sp.setLogictisId(Long.parseLong(claimId));
//			sp = (TrReturnLogisticsPO) dao.select(sp).get(0);
//			op.setId(sp.getReturnId());
			 
			 String notice="";
			 List<Map<String, Object>> select=null;
			 List<TmOldpartTransportPO> sList = dao.queryGetTransPList(getCurrDealerId());
			 if(sList==null || sList.size()<1){
				 notice="noTrans";
				 select=dao.getStr(1L);
			 }else{
				 select=dao.getStr(sList,return_type);
			 }
			System.out.println(select);
			act.setOutData("poValue", poValue);
			act.setOutData("notice", notice);
			act.setOutData("select", select);
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fsList= smDao.queryAttachFileInfo(claimId);
			act.setOutData("fsList", fsList);
			
			act.setForword(modClaimBackDetailUrl22);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 判断数量
	 */
	public void jugeMax(){
		String msg="false";
		String backId=request.getParamValue("i_back_id");//获取回运清单的修改主键
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tt_as_wr_old_returned_detail where return_id="+backId);
		List<TtAsWrOldReturnedDetailPO> returnDetaiList=null;
		//dao=ClaimBackListDao.getInstance();
		returnDetaiList=dao.getOldReturnedDetailPOByReturnId(sqlStr.toString(), 1,10000);
		String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
		List listBoxNo=new ArrayList();
		
		for(int count=0;count<returnDetaiList.size();count++){
			TtAsWrOldReturnedDetailPO taordp=(TtAsWrOldReturnedDetailPO)returnDetaiList.get(count);//取出修改前明细数据
			String num=request.getParamValue("boxOrd"+taordp.getId());
			if(num!=null){
				if(!listBoxNo.contains(num)){
					listBoxNo.add(num);
				}
			}
		}
		if(listBoxNo.size()>Integer.parseInt(boxTotalNum)){
			msg="true";
		}
		act.setOutData("msg", msg);
	}
	/**
	 * Function：索赔件回运清单维护--修改
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	public void updateReturnListInfo(){
		//dao=ClaimBackListDao.getInstance();
		String backId=request.getParamValue("i_back_id");//获取回运清单的修改主键
		String return_no=request.getParamValue("i_return_no");//获取回运清单的修改主键
		String freight_type=request.getParamValue("i_freight_type");//获取货运方式
		String return_type=request.getParamValue("i_return_type");//获取回运类型
		String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
		String transNo=request.getParamValue("transNo");//货运单号
		String sendDate = request.getParamValue("sendDate");//发运日期
		String tel = request.getParamValue("tel");//三包员电话
		
		List<TtAsWrOldReturnedDetailPO> returnDetaiList=null;
		StringBuffer sqlStr=new StringBuffer();
		int ret=0;
		//int retDetail=0;
		try{
			//保存索赔回运明细信息表
			sqlStr.append("select * from tt_as_wr_old_returned_detail where return_id="+backId);
			returnDetaiList=dao.getOldReturnedDetailPOByReturnId(sqlStr.toString(), 1,10000);
			int updateReturnNum=0;//记录修改的回运配件数
			for(int count=0;count<returnDetaiList.size();count++){
				TtAsWrOldReturnedDetailPO taordp=(TtAsWrOldReturnedDetailPO)returnDetaiList.get(count);//取出修改前明细数据
				if(Constant.BACK_TRANSPORT_TYPE_01.equals(Integer.valueOf(return_type))){//保存紧急回运类型修改回运数
					updateReturnNum+=Integer.parseInt(request.getParamValue("returnNum"+taordp.getId()));
				}
			}
			//保存索赔回运清单表信息
			TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
			
			mainPo.setTransportType(Integer.parseInt(freight_type));//货运方式
			mainPo.setTranNo(transNo==null||"".equals(transNo)?"":transNo);//设置货运单号
			if(Constant.BACK_TRANSPORT_TYPE_01.equals(Integer.valueOf(return_type))){//保存紧急回运类型修改主表配件数
				mainPo.setPartAmount(updateReturnNum);
			}
			if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
				mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
			}
			if(Utility.testString(sendDate)){
				Date sendTime = Utility.parseString2Date(sendDate, "yyyy-MM-dd");
				mainPo.setSendTime(sendTime);
			}
				
			mainPo.setUpdateBy(loginUser.getUserId());
			mainPo.setUpdateDate(new Date());
			mainPo.setTel(tel);
			ret=dao.updateClaimBackOrdMainInfo(return_no, mainPo);
			if(ret==1){
				act.setOutData("updateResult","updateSuccess");
				act.setOutData("sumNum",Integer.parseInt(boxTotalNum));
				
			}else{
				act.setOutData("updateResult","updateFailure");
			}
			//判断修改表是否成功
			
			//根据回运主键查询索赔配件清单表信息
			sqlStr.delete(0,sqlStr.length());
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tccc.code_desc return_desc,tawor.status,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_type,tcc.code_desc freight_type,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc,tc_user tu\n" );
			sqlStr.append("where tawor.status=tc.code_id and tawor.transport_type=tcc.code_id\n" );
			sqlStr.append("and tawor.create_by=tu.user_id and tawor.return_type=tccc.code_id\n" );
			sqlStr.append("and tawor.id="+backId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.part_id,");
			sqlStr.append("tawp.part_name,taword.n_return_amount,taword.return_amount,");
			sqlStr.append("taword.box_no,taword.warehouse_region,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark ");
			sqlStr.append("from tt_as_wr_old_returned_detail taword,");
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc ");
			sqlStr.append("where taword.part_id=tawp.part_id ");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+) ");
			sqlStr.append("and taword.return_id="+backId );
			sqlStr.append(" order by taword.create_date ");
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			act.setOutData("updateResult","updateSuccess");
			act.setForword(modClaimBackDetailUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			act.setOutData("approveResult","approveFailure");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-13
	public void updateReturnListInfo11(){
		//dao=ClaimBackListDao.getInstance();
		String backId=request.getParamValue("i_back_id");//获取回运清单的修改主键
		String return_no=request.getParamValue("i_return_no");//获取回运清单的修改主键
		String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
		String price = request.getParamValue("price");//获取申报运费
		String remark = request.getParamValue("remark");
		if(remark==null || "".equalsIgnoreCase(remark)){
			remark=" ";
		}
		@SuppressWarnings("unused")
		List<TtAsWrOldReturnedDetailPO> returnDetaiList=null;
		StringBuffer sqlStr=new StringBuffer();
		int ret=0;
		try{
			//保存索赔回运明细信息表
			sqlStr.append("select * from tt_as_wr_old_returned_detail where return_id="+backId);
			returnDetaiList=dao.getOldReturnedDetailPOByReturnId(sqlStr.toString(), 1,10000);
			//保存索赔回运清单表信息
			TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
			
			if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
				mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
			}
			mainPo.setUpdateBy(loginUser.getUserId());
			mainPo.setUpdateDate(new Date());
			mainPo.setPrice(Float.valueOf(price));
			mainPo.setRemark(remark);
			ret=dao.updateClaimBackOrdMainInfo(return_no, mainPo);
			if(ret==1){
				act.setOutData("updateResult","updateSuccess");
				act.setOutData("sumNum",Integer.parseInt(boxTotalNum));
				
			}else{
				act.setOutData("updateResult","updateFailure");
			}
			//判断修改表是否成功
			
			//根据回运主键查询索赔配件清单表信息
			sqlStr.delete(0,sqlStr.length());
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tccc.code_desc return_desc,tawor.status,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_type,tcc.code_desc freight_type,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator\n" );
			sqlStr.append("from tt_as_wr_old_returned tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc,tc_user tu\n" );
			sqlStr.append("where tawor.status=tc.code_id and tawor.transport_type=tcc.code_id\n" );
			sqlStr.append("and tawor.create_by=tu.user_id and tawor.return_type=tccc.code_id\n" );
			sqlStr.append("and tawor.id="+backId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.part_id,");
			sqlStr.append("tawp.part_name,taword.n_return_amount,taword.return_amount,");
			sqlStr.append("taword.box_no,taword.warehouse_region,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark ");
			sqlStr.append("from tt_as_wr_old_returned_detail taword,");
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc ");
			sqlStr.append("where taword.part_id=tawp.part_id ");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+) ");
			sqlStr.append("and taword.return_id="+backId );
			sqlStr.append(" order by taword.create_date ");
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			act.setOutData("updateResult","updateSuccess");
			act.setForword(modClaimBackDetailUrl);
			//this.returnOrder();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			act.setOutData("approveResult","approveFailure");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void checkTransport(){
		String transport_no=request.getParamValue("TRANSPORT_NO");
		TtAsWrOldReturnedPO po=new TtAsWrOldReturnedPO();
		po.setTransportNo(transport_no);
		List<TtAsWrOldReturnedPO> list=dao.select(po);
		if(list.size()>0){
			po=list.get(0);
			Float price=po.getPrice();
			if(price>0){
				act.setOutData("result", false);
			}else{
				act.setOutData("result", true);
			}
		}else{
			act.setOutData("result", true);
		}
		
		
	}
	
	
	
	
	
	//zhumingwei 2011-04-14
	public void updateReturnListInfo22(){
		//dao=ClaimBackListDao.getInstance();
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//物流单ID
		String sendTime = CommonUtils.checkNull(request.getParamValue("sendTime"));//发运日期
		String tel = CommonUtils.checkNull(request.getParamValue("tel"));//三包员电话
		String price = CommonUtils.checkNull(request.getParamValue("price"));//运费
		String arriveDate = CommonUtils.checkNull(request.getParamValue("arriveDate"));//预计到货时间
		String transportType = CommonUtils.checkNull(request.getParamValue("transportType"));//货运方式
		String transportNo = CommonUtils.checkNull(request.getParamValue("transportNo"));//发运单号
		String transportCompany = CommonUtils.checkNull(request.getParamValue("transportCompany"));//快递公司
		String transportRemark = CommonUtils.checkNull(request.getParamValue("transportRemark"));//快递备注
		
		StringBuffer sqlStr=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			
			//保存物流单表信息
			TtAsWrReturnedOrderPO po=new TtAsWrReturnedOrderPO();
			po.setId(Long.parseLong(claimId));
			TtAsWrReturnedOrderPO mainPo=new TtAsWrReturnedOrderPO();
			mainPo.setSendTime(sendTime.equals("")?null:sdf.parse(sendTime));
			mainPo.setTel(tel);
			mainPo.setPrice(price.equals("")?null:Float.parseFloat(price));
			mainPo.setArriveDate(arriveDate.equals("")?null:sdf.parse(arriveDate));
			mainPo.setTransportType(transportType.equals("")?null:Integer.parseInt(transportType));
			mainPo.setTransportNo(transportNo);
			mainPo.setTransportCompany(transportCompany);
			mainPo.setTransportRemark(transportRemark);
			mainPo.setUpdateBy(loginUser.getUserId());
			mainPo.setUpdateDate(new Date());
			int ret = dao.update(po, mainPo);
			if(ret>0){
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
			//附件功能：
			//String[] fjids = request.getParamValues("fjid");
			//FileUploadManager.fileUploadByBusiness(po.getId().toString(), fjids, loginUser);
			String ywzj=po.getId().toString();
			String[] fjids = request.getParamValues("fjid");//获取文件ID
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			if(ywzj!=null&&!ywzj.equals("")){//修改的时候
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			}else{
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			}
			act.setOutData("msg", "补录成功!");
			act.setForword(this.RETURN_MANAGE_INDEX);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件回运清单维护--删除
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	@SuppressWarnings("unchecked")
	public void delBackClaimInfo(){
		//dao=ClaimBackListDao.getInstance();
		StringBuffer sqlStr=new StringBuffer();
		//获得删除参数
		String backId=request.getParamValue("delId");//获取回运清单的修改主键
		try{
			
			sqlStr.append("select * from TT_AS_WR_OLD_RETURNED_DETAIL");
			sqlStr.append(" where return_id="+backId);
			List<TtAsWrOldReturnedDetailPO> list=dao.getOldReturnedDetailPOByReturnId(sqlStr.toString(),1,200);
			if(list!=null&&list.size()>0){
				for(int count=0;count<list.size();count++){
					//还原配件数到索赔申请配件表中
					TtAsWrOldReturnedDetailPO tt=(TtAsWrOldReturnedDetailPO)list.get(count);
					TtAsWrPartsitemPO dataObj=dao.getClaimPartItemPo(String.valueOf(tt.getPartId()));//从索赔申请配件表获得已回运数量
					TtAsWrPartsitemPO updateObj=new TtAsWrPartsitemPO();
					updateObj.setReturnNum(dataObj.getReturnNum()-tt.getReturnAmount());//将回运明细表的配件数量还原到索赔申请配件表中
					dao.updateClaimApplyPartInfo(String.valueOf(tt.getPartId()),updateObj);//修改数据
					
					//删除索赔回运明细表
					TtAsWrOldReturnedDetailPO delDetailObj=new TtAsWrOldReturnedDetailPO();
					delDetailObj.setId(tt.getId());
					dao.delete(delDetailObj);
				}
				//删除索赔回运清单表
				TtAsWrOldReturnedPO delObj=new TtAsWrOldReturnedPO();
				delObj.setId(Long.parseLong(backId));
				dao.delete(delObj);
			}
			act.setForword(queryBackListOrdUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public String trimStr(String _str){
		if(_str==null||"".equals(_str)){
			return "";
		}else{
			return _str.trim();
		}
	}
	
	public void exportDetail() {
		try {
			act = ActionContext.getContext();
			
			String returnId = request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//dao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> detail = dao.getExportDetail(Long.parseLong(returnId), 
					Long.parseLong(loginUser.getDealerId()));
			act.setOutData("pf", detail);
			act.setOutData("ORDER_ID", returnId);
			act.setForword(exportClaimBackDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toExcel() {
		act = ActionContext.getContext();
		loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		OutputStream os = null;
		try {
			String fileName = "旧件回收标签列表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> titleList = genHead();
			list.add(titleList);
			
			String returnId = request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			//dao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> details = dao.getExportDetail(Long.parseLong(returnId), 
					Long.parseLong(loginUser.getDealerId()));
			
			for (Map<String, Object> detail : details) {
				list.add(genBody(detail));
			}
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车导出功能 ");
			logger.error(loginUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	* @Title: genHead 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private List<Object> genHead() {
		List<Object> titleList = new LinkedList<Object>();
		//titleList.add("序号");
		titleList.add("三包单号");
		titleList.add("VIN码");
		titleList.add("生产厂家");
		titleList.add("车型");
		titleList.add("发动机号");
		titleList.add("行驶里程");
		titleList.add("购车日期");
		titleList.add("服务商代码");
		titleList.add("零件名称");
		titleList.add("零件编码");
		titleList.add("配套厂家");
		titleList.add("故障");
		titleList.add("客户姓名");
		titleList.add("电话");
		return titleList;
	}
	
	private List<Object> genBody(Map<String, Object> detail) {
		List<Object> dataList = new LinkedList<Object>();
		dataList.add(CommonUtils.checkNull(detail.get("CLAIM_NO")));        //三包单号
		dataList.add(CommonUtils.checkNull(detail.get("VIN")));             //VIN码
		dataList.add(CommonUtils.checkNull(detail.get("YIELDLY")));         //生产厂家
		dataList.add(CommonUtils.checkNull(detail.get("MODEL_CODE")));	    //车型
		dataList.add(CommonUtils.checkNull(detail.get("ENGINE_NO")));		//发动机号
		dataList.add(CommonUtils.checkNull(detail.get("IN_MILEAGE")));		//行驶里程
		dataList.add(CommonUtils.checkNull(detail.get("PURCHASED_DATE")));	//购车日期
		dataList.add(CommonUtils.checkNull(detail.get("DEALER_CODE")));		//服务商代码
		dataList.add(CommonUtils.checkNull(detail.get("PART_NAME")));		//零件名称
		dataList.add(CommonUtils.checkNull(detail.get("PART_CODE")));		//零件编码
		dataList.add(CommonUtils.checkNull(detail.get("DC_NAME")));			//配套厂家
		dataList.add(CommonUtils.checkNull(detail.get("REMARK")));          //故障
		dataList.add(CommonUtils.checkNull(detail.get("DELIVERER")));       //客户姓名
		dataList.add(CommonUtils.checkNull(detail.get("DELIVERER_PHONE"))); //电话
		return dataList;
	}
	
	/**
	 * 旧件回运清单管理
	 */
	public void queryReturnOrder(){
		try {
			
			
			//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<TmDealerPO> dealerList = dao.queryDealersByParentDealerId(loginUser.getDealerId());
			
			/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
			TmDealerPO po = new TmDealerPO();
			po.setDealerId(Long.parseLong(loginUser.getDealerId()));
			//ClaimBackListDao dao = ClaimBackListDao.getInstance();
			List<TmDealerPO> listPO = dao.select(po);
			if(listPO!=null && listPO.size()>0){
				TmDealerPO valuePO = listPO.get(0);
				String dealerLevel = valuePO.getDealerLevel().toString();
				act.setOutData("dealerLevel", dealerLevel);
			}
			/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
			act.setOutData("dealerList", dealerList);
			act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-12 变更旧件流程
	public void returnOrder(){
		try {
			
			
			//dao=ClaimBackListDao.getInstance();
			String dayType ="";
			 dayType = dao.isFourDay();
			 TmDealerPO d = new TmDealerPO();
			 d.setDealerId(Long.valueOf(loginUser.getDealerId()));
			 d = (TmDealerPO) dao.select(d).get(0);
			//System.out.println(dayType);
			act.setOutData("canAdd", dayType);
			act.setOutData("dealerLevel", d.getDealerLevel());
			act.setForword(this.RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔件回运清单维护--条件查询（新）
	 * 新增：2010-10-10
	 */
	@SuppressWarnings("unchecked")
	public void queryReturnOrderByCondition(){
		try {
			
			
			
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			//dao=ClaimBackListDao.getInstance();
			//查询条件
			String back_order_no = request.getParamValue("back_order_no");//回运清单号
			String create_start_date = request.getParamValue("create_start_date");//建单开始时间
			String create_end_date = request.getParamValue("create_end_date");//建单结束时间
			String report_start_date = request.getParamValue("report_start_date");// 提报起始日期
			String report_end_date = request.getParamValue("report_end_date");// 提报结束日期
			String ord_status=request.getParamValue("ord_status");//处理状态
			String dealeId = request.getParamValue("dealeId");//选择经销商
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer whereStr = new StringBuffer();
			StringBuffer orderByStr = new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,tawor.create_date,tawor.wr_start_date,tawor.return_date,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,\n");
			sqlStr.append("tc.code_desc status_desc,tawor.status,tawor.top_dealer_id dealer_id,d.dealer_name,\n");
			sqlStr.append("tawor.yieldly,tawor.dealer_level,"+loginUser.getDealerId()+" self_dealer_id\n");
			sqlStr.append("from TT_AS_WR_RETURNED_ORDER tawor,tm_dealer d,tc_code tc\n");
			sqlStr.append("where tawor.status=tc.code_id\n");
			sqlStr.append("and tawor.dealer_id = d.dealer_id");
			if(back_order_no!=null&&!"".equals(back_order_no))
				whereStr.append(" and tawor.return_no like '%"+back_order_no.toUpperCase()+"%'\n");
			if(dealeId!=null&&!"".equals(dealeId))
				whereStr.append(" and tawor.DEALER_ID="+dealeId+"\n");
			if (create_start_date != null && !"".equals(create_start_date))
				whereStr.append(" and tawor.create_date>=to_date('" + create_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (create_end_date != null && !"".equals(create_end_date))
				whereStr.append(" and tawor.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_start_date != null && !"".equals(report_start_date))
				whereStr.append(" and tawor.return_date>=to_date('" + report_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_end_date != null && !"".equals(report_end_date))
				whereStr.append(" and tawor.return_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if(ord_status!=null&&!"".equals(ord_status))
				whereStr.append(" and tawor.status=" + ord_status+"\n");
			
			Integer dealerLevel = Constant.DEALER_LEVEL_02;
			//21、查询对应经销商信息
			TmDealerPO conditionPO = new TmDealerPO();
			conditionPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
			List<TmDealerPO> dealerList = dao.select(conditionPO);
			if(dealerList!=null && dealerList.size()>0){
				TmDealerPO tempPO = dealerList.get(0);
				if(tempPO.getDealerLevel()!=null)
					dealerLevel = tempPO.getDealerLevel();
				    /************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
					act.setOutData("dealerLevel", dealerLevel);
					/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
			}
			if(Constant.DEALER_LEVEL_01.equals(dealerLevel))
				whereStr.append(" and tawor.TOP_DEALER_ID="+loginUser.getDealerId()+"\n");
			else
				whereStr.append(" and tawor.DEALER_ID="+loginUser.getDealerId()+"\n");
			
			orderByStr.append(" order by tawor.id desc\n");
			System.out.println("sqlsql=="+sqlStr.toString()+ whereStr.toString());
			PageResult<TtAsWrBackListQryBean> ps = dao.queryClaimBackList(sqlStr.toString()+ whereStr.toString() + orderByStr.toString(), curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-13
	public void queryReturnOrderByCondition11(){
		try {
			//dao=ClaimBackListDao.getInstance();
			//查询条件
			String back_order_no = request.getParamValue("back_order_no");//回运清单号
			String create_start_date = request.getParamValue("create_start_date");//建单开始时间
			String create_end_date = request.getParamValue("create_end_date");//建单结束时间
			String report_start_date = request.getParamValue("report_start_date");// 提报起始日期
			String report_end_date = request.getParamValue("report_end_date");// 提报结束日期
			String ord_status=request.getParamValue("ord_status");//处理状态
			String return_type=request.getParamValue("return_type");//处理状态
			String dealeId = request.getParamValue("dealeId");//选择经销商
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer whereStr = new StringBuffer();
			StringBuffer orderByStr = new StringBuffer();
			sqlStr.append("select tawor.borrow_no,tawor.price,tawor.auth_price auth_price,tawor.id as return_id,tawor.return_no as old_no,tawor.arrive_date,tawor.status as old_status,(select count(*) from tt_as_wr_returned_order_detail d where d.return_id=tawor.id and d.box_no is null) as box_no,tawor.id,tawor.return_no,tawor.create_date,to_char(TAWOR.WR_START_DATE,'yyyy-mm-dd')||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') WR_START_DATE,tawor.return_date,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,d.dealer_shortname dealer_shortname,\n");
			sqlStr.append("f_get_tc_code(tawor.status) status_desc,tawor.status,tawor.top_dealer_id dealer_id,\n");
			sqlStr.append("'长沙' yieldly_name, tawor.yieldly,tawor.dealer_level,"+loginUser.getDealerId()+" self_dealer_id,u1.name  auth_person_name ,tawor.return_type,u.name sign_name\n");
			sqlStr.append("from TT_AS_WR_RETURNED_ORDER tawor left join tc_code tcc on tcc.code_id = tawor.yieldly  left join tc_user u on u.user_id = tawor.sign_person  left join tc_user u1 on u1.user_id = tawor.in_warhouse_by\n");
			sqlStr.append("left join tm_dealer d on tawor.dealer_id = d.dealer_id\n");
			sqlStr.append("where  1=1\n");
			if(back_order_no!=null&&!"".equals(back_order_no))
				whereStr.append(" and tawor.return_no like '%"+back_order_no.toUpperCase()+"%'\n");
			if(dealeId!=null&&!"".equals(dealeId))
				whereStr.append(" and tawor.DEALER_ID="+dealeId+"\n");
			if (create_start_date != null && !"".equals(create_start_date))
				whereStr.append(" and tawor.create_date>=to_date('" + create_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (create_end_date != null && !"".equals(create_end_date))
				whereStr.append(" and tawor.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_start_date != null && !"".equals(report_start_date))
				whereStr.append(" and tawor.return_date>=to_date('" + report_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
			if (report_end_date != null && !"".equals(report_end_date))
				whereStr.append(" and tawor.return_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
			if(ord_status!=null&&!"".equals(ord_status))
				whereStr.append(" and tawor.status=" + ord_status+"\n");
			if(return_type!=null&&!"".equals(return_type))
				whereStr.append(" and tawor.return_type=" + return_type+"\n");
			Integer dealerLevel = Constant.DEALER_LEVEL_02;
			//21、查询对应经销商信息
			if(Utility.testString(loginUser.getDealerId())){
				TmDealerPO conditionPO = new TmDealerPO();
				conditionPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
				List<TmDealerPO> dealerList = dao.select(conditionPO);
				if(dealerList!=null && dealerList.size()>0){
					TmDealerPO tempPO = dealerList.get(0);
					if(tempPO.getDealerLevel()!=null)
						dealerLevel = tempPO.getDealerLevel();
					    /************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
						act.setOutData("dealerLevel", dealerLevel);
						/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
				}
				 
				  
				whereStr.append(" and tawor.DEALER_ID in("+loginUser.getDealerId()+","+dealerList.get(0).getParentDealerD()+")\n");
			}
			
			orderByStr.append(" order by tawor.id desc,tawor.return_no desc\n");
			//System.out.println("sqlsql=="+sqlStr.toString()+ whereStr.toString());
			PageResult<TtAsWrBackListQryBean> ps = dao.queryClaimBackList(sqlStr.toString()+ whereStr.toString() + orderByStr.toString(), curPage,
					Constant.PAGE_SIZE);
 			act.setOutData("ps", ps);
			//act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void queryReturnOrderByCondition11lj(){
		try {
			//dao=ClaimBackListDao.getInstance();
			//查询条件
			String back_order_no = request.getParamValue("back_order_no");//回运清单号
			String create_start_date = request.getParamValue("create_start_date");//建单开始时间
			String create_end_date = request.getParamValue("create_end_date");//建单结束时间
			String report_start_date = request.getParamValue("report_start_date");// 提报起始日期
			String report_end_date = request.getParamValue("report_end_date");// 提报结束日期
			String ord_status=request.getParamValue("ord_status");//处理状态
			String return_type=request.getParamValue("return_type");//处理状态
			String dealeId = request.getParamValue("dealeId");//选择经销商
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer whereStr = new StringBuffer();
			StringBuffer orderByStr = new StringBuffer();
			sqlStr.append("select  bor.borrow_person,tawor.borrow_no as borrow_no, bor.borrow_phone,bor.require_date  , bb.price,bb.auth_price auth_price,bb.id as return_id,bb.return_no as old_no,bb.arrive_date,bb.status as old_status,(select count(*) from tt_as_wr_old_returned_detail d where d.return_id=bb.id and d.box_no is null) as box_no,tawor.id,tawor.return_no,tawor.create_date,TAWOR.WR_START_DATE||'至'||to_char(tawor.return_end_date,'yyyy-mm-dd') WR_START_DATE,bb.return_date,\n");
			sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.dealer_shortname,\n");
			sqlStr.append("tc.code_desc status_desc,tawor.status,tawor.top_dealer_id dealer_id,\n");
			sqlStr.append("tcc.code_desc yieldly_name, tawor.yieldly,tawor.dealer_level,"+loginUser.getDealerId()+" self_dealer_id,u1.name  auth_person_name ,tawor.return_type,u.name sign_name\n");
			sqlStr.append("from TT_AS_WR_RETURNED_ORDER tawor  left join TT_AS_PART_BORROW bor  on tawor.return_no = bor.return_no   left join tc_code tcc on tcc.code_id = tawor.yieldly,tr_return_logistics aa,tt_as_wr_old_returned bb  left join tc_user u on u.user_id = bb.sign_person  left join tc_user u1 on u1.user_id = bb.in_warhouse_by left join tt_as_old_return_apply ap on ap.return_no=bb.return_no  ,tm_dealer d,tc_code tc\n");
			sqlStr.append("where tawor.status=tc.code_id and aa.return_id=tawor.id and bb.id=aa.logictis_id\n");
			sqlStr.append("and tawor.dealer_id = d.dealer_id\n");
			sqlStr.append("and  tawor.status='13121001'\n ");
			sqlStr.append("and tawor.return_type='10731002' ");
			sqlStr.append("and bb.status=10811001 ");
			sqlStr.append("and bb.is_delay=1");
//			sqlStr.append(" and ( ap.status=93451004 or ap.status!=93451001 and ap.status!=93451002 and ap.status!=93451003 ) ");
//			if(back_order_no!=null&&!"".equals(back_order_no))
//				whereStr.append(" and bb.return_no like '%"+back_order_no.toUpperCase()+"%'\n");
//			if(dealeId!=null&&!"".equals(dealeId))
//				whereStr.append(" and tawor.DEALER_ID="+dealeId+"\n");
//			if (create_start_date != null && !"".equals(create_start_date))
//				whereStr.append(" and tawor.create_date>=to_date('" + create_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
//			if (create_end_date != null && !"".equals(create_end_date))
//				whereStr.append(" and tawor.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
//			if (report_start_date != null && !"".equals(report_start_date))
//				whereStr.append(" and tawor.return_date>=to_date('" + report_start_date+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
//			if (report_end_date != null && !"".equals(report_end_date))
//				whereStr.append(" and tawor.return_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
//			if(ord_status!=null&&!"".equals(ord_status))
//				whereStr.append(" and bb.status=" + ord_status+"\n");
//			if(return_type!=null&&!"".equals(return_type))
//				whereStr.append(" and bb.return_type=" + return_type+"\n");
			Integer dealerLevel = Constant.DEALER_LEVEL_02;
			//21、查询对应经销商信息
			if(Utility.testString(loginUser.getDealerId())){
				TmDealerPO conditionPO = new TmDealerPO();
				conditionPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
				List<TmDealerPO> dealerList = dao.select(conditionPO);
				if(dealerList!=null && dealerList.size()>0){
					TmDealerPO tempPO = dealerList.get(0);
					if(tempPO.getDealerLevel()!=null)
						dealerLevel = tempPO.getDealerLevel();
					    /************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
						act.setOutData("dealerLevel", dealerLevel);
						/************Iverson add By 2010-11-16 查询的当前登录经销商的code,用来判断一二级经销商**********/
				}
				 
				  
				whereStr.append(" and tawor.DEALER_ID in("+loginUser.getDealerId()+","+dealerList.get(0).getParentDealerD()+")\n");
			}
			
			orderByStr.append(" order by tawor.id desc,bb.return_no desc\n");
			System.out.println("sqlsql=="+sqlStr.toString()+ whereStr.toString());
			PageResult<TtAsWrBackListQryBean> ps = dao.queryClaimBackList(sqlStr.toString()+ whereStr.toString() + orderByStr.toString(), curPage,
					Constant.PAGE_SIZE);
 			act.setOutData("ps", ps);
			//act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔件回运清单维护--新建清单页面(新)
	 * 注意：加入二级经销商后调整
	 */
	public void addReturnOrderPage(){
		try {
			
			
			act.setForword(this.OLDPART_RETURN_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--新建清单页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void addOldReturnOrder(){
		try {
			//dao=ClaimBackListDao.getInstance();
			List<TmBusinessAreaPO> list = dao.select(new TmBusinessAreaPO());
			request.setAttribute("yieldlyList", list);
			String dealer_id = request.getParamValue("dealer_id");
			
			if(Utility.testString(dealer_id))
			{
				act.setOutData("dealer_id",dealer_id);
			}else
			{
				act.setOutData("dealer_id", getCurrDealerId());
			}
			
			act.setForword(this.ADD_OLD_RETURN);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--新建清单页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void addBackOrddealerCode()
	{
		try {
			//dao=ClaimBackListDao.getInstance();
			String dealer_code = request.getParamValue("dealer_code");
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerCode(dealer_code);
			dealerPO.setDealerType(10771002);
			List<TmDealerPO> list= dao.select(dealerPO);
			if(list.size() == 0 )
			{
				act.setOutData("dealer_id", "请输入正确的服务站代码");
			}else
			{
				act.setOutData("dealer_id",""+list.get(0).getDealerId());
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "判断服务站");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void addReturnOrder1(){
		//dao=ClaimBackListDao.getInstance();
		String endDate = request.getParamValue("endDate");//结束时间
		
		String borrowNo = request.getParamValue("borrowNo");//结束时间
		String startDate = request.getParamValue("startDate");//结束时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String backType = DaoFactory.getParam(request, "backType");
		Date time = new Date();
		try {
			if(backType.equals("10731002")){
				Date a = format.parse(format.format(time));
				Date b = format.parse(endDate);
				Calendar cal1 = Calendar.getInstance(); 
				Calendar cal2 = Calendar.getInstance(); 
				cal1.setTime(a); 
				cal2.setTime(b);
				if(cal1.before(cal2)){
					act.setOutData("ok", "ok");
				}
				String dealer_id = request.getParamValue("dealer_id");
				if(!Utility.testString(dealer_id))
				{
					dealer_id = loginUser.getDealerId();
				}
				List<Map<String,Object>> list = dao.getClaimList(startDate,endDate,dealer_id);
				if(list!=null && list.size()>0){
					act.setOutData("ok", "hasMore");
				}
	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();//公用类，加年月日的
				Date nowTime = new Date();
				String nowTime1 = sdf.format(nowTime);
				String yearTime1= nowTime1.substring(0,4);//year
				nowTime1= nowTime1.substring(5,7);
				String nowTime2 = endDate.substring(5,7);
				String yearTime2 = endDate.substring(0,4);
				act.setOutData("nowTime2", nowTime2);
				act.setOutData("nowTime1", nowTime1);
				act.setOutData("yearTime1", yearTime1);
				act.setOutData("yearTime2", yearTime2);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//KFQ  2013-5-3 10:47 //zyw 2015-4-14 修改BUG
	public void addOldReturn(){
		//在这个里面同时要做二步操作
		//1.添加回运单
		//2.添加物流单
		String borrowNo = getParam("bowrrowNo");//紧急调件单号 bowrrowNo
		String dealer_id = getParam("dealer_id");//经销商代码
		
		String success = "SUCCESS";
		Long dealerId = 0l;
		if(Utility.testString(dealer_id)){
			dealerId = Long.parseLong(dealer_id);
		}else{
			 dealerId=getCurrDealerId();
		}
		
		boolean isDeal = DBLockUtil.lock(dealerId.toString(), DBLockUtil.BUSINESS_TYPE_05);
		//ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(isDeal){
			try {
				String startDate ="";
				String endDate ="";
				String[] claim_nos=null;
				//第一步添加回运单
				String yieldly = request.getParamValue("YIELDLY_TYPE");//产地
				String backType = request.getParamValue("backType");//回运类型
				if(backType.equals("10731001")){//紧急调件索赔单号
					claim_nos = request.getParamValues("claim_id");
				}
				if(backType.equals("10731002")){
					 startDate = request.getParamValue("startDate");//起始时间
					 endDate = request.getParamValue("endDate");//结束时间
				}
				String orderNO =Utility.GetClaimBillNo("",loginUser.getDealerCode(),"YD");//生成单号
				String returnID =SequenceManager.getSequence("");
				//ID
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
				//1、先记录回运单明细表
				dao.createReturnOrderDetail(startDate, endDate, yieldly, loginUser.getUserId(),Long.parseLong(returnID),dealerId,claim_nos,backType);
				
				//2、根据记录明细统计生成回运清单信息
				Long topDealerId = -1L;//查询登录用户进来的上级经销商ID(如果本来就是1级,默认为-1)
				Integer dealerLevel = Constant.DEALER_LEVEL_02;//经销商等级
				
				//2.1、查询对应经销商信息
				TmDealerPO conditionPO = new TmDealerPO();
				conditionPO.setDealerId(dealerId);
				List<TmDealerPO> dealerList = dao.select(conditionPO);
				TmDealerPO tempPO = null;
				if(dealerList!=null && dealerList.size()>0){
					tempPO = dealerList.get(0);
					if(tempPO.getDealerLevel()!=null)
						dealerLevel = tempPO.getDealerLevel();
				}
					
				//2.2、查询上级经销商信息
				TmDealerPO parentDealerPO = balanceDao.queryInvoiceMaker(dealerId);
				if(parentDealerPO!=null && parentDealerPO.getDealerId()!=null){
					topDealerId = parentDealerPO.getDealerId();
				}
				
				String sqlStr = "SELECT *  FROM TT_AS_WR_RETURNED_ORDER_DETAIL ROD where rod.return_id ="+returnID+" ";
				List<TtAsWrReturnedOrderDetailPO>  result =  dao.queryOrderDetail(sqlStr,100000, 1);
				
				if(result!=null && result.size()>0){//判断是否有回运单明细
					TtAsWrReturnedOrderPO roPO = new TtAsWrReturnedOrderPO();
					roPO.setId(Long.parseLong(returnID));//回运单ID
					roPO.setOemCompanyId(oemCompanyId);
					roPO.setCreateBy(loginUser.getUserId());
					roPO.setDealerId(dealerId);				//经销商ID
					roPO.setBorrowNo(borrowNo);
			        TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(Long.parseLong(dealer_id));
					ClaimBackListDao backListDao = new ClaimBackListDao();
					List<TmDealerPO> selectLists = backListDao.select(tmDealerPO);
					if(selectLists != null && selectLists.size() == 1) {
						roPO.setDealerName(selectLists.get(0).getDealerName());
						roPO.setDealerShortname(selectLists.get(0).getDealerShortname());
					}
					
					roPO.setDealerLevel(dealerLevel);       //经销商级别
					/****************Iverson Add By 2010-11-16 判断本经销商是否有独立结算*******************/
					if(Constant.BALANCE_LEVEL_SELF.equals(tempPO.getBalanceLevel())){
						roPO.setTopDealerId(dealerId);      
					}else{
						roPO.setTopDealerId(topDealerId);      
					}
					/****************Iverson Add By 2010-11-16 判断本经销商是否有独立结算 end****************/
					roPO.setPartAmount(0);                  //回运配件数
					roPO.setPartItemAmount(0);              //配件项数
					roPO.setWrAmount(0);                    //索赔单数量
					roPO.setReturnNo(orderNO);//回运清单号
					roPO.setStatus(Constant.RETURNORDER_TYPE_01);//回运单状态初始为未上报
					roPO.setReturnType(Integer.parseInt(backType));//回运类型
					if(backType.equals("10731001")){//紧急调件索赔单号
						startDate+=BaseUtils.getSystemDateStr1();
						endDate+=BaseUtils.getSystemDateStr1();
						roPO.setWrStartDate(sdf.parse(startDate));
						if(!"".equals(borrowNo)){
							String sql="update TT_AS_PART_BORROW t set t.is_return=1,t.return_no='"+orderNO+"' where 1=1 and t.id='"+borrowNo+"'";
							dao.update(sql, null);
						}
					}else{
						roPO.setWrStartDate(sdf.parse(startDate));
					}
					roPO.setYieldly(Long.valueOf(yieldly));
					dao.createReturnOrder11(roPO);  // 创建回运单 结束
					
					//====开始更新经销商旧件回运开始日期 zyw 2014-10-8
					if(backType.equals("10731002")){//如果不是紧急调件
						TtAsDealerTypePO po = new TtAsDealerTypePO();
						po.setDealerId(dealerId);
						po.setYieldly(yieldly);
						TtAsDealerTypePO poValue = new TtAsDealerTypePO();
						poValue.setOldDate(sdf.parse(endDate));
						poValue.setUpdateBy(loginUser.getUserId().toString());
						poValue.setUpdateDate(new Date());
						dao.update(po, poValue);
						StringBuffer sql= new StringBuffer();
						sql.append("update tt_as_dealer_type t set t.old_date = t.old_date + 1 where t.dealer_id =" +dealerId );
						dao.update(sql.toString(), null);
					}
					//===================================
					//3、将已经回运旧件的回运数量回写到索赔配件表中
					dao.writeBackReturnCount(result.get(0));
					
					//第二步：添加货运单
					Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
						Long logisticsId = Long.parseLong(SequenceManager.getSequence(""));//物流单Id = 货运单
						String logisticsNo = SequenceManager.getSequence("GO");//物流单号
						//1、保存回运单和物流单关系
						//1.0、检测对应回运清单是否已经回运
						TrReturnLogisticsPO checkPO = new TrReturnLogisticsPO();
						checkPO.setReturnId(Long.parseLong(returnID));
						List<?> checkList = dao.select(checkPO);
						if(checkList.size()<=0){  
							//1.1、保存回运清单关系
							TrReturnLogisticsPO returnLogisticsPO = new TrReturnLogisticsPO();
							returnLogisticsPO.setCreateBy(loginUser.getUserId());
							returnLogisticsPO.setCreateDate(new Date());
							returnLogisticsPO.setReturnId(Long.parseLong(returnID));
							returnLogisticsPO.setLogictisId(logisticsId);
							dao.insert(returnLogisticsPO);
							
							//2、将回运单明细导入到物流单明细表中
							dao.writeDetailToLogisticsDetail11(logisticsId, loginUser.getUserId());
							//3、根据物流单明细生成物流单
							//3.1、检测是否存在清单明细
							TtAsWrOldReturnedDetailPO detialPO = new TtAsWrOldReturnedDetailPO();
							detialPO.setReturnId(logisticsId);
							List<?> detailList = dao.select(detialPO);
							if(detailList!=null && detailList.size()>0){
								//3.2、生成物流清单
								TtAsWrOldReturnedPO orderPO1 = new TtAsWrOldReturnedPO();
								orderPO1.setId(logisticsId);
								orderPO1.setReturnNo(logisticsNo);
								orderPO1.setOemCompanyId(companyId);
								orderPO1.setYieldly(Long.valueOf(yieldly));
								orderPO1.setReturnType(Integer.parseInt(backType));
								orderPO1.setIsStatus(1);//是否显示
								orderPO1.setCreateBy(loginUser.getUserId());
								orderPO1.setStatus(Constant.BACK_LIST_STATUS_01);
								orderPO1.setDealerId(Long.parseLong(dealer_id));
								
								TmDealerPO tmDealerPO2 = new TmDealerPO();
								tmDealerPO2.setDealerId(Long.parseLong(dealer_id));
								List<TmDealerPO> selectLists2 = backListDao.select(tmDealerPO2);
								
								if(selectLists2 != null && selectLists2.size() == 1) {
									orderPO1.setDealerName(selectLists2.get(0).getDealerName());
									orderPO1.setDealerShortname(selectLists2.get(0).getDealerShortname());
								}
								
								dao.createLogisticsOrder1(orderPO1);
							}
						}
					//}
					//DBLockUtil.freeLock(dealerId.toString(), DBLockUtil.BUSINESS_TYPE_06);
					//新增物流单结束
				}else{
					success = "NODATE";
				}
				act.setOutData("returnID", returnID);
			} catch (Exception e) {
				success = "FAILURE";
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--新建清单页面");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}else{//其他人正在做回运单
			success = "DEALED";
		}
		DBLockUtil.freeLock(dealerId.toString(), DBLockUtil.BUSINESS_TYPE_05);
		act.setOutData("SUCCESS", success);
		act.setForword(this.OLDPART_RETURN_ADD_ZW);
	}
	
	/**
	 * 删除对应回运清单信息
	 */
	@SuppressWarnings("unchecked")
	public void deleteReturnOrder(){
		
		
		
		String success = "SUCCESS";
		try{
			String returnId = request.getParamValue("returnId");//回运清单ID
			//ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TtAsWrReturnedOrderDetailPO detailPO = new TtAsWrReturnedOrderDetailPO();
			detailPO.setReturnId(Long.parseLong(returnId));
			TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
			orderPO.setId(Long.parseLong(returnId));
			boolean isChange = false;
			//检测对应回运清单状态是否变化
			List<TtAsWrReturnedOrderPO> orderList = dao.select(orderPO);
			if(orderList!=null && orderList.size()>0){
				TtAsWrReturnedOrderPO orderTempPO = orderList.get(0);
				if(!Constant.RETURNORDER_TYPE_01.equals(orderTempPO.getStatus()))
					isChange = true;
			}
			if(!isChange){//当回运单状态未变化时删除对应索赔单
				
				TtAsWrReturnedOrderPO order=new TtAsWrReturnedOrderPO();
				order.setId(Long.parseLong(returnId));
				order=(TtAsWrReturnedOrderPO)dao.select(order).get(0);
				
				//1、将该回运单对应配件的索赔单回运数量清零
				dao.writeBackReturnCountZero(detailPO);
				//2、删除对应回运清单信息
				int count = dao.delete(orderPO);
				if(count > 0){
					/********Iverson add By 2010-11-22 删除同时回滚起止时间*******************/
					//根据经销商ID查询出最大的一个生回运单的日期
					String time = dao.getMaxDate(loginUser.getDealerId(),order.getYieldly());
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					TtAsDealerTypePO typeCon=new TtAsDealerTypePO();
					typeCon.setDealerId(Long.parseLong(loginUser.getDealerId()));
					typeCon.setYieldly(order.getYieldly().toString());
					TtAsDealerTypePO typeValue=new TtAsDealerTypePO();
					
					typeValue.setOldDate(sdf.parse(time));
					dao.update(typeCon, typeValue);
					/********Iverson add By 2010-11-22 删除同时回滚起止时间*******************/
				}
				//3、删除对应回运清单明细
				dao.delete(detailPO);
			}else{
				success = "FAILURE";
			}
		} catch (Exception e) {
			success = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", success);
		act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
	}
	
	//zhumingwei 2011-04-13
	public void deleteReturnOrder11(){
		
		
		
		String success = "SUCCESS";
		try{
			String returnId = request.getParamValue("returnId");//回运清单ID
			//ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TtAsWrReturnedOrderDetailPO detailPO = new TtAsWrReturnedOrderDetailPO();
			detailPO.setReturnId(Long.parseLong(returnId));
			TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
			orderPO.setId(Long.parseLong(returnId));
			boolean isChange = false;
			//检测对应回运清单状态是否变化
			List<TtAsWrReturnedOrderPO> orderList = dao.select(orderPO);
			TtAsWrReturnedOrderPO temp = orderList.get(0);
			Integer returnType = temp.getReturnType();
			if(orderList!=null && orderList.size()>0){
				TrReturnLogisticsPO po = new TrReturnLogisticsPO();
				po.setReturnId(Long.parseLong(returnId));
				TrReturnLogisticsPO poValue = (TrReturnLogisticsPO)dao.select(po).get(0);
				TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
				popo.setId(poValue.getLogictisId());
				TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
				
				if(!Constant.BACK_LIST_STATUS_01.equals(popoValue.getStatus()))
					isChange = true;
			}
			if(!isChange){//当回运单状态未变化时删除对应索赔单
				TtAsWrReturnedOrderPO order=new TtAsWrReturnedOrderPO();
				order.setId(Long.parseLong(returnId));
				order=(TtAsWrReturnedOrderPO)dao.select(order).get(0);
				String time = dao.getMaxDate(loginUser.getDealerId(),order.getYieldly());
				//1、将该回运单对应配件的索赔单回运数量清零
				dao.writeBackReturnCountZero(detailPO);
				//2、删除对应回运清单信息
				int count = dao.delete(orderPO);
				
				if(count > 0){
					if(returnType==10731002){
						/********Iverson add By 2010-11-22 删除同时回滚起止时间*******************/
						//根据经销商ID查询出最大的一个生回运单的日期
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						TtAsDealerTypePO typeCon=new TtAsDealerTypePO();
						typeCon.setDealerId(Long.parseLong(loginUser.getDealerId()));
						typeCon.setYieldly(order.getYieldly().toString());
						TtAsDealerTypePO typeValue=new TtAsDealerTypePO();
						
						typeValue.setOldDate(sdf.parse(time));
						dao.update(typeCon, typeValue);
						/********Iverson add By 2010-11-22 删除同时回滚起止时间*******************/
					}
					if(returnType==10731001){
						dao.update("update TT_AS_PART_BORROW t set t.is_return=0  where t.return_no='"+order.getReturnNo()+"' and  t.dealer_code='"+loginUser.getDealerCode()+"'", null);
					}
				}
				//3、删除对应回运清单明细
				//claimBackdao.delete(detailPO);
				String sql = " DELETE /*+RULE*/ FROM Tt_As_Wr_Returned_Order_Detail WHERE 1=1 AND Return_Id="+returnId;
				dao.delete(sql.toString(), null);
				
				//第二步删除物流单的相关信息
				//1、删除物流单明细
				TrReturnLogisticsPO po = new TrReturnLogisticsPO();
				po.setReturnId(Long.parseLong(returnId));
				TrReturnLogisticsPO poValue = (TrReturnLogisticsPO)dao.select(po).get(0);
				
				TtAsWrOldReturnedDetailPO detailPO1 = new TtAsWrOldReturnedDetailPO();
				detailPO1.setReturnId(poValue.getLogictisId());
				dao.delete(detailPO1);
				
				//2、删除物流单
				TtAsWrOldReturnedPO orderPO1 = new TtAsWrOldReturnedPO();
				orderPO1.setId(poValue.getLogictisId());
				dao.delete(orderPO1);
				//3、删除物流单通旧件清单关系
				TrReturnLogisticsPO rlPO = new TrReturnLogisticsPO();
				rlPO.setLogictisId(poValue.getLogictisId());
				dao.delete(rlPO);
			}else{
				success = "FAILURE";
			}
		} catch (Exception e) {
			success = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", success);
		act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
	}
	
	
	public void deleteReturnOrder1(){
		//dao = ClaimBackListDao.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			String returnId = request.getParamValue("returnId");//回运清单ID
			//ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
			orderPO.setId(Long.parseLong(returnId));
			TtAsWrReturnedOrderPO poValue = (TtAsWrReturnedOrderPO)dao.select(orderPO).get(0);
			String time =sdf.format(poValue.getReturnEndDate());
			List<TtAsWrReturnedOrderPO> list = dao.getDetailS(time,loginUser.getDealerId(),poValue.getYieldly());
			String msg = "";
			if(list.size()>0){//判断大小(如果删除的单子有比它更大的,那么就返回true,前台提示它必须从大删到小)
				msg = "error";
			}else{
				msg= "ok";
			}
			act.setOutData("msg", msg);
			act.setOutData("returnId", returnId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	//	act.setOutData("SUCCESS", success);
	//	act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
	}
	
	/**
	 * 修改回运清单状态
	 */
	@SuppressWarnings("unchecked")
	public void chanageReturnOrderStatus(){
		
		
		
		
		String success = "SUCCESS";
		
		try{
			String returnId = request.getParamValue("returnId");
			String status = request.getParamValue("status");
			
			TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
			orderPO.setId(Long.parseLong(returnId));
			
			TtAsWrReturnedOrderPO targetPO = new TtAsWrReturnedOrderPO();
			targetPO.setStatus(Integer.parseInt(status));
			targetPO.setUpdateBy(loginUser.getUserId());
			targetPO.setUpdateDate(new Date());
			
			if(Constant.RETURNORDER_TYPE_02.equals(targetPO.getStatus())){//上报更新上报日期
				targetPO.setReturnDate(new Date());
			}else if(Constant.RETURNORDER_TYPE_03.equals(targetPO.getStatus())){//完成更新完成日期
				targetPO.setCompleteDate(new Date());
			}
			
		//	ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			dao.update(orderPO, targetPO);
			
			/*******Iverson add By 根据旧件ID查询旧件结束时间************/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TtAsWrReturnedOrderPO poValue = (TtAsWrReturnedOrderPO)dao.select(orderPO).get(0);
			String oldDate = sdf.format(poValue.getWrStartDate());//旧件结束时间
			oldDate=oldDate.substring(11,oldDate.length());//截取字符串等到结束时间
			
			TtAsDealerTypePO po = new TtAsDealerTypePO();
			po.setDealerId(Long.parseLong(loginUser.getDealerId()));//经销商
			po.setYieldly(poValue.getYieldly().toString());//产地
			TtAsDealerTypePO poTime = new TtAsDealerTypePO();
			
			poTime.setOldDate(sdf.parse(oldDate));//修改值
			dao.update(po, poTime);
			/*******Iverson add By 根据旧件ID查询旧件结束时间************/
		} catch (Exception e) {
			success = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	
		act.setOutData("SUCCESS", success);
		act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
	}
	
	//zhumingwei 2011-04-13
	public void chanageReturnOrderStatus11(){
		
		
		
		String success = "SUCCESS";
		try{
			//第一步修改回运单的状态为待签收
			String returnId = request.getParamValue("returnId");
			String status = request.getParamValue("status");
			
			TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
			orderPO.setId(Long.parseLong(returnId));
			
			TtAsWrReturnedOrderPO targetPO = new TtAsWrReturnedOrderPO();
			targetPO.setStatus(Integer.parseInt(status));
			targetPO.setUpdateBy(loginUser.getUserId());
			targetPO.setUpdateDate(new Date());
			targetPO.setReturnDate(new Date());
			
			//ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			dao.update(orderPO, targetPO);
			
		} catch (Exception e) {
			success = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	
		act.setOutData("SUCCESS", success);
		act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
	}
	
	/**
	 * Function：索赔件回运清单维护--查询明细
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	public void queryReturnOrderDetail(){
		try{
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String closeFlag = request.getParamValue("closeFlag");//控制页面的关闭方式
			
			// 处理当前页
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,tawor.wr_start_date,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,\n" );
			sqlStr.append("tawor.return_type,tawor.status,\n" );
			sqlStr.append("tawor.transport_type,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tawor.tran_no\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor\n" );
			sqlStr.append("where 1=1\n" );
			sqlStr.append("and tawor.id="+claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.barcode_no,taword.id,to_char((select report_date from tt_as_wr_application where claim_no = taword.claim_no),'YYYY-MM-DD') as report_date,taword.claim_no,taword.vin,taword.part_id,taword.part_code,\n");
			sqlStr.append("taword.part_name,taword.n_return_amount,taword.return_amount,\n");
			sqlStr.append("taword.box_no,taword.warehouse_region, ba.area_name proc_factory,");
			sqlStr.append("decode(taword.deduct_remark,null,'',taword.deduct_remark) deduct_remark\n");
			sqlStr.append("from tt_as_wr_returned_order_detail taword,\n");
			sqlStr.append("tt_as_wr_partsitem tawp,tm_business_area   ba \n");
			sqlStr.append("where taword.part_id=tawp.part_id  \n");
			sqlStr.append("and taword.PROC_FACTORY = ba.area_id(+)\n");
			sqlStr.append("and taword.return_id="+claimId+"\n");
			sqlStr.append(" order by tawp.part_name \n");
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			
		    act.setForword(this.OLDPART_RETURN_DETAIL);
		    act.setOutData("closeFlag", CommonUtils.checkNull(closeFlag));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-13
	public void queryReturnOrderDetail11(){
		try{
			
			
			
			//dao=ClaimBackListDao.getInstance();
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String closeFlag = request.getParamValue("closeFlag");//控制页面的关闭方式
			
			// 处理当前页
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,tawor.wr_start_date,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,\n" );
			sqlStr.append("tawor.return_type,tawor.status,\n" );
			sqlStr.append("tawor.transport_type,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tawor.tran_no\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor\n" );
			sqlStr.append("where 1=1\n" );
			sqlStr.append("and tawor.id="+claimId);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.id,taword.claim_no,taword.vin,taword.part_id,taword.part_code,\n");
			sqlStr.append("taword.part_name,taword.n_return_amount,taword.return_amount,\n");
			sqlStr.append("taword.box_no,taword.warehouse_region,tc.code_desc proc_factory,");
			sqlStr.append("decode(taword.deduct_remark,null,'','') deduct_remark\n");
			sqlStr.append("from tt_as_wr_returned_order_detail taword,\n");
			sqlStr.append("tt_as_wr_partsitem tawp,tc_code tc\n");
			sqlStr.append("where taword.part_id=tawp.part_id\n");
			sqlStr.append("and taword.PROC_FACTORY=tc.code_id\n");
			sqlStr.append("and taword.return_id="+claimId+"\n");
			sqlStr.append(" order by tawp.part_name \n");
			List<TtAsWrOldPartDetailListBean> detailList = dao.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			
		    act.setForword(this.OLDPART_RETURN_DETAIL11);
		    act.setOutData("closeFlag", CommonUtils.checkNull(closeFlag));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 根据经销创建的旧件清单生成物流单
	 */
	public void createLogisticsOrderPage(){
		try{
			
			act.setForword(this.LOGISTICS_ORDER_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件回运单-创建初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询需要生成物流单的回运单
	 */
	public void queryReturnOrderForLogistics(){
		try {
			
			
			
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			//dao=ClaimBackListDao.getInstance();
			String ord_status=Constant.RETURNORDER_TYPE_03.toString();//处理状态
			String yieldly = request.getParamValue("yieldly");//产地
			PageResult<TtAsWrBackListQryBean> ps = null;
			if(yieldly!=null && !"".equals(yieldly)){
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer whereStr = new StringBuffer();
				StringBuffer orderByStr = new StringBuffer();
				sqlStr.append("select tawor.id,tawor.return_no,tawor.create_date,tawor.return_date,\n");
				sqlStr.append("tawor.wr_amount,tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,\n");
				sqlStr.append("tc.code_desc status_desc,tawor.status,tawor.top_dealer_id dealer_id,d.dealer_shortname dealer_name,\n");
				sqlStr.append("tawor.yieldly,tawor.dealer_level,"+loginUser.getDealerId()+" self_dealer_id\n");
				sqlStr.append("from TT_AS_WR_RETURNED_ORDER tawor,tm_dealer d,tc_code tc\n");
				sqlStr.append("where tawor.status=tc.code_id\n");
				sqlStr.append("and tawor.dealer_id = d.dealer_id");
	            whereStr.append(" and tawor.status=" + ord_status+"\n");
				whereStr.append(" and tawor.yieldly="+yieldly+"\n");
				whereStr.append(" and tawor.TOP_DEALER_ID="+loginUser.getDealerId()+"\n");
				orderByStr.append(" order by tawor.id desc\n");
				String sql = sqlStr.toString()+ whereStr.toString() + orderByStr.toString();
				
				ps = dao.queryClaimBackList(sql,1, Integer.MAX_VALUE);
			}
			act.setOutData("ps", ps);
			act.setForword(this.OLDPART_RETURN_MANAGE_INDEX);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createLogisticsOrder(){
		String success = "SUCCESS";
		
		
		
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		String dealerId = loginUser.getDealerId();
		boolean isDeal = DBLockUtil.lock(dealerId, DBLockUtil.BUSINESS_TYPE_06);
		if(isDeal) {//同步创建旧件物流单
			try {
				String yieldly = request.getParamValue("yieldly");//产地（发运地点）
				String backOrderNo = request.getParamValue("backOrderNo");//货运单号
				String sendDate = request.getParamValue("sendDate");//发运时间
				String freightType = request.getParamValue("freightType");//货运方式
				String boxNum = request.getParamValue("boxNum");//装箱总数
				String backType = request.getParamValue("backType");//回运类型
				String returnIds[] = request.getParamValues("returnId");//回运单ID集合
				
				if(returnIds!=null && returnIds.length>0 && Utility.testString(yieldly)){
				//	ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
					
					Long logisticsId = Long.parseLong(SequenceManager.getSequence(""));//物流单Id 
					String logisticsNo = SequenceManager.getSequence("GO");//物流单号
					//1、保存回运单和物流单关系
					for(int i=0;i<returnIds.length;i++){
						//10、检测对应回运清单是否已经回运
						TrReturnLogisticsPO checkPO = new TrReturnLogisticsPO();
						checkPO.setReturnId(Long.parseLong(returnIds[i]));
						List<?> checkList = dao.select(checkPO);
						if(checkList!=null && checkList.size()>0)
							continue;
						//11、保存回运清单关系
						TrReturnLogisticsPO returnLogisticsPO = new TrReturnLogisticsPO();
						returnLogisticsPO.setCreateBy(loginUser.getUserId());
						returnLogisticsPO.setCreateDate(new Date());
						returnLogisticsPO.setReturnId(Long.parseLong(returnIds[i]));
						returnLogisticsPO.setLogictisId(logisticsId);
						dao.insert(returnLogisticsPO);
						//12、修改回运清单状态
						TtAsWrReturnedOrderPO orderPO = new TtAsWrReturnedOrderPO();
						orderPO.setUpdateBy(loginUser.getUserId());
						orderPO.setUpdateDate(new Date());
						orderPO.setStatus(Constant.RETURNORDER_TYPE_05);
						TtAsWrReturnedOrderPO conditionPO = new TtAsWrReturnedOrderPO();
						conditionPO.setId(Long.parseLong(returnIds[i]));
						dao.update(conditionPO, orderPO);
					}
					
					//2、将回运单明细导入到物流单明细表中
					dao.writeDetailToLogisticsDetail(logisticsId, loginUser.getUserId());
					//3、根据物流单明细生成物流单
					//31、检测是否存在清单明细
					TtAsWrOldReturnedDetailPO detialPO = new TtAsWrOldReturnedDetailPO();
					detialPO.setReturnId(logisticsId);
					List<?> detailList = dao.select(detialPO);
					if(detailList!=null && detailList.size()>0){
						//32、生成物流清单
					    TtAsWrOldReturnedPO orderPO = new TtAsWrOldReturnedPO();
					    orderPO.setId(logisticsId);
					    orderPO.setReturnNo(logisticsNo);
					    orderPO.setOemCompanyId(companyId);
					    orderPO.setCreateBy(loginUser.getUserId());
					    orderPO.setYieldly(Long.valueOf(yieldly));
					    orderPO.setStatus(Constant.BACK_LIST_STATUS_01);
					    orderPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
					    orderPO.setIsStatus(0);//是否显示
					    if(Utility.testString(backType))
					    	orderPO.setReturnType(Integer.parseInt(backType));
					    if(Utility.testString(boxNum))
					    	orderPO.setParkageAmount(Integer.parseInt(boxNum));
					    if(Utility.testString(backOrderNo))
					    	orderPO.setTranNo(CommonUtils.checkNull(backOrderNo));
					    if(Utility.testString(freightType))
					    	orderPO.setTransportType(Integer.parseInt(freightType));
					    dao.createLogisticsOrder(orderPO, sendDate);
					}
				}else{
					success = "FAILURE";
				}
			} catch (Exception e) {
				success = "FAILURE";
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}else{
			success = "DEALED";
		}
		DBLockUtil.freeLock(dealerId, DBLockUtil.BUSINESS_TYPE_06);
		act.setOutData("SUCCESS", success);
		act.setForword("");
	}
	
	@SuppressWarnings("unchecked")
	public void deleteLogisticOrder(){
		String success = "SUCCESS";
		try {
			
			
			String returnId = request.getParamValue("returnId");
			
			//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			//1、删除物流单明细
			TtAsWrOldReturnedDetailPO detailPO = new TtAsWrOldReturnedDetailPO();
			detailPO.setReturnId(Long.parseLong(returnId));
			dao.delete(detailPO);
			//2、删除物流单
			TtAsWrOldReturnedPO orderPO = new TtAsWrOldReturnedPO();
			orderPO.setId(Long.parseLong(returnId));
			dao.delete(orderPO);
			//3、将旧件清单装箱修改为已完成
			//31、一级经销商 ： 经旧件清单状态修改为"未上报"
			dao.writeBackReturnOrderStatus(Constant.RETURNORDER_TYPE_01, Long.parseLong(returnId),Constant.DEALER_LEVEL_01);
			//32、非一级经销商 ： 经旧件清单状态修改为"已上报"
			dao.writeBackReturnOrderStatus2(Constant.RETURNORDER_TYPE_01, Long.parseLong(returnId),Constant.DEALER_LEVEL_01);
			//4、删除物流单通旧件清单关系
			TrReturnLogisticsPO rlPO = new TrReturnLogisticsPO();
			rlPO.setLogictisId(Long.parseLong(returnId));
			dao.delete(rlPO);
			
		} catch (Exception e) {
			success = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", success);
		act.setForword("");
	}
	
	/**
	 * Iverson add By 2010-11-23 根据Id查询装箱单号(去掉重复和空)
	 */
	public void queryBoxNo(){
		String id = request.getParamValue("id");
		//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		List<Map<String, Object>> list =dao.getBoxNo(Long.parseLong(id));
		act.setOutData("list", list);
		act.setOutData("id", id);
		act.setForword("/jsp/claim/oldPart/printInfo.jsp");
	}
	//审核明细打印
	public void queryBoxNo2(){
		String id = request.getParamValue("id");
		//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		List<Map<String, Object>> list =dao.getBoxNo(Long.parseLong(id));
		act.setOutData("list", list);
		act.setOutData("id", id);
		act.setForword("/jsp/claim/oldPart/printInfo2.jsp");
	}
	/**
	 * 创建旧件回运单的正常工单的（索赔审核通过时间 开始 至 结束 一个月之间）
	 */
	public void showDate(){
		String yieldly = request.getParamValue("yieldly");
		String dealer_id = request.getParamValue("dealer_id");
		Long dealerId = 0l;
		if(Utility.testString(dealer_id))
		{
			 dealerId = Long.parseLong( dealer_id );
		}else{
			 dealerId = getCurrDealerId();
		}
		
		
		//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(dealerId);
		po.setYieldly(yieldly);
		List<TtAsDealerTypePO> list=dao.select(po);
		TtAsDealerTypePO poValue=null;
		
		if(list!=null && list.size()>0){
			poValue = list.get(0);
			//======================算开始时间
			Date oldDate = poValue.getOldDate();//取出时间
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(oldDate);
			oldDate = calendar.getTime();//得到加一天后的值
			//======================算结束时间
			Calendar calendarEnd = Calendar.getInstance();//公用类，加年月日的
			calendarEnd.setTime(new Date());
			String month = Utility.handleDate2(new Date()).split("-")[2];
			month = month.trim();
			int  ri= Integer.valueOf(month);
			if(ri < 26 ){
				calendarEnd.add(Calendar.MONTH, -1);//当前月减1月
			}else{
				//calendarEnd.add(Calendar.MONTH, -2);//当前月减2月
			}
			String  oldDateEnd = Utility.handleDate9(calendarEnd.getTime())+"-25";
			
			//======================返回页面设置
			act.setOutData("oldDateEnd", oldDateEnd);
			act.setOutData("oldDate", oldDate); 
		}else{
			act.setOutData("oldDate", "false");
		}
	}
	public Date lastDayOfMonth(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.roll(Calendar.DAY_OF_MONTH, -1); 
		return cal.getTime(); 
	} 
	
	public void showDate11(){
		String yieldly = request.getParamValue("yieldly");
		//ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(loginUser.getDealerId()));
		po.setYieldly(yieldly);
		TtAsDealerTypePO poValue = (TtAsDealerTypePO)dao.select(po).get(0);
		
		Date oldDate = poValue.getOldDate();//取出时间
		Calendar calendar = Calendar.getInstance();//公用类，加年月日的
		calendar.setTime(oldDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
		oldDate = calendar.getTime();//得到加一天后的值
		
		Date balanceDate = poValue.getBalanceDate();//开票时间
		
		act.setOutData("oldDate", oldDate);
		act.setOutData("balanceDate", balanceDate);
	}
	
	/**
	 * Iverson add By 2010-12-28
	 * Function：
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	@SuppressWarnings("unchecked")
	public void updateReturnDetailInfo(){
		//dao=ClaimBackListDao.getInstance();
		String backId=request.getParamValue("i_back_id");
		String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
		String detailId [] = request.getParamValues("recesel");
//		String price = request.getParamValue("price");//申报运费
		String remark = request.getParamValue("remark");//获取备注
		
		TtAsWrReturnedOrderPO oldPo = new TtAsWrReturnedOrderPO();
		oldPo.setId(Long.parseLong(backId));
		TtAsWrReturnedOrderPO oldPoValue = (TtAsWrReturnedOrderPO)dao.select(oldPo).get(0);
		
		try{
			//保存索赔回运明细信息表
			TtAsWrReturnedOrderDetailPO po = new TtAsWrReturnedOrderDetailPO();
			TtAsWrReturnedOrderDetailPO updateObj=new TtAsWrReturnedOrderDetailPO();
			for(String id:detailId){
				po.setId(Long.parseLong(id));
				TtAsWrReturnedOrderDetailPO poValue=(TtAsWrReturnedOrderDetailPO)dao.select(po).get(0);
				
				if(Constant.BACK_TRANSPORT_TYPE_01.equals(oldPoValue.getReturnType())){
					updateObj.setReturnAmount(Integer.parseInt(request.getParamValue("returnNum"+poValue.getId())));
				}
				String num=request.getParamValue("boxOrd"+poValue.getId());
				if(num!=null){
					updateObj.setBoxNo(request.getParamValue("boxOrd"+poValue.getId()));
					updateObj.setUpdateBy(loginUser.getUserId());
					updateObj.setUpdateDate(new Date());
					dao.update(po,updateObj);
				}
			}
			
		
//			StringBuffer sd= new StringBuffer();
//			sd.append("select a.id from    Tt_As_Wr_Old_Returned_Detail a ,tt_as_wr_application b where\n" );
//			sd.append("a.claim_id = b.id and a.RETURN_ID ="+backId+" and b.claim_type = 10661006 and a.box_no is not null");
			
			
		
			
			//获得装箱总数
			String sql = "SELECT COUNT(1) FROM TT_AS_WR_RETURNED_ORDER_DETAIL D WHERE D.RETURN_ID="+backId+" and d.box_no is not null  GROUP BY D.BOX_NO";
			List<TtAsWrReturnedOrderDetailPO>  list = dao.select(sql, null, TtAsWrReturnedOrderDetailPO.class);
			TtAsWrReturnedOrderPO oldValue = new TtAsWrReturnedOrderPO();
			oldValue.setParkageAmount(list.size());
			oldValue.setUpdateBy(loginUser.getUserId());
			oldValue.setUpdateDate(new Date());
//			oldValue.setPrice(Float.valueOf(price));
			oldValue.setRemark(remark);
			dao.update(oldPo, oldValue);
			
			
			act.setOutData("NUM", list.size());
			act.setOutData("ok", "ok");
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			act.setOutData("approveResult","approveFailure");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//抵扣通知单 服务站初始化跳转
	public void oldPartDeductionQuery() {
		try {
			act = ActionContext.getContext();
//			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);‘
			act.setOutData("dealerId", String.valueOf(loginUser.getDealerId()));
			act.setForword(oldPartDeductionQuery_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "抵扣通知单 服务站初始化跳转");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	//旧件抵扣通知单查询
	public void oldPartDeductionQueryInit() {
		Map<String, String> params = new HashMap<String, String>();
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))
				: 1;
		try {
			params.put("claimId", request.getParamValue("claimId"));
			params.put("dealerId", request.getParamValue("dealerId"));
			params.put("deductionStatus", request.getParamValue("deductionStatus"));// 抵扣单状态
			params.put("deductionNo", request.getParamValue("deductionNo"));// 抵扣单单号
			params.put("updateDateStart", request.getParamValue("updateDateStart"));// 通知开始时间
			params.put("updateDateEnd", request.getParamValue("updateDateEnd"));// 通知结束时间

			PageResult<Map<String, Object>>ps = dao.oldPartDeductionQuery(params, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件抵扣通知单查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	

	// 抵扣通知单查看
	public void oldPartDeductionCheck() {
		try {
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			act.setOutData("claimId", claimId);
			act.setForword(oldPartDeductionDetail_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "抵扣通知单查看");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}
	// 抵扣通知单查询
	public void oldPartDeductionListQuery() {
		try {
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")):1;
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("claimId", claimId);
			PageResult<Map<String, Object>> ps = dao.oldPartDeductionInfor(params,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(oldPartDeductionDetail_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "抵扣通知单查看");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}
	
	//二次抵扣索赔单
	public void claimDeductionSecond() {
		try {
//			act = ActionContext.getContext();
//			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(CLAIM_DEDUCTION_SECOND_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次抵扣索赔单");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	//二次抵扣索赔单查询
	public void claimDeductionSecondQuery() {
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//经销商ID
		String appClaimNo = CommonUtils.checkNull(request.getParamValue("appClaimNo"));//索赔单号
		String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//索赔类型
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
		
		Map<String, String> params = new HashMap<String, String>();
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		try {
			params.put("dealerId", dealerId);
			params.put("appClaimNo", appClaimNo);
			params.put("repairType", repairType);
			params.put("vin", vin);
			PageResult<Map<String, Object>> ps = dao.claimDeductionSecondQuery(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次抵扣索赔单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//二次抵扣展示
	public void claimDeductionSecondShow() {
		try {
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔单ID
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("claimId", claimId);
			Map<String, Object> claimDeductionSecondMap = dao.claimDeductionSecondQuery(params);
			act.setOutData("claimId", claimId);
			act.setOutData("claimDeductionSecondMap", claimDeductionSecondMap);
			act.setForword(CLAIM_DEDUCTION_SECOND_SHOW_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次抵扣索赔单");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	//二次抵扣保存
	public void secondDeductionAmountSave() {
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔类型
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//经销商ID
		String secondDeductionAmount = CommonUtils.checkNull(request.getParamValue("secondDeductionAmount"));//二次抵扣金额
		String secondDeductionRemark = CommonUtils.checkNull(request.getParamValue("secondDeductionRemark"));//二次抵扣备注
		
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		try {
			params.put("claimId", claimId);
			params.put("dealerId", dealerId);
			params.put("secondDeductionAmount", secondDeductionAmount);
			params.put("secondDeductionRemark", secondDeductionRemark);
			params.put("loginUserId", loginUser.getUserId());
			
			dao.secondDeductionAmountSave(params);
			act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次抵扣保存");
			logger.error(loginUser, e1);
			act.setException(e1);
			act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
		}
	}
	
}
 	