package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.entity.OrderDetail;
import lk.ijse.dep.pos.entity.OrderDetailPK;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class OrderDetailDAOImpl extends CrudDAOImpl<OrderDetail,OrderDetailPK> implements OrderDetailDAO {

    @Override
    public List<OrderDetail> findAll() throws Exception {

        return  session.createQuery("From OrderDetail",OrderDetail.class).list();
    }

    @Override
    public boolean existsByItemCode(String itemCode) throws Exception {
       return session.createNativeQuery("SELECT * FROM order_detail WHERE itemCode=?1").setParameter(1,itemCode).uniqueResult()!=null;

    }
}
