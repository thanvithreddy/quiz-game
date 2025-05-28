package com.quizgame;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuizApplication {
    private static final int PORT = 8080;
    private static final Map<String, GameSession> sessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private static List<Question> questions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        initializeQuestions();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Static file handlers
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/start", new StartGameHandler());
        server.createContext("/api/answer", new AnswerHandler());
        server.createContext("/api/next", new NextQuestionHandler());
        server.createContext("/api/finish", new FinishGameHandler());
        server.createContext("/api/leaderboard", new LeaderboardHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("🎮 Quiz Game Server started!");
        System.out.println("🌐 Open your browser and go to: http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop the server");
    }

    private static void initializeQuestions() {
        questions.addAll(Arrays.asList(
                // Technology Questions

                new Question("What does 'AI' stand for?",
                        Arrays.asList("Artificial Intelligence", "Automated Intelligence", "Advanced Intelligence", "Applied Intelligence"),
                        0, "Technology", "medium"),
                new Question("Which programming language is known as 'Write Once, Run Anywhere'?",
                        Arrays.asList("Java", "Python", "C++", "JavaScript"),
                        0, "Technology", "medium"),
                new Question("What does 'HTTP' stand for?",
                        Arrays.asList("HyperText Transfer Protocol", "High Tech Transfer Protocol", "HyperText Transport Protocol", "High Transfer Text Protocol"),
                        0, "Technology", "easy"),
                new Question("Which company developed the React JavaScript library?",
                        Arrays.asList("Facebook", "Google", "Microsoft", "Apple"),
                        0, "Technology", "medium"),
                new Question("What is the time complexity of binary search?",
                        Arrays.asList("O(log n)", "O(n)", "O(n²)", "O(1)"),
                        0, "Technology", "hard"),

                // Science Questions
                new Question("What is the chemical symbol for gold?",
                        Arrays.asList("Au", "Ag", "Go", "Gd"),
                        0, "Science", "easy"),
                new Question("How many bones are in an adult human body?",
                        Arrays.asList("206", "205", "207", "208"),
                        0, "Science", "medium"),
                new Question("What is the speed of light in vacuum?",
                        Arrays.asList("299,792,458 m/s", "300,000,000 m/s", "299,000,000 m/s", "298,792,458 m/s"),
                        0, "Science", "hard"),
                new Question("Which planet is known as the Red Planet?",
                        Arrays.asList("Mars", "Venus", "Jupiter", "Saturn"),
                        0, "Science", "easy"),
                new Question("What is the most abundant gas in Earth's atmosphere?",
                        Arrays.asList("Nitrogen", "Oxygen", "Carbon Dioxide", "Argon"),
                        0, "Science", "medium"),

                // History Questions
                new Question("In which year did World War II end?",
                        Arrays.asList("1945", "1944", "1946", "1943"),
                        0, "History", "easy"),
                new Question("Who was the first person to walk on the moon?",
                        Arrays.asList("Neil Armstrong", "Buzz Aldrin", "John Glenn", "Alan Shepard"),
                        0, "History", "easy"),
                new Question("Which ancient wonder of the world was located in Alexandria?",
                        Arrays.asList("Lighthouse of Alexandria", "Hanging Gardens", "Colossus of Rhodes", "Statue of Zeus"),
                        0, "History", "medium"),
                new Question("The fall of Constantinople occurred in which year?",
                        Arrays.asList("1453", "1450", "1456", "1460"),
                        0, "History", "hard"),
                new Question("Who wrote 'The Art of War'?",
                        Arrays.asList("Sun Tzu", "Confucius", "Lao Tzu", "Mencius"),
                        0, "History", "medium"),

                // Geography Questions
                new Question("What is the capital of Australia?",
                        Arrays.asList("Canberra", "Sydney", "Melbourne", "Perth"),
                        0, "Geography", "medium"),
                new Question("Which is the longest river in the world?",
                        Arrays.asList("Nile", "Amazon", "Yangtze", "Mississippi"),
                        0, "Geography", "easy"),
                new Question("Mount Everest is located in which mountain range?",
                        Arrays.asList("Himalayas", "Alps", "Andes", "Rocky Mountains"),
                        0, "Geography", "easy"),
                new Question("Which country has the most time zones?",
                        Arrays.asList("France", "Russia", "United States", "China"),
                        0, "Geography", "hard"),
                new Question("What is the smallest country in the world?",
                        Arrays.asList("Vatican City", "Monaco", "Nauru", "San Marino"),
                        0, "Geography", "medium")
        ));

        Collections.shuffle(questions);
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            System.out.println("Requested path: " + path); // Debug line

            if (path.equals("/")) {
                path = "/index.html";
            }

            try {
                String content = getStaticContent(path);
                String contentType = getContentType(path);

                System.out.println("Serving file: " + path + " with content type: " + contentType); // Debug line
                System.out.println("Content length: " + content.length()); // Debug line

                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, content.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(content.getBytes());
                os.close();
            } catch (IOException e) {
                System.err.println("File not found: " + path); // Debug line
                e.printStackTrace(); // Debug line

                String notFound = "404 - File Not Found: " + path;
                exchange.sendResponseHeaders(404, notFound.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        }

        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            return "text/plain";
        }
    }
    static class StartGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> request = gson.fromJson(body, Map.class);

                String sessionId = UUID.randomUUID().toString();
                String playerName = request.get("playerName");
                String difficulty = request.get("difficulty");
                String category = request.get("category");

                List<Question> filteredQuestions = questions.stream()
                        .filter(q -> category.equals("all") || q.getCategory().equalsIgnoreCase(category))
                        .filter(q -> difficulty.equals("all") || q.getDifficulty().equalsIgnoreCase(difficulty))
                        .limit(10)
                        .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);

                Collections.shuffle(filteredQuestions);

                GameSession session = new GameSession(sessionId, playerName, filteredQuestions);
                sessions.put(sessionId, session);

                Map<String, Object> response = new HashMap<>();
                response.put("sessionId", sessionId);
                response.put("totalQuestions", filteredQuestions.size());
                response.put("currentQuestion", session.getCurrentQuestion());

                sendJsonResponse(exchange, response);
            }
        }
    }

    static class AnswerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, Object> request = gson.fromJson(body, Map.class);

                String sessionId = (String) request.get("sessionId");
                int answerIndex = ((Double) request.get("answerIndex")).intValue();

                GameSession session = sessions.get(sessionId);
                if (session != null) {
                    boolean correct = session.submitAnswer(answerIndex);

                    Map<String, Object> response = new HashMap<>();
                    response.put("correct", correct);
                    response.put("correctAnswerIndex", session.getCurrentQuestion().getCorrectAnswerIndex());
                    response.put("explanation", getExplanation(session.getCurrentQuestion()));

                    sendJsonResponse(exchange, response);
                }
            }
        }
    }

    static class NextQuestionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> request = gson.fromJson(body, Map.class);

                String sessionId = request.get("sessionId");
                GameSession session = sessions.get(sessionId);

                if (session != null) {
                    session.nextQuestion();

                    Map<String, Object> response = new HashMap<>();
                    if (session.hasMoreQuestions()) {
                        response.put("hasMore", true);
                        response.put("currentQuestion", session.getCurrentQuestion());
                        response.put("questionNumber", session.getCurrentQuestionIndex() + 1);
                    } else {
                        response.put("hasMore", false);
                    }

                    sendJsonResponse(exchange, response);
                }
            }
        }
    }

    static class FinishGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> request = gson.fromJson(body, Map.class);

                String sessionId = request.get("sessionId");
                GameSession session = sessions.get(sessionId);

                if (session != null) {
                    GameResult result = session.getResult();

                    Map<String, Object> response = new HashMap<>();
                    response.put("score", result.getScore());
                    response.put("totalQuestions", result.getTotalQuestions());
                    response.put("percentage", result.getPercentage());
                    response.put("timeTaken", result.getTimeTaken());
                    response.put("playerName", session.getPlayerName());
                    response.put("rank", getRank(result.getPercentage()));

                    sendJsonResponse(exchange, response);
                    sessions.remove(sessionId); // Clean up session
                }
            }
        }
    }

    static class LeaderboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Simple mock leaderboard - in a real app, you'd use a database
            List<Map<String, Object>> leaderboard = Arrays.asList(
                    createLeaderboardEntry("Alex", 95, "2:34"),
                    createLeaderboardEntry("Sarah", 90, "3:12"),
                    createLeaderboardEntry("Mike", 85, "2:58"),
                    createLeaderboardEntry("Emma", 80, "4:21"),
                    createLeaderboardEntry("John", 75, "3:45")
            );

            sendJsonResponse(exchange, leaderboard);
        }

        private Map<String, Object> createLeaderboardEntry(String name, int score, String time) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("name", name);
            entry.put("score", score);
            entry.put("time", time);
            return entry;
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }

    private static String getExplanation(Question question) {
        // Simple explanation logic - in a real app, you'd store explanations with questions
        return "The correct answer is: " + question.getOptions().get(question.getCorrectAnswerIndex());
    }

    private static String getRank(double percentage) {
        if (percentage >= 90) return "Expert";
        if (percentage >= 80) return "Advanced";
        if (percentage >= 70) return "Intermediate";
        if (percentage >= 60) return "Beginner";
        return "Novice";
    }

    // Fixed method to read actual file content
    private static String getStaticContent(String path) throws IOException {
        // Remove leading slash for resource path
        String resourcePath = path.startsWith("/") ? path.substring(1) : path;

        // Try to read from resources folder using ClassLoader
        InputStream inputStream = QuizApplication.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            }
        } else {
            throw new IOException("Resource not found: " + resourcePath);
        }
    }
    }



