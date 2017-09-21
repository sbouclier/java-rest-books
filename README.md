[![Build Status](https://travis-ci.org/sbouclier/java-rest-books.svg?branch=master)](https://travis-ci.org/sbouclier/java-rest-books)
[![Coverage Status](https://coveralls.io/repos/github/sbouclier/java-rest-books/badge.svg?branch=master)](https://coveralls.io/github/sbouclier/java-rest-books?branch=master)

# java-rest-books

Book REST service.

## Requirements
- JDK 8
- Maven 3.x

## Setup

Clone the repository:
```bash
git clone https://github.com/sbouclier/java-rest-books.git
```

Go inside the folder:
```bash
cd java-rest-books/
```

Run the application:
```bash
mvn clean install spring-boot:run
```

Open your browser an go to http://localhost:8080/api/books to see some books.

## API methods

### Create book

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d '{ \ 
   "authors": [ \ 
     { \ 
       "firstName": "John", \ 
       "lastName": "Doe" \ 
     } \ 
   ], \ 
   "description": "desc", \ 
   "isbn": "123-1234567890", \ 
   "publisher": "My publisher", \ 
   "title": "My book" \ 
 }' 'http://localhost:8080/api/books'
```

### Get a book

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/books/978-0321356680'
```

### Update book

```bash
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "authors": [ \ 
     { \ 
       "firstName": "John", \ 
       "lastName": "Doe" \ 
     } \ 
   ], \ 
   "description": "new desc", \ 
   "isbn": "978-1617292545", \ 
   "publisher": "new publisher", \ 
   "title": "new title" \ 
 }' 'http://localhost:8080/api/books/978-1617292545'
```

### Update a book's description

```bash
curl -X PATCH --header 'Content-Type: application/json' --header 'Accept: application/json' -d 'new description' 'http://localhost:8080/api/books/978-1491900864'
```

### Delete a book

```bash
curl -X DELETE --header 'Accept: */*' 'http://localhost:8080/api/books/978-1617292545'
```

### Get all books

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/books?sort=id&order=asc'
```