# Euro 2.0 AML
Repository for the AML controls and analysis for the Euro 2.0 system.

## Setup

Start the [Account Identity Service](https://github.com/cryptofiat/account-identity) in order to install the database.

Set up local postgres:
* `CREATE DATABASE euro2;`
* `CREATE USER euro2_aml WITH PASSWORD 'muhammed123';`
* connect to `euro2` database
* `CREATE SCHEMA aml;`
* `GRANT ALL PRIVILEGES ON SCHEMA aml TO GROUP euro2_aml;`
* `GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA aml TO euro2_aml;`

## TODO
- [ ] sanctions updates
- [ ] sanctions deletes
- [ ] DB of transactions
- [ ] DB transactions linked to IDs
- [ ] AML rules on DB

## Sanctions
Sources
* UN Sanctions
* * https://scsanctions.un.org/resources/xml/en/consolidated.xml
* EU Sanctions
* * File - http://ec.europa.eu/external_relations/cfsp/sanctions/list/version4/global/global.xml
* * Help - http://ec.europa.eu/external_relations/cfsp/sanctions/list/version4/global/help_online/help.html

## Misc
Count lines of code
`cloc . --exclude-ext=xml,sh,bat,md --exclude-list-file=.clocignore`
