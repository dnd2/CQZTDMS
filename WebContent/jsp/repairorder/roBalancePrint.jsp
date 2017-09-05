<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.TtAsRepairOrderExtBean"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartBean"%> 
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.text.DecimalFormat" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%
		Double price1 = 0.0;
		Double price2 = 0.0; 
	DecimalFormat   df  =   new  DecimalFormat("##0.00");
	/** 格式化金钱时保留的小数位数 */
	int minFractionDigits = 2;
  		/** 当格式化金钱为空时，默认返回值 */
  		String defaultValue = "0";
  		TtAsRepairOrderExtBean tawep = (TtAsRepairOrderExtBean) request.getAttribute("application");
	List<TtAsRoRepairPartBean> partLs = (LinkedList<TtAsRoRepairPartBean>) request.getAttribute("partLs");
	List<TtAsRoAddItemPO> otherLs = (LinkedList<TtAsRoAddItemPO>) request.getAttribute("otherLs");
	String id = (String) request.getAttribute("ID");
%>
<script language="javascript">    
function init(){
var type = document.getElementById("claimType").value;
var comType = document.getElementsByName("hasPart");
var flag = true;
for(var i=0;i<comType.length;i++){
	if(type==comType[i].value){
	document.getElementById(comType[i].value).setAttribute("checked","checked"); 
	flag = false;
	}
}
if(flag){
	document.getElementById("other").setAttribute("checked","checked"); 
}
}

function  ffTableId()
	{
	   	var ffTable = document.getElementById('ffTableIds');
	}
	
function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
} 

function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){    
	if (confirm('确定打印吗？')){
	wb.execwb(6,6)    
	}    
}    

function nowprint() {   
    window.print();   
}   
function window.onbeforeprint() {   
    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
}   
function window.onafterprint() {   
    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
}   

function sxsw(){
	WebBrowser.ExecWB(7,1); 
	window.opener=null; 
	window.close();

</script>

<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	</head>
<body onload="init(); ffTableId(); tiaoma();" >
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">幻速汽车维修结算工单</font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">
<input type="hidden" name="claimType" id="claimType" value="${bean.claimType }"/>
<center>
<table class="tab_print" width="750px">
	 <tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="9%"nowrap="true" style="border: none;">
      
       </td>
      <td width="8%"nowrap="true"style="border: none;">
       </td>
       <td width="25%"nowrap="true" style="border: none;">
       
       </td>
      <td width="8%"nowrap="true" style="border: none;">
       </td>
       <td width="25%"nowrap="true"style="font-weight: normal;; font-size:10px;" style="text-align: right;border: none;">
       	工单号:<%=CommonUtils.checkNull(tawep.getRoNo())%>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">服务商代码
       </td>
      <td width="25%"nowrap="true">
      ${dealerPO.dealerCode }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">服务商名称
       </td>
       <td width="25%"nowrap="true">
       ${application.dealerShortname }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">服务商电话
       </td>
       <td width="25%"nowrap="true">
        ${dealerPO.phone }
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">服务顾问
       </td>
      <td width="25%"nowrap="true">
      <%=CommonUtils.checkNull(tawep.getServiceAdvisor())%>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">进店日期
       </td>
       <td width="25%"nowrap="true">
       <%=CommonUtils.checkNull(tawep.getCreDate())%>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">结算日期
       </td>
       <td width="25%"nowrap="true">
       <%=CommonUtils.checkNull(Utility.handleDate(tawep.getForBalanceTime()))%>
       </td>
    </tr>
	 <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">用户信息
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">用户姓名
       </td>
      <td width="25%"nowrap="true">
      	${customerPO.ctmName }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">用户电话
       </td>
       <td width="25%"nowrap="true">
       ${customerPO.mainPhone }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">用户地址
       </td>
       <td width="25%"nowrap="true">
       ${customerPO.address }
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">送修人姓名
       </td>
      <td width="25%"nowrap="true">
      	<%=CommonUtils.checkNull(tawep.getDeliverer()) %>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">送修人电话
       </td>
       <td width="25%"nowrap="true">
       <%=CommonUtils.checkNull(tawep.getDelivererPhone()) %>
       </td>
      <td width="8%"nowrap="true" style="font-weight: bold; font-size:12px;"> 是否救援
       </td>
       <td width="25%"nowrap="true">
       <c:if test="${tawep.repairTypeCode ==  11441002}">
          是
       </c:if>
        <c:if test="${tawep.repairTypeCode !=  11441002}">
          否
       </c:if>
       </td>
    </tr>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">车辆信息
       </td>
    </tr>
    <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">车型
       </td>
      <td width="25%"nowrap="true">
      	<%=CommonUtils.checkNull(tawep.getModelName())%>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">VIN号
       </td>
       <td width="25%"nowrap="true">
       	<%=CommonUtils.checkNull(tawep.getVin())%>
       </td>
      <td width="8%"nowrap="true" style="font-weight: bold; font-size:12px;">发动机号
       </td>
       <td width="25%"nowrap="true">
       	<%=CommonUtils.checkNull(tawep.getEngineNo())%>
       </td>
    </tr>
    <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">行驶里程
       </td>
      <td width="25%"nowrap="true">
      	<%=CommonUtils.checkNull( tawep.getInMileage())%>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">购车日期
       </td>
       <td width="25%"nowrap="true">
       <%=CommonUtils.checkNull(Utility.handleDateAc(tawep.getGuaranteeDate()))%>
       </td>
      <td width="8%"nowrap="true" style="font-weight: bold; font-size:12px;">车牌
       </td>
       <td width="25%"nowrap="true">
       <%=CommonUtils.checkNull(tawep.getLicense())%>
       </td>
    </tr>
    <tr >
      <td colspan="1" style="text-align: left;" nowrap="true" style="font-weight: bold; font-size:12px;" nowrap="true">&nbsp;&nbsp;车辆用途
       </td>
       <td colspan="5" nowrap="true" style="text-align: left;">
             	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>非家用</span>
       </td>
    </tr>
    <tr >
      <td colspan="1" style="text-align: left;" nowrap="true" style="font-weight: bold; font-size:12px;">&nbsp;&nbsp;故障描述
      	
       </td>
       <td colspan="5" nowrap="true">
       <%=CommonUtils.checkNull(tawep.getTroubleDescriptions())%>
       </td>
    </tr>
    <tr >
      <td colspan="1" style="text-align: left;" style="font-weight: bold; font-size:12px;" nowrap="true">&nbsp;&nbsp;原因分析
      	
       </td>
       <td colspan="5" nowrap="true">
       <%=CommonUtils.checkNull(tawep.getTroubleReason())%>
       </td>
    </tr>
    <tr >
      <td colspan="1" style="text-align: left;" style="font-weight: bold; font-size:12px;" nowrap="true">&nbsp;&nbsp;处理方案
      	
       </td>
       <td colspan="5" nowrap="true">
       <%=CommonUtils.checkNull(tawep.getRepairMethod())%>
       </td>
    </tr>
</table>
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="50%"nowrap="true" style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="5" style="text-align: center; font-size: 18px; font-weight: bold;">维修项目明细
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 是否三包
       </td>
       <td  colspan="2" width="20%"nowrap="true" style="font-weight: bold; font-size:12px;">
      	 作业代码
       </td>
      <td width="50%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 作业名称
       </td>
    </tr>
    <c:forEach var="part" items="${partLs}" varStatus="st">
	    <tr >
		 <td width="10%"nowrap="true">
		 	${st.index+1 }
	       </td>
	       
	      <td width="10%"nowrap="true" >
	        是
	       </td>
	       <td colspan="2" width="20%"nowrap="true" >
	       	${part.labourCode }
	       </td>
	      <td width="50%"nowrap="true">
	      	${part.labourName }
	       </td>
	    </tr>
    </c:forEach>
</table>
<c:if test="${length!=0 }">
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="50%"nowrap="true" style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">补偿
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 供应商代码
       </td>
       <td  width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
      	 申请金额
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 审批金额
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 补偿关联主因件
       </td>
    </tr>
    <c:forEach var="compensationMoneyList" items="${compensationMoneyList}" varStatus="st">
	    <tr >
		 <td width="10%"nowrap="true">
		 	${st.index+1 }
	       </td>
	      <td width="10%"nowrap="true" >
			${compensationMoneyList.SUPPLIER_CODE }
	       </td>
	      <td width="10%"nowrap="true" >
			${compensationMoneyList.APPLY_PRICE }
	       </td>
	       <td width="10%"nowrap="true" >
	       	${compensationMoneyList.PASS_PRICE }
	       </td>
	      <td  width="10%"nowrap="true">
	      	${compensationMoneyList.PART_CODE}
	       </td>
	    </tr>
    </c:forEach>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">辅料
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 工时代码
       </td>
       <td  width="10%" nowrap="true" style="font-weight: bold; font-size:12px;">
      	 工时名称
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 金额
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	辅料关联主因件 
       </td>
    </tr>
    <c:forEach var="accessoryDtlList" items="${accessoryDtlList}" varStatus="st">
	    <tr >
		 <td width="10%"nowrap="true">
		 	${st.index+1 }
	       </td>
	       
	      <td width="10%"nowrap="true" >
			${accessoryDtlList.WORKHOUR_CODE }
	       </td>
	       <td width="10%" nowrap="true" >
	       	${accessoryDtlList.WORKHOUR_NAME }
	       </td>
	      <td  width="10%"nowrap="true">
	      	${accessoryDtlList.PRICE}&nbsp;
	       </td>
	       <td width="10%" nowrap="true"> 
	      	${accessoryDtlList.MAIN_PART_CODE }
       </td> 
	    </tr>
    </c:forEach>
</table>
</c:if>
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="20%"nowrap="true" style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="7" style="text-align: center; font-size: 18px; font-weight: bold;">维修零部件明细
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 是否三包
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
      	是否主损
       </td>
       <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;" >
      	 配件代码
       </td>
      <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 配件名称
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 计量单位
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;" >
     	数量
       </td>
    </tr>
    <c:forEach var="part1" items="${partLs}" varStatus="sta">
    <tr >
	 <td width="10%"nowrap="true">
	 	${sta.index+1 }
       </td>
      <td width="10%"nowrap="true" >
        是
       </td>
      <td width="10%"nowrap="true">
      <c:if test="${part1.responsNature  == 94001001}">是</c:if>
       <c:if test="${part1.responsNature  == 94001002}">否</c:if>
       </td>
       <td width="20%"nowrap="true" >
       ${part1.partNo }
       </td>
      <td width="20%"nowrap="true">
      	${part1.partName }
       </td>
       <td width="10%"nowrap="true">
       	<%-- ${part1.partCostPrice } --%>
       	${part1.unit }
       </td>
       <td width="10%"nowrap="true">
       	${part1.partQuantity }
       </td>
    </tr>
     </c:forEach>
</table>
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="50%"nowrap="true" style="border: none;">
       </td>
    </tr>
     <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">外出维修
       </td>
    </tr>
	<tr >
	 <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
		 是否三包
       </td>
      <td width="10%"nowrap="true" >
        是
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 外出里程
       </td>
       <td width="10%"nowrap="true">
         ${outrepairPO.outMileage}
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
       	外出地点
       </td>
       <td width="50%"nowrap="true">
        ${outrepairPO.endAdress}
       </td>
    </tr>
	<tr >
	 <td width="10%"nowrap="true" rowspan="2" style="font-weight: bold; font-size:12px;">
		满意度评价
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
      	非常满意
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	较满意
       </td>
       <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
       	不满意
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
       	非常不满意
       </td>
       <td width="50%"nowrap="true" style="text-align: left;" rowspan="2">
       		&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight: bold; font-size:12px;">不满意原因：</span>
       </td>
    </tr>
	<tr >
      <td width="10%"nowrap="true" >
      
       </td>
      <td width="10%"nowrap="true">
       </td>
       <td width="10%"nowrap="true">
       </td>
       <td width="10%"nowrap="true">
       </td>
    </tr>
</table>  
<table class="tab_print" width="750px">
	<tr >
      <td width="15%"nowrap="true" >
      	服务顾问签字：
       </td>
       <td width="60%"nowrap="true">
       </td>
       <td width="10%"nowrap="true">
       		用户签字：
       </td>
       <td width="15%"nowrap="true">
       </td>
    </tr>
</table>
<table class="tab_print" width="750px">
	<tr >
	 <td width="5%"nowrap="true" style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	年
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	月
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	日
       </td>
       
       <td width="52%"nowrap="true"style="border: none;">
       </td>
       <td width="5%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true"style="border: none;" >
      	年
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true"style="border: none;" >
      	月
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	日
       </td>
    </tr>
</table>
  <br>
    <p class="Noprint">
		<input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
		<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
		<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
    </p>
</center>
</form>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	} 
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>

