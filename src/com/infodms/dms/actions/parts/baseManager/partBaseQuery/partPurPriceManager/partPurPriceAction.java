package com.infodms.dms.actions.parts.baseManager.partBaseQuery.partPurPriceManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partBaseQuery.partPurPriceManager.partPurPriceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : partPurPriceAction 
 * @Description   : 配件销售价格查询
 * @author        : huchao
 * LastDate     : 2013-4-18
 */
public class partPurPriceAction {

	private static final String PART_PURPRICE_QUERY_URL = "/jsp/parts/baseManager/partBaseQuery/partPurPriceManager/partPurPriceQuery.jsp";//财务配件采购价格查询
	public Logger logger = Logger.getLogger(partPurPriceAction.class);
	private partPurPriceDao dao = partPurPriceDao.getInstance();

	/**
	 * 
	 * @Title :
	 * @Description: 配件采购价格查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-18
	 */
	public void partPurPriceQueryInit() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("poseBusType", logonUser.getPoseBusType());
			act.setForword(PART_PURPRICE_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title :
	 * @Description: 分页查询配件采购价格
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-5
	 */
	public void queryPartBuyPriceInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request
					.getParamValue("PART_CODE"));// 件号
			String partOldCode = CommonUtils.checkNull(request
					.getParamValue("PART_OLDCODE"));// 配件编码
			String partName = CommonUtils.checkNull(request
					.getParamValue("PART_CNAME"));// 配件名称
			String venderName = CommonUtils.checkNull(request
					.getParamValue("VENDER_NAME"));// 供应商名称
			String buyerName = CommonUtils.checkNull(request
					.getParamValue("BUYER_NAME"));// 采购员
			String str_state = CommonUtils.checkNull(request
					.getParamValue("STATE"));// 是否有效
			String str_isGuard = CommonUtils.checkNull(request
					.getParamValue("IS_GUARD"));// 是否暂估
			String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地

			int state = 0;
			int isGuard = 0;
			int isChanghe = 0; 

			if (!"".equals(str_state)) {
				state = CommonUtils.parseInteger(str_state);
			}
			if (!"".equals(str_isGuard)) {
				isGuard = CommonUtils.parseInteger(str_isGuard);
			}
			if (!"".equals(str_isChange)) {
                isChanghe = CommonUtils.parseInteger(str_isChange);
            }
			
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryBuyPriceList(
					partCode, partOldCode, partName, venderName, buyerName,
					state, isGuard,isChanghe, curPage, Constant.BILL_PAGE_SIZE,logonUser.getPoseBusType());
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 导出采购价格数据
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-18
	 */
	public void exportPartBuyPriceExcel() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[14];
			head[0] = "配件编码";
			head[1] = "配件名称";
			head[2] = "件号";
			head[3] = "供应商编码";
			head[4] = "供应商名称";
			head[5] = "采购价格";
			head[6] = "是否暂估";
//			head[7] = "采购员";
//			head[8] = "结算基地";
			head[7] = "新增日期";
			head[8] = "修改日期";
			head[9] = "修改人";
			head[10] = "是否有效";
			List<Map<String, Object>> list = dao.queryPartBuyPrice(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[14];
						detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[0] = CommonUtils.checkNull(map
								.get("PART_OLDCODE"));
						detail[1] = CommonUtils
								.checkNull(map.get("PART_CNAME"));
						detail[3] = CommonUtils.checkNull(map
								.get("VENDER_CODE"));
						detail[4] = CommonUtils.checkNull(map
								.get("VENDER_NAME"));
						detail[5] = CommonUtils.checkNull(map.get("BUY_PRICE"));
						detail[6] = CommonUtils.checkNull(map.get("IS_GUARD"));
//						detail[7] = CommonUtils.checkNull(map.get("BUYER_NAME"));
//						detail[8] = CommonUtils.checkNull(map.get("PART_IS_CHANGHE"));
						detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						detail[8] = CommonUtils.checkNull(map.get("UPDATE_DATE"));
						detail[9] = CommonUtils.checkNull(map.get("ACNT"));
						detail[10] = CommonUtils
								.checkNull(map.get("STATE"));
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
		}

	}

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "配件采购价格列表.xls";
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
