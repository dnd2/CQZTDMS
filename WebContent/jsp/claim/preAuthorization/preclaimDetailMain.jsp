<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%
	String contextPath = request.getContextPath();
	HashMap hm = (HashMap)request.getAttribute("FOREAPPROVAL_HASHMAP");//预授权信息
	//附近列表：
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>预授权状态明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
//展示附件
   function addUploadRowByDb1(filename,fileId,fileUrl){
 	var tab =document.getElementById("fileUploadTab");
 	var row =  tab.insertRow();
    row.className='table_list_row1';
    row.insertCell();
    row.insertCell();
    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
    row.cells(1).innerHTML = "<input disabled type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
}	
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预申请状态查询</div> 
<form name='fm' id='fm'>
    <table class="table_edit">
       <tr>
         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_9Letter">经销商代码：</td>
         <td><%=hm.get("DEALER_CODE")==null ? "":hm.get("DEALER_CODE").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">经销商名称：</td>
         <td ><%=hm.get("DEALER_SHORTNAME")==null ? "":hm.get("DEALER_SHORTNAME").toString() %></td>
         <td class="table_edit_3Col_label_8Letter">维修工单号：</td>
         <td >
				<%=hm.get("RO_NO")==null ? "" : hm.get("RO_NO") %>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_9Letter">VIN：</td>
         <td><%=hm.get("VIN")==null ? "":hm.get("VIN").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">牌照号：</td>
         <td ><%=hm.get("LICENSE_NO")==null ? "":hm.get("LICENSE_NO").toString() %></td>
         <td class="table_edit_3Col_label_8Letter">发动机号：</td>
         <td >
				<%=hm.get("ENGINE_NO")==null ? "" : hm.get("ENGINE_NO") %>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_9Letter">品牌：</td>
         <td><%=hm.get("BRAND_NAME")==null ? "":hm.get("BRAND_NAME").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">车系：</td>
         <td ><%=hm.get("SERIES_NAME")==null ? "":hm.get("SERIES_NAME").toString() %></td>
         <td class="table_edit_3Col_label_8Letter">车型：</td>
         <td >
				<%=hm.get("MODEL_NAME")==null ? "" : hm.get("MODEL_NAME") %>
         </td>
       </tr> 
       <tr>
         <td class="table_edit_3Col_label_9Letter">保修开始日期：</td>
         <td><%=hm.get("KEEP_BEG_DATE")==null ? "":hm.get("KEEP_BEG_DATE").toString().substring(0,10) %></td>
         <td class="table_edit_3Col_label_5Letter">产地：</td>
         <td >
			<script type="text/javascript">
			<!--
				document.write(getItemValue("<%=hm.get("YIELDLY")==null ? "" : hm.get("YIELDLY").toString()%>"));
			//-->
			</script>          
         </td>
       </tr> 
       <tr>
         <td class="table_edit_3Col_label_9Letter">接待员：</td>
         <td><%=hm.get("DEST_CLERK")==null ? "":hm.get("DEST_CLERK").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">进厂日期：</td>
         <td ><%=hm.get("IN_FACTORY_DATE")==null ? "":hm.get("IN_FACTORY_DATE").toString().substring(0,10) %></td>
         <td class="table_edit_3Col_label_8Letter">进厂里程数(km)：</td>
         <td >
				<%=hm.get("IN_MILEAGE")==null ? "" : hm.get("IN_MILEAGE") %>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_9Letter">申请日期：</td>
         <td><%=hm.get("APPROVAL_DATE")==null ? "" : hm.get("APPROVAL_DATE").toString().substring(0,10) %></td>
         <td class="table_edit_3Col_label_5Letter">申请人：</td>
         <td ><%=hm.get("APPROVAL_PERSON")==null ? "" : hm.get("APPROVAL_PERSON").toString() %></td>
         <td class="table_edit_3Col_label_8Letter">联系电话：</td>
         <td >
				<%=hm.get("APPROVAL_PHONE")==null ? "" : hm.get("APPROVAL_PHONE") %>
         </td>
       </tr> 
       <tr>
         <td class="table_edit_3Col_label_9Letter">外出时间：</td>
         <td>
         <%=hm.get("OUT_DATE")==null ? "" : hm.get("OUT_DATE").toString().substring(0,10) %>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出人：</td>
         <td><%=hm.get("OUT_PERSON")==null ? "" : hm.get("OUT_PERSON") %></td>
         <td class="table_edit_3Col_label_8Letter">外出费用：</td>
         <td><%=hm.get("OUT_FEE")==null ? "" : hm.get("OUT_FEE") %></td>
       </tr>         
       <tr>
         <td class="table_edit_3Col_label_9Letter">申请类型：</td>
         <td>
			<script type="text/javascript">
			<!--
				document.write(getItemValue("<%=hm.get("APPROVAL_TYPE")==null ? "" : hm.get("APPROVAL_TYPE").toString()%>"));
			//-->
			</script>          
         </td>
       </tr>
       <tr>
         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 项目信息</th>
       </tr>                                                
       <tr>
         <td class="table_edit_3Col_label_9Letter">项目类别：</td>
         <td>
			<script type="text/javascript">
			<!--
				document.write(getItemValue("<%=hm.get("ITEM_TYPE")==null ? "" : hm.get("ITEM_TYPE").toString()%>"));
			//-->
			</script>         	
         </td>
         <td class="table_edit_3Col_label_5Letter">项目代码：</td>
         <td>
         	 <%=hm.get("ITEM_CODE")==null ? "" : hm.get("ITEM_CODE").toString() %>
         </td>
         <td class="table_edit_3Col_label_8Letter">项目名称：</td>
         <td>
         	<%=hm.get("ITEM_DESC")==null ? "" : hm.get("ITEM_DESC").toString() %>
         </td>
       </tr>
<%--       <tr>--%>
<%--         <td class="table_edit_3Col_label_9Letter">申请日期：</td>--%>
<%--         <td>--%>
<%--         <%=hm.get("APPROVAL_DATE")==null ? "" : hm.get("APPROVAL_DATE").toString().substring(0,10) %>--%>
<%--         </td>--%>
<%--         <td class="table_edit_3Col_label_5Letter">申请人：</td>--%>
<%--         <td>--%>
<%--         	<%=hm.get("APPROVAL_PERSON")==null ? "" : hm.get("APPROVAL_PERSON").toString() %>--%>
<%--         </td>--%>
<%--         <td class="table_edit_3Col_label_5Letter">VIN：</td>--%>
<%--         <td>--%>
<%--         <%=hm.get("VIN")==null ? "" : hm.get("VIN").toString() %>--%>
<%--         </td>--%>
<%--       </tr>   --%>
       <tr>
         <td class="table_edit_3Col_label_9Letter">故障描述及维修方案：</td>
         <td>
         <%=hm.get("DEALER_REMARK")==null ? "" : hm.get("DEALER_REMARK").toString() %>
         </td>
         <td class="table_edit_3Col_label_5Letter"></td>
         <td>&nbsp;</td>
         <td class="table_edit_3Col_label_5Letter"></td>
         <td>&nbsp;</td>
       </tr>
<%--       <tr>--%>
<%--         <td class="table_edit_3Col_label_9Letter">审核意见：</td>--%>
<%--         <td>--%>
<%--         <%=hm.get("CHECK_REMARK")==null ? "" : hm.get("CHECK_REMARK").toString() %>--%>
<%--         </td>--%>
<%--         <td class="table_edit_3Col_label_5Letter"></td>--%>
<%--         <td>&nbsp;</td>--%>
<%--         <td class="table_edit_3Col_label_5Letter"></td>--%>
<%--         <td>&nbsp;</td>--%>
<%--       </tr>  --%>
       <tr>
         <td class="table_edit_3Col_label_9Letter">审核结果：</td>
         <td>
			<script type="text/javascript">
			<!--
				document.write(getItemValue("<%=hm.get("STATUS")==null ? "" : hm.get("STATUS") %>"));
			//-->
			</script>          
         </td>
         <td class="table_edit_3Col_label_5Letter"></td>
         <td>&nbsp;</td>
         <td class="table_edit_3Col_label_8Letter"></td>
         <td>&nbsp;</td>
       </tr>             
    </table>
<!-- 添加附件 -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
		<tr colspan="8">
			<th>
			<img class="nav" src="../../../img/subNav.gif" />
			&nbsp;附件列表：
			</th>
		</tr>
		<tr>
					<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
			</tr>
			<%for(int i=0;i<attachLs.size();i++) { %>
			<script type="text/javascript">
			addUploadRowByDb1('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
			</script>
			<%} %>
		</table>    
     <table class="table_add">
		<tr> 
            <td align=center>
				<input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
		    </td>
		</tr>
	</table>
</form>
</body>
</html>
