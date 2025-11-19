# Train Inventory Management System

A Spring Boot application for managing a model train collection, including locomotives, rolling stock, and maintenance records.

## Overview

This application provides a RESTful API for tracking and managing model train inventory. It supports various scales, manufacturers, and types of railway equipment commonly found in model railroading collections.

## Features

- **Locomotive Management**: Track steam, diesel, and electric locomotives with details like manufacturer, model number, road name, and power type
- **Rolling Stock Management**: Manage freight cars, passenger cars, and other rolling stock with AAR classifications
- **Maintenance Tracking**: Log maintenance activities and track maintenance status for all inventory items
- **Reporting**: Generate reports on inventory and maintenance records
- **Inventory Classification**: Support for various scales (HO, N, O, etc.) and historical eras

## Technology Stack

- Java 17
- Spring Boot 3.5.7
- Spring Web (RESTful API)
- Jackson for JSON serialization
- JUnit 5 for testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle (or use the included Gradle wrapper)

### Building the Project

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew bootRun
```

The application will start on the default Spring Boot port (8080).

### Running Tests

```bash
./gradlew test
```

## Project Structure

- `model/` - Domain models for Locomotive, RollingStock, and MaintenanceLog
- `controller/` - REST API endpoints
- `service/` - Business logic layer
- `repository/` - Data access layer
- `enums/` - Enumerations for scales, types, and statuses

## API Endpoints

The application provides REST endpoints for:
- Locomotive operations
- Rolling stock operations
- Maintenance log operations
- Inventory reporting

## License

This is a practice project for software craftsmanship exercises.
