# Kotlin Multiplatform SDK Demo

This repository contains a **Kotlin Multiplatform SDK** with sample Android and iOS applications.

The project demonstrates how to:

- Share business logic between Android and iOS.
- Expose a clear SDK API surface for host apps.
- Provide ready-made UI flows on Android (and SwiftUI integration on iOS).
- Use modern multiplatform tooling in a way that is suitable for real products.

---

## What the SDK Demonstrates

### Kotlin Multiplatform architecture

- Shared domain and data layer in `commonMain`.
- Platform-specific implementations (storage, logging, networking details) per target.
- Clear separation between:
    - **Public SDK API** (what host apps use).
    - **Internal implementation details**.

### Android & iOS integration

#### Android

- SDK consumed as a standard Gradle module.
- Jetpack Compose UI that connects to the shared logic.

#### iOS

- SDK exported as a Kotlin/Native framework.
- Swift-friendly wrappers and SwiftUI screens that use the shared code.

### SDK usage model

- Host app owns **authentication**, **navigation** and **theming**.
- SDK exposes:
    - Public functions and models.
    - State / flows that can be bound to the host app’s UI.
- Sample screens show how a host app can embed SDK-provided functionality.

---

## Project Structure

This is a Kotlin Multiplatform project targeting **Android** and **iOS**.

### `/composeApp`

`/composeApp` contains the **Android app** and any shared Compose Multiplatform UI.

**Key parts:**

- `composeApp/src/commonMain`  
  Shared UI elements and presentation logic written with Compose Multiplatform.

- Platform-specific source sets:
    - `androidMain` – Android-only integrations (Android SDK, permissions, etc.).
    - `iosMain` – iOS-specific hooks if Compose views are embedded on iOS.

This module shows how the SDK is **consumed** from an Android app and how shared UI is wired to the shared logic.

### `/iosApp`

`/iosApp` contains the **iOS sample application**.

It shows how to:

- Import the generated Kotlin framework into Xcode.
- Wrap shared Kotlin view models into Swift observable objects.
- Build SwiftUI screens that react to Kotlin Flows/StateFlows.

This is the reference for **iOS integration** of the SDK.

### `/shared`

`/shared` is the **core SDK module**, shared between all targets.

- `shared/src/commonMain`  
  Contains the platform-agnostic core:
    - Public SDK API (facades, use cases).
    - Domain models.
    - Business logic and state handling.
    - Interfaces for platform-specific implementations.

- Platform-specific folders (e.g. `androidMain`, `iosMain`)  
  Used when shared code needs to call into platform APIs (secure storage, logging, etc.).

In a production setup, this module would be published as:

- **Android:** a library artifact (AAR / Maven).
- **iOS:** an XCFramework / Swift Package.
