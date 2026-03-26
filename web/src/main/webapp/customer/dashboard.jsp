<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - National Bank</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
        .navbar { background-color: #005A9E; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .navbar a { color: white; text-decoration: none; padding: 0.5rem 1rem; }
        .navbar a:hover { background-color: #004175; border-radius: 4px; }
        .container { padding: 2rem; max-width: 1200px; margin: auto; }
        .welcome-msg { margin-bottom: 2rem; }
        .section { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); margin-bottom: 2rem; }
        h2 { color: #333; border-bottom: 2px solid #f4f4f4; padding-bottom: 0.5rem; margin-top: 0; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { text-align: left; padding: 0.75rem; border-bottom: 1px solid #ddd; }
        th { background-color: #f9f9f9; }
        .actions a { text-decoration: none; color: #005A9E; margin-right: 1rem; }
        .actions a:hover { text-decoration: underline; }
        .message { padding: 1rem; margin-bottom: 1rem; border-radius: 4px; text-align: center; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    </style>
</head>
<body>
<div class="navbar">
    <h1>National Bank</h1>
    <div>
        <a href="${pageContext.request.contextPath}/customer/update-profile">Update Profile</a>
        <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
    </div>
</div>

<div class="container">
    <div class="welcome-msg">
        <h2>Welcome, <c:out value="${customer.fullName}"/>!</h2>
    </div>

    <c:if test="${not empty param.transfer && param.transfer == 'success'}">
        <div class="message success">Fund transfer was successful.</div>
    </c:if>
    <c:if test="${not empty param.schedule && param.schedule == 'success'}">
        <div class="message success">Fund transfer has been scheduled successfully.</div>
    </c:if>
    <c:if test="${not empty param.update && param.update == 'success'}">
        <div class="message success">Your profile has been updated successfully.</div>
    </c:if>

    <div class="section">
        <h2>My Accounts</h2>
        <div class="actions">
            <a href="${pageContext.request.contextPath}/customer/transfer">New Fund Transfer</a>
            <a href="${pageContext.request.contextPath}/customer/schedule-transfer">Schedule a Transfer</a>
        </div>
        <table>
            <thead>
            <tr>
                <th>Account Number</th>
                <th>Account Type</th>
                <th>Balance</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${accounts}" var="account">
                <tr>
                    <td><c:out value="${account.accountNumber}"/></td>
                    <td><c:out value="${account.accountType}"/></td>
                    <td><fmt:formatNumber value="${account.balance}" type="currency" currencySymbol="LKR "/></td>
                    <td><c:out value="${account.status}"/></td>
                    <td><a href="${pageContext.request.contextPath}/customer/history?accountId=${account.id}">View History</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty accounts}">
                <tr><td colspan="5">You do not have any bank accounts yet.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h2>Pending Scheduled Transfers</h2>
        <table>
            <thead>
            <tr>
                <th>From Account</th>
                <th>To Account</th>
                <th>Amount</th>
                <th>Scheduled Date</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${scheduledTransactions}" var="st">
                <tr>
                    <td><c:out value="${st.sourceAccount.accountNumber}"/></td>
                    <td><c:out value="${st.destinationAccountNumber}"/></td>
                    <td><fmt:formatNumber value="${st.amount}" type="currency" currencySymbol="LKR "/></td>
                    <td><fmt:formatDate value="${st.scheduledTimestamp}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td><c:out value="${st.status}"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty scheduledTransactions}">
                <tr><td colspan="5">You have no pending scheduled transfers.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>