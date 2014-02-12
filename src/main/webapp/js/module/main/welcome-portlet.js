$(function() {
	$('#portlet-container').portlet({
		sortable: true,
		columns: [{
			width: '50%',
			portlets: [{
				title: '最新十条',
				content: {
					style: {
						maxHeight: 300
					},
					type:'ajax',
					dataType: 'json',
					url: ctx + '/policy/subjects/query',
					formatter:function(o,poi,data){
						var ct = "<ol>";
						$.each(data, function() {
							ct += "<li><a href='#' onclick=\"window.open('"+ this.subjUrl + "')\">" + this.subject + "</a></li>";
						});
						return ct + "</ol>";
					}
				}
			}, {
				title: '备用1',
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
				title: '备用2',
				content: {
					type: 'text',
					text: function() {
						return $('.project-info').html();
					}
				}
			}, {
				title: '备用3',
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