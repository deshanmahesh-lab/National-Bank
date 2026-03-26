package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.service.AccountService;
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

@WebServlet(name = "ManageAccountsServlet", urlPatterns = "/admin/manage-accounts")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class ManageAccountsServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @EJB
    private AccountService accountService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerIdStr = request.getParameter("customerId");

        if (customerIdStr == null || customerIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=missingCustomerId");
            return;
        }

        try {
            Long customerId = Long.parseLong(customerIdStr);
            Customer customer = customerService.getCustomerById(customerId);

            if (customer == null) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=customerNotFound");
                return;
            }

            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);

            request.setAttribute("customer", customer);
            request.setAttribute("accounts", accounts);
            request.getRequestDispatcher("/admin/manage_accounts.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=invalidCustomerId");
        }
    }
}