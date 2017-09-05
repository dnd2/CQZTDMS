<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
//修改返回---查询出修改记录并返回主页面
function refreshOnload(){
        var flag='<%=request.getAttribute("flag")%>';
	    if("onFlag"==flag){
	        window.onload=__extQuery__(1);
	    }
}
</script>
</head>
<BODY onload=refreshOnload();>
<DIV style="Z-INDEX: 200; POSITION: absolute; PADDING-BOTTOM: 1px; PADDING-LEFT: 1px; PADDING-RIGHT: 1px; DISPLAY: none; BACKGROUND: #ffcc00; TOP: 4px; PADDING-TOP: 1px" id=loader></DIV>
<DIV class=navigation><IMG alt="" src="../../../img/nav.gif">&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动主题管理</DIV>
<!-- 查询条件 begin -->
<FORM id=fm method=post name=fm>
<input type="hidden"  name="typeleng" value="${typeleng}">
  <TABLE class=table_query>
    <TBODY>
      <TR>
        <TD width="10%" align=right>主题编号： </TD>
        <TD width="20%"><INPUT id=activityCode class=middle_txt name=activityCode datatype="1,is_digit_letter,25"></TD>
        <td width="10%" align="right">活动类型：</td>
        <td width="20%">
        <c:if test="${typeleng == 0}">
	         <script type="text/javascript">
			genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",true,"short_sel","","fales",'0');
	 		</script>
 		</c:if>
 		 <c:if test="${typeleng == 1}">
	         <script type="text/javascript">
			genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",true,"short_sel","","fales",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
	 		</script>
 		</c:if>
		</td>
      </TR>
      <TR>
        <TD width="10%" align=right>主题名称：</TD>
        <TD><INPUT class=middle_txt name=activity_name></TD>
        <TD width="10%" align=right>活动日期：</TD>
        <TD align=left><DIV align=left>
            <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
            <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
            &nbsp;至&nbsp;
            <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
            <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
          </DIV></TD>
      </TR>
      <TR>
        <TD colSpan=11 align=middle><INPUT class=normal_btn onclick=__extQuery__(1); value=查询 type=button name=button>
          <INPUT class=normal_btn onclick="serviceActivityManageAddInit();" value=新增 type=button name=button1></TD>
      </TR>
    </TBODY>
  </TABLE>
  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</FORM>
<!-- 查询条件 end -->
<BR>
<!--页面列表 begin -->
<SCRIPT type=text/javascript>
var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageMotiveQuery.json";
				
	var title = null;

	var columns = [
	               {header:'序号',renderer:getIndex,align:'center'},
				{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
				{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
				{header: "活动类型 ",dataIndex: 'ACTIVITY_TYPE' ,align:'center',renderer:getItemValue},
				{header: "单台次活动次数",dataIndex: 'ACTIVITY_NUM' ,align:'center'},
				{header: "活动开始日期",dataIndex: 'SUBJECT_START_DATE' ,align:'center',renderer:getValue},
				{header: "活动结束日期",dataIndex: 'SUBJECT_END_DATE' ,align:'center',renderer:getValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SUBJECT_ID',renderer:serviceActivityManageUpdateInit ,align:'center'}
		      ];
		      //修改/删除/明细的超链接设置
		      //编辑的超链接设置
	function getValue(str)
	{
		var type = str.split(' ');
		return type[0];
	}
	function serviceActivityManageUpdateInit(value,meta,record)
	{
		var value1 = record.data.SUBJECT_ID ;
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageMotiveSelcet.do?activityId="+ value1 + "\">[查看]</a>"+"<a href=\"#\" onclick=\"subChecked("+value1+")\">[删除]</a>"+"<a href=\"#\" onclick=\"modifyActivity("+value1+")\">[修改]</a>");
	}
	
	function modifyActivity(activityId){
		window.location.href = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageModifyPage.do?activityId="+ activityId;
	}
	
	function subChecked(str) 
	{
		     MyConfirm("是否确认删除？",serviceActivityManageDelete,[str]);
	} 
	
	function serviceActivityManageDelete(str)
	{
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageMotiveDelete.json?subject_id='+str,returnBack,'fm','queryBtn');
	}
	
	//删除回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！服务活动添加了此主题！");
		}
	}
	//新增
	function serviceActivityManageAddInit()
	{
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/serviceActivityManageMotiveAddInit.do?typeleng="+${typeleng};
		fm.submit();
	}
	
</SCRIPT>
<!--页面列表 end -->
</BODY>
</HTML>
