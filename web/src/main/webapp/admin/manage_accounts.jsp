<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Accounts - Admin Panel</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
        .navbar { background-color: #343a40; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .navbar a { color: white; text-decoration: none; padding: 0.5rem 1rem; }
        .navbar a:hover { background-color: #495057; border-radius: 4px; }
        .container { padding: 2rem; max-width: 1200px; margin: auto; }
        .section { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); margin-bottom: 2rem; }
        h2, h3 { color: #333; border-bottom: 2px solid #f4f4f4; padding-bottom: 0.5rem; margin-top: 0; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { text-align: left; padding: 0.75rem; border-bottom: 1px solid #ddd; vertical-align: middle; }
        th { background-color: #f9f9f9; }
        .actions-form { display: flex; align-items: center; gap: 10px; }
        .actions-form input[type="number"] { padding: 0.4rem; border: 1px solid #ccc; border-radius: 4px; width: 100px; }
        .btn, .btn-danger { display: inline-block; padding: 0.4rem 0.8rem; border: none; border-radius: 4px; color: white; font-size: 0.9rem; cursor: pointer; text-decoration: none; }
        .btn { background-color: #28a745; } .btn:hover { background-color: #218838; }
        .btn-danger { background-color: #dc3545; } .btn-danger:hover { background-color: #c82333; }
        .btn-primary { background-color: #005A9E; } .btn-primary:hover { background-color: #004175; }
        .message { padding: 1rem; margin-bottom: 1rem; border-radius: 4px; text-align: center; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
<div class="navbar">
    <h1>Admin Panel</h1>
    <div>
        <a href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
        <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
    </div>
</div>

<div class="container">
    <c:if test="${not empty param.error}">
        <div class="message error">An error occurred: ${param.error}</div>
    </c:if>
    <c:if test="${not empty param.creation && param.creation == 'success'}"><div class="message success">Bank account created successfully.</div></c:if>
    <c:if test="${not empty param.deposit && param.deposit == 'success'}"><div class="message success">Deposit successful.</div></c:if>
    <c:if test="${not empty param.close && param.close == 'success'}"><div class="message success">Account closed successfully.</div></c:if>

    <div class="section">
        <h2>Manage Accounts for <c:out value="${customer.fullName}"/></h2>
        <table>
            <thead>
            <tr>
                <th>Account Number</th>
                <th>Type</th>
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
                    <td>
                        <form class="actions-form" action="${pageContext.request.contextPath}/admin/deposit" method="post" style="display:inline-flex;">
                            <input type="hidden" name="customerId" value="${customer.id}">
                            <input type="hidden" name="accountId" value="${account.id}">
                            <input type="number" name="amount" placeholder="Amount" step="0.01" min="1" required>
                            <button type="submit" class="btn">Deposit</button>
                        </form>
                        <form class="actions-form" action="${pageContext.request.contextPath}/admin/delete-account" method="post" style="display:inline-flex;">
                            <input type="hidden" name="customerId" value="${customer.id}">
                            <input type="hidden" name="accountId" value="${account.id}">
                            <button type="submit" class="btn-danger">Close</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty accounts}"><tr><td colspan="5">This customer has no bank accounts.</td></tr></c:if>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h3>Create New Bank Account</h3>
        <form action="${pageContext.request.contextPath}/admin/create-bank-account" method="post">
            <input type="hidden" name="customerId" value="${customer.id}">
            <table style="width:auto;">
                <tr>
                    <td><label for="accountType">Account Type:</label></td>
                    <td><input type="text" id="accountType" name="accountType" required></td>
                </tr>
                <tr>
                    <td><label for="initialDeposit">Initial Deposit (LKR):</label></td>
                    <td><input type="number" id="initialDeposit" name="initialDeposit" step="0.01" min="0" required></td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="submit" class="btn btn-primary">Create Account</button></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>