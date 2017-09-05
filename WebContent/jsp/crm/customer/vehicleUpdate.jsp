<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
 .nx_type{width:160px;}
</style>
<title>车辆信息修改</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;车辆信息修改 </div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<input type="hidden" id="decorationId" name="vehicleId" value="${tp.vechileId}"/>
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt"><div align="right">VIN：</div></td>
				<td width="30%" >
      				<input type="text" id="vin" name="vin" class="min_type" size="20" value="${tp.vin}"/>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">车型代码：</div></td>
				<td width="30%" >
      				 <input name="modelCode" type="text" id="modelCode"  class="min_type"  value="${tp.modelCode}"  />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">车型名称：</div></td>
				<td width="30%" >
      				 <input name="modelName" type="text" id="modelName"  class="min_type" value="${tp.modelName}"  />
    			</td>
    			<td width="20%" class="tblopt"><div align="right">购买日期：</div></td>
				<td width="30%" >
				  <input name="purDate" type="text" id="purDate"  class="nx_type" readonly  value="${buyDate}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'purDate', false);" />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">车身色：</td>
				<td width="30%" >
		             <input type="hidden" id="color" name="color" value="${tp.vechileColor}" />
		      		<div id="ddtopmenubar1" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:173px;" rel="ddsubmenu1" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6006', loadColor);" deftitle="--请选择--">
								<c:if test="${color==null}">--请选择--</c:if><c:if test="${color!=null}">${color}</c:if>
								</a>
								<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
    			</td>
				<td width="20%" class="tblopt"><div align="right">底盘号：</td>
				<td width="30%"  >
      				<input type="text" id="enVin" name="enVin" class="min_type" size="20" value="${tp.lowVin}"/>
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">购买价格：</td>
				<td width="30%" >
      				<input type="text" id="purPrice" name="purPrice" class="min_type" size="20" value="${tp.price}"/>
    			</td>
				<td width="20%" class="tblopt"><div align="right">车牌号：</td>
				<td width="30%"  >
      				<input type="text" id="vehicleNo" name="vehicleNo" class="min_type" size="20" value="${tp.carNumber}"/>
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">上牌日期：</td>
				<td width="30%" >
				 <input name="boardDate" type="text" id="boardDate"  class="nx_type" readonly  value="${boardDate}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'boardDate', false);" />
    			</td>
				<td width="20%" class="tblopt"><div align="right">音响PIN码：</td>
				<td width="30%"  >
      				<input type="text" id="pin" name="pin" class="min_type" size="20" value="${tp.pin}"/>
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">生产日期：</td>
				<td width="30%" >
				 <input name="productDate" type="text" id="productDate"  class="nx_type" readonly  value="${productDate}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'productDate', false);" />
    			</td>
				<td width="20%" class="tblopt"><div align="right">&nbsp;</td>
				<td width="30%"  >
      				&nbsp;
    			</td>
			</tr>
			<tr>
				<td class="table_query_3Col_input" colspan="4" align="center">
					<center>
					<input type="button" class="normal_btn" onclick="updateLink();" value="修 &nbsp; 改" id="queryBtn" /> 
					</center>
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">

	function updateLink(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/vehicleUpdate.json', addResult, "fm") ;
	}
	//数据回写
	function addResult(json){
		_hide();
		parent.$('inIframe').contentWindow.vechileTableAdd(json);
	}
	//颜色选择时执行的方法
	function loadColor(obj){
		$("color").value=obj.getAttribute("TREE_ID");
	}
</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>   
</body>
</html>