# Refactoring Exercise Catalog

This document outlines potential refactoring exercises that can be practiced with this codebase. Each exercise focuses on a specific code smell or design principle.

---

## Encapsulation Exercises

### 1. Encapsulate Fields
**Current Issue:** Model classes (Locomotive, RollingStock, MaintenanceLog) have public fields
**Files:** `model/Locomotive.java`, `model/RollingStock.java`, `model/MaintenanceLog.java`
**Concepts:** Encapsulation, Information Hiding
**Refactoring:** Convert public fields to private with getters and setters

### 2. Introduce Immutability for Request Objects
**Suggested Area:** Create request DTOs with immutable fields
**Concepts:** Immutability, Value Objects
**Refactoring:** Use final fields with constructor initialization

---

## Dependency Management Exercises

### 3. Replace Manual Instantiation with Dependency Injection
**Current Issue:** Services instantiate repositories using `new` keyword
**Files:** `service/LocomotiveService.java`, `service/RollingStockService.java`, `service/MaintenanceService.java`
**Example:** `private LocomotiveRepository repo = new LocomotiveRepository();`
**Concepts:** Dependency Injection, Inversion of Control
**Refactoring:** Use constructor injection with Spring's `@Autowired`

---

## Code Duplication Exercises

### 4. Extract ObjectMapper Configuration
**Current Issue:** Every repository method creates and configures ObjectMapper identically
**Files:** All repository classes
**Concepts:** DRY Principle, Extract Method
**Refactoring:** Create a single ObjectMapper bean or utility method

### 5. Create Base Repository Class
**Current Issue:** Repository classes duplicate CRUD operations
**Files:** `repository/LocomotiveRepository.java`, `repository/RollingStockRepository.java`
**Concepts:** Inheritance, Template Method Pattern
**Refactoring:** Extract common operations into `AbstractJsonRepository<T>`

### 6. Extract Filtering Logic
**Current Issue:** Similar filtering patterns repeated across repository methods
**Files:** Repository classes (findByScale, findByStatus methods)
**Concepts:** Strategy Pattern, Predicate Pattern
**Refactoring:** Create reusable filter/predicate methods

### 7. Consolidate Search Logic
**Current Issue:** Service search methods have nested conditionals and manual filtering
**Files:** `service/LocomotiveService.java:42-74`, similar in other services
**Concepts:** Extract Method, Guard Clauses
**Refactoring:** Use Java Streams with filter predicates

---

## Primitive Obsession Exercises

### 8. Replace Map with Response DTOs
**Current Issue:** Using `Map<String, Object>` for API responses
**Files:** `service/LocomotiveService.java:76-106`, service search methods
**Concepts:** Type Safety, First-Class Collections
**Refactoring:** Create dedicated response classes (LocomotiveResponse, SearchResultsResponse)

### 9. Create Request DTOs
**Suggested Area:** Controllers likely accept model objects directly
**Concepts:** Data Transfer Objects, Separation of Concerns
**Refactoring:** Create CreateLocomotiveRequest, UpdateLocomotiveRequest classes

---

## Error Handling Exercises

### 10. Replace Null Returns with Optional
**Current Issue:** Methods return null when items not found
**Files:** Repository findById methods, service methods
**Concepts:** Optional Pattern, Null Object Pattern
**Refactoring:** Use `Optional<T>` return types

### 11. Replace printStackTrace with Proper Logging
**Current Issue:** Exceptions caught and printed with printStackTrace()
**Files:** All repository classes
**Concepts:** Logging, Error Handling
**Refactoring:** Use SLF4J Logger for proper logging

### 12. Introduce Custom Exceptions
**Suggested Area:** Service layer should throw domain-specific exceptions
**Concepts:** Exception Handling, Domain-Driven Design
**Refactoring:** Create ResourceNotFoundException, InvalidRequestException

### 13. Add Global Exception Handler
**Suggested Area:** Create centralized exception handling for REST API
**Concepts:** Aspect-Oriented Programming, Cross-Cutting Concerns
**Refactoring:** Implement `@RestControllerAdvice` handler

---

## Method Length Exercises

### 14. Decompose Long Methods
**Current Issue:** Repository save/update methods are long with multiple responsibilities
**Files:** `repository/LocomotiveRepository.java:64-98`, similar methods
**Concepts:** Single Responsibility Principle, Extract Method
**Refactoring:** Break into smaller methods (readFile, generateId, writeFile)

### 15. Simplify Manual Mapping
**Current Issue:** getWithLogs method manually maps 13+ fields
**Files:** `service/LocomotiveService.java:76-106`
**Concepts:** Object Mapping, Reduce Complexity
**Refactoring:** Create a ModelMapper utility or use MapStruct

---

## Loop Refactoring Exercises

### 16. Replace Indexed Loops with Enhanced For-Each
**Current Issue:** Using `for(int i = 0; i < size; i++)` pattern
**Files:** Throughout service and repository classes
**Concepts:** Modern Java, Readability
**Refactoring:** Use enhanced for loops or streams

### 17. Convert Imperative Loops to Streams
**Current Issue:** Manual iteration with filtering logic
**Files:** Search methods, filter methods in repositories
**Concepts:** Functional Programming, Declarative Code
**Refactoring:** Use Java Stream API (filter, map, collect)

---

## Magic Values Exercises

### 18. Extract File Path Constants
**Current Issue:** Hardcoded file paths like "data/locomotives.json"
**Files:** Repository classes
**Concepts:** Constants, Configuration Management
**Refactoring:** Create configuration class with @Value annotations

### 19. Extract Search Defaults
**Current Issue:** Hardcoded pagination values (page: 0, pageSize: 20)
**Files:** `service/LocomotiveService.java:70-72`
**Concepts:** Named Constants, Configuration
**Refactoring:** Create constants or configuration properties

---

## Inheritance and Abstraction Exercises

### 20. Extract Common Base Class
**Suggested Area:** Locomotive and RollingStock share many fields
**Concepts:** Inheritance, DRY Principle
**Refactoring:** Create BaseInventoryItem abstract class per design document

### 21. Introduce Interface for Services
**Suggested Area:** Service classes could implement common interface
**Concepts:** Interface Segregation, Polymorphism
**Refactoring:** Create InventoryService<T> interface

---

## Validation Exercises

### 22. Add Input Validation
**Suggested Area:** No validation in service or controller layers
**Concepts:** Defensive Programming, Bean Validation
**Refactoring:** Use JSR-303 annotations (@NotNull, @Valid, etc.)

### 23. Introduce Validation Utility
**Suggested Area:** Business rule validation (price > 0, valid dates)
**Concepts:** Validation, Single Responsibility
**Refactoring:** Create ValidationUtil class per design document

---

## Testing and Testability Exercises

### 24. Improve Testability with Dependency Injection
**Related to:** Exercise #3
**Concepts:** Test Doubles, Interface Segregation
**Benefit:** Easier to inject hand-rolled fakes in tests

### 25. Extract File I/O for Better Testing
**Current Issue:** Repository classes directly handle file operations
**Concepts:** Separation of Concerns, Single Responsibility
**Refactoring:** Create JsonFileManager utility per design document

---

## Design Pattern Exercises

### 26. Apply Builder Pattern
**Suggested Area:** Model classes with many fields
**Concepts:** Creational Patterns, Fluent API
**Refactoring:** Add Builder inner classes to model objects

### 27. Apply Strategy Pattern for Filtering
**Current Issue:** Conditional logic for different filter types
**Concepts:** Behavioral Patterns, Open/Closed Principle
**Refactoring:** Create filter strategy implementations

### 28. Apply Factory Pattern for ObjectMapper
**Related to:** Exercise #4
**Concepts:** Creational Patterns
**Refactoring:** Create ObjectMapperFactory

---

## Modern Java Exercises

### 29. Use Records for Immutable DTOs
**Suggested Area:** Request/Response classes
**Concepts:** Java 14+ Features, Immutability
**Refactoring:** Convert DTOs to Java Records

---

## Recommended Exercise Paths

### Beginner Path
1. Encapsulate Fields (#1)
2. Replace Indexed Loops (#16)
3. Extract Constants (#18)
4. Add Logging (#11)

### Intermediate Path
1. Dependency Injection (#3)
2. Extract ObjectMapper (#4)
3. Replace Map with DTOs (#8)
4. Replace Null with Optional (#10)
5. Convert to Streams (#17)

### Advanced Path
1. Create Base Repository (#5)
2. Extract Common Base Class (#20)
3. Apply Template Method Pattern (#5)
4. Add Custom Exceptions (#12, #13)
5. Create JsonFileManager (#25)

### Comprehensive Refactoring Path
Work through exercises in numerical order for a complete transformation from the current implementation to the full design outlined in `class-design.md`.

---

## Notes

- Each exercise can be practiced independently
- Some exercises are dependent on others (noted in descriptions)
- The design document (`class-design.md`) shows the target architecture
- Current code intentionally contains code smells for practice purposes
- Tests exist to ensure refactoring doesn't break functionality
