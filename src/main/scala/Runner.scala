import paser.SelectQueryParser

object Runner {
  def main(args: Array[String]): Unit = {
    SelectQueryParser("select * from books;")
    SelectQueryParser("select id, name from books;")
    SelectQueryParser("select ALL id, name from books;")
    SelectQueryParser("select book_id, book-name from books;")

    SelectQueryParser("select ALL DISTINCT id, name from books;")
  }
}
