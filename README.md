## About ReservationTicketingWebService

ReservationTicketingWebService is the web service that provides ticketing services to the Zambia Railway Limited (ZRL) Service. Point of Sale devices connect to the web service to purchase tickets for customers. In sceenarios 
where customers may purchase tickets on the train where connectivity is an issue, ticket agents can sell tickets to customers through an offline means that stores the tickets purchased on the device then sends 
the purchased tickets to this web service later on for post-purchase of tickets after the train trip.
This web service provides a role-based system for ZRL staff to manage the processes involved in the purchase of tickets such as scheduling of trips, managing train stations, incident management etc.

## Technical Details

The ReservationTicketingWebService mobile application is developed using Java, database is Microsoft SQL

## Install the Java
Before proceeding, make sure your computer has Java installed. Minimum version is Java 8. See Oracle website for documentation on Java installation

## Install the Microsoft SQL
Before proceeding, make sure your computer has Microsoft SQL installed. See guidance online for installtion of Microsoft SQL and its tools.

## Dependency
Generate WAR file using your favorite IDE such as Eclipse or your command prompt/bash. <br><br>

Using Eclipse:<br>
Right Click on Project and click on "Export"<br>
Proceed with the steps to generate the war file.<br>
Go to your project Directory and inside Dist Folder you will get war file that you copy on your tomcat webApp Folder.<br>
Start the tomcat.<br>
It automatically extracts the folder from the war file.
