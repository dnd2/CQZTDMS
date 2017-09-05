<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.bean.TtAsRepairOrderExtBean"%>
<%@page import="com.infodms.dms.po.TtAsRoLabourPO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartPO"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>
<%@page import="com.infodms.dms.po.TtAsActivityPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>

<%
			TtAsRepairOrderExtBean tawep = (TtAsRepairOrderExtBean) request.getAttribute("application");
			List<TtAsRoLabourPO> itemLs = (LinkedList<TtAsRoLabourPO>) request.getAttribute("itemLs");
			List<TtAsRoRepairPartPO> partLs = (LinkedList<TtAsRoRepairPartPO>) request.getAttribute("partLs");
			List<TtAsRoAddItemPO> otherLs = (LinkedList<TtAsRoAddItemPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
		%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问题工单明细</title>
</head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;服务资料明细</div>
 <form method="post" name = "fm" id="fm" >
   <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
     </tr>
      <tr bgcolor="F3F4F8">
       <td width="14%" align="right">工单号：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getRoNo())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td width="13%" align="right">经销商代码：</td>
       <td width="19%"><%=CommonUtils.checkNull(tawep.getDealerCode())%></td>
       <td width="12%" align="right">经销商名称：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getDealerName())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">经销商电话：</td>
       <td><%=request.getAttribute("phone")%></td>
       <td align="right">维修类型：</td>
       <td>
       		<script>
       		document.write(getItemValue("<%=tawep.getRepairTypeCode()%>"));
       		</script>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">工单开始时间：</td>
       <td><%=CommonUtils.checkNull(Utility.handleDate(tawep.getRoCreateDate()))%></td>
       <td align="right">预计工单结束时间：</td>
       <td><%=CommonUtils.checkNull(Utility.handleDate(tawep.getDeliveryDate()))%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">进厂里程数：</td>
       <td><%=tawep.getInMileage()%></td>
       <td align="right">接 待 员：</td>
       <td><%=CommonUtils.checkNull(tawep.getServiceAdvisor())%></td>
     </tr>
      <tr bgcolor="F3F4F8">
       <td align="right">活动名称：</td>
       <td><%=CommonUtils.checkNull(tawep.getCampaignName())%></td>
       <td align="right">
                  是否固定费用：
       <%if (tawep.getCamFix()==1){ %>
	   <input type="checkbox" id="IS_FIX0" name="IS_FIX0" checked disabled/>
	   <%}else { %>
	   <input type="checkbox" id="IS_FIX0" name="IS_FIX0"   disabled />
	   <% } %>
	   </td>
     </tr>
   </table>
   <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 车辆信息</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <td width="14%" align="right">VIN：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getVin())%></td>
       <td width="13%" align="right">发动机号：</td>
       <td width="19%"><%=CommonUtils.checkNull(tawep.getEngineNo())%></td>
       <td width="12%" align="right">牌照号：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getLicense())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">品牌：</td>
       <td><%=CommonUtils.checkNullEx(tawep.getBrandName())%></td>
       <td align="right">车系：</td>
       <td><%=CommonUtils.checkNull(tawep.getSeriesName())%></td>
 	   <td align="right">车型：</td>
       <td><%=CommonUtils.checkNull(tawep.getModelName())%></td>
     </tr>
 	<tr bgcolor="F3F4F8">
       <td align="right">产地：</td>
       <td><%=CommonUtils.checkNull(tawep.getYieldly())%></td>
       <td align="right">购车日期：</td>
       <td><%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%></td>
 	   <td align="right">生产日期：</td>
       <td><%=CommonUtils.checkNull(Utility.handleDate(tawep.getProductDate()))%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">车主姓名：</td>
       <td>
       <c:if test="${fn:length(list)>0}">
       
       <c:out value="${list[0].CTM_NAME}"/>
       </c:if>
       </td>
       <td align="right">车主电话：</td>
       <td>
       <c:if test="${fn:length(list)>0}">
       <c:out value="${list[0].MAIN_PHONE}"/>
       </c:if>
       </td>
 	   <td align="right">三包代码：</td>
 	   <td>
 	   <c:if test="${fn:length(list)>0}">
       <c:out value="${list1[0].RULE_CODE}"/>
       </c:if>
       </td>
     </tr>
      <tr bgcolor="F3F4F8">
       <td align="right">配置：</td>
       <td><%=CommonUtils.checkNull(tawep.getPackageName())%></td>
     </tr>
   </table>
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 用户信息</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <td width="13%" align="right">用户名称：</td>
       <td width="19%"><%=CommonUtils.checkNull(tawep.getDeliverer()) %></td>
       <td width="12%" align="right">电话：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getDelivererPhone()) %></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td width="13%" align="right">手机：</td>
       <td width="19%"><%=CommonUtils.checkNull(tawep.getDelivererMobile()) %></td>
       <td width="12%" align="right">用户地址：</td>
       <td width="21%"><%=CommonUtils.checkNull(tawep.getDelivererAdress()) %></td>
     </tr>
   </table>
   <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
     <tr><th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />维修项目</th></tr>
	 <tr>
	        <th align="center">工时代码</th>
            <th align="center">工时名称</th>
            <th align="center">工时定额</th>
            <th align="center">工时单价</th>
            <th align="center">工时金额（元）</th>
            <th align="center">付费方式</th>
     </tr>
     <%
						for (int i = 0; i < itemLs.size(); i++) {
					%>
					<%
							TtAsRoLabourPO tl = (TtAsRoLabourPO)itemLs.get(i);
					%>	     
  
       		<tr>
            	<td><%=CommonUtils.checkNull(tl.getWrLabourcode())%></td>
            	<td><%=CommonUtils.checkNull(tl.getWrLabourname())%></td>
            	<td><%=CommonUtils.checkNull(tl.getStdLabourHour())%></td>
	            <td><%=CommonUtils.checkNull(tl.getLabourPrice())%></td>	
           	 	<td><%=CommonUtils.checkNull(tl.getLabourAmount())%></td>
           	 	<td>
           	 		<script>
       					document.write(getItemValue("<%=tl.getPayType()%>"));
       				</script>
           	 	</td>
        	</tr>
       <%
					}
					%>
  </table>
  <TABLE class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 维修配件</th>
           <tr  bgcolor="F3F4F8">
            <th > 是否三包</th>
            <th > 新件代码</th>
            <th > 新件名称</th>
            <th > 新件数量</th>
            <th > 单价</th>
            <th > 金额(元)</th>
            <th > 付费方式 </th>
           </tr>
           <%
						for (int i = 0; i < partLs.size(); i++) {
					%>
					<%
							TtAsRoRepairPartPO tl = partLs.get(i);
							//TtAsWrPartsitemPO tl = (TtAsWrPartsitemPO)clb.getMain();
					%>
       		<tr>
            	<td>
            	<%if (tl.getIsGua()==1) {%>
							<input type="checkbox" name="IS_GUA" checked disabled/>
							<%}else { %>
							<input type="checkbox" disabled /> <input type="hidden" name="IS_GUA" value="off"/>
							<%} %>
				</td>
            	<td><%=CommonUtils.checkNull(tl.getPartNo())%></td>
            	<td><%=CommonUtils.checkNull(tl.getPartName())%></td>
	            <td><%=(tl.getPartQuantity().intValue())%></td>	
           	 	<td><%=CommonUtils.checkNull(tl.getPartCostPrice())%></td>
           	 	<td><%=CommonUtils.checkNull(tl.getPartCostAmount())%></td>	
           	 	<td>
           	 		<script>
       					document.write(getItemValue("<%=tl.getPayType()%>"));
       				</script>
           	 	</td>
        	</tr>
    	   <%
						}
					%>
   </table>
       <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 申请内容</th>
     </tr>
    <tr>
					<td class="table_edit_2Col_label_5Letter">
						故障描述：
					</td>
					<td  >
						<textarea name='TROUBLE_DESC' id='TROUBLE_DESC' rows='2' cols='28' readonly='readonly'><%=CommonUtils.checkNull(tawep.getTroubleDescriptions()) %></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						故障原因：
					</td>
					<td>
						<textarea name='TROUBLE_REASON' id='TROUBLE_REASON' rows='2' cols='28' readonly='readonly'><%=CommonUtils.checkNull(tawep.getTroubleReason()) %></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
						维修措施：
					</td>
					<td  class="tbwhite">
						<textarea name='REPAIR_METHOD' id='REPAIR_METHOD' rows='2' cols='28' readonly='readonly'><%=CommonUtils.checkNull(tawep.getRepairMethod()) %></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						申请备注：
					</td>
					<td class="tbwhite">
						<textarea name='APP_REMARK' id='APP_REMARK' rows='2' cols='28' readonly='readonly'><%=CommonUtils.checkNull(tawep.getRemarks()) %></textarea>
					</td>
				</tr>
   </table>
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
          <input type="button" onClick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           &nbsp;&nbsp;
       </tr>
     </table>    
</form>
<script type="text/javascript">
	function closeWindow(){
		_hide();
	}
</script>
</body>
</html>