[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e735521926e34926aa08eca18c1e22bc)](https://www.codacy.com/gh/Roddg/graduation/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Roddg/graduation&amp;utm_campaign=Badge_Grade)
---
Graduation project TopJava
---
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

---
##REST API
