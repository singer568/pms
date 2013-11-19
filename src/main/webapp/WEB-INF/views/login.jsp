<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>登录页</title>
	<script>
		var logon = ${not empty user};
		if (logon) {
			location.href = '${ctx}/main/index';
		}
	</script>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-jquery-ui-theme.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script type="text/javascript">
	$(function() {
		$('button').button({
			icons: {
				primary: 'ui-icon-key'
			}
		});
	});
	</script>
</head>

<body style="margin-top: 10em;">
	<center>
	<c:if test="${not empty param.error}">
		<h2 id="error" class="alert alert-error">用户名或密码错误！！！</h2>
	</c:if>
	<c:if test="${not empty param.timeout}">
		<h2 id="error" class="alert alert-error">未登陆或超时！！！</h2>
	</c:if>
	<div style="width: 350px">
		<h2>项目运维管理系统</h2>
		<hr />
		<form action="${ctx }/user/logon" method="get">
			<table>
				<tr>
					<td width="80">用户名：</td>
					<td><input id="username" name="username" /></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input id="password" name="password" type="password" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<button type="submit">登录系统</button>
					</td>
				</tr>
			</table>
		</form>
		
	</div>
	</center>
</body>
</html>
