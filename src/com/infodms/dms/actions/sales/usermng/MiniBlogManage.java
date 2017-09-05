package com.infodms.dms.actions.sales.usermng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.usermng.MiniBlogMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsBlogDetailPO;
import com.infodms.dms.po.TtVsBlogLogPO;
import com.infodms.dms.po.TtVsBlogPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MiniBlogManage {
	private static Logger logger = Logger.getLogger(MiniBlogManage.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	MiniBlogMngDAO dao=MiniBlogMngDAO.getInstance();
	// 微博申请初始化页面(经销商)
	private final String miniBlogInitUrl = "/jsp/sales/userMng/miniBlogInit.jsp";
	private final String miniBlogAddUrl = "/jsp/sales/userMng/addBlogInfoAppDLR.jsp";
	private final String  CHOOSE_SALESMAN_URL="/jsp/sales/userMng/salesManChoose.jsp";
	private final String  miniBlogUpdateUrl="/jsp/sales/userMng/updateBlogInfoAppDLR.jsp";
	//车厂
	private final String miniBlogAuditInitUrl = "/jsp/sales/userMng/blogOEMInit.jsp";
	private final String miniBlogAuditUrl = "/jsp/sales/userMng/blogInfoAuditOEM.jsp";
	
	
	private final String miniBlogSelectInitUrl = "/jsp/sales/userMng/miniBlogSelectInit.jsp";
	private final String miniBlogSelectUrl = "/jsp/sales/userMng/blogQueryInfo.jsp";
	
	
	
	/**
	 * 微博申请初始化
	 */
	public void miniBlogAppInit(){
		try{
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			List<String> yearList=new ArrayList<String>();
			for(int i=year;i>=year-3;i--){
				yearList.add(i+"");
			}
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			act.setOutData("list", list);
			act.setOutData("yearList", yearList);
			act.setForword(miniBlogInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博申请查询
	 */
	public void miniBlogAppQuery(){
		try{
			int curPage=Integer.parseInt(CommonUtils.checkNull(request.getParamValue("curPage")));
			String blogNo=CommonUtils.checkNull(request.getParamValue("blogNo"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String integ_month=CommonUtils.checkNull(request.getParamValue("integ_month"));
			Map<String,String> map=new HashMap<String, String>();
			map.put("blogNo",blogNo);
			map.put("dealerId", logonUser.getDealerId());
			map.put("blogNo",blogNo);
			map.put("year", year);
			map.put("integ_month", integ_month);
			PageResult<Map<String, Object>> ps = dao.queryBlogAppInfo(map,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);  
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博申请添加初始化
	 */
	public void miniBlogAddPre(){
		try{
			String dealerId=logonUser.getDealerId();
			TmDealerPO tdpo=new TmDealerPO();
			tdpo.setDealerId(new Long(dealerId));
			tdpo=(TmDealerPO) dao.select(tdpo).get(0);
			act.setOutData("dealer", tdpo);
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			int month=c.get(Calendar.MONTH)+1;
			List<String> yearList=new ArrayList<String>();
			for(int i=year;i>=year-3;i--){
				yearList.add(i+"");
			}
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			act.setOutData("list", list);
			act.setOutData("yearList", yearList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(miniBlogAddUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void chooseSalesManInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CHOOSE_SALESMAN_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "微博积分销售人员初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:微博积分上报：查询销售顾问
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-18
	 */
	public void getPersonList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String selectedMan= CommonUtils.checkNull(request.getParamValue("selectedMan"));
			String dealerId=logonUser.getDealerId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
			Map<String,String> map=new HashMap<String, String>();
			map.put("dealerId", dealerId);
			map.put("selectedMan", selectedMan);
			PageResult<Map<String, Object>> ps = dao.getSalesManList(map,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博积分保存
	 */
	public void saveBlog() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String sumitflag=CommonUtils.checkNull(request.getParamValue("sumitflag"));
			String blogId=CommonUtils.checkNull(request.getParamValue("blogId"));
			saveData(logonUser,sumitflag,blogId);
			act.setOutData("flag","success");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "微博积分保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据状态保存数据
	 * @param logonUser
	 * @param sumitflag
	 * @param operateflag
	 */
	private void saveData(AclUserBean logonUser,String operateflag,String  blogId) throws Exception{
		String blog_no="";
		if(blogId!=null&&!"".equals(blogId)){
			TtVsBlogPO tvbp0=new TtVsBlogPO();
			tvbp0.setBlogId(new Long(blogId));
			tvbp0=(TtVsBlogPO) dao.select(tvbp0).get(0);
			//获取原来的单号
			blog_no=tvbp0.getBlogNo();
			//删除原来的数据
			TtVsBlogPO tvbp=new TtVsBlogPO();
			tvbp.setBlogId(new Long(blogId));
			TtVsBlogDetailPO tvbdp=new TtVsBlogDetailPO();
			tvbdp.setBlogId(new Long(blogId));
			dao.delete(tvbp);
			dao.delete(tvbdp);
			
		}
		
		String integ_month= CommonUtils.checkNull(request.getParamValue("integ_month"));
		String year= CommonUtils.checkNull(request.getParamValue("year"));
		String []blogOnes=request.getParamValues("webone");
		String []blogTwos=request.getParamValues("webtwo");
		String []blogThrees=request.getParamValues("webthree");
		String []blogIntegs=request.getParamValues("blog_integ");
		String []salesIds=request.getParamValues("salesIds");
		TtVsBlogPO blogPo=new TtVsBlogPO();
		String blog_id=SequenceManager.getSequence("");
		blogPo.setIntegMonth(new Long(integ_month));
		if(blogId!=null&&!"".equals(blogId)){
			blogPo.setBlogNo(blog_no);
		}else{
			blogPo.setBlogNo(CommonUtils.getWebNo(logonUser.getDealerId()));
			
		}
		//如果是提交
		if("1".equals(operateflag)){
			blogPo.setStatus(Constant.WEB_TYPE_02);
		}else{
			blogPo.setStatus(Constant.WEB_TYPE_01);
		}
		
		blogPo.setDealerId(new Long(logonUser.getDealerId()));
		blogPo.setBlogId(new Long(blog_id));
		blogPo.setCreateBy(logonUser.getUserId());
		blogPo.setCreateDate(new Date());
		blogPo.setYear(new Long(year));
		dao.insert(blogPo);
		 for(int i=0;i< blogOnes.length;i++){
			 TtVsBlogDetailPO blogDetailPo=new TtVsBlogDetailPO();
			 String blog_detail_id=SequenceManager.getSequence("");
			 blogDetailPo.setDetailId(new Long(blog_detail_id));
			 blogDetailPo.setBlogId(new Long(blog_id));
			 blogDetailPo.setBlogOne(blogOnes[i]);
			 blogDetailPo.setBlogTwo(blogTwos[i]);
			 blogDetailPo.setBlogThree(blogThrees[i]);
			 blogDetailPo.setBlogInteg(new Long(blogIntegs[i]));
			 blogDetailPo.setCreateDate(new Date());
			 blogDetailPo.setCreateBy(logonUser.getUserId());
			 blogDetailPo.setSalesId(new Long(salesIds[i]));
			 dao.insert(blogDetailPo);
		 }
	}
	/**
	 * 微博积分申请修改初始化
	 */
	public void miniBlogAppUpdatePre(){
		try{
			String blogId=request.getParamValue("blogId");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			tvpo.setBlogId(new Long(blogId));
			tvpo=(TtVsBlogPO) dao.select(tvpo).get(0);
			List<Map<String,Object>> lists=dao.selectDetailList(blogId);
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			List<String> yearList=new ArrayList<String>();
			for(int i=year-3;i<=year;i++){
				yearList.add(i+"");
			}
			String dealerId=logonUser.getDealerId();
			TmDealerPO tdpo=new TmDealerPO();
			tdpo.setDealerId(new Long(dealerId));
			tdpo=(TmDealerPO) dao.select(tdpo).get(0);
			act.setOutData("dealer", tdpo);
			act.setOutData("blog",tvpo);
			act.setOutData("list", list);
			act.setOutData("detailList", lists);
			act.setOutData("yearList", yearList);
			act.setForword(miniBlogUpdateUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询界面提交
	 */
	public void submitBlogApp(){
		try{
			String blogId=request.getParamValue("blogId");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			tvpo.setBlogId(new Long(blogId));
			TtVsBlogPO tvpo1=new TtVsBlogPO();
			tvpo1.setStatus(Constant.WEB_TYPE_02);
			dao.update(tvpo, tvpo1);
			act.setOutData("flag","success");
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询界面删除
	 */
	public void miniBlogDelete(){
		try{
			String blogId=request.getParamValue("blogId");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			tvpo.setBlogId(new Long(blogId));
			TtVsBlogPO tvpo1=new TtVsBlogPO();
			tvpo1.setStatus(Constant.WEB_TYPE_05);
			dao.update(tvpo, tvpo1);
			act.setOutData("flag","success");
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博申请初始化
	 */
	public void miniBlogAuditInit(){
		try{
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			List<String> yearList=new ArrayList<String>();
			for(int i=year-3;i<=year;i++){
				yearList.add(i+"");
			}
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			act.setOutData("list", list);
			act.setOutData("yearList", yearList);
			act.setForword(miniBlogAuditInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博积分申请修改初始化
	 */
	public void miniBlogAuditPre(){
		try{
			String blogId=request.getParamValue("blogId");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			tvpo.setBlogId(new Long(blogId));
			tvpo=(TtVsBlogPO) dao.select(tvpo).get(0);
			List<Map<String,Object>> lists=dao.selectDetailList(blogId);
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			String dealerId=tvpo.getDealerId().toString();
			TmDealerPO tdpo=new TmDealerPO();
			tdpo.setDealerId(new Long(dealerId));
			tdpo=(TmDealerPO) dao.select(tdpo).get(0);
			act.setOutData("dealer", tdpo);
			act.setOutData("blog",tvpo);
			act.setOutData("list", list);
			act.setOutData("detailList", lists);
			act.setForword(miniBlogAuditUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博审核查询
	 */
	public void miniBlogAuditQuery(){
		try{
			int curPage=Integer.parseInt(CommonUtils.checkNull(request.getParamValue("curPage")));
			String blogNo=CommonUtils.checkNull(request.getParamValue("blogNo"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String integ_month=CommonUtils.checkNull(request.getParamValue("integ_month"));
			Map<String,String> map=new HashMap<String, String>();
			map.put("blogNo",blogNo);
			map.put("year", year);
			map.put("integ_month", integ_month);
			PageResult<Map<String, Object>> ps = dao.queryBlogAuditInfo(map,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);  
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 审核
	 */
	public void auditBlog(){
		try{
			String blogId=request.getParamValue("blogId");
			String flag=request.getParamValue("flag");
			String auditRemark=request.getParamValue("auditRemark");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			
			tvpo.setBlogId(new Long(blogId));
			TtVsBlogPO tvpo1=new TtVsBlogPO();
			if("1".equals(flag)){
				tvpo1.setStatus(Constant.WEB_TYPE_03);
			}else{
				tvpo1.setStatus(Constant.WEB_TYPE_04);
			}
			dao.update(tvpo, tvpo1);
			TtVsBlogLogPO tvblPO=new TtVsBlogLogPO();
			tvblPO.setLogId(new Long(SequenceManager.getSequence("")));
			tvblPO.setBlogId(new Long(blogId));
			tvblPO.setAuditBy(logonUser.getUserId());
			tvblPO.setAuditDate(new Date());
			tvblPO.setAuditRemark(auditRemark);
			dao.insert(tvblPO);
			act.setOutData("flag","success");
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博积分查询
	 */
	public void miniBlogSelect(){
		try{
			int curPage=Integer.parseInt(CommonUtils.checkNull(request.getParamValue("curPage")));
			String blogNo=CommonUtils.checkNull(request.getParamValue("blogNo"));
			Map<String,String> map=new HashMap<String, String>();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String integ_month=CommonUtils.checkNull(request.getParamValue("integ_month"));
			map.put("year", year);
			map.put("integ_month", integ_month);
			map.put("blogNo",blogNo);
			map.put("dealerId", logonUser.getDealerId());
			PageResult<Map<String, Object>> ps = dao.queryBlogQueryInfo(map,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);  
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 微博积分初始化
	 */
	public void miniBlogSelectInit(){
		try{
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			List<String> yearList=new ArrayList<String>();
			for(int i=year-3;i<=year;i++){
				yearList.add(i+"");
			}
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			act.setOutData("list", list);
			act.setOutData("yearList", yearList);
			act.setForword(miniBlogSelectInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 微博积分申请修改初始化
	 */
	public void miniBlogQueryDetail(){
		try{
			String blogId=request.getParamValue("blogId");
			TtVsBlogPO tvpo=new TtVsBlogPO();
			tvpo.setBlogId(new Long(blogId));
			tvpo=(TtVsBlogPO) dao.select(tvpo).get(0);
			List<Map<String,Object>> lists=dao.selectDetailList(blogId);
			List<String> list=new ArrayList<String>();
			for(int i=1;i<=12;i++){
				list.add(i+"");
			}
			String dealerId=tvpo.getDealerId().toString();
			TmDealerPO tdpo=new TmDealerPO();
			tdpo.setDealerId(new Long(dealerId));
			tdpo=(TmDealerPO) dao.select(tdpo).get(0);
			act.setOutData("dealer", tdpo);
			act.setOutData("blog",tvpo);
			act.setOutData("list", list);
			act.setOutData("detailList", lists);
			act.setForword(miniBlogSelectUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"微博积分申请修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
