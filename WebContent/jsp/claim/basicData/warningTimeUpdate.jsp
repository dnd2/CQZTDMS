<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.po.TtAsWarningTimePO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"  %>
<%@ page import="java.util.Map"  %>
<% String contextPath = request.getContextPath(); %>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>维修时间预警规则新增</title>
</head>
<body>
<div class="wbox">
	<div class="navigation">
  		<img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔基本数据&gt;时间预警规则维护&gt;更新
  	</div>
  	<% TtAsWarningTimePO po = (TtAsWarningTimePO)request.getAttribute("warninfo"); %>
  	<form method="post" name="fm" id="fm">
  	<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
	    <table class="table_query" >
	     	<tr>  
	            <td style="text-align:right">预警规则：</td>
	            <td align="left">
					<input type='text'  name='WAINING_CODE' value='<%=po.getWarningCode() %>' id='WAINING_CODE'  class="middle_txt" datatype="0,is_null,30" />
	            	<input type="hidden" name="ID" id="ID" value="<%=po.getId() %>" />
	            </td>
	            <td style="text-align:right">预警说明：</td>
	            <td align="left">
					<input type='text'  name='WAINING_REMARK'  id='WAINING_REMARK' value='<%=po.getWainingRemark() %>'   class="middle_txt" datatype="0,is_null,100" />
	            </td>
				<td style="text-align:right">预警等级：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("WARNING_LEVEL",<%=Constant.SWANINGTIME_LEVEL%>,<%=po.getWainingLevel()%>,true,"","","true",'');
				    </script>
	            </td>
	          </tr>
	          <tr>
	            <td style="text-align:right">预警类型：</td>
	            <td align="left">
	            	<script type="text/javascript">
						 genSelBoxExp("WARNING_TYPE",<%=Constant.SWANINGTIME_TYPE%>,<%=po.getWainingType()%>,true,"","","true",'');
				    </script>
	            </td>
	            <td style="text-align:right">状态：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("STATUS",<%=Constant.STATUS%>,<%=po.getStatus()%>,true,"","","true",'');
				    </script>
				</td>
				<td style="text-align:right">有效期(月)：</td>
	            <td align="left"><input type='text'  name='VALID_DATE'  id='VALID_DATE' value='<%=po.getValidDate() %>' class="middle_txt" datatype="0,isDigit,10"/></td>
	          </tr>
	          <tr>
	            <td style="text-align:right">有效里程(公里)：</td>
	            <td align="left"><input type='text'  name='VALID_MILEAGE'  id='VALID_MILEAGE' value='<%=po.getValidMileage()%>' class="middle_txt" datatype="0,isDigit,10"/></td>
	            <td style="text-align:right">预警起时间(日)：</td>
	            <td align="left"><input type='text'  name='START_DATE'  id='START_DATE' value='<%=po.getWarningTimeStart()%>' class="middle_txt" datatype="0,isDigit,10"/></td>
	            <td style="text-align:right">预警止时间(日)：</td>
	            <td align="left"><input type='text'  name='END_DATE'  id='END_DATE' value='<%=po.getWarningTimeEnd()%>' class="middle_txt" datatype="0,isDigit,10"/></td>
	          </tr>
	          <tr>
				<td style="text-align:right">法规条款：</td>
				<td colspan="5">
				<textarea id="CLAUSE_STATUTE" name="CLAUSE_STATUTE" rows="3" cols="80"  datatype="1,is_textarea,1000"><%=po.getClauseStatute()%></textarea>
				<span style="color: red">*</span>
				</td>
			 </tr>
			 </table>
			 </div>
			 </div>
			 <div class="form-panel">
				<h2>授权角色</h2>
					<div class="form-body">	
			 <table class="table_list">
				<%
				List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("allAuthinfo");
				List<Map<String,Object>> sellist = (List<Map<String,Object>>)request.getAttribute("selAuthinfo");
				for(int i = 0;i<list.size();i++){
					Map<String,Object> map = list.get(i);
					if(i%3==0){
						%>
						<tr>
						<%
					}
					%>
					<td align="left"><input type="checkbox" name="chbox" id="chbox" value="<%=map.get("APPROVAL_LEVEL_CODE")%>"
						 <%
						for(int j = 0;j<sellist.size();j++){
							Map<String,Object> selmap = sellist.get(j);
							if(selmap.get("APPROVAL_LEVEL_CODE").equals(map.get("APPROVAL_LEVEL_CODE"))){
								%> checked="checked"<%
							}
						}
						%> 
					/>
					<%=map.get("APPROVAL_LEVEL_NAME")%>
					</td>
					<%
					if(i%3==2){
						%>
						</tr>
						<%
					}
				}
				%>
				</table>
				<input type="hidden" name="APPROVAL_LEVER" id="APPROVAL_LEVER"/>
				</td>
			 </tr>
			 </table>
			 <table class="table_query">
			 <tr>
			    <td colspan="6" style="text-align:center">
				   <input class="normal_btn" type="button" name="button1" id="commitBtn" value="保存"  onclick="saveWarning()"/>&nbsp;&nbsp;
				   <input class="normal_btn" type="button" name="button2" value="返回"  onclick="warningQI();"/>	   
			    </td>
		     </tr>   
	  </table>
	  </div>
	  </div>
  </form>
	<script type="text/javascript">
        //保存
		function saveWarning(){
			var warning_type = $('#WARNING_TYPE')[0].value;
			if(warning_type==null || warning_type==''){
				MyAlert("请选择预警类型!");
				return ;
			}
			var warning_level = $('#WARNING_LEVEL')[0].value;
			if(warning_level==null || warning_level==''){
				MyAlert("请选择预警等级!");
				return ;
			}
			var status = $('#STATUS')[0].value;
			if(status==null || status==''){
				MyAlert("请选择 状态!");
				return ;
			}
			var clause_statute = document.getElementById("CLAUSE_STATUTE").value;
			if(clause_statute==null || clause_statute==''){
				MyAlert("法规条款信息不能为空!");
				return;
			}
			if(clause_statute && clause_statute.length>100){
				MyAlert("法规条款信息长度不能超过 100!");
				return;
			} 
			
			var flagBool = false ;
			var chkBox = document.getElementsByName("chbox") ;
			for(var i=0; i<chkBox.length; i++) {
				if(chkBox[i].checked){
					flagBool = true ;
				}
			}
			
			if(!flagBool) {
				MyAlert("请选择授权角色") ;
				return false ;
			}
			
			if(submitForm('fm')){//查询表单数据格式
				MyConfirm("是否确认修改？",frmSubmit,[null]);
			}
		}
		function frmSubmit(){
			var chboxs = document.getElementsByName("chbox");
			var codes = '';
			for(var i=0;i<chboxs.length;i++){
				if(chboxs[i].checked){
					codes = codes+chboxs[i].value+",";
				}
			}
			 $('#APPROVAL_LEVER')[0].value = codes.substring(0,codes.length-1);
			 
			 document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/claim/basicData/WarningTime/warningTimeUpdate.json',showResult,'fm');		
				
			<%-- fm.action  = "<%=contextPath%>/claim/basicData/WarningTime/warningTimeUpdate.do";
			fm.submit(); --%>
		}
		
		function showResult(json){
			if(json.flag == "success"){
				MyAlert("修改成功！");
				__parent().__extQuery__(1) ;
				_hide() ;
				<%-- window.location.href = "<%=contextPath%>/claim/authorization/RuleMain/ruleInit.do"; --%>
			}else{
				MyAlert("新增失败，请联系管理员！");
			}
		}
		
		function warningQI(){
			window.location.href='<%=contextPath%>/claim/basicData/WarningTime/warningTimeQueryInit.do';
		}
	</script>
	</div>
</body>
</html>