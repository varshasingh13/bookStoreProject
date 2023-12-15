package business.book;

import business.BookstoreDbException;
import business.JdbcUtils;
import business.category.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import business.BookstoreDbException.BookstoreQueryDbException;

public class BookDaoJdbc implements BookDao {

    private static final String FIND_BY_BOOK_ID_SQL =
            "SELECT book_id, title, author, description, price, rating, is_public, is_featured, category_id " +
                    "FROM book " +
                    "WHERE book_id = ?";

    private static final String FIND_BY_CATEGORY_ID_SQL =
            "SELECT book_id, title, author, description, price, rating, is_public, is_featured, category_id " +
            "FROM book " +
            "WHERE category_id = ?";;

    private static final String FIND_RANDOM_BY_CATEGORY_ID_SQL =
            "SELECT book_id, title, author, description, price, rating, is_public, is_featured, category_id " +
                    "FROM book " +
                    "WHERE category_id = ? " +
                    "ORDER BY RAND() " +
                    "LIMIT ?";

    private static final String FIND_RANDOM_BY_CATEGORY_NAME_SQL =
            "SELECT book.book_id, book.title, book.author, book.description, book.price, book.rating, book.is_public, book.is_featured, book.category_id " +
                    "FROM book JOIN category ON book.category_id=category.category_id " +
                    "WHERE name = ? " +
                    "ORDER BY RAND() " +
                    "LIMIT ?";



    @Override
    public Book findByBookId(long bookId) {
        Book book = null;
        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_BOOK_ID_SQL)) {
            statement.setLong(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    book = readBook(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new BookstoreQueryDbException("Encountered a problem finding book " + bookId, e);
        }
        return book;
    }

    @Override
    public List<Book> findByCategoryId(long categoryId) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CATEGORY_ID_SQL)) {
            statement.setLong(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = readBook(resultSet);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new BookstoreQueryDbException("Encountered a problem finding book " + categoryId, e);
        }

        return books;
    }

    @Override
    public List<Book> findRandomByCategoryId(long categoryId, int limit) {
        List<Book> books = new ArrayList<>();


        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_RANDOM_BY_CATEGORY_ID_SQL)) {
            statement.setLong(1, categoryId);
            statement.setLong(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = readBook(resultSet);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new BookstoreQueryDbException("Encountered a problem finding book " + categoryId, e);
        }

        return books;
    }

    @Override
    public List<Book> findRandomByCategoryName(String categoryName, int limit) {
        List<Book> books = new ArrayList<>();


        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_RANDOM_BY_CATEGORY_NAME_SQL)) {
            statement.setString(1, categoryName);
            statement.setLong(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = readBook(resultSet);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new BookstoreQueryDbException("Encountered a problem finding book " + categoryName, e);
        }

        return books;
    }

    private Book readBook(ResultSet resultSet) throws SQLException {
        // TODO add description, isFeatured, rating to Book results
        long bookId = resultSet.getLong("book_id");
        String title = resultSet.getString("title");
        String author = resultSet.getString("author");
        int price = resultSet.getInt("price");
        boolean isPublic = resultSet.getBoolean("is_public");
        long categoryId = resultSet.getLong("category_id");
        String description = resultSet.getString("description");
        int rating = resultSet.getInt("rating");
        boolean isFeatured = resultSet.getBoolean("is_featured");

        return new Book(bookId, title, author, price, isPublic, categoryId, description, rating, isFeatured);
    }
}
