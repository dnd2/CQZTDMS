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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" callFunction="javascript:MyAlert();"></td>
         <td style="color: #252525;width: 115px;text-align: right">出库时间：</td>
         <td align="left" nowrap="true">
			<input name="out_start_date" type="text" class="short_time_txt" id="out_start_date" value="${startTime }" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="out_end_date" type="text" class="short_time_txt" value="${endTime }" id="out_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_end_date', false);" /> 
		</td>	
        
       </tr>
       <tr>
        <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
         <td style="color: #252525;width: 115px;text-align: right">出库类型：</td>
         <td nowrap>
            <script type="text/javascript">
			genSelBoxExp("out_claim_type",<%=Constant.OUT_CLAIM_TYPE%>,"",true,"short_sel","","false",'');
 			</script>
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">旧件退赔单号： </td>
         <td nowrap>
          <input id="range_no" name="range_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
        <td style="color: #252525;width: 115px;text-align: right">索赔单号： </td>
         <td nowrap>
          <input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">退赔类型： </td>
         <td nowrap>
          	<select name="diy_flag" class="short_sel">
          		<option value="1">手工单据</option>
          		<option value="0" selected="selected">系统单据</option>
          	</select>
         </td>
        <td style="color: #252525;width: 115px;text-align: right"> </td>
         <td nowrap>
         </td>
       </tr>
       
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="queryDate()">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="goToAdd();">
            &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="手工新增"  onClick="gotoDiyOutPartInit();">
         </td>
       </tr>
        <tr class="table_list_row1">
			<td >出库总数据量：<span id="a1"></span></td>
			<td >实物配件总数量：<span id="a2"></span></td>
			<td >关联件数量：<span id="a3"></span></td>
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
	function queryDate(){
		__extQuery__(1);
		 checkSum();
	}


   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutListByCondition.json?type=1";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'OUT_NO', align:'center',renderer:myLink},
  				{header: "退赔类型", dataIndex: 'DIY_FLAG', align:'center',renderer:diyFlag},
  				{header: "退赔单号", dataIndex: 'RANGE_NO', align:'center'},
  				{header: "线下单号", dataIndex: 'LINE_NUM', align:'center'},
  				{header: "出库人", dataIndex: 'NAME', align:'center'},
  				{header: "出库时间",dataIndex: 'OUT_TIME',align:'center'},
  				{header: "出库类型", dataIndex: 'OUT_TYPE', align:'center',renderer:getItemValue},
  				{header: "供应商编码", dataIndex: 'SUPPLY_CODE',align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  				{header: "出库数据量", dataIndex: 'ALL_AMOUNT', align:'center'},
  				{header: "实物配件数量", dataIndex: 'AMOUNT', align:'center'},
  				{header: "关联件数量", dataIndex: 'RELATION_AMOUNT', align:'center'},
  				{header: "关联退赔单", dataIndex: 'RELATIONAL_OUT_NO', align:'center',renderer:myLink2},
  				{header: "手工添加数", dataIndex: 'HAND_MARK', align:'center'}
  		      ];
  		      
  		      __extQuery__(1);
  		    checkSum();
  		    function diyFlag(value,meta,record){
		    	var str="";
		    	if(null==value || ""==value){
		    		str="系统单据";
		    	}else{
		    		str="手工单据";
		    	}
  		    	return String.format(str);
  		    }
  		    
  	function myLink(value,meta,record){
  	var flag = record.data.OUT_TYPE;
  	var relNo=record.data.RELATIONAL_OUT_NO;
  	var delflag = record.data.DEL_FLAG;
  	var diy_flag = record.data.DIY_FLAG;
  	var RANGE_NO = record.data.RANGE_NO;
  	var str="";
  	if(flag==95331001 && relNo==null){
  		str +="<a href='#' onClick='printRenge(\""+value+"\");'>[打印退赔单]</a>";
  		str +="<a href='#' onClick='saveRenge(\""+value+"\");'>[退赔单信息]</a>";
  	}
  	if(delflag!="2"){
  		str +="<a href='#' onClick='delOutDetail(\""+value+"\");'>[取消出库]</a>";
  		str +="<a href='#' onClick='AssociationLine(\""+RANGE_NO+"\");'>[关联线下单号]</a>";
  		
  	  	}
  	if("1"!=diy_flag){
  		str +="<a href='#' onClick='outDetail2(\""+value+"\");'>[出库明细]</a>";
  		str +="<a href='#' onClick='Handadd(\""+value+"\");'>[手工添加]</a>";
  	}
  		return String.format( str);
	}

  	function  AssociationLine(value){
  		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/AssociationLineAdd.do?rang_no='+value,600,400);//线下单号新增

  	}

	
    //手工添加
	function Handadd(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outDetail2.do?out_no="+value+"&type=add";
       	fm.method="post";
       	fm.submit();
	}
  	function myLink2(value,meta,record){
  	  	if(value!=null && value!=""){
  	  	var str ="<a href='#' onClick='saveRenge2(\""+value+"\");'>["+value+"]</a>";
  	  	  	}else{
		str="";
  	  	  	  	}
  		
  	  	return String.format( str);
  		}
	//生成退赔单
	function saveRenge(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveRengePer.do?cType=1&out_no="+value;
       	fm.method="post";
       	fm.submit();
	}
	//生成退赔单
	function saveRenge2(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveRengePer.do?cType=0&out_no="+value;
       	fm.method="post";
       	fm.submit();
	}
	//删除出库明细
	function delOutDetail(value){
		 MyConfirm("确定取消此次出库？",delOut,[value]);
		}
	  function delOut(str){
		 	 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/delOutOfStore.json?outNo="+str;
		 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
		   }
		   //签收回调处理
		  function afterCall(json){
		   	var retCode=json.updateResult;
		    if(retCode!=null&&retCode!=''){
		      if(retCode=="updateSuccess"){
		    	    MyAlert("删除成功!");
		 		__extQuery__(1);
		      }else {
		    	    MyAlert("删除失败!请联系管理员!");
		     }
		   }
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
	   OpenHtmlWindow('<%=contextPath%>/jsp/claim/oldPart/newOutStorageAddInit.jsp',400,200);
   }
   function chooseType(val){
	   if(val=="1"){
		   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addPageList.do";
		   fm.submit();
	   }else if(val=="2"){
		   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addPageListByMix.do";
		   fm.submit();
	   }
   }
	
   
   function getSum(){
	   
	   
	   var url="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutListByConditionSum.json?type=1"
		sendAjax(url,function(json){
			
			
			
		},'fm1');
	   
   }
   
   
   function checkSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutListByConditionSum.json",function(json){
		document.getElementById("a1").innerHTML = json.valueMap.ALL_AMOUNT_SUM == null ? '0' : json.valueMap.ALL_AMOUNT_SUM;//工时费
		document.getElementById("a2").innerHTML = json.valueMap.AMOUNT_SUM == null ? '0' : json.valueMap.AMOUNT_SUM;
		document.getElementById("a3").innerHTML = json.valueMap.RELATION_AMOUNT_SUM == null ? '0' : json.valueMap.RELATION_AMOUNT_SUM;
		},'fm');
		
	}
   function gotoDiyOutPartInit(){
	   window.location.href='<%=contextPath%>/OutStoreAction/gotoDiyOutPartInit.do';
   }
</script>
</BODY>
</html>