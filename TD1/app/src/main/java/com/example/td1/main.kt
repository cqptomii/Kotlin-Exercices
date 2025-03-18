package com.example.td1

fun showMenu(){
    println("1- Add a book")
    println("2- Remove a book")
    println("3- Search for a book by title")
    println("4- Search for a book by author")
    println("5- Borrow a book")
    println("6- Return a book")
    println("7- Display the list of books")
    println("8- Exit the program")
    println()
    println("Choose an option :")
}

fun main() {
    var menu: Boolean = true
    var lib = Library(mutableListOf())

    while (menu) {

        showMenu();
        val userInput = readlnOrNull()?.toIntOrNull()

        when (userInput) {
            1 -> lib.addBook()
            2 -> lib.removeBook()
            3 -> lib.searchBookByTitle()
            4 -> lib.searchBookByAuthor()
            5 -> lib.borrowBook()
            6 -> lib.returnBook()
            7 -> lib.displayBook()
            8 -> menu = false;
        }
    }
}