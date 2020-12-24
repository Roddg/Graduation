[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e735521926e34926aa08eca18c1e22bc)](https://www.codacy.com/gh/Roddg/graduation/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Roddg/graduation&amp;utm_campaign=Badge_Grade)

#Graduation project TopJava

![Bage](https://user-images.githubusercontent.com/7423323/98437301-76c39300-2124-11eb-8c4d-9b47489c902a.png)

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.


##REST API

Curl commands were tested using Git Bash

###Admin Users API

| Description | Method | Curl command                                      |
|-------------|------|-------------------------------------------------------|
| Get all    | GET | `curl -s http://localhost:8080/graduation/admin/users --user admin@gmail.com:admin`  |
| Get with id    | GET | `curl -s http://localhost:8080/graduation/admin/users/100000 --user admin@gmail.com:admin`  |
| Get by email    | GET | `curl -s http://localhost:8080/graduation/admin/users/by?email=user@yandex.ru --user admin@gmail.com:admin`  |
| Create new user    | POST | `curl -s -X POST -d '{"name":"newName","email":"newemail@ya.ru","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/users --user admin@gmail.com:admin`  |
| Update    | PUT | `curl -s -X PUT -d '{"name":"newName","email":"newemail@ya.ru","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/users/100000 --user admin@gmail.com:admin`  |
| Delete    | DELETE | `curl -s -X DELETE http://localhost:8080/graduation/admin/users/100000 --user admin@gmail.com:admin`  |
| Enable/disable    | PATCH | `curl -s -X PATCH http://localhost:8080/graduation/admin/users/100000?enabled=false --user admin@gmail.com:admin`  |

###User Profile API

| Description | Method | Curl command                                      |
|-------------|------|-------------------------------------------------------|
| Register    | POST | `curl -s -X POST -d '{"name":"newName","email":"newemail@ya.ru","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/profile/register`  |
| Get own     | GET  | `curl -s http://localhost:8080/graduation/profile/ --user user@yandex.ru:password` |
| Get user profile with votes     | GET  | `curl -s http://localhost:8080/graduation/profile/with-votes --user user@yandex.ru:password` |
| Update      | PUT  | `curl -s -X PUT -d '{"name":"newName","email":"newemail@ya.ru","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/profile/ --user user@yandex.ru:password` |
| Delete      |DELETE| `curl -s -X DELETE http://localhost:8080/graduation/profile --user user@yandex.ru:password` |


###Restaurants API

| Description | Method | Curl command                                      |
|-------------|------|-------------------------------------------------------|
| Get all restaurants    | GET | `curl -s http://localhost:8080/graduation/restaurants --user admin@gmail.com:admin`  |
| Get restaurants by date    | GET | `curl -s http://localhost:8080/graduation/restaurants/by?date=2020-12-10 --user admin@gmail.com:admin`  |
| Vote for restaurant    | POST | `curl -s -X POST http://localhost:8080/graduation/votes?restaurantId=100002 --user admin@gmail.com:admin`  |
| Get restaurant with id    | GET | `curl -s http://localhost:8080/graduation/admin/restaurants/100002 --user admin@gmail.com:admin`  |
| Create restaurant    | POST | `curl -s -X POST -d '{"name":"New","enabled":true,"registered":"2020-12-26T17:28:39.214+00:00"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants/ --user admin@gmail.com:admin`  |
| Update restaurant    | PUT | `curl -s -X PUT -d '{"id":100002,"name":"UpdatedName","enabled":true,"registered":"2020-12-26T17:30:01.415+00:00"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants/100002 --user admin@gmail.com:admin`  |
| Delete restaurant    | DELETE | `curl -s -X DELETE 'http://localhost:8080/graduation/admin/restaurants/100002' --user admin@gmail.com:admin`  |
| Enable/disable restaurant    | PATCH | `curl -s -X PATCH 'http://localhost:8080/graduation/admin/restaurants/100002?enabled=false' --user admin@gmail.com:admin`  |

###Dishes API

| Description | Method | Curl command                                      |
|-------------|------|-------------------------------------------------------|
| Get restaurant's dish    | GET | `curl -s 'http://localhost:8080/graduation/admin/restaurants/100002/dishes/100013' --user admin@gmail.com:admin`  |
| Create new dish    | POST | `curl -s -X POST -d '{"name":"New","price":1000,"date":"2020-12-26"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants/100002/dishes --user admin@gmail.com:admin`  |
| Update dish    | PUT | `curl -s -X PUT -d '{"id":100013,"name":"UpdatedName","price":500,"date":"2020-12-10"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants/100002/dishes/100013 --user admin@gmail.com:admin`  |
| Delete dish    | DELETE | `curl -s -X DELETE 'http://localhost:8080/graduation/admin/restaurants/100002/dishes/100013' --user admin@gmail.com:admin`  |