package com.deshan.app.banking.web.servlet.auth;

import com.deshan.app.banking.model.AccountStatus;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.service.CustomerService;
import com.deshan.app.banking.util.Encryption;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "ActivateAccountServlet", urlPatterns = "/auth/activate")
public class ActivateAccountServlet extends HttpServlet {

    @EJB
    private CustomerService customerService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String encodedEmail = request.getParameter("id");
        String verificationCode = request.getParameter("vc");
        String password = request.getParameter("password");

        if (encodedEmail == null || verificationCode == null || password == null || password.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp?activation=failed");
            return;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
            String email = new String(decodedBytes);

            Customer customer = customerService.getCustomerByEmail(email);

            if (customer != null && customer.getStatus() == AccountStatus.INACTIVE && verificationCode.equals(customer.getVerificationCode())) {
                customer.setPassword(Encryption.encrypt(password));
                customer.setStatus(AccountStatus.ACTIVE);
                customer.setVerificationCode(null);
                customerService.updateCustomer(customer);
                response.sendRedirect(request.getContextPath() + "/auth/login.jsp?activation=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/auth/login.jsp?activation=failed");
            }
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp?activation=failed");
        }
    }
}