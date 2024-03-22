## Веб-приложение PetFriend для поиска передержки для домашних животных

Приложение создавалось в рамках дипломного проекта по программе GeekBrains "Программист Java".
Разработанное **Pet Boarding Service API** включает в себя следующие контроллеры и эндпойтны:
### LoginController:
* **POST /api/login/{username}:** Принимает строку с именем пользователя. Возвращает объект AuthResponse, содержащий результат аутентификации.
* **POST /api/registerOwner:** Принимает объект OwnerRequest для регистрации владельца питомца.
* **POST /api/registerSitter:** Принимает объект SitterRequest для регистрации пет-ситтера.
### PetOwnerController:
* **GET /api/users/{username}:** Принимает строку с именем пользователя и возвращает объект PetOwner.
* **GET /api/users/dashboard/{userId}:** Принимает идентификатор пользователя и возвращает объект PetOwnerDashboard с информацией о личном кабинете пользователя, включая питомцев и запросы на передержку.
* **GET /api/users/dashboard/{userId}/myPets:** Принимает идентификатор пользователя и возвращает список его питомцев.
* **POST /api/users/dashboard/{userId}/addPet:** Принимает идентификатор пользователя и объект PetDto для добавления нового питомца.
* **POST /api/users/dashboard/{userId}/createRequest:** Принимает идентификатор пользователя и объект PetBoardingRequestDto для создания запроса на передержку питомца.
* **GET /api/users/findSitters/{requestId}:** Принимает идентификатор запроса на передержку и возвращает список подходящих пет-ситтеров.
* **POST /api/users/{userId}/makePersonalRequest:** Принимает идентификатор пользователя и объект PersonalRequestDto для создания персонального запроса.
* **POST /api/users/dashboard/addReview:** Принимает объект ReviewDto для создания отзыва о передержке.
### PetSitterController:
* **GET /api/petSitters/dashboard/{sitterId}:** Принимает идентификатор пользователя и возвращает объект PetSitterDashboard, содержащий информацию о пет-ситтере и его запросах на передержку.
* **POST /api/petSitters/acceptRequest/{requestId}:** Принимает идентификатор запроса на передержку и позволяет пет-ситтеру принять запрос на передержку питомца.
* **POST /api/petSitters/declineRequest/{requestId}:** Принимает идентификатор запроса на передержку. Отклоняет запрос на передержку питомца пет-ситтером.
* **POST /api/petSitters/toggleNewOrders/{userId}:** Принимает идентификатор пользователя и флаг newOrders для обновления статуса принятия новых заказов пет-ситтером.
* **POST /api/petSitters/changeAvailability/{userId}:** Принимает идентификатор пользователя и объект AvailabilityRequest с новыми доступными датами для передержки питомцев. Обновляет доступные даты для передержки питомцев у пет-ситтера.
### MainController:
* **GET /api/main/showUsers:** Возвращает список объектов PetOwner.
* **GET /api/main/showSitters:** Возвращает список объектов PetSitter.
* **DELETE /api/main/deleteUser/{userRole}/{userId}:** Принимает идентификатор и роль пользователя. Удаляет пользователя из системы.
* **POST /api/main/disableAccount/{userRole}/{userId}:** Принимает идентификатор и роль пользователя. Отключает учетную запись пользователя.
* **POST /api/main/enableAccount/{userRole}/{userId}:** Принимает идентификатор и роль пользователя. Включает учетную запись пользователя.
* **DELETE /api/main/deleteReview/{reviewId}:** Принимает идентификатор отзыва и удаляет этот отзыв.

Полная документация API доступна по адресу http://localhost:8080/swagger-ui при локальном запуске приложения.
База данных для приложения разворачивалась в Docker-контейнере. Вся необходимая конфигурация находится в Dockerfile и файле application.yml.
