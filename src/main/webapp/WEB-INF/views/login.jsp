<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/include-jquery-ui-theme.jsp"%>
		<%@ include file="/common/include-base-styles.jsp"%>

		<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
		<script	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
		<link href="${ctx }/css/login.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript">
			$(function() {
				$('button').button( {
					icons : {
						primary : 'ui-icon-key'
					}
				});
			});
		</script>
	</head>
	<body >
		<div class="loginpanel">
			<div class="formpanel">
				<form action="${ctx }/user/logon" method="post">
					<c:if test="${not empty param.error}">
						<div class="field">
							<errmsg>
							用户名或密码错误！！！
							</errmsg>
						</div>
					</c:if>
					<c:if test="${not empty param.timeout}">
						<div class="field">
							<errmsg>
							未登陆或超时！！！
							</errmsg>
						</div>
					</c:if>
					<div class="field">
						<label>
							账 户：
						</label>
						<input id="username" name="username" type="text" />
						<br />
					</div>
					<div class="field">
						<label>
							密 码：
						</label>
						<input id="password" name="password" type="password" />
						<br />
					</div>

					<div class="btn">
					<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="submit">
							登录系统
						</button>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>
