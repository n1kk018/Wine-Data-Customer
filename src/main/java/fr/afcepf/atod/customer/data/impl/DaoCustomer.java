/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.afcepf.atod.customer.data.impl;

import fr.afcepf.atod.customer.data.api.IDaoCustomer;
import fr.afcepf.atod.vin.data.exception.WineErrorCode;
import fr.afcepf.atod.vin.data.exception.WineException;
import fr.afcepf.atod.wine.data.impl.DaoGeneric;
import fr.afcepf.atod.wine.entity.Customer;
import fr.afcepf.atod.wine.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nikko
 */
@Service
@Transactional
public class DaoCustomer extends DaoGeneric<User, Integer> implements IDaoCustomer {
    /*****************************************************.
     *                  Requetes HQL
     ****************************************************/

    private static final String REQCONNEXION = "SELECT u FROM User u "
                + "LEFT JOIN FETCH u.adresses "
                + "WHERE  u.mail     = :paramMail "
                + "AND   u.password = :paramPassword "
                + "AND u.activated = 1 "
                + "AND u.user_type = 'CUSTOMER'";
    
    private static final String REQCUSTMAIL = "SELECT u FROM User u "
                + "LEFT JOIN FETCH u.adresses "
                + "WHERE  u.mail     = :paramMail "
                + "AND u.activated = 1 "
                + "AND u.user_type = 'CUSTOMER'";

    /****************************************************.
     *                 Fin Requetes HQL
     ****************************************************/
    /**
     * 
     * @param mail
     * @param password
     * @return
     * @throws WineException 
     */
    @Override
    public Customer connect(String mail, String password) throws WineException {
        Customer user = null;
        if (!mail.equalsIgnoreCase("") && 
                !password.equalsIgnoreCase("")) {
           user = (Customer) (getSf().getCurrentSession()
                   .createQuery(REQCONNEXION)
                   .setParameter("paramMail", mail)
                   .setParameter("paramPassword", password)
                   .uniqueResult());
 
            if (!user.getLastname().equalsIgnoreCase("")){
                return user;
            } else {
                throw  new WineException(WineErrorCode.LOGIN_MDP_INVALIDE, 
                    "user not in the db");
            }
        } else {
            throw  new WineException(WineErrorCode.LOGIN_MDP_INVALIDE, 
                    "mail  or password invalid");
        }        
    }

	@Override
	public Customer addCustomer(Customer custom) {
		Customer user = null;
		user = (Customer) getSf().getCurrentSession().save(custom);
		return user;
	}

    @Override
    public Customer findUserbyMail(String mail) throws WineException {
        Customer user = null;
        user = (Customer) getSf().getCurrentSession()
                .createQuery(REQCUSTMAIL)
                .setParameter("paramMail", mail)
                .uniqueResult();
        return user;
    }
    
}

