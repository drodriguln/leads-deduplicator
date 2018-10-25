# leads-deduplicator

## Problem Statement
Programming Challenge:

Take a variable number of identically structured json records and de-duplicate the set. An example file of records is given in the accompanying 'leads.json'. Output should be same format, with dups reconciled according to the following rules:

1. The data from the newest date should be preferred
2. duplicate IDs count as dups. Duplicate emails count as dups. Duplicate values elsewhere do not count as dups.
3. If the dates are identical the data from the record provided last in the list should be preferred

Simplifying assumption: the program can do everything in memory (don't worry about large files)

The application should also provide a log of changes including some representation of the source record, the output record and the individual field changes (value from and value to) for each field.

Please implement as a command-line java program.

## Run Instructions
This is a maven project. So make sure that is installed, and then in your cmd or terminal window, run:
1. "mvn package" to generate the fat jar.
2. "java -jar leads-parser-1.0-shaded.jar (path to leads.json)" (Note that leads.json is included in the root directory).

You will see the log statements print in the console window to indicate its transformation from the source to its deduplicated output.
