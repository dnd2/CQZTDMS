<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包策略车型维护</title>
	</head>
<body onload="queryResult();">
	<form method="post" name="fm" id="fm">		
		<script type="text/javascript">
			   var myPage;
			   //查询该策略设定的车型
			   var url = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/queryStrategyModel.json";
			   url = url + "?ID=" + <%=request.getParameter("ID")%>;			
			   var title = null;
			   var columns = [
			  				{header: "<input type=\"checkbox\" onclick=\"selectAll(this,'modelcb')\" name=\"allcb\"/>", dataIndex:'ID',align:'center',renderer:createCB},
			  				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
			  				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'}
			  				];

				//输出选择框列
				function createCB(value,meta,record){
					return String.format("<input type=\"checkbox\" name=\"modelcb\" value=\"" +record.data.ID+ "\"/>");
				}

				//添加车型信息
				function addModel(){
					var id = <%=request.getParameter("ID")%>;
					//显示车型选择界面
					guaranteeShowMaterialGroup('modelId','','true','3',id,saveModel);
				}

				//保存车型信息
				function saveModel(){
					var modelIds = document.getElementById('modelId').value;
					var turl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/saveGuaranteeStrategyModel.json";
					if(modelIds!=null && modelIds!='Undefined'){//当选择车型不为空时保存设定车型
						makeNomalFormCall(turl,showResult,'fm');
					}
				}

				function deleteModelConfirm(){
					var selectedModel = document.getElementsByName('modelcb');
					var isSelect = false;

					if(selectedModel!=null && selectedModel!='undefined'){
						for(var i=0;i<selectedModel.length;i++){
							if(selectedModel[i].checked){
								isSelect = true;
								break;
							}
						}
					}

					if(isSelect){
						MyConfirm("是否确认删除？",deleteModel,[null]);
					}else{
						MyAlert("请选择车型！");
					}
				}

				//删除设定车型
				function deleteModel(){
					var turl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/deleteGuaranteeStrategyModel.json";
					makeNomalFormCall(turl,showResult,'fm');
				}

				/**
				 * 三包策略车型：选择物料组树界面
				 * inputId   : 回填页面物料组ID域HTML元素id
				 * inputName ：回填页面物料组name域HTML元素id
				 * isMulti   : true值多选，否则单选
				 * groupLevel：输出的物料组等级
				 * strategyId: 三包策略ID
				 * @see DialogManager.js[showMaterialGroup()]
				 *      加入选择车型限制：同一三包规则设定过的车型不再显示       				 
				 */
				function guaranteeShowMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel ,strategyId)
				{
					if(!inputCode){ inputCode = null;}
					if(!inputName){ inputName = null;}
					if(!groupLevel){ groupLevel = null;}
					if(!strategyId){strategyId = -1;}
					OpenHtmlWindow("<%=contextPath%>"+"/claim/basicData/TreeGuaranteesStrategy/guaranteeGroupListQueryInit.do?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&STRATEGYID="+strategyId,770,410);
				}

				function showResult(json){
					queryResult();
				}

				function queryResult(){
					__extQuery__(1);
				}
				
			 </script>     
		  <!--分页  -->
		  <table class="table_edit" id="bottomTab">
		  	  <tr>
		  	  	 <td style="text-align:center">
		  	  	 	 <input type="hidden" name="modelId" id="modelId"/>
		  	  	 	 <input type="hidden" name="strategyId" id="strategyId" value="<%=request.getParameter("ID")%>"/>
		  	  	     <input class="normal_btn"  name="submit" type="button" onclick="addModel();" value ='新增'/>
                     &nbsp;&nbsp;
                     <input class="normal_btn" type="button" name="Submit" onclick="deleteModelConfirm();" value="删除"/>
		  	  	 </td>
		  	  </tr>
		  </table>
		  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		  <br/><br/><br/><br/><br/><br/>
	</form>
</body>
</html>