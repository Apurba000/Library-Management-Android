# Library-Management-Android

## Overview
This document contains the UI designs for a 7-screen library management Android app with minimal features for both regular users and administrators.

## Screen List
1. **Login Screen** - User authentication
2. **Registration Screen** - New user registration
3. **Home Screen** - Books list/catalog
4. **Book Details Page** - Individual book information
5. **Book Create Page** - Admin book entry form
6. **User Profile** - Member details and loan list
7. **Loan Details** - CRUD loan operations

---

## Screen 1: Login Screen

```
┌─────────────────────────────────────┐
│           📚 Library App            │
│                                     │
│                                     │
│        [App Logo/Icon]              │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Username                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Password                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        LOGIN                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │    Create New Account       │  │
│    └─────────────────────────────┘  │
│                                     │
│    [Forgot Password?]               │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Clean, centered design
- App branding at top
- Username/Password fields
- Login button (primary action)
- Registration link (secondary action)
- Forgot password link

### API Endpoints:
- `POST /api/users/login` (needs to be implemented)

---

## Screen 2: Registration Screen

```
┌─────────────────────────────────────┐
│  ← Back                    Register │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     First Name              │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Last Name               │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Username                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Email                   │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Phone                   │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Password                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Confirm Password        │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        REGISTER             │  │
│    └─────────────────────────────┘  │
│                                     │
│    Already have account? [Login]    │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Back navigation
- All required fields for member registration
- Password confirmation
- Register button
- Link back to login

### API Endpoints:
- `POST /api/users` (create user)
- `POST /api/members` (create member)

---

## Screen 3: Home Screen (Books List)

```
┌─────────────────────────────────────┐
│  📚 Books                    👤 Profile │
│                                     │
│    🔍 [Search books...]            │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 The Great Gatsby         │  │
│    │    by F. Scott Fitzgerald   │  │
│    │    Available: 3 copies      │  │
│    │    [BORROW]                 │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 To Kill a Mockingbird    │  │
│    │    by Harper Lee            │  │
│    │    Available: 1 copy        │  │
│    │    [BORROW]                 │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 1984                     │  │
│    │    by George Orwell         │  │
│    │    Available: 0 copies      │  │
│    │    [UNAVAILABLE]            │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 Pride and Prejudice      │  │
│    │    by Jane Austen           │  │
│    │    Available: 2 copies      │  │
│    │    [BORROW]                 │  │
│    └─────────────────────────────┘  │
│                                     │
│    [+ Add Book] (Admin only)        │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Search bar at top
- Book cards with title, author, availability
- Borrow button (enabled/disabled based on availability)
- Profile icon in header
- Add book button (admin only)
- Pull-to-refresh functionality

### API Endpoints:
- `GET /api/books` - Get all books
- `POST /api/loans/borrow` - Borrow a book
- `GET /api/books?search={term}` - Search books

---

## Screen 4: Book Details Page

```
┌─────────────────────────────────────┐
│  ← Back                    📚 Books │
│                                     │
│    ┌─────────────────────────────┐  │
│    │                             │  │
│    │        [Book Cover]         │  │
│    │                             │  │
│    └─────────────────────────────┘  │
│                                     │
│    📖 The Great Gatsby              │
│    by F. Scott Fitzgerald           │
│                                     │
│    📅 Published: 1925               │
│    📚 ISBN: 978-0743273565          │
│    📍 Location: Fiction Section     │
│                                     │
│    📊 Status: Available (3 copies)  │
│                                     │
│    📝 Description:                  │
│    Set in the Jazz Age on Long     │
│    Island, near New York City,     │
│    the novel depicts first-person  │
│    narrator Nick Carraway's        │
│    interactions with mysterious    │
│    millionaire Jay Gatsby...       │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        BORROW BOOK          │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        EDIT BOOK            │  │
│    └─────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Large book cover image
- Complete book information
- Availability status
- Borrow button (primary action)
- Edit button (admin only)
- Back navigation

### API Endpoints:
- `GET /api/books/{id}` - Get book details
- `POST /api/loans/borrow` - Borrow book
- `PUT /api/books/{id}` - Edit book (admin)

---

## Screen 5: Book Create Page (Admin Only)

```
┌─────────────────────────────────────┐
│  ← Back                    Save    │
│                                     │
│    📖 Add New Book                  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Book Title *            │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Author *                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     ISBN *                  │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Publisher               │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Publication Year        │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Total Copies *          │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Location                │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │     Description             │  │
│    │                             │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        SAVE BOOK            │  │
│    └─────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Form with all book fields
- Required fields marked with *
- Save button in header
- Back navigation
- Form validation

### API Endpoints:
- `POST /api/books` - Create new book

---

## Screen 6: User Profile

```
┌─────────────────────────────────────┐
│  ← Back                    Edit    │
│                                     │
│    👤 John Doe                      │
│    📧 john.doe@email.com            │
│    📱 +1 234-567-8900              │
│    📍 123 Main St, City, State      │
│                                     │
│    🏷️ Membership Status: Active     │
│    📅 Member Since: Jan 2024        │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        My Loans             │  │
│    └─────────────────────────────┘  │
│                                     │
│    📚 Currently Borrowed (2):       │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 The Great Gatsby        │  │
│    │    Due: Mar 15, 2024        │  │
│    │    [VIEW DETAILS]           │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │ 📖 To Kill a Mockingbird    │  │
│    │    Due: Mar 20, 2024        │  │
│    │    [VIEW DETAILS]           │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        LOGOUT               │  │
│    └─────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- User information display
- Membership status
- Active loans list
- View details buttons for each loan
- Edit profile button
- Logout button

### API Endpoints:
- `GET /api/members/{id}` - Get member details
- `GET /api/loans/by-member/{memberId}` - Get member's loans

---

## Screen 7: Loan Details

```
┌─────────────────────────────────────┐
│  ← Back                    Return   │
│                                     │
│    📖 The Great Gatsby              │
│    by F. Scott Fitzgerald           │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        [Book Cover]         │  │
│    └─────────────────────────────┘  │
│                                     │
│    📅 Borrowed: Feb 15, 2024        │
│    📅 Due Date: Mar 15, 2024        │
│    ⏰ Days Remaining: 5             │
│    📊 Status: Active                │
│                                     │
│    👤 Borrowed by: John Doe         │
│    📧 john.doe@email.com            │
│                                     │
│    📝 Notes:                        │
│    [No notes added]                 │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        RETURN BOOK          │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        RENEW LOAN           │  │
│    └─────────────────────────────┘  │
│                                     │
│    ┌─────────────────────────────┐  │
│    │        EDIT LOAN            │  │
│    └─────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- Complete loan information
- Book details
- Due date with countdown
- Return button (primary action)
- Renew option
- Edit loan details (admin)
- Back navigation

### API Endpoints:
- `GET /api/loans/{id}` - Get loan details
- `POST /api/loans/return` - Return book
- `PUT /api/loans/{id}` - Edit loan (admin)

---

## Navigation Flow

```
Login → Registration (optional)
  ↓
Home (Books List) → Book Details → Create Book (admin)
  ↓
Profile → Loan Details
```

### Navigation Structure:
- **Bottom Navigation** (3 tabs):
    - Home (Books)
    - Profile
    - My Loans

- **Stack Navigation**:
    - Login → Home → Books/Profile/Loans
    - Book Details → Create/Edit
    - Profile → Loan Details

---

## Design System

### Color Palette:
- **Primary**: #2196F3 (Blue)
- **Secondary**: #FF9800 (Orange)
- **Success**: #4CAF50 (Green)
- **Warning**: #FFC107 (Yellow)
- **Error**: #F44336 (Red)
- **Background**: #FFFFFF (White)
- **Surface**: #F5F5F5 (Light Gray)
- **Text Primary**: #212121 (Dark Gray)
- **Text Secondary**: #757575 (Medium Gray)

### Typography:
- **Headings**: Roboto Bold, 20-24sp
- **Body Text**: Roboto Regular, 14-16sp
- **Captions**: Roboto Regular, 12sp
- **Buttons**: Roboto Medium, 14sp

### Spacing:
- **Small**: 8dp
- **Medium**: 16dp
- **Large**: 24dp
- **Extra Large**: 32dp

### Components:
- Material Design 3 components
- Card-based layouts
- Floating Action Buttons (FAB)
- Bottom Navigation
- App Bar with actions
- Search bars
- Form inputs
- Loading indicators
- Error states

---

## API Endpoints Summary

### Authentication:
- `POST /api/users/login` - User login
- `POST /api/users` - Create user
- `POST /api/members` - Create member

### Books:
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book details
- `POST /api/books` - Create book (admin)
- `PUT /api/books/{id}` - Update book (admin)
- `DELETE /api/books/{id}` - Delete book (admin)

### Loans:
- `GET /api/loans/by-member/{memberId}` - Get member's loans
- `GET /api/loans/{id}` - Get loan details
- `POST /api/loans/borrow` - Borrow book
- `POST /api/loans/return` - Return book
- `PUT /api/loans/{id}` - Update loan (admin)

### Members:
- `GET /api/members/{id}` - Get member details
- `PUT /api/members/{id}` - Update member

---

## Implementation Notes

### Features to Implement:
- ✅ User authentication
- ✅ Book browsing and search
- ✅ Book borrowing and returning
- ✅ Loan management
- ✅ User profile management
- ✅ Admin book management

### Features to Skip (MVP):
- ❌ Advanced search filters
- ❌ Book categories
- ❌ Notifications
- ❌ Book reviews/ratings
- ❌ Advanced admin features
- ❌ Offline support

### Technical Considerations:
- Use Material Design 3 components
- Implement proper error handling
- Add loading states
- Use consistent navigation patterns
- Implement form validation
- Add proper accessibility support
- Use responsive design principles

---

## File Structure (Android)

```
app/
├── src/main/
│   ├── java/com/libraryapp/
│   │   ├── activities/
│   │   │   ├── LoginActivity.kt
│   │   │   ├── RegistrationActivity.kt
│   │   │   ├── HomeActivity.kt
│   │   │   ├── BookDetailsActivity.kt
│   │   │   ├── CreateBookActivity.kt
│   │   │   ├── ProfileActivity.kt
│   │   │   └── LoanDetailsActivity.kt
│   │   ├── adapters/
│   │   │   ├── BookAdapter.kt
│   │   │   └── LoanAdapter.kt
│   │   ├── models/
│   │   │   ├── Book.kt
│   │   │   ├── Loan.kt
│   │   │   ├── User.kt
│   │   │   └── Member.kt
│   │   ├── network/
│   │   │   ├── ApiService.kt
│   │   │   └── RetrofitClient.kt
│   │   └── utils/
│   │       ├── Constants.kt
│   │       └── SharedPrefs.kt
│   └── res/
│       ├── layout/
│       │   ├── activity_login.xml
│       │   ├── activity_registration.xml
│       │   ├── activity_home.xml
│       │   ├── activity_book_details.xml
│       │   ├── activity_create_book.xml
│       │   ├── activity_profile.xml
│       │   └── activity_loan_details.xml
│       ├── values/
│       │   ├── colors.xml
│       │   ├── strings.xml
│       │   └── themes.xml
│       └── drawable/
│           └── (icons and images)
```

---

*This UI design guide provides a complete blueprint for implementing a minimal but functional library management Android app.* 