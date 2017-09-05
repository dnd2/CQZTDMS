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
		   <th colspan="8" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 维修配件</th>
           <tr  bgcolor="F3F4F8">
            <th > 是否三包</th>
            <th > 新件代码</th>
            <th > 新件名称</th>
            <th > 新件数量</th>
            <th > 单价</th>
            <th > 金额(元)</th>
            <th > 付费方式 </th>
            <th >
				<input type="button" onClick="addNoPartItem('itemTable');" class="normal_btn" style="width:75%" value="添加无零件"/>
 			</th>
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
           	 	<td>
           	 	&nbsp&nbsp
           	 	</td>
        	</tr>
    	   <%
						}
					%>
			<tbody id="itemTable"></tbody>
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
          <input type="button" onClick="submitPartItems();" class="normal_btn" style="width=8%" value="提交"/>
           &nbsp;&nbsp;
       </tr>
     </table>    
</form>
<form method="post" name = "fm2" id="fm2" >
	<input type="hidden" id="proId" name="proId" value=""/>
	<input type="hidden" id="noPartItems" name="noPartItems" value=""/>
	<input type="hidden" id="vinMy" name="vinMy" value=""/>
	<input type="hidden" id="purchaseDateMy" name="purchaseDateMy" value=""/>
	<input type="hidden" id="inMileageMy" name="inMileageMy" value=""/>
</form>
<script type="text/javascript">
	function closeWindow(){
		_hide();
	}
	function addNoPartItem(tableId){
		 // 动态生成表格
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		insertRow.insertCell(6);
		insertRow.insertCell(7);
		//这里的NAME都加0，空的时候就自动不插入到后台了
		addTable.rows[length].cells[0].innerHTML =  '<tr><td><input type="checkbox" name="IS_GUA" disabled="disabled"/></td>';
		addTable.rows[length].cells[1].innerHTML =  '<td>00-000</td>';
		addTable.rows[length].cells[2].innerHTML =  '<td>无零件</td>';
		addTable.rows[length].cells[3].innerHTML =  '<td>1</td>';
		addTable.rows[length].cells[4].innerHTML =  '<td>0.0</td>';
		addTable.rows[length].cells[5].innerHTML =  '<td>0.0</td>';
		addTable.rows[length].cells[6].innerHTML =  '<td>索赔</td>';
		addTable.rows[length].cells[7].innerHTML =  '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td></tr>';
		return addTable.rows[length];
	}
	//删除行
	function delItem(obj){
	    var tr = this.getRowObj(obj);
    	//MyAlert(tr.parentNode.childNodes.length);
	    if(tr != null){
		    	tr.parentNode.removeChild(tr);
		 }else{
		    	throw new Error("the given object is not contained by the table");
		 }
	}
	//得到行对象
	function getRowObj(obj){
	  var i = 0;
	  while(obj.tagName.toLowerCase() != "tr"){
	    obj = obj.parentNode;
	    if(obj.tagName.toLowerCase() == "table")
	  return null;
	   }
	  return obj;
	}
	//提交
	function submitPartItems(){
		var roId = '<%=CommonUtils.checkNull(tawep.getId())%>';
		var p1 = <%=partLs.size()%>;
		var p2 = document.getElementById('itemTable').childNodes.length;
		var pNum = p1+p2;
		var lNum = <%=itemLs.size()%>;
		var vinMy = '<%=CommonUtils.checkNull(tawep.getVin())%>';
		var purchaseDateMy = '<%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>';
		var inMileageMy = '<%=tawep.getInMileage()%>';
		//noPartItems
		if((pNum-lNum)>=0){
			document.getElementById('proId').value = roId;
			document.getElementById('noPartItems').value = p2;
			document.getElementById('vinMy').value = vinMy;
			document.getElementById('purchaseDateMy').value = purchaseDateMy;
			document.getElementById('inMileageMy').value = inMileageMy;
			var url="<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/addNoPartItems.json"
			submitForm('fm2') ? sendAjax(url,showResult,"fm2") : "";
		}else{
			MyAlert("维修配件的项数必须大于等于维修项目的项数！");
			return;
		}
	}
	//返回函数 add by tanv 2012-12-26
	function showResult(obj){
		var flag = obj.flag;
		if(flag=='1'){
			MyAlert('无零件项添加成功.');
			_hide();
			//var win = top.window;
			//parent.location.reload();
			//self.window.opener.locaction.reload();
			var urlMy="<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/ProblemRepairOrderQueryForward.do";
			try{
				parent.${'inIframe'}.__extQuery__(1);
			}catch(e){
				location.href=urlMy;
			}
		}else if(flag=='-2'){
			MyAlert('没有需要添加的无零件项.');
			_hide();
			var urlMy="<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/ProblemRepairOrderQueryForward.do";
			try{
				parent.${'inIframe'}.__extQuery__(1);
			}catch(e){
				location.href=urlMy;
			}
		}else if(flag=='-1'){
			_hide();
			MyAlert('数据操作失败.');
		}else{
			MyAlert(obj.Exception.message);
		}
	}
</script>
</body>
</html>