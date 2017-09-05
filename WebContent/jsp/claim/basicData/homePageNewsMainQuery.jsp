<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		var goPage = document.getElementById("goPage").value;
   		__extQuery__(goPage);
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
	<input type="hidden" name = "goPage" id="goPage" value = "${curPage }" />
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;首页新闻</div>
   <table  class="table_query">
          <tr>
              <td class="table_query_2Col_label_6Letter">单据编码：</td>
              <td>
				<input name="newsCode" id="newsCode" datatype="1,is_null,30"  type="text" class="middle_txt"/>
              </td>
              <td class="table_query_2Col_label_6Letter">发表人：</td>
              <td>
				<input name="person" id="person" datatype="1,is_null,30"  type="text" class="middle_txt"/>
				<input id="logonUserId" type="hidden" value="${LONGON_USER_ID} "/>
              </td>                        
          </tr>
          <tr>
         	  <td align="right" nowrap="true">发表日期：</td>
			  <td align="left" nowrap="true">
				<input type="text" name="APPLY_DATE_START" id="APPLY_DATE_START"
	             value="" type="text" class="short_txt" 
	             datatype="1,is_date,10" group="APPLY_DATE_START,APPLY_DATE_END" 
	             hasbtn="true" callFunction="showcalendar(event, 'APPLY_DATE_START', false);"/>
	             &nbsp;至&nbsp;
	 			<input type="text" name="APPLY_DATE_END" id="APPLY_DATE_END" 
	 			value="" type="text" class="short_txt" datatype="1,is_date,10" 
	 			group="APPLY_DATE_START,APPLY_DATE_END" 
	 			hasbtn="true" callFunction="showcalendar(event, 'APPLY_DATE_END', false);"/>
			</td>
			<td class="table_query_2Col_label_6Letter">标题：</td>
            <td>
            	<input name="title" type="text" class="middle_txt" value=""/>
            </td> 
           </tr>
           <tr>
	           	<td align="right">单据状态：</td>
	 			<td align="left" nowrap="true">
				<script type="text/javascript">
					 genSelBoxExp("NEWS_STATUS",<%=Constant.NEWS_STATUS%>,"<%=Constant.NEWS_STATUS_1%>",true,"short_sel","","false","<%=Constant.NEWS_STATUS_2%>");
		    	</script>
				</td>
				<td align="right">新闻类别：</td>
	 			<td align="left" nowrap="true">
				<script type="text/javascript">
					 genSelBoxExp("NEWS_TYPE",<%=Constant.NEWS_TYPE%>,"",true,"short_sel","","false","");
		    	</script>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsQuary2.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "单据编码",sortable: false,dataIndex: 'NEWS_CODE',align:'center'},				
				{header: "发表人",sortable: false,dataIndex: 'VOICE_PERSON',align:'center'},
				{header: "发表日期",sortable: false,dataIndex: 'NEWS_DATE',align:'center',renderer:formatDate},
				{header: "标题",sortable: false,dataIndex: 'NEWS_TITLE',align:'center'},
				{header: "新闻类别",sortable: false,dataIndex: 'NEWS_TYPE',align:'center',renderer:getItemValue},
				{header: "单据状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'NEWS_ID',renderer:myLink ,align:'center'}
		      ];
	//修改的超链接设置
	function myLink(value,meta,record){
		var str = "<a href=\"#\" onclick='viewNews(\""+value+"\")'>[查看]</a>";//所以用户均可查看

		return String.format(str);		
	}

	
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	function viewNews(value){
		 location="<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=1&newsId="+value;
		 
	}
//设置超链接 end
  //新增
  
  function subFun(){
    fm.action="<%=contextPath%>/claim/basicData/HomePageNews/addNews.do";
    fm.submit();  
  }
//关闭方法：
function closeNews(str){
	MyConfirm("是否确认关闭？",col,[str]);
}  
function cancel(str){
	MyConfirm("是否确认作废？",can,[str]);
}
function dropNews(str){
	MyConfirm("是否确认删除？",drop,[str]);
}
function setTopNews(str){
	MyConfirm("是否确认置顶？",setTop,[str]);
}
function dropTopNews(str){
	MyConfirm("是否取消置顶？",dropTop,[str]);
}
//关闭
function col(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/closeNews.json?newsId='+str,colBack,'fm','');
}
//作废
function can(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/cancelNews.json?newsId='+str,cancelCall,'fm','');
}

//彻底删除
function drop(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/DropNews.json?newsId='+str,dropCall,'fm','');
}
//是否置顶
function setTop(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/setTopNews.json?newsId='+str,setTopCall,'fm','');
}
//是否取消置顶
function dropTop(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/dropTopNews.json?newsId='+str,dropTopCall,'fm','');
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
function dropCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
function setTopCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 置顶成功！");
		__extQuery__(1);
	} else {
		MyAlert("置顶失败！请联系管理员！");
	}
}
function dropTopCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 取消置顶成功！");
		__extQuery__(1);
	} else {
		MyAlert("取消置顶失败！请联系管理员！");
	}
} 
</script>
</body>
</html>