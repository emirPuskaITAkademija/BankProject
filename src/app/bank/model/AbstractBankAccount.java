package app.bank.model;

import app.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * CRUD
 *
 * <li> Create
 * <li> Retrieve
 * <li> Update
 * <li> Delete
 *
 * @author Grupa2
 */
public abstract class AbstractBankAccount {

    public BankAccount getThis() {
        return (BankAccount) this;
    }

    public void save() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.save(this);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.update(this);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public BankAccount get() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            BankAccount bankAccount = (BankAccount) session.get(BankAccount.class, getThis().getAccountNumber());
            session.getTransaction().commit();
            return bankAccount;
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.delete(this);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<BankAccount> loadBankAccounts() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            Query query = session.createQuery("from BankAccount");
            session.getTransaction().commit();
            List<BankAccount> bankAccounts = query.list();
            return bankAccounts;
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean transferMoney(BankAccount fromAccount, BankAccount toAccount, double amount) {
        if (fromAccount == null || toAccount == null) {
            return false;
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            return false;
        }
        if (amount < 0) {
            return false;
        }
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            Object fromBankAccountObj = session.get(BankAccount.class, fromAccount.getAccountNumber());
            if (fromBankAccountObj == null) {
                return false;
            }
            BankAccount fromBankAccount = (BankAccount) fromBankAccountObj;
            if (fromBankAccount.getAmount() < amount) {
                return false;
            }
            Object toBankAccountObj = session.get(BankAccount.class, toAccount.getAccountNumber());
            if (toBankAccountObj == null) {
                return false;
            }
            BankAccount toBankAccount = (BankAccount) toBankAccountObj;
            //emir racun - 1000
            fromBankAccount.setAmount(fromBankAccount.getAmount() - amount);
            toBankAccount.setAmount(toBankAccount.getAmount() + amount);
            session.update(fromBankAccount);
            session.update(toBankAccount);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }
}
