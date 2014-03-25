<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>网址列表</title>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/include-base-styles.jsp"%>
		<%@ include file="/common/include-jquery-ui-theme.jsp"%>
		<link
			href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css"
			type="text/css" rel="stylesheet" />
		<link href="${ctx }/js/common/plugins/qtip/jquery.qtip.min.css"
			type="text/css" rel="stylesheet" />
		<%@ include file="/common/include-custom-styles.jsp"%>

		<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
			type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js"
			type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js"
			type="text/javascript">
</script>
		<script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js"
			type="text/javascript">
</script>
		<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js"
			type="text/javascript">
</script>
		<script src="${ctx }/js/module/activiti/workflow.js"
			type="text/javascript">
</script>

		<style type="text/css">
body {
	TEXT-ALIGN: center;
}

#querydiv {
	MARGIN-RIGHT: auto;
	MARGIN-LEFT: auto;
}
</style>
	</head>
	<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/qktags"%>

	<body>
		<c:if test="${not empty message}">
			<div class="ui-widget">
				<div class="ui-state-highlight ui-corner-all"
					style="margin-top: 20px; padding: 0 .7em;">
					<p>
						<span class="ui-icon ui-icon-info"
							style="float: left; margin-right: .3em;"></span>
						<strong>提示：</strong>${message}
					</p>
				</div>
			</div>
		</c:if>

		<div id="querydiv" align="center" style="width: 100%; margin: 0px">
			<form name="queryForm" id="queryForm" action="#">
				<table style="width: 80%; margin-bottom: 0px" align="center">
					<tr>
						<td style="background-color: white">
							<label class="ui-button-text">
								编码：
							</label>
							<input type="text" name="search_LIKE_code" class="input-medium"
								value="${param.search_LIKE_code}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								名称：
							</label>
							<input type="text" name="search_LIKE_name" class="input-medium"
								value="${param.search_LIKE_name}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								省&nbsp;&nbsp;&nbsp;	份：
							</label>
							<input type="text" name="search_LIKE_province"
								class="input-medium" value="${param.search_LIKE_province}">
						</td>
						<td>
							<label class="ui-button-text">
								是否有效(1/0)：
							</label>
							<input type="text" name="search_EQ_valid"
								class="input-medium" value="${param.search_EQ_valid}">
						</td>
						<td>
							&nbsp;
						</td>
						<tr>
							<td style="background-color: white">
								<label class="ui-button-text">
									部门：
								</label>
								<input type="text" name="search_LIKE_department"
									class="input-medium" value="${param.search_LIKE_department}">
							</td>
							<td style="background-color: white">
								<label class="ui-button-text">
									板块：
								</label>
								<input type="text" name="search_LIKE_module"
									class="input-medium" value="${param.search_LIKE_module}">
							</td>
							<td style="background-color: white">
								<label class="ui-button-text">
									子板块：
								</label>
								<input type="text" name="search_LIKE_submodule"
									class="input-medium" value="${param.search_LIKE_submodule}">
									
									
							</td>
							<td style="background-color: white">
							<label class="ui-button-text">
								是否筛选(1/0)：
							</label>
							<input type="text" name="search_EQ_filter"
								class="input-medium" value="${param.search_EQ_filter}">
						</td>
						<td style="background-color: white">
								<a class="startup-process" href="javascript:querySubmit()">检索</a>
								<a
									class="startup-process ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary"
									href="${ctx}/spider/url/create">新增</a>
							
							</td>
							
							
						</tr>
						</form>
						</div>

						<table id="contentTable"
							class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>
										编码
									</th>
									<th>
										名称
									</th>
									<th>
										省份
									</th>
<%--									<th>--%>
<%--										省会--%>
<%--									</th>--%>
<%--									<th>--%>
<%--										中东西部--%>
<%--									</th>--%>
									<th>
										部门
									</th>
									<th>
										板块
									</th>
									<th>
										子版块
									</th>
									<th>
										网址
									</th>
									
<%--									<th>--%>
<%--										级别--%>
<%--									</th>--%>
<%--									<th>--%>
<%--										下一页--%>
<%--									</th>--%>
									<th>
										编码
									</th>
									<th>
										是否有效
									</th>
									<th>
										是否筛选
									</th>
									<th>
										上次抓取时间
									</th>
									<th>
										操作
									</th>
								</tr>
							</thead>
							<tbody>

								<c:forEach items="${urls.content}" var="url">
									<tr>
										<td>
											<a href="${ctx}/spider/url/update/${url.id}">${url.code}</a>
										</td>
										<td>
											${url.name}
										</td>
										<td>
											${url.province}
										</td>
<%--										<td>--%>
<%--											<c:choose>--%>
<%--												<c:when test="${url.isCapital=='1'}">是</c:when>--%>
<%--												<c:otherwise>否</c:otherwise>--%>
<%--											</c:choose>	--%>
<%--										</td>--%>
<%--										<td>--%>
<%--											<c:choose>--%>
<%--												<c:when test="${url.area=='MIDDLE'}">中部</c:when>--%>
<%--												<c:when test="${url.area=='EAST'}">东部</c:when>--%>
<%--												<c:when test="${url.area=='WEST'}">西部</c:when>--%>
<%--												<c:otherwise>其他</c:otherwise>--%>
<%--											</c:choose>--%>
<%--											--%>
<%--										</td>--%>
										<td>
											${url.department}
										</td>
										<td>
											${url.module}
										</td>
										<td>
											${url.submodule}
										</td>
										<td>
											${url.url}
										</td>
<%--										<td>--%>
<%--											${url.level.code}-${url.level.name}--%>
<%--										</td>--%>
<%--										<td>--%>
<%--											${url.catchNextPage}--%>
<%--										</td>--%>
										<td>
											${url.charset}
										</td>
										<td>
											<c:choose>
												<c:when test="${url.valid == null || url.valid=='1' }">是</c:when>
												<c:otherwise><font color="red" >否</font></c:otherwise>
											</c:choose>	
										</td>
										<td>
										<c:choose>
												<c:when test="${url.filter == null || url.filter=='1' }">是</c:when>
												<c:otherwise>否</c:otherwise>
											</c:choose>
										</td>
										<td>
											${url.catchTime}
										</td>
										<td>
											<a href="${ctx}/spider/url/delete/${url.id}">删除</a>
											&nbsp;&nbsp;
											<a href="${ctx}/spider/url/copy/${url.id}">复制</a>
											&nbsp;&nbsp;
											<a href="javascript:validate('${url.id}')">验证</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<tags:pagination page="${urls}" paginationSize="50" />
	</body>
	<script type="text/javascript">
function validate(id) {
	$.ajax( {
		type : "GET",
		url : "${ctx}/spider/url/validate",
		dataType : 'text',
		data : "id=" + id,
		success : function(data) {
			alert(data);
		}
	});
}

$(function() {
	$('.startup-process').button( {
		icons : {
			primary : 'ui-icon-play'
		}
	});
});

function querySubmit() {
	$('#queryForm').submit();
}
</script>
</html>
