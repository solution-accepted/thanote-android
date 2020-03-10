package edu.uci.thanote.apis.opentriviadb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Joxon on 2020-03-09.
 */
public class TriviaResponse {
    @SerializedName("response_code")
    private int code;

    @SerializedName("results")
    private List<Trivia> triviaList;

    public int getCode() {
        return code;
    }

    public List<Trivia> getTriviaList() {
        return triviaList;
    }

    public Trivia getSingleTrivia() {
        return triviaList.get(0);
    }
}

//{
//  "response_code": 0,
//  "results": [
//    {
//      "category": "Science: Computers",
//      "type": "multiple",
//      "difficulty": "medium",
//      "question": "How many bytes are in a single Kibibyte?",
//      "correct_answer": "1024",
//      "incorrect_answers": [
//        "2400",
//        "1000",
//        "1240"
//      ]
//    }
//  ]
//}
