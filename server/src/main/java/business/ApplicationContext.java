
package business;

import business.book.BookDao;
import business.book.BookDaoJdbc;
import business.category.CategoryDao;
import business.category.CategoryDaoJdbc;
import business.customer.CustomerDao;
import business.customer.CustomerDaoJdbc;
import business.order.*;

public class ApplicationContext {

    // TODO Add field and complete the getter for bookDao

    private CategoryDao categoryDao;
    private BookDao bookDao;

    private LineItemDao lineItemDao;

    private OrderDao orderDao;

    private OrderService orderService;

    private CustomerDao customerDao;


    public static ApplicationContext INSTANCE = new ApplicationContext();

    private ApplicationContext() {

        categoryDao = new CategoryDaoJdbc();
        bookDao = new BookDaoJdbc();
        orderDao = new OrderDaoJdbc();
        lineItemDao = new LineItemDaoJdbc();
        customerDao = new CustomerDaoJdbc();
        orderService = new DefaultOrderService();
        ((DefaultOrderService)orderService).setBookDao(bookDao);
        ((DefaultOrderService)orderService).setOrderDao(orderDao);
        ((DefaultOrderService)orderService).setCustomerDao(customerDao);
        ((DefaultOrderService)orderService).setLineItemDao(lineItemDao);
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public BookDao getBookDao() { return bookDao; }

    public OrderDao getOrderDao() { return orderDao; }


    public CustomerDao getCustomerDao() { return customerDao; }


    public LineItemDao getLineItemDao(){ return lineItemDao;}

    public OrderService getOrderService() { return orderService; }
}
