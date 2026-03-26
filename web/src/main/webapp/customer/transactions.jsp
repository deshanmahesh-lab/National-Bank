<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction History - National Bank</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
        .navbar { background-color: #005A9E; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .navbar a { color: white; text-decoration: none; padding: 0.5rem 1rem; }
        .navbar a:hover { background-color: #004175; border-radius: 4px; }
        .container { padding: 2rem; max-width: 1200px; margin: auto; }
        .section { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h2 { color: #333; border-bottom: 2px solid #f4f4f4; padding-bottom: 0.5rem; margin-top: 0; }
        .account-info { margin-bottom: 1.5rem; }
        .account-info p { margin: 0.5rem 0; font-size: 1.1rem; }
        .account-info strong { color: #555; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { text-align: left; padding: 0.75rem; border-bottom: 1px solid #ddd; }
        th { background-color: #f9f9f9; }
        .credit { color: #28a745; font-weight: bold; }
        .debit { color: #dc3545; font-weight: bold; }
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
    <div class="section">
        <h2>Transaction History</h2>
        <div class="account-info">
            <p><strong>Account Number:</strong> <c:out value="${account.accountNumber}"/></p>
            <p><strong>Account Type:</strong> <c:out value="${account.accountType}"/></p>
            <p><strong>Current Balance:</strong> <fmt:formatNumber value="${account.balance}" type="currency" currencySymbol="LKR "/></p>
        </div>
        <table>
            <thead>
            <tr>
                <th>Date & Time</th>
                <th>Description</th>
                <th>Type</th>
                <th>Amount</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${transactions}" var="tx">
                <tr>
                    <td><fmt:formatDate value="${tx.timestamp}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><c:out value="${tx.description}"/></td>
                    <c:choose>
                        <c:when test="${tx.sourceAccount.id == account.id}">
                            <td class="debit">DEBIT</td>
                            <td class="debit">- <fmt:formatNumber value="${tx.amount}" type="currency" currencySymbol="LKR "/></td>
                        </c:when>
                        <c:otherwise>
                            <td class="credit">CREDIT</td>
                            <td class="credit">+ <fmt:formatNumber value="${tx.amount}" type="currency" currencySymbol="LKR "/></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
            <c:if test="${empty transactions}">
                <tr><td colspan="4">No transactions found for this account.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>