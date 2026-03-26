<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile - National Bank</title>
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
        input[type="text"], input[type="email"] { width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        input[readonly] { background-color: #e9ecef; cursor: not-allowed; }
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
        <h2>Update Your Profile</h2>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/customer/update-profile" method="post">
            <div class="form-group">
                <label for="email">Email Address (Cannot be changed)</label>
                <input type="email" id="email" name="email" value="<c:out value='${customer.email}'/>" readonly>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${customer.fullName}'/>" required>
            </div>
            <div class="form-group">
                <label for="contactNumber">Contact Number</label>
                <input type="text" id="contactNumber" name="contactNumber" value="<c:out value='${customer.contactNumber}'/>" required>
            </div>
            <button type="submit" class="btn">Update Profile</button>
        </form>
    </div>
</div>
</body>
</html>