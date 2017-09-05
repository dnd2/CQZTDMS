<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="/WEB-INF/tld/change.tld" prefix="change"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style>
@media print{
INPUT {display:none}
}
</style>
<style type="text/css">
#myTable {
   
    border-collapse:collapse;
    }
#myTable td {
               border: 1px #000 solid;
               }

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
<title>结算清单打印</title>
</head>
<div id="kpr" align="center" class="Noprint">     
<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"></div>

<body>

<div style="font-size: 20px;" align="center">服务站结算费用汇总单</div>
			<div align="center">
				<table width="900px" class="tab_edit" id="tab1" >
  			<tr height="40px;">
  				<td colspan="5">服务站号: ${ps.DEALER_CODE }</td>
  				<td colspan="3">结算单号:${ps.BALANCE_NO }<input id="balanecNo" type="hidden" name="BALANCE_ODER"  value="${ps.BALANCE_NO}" />  </td>
  				<td colspan="2">服务站电话:${ps.PHONE } </td>
  			</tr>
  			<tr>
  				<td colspan="7" style="border-right: 0px;" align="left">重庆北汽幻速汽车销售有限公司 :    </td>
  				<td colspan="3" style="border-left: 0px;" align="right">一式三联，两联随发票寄出</td>
  			</tr>
  			<tr>
<%--   				<td align="center"><a style="color:red" href="#" style="text-decoration: none;cursor: pointer;" onclick="AppprintAll('${mapCLAIM.REMARK}')" >[一键打印]</a></td> --%>
  				<td colspan="10" align="left" >&nbsp;&nbsp;&nbsp;&nbsp;我站的保养、索赔单据等，经贵公司审核，结算情况如下:</td>
  			</tr>
  			<tr>
  				<td align="center">项目名称</td>
  				<td align="center" colspan="2">保养台次</td>
  				<td align="center" colspan="2">PDI台次</td>
  				<td align="center" colspan="2">索赔维修台次</td>
  				<td align="center" colspan="1" >活动台次</td>
  				<td align="center" colspan="2">合计台次</td>
  			</tr>
  			<tr>
  				<td align="center">台次（台）</td>
  				<td align="center" colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${by_num}','${by_ids}');" >${by_num}</a></td>
  				<td align="center" colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${pdi_num}','${pdi_ids}');" >${pdi_num}</a></td>
  				<td align="center" colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${z_num}','${z_ids}');" >${z_num}</a></td>
  				<td align="center"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${fw_num}','${fw_ids}');" >${fw_num}</a></td>
  				<td align="center" colspan="2">${hj_num}</td>
  			</tr>
  			<tr>
  		  		<td align="center">项目名称:</td>
  				<td align="center" colspan="2">pdi费</td>
  				<td align="center" colspan="2">保养费</td>
  				<td align="center" colspan="2">活动费</td>
  				<td align="center">外出费</td>
  				<td align="center">正负激励</td>
  				<td align="center">善于索赔</td>
  			</tr>
  			<tr>
  				<td align="center">金额（元）:</td>
  				<td align="center" colspan="2">${ps.MARKET_AMOUNT }</td>
  				<td align="center" colspan="2">${ps.FREE_AMOUNT }</td>
  				<td align="center" colspan="2">${ps.SERVICE_FIXED_AMOUNT }</td>
  				<td align="center">${ps.SPEOUTFEE_AMOUNT }</td>
  				<td align="center">${ps.APPEND_LABOUR_AMOUNT }</td>
  				<td align="center">${ps.APPEND_AMOUNT }</td>
  			</tr>
			<tr>
  		  		<td align="center">项目名称:</td>
  				<td align="center" colspan="2">工时费</td>
  				<td align="center" colspan="2">配件费</td>				
  				<td align="center" colspan="2">运费</td>
  				<td align="center">二次抵扣</td>
  				<td align="center">上次行政扣款</td>
  				<td align="center">本次行政扣款</td>
  			</tr>
  			<tr>
  				<td align="center">金额（元）:</td>
  				<td align="center" colspan="2">${ps.LABOUR_AMOUNT }</td>
  				<td align="center" colspan="2">${ps.PART_AMOUNT }</td>
  				<td align="center" colspan="2">${ps.RETURN_AMOUNT }</td>
  				<td align="center">${ps.AMOUNT_SUM }</td>
  				<td align="center">${ps.ADMIN_DEDUCT }</td>
  				<td align="center">${ps.FINANCIAL_DEDUCT }</td>
  			</tr>
  			
  			<tr>
  				<td align="center" colspan="10" id="fee_sum">
  					费用合计:${ps.NOTE_AMOUNT }
  				</td>
  			</tr>
  			</table><br>
  			
  			<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr>
  				<td colspan="10" align="left">发票开票信息如下:</td>
  			</tr>
    		<tr>
  				<td align="center" style="width: 15%">发票批号</td>
  				<td align="center" style="width: 15%">发票号</td>
  				<td align="center" style="width: 15%">金额</td>
  				<td align="center" style="width: 15%">税额</td>
  				<td align="center" style="width: 15%">合计</td>
  				<td align="center" style="width: 25%">备注</td>  
  			</tr>			
			<c:forEach items="${listBill}" var="ps"> 
			<tr>
				<td align="center" style="width: 15%">${ps.PC_NO }</td>
				<td align="center" style="width: 15%">${ps.BILL_NO }</td>
     			<td align="center" style="width: 15%">${ps.MONEY }</td>	
     			<td align="center" style="width: 15%">${ps.TAX_MONEY }</td>
     			<td align="center" style="width: 15%">${ps.TOTAL }</td>	     					
				<td align="center" style="width: 25%">${ps.REMARK }</td>	     					
			</tr>
			</c:forEach>
			<tr>
  				<td align="left" colspan="2">服务商索赔员:</td>
  				<td align="left" colspan="2">服务商财务:</td>
  				<td align="left"colspan="2">服务经理:</td>
  			</tr>		
  			<tr  style="height: 86px;">
  				<td align="left" valign="bottom" style="border: 0px;">备注:</td>
  				<td colspan="2" align="right" valign="top" style="border: 0px;"></td>
  				<td colspan="3" align="right" valign="top" style="border-left: 0px;">
  				单位名称:阿萨德法师打发手动阀啊阿萨德发<%-- ${mapdel.DEALER_NAME} --%>
  				(服务站盖发票专用章)
  				<div style="height: 60px;"></div>
  				&nbsp;&nbsp;&nbsp;&nbsp;年
  				&nbsp;&nbsp;&nbsp;&nbsp;月
  				&nbsp;&nbsp;&nbsp;&nbsp;日
  				</td>
  			</tr>
  			<tr>
  				<td colspan="3">北汽幻速签字确认:</td>
  				<td align="right" colspan="7">
  				&nbsp;&nbsp;&nbsp;&nbsp;年
  				&nbsp;&nbsp;&nbsp;&nbsp;月
  				&nbsp;&nbsp;&nbsp;&nbsp;日</td>
  			</tr>
  			<tr>
  				<td align="center" rowspan="5">购货单位</td>
  				<td colspan="2">购货单位</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td colspan="2">纳税人识别号</td>
  				<td colspan="7">500117083059795</td>
  			</tr>
  			<tr>
  				<td colspan="2">地 址 电 话</td>
  				<td colspan="7">重庆市合川区土场镇三口村 023-42661188</td>
  			</tr>
  			<tr>
  				<td colspan="2">开   户   行</td>
  				<td colspan="7">中信银行重庆九龙坡支行</td>
  			</tr>
  			<tr>
  				<td colspan="2">账     号</td>
  				<td colspan="7">7422410182600052664</td>
  			</tr>
  			<tr>
  				<td colspan="10"></td>
  			</tr>
  			<tr>
  				<td align="center" rowspan="3">收件单位</td>
  				
  				<td colspan="2">单 位 名 称</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td colspan="2">收件人姓名<input type="hidden" id="STATUS" name="STATUS" />  </td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司索赔管理部</td>
  			</tr>
  			<tr>
  				<td colspan="2">地址、电话</td>
  				<td colspan="7">重庆市北碚区土场镇三口村北汽银翔（研发中心二楼） 023-42668160</td>
  			</tr>
  			<tr>
  				<td colspan="3">邮 政 编 码</td>
  				<td colspan="7">401520</td>
  			</tr>
  		</table>
			</div>

<%-- <p>
  <c:forEach var="allList" items="${allList}" varStatus="j" >
  <c:set var="AppList"  value="${allList[0]}" />
  <c:if test="${fn:length(AppList) > 0}">
  <c:set var="AppListTemp"  value="${allList[1][0]}" />
  <c:set var="name"  value="${nameList[j.index]}" />
<div>
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="40" width="100%" style="font-size: 24px; font-weight: bold;"><span class="STYLE1">
			北汽幻速索赔${name}清单
		</span></td>
	</tr>
    <tr>
      <td colspan="8">
      <table class="tab_printsep" id="myTable" align="center" width="800px">
          <tr align="center">
            <td width="5%" align="center">序号</td>
            <td width="15%" align="center">索赔单号</td>
            <td width="15%" align="center">索赔类型</td>
            <td width="15%" align="center">总费用</td>
            <td width="15%" align="center">材料费</td>
            <td width="15%" align="center">工时费</td>
            <td width="15%" align="center">保养费</td> 
            <td width="15%" align="center"nowrap="true">辅料费</td>
           <td width="15%" align="center" nowrap="true">补偿费</td>
            <td width="15%" align="center" nowrap="true">活动或外派费</td>
         </tr>
            <% int i = 1;  %>
            <c:set var="pageSize"  value="10000" />
            <c:forEach var="AppList" items="${AppList}" varStatus="status">
              <tr   align="center">
                <td align="center"><%= i %></td>
                 <td align="center" nowrap="true">${AppList.CLAIM_NO}</td>
                <td align="center">
                	<change:change type="1066" val="${AppList.CLAIM_TYPE}"/>
                	<c:if test="${AppList.CLAIM_TYPE==11911001}">
                     	<change:change type="1191" val="${AppList.CLAIM_TYPE}"/>
                	</c:if>
               </td>
                <td align="center">${AppList.BALANCE_AMOUNT}</td>
                <td align="center">${AppList.BALANCE_PART_AMOUNT}</td>
                <td align="center" >${AppList.BALANCE_LABOUR_AMOUNT}</td>
                <td align="center" >${AppList.FREE_M_PRICE}</td>
               <td align="center" >${AppList.ACCESSORIES_PRICE}</td>
                <td align="center" >${AppList.COMPENSATION_MONEY}</td>
                <td align="center" >${AppList.BALANCE_NETITEM_AMOUNT}</td>
               <% i++; %>
                </td>
              </tr>
            </c:forEach>
	              <tr align="center">
	    	<td width="35%" align="center" colspan="3" nowrap="true">合计</td>
	    	<td width="15%" align="center">${AppListTemp.BALANCE_AMOUNT_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_PART_AMOUNT_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_LABOUR_AMOUNT_COUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.FREE_M_PRICE_COUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.ACCESSORIES_PRICE_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.COMPENSATION_MONEY_COUNT}</td> 
	        <td width="15%" align="center">${AppListTemp.BALANCE_NETITEM_AMOUNT_COUNT}</td>
        </table></td>
    </tr>
        </table>
        </td>
    </tr>
</table>
</div>
</c:if>
</c:forEach>
</div>


<c:if test="${!empty sec}">



<div>
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="40" width="100%" style="font-size: 24px; font-weight: bold;"><span class="STYLE1">
			北汽幻速索赔二次入库清单
		</span></td>
	</tr>
    <tr>
      <td colspan="8">
      <table class="tab_printsep" id="myTable" align="center" width="800px">
          <tr align="center">
            <td width="5%" nowrap="true" align="center">序号</td>
            <td width="15%" align="center" nowrap="true">经销商名称</td>
            <td width="15%" align="center">经销商代码</td>
            <td width="15%" align="center">配件代码</td>
            <td width="15%" align="center">是否主因件</td>
            <td width="15%" align="center">补偿费</td>
            <td width="15%" align="center">结算编号</td> 
            <td width="15%" align="center"nowrap="true">备注</td>
           <td width="15%" align="center" nowrap="true">申请日期</td>
            <td width="15%" align="center" nowrap="true">索赔单号</td>
         </tr>
            <c:set var="pageSize"  value="10000" />
            <c:forEach var="sec" items="${seclist}" varStatus="status">
              <tr   align="center">
                <td align="center"> ${status.index+1 }</td>
                 <td align="center" nowrap="true">${sec.DEALER_SHORTNAME}</td>
                <td align="center">${sec.DEALER_CODE}</td>
                <td align="center">${sec.PART_CODE}</td>
                <td align="center">${sec.IS_MAIN_CODE}</td>
                <td align="center" >${sec.AMOUNT}</td>
                <td align="center" >${sec.BALANCE_NO}</td>
               <td align="center" >${sec.REMARK}</td>
                <td align="center" >${sec.CREATE_DATE}</td>
                <td align="center" >${sec.CLAIM_NO}</td>
                </td>
              </tr>
            </c:forEach>
	              <tr align="center">
	    	<td width="35%" align="center" colspan="3" nowrap="true">合计</td>
	    	<td width="15%" align="center">${AppListTemp.BALANCE_AMOUNT_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_PART_AMOUNT_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_LABOUR_AMOUNT_COUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.FREE_M_PRICE_COUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.ACCESSORIES_PRICE_COUNT}</td>
	        <td width="15%" align="center">${AppListTemp.COMPENSATION_MONEY_COUNT}</td> 
	        <td width="15%" align="center">${AppListTemp.BALANCE_NETITEM_AMOUNT_COUNT}</td>
        </table></td>
    </tr>
        </table>
        </td>
    </tr>
</table>
</div>

</c:if>

 --%>
<script type="text/javascript">

var date =document.getElementById('createD').value;
var d = date.substr(0,16);
document.getElementById('createDate').innerHTML=d;
</script>
</body>
<script language="javascript">    
  
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
  
wb.execwb(6,6);
}    
}    
</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
</html>