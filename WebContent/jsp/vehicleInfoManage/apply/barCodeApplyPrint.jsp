<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%   
    //String code = "SC1022SAN.FAA.MY1";//条形码内容   
    	String contextPath = request.getContextPath();
   
%>  

    <style>
   
<!--

-->
td{ word-wrap: break-word; word-break: normal; } 


</style>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
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
        barHeight: "90",
        moduleSize: "2",
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


<body onload='tiaoma();' topmargin="0px" leftmargin="0px" rightmargin="0px" bottommargin="0px">  
<p class="Noprint">
<input type=button value=打印 onclick="nowprint();">
</p>
<center>

<table width="100px" class="tab_print">
  <tr>
    <td align="center" colspan="2">经销商：${bean.dealerCode }&nbsp;&nbsp;车主：${bean.ownName }
    <input type="hidden" name="vin" id="vin" value="${bean.vin }"></td>
  </tr>
  <tr>
    <td align="center" colspan="2"><div id="bcTarget" ></div></td>
  </tr>

</table>
<br>

<script type="text/javascript">		 

	$("#bcTarget").html("").show().barcode("<%=request.getAttribute("VIN")%>", "code39",settings); 
</script>

</center>
</body>  
</html>    
