# DRK Web - API

## Run the App:

### Database:

docker-compose up -d db


### Backend:

dotnet tool install --global dotnet-ef --version 7.*
cd backend/
cd csharp/
dotnet run


## Swagger Interface:

{baseUrl}/swagger/index.html



# To-Dos

- Objekte
- Controller (Add. User, ...)
- Pagination
- Filter
- OAuth / JWT
- Unit Tests
- Service PasswordChangeRequired & LastPasswordChangeDate



Sucher: Helfer, Organisation, Zeitraum, Auftragsnummer (nicht in jedem Protokoll)