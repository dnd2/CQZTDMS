package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartBillDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.po.TtPartBuyPriceHistoryPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class PartBillManager implements PTConstants{

	public Logger logger = Logger.getLogger(PartBillManager.class);
	private PartBillDao dao = PartBillDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询初始化,转到查询页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void partBillQueryInit() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_BILL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件服务商开票信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询配件服务商开票信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void queryPartBillInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerName = CommonUtils.checkNull(request
					.getParamValue("DEALER_NAME"));// 采购单位名称
            String dealerCode = CommonUtils.checkNull(request
                    .getParamValue("DEALER_CODE"));//  code
            String invType = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe"));//  开票类型
            
            dealerCode = dealerCode.toUpperCase();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartBillList(dealerName,dealerCode, invType, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件服务商开票信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 新增初始化,跳转到新增页面
	 * @Title      : 
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void addPartBillInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_BILL_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "服务商开票信息添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加发票信息信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void addPartBillInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String str_dealerId = CommonUtils.checkNull(request
					.getParamValue("DEALER_ID"));// 获取采购单位id
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("DEALER_CODE"));// 获取采购单位编码
			String dealerName = CommonUtils.checkNull(request
					.getParamValue("DEALER_NAME"));// 获取采购单位名称
			String taxName = CommonUtils.checkNull(request
					.getParamValue("TAX_NAME"));// 获取开票名称
			String taxNo = CommonUtils.checkNull(request
					.getParamValue("TAX_NO"));// 获取纳税人识别号
			String addr = CommonUtils.checkNull(request
					.getParamValue("ADDR"));// 获取地址
			String mailAddr = CommonUtils.checkNull(request
					.getParamValue("MAIL_ADDR"));// 获取开票邮寄地址
			String tel = CommonUtils.checkNull(request
					.getParamValue("TEL"));// 获取电话
			String bank = CommonUtils.checkNull(request
					.getParamValue("BANK"));// 获取开户行
			String account = CommonUtils.checkNull(request
					.getParamValue("ACCOUNT"));// 获取账号
			String remark = CommonUtils.checkNull(request
					.getParamValue("REMARK"));// 获取备注
//			String invTypeStr = CommonUtils.checkNull(request
//			        .getParamValue("dlrInvTpe").trim()); // 开票类型
//			int invType = Constant.DLR_INVOICE_TYPE_01;
//			if(null != invTypeStr && !"".equals(invTypeStr))
//			{
//				invType = Integer.parseInt(invTypeStr);
//			}
            
			String error = "";
			boolean flag = true;
			
			if(dao.checkDealerId(str_dealerId)){//验证采购单位是否已经存在
				error =error+ "采购单位已经存在,请重新选择!";
				flag = false;
			}
			
			if(!flag){
				act.setOutData("error", error);
				return;
			}
			
			TtPartBillDefinePO ttPartBillDefinePO = new TtPartBillDefinePO();
			ttPartBillDefinePO.setBillId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			ttPartBillDefinePO.setDealerId(CommonUtils.parseLong(str_dealerId));
			ttPartBillDefinePO.setDealerCode(dealerCode);
			ttPartBillDefinePO.setDealerName(dealerName);
			ttPartBillDefinePO.setTaxName(taxName);
			ttPartBillDefinePO.setInvType(Constant.DLR_INVOICE_TYPE_02); // 开票类型-统一为增值税普通发票
			ttPartBillDefinePO.setTaxNo(taxNo);
			ttPartBillDefinePO.setAddr(addr);
			ttPartBillDefinePO.setMailAddr(mailAddr);
			ttPartBillDefinePO.setTel(tel);
			ttPartBillDefinePO.setBank(bank);
			ttPartBillDefinePO.setAccount(account);
			ttPartBillDefinePO.setRemark(remark);
			ttPartBillDefinePO.setCreateDate(new Date());
			ttPartBillDefinePO.setCreateBy(logonUser.getUserId());
			
			dao.insert(ttPartBillDefinePO);
			act.setOutData("success", "添加成功!");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "配件服务商开票信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改初始化,转到修改页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void queryPartBillDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //服务商开票信息Id
			Map<String, Object> billInfo = dao.getPartBillDetail(billId);
			act.setOutData("billInfo", billInfo);
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			act.setOutData("curPage", curPage);
			act.setForword(PARTBILL_INFO_MOD);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务商开票信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void updatePartBillInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String billId = request.getParamValue("BILL_ID");
			
			String str_dealerId = CommonUtils.checkNull(request
					.getParamValue("DEALER_ID"));// 获取采购单位id
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("DEALER_CODE"));// 获取采购单位编码
			String dealerName = CommonUtils.checkNull(request
					.getParamValue("DEALER_NAME"));// 获取采购单位名称
			String taxName = CommonUtils.checkNull(request
					.getParamValue("TAX_NAME"));// 获取开票名称
			String taxNo = CommonUtils.checkNull(request
					.getParamValue("TAX_NO"));// 获取纳税人识别号
			String addr = CommonUtils.checkNull(request
					.getParamValue("ADDR"));// 获取地址
			String mailAddr = CommonUtils.checkNull(request
					.getParamValue("MAIL_ADDR"));// 获取发票邮寄地址
			String tel = CommonUtils.checkNull(request
					.getParamValue("TEL"));// 获取电话
			String bank = CommonUtils.checkNull(request
					.getParamValue("BANK"));// 获取开户行
			String account = CommonUtils.checkNull(request
					.getParamValue("ACCOUNT"));// 获取账号
			String remark = CommonUtils.checkNull(request
					.getParamValue("REMARK"));// 获取备注
//			String invTypeStr = CommonUtils.checkNull(request.getParamValue("dlrInvTpe").trim());
//			int invType = Constant.DLR_INVOICE_TYPE_01;
//			if(null != invTypeStr && !"".equals(invTypeStr))
//			{
//				invType = Integer.parseInt(invTypeStr);
//			}
			
			TtPartBillDefinePO spo = new TtPartBillDefinePO();//源po
			spo.setBillId(CommonUtils.parseLong(billId));
			
			TtPartBillDefinePO ttPartBillDefinePO = new TtPartBillDefinePO();//更新po
			ttPartBillDefinePO.setDealerId(CommonUtils.parseLong(str_dealerId));
			ttPartBillDefinePO.setDealerCode(dealerCode);
			if(dealerName.indexOf(dealerCode)>-1){
				ttPartBillDefinePO.setDealerName(dealerName.substring(0, dealerName.indexOf("(")));
			}else{
			    ttPartBillDefinePO.setDealerName(dealerName);
			}
			ttPartBillDefinePO.setTaxName(taxName);
			ttPartBillDefinePO.setTaxNo(taxNo);
			ttPartBillDefinePO.setInvType(Constant.DLR_INVOICE_TYPE_02);
			ttPartBillDefinePO.setAddr(addr);
			ttPartBillDefinePO.setMailAddr(mailAddr);
			ttPartBillDefinePO.setTel(tel);
			ttPartBillDefinePO.setBank(bank);
			ttPartBillDefinePO.setAccount(account);
			ttPartBillDefinePO.setRemark(remark);
			ttPartBillDefinePO.setUpdateDate(new Date());
			ttPartBillDefinePO.setUpdateBy(logonUser.getUserId());
			
			dao.update(spo, ttPartBillDefinePO);

            act.setOutData("success", "修改成功!");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务商开票信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 失效 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void celPartBill(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //配件服务商开票信息Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			
			TtPartBillDefinePO spo = new TtPartBillDefinePO();//源po
			spo.setBillId(((Long.parseLong(billId))));
			TtPartBillDefinePO po = new TtPartBillDefinePO();//更新po
			po.setState(Constant.STATUS_DISABLE);
			po.setDisableBy(logonUser.getUserId());
			po.setDisableDate(new Date());
			
			dao.update(spo, po);
			act.setOutData("success", "失效成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"配件服务商开票信息失效");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 有效 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void selPartBill(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //配件服务商开票信息Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			
			TtPartBillDefinePO spo = new TtPartBillDefinePO();//源po
			spo.setBillId(((Long.parseLong(billId))));
			TtPartBillDefinePO po = new TtPartBillDefinePO();//更新po
			po.setState(Constant.STATUS_ENABLE);
			
			dao.update(spo, po);
			act.setOutData("curPage", curPage);
			act.setOutData("success", "设置有效成功!");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"让配件服务商开票信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 下载配件服务商开票信息数据 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void exportPartBillExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[10];
			head[0] = "采购单位编码";
			head[1] = "采购单位名称";
			head[2] = "开票名称";
			head[3] = "开票类型";
			head[4] = "纳税人识别号";
			head[5] = "地址";
			head[6] = "发票邮寄地址";
			head[7] = "电话";
			head[8] = "开户行";
			head[9] = "账号";
			List<Map<String, Object>> list = dao.queryPartBill(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[10];
						detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
						detail[1] = CommonUtils.checkNull(map
								.get("DEALER_NAME"));
						detail[2] = CommonUtils
								.checkNull(map.get("TAX_NAME"));
						int inType = Integer.parseInt(CommonUtils.checkNull(map.get("INV_TYPE")));
						int inType1 = Constant.DLR_INVOICE_TYPE_01;
						if(inType1 == inType)
						{
							detail[3] = "增值税专用发票";
						}
						else
						{
							detail[3] = "增值税普通发票";
						}
						detail[4] = CommonUtils.checkNull(map
								.get("TAX_NO"));
						detail[5] = CommonUtils.checkNull(map
								.get("ADDR"));
						detail[6] = CommonUtils.checkNull(map
								.get("MAIL_ADDR"));
						detail[7] = CommonUtils.checkNull(map.get("TEL"));
						detail[8] = CommonUtils.checkNull(map.get("BANK"));
						detail[9] = CommonUtils
								.checkNull(map.get("ACCOUNT"));
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1);
			} else {
				BizException e1 = new BizException(act,
						ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
				throw e1;
			}

		} catch (Exception e) {
			BizException e1 = null;
			if (e instanceof BizException) {
				e1 = (BizException) e;
			} else {
				e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
						"文件下载错误");
			}
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_BILL_QUERY_URL);
		}
	}
	
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "配件服务商开票信息.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
						/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
                        ws.addCell(new Label(i, z, str[i]));
                    }
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}
