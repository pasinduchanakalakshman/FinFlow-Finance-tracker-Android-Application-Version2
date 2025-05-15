# 💰 Finflow - Personal Finance Tracker

A modern, intuitive Android application built with Kotlin to help you manage your personal finances effectively. Track your income, expenses, and savings with ease while gaining valuable insights into your spending habits.

## 🌟 Features

- **📊 Interactive Dashboard**
  - Real-time overview of income, expenses, and balance
  - Visual budget progress tracking
  - Spending breakdown by categories
  - Recent transactions list

- **💳 Transaction Management**
  - Add, edit, and delete transactions
  - Categorize transactions (Food, Transport, Bills, etc.)
  - Date-based transaction filtering
  - Monthly transaction history

- **📈 Budget Tracking**
  - Set monthly budget limits
  - Visual budget progress indicators
  - Budget alerts and notifications
  - Category-wise spending analysis

- **⚙️ User Preferences**
  - Customizable currency settings
  - Dark/Light theme support
  - Notification preferences
  - Data backup functionality

## 🛠️ Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database
- **UI Components**: Material Design
- **Charts**: MPAndroidChart
- **Dependency Injection**: Manual DI
- **Coroutines**: For asynchronous operations
- **SharedPreferences**: For user preferences

## 📱 Screenshots

![Screen1](https://github.com/user-attachments/assets/a1350a87-7109-46a5-8465-3094a79677df)
![Screen2](https://github.com/user-attachments/assets/dbdee4b3-0d12-453e-911f-f3c3d0c7e5c5)
![Screen3](https://github.com/user-attachments/assets/a505ef80-0436-4c3b-a254-74ada82a094e)
![Screen4](https://github.com/user-attachments/assets/e4f83746-abb9-4620-91eb-5650b4220930)
![Screen5](https://github.com/user-attachments/assets/8cf5f6c4-83ff-4c03-96f4-ee463ba25542)
![Screen6](https://github.com/user-attachments/assets/446fe419-0939-4688-af17-9dead86a38b4)
![Screen7](https://github.com/user-attachments/assets/8fbfb043-3da9-4a50-bd0d-008d7b3202e9)
![Screen8](https://github.com/user-attachments/assets/eba94c9a-2aeb-4155-b7a0-d3c2ec754328)
![Screen9](https://github.com/user-attachments/assets/856b76a5-f36c-4569-bcce-8cc8653754cd)
![Screen10](https://github.com/user-attachments/assets/570ac2aa-1121-4fe8-a2bf-dfecc711686d)
![Screen11](https://github.com/user-attachments/assets/509f8f9c-bf1b-4c37-a439-c58263f99995)

## 📦 Project Structure
```
app/
├── data/           # Data layer (Repository, DAO, Database)
├── model/          # Data models
├── ui/             # UI components and activities
├── notification/   # Notification handling
└── utils/          # Utility classes
```

## 🔧 Configuration
The app uses the following key configurations:
- Room Database for local storage
- SharedPreferences for user settings
- MPAndroidChart for data visualization
- Material Design components for UI


## 🙏 Acknowledgments
Special thanks to the following tools and libraries:

- [Material Design](https://m3.material.io/) – UI components and guidelines  
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) – Charting library  
- [AndroidX](https://developer.android.com/jetpack/androidx) – Core Jetpack components
