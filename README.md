[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/F0ieClPf)
# Team Project

## Overview
This repository contains the codebase for our team software project.  
It is built with Java and managed using Maven.

## Team Members
Benjamin Grassie
Connor McGough
Olena Shevchenko
Rhys Womack
Cheng An Wong
Jiaying Ye
Wanlin Zhong

## Tech Stack
- Java 17+
- Maven

## Project Structure
- `src/main/java` — main source code
- `src/test/java` — unit tests
- `src/main/resources` — configuration files

## How to Run
To run backend:
- Navigate to directory 'server'
- On Linux/Mac run: ./mvnw spring-boot:run in CLI
- On Windows run: .\mvnw spring-boot:run in CLI

To run frontend:
- Navigate to directory 'client'
- Run `npm install` to install dependencies (if first time running)
- Run 'npm run dev' in CLI (dev command currently)

## Generating RSA key pair 
- Navigate to /src/main/resources
- Execute the following :
```bash
mkdir certs
cd certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
```
<img width="444" height="186" alt="image" src="https://github.com/user-attachments/assets/440c7f8b-1074-46b5-b082-8a4d40995350" />


## Login credentials 
- Testing different roles : could be adjusted in CommandLineRunner main class
- Accessing pages for users in database is done through same email and password input (check /h2-console)
- Default user credentials : 
```bash
email : test@sheffield.ac.uk
password: test
role : ADMIN
```
<img width="887" height="228" alt="image" src="https://github.com/user-attachments/assets/1a040784-0ed3-4471-8ed7-8f1c2b0b2d8a" />

### Workflow
- User home page is a list of their assigned modules. The teaching support team (admins) will see all active (non-deleted) modules
- Clicking <strong>See assignments</strong> under the module card dropdown will redirect to the list of assessments under that module
- Clicking assessment details leads to the progression and stage tracking/advance of that assessment. Admins can progress and advance stages at any time, while regular users can only advance stages they have the right context/role for.

