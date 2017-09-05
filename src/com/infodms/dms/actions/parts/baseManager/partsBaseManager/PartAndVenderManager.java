package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartAndVenderDao;
import com.infodms.dms.dao.parts.salesManager.carFactorySalesManager.boOrderMananger.EmergentOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartVenderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 配件供应商维护
 * @author fanzhineng
 *
 */
public class PartAndVenderManager extends BaseImport implements PTConstants {
	public Logger logger = Logger.getLogger(PartAndVenderManager.class);
	private PartAndVenderDao dao = PartAndVenderDao.getInstance();
	/*======================================================================*/

	/**
	 * 配件供应商维护查询页面
	 */
	private static final String TO_PART_AND_VENDER_MAIN = "/jsp/parts/baseManager/partsBaseManager/partVender/partAndVenderMain.jsp";

	/**
	 * 配件供应商维护新增页面
	 */
	private static final String TO_PART_AND_VENDER_ADD = "/jsp/parts/baseManager/partsBaseManager/partVender/partAndVenderAdd.jsp";

	/**
	 * 跳转到修改页面
	 */
	private static final String TO_UPDATE_RELATION = "/jsp/parts/baseManager/partsBaseManager/partVender/partAndVenderUpdate.jsp";
	/*======================================================================*/
	/**
	 * 跳转到配件供应商维护主页
	 */
	public void toPartAndVenderMain(){
		ActionContext act = ActionContext.getContext();
		//RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(TO_PART_AND_VENDER_MAIN);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转到配件供应商维护主页异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 下载模板
	 */
	public void templateDownload(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
        OutputStream os = null;
		try {
			List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            //标题
            listHead.add("供应商代码");
            listHead.add("供应商名称");
            listHead.add("配件编码");
            listHead.add("是否有效");
            listHead.add("备注(来源)");
            list.add(listHead);
            //查询供应商
            List<Map<String,Object>> dealerList =  dao.getVenderInfos();
            String[] dealerHead = {"供应商全称", "供应商代码"};
            // 导出的文件名
            String fileName = "配件供应商维护模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            this.createXlsFile(list,dealerHead,dealerList,os,null);
            os.flush();

		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下载模板异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}finally {
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
	 * 创建xls下载文件
	 * @param os 文件流
	 * @param dealerList 供应商结果集
	 * @param dealerHead 供应商标题
	 * @param list 标题
	 * @param dataList
	 *
	 */
	public void createXlsFile(List<List<Object>> list, String[] dealerHead, List<Map<String, Object>> dealerList, OutputStream os, List<Map<String, Object>> dataList){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    try {
		   WritableWorkbook workbook = Workbook.createWorkbook(os);
		   WritableSheet sheet = workbook.createSheet("下载模板", 0);
		   if(dealerHead != null){
			   //供应商模板不为空
			   for(int i = 0; i < list.size(); i++){
				   for (int j = 0; j < list.get(i).size(); j++) {
					   // 添加单元格
                       sheet.addCell(new Label(j, i, (list.get(i).get(j) != null ? list.get(i).get(j).toString() : "")));
				   }
			   }
			   //创建供应商标题
			   WritableSheet sheet1 = workbook.createSheet("供应商信息", 1);
			   for (int i = 0; i < dealerHead.length; i++) {
                   sheet1.addCell(new Label(i, 0, dealerHead[i]));
               }

			   if(dealerHead.length>0){
				   //套用多少行公式
	                for (int i = 0; i < 5; i++) {
	                    insertFormula(sheet, 0, i + 1, "VLOOKUP(B" + (i + 2) + ",'供应商信息'!A:B,2,0)", getDataCellFormat(CellType.STRING_FORMULA));//套用公式
	                }
			   }

			   //创建供应商名称和编码
			   if (dealerList != null && dealerList.size() > 0) {
                   for (int i = 0; i < dealerList.size(); i++) {
                       Map<String, Object> map = dealerList.get(i);
                       sheet1.addCell(new Label(0, i + 1, CommonUtils.checkNull(map.get("VENDER_NAME"))));
                       sheet1.addCell(new Label(1, i + 1, CommonUtils.checkNull(map.get("VENDER_CODE"))));
                   }
               }

			   if(dataList!=null && dataList.size()>0){
				   for (int i = 0; i < dataList.size(); i++) {
					   Map<String, Object> map = dataList.get(i);
					   sheet.addCell(new Label(0, i + 1, CommonUtils.checkNull(map.get("DEALER_CODE"))));
					   sheet.addCell(new Label(1, i + 1, CommonUtils.checkNull(map.get("DEALER_NAME"))));
                       sheet.addCell(new Label(2, i + 1, CommonUtils.checkNull(map.get("PART_OLDCODE"))));
                       if(map.get("STATE")!=null){
                    	   Integer state = Integer.parseInt(map.get("STATE").toString());
                    	   if(state.equals(Constant.IF_TYPE_YES)){
                    		   sheet.addCell(new Label(3, i + 1, "是"));
                    	   }else if(state.equals(Constant.IF_TYPE_NO)){
                    		   sheet.addCell(new Label(3, i + 1, "否"));
                    	   }
                       }else{
                    	   sheet.addCell(new Label(3, i + 1, "否"));
                       }
					   sheet.addCell(new Label(4, i + 1, CommonUtils.checkNull(map.get("REMARK"))));
				   }
			   }

		   }else{
			   for (int i = 0; i < list.size(); i++) {
                   for (int j = 0; j < list.get(i).size(); j++) {
                       // 添加单元格
                       sheet.addCell(new Label(j, i, (list.get(i).get(j) != null ? list.get(i).get(j).toString() : "")));
                   }
               }
		   }

		   workbook.write();
           workbook.close();
		}catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "创建xls下载文件异常");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
	}

	 public void insertFormula(WritableSheet sheet, Integer col, Integer row, String formula, WritableCellFormat format) {
		try {
			Formula f = new Formula(col, row, formula);
			sheet.addCell(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WritableCellFormat getDataCellFormat(CellType type) {
        WritableCellFormat wcf = null;
        try {
            // 字体样式
            if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA) {// 数字

                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.00");

                wcf = new WritableCellFormat(nf);

            } else if (type == CellType.DATE || type == CellType.DATE_FORMULA) {// 日期

                jxl.write.DateFormat df = new jxl.write.DateFormat(

                        "yyyy-MM-dd hh:mm:ss");

                wcf = new jxl.write.WritableCellFormat(df);

            } else {

                WritableFont wf = new WritableFont(WritableFont.TIMES, 10,

                        WritableFont.NO_BOLD, false);// 最后一个为是否italic

                wcf = new WritableCellFormat(wf);

            }

            // 对齐方式

            wcf.setAlignment(Alignment.CENTRE);

            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);

            // 边框

            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);

            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);

            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);

            // 背景色

            wcf.setBackground(Colour.WHITE);

            wcf.setWrap(true);// 自动换行

        } catch (WriteException e) {

            e.printStackTrace();

        }
        return wcf;

    }

	/**
	 * 配件供应商xls文件上传
	 */
	//暂时注释导入功能
	/*@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public void uploadXlsFile(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		CommonDAO commonDAO  = new CommonDAO();
		String err = "";
		StringBuffer errorInfo = new StringBuffer("");
	    try {
	    	long maxSize = 1024 * 1024 * 5;
	    	int errNum = insertIntoTmp(request, "uploadFile", 5, 3, maxSize,1);

	    	if (errNum != 0) {
				switch (errNum) {
				case 1:
					err += "文件列数过多!";
					break;
				case 2:
					err += "空行不能大于三行!";
					break;
				case 3:
					err += "文件不能为空!";
					break;
				case 4:
					err += "文件不能为空!";
					break;
				case 5:
					err += "文件不能大于!";
					break;
				default:
					break;
				}
			}
	    	if("".equals(err)){
	    		//允许最大行
				int maxRow = Integer.parseInt(commonDAO.getPara(60061003+""));
				boolean errNumRow = compartoRowOutOfIndex(request, "uploadFile",maxRow,1);
				if(errNumRow==false){
					err += "sheet表"+1+"，订单行数超过"+maxRow+"行!（包括标题）";
				}
			}

	    	if (!"".equals(err)) {
				BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "");
				throw e1;
			}else{
				List<Map> list = getMapList();
                List voList = new ArrayList();
                loadVoList(voList, list, errorInfo);

                if (errorInfo.length() > 0) {
                	err = errorInfo.toString();
                    BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "");
                    throw e1;
                }

                //循环读取从excel导入的数据集合
                String dealerId = loginUser.getDealerId();
                if("".equals(dealerId)){
                	dealerId = Constant.OEM_ACTIVITIES;
                }

                Date uploadDate = new Date();//上传时间
                Map<String, Object> map4 = null;
                for (int i = 0; i < voList.size(); i++) {
                	TtPartVenderPO pvpo = (TtPartVenderPO)voList.get(i);
                	//检查是否存在
                	map4 = dao.validatePartAndVenderIsCz(pvpo.getVenderId().toString(),pvpo.getPartId().toString());
                	if(map4==null){
                		//不存在的供应商与配件对应关系
                    	pvpo.setSvId(Long.parseLong(SequenceManager.getSequence("")));
                    	pvpo.setCreateUser(loginUser.getUserId());
                    	pvpo.setCreateDate(uploadDate);
                    	pvpo.setStatus(1L);
                    	pvpo.setIsDefult(1);//新增的配件与供应商关系都是默认供应商
                    	dao.insert(pvpo);//插入
                	}else{
                		//存在的供应商与配件对应关系
                		TtPartVenderPO pvpov = new TtPartVenderPO();
                		pvpov.setSvId(Long.valueOf(map4.get("SV_ID").toString()));

                		pvpo.setModifyDate(uploadDate);
                		pvpo.setModifyUser(loginUser.getUserId());
                		pvpo.setStatus(1L);
                		dao.update(pvpov, pvpo);//更新
                	}

                }
			}
	    	act.setOutData("info", "执行成功！");
	    	toPartAndVenderMain();
	    }catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件供应商xls文件上传异常");
            logger.error(loginUser, e1);
            if(err!=""){
            	act.setOutData("info", err);
            }else{
            	act.setOutData("info", "紧急订单授权xls文件上传异常");
            }

            toPartAndVenderMain();
        }
	}
	*/

	/**
	 * 循环获取cell，生成数据存入list
	 * @param request
	 * @param emerList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo)throws Exception {
		if (null == list) {
            list = new ArrayList();
        }

		for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                parseCells(voList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
	}
	/**
     * 装载VO
     *
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :
     * @Title :
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseCells(List list, String rowNum, Cell[] cells, StringBuffer errorInfo) throws Exception {
		if ("" == subCell(cells[0].getContents().trim())) {
			//供应商代码
			errorInfo.append("第" + rowNum + "行，供应商代码为空,请修改后再上传!");
            return;
		}
//		if ("" == subCell(cells[1].getContents().trim())) {
//            errorInfo.append("第" + rowNum + "行，供应商名称为空,请修改后再上传!");
//            return;
//        }
		if ("" == subCell(cells[2].getContents().trim())) {
            errorInfo.append("第" + rowNum + "行，配件编码为空,请修改后再上传!");
            return;
        }
		if ("" == subCell(cells[3].getContents().trim())) {
            errorInfo.append("第" + rowNum + "行，是否有效不能为空,请修改后再上传!");
            return;
        }

		Map<String, Object> map = null;//供应商
		map = dao.getVenderInfos(subCell(cells[0].getContents().trim()));
        if (map == null) {
            errorInfo.append("第" + rowNum + "行的供应商编码不存在,请修改后再上传!");
            return;
        }
        Map<String, Object> map2 = null;//配件
        map2 = EmergentOrderDao.getInstance().validatePartInfo(subCell(cells[2].getContents().trim()));
		if(map2 == null){
			errorInfo.append("第" + rowNum + "行配件编码不存在,或配件已经失效,请修改后再上传!");
            return;
		}
		Long dealerId = Long.valueOf(map.get("VENDER_ID").toString());
		String dealerCode = map.get("VENDER_CODE").toString();
		Long partId = Long.valueOf(map2.get("PART_ID").toString());
		String partOldcode = map2.get("PART_OLDCODE").toString();

		TtPartVenderPO pvpo = new TtPartVenderPO();
		pvpo.setPartId(partId);
		pvpo.setVenderId(dealerId);
		//验证是否存在重复数据
        for (int i = 0; i < list.size(); i++) {
        	TtPartVenderPO pvpox = (TtPartVenderPO) list.get(i);
			Long deId = pvpox.getVenderId();
			Long paId = pvpox.getPartId();
			if(deId.equals(dealerId) && paId.equals(partId)){
				//同一个供应商，同一个配件已经存在
				errorInfo.append("第："+rowNum+"行，供应商："+dealerCode+",配件编码："+partOldcode+"，已经存在！");
			}
		}
        Integer state = Constant.IF_TYPE_YES;
        if("是".equals(subCell(cells[3].getContents().trim())) || "有效".equals(subCell(cells[3].getContents().trim()))){
        	state = Constant.IF_TYPE_YES;
        }
        if("否".equals(subCell(cells[3].getContents().trim())) || "无效".equals(subCell(cells[3].getContents().trim()))){
        	state = Constant.IF_TYPE_NO;
        }
        pvpo.setState(state);
		if ("" != subCell(cells[4].getContents().trim())) {
			pvpo.setRemark(cells[4].getContents());
		}
		list.add(pvpo);//存入list

	}

	 /**
     * 截取字符串
     *
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 200) {
            newAmt = orgAmt.substring(0, 200);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }

	/**
	 * 获取配件供应商维护信息
	 */
	public void getPartAndVenderMain(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 处理当前页
        	Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
        	PageResult<Map<String, Object>> ps = dao.getPartAndVenderMain(request,Constant.PAGE_SIZE,curPage);
        	act.setOutData("ps", ps);
        	act.setOutData("curPage", curPage);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配件供应商维护信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转新增页面
	 */
	public void toPartAndVenderAdd(){
		ActionContext act = ActionContext.getContext();
		//RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(TO_PART_AND_VENDER_ADD);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转新增页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件供应维护保存
	 */
	@SuppressWarnings("unchecked")
	public void saveRelation(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String VENDER_ID = request.getParamValue("VENDER_ID");
			String PART_ID = request.getParamValue("PART_ID");
			String state = request.getParamValue("state");
			TtPartVenderPO pvpo = new TtPartVenderPO();
			pvpo.setVenderId(Long.valueOf(VENDER_ID));
			pvpo.setPartId(Long.valueOf(PART_ID));
			List<TtPartVenderPO> pList = dao.select(pvpo);
			if(pList.size()>0){
				act.setOutData("error", "保存失败，供应商与配件关系已存在！");
			}else if(pList.size()==0){
				TtPartVenderPO po = new TtPartVenderPO();
				po.setPartId(Long.valueOf(PART_ID));
				TtPartVenderPO uppo = new TtPartVenderPO();
				uppo.setIsDefult(0);
				dao.update(po, uppo);
				pvpo = new TtPartVenderPO();
				pvpo.setSvId(Long.valueOf(SequenceManager.getSequence("")));
				pvpo.setVenderId(Long.valueOf(VENDER_ID));
				pvpo.setPartId(Long.valueOf(PART_ID));
				pvpo.setCreateDate(new Date());
				pvpo.setCreateUser(loginUser.getUserId());
				pvpo.setStatus(1L);
				pvpo.setIsDefult(1);
				if(StringUtil.notNull(state)){
					pvpo.setState(Integer.parseInt(state));
				}else{
					pvpo.setState(Constant.IF_TYPE_YES);
				}
				dao.insert(pvpo);


	            TtPartBuyPricePO selBuyPo = new TtPartBuyPricePO();
	            selBuyPo.setPartId(pvpo.getPartId());
	            selBuyPo.setVenderId(pvpo.getVenderId());
	            List<TtPartBuyPricePO> buyPricePOList = dao.select(selBuyPo);
	            if(buyPricePOList.size() == 0){
	                TtPartBuyPricePO ttPartBuyPricePO = new TtPartBuyPricePO();
	                ttPartBuyPricePO.setPriceId(CommonUtils.parseLong(SequenceManager.getSequence("")));
	                ttPartBuyPricePO.setBuyPrice(0d); // 采购价
	                ttPartBuyPricePO.setClaimPrice(0d); // 售后索赔价
	                // ttPartBuyPricePO.setPlanPrice(CommonUtils.parseDouble(str_planPrice));
	                ttPartBuyPricePO.setIsGuard(Constant.IS_GUARD_YES); // 是否暂估
	                ttPartBuyPricePO.setCreateDate(new Date()); // 创建时间
	                ttPartBuyPricePO.setCreateBy(loginUser.getUserId()); // 创建人
	                ttPartBuyPricePO.setState(Constant.STATUS_ENABLE); // 是否有效
	                ttPartBuyPricePO.setStatus(1); // 状态
	                ttPartBuyPricePO.setVenderId(pvpo.getVenderId()); // 供应商id
	                ttPartBuyPricePO.setPartId(pvpo.getPartId()); // 配件id
	                // 插入采购记录
	                dao.insert(ttPartBuyPricePO); // 插入记录
	            }
				
				
				act.setOutData("success", "保存成功！");
			}
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件供应维护保存异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件供应维护状态设置
	 */
	@SuppressWarnings("unchecked")
	public void deleteRelateion(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String svId = request.getParamValue("svId");
			String state = request.getParamValue("state");
			if(StringUtil.isNull(svId)){
				act.setOutData("error", "关系获取异常！");
			}else{
				TtPartVenderPO pvpo = new TtPartVenderPO();
				pvpo.setSvId(Long.valueOf(svId));
				List<TtPartVenderPO> list = dao.select(pvpo);
				TtPartVenderPO pvpox = new TtPartVenderPO();
				pvpox.setState(Integer.parseInt(state));
				pvpox.setStatus(1L);
				pvpox.setModifyDate(new Date());
				pvpox.setModifyUser(loginUser.getUserId());
				dao.update(pvpo, pvpox);
				TtPartBuyPricePO bppo=new TtPartBuyPricePO();
				TtPartBuyPricePO sxpo=new TtPartBuyPricePO();
				bppo.setPartId(list.get(0).getPartId());
				bppo.setVenderId(list.get(0).getVenderId());
				if(state.equals("10041001")){
					sxpo.setState(Constant.STATUS_ENABLE);
				}else{
					sxpo.setState(Constant.STATUS_DISABLE);
				}
				sxpo.setUpdateDate(new Date());
				sxpo.setUpdateBy(loginUser.getUserId());
				dao.update(bppo, sxpo);
				act.setOutData("success", "执行成功！");
			}
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件供应维护状态设置异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获取配件信息
	 */
	public void getPartInfos(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 处理当前页
        	Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
        	PageResult<Map<String, Object>> ps = dao.getPartInfos(request,Constant.PAGE_SIZE,curPage);
        	act.setOutData("ps", ps);
        	act.setOutData("curPage", curPage);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配件信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转到修改页面
	 */
	public void toUpdateRelation(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String svId = request.getParamValue("svId");
			if(StringUtil.isNull(svId)){
				throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取关系异常");
			}else{
				Map<Object,String> ps = dao.getInfoBySvId(svId);
				act.setOutData("ps", ps);
			}
			act.setForword(TO_UPDATE_RELATION);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转到修改页面异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件供应商维护修改
	 */
	@SuppressWarnings("unchecked")
	public void updateRelation(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String svId = request.getParamValue("SV_ID");
			String venderId = request.getParamValue("VENDER_ID");
			String partId = request.getParamValue("PART_ID");
			String oldVenderId = request.getParamValue("OLD_VENDER_ID");
			
			if(!oldVenderId.equals(venderId)){
			    TtPartVenderPO pvpo = new TtPartVenderPO();
			    pvpo.setVenderId(Long.valueOf(venderId));
			    pvpo.setPartId(Long.valueOf(partId));
			    List<TtPartVenderPO> pList = dao.select(pvpo);
			    
			    if(pList.size()>0){
			        act.setOutData("error", "修改失败，供应商与配件关系已存在！");
			        return;
			    }
			}
            
			String state = request.getParamValue("state");
//			String isDefult = CommonUtils.checkNull(request.getParamValue("is_defult"));
			TtPartVenderPO pv = new TtPartVenderPO();
			pv.setSvId(Long.valueOf(svId));
			TtPartVenderPO pvx = new TtPartVenderPO();
			pvx.setVenderId(Long.valueOf(venderId));
			pvx.setModifyDate(new Date());
			pvx.setModifyUser(loginUser.getUserId());
			pvx.setState(Integer.parseInt(state));
//			pvx.setIsDefult(Integer.parseInt(isDefult));
//			pvx.setIsDefult(0);
			dao.update(pv, pvx);
			
			
			TtPartBuyPricePO selOldBpPo = new TtPartBuyPricePO();
			selOldBpPo.setPartId(Long.parseLong(partId));
			selOldBpPo.setVenderId(Long.parseLong(oldVenderId));
			List<TtPartBuyPricePO> oldbpPOlist = dao.select(selOldBpPo);
			
			// 如果配件采购价格表存在配件与供应商的关系就修改，否之新增
			if(oldbpPOlist.size() == 0){
                // 插入采购记录
                TtPartBuyPricePO ttPartBuyPricePO = new TtPartBuyPricePO();
                ttPartBuyPricePO.setPriceId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                ttPartBuyPricePO.setBuyPrice(0d); // 采购价
                ttPartBuyPricePO.setClaimPrice(0d); // 售后索赔价
                ttPartBuyPricePO.setIsGuard(Constant.IS_GUARD_YES); // 是否暂估
                ttPartBuyPricePO.setCreateDate(new Date()); // 创建时间
                ttPartBuyPricePO.setCreateBy(loginUser.getUserId()); // 创建人
                ttPartBuyPricePO.setState(Constant.STATUS_ENABLE); // 是否有效
                ttPartBuyPricePO.setStatus(1); // 状态
                ttPartBuyPricePO.setVenderId(pvx.getVenderId()); // 供应商id
                ttPartBuyPricePO.setPartId(Long.parseLong(partId)); // 配件id
                dao.insert(ttPartBuyPricePO); // 插入记录
			}else{
	            TtPartBuyPricePO upselOldBpPo = new TtPartBuyPricePO();
	            upselOldBpPo.setPriceId(oldbpPOlist.get(0).getPriceId());
	            
	            TtPartBuyPricePO upbpPO = new TtPartBuyPricePO();
	            upbpPO.setVenderId(pvx.getVenderId()); // 供应商id
	            
	            dao.update(selOldBpPo, upbpPO);
			    
			}
			
			
			act.setOutData("success", "修改成功！");
		}catch (Exception e) {// 异常方法
			act.setOutData("error", "修改保存失败！");
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件供应商维护修改异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获取供应商行
	 */
	public void getVenders(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 处理当前页
        	Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
        	PageResult<Map<String, Object>> ps = dao.getVenderInfos(request,Constant.PAGE_SIZE,curPage);
        	act.setOutData("ps", ps);
        	act.setOutData("curPage", curPage);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取供应商行异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 根据条件导出数据
	 */
	public void exportByCondition(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
        OutputStream os = null;
		try {
			//查询要导出的数据
			List<Map<String,Object>> dataList = dao.getExportByCondition(act.getRequest());


			List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            //标题
            listHead.add("供应商代码");
            listHead.add("供应商名称");
            listHead.add("配件编码");
            listHead.add("是否有效");
            listHead.add("备注(来源)");
            list.add(listHead);
            //查询供应商
            List<Map<String,Object>> dealerList =  dao.getVenderInfos();
            String[] dealerHead = {"供应商全称", "供应商代码"};

            // 导出的文件名
            String fileName = "配件供应商维护导出.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            this.createXlsFile(list,dealerHead,dealerList,os,dataList);
            os.flush();

		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下载模板异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
