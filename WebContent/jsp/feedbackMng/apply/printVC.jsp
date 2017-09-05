<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务车申请表打印</title>
</head>
<body>
<br/>
<center><strong><font size="5">服务车申报审批表</font></strong></center>
<br>
<div align="center">
<table border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr style="width:100%">
		<td width="14%" align="left" class="tdp">&nbsp;单据编码：</td>
		<td width="34%" align="left" class="tdp">&nbsp;${map.APPLY_NO}</td>
		<td width="14%" align="left" class="tdp">&nbsp;制单日期：</td>
		<td width="14%" align="left" class="tdp">&nbsp;
			<fmt:formatDate value='${map.MAKE_DATE}' pattern="yyyy-MM-dd"/>
		</td>
		<td width="12%" align="left" class="tdp">&nbsp;服务站代码：</td>
		<td width="14%" align="left" class="tdpright">&nbsp;${map.DEALER_CODE}</td>
	</tr>
	<tr>
		<td align="left" class="tdp">&nbsp;服务站名称：</td>
		<td align="left" class="tdp">&nbsp;${map.DEALER_NAME}</td>
		<td align="left" class="tdp">&nbsp;申请单位联系人：</td>
		<td align="left" class="tdp">&nbsp;${map.LINK_MAN}</td>
		<td align="left" class="tdp">&nbsp;申请单位电话：</td>
		<td align="left" class="tdpright">&nbsp;${map.LINK_PHONE}</td>
	</tr>
	<tr>
		<td align="left" class="tdp">&nbsp;经销商名称：</td>
		<td align="left" class="tdp">&nbsp;${map.DEALER_NAME1}</td>
		<td align="left" class="tdp">&nbsp;经销商电话：</td>
		<td align="left" class="tdp">&nbsp;${map.DEALER_PHONE}</td>
		<td align="left" class="tdp">&nbsp;申请单位传真：</td>
		<td align="left" class="tdpright">&nbsp;${map.FAX_NO}</td>
	</tr>
	<tr>
		<td align="left" class="tdp">&nbsp;申请车型及状态：</td>
		<td align="left" class="tdp">&nbsp;${map.MODEL_CODE}</td>
		<td align="left" class="tdp">&nbsp;单位类型：</td>
		<td align="left" class="tdp">&nbsp;
			<script type="text/javascript">
   				writeItemValue(${map.DEALER_LEVEL});
   			</script>
		</td>
		<td align="left" class="tdp">&nbsp;单位类别：</td>
		<td align="left" class="tdpright">&nbsp;
			<script type="text/javascript">
   				writeItemValue(${map.DEALER_TYPE});
   			</script>
		</td>
	</tr>
	<tr>
		<td align="left" class="tdp">&nbsp;单位等级：</td>
		<td align="left" class="tdp"></td>
		<td align="left" class="tdp">&nbsp;服务车类型：</td>
		<td align="left" colspan="3" class="tdpright">&nbsp;
			<script type="text/javascript">
   				writeItemValue(${map.CAR_TYPE});
   			</script>
		</td>
	</tr>
	<tr>
		<td class="tdp" align="left">&nbsp;申请备注：</td>
		<td colspan="5" class="tdpright" align="left">&nbsp;${map.REMARK}</td>
	</tr>
	<tr>
		<td colspan="6" class="tdpright" align="left">
			<font size="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我公司承诺该车只用于长安汽车的销售服务工作，并按长安公司要求制作形象，四年内不得转让。若违反以上承诺，本公司将接受长安公司的处罚</font> 
		</td>
	</tr>
	<tr>
		<td height="4" colspan="6" class="tdpright">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" align="left" class="tdp">&nbsp;申购车标准价：</td>
		<td align="left" class="tdp">&nbsp;${map.STANDARD_PRICE}</td>
		<td colspan="2" align="left" class="tdp">&nbsp;申购车折让总支持额度：</td>
		<td align="left" class="tdpright">&nbsp;${map.SUPPORT_QUOTA}</td>
	</tr>
	<tr>
		<td colspan="2" align="left" class="tdp">&nbsp;申购车第一年支持额度：</td>
		<td align="left" class="tdp">&nbsp;${map.SUPPORT_QUOTA2}</td>
		<td colspan="2" align="left" class="tdp">&nbsp;申购车第一年优惠后购车金额：</td>
		<td align="left" class="tdpright">&nbsp;${map.DISCOUNT_AMOUNT}</td>
	</tr>
	<tr>
		<td colspan="6" height="4px" class="tdpright">&nbsp;</td>
	</tr>
	<tr>
		<td align="left" class="tdp">&nbsp;VIN：</td>
		<td align="left" class="tdp">&nbsp;${map.VIN}</td>
		<td align="left" class="tdp">&nbsp;发动机号：</td>
		<td colspan="3" align="left" class="tdpright">&nbsp;${map.ENGINE_NO}</td>
	</tr>
	<tr>
		<td colspan="6" class="tdpright">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="6">
			<table class="tabp">
				<tr>
					<td class="tdp">授权人员</td>
					<td class="tdp">授权时间</td>
					<td class="tdp">授权结果</td>
					<td class="tdp">授权意见</td>
				</tr>
				<c:forEach var="audit" items="${list}">
		    		<tr>
		    			<td class="tdp">${audit.NAME}</td>
		    			<td class="tdp">
		    				<fmt:formatDate value='${audit.CREATE_DATE}' pattern='yyyy-MM-dd' />
		    			</td>
		    			<td class="tdp">
		    				<script type="text/javascript">
		    					writeItemValue(${audit.AUDIT_STATUS});
		    				</script>
		    			</td>
		    			<td class="tdp">${audit.REMARK}</td>
		    		</tr>
		    	</c:forEach>
			</table>
		</td>
	</tr>
</table>
<br><br><br>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr">
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();">    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();">    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">
			</div>
		</td>
	</tr>
</table>
</div>
<script language="javascript">

	function printsetup()
	{
		wb.execwb(8,1);// 打印页面设置
	}
	function printpreview()
	{
		wb.execwb(7,1);// 打印页面预览
	}
	function printit()
	{
		if(confirm('确定打印吗？'))
		{
			wb.execwb(6,6)
		}
	}
	
</script> 
</body>
</html>
