<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
	String contextPath = request.getContextPath();
    String orgId = (String)request.getAttribute("orgId");
    List<Map<String, Object>>  listCheckBox = (List<Map<String, Object>>)request.getAttribute("ListCheckBox"); 
	List actionList = (List)request.getAttribute("actionList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉维护(区域)</title>
<script type="text/javascript">
function doInit(){
	selectAction();
	loadcalendar();  //初始化时间控件
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉维护(区域)</div>
 <form method="post" name = "fm" id="fm">
   <input type="hidden" name="compId" id="compId" value="<c:out value="${complaintMap.COMP_ID}"/>"/>
   <input type="hidden" name="auditId" id="auditId" value="<c:out value="${detailMap.ID}"/>"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户基本信息</th>
     <tr>
	    <td class="table_query_2Col_label_5Letter">联系人名称：</td>
	    <td><c:out value="${complaintMap.LINK_MAN}"/></td>
	    <td class="table_query_2Col_label_4Letter">性别：</td>
	    <td><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.SEX}"/>');
		  </script></td>
	 </tr>
     <tr>
       <td class="table_query_2Col_label_4Letter">生日：</td>
	   <td><c:out value="${complaintMap.BIRTHDAY}"/></td>
       <td class="table_query_2Col_label_4Letter">年龄：</td>
       <td><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.AGE}"/>');
		  </script></td>
    </tr>
    <tr>
       <td class="table_query_2Col_label_4Letter">所属大区：</td>
       <td><c:out value="${complaintMap.ORG_CODE}"/></td>
       <td class="table_query_2Col_label_4Letter">联系电话：</td>
       <td><c:out value="${complaintMap.TEL}"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_4Letter">省份：</td>
      <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.PROVINCE}"/>');
		  </script></td>
      <td class="table_query_2Col_label_4Letter">Email：</td>
      <td><c:out value="${complaintMap.E_MAIL}"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_4Letter">地级市：</td>
      <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.CITY}"/>');
		  </script></td>  
      <td class="table_query_2Col_label_4Letter">邮编：</td>
      <td><c:out value="${complaintMap.ZIP_CODE}"/></td>
     </tr>
     <tr>
     <td class="table_query_2Col_label_4Letter">区、县：</td>
     <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.DISTRICT}"/>');
		  </script></td> 
     <td class="table_query_2Col_label_5Letter">投诉经销商：</td>
     <td><c:out value="${complaintMap.DEALER_CODE}"/></td>
     </tr>
     <tr>
      <td class="table_query_2Col_label_4Letter">家庭住址：</td>
      <td><c:out value="${complaintMap.ADDRESS}"/></td> 
     </tr>
  </table>
  <br>
  <table class="table_list">
   <th colspan="5" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户车辆信息</th>
      <TR>
      	<th>VIN</th>
        <th>车型</th>
        <th>发动机号</th>
        <th>车牌号</th>
        <th>购车日期</th>
      </TR>
      <TR class="table_list_row2">
      <TD><c:out value="${complaintMap.VIN}"/></TD>
      <TD><c:out value="${complaintMap.GROUP_NAME}"/></TD>
      <TD><c:out value="${complaintMap.ENGINE_NO}"/></TD>
      <TD><c:out value="${complaintMap.LICENSE_NO}"/></TD>
      <TD><c:out value="${complaintMap.PURCHASED_DATE}"/></TD>
     </TR>
  </table>
  <br>
  <TABLE class="table_query">
		<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 投诉内容</th>
		<TR>
		  <TD><div align="right">投诉编号：</div></TD>
		  <TD><input type="hidden" name="compCode" id="compCode" value="<c:out value="${complaintMap.COMP_CODE}"/>"/></TD>
		  <TD><div align="left"><c:out value="${complaintMap.COMP_CODE}"/></div></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉时间：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left"><c:out value="${complaintMap.CREATE_DATE}"/></div></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉等级：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left"><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_LEVEL}"/>');
		  </script></div></TD>
	    </TR>
		<TR>
		  <TD><div align="right">投诉类型：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		  <script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_TYPE}"/>');
		  </script>
		  </div></TD>
	  </TR>
	  <TR>
		  <TD><div align="right">投诉来源：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		  <script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_SOURCE}"/>');
		  </script></div></TD>
	  </TR>
	   <TR>
		  <TD><div align="right">客户投诉内容：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left"><c:out value="${complaintMap.COMP_CONTENT}"/></div></TD>
	  </TR>
	</TABLE>
	<br>
  <table class="table_query">
		<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 处理明细</th>
		<TR>
			<TD>
			<div align="right">处理时间：</div></TD>
			<TD>&nbsp;</TD>
			<TD><!--<c:out value="${detailMap.AUDIT_DATE}"/>--></TD>
		</TR>
		<TR>
			<TD>
			<div align="right">发生动作：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
			<%-- 
			<%for(int i=0;i<listCheckBox.size();i++){ %>
			<%if(actionList!=null&&actionList.contains(listCheckBox.get(i).get("CODE_ID"))) {%>
				<input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>" checked> <%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%}else{ %>
			  <input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>"><%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%} %>
			<%} %>
			--%>
			<%for(int i=0;i<listCheckBox.size();i++){ %>
			<input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>"><%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%} %>
			</div>			
			</TD>
		</TR>
		<TR>
			<TD>
			<div align="right">处理结果：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
			<!-- 
			  <script type="text/javascript">
            	genSelBoxExp("auditResult",<%=Constant.AUDIT_RESULT_TYPE%>,"<c:out value="${detailMap.AUDIT_RESULT}"/>",true,"short_sel","onchange='selectAction()'","false",'');
              </script>
              -->
              <script type="text/javascript">
            	genSelBoxExp("auditResult",<%=Constant.AUDIT_RESULT_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'');
              </script>
             <span id="partcode" style="display:none">备件编码：
              <input name="partCode" type="text" id="partCode"  class="middle_txt" datatype="0,is_null,15" value="<c:out value="${detailMap.PART_CODE}"/>"></span>
               <span id="supplier" style="display:none">上级保供单位：
              <input name="supplier" type="text" id="supplier"  class="middle_txt" datatype="0,is_null,20" value="<c:out value="${detailMap.SUPPLIER}"/>"></span>
			</div></TD>
		</TR>
		<TR>
			<TD>
			<div align="right">跟进结果描述：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
            	<textarea name="auditContent"
				style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5></textarea></TD>
		</TR>
        <TR>
		  <td colspan="3" align="center"><input name="saveBtn" type="button" class="normal_btn" onclick="saveModify()" value="保存" />&nbsp;&nbsp;&nbsp;&nbsp;
			 <input name="cnfrmClsBtn" type="button" class="long_btn" onclick="complaintClose()" value="建议关闭" /></td>
	    </TR>
  </table>
  <br>
    <table class="table_query">
	  <tr>
	    <th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" class="nav" />
	      分配明细</th>
      </tr>
      <TR>
		  <TD><div align="right">分配处理意见:</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><textarea name="assignContent"
				style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5></textarea>
		 </TD>
	  </TR>
	  <tr>
	    <td><div align="right">经销商:</div></td>
	    <td>&nbsp;</td>
	    <td><input name="dealerCode" type="text" id="dealerCode" size="40" />
	       <input type="hidden" name="dealerId" id="dealerId" value=""/>    
		   <input name="dealerSel" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','false','<%=orgId%>',true)" value="&hellip;" />	
		</td>	
	    <td>
		&nbsp;&nbsp;&nbsp;&nbsp;</td>		
      </tr>
	</table>
	<br>
	 <TABLE class="table_list">
    <th colspan="8" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户投诉处理历史</th>
	 <TR >
        <th>序号</th>
        <th>处理大区</th>
        <th>处理服务中心</th>
        <th>处理人</th>
        <th>处理时间</th>
        <th>发生动作</th>
        <th>联系结果</th>
        <th>联系结果描述</th>
    </TR>
    	<%
   	   		List list = (List)request.getAttribute("detailList");
   	   		if(list.size()>0){
   	   			for(int i=0;i<list.size();i++){
   	   				Map map = (Map)list.get(i);
   	   	%>
   	   	 <tr class="table_list_row2">
      		<td><%=map.get("ROWNUM")==null?"":map.get("ROWNUM")%></td>
           	<td><%=map.get("ORG_NAME")==null?"":map.get("ORG_NAME")%></td>
           	<td><%=map.get("DEALER_NAME")==null?"":map.get("DEALER_NAME")%></td>
           	<td><%=map.get("NAME")==null?"":map.get("NAME")%></td>
            <td><%=map.get("AUDIT_DATE")==null?"":map.get("AUDIT_DATE")%></td>	
          	<td>
          		<script type='text/javascript'>
          			writeItemValues('<%=map.get("AUDIT_ACTION")%>')
				</script>
			</td>
          	<td>
          		<%
          			if (map.get("AUDIT_RESULT") != null) {
          				if (String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_02)) || 
          					String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_03))) {
          		%>
          					<a href="#" onclick="showPart('<%=map.get("PART_CODE")%>', '<%=map.get("SUPPLIER")%>');">
          						<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          						</script>
          					</a>
          		<% 
          				} else {
          		%>
          					<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          					</script>
          		<%
          				}
          			}
          		 %>
			</td>
          	<td title="<%=map.get("AUDIT_CONTENT")%>">&nbsp;
          		<a href="#" onclick="showAllMsg('<%=map.get("ID")%>');">
          		<%
          			if(map.get("AUDIT_CONTENT")!=null){
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()<=10){
          		%>
          			<%=map.get("AUDIT_CONTENT")%>
          		<%
          				}
          		%>
          		<%
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()>10){
          					String s = String.valueOf(map.get("AUDIT_CONTENT"));
          					s = s.substring(0,9);
          		%>
          			<%=s%>...</a>&nbsp;&nbsp;&nbsp;
          		<%
          				}
          			}
          		%>
          	</td>
       	</tr>
   	   	<%		
   	   			}
   	   		}
   	    %>
    </TABLE>
    <br>
    <TABLE class="table_query">
	<TR><TD align="center">
	<input name="assign" type="button" class="normal_btn" onclick="assignDisposalToDealer()" value="分配"/>
	<input name="back" type="button" class="normal_btn" onClick="history.back();" value="返回"></TD>
	</TR>	
	</TABLE>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
// 动态显示备件编码和上级保供单位
function selectAction(){
		if(document.fm.auditResult.value == '<%=Constant.AUDIT_RESULT_TYPE_02%>'||document.fm.auditResult.value == '<%=Constant.AUDIT_RESULT_TYPE_03%>') {
			
			document.getElementById("partcode").style.display = "inline";
			document.getElementById("supplier").style.display = "inline";
			
		}else{
		    document.getElementById("partcode").style.display = "none";
			document.getElementById("supplier").style.display = "none";
		}
	}	
function showAllMsg(value){
	var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showAllMsg.do?value='+value ;
	OpenHtmlWindow(url,440,300);
}	

function showPart(partCode, supplier) {
	var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showPart.do?partCode='+partCode+'&supplier='+supplier ;
	OpenHtmlWindow(url,440,300);
}

// 保存
function saveModify(){
	 var cnt=0;
	var chk = document.getElementsByName("auditAction");
	
	var l = chk.length;
	
	for(var i=0;i<l;i++){
		if(chk[i].checked){
			 cnt++;
		}
	}
	
	var auditResult = document.fm.auditResult.value;
	
	var auditContent = document.fm.auditContent.value;
	
	if(cnt==0){
		MyAlert("未做处理，跟进结果描述将无效!");
		return;
	}
	if(cnt != 0){
		if (!auditResult) {
			MyAlert("请选择处理结果");
			return;
		}
		if (!auditContent) {
			MyAlert("跟进结果描述不能为空");
			return;
		}	
	}
	
	if(submitForm('fm')){
		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalArea/saveModify.do";
    	fm.submit();
	}
}

// 投诉关闭
function complaintClose()
{
	MyConfirm("确认关闭吗？", complaintCloseToDo);

}

function complaintCloseToDo()
{
	if(submitForm('fm'))
	{
		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalArea/closeComplaintByArea.do";
    	fm.submit();
    }
}

// 分配
function assignDisposalToDealer(){
	var dealerId = document.getElementById("dealerId").value;
	if (!dealerId) {
		MyAlert('请选择一家经销商');	
		return;
	}
	fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalArea/assignComplaint.do";
    fm.submit();
}	



</script>
<!--页面列表 end -->
</body>
</html>
