<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供应商问题反馈</title>
<% String contextPath = request.getContextPath();
	String isReutrn=request.getAttribute("isReturn")==null?"":request.getAttribute("isReturn").toString();

	List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
	if(isReutrn.equals("1")) isReutrn="__extQuery__(1);";
%>
</head>
<BODY onload="doInit();<%=isReutrn %>">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;供应商问题反馈</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td align="right" nowrap="nowrap"  >经销商代码：</td>            
         <td align="left" nowrap="nowrap" >
        <input id="dealerCode" name="supply_code"  type="text" class="middle_txt">
         </td>
         <td align="right" nowrap="nowrap" >经销商名称：</td>
         <td align="left" nowrap="nowrap"><input id="supply_name" name="supply_name"  type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
        <tr>
         <td align="right" nowrap="nowrap"  >配件代码：</td>            
         <td align="left" nowrap="nowrap" >
        <input id="part_name" name="part_code"  type="text" class="middle_txt">
         </td>
         <td align="right" nowrap="nowrap" >配件名称：</td>
         <td align="left" nowrap="nowrap"><input id="part_name" name="part_name" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
     <tr>
       		<td align="right" nowrap="nowrap" >状态：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("status",<%=Constant.PROBLEM_TYPE%>,"",true,"short_sel","","false",'');
	           </script>
	          </td>
	         <td align="right" nowrap="nowrap" >类型：</td>
	         <td align="left" nowrap="nowrap">
	        	<select name="type" class="short_sel">
	        	    <option value=""> =请选择=</option>
	        		<option value="0"> 供应商与配件关系</option>
	        	<option value="1">配件价格</option>
	        	</select>
	          </td>
         </tr>
       <tr>
         <td align="center" colspan="4" nowrap="nowrap">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
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
   var url = "<%=contextPath%>/claim/oldPart/OldPartMakerProblemManager/makerProblemQuery.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "操作",dataIndex: 'PROBLEM_ID',renderer:myLink,align:'center'},
  				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  				{header: "次数", dataIndex: 'NUM', align:'center'},
  				{header: "类型", dataIndex: 'TYPE', align:'center',renderer:typeFormat},
  				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
  				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
  				{header: "修改时间	", dataIndex: 'UPDATE_DATE', align:'center',renderer:formatDate},
  				{header: "备注", dataIndex: 'REMARK', align:'center'}
  		      ];
  		      __extQuery__(1);
   function typeFormat(value,meta,record){
	   if(value==0){
		   return String.format("供应商与配件关系");
	   }else{
		   return String.format("配件价格");
	   }
   }
   //超链接设置
   function myLink(value,meta,record){
	   return String.format("<a href='<%=contextPath%>/claim/oldPart/OldPartMakerProblemManager/makerProblemDetail.do?problem_id="+value+"'>明细</href>");
   }

   //首先检查是否填写货运单号，否则不予上报
   function checkTransNo(report_id,boxNumber){
	   if(boxNumber>0){
	       MyAlert('有数据未填写装箱单号!');
	       return;
	   }else{
		   var url= "<%=contextPath%>/claim/oldPart/OldPartMakerProblemManager/checkTransNo.json?report_id="+report_id;
		   makeNomalFormCall(url,isReport,'fm','');
	   }
   }
   function isReport(json){
	   var isFillTransNo=json.flag;
	   var report_id=json.report_id;
	   //暂时去掉限制货运单号的限制(要放开去掉注释就OK)
	   if(isFillTransNo=='0'||isFillTransNo==null){
           MyAlert("货运单号不能为空!");
           return;
	   }
	   MyConfirm("确认上报吗？",report,[report_id]);
   }
   function report(str){
       fm.action="<%=contextPath%>/claim/oldPart/OldPartMakerProblemManager/reportClaimInfo11.do?report_id="+str;
       fm.method="post";
       fm.submit();
   }
   
   
   function isCheck(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage6.do?CLAIM_ID="+id;
       fm.method="post";
       fm.submit();
   }

   function isCheck12(id){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id+"&types=0";
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
   }
   
   
   function isCheck1221(id){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id+"&types=1";
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
   }
   function showDetail21(json){
		var ok = json.ok;
		var id = json.id;
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id;
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
	}

   function isCheck13(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage51.do?CLAIM_ID="+id;
	   fm.method="post";
	   fm.submit();
	}
	
   function isCheck1(id){
     fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+id;
	   fm.method="post";
	   fm.submit();
   }
	
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }
   function doInit(){
	  loadcalendar();
   }
</script>
</BODY>
</html>