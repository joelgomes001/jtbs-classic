# JTBS Classic

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**JTBS Classic** is a lightweight, real-time live-streaming companion system. It consists of a real-time web portal (for viewing and admin controls) and a dedicated Android application optimized for instant playback.

---

## 📥 Direct APK Download

Get the official Android application directly onto your device:

### **[👉 Download JTBS Classic APK 👈](https://github.com/joelgomes001/jtbs-classic/raw/main/assets/jtbs-classic-debug.apk)**

*Note: After downloading, open the APK file and follow your device's prompts to install (you may need to enable "Install from Unknown Sources" for your browser).*

---

## ✨ Features

- ⚡ **Instant Autoplay with Sound**: The stream and audio start playing immediately as soon as the app is opened—no manual touch or interaction required.
- 📡 **Multi-Source Support**: Plays standard HLS (.m3u8) streams, YouTube Live streams, and Facebook Live streams.
- 🎛️ **Real-Time Admin Controls**: Toggle stream status, change video sources, update logos, and configure offline pages dynamically. Changes sync to viewers in less than 1 second.
- ☁️ **Serverless Architecture**: Utilizes Firebase (Authentication, Firestore, Cloud Functions, and Hosting) for high speed, zero-maintenance, and low/zero cost.

---

## 🛠️ System Architecture

### 1. Android Viewer App (`.apk`)
A lightweight, fast, and auto-focused wrapper optimized for fullscreen playback. Designed specifically for smart devices, TVs, and mobile screens to display the live broadcast with zero delay and instant-on audio capabilities.

### 2. Live Web Player (Firebase Hosted)
A public web interface that pulls live configuration and video states dynamically from Firestore.

### 3. Admin Dashboard
A secure dashboard for broadcasters to:
- Turn the live feed on and off.
- Instantly swap stream links (HLS, YouTube, or Facebook).
- Customize the offline brand presence, logos, and messages.

---

## ⚙️ Backend Tech Stack
- **Database**: Google Cloud Firestore (Real-time Config & Settings)
- **Functions**: Node.js Firebase Cloud Functions (Admin Role Management)
- **Hosting**: Firebase Hosting (Web & Static asset delivery)
- **Authentication**: Firebase Auth (Admin identity verification)
