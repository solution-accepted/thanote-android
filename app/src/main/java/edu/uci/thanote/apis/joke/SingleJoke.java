package edu.uci.thanote.apis.joke;

public class SingleJoke extends Joke {
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
}
