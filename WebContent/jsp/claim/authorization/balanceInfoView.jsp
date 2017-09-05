<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 24px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 14px;
	color: #000000;
}

p {
	page-break-after: always;
}

</style>

<style>
<!--
td.boldTd {
	font-size: 14px;
	font-family: '宋体';
	font-weight: bold;
	height: 20px;
	text-align: left;
}

td.normalTd {
	font-size: 14px;
	font-family: '宋体';
	height: 20px;
}

table.sepTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.sepTab td,table.sepTab th {
	border: 1px solid black;
	font-size: 14px;
	font-family: '宋体';
	height: 20px;
}

table.midTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.midTab td {
	border: 1px solid black;
	font-family: '宋体';
	height: 20px;
	font-weight: normal;
}

table.largeTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.largeTab td,table.largeTab th {
	border: 1px solid black;
	font-size: 20px;
	font-family: '宋体';
	height: 30px;
}
-->
</style>

<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
//设置网页打印的页眉页脚为空    
function PageSetup_Null()   
{   
   try{    
       var Wsh=new ActiveXObject("WScript.Shell");    
       HKEY_Key="header";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
       HKEY_Key="footer";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
   }catch(e){}    
}


</script>
<title></title>
</head>
<body>
<center>
<form id="fm" name="fm">
            <center>
			<div style="width: 820px; text-align: center;"  align="center">
			<h4>服务站结算费用汇总单 </h4>
				<table width="90%;" class="tab_edit" style="text-align: center;" >
  			<tr height="40px;">
  				<td colspan="4">服务站号: ${mapdel.DEALER_CODE}</td>
  				<td colspan="3">联系电话:${mapdel.PHONE}</td>
  				<td colspan="3" >结算单号：${mapCLAIM.REMARK}<input id="balanecNo" type="hidden" name="BALANCE_ODER"  value="${mapCLAIM.REMARK}" />  </td>
  			</tr>
  			<tr>
  				<td colspan="10" align="left">重庆北汽幻速汽车销售有限公司        白联：北汽财务       红联：北汽售后     黄联：服务商存档  </td>
  			</tr>
  			<tr>
<%--   				<td align="center"><a style="color:red" href="#" style="text-decoration: none;cursor: pointer;" onclick="AppprintAll('${mapCLAIM.REMARK}')" >[一键打印]</a></td> --%>
  				<td colspan="10" align="left" >我站的保养、索赔单据等，经贵公司审核，结算情况如下</td>
  			</tr>
  			<tr>
  				<td>项目名称</td>
  				<td colspan="2">保养台次</td>
  				<td colspan="2">PDI台次</td>
  				<td colspan="2">一般索赔台次</td>
  				<td >二次入库台次</td>
  				<td>活动台次</td>
  				<td >合计台次</td>
  			</tr>
  			<tr>
  				<td>台次（台）</td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661002,'${mapApp.CLAIM_TYPE_02}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_02}</a><span style="color: red;font-size: 10px;">(打印)</span> </td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661011,'${mapApp.CLAIM_TYPE_11}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_11}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661001,'${mapApp.CLAIM_TYPE_01}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_01}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td><a href="#" style="text-decoration: none;cursor: pointer;" onclick="AppprintSencondByNo('${InfoSecondTime}','${mapCLAIM.REMARK}');" >${InfoSecondTime}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td> <a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661006,'${mapApp.CLAIM_TYPE_06}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_06}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td>${mapApp.COUNTSUM+InfoSecondTime}</td>
  			</tr>
  			<tr>
  				<td >项目名称</td>
  				<td >PDI费</td>
  				<td >保养费</td>
  				<td >一般索赔费</td>
  				<td >服务活动费</td>
  				
  				<td >旧件运费</td>
  				<td >返款金额</td>
  				<td >二次入库</td>
  				<td >其它金额</td>
  				<td >正负激励费用</td>
<!--   				<td>合计费用</td> -->
  			</tr>
  			<tr>
  				<td>金额（元）</td>
  				<td>${mapApp.CLAIM_TYPE_AMOUNT_11}</td>
  				<td>${mapApp.CLAIM_TYPE_AMOUNT_02}</td>
  				<td>${mapApp.CLAIM_TYPE_AMOUNT_01}</td>
  				<td>${mapApp.CLAIM_TYPE_AMOUNT_06}</td>
  				<td>${mapCLAIM.RETURN_AMOUNT}</td>
  				<td>${mapCLAIM.APPEND_AMOUNT}</td>
  				<td>${InfoMoney.secondTimeMoney}</td>
  				<td>${mapCLAIM.OTHERACCOUNT}</td>
  				<td>${mapCLAIM.PLUS_MINUS_SUM }</td>
  			</tr>
  			<tr>
  				<td >索赔费转备件款</td>
  				<td >是</td>
  				<td colspan="2">保养PDI费用</td>
  				<td colspan="2">${InfoMoney.pdiAndKeepFitMoney }</td>
  				<td>维修材料费</td>
  				<td>${InfoMoney.partAndAccMoney }</td>
  				<td>维修工时及其他</td>
  				<td>${InfoMoney.ohersMoney }</td>
  				
  			</tr>
  			
  			<tr>
  				<td align="center" colspan="10" id="fee_sum">
  					费用合计：${mapCLAIM.AMOUNT_SUM}
  				</td>
  			</tr>

  			<tr>
  				<td colspan="10" align="left">发票开票情况如下：</td>
  			</tr>
  			
  			<tr>
  				<td>开票日期</td>
  				<td>${date}</td>
  				<td colspan="2"></td>
  				<td colspan="2" ></td>
  				<td colspan="2" >
  				<c:if test="${STATUS == 0 }">
  				<select name="invoice_name" id="invoice_name">
  				<option value="0">-请选择-</option>
  				<c:forEach items="${voices}" var="voice">
  					<option value="${voice.taxRate}">${voice.invoiceName}</option>
  				</c:forEach>
  				</select>
  				</c:if>
  				</td>
  				<td colspan="2" id="ticket_fee" >开票金额:0  </td>
  				<input type="hidden" name="CLAIM_AMOUNT_SUM" id="claim_amount"/>
  			</tr>
  			
  			
  			
  			<tr>
  				<td><c:if test="${STATUS == 0 }">
  				<a href="#"><button onclick="addLine();">新增</button></a>
  				</c:if></td>
  				<td>序号</td>
  				<td colspan="2">发票批号</td>
  				<td colspan="2">发票号码</td>
  				<td>金额</td>
<!--   				<td>税金</td> -->
  				<td>税额</td>
  				<td>合计</td>
<!--   				<td>开票类别</td> -->
  				<td>备注</td>
  			</tr>
  			
<%--   			<c:if test="${STATUS != 0}"> --%>
<!--   			<tr> -->
<!--   				<td></td> -->
<!--   				<td>1</td> -->
<%--   				<td colspan="2">${LABOUR_RECEIPT }</td> --%>
<%--   				<td colspan="2">${PART_RECEIPT}</td> --%>
<%--   				<td>${AMOUNT_OF_MONEY }</td> --%>
<%--   				<td>${TAX_RATE_MONEY }</td> --%>
<%--   				<td>${mapCLAIM.AMOUNT_SUM}</td> --%>
<!--   				<td>索赔费</td> -->
<!--   				<td></td> -->
<!--   				<td></td> -->
<!--   			</tr> -->
<%--   			</c:if> --%>
  			
  			<c:if test="${STATUS == 0 }">
  			<c:forEach items="${list}" var="po" varStatus="status">
  				<tr>
  				<td></td>
  				<td>${status.index+1}</td>
  				<td ><input style="width: 130px;" type="text" name= "LABOUR_RECEIPT" value="${po.labourReceipt }" id="LABOUR_RECEIPT${status.index+1}" datatype="0,is_null,10" /> </td>
  				<td style="bo"></td>
  				<td ><input style="width: 130px;" type="text" name= "PART_RECEIPT" value="${po.partReceipt }" id="PART_RECEIPT${status.index+1}" datatype="0,is_null,8" /></td>
  				<td></td>
  				<td ><input style="width: 80px;"   name= "AMOUNT_OF_MONEY" value="${po.amountOfMoney }" id="AMOUNT_OF_MONEY${status.index+1}" /></td>
  				<td><input style="width: 80px;"   name= "TAX_RATE_MONEY" value="${po.taxRateMoney }" id="TAX_RATE_MONEY${status.index+1}"  onChange="checkNum(this);"/></td>
  				<td><input style="width: 80px;" name= "AMOUNT_SUM" value="${po.amountSum }"  onClick="countAll(${status.index+1});" id="sum${status.index+1}"/></td>
  				<td><input style="width: 80px;" name= "REMARK" value="${po.remark }" onClick="countAll(${status.index+1});" id="sum${status.index+1}" /></td>
  				<input type="hidden" name="ID"  value="${po.id}"
  			</tr>
  			</c:forEach>
  			</c:if>
  			
  			
  			<c:if test="${STATUS == 1 }">
  			<c:forEach items="${list}" var="po" varStatus="status">
  				<tr>
  				<td></td>
  				<td>${status.index+1}</td>
  				<td style="border-right: 0px;">${po.labourReceipt }</td>
  				<td style="border-left: 0px;"></td>
  				<td style="border-right: 0px;">${po.partReceipt }</td>
  				<td style="border-left: 0px;"> </td>
  				<td >${po.amountOfMoney }</td>
  				<td>${po.taxRateMoney }</td>
  				<td>${po.amountSum }</td>
  				<td>${po.remark }</td>
  			</tr>
  			
  			
  			</c:forEach>
  			
  			
  			</c:if>
  			
<%--   			<c:if test="${STATUS == 0 && nature == 1 && typesta != 1  }"> --%>
<!--   			<tr> -->
<!--   				<td></td> -->
<!--   				<td>3</td> -->
<%--   				<td colspan="2"><input style="width: 130px;" type="text" name= "LABOUR_RECEIPT" value="${LABOUR_RECEIPT }" id="LABOUR_RECEIPT" datatype="0,is_null,10" /> </td> --%>
<%--   				<td colspan="2"><input style="width: 130px;" type="text" name= "PART_RECEIPT" value="${PART_RECEIPT }" id="PART_RECEIPT" datatype="0,is_null,8" /></td> --%>
<!--   				<td ><input style="width: 70px;"   name= "AMOUNT_OF_MONEY" value="" /></td> -->
<!--   				<td><input style="width: 70px;"   name= "TAX_RATE_MONEY" value="" /></td> -->
<!--   				<td><input style="width: 70px;" name= "TAX_RATE_MONEY" value="" /></td> -->
<!--   				<td><input style="width: 70px;" name= "TAX_RATE_MONEY" value="" /></td> -->
<!--   			</tr> -->
  			
<%--   			</c:if> --%>
  			
  			
  			
  			
  			
<%--   			<c:if test="${STATUS == 0  }"> --%>
<!--   			<tr> -->
<!--   				<td>2</td> -->
<%--   				<td><input type="text" name= "LABOUR_RECEIPT" value="${LABOUR_RECEIPT}" id="LABOUR_RECEIPT1" datatype="0,is_null,10" /> </td> --%>
<%--   				<td><input type="text" name= "PART_RECEIPT" value="${PART_RECEIPT}" id="PART_RECEIPT1" datatype="0,is_null,8" /></td> --%>
<%--   				<td><input type="text" name= "AMOUNT_OF_MONEY" value="${AMOUNT_OF_MONEY}" id="AMOUNT_OF_MONEY" datatype="0,is_double" decimal="2"  /></td> --%>
<%--   				<td><input type="text" name= "TAX_RATE_MONEY" value="${TAX_RATE_MONEY}" id="TAX_RATE_MONEY" datatype="0,is_double" decimal="2" /></td> --%>
<%--   				<td>${mapCLAIM.AMOUNT_SUM}</td> --%>
<%--   				<td>${date}<input type="hidden" value="${date}" name= "creatdate" /></td> --%>
<%--   				<td>${mapdel.TAXPAYER_NATURE }</td> --%>
<!--   				<td>索赔费</td> -->
<!--   			</tr> -->
<%--   			</c:if> --%>
  			
  			
  			
  			
  			
  			<tr>
  				<td colspan="2">服务商索赔员：</td>
  				<td>${mapdel.SPY_MAN }</td>
  				<td colspan="3">服务商财务:</td>
  				<td>${mapdel.FINANCE_MANAGER_NAME }</td>
  				<td colspan="2">服务经理：</td>
  				<td>${mapdel.SER_MANAGER_NAME }</td>
  			</tr>
  			<tr  style="height: 156px;">
  				<td align="left" valign="bottom" style="border: 0px;">备注：</td>
  				<td colspan="6" align="right" valign="top" style="border: 0px;">单位名称：(${mapdel.DEALER_NAME})</td>
  				<td colspan="3" align="center" valign="top" style="border: 0px;">
  				(服务站盖发票专用章) 
  				<div style="height: 120px;"></div>
  				&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp; 月&nbsp;&nbsp;&nbsp;&nbsp; 日
  				</td>
  			</tr>
  			<tr>
  				<td colspan="2">北汽幻速签字确认：</td>
  				<td colspan="5">&nbsp;</td>
  				<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
  			</tr>
  			<tr>
  				<td>购</td>
  				<td colspan="2">纳税人识别号</td>
  				<td colspan="7">500117083059795</td>
  			</tr>
  			<tr>
  				<td>货</td>
  				<td colspan="2">地 址 电 话</td>
  				<td colspan="7">重庆市合川区土场镇三口村 023-42661188</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">开   户   行</td>
  				<td colspan="7">中信银行重庆九龙坡支行</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">账     号</td>
  				<td colspan="7">7422410182600052664</td>
  			</tr>
  			<tr>
  				<td colspan="10"></td>
  			</tr>
  			<tr>
  				<td>收</td>
  				<td colspan="2">单 位 名 称</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td>件</td>
  				<td colspan="2">收件人姓名<input type="hidden" id="STATUS" name="STATUS" />  </td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司索赔管理部</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">地址、电话</td>
  				<td colspan="7">重庆市北碚区土场镇三口村北汽银翔（研发中心二楼） 023-42668160</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">邮 政 编 码</td>
  				<td colspan="7">401520</td>
  			</tr>
  		</table>
			</div>
			</center>
</form>
</center>
<script type="text/javascript">

var date =document.getElementById('createD').value;
var d = date.substr(0,10);
document.getElementById('createDate').innerHTML=d;
</script>
</body>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
<tr>    
<td width="100%" height="25" colspan="3"><script language="javascript">    
  
function printsetup(){    
// 打印页面设置    
wb.execwb(8,1);    
}    
function printpreview(){    
// 打印页面预览      
wb.execwb(7,1);    
}      
function printit()    
{    
if (confirm('确定打印吗？')){    
  
wb.execwb(6,6)    
}    
}    
function remove(obj){
	   var tr=obj.parentNode.parentNode;
	   tr.parentNode.removeChild(tr);
	   tdLength--;
	   rowNum--;
};
</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
</tr>   
</table>  
</html>