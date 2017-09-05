<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<TITLE>问题工单表查询</TITLE>
<% 
	String contextPath = request.getContextPath();
	Date time = new Date();
	Date time1= new Date(time.getYear(), time.getMonth(), 1);
	Date time2= new Date(time.getYear(), time.getMonth()+1, 0);
	DateFormat dateF=new SimpleDateFormat("yyyy-MM-dd");
	String startTime = dateF.format(time1);
	String endTime = dateF.format(time2);
	request.setAttribute("startTime", startTime);
	request.setAttribute("endTime", endTime);
%>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/applyQuery.json";
				
	var title = null;

	var columns = [
				{header: "<input type='checkbox' id='mainBox' onclick='selectBoxs();'/>",width:'5%',dataIndex: 'ID',renderer:myLink2,align:'center'},
				{id:'id',header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'15%', dataIndex: 'DEALER_NAME'},
				{header: "上端工单号", width:'15%', dataIndex: 'RO_NO'},
				{header: "维修类型", width:'10%', dataIndex: 'REPAIR_TYPE_CODE'},
				{header: "车牌号", width:'10%', dataIndex: 'LICENSE'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "车系", width:'15%', dataIndex: 'GROUP_NAME'},
				{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
			    {header: "提报日期", width:'10%', dataIndex: 'DELIVERY_DATE',renderer:formatDate},				
				{header: "问题原因", width:'15%', dataIndex: 'REMARK1',align:'left'},
				{header: "操作",width:'5%',dataIndex: 'ID',renderer:myLink1,align:'center'}		
	      ];
	//添加复选框 begin add by tanv 2013-02-18
	function selectBoxs(){
		var mainBox = document.getElementById("mainBox");
		var smallBoxs = document.getElementsByName("smallBox");
		for(var i=0;i<smallBoxs.length;i++){
			if(mainBox.checked){
				smallBoxs[i].checked = true;
			}else{
				smallBoxs[i].checked = false;
			}
		}
	}
	//创建复选框
	function myLink2(value,meta,record){
		var value = record.data.ID;
		var id='box'+value;
		var noPartsFlag = record.data.INSURATION_NO;
		return String.format("<input type='checkbox' name='smallBox' onclick='checkStatus();' id='"+id+"' value='"+value+"'/>");		 
	}
	//复选框状态判定
	function checkStatus(){
		var mainBox = document.getElementById("mainBox");
		var smallBoxs = document.getElementsByName("smallBox");
		var flag = true;
		for(var i=0;i<smallBoxs.length;i++){
			if(smallBoxs[i].checked == false){
				flag = false;
				break;
			}
		}
		mainBox.checked=flag;
	}
	//废除问题工单
	function delPro(){
		document.getElementById("delButton").disabled = 'disabled';
		var ids = '';
		var smallBoxs = document.getElementsByName("smallBox");
		if(smallBoxs){
			for(var i=0;i<smallBoxs.length;i++){
				if(smallBoxs[i].checked){
					ids = ids+smallBoxs[i].value+',';
				}
			}
		}
		if(ids==''){
			document.getElementById("delButton").disabled = '';
			MyAlert("未选择任何问题工单！") ;
			return;
		}
		MyConfirm("是否删除?",delProConfirm,[ids]);
		document.getElementById("delButton").disabled = '';
	}
	function delProConfirm(ids){
		var urlBatch = "<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/delProblemRoBatch.json" ;
		makeCall(urlBatch, showResult, {idStr:ids}) ;
	}
	//添加复选框 end add by tanv 2013-02-18
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
		var rostatus=record.data.roStatus;
		var noPartsFlag = record.data.INSURATION_NO;
		if(noPartsFlag){
			if('50122501'==noPartsFlag){
				return String.format("<a href='#' onclick='addNoPartItems("+value+")'>[添加无零件]</a><a href='#' onclick='sel("+value+")'>[明细]</a><a href='#' onclick='toDel("+value+")'>[废弃]</a>");		 
			}else{
				return String.format("<a href='#' onclick='sel("+value+")'>[明细]</a><a href='#' onclick='toDel("+value+")'>[废弃]</a>");		 
			}
		}else{
			return String.format("<a href='#' onclick='sel("+value+")'>[明细]</a><a href='#' onclick='toDel("+value+")'>[废弃]</a>");		 
		}
	}
		
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/queryDetail.do?ID='+value,800,500);
	}
	//添加无零件项页面 add by tanv 2012-12-26
	function addNoPartItems(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/queryDetailForAddNPItem.do?ID='+value,800,500);
	}
	
	function toDel(value){
		MyConfirm("是否删除?",del,[value]);
	}
	
	function del(value){
		var url = "<%=contextPath%>/feedbackmng/query/ProblemRepairOrderQuery/delProblemRo.json" ;
		makeCall(url, showResult, {ID:value}) ;
	}
	
	function showResult(json) {
		var flag = json.flag ;
		if(flag > 0) {
			MyAlert("废弃成功！") ;
			__extQuery__(1);
		} else {
			MyAlert("废弃失败！") ;
		}
	}
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;信息反馈查询&gt;问题工单表查询</div>
  <form method="post" name = "fm" id="fm">
	<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>

       <TABLE align=center width="95%" class="table_query" >

          <tr>
            <td class="table_query_2Col_label_5Letter">工单号：</td>
            <td ><input id="RoNo" name="RoNo" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18"/></td>
            <td class="table_query_2Col_label_5Letter">车型：</td>
            <td><script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='MODEL_ID' name='MODEL_ID'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script></td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter">提报时间：</td>
              <td  nowrap="nowrap">
              <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID" value="${startTime}" datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID" value="${endTime}" datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
  			</td>
  			<td  class="table_query_2Col_label_5Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","true",'');
	       	</script>
 		    </td> 
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter">VIN：</td>
            <td  nowrap="nowrap" >
  				<input type="text" name="VIN" id="VIN"  class="middle_txt" VALUE=""/>
  			</td>
  			<td class="table_query_2Col_label_5Letter">是否全自费：</td>
  			<td class="table_query_2Col_label_5Letter">
  				<select id="isSelfPayAll" name="isSelfPayAll" style="width:100px">
  					<option value="-1">-请选择-</option>
  					<option value="0">否</option>
  					<option value="1">是</option>
  				</select>
  			</td>
          </tr>
          <tr>
            <td colspan="6" align="center" nowrap>
            	<input class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            	<input class="normal_btn" type="BUTTON" name="button2" id="delButton" value="废弃"  onclick="delPro();" />
            </td>
          </tr>
  </table>
    <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br>
</BODY>
</html>