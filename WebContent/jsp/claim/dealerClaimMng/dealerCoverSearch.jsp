<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>封面打印</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/dealerPrintSearch.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "经销商代码", width:'15%', dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'7%', dataIndex: 'DEALER_NAME'},
					{header: "结束基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
					{header: "劳务费总计", width:'15%', dataIndex: 'LABOUR_PRICE'},
					{header: "材料费总计", width:'15%', dataIndex: 'PART_PRICE'},
					{header: "时间区间", width:'15%', dataIndex: 'STAR_DATE',renderer:formatDate3},
					{header: "打印次数", width:'15%', dataIndex: 'PRINT_TIMES'},
					{header: "首次打印时间", width:'15%', dataIndex: 'PRINT_DATE',renderer:formatDate},
					{header: "上次次打印时间", width:'15%', dataIndex: 'LAST_PRINT_DATE',renderer:formatDate2},
					{width:'5%',header: "操作", dataIndex: 'ID',align:'center',renderer:myLink}
		      ];

function myLink(value,metadata,record){
	 return String.format("<a href=\"#\" onclick=prints("+record.data.ID+"); >[明细]</a>");
}
function prints(id){
	fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/dealerPrintDetails.do?id="+id;
			fm.submit();
}

	      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		__extQuery__(1);
	}

	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	function formatDate2(value,meta,record) {
		 if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	function formatDate3(value,meta,record) {
	var star = record.data.STAR_DATE;
	var end = record.data.END_DATE;
		if (star==""||end==null) {
			return "";
		}else {
			return star.substr(0,10)+"  至   "+end.substr(0,10);
		}
	}
	
	
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY onload="doInit">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;上报封面查询</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
    	<tr>
			<td align="right">服务站代码：</td>
			<td align="left">
			<input name="dealerCode" id = "dealerCode" value="" type="text" class="middle_txt">
				</td>
            <td > 服务站名称</td>
         	<TD  align="left">
         	 <input name="dealerName" id = "dealerName" value="" type="text" class="middle_txt">
         	</TD>
          </tr>
                       
             <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
           
			</td>
           </tr>
  </table>
  
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>