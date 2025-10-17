# Cyber-Security-News-Mobile-App


## Overview

This mobile app provides users with the latest cybersecurity news using the [News API](https://newsapi.org/). Additionally, it includes a secure authentication system using Firebase for user registration and login.

## Features

- **Cybersecurity News:** Get real-time cybersecurity news from various sources through the News API.
  
- **User Authentication:** Securely register and log in using Firebase Authentication.


## Getting Started

### Prerequisites

-  [Android Studio](https://developer.android.com/studio)
- [Java](https://www.java.com/en/download/)
- Firebase project with Authentication enabled
- News API key from [https://newsapi.org/](https://newsapi.org/)

### Installation

1. Clone the repository:

   ```bash
[   git clone https://github.com/Wh02m1/cybersecurity-news-app.git
](https://github.com/Wh02m1/Cyber-Security-News-Mobile-App.git)
2. Link Firebase to the project
3. Get Api key from https://newsapi.org/ and add it to the this Line in the **main_activity.java**
     ```java
     NewsApiClient newsApiClient = new NewsApiClient("API_KEY_HERE");
 
