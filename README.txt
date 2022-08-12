Wesley Reynolds
------------------------------------------------------------------------------------------------
Report1:
Input - Apache HTTP Access Log AND csv file mapping ip addresses to countries.
Output - A file mapping the number of requests made by each ip address, with the ip address replaced with the name of the country associated with the ip address.

AggOnCountry:
Input - A file mapping the number of requests made by each ip address, with the ip address replaced with the name of the country associated with the ip address.
Output - A file mapping country names to the number of requests made by ip addresses in that country.

SortKeysByDSCValue:
Input - A file mapping country names to the number of requests made by ip addresses in that country.
Output - A file mapping country names to the number of requests made by ip addresses in that country, sorted by descending number of requests.

Report 1:
./gradlew run --args="Report1 input/access.log input/hostname_country.csv out_report1_temp1/"
./gradlew run --args="AggOnCountry out_report1_temp1/ out_report1_temp2/"
./gradlew run --args="SortKeysByDSCValue out_report1_temp2/ out_report1/"
Input - Apache HTTP Access Log AND csv file mapping ip addresses to countries.
Output - A file mapping country names to the number of requests made by ip addresses in that country, sorted by descending number of requests.
------------------------------------------------------------------------------------------------
CountryToURL:
Input - Apache HTTP Access Log AND csv file mapping ip addresses to countries.
Output - A file mapping country names to urls requested by ip addresses associated with the country name.

CountTuples:
Input - A file mapping country names to urls requested by ip addresses associated with the country name.
Output - A file that maps each url to a country and the number of requests that the country has made for the url.

SortTuples:
Input - A file that maps each url to a country and the number of requests that the country has made for the url.
Output - A file that maps each country to a each url requested AND the number of requests for that url by ip addresses in the country.

Report 2:
./gradlew run --args="CountryToURL input/access.log input/hostname_country.csv out_report2_temp1/
./gradlew run --args="CountTuples out_report2_temp1/ out_report2_temp2/"
./gradlew run --args="SortTuples out_report2_temp2/ out_report2/"
Input - Apache HTTP Access Log AND csv file mapping ip addresses to countries.
Output - A file that maps each country to a each url requested AND the number of requests for that url by ip addresses in the country.
------------------------------------------------------------------------------------------------
