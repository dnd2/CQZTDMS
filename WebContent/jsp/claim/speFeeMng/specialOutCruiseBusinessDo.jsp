<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.bean.TtAsWrSpeoutfeeBean" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%String contextPath = request.getContextPath();%>
<%
TtAsWrSpeoutfeeBean feeBean =(TtAsWrSpeoutfeeBean)request.getAttribute("feeBean");
List<TtAsWrSpeoutfeeBean> list=(List<TtAsWrSpeoutfeeBean>)request.getAttribute("list");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡航服务线路审核单审核</title>
<script type="text/javascript">
    var isApproveFlag="unApproved";
    //新增三包规则
	function SpecialAutitingCheck(flag){
		document.getElementById("flag").value=flag;
		var auditingOpinion=document.getElementById("auditingOpinion").value;
		if("08"==flag){//退回
		   if(auditingOpinion==""){
			  MyDivAlert("请填写审核意见！");
		      return false;
		   }
		}
		if("05"==flag){//中止
		   if(auditingOpinion==""){
			  MyDivAlert("请填写审核意见！");
		      return false;
		   }
		}
		if(auditingOpinion.length>60){
		   MyDivAlert("审核意见请在60个字内！");
		   return false;
	    }
		var submit_url= "<%=contextPath%>/claim/speFeeMng/SpecialOutCruiseAutiting/SpecialAutiting.json";
		makeNomalFormCall(submit_url,CallAfterPass,'fm','createOrdBtn');
	}
	function CallAfterPass(json){
        var retCode=json.retCode;
        var flag=json.flag;
        if(retCode=="success"){
        	isApproveFlag="haveApproved";
        	document.getElementById("passBtn").disabled="disabled";//将通过按钮置为失效
        	document.getElementById("backBtn").disabled="disabled";//将退回按钮置为失效
        	document.getElementById("stopBtn").disabled="disabled";//将中止按钮置为失效
        	if("05"==flag){
        		MyDivConfirm("中止成功！",refreshParentPage,"");
            }else if("08"==flag){
            	MyDivConfirm("退回成功！",refreshParentPage,"");
            }else if("04"==flag){
            	MyDivConfirm("通过成功！",refreshParentPage,"");
            }
        }else{
            if("05"==flag){
            	MyDivAlert("中止失败！");
            }else if("08"==flag){
            	MyDivAlert("退回失败！");
            }else if("04"==flag){
            	MyDivAlert("通过失败！");
            }
        }
	}
	//刷新父页面
	function refreshParentPage(){
		parent.window._hide();
		parentContainer.__extQuery__(1);
	}
	function closeMe(){
		if(isApproveFlag=="unApproved"){
		   parent.window._hide();
		}else if(isApproveFlag=="haveApproved"){
		   refreshParentPage();
		}
	}
</script>
</head>

<body>
<div class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;巡航服务线路审核单
</div>
<form method="post" id="fm" name="fm">
	<table class="table_edit">
	      <tr>
			<th colspan="7">
			   <img class="nav" src="<%=contextPath %>/img/subNav.gif" />审核操作
			</th>
		  </tr>
          <tr> 
            <td height="12" align=left>审核意见：</td>
            <td align=left>
	            <span class="tbwhite">
		            <textarea  name="auditingOpinion"  id="auditingOpinion"  rows="2" cols="55" datatype="1,is_textarea,60"></textarea>
		            <input type="button"  class="normal_btn" style="width=8%" value="通过" id="passBtn" name="passBtn" onclick="SpecialAutitingCheck('04');"/>
		            <input type="button"  class="normal_btn" style="width=8%" value="退回" id="backBtn" name="backBtn" onclick="SpecialAutitingCheck('08');"/>
		            <input type="button"  class="normal_btn" style="width=8%" value="中止" id="stopBtn" name="stopBtn" onclick="SpecialAutitingCheck('05');"/>
		            <input type="button"  class="normal_btn" style="width=8%" value="关闭" id="closeBtn" name="closeBtn" onclick="closeMe();"/>
	            </span>
            </td>
          </tr>
		</table>
        <br/>
   <table align=center width="95%" class="table_edit" >	
    <tr>
		<th colspan="20">
			<img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息
		</th>
	</tr>
	   <tr>   <input type="hidden" id="flag" name="flag" />
	          <input type="hidden" id="id" name="id" value="<%=feeBean.getId() %>"/>
			  <td align="right" nowrap="nowrap">经销商代码：</td>
			  <td><%=feeBean.getDealerCode()==null?"":feeBean.getDealerCode() %> </td>
			  <td width="11%" align="right" nowrap="nowrap">经销商位名称：</td>
			  <td><%=feeBean.getDealerShortname()==null?"":feeBean.getDealerShortname() %> </td>
			  <td width="7%" align="right" nowrap="nowrap">&nbsp;</td>
			  <td colspan="6">&nbsp;</td>
	    </tr>
		<tr>
		 <td width="7%" align="right" nowrap="nowrap">制单时间：</td>
		 <td><%=feeBean.getMakeDate()==null?"":feeBean.getMakeDate() %></td>	
		 <td width="11%" align="right" nowrap="nowrap">省份：</td>
		 <td><%=feeBean.getPrivinceName()==null?"":feeBean.getPrivinceName() %> </td>
		 <td width="7%" align="right" nowrap="nowrap">巡航目的地：</td>
		 <td colspan="6"><%=feeBean.getCrWhither()==null?"":feeBean.getCrWhither() %></td>
	    </tr>
		<tr>
			<td width="7%" align="right" nowrap="nowrap">巡航总里程(公里)：</td>
			<td ><%=feeBean.getCrMileage()==null?"":feeBean.getCrMileage() %></td>	
			<td width="11%" align="right" nowrap="nowrap">巡航时间(天)：</td>
			<td><%=feeBean.getCrDay()==null?"":feeBean.getCrDay() %></td>
			<td width="7%" align="right" nowrap="nowrap">巡航服务负责人：</td>
			<td colspan="6"><%=feeBean.getCrPrincipal()==null?"":feeBean.getCrPrincipal() %></td>
	   </tr>
	   <tr>
		<td width="7%" align="right" nowrap="nowrap">巡航服务电话：</td>
		 <td><%=feeBean.getCrPhone()==null?"":feeBean.getCrPhone() %></td>	
		<td width="11%" align="right"  nowrap="nowrap">开展巡航服务原因：</td>
		 <td colspan="3">
           <textarea id="service_reason" name="service_reason" rows="3" cols="30" readonly="readonly"><%=feeBean.getCrCause()==null?"":feeBean.getCrCause() %></textarea>
         </td>
	  </tr>
	</table>
    <br/>
	<table align="center" width="95%" class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
        <th colspan="6" align="left"><img src="<%=contextPath %>/img/subNav.gif" alt="" class="nav" /> 审批明细</th>
      </tr>
      <tr>
        <th>审批时间</th>
        <th>审批人员</th>
        <th>人员部门</th>
        <th>审批状态</th>
        <th>审批意见 </th>
      </tr>
      <c:forEach var="list" items="${list}">
	      <tr class="table_list_row1">
	        <td><c:out value="${list.auditingDate}"></c:out></td>
	        <td><c:out value="${list.name}"></c:out></td>
	        <td><c:out value="${list.orgName}"></c:out></td>
	        <td>
	        <script type='text/javascript'>
						       var status=getItemValue('${list.status}');
						       document.write(status) ;
			</script>
	        </td>
	        <td><textarea rows="1" cols="15" readonly="readonly"><c:out value="${list.auditingOpinion}"></c:out></textarea></td>
	      </tr>
      </c:forEach>
    </table>
    </form>
</body>
</html>