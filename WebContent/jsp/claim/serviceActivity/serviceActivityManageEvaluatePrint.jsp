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

<% 
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<body onload='tiaoma();' topmargin="0px" leftmargin="0px" rightmargin="0px" bottommargin="0px">  
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT> 
<center>
  <div style="font-size: 24px; font-weight: bold; font-family: '宋体'">2013年服务营销费用支付审批表</div>
  <br>
  <form method="post" name="fm" id="fm">
    <table width="1200px" class="tab_printvoid">
      <tr>
      	<td width="5%" style="font-weight: bold; text-align: center; height: 30px;">序号</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">服务站代码</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">服务站名称</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">支付项目</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">主题编号</td>
        <td width="15%" style="font-weight: bold; text-align: center; height: 30px;">支付依据</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">申请金额（元）</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">评估金额（元）</td>
        <td width="10%" style="font-weight: bold; text-align: center; height: 30px;">备注</td>
      </tr>
      <% int i = 1; %>
      <c:set var="total" value="0" />
      <c:set var="total1" value="0" />
      <c:forEach var="Evaluate" items="${Evaluate}">
        <tr>
          <td align="center" height="30px">${Evaluate.ID}</td>
          <td align="center" height="30px">${Evaluate.DEALER_CODE}</td>
          <td height="30px">&nbsp;${Evaluate.DEALER_NAME}</td>
          <td align="center" height="30px">
           <script type='text/javascript'>
		       var proCode=getItemValue('${Evaluate.ACTIVITY_TYPE}');
		       document.write(proCode) ;
		     </script></td>
          <td align="center" height="30px">${Evaluate.SUBJECT_NO}</td>
          <td align="center" height="28px">${Evaluate.SUBJECT_NAME}</td>
          <td align="center" height="30px">${Evaluate.REPAIR_TOTAL}</td>
          <td align="center" height="30px">${Evaluate.EVALUATE_AMOUNT}</td>
          <td align="center" height="30px"></td>
           <c:set var="total" value="${total + Evaluate.EVALUATE_AMOUN}"></c:set>
           <c:set var="total1" value="${total1 + Evaluate.REPAIR_TOTAL}"></c:set>
        </tr>
      </c:forEach>
      <tr>
      	<td colspan="6"  style="text-align: right; height: 30px;">费用合计（元）：</td>
      	<td style="text-align: center; height: 30px;">${total1 }</td>
      	<td style="text-align: center; height: 30px;">${total }</td>
        <td width="20%" style="text-align: center; height: 30px;"></td>
      </tr>
    </table>
    
    <input type="hidden" name="day" id="day" value="${day}" >
    <input type="hidden" name="iconunt" value="<%= i %>" >
    <br>
    <table width="900px" class="tab_printvoid">
      <tr>
        <td width="17%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">销售公司总经理</td>
        <td width="17%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">售后副总</td>
        <td width="17%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">销售财务处</td>
        <td width="17%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">服务营销处</td>
        <td width="16%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">校对</td>
        <td width="16%" align="center" style="font-size: 14px; font-weight: bold; height: 40px;">编制</td>
      </tr>
      <tr>
        <td height="40px">&nbsp;</td>
        <td height="40px">&nbsp;</td>
        <td height="40px">&nbsp;</td>
        <td height="40px">&nbsp;</td>
        <td height="40px">&nbsp;</td>
        <td height="40px">&nbsp;</td>
      </tr>
    </table>
    <br>
    <p class="Noprint">
		<input type=button name= button_print class="normal_btn" value="打印" onclick ="nowprint();">
		<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
		<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
    </p>
  </form>
</center>

</body>  
</html>    
