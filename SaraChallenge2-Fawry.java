import java.util.*;

abstract class book {
    String isbn;
    String title;
    String author;
    int year;
    double price;

    public book(String isbn, String title, String author, int year, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public String getBasicInfo() {
        return "isbn: " + isbn + ", title: " + title + ", author: " + author +
                ", year: " + year + ", price: " + price;
    }

    abstract boolean isAvailable();
    abstract void decreaseQuantity(int qty) throws Exception;
    abstract void sendToCustomer(String email, String address);

    public static void main(String[] args) {
        System.out.println("bookstore -> this isnt the right class to run :(");
    }
}

class paperBook extends book {
    int stock;

    public paperBook(String isbn, String title, String author, int year, double price, int stock) {
        super(isbn, title, author, year, price);
        this.stock = stock;
    }

    boolean isAvailable() {
        return stock > 0;
    }

    void decreaseQuantity(int qty) throws Exception {
        if (stock < qty) {
            throw new Exception("book store: not enough in stock!!");
        }
        stock = stock - qty;
    }

    void sendToCustomer(String email, String address) {
        shipping.send(address);
    }

    public String getBasicInfo() {
        return super.getBasicInfo() + ", kind: PaperBook, stock left: " + stock;
    }
}

class eBook extends book {
    String filetype;

    public eBook(String isbn, String title, String author, int year, double price, String filetype) {
        super(isbn, title, author, year, price);
        this.filetype = filetype;
    }

    boolean isAvailable() {
        return true;
    }

    void decreaseQuantity(int qty) {
      
    }

    void sendToCustomer(String email, String address) {
        mail.send(email);
    }

    public String getBasicInfo() {
        return super.getBasicInfo() + ", kind: eBook, file: " + filetype;
    }
}

class showcaseBook extends book {
    public showcaseBook(String isbn, String title, String author, int year) {
        super(isbn, title, author, year, 0);
    }

    boolean isAvailable() {
        return false;
    }

    void decreaseQuantity(int qty) throws Exception {
        throw new Exception("book store: u cant sell demo book lol");
    }

    void sendToCustomer(String email, String address) {
    }

    public String getBasicInfo() {
        return super.getBasicInfo() + ", kind: showcase";
    }
}

class bookstore {
    HashMap<String, book> books = new HashMap<>();

    public void addBook(book b) {
        books.put(b.getIsbn(), b);
    }

    public book removeOldBook(int ageLimit) {
        int current = Calendar.getInstance().get(Calendar.YEAR);
        for (String key : new ArrayList<>(books.keySet())) {
            book b = books.get(key);
            if ((current - b.getYear()) > ageLimit) {
                books.remove(key);
                return b;
            }
        }
        return null;
    }

    public double buyBook(String isbn, int quantity, String email, String address) throws Exception {
        book b = books.get(isbn);
        if (b == null || !b.isAvailable()) {
            throw new Exception("book not found or cant be bought");
        }
        b.decreaseQuantity(quantity);
        b.sendToCustomer(email, address);
        return b.getPrice() * quantity;
    }

    public void showBooks() {
        System.out.println("bookstore inventory :");
        if (books.isEmpty()) {
            System.out.println("nothing inside..");
        } else {
            for (book b : books.values()) {
                System.out.println(">> " + b.getBasicInfo());
            }
        }
    }
}

class shipping {
    public static void send(String addr) {
        System.out.println("shipping to: " + addr);
    }
}

class mail {
    public static void send(String email) {
        System.out.println("sending ebook to: " + email);
    }
}

class Main {
    public static void main(String[] args) {
        System.out.println("starting bookstore test");
        bookstore qStore = new bookstore();

        System.out.println("\nAdding Books...");
        paperBook b1 = new paperBook("B1", "summer time", "sara", 2016, 120.5, 5);
        eBook b2 = new eBook("B2", "fawry internship", "ali", 2022, 60, "PDF");
        showcaseBook b3 = new showcaseBook("B3", "bue student life", "laila", 2008);

        qStore.addBook(b1);
        qStore.addBook(b2);
        qStore.addBook(b3);

        qStore.showBooks();

        try {
            System.out.println("\nBuying some books...");
            double total1 = qStore.buyBook("B1", 2, "sara@mail.com", "6th of oct");
            System.out.println("paid: " + total1);

            double total2 = qStore.buyBook("B2", 1, "ebookTest@mail.com", "");
            System.out.println("paid: " + total2);
        } catch (Exception ex) {
            System.out.println("oops: " + ex.getMessage());
        }

        System.out.println("\nremoving books that are too old");
        qStore.removeOldBook(10);

        System.out.println("\nafter removal:");
        qStore.showBooks();
    }
}
