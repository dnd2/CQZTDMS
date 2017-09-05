<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>人员修改</title>

</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 机构人员查询</div>
<form id="fm" name="fm" method="post">
	<table class="table_query" border="0">
		<tr>
			<td align="right">经销商代码：</td>
			<td align="left">
			${dealer.dealerCode}
			</td>
			<td align="right">经销商名称：</td>
			<td align="left">
			${dealer.dealerName} 
			</td>
		</tr>
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
			${tvprp.name}
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
			${tvprp.idNo}
			</td>
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
		  <td align="left">
		  	${tvprp.email}
		  </td>
		  <td align="right">联系手机：</td>
		  <td align="left">
		  ${tvprp.mobile}
		  </td>
	  </tr>
	  <tr>
          <td align="right">职位：</td>
		  <td align="left">
		  <script>document.write(getItemValue('${tvprp.position}'));</script>
	       </td>
	       <td align="right">性别：</td>
			<td align="left">
			 <script>document.write(getItemValue('${tvprp.gender}'));</script>
	         </td>
	  	</tr>
		<tr>
			<td align="right">入职日期：</td>
			<td align="left">
			${entryDate}
			</td>
			 <td align="right">是否投资人：</td>
		  <td align="left">
		  <script>document.write(getItemValue('${tvprp.isInvestor}'));</script>
	      </td>
		</tr>
		<tr>
          <td align="right">所属银行：</td>
		  <td align="left">
		   <script>document.write(getItemValue('${tvprp.bank}'));</script>
		   </td>
		   <td align="right">银行卡号：</td>
		  <td align="left">
		  ${tvprp.bankCardno}
          </td>
	  </tr>
		<tr>
			<td align="right">业绩积分：</td>
			<td align="left">
				${tvprp.performanceInteg}
			</td>
			<td align="right">认证积分：</td>
			<td align="left">
				${tvprp.authenticationInteg}
			</td>
		</tr>
		<tr>
			<td align="right">年限积分：</td>
			<td align="left">
				${tvprp.yearInteg}
			</td>
			<td align="right">已兑现业绩积分：</td>
			<td align="left">
				${tvprp.alreadyAgainstPinteg}
			</td>
		</tr>
		<tr>
			<td align="right">已兑现认证积分：</td>
			<td align="left">
				${tvprp.alreadyAgainstAinteg}
			</td>
			<td align="right">已兑现年限积分：</td>
			<td align="left">
				${tvprp.alreadyAgainstYearinteg}
			</td>
		</tr>
		<tr>
		<tr>
			<td align="right">最后认证时间：</td>
			<td align="left">
				${authenDate} 
			</td>
			<td align="right">认证等级：</td>
			<td align="left">
			<script>document.write(getItemValue('${tvprp.authenticationLevel}'));</script>
				
			</td>
		</tr>
		<tr>
			<td align="right">连续三个月为零：</td>
			<td align="left">
			<script>document.write(getItemValue('${tvprp.threeMonthZero}'));</script>
				
			</td>
			<td align="right">离职时间：</td>
			<td align="left">
			${leaveDate}
				
			</td>
		</tr>
		<tr>
			<td align="right">最后审核时间：</td>
			<td align="left">
				${auditDate}
			</td>
			<td align="right">创建日期：</td>
			<td align="left">
				${createDate} 
			</td>
		</tr>
		<tr>
			<td align="right">剩余积分：</td>
			<td align="left">
				${tvprp.remainInteg}
			</td>
			<td align="right">学历：</td>
			<td align="left">
				
				<script>document.write(getItemValue('${tvprp.degree}'));</script>
			</td>
		</tr>
		<tr>
			<td align="right">备注：</td>
			<td align="left">
				${tvprp.remark}
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
		  <td align="center" colspan="4">
		  <input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		  </td>
		</tr>
	</table>
</form>
</div>
</body>
</html>
