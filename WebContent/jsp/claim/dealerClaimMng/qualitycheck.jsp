<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
<title>质量信息跟踪卡查看</title>
<script language="javascript">
	
</script>
</head>
<body>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：质量信息跟踪卡查看</div>
  <form>
  <c:if test="${po!=null}">
   <table class="table_edit">
			<tr>
				<th>
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;基本信息
				</th>
			</tr>
	</table>
    <table  class="table_query">
          <tr>
            <td align="right" width="20%">信息发出单位编号：</td>
            <td align="left" width="30%">${po.dealerCode }</td>
            <td align="right" width="20%">投诉类型：</td>
            <td align="left" width="30%">
               <script language="javascript">document.write(getItemValue(${po.complainSort}));</script>
            </td>                   
          </tr>
          <tr>
            <td align="right" width="20%">信息发出单位：</td>
            <td align="left" width="30%">${po.dealerName} </td>
            <td align="right" width="20%">联系电话：</td>
            <td align="left" width="30%">${po.tel}</td>                   
          </tr>
          <tr>
            <td align="right" width="20%">信息类别：</td>
            <td align="left" width="30%">
               <script language="javascript">document.write(getItemValue(${po.informationSort}));</script>
            </td>                 
            <td align="right" width="20%">发出时间：</td>
            <td align="left" width="30%">            
                <fmt:formatDate value="${po.currentDate}" type="both" pattern="yyyy-MM-dd"/>
            </td>                   
          </tr>
          <tr>
            <td align="right" width="20%">车辆识别码：</td>
            <td align="left" width="30%">${po.vin}</td>
            <td align="right" width="20%">车型：</td>
            <td align="left" width="30%">${po.modelCode} </td>                   
          </tr>
          <tr>
            <td align="right" width="20%">发动机号：</td>
            <td align="left" width="30%">${po.engineNo}</td>
            <td align="right" width="20%">出厂日期：</td>            
            <td align="left" width="30%">            
                <fmt:formatDate value="${po.factoryDate}" type="both" pattern="yyyy-MM-dd"/>
            </td>                  
          </tr>
          <tr>
            <td align="right" width="20%">购车日期：</td>
            <td align="left" width="30%">            
                <fmt:formatDate value="${po.purchasedDate}" type="both" pattern="yyyy-MM-dd"/>
            </td>             
            <td align="right" width="20%">行驶里程：</td>
            <td align="left" width="30%">${po.mileage}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">客户姓名：</td>
            <td align="left" width="30%">${po.customName}</td>
            <td align="right" width="20%">联系电话：</td>
            <td align="left" width="30%">${po.phone}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">问题部位：</td>
            <td align="left" width="30%">
                <script language="javascript">document.write(getItemValue(${po.questionPart}));</script>
            </td>
            <td align="right" width="20%">故障性质：</td>
            <td align="left" width="30%">
                <script language="javascript">document.write(getItemValue(${po.faultNatrue}));</script>
            </td>                   
          </tr>
           <tr>
            <td align="right" width="20%">变速器类型：</td>
            <td align="left" width="30%">
                <script language="javascript">document.write(getItemValue(${po.gearboxNatrue}));</script>
            </td>
            <td align="right" width="20%">车辆用途：</td>
            <td align="left" width="30%">${po.vhclUse}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">故障发生时的车速情况：</td>
            <td align="left" width="30%">${po.speedCase}</td>
            <td align="right" width="20%">故障发生时的路面情况：</td>
            <td align="left" width="30%">${po.roadCase}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">故障发生时的装载情况：</td>
            <td align="left" width="30%">${po.loadingCase}</td>
            <td align="right" width="20%">故障发生时的车辆改装情况：</td>
            <td align="left" width="30%">${po.adaptCase}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">故障零部件生产厂家：</td>
            <td align="left" width="30%">${po.produceFactroy}</td>
            <td align="right" width="20%">完整发动机号：</td>
            <td align="left" width="30%">${po.completeEngineno}</td>                   
          </tr>
           <tr>
            <td align="right" width="20%">故障原因及处理意见：</td>
            <td colspan="3" align="left" width="80%">${po.faultOpinion}</td>                           
          </tr>
           <tr>
            <td align="right" width="20%">配套件损坏情况(件名及供应商等)：</td>
            <td colspan="3" align="left" width="80%">${po.damageCase}</td>                    
          </tr>
          <tr>
            <td align="right" width="20%">损失估计：</td>
            <td colspan="3" align="left" width="80%">${po.damagePrice}</td>                    
          </tr>
          <tr>
            <td align="right" width="20%">技术服务处处理意见及处理人,处理时间： </td>
            <td colspan="3" align="left" width="80%">
                ${po.processOpinion}&nbsp;${po.processPerson}&nbsp;
                <fmt:formatDate value="${po.processDate}" type="both" pattern="yyyy-MM-dd"/>
            </td>                    
          </tr>
          <tr>
            <td align="right" width="20%">结果：</td>
            <td colspan="3" align="left" width="80%">${po.result}</td>                    
          </tr>
          <tr>
            <td align="right" width="20%">登记人：</td>
            <td colspan="3" align="left" width="80%">${po.booker}</td>                    
          </tr>
          <tr>
            <td align="right" width="20%">回访单位：</td>
            <td colspan="3" align="left" width="80%">${po.backUnit}</td>                    
          </tr>
          <tr>
            <td align="right" width="20%">回访日期：</td>
            <td colspan="3" align="left" width="80%">            
                <fmt:formatDate value="${po.backDate}" type="both" pattern="yyyy-MM-dd"/>
            </td>                          
          </tr>
          <tr>
            <td align="right" width="20%">围绕原因分析所开展的工作：</td>
            <td colspan="3" align="left" width="80%">${po.caseWork}</td>                    
          </tr>          
           <tr>
            <td align="right" width="20%">问题原因：</td>
            <td align="left" width="30%">${po.questionCase}</td>
            <td align="right" width="20%">结论：</td>
            <td align="left" width="30%">${po.conntion}</td>                   
          </tr>
          <tr>
            <td align="right" width="20%">责任单位：</td>
            <td align="left" width="30%">${po.dutyUnit}</td>
            <td align="right" width="20%">临时措施：</td>
            <td align="left" width="30%">${po.tempStep}</td>                   
          </tr>
          <tr>
            <td align="right" width="20%">永久措施：</td>
            <td align="left" width="30%">${po.forveStep}</td>
            <td align="right" width="20%">建议：</td>
            <td align="left" width="30%">${po.suggest}</td>                   
          </tr>
          <tr>
            <td align="right" width="20%">改进情况客户服务部意见： </td>
            <td colspan="3" align="left" width="80%">${po.serviceOpinion}</td>                    
          </tr>
  </table>	
  </c:if>
  <c:if test="${po==null}">
     <div class="pageTips">质量信息跟踪卡没数据</div>
  </c:if>
  <table class="table_edit">
			<tr>
				<th style="text-align:center;">
				   <input type="button" class="normal_btn" value="关闭" onclick="window.close();"/>
				</th>
			</tr>
	</table>
</form>
</body>
</html>