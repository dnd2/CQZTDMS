<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.bean.TtAsWrSpeoutfeeBean"%>
<%@ page import="com.infodms.dms.po.TtAsWrCruiseAuditingPO"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.util.CommonUtils;"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%
String contextPath = request.getContextPath();
TtAsWrSpeoutfeeBean feeBean =(TtAsWrSpeoutfeeBean)request.getAttribute("feeBean");
List<TtAsWrCruiseAuditingPO> list=(List<TtAsWrCruiseAuditingPO>)request.getAttribute("list");
List<TtAsWrSpeoutfeeBean> listBean=(List<TtAsWrSpeoutfeeBean>)request.getAttribute("listBean");
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊外出费用结算单审核</title>
<script type="text/javascript">
    var isApproveFlag="unApproved";
    //新增三包规则
	function SpecialBalancleCheck(flag){
        if(!submitForm('fm')) {//表单校验
			return false;
	    }
		var id=document.getElementById("id").value;
		document.getElementById("flag").value=flag;
		var auditingOpinion=document.getElementById("auditingOpinion").value;
		if("06"==flag){
		   if(auditingOpinion==""){
			   MyDivAlert("请填写审核意见！");
		      return false;
		   }
		}
	   if(auditingOpinion.length>60){
		   MyDivAlert("审核意见请在60个字内！");
		   return false;
	   }
	   var submit_url= "<%=contextPath%>/claim/speFeeMng/SpecialOutDeclarationCardBalancle/SpecialBalancle.json?id="+id;
	   makeNomalFormCall(submit_url,CallAfterPass,'fm','createOrdBtn');
	}
	function CallAfterPass(json){
        var retCode=json.retCode;
        var flag=json.flag;
        if(retCode=="success"){
        	isApproveFlag="haveApproved";
        	document.getElementById("recommit").disabled="disabled";//将结算按钮置为失效
    		document.getElementById("recommit2").disabled="disabled";//将驳回按钮置为失效
        	if("06"==flag){
        		MyDivConfirm("驳回成功！",refreshParentPage,"");
            }else{
            	MyDivConfirm("结算成功！",refreshParentPage,"");
            }
        }else{
            if("06"==flag){
            	MyDivAlert("驳回失败！");
            }else{
            	MyDivAlert("结算失败！");
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
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：售后服务管理
&gt;特殊费用管理&gt;特殊外出费用结算单</div>
<form name="fm" id="fm">
<table class="table_edit">
	<tr>
		<th colspan="7"><img class="nav"
			src="<%=contextPath %>/img/subNav.gif" />审核操作</th>
	</tr>
	<tr>
		<td height="12" align=left>审核意见：</td>
		<td align=left><span class="tbwhite"> <textarea
			name="auditingOpinion" id="auditingOpinion" rows="2" cols="65"
			datatype="1,is_null,60"></textarea> <input class="normal_btn"
			type="button" value="结算" name="recommit"
			onclick="SpecialBalancleCheck('05');" /> <input class="normal_btn"
			type="button" value="驳回" name="recommit2"
			onclick="SpecialBalancleCheck('06');" /> <input class="normal_btn"
			type="button" value="关闭" name="recommit3" onclick="closeMe();" /> </span></td>
	</tr>
</table>
<table align=center width="95%" class="table_edit">
	<tr>
		<th colspan="20"><img class="nav"
			src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
	</tr>
	<tr>
		<input type="hidden" id="flag" name="flag" />
		<input type="hidden" id="id" name="id" value="<%=feeBean.getId() %>" />
		<td align="right">费用单据编码：</td>
		<td><%=feeBean.getFeeNo() %></td>
		<td align="right">&nbsp;</td>
		<td>&nbsp;</td>
		<td align="right">&nbsp;</td>
		<td colspan="6">&nbsp;</td>
	</tr>
	<tr>
		<td width="11%" align="right" nowrap="nowrap">巡航单据编码：</td>
		<td><%=feeBean.getCrNo()==null?"":feeBean.getCrNo() %></td>
		<td width="15%" align="right" nowrap="nowrap">制单人姓名：</td>
		<td><%=feeBean.getCreateBy()==null?"":feeBean.getCreateBy() %></td>
		<td width="11%" align="right" nowrap="nowrap">制单日期：</td>
		<td colspan="6"><%=feeBean.getMakeDate()==null?"":feeBean.getMakeDate() %></td>
	</tr>
	<tr>
		<td width="11%" align="right" nowrap="nowrap">申请单位：</td>
		<td><%=feeBean.getDealerShortname()==null?"":feeBean.getDealerShortname()%></td>
		<td width="15%" align="right" nowrap="nowrap">生产厂家：</td>
		<td width="19%" align="left" nowrap="nowrap"><%=request.getAttribute("productName")==null?"":request.getAttribute("productName") %></td>
		<td width="11%" align="right" nowrap="nowrap">费用渠道：</td>
		<td width="25%" align="left" nowrap="nowrap"><%=request.getAttribute("code_name")==null?"":request.getAttribute("code_name") %></td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">外出开始日期：</td>
		<td align="left" nowrap="nowrap"><%=feeBean.getStartDate()==null?"":feeBean.getStartDate() %></td>
		<td align="right" nowrap="nowrap">外出结束日期：</td>
		<td align="left" nowrap="nowrap"><%=feeBean.getEndDate()==null?"":feeBean.getEndDate() %></td>
		<td align="right" nowrap="nowrap">外出天数：</td>
		<td align="left" nowrap="nowrap"><%=feeBean.getCrDay()==null?"":feeBean.getCrDay() %>天</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">目的地：</td>
		<td colspan="5" align="left" nowrap="nowrap"><%=feeBean.getCrWhither()==null?"":feeBean.getCrWhither() %></td>
	</tr>
	<tr>
		<td width="11%" align="right" nowrap="nowrap">外出人员数量：</td>
		<td width="19%" align="left" nowrap="nowrap"><%=feeBean.getPersonNum()==null?"":feeBean.getPersonNum() %></td>
		<td align="right" nowrap="nowrap">出差人员姓名：</td>
		<td align="left" nowrap="nowrap"><%=feeBean.getPersonName()==null?"":feeBean.getPersonName() %></td>
		<td width="11%" align="right" nowrap="nowrap">总里程：</td>
		<td width="25%" align="left" nowrap="nowrap"><%=feeBean.getSingleMileage()==null?"":feeBean.getSingleMileage() %>km</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">过路过桥费(元)：</td>
		<td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getPassFee())%>'));
          </script>元
        </td>
		<td align="right" nowrap="nowrap">车辆或交通补助(元)：</td>
        <td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getTrafficFee())%>'));
          </script>元
        </td>
		<td align="right" nowrap="nowrap">住宿费(元)：</td>
        <td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getQuarterFee())%>'));
          </script>元
        </td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">餐补费(元)：</td>
        <td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getEatFee())%>'));
          </script>元
        </td>
		<td align="right" nowrap="nowrap">人员补助(元)：</td>
        <td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getPersonSubside())%>'));
          </script>元
        </td>
		<td align="right" nowrap="nowrap">总费用(元)：</td>
        <td align="left" nowrap="nowrap">
          <script type="text/javascript">
            document.write(amountFormat('<%=CommonUtils.checkNull(feeBean.getFee())%>'));
          </script>元
        </td>
	</tr>
	<tr>
		<td width="11%" align="right" nowrap="nowrap">申请内容：</td>
		<td align="left" nowrap="nowrap" colspan="6">
		<textarea id="service_reason" name="service_reason" rows="3" cols="30" readonly="readonly"><%=feeBean.getApplyContent()==null?"":feeBean.getApplyContent() %></textarea>
		</td>
	</tr>
</table>
<!-- 添加附件 开始 
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table> 
   添加附件 结束 --> <!-- 展示附件 开始-->
<table class="table_info" border="0" id="file">
	<tr colspan="8">
		<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：</th>
		<th><span align="left"></span></th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include
			page="${contextPath}/uploadDiv.jsp" /></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	<script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
    </script>
	<%}%>
</table>
<!-- 展示附件 结束-->

<table align="center" width="95%" class="table_list"
	style="border-bottom: 1px solid #DAE0EE">
	<tr>
		<th colspan="20" align="left"><img class="nav"
			src="<%=contextPath %>/img/subNav.gif" /> 车辆信息</th>
	</tr>
	<tr>
		<th>VIN码</th>
		<th>发动机号</th>
		<th>车型</th>
		<th>生产日期</th>
		<th>里程</th>
		<th>用户姓名</th>
		<th>电话</th>
		<th>销售日期</th>
		<th>备注</th>
	</tr>
	<c:forEach var="list" items="${list}">
		<tr class="table_list_row1">
			<td><c:out value="${list.vin}"></c:out></td>
			<td><c:out value="${list.engineNo}"></c:out></td>
			<td><c:out value="${list.model}"></c:out></td>
			<td><c:out value="${list.productDate}"></c:out></td>
			<td><c:out value="${list.mileage}"></c:out>km</td>
			<td><c:out value="${list.customerName}"></c:out></td>
			<td><c:out value="${list.customerPhone}"></c:out></td>
			<td><c:out value="${list.saleDate}"></c:out></td>
			<td>
                <textarea rows="1" cols="15" readonly="readonly"><c:out value="${list.remark}"></c:out></textarea>
            </td>
		</tr>
	</c:forEach>
</table>

<table align="center" width="95%" class="table_list"
	style="border-bottom: 1px solid #DAE0EE">
	<tr>
		<th colspan="6" align="left"><img class="nav"
			src="<%=contextPath%>/img/subNav.gif" /> 审批明细</th>
	</tr>
	<tr>
		<th>审批时间</th>
		<th>审批人员</th>
		<th>人员部门</th>
		<th>审批状态</th>
		<th>审批意见</th>
	</tr>
	<c:forEach var="listBean" items="${listBean}">
		<tr class="table_list_row1">
			<td><c:out value="${listBean.auditingDate}"></c:out></td>
			<td><c:out value="${listBean.name}"></c:out></td>
			<td><c:out value="${listBean.orgName}"></c:out></td>
			<td><script type='text/javascript'>
	var status = getItemValue('${listBean.status}');
	document.write(status);
</script></td>
			<td><textarea rows="1" cols="15" readonly="readonly"><c:out
				value="${listBean.auditingOpinion}"></c:out></textarea></td>
		</tr>
	</c:forEach>
</table>
</form>
</body>
</html>