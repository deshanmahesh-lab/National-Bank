<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>403 - Access Denied</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; text-align: center; padding: 50px; }
        .container { max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h1 { font-size: 72px; color: #dc3545; margin: 0; }
        h2 { color: #333; }
        p { font-size: 18px; }
        a { color: #005A9E; text-decoration: none; font-weight: bold; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h1>403</h1>
    <h2>Access Denied / Forbidden</h2>
    <p>You do not have permission to view this page. Your access level does not grant you the rights to this resource.</p>
    <p><a href="${pageContext.request.contextPath}/customer/dashboard">Return to Dashboard</a></p>
</div>
</body>
</html>