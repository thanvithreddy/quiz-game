// Quiz Game JavaScript
let currentSession = null;
let currentQuestionNumber = 1;
let totalQuestions = 0;
let timeLeft = 30;
let timer = null;

// DOM Elements
const startScreen = document.getElementById('start-screen');
const quizScreen = document.getElementById('quiz-screen');
const resultScreen = document.getElementById('result-screen');
const leaderboardScreen = document.getElementById('leaderboard-screen');

const playerNameInput = document.getElementById('playerName');
const categorySelect = document.getElementById('category');
const difficultySelect = document.getElementById('difficulty');
const startBtn = document.getElementById('startBtn');

const questionElement = document.getElementById('question');
const optionsElement = document.getElementById('options');
const questionNumberElement = document.getElementById('question-number');
const timerElement = document.getElementById('timer');
const nextBtn = document.getElementById('nextBtn');

const scoreElement = document.getElementById('score');
const percentageElement = document.getElementById('percentage');
const timeElement = document.getElementById('time-taken');
const rankElement = document.getElementById('rank');
const playAgainBtn = document.getElementById('playAgainBtn');
const viewLeaderboardBtn = document.getElementById('viewLeaderboardBtn');

const leaderboardList = document.getElementById('leaderboard-list');
const backToMenuBtn = document.getElementById('backToMenuBtn');

// Event Listeners
startBtn.addEventListener('click', startQuiz);
nextBtn.addEventListener('click', nextQuestion);
playAgainBtn.addEventListener('click', () => showScreen('start'));
viewLeaderboardBtn.addEventListener('click', showLeaderboard);
backToMenuBtn.addEventListener('click', () => showScreen('start'));

// Screen Management
function showScreen(screenName) {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.remove('active');
    });

    switch(screenName) {
        case 'start':
            startScreen.classList.add('active');
            break;
        case 'quiz':
            quizScreen.classList.add('active');
            break;
        case 'result':
            resultScreen.classList.add('active');
            break;
        case 'leaderboard':
            leaderboardScreen.classList.add('active');
            break;
    }
}

// Start Quiz
async function startQuiz() {
    const playerName = playerNameInput.value.trim();
    const category = categorySelect.value;
    const difficulty = difficultySelect.value;

    if (!playerName) {
        alert('Please enter your name');
        return;
    }

    try {
        const response = await fetch('/api/start', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                playerName: playerName,
                category: category,
                difficulty: difficulty
            })
        });

        if (!response.ok) {
            throw new Error('Failed to start quiz');
        }

        const data = await response.json();
        currentSession = data.sessionId;
        totalQuestions = data.totalQuestions;
        currentQuestionNumber = 1;

        displayQuestion(data.currentQuestion);
        showScreen('quiz');
        startTimer();

    } catch (error) {
        console.error('Error starting quiz:', error);
        alert('Failed to start quiz. Please try again.');
    }
}

// Display Question
// Debug version of displayQuestion function
function displayQuestion(question) {
    console.log('Question object received:', question); // Debug line
    console.log('Question text:', question.question); // Debug line
    console.log('Question options:', question.options); // Debug line

    const questionElement = document.getElementById('question');
    console.log('Question element found:', questionElement); // Debug line

    if (question && question.question) {
        questionElement.textContent = question.question;
        console.log('Question text set to:', question.question); // Debug line
    } else {
        console.error('Question or question.question is undefined!');
        questionElement.textContent = 'Error: Question not found';
    }

    questionNumberElement.textContent = `Question ${currentQuestionNumber} of ${totalQuestions}`;

    optionsElement.innerHTML = '';
    if (question.options && Array.isArray(question.options)) {
        question.options.forEach((option, index) => {
            const button = document.createElement('button');
            button.className = 'option-btn';
            button.textContent = option;
            button.addEventListener('click', () => selectAnswer(index));
            optionsElement.appendChild(button);
        });
    } else {
        console.error('Options not found or not an array:', question.options);
    }

    nextBtn.style.display = 'none';
    resetTimer();
}

// Select Answer
async function selectAnswer(answerIndex) {
    // Disable all option buttons
    document.querySelectorAll('.option-btn').forEach(btn => {
        btn.disabled = true;
    });

    clearInterval(timer);

    try {
        const response = await fetch('/api/answer', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                sessionId: currentSession,
                answerIndex: answerIndex
            })
        });

        if (!response.ok) {
            throw new Error('Failed to submit answer');
        }

        const data = await response.json();

        // Show correct/incorrect styling
        const optionButtons = document.querySelectorAll('.option-btn');
        optionButtons[data.correctAnswerIndex].classList.add('correct');

        if (!data.correct) {
            optionButtons[answerIndex].classList.add('incorrect');
        }

        // Show explanation if available
        if (data.explanation) {
            const explanationDiv = document.createElement('div');
            explanationDiv.className = 'explanation';
            explanationDiv.textContent = data.explanation;
            optionsElement.appendChild(explanationDiv);
        }

        nextBtn.style.display = 'block';

    } catch (error) {
        console.error('Error submitting answer:', error);
        alert('Failed to submit answer. Please try again.');
    }
}

// Next Question
async function nextQuestion() {
    try {
        const response = await fetch('/api/next', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                sessionId: currentSession
            })
        });

        if (!response.ok) {
            throw new Error('Failed to get next question');
        }

        const data = await response.json();

        if (data.hasMore) {
            currentQuestionNumber = data.questionNumber;
            displayQuestion(data.currentQuestion);
            startTimer();
        } else {
            finishQuiz();
        }

    } catch (error) {
        console.error('Error getting next question:', error);
        alert('Failed to get next question. Please try again.');
    }
}

// Finish Quiz
async function finishQuiz() {
    try {
        const response = await fetch('/api/finish', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                sessionId: currentSession
            })
        });

        if (!response.ok) {
            throw new Error('Failed to finish quiz');
        }

        const data = await response.json();

        scoreElement.textContent = `${data.score}/${data.totalQuestions}`;
        percentageElement.textContent = `${Math.round(data.percentage)}%`;
        timeElement.textContent = formatTime(data.timeTaken);
        rankElement.textContent = data.rank;

        showScreen('result');

    } catch (error) {
        console.error('Error finishing quiz:', error);
        alert('Failed to finish quiz. Please try again.');
    }
}

// Show Leaderboard
async function showLeaderboard() {
    try {
        const response = await fetch('/api/leaderboard');

        if (!response.ok) {
            throw new Error('Failed to load leaderboard');
        }

        const data = await response.json();

        leaderboardList.innerHTML = '';
        data.forEach((entry, index) => {
            const listItem = document.createElement('li');
            listItem.innerHTML = `
                <span class="rank">#${index + 1}</span>
                <span class="name">${entry.name}</span>
                <span class="score">${entry.score}%</span>
                <span class="time">${entry.time}</span>
            `;
            leaderboardList.appendChild(listItem);
        });

        showScreen('leaderboard');

    } catch (error) {
        console.error('Error loading leaderboard:', error);
        alert('Failed to load leaderboard. Please try again.');
    }
}

// Timer Functions
function startTimer() {
    timeLeft = 30;
    updateTimerDisplay();

    timer = setInterval(() => {
        timeLeft--;
        updateTimerDisplay();

        if (timeLeft <= 0) {
            clearInterval(timer);
            // Auto-submit with no answer (-1 indicates timeout)
            selectAnswer(-1);
        }
    }, 1000);
}

function resetTimer() {
    if (timer) {
        clearInterval(timer);
    }
    timeLeft = 30;
    updateTimerDisplay();
}

function updateTimerDisplay() {
    timerElement.textContent = timeLeft;
    timerElement.className = timeLeft <= 10 ? 'timer warning' : 'timer';
}

// Utility Functions
function formatTime(milliseconds) {
    const seconds = Math.floor(milliseconds / 1000);
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    showScreen('start');
});