<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="refreshOnload();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理</div>
<!-- 查询条件 begin -->
<form method="post" name ="fm" id="fm">
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">模板编号：</td>
      	<td width="15%" nowrap="true">   
      		<input type="text"  name="templet_no" id="templet_no" maxlength="30" class="middle_txt"/> 	
         </td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">模板名称：</td>
      	<td width="15%" nowrap="true">
	      	<input type="text"  name="templet_name" id="templet_name" maxlength="30" class="middle_txt"/>
	   	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">活动编号：</td>
		<td width="15%" nowrap="true">
			 <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" maxlength="30" />
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">活动名称：</td>
      	<td width="15%" nowrap="true">   
      		<input type="text" class="middle_txt" name="activity_name" maxlength="30"/>
         </td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">活动状态：</td>
      	<td width="15%" nowrap="true">
		  	<script type="text/javascript">
  					genSelBoxExp("status",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","false",'');
  			</script>
	   	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">活动主題：</td>
		<td width="15%" nowrap="true">
			 <input type="text" readonly="readonly" name="subjectName" id="subjectName" class="middle_txt"/>
			<input type="hidden" name="subjectId" id="subjectId"/>
			<input type="button" class="mini_btn" value="..." onclick="showsubjectId('subjectName','subjectId');"/>
           	<input type="button" class="normal_btn" value="清除" onclick="wrapOut();"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    	    <input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = '<%=contextPath%>/ActivityAction/activityList.json?query=true';
				
	var title = null;

	var columns = [
					{header:'序号',renderer:getIndex,align:'center'},
					{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:opear,align:'center'},
					{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
					{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
					{header: "主题编号",dataIndex: 'SUBJECT_NO' ,align:'center'},
					{header: "活动类型",dataIndex: 'ACTIVITY_TYPE' ,align:'center',renderer:getItemValue},
					{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
					{header: "模板编号",dataIndex: 'TEMPLET_NO' ,align:'center'},
					{header: "模板名称",dataIndex: 'TEMPLET_NAME' ,align:'center'},
					{header: '活动状态',dataIndex:'STATUS',align:'center',renderer:getItemValue}
	              ];
	function wrapOut(){
	  $('subjectName').value = '';
	  $('subjectId').value = '';
	}
	function opear(value,meta,record){
		var status = record.data.STATUS;
		var templet_id = record.data.TEMPLET_ID;
		var url="";
	    if(status==10681002){
	    	var urlView="<%=contextPath%>/ActivityAction/activityintoOthers.do?activity_id="+value+"&id="+templet_id;
	    	//url+="<a href='#' onclick='updateAfter(this,\""+ value +"\")';'>[修改]</a>";
	    	url+="<a href='"+urlView+"'>[提报新增]</a>";
	    }
    	var urlView="<%=contextPath%>/ActivityAction/activityView.do?activity_id="+value+"&id="+templet_id;
	    url+="<a href='"+urlView+"'>[明细]</a>";
	    if(status==10681001){
	    	url+="<a href='#' onclick='cancel(\""+ value +"\",\""+ templet_id +"\")';'>[作废]</a>";
	    	var urlUpdate="<%=contextPath%>/ActivityAction/activityUpdateInit.do?activity_id="+value+"&id="+templet_id;
	    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
	    }
		return String.format(url);
	}
	function cancel(activity_id,templet_id){
		MyConfirm("是否确认作废？",cancelSure,[activity_id,templet_id]);
	}
	function cancelSure(activity_id,templet_id){
		makeNomalFormCall('<%=contextPath%>/ActivityAction/cancelAcSure.json?activity_id='+activity_id+'&templet_id='+templet_id,returnBack,'fm','queryBtn');
	}
	function showsubjectId(subjectName,subjectId){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/subjectName.jsp',800,460);
	}
	function myRadio(subject_id,subject_no,subject_name){
		$('subjectId').value=subject_id;
		$('subjectName').value=subject_name;
	}
	function add(){
		window.location.href='<%=contextPath%>/ActivityAction/activityAdd.do';
	}
	
	//回调函数
	function returnBack(json){
		var succ = json.succ;
		if(succ==1){
			__extQuery__(1);
			MyAlert("作废成功！");
		}else{
			MyAlert("作废失败！请联系管理员！");
		}
	}
	
</script>
<!--页面列表 end -->
</body>
</html>