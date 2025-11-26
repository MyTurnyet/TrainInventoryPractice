# Train Inventory Refactoring Practice - Implementation Checklist

## Project Overview
This checklist covers the implementation of a Spring Boot application with **intentional anti-patterns** for teaching dependency inversion and ports & adapters architecture.

### Anti-Patterns to Include:
- ✓ Direct instantiation (new keyword in constructors)
- ✓ Static utility methods called directly from business logic
- ✓ Hard-coded file paths and configuration
- ✓ Concrete class dependencies (no interfaces)

---

## Phase 1: Domain Models & Enums

### Enums (6)
- [ ] Create `Scale` enum (HO, N, O, G, Z, S, OO)
- [ ] Create `MaintenanceStatus` enum (OPERATIONAL, NEEDS_MAINTENANCE, IN_MAINTENANCE, OUT_OF_SERVICE, RETIRED)
- [ ] Create `LocomotiveType` enum (STEAM, DIESEL, ELECTRIC, GAS_TURBINE)
- [ ] Create `PowerType` enum (DC, DCC, DCC_SOUND, BATTERY)
- [ ] Create `AARType` enum (XM, XMO, FC, TA, HM, RP, GN, XL, GB, FM, RB, SA, CA, PA, BA)
- [ ] Create `InventoryType` enum (LOCOMOTIVE, ROLLING_STOCK)

### Domain Models (4)
- [ ] Create `BaseInventoryItem` abstract class with all common fields
- [ ] Create `Locomotive` class extending BaseInventoryItem
- [ ] Create `RollingStock` class extending BaseInventoryItem
- [ ] Create `MaintenanceLog` class

---

## Phase 2: Data Transfer Objects (DTOs)

### Request DTOs (6)
- [ ] Create `CreateLocomotiveRequest` DTO
- [ ] Create `UpdateLocomotiveRequest` DTO
- [ ] Create `CreateRollingStockRequest` DTO
- [ ] Create `UpdateRollingStockRequest` DTO
- [ ] Create `CreateMaintenanceLogRequest` DTO
- [ ] Create `UpdateMaintenanceStatusRequest` DTO

### Response DTOs (5)
- [ ] Create `LocomotiveResponse` DTO
- [ ] Create `RollingStockResponse` DTO
- [ ] Create `MaintenanceLogResponse` DTO
- [ ] Create `InventorySummaryResponse` DTO
- [ ] Create `SearchResultsResponse<T>` generic DTO

---

## Phase 3: Static Utility Classes (Anti-Pattern)

### Utility Classes (3) - Implement as STATIC
- [ ] Create `JsonFileManager` with static methods (readFromFile, writeToFile, ensureFileExists)
  - Hard-code data directory path: "data/"
  - No abstraction, direct file I/O
- [ ] Create `IdGenerator` with static methods (generateId, getCurrentMaxId)
  - Use static HashMap to track IDs
  - No thread safety
- [ ] Create `ModelMapper` with static methods for DTO conversions
  - Direct field-by-field mapping
  - No configuration

---

## Phase 4: Exception Classes

### Custom Exceptions (2)
- [ ] Create `ResourceNotFoundException` extending RuntimeException
- [ ] Create `JsonFileException` extending RuntimeException

---

## Phase 5: Repository Layer (Concrete Classes)

### Repositories (3) - No Interfaces, Hard Dependencies
- [ ] Create `LocomotiveRepository`
  - Hard-code file path "data/locomotives.json"
  - Call static JsonFileManager methods directly
  - Call static IdGenerator methods directly
  - Implement: save, findById, findAll, update, deleteById, findByScale, findByManufacturer
- [ ] Create `RollingStockRepository`
  - Hard-code file path "data/rolling-stock.json"
  - Call static JsonFileManager methods directly
  - Call static IdGenerator methods directly
  - Implement: save, findById, findAll, update, deleteById, findByScale, findByAarType
- [ ] Create `MaintenanceLogRepository`
  - Hard-code file path "data/maintenance-logs.json"
  - Call static JsonFileManager methods directly
  - Call static IdGenerator methods directly
  - Implement: save, findById, findAll, findByInventoryItemId, deleteById

---

## Phase 6: Service Layer (Concrete Classes with Direct Instantiation)

### Services (3) - No Interfaces, Create Dependencies with 'new'
- [ ] Create `LocomotiveService`
  - Instantiate LocomotiveRepository with `new` in constructor
  - Call static ModelMapper methods directly
  - Implement: createLocomotive, getLocomotiveById, getAllLocomotives, updateLocomotive, deleteLocomotive
- [ ] Create `RollingStockService`
  - Instantiate RollingStockRepository with `new` in constructor
  - Call static ModelMapper methods directly
  - Implement: createRollingStock, getRollingStockById, getAllRollingStock, updateRollingStock, deleteRollingStock
- [ ] Create `MaintenanceService`
  - Instantiate MaintenanceLogRepository with `new` in field declaration
  - Instantiate LocomotiveRepository with `new` in field declaration
  - Instantiate RollingStockRepository with `new` in field declaration
  - Call static ModelMapper methods directly
  - Implement: createMaintenanceLog, getMaintenanceLogById, getMaintenanceLogsByItemId, updateMaintenanceStatus, deleteMaintenanceLog

---

## Phase 7: Controller Layer (Direct Instantiation)

### Controllers (3) - Use @RestController but create Services with 'new'
- [ ] Create `LocomotiveController`
  - Add @RestController annotation
  - Instantiate LocomotiveService with `new` in constructor (NOT @Autowired)
  - Implement REST endpoints: POST /, GET /{id}, GET /, PUT /{id}, DELETE /{id}
- [ ] Create `RollingStockController`
  - Add @RestController annotation
  - Instantiate RollingStockService with `new` in constructor
  - Implement REST endpoints: POST /, GET /{id}, GET /, PUT /{id}, DELETE /{id}
- [ ] Create `MaintenanceController`
  - Add @RestController annotation
  - Instantiate MaintenanceService with `new` in constructor
  - Implement REST endpoints: POST /, GET /{id}, GET /item/{itemId}, PUT /status/{itemId}, DELETE /{id}

---

## Phase 8: Application Configuration

### Spring Boot Main Class (1)
- [ ] Create `TrainInventoryApplication` with @SpringBootApplication
  - Simple main method
  - No component scanning configuration needed

---

## Phase 9: Test Implementation (Comprehensive Coverage)

### Repository Tests (3) - Test with Real File I/O
- [ ] Create `LocomotiveRepositoryTest`
  - Test all CRUD operations
  - Use real JSON files in test directory
  - Clean up files in @AfterEach
  - Tests will be slow and fragile (intentional)
- [ ] Create `RollingStockRepositoryTest`
  - Test all CRUD operations
  - Use real JSON files in test directory
  - Clean up files in @AfterEach
- [ ] Create `MaintenanceLogRepositoryTest`
  - Test all CRUD operations
  - Use real JSON files in test directory
  - Clean up files in @AfterEach

### Service Tests (3) - Integration-style Tests
- [ ] Create `LocomotiveServiceTest`
  - Test business logic
  - Uses real repository (with real file I/O)
  - Hard to isolate failures
  - Slow execution (intentional anti-pattern)
  - Use Mokito to mock some values
- [ ] Create `RollingStockServiceTest`
  - Test business logic
  - Uses real repository
  - Hard to isolate failures
  - Use Mokito to mock some values
- [ ] Create `MaintenanceServiceTest`
  - Test business logic
  - Uses real repositories (all three)
  - Very slow and coupled (intentional)
  - Use Mokito to mock some values

### Controller Tests (3) - Spring MVC Tests
- [ ] Create `LocomotiveControllerTest`
  - Use @WebMvcTest or full integration tests
  - Test REST endpoints
  - Hard-coded service instantiation makes mocking difficult
- [ ] Create `RollingStockControllerTest`
  - Use @WebMvcTest or full integration tests
  - Test REST endpoints
- [ ] Create `MaintenanceControllerTest`
  - Use @WebMvcTest or full integration tests
  - Test REST endpoints

---

## Phase 10: Data Directory Setup

### File System Structure
- [ ] Create `data/` directory in project root
- [ ] Create empty `data/locomotives.json` with empty array `[]`
- [ ] Create empty `data/rolling-stock.json` with empty array `[]`
- [ ] Create empty `data/maintenance-logs.json` with empty array `[]`
- [ ] Add `data/*.json` to .gitignore (keep directory, ignore contents)

---

## Phase 11: Documentation

### Teaching Materials
- [ ] Create README section explaining the anti-patterns present
- [ ] Document which classes demonstrate which anti-patterns
- [ ] List suggested refactoring exercises in order of complexity
- [ ] Create "refactoring-goals.md" showing target architecture (ports & adapters)

---

## Phase 12: Verification

### Final Checks
- [ ] Run `./gradlew build` - ensure all tests pass
- [ ] Manually test API endpoints with curl or Postman
- [ ] Verify JSON files are created and populated correctly
- [ ] Document the "smell test" - list all the problems a student should identify
- [ ] Create example refactoring for one class to show the pattern

---

## Summary Statistics

**Production Code:**
- 6 Enums
- 4 Domain Models
- 11 DTOs
- 3 Static Utility Classes
- 2 Exception Classes
- 3 Repository Classes (concrete, no interfaces)
- 3 Service Classes (concrete, no interfaces)
- 3 Controller Classes
- 1 Application Main Class

**Total Production Classes: 36**

**Test Code:**
- 3 Repository Tests
- 3 Service Tests
- 3 Controller Tests

**Total Test Classes: 9**

**Total Files to Create: 45**

---

## Anti-Patterns Summary (For Teaching Reference)

### Dependency Inversion Violations:
1. **Controllers create Services with `new`** - Should use dependency injection
2. **Services create Repositories with `new`** - Should use dependency injection
3. **MaintenanceService creates 3 repositories** - Multiple hard dependencies
4. **No interfaces** - Everything depends on concrete implementations
5. **Static utility methods** - Cannot be mocked or substituted
6. **Hard-coded file paths** - Configuration is not externalized
7. **No abstraction boundaries** - Direct coupling between all layers

### Testing Pain Points (Intentional):
1. **Repository tests use real file I/O** - Slow, brittle, order-dependent
2. **Service tests cannot mock repositories** - Because they're created with `new`
3. **Controller tests cannot mock services** - Same reason
4. **Static utilities cannot be mocked** - Mockito struggles with statics
5. **Tests must clean up file system** - Side effects between tests
6. **No test isolation** - Everything touches real resources

### Refactoring Opportunities:
1. Extract interfaces for all repositories
2. Extract interfaces for all services
3. Introduce dependency injection (@Autowired)
4. Replace static utilities with injectable services
5. Externalize configuration (application.properties)
6. Introduce repository interfaces (ports)
7. Implement adapters for file storage
8. Add proper exception handling
9. Introduce domain services
10. Apply ports & adapters (hexagonal architecture)
