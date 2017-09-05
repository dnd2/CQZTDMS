package com.infodms.dms.actions.parts.image;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.FileUpLoadDAO;
//import com.infodms.dms.dao.parts.image.PartImgManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
//import com.infodms.dms.po.TtPartImgErrFeedbackPO;
//import com.infodms.dms.po.TtPartImgPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 备件图片通用类
 * @author fanzhineng
 * @Date 2016-11-22
 * @remark CommonUtilActions.getDealerByLoginUser 获取经销商
 */
public class PartImgManager extends BaseImport implements PTConstants{
	/*public static final Logger logger = Logger.getLogger(PartImgManager.class);
	private static final PartImgManagerDao dao = PartImgManagerDao.getInstance();
	*//*=================================URL START=======================================*//*
	*//**
	 * 跳转到备件图片管理页面
	 *//*
	private static final String TO_PART_IMG_JSP = "/jsp/parts/PartImgManager/toPartImgJsp.jsp";
	*//**
	 * 跳转到备件实物图片查看上传页面
	 *//*
	private static final String TO_PART_IMG_SELECT_JSP = "/jsp/parts/PartImgManager/toPartImgSelectJsp.jsp";
	*//**
	 * 跳转到客服公司图片查看页面
	 *//*
	private static final String TO_PART_IMG_SELECT_BYPDC_JSP = "/jsp/parts/PartImgManager/toPartImgSelectByPDCJsp.jsp";
	*//**
	 * 跳转到备件实物图片审核页面
	 *//*
	private static final String TO_PART_IMG_CHECK_JSP = "/jsp/parts/PartImgManager/toPartImgCheckJsp.jsp";
	*//**
	 * 跳转到备件图片错误反馈处理页面
	 *//*
	private static final String TO_PART_IMG_ERR_BACK_CK_JSP = "/jsp/parts/PartImgManager/toPartImgErrBackCkJsp.jsp";
	*//**
	 * 跳转到图片排序页面
	 *//*
	private static final String TO_PART_IMG_SORT_JSP = "/jsp/parts/PartImgManager/toPartImgSortJsp.jsp";
	*//**
	 * 跳转到根据备件排序图片页面
	 *//*
	private static final String TO_PART_IMG_SORT_BY_PARTID_JSP = "/jsp/parts/PartImgManager/toPartImgSortByPartIdJsp.jsp";
	*//*=================================URL END=========================================*//*
	*//**
	 * 跳转到备件图片管理页面
	 *//*
	public void toPartImgJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PART_IMG_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到备件图片管理页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 查询备件图片信息
	 *//*
	public void getPartImgInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartImgInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询备件图片信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 跳转到备件实物图片查看上传页面
	 *//*
	@SuppressWarnings("unchecked")
	public void toPartImgSelectJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String partId = request.getParamValue("partId");
			act.setOutData("partId", partId);
			
			TtPartImgPO img = new TtPartImgPO();
			img.setPartId(Long.valueOf(partId));
			img.setIsDelete(Constant.IF_TYPE_NO);
			List<TtPartImgPO> imgList = dao.select(img);
			int size = 0;
			if(imgList!=null && !imgList.isEmpty() && imgList.size()>0){
				size = imgList.size();
			}
			act.setOutData("size", size);
			
			act.setForword(TO_PART_IMG_SELECT_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到备件实物图片查看上传页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 根据备件查询备件图片信息(已审核生效的图片，显示给经销商看)
	 *//*
	public void getImgsByPartIdSx(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			List<Map<String,Object>> imgList = dao.getImgsByPartId(request);
			act.setOutData("imgList", imgList);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "根据备件查询备件图片信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 上传备件实物图片
	 *//*
	@SuppressWarnings("unchecked")
	public void uploadPartImg(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String partId = request.getParamValue("partId");
			String remark = request.getParamValue("remark");
			if(StringUtil.isNull(partId)){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "无指定备件，无法上传文件!");
			}
			act.setOutData("partId", partId);
			//验证备件实物条数
			TtPartImgPO img = new TtPartImgPO();
			img.setPartId(Long.valueOf(partId));
			img.setIsDelete(Constant.IF_TYPE_NO);
			List<TtPartImgPO> imgPoList = dao.select(img);
			
			int imgSort = 0;
			if(imgPoList!=null && !imgPoList.isEmpty() && imgPoList.size()>=5){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该备件实物图片已达上限5个，无法继续上传!");
			}
			
			if(imgPoList==null || imgPoList.isEmpty() || imgPoList.size()==0){
				imgSort = 1;
			}else{
				imgSort = imgPoList.size() + 1;
			}
			
			//传入当前登录用户戳，获取经销商信息
			CommonUtilActions cu = new CommonUtilActions();
			TmDealerPO tmd = cu.getDealerByLoginUser(loginUser);
			
			TtPartImgPO imgPo = new TtPartImgPO();
			imgPo.setImgId(Long.valueOf(SequenceManager.getSequence("")));
			imgPo.setPartId(Long.valueOf(partId));
			
			imgPo.setDealerId(tmd.getDealerId());
			imgPo.setDealerCode(tmd.getDealerCode());
			imgPo.setDealerName(tmd.getDealerName());
			
			imgPo.setCreateBy(loginUser.getUserId());
			imgPo.setCreateDate(new Date());
			if(StringUtil.notNull(remark)){
				if(remark.length()>50){
					remark = remark.substring(0, 50);
				}
				imgPo.setRemark(remark);
			}
			imgPo.setImgSort(imgSort);
			dao.insert(imgPo);
			
			if(act.getException() != null){
				throw new Exception(act.getException().getMessage());
			}
			FileObject uploadFile = request.getParamObject("uploadFile");
			if(uploadFile.getLength() > 1024*1000*1){
				throw new Exception("上传文件限制大小为1M，服务器资源有限，请节约使用！");
			}
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
			FileStore store = FileStore.getInstance();
			// 上传到文件服务器并获取文件ID
			String fileid = store.write(uploadFile.getFileName(), uploadFile.getContent());
			// 通过文件ID获取文件URL
			String fileUrl = store.getDomainURL(fileid);
			FsFileuploadPO po = new FsFileuploadPO();
			po.setFileid(fileid);
			po.setYwzj(imgPo.getImgId());//写入业务主键
			po.setFileurl(fileUrl);
			po.setFilename(fileName);
			FileUpLoadDAO daoFile = new FileUpLoadDAO();
			daoFile.addDisableFile(po, loginUser);
			//end
			
			//-----
			List<TtPartImgPO> imgList = dao.select(img);
			int size = 0;
			if(imgList!=null && !imgList.isEmpty() && imgList.size()>0){
				size = imgList.size();
			}
			act.setOutData("size", size);
			//-----
			
			if(po.getFjid().intValue()!=0){
				act.setOutData("uploadImgInfoRs", "上传成功，待客服公司审核后才能生效！");
			}else{
				act.setOutData("uploadImgInfoRs", "上传文件异常，请联系管理员！");
			}
			act.setForword(TO_PART_IMG_SELECT_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "上传备件实物图片异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
			act.setForword(TO_PART_IMG_SELECT_JSP);
		}
	}
	
	*//**
	 * 跳转到客服公司图片查看页面
	 *//*
	public void toPartImgSelectByPDCJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			act.setOutData("imgId", imgId);
			act.setForword(TO_PART_IMG_SELECT_BYPDC_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到客服公司图片查看页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 查看指定备件实物图片
	 *//*
	public void getImgsByImgIdSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			List<Map<String,Object>> imgList = dao.getImgsByImgIdSelect(request);
			act.setOutData("imgList", imgList);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看指定备件实物图片异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 跳转到备件实物图片审核页面
	 *//*
	public void toPartImgCheckJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			act.setOutData("imgId", imgId);
			act.setForword(TO_PART_IMG_CHECK_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到备件实物图片审核页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 审核备件实物图片
	 *//*
	@SuppressWarnings("unchecked")
	public void chekPartImg(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			String opt = request.getParamValue("opt");
			if(StringUtil.notNull(imgId)){
				if(StringUtil.isNull(opt)){
					throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "操作类型异常，无法审核！");
				}
				TtPartImgPO img = new TtPartImgPO();
				img.setImgId(Long.valueOf(imgId));
				
				TtPartImgPO imgs = (TtPartImgPO) dao.select(img).get(0);
				if(imgs.getState().intValue()!=0){
					throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "该图片已审核，请勿重复审核！");
				}
				
				TtPartImgPO imgx = new TtPartImgPO();
				imgx.setCheckBy(loginUser.getUserId());
				imgx.setCheckDate(new Date());
				
				if("2".equals(opt)){
					//通过
					imgx.setState(2);
				}
				if("1".equals(opt)){
					//驳回
					imgx.setState(1);
					imgx.setStatus(Constant.STATUS_DISABLE);//无效待删除
				}
				dao.update(img, imgx);
				act.setOutData("success", "审核完成！");
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "获取图片标志异常，请重试！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核备件实物图片异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 删除备件实物图片
	 *//*
	@SuppressWarnings("unchecked")
	public void deletePartImg(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			if(StringUtil.notNull(imgId)){
				//删除图片关系
				TtPartImgPO img = new TtPartImgPO();
				img.setImgId(Long.valueOf(imgId));
				
				TtPartImgPO imgx = new TtPartImgPO();
				imgx.setIsDelete(Constant.IF_TYPE_YES);//是删除
				imgx.setDeleteDate(new Date());
				imgx.setDeleteBy(loginUser.getUserId());
				imgx.setStatus(Constant.STATUS_DISABLE);
				
				dao.update(img, imgx);
				
				//删除图片
				FsFileuploadPO fs = new FsFileuploadPO();
				fs.setYwzj(Long.valueOf(imgId));
				List<FsFileuploadPO> fsfList = dao.select(fs);
				if(fsfList!=null && !fsfList.isEmpty() && fsfList.size()>0){
					for (FsFileuploadPO fTemp : fsfList) {
						FileStore store = FileStore.getInstance();
						store.delete(fTemp.getFileid());//删除服务器上的图片文件
						dao.delete(fs);//删除图片附件关联数据
					}
				}
				
				//删除图片时，处理所有未处理的反馈
				TtPartImgErrFeedbackPO err = new TtPartImgErrFeedbackPO();
				err.setImgId(Long.valueOf(imgId));
				err.setState(Constant.IF_TYPE_NO);
				
				TtPartImgErrFeedbackPO errx = new TtPartImgErrFeedbackPO();
				errx.setState(Constant.IF_TYPE_YES);
				errx.setCheckBy(loginUser.getUserId());
				errx.setCheckDate(new Date());
				errx.setCheckRemark("删除此图片（批量处理）！");
				
				dao.update(err, errx);
				
				act.setOutData("success", "删除成功！");
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "获取图片标志异常，请重试！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "删除备件实物图片异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 设置图片状态
	 *//*
	@SuppressWarnings("unchecked")
	public void setPartImgStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			String opt = request.getParamValue("opt");
			if(StringUtil.notNull(imgId)){
				if(StringUtil.isNull(opt)){
					throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "操作类型异常，无法操作！");
				}
				TtPartImgPO img = new TtPartImgPO();
				img.setImgId(Long.valueOf(imgId));
				
				TtPartImgPO imgx = new TtPartImgPO();
				imgx.setUpdateDate(new Date());
				imgx.setUpdateBy(loginUser.getUserId());
				if("2".equals(opt)){
					//失效
					imgx.setStatus(Constant.STATUS_DISABLE);
					
					//失效图片时，处理所有未处理的反馈
					TtPartImgErrFeedbackPO err = new TtPartImgErrFeedbackPO();
					err.setImgId(Long.valueOf(imgId));
					err.setState(Constant.IF_TYPE_NO);
					
					TtPartImgErrFeedbackPO errx = new TtPartImgErrFeedbackPO();
					errx.setState(Constant.IF_TYPE_YES);
					errx.setCheckBy(loginUser.getUserId());
					errx.setCheckDate(new Date());
					errx.setCheckRemark("失效此图片（批量处理）！");
					
					dao.update(err, errx);
				}
				if("1".equals(opt)){
					//有效
					imgx.setStatus(Constant.STATUS_ENABLE);
				}
				dao.update(img, imgx);
				
				act.setOutData("success", "操作成功！");
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "获取图片标志异常，请重试！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "设置图片状态异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 反馈备件实物图片错误
	 *//*
	@SuppressWarnings("unchecked")
	public void backPartImgErr(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			if(StringUtil.notNull(imgId)){
				
				CommonUtilActions cu = new CommonUtilActions();
				TmDealerPO tmd = cu.getDealerByLoginUser(loginUser);
				
				String remark = request.getParamValue("remark"+imgId);
				
				TtPartImgErrFeedbackPO back = new TtPartImgErrFeedbackPO();
				back.setImgId(Long.valueOf(imgId));
				back.setDealerId(tmd.getDealerId());
				
				List<TtPartImgErrFeedbackPO> backsList = dao.select(back);
				
				if(backsList!=null && !backsList.isEmpty() && backsList.size()>0){
					act.setOutData("success", "反馈成功！");
				}else{
					TtPartImgErrFeedbackPO err = new TtPartImgErrFeedbackPO();
					err.setBackId(Long.valueOf(SequenceManager.getSequence("")));
					err.setImgId(Long.valueOf(imgId));
					
					err.setDealerId(tmd.getDealerId());
					err.setDealerCode(tmd.getDealerCode());
					err.setDealerName(tmd.getDealerName());
					
					err.setCreateDate(new Date());
					err.setCreateBy(loginUser.getUserId());
					if(StringUtil.notNull(remark)){
						if(remark.length()>100){
							err.setRemark(remark.substring(0, 99));
						}else{
							err.setRemark(remark);
						}
					}
					dao.insert(err);
					
					act.setOutData("success", "反馈成功！");
				}
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "获取图片标志异常，请重试！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "反馈备件实物图片错误异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 跳转到备件图片错误反馈处理页面
	 *//*
	public void toPartImgErrBackCkJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			act.setOutData("imgId", imgId);
			act.setForword(TO_PART_IMG_ERR_BACK_CK_JSP);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到备件图片错误反馈处理页面异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 查询备件图片错误反馈信息
	 *//*
	public void getPartImgErrBackCkInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartImgErrBackCkInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询备件图片错误反馈信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 单条反馈忽略
	 *//*
	@SuppressWarnings("unchecked")
	public void partImgNoBack(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String backId = request.getParamValue("backId");
			if(StringUtil.notNull(backId)){
				TtPartImgErrFeedbackPO err = new TtPartImgErrFeedbackPO();
				err.setBackId(Long.valueOf(backId));
				
				TtPartImgErrFeedbackPO errx = new TtPartImgErrFeedbackPO();
				errx.setState(Constant.IF_TYPE_YES);
				errx.setCheckBy(loginUser.getUserId());
				errx.setCheckDate(new Date());
				errx.setCheckRemark("忽略此反馈！");
				
				dao.update(err, errx);
				
				act.setOutData("success", "操作成功！");
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "未获取单条反馈数据！无法忽略，请重试！");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "单条反馈忽略异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 忽略所有未审核的反馈信息
	 *//*
	@SuppressWarnings("unchecked")
	public void partImgAllNoBack(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String imgId = request.getParamValue("imgId");
			if(StringUtil.notNull(imgId)){
				TtPartImgErrFeedbackPO err = new TtPartImgErrFeedbackPO();
				err.setImgId(Long.valueOf(imgId));
				err.setState(Constant.IF_TYPE_NO);
				
				TtPartImgErrFeedbackPO errx = new TtPartImgErrFeedbackPO();
				errx.setState(Constant.IF_TYPE_YES);
				errx.setCheckBy(loginUser.getUserId());
				errx.setCheckDate(new Date());
				errx.setCheckRemark("忽略此反馈（批量处理）！");
				
				dao.update(err, errx);
				
				act.setOutData("success", "操作成功！");
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "获取反馈信息异常！无法忽略，请重试！");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "忽略所有未审核的反馈信息异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 跳转到图片排序页面
	 *//*
	public void toPartImgSortJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PART_IMG_SORT_JSP);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到图片排序页面异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
			this.toPartImgJsp();
		}
	}
	
	*//**
	 * 查询需要图片排序的备件信息
	 *//*
	public void getPartImgSortInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartImgSortInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询需要图片排序的备件信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	*//**
	 * 跳转到根据备件排序图片页面
	 *//*
	public void toPartImgSortByPartIdJsp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			List<Map<String,Object>> imgList = dao.getImgsByPartIdSort(request);
			act.setOutData("imgList", imgList);
			act.setForword(TO_PART_IMG_SORT_BY_PARTID_JSP);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到根据备件排序图片页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
			toPartImgSortJsp();
		}
	}
	
	
	*//**
	 * 保存备件实物排序
	 *//*
	@SuppressWarnings("unchecked")
	public void savePartImgSort(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] imgIds = request.getParamValues("IMG_ID");
			if(imgIds==null || imgIds.length==0){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "获取保存数据异常");
			}
			for (String imgId : imgIds) {
				String imgSort = request.getParamValue("IMG_SORT"+imgId);
				if(StringUtil.isNull(imgSort)){
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "获取备件顺序号异常");
				}
				TtPartImgPO img = new TtPartImgPO();
				img.setImgId(Long.valueOf(imgId));
				
				TtPartImgPO imgx = new TtPartImgPO();
				imgx.setSortBy(loginUser.getUserId());
				imgx.setSortDate(new Date());
				imgx.setImgSort(Integer.parseInt(imgSort));
				dao.update(img, imgx);
				act.setOutData("success", "保存成功！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存备件实物排序异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}*/
	
}
