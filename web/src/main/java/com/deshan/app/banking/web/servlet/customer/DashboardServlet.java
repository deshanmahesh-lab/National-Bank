package com.deshan.app.banking.web.servlet.customer;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.model.ScheduledTransaction;
import com.deshan.app.banking.service.AccountService;
import com.deshan.app.banking.service.CustomerService;
import com.deshan.app.banking.service.ScheduledTransactionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = "/customer/dashboard")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER", "ADMIN"}))
public class DashboardServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @EJB
    private AccountService accountService;

    @EJB
    private ScheduledTransactionService scheduledTransactionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Principal principal = request.getUserPrincipal();
        String userEmail = principal.getName();

        Customer customer = customerService.getCustomerByEmail(userEmail);
        request.setAttribute("customer", customer);

        if (request.isUserInRole("ADMIN")) {
            request.getRequestDispatcher("/admin/dashboard").forward(request, response);
        } else if (request.isUserInRole("USER")) {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            List<ScheduledTransaction> scheduledTransactions = scheduledTransactionService.getScheduledTransactionsByCustomerId(customer.getId());

            request.setAttribute("accounts", accounts);
            request.setAttribute("scheduledTransactions", scheduledTransactions);

            request.getRequestDispatcher("/customer/dashboard.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp?error=unauthorized");
        }
    }
}