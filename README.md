# ePJ2 - Electric Vehicle Rental Simulation System

## Project Description

ePJ2 is a Java-based simulation system for an electric vehicle (EV) rental company operating in Java City. The system simulates rental operations, tracks vehicle status, generates financial reports, and visualizes vehicle movements on a city map grid.

## Key Features

### Core Functionality
- **Multi-type EV management** (cars, bikes, scooters) with battery tracking
- **Rental simulation** with real-time movement visualization
- **Dynamic pricing calculation** based on multiple factors
- **Financial reporting** (summary and daily views)
- **Maintenance tracking** for vehicle issues
- **Thread-based simulation** of concurrent rentals

### Technical Highlights
- **Java 17** with multithreading for concurrent simulations
- **JavaFX/Swing** GUI interfaces
- **Properties file** configuration for business rules
- **Serialization** for top-performing vehicle data
- **Map visualization** (20x20 grid)
- **Invoice generation** in text format

## System Components

### 1. Vehicle Management
- **Electric Cars**:
  - Battery level tracking
  - Passenger capacity
  - Maintenance status
  
- **Electric Bikes**:
  - Battery level tracking
  - Range per charge
  - Maintenance status

- **Electric Scooters**:
  - Battery level tracking
  - Maximum speed
  - Maintenance status

### 2. Rental Simulation
- Time-synchronized thread execution
- Map movement visualization (straight-line paths)
- Automatic battery discharge during movement
- Rental duration tracking (in seconds)

### 3. Pricing System
| Factor | Calculation |
|--------|-------------|
| Base Price | Vehicle type × rental duration |
| Distance | Narrow/Wide city area multipliers |
| Discounts | Every 10th rental discount |
| Promotions | Special event discounts |
| Maintenance | Repair cost calculations |

### 4. Reporting
- **Summary Reports**:
  - Total revenue
  - Total discounts
  - Maintenance costs
  - Tax calculations
  - Operational expenses

- **Daily Reports**:
  - Date-grouped financial data
  - Table-formatted output
