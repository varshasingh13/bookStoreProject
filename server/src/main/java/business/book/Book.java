package business.book;

/*
 * TODO: Create a record constructor with fields corresponding to the fields in the
 * book table of your database.
 */

public record Book(long bookId, String title, String author,
				   int price,
				   boolean isPublic, long categoryId, String description, int rating, boolean isFeatured) {
	public Book(long bookId, String title, String author, int price, boolean isPublic, long categoryId, String description, int rating, boolean isFeatured) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.price = price;
		this.isPublic = isPublic;
		this.categoryId = categoryId;
		this.description = description;
		this.rating = rating;
		this.isFeatured = isFeatured;
	}

}
