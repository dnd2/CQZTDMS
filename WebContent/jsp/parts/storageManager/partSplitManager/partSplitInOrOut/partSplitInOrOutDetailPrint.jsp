<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title></title>
<style type="text/css">
html,body{font-size:12px;margin:0px;height:100%;}
.mesWindow{border:#666 1px solid;background:#fff;}
.mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
.mesWindowContent{margin:4px;font-size:12px;}
.mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
</style>
</head>
<script language="javascript">
//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
	
		document.write(str);
	}
function getIndex(){
	document.write(document.getElementById("file").rows.length-1);
}

function printOrder(){
	$("printBtn").style.display="none";
	window.print();
	$("printBtn").style.display="";
}
</script>
</head>
<body onload="">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type=textarea name=datapackager style="display:none" value=''>	
	<div name=thisblock id=thisblock>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="90%" >	
	<br><center><font size="+1"><b>
        	配件拆合件出入库清单
	</b></font></center>
	</TABLE>
	<table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
		<tr>
			<td>
				拆合单号：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.SPCPD_CODE}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				制单单位：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.ORG_CNAME}
			</td>
			<td>
				制单人：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.NAME}
			</td>		
		</tr>
		<tr>
			<td>
				出入库房：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.WH_CNAME}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				拆合类型：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.SPCPD_TYPE1}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>		
		</tr>
	</table>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr> 
	        <td  colspan=1>序号</td> 
	        <td  colspan=1>配件件号</td>
	        <td  colspan=1>配件编码</td> 
	        <td  colspan=1>配件名称 </td>
	        <td  colspan=1>是否总成件</td>
	        <td  colspan=1>拆分数量 </td>
	        <!-- <td  colspan=1>成本比例</td> -->
	        <td  colspan=1>出入库数量 </td>
	        <td  colspan=1>金额 </td>
	        <td  colspan=1>货位</td>
	    </tr>
	    <c:forEach items="${dataMap.detailList}" var="data" >
	    	<tr> 
		    	<td>
		    		<script language="javascript">
						getIndex()
					</script>
		    	</td> 
		        <td>
		        	&nbsp;${data.PART_CODE}
		        </td> 
		        <td >
		        	&nbsp;${data.PART_OLDCODE}
		        </td> 
		        <td >
		        	&nbsp;${data.PART_CNAME}
		        </td> 
		         <td >
		        	&nbsp;
		        	<script type="text/javascript">
					getCode(${data.IS_MB});
				</script>
		        </td> 
		         <td >
		        	&nbsp;${data.SPLIT_QTY}
		        </td> 
		         <!--<td >
		        	&nbsp;${data.SPLIT_RATE}
		        </td> -->
		         <td >
		        	&nbsp;${data.QTY}
		        </td> 
		         <td >
		        	&nbsp;${data.AMOUNT}
		        </td> 
		         <td >
		        	&nbsp;${data.LOC_NAME}
		        </td> 
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div>
	<table border=0 width=100%>
					<tr align="center">
						<td>
							<input type=button id="printBtn" class="txtToolBarButton" value="打印" onClick="printOrder();">
						</td>
					</tr>
				</table>
</form>
</body>
</html>