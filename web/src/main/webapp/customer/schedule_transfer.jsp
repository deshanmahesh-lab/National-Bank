<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    request.setAttribute("now", sdf.format(new Date()));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Transfer - National Bank</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
        .navbar { background-color: #005A9E; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .navbar a { color: white; text-decoration: none; padding: 0.5rem 1rem; }
        .navbar a:hover { background-color: #004175; border-radius: 4px; }
        .container { padding: 2rem; max-width: 800px; margin: auto; }
        .form-container { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 1.5rem; margin-top: 0; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; color: #555; }
        input[type="text"], input[type="number"], input[type="datetime-local"], select { width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        .btn { display: inline-block; padding: 0.75rem 1.5rem; border: none; border-radius: 4px; background-color: #005A9E; color: white; font-size: 1rem; cursor: pointer; text-decoration: none; text-align: center; }
        .btn:hover { background-color: #004175; }
        .message { padding: 1rem; margin-bottom: 1rem; border-radius: 4px; text-align: center; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
<div class="navbar">
    <h1>National Bank</h1>
    <div>
        <a href="${pageContext.request.contextPath}/customer/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
    </div>
</div>

<div class="container">
    <div class="form-container">
        <h2>Schedule a Future Transfer</h2>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/customer/schedule-transfer" method="post">
            <div class="form-group">
                <label for="fromAccountId">From Account</label>
                <select id="fromAccountId" name="fromAccountId" required>
                    <c:forEach items="${userAccounts}" var="account">
                        <option value="${account.id}">
                                ${account.accountNumber} - ${account.accountType} (Balance: <fmt:formatNumber value="${account.balance}" type="currency" currencySymbol="LKR "/>)
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="toAccountNumber">To Account Number</label>
                <input type="text" id="toAccountNumber" name="toAccountNumber" required>
            </div>
            <div class="form-group">
                <label for="amount">Amount (LKR)</label>
                <input type="number" id="amount" name="amount" step="0.01" min="1" required>
            </div>
            <div class="form-group">
                <label for="scheduledDateTime">Date and Time</label>
                <input type="datetime-local" id="scheduledDateTime" name="scheduledDateTime" min="${now}" required>
            </div>
            <button type="submit" class="btn">Schedule Transfer</button>
        </form>
    </div>
</div>
</body>
</html>