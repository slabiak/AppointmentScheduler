[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/slabiak/AppointmentScheduler)

# Appointment scheduler

>This is a Spring Boot Web Application to manage and schedule appointments between providers and customers. It has many features such as automatic invoicing, email notifications, appointments cancelation, providers individual working plans with brakes etc.


<a href="https://github.com/slabiak/slabiak.github.io/blob/master/images/appointmentscheduler/calendar.png?raw=true"><img src="https://github.com/slabiak/slabiak.github.io/blob/master/images/appointmentscheduler/calendar.png?raw=true" width="600"></a>

## Demo

The live demo of this app is deployed and can be found [here](https://appointmentscheduler2.herokuapp.com) 

You can use the following credentials with live demo:

| Account type | Username | Password 
| --- | --- | --- |
| `admin` | admin | qwerty123 |
| `provider` | provider |qwerty123 |
| `corporate customer` | customer_c |qwerty123 |
| `retail customer` | customer_r |qwerty123 |

## Blog

This application is being described in [devoxify.com](https://devoxify.com/) blog. If you are interested in how this project was created, what issues were encoutered and how they were solved I highly encourage you to visit this blog.

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/slabiak/AppointmentScheduler.git
```

**2. Create MySQL database**

```bash
create database appointmentscheduler
```

- After that run MySQL script to create tables `src/main/resources/appointmentscheduler.sql`

**3. Configure enviroment variables**

+ open `src/main/resources/application.properties`
+ set env variables for JDBC `dbURL`, `dbUsername`, `dbPassword`
+ set env variables for mail server  `mailUsername`, `mailPassword`
+ set jwtSecret, encoded with Base64 `jwtSecret`

**4. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>

**5. Login to admin account**

+ username: `admin`
+ password: `qwerty123`


## Account types 

`admin` -  is created at database initialization. Admin can add new providers,  services and assign services to providers. Admin can see list of all: appointments, providers, customers, invoices. He can also issue invoices manually for all confirmed appointments.

`provider` - can by created by admin only. Provider can set his own working plan, add brakes to that working plan and change his available services. Provider sees only his own appointments.

`customer retail` - registration page is public and can be created by everyone. Customer can only book new appointments and manage them. This type of customer sees only services which targets retail customer.

`customer corporate` - almost the same as retail customer. The only difference is that this type of account needs to provide VAT number and Company Name and can see only services which targets corporrate customer.

## Booking process

To book a new appointment customer needs to click `New Appointment` button on all appointments page and then:

1. Choose desired work from available works list
2. Choose provider for selected work
3. Choose on of available date which is presented to him
4. Click book on confirmation page

Available hours are calculatated with getAvailableHours function from AppointmentService:

`List<TimePeroid> getAvailableHours(int providerId,int customerId, int workId, LocalDate date)`

This function works as follow:

1. gets selected provider working plan
2. gets working hours from working plan for selected day 
3. excludes all brakes from working hours
4. excludes all providers booked appointments for that day
5. excludes all customers booked appointments for that day
6. gets selected work duration and calculate available time peroids 
7. returns available hours

## Appointments lifecycle

**1. Every appointment has it's own status. Below you can find description for every possible status:**

| Status                | Set by   | When                                           | Condition                                                    |
| --------------------- | -------- | ---------------------------------------------- | ------------------------------------------------------------ |
| `scheudled`           | system   | New appointment is created                     | -                                                            |
| `finished`            | system   | Current date is after appointment end time     | current appointment status is `scheduled` and current date is after appointment end time |
| `confirmed`           | system   | Current date is 24h after appointment end time | current appointment status is `finished` and current date is more than 24h after appointment end time |
| `invoiced`            | system   | Invoice for appointment is created             | -                                                            |
| `canceled`            | customer | Customer clicks cancel button                  | current appointment status is `scheduled` and current date is not less than 24h before appointment start time and user total canceled appointments number for current month is not greater than 1 |
| `rejection requested` | customer | Customer clicks reject button                  | current appointment status is `finished` and current date is not more than 24h after appointment end time |
| `rejection accepted`  | provider | Provider clicks accept rejection button        | current appointment status is `rejection requested`          |

**2. Normal appointment lifecycle is:**

1. scheduled - after user creates new appointment
2. finished - after system time is after appointment end time
3. confirmed - after system time is more than 24h after appointment end time and user didn't request rejection
4. invoiced - after invoiced is issued automatically on the 1st day of next month

**3. Appointment rejection**

After appointment status is changed to finished system automatically sends email to customer with information that appointment is finished. In case that the appointment didn't take place there is also a link attached to that email that allows customer to reject that the appointment didn't take place. That link is valid for 24h after appointment finished time. If user will no click that link then appointment status will be automatically chaned to confirmed after 24h and invoiced at the 1st day of next month. If user will click that link an email is send to provider that his customer requested rejection. If provied will accept that rejection then appointment status will be changed to rejection accepted and appointment will be not invoiced.


**4. Apppointment cancellation**

Every appointment can be canceled by customer or provider. Customer is allowed to cancel 1 appointment in a month no less than 24h before appointment start date. Provider is allowed to cancel his appointments without any limit as long as the appointment status is `scheduled`. 

## Notifications

**1. An email notification is sent when:**

+ appointment is finished
+ appointment rejection is rquested
+ appointment rejection is accepted
+ new appointment is created
+ appointment is canceled
+ invoice is issued

Email templates can be found here: `src\main\resources\templates\email`


## Built With

* [Fullcalendar](https://fullcalendar.io/) - A JavaScript event calendar
* [FlyingSaucer](https://github.com/flyingsaucerproject/flyingsaucer) - Used to generate invoice PDF
* [jjwt](https://github.com/jwtk/jjwt) - Used to generate/validate JWT tokens

## Contribute

Let's together make AppointmentScheduler awesome!

If you have any suggestions/ideas please feel free to write about it. You are also welcome to fork this project and send pull request with your changes.


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
