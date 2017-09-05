<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
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
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/dealerPrintDetail.json";
				
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
	 return String.format("<a href=\"#\" onclick=prints("+record.data.ID+"); >[打印]</a>");
}
function prints(id){
	var type = $('dealerType').value;
	if(type==<%=Constant.DEALER_LEVEL_02%>){
		MyAlert("二级站不需打印封面,由一级站打印!");
		return false;
	}else{
	MyConfirm("打印封面？",print1,[id]);
	}
		
}
function print1(id){
$('queryBtn').disabled = true;
	fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/prints.do?id="+id;
			fm.submit();
}
    function showPrintPage1(claimId){ 
        
        var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 

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
	
	function printNow(){
		var yieldly = $('YIELDLY_TYPE').value;
		var startDate = $('startDate').value;
		var endDate = $('endDate').value;
		var flag = true;
		if(yieldly==""){
		MyAlert("请选择结算基地!");
		flag = false;
		return false;
		}
		if(startDate==""||endDate==""){
			MyAlert("打印开始时间,结束不能不能为空,请联系管理员.");
			flag = false;
			return false;
		}else if(startDate>=endDate){
			MyAlert("本月封面已打印,请在明细中补打!");
			flag = false;
			return false;
		}
		var type = $('dealerType').value;
		if(flag){
		if(type==<%=Constant.DEALER_LEVEL_02%>){
			MyAlert("二级站不需打印封面,由一级站打印!");
			return false;
		}else{
		MyConfirm("打印封面？",saveCover,[]);
		}
		}
	}
	function saveCover(){
	$('queryBtn').disabled = true;
			fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/coverSave.do";
			fm.submit();
	}
	function showDate(yieldly){
	if(yieldly==""){
			$('div').innerText='当前打印开始时间：';
			$('startDate').value="";

			$('divEnd').innerText='当前打印结束时间：';
			$('endDate').value="";
	}else{
		var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/showDate.json?yieldly="+yieldly;
		sendAjax(url,showDetail2,"fm")
		}
	}

	function showDetail2(json){
	if(json.oldDate=="noTime"){
		MyAlert("该站未维护相关时间!请联系管理员.");
	}else{
		var oldDate = json.oldDate;
		var oldDateEnd = json.oldDateEnd;
			var oldTime = oldDate.substring(0,10);
			$('div').innerText='当前打印开始时间：'+oldTime;
			$('startDate').value=oldTime;

			var oldTimeEnd = oldDateEnd.substring(0,10);
			$('divEnd').innerText='当前打印结束时间：'+oldTimeEnd;
			$('endDate').value=oldTimeEnd;
		}
	}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY onload="doInit">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;封面打印</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
    <input type="hidden" id="startDate" name="startDate" value=""/>
    <input type="hidden" id="endDate" name="endDate" value=""/>
    <input type="hidden" id="dealerType" name="dealerType" value="${dealerType }"/>
    	<tr>
			<td align="right">结算基地：</td>
			<td align="left">
			<select name="YIELDLY_TYPE" id="YIELDLY_TYPE" style="width: 150px;" onChange="showDate(this.value);">
				<option value="">-请选择-</option>
				<option value="<%=Constant.PART_IS_CHANGHE_01%>">重庆</option>
				</select>
				</td>
            <td id="div" >当前打印开始时间：</td>
          
            <td id="divEnd" >当前打印结束时间：</td>
          </tr>
                       
             <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="打印封面"  onClick="printNow();" />
           
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