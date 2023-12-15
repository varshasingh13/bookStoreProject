package business.order;

import api.ApiException;
import business.BookstoreDbException;
import business.JdbcUtils;
import business.book.Book;
import business.book.BookDao;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.Customer;
import business.customer.CustomerDao;
import business.customer.CustomerForm;

import java.sql.*;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;

	private OrderDao orderDao;

	private CustomerDao customerDao;

	private LineItemDao lineItemDao;

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}


	public void setLineItemDao(LineItemDao lineItemDao){this.lineItemDao = lineItemDao;}

	@Override
	public OrderDetails getOrderDetails(long orderId) {
		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.customerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.bookId()))
				.toList();
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);

		try (Connection connection = JdbcUtils.getConnection()) {
			Date ccExpDate = getCardExpirationDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());
			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					ccExpDate, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}



	}
	private Date getCardExpirationDate(String monthString, String yearString) {

		try {
			// Parse month and year strings to integers
			int month = Integer.parseInt(monthString);
			int year = Integer.parseInt(yearString);

			// Get current date
			Calendar calendar = Calendar.getInstance();

			// Set the calendar to the first day of the next month
			calendar.set(Calendar.MONTH, month - 1); // Month is zero-based
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			// Add one month to get the last day of the current month
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			// Get the last day of the month as the card expiration date
			return calendar.getTime();
		} catch (IllegalArgumentException ex) {
			// Handle parsing errors
			ex.printStackTrace(); // Log or handle the exception as needed
			return null; // Return null if parsing fails
		}
	}
	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId,
						item.getBookId(), item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}

	private int generateConfirmationNumber()
	{
		return ThreadLocalRandom.current().nextInt(999999999);


	}

	private void validateCustomer(CustomerForm customerForm) {

    	String name = customerForm.getName();
		String address = customerForm.getAddress();
		String phone = customerForm.getPhone();
		String email = customerForm.getEmail();
		String ccNumber = customerForm.getCcNumber();

		if (name == null || name.isEmpty()){
			throw new ApiException.ValidationFailure("name","Missing name field");
		}
		if (name.length()<4 || name.length() > 45) {
			throw new ApiException.ValidationFailure("name","Invalid name field");
		}

		if (address == null || address.isEmpty()){
			throw new ApiException.ValidationFailure("address","Missing address field");
		}
		if (address.length()<4 || address.length() > 45) {
			throw new ApiException.ValidationFailure("address","Invalid address field");
		}

		if (phone == null || phone.isEmpty()){
			throw new ApiException.ValidationFailure("phone","Missing phone field");
		}
		String phoneDigits = phone.replaceAll("\\D", "");
		if (phoneDigits.length() != 10){
			throw new ApiException.ValidationFailure("phone","Invalid phone field");
		}

		if (email == null || email.isEmpty()){
			throw new ApiException.ValidationFailure("email","Missing email field");
		}
		if (!email.matches("\\S+@\\S+\\.\\S+")) {
			throw new ApiException.ValidationFailure("email","Invalid email field");
		}
		if (ccNumber == null || ccNumber.isEmpty()){
			throw new ApiException.ValidationFailure("ccNumber","Missing ccNumber field");
		}
		String ccNumberDigits = ccNumber.replaceAll("\\D", "");

		if (ccNumberDigits.length() < 14 || ccNumberDigits.length() > 16){
			throw new ApiException.ValidationFailure("ccNumber","Invalid ccNumber field");
		}

		// TODO: Validation checks for address, phone, email, ccNumber

		if (expiryDateIsInvalid(customerForm.getCcExpiryMonth(), customerForm.getCcExpiryYear())) {
			throw new ApiException.ValidationFailure("Please enter a valid expiration date.");

		}
	}

	private boolean expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {

		try {
			int month = Integer.parseInt(ccExpiryMonth);
			int year = Integer.parseInt(ccExpiryYear);


			YearMonth currentYearMonth = YearMonth.now();

			if ((year==0 || month==0 ) || YearMonth.of(year, month).isBefore(currentYearMonth)) {
				return true;
			}
		} catch (NumberFormatException e) {

			return true;
		}

		return false; // Expiry date is valid

	}

	private void validateCart(ShoppingCart cart) {

		if (cart.getItems().size() <= 0) {
			throw new ApiException.ValidationFailure("Cart is empty.");
		}

		cart.getItems().forEach(item-> {
			if (item.getQuantity() < 0 || item.getQuantity() > 99) {
				throw new ApiException.ValidationFailure("Invalid quantity");
			}
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			// TODO: complete the required validations
			if (item.getBookForm().getPrice() !=databaseBook.price()){
				throw new ApiException.ValidationFailure("Book price is not matched");
			}
//			if (item.getBookForm().getCategoryId() !=databaseBook.categoryId()){
//				throw new ApiException.ValidationFailure("Category is not matched");
//			}
		});
	}

}
