# Cinema Reservation System

The Cinema Reservation Application is a web-based platform designed to enable users to view, book, and manage movie reservations with ease. The application provides a comprehensive system for both users and administrators, offering seamless functionality for movie management and booking.

## Features

**User-Friendly Interface:** A simple and intuitive UI that allows users to browse available movies, make reservations, register accounts, and update their profiles.

**Admin Management Panel:** Administrators can add, edit, and remove movies, manage screenings, oversee cinema halls, handle user accounts, and monitor reservations.

**Reservation System:** Users can easily book tickets for specific movies and screenings.

**Responsive Design:** The application is fully responsive, providing a seamless experience across desktop and mobile devices.

## Technologies Used

**Frontend:** 
- HTML, CSS, JavaScript, Thymeleaf for server-side rendering.
  
**Backend:**
- Spring Boot for handling server-side logic.
  
**Database:**
- Supports both H2 (in-memory) and MySQL databases for persistent data storage.
  
**Development Tools:**
- IntelliJ IDEA for efficient development and project management.
  

## Getting Started

### Prerequisites

- JDK 11 or higher
- Maven
- An IDE (e.g., IntelliJ IDEA)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/oskarbroszkowski/cinema-reservation.git
   cd cinema-reservation
   
2. Build the project using Maven:

   ```bash
   mvn clean install

3. Run the application:

   ```bash
   mvn spring-boot:run

4. Open your web browser and go to http://localhost:8080 to access the application.

### Usage

To view movies, navigate to the homepage.

You can register your own account and log in to the application.

Users can click on movie titles to view more details and make reservations using the "Book" buttons.

After logging in, you can manage your profile and reservations.

Admin users can access the admin panel to manage the current cinema setup.

To register as an Admin, please use the Admin password: AdministratorPassword!@#

As an Admin, you can manage movies, screenings, users, and reservations.

### Contributing

If you want to contribute to this project, feel free to fork the repository and submit a pull request.

### License

This project is licensed under the MIT License. See the LICENSE file for details.

### Acknowledgments

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
