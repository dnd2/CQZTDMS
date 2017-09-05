<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>应用知识库</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
<input type=hidden name='listModelGroup' id='listModelGroup' value='${listModelGroup }'/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;应用知识库</div>
   <table  class="table_query">
          <tr>
              <td class="table_query_2Col_label_6Letter">单据编码：</td>
              <td>
				<input name="newsCode" id="newsCode" datatype="1,is_null,30"  type="text" class="middle_txt"/>
              </td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
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
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td class="table_query_2Col_label_6Letter">标题：</td>
            <td>
				<input name="title" value="" type="text" class="middle_txt"/>
            </td> 
           </tr>
           <tr>
	           	<td align="right">单据状态：</td>
	 			<td align="left" nowrap="true">
				<script type="text/javascript">
					 genSelBoxExp("NEWS_STATUS",<%=Constant.NEWS_STATUS%>,"<%=Constant.NEWS_STATUS_1%>",true,"short_sel","","false","<%=Constant.NEWS_STATUS_2%>");
		    	</script>
			</td>
			<c:choose>
			<c:when test="${!empty listModelGroup}">
				<td align="right">车型组：</td>
				<td align="left" nowrap="true">
					<select id="modelGroupSel" name="modelGroupSel">
						<option value="" selected="selected">-请选择-</option>
						<option value="0">所有</option>
						<c:forEach items="${listModelGroup}" var="model">
							<option value="${model.wrgroupId }">${model.wrgroupName }</option>
						</c:forEach>
					</select>
				</td>
				<td align="right">维修部位：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
							genSelBoxExp("modelPartSel",<%=Constant.REPAIR_PART%>,"",true,"short_sel","","false",'');
					</script>
				</td>
			</c:when>
			<c:otherwise>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			</c:otherwise>
			</c:choose>
           </tr>
		   <tr>    
		   <td colspan="6" align="center">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
           </td></tr>
       
  </table>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsQuary1.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "单据编码",sortable: false,dataIndex: 'CODE',align:'center'},				
				{header: "发表人",sortable: false,dataIndex: 'VOICE_PERSON',align:'center'},
				{header: "发表日期",sortable: false,dataIndex: 'PUBLISHED_DATE',align:'center',renderer:formatDate},
				{header: "标题",sortable: false,dataIndex: 'TITLE',align:'center'},
				{header: "单据状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
	if($('listModelGroup').value!=''){		      
		columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "单据编码",sortable: false,dataIndex: 'CODE',align:'center'},				
				{header: "发表人",sortable: false,dataIndex: 'VOICE_PERSON',align:'center'},
				{header: "发表日期",sortable: false,dataIndex: 'PUBLISHED_DATE',align:'center',renderer:formatDate},
				{header: "标题",sortable: false,dataIndex: 'TITLE',align:'center'},
				{header: "车型组",sortable: false,dataIndex: 'WRGROUP_ID',align:'center',renderer:myHref},
				{header: "维修部位",sortable: false,dataIndex: 'MODEL_PART',align:'center',renderer:getItemValue},
				{header: "单据状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
		     }
//设置超链接  begin      
	
	function myHref(value,meta,record){
		
		var modelGroupId = record.data.WRGROUP_ID;
		if('0'==modelGroupId){
			modelGroupId = "所有";	
		}
		
		return modelGroupId;
	}
	
	//修改的超链接设置
	function myLink(value,meta,record){
		//<a href=\"#\" onclick='cancel(\""+value+"\")'>[作废]</a>
		if(record.data.STATUS==<%=Constant.NEWS_STATUS_3%>){
			return String.format(
					"<a href=\"#\" onclick='viewNews(\""+value+"\")'>[查看]</a>"
			)
		}
		if(record.data.STATUS==<%=Constant.NEWS_STATUS_1%>&&$('logonUserId').value==record.data.CREATE_BY){
	    	return String.format(
	    	         "<a href=\"#\" onclick='viewNews(\""+value+"\")'>[查看]</a><a href=\"<%=contextPath%>/claim/basicData/HomePageNews/modifyNews1.do?newsId="
	    					+ value + "\">[修改]</a><a href=\"#\" onclick='closeNews(\""+value+"\")'>[关闭]</a>");
		}
		else{
			return String.format(
					"<a href=\"#\" onclick='viewNews(\""+value+"\")'>[查看]</a>"
			)
		}
		    		    		
}

	
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	function viewNews(value){
		 location="<%=contextPath%>/claim/basicData/HomePageNews/viewNews1.do?comman=1&newsId="+value;
		 
	}
//设置超链接 end
  //新增
  
  function subFun(){
    fm.action="<%=contextPath%>/claim/basicData/HomePageNews/addApplicationKnowledgeBase.do";
    fm.submit();  
  }
//关闭方法：
function closeNews(str){
	MyConfirm("是否确认关闭？",col,[str]);
}  
function cancel(str){
	MyConfirm("是否确认作废？",can,[str]);
}
//关闭
function col(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/closeApplicationKnowledgeBase.json?newsId='+str,colBack,'fm','');
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