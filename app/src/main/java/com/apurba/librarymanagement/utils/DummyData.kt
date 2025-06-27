package com.apurba.librarymanagement.utils

import com.apurba.librarymanagement.models.Book

object DummyData {
    
    fun getDummyBooks(): List<Book> {
        return listOf(
            Book(
                id = 1,
                isbn = "978-0743273565",
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                publisher = "Scribner",
                publicationYear = 1925,
                genre = "Classic",
                description = "A classic novel about the American Dream and the Jazz Age.",
                totalCopies = 3,
                availableCopies = 3,
                location = "Fiction Section - Shelf A1",
                coverImageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 2,
                isbn = "978-0446310789",
                title = "To Kill a Mockingbird",
                author = "Harper Lee",
                publisher = "Grand Central Publishing",
                publicationYear = 1960,
                genre = "Classic",
                description = "A powerful story about racial injustice and moral growth.",
                totalCopies = 2,
                availableCopies = 1,
                location = "Fiction Section - Shelf A2",
                coverImageUrl = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 3,
                isbn = "978-0451524935",
                title = "1984",
                author = "George Orwell",
                publisher = "Signet Classic",
                publicationYear = 1949,
                genre = "Dystopian",
                description = "A dystopian novel about totalitarianism and surveillance.",
                totalCopies = 2,
                availableCopies = 0,
                location = "Fiction Section - Shelf A3",
                coverImageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 4,
                isbn = "978-0141439518",
                title = "Pride and Prejudice",
                author = "Jane Austen",
                publisher = "Penguin Classics",
                publicationYear = 1813,
                genre = "Romance",
                description = "A romantic novel of manners in Georgian-era England.",
                totalCopies = 3,
                availableCopies = 2,
                location = "Fiction Section - Shelf A4",
                coverImageUrl = "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 5,
                isbn = "978-0547928241",
                title = "The Hobbit",
                author = "J.R.R. Tolkien",
                publisher = "Houghton Mifflin Harcourt",
                publicationYear = 1937,
                genre = "Fantasy",
                description = "A fantasy novel about Bilbo Baggins' journey with dwarves.",
                totalCopies = 4,
                availableCopies = 4,
                location = "Fiction Section - Shelf A5",
                coverImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 6,
                isbn = "978-0316769488",
                title = "The Catcher in the Rye",
                author = "J.D. Salinger",
                publisher = "Little, Brown and Company",
                publicationYear = 1951,
                genre = "Coming-of-age",
                description = "A coming-of-age story about teenage alienation and loss.",
                totalCopies = 2,
                availableCopies = 1,
                location = "Fiction Section - Shelf A6",
                coverImageUrl = "https://images.unsplash.com/photo-1513475382585-d06e58bcb0e0?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 7,
                isbn = "978-0399501487",
                title = "Lord of the Flies",
                author = "William Golding",
                publisher = "Penguin Books",
                publicationYear = 1954,
                genre = "Allegory",
                description = "A novel about the dark side of human nature and civilization.",
                totalCopies = 3,
                availableCopies = 2,
                location = "Fiction Section - Shelf A7",
                coverImageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            ),
            Book(
                id = 8,
                isbn = "978-0451526342",
                title = "Animal Farm",
                author = "George Orwell",
                publisher = "Signet",
                publicationYear = 1945,
                genre = "Allegory",
                description = "An allegorical novella about the Russian Revolution.",
                totalCopies = 2,
                availableCopies = 1,
                location = "Fiction Section - Shelf A8",
                coverImageUrl = "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=400&h=600&fit=crop",
                createdAt = "2025-06-26T09:00:00.000Z",
                updatedAt = "2025-06-26T09:00:00.000Z"
            )
        )
    }
} 