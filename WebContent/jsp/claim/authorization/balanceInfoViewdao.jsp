<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<body onload="ffTableId();">
<center>
<form id="fm" name="fm">
			<div style="width:820px; text-align: center;" id="${balancePO1.yieldly}" align="center">
				<table width="100%">
					<tr>
						<td align="center" colspan="3"><span
							style="font-size: 20px; font-weight: bold;">昌河汽车(东安发动机)质量保证费用结算通知单</span>
						</td>
					</tr>
					<tr>
						<td class="boldTd" width="48%">服务站名称&nbsp;${dealerName}</td>
						<td class="boldTd" width="26%">结算编号&nbsp;${yldlist[0].REMARK}</td>
						<td class="boldTd" width="26%">上报批号:&nbsp;${sbNO}</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td class="normalTd" colspan="4" align="left">&nbsp; &nbsp;贵单位 ${timap.ENDDATE } 来结算单据 共 ${timap.APLCOUNT} 张（旧件  ${tiorder.IN_WARHOUSE_DATE }  验收）,其中正常单 ${tatol2} 张 强保单 ${tatol3} 张 售前单 ${tatol4} 张 </td>
					</tr>
					<tr>
						<td class="normalTd" colspan="4" align="left"> 外派单 ${tatol5} 张 特殊单 ${tatol6} 张  活动单 ${tatol7} 张 其它 ${timap.APLCOUNT-tatol2-tatol3-tatol4-tatol5-tatol6-tatol7} 张 &nbsp; &nbsp;&nbsp;本次结算情况如下：<span style="color: red;">开票金额为0的产地无需开票</td>
					</tr>
				</table>
				<table class="sepTab">
					<tr>
						<td rowspan="2"><center>产地</center></td>
						<td rowspan="2"><center>劳务费(元)</center></td>
						<td colspan="2"><center>材料费(元)</center></td>
						<td colspan="2"><center>金额总计(元)</center></td>
					</tr>
					<tr>
						<td>一般纳税人</td>
						<td>小规模纳税人</td>
						<td>一般纳税人</td>
						<td>小规模纳税人</td>
					</tr>
						<tr>
							<td>东安</td>
							<td>${allmap.LABOUR_AMOUNT}</td>
							<td>${allmap.PART_AMOUNT}</td>
							<td>${allmap.PART_AMOUNT01}</td>
							<td>${allmap.AMOUNT_SUM}</td>
							<td>${allmap.AMOUNT_SUM01}</td>
						</tr>
					<tr>
						<td>合计</td>
						<td>${allmap.LABOUR_AMOUNT}</td>
						<td>${allmap.PART_AMOUNT}</td>
						<td>${allmap.PART_AMOUNT01}</td>
						<td>${allmap.AMOUNT_SUM}</td>
						<td>${allmap.AMOUNT_SUM01}</td>
					</tr>
				</table>
				<table width="95%">
					<tr>
						<td class="normalTd" align="left">&nbsp;&nbsp;如贵单位无异议，请将劳务费与材料费分别开具增值税发票，在三张返</td>
					</tr>
					<tr>
						<td class="normalTd" align="left">回联上加盖财务章一并返回，东安公司将按贵单位发票上的名称，开户行和帐号办理转帐手续。</td>
					</tr>
				</table>
				<table class="midTab" width="100%">
						<tr>
							<td>开票信息</td>
							<td>哈尔滨东安</td>
						</tr>
						<tr>
							<td>全称</td>
							<td>哈尔滨东安汽车动力股份有限公司</td>
						</tr>
						<tr>
							<td>地址</td>
							<td>南岗区高新技术开发区13栋</td>
						</tr>
						<tr>
							<td>电话</td>
							<td>86573865</td>
						</tr>
						<tr>
							<td>开户行</td>
							<td>工商银行哈尔滨保国支行</td>
						</tr>
						<tr>
							<td>账号</td>
							<td>3500080209003508897</td>
						</tr>
						<tr>
							<td>税号</td>
							<td>23019871201745X</td>
						</tr>
				</table>
				<table width="100%" class="tab_printvoid" >
				<tr>
					<td   width="20%" height="40px;" style="font-size: 20px; text-align: center;">室主任</td>
					<td height="40px;" width="30%" >&nbsp;</td>
					<td height="40px;" style="font-size: 20px; text-align: center;">主管结算员</td>
					<td height="40px;" width="30%" >&nbsp;</td>
				</tr>
				</table>
				<table width="100%">
					<tr>
						<td class="normalTd" align="left">&nbsp; &nbsp;注：贵单位的材料费用将划至东安公司配件分公司,此单一式四联，服务站自存一联，返回东安动力三联 返回寄信地址：哈尔滨市平房区保国大街51号东安动力销售部(邮编150066)</td>
					</tr>
				</table>
			</div>
			<p/>
			<center>
			<div style="width: 820px;">
				<table width="100%">
					<tr>
						<td align="center"><span
							style="font-size: 20px; font-weight: bold;">昌河汽车(东安发动机)质量保证费用结算明细表</span>
						</td>
					</tr>
					<tr>
						<td class="boldTd">结算编号：&nbsp; &nbsp; ${yldlist[0].REMARK}</td>
					</tr>
					<tr>
						<td class="boldTd">上报批号:&nbsp; &nbsp; ${sbNO}</td>
					</tr>
				</table>
				<table width="100%" class="sepTab" id="sepTab" >
					<tr>
						<td  align="center" >费用类型</td>
						<td  align="center"  >劳务费(元)</td>
						<td  align="center" >材料费(元)</td>
					</tr>
					<c:forEach var="dealist" items="${dealist}">
					<tr>
						<td>
							<script type='text/javascript'>
						       var proCode=getItemValue('${dealist.CLAIM_TYPE}');
						       document.write(proCode) ;
						    </script>
						</td>
						<td>${dealist.LABOUR_AMOUNT}</td>
						<td>${dealist.PART_AMOUNT}</td>
					</tr>
					</c:forEach>
					<tr>
						<td>抵扣</td>
						<td>
						0
						</td>
						<td>
						<c:if test="${discount.DISCOUNT > 0}">-${discount.DISCOUNT}</c:if>
						<c:if test="${discount.DISCOUNT == 0 || discount.DISCOUNT == null}">0</c:if>
						</td>
					</tr>
					<tr>
						<td>运费</td>
						<td>${map.RETURN_AMOUNT}</td>
						<td>0</td>
					</tr>
					<tr>
						<td>正负激励</td>
						<td>${map.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${map.PLUS_MINUS_DATUM_SUM}</td>
					</tr>
					<tr>
						<td>特殊费用</td>
						<td>${map.SPECIAL_LABOUR_SUM}</td>
						<td>${map.SPECIAL_DATUM_SUM}</td>
					</tr>
					<tr>
						<td>营销处费用</td>
						<td>${map.SERVICE_TOTAL_AMOUNT_MARKET}</td>
						<td>0</td>
					</tr>
					<tr>
					
					<td>三包凭证补办</td>
					<td><c:if test="${map.PACKGE_CHANGE_SUM == 0}">0</c:if> <c:if test="${map.PACKGE_CHANGE_SUM != 0}">-${map.PACKGE_CHANGE_SUM }</c:if></td>
					<td>0</td>
					</tr>
					<tr>
					<td>条码补办费用</td>
					<td><c:if test="${map.BARCODE_SUM  == 0}">0</c:if> <c:if test="${map.BARCODE_SUM  != 0}">-${map.BARCODE_SUM }</c:if></td>
					<td>0</td>
					</tr>
					
					<tr>
					<td>行政扣款费用</td>
					<td><c:if test="${map.LABOUR_SUM < 0 }">${map.LABOUR_SUM }</c:if><c:if test="${map.LABOUR_SUM >= 0 }">0</c:if> </td>
					<td><c:if test="${map.DATUM_SUM < 0 }">${map.DATUM_SUM }</c:if><c:if test="${map.DATUM_SUM >= 0 }">0</c:if></td>
					</tr>
					
					<tr>
						<td>总计</td>
						<td>${map.ALL_LABOUR_AMOUNT}</td>
						<td>${map.ALL_PART_AMOUNT}</td>
					</tr>
					<tr>
				</table>
			</div>
			</center>
</form>
</center>
<script type="text/javascript">

function  ffTableId()
{
      	var ffTable = document.getElementById('sepTab');
      	var trs = ffTable.rows;
		var tds1 = trs[0].insertCell(-1);
		var tds2 = trs[0].insertCell(-1);
		var tds3 = trs[0].insertCell(-1);
		var tds4 = trs[1].insertCell(-1);
		var tds5 = trs[1].insertCell(-1);
		var tds6 = trs[1].insertCell(-1);
		tds1.innerHTML = '一级经销商代码';
		tds2.innerHTML = '劳务费';
		tds3.innerHTML = '材料费';
		tds4.innerHTML = '${map.DEALER_CODE}';
		tds5.innerHTML = '${onedealerl}';
		tds6.innerHTML = '${onedealerp}';
		var tds7 = trs[2].insertCell(-1);
		var tds8 = trs[2].insertCell(-1);
		var tds9 = trs[2].insertCell(-1);
		tds7.innerHTML = '二级经销商代码';
		tds8.innerHTML = '劳务费';
		tds9.innerHTML = '材料费';
		if(trs.length < ${fn:length(sendlist)}+3)
		{
		  for(var t = trs.length ;t < ${fn:length(sendlist)}+3 ; t++)
		  {
		     var tr = ffTable.insertRow(-1);
		     var tdd1 = tr.insertCell(-1);
		     var tdd2 = tr.insertCell(-1);
		     var tdd3 = tr.insertCell(-1);
		  }
		}
		
		
		var j = 3;
    <%     List<Map<String, Object>> sendlist = (List<Map<String, Object>>)request.getAttribute("sendlist");
           for(int i = 0 ; i < sendlist.size() ; i++)
           {
         %>
              var tds10 = trs[j].insertCell(-1);
            var tds11 = trs[j].insertCell(-1);
            var tds12 = trs[j].insertCell(-1);
            tds10.innerHTML = "<%= sendlist.get(i).get("SECOND_DEALER_CODE") %>";
            tds11.innerHTML = "<%= sendlist.get(i).get("SERVICE_CHARGE") %>"; 
            tds12.innerHTML = "<%= sendlist.get(i).get("MATERIAL_CHARGE") %>";
		    	  j++;
          <%
		       }
		%>
		
		
}


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

</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> </td>    
</tr>   
</table>  
</html>