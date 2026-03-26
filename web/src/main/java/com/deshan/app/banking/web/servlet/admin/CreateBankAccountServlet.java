package com.deshan.app.banking.web.servlet.admin;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.AccountStatus;
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
import java.util.UUID;

@WebServlet(name = "CreateBankAccountServlet", urlPatterns = "/admin/create-bank-account")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class CreateBankAccountServlet extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private CustomerService customerService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerIdStr = request.getParameter("customerId");

        try {
            Long customerId = Long.parseLong(customerIdStr);
            String accountType = request.getParameter("accountType");
            double initialDeposit = Double.parseDouble(request.getParameter("initialDeposit"));

            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=customerNotFound");
                return;
            }

            Account newAccount = new Account();
            newAccount.setCustomer(customer);
            newAccount.setAccountType(accountType);
            newAccount.setBalance(initialDeposit);
            newAccount.setStatus(AccountStatus.ACTIVE);

            String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            newAccount.setAccountNumber(accountNumber);

            accountService.createAccount(newAccount);

            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerId + "&creation=success");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=invalidInput");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/manage-accounts?customerId=" + customerIdStr + "&creation=failed");
        }
    }
}