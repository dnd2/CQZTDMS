<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件回运申请明细</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件回运申请明细</div>
 <form method="post" name ="fm" id="fm">
 	<input type=hidden value="1" name="COMMAND" id="COMMAND"/>
 	<input type=hidden value="${listBean[0].returnApplyId }" name="RETURN_APPLY_ID" id="RETURN_APPLY_ID"/>
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="16" >
					<h3>旧件回运申请表</h3>
				</th>
				<tr  align="center" class="table_list_row1">
				    <td colspan="3" align="left">
				   		服务站名称：${DEALER_NAME }
				    </td>
				    <td colspan="3" align="left">
				   		服务站编码：${DEALER_CODE }
				    </td>
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						回运清单号
					</td>
					<td>
						配件代码
					</td>
					<td>
						配件名称
					</td>
					<td>
						索赔单号
					</td>
					<td>
						条码
					</td>
					<td>
						抵扣原因
					</td>
					<td>
						审核
					</td>
					<c:if test="${IS_OEM==null }">
					<td>
						备注
					</td>
					</c:if>
				</tr>
				<c:forEach items="${listBean}" var="bean">
					<tr>
						<td><input type="hidden" name="returnNo" value="${bean.returnNo }"/>${bean.returnNo }</td>
						<td>${bean.partCode }</td>
						<td>${bean.partName }</td>
						<td>${bean.claimNo }</td>
						<td>${bean.barcodeNo }</td>
						<td><script type="text/javascript">
						 var deductRemark=getItemValue('${bean.deductRemark}');
			              document.write(deductRemark) ;
						</script></td>
						<td>
						<c:choose>
							<c:when test="${VIEW_OR_CHECK==1 }">
								同意<input type="checkbox"  name="IS_AGREE" value="${bean.detailId}"/>										
								<input type="hidden"  name="IS_AGREE1" value="${bean.detailId}"/>										
								<input type="hidden"  name="bool" value=""/>										
							</c:when>
							<c:otherwise>
								<c:if test="${bean.isAgree==95581001}">
									同意
								</c:if>
								<c:if test="${bean.isAgree==95581002}">
									<span style="color: red;">不同意</span>
								</c:if>
								<c:if test="${bean.isAgree==0}">
									未审核
								</c:if>
							</c:otherwise>
						</c:choose>
						</td>
						<td>
						<c:if test="${IS_OEM==null }">
						<c:choose>
						<c:when test="${VIEW_OR_CHECK==1 }">
							<input type=text  name="REMARK" value="${bean.remark}"/>
						</c:when>
						<c:otherwise>
							${bean.remark }
						</c:otherwise>
						</c:choose>
						</c:if>
						</td>
					</tr>
				</c:forEach>
				<tbody id="transportTable">
				</tbody>
			</table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
		<c:if test="${VIEW_OR_CHECK==1 }">
      		<input type="button" id="pass_btn" onclick="checkDo();" class="normal_btn" style="width=8%" value="保存"/>
    	</c:if>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">

	function checkDo()
	{   
		if(submitForm(fm)==false) {
			return;
		}
		var REMARK=document.getElementsByName("REMARK");
		var IS_AGREE=document.getElementsByName("IS_AGREE");
		var IS_AGREE1=document.getElementsByName("IS_AGREE1");
		var bool=document.getElementsByName("bool");
		var temp=0;
		if(IS_AGREE!=undefined && REMARK!=undefined ){
			for(var i=0;i<IS_AGREE.length;i++){
				if(IS_AGREE[i].checked==false && REMARK[i].value==""){
					MyAlert("提示：请填写不同意的备注");
					temp++;
				}else if(IS_AGREE[i].checked==false){
					bool[i].value="0"
					//IS_AGREE1[i].value="0";
				}else if(IS_AGREE[i].checked==true){
					REMARK[i].value="无";
					bool[i].value="1"
					//IS_AGREE1[i].value=IS_AGREE[i].value;
				}
			}
		} 
		if(temp!=0){
			return;
		}
		MyConfirm("确认审核！",checkConfirm, []);
	}
	function checkConfirm()
	{
		$('pass_btn').disabled = true ;
		makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/oemReturnApplyCheck.json",addSpeciaBack,'fm','queryBtn'); 
	}
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/queryOldpartReturnApply.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}

</script>
</body>
</html>
