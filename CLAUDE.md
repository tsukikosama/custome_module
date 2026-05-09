# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Employee Internal Portal System (员工内部积分商城系统) built on the ContiNew Admin framework. The system allows employees to earn points through overtime and other activities, redeem products, and administrators to manage orders and procurement. It's a multi-module Maven project using Spring Boot 3, Java 17, and follows the ContiNew Admin conventions.

## Build and Development Commands

### Building the Project
```bash
# Clean and compile (includes automatic code formatting via Spotless plugin)
mvn clean compile

# Package the application
mvn clean package

# Skip tests during build (configured by default)
mvn clean package -DskipTests

# Run specific tests
mvn test -Dtest=TestClassName

# Run the application (from continew-server module)
cd continew-server
mvn spring-boot:run
```

### Database Management
```bash
# The project uses Liquibase for database version management
# SQL scripts are located in: continew-server/src/main/resources/db/changelog/
# MySQL scripts: continew-server/src/main/resources/db/changelog/mysql/
# PostgreSQL scripts: continew-server/src/main/resources/db/changelog/postgresql/
```

## Architecture and Module Structure

### Module Organization
- **continew-server**: Main deployment module with startup class `ContiNewAdminApplication`, contains common controllers and configuration
- **continew-system**: System management features (users, roles, departments, menus, etc.)
- **continew-customer**: Client API module for employee-facing features (orders, products, activities)
- **continew-hr-common**: HR common utilities and shared entities (Order entities, common mappers)
- **continew-common**: Shared utilities, base classes, common configuration
- **continew-plugin**: Extensible plugin modules (open API, tenant, schedule, code generator)
- **continew-extension**: Extension modules (schedule server)

### Key Architectural Patterns
- **CRUD Base Classes**: Controllers extend `BaseController<Service, Resp, DetailResp, Query, Req>` for automatic CRUD generation
- **Layer Separation**: Controller → Service → Mapper → Entity (DO) pattern
- **Naming Conventions**: 
  - DO suffix for database entities
  - REQ suffix for request parameters  
  - RESP suffix for response parameters
- **MyBatis Plus**: Uses MyBatis Plus with XML mappers for complex multi-table queries
- **Lombok**: Extensive use of Lombok with global configuration for `@EqualsAndHashCode(callSuper = true)` and `@ToString(callSuper = true)`

## Business Domain Structure

### Core Business Entities
- **Order Management** (`biz_order`): Employee product orders with status workflow (pending → processing → delivered → completed)
- **Product Management** (`biz_product`): Redeemable products with points and monthly limits
- **Points System** (`biz_points_log`): Points transaction history with types (overtime conversion, redemption, expiration, refund, etc.)
- **Overtime Tracking** (`biz_overtime`): Employee overtime records with audit workflow
- **Activity Management** (`biz_activity`, `biz_activity_member`): Company activities with member management
- **Wish Management** (`biz_wish`): Employee product wishes that can be converted to products
- **Suggestion Management** (`biz_suggestion`): Employee feedback system
- **Carousel Images** (`biz_carousel_image`): Homepage banner management

### Important Business Rules
- **Order Status Flow**: Pending (1) → Processing (2) → Delivered (5) → Completed (3), with Cancelled (4) as terminal state
- **Points Transaction**: Positive amounts increase points, negative amounts decrease points
- **Activity Approval**: Only approved activities (status=2) are visible to employees
- **Wish to Product**: Admin can convert employee wishes to actual products

## Configuration and Environment

### Configuration Files
- Main config: `continew-server/src/main/resources/config/application.yml`
- Environment-specific: `application-dev.yml`, `application-prod.yml`
- The system supports environment variables for database and Redis configuration

### Key Technologies
- **Authentication**: Sa-Token with JWT (jwt-simple mode)
- **Database**: MyBatis Plus with CosId for distributed ID generation
- **Cache**: JetCache with Redis support
- **API Documentation**: NextDoc4j (Swagger UI alternative) at `/swagger-ui`
- **File Storage**: X File Storage supporting multiple backends
- **Scheduling**: Snail Job for distributed task scheduling
- **Excel Processing**: Fast Excel for import/export functionality

### DingTalk Integration
The system integrates with DingTalk for messaging and user management. Configuration is in `application.yml` under `dingtalk` section with support for environment variables `DINGTALK_APP_ID` and `DINGTALK_APP_SECRET`.

## Code Quality and Standards

### Code Formatting
- **Automatic Formatting**: The Spotless Maven plugin automatically formats code during compilation
- **Style Guide**: Follows Alibaba Java Coding Guidelines (黄山版)
- **Lombok Configuration**: Global settings in `lombok.config` with certain annotations disabled for safety
- **Comment Coverage**: Project maintains >45% comment coverage

### Before Committing Code
```bash
# Close all code windows first to avoid IDE format differences
mvn compile
# Then commit without reopening files to preserve formatting
```

### Testing
- Unit tests are skipped by default in Maven configuration
- Test classes should be placed in corresponding `src/test/java` directories
- Main test class: `continew-server/src/test/java/top/continew/admin/ContiNewAdminApplicationTests.java`

## API Development Guidelines

### Controller Development
- Extend `BaseController` for automatic CRUD capabilities
- Use `@CrudRequestMapping` annotation for automatic CRUD endpoint generation
- Client APIs use `/api/*` prefix, admin APIs use `/biz/*` prefix
- Include comprehensive API documentation with parameter examples

### Service Layer
- Business logic should be in ServiceImpl classes, not Controllers
- Use `@Transactional` for database operations
- Follow the existing service interfaces and implementation patterns

### Database Operations
- Use MyBatis Plus for simple CRUD operations
- Create XML mappers in `src/main/resources/mapper/` for complex queries
- Multi-table queries should be implemented in XML mapper files
- Follow the entity naming: table names map to DO classes

### Request/Response Patterns
- REQ classes for request parameters with validation annotations
- RESP classes for response data (list responses, detail responses)
- Use proper validation annotations and document constraints

## Employee Portal Specific Considerations

### User Context
- The system manages employee data with points, orders, and activities
- Employee integration with DingTalk for messaging
- Department-based organization structure

### Points System Logic
- Points are earned through overtime (type=1), activities (type=7), or corrections (type=6)
- Points are spent on product redemption (type=2, negative amount)
- Points can be refunded when orders are cancelled (type=4, positive amount)
- All point transactions must create entries in `biz_points_log` table

### Order Processing Workflow
1. Employee places order → points deducted → status=PENDING
2. Admin processes procurement → status=PROCESSING  
3. Admin delivers product → status=DELIVERED
4. Employee confirms receipt → status=COMPLETED
5. System auto-completes after 7 days if no confirmation

### Code Generation
The system includes a code generator that can create 80-95% of CRUD code:
- Access at admin interface (Development Tools section)
- Generates Controller, Service, Mapper, Entity classes
- Includes API documentation and parameter validation
- Follows project conventions automatically

## Important Notes

- **Do not modify existing interface return structures** - maintain compatibility
- **Do not modify features unrelated to the current task** - minimize changes
- **Prioritize reusing existing Services** - check for similar functionality first
- **Multi-table SQL should be implemented in XML** - don't use MyBatis Plus annotations
- **Controllers should not contain business logic** - delegate to Service layer
- **Always use Lombok annotations** - follow project patterns
- **Test the golden path and edge cases** - verify functionality works end-to-end

## Project Documentation

Comprehensive Chinese documentation is available in `员工内部系统开发文档.md` covering:
- Detailed API specifications for all modules
- Complete database schema definitions
- Business workflow diagrams
- Request/response examples
- Error handling specifications

Refer to this document for specific business logic requirements when implementing new features.