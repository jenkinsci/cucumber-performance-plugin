cucumber-performance
====================

Version 1.2 released on 31st July 2013.

This plugin reports on the performance over time of testing jobs run by Jenkins containing tests using the Cucumber-JVM framework.

It works by parsing the relevant output files from Cucumber-JVM, aggregating the information and presenting it in a coherent and easy-to-understand format.

The main report is a graphical representation of the time taken by successful builds of that project, and from there it's possible to drill down into the features of that project, and onwards to scenarios.

Future plans include sortable tables, overall performance by steps, scenarios and features, and potentially an overall trending indicator in Jenkins itself. Any suggestions for future features will be most welcome - please use the issue reporting tool in GitHub to send me these.

The code is contained in this GitHub repo (https://github.com/TrueDub/cucumber-performance), with the latest released version on the master branch, and a named branch for the latest version under development (currently V1.3). 

This project owes a lot to the excellent Cucumber-JVM reporting plugins developed by Kingsley Hendrickse and Masterthought, and available at https://github.com/masterthought

Many thanks to them for their hard work!
