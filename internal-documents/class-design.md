# Model Railroad Inventory API - Class Design

## Overview
This document outlines the class structure for a robust API application that tracks model railroad inventory, including locomotives and rolling stock with maintenance tracking capabilities.

---

## Domain Models

### 1. BaseInventoryItem (Abstract)
**Purpose:** Base class for all inventory items
**Attributes:**
- id: Long (Primary Key)
- manufacturer: String
- modelNumber: String
- scale: Scale (Enum)
- roadName: String
- color: String (from enum)
- description: String
- purchasePrice: BigDecimal
- purchaseDate: LocalDate
- currentValue: BigDecimal
- notes: String
- createdDate: LocalDateTime
- lastModifiedDate: LocalDateTime
- maintenanceStatus: MaintenanceStatus (Enum)

### 2. Locomotive (extends BaseInventoryItem)
**Purpose:** Represents a locomotive
**Additional Attributes:**
- locomotiveType: LocomotiveType (Enum: STEAM, DIESEL, ELECTRIC)
- powerType: PowerType (Enum: DC, DCC, DCC_SOUND)
- roadNumber: String

### 3. RollingStock (extends BaseInventoryItem)
**Purpose:** Represents rolling stock (freight/passenger cars)
**Additional Attributes:**
- aarType: AARType (Enum: XM, FC, TA, HM, etc.)
- carType: String (descriptive name: "Box Car", "Tank Car", etc.)
- roadNumber: String
- capacity: String (e.g., "50 ton", "60 foot")

### 4. MaintenanceLog
**Purpose:** Tracks maintenance history for inventory items
**Attributes:**
- id: Long (Primary Key)
- inventoryItemId: Long (Foreign Key)
- maintenanceDate: LocalDate
- description: String
- workPerformed: String
- performedBy: String
- notes: String
- createdDate: LocalDateTime

---

## Enumerations

### 5. Scale
- HO
- N
- O
- G
- Z
- S
- OO

### 6. MaintenanceStatus
- OPERATIONAL
- NEEDS_MAINTENANCE
- IN_MAINTENANCE
- OUT_OF_SERVICE
- RETIRED

### 7. LocomotiveType
- STEAM
- DIESEL
- ELECTRIC
- GAS_TURBINE

### 8. PowerType
- DC
- DCC
- DCC_SOUND
- BATTERY

### 9. AARType
**Purpose:** Standard Association of American Railroads car type codes
- XM (Box Car)
- XMO (Box Car, Overheight)
- FC (Flat Car)
- TA (Tank Car)
- HM (Hopper Car)
- RP (Refrigerator Car - Passenger Service)
- GN (Gondola)
- XL (Box Car - Loader)
- GB (Gondola - Ballast)
- FM (Flat Car - Military)
- RB (Refrigerator Car - Bunkerless)
- SA (Stock Car - Animal)
- CA (Caboose)
- PA (Passenger Car)
- BA (Baggage Car)

### 10. InventoryType
- LOCOMOTIVE
- ROLLING_STOCK

---

## Data Transfer Objects (DTOs)

### Request DTOs

### 11. CreateLocomotiveRequest
**Attributes:** All fields from Locomotive (except id, dates)

### 12. UpdateLocomotiveRequest
**Attributes:** All fields from Locomotive (except id, dates)

### 13. CreateRollingStockRequest
**Attributes:** All fields from RollingStock (except id, dates)

### 14. UpdateRollingStockRequest
**Attributes:** All fields from RollingStock (except id, dates)

### 15. CreateMaintenanceLogRequest
**Attributes:**
- inventoryItemId: Long
- maintenanceDate: LocalDate
- description: String
- workPerformed: String
- performedBy: String
- notes: String

### 16. UpdateMaintenanceStatusRequest
**Attributes:**
- maintenanceStatus: MaintenanceStatus

### Response DTOs

### 17. LocomotiveResponse
**Attributes:** All fields from Locomotive + maintenanceLogs collection

### 18. RollingStockResponse
**Attributes:** All fields from RollingStock + maintenanceLogs collection

### 19. MaintenanceLogResponse
**Attributes:** All fields from MaintenanceLog

### 20. InventorySummaryResponse
**Attributes:**
- totalItems: Integer
- totalLocomotives: Integer
- totalRollingStock: Integer
- itemsByScale: Map<Scale, Integer>
- itemsByStatus: Map<MaintenanceStatus, Integer>
- totalInventoryValue: BigDecimal

### 21. SearchResultsResponse<T>
**Generic wrapper for search results**
**Attributes:**
- results: List<T>
- totalResults: Long
- page: Integer
- pageSize: Integer

---

## Repository Layer (JSON File-Based)

### 22. LocomotiveRepository
**Purpose:** Manages JSON file operations for locomotives
**Data File:** data/locomotives.json
**Methods:**
- save(Locomotive locomotive): Locomotive
- findById(Long id): Optional<Locomotive>
- findAll(): List<Locomotive>
- update(Long id, Locomotive locomotive): Locomotive
- deleteById(Long id): void
- findByManufacturer(String manufacturer): List<Locomotive>
- findByScale(Scale scale): List<Locomotive>
- findByMaintenanceStatus(MaintenanceStatus status): List<Locomotive>
- findByRoadName(String roadName): List<Locomotive>
- findByLocomotiveType(LocomotiveType type): List<Locomotive>
- search(SearchCriteria criteria): List<Locomotive>
- getNextId(): Long

### 23. RollingStockRepository
**Purpose:** Manages JSON file operations for rolling stock
**Data File:** data/rolling-stock.json
**Methods:**
- save(RollingStock rollingStock): RollingStock
- findById(Long id): Optional<RollingStock>
- findAll(): List<RollingStock>
- update(Long id, RollingStock rollingStock): RollingStock
- deleteById(Long id): void
- findByManufacturer(String manufacturer): List<RollingStock>
- findByScale(Scale scale): List<RollingStock>
- findByMaintenanceStatus(MaintenanceStatus status): List<RollingStock>
- findByRoadName(String roadName): List<RollingStock>
- findByAarType(AARType aarType): List<RollingStock>
- search(SearchCriteria criteria): List<RollingStock>
- getNextId(): Long

### 24. MaintenanceLogRepository
**Purpose:** Manages JSON file operations for maintenance logs
**Data File:** data/maintenance-logs.json
**Methods:**
- save(MaintenanceLog log): MaintenanceLog
- findById(Long id): Optional<MaintenanceLog>
- findAll(): List<MaintenanceLog>
- update(Long id, MaintenanceLog log): MaintenanceLog
- deleteById(Long id): void
- findByInventoryItemId(Long itemId): List<MaintenanceLog>
- findByInventoryItemIdOrderByMaintenanceDateDesc(Long itemId): List<MaintenanceLog>
- findByMaintenanceDateBetween(LocalDate start, LocalDate end): List<MaintenanceLog>
- getNextId(): Long

---

## Service Layer

### 25. LocomotiveService
**Purpose:** Business logic for locomotive operations
**Methods:**
- createLocomotive(CreateLocomotiveRequest request): LocomotiveResponse
- getLocomotiveById(Long id): LocomotiveResponse
- getAllLocomotives(): List<LocomotiveResponse>
- updateLocomotive(Long id, UpdateLocomotiveRequest request): LocomotiveResponse
- deleteLocomotive(Long id): void
- searchLocomotives(SearchCriteria criteria): SearchResultsResponse<LocomotiveResponse>
- getLocomotivesByScale(Scale scale): List<LocomotiveResponse>
- getLocomotivesByStatus(MaintenanceStatus status): List<LocomotiveResponse>

### 26. RollingStockService
**Purpose:** Business logic for rolling stock operations
**Methods:**
- createRollingStock(CreateRollingStockRequest request): RollingStockResponse
- getRollingStockById(Long id): RollingStockResponse
- getAllRollingStock(): List<RollingStockResponse>
- updateRollingStock(Long id, UpdateRollingStockRequest request): RollingStockResponse
- deleteRollingStock(Long id): void
- searchRollingStock(SearchCriteria criteria): SearchResultsResponse<RollingStockResponse>
- getRollingStockByAarType(AARType aarType): List<RollingStockResponse>
- getRollingStockByStatus(MaintenanceStatus status): List<RollingStockResponse>

### 27. MaintenanceService
**Purpose:** Business logic for maintenance tracking
**Methods:**
- createMaintenanceLog(CreateMaintenanceLogRequest request): MaintenanceLogResponse
- getMaintenanceLogById(Long id): MaintenanceLogResponse
- getMaintenanceLogsByItemId(Long itemId): List<MaintenanceLogResponse>
- updateMaintenanceStatus(Long itemId, MaintenanceStatus status): void
- deleteMaintenanceLog(Long id): void
- getRecentMaintenance(int days): List<MaintenanceLogResponse>
- getItemsNeedingMaintenance(): List<BaseInventoryItem>

### 28. InventoryReportService
**Purpose:** Generate inventory reports and statistics
**Methods:**
- getInventorySummary(): InventorySummaryResponse
- getInventoryByScale(): Map<Scale, List<InventoryItemSummary>>
- getInventoryByStatus(): Map<MaintenanceStatus, List<InventoryItemSummary>>
- getInventoryValueReport(): InventoryValueReport
- exportInventoryToCsv(): byte[]
- exportInventoryToPdf(): byte[]

---

## Controller Layer (REST API)

### 29. LocomotiveController
**Base Path:** /api/locomotives
**Endpoints:**
- POST / - Create locomotive
- GET /{id} - Get locomotive by ID
- GET / - Get all locomotives
- PUT /{id} - Update locomotive
- DELETE /{id} - Delete locomotive
- GET /search - Search locomotives
- GET /scale/{scale} - Get by scale
- GET /status/{status} - Get by status

### 30. RollingStockController
**Base Path:** /api/rolling-stock
**Endpoints:**
- POST / - Create rolling stock
- GET /{id} - Get rolling stock by ID
- GET / - Get all rolling stock
- PUT /{id} - Update rolling stock
- DELETE /{id} - Delete rolling stock
- GET /search - Search rolling stock
- GET /aar-type/{aarType} - Get by AAR type
- GET /status/{status} - Get by status

### 31. MaintenanceController
**Base Path:** /api/maintenance
**Endpoints:**
- POST / - Create maintenance log
- GET /{id} - Get maintenance log by ID
- GET /item/{itemId} - Get all logs for an item
- PUT /status/{itemId} - Update maintenance status
- DELETE /{id} - Delete maintenance log
- GET /needs-maintenance - Get items needing maintenance
- GET /recent - Get recent maintenance activity

### 32. ReportController
**Base Path:** /api/reports
**Endpoints:**
- GET /summary - Get inventory summary
- GET /by-scale - Get inventory grouped by scale
- GET /by-status - Get inventory grouped by status
- GET /value - Get inventory value report
- GET /export/csv - Export to CSV
- GET /export/pdf - Export to PDF

---

## Utility Classes

### 33. SearchCriteria
**Purpose:** Encapsulate search parameters
**Attributes:**
- manufacturer: String
- scale: Scale
- roadName: String
- maintenanceStatus: MaintenanceStatus
- minValue: BigDecimal
- maxValue: BigDecimal
- era: String
- page: Integer
- pageSize: Integer
- sortBy: String
- sortDirection: SortDirection

### 34. ModelMapper
**Purpose:** Map between entities and DTOs
**Methods:**
- toLocomotiveResponse(Locomotive entity): LocomotiveResponse
- toRollingStockResponse(RollingStock entity): RollingStockResponse
- toMaintenanceLogResponse(MaintenanceLog entity): MaintenanceLogResponse
- toLocomotive(CreateLocomotiveRequest request): Locomotive
- toRollingStock(CreateRollingStockRequest request): RollingStock

### 35. ValidationUtil
**Purpose:** Custom validation logic
**Methods:**
- validateScale(String scale): boolean
- validateAARType(String aarType): boolean
- validatePrice(BigDecimal price): boolean
- validateDate(LocalDate date): boolean

### 36. JsonFileManager
**Purpose:** Handle JSON file I/O operations
**Methods:**
- readFromFile(String filePath, Class<T> type): List<T>
- writeToFile(String filePath, List<T> data): void
- ensureFileExists(String filePath): void
- createBackup(String filePath): void
- lockFile(String filePath): FileLock
- unlockFile(FileLock lock): void

### 37. IdGenerator
**Purpose:** Generate unique IDs for entities stored in JSON
**Methods:**
- generateId(String entityType): Long
- getCurrentMaxId(List<?> entities): Long
- resetIdCounter(String entityType): void

---

## Exception Handling

### 38. ResourceNotFoundException
**Extends:** RuntimeException
**Purpose:** Thrown when requested resource not found

### 39. InvalidRequestException
**Extends:** RuntimeException
**Purpose:** Thrown when request validation fails

### 40. DuplicateResourceException
**Extends:** RuntimeException
**Purpose:** Thrown when attempting to create duplicate resource

### 41. JsonFileException
**Extends:** RuntimeException
**Purpose:** Thrown when JSON file operations fail (read/write errors)

### 42. GlobalExceptionHandler
**Purpose:** Centralized exception handling for REST API
**Annotations:** @RestControllerAdvice
**Methods:**
- handleResourceNotFound(ResourceNotFoundException): ResponseEntity
- handleInvalidRequest(InvalidRequestException): ResponseEntity
- handleDuplicateResource(DuplicateResourceException): ResponseEntity
- handleJsonFileException(JsonFileException): ResponseEntity
- handleGenericException(Exception): ResponseEntity

---

## Configuration Classes

### 43. JsonStorageConfig
**Purpose:** JSON file storage configuration
**Attributes:**
- dataDirectory: String (e.g., "data/")
- locomotivesFile: String
- rollingStockFile: String
- maintenanceLogsFile: String
- backupEnabled: boolean
- backupDirectory: String
**Methods:**
- initializeStorage(): void
- validateDataDirectory(): boolean
- createDataDirectoryIfNotExists(): void

### 44. SecurityConfig
**Purpose:** API security configuration (if needed)

### 45. SwaggerConfig
**Purpose:** API documentation configuration (OpenAPI/Swagger)

### 46. CorsConfig
**Purpose:** CORS configuration for frontend integration

---

## Data File Structure

### JSON File Locations
- **Locomotives:** `data/locomotives.json`
- **Rolling Stock:** `data/rolling-stock.json`
- **Maintenance Logs:** `data/maintenance-logs.json`
- **Backups:** `data/backups/` (timestamped copies)

### Sample JSON Structure

**locomotives.json:**
```json
[
  {
    "id": 1,
    "manufacturer": "Athearn",
    "modelNumber": "RTR-87901",
    "scale": "HO",
    "roadName": "Union Pacific",
    "color": "Yellow/Gray",
    "era": "Modern",
    "description": "SD70M Locomotive",
    "purchasePrice": 189.99,
    "purchaseDate": "2024-01-15",
    "currentValue": 189.99,
    "notes": "DCC Ready",
    "maintenanceStatus": "OPERATIONAL",
    "locomotiveType": "DIESEL",
    "powerType": "DCC_SOUND",
    "roadNumber": "4141"
  }
]
```

**rolling-stock.json:**
```json
[
  {
    "id": 1,
    "manufacturer": "Walthers",
    "modelNumber": "910-2961",
    "scale": "HO",
    "roadName": "Santa Fe",
    "color": "Silver/Red",
    "era": "1950s",
    "description": "50' Reefer",
    "purchasePrice": 34.99,
    "purchaseDate": "2024-02-10",
    "currentValue": 34.99,
    "notes": "Metal wheels",
    "maintenanceStatus": "OPERATIONAL",
    "aarType": "RB",
    "carType": "Refrigerator Car",
    "roadNumber": "23456",
    "capacity": "50 foot"
  }
]
```

**maintenance-logs.json:**
```json
[
  {
    "id": 1,
    "inventoryItemId": 1,
    "maintenanceDate": "2024-03-20",
    "description": "Routine cleaning",
    "workPerformed": "Cleaned wheels and motor, lubricated gears",
    "performedBy": "Owner",
    "notes": "Running smoothly"
  }
]
```

---

## Total Classes: 46

**Breakdown:**
- Domain Models: 4
- Enumerations: 6
- DTOs: 15
- Repositories: 3
- Services: 4
- Controllers: 4
- Utility Classes: 5
- Exception Classes: 5
- Configuration Classes: 4
