<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script src="<%=request.getContextPath()%>/js/notice/noticeModel/noticeModel.js" ></script>
<title>消息提醒模版维护</title>
</head>
<body onload="initPage();">
<div class="wbox">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:系统管理&gt;经销商管理&gt;消息提醒模版维护
	<div id="noticeModelEditDiv">
		<form id="noticeEditForm" method="post" >
		<input type="hidden" id="id" name="id" value="${nmBean.id }" />
		<table id="noticeModelEditTable" class="table_query" border="0">
			<tr>
				<td class="tblopt">模版名称</td>
				<td colspan="2">
					<input type="text" id="nmModelname" name="nmModelname" datatype="0,is_null,20" value="${nmBean.nmModelname }"/>
				</td>
				<td class="tblopt">模版描述</td>
				<td colspan="2">
					<textarea rows="3" cols="60" id="nmModeldesc" name="nmModeldesc" datatype="0,is_textarea,200">${nmBean.nmModeldesc }</textarea>
				</td>
			</tr>
			<tr>
				<td class="tblopt">提醒菜单</td>
				<td colspan="2">
					<input type="hidden" id="nmMenuid" name="nmMenuid" value="${nmBean.nmMenuid }"/>
					<input type="text" id="nmMenuname" name="nmMenuname" readonly="readonly" datatype="0,is_null,200" value="${nmBean.funcName }"/>
					<input name="menubtn" type="button" class="mini_btn" onclick="showAllFunc('nmMenuname','nmMenuid','false');" value="..." />
				</td>
				<td class="tblopt">经销商字段</td>
				<td colspan="2">
					<select id="nmDealerfield" name="nmDealerfield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmDealerfield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmDealerfield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
			</tr>
			<tr>
				<td class="tblopt">提醒对象字段</td>
				<td colspan="2">
					<select id="nmTarfield" name="nmTarfield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmTarfield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmTarfield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">提醒对象类型</td>
				<td colspan="2">
					<select id="nmTartype" name="nmTartype" datatype='0,is_null,200' onchange="changeTartype(this)">
						<option>--请选择--</option>
						<c:forEach items="${tarTypeList }" var="tt" varStatus="index">
							<c:choose>
								<c:when test="${tt.id==nmBean.nmTartype }">
									<option value="${tt.id }" selected="selected">${tt.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${tt.id }">${tt.name }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<c:if test="${nmBean.nmTarvalue!=null }">
						<input type="text" id="nmTarvalue" name="nmTarvalue" value="${nmBean.nmTarvalue}" datatype='0,is_null,200' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="tblopt">业务单ID字段</td>
				<td colspan="2">
					<select id="nmBusinessidfield" name="nmBusinessidfield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmBusinessidfield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmBusinessidfield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">业务单号字段</td>
				<td colspan="2">
					<select id="nmBusinessnumfield" name="nmBusinessnumfield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmBusinessnumfield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmBusinessnumfield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
			</tr>
			<tr>
				<td class="tblopt">提醒状态字段</td>
				<td colspan="2">
					<select id="nmNoticestatefield" name="nmNoticestatefield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmNoticestatefield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmNoticestatefield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">提醒状态阀值</td>
				<td colspan="2">
					<select id="nmNoticestaterelation" name="nmNoticestaterelation" datatype='0,is_null,200'>
						<option>--请选择--</option>
						<c:forEach items="${relationList }" var="tt" varStatus="index">
							<c:choose>
								<c:when test="${tt.id==nmBean.nmNoticestaterelation }">
									<option value="${tt.id }" selected="selected">${tt.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${tt.id }">${tt.name }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<input type="text" id="nmNoticestatevalue" name="nmNoticestatevalue" datatype='0,is_null,20'  value="${nmBean.nmNoticestatevalue }"/>
				</td>
			</tr>
			<tr>
				<td class="tblopt">提醒时间字段</td>
				<td colspan="2">
					<select id="nmNoticetimefield" name="nmNoticetimefield" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmNoticetimefield!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmNoticetimefield }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">提示时间差(分钟)</td>
				<td colspan="2">
					<input type="text" id="nmNoticemintime" name="nmNoticemintime" datatype='0,is_digit,5' value="${nmBean.nmNoticemintime }"/>
				</td>
			</tr>
			<tr>
				<td class="tblopt">启用</td>
				<td colspan="2">
					<c:choose>
						<c:when test='${nmBean!=null&&"1"==nmBean.nmModelstate }'>
							<input type="checkbox" id="nmModelstate" name="nmModelstate" checked="checked" value="1"/>
						</c:when>
						<c:otherwise>
							<input type="checkbox" id="nmModelstate" name="nmModelstate" value="1"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tblopt">提醒对象类型</td>
				<td colspan="2">
					<select id="nmNoticetype" name="nmNoticetype" datatype='0,is_null,200'>
						<option>--请选择--</option>
						<c:forEach items="${noticeTypeList }" var="tt" varStatus="index">
							<c:choose>
								<c:when test="${tt.id==nmBean.nmNoticetype }">
									<option value="${tt.id }" selected="selected">${tt.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${tt.id }">${tt.name }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<c:if test="${nmBean!=null&&nmBean.nmOtherstatefield1!=null }">
			<tr>
				<td class="tblopt">附加条件1</td>
				<td colspan="2">
					<select id="nmOtherstatefield1" name="nmOtherstatefield1" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmOtherstatefield1!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmOtherstatefield1 }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">附加条件值1</td>
				<td colspan="2">
					<select id="nmOtherstaterelation1" name="nmOtherstaterelation1" datatype='0,is_null,200'>
						<option>--请选择--</option>
						<c:forEach items="${relationList }" var="tt" varStatus="index">
							<c:choose>
								<c:when test="${tt.id==nmBean.nmOtherstaterelation1 }">
									<option value="${tt.id }" selected="selected">${tt.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${tt.id }">${tt.name }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<input type="text" id="nmOtherstatevalue1" name="nmOtherstatevalue1" datatype='0,is_null,20'  value="${nmBean.nmOtherstatevalue1 }"/>
					<input style="float: right" type="button" onclick="delRow(this);" value="删除条件" class="cssbutton"/>
				</td>
			</tr>
			</c:if>
			<c:if test="${nmBean!=null&&nmBean.nmOtherstatefield2!=null }">
			<tr>
				<td class="tblopt">附加条件2</td>
				<td colspan="2">
					<select id="nmOtherstatefield2" name="nmOtherstatefield2" datatype='0,is_null,200'>
					<c:if test="${nmBean!=null&&nmBean.nmOtherstatefield2!=null }">
						<option>--请选择--</option>
						<c:forEach items="${fieldList}" var="field" varStatus="index">
							<c:choose>
								<c:when test="${field.COLUMN_NAME==nmBean.nmOtherstatefield2 }">
									<option value="${field.COLUMN_NAME }" selected="selected">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:when>
								<c:otherwise>
									<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					</select>
				</td>
				<td class="tblopt">附加条件值2</td>
				<td colspan="2">
					<select id="nmOtherstaterelation2" name="nmOtherstaterelation2" datatype='0,is_null,200'>
						<option>--请选择--</option>
						<c:forEach items="${relationList }" var="tt" varStatus="index">
							<c:choose>
								<c:when test="${tt.id==nmBean.nmOtherstaterelation2 }">
									<option value="${tt.id }" selected="selected">${tt.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${tt.id }">${tt.name }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<input type="text" id="nmOtherstatevalue2" name="nmOtherstatevalue2" datatype='0,is_null,20'  value="${nmBean.nmOtherstatevalue2 }"/>
					<input style="float: right" type="button" onclick="delRow(this);" value="删除条件" class="cssbutton"/>
				</td>
			</tr>
			</c:if>
		</table>
		</form>
	</div>
	<div id="buttonDiv" align="center">
		<input name="button" type="button" class="cssbutton" onclick="addNoticeState();" value="增加条件" />
		<input name="button" type="button" class="cssbutton" onclick="saveNoticeModel();" value="保存" />
		<input name="button" type="button" class="cssbutton" onclick="window.history.back(-1);" value="返回" />
	</div>
</div>
<div id="hiddenDiv" style="display: none">
	<select id="columnSelect">
		<option>--请选择--</option>
		<c:forEach items="${fieldList}" var="field" varStatus="index">
			<option value="${field.COLUMN_NAME }">${field.COLUMN_NAME }:${field.COMMENTS }</option>
		</c:forEach>
	</select>
	<select id="relationSelect">
		<option>--请选择--</option>
		<c:forEach items="${relationList }" var="tt" varStatus="index">
			<option value="${tt.id }">${tt.name }</option>
		</c:forEach>
	</select>
	
	<input style="float: right" type="button" onclick="delRow(this);" id="delRow" value="删除条件" class="cssbutton"/>
	<input type="text" id="nmTarValueModel" name="nmTarvalue" datatype='0,is_null,200' />
</div>
</body>
</html>