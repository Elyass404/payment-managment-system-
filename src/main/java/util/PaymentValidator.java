package util;

import org.model.Payment;

public class PaymentValidator {
    public static String validate(Payment payment){
    if (payment.getAmount() < 0) {
        return "Invalid payment: amount cannot be negative.";
    }
        if (payment.getAmount() > 25000) {
        return "Invalid payment: amount exceeds allowed limit.";
    }
        if (payment.getPaymentType().equals("Salary") && payment.getAmount() < 4500) {
        return "Invalid salary: our salaries are not that low .";
    }
        if ((payment.getPaymentType().equals("Bonus") || payment.getPaymentType().equals("Prime")) && payment.getAmount() > 4000){
        return "Invalid Bonus/Prime: it is too hight for a bonus. Aww bzaf!.";

        }
        return null; //if everything passed the test
}
}
