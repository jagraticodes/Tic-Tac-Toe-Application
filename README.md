# Tic-Tac-Toe-Application

An advanced, high-fidelity desktop Tic-Tac-Toe application built using **Java Swing** and **Java 2D Vector Graphics (`Graphics2D`)**. This project transitions away from traditional console layouts or native operating system widgets to implement a fully custom-painted UI rendering pipeline from scratch.

---

## 🚀 Architectural & Technical Highlights

* **🎨 Advanced Graphics2D Pipeline:** Replaces generic UI buttons with an anti-aliased mathematical vector canvas (`VALUE_ANTIALIAS_ON`), producing incredibly fluid, resolution-independent geometry and smooth text tracking.
* **⚡ Multi-Pass Neon Glow Art:** The `X` (Neon Pink) and `O` (Neon Cyan) figures are custom-drawn using an alpha-blended rendering loop. It paints a thick, semi-transparent base layer to emulate a soft light glow, followed by a sharp, high-contrast foreground stroke.
* **🧠 Algorithmic AI Opponent Engine:** Features an asynchronous computer opponent programmed with a smart decision matrix capable of active win-path forecasting, reactive human-move blocking, and strategic grid control (such as center dominance).
* **⏱️ Asynchronous Multi-Threading UX:** Utilizes internal timing routines (`javax.swing.Timer`) to introduce an artificial "thinking latency" when playing against the AI. This ensures a polished, human-like interface flow instead of an instant machine calculation.
* **📊 Frosted Glassmorphism UI:** Built a custom-themed dark mode dashboard incorporating premium color gradients, active mouse-hover overlays, and a persistent match scoreboard tracking stats seamlessly across mode switches.

---

## 🛠️ Concepts & Frameworks Applied

* **Custom Component Painting:** Overriding the core standard `paintComponent` routine to gain absolute coordinate-level control over rendering surfaces.
* **Thread-Safe Event Dispatching:** Enforcing Event Dispatch Thread (`EDT`) rules via `SwingUtilities.invokeLater` to safely synchronize updates between visual states and logical arrays.
* **State Management Architecture:** Clear structural separation between coordinate token metrics (Logical Matrix) and the Canvas element (Visual Matrix).

---

## 💻 Setup & Execution (VS Code / CLI)

### Prerequisites
* Java Development Kit (JDK) 11 or higher installed on your system.

### Steps to Run
1. Clone this repository to your local directory:
   ```bash
   git clone [https://github.com/YOUR_USERNAME/elite-tictactoe-ai-engine.git](https://github.com/YOUR_USERNAME/elite-tictactoe-ai-engine.git)
