package com.deshan.app.banking.web.security;

import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.service.CustomerService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.Set;

@ApplicationScoped
public class AppIdentityStore implements IdentityStore {

    @EJB
    private CustomerService customerService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential upc = (UsernamePasswordCredential) credential;

            String email = upc.getCaller();
            String encryptedPassword = upc.getPasswordAsString();

            if (customerService.validateCredentials(email, encryptedPassword)) {
                Customer customer = customerService.getCustomerByEmail(email);
                return new CredentialValidationResult(customer.getEmail(), Set.of(customer.getUserRole().name()));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}