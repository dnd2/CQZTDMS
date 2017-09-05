<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
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
function viewNews(value){
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
}

function subModify(str) 
{
	     MyConfirm("是否修改？",modifyActivity,[str]);
} 


function modifyActivity(str){
	var modifyEndDate = $("modifyEndDate").value;
	var modifyFactEndDate = $("modifyFactEndDate").value;
	//原来的信息录入结束日期、活动开展结束日期
	var endDate = $("endDate").value;
	var factEndDate = $("factEndDate").value;
	if(modifyEndDate == ""){
		MyAlert("信息录入结束日期不能为空");
		return;
	}
	if(modifyFactEndDate == ""){
		MyAlert("活动开展结束日期不能为空");
		return;
	}
	if(!checkDate(endDate,modifyEndDate )){
		MyAlert("新设定的信息录入结束日期必须大于等于原来设定的信息录入结束日期");
		return;
	}
	if(!checkDate(factEndDate, modifyFactEndDate)){
		MyAlert("新设定的信息录入结束日期必须大于等于原来设定的信息录入结束日期");
		return;
	}
	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageModify.json?subject_id='+str,returnBack,'fm');
}

//删除回调函数
function returnBack(json){
	var update = json.returnValue;
	if(update==1){
		MyAlert("修改成功");
		$("endDate").value = $("modifyEndDate").value;
		$("factEndDate").value = $("modifyFactEndDate").value;
	}else{
		MyAlert("修改失败");
	}
}


</script>
</HEAD>
<BODY >
<DIV style="Z-INDEX: 200; POSITION: absolute; PADDING-BOTTOM: 1px; PADDING-LEFT: 1px; PADDING-RIGHT: 1px; DISPLAY: none; BACKGROUND: #ffcc00; TOP: 4px; PADDING-TOP: 1px" id=loader></DIV>
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
    <TR>
      <TD></TD>
    </TR>
    <TR>
      <TD height=30><DIV class=navigation><IMG src="../../../img/nav.gif">&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动主题查询 </DIV></TD>
    </TR>
  </TBODY>
</TABLE>
<FORM id=fm method=post name=fm>
	<input type ="hidden" id="endDate" value = "${endDate}"/>
	<input type ="hidden" id="factEndDate" value = "${factEndDate}"/>
  <TABLE class=table_edit>
    <TBODY>
      <TR>
        <TH colSpan=6><IMG class=nav src="../../../img/subNav.gif">服务活动主题信息</TH>
      </TR>
      <TR>
        <TD width="20%" align=right>主题编号：</TD>
        <TD width="30%">${activitySubjectPO.subjectNo}</TD>
        <TD width="20%" align=right>主题名称：</TD>
        <TD width="30%">${activitySubjectPO.subjectName}</TD>
      </TR>
      <TR>
        <TD width="10%" align=right>活动类型：</TD>
        <TD width="20%">
        <script type='text/javascript'>
				       var activityType=getItemValue('${activitySubjectPO.activityType}');
				       document.write(activityType) ;
				     </script>
				</TD>
        <TD width="10%" align=right>信息录入日期：</TD>
        <TD width="20%"><DIV align=left>
        ${stratDate}<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt"></span>&nbsp;至&nbsp;<input name="modifyEndDate" type="text" class="short_time_txt" value="${endDate}" id="modifyEndDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'modifyEndDate', false);" /></DIV></TD>
      </TR>
      <TR>
        <td align="right">单台次活动次数：</td>
        <td>${activitySubjectPO.activityNum}</td>
        <td align="right">信息录入后&nbsp;&nbsp;</td>
        <td>${activitySubjectPO.days} 天内上传总结 </td>
      </TR>
      <TR>
        <td align="right">责任人：</td>
        <td>${username}</td>
        <TD width="10%" align=right>活动开展日期：</TD>
        <TD width="20%"><DIV align=left>
        ${factStartDate}<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt"></span>&nbsp;至&nbsp;<input name="modifyFactEndDate" type="text" class="short_time_txt" value="${factEndDate}" id="modifyFactEndDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'modifyFactEndDate', false);" /></DIV></TD>
      </TR>
    </TBODY>
  </TABLE>
  <TABLE width="100%" class="table_list">
    <TBODY>
      <TR>
        <TD align="left">&nbsp;</TD>
      </TR>
      <TR>
        <TD colSpan=6><TABLE id=t_news class=table_list_line border=1>
            <TBODY>
              <tr>
                <th align="middle" nowrap="nowrap">NO </th>
                <th align="middle" nowrap="nowrap">编码 </th>
                <th align="middle" nowrap="nowrap">新闻名称</th>
              </tr>
               <c:if test="${!empty listNews}">
	        	<c:forEach var="newDetail" items="${listNews}" varStatus="vs">
	        		<tr >
			          <th width="50" align="center" nowrap="nowrap" >1</th>
			          <th width="220" align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>${newDetail.NEWS_CODE }</a> </th>
			          <th width="400" align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</th>
			        </tr>
	        	</c:forEach>
	        </c:if>
            </TBODY>
          </TABLE></TD>
      </TR>
    </TBODY>
  </TABLE>
  <BR>
  <TABLE width="100%">
    <TBODY>
      <TR>
        <TD colSpan=4 align="center"><INPUT class=normal_btn onclick=JavaScript:history.back() value=返回 type=button name=bt_back>&nbsp;&nbsp;<INPUT class=normal_btn onclick="subModify('${activitySubjectPO.subjectId}');" value="修改" type=button name=bt_back></TD>
      </TR>
      <TR>
        <TD colSpan=4 align=middle></TD>
      </TR>
    </TBODY>
  </TABLE>
</FORM>
<DIV style="VISIBILITY: hidden" id=checkMsgDiv0 class=tipdiv></DIV>

