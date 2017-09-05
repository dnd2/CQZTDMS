<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />	
<title>制造商信息查看</title>
<style>
.img {
	border: none
}
</style>

</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	基础信息管理 &gt; 配件基础信息维护 &gt; 制造商信息维护 &gt; 查看 </div>
	<form id="fm" name="fm" method="post">
	<table class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 制造商信息</th>
	<tr>
		<td width="10%" align="right" >制造商编码：</td>
		<td width="20%" >
			<input class="middle_txt" type="text" name="MAKER_CODE" id="code" value="${makerInfo.MAKER_CODE}" disabled="disabled"/>
		</td>
		<td width="10%" align="right"  >制造商名称：</td>
		<td width="20%"><input type="text" class="normal_txt" name="MAKER_NAME" id="name" value="${makerInfo.MAKER_NAME}" disabled="disabled"/>
	    </td>	
	    
	    <td width="10%" align="right">供应商名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" disabled="disabled" id="VENDER_NAME"  name="VENDER_NAME" value="${makerInfo.VENDER_NAME}"/>
        </td>
                
	</tr>
    <tr>
        
        <td width="10%" align="right"  >国内/国外：</td>
		<td width="20%">
		    <script type="text/javascript">
		        genSelBox("IS_ABROAD",<%=Constant.IS_ABROAD_M%>,${makerInfo.IS_ABROAD},false,'','disabled="disabled"',"");
		    </script>
		</td>	
		
		<td align="right" >制造商类型：</td>
		<td>
		<script type="text/javascript">
		        genSelBox("MAKER_TYPE",<%=Constant.PARTMAKER_TYPE%>,${makerInfo.MAKER_TYPE},false,'','disabled="disabled"',"");
		</script>
        </td>
        
		<td align="right" >联系人：</td>
		<td><input class="middle_txt" type="text" name="LINKMAN" value="${makerInfo.LINKMAN}" disabled="disabled"/></td>
		
	</tr>
    <tr>
        
        <td align="right" >联系电话：</td>
		<td>
			<input type="text" class="normal_txt" name="TEL" id="tel" value="${makerInfo.TEL}" disabled="disabled"/>
		</td>
        <td align="right" >传真：</td>
		<td>
			<input type="text" class="normal_txt" name="FAX" value="${makerInfo.FAX}" disabled="disabled"/>
		</td>
		<td align="right" >是否有效：</td>
		<td>
		 <script type="text/javascript">
		       genSelBox("STATE",<%=Constant.STATUS%>,${makerInfo.STATE},false,'','disabled="disabled"',"");
		  </script>
	    </td>
      </tr>
       <tr>
                <td width="10%" align="right">地址：</td>
                <td colspan="5">
                <input class="maxlong_txt" type="text" name="ADDR" id="addr" value="${makerInfo.ADDR}" disabled="disabled"/>
                </td>
            </tr>
	</table>
	<table class="table_edit">
	<tr>
    	<td align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="关闭" onclick="_hide();"  class="normal_btn"/>
        </td>
    </tr>
  </table>
  </form>
</div>
</body>
</html>
