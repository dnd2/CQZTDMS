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
</script>
<SCRIPT type=text/javascript>


    
    function addRow(tableId,newId,newCode,newTitle){
	 	
	 	
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);


		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
			addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'</td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><a href="#" onclick="viewNews('+newId+')">'+newCode+'</a><input type=hidden name="newsId" value="'+newId+'"/></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td>'+newTitle+'</td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" /></td>';
			
			return addTable.rows[length];
		}
    
	function subChecked() {
			if(!submitForm('fm')) {
				return false;
			}
			var activityType= document.getElementById('activityType').value;
			if(activityType.length == 0)
			{
				MyAlert("请选择活动类型");
				return;
			}
			
		     MyConfirm("是否确认添加？",jude);
	}
	function jude()
	{
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageMotivejude.json',updatejudeBack,'fm','');
	}
	function updatejudeBack(json)
	{
		if(json.back == '0')
		{
			MyAlert("主题编号或者主题名称不能重复");
		}else if(json.back == '1')
		{
			add();
		}
	}
	//添加服务活动主数据信息
	function add(){
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/ServiceActivityManageMotiveAdd.json',updateBack,'fm','');
	}
	//修改索赔工时回调方法：
 function updateBack(json) {
	if(json.success != null && json.success == "yes") {
		MyAlertForFun("新增成功！",function(){
		   var typeleng = document.getElementById('typeleng').value; 
			if(typeleng == '1')
			{
				location = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/serviceActivityManageMarkInit.do";
			}else if(typeleng == '0')
			{
				location = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageMotive/serviceActivityManageMotiveInit.do";
			}
			
			
		});
	} else {
		MyAlert("新增失败！请联系管理员！");
	}
}
	
	/*******
		服务活动增加首页新闻连接
	addUser:	xiongchuan 
	addTime:    2011-07-06	
	***/
	function selectMainNew(){
			var tabl=document.all['t_news'];
			if(tabl.children[0].children.length == 2)
			{
				MyAlert("新闻以添加");
			}else
			{
				OpenHtmlWindow('<%=contextPath%>/claim/other/Bonus/newsQuery.do',800,500);
			}
		  	
	}
	function deleteRowConfirm(obj){
		  var tabl=document.all['t_news'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}

	function viewNews(value){
		OpenHtmlWindow("/CHANADMS/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
</SCRIPT>
</HEAD>
<BODY >
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
    <TR>
      <TD></TD>
    </TR>
    <TR>
      <TD height=30><DIV class=navigation><IMG src="../../../img/nav.gif">&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动主题新增</DIV></TD>
    </TR>
  </TBODY>
</TABLE>
<FORM id=fm method=post name=fm>
<input type="hidden" id="typeleng" value="${typeleng}">
  <TABLE class=table_edit>
    <TBODY>
      <TR>
        <TH colSpan=6><IMG class=nav src="../../../img/subNav.gif">服务活动主题信息</TH>
      </TR>
      <TR>
        <td width="20%" align="right">主题编号：</td>
        <td width="30%"><input name="activityName" class="middle_txt" id="activityName"  maxlength="25" datatype="0,is_null,25" /></td>
        <td width="20%" align="right">主题名称：</td>
        <td width="30%">
          <input name="activityName2" class="middle_txt" id="activityName2"  maxlength="25" datatype="0,is_null,25" /></td>
        <!--
		<td width="16%" align="right">活动编号：</td>
		<td width="28%" align="left">
		             系统自动生成(新增时不显示)<font color="red">*</font>
			  <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="0,is_digit_letter,18" > 
		</td>
		-->
      </TR>
      <TR>
        <td width="10%" align="right">活动类型：</td>
        <td width="20%">
        	<c:if test="${typeleng == 0}">
	         <script type="text/javascript">
			genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",false,"short_sel","","fales",'');
	 		</script>
 		</c:if>
 		 <c:if test="${typeleng == 1}">
	         <script type="text/javascript">
			genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",false,"short_sel","","fales",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
	 		</script>
 		</c:if>
        </td>
        <td width="10%" align="right">信息录入日期：</td>
        <td width="20%"><div align="left">
          <input name="checkSDate" class="short_txt" id="checkSDate"  maxlength="10" datatype="0,is_date,10" group="checkSDate,checkEDate" />
          <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
          &nbsp;至&nbsp;
          <input name="checkEDate" class="short_txt" id="checkEDate"  maxlength="10" datatype="0,is_date,10" group="checkSDate,checkEDate" />
          <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
           </div></td>
          
      </TR>
      <TR>
        <td align="right">单台次活动次数：</td>
        <td>
          <input id="single_num" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  class="middle_txt" name="single_num" value="1" datatype="0,is_digit,4" maxlength="4" /></td>
        <td align="right">信息录入后&nbsp;&nbsp;</td>
        <td><input id="uploadPrePeriod" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  class="middle_txt" value="10"  maxlength="4" name="uploadPrePeriod"/>
          <span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span> 天内上传总结 </td>
      </TR>
      <TR>
        <td align="right">责任人：</td>
        <td><select id="select2" class="short_sel" name="select2">
        <c:forEach var="user" items="${user}" >
        <option value="${user.user_id}" >
          <c:out value="${user.name}"/>
          </option>
        </c:forEach>	
        </select></td>
        <td width="10%" align="right">活动开展日期：</td>
        <td width="20%"><div align="left">
          <input name="factStartDate" class="short_txt" id="factStartDate"  maxlength="10" datatype="0,is_date,10" group="factStartDate,factEndDate" />
          <input class="time_ico" onclick="showcalendar(event, 'factStartDate', false);" value=" " type="button" />
          &nbsp;至&nbsp;
          <input name="factEndDate" class="short_txt" id="factEndDate"  maxlength="10" datatype="0,is_date,10" group="factStartDate,factEndDate" />
          <input class="time_ico" onclick="showcalendar(event, 'factEndDate', false);" value=" " type="button" />
           </div></td>
      </TR>
    </TBODY>
  </TABLE>
  <TABLE width="100%" class="table_list">
    <TBODY>
      <TR>
        <TD align="left"><INPUT class=normal_btn onclick="selectMainNew();" value=新增 type=button name=add4></TD>
      </TR>
      <TR>
        <TD colSpan=6><TABLE id=t_news class=table_list_line border=1>
            <TBODY>
              <tr>
                <th width="50" align="middle" nowrap="nowrap">NO </th>
                <th width="220" align="middle" nowrap="nowrap">编码 </th>
                <th width="400" align="middle" nowrap="nowrap">新闻名称</th>
                <th width="80" align="middle" nowrap="nowrap">操作 </th>
              </tr>
            </TBODY>
          </TABLE></TD>
      </TR>
    </TBODY>
  </TABLE>
  <BR>
  <TABLE width="100%">
    <TBODY>
      <TR>
        <TD colSpan=4 align="center"><INPUT id=commitBtn onclick="subChecked();" class="normal_btn" onclick=""; value="保存" type=button name=bt_add>
          <INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back></TD>
      </TR>
      <TR>
        <TD colSpan=4 align=middle></TD>
      </TR>
    </TBODY>
  </TABLE>
</FORM>