$(function() {
	$('#portlet-container').portlet({
		sortable: true,
		columns: [{
			width: '50%',
			portlets: [{
				title: '待办任务',
				content: {
					style: {
						maxHeight: 300
					}
				}
			}, {
				title: '会签（多实例）说明',
				content: {
					type: 'text',
					text: function() {
						return $('#multiInstance').html();
					}
				}
			}]
		},{
			width: '50%',
			portlets: [{
				title: '项目说明',
				content: {
					type: 'text',
					text: function() {
						return $('.project-info').html();
					}
				}
			}, {
				title: '资源链接',
				content: {
					type: 'text',
					text: function() {
						return $('.links').html();
					}
				}
			}]
		}]
	});
});