OUTPUT_FOLDER=files
echo "Downloading L Stops Data..."
wget -O "$OUTPUT_FOLDER/CTA_-_System_Information_-_List_of__L__Stops.csv" https://data.cityofchicago.org/api/views/8pix-ypme/rows.csv?accessType=DOWNLOAD
echo "Downloading Daily L Station Entry  Data..."
wget -O "$OUTPUT_FOLDER/CTA_-_Ridership_-__L__Station_Entries_-_Daily_Totals.csv" https://data.cityofchicago.org/api/views/5neh-572f/rows.csv?accessType=DOWNLOAD 
echo "Done"
