<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.bean.CruiServiceBasicHeaderInfoBean" %>
<%@ page import="com.infodms.dms.bean.SpeFeeVehicleListInfoBean" %>
<%@ page import="com.infodms.dms.bean.SpeFeeApproveDetailInfoBean" %>
<%@ page import="com.infodms.dms.bean.SpeFeeApproveLogListBean" %>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>特殊外出费用确认单</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   SpeFeeApproveDetailInfoBean detailBean=(SpeFeeApproveDetailInfoBean)request.getAttribute("detailBean");
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
   List<SpeFeeVehicleListInfoBean> vehicleList = (LinkedList<SpeFeeVehicleListInfoBean>)request.getAttribute("vehicleList");
   List<SpeFeeApproveLogListBean> logList = (LinkedList<SpeFeeApproveLogListBean>)request.getAttribute("logList");
%>
</head>
<script type="text/javascript">
function keyListnerResp(){
   if((typeof window.event)!= 'undefined'){
	   var type = event.srcElement.type;   
       var code = event.keyCode;
       if(type=='text'||type=='textarea'){
    	   event.returnValue=true;
       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
            event.returnValue=false;       
         }
       }
       if ((code==116)||(event.ctrlKey&event.keyCode==82)){//屏蔽刷新
    	   event.returnValue=false;
       }
   }
}
//屏蔽鼠标右键
function document.oncontextmenu(){ 
   return false; 
} 
	
</script>
<body onkeydown="keyListnerResp();">
 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;特殊外出费用确认单</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" id="fjids" name="fjids"/>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核操作</th>
     <tr bgcolor="F3F4F8">
       <td align="right">审核意见：</td>
       <td>
         <textarea rows="3" cols="50" id="approve_content" name="approve_content" datatype="1,is_textarea,60"></textarea>
         <input type="button" id="pass_btn" value="通过" class="normal_btn" onclick="pass()"/>
         &nbsp;&nbsp;
         <input type="button" id="reject_btn" value="驳回" class="normal_btn" onclick="reject()"/>
         &nbsp;&nbsp;
         <input type="button" id="pass_btn" value="关闭" class="normal_btn" onclick="closeMe();"/>
       </td>
     </tr>
  </table>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
     <tr bgcolor="F3F4F8">
       <td align="right">特殊外出单据：</td>
       <td><%=CommonUtils.checkNull(detailBean.getFee_no())%></td>
       <td align="right">巡航单据：</td>
       <td><%=CommonUtils.checkNull(detailBean.getCr_no())%></td>
       <td align="right"></td>
       <td></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">经销商代码：</td>
       <td><%=CommonUtils.checkNull(detailBean.getDealer_code())%></td>
       <td align="right">经销商名称：</td>
       <td><%=CommonUtils.checkNull(detailBean.getDealer_name())%></td>
       <td align="right">特殊外出制单日期：</td>
       <td><%=CommonUtils.checkNull(detailBean.getMake_date())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航目的地：</td>
       <td><%=CommonUtils.checkNull(detailBean.getCr_whither())%></td>
       <td align="right">生产厂家：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("produce_name"))%></td>
       <td align="right">费用渠道：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("code_name"))%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出开始日期：</td>
       <td>
          <%=CommonUtils.checkNull(detailBean.getStart_date())%>
       </td>
       <td align="right">外出结束日期：</td>
       <td>
          <%=CommonUtils.checkNull(detailBean.getEnd_date())%>
       </td>
       <td align="right">外出天数：</td>
       <td><%=CommonUtils.checkNull(detailBean.getOut_days())%>天</td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出人数：</td>
       <td>
          <%=CommonUtils.checkNull(detailBean.getPerson_num())%>
       </td>
       <td align="right">出差人员：</td>
       <td>
          <textarea id="out_name" name="out_name" rows="2" cols="20" readonly="readonly"><%=CommonUtils.checkNull(detailBean.getPerson_name())%></textarea>
       </td>
       <td align="right">总里程：</td>
       <td><%=CommonUtils.checkNull(detailBean.getSingle_mileage())%>km</td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">过路过桥费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getPass_fee())%>'));
          </script>元
       </td>
       <td align="right">车辆或交通补助：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getTraffic_fee())%>'));
          </script>元
       </td>
       <td align="right">住宿费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getQuarter_fee())%>'));
          </script>元
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">餐补费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getEat_fee())%>'));
          </script>元
       </td>
       <td align="right">人员补助：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getPerson_subside())%>'));
          </script>元
       </td>
       <td align="right">总费用：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailBean.getTotal_fee())%>'));
          </script>元
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">申请内容：</td>
       <td colspan="3">
          <textarea id="apply_content" name="apply_content" rows="4" cols="50" readonly="readonly"><%=CommonUtils.checkNull(detailBean.getApply_content())%></textarea>
       </td>
     </tr>
  </table>
  <!-- 展示附件 -->
  <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
  </table>
  
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="12"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆信息
     </th>
     <tr bgcolor="F3F4F8">
       <th align="center">VIN</th>
       <th align="center">发动机号</th>
       <th align="center">车型</th>
       <th align="center">生产日期</th>
       <th align="center">里程</th>
       <th align="center">客户姓名</th>
       <th align="center">客户电话</th>
       <th align="center">销售日期</th>
       <th align="center">备注</th>
     </tr>
     <tbody id="itemTable">
      <%for(int count=0;count<vehicleList.size();count++){
    	  SpeFeeVehicleListInfoBean vehicleInfo=new SpeFeeVehicleListInfoBean();
    	  vehicleInfo=(SpeFeeVehicleListInfoBean)vehicleList.get(count);
        %>
       <tr>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getVin())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getEngine_no())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getModel())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getProduct_date())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getMileage())%>km
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getCustomer_name())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getCustomer_phone())%>
         </td>
         <td>
            <%=CommonUtils.checkNull(vehicleInfo.getSale_date())%>
         </td>
         <td>
            <textarea rows="1" cols="15" readonly="readonly"><%=CommonUtils.checkNull(vehicleInfo.getRemark())%></textarea>
         </td>
       </tr>
      <%}%>
	 </tbody>
  </table>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="12"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审批明细
     </th>
     <tr bgcolor="F3F4F8">
       <th align="center">审批日期</th>
       <th align="center">审批人员</th>
       <th align="center">人员部门</th>
       <th align="center">审批状态</th>
       <th align="center">审批意见</th>
     </tr>
      <%
      if(logList!=null){
    	  for(int count=0;count<logList.size();count++){
    		  SpeFeeApproveLogListBean logInfo=new SpeFeeApproveLogListBean();
    		  logInfo=(SpeFeeApproveLogListBean)logList.get(count);
        	  
          %>
          <tr>
           <td>
             <%=CommonUtils.checkNull(logInfo.getAuditing_date())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getUser_name())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getDept_name())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getAudit_status())%>
           </td>
           <td>
            <textarea rows="1" cols="15" readonly="readonly"><%=CommonUtils.checkNull(logInfo.getAuditing_opinion())%></textarea>
           </td>
          </tr>
          <%
    	  }
      }%>
       
  </table>
  <!-- 资料显示区结束 -->
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
var closeStr="unApproved";//关闭控制，如没有审批只是关闭当前审批页面，否则关闭页面并刷新父页面
function closeMe(){
	if(closeStr=="unApproved"){
	   parent.window._hide();
	}else if(closeStr=="haveApproved"){
	   refreshParentPage();
	}
}
//格式化时间
function formatDate(value){
  return String.format(value.substring(0,10));
}
//格式化取出空的现象
function formatNumber(value){
	if(value==null||value==""||value=="undefined"||typeof(value)== "undefined") return 0;
	return value;
}
//格式化取出空的现象
function formatNull(value){
	if(value==null||value==""||value=="undefined"||typeof(value)== "undefined") return "";
	return value;
}
function doInit(){
	loadcalendar();
}
//回到查询页面
function goSearchPage(){
	fm.action="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/queryListPage.do";
    fm.submit();
}
//通过
function pass(){
	var auditingOpinion=document.getElementById("approve_content").value;
	if(auditingOpinion.length>60){
	   MyDivAlert("审核意见请在60个字内！");
	   return false;
    }
	var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeConfirmManager/passSpeOper.json?order_id="+<%=detailBean.getId()%>;
	makeNomalFormCall(submit_url,CallAfterPass,'fm','createOrdBtn');
}
//回调通过函数
function CallAfterPass(json){
	var retCode=json.retCode;
	if(retCode=="success"){
		closeStr="haveApproved";
		document.getElementById("pass_btn").disabled="disabled";
		document.getElementById("reject_btn").disabled="disabled";
		MyDivConfirm("审批成功！",refreshParentPage,"");
	}else if(retCode=="failure_001"){
        MyDivAlert("无法获得特殊费用单据号！");
	}else if(retCode=="failure_002"){
        MyDivAlert("无法获得登陆人信息！");
	}
}
//驳回
function reject(){
	var reject_reason=document.getElementById("approve_content").value;
	if(reject_reason==''){
		MyDivAlert("请填写驳回时的审核意见！");
		return;
	}
	if(reject_reason.length>60){
	   MyDivAlert("审核意见请在60个字内！");
	   return false;
    }
	var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeConfirmManager/rejectSpeOper.json?order_id="+<%=detailBean.getId()%>;
	makeNomalFormCall(submit_url,CallAfterReject,'fm','createOrdBtn');
}
//回调驳回函数
function CallAfterReject(json){
	var retCode=json.retCode;
	if(retCode=="success"){
		closeStr="haveApproved";
		document.getElementById("pass_btn").disabled="disabled";
		document.getElementById("reject_btn").disabled="disabled";
		MyDivConfirm("驳回成功！",refreshParentPage,"");
	}else if(retCode=="failure_001"){
        MyDivAlert("无法获得特殊费用单据号！");
	}else if(retCode=="failure_002"){
        MyDivAlert("无法获得登陆人信息！");
	}
}
//刷新父页面
function refreshParentPage(){
	parent.window._hide();
	parentContainer.refreshPage();
}
</script>
</body>
</html>