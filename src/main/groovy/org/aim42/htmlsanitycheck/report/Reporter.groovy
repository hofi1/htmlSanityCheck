package org.aim42.htmlsanitycheck.report

import org.aim42.htmlsanitycheck.collect.PerRunResults
import org.aim42.htmlsanitycheck.collect.SingleCheckResults
import org.aim42.htmlsanitycheck.collect.SinglePageResults

/**
 *  superclass for reporting results.
 *  Subclasses will define the concrete output format
 */

abstract class Reporter {

    // nonsense - need that only once...
    // TODO: rewrite tests to work with PerRunResults
    protected ArrayList<SinglePageResults> pageResults

    // subclasses need to access runResults...
    protected PerRunResults runResults

    protected String createdOnDate

    /**
     * create the reporter
     */
    public Reporter() {
        this.createdOnDate = new Date().format('dd. MMMM YYYY, HH:mm')
    }

    /**
     * primarily for testing: create with just one instance of SingleCheckResults
     * @param pageResults
     */
    public Reporter( SinglePageResults singlePageResults ) {
        this.pageResults = new ArrayList<SinglePageResults>( )
        this.pageResults.add( singlePageResults )

    }

    /**
     * Usually a Reporter instance shall be constructed with its appropriate
     * @see PerRunResults, as the latter contains all findings.
     * @param runResults
     */
    public Reporter( PerRunResults runResults ) {
        this()
        this.runResults = runResults
        this.pageResults = runResults.getResultsForAllPages()

    }


    /**
     * add checking results for one page
     */
    public addCheckingResultsForOnePage( SinglePageResults singlePageResults) {
        pageResults.add( singlePageResults )
    }

    /**
     * main entry point for reporting - to be called when a report is requested
     *
     * Uses template-method to delegate most concrete implementations to subclasses
     */
    public void reportFindings() {

        initReport()

        reportOverallSummary()

        reportAllPages()

        closeReport()
    }

    private void reportAllPages() {
        pageResults.each { pageResult ->
            reportPageSummary( pageResult )
            reportPageDetails( pageResult )
            reportPageFooter()
        }
    }

    protected int totalNrOfPages() {
      return pageResults.size()
    }

    protected int totalNrOfChecks() {
        int totalNrOfChecks = 0
        pageResults.each { pageResult ->
            totalNrOfChecks += pageResult.nrOfItemsCheckedOnPage()
        }
        return totalNrOfChecks
    }

    protected int totalNrOfFindings() {
        int totalFindings = 0
        pageResults.each { pageResult ->
            totalFindings += pageResult.nrOfFindingsOnPage()
        }
        return totalFindings
    }

    // delegate *real* work to subclasses

    // needs to e.g. open files or streams
    protected void initReport() {
        // default: do nothing
    }

    abstract protected void reportOverallSummary()

    abstract protected void reportPageSummary( SinglePageResults pageResult )
    abstract protected void reportPageDetails( SinglePageResults pageResult )
    abstract protected void reportPageFooter( )

    abstract protected void reportSingleCheckSummary( SingleCheckResults singleCheckResults )
    abstract protected void reportSingleCheckDetails( SingleCheckResults singleCheckResults )

    protected void closeReport() {
        // default: do nothing
    }


}

/************************************************************************
 * This is free software - without ANY guarantee!
 *
 *
 * Copyright 2013, Dr. Gernot Starke, arc42.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *********************************************************************** */
