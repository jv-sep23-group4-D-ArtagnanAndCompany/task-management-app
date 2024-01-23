![Task management app](https://miro.medium.com/v2/resize:fit:720/format:webp/1*8G1vA7egoxrL4Bb7RAgnPQ.jpeg)
# 📅 TASK MANAGEMENT APP 📅
___
## 👋 Introduction 👋
Welcome to the Task Management Project – your gateway to effective task and project management!

In today's fast-paced world, staying organized and on top of tasks is crucial.
Our web-based application is designed to simplify and enhance your task management experience.
Whether you're an individual looking to streamline personal activities or a team collaborating on complex projects, this system has got you covered.
This system enables task creation, assignment, progress tracking, and completion.
___
## 🌐 Technology stack 🌐
* Programming Language: Java
* Application Configuration: Spring Boot, Spring, Maven
* Accessing Data Spring Data: JPA, Hibernate, MySQL, PostgreSQL
* Web Development: Spring MVC, Servlets, JSP, Tomcat
* Security: Spring Security, JWT
* Integration with: DropBox API, Telegram API
* Testing and Documentation: JUnit, Mockito, Swagger, TestContainers
* Version Control: Git
* Deploy: AWS
___
## 💻 Functionality 💻
1. **Auth Controller**:
    - POST: /api/auth/register - User registration
    - POST: /api/auth/login - User authentication

2. **Users Controller: Managing authentication and user registration**
    - PUT: /users/{id}/role - update user role
    - GET: /users/me - get my profile info
    - PUT/PATCH: /users/me - update profile info

3. **Project Controller**:
    - POST: /api/projects - Create a new project
    - GET: /api/projects - Retrieve user's projects
    - GET: /api/projects/{id} - Retrieve project details
    - PUT: /api/projects/{id} - Update project
    - DELETE: /api/projects/{id} - Delete project

4. **Task Controller**:
    - POST: /api/tasks - Create a new task
    - GET: /api/tasks/projectId/{projectId} - Retrieve tasks for a project
    - GET: /api/tasks/{id} - Retrieve task details
    - PUT: /api/tasks/{id} - Update task
    - DELETE: /api/tasks/{id} - Delete task

5. **Comment Controller**:
    - POST: /api/comments - Add a comment to a task
    - GET: /api/comments?taskId={taskId} - Retrieve comments for a task

6. **Attachment Controller**:  // This is where we will interact with Dropbox API
    - POST: /api/attachments - Upload an attachment to a task (File gets uploaded to Dropbox, and we store the Dropbox File ID in our database)
    - GET: /api/attachments?taskId={taskId} - Retrieve attachments for a task (Get the Dropbox File ID from the database and retrieve the actual file from Dropbox)

7. **Label Controller**:
    - POST: /api/labels - Create a new label
    - GET: /api/labels - Retrieve labels
    - PUT: /api/labels/{id} - Update label
    - DELETE: /api/labels/{id} - Delete label
___
## 👨‍🚀 Ability to send requests to the endpoints by Postman 👨‍🚀
Moreover, you can try sending requests to the endpoints using Postman by clicking [here](https://www.postman.com/lunar-module-cosmologist-43034160/workspace/my-projects/collection/31108999-59b72060-37e1-4d64-a2b3-82b649343457?action=share&creator=31108999).
It's a collection of prepared requests where you can test functionality of the controllers.
Here are all endpoints, to which users can send requests. Log in and registration endpoints don't need a token, but the others do.
Firstly, you need to register if you haven't already. Next, you'll need to log in to obtain a token.
After that, you must pass the token in the header authorization in the Bearer Token field.
To elaborate on that, I recorded a video where I showed how all endpoints work, including endpoints, which are accessible for administrators.
You can watch it by clicking [here](https://www.loom.com/share/53573f09fe684103896f3e9107d278fe) 😊🎥.
Also, you can send requests from swagger by clicking [here](http://ec2-54-236-111-138.compute-1.amazonaws.com/swagger-ui/index.html#/) 😊.
___
## 🧰 Setup Instructions 🧰
1. Clone repository: clone from the console with the command: git clone https://github.com/jv-sep23-group4-D-ArtagnanAndCompany/task-management-app.git
2. Check database setup: Customize the database settings in the application.properties file.
3. Build and run project: mvn spring-boot:run
___
## 🐳 Running with Docker Compose 🐳
If you prefer to run the task management application in a Docker container using Docker Compose, follow these steps:

1. Clone repository: Clone the repository from the console with the command: git clone https://github.com/jv-sep23-group4-D-ArtagnanAndCompany/task-management-app.git

2. Check Docker Compose file: Ensure that the docker-compose.yml file in the root of the project is configured appropriately. You can customize environment variables, ports, and other settings in this file.

3. Build and run the project: Execute the following commands in the project root directory:
   ```bash
   docker-compose build
   docker-compose up
___
## ⌛ History of creating the project (Challenges faced) ⌛
### _Configuration Management and GitHub_
To enhance configuration management and address the potential exposure of sensitive information on GitHub, we implemented several measures. 
Initially, variables were sourced from application.properties, which were publicly available on GitHub. 
To rectify this, We introduced an environment (env) file and an application-local.properties file.

Sensitive variables were now stored in the env file, which we promptly added to the .gitignore to prevent accidental exposure on GitHub. 
Custom values for these sensitive variables were set specifically for the test application.properties. 
Furthermore, we incorporated the spring.config.import=optional:file:.env[.properties] annotation in the application-local.properties file. 
This allowed the application to pull variables locally from the env file, ensuring secure configuration management.
### _Task Status Notification_
Recognizing the need to keep users informed about changes in their task status, we implemented a Telegram bot for efficient and rapid communication. 
This addition aimed to provide users with a quick and convenient means of receiving updates on their tasks.
### _Testing Controller and Authentication Issue_
During the testing of the controller, a ClassCastException error surfaced due to the user not being enrolled in authentication. 
To address this, we introduced a solution by incorporating a setUp method with a @BeforeEach annotation. 
In this method, the User was set up in Authentication within the SecurityConfig. 
This approach ensured proper authentication and resolved the ClassCastException issue during controller testing.
___
## 💹 Possible improvements 💹
![App Screenshot](https://media3.giphy.com/media/KEeyysnlLdJ4afgEhk/giphy.gif)



In the future, we plan to add a monthly subscription.
The user will be able to use the free version of the application, but it will be limited in some way,
and to get the full version, you will need to pay a monthly fee.
This will provide an opportunity to maintain and improve the application in the future.
We also want to integrate a feedback system to receive feedback from users and implement appropriate changes.
___
## 💟 Thanks 💟
Thank you for reading to the end, Good Luck!!! 🌈


![App Screenshot](https://gifsec.com/wp-content/uploads/2022/10/good-luck-gif-1.gif)
