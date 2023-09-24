# HS-Kleinanzeigen
To run the project you need the following infrastructure: 
  - Java (JDK 17 or newer)
  - Maven (version 3.8 or newer)
  - Docker

The application will use a relational database to persist the data. To create one run the following command:
```
$ docker run --name=mysql -p 4406:3306 -e MYSQL_ROOT_PASSWORD=start01 -e MYSQL_DATABASE=KLEINANZEIGEN -d mysql:8.0.22
```
The application uses Redis for chaching. You can start Redis with the following command:
```
$ docker run --name=hs-kleinanzeigen-cache -p 127.0.0.1:6379:6379 -d redis:6.2.6
```
## Project idea
The aim of the project is to develop an online platform for classified ads. The service enables
the free creation and search of bids and requests of commodities or
rooms in shared flats.
The platform serves only to mediate between sellers and interested parties. A possible
Purchase transaction is not supported.
## Description
To use the platform, a user must register, an email address serves as a username. Each user can also enter his name, location and phone number.
Users can create classified ads. Each ad includes the following information
  - Ad type (bid or wanted)
  - category
  - Title of the ad
  - Description of the ad
  - Pictures (optional, maximum 5)
  - price
  - location

Users can search for classified ads. The search allows different filters:
  - Ad type (bid or wanted)
  - category
  - Title of the ad
  - Price (bid)
  - Location

The search results are displayed on an overview page. On the overview only
title, price and location of the ad. If the user selects an ad, he or she is taken to the
the detail page. This page contains all information about the ad as well as the name and phone number of the user who placed the ad.
phone number of the user who placed the ad.

If a user is interested in an ad, he can add it to his notepad. He can also contact the creator of the ad directly on the details page. Each message
therefore always has a reference to an advertisement.

Every user has a private area, where he can see his notepad, his active ads and his message history.
his message history.
