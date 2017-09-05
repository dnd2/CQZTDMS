<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />


<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<head>
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
<style type="text/css">
table.sale_sepTab {
	border-collapse: collapse;
	border: 1px solid black;
	width: 100%;
}

table.sale_sepTab td,table.sale_sepTab th {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 14px;
	height: 16px;
}

td.saleSepTd {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 14px;
	height: 16px;
	text-align: left;
	font-weight: bold;
}

td.saleHTd {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 14px;
	height: 30px;
	text-align: left;
	font-weight: bold;
}

td.saleCenterTd {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 14px;
	height: 16px;
	text-align: center;
	font-weight: bold;
}

td.saleTd {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 14px;
	height: 16px;
	text-align: left;
}

span.hightTd {
	font-size: 20px;
	font-family: '宋体';
	font-weight: bold;
	height: 30px;
}
</style>
<title></title>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> valueMap = (Map<String, Object>) request
			.getAttribute("valueMap");
	List<Map<String, Object>> valueList = (List<Map<String, Object>>) request
			.getAttribute("valueList");
%>
</head>
<body>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><object
					classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0"
					id="wb" name="wb" width="3"></object>
				<div id="kpr" align="center">
					<input class="ipt" type="button" value="打印"
						onclick="kpr.style.display='none';javascript:printit();" /> <input
						class="ipt" type="button" value="打印页面设置"
						onclick="javascript:printsetup();" /> <input class="ipt"
						type="button" value="打印预览"
						onclick="kpr.style.display='none';javascript:printpreview();" />
				</div></td>
		</tr>
	</table>
<center>
  <input type="hidden" id="wayBillId" value="${BONO}"/>
  <% int i = 0;%>
  <c:forEach items="${valueList}" var="item" varStatus="status">
  <%if(++i < valueList.size()) {
	 %>
	<div style="width: 820px; page-break-after: always;" >
  <%} else {%>
  <div style="width: 820px;">
  <%} %>
    <table border="0" cellpadding="0" cellspacing="0" class="sale_sepTab">
      <tr>
        <td colspan="8">潍柴（重庆）汽车有限公司</td>
      </tr>
      <tr>
        <td colspan="8"><span class="hightTd">商品车交接及点检单</span> </td>
      </tr>
      <tr>
        <td colspan="4" class="saleTd"> 托运单位：潍柴（重庆）汽车有限公司 </td>
        <td colspan="2" class="saleTd">发运日期：<script type="text/javascript">
							    var d = new Date();
							    var year = d.getFullYear();
							    var Month = d.getMonth()+1;
							    var day = d.getDate();
								document.write(year+"年&nbsp;"+Month+"月&nbsp;"+day+"日");								
							</script></td>
		<td colspan="2" class="saleTd">编号：
          <c:out value="${item.PASS_NO }"></c:out></td>
      </tr>
      <tr>
        <td colspan="4" class="saleTd">发运计划编号：<c:out value="${item.BILL_NO }" /></td>
        <td colspan="2" class="saleTd">销售订单编号：<c:out value="${item.ORDER_NO}" /></td>
        <td colspan="2" class="saleTd">装运道号：<c:out value="${item.LOADS}" />&nbsp;运输方式：
          <c:out value="${item.SENDTYPE }" />&nbsp;车牌号：
          <c:out value="${item.CAR_NO }" /></td>
      </tr>
      <tr >
        <td colspan="4" class="saleSepTd">VIN：<c:out value="${item.VIN }" />&nbsp;
                           发动机号：<c:out value="${item.ENGINE_NO }" /></td>
        <td colspan="3" class="saleSepTd"> 车型：<c:out value="${item.MODEL_NAME }" />&nbsp;配置：<c:out value="${item.PACKAGE_NAME }" /></td>
        <td class="saleSepTd">颜色：<c:out value="${item.COLOR_NAME }" /></td>
      </tr>
      <tr>
        <td colspan="4" class="saleSepTd" bgcolor="#99CCFF">随车搭载品名称</td>
        <td colspan="2" class="saleCenterTd" bgcolor="#99CCFF">重点部位点检</td>
        <td class="saleCenterTd" bgcolor="#99CCFF">装车前点检</td>
        <td class="saleCenterTd" bgcolor="#99CCFF">接收点检</td>
      </tr>
      <tr>
        <td colspan="2" rowspan="2" width="20%"></td>
        <td width="10%" class="saleCenterTd">装车前点检</td>
        <td width="10%" class="saleCenterTd">接收点检</td>
        <td colspan="2" width="26%">前保险杠</td>
        <td width="17%"></td>
        <td width="17%"></td>
      </tr>
      <tr >
        <td>有/无</td>
        <td>有/无</td>
        <td colspan="2" class="saleTd">右前大灯及右后视镜</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td width="5%">1</td>
        <td width="15%">备胎及盖板</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">右前门及翼子板</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>2</td>
        <td>千斤顶</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">右后门、侧围及右后灯</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>3</td>
        <td>千斤顶摇把</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">后背门</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>4</td>
        <td>轮胎扳手</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">后保险杠</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>5</td>
        <td>三角警示牌</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">左后门、侧围及左后灯</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>6</td>
        <td>点烟器</td>
        <td>√</td>
        <td></td>
        <td colspan="2" class="saleTd">左前门及翼子板</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td>7</td>
        <td>烟灰缸</td>
        <td>×</td>
        <td>×</td>
        <td colspan="2" class="saleTd">左前大灯及左后视镜</td>
        <td></td>
        <td></td>    
      </tr>
      <tr>       
        <td>8</td>
        <td>钥匙及遥控器</td> 
        <td>√</td>
        <td></td>       
        <td colspan="2" class="saleTd">其他位置</td>
        <td></td>
        <td></td>      
      </tr>
      <tr>        
        <td>9</td>
        <td>随车资料</td>  
        <td>√</td>
        <td></td>       
        <td colspan="4" class="saleTd">损伤描述：1划伤.2擦伤.3掉漆.4磕伤.5凹陷.6破损.7污损.8裂痕.</td>
      </tr>
      <tr>        
        <td colspan="3" class="saleSepTd">经销商名称：<br/>
          <c:out value="${item.DEALER_NAME }"/></td>
        <td colspan="3" rowspan="2" class="saleSepTd">接车地址：<br/>
          <c:out value="${item.ADDRESS }"/></td>
        <td colspan="2" rowspan="2" class="saleSepTd">接车联系人：<c:out value="${item.LINK_MAN }"/>
          <br /> 接车联系电话：<br /><c:out value="${item.TEL }"/>&nbsp;<c:out value="${item.MOBILE_PHONE}"/>
          </td>
      </tr>
      <tr >        
        <td colspan="3" class="saleSepTd">承运商名称：
          <c:out value="${logiName }"/></td>
      </tr>
      <tr>        
        <td colspan="5" class="saleSepTd">经销商接车人员描述： <br /><br />
          经销商接车人员签字： <br />
          经销商接车盖章处：<br />
          日期： </td>
        <td colspan="3" class="saleSepTd">承运商司机交车描述：<br /> <br /><br />
          承运商司机签字：<br />
          日期： </td>
      </tr>
      <tr>        
        <td colspan="5" class="saleSepTd">委托方经办人签字：</td>
        <td colspan="3" class="saleSepTd">承运商经办人签字：</td>
      </tr>
      <tr>
        <td colspan="8" class="saleTd">交付要求：请在 月<span >&nbsp; </span>日到达；到达<span >&nbsp;&nbsp;&nbsp; </span>前务必与接车人联系落实实际交车地址，切记！ </td>
      </tr>
    </table>
    </div>
  </c:forEach>
</center>
	
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
   }catch(e){MyAlert(e);}    
}
</script>
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
			var wayBillId=document.getElementById("wayBillId").value;
			var url = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/updateWayBill.json";
	    	makeCall(url,updatePint,{wayBillId:wayBillId});
		}    
	}
	function updatePint(json){
		if(json.returnValue==1){
			wb.execwb(6,6)  ;
		}else if(json.returnValue==3){
			MyAlert("打印异常，请重试！");
		}
		
	}
</script>
</body>
</html>