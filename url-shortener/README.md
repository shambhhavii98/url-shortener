# SnapLink | Full-Stack URL Shortener & Analytics Platform

A high-performance, full-stack application built using **Java, Spring Boot, and Vanilla JavaScript**. This system dynamically generates deterministic shortened URL aliases, handles request analytics tracking, enforces configurable click ceilings, and streams real-time runtime-generated QR codes.

## 🚀 Features
* **Full-Stack UI:** Single-page reactive application styled with Tailwind CSS.
* **In-Memory Database:** Powered by H2 and Spring Data JPA for instantaneous local data handling.
* **Click Limits & Expiry:** Automatically invalidates links once a user-defined click ceiling is reached.
* **Dynamic QR Codes:** Integrates the ZXing library to stream QR code graphics directly through the HTTP response stream.
* **Analytics Logging:** Captures tracking data including user timestamps, click events, and browser metadata.

## 🛠️ Tech Stack
* **Backend:** Java 21, Spring Boot 3.3.5, Spring Data JPA, Hibernate
* **Database:** H2 (In-Memory)
* **Frontend:** HTML5, JavaScript (Fetch API), Tailwind CSS
* **Libraries:** ZXing (Zebra Crossing) for QR generation

## ⚙️ How to Run Locally
1. Clone the repository to your local machine.
2. Open the project in VS Code.
3. Ensure your `application.properties` profile is configured to port `8081`.
4. Run the main class `UrlShortenerApplication.java`.
5. Access the user interface by navigating to: `http://localhost:8081/static/index.html`