# Electronify

The main goal of this project is to develop the backend of an e-commerce website specializing in electronic devices. The backend is built using Java 21 and Spring Boot, ensuring a reliable, scalable, and maintainable architecture.
The project includes the creation of a RESTful API that manages products, categories, and shopping carts. The API is fully documented using Swagger for ease of understanding and integration.
Product images are handled via Cloudinary, allowing efficient and secure image storage and delivery. The backend is fully testable using tools like Postman or any other API testing platform.
This backend is designed to seamlessly integrate with an existing frontend application, delivering a complete and modern e-commerce experience.

## Technologies Used

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![draw.io](https://img.shields.io/badge/draw.io-F08705?style=for-the-badge&logo=diagramsdotnet&logoColor=white)
![Cloudinary](https://img.shields.io/badge/cloudinary-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)
![Swagger](https://img.shields.io/badge/swagger-%2385EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black)

## Clone the Repository

```bash
git clone https://github.com/Team-3-Electronify/Electronify.git
cd Electronify
```
### Run

```bash
./mvnw spring-boot:run
```
or
```bash
mvn spring-boot:run
```
Alternative Way to Run the Application
If you are using an IDE such as IntelliJ IDEA,VS Code etc, you can simply click the “Run” button or run the main application class directly (the one annotated with @SpringBootApplication).
For example, in IntelliJ IDEA, right-click the main class and choose "Run 'ElectronifyApplication...main()'".

## API Endpoints

### Product

- `GET /api/products` — Get all products
- `GET /api/products/{id}` — Get product by ID
- `GET /api/products/filter` — Get product by filters
- `POST /api/products` — Add new product
- `PUT /api/products/{id}` — Update product
- `DELETE /api/products/{id}` — Delete product

### Category

- `GET /api/categories` — Get all categories
- `GET /api/categories/{id}` — Get category by ID
- `POST /api/categories` — Add new category
- `PUT /api/categories/{id}` — Update category
- `DELETE /api/categories/{id}` — Delete category by ID

### Review

- `GET /api/reviews/byUser` — Get all reviews by userID
- `GET /api/reviews/byProduct` — Get all reviews by productID
- `POST /api/reviews` — Add new review

### User

- `GET /api/users` — Get all users
- `GET /api/users/{id}` — Get user by ID
- `POST /api/auth/register` — Registration of new user
- `POST /api/auth/login` — User Log in
- `PUT /api/users/{id}` — Update user by ID
- `DELETE /api/users/{id}` — Delete category by ID

### Cart

- `GET /api/cart` — Get cart by username
- `POST /api/cart/add/{productId}` — Add new cart item by product ID
- `PUT /api/cart/update/{productId}` — Update cart item by product ID
- `DELETE /api/cart/remove/{productId}` — Delete cart item by product ID

## Class Diagram

[View Class Diagram] (https://drive.google.com/file/d/1cg5x1FwFY-r0HY_zOLb9dy0hgz31yhDP/view?usp=share_link)

## Flow chat Diagram
[View Flow chat Diagram] (https://drive.google.com/file/d/1lRTPlr-jibYnW6GV97WK4XKShyFYudHC/view?usp=sharing)
## Contributors
Paula Calvo Garcia
    <a href="https://github.com/PCalvoGarcia">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>
Nadiia Alaieva
    <a href="https://github.com/tizzifona">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>
Vita Poperechna
    <a href="https://github.com/VitaPoperechna">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>
Anna Nepyivoda
    <a href="https://github.com/NepyAnna">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>
