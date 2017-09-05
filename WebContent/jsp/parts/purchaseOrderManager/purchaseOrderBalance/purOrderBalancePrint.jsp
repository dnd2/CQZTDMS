<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%   
    //String code = "SC1022SAN.FAA.MY1";//条形码内容   
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
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
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
	document.getElementById("error").style.display="none";
	wb.execwb(7,1);
	document.getElementById("error").style.display="";
}      
function printit(){    
	if (confirm('确定打印吗？')){
		document.getElementById("error").style.display="none";
	    wb.execwb(6,6);
	    document.getElementById("error").style.display="";
	}    
}    

function printBefore() {
	document.getElementById("error").style.display="none";
}

function printAfter() {
	document.getElementById("error").style.display="";
}

function nowprint() {   
    window.print();   
}   
/* function window.onbeforeprint() {   
    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
}   
function window.onafterprint() {   
    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
}  */  

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
    .PageNext {
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
          //  RegWsh.RegWrite(hkey_root+hk“”,"");
            
            hkey_key="footer";
            RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
            
      }catch(e){
     // MyAlert(e.description());
      }
     </script>
</head>  
<!--    <script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-barcode-2.0.2.min.js"></script> -->

<body>
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT> 
<form method="post" name="fm" id="fm">
<div id= 'tab'>
<table align="center" width="650px">
<tr>
  <td align="center" colspan="2">
	<span style="font-size: 20px;">付&nbsp;&nbsp;款&nbsp;&nbsp;凭&nbsp;&nbsp;证</span>
	<hr color="black" width="50%">
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
	  ${map.INVO_NO}
	</td>
</tr>
<tr>
  <td align="left">&nbsp;贷方科目:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 结算单号:&nbsp;&nbsp; ${map.BALANCE_CODE}</td>
  <td align="right">NO.&nbsp;${NO}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${map.BALANCE_DATE}&nbsp;
  </td>
</tr>
</table>
<table  align="center" class="tab_printsep">
	<tr>
	 <td width="24%" colspan="2">借方科目</td>
	 <td width="36%" rowspan="2" colspan="2">申请借（付）款类容</td>
	 <td width="20%" colspan="12" >申请金额</td>
	 <td width="20%" colspan="12">批准付款金额</td>
	</tr>
	<tr>
	<td width="12%" >一级账户</td>
	<td width="12%" >二级账户</td>
	<td >十</td>
	<td >亿</td>
	<td >千</td>
	<td >百</td>
	<td >十</td>
	<td >万</td>
	<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
	<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
	<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="gs" colspan="2"><div style="text-align: left;">&nbsp;支付：${map.VENDER_NAME}</div></td>
	<td id="a">${m1}</td><td id="b">${m2}</td><td id="c">${m3}</td><td id="d">${m4}</td><td id="e">${m5}</td><td id="f">${m6}</td>
	<td id="g">${m7}</td><td id="h">${m8}</td><td id="i">${m9}</td><td id="j">${m10}</td><td id="k">${m11}</td><td id="l">${m12}</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="dz" colspan="2"><div style="text-align: left;">&nbsp;项数：${map.DTL_NUM}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="yh" colspan="2"><div style="text-align: left;">&nbsp;开户行：${definePO.bank}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td id="zh" colspan="2"><div style="text-align: left;">&nbsp;账号：${definePO.account}</div></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2"><span style="font-size: 14px; text-align: left; width: 100%">&nbsp;领导批示：</span></td>
		<td colspan="4"><span style="font-size: 14px; text-align: left; width: 100%">&nbsp;借款单位：销售公司&nbsp;&nbsp;负责人</span></td>
		<td colspan="10"><span style="font-size: 14px; text-align: left; width: 100%">&nbsp;支付人</span></td>
		<td colspan="12"><span style="font-size: 14px; text-align: left; width: 100%">&nbsp;证件号（&nbsp;&nbsp;）</span></td>
	</tr>
</table>
<center>
	<table align="center" width="650px">
		<tr>
			<td colspan="5" height="28"><span style="font-size: 16px; text-align: left; width: 100%">&nbsp;&nbsp;&nbsp;&nbsp;共计人民币（大写）：</span></td>
		</tr>
		<tr>
			<td width="20%" style="font-size: 14px;">&nbsp;会计主管：</td>
			<td width="20%" style="font-size: 14px;">&nbsp;出纳员：</td>
			<td width="20%" style="font-size: 14px;">&nbsp;组长：</td>
			<td width="20%" style="font-size: 14px;">&nbsp;复核：</td>
			<td width="20%" style="font-size: 14px;">&nbsp;会计员：</td>
		</tr>
	</table>
</center>
</div>
<br/>

<center>
  <p class="Noprint page-print-buttons">
	<input type=button name= button_print class="u-button" value="打印" data-before="printBefore" data-after="printAfter">
	<input type=button name= button_print class="long_btn u-button" value="打印设置">
	<input type=button name= button_print class="u-button" value="打印预览" data-before="printBefore" data-after="printAfter">
  </p>
</center>
<br/>
<center>
<span id="error">
<font size="3" color="red">注意:${error }</font>
</span>
</center>
</form>
</body>
</html>