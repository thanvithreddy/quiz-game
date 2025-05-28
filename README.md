# 🎮 Quiz Game Application

A modern, interactive web-based quiz game built with Java backend and vanilla JavaScript frontend. Test your knowledge across multiple categories including Technology, Science, History, and Geography with different difficulty levels.

## ✨ Features

- **Multiple Categories**: Technology, Science, History, Geography, or Mixed (All)
- **Difficulty Levels**: Easy, Medium, Hard, or All levels
- **Real-time Timer**: 30-second countdown for each question
- **Interactive UI**: Smooth transitions and responsive design
- **Score Tracking**: Detailed results with percentage and time taken
- **Ranking System**: Performance-based ranking (Novice to Expert)
- **Leaderboard**: View top performers (mock data)
- **Session Management**: Secure session handling for each game

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven (for dependency management)
- Modern web browser

### Dependencies

Add these dependencies to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.9</version>
    </dependency>
</dependencies>
```

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/quiz-game.git
   cd quiz-game
   ```

2. **Compile the Java application**
   ```bash
   javac -cp ".:gson-2.8.9.jar" com/quizgame/*.java
   ```

3. **Create the resources directory structure**
   ```
   src/main/resources/
   ├── index.html
   ├── style.css
   └── script.js
   ```

4. **Run the application**
   ```bash
   java -cp ".:gson-2.8.9.jar" com.quizgame.QuizApplication
   ```

5. **Open your browser**
   Navigate to `http://localhost:8080`

## 🏗️ Project Structure

```
quiz-game/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── quizgame/
│       │           ├── QuizApplication.java    # Main server application
│       │           ├── Question.java          # Question model
│       │           ├── GameSession.java       # Game session management
│       │           └── GameResult.java        # Result calculation
│       └── resources/
│           ├── index.html                     # Main HTML page
│           ├── style.css                      # Styling
│           └── script.js                      # Frontend JavaScript
├── pom.xml                                    # Maven dependencies
└── README.md
```

## 🎯 How to Play

1. **Enter your name** in the player name field
2. **Select category** (Technology, Science, History, Geography, or All)
3. **Choose difficulty** (Easy, Medium, Hard, or All)
4. **Click "Start Quiz"** to begin
5. **Answer questions** within the 30-second time limit
6. **View your results** and ranking at the end
7. **Check the leaderboard** to see top performers

## 🔧 API Endpoints

The application provides several REST endpoints:

- `POST /api/start` - Start a new quiz session
- `POST /api/answer` - Submit an answer for the current question
- `POST /api/next` - Move to the next question
- `POST /api/finish` - Complete the quiz and get results
- `GET /api/leaderboard` - Retrieve leaderboard data

## 📊 Game Features

### Question Categories
- **Technology**: Programming, AI, Web development
- **Science**: Chemistry, Physics, Biology, Astronomy
- **History**: World events, Historical figures
- **Geography**: Countries, Capitals, Natural features

### Difficulty Levels
- **Easy**: Basic knowledge questions
- **Medium**: Intermediate level questions
- **Hard**: Advanced and challenging questions

### Scoring System
- Each correct answer adds 1 point to your score
- Final percentage is calculated based on correct answers
- Time tracking for performance analysis

### Ranking System
- **Expert**: 90-100%
- **Advanced**: 80-89%
- **Intermediate**: 70-79%
- **Beginner**: 60-69%
- **Novice**: Below 60%

## 🛠️ Technical Details

### Backend (Java)
- **HTTP Server**: Built-in Java HttpServer
- **JSON Processing**: Google Gson library
- **Session Management**: ConcurrentHashMap for thread safety
- **Static File Serving**: Custom file handler for web assets

### Frontend (JavaScript)
- **Vanilla JavaScript**: No frameworks, pure ES6+
- **Async/Await**: Modern asynchronous programming
- **Responsive Design**: Works on desktop and mobile
- **Timer Management**: Real-time countdown functionality

### Key Classes

#### `Question.java`
- Represents a quiz question with options and metadata
- Includes category, difficulty, and correct answer tracking

#### `GameSession.java`
- Manages individual player sessions
- Tracks progress, score, and timing

#### `GameResult.java`
- Calculates and stores final results
- Provides percentage and performance metrics

## 🔒 Security Features

- Session-based game management
- Input validation and sanitization
- CORS headers for web security
- Safe file serving with content type validation

## 🚀 Deployment

### Local Development
The application runs on `localhost:8080` by default.

### Production Deployment
1. Package the application with Maven
2. Deploy to your preferred Java hosting service
3. Configure environment variables for production settings

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Future Enhancements

- [ ] Database integration for persistent storage
- [ ] Real leaderboard with database backend
- [ ] User authentication and profiles
- [ ] Question difficulty balancing
- [ ] Admin panel for question management
- [ ] Multiplayer support
- [ ] Question explanations and learning resources
- [ ] Mobile app version

## 🐛 Known Issues

- Leaderboard currently uses mock data
- Timer may not sync perfectly across slow connections
- File serving requires proper resource directory setup



## 🙏 Acknowledgments

- Thanks to the open-source community
- Inspired by classic quiz games
- Built with passion for learning and knowledge sharing

---

⭐ **Star this repository** if you found it helpful!

🐛 **Report bugs** by creating an issue

💡 **Suggest features** through pull requests
