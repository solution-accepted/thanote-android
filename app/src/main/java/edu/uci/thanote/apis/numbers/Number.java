package edu.uci.thanote.apis.numbers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joxon on 2020-03-09.
 */
public class Number {

    private boolean found;

    @SerializedName("number")
    private String title;

    @SerializedName("text")
    private String detail;

    public boolean isFound() {
        return found;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}

//Include the query parameter json or set the HTTP header Content-Type to application/json to return the fact and associated meta-data as a JSON object, with the properties:
//
//    text: A string of the fact text itself.
//    found: Boolean of whether there was a fact for the requested number.
//    number: The floating-point number that the fact pertains to. This may be useful for, eg. a /random request or notfound=floor. For a date fact, this is the 1-indexed day of a leap year (eg. 61 would be March 1st).
//    type: String of the category of the returned fact.
//    date (sometimes): A day of year associated with some year facts, as a string.
//    year (sometimes): A year associated with some date facts, as a string.
//
//http://numbersapi.com/random/year?json
//⇒ {
//    "text": "2012 is the year that the century's second and last solar transit of Venus occurs on June 6.",
//    "found": true,
//    "number": 2012,
//    "type": "year",
//    "date": "June 6"
//}