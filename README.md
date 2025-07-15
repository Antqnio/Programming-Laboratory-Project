# Programming Laboratory Project – Client

## Overview
This repository contains the client-side application for the Programming Laboratory Project. The client is built with:

- **Java 11+**
- **Spring Boot** (for application configuration and HTTP client support via `RestTemplate` or `WebClient`)
- **Maven** (for dependency management and build)
- **NetBeans IDE** (recommended for easy project setup)

It interacts with the server’s REST API (see [Programming Laboratory Project – Server](https://github.com/Antqnio/Programming-Laboratory-Project-Server)), performing operations on project resources.

## Features
- Connects to server endpoints to list, create, update, and delete projects
- Parses and displays JSON responses
- Simple CLI or GUI interface (depending on implementation)
- Error handling and logging

---

## Prerequisites
- **Java Development Kit (JDK) 11** or higher  
- **Maven** 3.6+  
- **NetBeans IDE 8.2+** (recommended)  
- **Server** running on `http://localhost:8080` (or your configured server URL)
- **Multilanguage support**: Italian and English – language can be changed at runtime (via XML files)

---
## Building and Running
#### Using NetBeans IDE
1. Open NetBeans and choose File > Open Project, then select this repository’s root folder.
2. Right-click the project in the Projects pane and select Clean and Build.
3. After build completion, right-click again and choose Run.

---

## 📄 License

This project is licensed under the MIT License.
