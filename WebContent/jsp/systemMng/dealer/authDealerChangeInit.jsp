<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商信息变更</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理&gt;个人信息管理&gt;经销商信息变更</div>
   <table  class="table_query">
          <tr>
              <td class="table_query_2Col_label_6Letter">单据编号：</td>
              <td>
				<input name="code" id="code" type="text" class="middle_txt"/>
              </td>
              <td class="table_query_2Col_label_6Letter">申请单状态：</td>
              <td>
				<script type="text/javascript">
					genSelBoxExp("status",<%=Constant.DEALER_CHANGE%>,"<%=Constant.DEALER_CHANGE_02%>",false,"short_sel",'',"false",'<%=Constant.DEALER_CHANGE_01%>,<%=Constant.DEALER_CHANGE_04%>');
		    	</script>
              </td>
          </tr>
          <tr>
         	  <td align="right" nowrap="true">审核时间：</td>
			  <td align="left" nowrap="true">
				<input type="text" name="startDate" id="startDate"
	             value="" type="text" class="short_txt" 
	             datatype="1,is_date,10" group="startDate,endDate" 
	             hasbtn="true" callFunction="showcalendar(event, 'startDate', false);"/>
	             &nbsp;至&nbsp;
	 			<input type="text" name="endDate" id="endDate" 
	 			value="" type="text" class="short_txt" datatype="1,is_date,10" 
	 			group="startDate,endDate" 
	 			hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
			</td>
           </tr>
		   <tr>    
		   <td colspan="6" align="center">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
           </td></tr>
       
  </table>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/sysmng/dealer/DealerInfo/queryAuthDealerChange.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "单据编号",sortable: false,dataIndex: 'DEALER_NUM',align:'center'},				
				{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},
				{header: "变更日期",sortable: false,dataIndex: 'DEALER_TIME',align:'center'},
				{header: "申请状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'DEALER_CHANGE_ID',renderer:myLink ,align:'center'}
		      ];
	
	//修改的超链接设置
	function myLink(value,meta,record){
		if(record.data.STATUS==<%=Constant.DEALER_CHANGE_02%>){
			return String.format("<a href=\"#\" onclick='viewDetailInfo(\""+value+"\")'>[审核]</a>");
		}
		if(record.data.STATUS==<%=Constant.DEALER_CHANGE_03%>){
			return String.format("<a href=\"#\" onclick='detailInfo(\""+value+"\")'>[明细]</a>");
		}
	}

	function viewDetailInfo(id){
		window.location="<%=contextPath%>/sysmng/dealer/DealerInfo/authInfo.do?id="+id;
	}
	function detailInfo(id){
		window.location="<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfo.do?id="+id;
	}
	function reported(id){
		MyConfirm("是否确认审核？",reportChange,[id]);
	}
	function reportChange(id){
		makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/reported.json?id='+id,showResult,'fm');
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			 __extQuery__(1);
		}else{
			MyAMyAlert("上报失败!");
		}
	}
//关闭方法：
function closeNews(str){
	MyConfirm("是否确认关闭？",col,[str]);
}  
//关闭
function col(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/closeNews.json?newsId='+str,colBack,'fm','');
}
//作废
function can(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/cancelNews.json?newsId='+str,cancelCall,'fm','');
}
//删除回调方法：
function colBack(json) {
	if(json.flag!= null && json.flag==true) {
		MyAlert("关闭成功！");
		__extQuery__(1);
	} else {
		MyAlert("关闭失败！请联系管理员！");
	}
}  
function cancelCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert("作废成功！");
		__extQuery__(1);
	} else {
		MyAlert("作废失败！请联系管理员！");
	}
}  
doInit();
</script>
</body>
</html>