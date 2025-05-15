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
| Dashboard | Transactions | Budget |
|-----------|--------------|---------|
| <img src="screenshots/dashboard.png" width="200"> | <img src="screenshots/transactions.png" width="200"> | <img src="screenshots/budget.png" width="200"> |

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK 21 or higher
- Kotlin 1.6.0 or higher

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/finflow.git
   ```
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on your device or emulator

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

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors
- Your Name - Initial work

## 🙏 Acknowledgments
- Material Design for UI components
- MPAndroidChart for data visualization
- Android Jetpack for architecture components
