<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<%
Map<String, Object> list =(Map<String, Object>) request.getAttribute("list");
%>
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
<title>旧件清单打印</title>
</head>
<div id="kpr" align="center" class="Noprint">     
<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"></div>

<body>
  <p>
<div>
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="40" width="100%" style="font-size: 24px; font-weight: bold;"><span class="STYLE1">
			北汽幻速索赔故障件返回清单
		</span></td>
	</tr>
    <tr>
      <td height="20" colspan="8" align="center"><table class="tab_printsep" width="800px">
          <tr>
            <td width="13%">运单号：</td>
            <td width="20%">${list.TRAN_NO}</td>
            <td width="13%">应到件数：</td>
            <td width="20%">&nbsp;${list.PARKAGE_AMOUNT}</td>
            <td width="14%">实到件数：</td>
            <td width="20%">&nbsp;</td>
          </tr>
          <tr>
            <td >发运方式：</td>
            <td ><script type="text/javascript">
	              		document.write(getItemValue('${list.TRANSPORT_TYPE }'))
	       			</script></td>
            <td >回运日期：<input type="hidden" id="createD" name="date"value="${list.CREATE_DATE }"/></td>
            <td id="createDate">  </td>
            <td >包装方式：</td>
            <td >&nbsp;</td>
          </tr>
          <tr>
            <td >包装情况：</td>
            <td >&nbsp;</td>
            <td >故障卡情况：</td>
            <td >&nbsp;</td>
            <td >清单情况：</td>
            <td >&nbsp;</td>
          </tr>
          <tr>
            <td >服务站代码：</td>
            <td >${list.DEALER_CODE}</td>
            <td >服务站名称：</td>
            <td >${list.DEALER_SHORTNAME}</td>
            <td >服务站电话：</td>
            <td >${list.PHONE}</td>
          </tr>
           <tr>
            <td >三包员电话：</td>
            <td colspan="2" align="left">${list.TEL}</td>
             <td >外包装情况：</td>
            <td colspan="2" align="left"></td>
          </tr>
        </table></td>
    </tr>
    <tr>
      <td colspan="9">
      <table class="tab_printsep" align="center" width="800px">
          <tr align="center">
            <td width="5%" align="center">序号</td>
            <td width="10%" align="center">索赔单号</td>
            <td width="5%" align="center">车型</td>
            <td width="10%" align="center">配件代码</td>
            <td width="13%" align="center">配件名称</td>
            <td width="13%" align="center">供应商代码</td>
            <td width="10%" align="center">故障描述</td>
            <td width="5%" align="center">申请数量</td>
            <td width="5%" align="center">验收数量</td>
            <td width="5%" align="center">装箱单号</td>
            <!--  <td width="5%" align="center">条码</td>-->
            <td width="5%" align="center">序号</td>
          </tr>
            <c:set var="pageSize"  value="10000" />
            <c:forEach var="dList" items="${detailList}" varStatus="status">
              <tr   align="center">
                <td align="center">${status.index+1}</td>
                <td align="center">${dList.CLAIM_NO}</td>
                <td align="center">${dList.MODEL_CODE}</td>
                <td align="center" >${dList.PART_CODE}</td>
                <td align="center"><script>
	    		var str='${dList.PART_NAME}';
	    		var s = '' ;
	    		for(var i = 0 ;i<str.length;i++){
					s+=str.substring(i,i+1);
					if((i+1)%12==0){
						s+='<br/>';
					}
	    		}
	    		document.write(s);
	    	</script></td>
                <td align="center">${dList.PRODUCER_CODE}</td>
                <td align="center">${dList.BMARK}</td>
                <td align="center">${dList.RETURN_AMOUNT}</td>
                <td align="center"></td>
                <td align="center">${dList.BOX_NO}</td>
                <td align="center">${fn:substring(dList.BARCODE_NO, 11, -1)} 
                </td>
              </tr>
            </c:forEach>
        </table></td>
    </tr>
    <tr>
    <td height="1">&nbsp;</td>
    </tr>
    <tr>
      <td align="center" colspan="9"><table class="tab_printsep" align="center" width="800">
          <tr align="center">
            <td rowspan="4" width="10%">服务站<br/>
              签字<br/>
              确认</td>
            <td  width="30%">索赔员</td>
            <td  width="30%">站长</td>
            <td  width="30%">服务站盖章</td>
          </tr>
          <tr>
            <td ><div style="height: 35px;"/></td>
            <td ></td>
            <td ></td>
          </tr>
        </table></td>
    </tr>
    <tr>
    <td height="1">&nbsp;</td>
    </tr>
    <tr>
      <td align="center" colspan="9"><table class="tab_printsep" align="center" width="800">
          <tr align="center">
            <td rowspan="4" width="10%">北汽<br/>
              本部<br/>
              确认</td>
            <td  width="20%">分检技术员</td>
            <td  width="20%">分检日期</td>
            <td  width="25%">保管员</td>
            <td  width="25%">室主任</td>
          </tr>
          <tr>
            <td ><div style="height: 35px;"/></td>
            <td ></td>
            <td ></td>
            <td ></td>
          </tr>
        </table></td>
    </tr>
    <tr style="width: 800px; text-align: left;font-size: 14px;">
    <td colspan="4" align="center">
	提示：1、验收数量由北汽本部验收人员填写，装箱号由服务站将故障件装箱时填写。<br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、服务站索赔员、站长必须对故障件返件情况签字确认，并加盖服务站公章。<br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、北汽本部验收确认，由北汽本部人员验收后签字确认。<br></td>
</tr>
</table>
</div>
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
  
wb.execwb(6,6)    
}    
}    
</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
</html>