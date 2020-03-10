package edu.uci.thanote.apis.opentriviadb;

import com.google.gson.annotations.SerializedName;
import edu.uci.thanote.apis.BasicNote;

import java.util.List;

/**
 * Created by Joxon on 2020-03-09.
 */
public class Trivia implements BasicNote {
    private String category;
    private String type;
    private String difficulty;
    private String question;
    @SerializedName("correct_answer")
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    private List<String> incorrectAnswers;

    public String getTitle() {
        return "Trivia: " + question;
    }

    public String getDetail() {
        return correctAnswer;
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