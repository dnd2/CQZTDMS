<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>免费保养模板</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/MainTainAction/keepFitTemplate.json?query=true&status=18041002";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "模板编号", width:'15%', dataIndex: 'KEEP_FIT_NO'},
				{header: "模板名称", width:'15%', dataIndex: 'KEEP_FIT_NAME'},
				{header: "总费用", width:'15%', dataIndex: 'KEEP_FIT_AMOUNT'},
				{header: "模版类型", width:'15%', dataIndex: 'CHOOSE_TYPE',renderer:getItemValue},
				{header: "创建时间", width:'15%', dataIndex: 'CREATE_DATE'},
				{header: "状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue}
	      ];
	      function myLink(value,meta,record){
			return String.format("<input type='radio' name='rd' onclick='getKeepFitData(\""+record.data.ID+"\",\""+record.data.KEEP_FIT_NO+"\",\""+record.data.CHOOSE_TYPE+"\")' />");
	      }
	      function getKeepFitData(id,keep_fit_no,choose_type){
	    	  	var vin=$("vin").value;
	    	  	if(""!=vin){
	    	  		var url='<%=contextPath%>/MainTainAction/checkIsFirst.json?vin='+vin+'&choose_type='+choose_type;
	    	  		sendAjax(url,function(json){
	    	  			var free_times=json.map.free_times;
	    	  			var isFree=json.map.isFree;
	    	  			
	    	  			if(free_times==0){
	    	  				if(isFree=="true"){
		    	  				if("93431001"==choose_type){//首保 强保
			    					sendAjax('<%=contextPath%>/MainTainAction/getKeepFitData.json?id='+id+'&keep_fit_no='+keep_fit_no,backKeepFitData,'fm');
		    	  				}else{
		    	  					MyAlert("提示：该"+vin+"未做首保，先做首保！");
		    	  				}
	    	  				}else{
	    	  					MyAlert("提示：该"+vin+"超期135天或里程大于5000KM只能自费保养！");
	    	  				}
	    	  			}
	    	  			if(free_times!=0){
	    	  				if(isFree=="true"){
		    	  				if("93431002"==choose_type){//赠送
			    					sendAjax('<%=contextPath%>/MainTainAction/getKeepFitData.json?id='+id+'&keep_fit_no='+keep_fit_no,backKeepFitData,'fm');
		    	  				}else{
		    	  					MyAlert("提示：该"+vin+"已经做过首保，请选择赠送保养！");
		    	  				}
	    	  				}else{
	    	  					MyAlert("提示：该"+vin+"超期135天或里程大于5000KM只能自费保养！");
	    	  				}
	    	  			}
	    	  		},'fm');
	    	  	}else{
	    	  		sendAjax('<%=contextPath%>/MainTainAction/getKeepFitData.json?id='+id+'&keep_fit_no='+keep_fit_no,backKeepFitData,'fm');
	    	  	}
	      }
	      function backKeepFitData(json){
		    var labours2 = json.labours2;
		  	var parts2 = json.parts2;
		  	var keep_fit_no=json.keep_fit_no;
	  		if (parent.$('inIframe')){
	 			parentContainer.backKeepFitData(keep_fit_no,labours2,parts2);
	 		}else{
				parent.backKeepFitData(keep_fit_no,labours2,parts2);
			}
			 parent._hide();
	     }
</script>
<!--页面列表 end --> 
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;免费保养模板查询&nbsp;&nbsp;&nbsp;<span style="color: red;font-weight: bold;">提示：请先看是否实销或首保已经做过将查询不出数据</span>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<input class="middle_txt" id="vin"  name="vin" maxlength="30" type="hidden" value="${vin }"/>
<input class="middle_txt" id="in_mileage"  name="in_mileage" maxlength="30" type="hidden" value="${in_mileage }"/>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">模板编号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="keep_fit_no"  name="keep_fit_no" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">模板名称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="keep_fit_name"  name="keep_fit_name" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>