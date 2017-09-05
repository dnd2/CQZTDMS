<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;无旧件单</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" callFunction="javascript:MyAlert();"></td>
         <td style="color: #252525;width: 115px;text-align: right">出库时间：</td>
         <td align="left" nowrap="true">
			<input name="out_start_date" type="text" class="short_time_txt" id="out_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="out_end_date" type="text" class="short_time_txt" id="out_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_end_date', false);" /> 
		</td>	
        
       </tr>
       <tr>
        <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="SUPPLY_CODE" name="SUPPLY_CODE" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
         <td style="color: #252525;width: 115px;text-align: right">旧件出库单号： </td>
         <td nowrap>
          <input id="OLD_PART_OUT_NO" name="OLD_PART_OUT_NO" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       </tr>
       
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="goToAdd();">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutListByCondition.json?type=2";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'OUT_NO', align:'center',renderer:myLink},
  				{header: "出库号", dataIndex: 'OUT_NO', align:'center'},
  				{header: "出库人", dataIndex: 'NAME', align:'center'},
  				{header: "出库时间",dataIndex: 'OUT_TIME',align:'center'},
  				{header: "供应商编码", dataIndex: 'SUPPLY_CODE',align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  			//	{header: "索赔单数", dataIndex: 'AMOUNT', align:'center'},
  				{header: "配件数", dataIndex: 'PART_AMOUNT', align:'center'}
  		      ];
  		      
  		      __extQuery__(1);
  	function myLink(value,meta,record){
  	var flag = record.data.FLAG;
  	var str="";
  	if(flag==1){
  		str +="<a href='#' onClick='saveRenge(\""+value+"\");'>[生成退赔单]</a>";
  	}else if(flag==2){
  		str +="<a href='#' onClick='printRenge(\""+value+"\");'>[打印退赔单]</a>";
  	}
  		str +="<a href='#' onClick='outDetail2(\""+value+"\");'>[明细]</a>";
  
  		return String.format( str);
	}
	//生成退赔单
	function saveRenge(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveRengePer.do?out_no="+value;
       	fm.method="post";
       	fm.submit();
	}
	//打印退赔单
	function printRenge(value){
		window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/printRenge.do?out_no='+value,"打印退赔单", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	//明细
	function outDetail2(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outDetail2.do?out_no="+value;
       	fm.method="post";
       	fm.submit();
	}
	function detailDown(value,supplayCode){
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkDoor.json?out_no="+value+"&code="+supplayCode+"&type=5";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
 	 
	}
	
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function doInit(){
	   loadcalendar();
   }
   //转到新增页面
   function goToAdd(){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addNoPageList.do";
       fm.submit();
   }
</script>
</BODY>
</html>