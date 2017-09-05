	function setScrollTop(){
		var inIframe=parent.document.getElementById("inIframe");
		if(inIframe != null )
		var top=inIframe.contentWindow.document.documentElement.scrollTop;
		$("top").value=top;
	}
	//当客户类型发生变化时执行的方法
	function loadCtmType(obj){
		$("ctmType").value=obj.getAttribute("TREE_ID");
	}
	//当证件类型选择发生改变的时候执行的方法
	function loadPaperType(obj){
		$("paperType").value=obj.getAttribute("TREE_ID");
	}
	function loadCardType(obj){
		$("cardType").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyBudget2(obj){
		$("buy_budget").value=obj.getAttribute("TREE_ID");
	}
	function loadCarFrum(obj){
		$("carFrum").value=obj.getAttribute("TREE_ID");
	}
	function loadDefeatType(obj){
		$("defeatType").value=obj.getAttribute("TREE_ID");
	}
	function loadDefeatReason(obj){
		$("defeatReason").value=obj.getAttribute("TREE_ID");
	}
	function loadDefeatReason2(obj){
		$("defeatReasonB").value=obj.getAttribute("TREE_ID");
	}
	function loadFitTime(obj){
		$("fitTime").value=obj.getAttribute("TREE_ID");
	}
	function loadFitArea(obj){
		$("fitArea").value=obj.getAttribute("TREE_ID");
	}
	function loadConcern(obj){
		$("concern").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyUse(obj){
		$("buyUse").value=obj.getAttribute("TREE_ID");
	}
	function loadbColorLike(obj){
		$("colorLike").value=obj.getAttribute("TREE_ID");
	}
	
	function loadIfMarry(obj){
		$("ifMarry").value=obj.getAttribute("TREE_ID");
	}
	function loadPolitical(obj){
		$("political").value=obj.getAttribute("TREE_ID");
	}
	function loadGender(obj){
		$("gender").value=obj.getAttribute("TREE_ID");
	}
	function loadIncome(obj){
		$("income").value=obj.getAttribute("TREE_ID");
	}
		function loadIndustry(obj){
		$("industry").value=obj.getAttribute("TREE_ID");
	}
		function loadJob(obj){
		$("job").value=obj.getAttribute("TREE_ID");
	}
	function loadInterestThree(obj){
		$("interestThree").value=obj.getAttribute("TREE_ID");
	}
	function loadInterestTwo(obj){
		$("interestTwo").value=obj.getAttribute("TREE_ID");
	}
	function loadInterestOne(obj){
		$("interestOne").value=obj.getAttribute("TREE_ID");
	}
	function loadDegree(obj){
		$("degree").value=obj.getAttribute("TREE_ID");
	}
	
	function loadCtmProp(obj){
		$("ctmProp").value=obj.getAttribute("TREE_ID");
	}
	function loadCtmType(obj){
		$("ctmType").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyWay(obj){
		$("buyWay").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyType(obj){
		$("buyType").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyType2(obj){
		$("buy_type").value=obj.getAttribute("TREE_ID");
	}
	function loadCtmRank(obj){
		$("ctmRank").value=obj.getAttribute("TREE_ID");
	}
	//function loadComeReason(obj){
		//alert(12);
		//$("comeReason").value=obj.getAttribute("TREE_ID");
	//}
	function loadJcway(obj){
		$("jcway").value=obj.getAttribute("TREE_ID");
	}
	function loadIfDriving(obj){
		$("ifDriving").value=obj.getAttribute("TREE_ID");
		doDrivingChange();
	}
	function loadIfDriving2(obj){
		$("test_driving").value=obj.getAttribute("TREE_ID");
	}
	function loadFollowTime(obj){
		$("follow_time").value=obj.getAttribute("TREE_ID");
	}
	
	function loadLeadsOrigin(obj){
		$("leads_origin").value=obj.getAttribute("TREE_ID");
	}
	function loadLeadsStatus(obj){
		$("leads_status").value=obj.getAttribute("TREE_ID");
	}
	function loadAllotStatus(obj){
		$("allot_status").value=obj.getAttribute("TREE_ID");
	}
	function loadProductSale(obj){
		$("new_product_sale").value=obj.getAttribute("TREE_ID");
	}
	function loadTimeOut(obj){
		$("time_out").value=obj.getAttribute("TREE_ID");
	}
	function loadOwnerPaperType(obj){
		$("owner_paper_type").value=obj.getAttribute("TREE_ID");
	}
	function loadOrderStatus(obj){
		$("order_status").value=obj.getAttribute("TREE_ID");
	}
	function loadCustomerStatus(obj){
		$("customer_status").value=obj.getAttribute("TREE_ID");
	}
	function loadIntentColor(obj){
		$("intentColor").value=obj.getAttribute("TREE_ID");
	}
	function loadSalesProgress(obj){
		$("salesProgress").value=obj.getAttribute("TREE_ID");
	}
	function loadBuyBudget(obj){
		$("buyBudget").value=obj.getAttribute("TREE_ID");
	}
	function loadComeReason(obj){
		$("comeReason").value=obj.getAttribute("TREE_ID");
	}
	function loadComeReason2(obj){
		$("come_reason").value=obj.getAttribute("TREE_ID");
	}
	function loadCollectFashion(obj){
		$("collect_fashion").value=obj.getAttribute("TREE_ID");
	}
	function loadCollectFashion1(obj){
		$("collect_fashion1").value=obj.getAttribute("TREE_ID");
	}
	function loadCustomerType(obj){
		$("customer_type").value=obj.getAttribute("TREE_ID");
	}
	function loadIntentType(obj){
		$("intent_type").value=obj.getAttribute("TREE_ID");
	}
	function loadIntentType2(obj){
		$("intent_type2").value=obj.getAttribute("TREE_ID");
	}
	function loadIntentType3(obj){
		$("intent_type3").value=obj.getAttribute("TREE_ID");
	}
	function loadFollowType(obj){
		$("follow_type").value=obj.getAttribute("TREE_ID");
	}
	function loadInviteType(obj){
		$("invite_type").value=obj.getAttribute("TREE_ID");
	}
	function loadInviteWay(obj){
		$("invite_way").value=obj.getAttribute("TREE_ID");
	}
	function loadInviteType2(obj){
		$("invite_type_new").value=obj.getAttribute("TREE_ID");
	}
	function loadSalesProgress2(obj){
		$("sales_progress").value=obj.getAttribute("TREE_ID");
	}
	function loadDealType(obj){
		$("deal_type").value=obj.getAttribute("TREE_ID");
	}
	
	//验证是否可以做订单false不可以   true可以做订单
	function judgeIfAbleOder(tel){
		var url=globalContextPath+"/crm/taskmanage/TaskManage/judgeIfAbleOrder.json?telephone="+tel;
		var flag=false;
		makeSameCall(url,orderBack,"fm");
		function orderBack(json){
			if(json.flag=="1"){
				flag=true;
			}
		}
		return flag;
	}
	
	//验证是否可以做订单false不可以   true可以做订单（当日是否有到店）
	function judgeIfAbleOderDate(tel){
		var url=globalContextPath+"/crm/taskmanage/TaskManage/judgeIfAbleOrderDate.json?telephone="+tel;
		var flag=false;
		makeSameCall(url,orderBack,"fm");
		function orderBack(json){
			if(json.flag=="1"){
				flag=true;
			}
		}
		return flag;
	}
	//验证是否可以做交车   true可以做交车（false表示不可以做交车）
	function judgeIfAbleDelvy(orderDetailId){
		var url=globalContextPath+"/crm/taskmanage/TaskManage/judgeIfAbleDelvy.json?orderDetailId="+orderDetailId;
		var flag=false;
		makeSameCall(url,orderBack,"fm");
		function orderBack(json){
			if(json.flag=="1"){
				flag=true;
			}
		}
		return flag;
	}
	