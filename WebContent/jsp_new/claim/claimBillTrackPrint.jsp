<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.text.DecimalFormat" %>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%> 
<%
Double price1 = 0.0;//材料费
Double price2 = 0.0; //工时费
DecimalFormat   df  =   new  DecimalFormat("##0.00");
List<TtAsWrApplicationExtPO> list = (LinkedList<TtAsWrApplicationExtPO>) request.getAttribute("list2");
// List<Map<String, Object>> ttClaimAccessoryDtlList = request.getAttribute("claimNoAccessoryList");
%>
<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    p {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请单打印</title>
	</head>
<body onload="printQieh(${bean.claimType });">
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<br/>
	<center>
		<strong>
			<font size="4">
				<c:if test="${bean.balanceYieldly == 95411001 }">北汽幻速质量保证服务结算单</c:if>
				<c:if test="${bean.balanceYieldly == 95411002 }">北汽幻速质量保证服务结算单</c:if>
				(<script type="text/javascript">document.write(getItemValue('${bean.claimType}'));</script>)
			</font>
		</strong>
	</center>
<br/>
<form method="post" name="fm" id="fm">
  <input type="hidden" name="codeId" value="80081001"/>
  <table class="tabp2" align="center">
    <tr>
      <td class="tdp" nowrap> 经销商名称： </td>
      <td class="tdp" colspan="5" > ${bean.dealerName } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 索赔单类型： </td>
      <td class="tdp" id="claimTypename"><script type="text/javascript">
				document.write(getItemValue('${bean.claimType}'));
				</script></td>
      <td class="tdp" nowrap> 索赔单号： </td>
      <td class="tdp"> ${bean.claimNo } </td>
      <td class="tdp" nowrap> 开工单日期： </td>
      <td class="tdp"> ${bean.roDate } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 经销商代码： </td>
      <td class="tdp"> ${bean.dealerCode } </td>
      <td class="tdp" nowrap>审核时间：</td>
      <td  class="tdp"> ${bean.reportDate } </td>
      <td class="tdp"> 生产厂家： </td>
      <td class="tdp"> ${bean.areaName } </td>
    </tr>
    <tr>
      <td class="tdp"> 车型： </td>
      <td class="tdp"> ${bean.modelCode } </td>
      <td class="tdp" nowrap> 车型状态： </td>
      <td class="tdp"> ${bean.groupCode } </td>
      <td class="tdp" nowrap> 购车日期： </td>
      <td class="tdp" nowrap> ${bean.buyDate } </td>
    </tr>
    <tr>
      <td class="tdp"> VIN： </td>
      <td class="tdp"> ${bean.vin } </td>
      <td class="tdp" nowrap> 发动机号： </td>
      <td class="tdp"> ${bean.engineNo } </td>
      <td class="tdp" nowrap> 牌照号： </td>
      <td class="tdp"> ${bean.licenseNo } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 送修人电话： </td>
      <td class="tdp">${bean.deliverPhone }</td>
      <td class="tdp" nowrap> 送修人姓名： </td>
      <td class="tdp"> ${bean.deliverer } </td>
      </td>
      <td class="tdp" nowrap> 接待员： </td>
      <td class="tdp"> ${bean.serviceAdvisor } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 用户电话： </td>
      <td class="tdp">${bean.customerPhone }</td>
       <td class="tdp" nowrap> 用户姓名： </td>
      <td class="tdp">${bean.customerName }</td>
      <td class="tdp" nowrap> 出厂日期： </td>
      <td class="tdp" ><fmt:formatDate value="${bean.productDate }"  pattern="yyyy-MM-dd hh:mm:ss"/>  </td>
      </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 工单结算日期： </td>
      <td class="tdp"> ${bean.banlanceDate } </td>
      <td class="tdp" nowrap> 行驶里程： </td>
      <td class="tdp"> ${bean.inMileage }(km) </td>
      <td class="tdp" nowrap> 维修次数： </td>
      <td class="tdp"> ${repairTimes } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap> 装配代码： </td>
      <td class="tdp" colspan="3" ><!-- 				A58B22D1E5F7G9H8J3LR0S3V2W1X5Z1
 -->
        ${bean.materialCode } </td>
         <td class="tdp" nowrap> 车系名称： </td>
      <td class="tdp" >
        ${bean.seriesName } </td>
    </tr>
    <tr>
      <td class="tdp" nowrap style="border-bottom-color: white;">用户地址：</td>
      <td colspan="5" class="tdp" style="border-bottom-color: white;"> ${bean.address } </td>
    </tr>
    <!--  ---------------   售前维修开始                            ---------------- -->
    <div id="shouqianweix" style="display: none">
      <table class="tabp2"  align="center" id="shouqianweix1" style="display: none">
        <tr>
          <td class="tdp"> 故障现象描述： </td>
          <td colspan="5"  class="tdp"> ${bean.reason} </td>
        </tr>
        <tr>
          <td  class="tdp"> 故障原因： </td>
          <td colspan="5" class="tdp"> ${bean.miaoShu  } </td>
        </tr>
        <tr>
          <td  class="tdp"> 维修措施： </td>
          <td colspan="5" class="tdp"> ${bean.repairMethod } </td>
        </tr>
        <tr>
          <td  class="tdp"> 备注： </td>
          <td colspan="5"  class="tdp"><c:if test="${bean.remarks != 'null'}"> ${bean.remarks } </c:if></td>
        </tr>
        <tr>
          <td  class="tdp" width="10%"> 授权说明： </td>
          <td  class="tdp" colspan="5"><c:if test="${ bean.opinion != 'null' }"> ${bean.opinion } </c:if></td>
        </tr>
        <tr>
          <td class="tdp"> 授权人： </td>
          <td class="tdp"> ${bean.auditName } </td>
          <td class="tdp""> 总申报工时数： </td>
          <td class="tdp"> ${bean.labourHours } </td>
          <td class="tdp"> 总申报材料费： </td>
          <td  class="tdp"> ${bean.partAmount } </td>
        </tr>
        <tr>
          <td class="tdp"> 总申报费用： </td>
          <td class="tdp"> ${bean.grossCredit } </td>
          <td class="tdp"> 追加工时数： </td>
          <td  class="tdp"> ${bean.appendlabourNum } </td>
          <td class="tdp"> 追加工时费： </td>
          <td  class="tdp"> ${bean.appendlabourAmount } </td>
        </tr>
        <tr>
          <table class="tabp2"  align="center" id="shouqianweix2" style="display: none">
            <tr>
              <td class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">数量</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">顾客问题</td>
              <td class="tdp" colspan="1" nowrap style="border-top-color: white;">旧件代码</td>
              <td class="tdp" colspan="1" nowrap style="border-top-color: white;">旧件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">配件费</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时数</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时费</td>
            </tr>
            <% if(list!=null && list.size()>0){
				 for(int i=0;i<list.size();i++){%>
            <tr style='' >
              <td class="tdp"><%=i+1 %></td>
              <td class="tdp"><%=list.get(i).getPartCode() %></td>
              <td class="tdp" ><%=list.get(i).getPartName() %></td>
              <td class="tdp" align="center"><%=list.get(i).getQuantity() %></td>
              <td class="tdp"><%=list.get(i).getLabourCode() %></td>
              <td class="tdp"><%=list.get(i).getLabourName() %></td>
              <td class="tdp"><%=list.get(i).getRemark()==null ? "":list.get(i).getRemark()==null %></td>
              <td class="tdp"><%=list.get(i).getDownCode() %></td>
              <td class="tdp"><%=list.get(i).getDownName() %></td>
              <td class="tdp"><%=list.get(i).getAmount() %></td>
              <td class="tdp"><%=list.get(i).getLabourHours() %></td>
              <td class="tdp"><%=list.get(i).getLabourAmount() %></td>
            </tr>
            <%	 }  } else{%>
            <tr style='' >
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
            </tr>
            <%  }%>
          </table>
          <table class="tabp2"  align="center" id="fuliao7" style="display: none">
            <tr>
              <td  class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td width="40%" class="tdp" nowrap style="border-top-color: white;">金额</td>
            </tr>
            <c:forEach items="${claimNoAccessoryList}" var="claimNoAccessoryList" varStatus="i">
            <tr style='' >
              <td class="tdp">${i.index+1}</td>
              <td class="tdp">${claimNoAccessoryList.WORKHOUR_CODE}</td>
              <td class="tdp" >${claimNoAccessoryList.WORKHOUR_NAME}</td>
              <td class="tdp" align="right">${claimNoAccessoryList.PRICE}&nbsp;</td>
            </tr>
            </c:forEach>
            <tr>
            <td class="tdp"colspan="2">辅料关联主因件</td>
            <td class="tdp" colspan="2">
            ${claimNoAccessoryList[0].MAIN_PART_CODE}
            </td>
            </tr>
          </table>
        </tr>
      </table>
    </div>
    <!--  ---------------   售前维修结束                            ---------------- -->
    <!--  ---------------   急件开始                            ---------------- -->
    <div id="jijian" style="display: none">
      <table class="tabp2"  align="center" id="jijian1" style="display: none">
        <tr>
          <td class="tdp"> 故障现象描述： </td>
          <td colspan="5"  class="tdp"> ${bean.miaoShu } </td>
        </tr>
        <tr>
          <td  class="tdp"> 故障原因： </td>
          <td colspan="5" class="tdp"> ${bean.reason } </td>
        </tr>
        <tr>
          <td  class="tdp"> 维修措施： </td>
          <td colspan="5" class="tdp"> ${bean.repairMethod } </td>
        </tr>
        <tr>
          <td  class="tdp"> 备注： </td>
          <td colspan="5"  class="tdp"><c:if test="${bean.remarks != 'null'}"> ${bean.remarks } </c:if></td>
        </tr>
        <tr>
          <td  class="tdp" > 授权说明： </td>
          <td  class="tdp" colspan="5"><c:if test="${ bean.opinion != 'null' }"> ${bean.opinion } </c:if></td>
        </tr>
        <tr>
          <td class="tdp"> 授权人： </td>
          <td class="tdp"> ${bean.auditName } </td>
          <td class="tdp""> 总申报工时数： </td>
          <td class="tdp"> ${bean.labourHours } </td>
          <td class="tdp"> 总申报材料费： </td>
          <td  class="tdp"> ${bean.partAmount } </td>
        </tr>
        <tr>
          <td class="tdp"> 总申报费用： </td>
          <td class="tdp"> ${bean.grossCredit } </td>
          <td class="tdp"> 追加工时数： </td>
          <td  class="tdp"> ${bean.appendlabourNum } </td>
          <td class="tdp"> 追加工时费： </td>
          <td  class="tdp"> ${bean.appendlabourAmount } </td>
        </tr>
        <tr>
          <table class="tabp2"  align="center" id="jijian2" style="display: none">
            <tr>
              <td class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">数量</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">顾客问题</td>
              <td class="tdp" colspan="1" nowrap style="border-top-color: white;">旧件代码</td>
              <td class="tdp" colspan="1" nowrap style="border-top-color: white;">旧件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">配件费</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时数</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时费</td>
            </tr>
            <% if(list!=null && list.size()>0){
				 for(int i=0;i<list.size();i++){%>
            <tr style='' >
              <td class="tdp"><%=i+1 %></td>
              <td class="tdp"><%=list.get(i).getPartCode() %></td>
              <td class="tdp" ><%=list.get(i).getPartName() %></td>
              <td class="tdp" align="center"><%=list.get(i).getQuantity() %></td>
              <td class="tdp"><%=list.get(i).getLabourCode() %></td>
              <td class="tdp"><%=list.get(i).getLabourName() %></td>
              <td class="tdp"><%=list.get(i).getRemark()== null ? "":list.get(i).getRemark() %></td>
              <td class="tdp"><%=list.get(i).getDownCode() %></td>
              <td class="tdp"><%=list.get(i).getDownName() %></td>
              <td class="tdp"><%=list.get(i).getAmount() %></td>
              <td class="tdp"><%=list.get(i).getLabourHours() %></td>
              <td class="tdp"><%=list.get(i).getLabourAmount() %></td>
            </tr>
            <%	 }  } else{%>
            <tr style='' >
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
            </tr>
            <%  }%>
          </table>
          <table class="tabp2"  align="center" id="fuliao12" style="display: none">
            <tr>
              <td  class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td width="40%" class="tdp" nowrap style="border-top-color: white;">金额</td>
            </tr>
            <c:forEach items="${claimNoAccessoryList}" var="claimNoAccessoryList" varStatus="i">
            <tr style='' >
              <td class="tdp">${i.index+1}</td>
              <td class="tdp">${claimNoAccessoryList.WORKHOUR_CODE}</td>
              <td class="tdp" >${claimNoAccessoryList.WORKHOUR_NAME}</td>
              <td class="tdp" align="right">${claimNoAccessoryList.PRICE}&nbsp;</td>
            </tr>
            </c:forEach>
            <tr>
            <td class="tdp"colspan="2">辅料关联主因件</td>
            <td class="tdp" colspan="2">
            ${claimNoAccessoryList[0].MAIN_PART_CODE}
            </td>
            </tr>
          </table>
        </tr>
      </table>
    </div>
    <!--  ---------------   急件结束                            ---------------- -->
    <!--  ---------------   外出维修开始                            ---------------- -->
    <div id="waichuweix" style="display: none">
        <table class="tabp2"  style="display: none;" id="waichuweix3" align="center">
          <tr>
            <td class="tdp" nowrap align="left"  width="13%"> 住宿费： </td>
            <td class="tdp" align="left"  width="13%" > ${zhushu } </td>
            <td class="tdp" nowrap align="left"  width="13%"> 餐补费： </td>
            <td class="tdp"  width="13%" > ${canbu } </td>
            <td class="tdp" nowrap align="left"  width="13%"> 人员补助： </td>
            <td class="tdp"  width="13%"> ${buzhu } </td>
          </tr>
          <tr>
            <td class="tdp" style="border-bottom-color: white;" align="left"> 过路过桥费： </td>
            <td class="tdp" style="border-bottom-color: white;"> ${guoqiao } </td>
            <td class="tdp" nowrap style="border-bottom-color: white;" align="left"> 交通补助费： </td>
            <td colspan="3" class="tdp" style="border-bottom-color: white;"> ${jiaot } </td>
          </tr>
        </table>
    </div>
    <table class="tabp2"  align="center" id="waichuweix1" style="display: none">
      <tr>
        <td class="tdp"> 外出开始时间： </td>
        <td class="tdp"><fmt:formatDate value="${tawep.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td class="tdp"> 外出结束时间： </td>
        <td class="tdp"><fmt:formatDate value="${tawep.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td class="tdp"> 派车车牌号： </td>
        <td class="tdp"> ${tawep.outLicenseno} </td>
      </tr>
      <tr>
        <td class="tdp"> 出差人： </td>
        <td class="tdp"> ${tawep.outPerson} </td>
        <td class="tdp"> 出差目的地： </td>
        <td class="tdp"> ${tawep.outSite} </td>
        <td class="tdp"> 外派总里程： </td>
        <td class="tdp"> ${tawep.outMileage} </td>
      </tr>
      <tr>
        <td class="tdp">拖车费：</td>
        <td class="tdp" >
          <c:forEach var="detail" items="${nList}">
            <c:if test='${detail.itemCode == "QT001" }'> ${detail.amount } </c:if>
          </c:forEach></td>
        <td class="tdp">外出费：</td>
        <td class="tdp">
          <c:forEach var="detail" items="${nList}">
            <c:if test='${detail.itemCode == "QT002" }'> ${detail.amount } </c:if>
          </c:forEach></td>
        <td class="tdp">旧件运费：</td>
        <td class="tdp" >
          <c:forEach var="detail" items="${nList}">
            <c:if test='${detail.itemCode == "QT003" }'> ${detail.amount } </c:if>
          </c:forEach></td>
      </tr>
      <tr>
        <td class="tdp">补扣款：</td>
        <td class="tdp">
          <c:forEach var="detail" items="${nList}">
            <c:if test='${detail.itemCode == "QT004" }'> ${detail.amount } </c:if>
          </c:forEach></td>
        <td class="tdp">其他：</td>
        <td class="tdp">
          <c:forEach var="detail" items="${nList}">
            <c:if test='${detail.itemCode == "QT005" }'> ${detail.amount } </c:if>
          </c:forEach></td>
        <td class="tdp">总外出费用：</td>
        <td class="tdp" > ${outAll } </td>
      </tr>
      <tr>
        <td class="tdp"> 总申报材料费： </td>
        <td class="tdp"> ${bean.partAmount } </td>
        <td class="tdp"> 总申报工时费： </td>
        <td class="tdp"> ${bean.labourAmount } </td>
        <td class="tdp"> 总申报费用： </td>
        <td class="tdp"> ${bean.grossCredit } </td>
      </tr>
      <tr>
        <td  class="tdp"> 故障现象描述： </td>
        <td colspan="5" class="tdp"> ${bean.miaoShu } </td>
      </tr>
      <tr>
        <td  class="tdp"> 故障原因： </td>
        <td colspan="5" class="tdp"> ${bean.reason } </td>
      </tr>
      <tr>
        <td  class="tdp"> 维修措施： </td>
        <td colspan="5" class="tdp"> ${bean.repairMethod } </td>
      </tr>
      <tr>
        <td  class="tdp"> 外出申请备注： </td>
        <td colspan="5" class="tdp"> ${bean.remarks } </td>
      </tr>
      <tr>
        <td  class="tdp" width="10%"> 授权说明： </td>
        <td colspan="5"  class="tdp"><c:if test="${bean.opinion != 'null' }"> ${bean.opinion } </c:if></td>
      </tr>
      <tr>
        <td class="tdp">授权人：</td>
        <td class="tdp" colspan="5">${bean.auditName }</td>
      </tr>
      <tr>
        <td colspan="6"><table class="tabp2"  align="center" id="waichuweix2" style="display: none">
            <tr>
              <td class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">新件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">数量 </td>
              <td class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">顾客问题</td>
              <td class="tdp" nowrap style="border-top-color: white;">旧件代码</td>
              <td class="tdp" nowrap style="border-top-color: white;">旧件名称</td>
              <td class="tdp" nowrap style="border-top-color: white;">配件费</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时数</td>
              <td class="tdp" nowrap style="border-top-color: white;">工时费</td>
            </tr>
            <% if(list!=null && list.size()>0){
				 for(int i=0;i<list.size();i++){%>
            <tr style='' >
              <td class="tdp"><%=i+1 %></td>
              <td class="tdp"><%=list.get(i).getPartCode() %></td>
              <td class="tdp"><%=list.get(i).getPartName() %></td>
              <td class="tdp" align="center"><%=list.get(i).getQuantity() %></td>
              <td class="tdp"><%=list.get(i).getLabourCode() %></td>
              <td class="tdp"><%=list.get(i).getLabourName() %></td>
              <td class="tdp"><%=list.get(i).getRemark()==null ? "": list.get(i).getRemark()%></td>
              <td class="tdp"><%=list.get(i).getDownCode() %></td>
              <td class="tdp"><%=list.get(i).getDownName() %></td>
              <td class="tdp"><%=list.get(i).getAmount() %></td>
              <td class="tdp"><%=list.get(i).getLabourHours() %></td>
              <td class="tdp"><%=list.get(i).getLabourAmount() %></td>
            </tr>
            <%	 }  } else{%>
            <tr style='' >
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
            </tr>
            <%  }%>
          </table></td>
                    <table class="tabp2"  align="center" id="fuliao9" style="display: none">
            <tr>
              <td  class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td width="40%" class="tdp" nowrap style="border-top-color: white;">金额</td>
            </tr>
            <c:forEach items="${claimNoAccessoryList}" var="claimNoAccessoryList" varStatus="i">
            <tr style='' >
              <td class="tdp">${i.index+1}</td>
              <td class="tdp">${claimNoAccessoryList.WORKHOUR_CODE}</td>
              <td class="tdp" >${claimNoAccessoryList.WORKHOUR_NAME}</td>
              <td class="tdp" align="right">${claimNoAccessoryList.PRICE}&nbsp;</td>
            </tr>
            </c:forEach>
            <tr>
            <td class="tdp"colspan="2">辅料关联主因件</td>
            <td class="tdp" colspan="2">
            ${claimNoAccessoryList[0].MAIN_PART_CODE}
            </td>
            </tr>
          </table>
      </tr>
    </table>
    <!--  ---------------   外出维修结束                            ---------------- -->
    <!--  ---------------   一般维修开始                            ---------------- -->
    <div  id="yibanweixiu" style="display: none">
	  <table class="tab_printsep1"  align="center" id="yibanweixiu1" style="display: none">
	    <tr>
	      <td > 故障现象描述： </td>
	      <td colspan="5"> ${bean.miaoShu } </td>
	    </tr>
	    <tr>
	      <td > 故障原因： </td>
	      <td colspan="5"> ${bean.reason } </td>
	    </tr>
	    <tr>
	      <td > 维修措施：</td>
	      <td colspan="5"> ${bean.repairMethod } </td>
	    </tr>
	    <tr>
	      <td > 备注：</td>
	      <td  colspan="5">${bean.remarks }</td>
	    </tr>
	    <tr>
	      <td width="10%"> 授权说明：</td>
	      <td  colspan="5"><c:if test="${bean.opinion != 'null' }"> ${bean.opinion }</c:if></td>
	    </tr>
	    <tr>
	      <td > 授权人：</td>
	      <td > ${bean.auditName } </td>
	      <td > 总申报工时数： </td>
	      <td > ${bean.labourHours } </td>
	      <td > 总申报材料费： </td>
	      <td > ${bean.partAmount } </td>
	    </tr>
	    <tr>
	      <td > 总申报费用： </td>
	      <td > ${bean.grossCredit } </td>
	      <td > 追加工时数： </td>
	      <td > ${bean.appendlabourNum } </td>
	      <td > 追加工时费： </td>
	      <td > ${bean.appendlabourAmount } </td>
	    </tr>
	  </table>
	  <table class="tabp2"  align="center" id="yibanweixiu2" style="display: none" >
            <tr>
              <td class="tdp" nowrap style="border-top-color:white;">行号</td>
              <td class="tdp" style="border-top-color:white;" >新件代码</td>
              <td class="tdp" style="border-top-color:white;" >新件名称</td>
              <td class="tdp" nowrap style="border-top-color:white;" >数量</td>
              <td class="tdp" style="border-top-color:white;" >工时代码</td>
              <td class="tdp" style="border-top-color:white;" >工时名称</td>
              <td class="tdp" nowrap style="border-top-color:white;">顾客问题</td>
              <td class="tdp" nowrap style="border-top-color:white;">旧件代码</td>
              <td class="tdp" nowrap style="border-top-color:white;">旧件名称</td>
              <td class="tdp" nowrap style="border-top-color:white;">配件费</td>
              <td class="tdp" nowrap style="border-top-color:white;">工时数</td>
              <td class="tdp" nowrap style="border-top-color:white;">工时费</td>
            </tr>
            <% if(list!=null && list.size()>0){
				 for(int i=0;i<list.size();i++){%>
            <tr>
              <td class="tdp"><%=i+1 %></td>
              <td class="tdp"><%=list.get(i).getPartCode() %></td>
              <td class="tdp"><%=list.get(i).getPartName() %></td>
              <td class="tdp" align="center"><%=list.get(i).getQuantity() %></td>
              <td class="tdp"><%=list.get(i).getLabourCode() %></td>
              <td class="tdp"><%=list.get(i).getLabourName() %></td>
              <td class="tdp"><%=list.get(i).getRemark()==null ? "":list.get(i).getRemark()%></td>
              <td class="tdp"><%=list.get(i).getDownCode() %></td>
              <td class="tdp"><%=list.get(i).getDownName() %></td>
              <td class="tdp"><%=list.get(i).getAmount() %></td>
              <td class="tdp"><%=list.get(i).getLabourHours() %></td>
              <td class="tdp"><%=list.get(i).getLabourAmount() %></td>
            </tr>
            <%	 }  } else{%>
            <tr>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
              <td class="tdp">&nbsp;</td>
            </tr>
            <%  }%>
          </table>
          <table class="tabp2"  align="center" id="fuliao1" style="display: none">
            <tr>
              <td  class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td width="40%" class="tdp" nowrap style="border-top-color: white;">金额</td>
            </tr>
            <c:forEach items="${claimNoAccessoryList}" var="claimNoAccessoryList" varStatus="i">
            <tr style='' >
              <td class="tdp">${i.index+1}</td>
              <td class="tdp">${claimNoAccessoryList.WORKHOUR_CODE}</td>
              <td class="tdp" >${claimNoAccessoryList.WORKHOUR_NAME}</td>
              <td class="tdp" align="right">${claimNoAccessoryList.PRICE}&nbsp;</td>
            </tr>
            </c:forEach>
            <tr>
            <td class="tdp"colspan="2">辅料关联主因件</td>
            <td class="tdp" colspan="2">
            ${claimNoAccessoryList[0].MAIN_PART_CODE}
            </td>
            </tr>
          </table>
	</div>
    <!--  ---------------   一般维修结束                            ---------------- -->
    <!--  ---------------   免费保养开始                            ---------------- -->
    <div id="mianfby" style="display: none">
      <table class="tabp2"  align="center">
        <!-- <th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				免费保养
			</th>-->
        <tr>
          <td class="tdp" width="10%"> 保养次数： </td>
          <td class="tdp" width="10%"> ${bean.freeTimes } </td>
          <td class="tdp" width="13%" > 总申报费用： </td>
          <td class="tdp" width="20%" > ${baoy } </td>
          <td class="tdp" width="10%"> 材料费： </td>
          <td class="tdp" width="15%"> ${part } </td>
            <td class="tdp" width="10%"> 工时费： </td>
          <td class="tdp" width="15%"> ${labour } </td>
        </tr>
        <tr>
         <td class="tdp" width="10%"> 授权人： </td>
          <td class="tdp" width="10%"> ${bean.auditName } </td>
          <td  class="tdp" width="10%"> 授权说明： </td>
          <td  class="tdp" colspan="5"><c:if test="${bean.opinion != 'null' }"> ${bean.opinion } </c:if></td>
           
        </tr>
      </table>
    </div>
    <!--  ---------------   免费保养结束                           ---------------- -->
    <!--  ---------------   活动开始                           ---------------- -->
    <div  id="fuwhuod"  style="display: none;">
      <table class="tabp2" align="center">
        <tr >
          <td class="tdp" colspan="6" align="center" style="border-bottom-color: white;">活动内容</td>
        </tr>
      </table>
      <table class="tabp2" align="center">
        <tr>
          <td class="tdp" >活动代码</td>
          <td class="tdp" colspan="3">${a.activityCode }</td>
        </tr>
        <tr>
          <td class="tdp">活动名称</td>
          <td class="tdp">${a.activityName }</td>
          <td class="tdp">活动费用</td>
          <td class="tdp">${bean.grossCredit-partAmount-labourAmount }</td>
        </tr>
        <tr>
          <td class="tdp">优惠工时费率</td>
          <td class="tdp">${labourDown }%</td>
          <td class="tdp">优惠材料费率</td>
          <td class="tdp">${partDown }%</td>
        </tr>
        <tr>
          <td class="tdp">活动开始时间</td>
          <td class="tdp"><fmt:formatDate value="${a.startdate }" pattern="yyyy-MM-dd HH:mm"/></td>
          <td class="tdp">活动结束时间</td>
          <td class="tdp"><fmt:formatDate value="${a.enddate }" pattern="yyyy-MM-dd HH:mm"/></td>
        </tr>
      </table>
      <table class="tabp2" align="center">
        <tr>
          <td class="tdp" nowrap style="border-top-color:white;">行号</td>
          <td class="tdp" width="80px" style="border-top-color:white;" >新件代码</td>
          <td class="tdp" width="80px" style="border-top-color:white;" >新件名称</td>
          <td class="tdp"  style="border-top-color:white;" >   数量</td>
          <td class="tdp" width="80px" style="border-top-color:white;" >工时代码</td>
          <td class="tdp" width="60px" style="border-top-color:white;" >工时名称</td>
          <td class="tdp" nowrap width="80px" style="border-top-color:white;">顾客问题</td>
          <td class="tdp" nowrap style="border-top-color:white;">旧件代码</td>
          <td class="tdp" colspan="1" nowrap style="border-top-color:white;">旧件名称</td>
          <td class="tdp" nowrap style="border-top-color:white;">配件费</td>
          <td class="tdp" nowrap style="border-top-color:white;">工时数</td>
          <td class="tdp" nowrap style="border-top-color:white;">工时费</td>
        </tr>
        <% if(list!=null && list.size()>0){
				 for(int i=0;i<list.size();i++){%>
        <tr style='' >
          <td class="tdp"><%=i+1 %></td>
          <td class="tdp"><%=list.get(i).getPartCode() %></td>
          <td class="tdp"><%=list.get(i).getPartName() %></td>
          <td class="tdp" align="center"><%=list.get(i).getQuantity() %></td>
          <td class="tdp"><%=list.get(i).getLabourCode() %></td>
          <td class="tdp"><%=list.get(i).getLabourName() %></td>
          <td class="tdp"><%=list.get(i).getRemark()==null ? "" :list.get(i).getRemark() %></td>
          <td class="tdp"><%=list.get(i).getDownCode() %></td>
          <td class="tdp"><%=list.get(i).getDownName() %></td>
          <td class="tdp"><%=list.get(i).getAmount() %></td>
          <td class="tdp"><%=list.get(i).getLabourHours() %></td>
          <td class="tdp"><%=list.get(i).getLabourAmount() %></td>
        </tr>
        <%	 }  } else{%>
        <tr style='' >
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp"colspan="1">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
          <td class="tdp">&nbsp;</td>
        </tr>
        <%  }%>
      </table>
      <table class="tabp2" align="center">
        <tr >
          <td class="tdp" width="100px" style="border-top-color: white;" >活动赠送费用</td>
          <td class="tdp" style="border-top-color: white;">${bean.grossCredit-partAmount-labourAmount }</td>
          <td class="tdp" width="80px" style="border-top-color: white;">材料费总计</td>
          <td class="tdp" style="border-top-color: white;">${bean.partAmount }</td>
          <td class="tdp" width="80px" style="border-top-color: white;">工时费总计</td>
          <td class="tdp" style="border-top-color: white;">${bean.labourAmount }</td>
          <td class="tdp" width="60px" style="border-top-color: white;">费用总计</td>
          <td class="tdp" style="border-top-color: white;">${bean.grossCredit }</td>
        </tr>
      </table>
  <!--  ---------------   活动结束                           ---------------- -->
    </div>
      <!--  ---------------   pdi开始                            ---------------- -->
    <div id="pdi" style="display: none">
      <table class="tabp2"  align="center">
        <tr>
          <td class="tdp" width="10%"> 申报费用： </td>
          <td class="tdp" width="10%"> ${bean.grossCredit } </td>
         <td class="tdp" width="10%"> 授权人： </td>
          <td class="tdp" width="10%"> ${bean.auditName } </td>
          <td  class="tdp"width="10%" > 授权说明： </td>
          <td  class="tdp" colspan="3"><c:if test="${bean.opinion != 'null' }"> ${bean.opinion } &nbsp;</c:if></td>
           
        </tr>
      </table>
    </div>
    <!--  ---------------  pdi结束                           ---------------- -->
  </table>
  <center>
    <div style="width: 800px;">
      <div style="height: 5px;">&nbsp;</div>
      <tfoot style="display: table-footer-group;">
      <table width="100%">
        <tr>
          <td width="33%" class="tdpvoid" align="left">服务站盖章:</td>
          <td width="33%" class="tdpvoid" align="center">鉴定员: </td>
          <td width="15%" class="tdpvoid" align="center">&nbsp;</td>
          <td width="19%" class="tdpvoid" colspan="4" align="left">客户签字:</td>
        </tr>
      </table>
      </tfoot>
    </div>
  </center>
  <table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >
    <tr>
      <td width="100%" height="25" colspan="3"><div id="kpr" align="center">
          <input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>
          <input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />
          <input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>
          <!--  <select  onclick="printQieh(this.value)" >
				<option value="10661001" >正常维修</option>
				<option value="10661002">强保定检</option>
				<option value="10661007">售前维修</option>
				<option value="10661006">活动</option>
				<option value="10661009">外派服务</option>
				</select>   
				-->
        </div></td>
    </tr>
  </table>
</form>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	} 
	
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
	function printQieh(type){
// 		MyAlert(type);
        if(type=='10661001' || type=='10661010'|| type=='10661012'){
        	
        	 document.getElementById("yibanweixiu").style.display="";
        	 if('${length}'!="0"){
		        document.getElementById("fuliao1").style.display="";
        	 }
        	 document.getElementById("yibanweixiu1").style.display="";
        	 document.getElementById("yibanweixiu2").style.display="";
        	 document.getElementById("gaizhang").style.display="";
        	 
         	 document.getElementById("mianfby").style.display="none";
         	 document.getElementById("shouqianweix").style.display="none";
        	 document.getElementById("shouqianweix1").style.display="none";
        	 document.getElementById("shouqianweix2").style.display="none";
           	 document.getElementById("waichuweix").style.display="none";
        	 document.getElementById("waichuweix1").style.display="none";
        	 document.getElementById("waichuweix2").style.display="none";
        	 document.getElementById("waichuweix3").style.display="none";
        	 document.getElementById("fuwhuod").style.display="none";
        	 document.getElementById("pdi").style.display="none";

        }else if(type=='10661002'){

        	 document.getElementById("mianfby").style.display="";
        	 
        	 document.getElementById("yibanweixiu").style.display="none";
        	 document.getElementById("yibanweixiu1").style.display="none";
        	 document.getElementById("yibanweixiu2").style.display="none";
        	 document.getElementById("gaizhang").style.display="";

        	 
         	 document.getElementById("shouqianweix").style.display="none";
        	 document.getElementById("shouqianweix1").style.display="none";
        	 document.getElementById("shouqianweix2").style.display="none";
           	 document.getElementById("waichuweix").style.display="none";
        	 document.getElementById("waichuweix1").style.display="none";
        	 document.getElementById("waichuweix2").style.display="none";
        	 document.getElementById("waichuweix3").style.display="none";
        	 document.getElementById("fuwhuod").style.display="none"; 
        	 document.getElementById("pdi").style.display="none";

        }else if(type=='10661007'){

        	 document.getElementById("shouqianweix").style.display="";
        	 document.getElementById("shouqianweix1").style.display="";
        	 document.getElementById("shouqianweix2").style.display="";
        	 if('${length}'!=0){
	        	 document.getElementById("fuliao7").style.display="";
        	 }
        	 document.getElementById("gaizhang").style.display="";

        	 
        	 document.getElementById("yibanweixiu").style.display="none";
        	 document.getElementById("yibanweixiu1").style.display="none";
        	 document.getElementById("yibanweixiu2").style.display="none";
        	 document.getElementById("mianfby").style.display="none";
           	 document.getElementById("waichuweix").style.display="none";
        	 document.getElementById("waichuweix1").style.display="none";
        	 document.getElementById("waichuweix2").style.display="none";
        	 document.getElementById("waichuweix3").style.display="none";
        	 document.getElementById("fuwhuod").style.display="none";
        	 document.getElementById("pdi").style.display="none";


        }else if(type=='10661009'){
        	 document.getElementById("waichuweix").style.display="";
        	 document.getElementById("waichuweix1").style.display="";
        	 document.getElementById("waichuweix2").style.display="";
        	 document.getElementById("waichuweix3").style.display="";
        	 document.getElementById("fuliao9").style.display="";
        	 document.getElementById("gaizhang").style.display="";

        	 document.getElementById("shouqianweix").style.display="none";
        	 document.getElementById("shouqianweix1").style.display="none";
        	 document.getElementById("shouqianweix2").style.display="none";
        	 
        	 document.getElementById("yibanweixiu").style.display="none";
        	 document.getElementById("yibanweixiu1").style.display="none";
        	 document.getElementById("yibanweixiu2").style.display="none";
        	 document.getElementById("mianfby").style.display="none";
        	 document.getElementById("fuwhuod").style.display="none";
        	 document.getElementById("pdi").style.display="none";

        }else if(type=='10661006'){
        	 document.getElementById("fuwhuod").style.display="";

        	
        	 document.getElementById("mianfby").style.display="none";
        	 
        	 document.getElementById("yibanweixiu").style.display="none";
        	 document.getElementById("yibanweixiu1").style.display="none";
        	 document.getElementById("yibanweixiu2").style.display="none";
        	 
         	 document.getElementById("shouqianweix").style.display="none";
        	 document.getElementById("shouqianweix1").style.display="none";
        	 document.getElementById("shouqianweix2").style.display="none";
           	 document.getElementById("waichuweix").style.display="none";
        	 document.getElementById("waichuweix1").style.display="none";
        	 document.getElementById("waichuweix2").style.display="none";
        	 document.getElementById("waichuweix3").style.display="none";
        	 document.getElementById("gaizhang").style.display="none";
        	 document.getElementById("pdi").style.display="none";
        	
        }else if(type=='10661011'){
        	document.getElementById("pdi").style.display="";
            
       	 document.getElementById("fuwhuod").style.display="none";

     	
    	 document.getElementById("mianfby").style.display="none";
    	 
    	 document.getElementById("yibanweixiu").style.display="none";
    	 document.getElementById("yibanweixiu1").style.display="none";
    	 document.getElementById("yibanweixiu2").style.display="none";
    	 
     	 document.getElementById("shouqianweix").style.display="none";
    	 document.getElementById("shouqianweix1").style.display="none";
    	 document.getElementById("shouqianweix2").style.display="none";
       	 document.getElementById("waichuweix").style.display="none";
    	 document.getElementById("waichuweix1").style.display="none";
    	 document.getElementById("waichuweix2").style.display="none";
    	 document.getElementById("waichuweix3").style.display="none";
    	 document.getElementById("gaizhang").style.display="none";
    	
    }
        
	}
</script> 
</div>
</body>

</html>

