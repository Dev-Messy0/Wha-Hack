# Wha Hack v2.0 - Hacker Desktop Preview Application

[![Android Build APK](https://github.com/devmessy/wha-hack/actions/workflows/build.yml/badge.svg)](https://github.com/devmessy/wha-hack/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)

**Wha Hack** est une application Android à interface style *Cyberpunk Hacker* permettant d'afficher et de synchroniser une session Web en mode Bureau (Desktop) avec un guide interactif trilingue/multilingue (5 langues).

> ⚠️ **AVERTISSEMENT LÉGAL ET ÉDUCATTIF**  
> Cette application a été conçue à des fins **strictement éducatives et d'apprentissage**. **Dev Messy** décline toute responsabilité quant à l'utilisation qui en est faite ou en cas de non-respect des conditions d'utilisation (CGU) des services tiers (WhatsApp).

---

## ☠️ Fonctionnalités Principales

- **Interface Hacker Cyberpunk** : Thème sombre haute contraste avec touches néon vert matrices.
- **Guide Multi-Langues (5 Langues)** : Instructions claires étape par étape en **Français**, **English**, **中文 (Chinois)**, **हिन्दी (Hindi)** et **اردو (Ourdou)**.
- **Rappel de Responsabilité** : Affichage explicite de l'avertissement éducatif et non-responsabilité du créateur **Dev Messy**.
- **Lanceur de Discussion Directe** : Envoyez un message ou démarrez une conversation sans ajouter le numéro à vos contacts téléphone.
- **Basculement de Mode** : Commutation instantanée entre le mode Bureau (Web QR Scan) et le mode Mobile.
- **Gestion des Autorisations** : Prise en charge fluide de la caméra pour le scan de QR Code.

---

## 🛠️ Instructions de Build (Génération d'APK)

### 1. Cloner le Dépôt
```bash
git clone https://github.com/devmessy/wha-hack.git
cd wha-hack
```

### 2. Compilation en Ligne de Commande avec Gradle
Sur Linux/macOS :
```bash
chmod +x gradlew
./gradlew assembleDebug
```

Sur Windows :
```cmd
gradlew.bat assembleDebug
```

L'APK généré se trouvera dans le dossier :
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🤖 Automations GitHub Actions (CI/CD)

Ce projet inclut un workflow GitHub Actions préconfiguré dans `.github/workflows/build.yml`.

Chaque fois que vous poussez du code (`git push`) ou créez une *Pull Request* sur la branche `main` ou `master`, GitHub Actions va automatiquement :
1. Configurer l'environnement Java JDK 17 et Android SDK.
2. Accorder les permissions d'exécution à Gradle.
3. Compiler le projet et générer l'APK Debug.
4. Héberger et rendre téléchargeable le fichier APK dans les **Artifacts** du workflow GitHub.

---

## 📜 Crédits & Auteur

- **Créateur** : Dev Messy
- **Licence** : Usage éducatif
