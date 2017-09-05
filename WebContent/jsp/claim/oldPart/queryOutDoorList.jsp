<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库开票</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库开票</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" callFunction="javascript:MyAlert();"></td>
         <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="SUPPLY_CODE" name="SUPPLY_CODE" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       
        <!--   <td style="color: #252525;width: 115px;text-align: right">出库时间：</td>
         <td align="left" nowrap="true">
			<input name="out_start_date" type="text" class="short_time_txt" id="out_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="out_end_date" type="text" class="short_time_txt" id="out_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'out_end_date', false);" /> 
		</td>	-->
        
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutDoorList.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'OUT_NO', align:'center',renderer:myLink},
  				{header: "出门证号", dataIndex: 'OUT_NO', align:'center'},
  				{header: "操作人", dataIndex: 'NAME', align:'center'},
  				{header: "操作时间",dataIndex: 'OUT_TIME',align:'center'},
  				{header: "供应商编码", dataIndex: 'OUT_COMPANY_CODE',align:'center'},
  				{header: "供应商名称", dataIndex: 'OUT_COMPANY', align:'center'}
  		      ];
  		      
  		      __extQuery__(1);
  	function myLink(value,meta,record){
  	var flag1 = record.data.DEL_FLAG;
  	var flag2 = record.data.DEL_FLAG2;
  	var str="";
  		str="<a href='#' onClick='outPartDetail(\""+value+"\",\""+record.data.OUT_COMPANY_CODE+"\");'>[明细]</a>"+
  		"<a href='#' onClick='detailDown(\""+value+"\",\""+record.data.OUT_COMPANY_CODE+"\");'>[导出]</a>"+
  		"<a href='#' onClick='detailPrint(\""+value+"\",\""+record.data.OUT_COMPANY_CODE+"\");'>[打印出门证]</a>";
  		if(flag1==0 && flag2==1){
  		str+="<a href='#' onClick='outDel(\""+value+"\",\""+record.data.OUT_COMPANY_CODE+"\");'>[删除]</a>";
  		}
  		str+="<a href='#' onClick='outPartNoticeInfo(\""+value+"\",\""+record.data.OUT_COMPANY_CODE+"\");'>[通知单明细]</a>";
  		return String.format( str);
	}
	//出门证删除
	function outDel(value,supplayCode){
		MyConfirm("确定删除？",outDel2,[value]);
	
	   }
	   
	function outDel2(outNo){
	var url="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outDel.json?out_no="+outNo;
 	 makeNomalFormCall(url,delAfter,'fm','');
	}
	
	function delAfter(json){
		var retCode=json.updateResult;
		if(retCode=="ok"){
			MyAlert("删除成功!");
		}else{
			MyAlert(json.strInfo);
		}
		 __extQuery__(1);
	}
	//出门证明细
	function outPartDetail(value,supplayCode){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outDetail.do?out_no="+value+"&code="+supplayCode;
       fm.method="post";
       fm.submit();
	}
	//出门证打印
	function detailPrint(value,supplayCode){
	 window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/detailPrint.do?out_no='+value+'&code='+supplayCode,"旧件出门证打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	//var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkDoor.json?out_no="+value+"&code="+supplayCode+"&type=0";
 	// makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}
	//出门证导出
	function detailDown(value,supplayCode){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/detailDown.json?out_no="+value+"&code="+supplayCode;
       	fm.method="post";
       	fm.submit();
	//var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkDoor.json?out_no="+value+"&code="+supplayCode+"&type=5";
 	// makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}
	//通知单生成
	function outPartNoticeInfo(value,supplayCode){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+value+"&code="+supplayCode;
       	fm.method="post";
       	fm.submit();
	//var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkDoor.json?out_no="+value+"&code="+supplayCode+"&type=1";
 	// makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}
	function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	    MyAlert("请先生成出门证!");
      }else if(json.type==0){
    	  window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/detailPrint.do?out_no='+json.outNo+'&code='+json.supplayCode,"旧件出门证打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
     } else if(json.type==1){
       	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+json.outNo+"&code="+json.supplayCode;
       	fm.method="post";
       	fm.submit();
     }else if(json.type==5){
       	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/detailDown.json?out_no="+json.outNo+"&code="+json.supplayCode;
       	fm.method="post";
       	fm.submit();
     }
  }
	
	//通知单打印
	function NoticePrint(value,supplayCode){
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkNotice.json?out_no="+value+"&code="+supplayCode;
 	 makeNomalFormCall(url,afterCall2,'fm','createOrdBtn');
	}
function afterCall2(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	    MyAlert("请先生成通知单!");
      }else {
    	 window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticePrint.do?out_no='+json.outNo+'&code='+json.supplayCode,"旧件索赔通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	} 
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
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addOutDoor.do";
       fm.submit();
   }
</script>
</BODY>
</html>