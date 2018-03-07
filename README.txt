TASK:

Sending mails with big csv file as input

Given is a big csv file (size: multiple gigabytes, encoding: UTF-8) with email, firstname and lastname separated with ; and " as quotes.

The application should read the csv file and send emails to all recipients in an effective way.

For testing please do not send any emails. Just implement a mock mail sending service. It should wait for half a second to mock the sending time needed and then log a success message mentioning the recipient.

Please also describe in few sentences your implementation idea!



Test Data:

./genTestInput.pl DUMMY.csv 20000000

( If you want to generate bigger files using ./genTestInput.pl, make sure you have enough RAM, 4G + swap space is enough for 20000000 entries or approximately 1G file size, if nothing else is running )



Build:

./gradlew --info build

Command line:

java -DinputFile=DUMMY.csv -jar ./build/libs/blackSilverBassetHound-0.0.1-SNAPSHOT.jar

Idea:

Due to the expected duration of sending out millions of emails, we need to have some persistent data, to allow for graceful restarts.

Therefore we call one logical run of sending out an email to all listed recipients a 'campaign' (class Campaign). The program allows it to specify a campaign name. Associated with each campaign are then peristent data, such as the progress within the list of recipients, the list of failed email deliveries, and the list of successful email deliveries. Persistent data would be kept in a suitable data base, and data for different campaigns shall be separated from each other. Class DataStore is a dummy front for this hypothetical database store.

The File IO needs the ability to handle large files, to decode UTF-8, and to get and set a particular file position. Ideally one would use RandomAccessFile, and a memory mapping for that, but for now we use BufferedReader, and instead of the file position we use a line number. This somewhat slows down repositioning after a program restart.

CSV parsing might ideally be performed by some existing library class, such as FlatFileItemReader, but for this mock-up we use some quick manual implementation in the class RecipientsParser.

