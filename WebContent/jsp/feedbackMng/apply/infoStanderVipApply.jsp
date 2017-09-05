<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合格证补办更换申请表明细</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt;合格证补办更换申请表</div>
 <form method="post" name = "fm" >
 
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
          <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(合格证补办收费500元/张，合格证车身颜色更改：200元/张)</font></th>
          </tr>
		  <tr>
		    <td class="table_query_3Col_label_6Letter">工单号：</td>
		    <td class="table_query_3Col_label_6Letter">
		    	<c:out value="${svInfo.ORDER_ID}"/>
		    </td>
		    <td class="table_query_3Col_label_6Letter">服务中心代码：</td>
		    <td class="table_query_3Col_label_6Letter">
		    	<c:out value="${svInfo.DEALER_CODE}"/>
		    </td>
		    <td class="table_query_3Col_label_6Letter">服务中心名称：</td>
		    <td class="table_query_3Col_label_6Letter">
		    	<c:out value="${svInfo.DEALER_NAME}"/>
		    </td>
	      </tr>
		  <tr bgcolor="FFFFFF">
		    <td class="table_query_3Col_label_6Letter">服务中心联系人：</td>
		    <td class="table_query_3Col_label_6Letter">
		    	<c:out value="${svInfo.LINK_MAN}"/>
		    </td>
		    <td class="table_query_3Col_label_6Letter">服务中心联系电话：</td>
		    <td class="table_query_3Col_label_6Letter">
		    	<c:out value="${svInfo.TEL}"/>
		    </td>
		    <td class="table_query_3Col_label_6Letter">服务中心邮编：</td>
		    <td class="table_query_3Col_label_6Letter">
				<c:out value="${svInfo.ZIP_CODE}"/>
			</td>
	      </tr>
		  <tr bgcolor="FFFFFF">
		    <td class="table_query_3Col_label_6Letter">服务中心地址：</td>
		    <td colspan="5">
		    	<c:out value="${svInfo.ADDRESS}"/>
		    </td>
	      </tr>
          
		  <tr bgcolor="F3F4F8">
            <td class="table_query_3Col_label_6Letter">工单类型：</td>
            <td class="table_query_3Col_label_6Letter">
            	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${svInfo.ST_TYPE}"/>');
					document.write(name) ;
				</script> 
            </td>
            <td class="table_query_3Col_label_6Letter">操作类型：</td>
            <td class="table_query_3Col_label_6Letter">
            	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${svInfo.ST_ACTION}"/>');
					document.write(name);
				</script>
            </td>
          </tr>
          <tr>
            <td class="table_query_3Col_label_6Letter">申请内容：</td>
            <td colspan="5">
            	<c:out value="${svInfo.ST_CONTENT}"/>
            </td> 
          </tr>
     </table> 
 	 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
 	 	  <tr>
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 车辆信息</th>
		  </tr>
		  <tr bgcolor="F3F4F8">
		    <td class="table_query_3Col_label_6Letter">车辆识别码(VIN)：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.VIN}"/></td>
		    <td class="table_query_3Col_label_6Letter">车型：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.GROUP_NAME}"/></td>
		    <td class="table_query_3Col_label_6Letter">发动机号：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.ENGINE_NO}"/></td>
	      </tr>
		  <tr bgcolor="F3F4F8">
		    <td class="table_query_3Col_label_6Letter">颜色：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.COLOR}"/></td>
		    <td class="table_query_3Col_label_6Letter">生产日期：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.PRODUCT_DATE}"/></td>
		    <td class="table_query_3Col_label_6Letter">购车日期：</td>
		    <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.PURCHASED_DATE}"/></td>
	      </tr>
          <tr bgcolor="F3F4F8">
            <td class="table_query_3Col_label_6Letter">车主姓名：</td>
            <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.CUSTOMER_NAME}"/></td>
            <td class="table_query_3Col_label_6Letter">车主身份证号：</td>
            <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.CERT_NO}"/></td>
            <td class="table_query_3Col_label_6Letter">车主联系电话：</td>
            <td class="table_query_3Col_label_6Letter"><c:out value="${svInfo.MOBILE}"/></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td class="table_query_3Col_label_6Letter">车主地址：</td>
            <td colspan="5" ><c:out value="${svInfo.ADDRESS_DESC}"/></td>
          </tr>
   </table>        
  

<!-- 展示附件 开始-->
  <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
  </table>
<!-- 展示附件 结束-->
   <table class="table_list" style="border-bottom:1px solid #DAE0EE">
   		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批明细</th>
		
        <tr  bgcolor="F3F4F8">
            <th>审批时间</th>
            <th>审批人员</th>
            <th>人员部门</th>
            <th>审批状态</th>
            <th>审批意见</th>
    	</tr>
        <c:forEach items="${auditList}" var="al">
       		<tr>
            	<td><c:out value="${al.AUDIT_DATE}"/></td>
            	<td><c:out value="${al.NAME}"/></td>
            	<td><c:out value="${al.ORG_NAME}"/></td>
            	<td>
	            	<script type='text/javascript'>
	     			  var name=getItemValue(<c:out value="${al.AUDIT_STATUS}"/>);
	      			  document.write(name) ;
					</script>
            	</td>
           	 	<td><c:out value="${al.AUDIT_CONTENT}"/></td>
        	</tr>
       	</c:forEach>
   </table>    
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
         	<input type="hidden" id="aaaa"/>
           <div id="a1">
	           <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           </div>
           <div id="a2" style="display:none">
	           <input type="button" onclick="window.print();" class="normal_btn" value="打印"/>
	           &nbsp;&nbsp;
	           <input type="button" onclick="window.close();" class="normal_btn" style="width=8%" value="关闭"/>
           </div>
       </tr>
     </table>    
</form>
<script type="text/javascript">
	$('aaaa').value = '<%=request.getParameter("flag")%>' ;
	if($('aaaa').value!='null'){
		$('a2').style.display = 'block' ;
		$('a1').style.display = 'none' ;
	}
</script>
</body>
</html>
