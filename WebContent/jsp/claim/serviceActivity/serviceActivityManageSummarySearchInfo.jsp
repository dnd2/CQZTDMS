<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动评估总结</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" language="javascript">
function save(){
	fm.action="<%=contextPath%>/claim/basicData/HomePageNews/saveNews.do"
	fm.submit();
}
function delt(id,obj)
{
	 var tabl=document.all['table_info'];
	 var index = obj.parentElement.parentElement.rowIndex;
	 tabl.deleteRow(index); 
	 makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/ServiceActivityManagedel.json?id='+id,delBack,'fm','');
}
function delBack(json)
{
	MyAlert('删除成功');
}
function checkedLoad(){
	if(!submitForm('fm')) {
		return false;
	}else{
	    disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
		MyConfirm("是否确认上报？",updateLoad);
	}
}
//服务活动评估总结上报
function updateLoad(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/serviceActivitySummaryOption.do";
		fm.submit();
	}
</script>
</head>

<body>
	<div class="navigation">
	     <img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估查询
	</div>
 <form name="fm" id="fm" method="post">
  <table class="table_edit">
	     <tr>
		      <th colspan="4" align="left">
		      		<img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息
		      </th>
	     </tr>
          <tr>
            <td width="19%"   align="right">主题编号：</td>
            <td width="30%" align="left" ><c:out value="${ttAsActivityBean.subject_no}"></c:out> </td>
            <td width="15%" align="right">主题名称： </td>
            <td width="36%" align="left">
                  <input type="hidden" name="subjectid" id="subjectid"  class="middle_txt"  value="${ttAsActivityBean.subjectId}"/>
                   <input type="hidden" name="evaluateid" id="evaluateid"  class="middle_txt"  value="${ttAsActivityBean.evaluateid}"/>
          		  <c:out value="${ttAsActivityBean.subject_name}"></c:out>
            </td>
          </tr>
          <tr >
            <td align="right">活动类型：</td>
            <td align="left">
                <script type='text/javascript'>
				       var activityType=getItemValue('${ttAsActivityBean.activityType}');
				       document.write(activityType) ;
				     </script>
            </td> 
            <td align="right">责任人：</td>
            <td>
            	<c:out value="${ttAsActivityBean.duty_person}"></c:out>
            </td>
          </tr>
          <tr>
            <td  align="right">活动日期：</td>
            <td  align="left">
            	 ${ttAsActivityBean.subject_start_date}<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt"></span>&nbsp;至&nbsp;${ttAsActivityBean.subject_end_date}</DIV></TD>
            </td> 
            <td  align="right">所在区域：</td>
            <td  align="left">
            	${org_name} 
            </td>
          </tr>
          <tr>
            <td  align="right">老带新信息留存数：</td>
            <td align="left" >
                ${ttAsActivityBean.on_amount}
             </td>
            <td align="right">老带新成交数：</td>
            <td align="left">
                ${ttAsActivityBean.on_camount}</td>
          </tr>
          <tr>
            <td  align="right">
           		 活动效果自我评价：</td>
            <td  colspan="3" align="left" >
	            <span class="tbwhite">
	                <textarea disabled="disabled"  name="evaluate"  id="evaluate"    rows="5" cols="80" datatype="0,is_null,200">${ttAsActivityBean.evaluate}</textarea>
	            </span>
            </td>
          </tr>
          <tr>
            <td  align="right">建议及改进措施：</td>
            <td  colspan="3" align="left" >
	            <span class="tbwhite">
	              <textarea  disabled="disabled" name="measures"  id="measures"   rows="5" cols="80" datatype="0,is_null,200">${ttAsActivityBean.measures}</textarea>
	            </span>
            </td>
          </tr>
		  <tr>
            <td  align="left">&nbsp;</td>
            <td  colspan="3" align="left" >&nbsp;</td>
          </tr>
        </table>
        <br />
        <table  class="table_query">
          <tr> 
            <td height="12" align=center>
			  <input type="button" onClick="javascript:history.go(-1);"  class="normal_btn"  value="返回"/>
		    </td>
		  </tr>
        </table>
        <table class="table_list">
    <tbody>
      <tr>
        <th colspan="4" align="left"><span class="navigation"><img alt="" src="../../../img/nav.gif" /></span>服务活动信息</th>
      </tr>
      <tr>
        <th >活动编码</th>
        <th >活动名称</th>
        <th >三包内数量</th>
        <th >三包外数量</th>
      </tr>
      <c:forEach var="ttAsActivity" items="${ttAsActivity}" >
      <tr class="table_list_row1">
        <td><c:out value="${ttAsActivity.activity_code}"/></td>
        <td><c:out value="${ttAsActivity.activity_name}"/></td>
        <td><font><c:if test="${ttAsActivity.part_num==null}">0</c:if><c:if test="${ttAsActivity.part_num !=null}"><c:out value="${ttAsActivity.part_num}"/></c:if></font></td>
        <td><font><c:if test="${ttAsActivity.part_num_w==null}">0</c:if><c:if test="${ttAsActivity.part_num !=null}"><c:out value="${ttAsActivity.part_num_w}"/></c:if></font></td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  <table class="table_info" id="table_info" name="table_info" border="0" id="file" >
	<tr colspan="8">
 		<td width="100%" colspan="3">
 		  	<jsp:include page="${contextPath}/uploadDiv.jsp" /> 
 </tr>
 
  <c:forEach var="FileuploadPO" items="${FileuploadPO}" >
  <script type="text/javascript">
	 	 addUploadRowByDL('${FileuploadPO.filename}','${FileuploadPO.fileid}','${FileuploadPO.fileurl}','${FileuploadPO.fjid}');
 </script>
 </c:forEach>
</table>
</form>
</body>
</html>