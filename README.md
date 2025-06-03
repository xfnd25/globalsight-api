# GlobalSight API

The GlobalSight API provides endpoints for managing and simulating natural disaster events. It allows users to register, log in, and then create disaster simulations, get predictions for them, and view historical disaster data.

## Disaster Simulation Endpoint (`/api/simulations`)

This endpoint is used to create and manage new disaster simulations. Users can input various parameters of a potential disaster to get a simulated response and risk assessment.

### `POST /api/simulations` - Create a New Disaster Simulation

This endpoint allows users to submit the initial parameters of a disaster scenario. The system will then be able to process this information, potentially with an AI service, to predict outcomes like fatality categories and suggest appropriate responses, such as drone deployment.

#### Request Body Examples

Below are examples of JSON payloads for creating different disaster simulations.

##### Example 1: Flood in South America

This example represents a flood event occurring in South America.

```json
[
  {
    "inputYear": 2025,           // The year the disaster starts
    "inputStartMonth": 7,        // The month the disaster starts (1-12)
    "inputStartDay": 15,         // The day the disaster starts
    "inputEndYear": 2025,          // The year the disaster ends
    "inputEndMonth": 7,          // The month the disaster ends (1-12)
    "inputEndDay": 18,           // The day the disaster ends
    "inputDisasterGroup": "Natural", // Broad category of the disaster
    "inputDisasterSubgroup": "Hydrological", // More specific subgroup (e.g., Hydrological, Geophysical)
    "inputDisasterType": "Flood",    // Specific type of disaster
    "inputContinent": "South America", // Continent where the disaster occurs
    "inputRegion": "South America",  // Specific region within the continent
    "inputDisMagScale": "Km2",       // Scale used for magnitude (e.g., Richter, Km2, Severity)
    "inputDisMagValue": 500.0,     // Numeric value of the disaster's magnitude
    "inputLatitude": -23.5505,     // Latitude of the disaster's epicenter/main location
    "inputLongitude": -46.6333,    // Longitude of the disaster's epicenter/main location
    "inputCpi": 115.5              // Consumer Price Index at the time of the disaster, if available
  }
]
```

##### Example 2: Earthquake in Asia

This example represents an earthquake event in South-Eastern Asia.

```json
[
  {
    "inputYear": 2026,
    "inputStartMonth": 1,
    "inputStartDay": 10,
    "inputEndYear": 2026,
    "inputEndMonth": 1,
    "inputEndDay": 10,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Geophysical", // Different subgroup
    "inputDisasterType": "Earthquake",     // Different disaster type
    "inputContinent": "Asia",
    "inputRegion": "South-Eastern Asia", // More specific region
    "inputDisMagScale": "Richter",      // Magnitude scale specific to earthquakes
    "inputDisMagValue": 7.2,           // Magnitude value on the Richter scale
    "inputLatitude": 3.2028, 
    "inputLongitude": 101.6028,
    "inputCpi": 120.0 
  }
]
```

##### Example 3: Drought in Africa

This example represents a prolonged drought event in Eastern Africa. Note that the disaster spans multiple months.

```json
[
  {
    "inputYear": 2025,
    "inputStartMonth": 11,
    "inputStartDay": 1,
    "inputEndYear": 2026,            // Disaster extends into the next year
    "inputEndMonth": 2,
    "inputEndDay": 28,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Climatological", // Different subgroup
    "inputDisasterType": "Drought",        // Different disaster type
    "inputContinent": "Africa",
    "inputRegion": "Eastern Africa",
    "inputDisMagScale": "Severity",     // Magnitude scale for drought might be qualitative
    "inputDisMagValue": null,          // Magnitude value might not be applicable or available for drought
    "inputLatitude": -1.2921, 
    "inputLongitude": 36.8219,
    "inputCpi": 98.7
  }
]
```
