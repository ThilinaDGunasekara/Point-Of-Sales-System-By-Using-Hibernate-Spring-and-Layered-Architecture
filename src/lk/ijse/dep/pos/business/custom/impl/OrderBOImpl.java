package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.AppInitializer;
import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.OrderDTO;
import lk.ijse.dep.pos.dto.OrderDTO2;
import lk.ijse.dep.pos.dto.OrderDetailDTO;
import lk.ijse.dep.pos.entity.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class OrderBOImpl implements OrderBO {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private QueryDAO queryDAO;

    @Override
    public int getLastOrderId() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDAO.setSession(session);
            session.beginTransaction();
            int lastOrderId = orderDAO.getLastOrderId();
            session.getTransaction().commit();
            return lastOrderId;
        }
    }

    @Override
    public void placeOrder(OrderDTO order) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDAO.setSession(session);
            itemDAO.setSession(session);
            orderDetailDAO.setSession(session);

            session.beginTransaction();

            int oId = order.getId();
            orderDAO.save(new Order(oId, new java.sql.Date(new Date().getTime()), session.get(Customer.class, order.getCustomerId())));

            for (OrderDetailDTO orderDetail : order.getOrderDetails()) {
                orderDetailDAO.save(new OrderDetail(oId, orderDetail.getCode(),
                        orderDetail.getQty(), orderDetail.getUnitPrice()));

                Item item = itemDAO.find(orderDetail.getCode());
                item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
                itemDAO.update(item);

            }

            session.getTransaction().commit();
        }

    }

    @Override
    public List<OrderDTO2> getOrderInfo(String query) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDAO.setSession(session);
            queryDAO.setSession(session);
            session.beginTransaction();
            List<CustomEntity> ordersInfo = queryDAO.getOrdersInfo(query);
            List<OrderDTO2> al = new ArrayList<>();
            session.getTransaction().commit();
            for (CustomEntity customEntity : ordersInfo) {
                al.add(new OrderDTO2(customEntity.getOrderId(), customEntity.getOrderDate(), customEntity.getCustomerId(), customEntity.getCustomerName(), customEntity.getOrderTotal()));
            }
            return al;
        }
    }
}
