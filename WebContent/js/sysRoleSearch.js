// JavaScript Document


var fields = [
			   {name: 'company',type: 'int'},
			   {name: 'price', type: 'string'},
			   {name: 'change', type: 'string'},
			   {name: 'pctChange', type: 'string'},
			   {name: 'lastChange', type: 'string'}
        	];

var columns =  [
				{id:'company',header: "序号", sortable: true, dataIndex: 'company'},
				{header: "角色代码",sortable: true, dataIndex: 'price'},
				{header: "角色名称", sortable: true, dataIndex: 'change'},
				{header: "状态", sortable: true, dataIndex: 'pctChange'},
				{header: "操作", sortable: true,dataIndex: 'lastChange'}
        		];

function createGrid(fields,columns){

    // NOTE: This is an example showing simple state management. During development,
    // it is generally best to disable state management as dynamically-generated ids
    // can change across page loads, leading to unpredictable results.  The developer
    // should ensure that stable state ids are set for stateful components in real apps.    
    //Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

   // create the data store
    var store = new Ext.data.ArrayStore({
        fields: fields
    });
    store.loadData(myData);

    // create the Grid
    var grid = new Ext.grid.GridPanel({
        store: store,
        columns:columns,
        stripeRows: true,
        autoExpandColumn: 'company',
        height:275,
        width:600,
        title:'<img class="nav" src="grid/subNav.gif" />&nbsp;角色列表'
    });
    grid.render('grid-example');
		
		Ext.fly(gridId).clip();
	}
