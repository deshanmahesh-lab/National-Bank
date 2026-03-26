package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.service.CustomerService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = "/admin/dashboard")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class AdminDashboardServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> allCustomers = customerService.getAllCustomers();
        request.setAttribute("allCustomers", allCustomers);
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}