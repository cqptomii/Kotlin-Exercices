package com.example.td1

import androidx.annotation.Nullable

class Library(private var bookList : MutableList<Book> = mutableListOf() ) {

    fun addBook(){

        println("Enter the Book Title")
        println()
        val bookTitle = readlnOrNull()  ?: return

        println("Enter the Book Author")
        println()
        val bookAuthor = readlnOrNull() ?: return

        println("Enter the Book availability")
        println()
        val bookAvailable = readlnOrNull()?.toBoolean() ?: return


        val tempBook = Book(bookTitle, bookAuthor, bookList.count(),bookAvailable)

        bookList.add(tempBook)
        println("${tempBook.title} add to the library.")
    }

    fun removeBook(){
        println("Enter the Book Title")
        println()
        val bookTitle = readlnOrNull() ?: return

        for (book in bookList) {
            if (book.title == bookTitle) {
                bookList.remove(book)
                println("${book.title} delete from the library")
            }
        }
    }

    fun searchBookByTitle(): Book? {
        println("Enter the Book Title")
        println()
        val bookTitle = readlnOrNull() ?: return null
        return bookList.find { it.title == bookTitle }?.also {
            println("Found: '${it.title}' by ${it.author}")
        } ?: run {
            println("Book '$bookTitle' not found.")
            null
        }
    }

    fun searchBookByAuthor(): Book? {
        println("Enter the Book Author")
        println()
        val bookAuthor = readlnOrNull() ?: return null

        return bookList.find { it.title == bookAuthor }?.also {
            println("Found: '${it.title}' by ${it.author}")
        } ?: run {
            println("Book '$bookAuthor' not found.")
            null
        }
    }

    fun borrowBook(){

        println("Enter the Book Title")
        println()
        val bookTitle = readlnOrNull() ?:return
        println("Enter the Book Author")
        println()
        val bookAuthor = readlnOrNull() ?:return

        for(book in this.bookList){
            if(book.title == bookTitle && book.author == bookAuthor){
                if(!book.available){
                    println("This book is not available")
                    return
                }else{
                    book.available = false
                    println("You have successfully borrow this book")
                    return
                }
            }

        }

        println("The book  is not in the library")
    }

    fun returnBook(){

        println("Enter the Book Title")
        println()
        val bookTitle = readlnOrNull() ?:return
        println("Enter the Book Author")
        println()
        val bookAuthor = readlnOrNull() ?:return

        for(book in this.bookList){
            if(book.title == bookTitle && book.author == bookAuthor){
                if(!book.available){
                    book.available = true
                    println("You have return the book ${book.title}")
                    return
                }else{
                    println("This book is already available")
                }
            }
        }

        println("The book  is not in the library")
    }

    fun displayBook(){

        println("Book amount : ${this.bookList.count()}")
        println("=== Library Menu ===")
        for(book in this.bookList){
            println("------")
            println("Book Id : ${book.id}")
            println("Book Title : ${book.title}")
            println("Book Author : ${book.author}")
            println("Book disponibility : ${ book.available}")
            println(" ")
        }
    }
}