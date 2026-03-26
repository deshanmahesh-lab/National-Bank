<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Transaction - National Bank</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .container { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }
        h2 { color: #333; margin-bottom: 1rem; }
        p { color: #555; margin-bottom: 1.5rem; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; color: #555; font-weight: bold; }
        input[type="text"] { width: 60%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; text-align: center; font-size: 1.2rem; letter-spacing: 5px; }
        .btn { width: 100%; padding: 0.75rem; border: none; border-radius: 4px; background-color: #28a745; color: white; font-size: 1rem; cursor: pointer; }
        .btn:hover { background-color: #218838; }
        .message { padding: 1rem; margin-bottom: 1rem; border-radius: 4px; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .info { background-color: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }
    </style>
</head>
<body>
<div class="container">
    <h2>Verify Your Transaction</h2>
    <p>A One-Time Password (OTP) has been sent to your registered email address.</p>

    <div class="message info">Please enter the 6-digit code below to confirm.</div>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/customer/verify-otp" method="post">
        <div class="form-group">
            <label for="otp">Enter OTP</label>
            <input type="text" id="otp" name="otp" maxlength="6"  required>
        </div>
        <button type="submit" class="btn">Confirm Transfer</button>
    </form>
</div>
</body>
</html>