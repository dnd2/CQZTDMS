<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%   
    //String code = "SC1022SAN.FAA.MY1";//条形码   
    	String contextPath = request.getContextPath();
%> 
<style>
<!--
  td{ word-wrap: break-word; word-break: normal; } 
-->
</style>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<%
int k = 0;
%>
<head>
<title></title>  
<script language="javascript">  
<!--   
NS4 = (document.layers) ? 1 : 0;   
visble_property_prefix = (NS4) ? "document.layers." : "";   
visble_property_suffix = (NS4) ? ".visibility" : ".style.display";   
visble_property_true = (NS4) ? "show" : "block";   
visble_property_false = (NS4) ? "hide" : "none";   
visble_property_printview = visble_property_prefix + "viewpanel" + visble_property_suffix;  

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

}
var settings = {
        output:"css",
        bgColor: "#FFFFFF",
        color: "#000000",
        barWidth: "1",
        barHeight: "40",
        moduleSize: "1",
        posX: "1",
        posY: "1",
        addQuietZone: true,
        marginHRI: "1",
        showHRI: true
   };
//-->  
</script>  
  <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
   <!--media=print 这个属性在打印时有效 有些不想打印出来的分页打印的都可以应用这类样式进行控制 在非打印时是无效的(可从打印预览中看到效果)-->
   <style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    p {
     page-break-after: always;
    }
   </style>
   
   <!-- 这个是普通样式 -->
   
   <script type="text/javascript">
    var hkey_root,hkey_path,hkey_key   
    hkey_root="HKEY_CURRENT_USER";
    hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
      //这个是用来设置打印页眉页脚的，你可以设置为空或者其它
      try{   
            var RegWsh = new ActiveXObject("WScript.Shell"); 
              
            hkey_key="header";
            RegWsh.RegWrite(hkey_root+hk“”,"");
            
            hkey_key="footer";
            RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
            
      }catch(e){
      MyAlert(e.description());
      }
     </script>
</head>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
   <script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-barcode-2.0.2.min.js"></script>

<body>
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT> 

<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" name="balance_yieldly"/>
<div id= 'tab'>
<table align="center" width="800px">
<br/>
<br/>
<tr>
  <td align="center" colspan="2">
	<span style="font-size: 26px;">付&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;款&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;凭&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;证</span>
	<hr color="black" width="45%">
	</td>
</tr>
<tr>
	<td align="center" colspan="2">
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  ${paymentPO.labourReceipt}
	</td>
</tr>
<tr>
  <td align="left" style="font-size: 14px;">&nbsp;贷方科目:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 结算编号:&nbsp;&nbsp; ${map.REMARK}</td>
  <td align="right" style="font-size: 14px;">NO.&nbsp;${NO}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${CreatDate}&nbsp;</td>
</tr>
</table>
<table  align="center" class="tab_printsep">
	<tr>
	 <td width="24%" colspan="2">借方科目</td>
	 <td width="36%" rowspan="2" colspan="2">申请借（付）款内容</td>
	 <td width="20%" colspan="12" >申请金额</td>
	 <td width="20%" colspan="12">批准付款金额</td>
	</tr>
	<tr>
	<td width="12%" >一级账户</td>
	<td width="12%" >二级账户</td>
	<td >十</td>
	<td >亿</td>
	<td >千</td>
	<th style="border-width: 1px; border-right-width: 2px;">百</th>
	<td >十</td>
	<td >万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th><td>角</td><td>分</td>
	<td>十</td><td>亿</td><td>千</td><th style="border-width: 1px; border-right-width: 2px;">百</th><td>十</td><td>万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th><td>角</td><td>分</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="gs" colspan="2"><div style="text-align: left;">&nbsp;支付：${dealerPO.dealerShortname}</div></td>
	<td id="a">${p1}</td><td id="b">${p2}</td><td id="c">${p3}</td><th id="d" style="border-width: 1px; border-right-width: 2px;">${p4}</th><td id="e">${p5}</td><td id="f">${p6}</td>
	<th id="g" style="border-width: 1px; border-right-width: 2px;">${p7}</th><td id="h">${p8}</td><td id="i">${p9}</td><th id="j" style="border-width: 1px; border-right-width: 2px;">${p10}</th><td id="k">${p11}</td><td id="l">${p12}</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="dz" colspan="2"><div style="text-align: left;">&nbsp;三包费&nbsp;&nbsp;&nbsp;&nbsp; 所在省份：${REGION_NAME}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="yh" colspan="2"><div style="text-align: left;">&nbsp;开户行：${definePO.bank}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="zh" colspan="2"><div style="text-align: left;">&nbsp;账号：${definePO.account}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
</table>
<center>
	<table align="center" width="800px" style="border-collapse: collapse;" frame="void">
		<tr height="60px">
			<td width="30%" style="border:1px solid black; border-top-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;领导批示：</span></td>
			<td width="36%" style="border:1px solid black; border-top-color: white; border-right-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;借款单位：销售公司&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;负责人</span></td>
			<td width="20%" style="border:1px solid black; border-top-color: white; border-right-color: white;border-left-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;支款人</span></td>
			<td width="14%" style="border:1px solid black; border-top-color: white; border-left-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;证件号（&nbsp;&nbsp;&nbsp;&nbsp;）</span></td>
		</tr>
	</table>
</center>
<center>
	<table align="center" width="800px">
		<tr>
			<td colspan="5" height="35px"><span style="font-size: 20px; text-align: left; width: 100%">&nbsp;&nbsp;&nbsp;&nbsp;共计人民币（大写）：</span></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td width="22%" style="font-size: 14px;">&nbsp;会计主管：</td>
			<td width="21%" style="font-size: 14px;">&nbsp;出纳员：</td>
			<td width="18%" style="font-size: 14px;">&nbsp;组长：</td>
			<td width="18%" style="font-size: 14px;">&nbsp;复核：</td>
			<td width="21%" style="font-size: 14px;">&nbsp;会计员：</td>
		</tr>
	</table>
</center>
</div>
<c:if test="${paymentPO.paymentType == '94111001'}"><p/></c:if>
<div>
<br/>
<br/>
<table align="center" width="800px">
<tr>
  <td align="center" colspan="2">
	<span style="font-size: 26px;">转&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;款&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;凭&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;证</span>
	<hr color="black" width="45%">
	</td>
</tr>
<tr>
	<td align="center" colspan="2">
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	${paymentPO.partReceipt}  <c:if test="${paymentPO.paymentType == '94111002'}">（全转配件）</c:if>
	</td>
</tr>
<tr>
  <td align="left" style="font-size: 14px;">&nbsp;贷方科目:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 结算编号:&nbsp;&nbsp; ${map.REMARK}</td>
  <td align="right" style="font-size: 14px;">NO.&nbsp;${NO}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${CreatDate}&nbsp;</td>
</tr>
</table>

<table id="tab1" align="center" width="100%" class="tab_printsep" border='0'>
	<tr>
	 <td width="24%" colspan="2">借方科目</td>
	 <td width="36%" rowspan="2" colspan="2">申请借（付）款内容</td>
	 <td width="20%" colspan="12" >申请金额</td>
	 <td width="20%" colspan="12">批准付款金额</td>
	</tr>
	<tr>
	<td width="12%">一级账户</td>
	<td width="12%">二级账户</td>
	<td>十</td><td>亿</td><td>千</td><th style="border-width: 1px; border-right-width: 2px;">百</th><td>十</td><td>万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th><td>角</td><td>分</td>
	<td>十</td><td>亿</td><td>千</td><th style="border-width: 1px; border-right-width: 2px;">百</<th><td>十</td><td>万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</<th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th></td><td>角</td><td>分</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="gs1" colspan="2"><div style="text-align: left;">&nbsp;支付：${ dealerPO.dealerShortname}</div></td>
	<td id="1">${t1}</td><td id="2">${t2}</td><td id="3">${t3}</td><th id="4" style="border-width: 1px; border-right-width: 2px;">${t4}</td><td id="5">${t5}</td><td id="6">${t6}</td>
	<th id="7" style="border-width: 1px; border-right-width: 2px;">${t7}</th><td id="8">${t8}</td><td id="9">${t9}</td><th id="10" style="border-width: 1px; border-right-width: 2px;">${t10}</th><td id="11">${t11}</td><td id="12">${t12}</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="dz1" colspan="2"><div style="text-align: left;">&nbsp;转配件处&nbsp;&nbsp;&nbsp;&nbsp; 所在省份：${REGION_NAME}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="yh1" colspan="2"><div style="text-align: left;">&nbsp;三包费</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="zh1" colspan="2"></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
</table>
<table id="tab2"  align="center" width="100%" class="tab_printsep" border='0'>
	<tr>
	 <td width="24%" colspan="2">借方科目</td>
	 <td width="36%" rowspan="2" colspan="2">申请借（付）款内容</td>
	 <td width="20%" colspan="12" >申请金额</td>
	 <td width="20%" colspan="12">批准付款金额</td>
	</tr>
	<tr>
	<td width="12%">一级账户</td>
	<td width="12%">二级账户</td>
	<td>十</td><td>亿</td><td>千</td><th style="border-width: 1px; border-right-width: 2px;">百</th><td>十</td><td>万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th><td>角</td><td>分</td>
	<td>十</td><td>亿</td><td>千</td><th style="border-width: 1px; border-right-width: 2px;">百</<th><td>十</td><td>万</td>
	<th style="border-width: 1px; border-right-width: 2px;">千</<th><td>百</td><td>十</td><th style="border-width: 1px; border-right-width: 2px;">元</th></td><td>角</td><td>分</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="gs1" colspan="2"><div style="text-align: left;">&nbsp;支付：${ dealerPO.dealerShortname}</div></td>
	<td id="1">${m1}</td><td id="2">${m2}</td><td id="3">${m3}</td><th id="4" style="border-width: 1px; border-right-width: 2px;">${m4}</td><td id="5">${m5}</td><td id="6">${m6}</td>
	<th id="7" style="border-width: 1px; border-right-width: 2px;">${m7}</th><td id="8">${m8}</td><td id="9">${m9}</td><th id="10" style="border-width: 1px; border-right-width: 2px;">${m10}</th><td id="11">${m11}</td><td id="12">${m12}</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="dz1" colspan="2"><div style="text-align: left;">&nbsp;转配件处&nbsp;&nbsp;&nbsp;&nbsp; 所在省份：${REGION_NAME}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="yh1" colspan="2"><div style="text-align: left;">&nbsp;三包费</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="zh1" colspan="2"></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	<th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td><th style="border-width: 1px; border-right-width: 2px;">&nbsp;</<th><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
</table>
<center>
	<table align="center" width="800px" style="border-collapse: collapse;" frame="void">
		<tr height="60px">
			<td width="30%" style="border:1px solid black; border-top-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;领导批示：</span></td>
			<td width="36%" style="border:1px solid black; border-top-color: white; border-right-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;借款单位：销售公司&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;负责人</span></td>
			<td width="20%" style="border:1px solid black; border-top-color: white; border-right-color: white; border-left-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;支款人</span></td>
			<td width="14%" style="border:1px solid black; border-top-color: white; border-left-color: white;"><span style="font-size: 14px; text-align: left;">&nbsp;&nbsp;证件号（&nbsp;&nbsp;&nbsp;&nbsp;）</span></td>
		</tr>
	</table>
</center>
<center>
	<table align="center" width="800px">
		<tr>
			<td colspan="5" height="35px"><span style="font-size: 20px; text-align: left; width: 100%">&nbsp;&nbsp;&nbsp;&nbsp;共计人民币（大写）：</span></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td width="22%" style="font-size: 14px;">&nbsp;会计主管：</td>
			<td width="21%" style="font-size: 14px;">&nbsp;出纳员：</td>
			<td width="18%" style="font-size: 14px;">&nbsp;组长：</td>
			<td width="18%" style="font-size: 14px;">&nbsp;复核：</td>
			<td width="21%" style="font-size: 14px;">&nbsp;会计员：</td>
		</tr>
	</table>
	<br/>
</center>
</div>
<center>
	 <div class="Noprint"> 
	<input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
	<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
	<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
	</div>
</center>

<script type="text/javascript">
	
	function baochu()
	{
		var balance_oder = document.getElementById('balance_oder').value;
		var labour_receipt = document.getElementById('labour_receipt').value;
		var part_receipt = document.getElementById('part_receipt').value;
		if(balance_oder == 0 || labour_receipt == 0 || part_receipt == 0)
		{
			 MyAlert("请填写好 结算单号 劳务费发票 材料费发票 ！");
		}else
		{
			 MyConfirm("是否确认保存？",update);
		}
	}
	
	function update()
	{
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/updatePayMent.json?status=0";
		makeNomalFormCall(url,updateBack,'fm','');
	}
	
	function updateBack(json)
	{
		MyAlertForFun("保存成功！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	function commit()
	{
		var balance_oder = document.getElementById('balance_oder').value;
		var labour_receipt = document.getElementById('labour_receipt').value;
		var part_receipt = document.getElementById('part_receipt').value;
		if(balance_oder == 0 || labour_receipt == 0 || part_receipt == 0)
		{
			 MyAlert("请填写好 结算单号 劳务费发票 材料费发票 ！");
		}else
		{
			 MyConfirm("是否确认保存？",insert);
		}
	}
	
	function insert()
	{
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/updatePayMent.json?status=1";
		makeNomalFormCall(url,insertBack,'fm','');
	}
	
	function insertBack()
	{
		MyAlertForFun("移交上级！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	function updateBack(json)
	{
		MyAlertForFun("保存成功！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	
	function doCusChange(value)
	{
		if(value == '94111002')
		{
			document.getElementById('tab').style.display = 'none';
			document.getElementById('tab1').style.display = 'none';
			document.getElementById('tab2').style.display = '';
		}else if(value == '94111001'){
			document.getElementById('tab').style.display = '';
			document.getElementById('tab1').style.display = '';
			document.getElementById('tab2').style.display = 'none';
		}
	}
	doCusChange('${paymentPO.paymentType}');
</script>
</form>
</body>
</html>