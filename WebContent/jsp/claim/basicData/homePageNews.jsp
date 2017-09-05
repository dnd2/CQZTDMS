<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="java.util.*"%>
<%@page import="com.infodms.dms.po.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = request.getAttribute("lists") == null ? null : (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script> --%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/kindeditor/kindeditor.js" /></script>
	<title>首页新闻</title>
	<style type="text/css">
		input[type="checkbox"] {
			cursor: pointer;
		}
		i{cursor: pointer;}
	</style>
	<script >
		var __index = 1;
		
		function changeNewsViewType(){
			var obj = document.getElementById('viewNewsType');
			if(obj.value==<%=Constant.VIEW_NEWS_type_1%>){
				// 如果是经销商端
				$("span[name='xwqd']").each(function(){
					$(this).show();
				});
				
				$("input[name='channel']").each(function(){
					if($(this).val() == '11991003'){
						$(this).attr("checked",false);
						$("#wl_xwqd").hide();
					}
				});
				$("input[name='sendOrg']").each(function(){
					$(this).attr("disabled",false);
				});
			}
			else if(obj.value == "<%=Constant.VIEW_NEWS_type_2 %>") {
				// 如果是OEM端
				$("#wl_xwqd").show();
				$("span[name='xwqd']").each(function(){
					$(this).show();
				});
				$("input[name='sendOrg']").each(function(){
					$(this).attr("disabled",false);
				});
			}
			else
			{
				// 如果是公用
				$("span[name='xwqd']").each(function(){
					$(this).hide();
				});
				$("input[name='sendOrg']").each(function(){
					$(this)[0].checked = true;
					$(this).attr("disabled",true);
				});
			}
		}

		function save(){
		    if(document.getElementById("news_type")==null ||document.getElementById("news_type").value==''){
		    	MyAlert("请选择新闻类别!");
		    	return false;
		    }

		    if(document.getElementById("contents")==null ||document.getElementById("contents").value==''){
		    	MyAlert("请填写新闻内容!");
		    	return false;
		    }
		    var channelChoose = false;
		    $("input[name='channel']").each(function(){
		    	if($(this)[0].checked == true) channelChoose = true;
		    });
		    if(channelChoose != true) {
		    	MyAlert("请选择新闻渠道!");
		    	return false;
		    }
		    
			if(!submitForm('fm')) {
				return false;
			}
			makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/saveOrUpdateNews.json',function(json){
				if(json.Exception) {
					MyAlert(json.Exception.message);
				} else {
					MyAlert('发布成功!',function() {
						window.location.href = "<%=contextPath%>/claim/basicData/HomePageNews/mainNews.do";
					});
				}
			}, 'fm');
		}
		
		// 页面初始化
		$(function(){
			KE.show({id : 'contents'});
			changeNewsViewType();
			var channel = $("#channelChecked").val();
			if(channel != ''){
				 $("input[name='channel']").each(function(){
			    	if(channel.search($(this)[0].value) != -1){
			    		$(this)[0].checked = true;
			    	}
			    });
			}
		});
	</script>
</head>
<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;首页新闻
	</div>
	<form name='fm' id='fm' method="post">
		<input name="newsCode" value="${newsCode}" type="hidden" />
 		<input name="newsId" value="${list.NEWS_ID}" type="hidden" />
 		<input name="flag" value="${flag}" type="hidden" />
 		<input type="hidden"  name="companyId" value="${list.ORG_ID}"/>
 		<input type="hidden"  name="companyName" value="${list.ORG_NAME}"/>
 		<input type="hidden"  name="name" value="${list.VOICE_PERSON}"/>
 		<input type="hidden" id="channelChecked" value="${channel }"/>
		<table class="table_query">
			<tr>
			 	<td class="right">新闻编号：</td>
			 	<td>${newsCode}</td>
			 	<td class="right">发表单位：</td>
			 	<td>${list.ORG_NAME}</td>
		 	</tr>
			<tr>
				<td class="right">发表人：</td>
			 	<td >${list.VOICE_PERSON}</td>
			 	<td class="right">发表日期：</td>
			 	<td>${date}</td>
			</tr>
			<tr>
				<td class="right">新闻类型：</td>
				<td>
			 		<script type="text/javascript">
						genSelBoxExp("viewNewsType",<%=Constant.VIEW_NEWS_TYPE%>,"${list.DUTY_TYPE}",false,"","onchange=\"changeNewsViewType()\"","true",'');
				    </script>
				</td>
				<td class="right"><span name="xwqd">新闻渠道：</span></td>
				<td>
					<span name="xwqd">
						<input name="channel" type="checkbox" value="11991001">整车销售
						<input name="channel" type="checkbox" value="11991002">售后备件
						<span id="wl_xwqd"><input name="channel" type="checkbox" value="11991003">储运物流</span>
						<span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
					</span>
				</td>
				
			</tr>
		 	<tr>
		 		<td class="right">新闻类别：</td>
		 		<td>
		 			<script type="text/javascript">
						 genSelBoxExp("news_type",<%=Constant.NEWS_TYPE%>,"${list.NEWS_TYPE }",true,"","","true",'');
				    </script>
		 		</td>
		 		<td class="right">失效日期：</td>
		 		<td>
					<input type="text" class="wdate_txt" id="expiryDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${list.EXPIRY_DATE }"/> 
		 		</td>
		 	</tr>    
		 	<tr>
		 		<td class="right">发送区域：</td>
		 		<td colspan="3">
		 			<c:forEach items="${orgList }" var="list">
		 				<c:choose>
		 					<c:when test="${list.IS_CHECKED eq 1}">
		 						<input type="checkbox" name="sendOrg" value="${list.ORG_ID }" checked="checked"/> ${list.ORG_NAME }
		 					</c:when>
		 					<c:otherwise>
		 						<input type="checkbox" name="sendOrg" value="${list.ORG_ID }"/> ${list.ORG_NAME }
		 					</c:otherwise>
		 				</c:choose>
		 			</c:forEach>
		 		</td>
		 	</tr>       
		 	<tr>
		 		<td class="right">新闻标题：</td>
		 		<td colspan="3">
		 			<input name="title" value='${list.NEWS_TITLE}' type="text" style="width:350px;" class="middle_txt" id="title" maxlength="3000" datatype="0,is_null,300"/>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="right" style="vertical-align: top;">新闻内容：</td>
		 		<td colspan="3">
			 		<textarea name="contents" id="contents" cols="100" rows="8" style="width:740px;height:200px;resize:none">
			 			${list.CONTENTS }
			 		</textarea>
				</td>
		 	</tr>
		 	<tr>
		 		<td class="right">添加附件：</td>
		 		<td colspan="3">
		 			<input type="button" class="u-button"  onclick="showUploadWin()" value ='添加附件'/>
		 			<span id="fileUploadTab">
		 			<%if(attachLs != null) { for(int i=0;i<attachLs.size();i++) { %>
					  	<script type="text/javascript">
					  		editUploadRow('<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
					  	</script>
					<%}} %>
		 			</span>
		 		</td>
		 	</tr>
		 	<tr>
			 	<td class="center" colspan="4">
			 		<input class="u-button" type="button" value="发布" onclick="save();"/> 
			 		<input class="u-button" type="button" onclick="history.back();" value="返回"/>
			 	</td>
			</tr>
			<%-- 
			  	<script type="text/javascript">
			  		addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
			  	</script>
			 --%>
		</table>
	</form>
</div>
</body>
</html>