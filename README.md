# Lost & Found System
The system provides CRUD operations (create, read, update and delete data from database), role-based access control (RBAC) and login, logout functionality. 

To protect from too many login attempt with incorrect password, account will be locked for more than 3 failed log in. 

It will be automatically unlocked after one hour.

## Table of Contents
- [Technologies](#technologies)  
- [Setup](#setup)  
- [System Overview](#system-overview)  
- [System Captures](#system-captures)  
- [Ideas for Enhancement](#ideas-for-enhancement)  

## Technologies
  - Front-end
      - Thymeleaf
      - HTML 5
      - CSS 3
      - Bootstrap 5
  - Backend
      - Java 8
      - Spring Web
      - Spring Boot DevTools
      - Spring Data JPA
      - Spring Security
      - Lombok
      - MapStruct
      - H2 Database
      - Validation
      - Maven

## System Overview
This system is aimed to help people finding their lost properties. If they lost or found something, they can report by using this system. It provides two roles (admin, user) to access the system. 

Admin can
- Manage users – (CRUD – create, read, update, and delete) user detail and information.
- Manage user roles.
- Manage lost/found item reports of all users.

User can
- Create lost/found item reports.
- Read all lost/found item reports.
- Edit and delete their own reported items.
- Edit user information.
- Add lost Item Image.
- Filter Item based on category

<a name="system-captures"></a>

## System Captures

#### Login
![This is an image](/capture/home.png)

#### Registration
![This is an image](/capture/register.png)

#### Profile page
![This is an image](/capture/profile.png)

#### User Management
![This is an image](/capture/user.png)

#### Item Details
![This is an image](/capture/info.png)