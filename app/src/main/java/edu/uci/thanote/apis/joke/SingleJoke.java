package edu.uci.thanote.apis.joke;


import edu.uci.thanote.apis.BasicNote;

public class SingleJoke extends Joke implements BasicNote {
    private String joke;

    public String getJoke() {
        return joke;
    }

    @Override
    public String toString() {
        return super.toString() + "SingleJoke{" +
                "joke='" + joke + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return "Joke: " + getCategory();
    }

    @Override
    public String getDetail() {
        return joke;
    }
}
