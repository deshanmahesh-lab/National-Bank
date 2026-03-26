# National Bank - Core Banking System

## Overview
[cite_start]The "National Bank" project is an initiative to modernize a bank's core operational systems using the Jakarta EE platform[cite: 20]. [cite_start]The primary goal is to automate critical, time-sensitive banking functions that are prone to human error when performed manually[cite: 21]. [cite_start]The system architecture is based on a modular Enterprise Archive (EAR), which promotes a clear separation of concerns across Core, EJB, and Web modules[cite: 24, 25, 27, 29].

## Key Features
* [cite_start]**Scheduled Fund Transfers:** Utilizes the EJB Timer Service to allow customers to queue fund transfers for a specific future date and time, processed automatically by the system[cite: 32, 55].
* [cite_start]**Automated Interest Calculations:** Automatically calculates and credits interest to savings accounts on a periodic basis, triggered by a recurring timer[cite: 33, 34].
* [cite_start]**Robust Transaction Management:** Employs Bean-Managed Transactions (BMT) for fine-grained programmatic control over complex operations like fund transfers, and Container-Managed Transactions (CMT) for standard EJB beans[cite: 74, 75, 76].
* [cite_start]**Advanced Security (RBAC):** Enforces Role-Based Access Control with defined USER and ADMIN roles using the Jakarta Security API, `@ServletSecurity` annotations, and `web.xml` constraints[cite: 99, 101, 102].
* [cite_start]**OTP Verification:** Integrates OTP generation and validation via sessions for secure fund transfers[cite: 209, 211, 237].

## Technology Stack
* [cite_start]**Platform:** Jakarta EE [cite: 20]
* [cite_start]**Language:** Java 17 [cite: 485]
* [cite_start]**Application Server:** Payara 6 [cite: 483]
* [cite_start]**Architecture:** Maven Multi-module Structure (core, ejb, web) [cite: 441, 443]
