<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.bean.CruiServiceBasicHeaderInfoBean" %>
<%@ page import="com.infodms.dms.bean.SpeFeeVehicleListInfoBean" %>
<%@ page import="com.infodms.dms.bean.SpeFeeDetailInfoBean" %>
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
<title>特殊外出费用查询</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   CruiServiceBasicHeaderInfoBean beanInfo=(CruiServiceBasicHeaderInfoBean)request.getAttribute("beanInfo");
   SpeFeeDetailInfoBean detailSpeBean=(SpeFeeDetailInfoBean)request.getAttribute("detailSpeBean");
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
   List<SpeFeeVehicleListInfoBean> vehicleList = (LinkedList<SpeFeeVehicleListInfoBean>)request.getAttribute("vehicleList");
   List<SpeFeeApproveLogListBean> logList = (LinkedList<SpeFeeApproveLogListBean>)request.getAttribute("logList");
   String vehicleStr="";
%>
</head>
<BODY>
 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;特殊外出费用查询</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" id="fjids" name="fjids"/>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
     <tr bgcolor="F3F4F8">
       <td align="right">经销商代码：</td>
       <td><%=CommonUtils.checkNull(detailSpeBean.getDealer_code())%></td>
       <td align="right">经销商名称：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getDealer_name())%>
       </td>
       <td align="right">特殊费用单据号：</td>
       <td><%=CommonUtils.checkNull(detailSpeBean.getFee_no())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出开始日期：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getStart_date())%>
       </td>
       <td align="right">外出结束日期：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getEnd_date())%>
       </td>
       <td align="right">外出天数：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getOut_days())%>天
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出人数：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getPerson_num())%>
       </td>
       <td align="right">出差人员：</td>
       <td>
          <textarea id="out_name" name="out_name" rows="2" cols="20" readonly="readonly"><%=CommonUtils.checkNull(detailSpeBean.getPerson_name())%></textarea>
       </td>
       <td align="right">总里程：</td>
       <td>
          <%=CommonUtils.checkNull(detailSpeBean.getSingle_mileage())%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">过路过桥费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getPass_fee())%>'));
          </script>元
       </td>
       <td align="right">车辆或交通补助：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getTraffic_fee())%>'));
          </script>元
       </td>
       <td align="right">住宿费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getQuarter_fee())%>'));
          </script>元
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">餐补费：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getEat_fee())%>'));
          </script>元
       </td>
       <td align="right">人员补助：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getPerson_subside())%>'));
          </script>元
       </td>
       <td align="right">总费用：</td>
       <td>
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(detailSpeBean.getTotal_fee())%>'));
          </script>元
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">申请内容：</td>
       <td colspan="3">
          <textarea id="apply_content" name="apply_content" rows="4" cols="50" readonly="readonly"><%=CommonUtils.checkNull(detailSpeBean.getApply_content())%></textarea>
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
		<th>
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
    	  vehicleStr=vehicleInfo.getVin()+",";
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
            <textarea rows="1" cols="30" readonly="readonly"><%=CommonUtils.checkNull(logInfo.getAuditing_opinion())%></textarea>
           </td>
          </tr>
          <%
    	  }
      }%>
       
  </table>
  <table class="table_list">
    <tr > 
      <th height="12" align=center>
       <input type="button" onclick="_hide()" class="normal_btn" style="width=8%" value="关闭"/></th>
    </tr>
  </table>
  <!-- 资料显示区结束 -->
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
function showUploadRowByDb(filename,fileId,fileUrl){
   var tab =document.getElementById("fileUploadTab");
   var row =tab.insertRow();
   row.className='table_list_row1';
   row.insertCell();
   row.insertCell();
   row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
   row.cells(1).innerHTML = "";    
}
</script>
</BODY>
</html>