//加载布局
//布局加载完成加载手风琴
//手风琴加载完成加载功能树
//功能树加载完成加载tabs
//为了迎合老功能,采用老的inIframe名称
//而且根据tabs的变化对应的iframe名称也跟着变化
//当前tba iframe id=inIframe
//其他 id= fun_id	
$(function() {
	 //布局
    $("#layout1").ligerLayout({ leftWidth: 190, height: '100%', topHeight:40 });
    $("#framecenter").ligerTab({ height: 500,onAfterSelectTabItem :function(targettabid){
    	var old = $("#inIframe");
    	if( old.attr("name") != targettabid ){
    		old.attr( "id",old.attr("name") );
    	}
    	$(".l-tab-content-item[tabid='"+targettabid +"'] iframe").attr("id","inIframe");
    	//关闭时将原页面清空  20150402 ranke
    	/*
    	$(".l-tab-content-item[tabid='"+targettabid +"']").attr("tabid","inIframe").attr("tabid_old",targettabid);
    	*/
    },
    onBeforeSelectTabItem :function( targettabid ) {
    	var tab =  $(".l-tab-content-item[tabid='"+targettabid +"']");
    	var inIframe = $(".l-tab-content-item[tabid='inIframe']");
    	if(  0 == tab.length  ){
    		if( targettabid == inIframe.attr( "tabid_old" ) ){
    			inIframe.attr("tabid", targettabid );
    		}
    		
    	}else{
    		if( 0 != inIframe.length ){
    			inIframe.attr("tabid", inIframe.attr( "tabid_old" ) );
    		}
    	}

	}
    
    });
    initAccordion();
    
});

function initAccordion() {
	var url = global_Path + "/common/MenuShow/getUserSysFun.json";
	$.ajax({
		url:url,
		type:"post",
//		dataType:"json",
		success:function(res){
			res = eval("("+res+")");
			res= res.sysfun;
			var rows = [];
			
			for( var i=0;i<res.length;i++ ){
				var row = res[i];
				var red = {};
				red.id = row.funcId;
				red.pid = row.parFuncId;
				red.text = row.funcName;
				if( row.funcCode && row.funcType&& 1002!=row.funcType && 1001!=row.funcType){
					red.url = global_Path + row.funcCode + ".do";
				}
				rows.push(red);
			}
			
//			$("#accordion1").ligerGetAccordionManager();
//			    //将ID、ParentID这种数据格式转换为树格式
			  $("#leftTree").ligerTree({
			                 data:rows, 
			                 idFieldName :'id',
			                 parentIDFieldName :'pid',
			                 checkbox:false,
			                 onclick:function(node){
			                	 if( !node.data.url ){ return ;}
			                	 var tab = $("#framecenter").ligerGetTabManager();
			                	 tab.addTabItem({ tabid : node.data.id,text: node.data.text, url: node.data.url });
			                 }
			                }
			                 );
			
		},
		error:function(){
			alert(1231)
		}
	});
	
}