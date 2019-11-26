package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class QueryDAOImpl implements QueryDAO {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public CustomEntity getOrderInfo(int orderId) throws Exception {
        return null;
       /* ResultSet rst = CrudUtil.execute("SELECT C.customerId, C.name, O.date  FROM Customer C INNER JOIN `Order` O ON C.customerId=O.customerId WHERE O.id=?", orderId);
        if (rst.next()){
            return new CustomEntity(orderId,
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3));
        }else{
            return null;
        }*/
    }

    @Override
    public CustomEntity getOrderInfo2(int orderId) throws Exception {
        return null;
       /* ResultSet rst = CrudUtil.execute("SELECT O.id, C.customerId, C.name, O.date, SUM(OD.qty * OD.unitPrice) AS Total  FROM Customer C INNER JOIN `Order` O ON C.customerId=O.customerId\" +\n" +
                "                \" INNER JOIN OrderDetail OD on O.id = OD.orderId WHERE O.id=? GROUP BY orderId", orderId);
        if (rst.next()){
            return new CustomEntity(rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getDouble(5));
        }else{
            return null;
        }*/
    }

    @Override
    public List<CustomEntity> getOrdersInfo(String query) throws Exception {

        NativeQuery nativeQuery = session.createNativeQuery("SELECT O.id as orderId, C.customerId as customerId, C.name as customerName, O.date as orderDate, SUM(OD.qty * OD.unitPrice) AS orderTotal  FROM customer C INNER JOIN `order` O ON C.customerId=O.customer_Id " +
                "INNER JOIN order_detail OD on O.id = OD.orderId WHERE O.id LIKE ?1 OR C.customerId LIKE ?2 OR C.name LIKE ?3 OR O.date LIKE ?4 GROUP BY O.id");

        nativeQuery.setParameter(1, query);
        nativeQuery.setParameter(2, query);
        nativeQuery.setParameter(3, query);
        nativeQuery.setParameter(4, query);


        Query query1 = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));

        List<CustomEntity> list = query1.list();

        return list;


    }
     /*   ResultSet rst = CrudUtil.execute("SELECT O.id, C.customerId, C.name, O.date, SUM(OD.qty * OD.unitPrice) AS Total  FROM Customer C INNER JOIN `Order` O ON C.customerId=O.customerId " +
                "INNER JOIN OrderDetail OD on O.id = OD.orderId WHERE O.id LIKE ? OR C.customerId LIKE ? OR C.name LIKE ? OR O.date LIKE ? GROUP BY O.id", query,query,query,query);
        List<CustomEntity> al = new ArrayList<>();
        while (rst.next()){
            al.add(new CustomEntity(rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getDouble(5)));
        }
        return al;
    }*/
}
