package app;

import app.bank.model.BankAccount;
import app.hibernate.HibernateUtil;
import org.hibernate.Session;

public class Application {

    public static void main(String[] args) {

        //BEZ abstract active record
        BankAccount bankAccount = new BankAccount("12232", 10.0);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.save(bankAccount);
        //SA active
        BankAccount bankAccount1 = new BankAccount("1223223", 10.0);
        bankAccount1.save();
    }

}
