<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - National Bank</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
        .navbar { background-color: #343a40; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .navbar a { color: white; text-decoration: none; padding: 0.5rem 1rem; }
        .navbar a:hover { background-color: #495057; border-radius: 4px; }
        .container { padding: 2rem; max-width: 1200px; margin: auto; }
        .section { background-color: #fff; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h2 { color: #333; border-bottom: 2px solid #f4f4f4; padding-bottom: 0.5rem; margin-top: 0; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { text-align: left; padding: 0.75rem; border-bottom: 1px solid #ddd; }
        th { background-color: #f9f9f9; }
        .actions a { text-decoration: none; color: #005A9E; padding: 0.4rem 0.8rem; border: 1px solid #005A9E; border-radius: 4px; }
        .actions a:hover { background-color: #005A9E; color: white; }
        .page-actions { margin-bottom: 1.5rem; }
        .btn { display: inline-block; padding: 0.6rem 1.2rem; border: none; border-radius: 4px; background-color: #005A9E; color: white; font-size: 1rem; cursor: pointer; text-decoration: none; }
        .btn:hover { background-color: #004175; }
    </style>
</head>
<body>
<div class="navbar">
    <h1>Admin Panel</h1>
    <div>
        <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
    </div>
</div>

<div class="container">
    <div class="section">
        <h2>Customer Management</h2>
        <div class="page-actions">
            <a href="${pageContext.request.contextPath}/admin/create_customer.jsp" class="btn">Create New Customer</a>
        </div>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Contact Number</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${allCustomers}" var="customer">
                <tr>
                    <td><c:out value="${customer.id}"/></td>
                    <td><c:out value="${customer.fullName}"/></td>
                    <td><c:out value="${customer.email}"/></td>
                    <td><c:out value="${customer.contactNumber}"/></td>
                    <td><c:out value="${customer.status}"/></td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/admin/manage-accounts?customerId=${customer.id}">Manage Accounts</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty allCustomers}">
                <tr><td colspan="6">No customers found in the system.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>